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

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.NetworkUtil;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.BaseComponent;
import edu.usu.sdl.openstorefront.core.entity.ChangeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentComment;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.LookupEntity;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.core.entity.UserSubmission;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.EditSubmissionOptions;
import edu.usu.sdl.openstorefront.core.util.UnitConvertUtil;
import edu.usu.sdl.openstorefront.core.view.ChangeEntryTypeAction;
import edu.usu.sdl.openstorefront.core.view.ChangeOwnerAction;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminView;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminWrapper;
import edu.usu.sdl.openstorefront.core.view.ComponentAttributeView;
import edu.usu.sdl.openstorefront.core.view.ComponentDetailView;
import edu.usu.sdl.openstorefront.core.view.ComponentFilterParams;
import edu.usu.sdl.openstorefront.core.view.ComponentLookupModel;
import edu.usu.sdl.openstorefront.core.view.ComponentPrintView;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionResponseView;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionView;
import edu.usu.sdl.openstorefront.core.view.ComponentResourceView;
import edu.usu.sdl.openstorefront.core.view.ComponentReviewView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.ComponentView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.core.view.MultipleEntryAction;
import edu.usu.sdl.openstorefront.core.view.MultipleIds;
import edu.usu.sdl.openstorefront.core.view.RequiredForComponent;
import edu.usu.sdl.openstorefront.core.view.RestErrorModel;
import edu.usu.sdl.openstorefront.core.view.TagView;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import edu.usu.sdl.openstorefront.web.rest.resource.ComponentRESTResource;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.mail.Message;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileWriter;
import net.java.truevfs.access.TPath;
import net.java.truevfs.access.TVFS;
import net.java.truevfs.kernel.spec.FsSyncException;
import org.apache.commons.lang3.StringUtils;
import org.codemonkey.simplejavamail.email.Email;

/**
 * Note: This is a chained inheritance which is exception to the general case
 * However, it allow for breaking up of a very large class in JAX-RS friendly
 * way with less code/maintenance.
 *
 * @author dshurtleff
 */
public abstract class GeneralComponentResourceExt
		extends BaseResource
{

	protected static final Logger LOG = Logger.getLogger(ComponentRESTResource.class.getSimpleName());
	protected static final String BASE_RESOURCE_PATH = "v1/resource/components/";

	@Context
	HttpServletRequest request;

	@GET
	@APIDescription("Get a list of components <br>(Note: this is only the top level component object, See Component Detail for composite resource.)")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentSearchView.class)
	public List<ComponentSearchView> getComponents()
	{
		return service.getComponentService().getComponents();
	}

	@GET
	@APIDescription("Get a list of components based on filterQueryParams for selection list.")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentLookupModel.class)
	@Path("/lookup")
	public Response getComponentLookupList(
			@BeanParam ComponentFilterParams filterQueryParams,
			@QueryParam("includePending")
			@APIDescription("Include Pending change request")
			@DefaultValue("false") boolean includePending
	)
	{
		if (filterQueryParams != null) {

			ValidationResult validationResult = filterQueryParams.validate();
			if (!validationResult.valid()) {
				return sendSingleEntityResponse(validationResult.toRestError());
			}

			List<ComponentLookupModel> lookupModels;

			List<Component> components = filterStatusOnComponents(filterQueryParams, includePending);

			components = filterEngine.filter(components);
			lookupModels = ComponentLookupModel.toView(components);
			lookupModels = filterQueryParams.filter(lookupModels);

			GenericEntity<List<ComponentLookupModel>> entity = new GenericEntity<List<ComponentLookupModel>>(lookupModels)
			{
			};
			return sendSingleEntityResponse(entity);
		} else {
			List<ComponentLookupModel> lookupModels;

			Component componentExample = new Component();

			List<Component> components = service.getPersistenceService().queryByExample(componentExample);

			if (!includePending) {
				components.removeIf(c -> {
					return Component.PENDING_STATUS.equals(c.getActiveStatus());
				});
			}

			components = filterEngine.filter(components);
			lookupModels = ComponentLookupModel.toView(components);

			GenericEntity<List<ComponentLookupModel>> entity = new GenericEntity<List<ComponentLookupModel>>(lookupModels)
			{
			};
			return sendSingleEntityResponse(entity);
		}

	}

	private List<Component> filterStatusOnComponents(ComponentFilterParams filterQueryParams, boolean includePending)
	{
		Component componentExample = new Component();
		if (!filterQueryParams.getAll()) {
			if (OpenStorefrontConstant.STATUS_VIEW_ALL.equalsIgnoreCase(filterQueryParams.getStatus())) {
				componentExample.setActiveStatus(null);
			} else {
				componentExample.setActiveStatus(filterQueryParams.getStatus());
			}

			if (OpenStorefrontConstant.STATUS_VIEW_ALL.equalsIgnoreCase(filterQueryParams.getApprovalState())) {
				componentExample.setApprovalState(null);
			} else {
				componentExample.setApprovalState(filterQueryParams.getApprovalState());
			}
		}
		componentExample.setComponentType(filterQueryParams.getComponentType());
		if (componentExample.getComponentType() != null && componentExample.getComponentType().equals(ComponentType.ALL)) {
			componentExample.setComponentType(null);
		}
		List<Component> components = service.getPersistenceService().queryByExample(componentExample);
		if (!includePending) {
			components.removeIf(c -> {
				return Component.PENDING_STATUS.equals(c.getActiveStatus());
			});
		}
		return components;
	}

	@GET
	@APIDescription("Get all resources for components")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentResourceView.class)
	@Path("/resources")
	public Response getComponentAllResources()
	{
		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setActiveStatus(ComponentResource.ACTIVE_STATUS);
		List<ComponentResource> componentResources = service.getPersistenceService().queryByExample(componentResourceExample);
		componentResources = filterEngine.filter(componentResources, true);

		List<ComponentResourceView> views = ComponentResourceView.toViewList(componentResources);

		GenericEntity<List<ComponentResourceView>> entity = new GenericEntity<List<ComponentResourceView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Get valid component approval statuses")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(LookupModel.class)
	@Path("/approvalStatus")
	public List<LookupModel> getComponentApprovalStatus()
	{
		List<LookupModel> lookupModels = new ArrayList<>();
		List<ApprovalStatus> lookups = service.getLookupService().findLookup(ApprovalStatus.class, ApprovalStatus.ACTIVE_STATUS);
		for (LookupEntity lookupEntity : lookups) {
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(lookupEntity.getCode());
			lookupModel.setDescription(lookupEntity.getDescription());
			lookupModels.add(lookupModel);
		}
		return lookupModels;
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_READ)
	@APIDescription("Get a list of all components <br>(Note: this is only the top level component object, See Component Detail for composite resource.)")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentAdminWrapper.class)
	@Path("/filterable")
	public Response getComponentList(@BeanParam ComponentFilterParams filterQueryParams
	)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentAdminWrapper componentAdminWrapper = service.getComponentService().getFilteredComponents(filterQueryParams, null);

		return sendSingleEntityResponse(componentAdminWrapper);
	}

	@GET
	@APIDescription("Get a list of all components from search query")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(Component.class)
	@Path("/typeahead")
	public Set<LookupModel> getTypeahead(
			@QueryParam("search")
			@RequiredParam String search
	)
	{
		TextSanitizer sanitizer = new TextSanitizer();
		search = (String) sanitizer.santize(search);
		return service.getComponentService().getTypeahead(search);
	}

	@GET
	@APIDescription("Get a list of components by an id set.  If it can't find a component for a griven id it's not returned.")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(Component.class)
	@Path("/list")
	public List<Component> batchGetComponents(
			@QueryParam("idList")
			@RequiredParam List<String> idList
	)
	{
		List<Component> componentViews = new ArrayList<>();
		idList.forEach(componentId -> {
			Component view = service.getPersistenceService().findById(Component.class, componentId);
			view = filterEngine.filter(view);
			if (view != null) {
				componentViews.add(view);
			}
		});

		return componentViews;
	}

	@GET
	@APIDescription("Gets a component <br>(Note: this is only the top level component object)")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(Component.class)
	@Path("/{id}")
	public Response getComponentSingle(
			@PathParam("id")
			@RequiredParam String componentId
	)
	{
		Component view = service.getPersistenceService().findById(Component.class, componentId);
		view = filterEngine.filter(view);
		return sendSingleEntityResponse(view);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_READ)
	@APIDescription("Gets a component admin view")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentAdminView.class)
	@Path("/{id}/admin")
	public Response getComponentAdminView(
			@PathParam("id")
			@RequiredParam String componentId
	)
	{
		ComponentAdminView componentAdminView = null;
		ComponentAdminWrapper views = service.getComponentService().getFilteredComponents(ComponentFilterParams.defaultFilter(), componentId);
		if (!views.getComponents().isEmpty()) {
			componentAdminView = views.getComponents().get(0);
		}
		return sendSingleEntityResponse(componentAdminView);
	}

	@GET
	@APIDescription("Export a component with full component details.")
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_EXPORT)
	@Produces({MediaType.WILDCARD})
	@DataType(ComponentAll.class)
	@Path("/{id}/export")
	public Response getComponentExport(
			@PathParam("id")
			@RequiredParam String componentId
	)
	{
		ComponentAll componentAll = service.getComponentService().getFullComponent(componentId);
		if (componentAll != null) {
			List<ComponentAll> fullComponents = new ArrayList<>();
			fullComponents.add(componentAll);
			return exportComponents(fullComponents);
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@POST
	@APIDescription("Exports a set of components.  POST ZIP or JSON file to Upload.action?UploadComponent (multipart/form-data) uploadFile to import (Requires Admin)")
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_EXPORT)
	@Produces({MediaType.WILDCARD})
	@DataType(ComponentAll.class)
	@Path("/export")
	public Response getComponentExport(
			@FormParam("id")
			@RequiredParam List<String> ids
	)
	{
		List<ComponentAll> fullComponents = new ArrayList<>();
		for (String componentId : ids) {
			ComponentAll componentAll = service.getComponentService().getFullComponent(componentId);
			fullComponents.add(componentAll);
		}
		return exportComponents(fullComponents);
	}

	private Response exportComponents(List<ComponentAll> fullComponents)
	{
		if (!fullComponents.isEmpty()) {
			try {
				String archiveName = exportComponentData(fullComponents);

				Set<String> fileNameMediaSet = new HashSet<>();
				Set<String> fileNameResourceSet = new HashSet<>();
				for (ComponentAll componentAll : fullComponents) {
					addMediaToExport(componentAll, fileNameMediaSet, archiveName);
					addResourcesToExport(componentAll, fileNameResourceSet, archiveName);
				}
				TVFS.umount();

				Response.ResponseBuilder response = Response.ok((StreamingOutput) (OutputStream output) -> {
					Files.copy(Paths.get(archiveName), output);
				});
				response.header(HEADER_CONTENT_TYPE, "application/zip");
				response.header(HEADER_CONTENT_DISPOSITION, "attachment; filename=\"ExportedComponents.zip\"");
				return response.build();
			} catch (JsonProcessingException | FsSyncException ex) {
				throw new OpenStorefrontRuntimeException("Unable to export components. Writing issue or Sync Issue", ex);
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException("Unable to export components. File Issue", ex);
			}
		} else {
			Response.ResponseBuilder response = Response.ok("[]");
			response.header(HEADER_CONTENT_TYPE, MediaType.APPLICATION_JSON);
			response.header(HEADER_CONTENT_DISPOSITION, "attachment; filename=\"ExportedComponents.json\"");
			return response.build();
		}
	}

	private void addResourcesToExport(ComponentAll componentAll, Set<String> fileNameResourceSet, String archiveName) throws IOException
	{
		//localreources
		for (ComponentResource componentResource : componentAll.getResources()) {
			java.nio.file.Path resourcePath = componentResource.pathToResource();
			if (resourcePath != null) {
				if (resourcePath.toFile().exists()) {
					String name = resourcePath.getFileName().toString();
					if (!fileNameResourceSet.contains(name)) {
						java.nio.file.Path archiveResourcePath = new TPath(archiveName + "/resources/" + name);
						Files.copy(resourcePath, archiveResourcePath);
						fileNameResourceSet.add(name);
					}
				} else {
					LOG.log(Level.WARNING, () -> MessageFormat.format("Resource not found (Not included in export) filename: {0}", (componentResource.getFile() == null ? "" : componentResource.getFile().getFileName())));
				}
			}
		}
	}

	private void addMediaToExport(ComponentAll componentAll, Set<String> fileNameMediaSet, String archiveName) throws IOException
	{
		//media
		for (ComponentMedia componentMedia : componentAll.getMedia()) {
			java.nio.file.Path mediaPath = componentMedia.pathToMedia();
			if (mediaPath != null) {
				if (mediaPath.toFile().exists()) {
					String name = mediaPath.getFileName().toString();
					if (!fileNameMediaSet.contains(name)) {
						java.nio.file.Path archiveMediaPath = new TPath(archiveName + "/media/" + name);
						Files.copy(mediaPath, archiveMediaPath);
						fileNameMediaSet.add(name);
					}
				} else {
					LOG.log(Level.WARNING, () -> MessageFormat.format("Media not found (Not included in export) filename: {0}", (componentMedia.getFile() == null ? "" : componentMedia.getFile().getFileName())));
				}
			}
		}
	}

	private String exportComponentData(List<ComponentAll> fullComponents) throws JsonProcessingException
	{
		String componentJson = StringProcessor.defaultObjectMapper().writeValueAsString(fullComponents);
		String archiveName = FileSystemManager.SYSTEM_TEMP_DIR + "/exportComponent-" + System.currentTimeMillis() + ".zip";
		File entry = new TFile(archiveName + "/components.json");
		try (Writer writer = new TFileWriter(entry)) {
			writer.write(componentJson);
		} catch (IOException io) {
			throw new OpenStorefrontRuntimeException("Unable to export components.", io);
		}
		return archiveName;
	}

	@GET
	@APIDescription("Get a list of components with a given tag")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(TagView.class)
	@Path("/singletagview")
	public Response getAllComponentsForTag(
			@QueryParam("tag") String tagText,
			@QueryParam("approvedOnly") boolean approvedOnly
	)
	{
		List<TagView> views = new ArrayList<>();

		ComponentTag componentTagExample = new ComponentTag();
		componentTagExample.setActiveStatus(Component.ACTIVE_STATUS);
		componentTagExample.setText(tagText);

		List<ComponentTag> tags = service.getPersistenceService().queryByExample(componentTagExample);
		tags = filterEngine.filter(tags, true);

		if (approvedOnly) {
			tags = tags.stream()
					.filter(tag -> service.getComponentService().checkComponentApproval(tag.getComponentId()))
					.collect(Collectors.toList());
		}

		views.addAll(TagView.toView(tags));

		GenericEntity<List<TagView>> entity = new GenericEntity<List<TagView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Get a list of components tags")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(TagView.class)
	@Path("/tagviews")
	public Response getAllComponentTags(
			@QueryParam("approvedOnly") boolean approvedOnly
	)
	{
		List<TagView> views = new ArrayList<>();

		ComponentTag componentTagExample = new ComponentTag();
		componentTagExample.setActiveStatus(Component.ACTIVE_STATUS);

		List<ComponentTag> tags = service.getPersistenceService().queryByExample(componentTagExample);

		if (approvedOnly) {
			tags = tags.stream()
					.filter(tag -> service.getComponentService().checkComponentApproval(tag.getComponentId()))
					.collect(Collectors.toList());
		}

		views.addAll(TagView.toView(tags));

		GenericEntity<List<TagView>> entity = new GenericEntity<List<TagView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_REVIEW_READ)
	@APIDescription("Get a list of components reviews")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentReviewView.class)
	@Path("/reviewviews")
	public Response getAllComponentReviews(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentReview reviewExample = new ComponentReview();
		reviewExample.setActiveStatus(filterQueryParams.getStatus());

		List<ComponentReview> componentReviews = service.getPersistenceService().queryByExample(reviewExample);
		componentReviews = filterQueryParams.filter(componentReviews);
		List<ComponentReviewView> views = ComponentReviewView.toViewList(componentReviews);

		GenericEntity<List<ComponentReviewView>> entity = new GenericEntity<List<ComponentReviewView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_QUESTIONS_READ)
	@APIDescription("Get a list of components questions")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentQuestionView.class)
	@Path("/questionviews")
	public Response getAllComponentQuestions(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentQuestion questionExample = new ComponentQuestion();
		questionExample.setActiveStatus(filterQueryParams.getStatus());

		List<ComponentQuestion> componentQuestions = service.getPersistenceService().queryByExample(questionExample);
		componentQuestions = filterQueryParams.filter(componentQuestions);

		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		List<ComponentQuestionResponse> componentQuestionResponses = service.getPersistenceService().queryByExample(responseExample);
		Map<String, List<ComponentQuestionResponseView>> responseMap = new HashMap<>();
		for (ComponentQuestionResponse componentQuestionResponse : componentQuestionResponses) {
			if (responseMap.containsKey(componentQuestionResponse.getQuestionId())) {
				responseMap.get(componentQuestionResponse.getQuestionId()).add(ComponentQuestionResponseView.toView(componentQuestionResponse));
			} else {
				List<ComponentQuestionResponseView> responseViews = new ArrayList<>();
				responseViews.add(ComponentQuestionResponseView.toView(componentQuestionResponse));
				responseMap.put(componentQuestionResponse.getQuestionId(), responseViews);
			}
		}
		List<ComponentQuestionView> views = ComponentQuestionView.toViewList(componentQuestions, responseMap);

		GenericEntity<List<ComponentQuestionView>> entity = new GenericEntity<List<ComponentQuestionView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@POST
	@APIDescription("Creates a component")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(RequiredForComponent.class)
	public Response createComponent(
			@RequiredParam RequiredForComponent component)
	{
		if (!SecurityUtil.hasPermission(SecurityPermission.ADMIN_ENTRY_CREATE)) {
			component.getComponent().setApprovalState(ApprovalStatus.NOT_SUBMITTED);
		}

		ValidationModel validationModel = new ValidationModel(component);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {

			//Convert attribute Unit if needed (from user to base)
			RequiredForComponent savedComponent = service.getComponentService().saveComponent(component);
			return Response.created(URI.create(BASE_RESOURCE_PATH + savedComponent.getComponent().getComponentId())).entity(savedComponent).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@APIDescription("Updates a component and emails the vendor")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response updateComponent(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam RequiredForComponent component)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_UPDATE);
		if (response != null) {
			return response;
		}

		component.getComponent().setComponentId(componentId);
		Set<String> attributeKeySet = new HashSet<>();
		component.getAttributes().forEach(attribute -> {
			attribute.getComponentAttributePk().setComponentId(componentId);
			attribute.setComponentId(componentId);
			attributeKeySet.add(attribute.getComponentAttributePk().pkValue());
		});

		//pull existing for pending id; need it for validation
		Component existingRecord = new Component();
		existingRecord.setComponentId(componentId);
		existingRecord = existingRecord.find();
		if (existingRecord != null) {
			component.getComponent().setPendingChangeId(existingRecord.getPendingChangeId());
		}

		ValidationModel validationModel = new ValidationModel(component);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {

			//convert Units on incoming attributes only
			for (ComponentAttribute incomingAttribute : component.getAttributes()) {
				AttributeType type = service.getAttributeService().findType(incomingAttribute.getComponentAttributePk().getAttributeType());
				if (StringUtils.isNotBlank(type.getAttributeUnit())
						&& StringUtils.isNotBlank(incomingAttribute.getPreferredUnit())
						&& !type.getAttributeUnit().equals(incomingAttribute.getPreferredUnit())) {

					incomingAttribute.getComponentAttributePk().setAttributeCode(
							UnitConvertUtil.convertUserUnitToBaseUnit(
									type.getAttributeUnit(),
									incomingAttribute.getPreferredUnit(),
									incomingAttribute.getComponentAttributePk().getAttributeCode()
							)
					);
				}
			}

			List<AttributeType> requiredAttributeTypes = service.getAttributeService().findRequiredAttributes(component.getComponent().getComponentType(), false, null);
			Set<String> requiredTypeSet = requiredAttributeTypes.stream()
					.map(AttributeType::getAttributeType)
					.collect(Collectors.toSet());

			//pick up all existing active attributes not already in the update
			ComponentAttribute componentAttributeExample = new ComponentAttribute();
			componentAttributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
			componentAttributeExample.setComponentId(componentId);
			List<ComponentAttribute> componentAttributes = service.getPersistenceService().queryByExample(componentAttributeExample);
			for (ComponentAttribute componentAttribute : componentAttributes) {
				if (!attributeKeySet.contains(componentAttribute.getComponentAttributePk().pkValue())) {

					//don't add required for the type; if missing (Allow the multiple requires to be removed)
					AttributeType attributeType = service.getAttributeService().findType(componentAttribute.getComponentAttributePk().getAttributeType());
					if (!(attributeType != null
							&& requiredTypeSet.contains(componentAttribute.getComponentAttributePk().getAttributeType()))) {
						component.getAttributes().add(componentAttribute);
					}
				}
			}

			service.getComponentService().saveComponent(component);
			Component updatedComponent = new Component();
			updatedComponent.setComponentId(componentId);
			updatedComponent = updatedComponent.find();

			Set<String> permissions = SecurityUtil.getUserContext().permissions();
			Boolean hasPermission = permissions.contains(SecurityPermission.ADMIN_ENTRY_UPDATE);
			String vendor = updatedComponent.getOwnerUser();

			if (hasPermission && vendor != null) {

				String vendorEmail = service.getUserService().getEmailFromUserProfile(vendor);
				Email email = MailManager.newEmail();
				email.setSubject("SPOON Entry Updated");
				email.setText(
					"Your entry, " + 
					updatedComponent.getName() +
					", on spoonsite.com, has been updated by a system administrator. "
				);
				email.addRecipient("", vendorEmail, Message.RecipientType.TO);

				MailManager.send(email, true);
			}

			return Response.ok(updatedComponent).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_APPROVE)
	@Produces(MediaType.APPLICATION_JSON)
	@APIDescription("Approves a set components; Typically for approve related entries.")
	@DataType(Component.class)
	@Path("/approve")
	public List<Component> approveComponents(
			@DataType(String.class) List<String> componentIds)
	{
		List<Component> approvedComponents = new ArrayList<>();
		for (String componentId : componentIds) {
			Component component = service.getComponentService().approveComponent(componentId);
			approvedComponents.add(component);
		}
		return approvedComponents;
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_APPROVE)
	@Produces(MediaType.APPLICATION_JSON)
	@APIDescription("Approves a component")
	@DataType(Component.class)
	@Path("/{id}/approve")
	public Response approveComponent(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		Component component = service.getComponentService().approveComponent(componentId);
		return sendSingleEntityResponse(component);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_CREATE)
	@APIDescription("Create a copy of a component")
	@DataType(Component.class)
	@Path("/{id}/copy")
	public Response copyComponent(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		Component component = new Component();
		component.setComponentId(componentId);
		component = component.find();
		if (component != null) {
			component = service.getComponentService().copy(componentId);
			response = Response.created(URI.create(BASE_RESOURCE_PATH + component.getComponentId())).entity(component).build();
		}
		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MERGE)
	@APIDescription("Merge component A to component B")
	@DataType(Component.class)
	@Path("/{mergeId}/{targetId}/merge")
	public Response mergeComponent(
			@PathParam("mergeId")
			@RequiredParam String mergeId,
			@PathParam("targetId")
			@RequiredParam String targetId
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		Component mergeComponent = new Component();
		mergeComponent.setComponentId(mergeId);
		mergeComponent = mergeComponent.find();

		Component targetComponent = new Component();
		targetComponent.setComponentId(targetId);
		targetComponent = targetComponent.find();
		if (mergeComponent != null && targetComponent != null) {
			if (mergeComponent.equals(targetComponent)) {
				ValidationResult validationResult = new ValidationResult();
				RuleResult ruleResult = new RuleResult();
				ruleResult.setMessage("Merge Component Id cannot be the same as the Target ComponentId");
				ruleResult.setFieldName("targetComponentId");
				validationResult.getRuleResults().add(ruleResult);
				response = sendSingleEntityResponse(validationResult.toRestError());
			} else {
				Component component = service.getComponentService().merge(mergeId, targetId);
				response = sendSingleEntityResponse(component);
			}
		} else {
			if (mergeComponent == null) {
				LOG.log(Level.FINE, () -> MessageFormat.format("Unable to merge Component....merge component not found: {0}", mergeId));
			}
			if (targetComponent == null) {
				LOG.log(Level.FINE, () -> MessageFormat.format("Unable to merge Component....target component not found: {0}", targetId));
			}
		}
		return response;
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TOGGLE_STATUS)
	@APIDescription("Activates a component")
	@Path("/{id}/activate")
	public Response activateComponent(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		Component view = service.getPersistenceService().findById(Component.class, componentId);
		if (view != null) {
			view = service.getComponentService().activateComponent(componentId);
		}
		return sendSingleEntityResponse(view);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_CHANGEOWNER)
	@APIDescription("Changes owner of component")
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("/{id}/changeowner")
	public Response changeOwner(
			@PathParam("id")
			@RequiredParam String componentId,
			String newOwner)
	{
		Component component = service.getPersistenceService().findById(Component.class, componentId);
		if (component != null) {
			service.getComponentService().changeOwner(componentId, newOwner);
			component.setCreateUser(newOwner);
		}
		return sendSingleEntityResponse(component);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_CHANGETYPE)
	@APIDescription(
			"Changes the Entry Type of an existing components to another existing Entry Type"
	)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/componenttype/{newType}")
	public Response changeType(
			@PathParam("newType") String newType,
			@APIDescription("expected to be a set valid component ids") MultipleIds multipleIds)
	{
		ComponentType found = new ComponentType();
		found.setComponentType(newType);
		found = found.find();
		if (found != null) {
			for (String componentId : multipleIds.getIds()) {
				service.getComponentService().changeComponentType(componentId, newType);
			}

			return Response.ok().build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@APIDescription("Changes librarian assigned to a component")
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("/{id}/assignLibrarian")
	public Response assignLibrarian(
			@PathParam("id")
			@RequiredParam String componentId,
			String assignLibrarian)
	{
		Component component = service.getPersistenceService().findById(Component.class, componentId);
		if (component != null) {
			service.getComponentService().assignLibrarian(componentId, assignLibrarian);
			component.setAssignedLibrarian(assignLibrarian);
		}
		return sendSingleEntityResponse(component);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TOGGLE_STATUS)
	@APIDescription("Inactivates Component and removes any assoicated user watches.")
	@Path("/{id}")
	public void deleteComponentSingle(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		service.getComponentService().deactivateComponent(componentId);
	}

	@GET
	@APIDescription("Validates the state of a given component")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentDetailView.class)
	@Path("/{id}/validate")
	public Response getComponentValidation(
			@PathParam("id")
			@RequiredParam String componentId
	)
	{
		Component tempComponent = service.getPersistenceService().findById(Component.class, componentId);
		if (tempComponent == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		List<ComponentAttribute> componentAttributes = service.getComponentService().getAttributesByComponentId(componentId);
		RequiredForComponent validationModel = new RequiredForComponent();
		validationModel.setComponent(tempComponent);
		validationModel.setAttributes(componentAttributes);
		ValidationResult result = validationModel.checkForComplete();
		if (result.valid()) {
			return Response.ok().build();
		}
		return Response.ok(result.toRestError()).build();
	}

	@GET
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentDetailView.class)
	@Path("/{id}/detail")
	public Response getComponentDetails(
			@PathParam("id")
			@RequiredParam String componentId,
			@QueryParam("type")
			@DefaultValue("default")
			@APIDescription("Pass 'Print' to retrieve special print view") String type
	)
	{
		ComponentPrintView componentPrint = null;
		ComponentDetailView componentDetail = service.getComponentService().getComponentDetails(componentId);

		if (type.equals("print")) {
			componentPrint = ComponentPrintView.toView(componentDetail);
			componentDetail = null;
		}
		//Track Views
		if (componentDetail != null || componentPrint != null) {
			String componentType = null;
			if (componentDetail != null) {
				componentType = componentDetail.getComponentType();
			} else if (componentPrint != null) {
				componentType = componentPrint.getComponentType();
			}

			ComponentTracking componentTracking = new ComponentTracking();
			componentTracking.setClientIp(NetworkUtil.getClientIp(request));
			componentTracking.setComponentId(componentId);
			componentTracking.setComponentType(componentType);
			componentTracking.setEventDts(TimeUtil.currentDate());
			componentTracking.setTrackEventTypeCode(TrackEventCode.VIEW);
			componentTracking.setActiveStatus(ComponentTracking.ACTIVE_STATUS);
			componentTracking.setCreateUser(SecurityUtil.getCurrentUserName());
			componentTracking.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentTracking(componentTracking);
		}
		service.getComponentService().setLastViewDts(componentId, SecurityUtil.getCurrentUserName());
		if (componentDetail != null) {
			for(ComponentAttributeView attrView : componentDetail.getAttributes()){
				attrView.setCodeDescription(service.getAttributeService().crushGeneralNumericString(attrView.getCodeDescription()));
			}
			return sendSingleEntityResponse(componentDetail);
		} else if (componentPrint != null) {
			return sendSingleEntityResponse(componentPrint);
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@DELETE
	@APIDescription("Delete component and all related entities")
	@Path("/{id}/cascade")
	public Response deleteComponent(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_DELETE, false);
		if (response != null) {
			return response;
		}

		service.getComponentService().cascadeDeleteOfComponent(componentId);
		return Response.ok().build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(Component.class)
	@APIDescription("Create a change request component")
	@Path("/{id}/changerequest")
	public Response createChangeRequest(
			@PathParam("id")
			@RequiredParam String componentId
	)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_CHANGEREQUEST_MANAGEMENT, true);
		if (response != null) {
			return response;
		}

		Component component = service.getComponentService().createPendingChangeComponent(componentId);
		response = Response.created(URI.create(BASE_RESOURCE_PATH + component.getComponentId())).entity(component).build();
		return response;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(UserSubmission.class)
	@APIDescription("Return a user submission view of the component. User Submission is not saved...so this is a read-only version.")
	@Path("/{id}/usersubmission")
	public Response convertToUserSubmission(
			@PathParam("id")
			@RequiredParam String componentId
	)
	{
		UserSubmission userSubmission = null;

		Component component = new Component();
		component.setComponentId(componentId);
		component = component.find();
		if (component != null) {
			userSubmission = service.getSubmissionFormService().componentToSubmission(componentId);
		}
		return sendSingleEntityResponse(userSubmission);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(UserSubmission.class)
	@APIDescription("Creates a user submission from an entry (must not be approved). Note: this will remove original entry.")
	@Path("/{id}/usersubmission")
	public Response createUserSubmission(
			@PathParam("id")
			@RequiredParam String componentId
	)
	{
		Evaluation evaluation = new Evaluation();
		evaluation.setOriginComponentId(componentId);
		List<Evaluation> evaluations = evaluation.findByExample();
		if (!evaluations.isEmpty()) {
			RestErrorModel error = new RestErrorModel();
			error.getErrors().put("componentId", "Unable to edit due to attached evaluations.");
			return sendSingleEntityResponse(error);
		}

		EditSubmissionOptions options = new EditSubmissionOptions();
		options.setRemoveComponent(true);
		return handleCreateUserSubmission(componentId, options);
	}

	private Response handleCreateUserSubmission(String componentId, EditSubmissionOptions options)
	{
		//Note: this permission is covering 3 different cases.  Right now admin can't hit this from the UI without becoming an owner.
		//Refactor later if that is an issue.
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_CHANGEREQUEST_MANAGEMENT, true);
		if (response != null) {
			return response;
		}
		Component component = new Component();
		component.setComponentId(componentId);
		component = component.find();
		if (component != null) {
			if (!options.isForChangeRequest() && ApprovalStatus.APPROVED.equals(component.getApprovalState())) {
				throw new OpenStorefrontRuntimeException("Unable to edit an Approved entry.", "Refresh and check data.");
			}

			UserSubmission userSubmission = service.getSubmissionFormService().editComponentForSubmission(componentId, options);

			response = Response.created(URI.create("v1/resource/usersubmission/" + userSubmission.getUserSubmissionId())).entity(userSubmission).build();
		} else {
			response = Response.status(Response.Status.NOT_FOUND).build();
		}

		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(UserSubmission.class)
	@APIDescription("Create a change request component")
	@Path("/{id}/changerequestforsubmission")
	public Response changeRequestForSubmission(
			@PathParam("id")
			@RequiredParam String componentId
	)
	{
		EditSubmissionOptions options = new EditSubmissionOptions();
		options.setForChangeRequest(true);
		return handleCreateUserSubmission(componentId, options);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(UserSubmission.class)
	@APIDescription("Edit a change request component (Make sure to set the component id to the change request's id")
	@Path("/{id}/editchangerequest")
	public Response editChangeRequestForSubmission(
			@PathParam("id")
			@RequiredParam String componentId
	)
	{
		EditSubmissionOptions options = new EditSubmissionOptions();
		options.setForChangeRequest(true);
		options.setEditChangeRequest(true);
		options.setRemoveComponent(true);
		return handleCreateUserSubmission(componentId, options);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_CHANGEOWNER)
	@Consumes(MediaType.APPLICATION_JSON)
	@APIDescription("Change owner of listed components and attach a comment")
	@Path("/changeowner")
	public Response changeOwnerAndComment(
			ChangeOwnerAction changeOwnerAction
	)
	{
		return entryActionWithComment((component) -> {
			String comment = changeOwnerAction.getComment().getComment();
			if (comment == null){
				comment = "";
			}
			service.getChangeLogService().logOtherChange(component, ChangeType.APPROVED, "Owner changed to " + changeOwnerAction.getNewOwner() + "<br>" + comment);
			service.getComponentService().changeOwner(component.getComponentId(), changeOwnerAction.getNewOwner());
		}, changeOwnerAction);
	}

	private Response entryActionWithComment(Consumer<Component> consumer, MultipleEntryAction multipleEntryActionObject)
	{
		ValidationModel validationModel = new ValidationModel(multipleEntryActionObject);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			for (String componentId : multipleEntryActionObject.getComponentIds()) {
				Component component = service.getPersistenceService().findById(Component.class, componentId);
				if (component != null) {
					consumer.accept(component);
					saveEntryActionComment(multipleEntryActionObject, componentId);
				} else {
					LOG.log(Level.FINE, () -> "Cannot find componentId: " + componentId);
				}
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		return Response.ok().build();
	}

	private void saveEntryActionComment(MultipleEntryAction entryAction, String componentId)
	{
		if (entryAction.getComment() != null) {
			ComponentComment componentComment = new ComponentComment();
			componentComment.setComponentId(componentId);
			componentComment.setCommentType(entryAction.getComment().getCommentType());
			componentComment.setComment(entryAction.getComment().getComment());
			componentComment.setPrivateComment(entryAction.getComment().getPrivateComment());
			componentComment.save();
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_CHANGETYPE)
	@Consumes(MediaType.APPLICATION_JSON)
	@APIDescription("Change type of listed components and attach a comment")
	@Path("/changetype")
	public Response changeTypeAndComment(
			ChangeEntryTypeAction changeEntryTypeAction
	)
	{
		return entryActionWithComment((component) -> {
			service.getComponentService().changeComponentType(component.getComponentId(), changeEntryTypeAction.getNewType());
		}, changeEntryTypeAction);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TOGGLE_STATUS)
	@Consumes(MediaType.APPLICATION_JSON)
	@APIDescription("Change status of listed components and attach a comment")
	@Path("/togglestatus")
	public Response toggleAndComment(
			MultipleEntryAction multipleEntryAction
	)
	{
		return entryActionWithComment((component) -> {
			String comment = multipleEntryAction.getComment().getComment();
			if (comment == null){
				comment = "";
			}
			if (Component.ACTIVE_STATUS.equals(component.getActiveStatus())) {
				service.getChangeLogService().logOtherChange(component, ChangeType.INACTIVATED, comment);
				service.getComponentService().deactivateComponent(component.getComponentId());
			} else {
				service.getChangeLogService().logOtherChange(component, ChangeType.ACTIVATED, comment);
				service.getComponentService().activateComponent(component.getComponentId());
			}
		}, multipleEntryAction);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_CHANGEREQUEST_MANAGEMENT)
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(Component.class)
	@APIDescription("Merges change request component with it parent component")
	@Path("/{changeRequestId}/mergechangerequest")
	public Response mergeChangeRequest(
			@PathParam("changeRequestId")
			@RequiredParam String componentId
	)
	{
		Component component = service.getComponentService().mergePendingChange(componentId);
		return Response.ok(component).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentView.class)
	@APIDescription("Get change requests for a component")
	@Path("/{id}/pendingchanges")
	public Response getPendingChanges(
			@PathParam("id")
			@RequiredParam String componentId
	)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_PENDINGCHANGE_READ, true);
		if (response != null) {
			return response;
		}

		Component componentExample = new Component();
		componentExample.setPendingChangeId(componentId);

		List<Component> components = componentExample.findByExample();
		GenericEntity<List<ComponentView>> entity = new GenericEntity<List<ComponentView>>(ComponentView.toViewListWithUserInfo(components))
		{
		};
		return sendSingleEntityResponse(entity);
	}

	// <editor-fold defaultstate="collapsed"  desc="Private Utils">
	protected void checkBaseComponentBelongsToComponent(BaseComponent component, String componentId)
	{
		if (!component.getComponentId().equals(componentId)) {
			throw new OpenStorefrontRuntimeException("Entity does not belong to component", "Check input.");
		}
	}

	protected Response checkComponentOwner(String componentId, String permission)
	{
		return checkComponentOwner(componentId, permission, false);
	}

	protected Response checkComponentOwner(String componentId, String permission, boolean skipApproveCheck)
	{
		Response response;

		Component component = new Component();
		component.setComponentId(componentId);
		component = component.find();
		if (component != null) {
			response = ownerCheck(component, permission);
			if (response == null
					&& !SecurityUtil.hasPermission(permission)
					&& !skipApproveCheck
					&& ApprovalStatus.APPROVED.equals(component.getApprovalState())) {
				response = Response.status(Response.Status.FORBIDDEN).build();
			}
		} else {
			response = Response.status(Response.Status.NOT_FOUND).build();
		}
		return response;
	}

}
