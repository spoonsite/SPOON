/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.web.rest.resource.component;

import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentComment;
import edu.usu.sdl.openstorefront.core.entity.ComponentCommentType;
import edu.usu.sdl.openstorefront.core.entity.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.core.entity.ComponentEvaluationSectionPk;
import edu.usu.sdl.openstorefront.core.entity.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.core.entity.ComponentMetadata;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.view.ComponentEvaluationSectionView;
import edu.usu.sdl.openstorefront.core.view.ComponentExternalDependencyView;
import edu.usu.sdl.openstorefront.core.view.ComponentMetadataView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.WorkPlanLinkView;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.mail.Message;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codemonkey.simplejavamail.email.Email;

/**
 *
 * @author dshurtleff
 */
public abstract class ComponentExtendedSubResourceExt
		extends ComponentCommonSubResourceExt
{

	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource DEPENDENCY section">
	@GET
	@APIDescription("Get the dependencies for the component")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentExternalDependency.class)
	@Path("/{id}/dependencies")
	public List<ComponentExternalDependency> getComponentDependencies(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentExternalDependency.class, componentId);
	}

	@GET
	@APIDescription("Gets a dependency for a component")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentExternalDependency.class)
	@Path("/{id}/dependencies/{dependencyId}")
	public Response getComponentDependency(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("dependencyId")
			@RequiredParam String dependencyId)
	{
		ComponentExternalDependency dependencyExample = new ComponentExternalDependency();
		dependencyExample.setDependencyId(dependencyId);
		dependencyExample.setComponentId(componentId);
		ComponentExternalDependency componentExternalDependency = service.getPersistenceService().queryOneByExample(dependencyExample);
		return sendSingleEntityResponse(componentExternalDependency);
	}

	@GET
	@APIDescription("Get the dependencies from the entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentExternalDependencyView.class)
	@Path("/{id}/dependencies/view")
	public Response getComponentDependencies(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentExternalDependency dependencyExample = new ComponentExternalDependency();
		dependencyExample.setActiveStatus(filterQueryParams.getStatus());
		dependencyExample.setComponentId(componentId);

		List<ComponentExternalDependency> componentExternalDependencies = service.getPersistenceService().queryByExample(dependencyExample);
		componentExternalDependencies = filterQueryParams.filter(componentExternalDependencies);
		List<ComponentExternalDependencyView> views = ComponentExternalDependencyView.toViewList(componentExternalDependencies);

		GenericEntity<List<ComponentExternalDependencyView>> entity = new GenericEntity<List<ComponentExternalDependencyView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@DELETE
	@APIDescription("Inactivates a dependency from the component")
	@Path("/{id}/dependencies/{dependencyId}")
	public Response deleteComponentDependency(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("dependencyId")
			@RequiredParam String dependencyId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_DEPENDENCY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentExternalDependency componentExternalDependency = service.getPersistenceService().findById(ComponentExternalDependency.class, dependencyId);
		if (componentExternalDependency != null) {
			checkBaseComponentBelongsToComponent(componentExternalDependency, componentId);
			service.getComponentService().deactivateBaseComponent(ComponentExternalDependency.class, dependencyId);
		}
		return Response.ok().build();
	}

	@PUT
	@APIDescription("Activates a dependency from the component")
	@Path("/{id}/dependencies/{dependencyId}/activate")
	public Response activatieComponentDependency(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("dependencyId")
			@RequiredParam String dependencyId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_DEPENDENCY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentExternalDependency dependencyExample = new ComponentExternalDependency();
		dependencyExample.setDependencyId(dependencyId);
		dependencyExample.setComponentId(componentId);
		ComponentExternalDependency componentExternalDependency = service.getPersistenceService().queryOneByExample(dependencyExample);
		if (componentExternalDependency != null) {
			service.getComponentService().activateBaseComponent(ComponentExternalDependency.class, dependencyId);
		}
		return sendSingleEntityResponse(componentExternalDependency);
	}

	@POST
	@APIDescription("Add a dependency to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentExternalDependency.class)
	@Path("/{id}/dependencies")
	public Response addComponentDependency(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentExternalDependency dependency)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_DEPENDENCY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		dependency.setComponentId(componentId);
		return saveDependency(dependency, true);
	}

	@PUT
	@APIDescription("Update a dependency associated to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentExternalDependency.class)
	@Path("/{id}/dependencies/{dependencyId}")
	public Response updateComponentDependency(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("dependencyId")
			@RequiredParam String dependencyId,
			ComponentExternalDependency dependency)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_DEPENDENCY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentExternalDependency componentExternalDependency = service.getPersistenceService().findById(ComponentExternalDependency.class, dependencyId);
		if (componentExternalDependency != null) {
			checkBaseComponentBelongsToComponent(componentExternalDependency, componentId);
			dependency.setComponentId(componentId);
			dependency.setDependencyId(dependencyId);
			response = saveDependency(dependency, false);
		}
		return response;
	}

	private Response saveDependency(ComponentExternalDependency dependency, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(dependency);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			dependency.setActiveStatus(ComponentExternalDependency.ACTIVE_STATUS);
			dependency.setCreateUser(SecurityUtil.getCurrentUserName());
			dependency.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentDependency(dependency);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create(BASE_RESOURCE_PATH + dependency.getComponentId() + "/dependency/" + dependency.getDependencyId())).entity(dependency).build();
		} else {
			return Response.ok(dependency).build();
		}
	}
	//</editor-fold>

	// <editor-fold defaultstate="collapsed"  desc="ComponentRESTResource Evaluation Section section">
	@GET
	@APIDescription("Gets evaluation sections associated to the component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentEvaluationSection.class)
	@Path("/{id}/sections")
	public List<ComponentEvaluationSection> getComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentEvaluationSection.class, componentId);
	}

	@GET
	@APIDescription("Gets  evaluation sections associated to the component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentEvaluationSectionView.class)
	@Path("/{id}/sections/view")
	public List<ComponentEvaluationSectionView> getComponentEvaluationSectionView(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		List<ComponentEvaluationSection> sections = service.getComponentService().getBaseComponent(ComponentEvaluationSection.class, componentId);
		List<ComponentEvaluationSectionView> views = ComponentEvaluationSectionView.toViewList(sections);
		views.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_DESCENDING, ComponentEvaluationSectionView.NAME_FIELD));
		return views;
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_EVALSECTION_MANAGEMENT)
	@APIDescription("Deletes an evaluation section from the component")
	@Path("/{id}/sections/{evalSection}")
	public void deleteComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("evalSection")
			@RequiredParam String evalSection)
	{
		ComponentEvaluationSectionPk pk = new ComponentEvaluationSectionPk();
		pk.setComponentId(componentId);
		pk.setEvaluationSection(evalSection);
		service.getComponentService().deleteBaseComponent(ComponentEvaluationSection.class, pk);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_EVALSECTION_MANAGEMENT)
	@APIDescription("Deletes all evaluation section from the component")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/sections")
	public void deleteAllComponentEvaluationSections(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		service.getComponentService().deleteAllBaseComponent(ComponentEvaluationSection.class, componentId);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_EVALSECTION_MANAGEMENT)
	@APIDescription("Adds a group evaluation section to the component")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentEvaluationSection.class)
	@Path("/{id}/sections/all")
	public Response saveComponentEvaluationSections(
			@PathParam("id")
			@RequiredParam String componentId,
			@DataType(ComponentEvaluationSection.class)
			@RequiredParam List<ComponentEvaluationSection> sections)
	{
		ValidationResult allValidationResult = new ValidationResult();
		for (ComponentEvaluationSection section : sections) {
			section.setComponentId(componentId);
			section.getComponentEvaluationSectionPk().setComponentId(componentId);

			ValidationModel validationModel = new ValidationModel(section);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (!validationResult.valid()) {
				allValidationResult.merge(validationResult);
			}
		}

		if (allValidationResult.valid()) {
			for (ComponentEvaluationSection section : sections) {
				section.setActiveStatus(ComponentEvaluationSection.ACTIVE_STATUS);
				section.setCreateUser(SecurityUtil.getCurrentUserName());
				section.setUpdateUser(SecurityUtil.getCurrentUserName());
			}
			service.getComponentService().saveComponentEvaluationSection(sections);
		} else {
			return Response.ok(allValidationResult.toRestError()).build();
		}
		return Response.ok().build();
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_EVALSECTION_MANAGEMENT)
	@APIDescription("Adds an evaluation section to the component")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentEvaluationSection.class)
	@Path("/{id}/sections")
	public Response addComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentEvaluationSection section)
	{
		section.setComponentId(componentId);
		section.getComponentEvaluationSectionPk().setComponentId(componentId);
		return saveSection(section, true);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_EVALSECTION_MANAGEMENT)
	@APIDescription("Updates an evaluation section for a component")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/sections/{evalSectionId}")
	public Response updateComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("evalSectionId")
			@RequiredParam String evalSectionId,
			@RequiredParam ComponentEvaluationSection section)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentEvaluationSectionPk pk = new ComponentEvaluationSectionPk();
		pk.setComponentId(componentId);
		pk.setEvaluationSection(evalSectionId);
		ComponentEvaluationSection componentEvaluationSection = service.getPersistenceService().findById(ComponentEvaluationSection.class, pk);
		if (componentEvaluationSection != null) {
			section.setComponentId(componentId);
			section.setComponentEvaluationSectionPk(pk);
			response = saveSection(section, false);
		}
		return response;
	}

	private Response saveSection(ComponentEvaluationSection section, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(section);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			section.setActiveStatus(ComponentEvaluationSection.ACTIVE_STATUS);
			section.setCreateUser(SecurityUtil.getCurrentUserName());
			section.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentEvaluationSection(section);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create(BASE_RESOURCE_PATH
					+ section.getComponentId() + "/sections/"
					+ StringProcessor.urlEncode(section.getComponentEvaluationSectionPk().getEvaluationSection()))).entity(section).build();
		} else {
			return Response.ok(section).build();
		}
	}
	//</editor-fold>

	// <editor-fold  defaultstate="collapsed"  desc="ComponentRESTResource COMMENT section">
	@GET
	@APIDescription("Gets the list of comments associated to an entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentComment.class)
	@Path("/{id}/comments")
	public Response getComponentComment(
			@PathParam("id")
			@RequiredParam String componentId,
			@DefaultValue("false")
			@QueryParam("submissionOnly") boolean submissionOnly)
	{
		String ANONYMOUS = "Anonymous";
		String ADMIN = "Admin";
		Component component = new Component();
		component.setComponentId(componentId);
		component = component.find();
		if (component == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		String owner = component.entityOwner();
		List<ComponentComment> comments = service.getComponentService().getBaseComponent(ComponentComment.class, componentId);

		if (SecurityUtil.hasPermission(SecurityPermission.ADMIN_ENTRY_COMMENT_MANAGEMENT)) {
			/*        SUPER-ADMIN            */
			if (submissionOnly) {
				comments = comments.stream().filter(comment -> ComponentCommentType.SUBMISSION.equals(comment.getCommentType())).collect(Collectors.toList());
				return Response.ok(commentsToGenericEntity(comments)).build();
			} else {
				return Response.ok(commentsToGenericEntity(comments)).build();
			}
		} else if (SecurityUtil.hasPermission(SecurityPermission.WORKFLOW_ADMIN_SUBMISSION_COMMENTS)) {
			/*        LIBRARIAN             */
			List<ComponentComment> submissionComments = comments.stream().filter(comment -> ComponentCommentType.SUBMISSION.equals(comment.getCommentType())).collect(Collectors.toList());
			submissionComments.forEach((comment) -> {
				if (!SecurityUtil.isCurrentUserTheOwner(comment)) {
					comment.setCreateUser(ANONYMOUS);
					comment.setUpdateUser(ANONYMOUS);
				}
			});
			return Response.ok(commentsToGenericEntity(submissionComments)).build();
		} else if (SecurityUtil.isCurrentUserTheOwner(component)) {
			/*        ENTRY OWNER            */
			List<ComponentComment> submissionComments;
			submissionComments = comments.stream().filter(comment
					-> ComponentCommentType.SUBMISSION.equals(comment.getCommentType())
					&& !Convert.toBoolean(comment.getPrivateComment())
			).collect(Collectors.toList());
			submissionComments.forEach((comment) -> {
				if (!comment.getCreateUser().equals(SecurityUtil.getCurrentUserName())
						&& !comment.getCreateUser().equals(owner)) {
					comment.setCreateUser(ANONYMOUS);
				}
				if (!comment.getUpdateUser().equals(SecurityUtil.getCurrentUserName())
						&& !comment.getUpdateUser().equals(owner)) {
					comment.setUpdateUser(ANONYMOUS);
				}
				if (Convert.toBoolean(comment.getAdminComment())) {
					comment.setCreateUser(ADMIN);
					comment.setUpdateUser(ADMIN);
				}
			});
			return Response.ok(commentsToGenericEntity(submissionComments)).build();
		} else {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}

	@DELETE
	@APIDescription("Delete a comment by id from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/comments/{commentId}")
	public Response deleteComponentCommentById(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("commentId")
			@RequiredParam String commentId)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentComment example = new ComponentComment();
		example.setCommentId(commentId);
		example.setComponentId(componentId);
		ComponentComment componentComment = service.getPersistenceService().queryOneByExample(new QueryByExample<>(example));
		if (componentComment != null) {
			response = ownerCheck(componentComment, SecurityPermission.ADMIN_ENTRY_COMMENT_MANAGEMENT);
			if (response == null) {
				service.getComponentService().deleteBaseComponent(ComponentComment.class, commentId);
			}
		}
		return response;
	}

	@PUT
	@APIDescription("Update a comment associated to the component")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/comments/{commentId}")
	public Response updateComponentComment(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("commentId")
			@RequiredParam String commentId,
			ComponentComment comment)
	{
		Component component = new Component();
		component.setComponentId(componentId);
		component = component.find();
		if (component == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		ComponentComment componentComment = service.getPersistenceService().findById(ComponentComment.class, commentId);
		if (componentComment == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		if (component.getComponentId().equals(componentComment.getComponentId())
				&& SecurityUtil.isCurrentUserTheOwner(componentComment)) {
			comment.setComponentId(componentId);
			comment.setCommentId(commentId);
			return saveComment(comment, false);
		} else {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}

	private GenericEntity<List<ComponentComment>> commentsToGenericEntity(List<ComponentComment> comments)
	{
		return new GenericEntity<List<ComponentComment>>(comments)
		{
		};
	}

	private Response saveComment(ComponentComment comment, Boolean isCreated)
	{
		ValidationModel validationModel = new ValidationModel(comment);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentComment(comment);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		return isCreated ? Response.created(URI.create(BASE_RESOURCE_PATH + comment.getComponentId() + "/comments/" + comment.getCommentId())).entity(comment).build() : Response.ok(comment).build();
	}

	@POST
	@APIDescription("Add a single comment to the specified component")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentComment.class)
	@Path("/{id}/comments")
	public Response createComponentComment(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentComment comment,
			Boolean willSendEmail)
	{
		willSendEmail = true;
		Component component = new Component();
		component.setComponentId(componentId);
		component = component.find();
		if (component == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		if (SecurityUtil.isCurrentUserTheOwner(component)
				|| SecurityUtil.hasPermission(SecurityPermission.ADMIN_ENTRY_COMMENT_MANAGEMENT)
				|| SecurityUtil.hasPermission(SecurityPermission.WORKFLOW_ADMIN_SUBMISSION_COMMENTS)) {
			comment.setComponentId(componentId);
			if (SecurityUtil.hasPermission(SecurityPermission.ADMIN_ENTRY_COMMENT_MANAGEMENT)
					|| SecurityUtil.hasPermission(SecurityPermission.WORKFLOW_ADMIN_SUBMISSION_COMMENTS)) {
				comment.setAdminComment(true);
			}

			String vendor = component.getOwnerUser();
			if (willSendEmail && vendor != null){
				Email email = MailManager.newEmail();
				email.setSubject("SPOON Entry Change Request Approved");
				email.setText("Your entry change request for " + component.getName() + ", on spoonsite.com, has been approved by a system administrator. ");
				String vendorEmail = service.getUserService().getEmailFromUserProfile(vendor);
				email.addRecipient("", vendorEmail, Message.RecipientType.TO);
				MailManager.send(email, true);
			}
			return saveComment(comment, true);
		} else {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}
	// </editor-fold>

	//Deprecated: in future version
	// <editor-fold defaultstate="collapsed"  desc="ComponentRESTResource METADATA section">
	@GET
	@APIDescription("Get the entire metadata list")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentMetadata.class)
	@Path("/metadata")
	public List<ComponentMetadata> getComponentMetadata()
	{
		return service.getComponentService().getMetadata();
	}

	@GET
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentMetadata.class)
	@Path("/{id}/metadata")
	public List<ComponentMetadata> getComponentMetadata(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentMetadata.class, componentId);
	}

	@GET
	@APIDescription("Gets a metadata entity for a component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentMetadata.class)
	@Path("/{id}/metadata/{metadataId}")
	public Response getComponentMetadataEntity(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("metadataId")
			@RequiredParam String metadataId)
	{
		ComponentMetadata metadataExample = new ComponentMetadata();
		metadataExample.setMetadataId(metadataId);
		metadataExample.setComponentId(componentId);
		ComponentMetadata componentMetadata = service.getPersistenceService().queryOneByExample(metadataExample);
		return sendSingleEntityResponse(componentMetadata);
	}

	@GET
	@APIDescription("Get the dependencies from the entity")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentMetadataView.class)
	@Path("/{id}/metadata/view")
	public Response getComponentMetadataView(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentMetadata metadataExample = new ComponentMetadata();
		metadataExample.setActiveStatus(filterQueryParams.getStatus());
		metadataExample.setComponentId(componentId);

		List<ComponentMetadata> componentMetadata = service.getPersistenceService().queryByExample(metadataExample);
		componentMetadata = filterQueryParams.filter(componentMetadata);
		List<ComponentMetadataView> views = ComponentMetadataView.toViewList(componentMetadata);

		GenericEntity<List<ComponentMetadataView>> entity = new GenericEntity<List<ComponentMetadataView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@DELETE
	@APIDescription("Inactivates metadata from the specified component")
	@Path("/{id}/metadata/{metadataId}")
	public Response deleteComponentMetadata(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("metadataId")
			@RequiredParam String metadataId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_UPDATE);
		if (response != null) {
			return response;
		}

		ComponentMetadata componentMetadata = service.getPersistenceService().findById(ComponentMetadata.class, metadataId);
		if (componentMetadata != null) {
			checkBaseComponentBelongsToComponent(componentMetadata, componentId);
			service.getComponentService().deactivateBaseComponent(ComponentMetadata.class, metadataId);
		}
		return Response.noContent().build();
	}

	@PUT
	@APIDescription("Deletes metadata from the specified component")
	@Path("/{id}/metadata/{metadataId}/activate")
	public Response activateComponentMetadata(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("metadataId")
			@RequiredParam String metadataId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_UPDATE);
		if (response != null) {
			return response;
		}

		ComponentMetadata metadataExample = new ComponentMetadata();
		metadataExample.setMetadataId(metadataId);
		metadataExample.setComponentId(componentId);
		ComponentMetadata componentMetadata = service.getPersistenceService().queryOneByExample(metadataExample);
		if (componentMetadata != null) {
			service.getComponentService().activateBaseComponent(ComponentMetadata.class, metadataId);
		}
		return sendSingleEntityResponse(componentMetadata);
	}

	@POST
	@APIDescription("Add metadata to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentMetadata.class)
	@Path("/{id}/metadata")
	public Response addComponentMetadata(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentMetadata metadata)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_UPDATE);
		if (response != null) {
			return response;
		}

		metadata.setComponentId(componentId);
		return saveMetadata(metadata, true);
	}

	@PUT
	@APIDescription("Update metadata associated to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/metadata/{metadataId}")
	public Response updateComponentMetadata(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("metadataId")
			@RequiredParam String metadataId,
			@RequiredParam ComponentMetadata metadata)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_UPDATE);
		if (response != null) {
			return response;
		}

		response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentMetadata componentMetadata = service.getPersistenceService().findById(ComponentMetadata.class, metadataId);
		if (componentMetadata != null) {
			checkBaseComponentBelongsToComponent(componentMetadata, componentId);
			metadata.setMetadataId(metadataId);
			metadata.setComponentId(componentId);
			response = saveMetadata(metadata, false);
		}
		return response;
	}

	private Response saveMetadata(ComponentMetadata metadata, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(metadata);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			metadata.setActiveStatus(ComponentMetadata.ACTIVE_STATUS);
			metadata.setCreateUser(SecurityUtil.getCurrentUserName());
			metadata.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentMetadata(metadata);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create(BASE_RESOURCE_PATH + metadata.getComponentId() + "/metadata/" + metadata.getMetadataId())).entity(metadata).build();
		} else {
			return Response.ok(metadata).build();
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed"  desc="WorkPlan section">
	@GET
	@APIDescription("Get the worklink for a component")
	@RequireSecurity(SecurityPermission.USER_WORKPLAN_READ)
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(WorkPlanLinkView.class)
	@Path("/{id}/worklink")
	public Response getComponentWorkLink(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		WorkPlanLink workLink = service.getWorkPlanService().getWorkPlanForComponent(componentId);
		GenericEntity<WorkPlanLinkView> entity = new GenericEntity<WorkPlanLinkView>(WorkPlanLinkView.toView(workLink))
		{
		};
		return sendSingleEntityResponse(entity);
	}
	// </editor-fold>
}
