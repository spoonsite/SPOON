/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.usu.sdl.openstorefront.web.rest.resource;

// <editor-fold defaultstate="collapsed"  desc="IMPORTS">
import com.fasterxml.jackson.core.JsonProcessingException;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.NetworkUtil;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.QueryType;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.BaseComponent;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.core.entity.ComponentEvaluationSectionPk;
import edu.usu.sdl.openstorefront.core.entity.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegration;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegrationConfig;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentMetadata;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentRelationship;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewCon;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewConPk;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewPro;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewProPk;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.ComponentVersionHistory;
import edu.usu.sdl.openstorefront.core.entity.LookupEntity;
import edu.usu.sdl.openstorefront.core.entity.ReviewCon;
import edu.usu.sdl.openstorefront.core.entity.ReviewPro;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.ComponentRestoreOptions;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.sort.SortUtil;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminView;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminWrapper;
import edu.usu.sdl.openstorefront.core.view.ComponentAttributeView;
import edu.usu.sdl.openstorefront.core.view.ComponentContactView;
import edu.usu.sdl.openstorefront.core.view.ComponentDetailView;
import edu.usu.sdl.openstorefront.core.view.ComponentEvaluationSectionView;
import edu.usu.sdl.openstorefront.core.view.ComponentExternalDependencyView;
import edu.usu.sdl.openstorefront.core.view.ComponentFilterParams;
import edu.usu.sdl.openstorefront.core.view.ComponentIntegrationView;
import edu.usu.sdl.openstorefront.core.view.ComponentMediaView;
import edu.usu.sdl.openstorefront.core.view.ComponentMetadataView;
import edu.usu.sdl.openstorefront.core.view.ComponentPrintView;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionResponseView;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionView;
import edu.usu.sdl.openstorefront.core.view.ComponentRelationshipView;
import edu.usu.sdl.openstorefront.core.view.ComponentResourceView;
import edu.usu.sdl.openstorefront.core.view.ComponentReviewProCon;
import edu.usu.sdl.openstorefront.core.view.ComponentReviewView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.ComponentTrackingWrapper;
import edu.usu.sdl.openstorefront.core.view.ComponentView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.core.view.RequiredForComponent;
import edu.usu.sdl.openstorefront.core.view.RestErrorModel;
import edu.usu.sdl.openstorefront.core.view.TagView;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.io.export.DescribeExport;
import edu.usu.sdl.openstorefront.service.io.export.Exporter;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import jersey.repackaged.com.google.common.collect.Lists;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileWriter;
import net.java.truevfs.access.TPath;
import net.java.truevfs.access.TVFS;
import net.java.truevfs.kernel.spec.FsSyncException;

/**
 * ComponentRESTResource Resource
 *
 * @author dshurtleff
 * @author jlaw
 */
@Path("v1/resource/components")
@APIDescription("Components are the central resource of the system.")
public class ComponentRESTResource
		extends BaseResource
{

	private static final Logger LOG = Logger.getLogger(ComponentRESTResource.class.getSimpleName());

	@Context
	HttpServletRequest request;

	// <editor-fold defaultstate="collapsed"  desc="COMPONENT GENERAL FUNCTIONS">
	@GET
	@APIDescription("Get a list of components <br>(Note: this only the top level component object, See Component Detail for composite resource.)")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentSearchView.class)
	public List<ComponentSearchView> getComponents()
	{
		return service.getComponentService().getComponents();
	}

	@GET
	@APIDescription("Get a list of components based on filterQueryParams for selection list.")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(LookupModel.class)
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

			List<LookupModel> lookupModels = new ArrayList<>();

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

			components = FilterEngine.filter(components);
			for (Component component : components) {
				LookupModel lookupModel = new LookupModel();
				lookupModel.setCode(component.getComponentId());
				lookupModel.setDescription(component.getName());
				lookupModels.add(lookupModel);
			}
			lookupModels = filterQueryParams.filter(lookupModels);

			GenericEntity<List<LookupModel>> entity = new GenericEntity<List<LookupModel>>(lookupModels)
			{
			};
			return sendSingleEntityResponse(entity);
		} else {
			List<LookupModel> lookupModels = new ArrayList<>();

			Component componentExample = new Component();

			List<Component> components = service.getPersistenceService().queryByExample(componentExample);

			if (!includePending) {
				components.removeIf(c -> {
					return Component.PENDING_STATUS.equals(c.getActiveStatus());
				});
			}

			components = FilterEngine.filter(components);
			for (Component component : components) {
				LookupModel lookupModel = new LookupModel();
				lookupModel.setCode(component.getComponentId());
				lookupModel.setDescription(component.getName());
				lookupModels.add(lookupModel);
			}

			GenericEntity<List<LookupModel>> entity = new GenericEntity<List<LookupModel>>(lookupModels)
			{
			};
			return sendSingleEntityResponse(entity);
		}

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
		componentResources = FilterEngine.filter(componentResources, true);

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
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@APIDescription("Get a list of all components <br>(Note: this only the top level component object, See Component Detail for composite resource.)")
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
		Set<LookupModel> lookups = service.getComponentService().getTypeahead(search);
		return lookups;
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
			view = FilterEngine.filter(view);
			if (view != null) {
				componentViews.add(view);
			}
		});

		return componentViews;
	}

	@GET
	@APIDescription("Gets a component <br>(Note: this only the top level component object only)")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(Component.class)
	@Path("/{id}")
	public Response getComponentSingle(
			@PathParam("id")
			@RequiredParam String componentId
	)
	{
		Component view = service.getPersistenceService().findById(Component.class, componentId);
		view = FilterEngine.filter(view);
		return sendSingleEntityResponse(view);
	}

	@GET
	@RequireSecurity("SecurityPermission.ADMIN_ENTRY_MANAGEMENT")
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
		if (views.getComponents().isEmpty() == false) {
			componentAdminView = views.getComponents().get(0);
		}
		return sendSingleEntityResponse(componentAdminView);
	}

	@GET
	@APIDescription("Export a component with full component details.")
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
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
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
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
		if (fullComponents.isEmpty() == false) {
			try {
				String componentJson = StringProcessor.defaultObjectMapper().writeValueAsString(fullComponents);
				String archiveName = FileSystemManager.getDir(FileSystemManager.SYSTEM_TEMP_DIR) + "/exportComponent-" + System.currentTimeMillis() + ".zip";
				File entry = new TFile(archiveName + "/components.json");
				try (Writer writer = new TFileWriter(entry)) {
					writer.write(componentJson);
				} catch (IOException io) {
					throw new OpenStorefrontRuntimeException("Unable to export components.", io);
				}

				Set<String> fileNameMediaSet = new HashSet<>();
				Set<String> fileNameResourceSet = new HashSet<>();
				for (ComponentAll componentAll : fullComponents) {
					//media
					for (ComponentMedia componentMedia : componentAll.getMedia()) {
						java.nio.file.Path mediaPath = componentMedia.pathToMedia();
						if (mediaPath != null) {
							if (mediaPath.toFile().exists()) {
								String name = mediaPath.getFileName().toString();
								if (fileNameMediaSet.contains(name) == false) {
									java.nio.file.Path archiveMediaPath = new TPath(archiveName + "/media/" + name);
									Files.copy(mediaPath, archiveMediaPath);
									fileNameMediaSet.add(name);
								}
							} else {
								LOG.log(Level.WARNING, MessageFormat.format("Media not found (Not included in export) filename: {0}", componentMedia.getFileName()));
							}
						}
					}

					//localreources
					for (ComponentResource componentResource : componentAll.getResources()) {
						java.nio.file.Path resourcePath = componentResource.pathToResource();
						if (resourcePath != null) {
							if (resourcePath.toFile().exists()) {
								String name = resourcePath.getFileName().toString();
								if (fileNameResourceSet.contains(name) == false) {
									java.nio.file.Path archiveResourcePath = new TPath(archiveName + "/resources/" + name);
									Files.copy(resourcePath, archiveResourcePath);
									fileNameResourceSet.add(name);
								}
							} else {
								LOG.log(Level.WARNING, MessageFormat.format("Resource not found (Not included in export) filename: {0}", componentResource.getFileName()));
							}
						}
					}
				}
				TVFS.umount();

				Response.ResponseBuilder response = Response.ok(new StreamingOutput()
				{

					@Override
					public void write(OutputStream output) throws IOException, WebApplicationException
					{
						Files.copy(Paths.get(archiveName), output);
					}

				});
				response.header("Content-Type", "application/zip");
				response.header("Content-Disposition", "attachment; filename=\"ExportedComponents.zip\"");
				return response.build();
			} catch (JsonProcessingException | FsSyncException ex) {
				throw new OpenStorefrontRuntimeException("Unable to export components.", ex);
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException("Unable to export components.", ex);
			}
		} else {
			Response.ResponseBuilder response = Response.ok("[]");
			response.header("Content-Type", MediaType.APPLICATION_JSON);
			response.header("Content-Disposition", "attachment; filename=\"ExportedComponents.json\"");
			return response.build();
		}
	}

	@POST
	@APIDescription("Exports a set of components. In describe record format.")
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@Produces({MediaType.WILDCARD})
	@DataType(ComponentAll.class)
	@Path("/export/describe")
	public Response getComponentExportDescribe(
			@FormParam("id")
			@RequiredParam List<String> ids
	)
	{
		List<ComponentAll> fullComponents = new ArrayList<>();
		for (String componentId : ids) {
			ComponentAll componentAll = service.getComponentService().getFullComponent(componentId);
			fullComponents.add(componentAll);
		}

		Exporter exporter = new DescribeExport();
		File exportFile = exporter.export(fullComponents);

		Response.ResponseBuilder response = Response.ok((StreamingOutput) (OutputStream output) -> {
			Files.copy(exportFile.toPath(), output);
		});
		response.header("Content-Type", "application/zip");
		response.header("Content-Disposition", "attachment; filename=\"ExportedComponents.zip\"");
		return response.build();
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
		tags = FilterEngine.filter(tags, true);

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
	@RequireSecurity(SecurityPermission.ADMIN_REVIEW)
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
	@RequireSecurity(SecurityPermission.ADMIN_QUESTIONS)
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
		if (!SecurityUtil.hasPermission(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)) {
			component.getComponent().setApprovalState(ApprovalStatus.NOT_SUBMITTED);
		}

		ValidationModel validationModel = new ValidationModel(component);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			RequiredForComponent savedComponent = service.getComponentService().saveComponent(component);
			return Response.created(URI.create("v1/resource/components/" + savedComponent.getComponent().getComponentId())).entity(savedComponent).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@APIDescription("Updates a component")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response updateComponent(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam RequiredForComponent component)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
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

			//pick up all existing active attributes not already in the update
			ComponentAttribute componentAttributeExample = new ComponentAttribute();
			componentAttributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
			componentAttributeExample.setComponentId(componentId);
			List<ComponentAttribute> componentAttributes = service.getPersistenceService().queryByExample(componentAttributeExample);
			for (ComponentAttribute componentAttribute : componentAttributes) {
				if (attributeKeySet.contains(componentAttribute.getComponentAttributePk().pkValue()) == false) {
					component.getAttributes().add(componentAttribute);
				}
			}

			service.getComponentService().saveComponent(component);
			Component updatedComponent = new Component();
			updatedComponent.setComponentId(componentId);
			updatedComponent = updatedComponent.find();

			return Response.ok(updatedComponent).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
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
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
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
			response = Response.created(URI.create("v1/resource/components/" + component.getComponentId())).entity(component).build();
		}
		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
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
				LOG.log(Level.FINE, MessageFormat.format("Unable to merge Component....merge component not found: {0}", mergeId));
			}
			if (targetComponent == null) {
				LOG.log(Level.FINE, MessageFormat.format("Unable to merge Component....target component not found: {0}", targetId));
			}
		}
		return response;
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
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
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
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

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@APIDescription("Inactivates Component and removes any assoicated user watches.")
	@Path("/{id}")
	public void deleteComponentSingle(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		service.getComponentService().deactivateComponent(componentId);
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
			@APIDescription("Pass 'Print' to retrieve special print view") String type)
	{
		ComponentPrintView componentPrint = null;
		ComponentDetailView componentDetail = null;
		if (type.equals("print")) {
			ComponentDetailView temp = service.getComponentService().getComponentDetails(componentId);
			componentPrint = ComponentPrintView.toView(temp);
		} else {
			componentDetail = service.getComponentService().getComponentDetails(componentId);
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
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT, false);
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
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT, true);
		if (response != null) {
			return response;
		}

		Component component = service.getComponentService().createPendingChangeComponent(componentId);
		response = Response.created(URI.create("v1/resource/components/" + component.getComponentId())).entity(component).build();
		return response;
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
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
		Response response = Response.ok(component).build();
		return response;
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
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT, true);
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

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Version history">
	@GET
	@APIDescription("Gets all version history for a component")
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@DataType(ComponentVersionHistory.class)
	@Path("/{id}/versionhistory")
	public Response getComponentVersionHistory(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		ComponentVersionHistory versionHistory = new ComponentVersionHistory();
		versionHistory.setActiveStatus(ComponentVersionHistory.ACTIVE_STATUS);
		versionHistory.setComponentId(componentId);

		List<ComponentVersionHistory> versionHistories = service.getPersistenceService().queryByExample(versionHistory);

		GenericEntity<List<ComponentVersionHistory>> entity = new GenericEntity<List<ComponentVersionHistory>>(versionHistories)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets a version history record")
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@DataType(ComponentVersionHistory.class)
	@Path("/{id}/versionhistory/{versionHistoryId}")
	public Response getComponentVersionHistoryRecord(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("versionHistoryId")
			@RequiredParam String versionHistoryId)
	{
		ComponentVersionHistory componentVersionHistory = new ComponentVersionHistory();
		componentVersionHistory.setVersionHistoryId(versionHistoryId);
		componentVersionHistory.setComponentId(componentId);
		componentVersionHistory = (ComponentVersionHistory) componentVersionHistory.find();
		return sendSingleEntityResponse(componentVersionHistory);
	}

	@GET
	@APIDescription("Gets the detail of a component version")
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@DataType(ComponentDetailView.class)
	@Path("/{id}/versionhistory/{versionHistoryId}/view")
	public Response viewComponentVerison(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("versionHistoryId")
			@RequiredParam String versionHistoryId)
	{
		ComponentDetailView componentDetailView = service.getComponentService().viewSnapshot(versionHistoryId);
		return sendSingleEntityResponse(componentDetailView);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@APIDescription("Create a version of the current component")
	@DataType(ComponentVersionHistory.class)
	@Path("/{id}/versionhistory")
	public Response snapshotComponent(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		Component component = new Component();
		component.setComponentId(componentId);
		component = component.find();
		if (component != null) {
			ComponentVersionHistory versionHistory = service.getComponentService().snapshotVersion(componentId, null);
			response = Response.created(URI.create("v1/resource/components/" + component.getComponentId())).entity(versionHistory).build();
		}
		return response;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@APIDescription("Restores a version of the current component")
	@DataType(ComponentVersionHistory.class)
	@Path("/{id}/versionhistory/{versionHistoryId}/restore")
	public Response restoreSnapshot(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("versionHistoryId")
			@RequiredParam String versionHistoryId,
			ComponentRestoreOptions options)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		ComponentVersionHistory componentVersionHistory = new ComponentVersionHistory();
		componentVersionHistory.setVersionHistoryId(versionHistoryId);
		componentVersionHistory.setComponentId(componentId);
		componentVersionHistory = (ComponentVersionHistory) componentVersionHistory.find();
		if (componentVersionHistory != null) {
			if (options == null) {
				options = new ComponentRestoreOptions();
			}

			Component component = service.getComponentService().restoreSnapshot(versionHistoryId, options);
			response = sendSingleEntityResponse(component);
		}
		return response;
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@APIDescription("Delete a version history record")
	@Path("/{id}/versionhistory/{versionHistoryId}")
	public void deleteVersionHistory(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("versionHistoryId")
			@RequiredParam String versionHistoryId)
	{
		//confirm that the version belong to the component
		ComponentVersionHistory componentVersionHistory = new ComponentVersionHistory();
		componentVersionHistory.setVersionHistoryId(versionHistoryId);
		componentVersionHistory.setComponentId(componentId);
		componentVersionHistory = (ComponentVersionHistory) componentVersionHistory.find();
		if (componentVersionHistory != null) {
			service.getComponentService().deleteSnapshot(versionHistoryId);
		}
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed"  desc="ComponentRESTResource ATTRIBUTE Section">
	@GET
	@APIDescription("Gets attributes for a component")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentAttribute.class)
	@Path("/{id}/attributes")
	public Response getComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		componentAttributeExample.setActiveStatus(filterQueryParams.getStatus());
		componentAttributeExample.setComponentId(componentId);
		List<ComponentAttribute> componentAttributes = service.getPersistenceService().queryByExample(componentAttributeExample);
		componentAttributes = filterQueryParams.filter(componentAttributes);

		GenericEntity<List<ComponentAttribute>> entity = new GenericEntity<List<ComponentAttribute>>(componentAttributes)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets attributes for a component")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentAttributeView.class)
	@Path("/{id}/attributes/view")
	public Response getComponentAttributeView(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		componentAttributeExample.setActiveStatus(filterQueryParams.getStatus());
		componentAttributeExample.setComponentId(componentId);
		List<ComponentAttribute> componentAttributes = service.getPersistenceService().queryByExample(componentAttributeExample);
		componentAttributes = filterQueryParams.filter(componentAttributes);
		List<ComponentAttributeView> componentAttributeViews = ComponentAttributeView.toViewList(componentAttributes);

		GenericEntity<List<ComponentAttributeView>> entity = new GenericEntity<List<ComponentAttributeView>>(componentAttributeViews)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets attributes for component and a given type")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentAttribute.class)
	@Path("/{id}/attributes/{attributeType}")
	public List<ComponentAttribute> getComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("attributeType")
			@RequiredParam String attributeType)
	{
		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		componentAttributeExample.setComponentId(componentId);
		ComponentAttributePk componentAttributePk = new ComponentAttributePk();
		componentAttributePk.setAttributeType(attributeType);
		componentAttributeExample.setComponentAttributePk(componentAttributePk);
		List<ComponentAttribute> componentAttributes = service.getPersistenceService().queryByExample(new QueryByExample(componentAttributeExample));
		return componentAttributes;
	}

	@GET
	@APIDescription("Get the codes for a specified attribute type of an entity")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(AttributeCode.class)
	@Path("/{id}/attributes/{attributeType}/codes")
	public List<AttributeCode> getComponentAttributeCodes(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("attributeType")
			@RequiredParam String attributeType)
	{
		List<AttributeCode> attributeCodes = new ArrayList<>();

		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		componentAttributeExample.setComponentId(componentId);
		ComponentAttributePk componentAttributePk = new ComponentAttributePk();
		componentAttributePk.setAttributeType(attributeType);
		componentAttributeExample.setComponentAttributePk(componentAttributePk);
		List<ComponentAttribute> componentAttributes = service.getPersistenceService().queryByExample(new QueryByExample(componentAttributeExample));
		for (ComponentAttribute attribute : componentAttributes) {
			AttributeCodePk attributeCodePk = new AttributeCodePk();
			attributeCodePk.setAttributeCode(attribute.getComponentAttributePk().getAttributeCode());
			attributeCodePk.setAttributeType(attribute.getComponentAttributePk().getAttributeType());
			AttributeCode attributCode = service.getAttributeService().findCodeForType(attributeCodePk);
			if (attributCode != null) {
				attributeCodes.add(attributCode);
			}
		}
		return attributeCodes;
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@APIDescription("Remove all attributes from the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/attributes")
	public void deleteComponentAttributes(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response == null) {
			ComponentAttribute attribute = new ComponentAttribute();
			attribute.setComponentId(componentId);
			service.getPersistenceService().deleteByExample(attribute);
		}
	}

	@DELETE
	@APIDescription("Inactivates an attribute")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/attributes/{attributeType}/{attributeCode}")
	public Response inactivateComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("attributeType")
			@RequiredParam String attributeType,
			@PathParam("attributeCode")
			@RequiredParam String attributeCode)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}
		ComponentAttributePk pk = new ComponentAttributePk();
		pk.setAttributeCode(attributeCode);
		pk.setAttributeType(attributeType);
		pk.setComponentId(componentId);
		service.getComponentService().deactivateBaseComponent(ComponentAttribute.class, pk);
		return Response.ok().build();
	}

	@DELETE
	@APIDescription("Delete an attribute from the entity (Hard Removal)")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/attributes/{attributeType}/{attributeCode}/force")
	public Response deleteComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("attributeType")
			@RequiredParam String attributeType,
			@PathParam("attributeCode")
			@RequiredParam String attributeCode)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentAttributePk pk = new ComponentAttributePk();
		pk.setAttributeCode(attributeCode);
		pk.setAttributeType(attributeType);
		pk.setComponentId(componentId);
		service.getComponentService().deleteBaseComponent(ComponentAttribute.class, pk);
		return Response.ok().build();
	}

	@PUT
	@APIDescription("Activates an attribute on the component")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/attributes/{attributeType}/{attributeCode}/activate")
	public Response activateComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("attributeType")
			@RequiredParam String attributeType,
			@PathParam("attributeCode")
			@RequiredParam String attributeCode)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentAttributePk pk = new ComponentAttributePk();
		pk.setAttributeCode(attributeCode);
		pk.setAttributeType(attributeType);
		pk.setComponentId(componentId);
		service.getComponentService().activateBaseComponent(ComponentAttribute.class, pk);
		ComponentAttribute componentAttribute = service.getPersistenceService().findById(ComponentAttribute.class, pk);
		return sendSingleEntityResponse(componentAttribute);
	}

	@POST
	@APIDescription("Add an attribute to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentAttribute.class)
	@Path("/{id}/attributes")
	public Response addComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentAttribute attribute)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		attribute.setComponentId(componentId);
		attribute.getComponentAttributePk().setComponentId(componentId);

		ValidationModel validationModel = new ValidationModel(attribute);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		validationResult.merge(service.getComponentService().checkComponentAttribute(attribute));
		if (validationResult.valid()) {
			service.getComponentService().saveComponentAttribute(attribute);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		return Response.created(URI.create("v1/resource/components/"
				+ attribute.getComponentAttributePk().getComponentId() + "/attributes/"
				+ StringProcessor.urlEncode(attribute.getComponentAttributePk().getAttributeType()) + "/"
				+ StringProcessor.urlEncode(attribute.getComponentAttributePk().getAttributeCode()))).entity(attribute).build();
	}
	// </editor-fold>

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
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
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
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
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
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
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
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
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
			return Response.created(URI.create("v1/resource/components/" + dependency.getComponentId() + "/dependency/" + dependency.getDependencyId())).entity(dependency).build();
		} else {
			return Response.ok(dependency).build();
		}
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource CONTACT section">
	@GET
	@APIDescription("Gets all contact for a component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentContact.class)
	@Path("/{id}/contacts")
	public List<ComponentContact> getComponentContact(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentContact.class, componentId);
	}

	@GET
	@APIDescription("Gets all contact for a component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentContactView.class)
	@Path("/{id}/contacts/view")
	public Response getComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentContact componentContactExample = new ComponentContact();
		componentContactExample.setActiveStatus(filterQueryParams.getStatus());
		componentContactExample.setComponentId(componentId);

		List<ComponentContact> contacts = service.getPersistenceService().queryByExample(componentContactExample);
		List<ComponentContactView> contactViews = new ArrayList<>();
		contacts.forEach(contact -> {
			contactViews.add(ComponentContactView.toView(contact));
		});

		GenericEntity<List<ComponentContactView>> entity = new GenericEntity<List<ComponentContactView>>(contactViews)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@DELETE
	@APIDescription("Delete a contact from the component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/contacts/{componentContactId}")
	public Response deleteComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("componentContactId")
			@RequiredParam String componentContactId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentContact componentContact = service.getPersistenceService().findById(ComponentContact.class, componentContactId);
		if (componentContact != null) {
			checkBaseComponentBelongsToComponent(componentContact, componentId);
			service.getComponentService().deactivateBaseComponent(ComponentContact.class, componentContactId);
		}
		return Response.ok().build();
	}

	@DELETE
	@APIDescription("Delete a contact from the component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/contacts/{componentContactId}/force")
	public Response hardDeleteComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("componentContactId")
			@RequiredParam String componentContactId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentContact componentContact = service.getPersistenceService().findById(ComponentContact.class, componentContactId);
		if (componentContact != null) {
			checkBaseComponentBelongsToComponent(componentContact, componentId);
			service.getComponentService().deleteBaseComponent(ComponentContact.class, componentContactId);
		}
		return Response.ok().build();
	}

	@PUT
	@APIDescription("Activate a contact on the component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/contacts/{componentContactId}/activate")
	public Response activateComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("componentContactId")
			@RequiredParam String componentContactId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentContact componentContact = service.getPersistenceService().findById(ComponentContact.class, componentContactId);
		if (componentContact != null) {
			checkBaseComponentBelongsToComponent(componentContact, componentId);
			service.getComponentService().activateBaseComponent(ComponentContact.class, componentContactId);
			componentContact = service.getPersistenceService().findById(ComponentContact.class, componentContactId);
		}
		return sendSingleEntityResponse(componentContact);
	}

	@POST
	@APIDescription("Add a contact to the component")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentContact.class)
	@Path("/{id}/contacts")
	public Response addComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentContact contact)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		contact.setComponentId(componentId);
		return saveContact(contact, true);
	}

	@PUT
	@APIDescription("Update a contact associated to the component")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/contacts/{componentContactId}")
	public Response updateComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("componentContactId")
			@RequiredParam String componentContactId,
			ComponentContact contact)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentContact componentContact = service.getPersistenceService().findById(ComponentContact.class, componentContactId);
		if (componentContact != null) {
			checkBaseComponentBelongsToComponent(componentContact, componentId);
			contact.setComponentId(componentId);
			contact.setComponentContactId(componentContactId);
			response = saveContact(contact, false);
		}
		return response;
	}

	private Response saveContact(ComponentContact contact, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(contact);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			contact.setActiveStatus(ComponentContact.ACTIVE_STATUS);
			contact.setCreateUser(SecurityUtil.getCurrentUserName());
			contact.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentContact(contact);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/components/" + contact.getComponentId() + "/contacts/" + contact.getContactId())).entity(contact).build();
		} else {
			return Response.ok(contact).build();
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
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
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
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
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
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
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
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
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
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
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
			return Response.created(URI.create("v1/resource/components/"
					+ section.getComponentId() + "/sections/"
					+ StringProcessor.urlEncode(section.getComponentEvaluationSectionPk().getEvaluationSection()))).entity(section).build();
		} else {
			return Response.ok(section).build();
		}
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource RESOURCE section">
	@GET
	@APIDescription("Get the resources associated to the given component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentResource.class)
	@Path("/{id}/resources")
	public List<ComponentResource> getComponentResource(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		List<ComponentResource> componentResources = service.getComponentService().getBaseComponent(ComponentResource.class, componentId);
		componentResources = SortUtil.sortComponentResource(componentResources);
		return componentResources;
	}

	@GET
	@APIDescription("Get the resources associated to the given component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentResourceView.class)
	@Path("/{id}/resources/view")
	public Response getComponentResourceView(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setActiveStatus(filterQueryParams.getStatus());
		componentResourceExample.setComponentId(componentId);
		List<ComponentResource> componentResources = service.getPersistenceService().queryByExample(componentResourceExample);
		componentResources = filterQueryParams.filter(componentResources);
		List<ComponentResourceView> views = ComponentResourceView.toViewList(componentResources);

		GenericEntity<List<ComponentResourceView>> entity = new GenericEntity<List<ComponentResourceView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Get a resource associated to the given component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentResource.class)
	@Path("/{id}/resources/{resourceId}")
	public Response getComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("resourceId")
			@RequiredParam String resourceId)
	{
		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setComponentId(componentId);
		componentResourceExample.setResourceId(resourceId);
		ComponentResource componentResource = service.getPersistenceService().queryOneByExample(componentResourceExample);
		return sendSingleEntityResponse(componentResource);
	}

	@DELETE
	@APIDescription("Inactivates a given resource from the specified component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/resources/{resourceId}")
	public Response inactivateComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("resourceId")
			@RequiredParam String resourceId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setComponentId(componentId);
		componentResourceExample.setResourceId(resourceId);
		ComponentResource componentResource = service.getPersistenceService().queryOneByExample(componentResourceExample);
		if (componentResource != null) {
			service.getComponentService().deactivateBaseComponent(ComponentResource.class, resourceId);
		}
		return Response.ok().build();
	}

	@DELETE
	@APIDescription("Delete a given resource from the specified component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/resources/{resourceId}/force")
	public Response deleteComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("resourceId")
			@RequiredParam String resourceId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setComponentId(componentId);
		componentResourceExample.setResourceId(resourceId);
		ComponentResource componentResource = service.getPersistenceService().queryOneByExample(componentResourceExample);
		if (componentResource != null) {
			service.getComponentService().deleteBaseComponent(ComponentResource.class, resourceId);
		}
		return Response.ok().build();
	}

	@PUT
	@APIDescription("Activates a resource")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/resources/{resourceId}/activate")
	public Response activateComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("resourceId")
			@RequiredParam String resourceId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setComponentId(componentId);
		componentResourceExample.setResourceId(resourceId);
		ComponentResource componentResource = service.getPersistenceService().queryOneByExample(componentResourceExample);
		if (componentResource != null) {
			service.getComponentService().activateBaseComponent(ComponentResource.class, resourceId);
			componentResource.setActiveStatus(Component.ACTIVE_STATUS);
		}
		return sendSingleEntityResponse(componentResource);
	}

	@POST
	@APIDescription("Add a resource to the given entity.  Use a form to POST Resource.action?UploadResource to upload file.  "
			+ "To upload: pass the componentResource.resourceType...etc and 'file'.")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentResource.class)
	@Path("/{id}/resources")
	public Response addComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentResource resource)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		resource.setComponentId(componentId);
		return saveResource(resource, true);
	}

	@PUT
	@APIDescription("Update a resource associated with a given entity. Use a form to POST Resource.action?UploadResource to upload file. "
			+ " To upload: pass the componentResource.resourceType...etc and 'file'.")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/resources/{resourceId}")
	public Response updateComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("resourceId")
			@RequiredParam String resourceId,
			@RequiredParam ComponentResource resource)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setComponentId(componentId);
		componentResourceExample.setResourceId(resourceId);
		ComponentResource componentResource = service.getPersistenceService().queryOneByExample(componentResourceExample);
		if (componentResource != null) {
			resource.setComponentId(componentId);
			resource.setResourceId(resourceId);
			response = saveResource(resource, false);
		}
		return response;
	}

	private Response saveResource(ComponentResource resource, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(resource);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			resource.setActiveStatus(ComponentResource.ACTIVE_STATUS);
			resource.setCreateUser(SecurityUtil.getCurrentUserName());
			resource.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentResource(resource);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/components/" + resource.getComponentId() + "/resources/" + resource.getResourceId())).entity(resource).build();
		} else {
			return Response.ok(resource).build();
		}
	}
	// </editor-fold>

	// <editor-fold  defaultstate="collapsed"  desc="ComponentRESTResource MEDIA section">
	@GET
	@APIDescription("Gets the list of media associated to an entity")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentMedia.class)
	@Path("/{id}/media")
	public List<ComponentMedia> getComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentMedia.class, componentId);
	}

	@GET
	@APIDescription("Get the resources associated to the given component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentMediaView.class)
	@Path("/{id}/media/view")
	public Response getComponentMediaView(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentMedia componentMediaExample = new ComponentMedia();
		componentMediaExample.setActiveStatus(filterQueryParams.getStatus());
		componentMediaExample.setComponentId(componentId);
		List<ComponentMedia> componentMedia = service.getPersistenceService().queryByExample(componentMediaExample);
		componentMedia = filterQueryParams.filter(componentMedia);
		List<ComponentMediaView> views = ComponentMediaView.toViewList(componentMedia);

		GenericEntity<List<ComponentMediaView>> entity = new GenericEntity<List<ComponentMediaView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Get a media item associated to the given component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentMedia.class)
	@Path("/{id}/media/{mediaId}")
	public Response getComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("mediaId")
			@RequiredParam String mediaId)
	{
		ComponentMedia componentMediaExample = new ComponentMedia();
		componentMediaExample.setComponentId(componentId);
		componentMediaExample.setComponentMediaId(mediaId);
		ComponentMedia componentMedia = service.getPersistenceService().queryOneByExample(componentMediaExample);
		return sendSingleEntityResponse(componentMedia);
	}

	@DELETE
	@APIDescription("Inactivates media from the specified component")
	@Path("/{id}/media/{mediaId}")
	public Response inactivateComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("mediaId")
			@RequiredParam String mediaId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentMedia componentMedia = service.getPersistenceService().findById(ComponentMedia.class, mediaId);
		if (componentMedia != null) {
			checkBaseComponentBelongsToComponent(componentMedia, componentId);
			service.getComponentService().deactivateBaseComponent(ComponentMedia.class, mediaId);
		}
		return Response.ok().build();
	}

	@DELETE
	@APIDescription("Deletes media from the specified entity")
	@Path("/{id}/media/{mediaId}/force")
	public Response deleteComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("mediaId")
			@RequiredParam String mediaId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentMedia componentMedia = service.getPersistenceService().findById(ComponentMedia.class, mediaId);
		if (componentMedia != null) {
			checkBaseComponentBelongsToComponent(componentMedia, componentId);
			service.getComponentService().deleteBaseComponent(ComponentMedia.class, mediaId);
		}
		return Response.ok().build();
	}

	@PUT
	@APIDescription("Activates media from the specified component")
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(ComponentMedia.class)
	@Path("/{id}/media/{mediaId}/activate")
	public Response activateComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("mediaId")
			@RequiredParam String mediaId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentMedia componentMediaExample = new ComponentMedia();
		componentMediaExample.setComponentId(componentId);
		componentMediaExample.setComponentMediaId(mediaId);
		ComponentMedia componentMedia = service.getPersistenceService().queryOneByExample(componentMediaExample);
		if (componentMedia != null) {
			checkBaseComponentBelongsToComponent(componentMedia, componentId);
			service.getComponentService().activateBaseComponent(ComponentMedia.class, mediaId);
			componentMedia.setActiveStatus(ComponentMedia.ACTIVE_STATUS);
		}
		return sendSingleEntityResponse(componentMedia);
	}

	@POST
	@APIDescription("Add media to the specified entity (leave the fileName blank if you want your supplied link to be it's location) "
			+ " Use a form to POST Media.action?UploadMedia to upload file.  To upload: pass the componentMedia.mediaTypeCode...etc and 'file'.")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentMedia.class)
	@Path("/{id}/media")
	public Response addComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentMedia media)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}
		media.setComponentId(componentId);
		return saveMedia(media, true);
	}

	@PUT
	@APIDescription("Update media associated to the specified entity (leave the fileName blank if you want your supplied link to be it's location) "
			+ " Use a form to POST Media.action?UploadMedia to upload file.  To upload: pass the componentMedia.mediaTypeCode...etc and 'file'.")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/media/{mediaId}")
	public Response updateComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("mediaId")
			@RequiredParam String mediaId,
			@RequiredParam ComponentMedia media)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentMedia componentMedia = service.getPersistenceService().findById(ComponentMedia.class, mediaId);
		if (componentMedia != null) {
			checkBaseComponentBelongsToComponent(componentMedia, componentId);
			media.setComponentId(componentId);
			media.setComponentMediaId(mediaId);
			response = saveMedia(media, false);
		}
		return response;
	}

	private Response saveMedia(ComponentMedia media, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(media);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			media.setActiveStatus(ComponentMedia.ACTIVE_STATUS);
			media.setCreateUser(SecurityUtil.getCurrentUserName());
			media.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentMedia(media);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/components/" + media.getComponentId() + "/media/" + media.getComponentMediaId())).entity(media).build();
		} else {
			return Response.ok(media).build();
		}
	}
	// </editor-fold>

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
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
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
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
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
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
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
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
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
			return Response.created(URI.create("v1/resource/components/" + metadata.getComponentId() + "/metadata/" + metadata.getMetadataId())).entity(metadata).build();
		} else {
			return Response.ok(metadata).build();
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed"  desc="ComponentRESTResource QUESTION section">
	@GET
	@APIDescription("Get the questions associated with the specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentQuestion.class)
	@Path("/{id}/questions")
	public List<ComponentQuestion> getComponentQuestions(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentQuestion.class, componentId);
	}

	@GET
	@APIDescription("Get the questions associated with the specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentQuestionView.class)
	@Path("/{id}/questions/view")
	public Response getComponentQuestionView(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentQuestion questionExample = new ComponentQuestion();
		questionExample.setActiveStatus(filterQueryParams.getStatus());
		questionExample.setComponentId(componentId);

		List<ComponentQuestion> componentQuestions = service.getPersistenceService().queryByExample(questionExample);
		String user = SecurityUtil.getCurrentUserName();
		if (filterQueryParams.getStatus().equals(ComponentQuestion.ACTIVE_STATUS)) {
			ComponentQuestion pendingQuestionExample = new ComponentQuestion();
			pendingQuestionExample.setActiveStatus(ComponentQuestion.PENDING_STATUS);
			pendingQuestionExample.setComponentId(componentId);
			pendingQuestionExample.setCreateUser(user);
			componentQuestions.addAll(service.getPersistenceService().queryByExample(pendingQuestionExample));
		}
		componentQuestions = filterQueryParams.filter(componentQuestions);
		componentQuestions.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, ComponentQuestion.FIELD_CREATE_DTS));

		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);

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

	@GET
	@APIDescription("Get the questions associated with the specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentQuestionView.class)
	@Path("/{id}/questions/details")
	public List<ComponentQuestionView> getDetailComponentQuestions(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		List<ComponentQuestionView> componentQuestionViews = new ArrayList<>();
		List<ComponentQuestion> questions = service.getComponentService().getBaseComponent(ComponentQuestion.class, componentId);
		for (ComponentQuestion question : questions) {
			ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
			responseExample.setActiveStatus(ComponentQuestionResponse.ACTIVE_STATUS);
			responseExample.setQuestionId(question.getQuestionId());

			List<ComponentQuestionResponse> responses = service.getPersistenceService().queryByExample(new QueryByExample(responseExample));
			ComponentQuestionView questionView = ComponentQuestionView.toView(question, ComponentQuestionResponseView.toViewList(responses));
			componentQuestionViews.add(questionView);
		}
		return componentQuestionViews;
	}

	@GET
	@APIDescription("Get a question for the specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentQuestion.class)
	@Path("/{id}/questions/{questionId}")
	public Response getComponentQuestionResponses(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		ComponentQuestion componentQuestion = service.getPersistenceService().findById(ComponentQuestion.class, questionId);
		if (componentQuestion != null) {
			checkBaseComponentBelongsToComponent(componentQuestion, componentId);
		}
		return sendSingleEntityResponse(componentQuestion);
	}

	@DELETE
	@APIDescription("Inactivates a question from the specified entity")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/questions/{questionId}")
	public Response deleteComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		Response response = Response.ok().build();
		ComponentQuestion componentQuestion = service.getPersistenceService().findById(ComponentQuestion.class, questionId);
		if (componentQuestion != null) {
			checkBaseComponentBelongsToComponent(componentQuestion, componentId);
			response = ownerCheck(componentQuestion, SecurityPermission.ADMIN_QUESTIONS);
			if (response == null) {
				service.getComponentService().deactivateBaseComponent(ComponentQuestion.class, questionId);
				response = Response.ok().build();
			}
		}
		return response;
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_QUESTIONS)
	@APIDescription("Activates a question from the specified entity")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/questions/{questionId}/activate")
	public Response activateComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		ComponentQuestion componentQuestionExample = new ComponentQuestion();
		componentQuestionExample.setComponentId(componentId);
		componentQuestionExample.setQuestionId(questionId);

		ComponentQuestion componentQuestion = service.getPersistenceService().queryOneByExample(componentQuestionExample);
		if (componentQuestion != null) {
			service.getComponentService().activateBaseComponent(ComponentQuestion.class, questionId);
			componentQuestion.setActiveStatus(ComponentQuestion.ACTIVE_STATUS);
		}
		return sendSingleEntityResponse(componentQuestion);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_QUESTIONS)
	@APIDescription("Set a question to pending for the specified entity")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/questions/{questionId}/pending")
	public Response pendingComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		ComponentQuestion componentQuestionExample = new ComponentQuestion();
		componentQuestionExample.setComponentId(componentId);
		componentQuestionExample.setQuestionId(questionId);

		ComponentQuestion componentQuestion = service.getPersistenceService().queryOneByExample(componentQuestionExample);
		if (componentQuestion != null) {
			service.getComponentService().setQuestionPending(questionId);
			componentQuestion.setActiveStatus(ComponentQuestion.PENDING_STATUS);
		}
		return sendSingleEntityResponse(componentQuestion);
	}

	@POST
	@APIDescription("Add a new question to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentQuestion.class)
	@Path("/{id}/questions")
	public Response addComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentQuestion question)
	{
		question.setComponentId(componentId);
		return saveQuestion(question, true);
	}

	@PUT
	@APIDescription("Update a question associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/questions/{questionId}")
	public Response updateComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@RequiredParam ComponentQuestion question)
	{
		Response response = Response.ok().build();
		ComponentQuestion componentQuestion = service.getPersistenceService().findById(ComponentQuestion.class, questionId);
		if (componentQuestion != null) {
			checkBaseComponentBelongsToComponent(componentQuestion, componentId);
			response = ownerCheck(componentQuestion, SecurityPermission.ADMIN_QUESTIONS);
			if (response == null) {
				question.setComponentId(componentId);
				question.setQuestionId(questionId);
				response = saveQuestion(question, false);
			}
		}
		return response;
	}

	private Response saveQuestion(ComponentQuestion question, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(question);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			if (PropertiesManager.getValue(PropertiesManager.KEY_USER_REVIEW_AUTO_APPROVE, "true").toLowerCase().equals("true")) {
				question.setActiveStatus(ComponentQuestion.ACTIVE_STATUS);
			} else {
				question.setActiveStatus(ComponentQuestion.PENDING_STATUS);
			}
			question.setCreateUser(SecurityUtil.getCurrentUserName());
			question.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentQuestion(question);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/components/" + question.getComponentId() + "/questions/" + question.getQuestionId())).entity(question).build();
		} else {
			return Response.ok(question).build();
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed"  desc="ComponentRESTResource QUESTION RESPONSE section">
	@GET
	@APIDescription("Gets the responses for a given question associated to the specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentQuestionResponse.class)
	@Path("/{id}/questions/{questionId}/responses")
	public List<ComponentQuestionResponse> getComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		List<ComponentQuestionResponse> responses = service.getPersistenceService().queryByExample(responseExample);
		return responses;
	}

	@GET
	@APIDescription("Gets the responses for a given question associated to the specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentQuestionResponse.class)
	@Path("/{id}/questions/{questionId}/responses/{responseId}")
	public Response getComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@PathParam("responseId")
			@RequiredParam String responseId)
	{
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		responseExample.setResponseId(responseId);
		ComponentQuestionResponse questionResponse = service.getPersistenceService().queryOneByExample(responseExample);
		return sendSingleEntityResponse(questionResponse);
	}

	@DELETE
	@APIDescription("Inactivates a response from the given question on the specified component")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/questions/{questionId}/responses/{responseId}")
	public Response deleteComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@PathParam("responseId")
			@RequiredParam String responseId)
	{
		Response response = Response.ok().build();
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		responseExample.setResponseId(responseId);
		ComponentQuestionResponse questionResponse = service.getPersistenceService().queryOneByExample(responseExample);
		if (questionResponse != null) {
			response = ownerCheck(questionResponse, SecurityPermission.ADMIN_QUESTIONS);
			if (response == null) {
				service.getComponentService().deactivateBaseComponent(ComponentQuestionResponse.class, responseId);
				response = Response.ok().build();
			}
		}
		return response;
	}

	@PUT
	@RequireSecurity
	@APIDescription("Activates a response from the given question on the specified component")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/questions/{questionId}/responses/{responseId}/activate")
	public Response activateComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@PathParam("responseId")
			@RequiredParam String responseId)
	{
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		responseExample.setResponseId(responseId);
		ComponentQuestionResponse questionResponse = service.getPersistenceService().queryOneByExample(responseExample);
		if (questionResponse != null) {
			service.getComponentService().activateBaseComponent(ComponentQuestionResponse.class, responseId);
			questionResponse.setActiveStatus(ComponentQuestionResponse.ACTIVE_STATUS);
		}
		return sendSingleEntityResponse(questionResponse);
	}

	@PUT
	@RequireSecurity
	@APIDescription("Sets a response from the given question on the specified component to Pending")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/questions/{questionId}/responses/{responseId}/pending")
	public Response pendingComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@PathParam("responseId")
			@RequiredParam String responseId)
	{
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		responseExample.setResponseId(responseId);
		ComponentQuestionResponse questionResponse = service.getPersistenceService().queryOneByExample(responseExample);
		if (questionResponse != null) {
			service.getComponentService().setQuestionResponsePending(responseId);
			questionResponse.setActiveStatus(ComponentQuestionResponse.PENDING_STATUS);
		}
		return sendSingleEntityResponse(questionResponse);
	}

	@POST
	@APIDescription("Add a response to the given question on the specified component")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentQuestionResponse.class)
	@Path("/{id}/questions/{questionId}/responses")
	public Response addComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@RequiredParam ComponentQuestionResponse response)
	{
		response.setComponentId(componentId);
		response.setQuestionId(questionId);
		return saveQuestionResponse(response, true);
	}

	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/questions/{questionId}/responses/{responseId}")
	public Response updateComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@PathParam("responseId")
			@RequiredParam String responseId,
			@RequiredParam ComponentQuestionResponse questionResponseInput)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		responseExample.setResponseId(responseId);
		ComponentQuestionResponse questionResponse = service.getPersistenceService().queryOneByExample(responseExample);
		if (questionResponse != null) {
			response = ownerCheck(questionResponse, SecurityPermission.ADMIN_QUESTIONS);
			if (response == null) {
				questionResponseInput.setComponentId(componentId);
				questionResponseInput.setQuestionId(questionId);
				questionResponseInput.setResponseId(responseId);
				response = saveQuestionResponse(questionResponseInput, false);
			}
		}
		return response;
	}

	private Response saveQuestionResponse(ComponentQuestionResponse response, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(response);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			if (PropertiesManager.getValue(PropertiesManager.KEY_USER_REVIEW_AUTO_APPROVE, "true").toLowerCase().equals("true")) {
				response.setActiveStatus(ComponentQuestionResponse.ACTIVE_STATUS);
			} else {
				response.setActiveStatus(ComponentQuestionResponse.PENDING_STATUS);
			}
			response.setCreateUser(SecurityUtil.getCurrentUserName());
			response.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentQuestionResponse(response);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {

			return Response.created(URI.create("v1/resource/components/" + response.getComponentId() + "/questions/" + response.getQuestionId() + "/responses/" + response.getResponseId())).entity(response).build();
		} else {
			return Response.ok(response).build();
		}
	}
	// </editor-fold>

	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource REVIEW section">
	@GET
	@APIDescription("Get the reviews for a specified entity")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentReview.class)
	@Path("/{id}/reviews")
	public List<ComponentReview> getComponentReview(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentReview.class, componentId);
	}

	@GET
	@APIDescription("Get the reviews for a specified entity")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentReviewView.class)
	@Path("/{id}/reviews/view")
	public Response getComponentReviewView(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentReview reviewExample = new ComponentReview();
		reviewExample.setActiveStatus(filterQueryParams.getStatus());
		reviewExample.setComponentId(componentId);

		List<ComponentReview> componentReviews = service.getPersistenceService().queryByExample(reviewExample);

		if (filterQueryParams.getStatus().equals(ComponentReview.ACTIVE_STATUS)) {
			ComponentReview pendingReviewExample = new ComponentReview();
			pendingReviewExample.setActiveStatus(ComponentReview.PENDING_STATUS);
			pendingReviewExample.setCreateUser(SecurityUtil.getCurrentUserName());
			pendingReviewExample.setComponentId(componentId);

			List<ComponentReview> pendingComponentReviews = service.getPersistenceService().queryByExample(pendingReviewExample);
			componentReviews.addAll(pendingComponentReviews);
		}
		componentReviews = filterQueryParams.filter(componentReviews);
		List<ComponentReviewView> views = ComponentReviewView.toViewList(componentReviews);
		views.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_DESCENDING, ComponentReviewView.UPDATE_DATE_FIELD));

		GenericEntity<List<ComponentReviewView>> entity = new GenericEntity<List<ComponentReviewView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Get the reviews for a specified user")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentReviewView.class)
	@Path("/reviews/{username}")
	public List<ComponentReviewView> getComponentReviewsByUsername(
			@PathParam("username")
			@RequiredParam String username)
	{
		return service.getComponentService().getReviewByUser(username);
	}

	@DELETE
	@APIDescription("Delete a review from the specified entity")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/reviews/{reviewId}")
	public Response deleteComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		Response response = Response.ok().build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			response = ownerCheck(componentReview, SecurityPermission.ADMIN_REVIEW);
			if (response == null) {
				service.getComponentService().deactivateBaseComponent(ComponentReview.class, reviewId);
			}
		}
		return response;
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_REVIEW)
	@APIDescription("Activate a review on the specified component")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/reviews/{reviewId}/activate")
	public Response activateComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		ComponentReview componentReviewExample = new ComponentReview();
		componentReviewExample.setComponentId(componentId);
		componentReviewExample.setComponentReviewId(reviewId);

		ComponentReview componentReview = service.getPersistenceService().queryOneByExample(componentReviewExample);
		if (componentReview != null) {
			service.getComponentService().activateBaseComponent(ComponentReview.class, reviewId);
			componentReview.setActiveStatus(ComponentReview.ACTIVE_STATUS);
		}
		return sendSingleEntityResponse(componentReview);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_REVIEW)
	@APIDescription("Sets a review on the specified component to pending")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/reviews/{reviewId}/pending")
	public Response pendingComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		ComponentReview componentReviewExample = new ComponentReview();
		componentReviewExample.setComponentId(componentId);
		componentReviewExample.setComponentReviewId(reviewId);

		ComponentReview componentReview = service.getPersistenceService().queryOneByExample(componentReviewExample);
		if (componentReview != null) {
			service.getComponentService().setReviewPending(reviewId);
			componentReview.setActiveStatus(ComponentReview.PENDING_STATUS);
		}
		return sendSingleEntityResponse(componentReview);
	}

	@POST
	@APIDescription("Add a review to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReview.class)
	@Path("/{id}/reviews")
	public Response addComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentReview review)
	{
		review.setComponentId(componentId);
		return saveReview(review, true);
	}

	@PUT
	@APIDescription("Update a review associated with the given entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/reviews/{reviewId}")
	public Response updateComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam ComponentReview review)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview, SecurityPermission.ADMIN_REVIEW);
			if (response == null) {
				review.setComponentId(componentId);
				review.setComponentReviewId(reviewId);
				response = saveReview(review, false);
			}
		}
		return response;
	}

	private Response saveReview(ComponentReview review, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(review);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			review.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			review.setCreateUser(SecurityUtil.getCurrentUserName());
			review.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentReview(review);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/components/" + review.getComponentId() + "/review/" + review.getComponentReviewId())).entity(review).build();
		} else {
			return Response.ok(review).build();
		}
	}

	@POST
	@APIDescription("Add a review to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReviewView.class)
	@Path("/{id}/reviews/detail")
	public Response addComponentReviewDetail(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentReviewView review)
	{
		review.setComponentId(componentId);
		return saveFullReview(review, true);
	}

	@PUT
	@APIDescription("Update a review associated with the given entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/reviews/{reviewId}/detail")
	public Response updateComponentReviewDetail(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam ComponentReviewView review)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview, SecurityPermission.ADMIN_REVIEW);
			if (response == null) {
				review.setComponentId(componentId);
				review.setReviewId(reviewId);
				response = saveFullReview(review, false);
			}
		}
		return response;
	}

	private Response saveFullReview(ComponentReviewView review, Boolean post)
	{
		ComponentReview componentReview = new ComponentReview();
		componentReview.setComponentId(review.getComponentId());
		componentReview.setComponentReviewId(review.getReviewId());
		componentReview.setComment(review.getComment());
		componentReview.setLastUsed(review.getLastUsed());
		componentReview.setOrganization(review.getOrganization());
		componentReview.setRating(review.getRating());
		componentReview.setRecommend(review.isRecommend());
		componentReview.setTitle(review.getTitle());
		componentReview.setUserTimeCode(review.getUserTimeCode());
		componentReview.setUserTypeCode(review.getUserTypeCode());
		componentReview.setSecurityMarkingType(review.getSecurityMarkingType());

		List<ComponentReviewPro> pros = new ArrayList<>();
		for (ComponentReviewProCon pro : review.getPros()) {
			ComponentReviewPro componentReviewPro = new ComponentReviewPro();
			ComponentReviewProPk reviewProPk = new ComponentReviewProPk();
			reviewProPk.setReviewPro(pro.getText());
			componentReviewPro.setComponentReviewProPk(reviewProPk);
			pros.add(componentReviewPro);
		}

		List<ComponentReviewCon> cons = new ArrayList<>();
		for (ComponentReviewProCon con : review.getCons()) {
			ComponentReviewCon componentReviewCon = new ComponentReviewCon();
			ComponentReviewConPk reviewConPk = new ComponentReviewConPk();
			reviewConPk.setReviewCon(con.getText());
			componentReviewCon.setComponentReviewConPk(reviewConPk);
			cons.add(componentReviewCon);
		}

		ValidationResult validationResult = service.getComponentService().saveDetailReview(componentReview, pros, cons);

		if (validationResult.valid() == false) {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/components/" + componentReview.getComponentId() + "/review/" + componentReview.getComponentReviewId())).entity(review).build();
		} else {
			return Response.ok(review).build();
		}
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource REVIEW CON section">
	@GET
	@APIDescription("Get the cons associated to a review")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentReviewCon.class)
	@Path("/{id}/reviews/{reviewId}/cons")
	public List<ComponentReviewCon> getComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		ComponentReviewCon componentReviewConExample = new ComponentReviewCon();
		ComponentReviewConPk componentReviewConPk = new ComponentReviewConPk();
		componentReviewConPk.setComponentReviewId(reviewId);
		componentReviewConExample.setComponentReviewConPk(componentReviewConPk);
		componentReviewConExample.setComponentId(componentId);
		return service.getPersistenceService().queryByExample(new QueryByExample(componentReviewConExample));
	}

	@GET
	@APIDescription("Get the cons associated to a review")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentReviewCon.class)
	@Path("/{id}/reviews/{reviewId}/cons/{conId}")
	public Response getComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@PathParam("conId")
			@RequiredParam String conId)
	{
		ComponentReviewCon componentReviewConExample = new ComponentReviewCon();
		ComponentReviewConPk componentReviewConPk = new ComponentReviewConPk();
		componentReviewConPk.setComponentReviewId(reviewId);
		componentReviewConPk.setReviewCon(conId);
		componentReviewConExample.setComponentReviewConPk(componentReviewConPk);
		componentReviewConExample.setComponentId(componentId);

		ComponentReviewCon reviewCon = service.getPersistenceService().queryOneByExample(new QueryByExample(componentReviewConExample));
		return sendSingleEntityResponse(reviewCon);
	}

	@DELETE
	@APIDescription("Deletes all cons from the given review accociated with the specified entity")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/reviews/{reviewId}/cons")
	public Response deleteComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		Response response = Response.ok().build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview, SecurityPermission.ADMIN_REVIEW);
			if (response == null) {
				ComponentReviewCon example = new ComponentReviewCon();
				ComponentReviewConPk pk = new ComponentReviewConPk();
				pk.setComponentReviewId(reviewId);
				example.setComponentReviewConPk(pk);
				service.getPersistenceService().deleteByExample(example);
			}
		}
		return response;
	}

	@POST
	@APIDescription("Add a cons to the given review associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReviewCon.class)
	@Path("/{id}/reviews/{reviewId}/cons")
	public Response addComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam String text)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview, SecurityPermission.ADMIN_REVIEW);
			if (response == null) {
				ComponentReviewCon con = new ComponentReviewCon();
				ComponentReviewConPk pk = new ComponentReviewConPk();
				pk.setComponentReviewId(reviewId);
				ReviewCon conCode = service.getLookupService().getLookupEnity(ReviewCon.class, text);
				if (conCode == null) {
					conCode = service.getLookupService().getLookupEnityByDesc(ReviewCon.class, text);
					if (conCode == null) {
						pk.setReviewCon(null);
					} else {
						pk.setReviewCon(conCode.getCode());
					}
				} else {
					pk.setReviewCon(conCode.getCode());
				}
				con.setComponentReviewConPk(pk);
				con.setActiveStatus(ComponentReviewCon.ACTIVE_STATUS);
				con.setComponentId(componentId);
				ValidationModel validationModel = new ValidationModel(con);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult validationResult = ValidationUtil.validate(validationModel);

				if (validationResult.valid()) {
					con.setCreateUser(SecurityUtil.getCurrentUserName());
					con.setUpdateUser(SecurityUtil.getCurrentUserName());
					service.getComponentService().saveComponentReviewCon(con);
					response = Response.created(URI.create("v1/resource/components/" + con.getComponentId()
							+ "/reviews/" + con.getComponentReviewConPk().getComponentReviewId()
							+ "/cons/" + con.getComponentReviewConPk().getReviewCon())).entity(con).build();

				} else {
					response = Response.ok(validationResult.toRestError()).build();
				}
			}
		}
		return response;
	}
	// </editor-fold>

	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource REVIEW PRO section">
	@GET
	@APIDescription("Get the pros for a review associated with the given entity")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentReviewPro.class)
	@Path("/{id}/reviews/{reviewId}/pros")
	public List<ComponentReviewPro> getComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		ComponentReviewPro componentReviewProExample = new ComponentReviewPro();
		componentReviewProExample.setComponentId(componentId);
		ComponentReviewProPk componentReviewProPk = new ComponentReviewProPk();
		componentReviewProPk.setComponentReviewId(reviewId);
		componentReviewProExample.setComponentReviewProPk(componentReviewProPk);
		return service.getPersistenceService().queryByExample(new QueryByExample(componentReviewProExample));
	}

	@GET
	@APIDescription("Get the pros for a review associated with the given entity")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentReviewPro.class)
	@Path("/{id}/reviews/{reviewId}/pros/{proId}")
	public Response getComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@PathParam("proId")
			@RequiredParam String proId)
	{
		ComponentReviewPro componentReviewProExample = new ComponentReviewPro();
		componentReviewProExample.setComponentId(componentId);
		ComponentReviewProPk componentReviewProPk = new ComponentReviewProPk();
		componentReviewProPk.setComponentReviewId(reviewId);
		componentReviewProPk.setReviewPro(proId);
		componentReviewProExample.setComponentReviewProPk(componentReviewProPk);
		ComponentReviewPro componentReviewPro = service.getPersistenceService().queryOneByExample(new QueryByExample(componentReviewProExample));
		return sendSingleEntityResponse(componentReviewPro);
	}

	@DELETE
	@APIDescription("Deletes all pros from the review associated with a specified entity")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/reviews/{reviewId}/pros")
	public Response deleteComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		Response response = Response.ok().build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview, SecurityPermission.ADMIN_REVIEW);
			if (response == null) {
				ComponentReviewPro example = new ComponentReviewPro();
				ComponentReviewProPk pk = new ComponentReviewProPk();
				pk.setComponentReviewId(reviewId);
				example.setComponentReviewProPk(pk);
				service.getPersistenceService().deleteByExample(example);
				service.getComponentService().deactivateBaseComponent(ComponentReviewPro.class, pk);
			}
		}
		return response;
	}

	@POST
	@APIDescription("Add a pro to the review associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReviewPro.class)
	@Path("/{id}/reviews/{reviewId}/pros")
	public Response addComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam String text)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview, SecurityPermission.ADMIN_REVIEW);
			if (response == null) {
				ComponentReviewPro pro = new ComponentReviewPro();
				ComponentReviewProPk pk = new ComponentReviewProPk();
				pk.setComponentReviewId(reviewId);
				ReviewPro proCode = service.getLookupService().getLookupEnity(ReviewPro.class, text);
				if (proCode == null) {
					proCode = service.getLookupService().getLookupEnityByDesc(ReviewPro.class, text);
					if (proCode == null) {
						pk.setReviewPro(null);
					} else {
						pk.setReviewPro(proCode.getCode());
					}
				} else {
					pk.setReviewPro(proCode.getCode());
				}
				pro.setComponentReviewProPk(pk);
				pro.setActiveStatus(ComponentReviewPro.ACTIVE_STATUS);
				pro.setComponentId(componentId);

				ValidationModel validationModel = new ValidationModel(pro);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult validationResult = ValidationUtil.validate(validationModel);
				if (validationResult.valid()) {
					pro.setCreateUser(SecurityUtil.getCurrentUserName());
					pro.setUpdateUser(SecurityUtil.getCurrentUserName());
					service.getComponentService().saveComponentReviewPro(pro);
					response = Response.created(URI.create("v1/resource/components/" + pro.getComponentId()
							+ "/reviews/" + pro.getComponentReviewProPk().getComponentReviewId()
							+ "/pros/" + pro.getComponentReviewProPk().getReviewPro())).entity(pro).build();
				} else {
					response = Response.ok(validationResult.toRestError()).build();
				}
			}
		}
		return response;
	}
	// </editor-fold>

	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource TAG section">
	@GET
	@APIDescription("Get the entire tag list (Tag Cloud), excluding the tags already used by a some component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTag.class)
	@Path("/{id}/tagsfree")
	public List<ComponentTag> getFreeComponentTags(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		Component componentExample = new Component();
		componentExample.setComponentId(componentId);
		Component component = componentExample.find();

		if (component != null) {
			ComponentTag componentTagExample = new ComponentTag();
			componentTagExample.setComponentId(componentId);
			List<ComponentTag> componentTags = componentTagExample.findByExample();

			List<ComponentTag> allTags = service.getComponentService().getTagCloud();
			List<ComponentTag> filteredTags = new ArrayList<>();

			for (ComponentTag tag : allTags) {
				boolean pass = true;
				for (ComponentTag myTag : componentTags) {
					if (myTag.getText().toLowerCase().equals(tag.getText().toLowerCase())) {
						pass = false;
						break;
					}
				}
				if (pass) {
					filteredTags.add(tag);
				}
			}

			return filteredTags;
		} else {
			return service.getComponentService().getTagCloud();
		}
	}

	@GET
	@APIDescription("Get the entire tag list (Tag Cloud)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTag.class)
	@Path("/tags")
	public List<ComponentTag> getComponentTags()
	{
		return service.getComponentService().getTagCloud();
	}

	@GET
	@APIDescription("Get all the tag list for a specified component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTag.class)
	@Path("/{id}/tags")
	public List<ComponentTag> getComponentTag(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentTag.class, componentId);
	}

	@GET
	@APIDescription("Get all the tag list for a specified component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(TagView.class)
	@Path("/{id}/tagsview")
	public List<TagView> getComponentTagView(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		List<ComponentTag> tags = service.getComponentService().getBaseComponent(ComponentTag.class, componentId);
		return TagView.toView(tags);
	}

	@GET
	@APIDescription("Get a tag for a component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTag.class)
	@Path("/{id}/tags/{tagId}")
	public Response getComponentTag(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("tagId")
			@RequiredParam String tagId)
	{
		ComponentTag componentTagExample = new ComponentTag();
		componentTagExample.setComponentId(componentId);
		componentTagExample.setTagId(tagId);
		ComponentTag componentTag = service.getPersistenceService().queryOneByExample(new QueryByExample(componentTagExample));
		return sendSingleEntityResponse(componentTag);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@APIDescription("Delete all tags from the specified component")
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(ComponentTag.class)
	@Path("/{id}/tags")

	public void deleteComponentTags(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		ComponentTag example = new ComponentTag();
		example.setComponentId(componentId);
		service.getComponentService().deleteAllBaseComponent(ComponentTag.class, componentId);
	}

	@DELETE
	@APIDescription("Delete a tag by id from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/tags/{tagId}")
	public Response deleteComponentTagById(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("tagId")
			@RequiredParam String tagId)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentTag example = new ComponentTag();
		example.setTagId(tagId);
		example.setComponentId(componentId);
		ComponentTag componentTag = service.getPersistenceService().queryOneByExample(new QueryByExample(example));
		if (componentTag != null) {
			response = ownerCheck(componentTag, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
			if (response == null) {
				service.getComponentService().deleteBaseComponent(ComponentTag.class, tagId);
			}
		}
		return response;
	}

	@DELETE
	@APIDescription("Delete a single tag from the specified entity by the Tag Text")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/tags/text")
	public Response deleteComponentTag(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentTag example)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		ComponentTag componentTagExample = new ComponentTag();
		componentTagExample.setComponentId(componentId);
		componentTagExample.setText(example.getText());
		ComponentTag tag = service.getPersistenceService().queryOneByExample(new QueryByExample(componentTagExample));

		if (tag != null) {
			response = ownerCheck(tag, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
			if (response == null) {
				service.getComponentService().deleteBaseComponent(ComponentTag.class, tag.getTagId());
				response = Response.ok().build();
			}
		}
		return response;
	}

	@POST
	@APIDescription("Add tags to the specified component")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentTag.class)
	@Path("/{id}/tags/list")
	public Response addComponentTags(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam
			@DataType(ComponentTag.class) List<ComponentTag> tags)
	{
		Boolean valid = Boolean.TRUE;
		List<ComponentTag> verified = new ArrayList<>();
		List<RestErrorModel> unVerified = new ArrayList<>();
		if (tags.size() > 0) {
			for (ComponentTag tag : tags) {
				tag.setActiveStatus(ComponentTag.ACTIVE_STATUS);
				tag.setComponentId(componentId);
				ValidationModel validationModel = new ValidationModel(tag);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult validationResult = ValidationUtil.validate(validationModel);
				if (validationResult.valid()) {
					tag.setCreateUser(SecurityUtil.getCurrentUserName());
					tag.setUpdateUser(SecurityUtil.getCurrentUserName());
					verified.add(tag);
				} else {
					valid = Boolean.FALSE;
					unVerified.add(validationResult.toRestError());
				}
			}
			if (valid) {
				if (verified.size() > 0) {
					verified.stream().forEach((tag) -> {
						service.getComponentService().saveComponentTag(tag);
					});
					GenericEntity<List<ComponentTag>> entity = new GenericEntity<List<ComponentTag>>(Lists.newArrayList(verified))
					{
					};
					return Response.created(URI.create("v1/resource/components/" + verified.get(0).getComponentId() + "/tags/" + verified.get(0).getTagId())).entity(entity).build();
				} else {
					return Response.notAcceptable(null).build();
				}
			} else {
				GenericEntity<List<RestErrorModel>> entity = new GenericEntity<List<RestErrorModel>>(Lists.newArrayList(unVerified))
				{
				};
				return Response.ok(entity).build();
			}
		} else {
			return Response.notAcceptable(null).build();
		}
	}

	@POST
	@APIDescription("Add a single tag to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentTag.class)
	@Path("/{id}/tags")
	public Response addComponentTag(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentTag tag)
	{
		tag.setActiveStatus(ComponentTag.ACTIVE_STATUS);
		tag.setComponentId(componentId);

		List<ComponentTag> currentTags = service.getComponentService().getBaseComponent(ComponentTag.class, componentId);
		Boolean cont = Boolean.TRUE;
		if (currentTags != null && currentTags.size() > 0) {
			for (ComponentTag item : currentTags) {
				if (item.getText().equals(tag.getText())) {
					cont = Boolean.FALSE;
				}
			}
		}

		ValidationModel validationModel = new ValidationModel(tag);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			tag.setCreateUser(SecurityUtil.getCurrentUserName());
			tag.setUpdateUser(SecurityUtil.getCurrentUserName());
			if (cont) {
				service.getComponentService().saveComponentTag(tag);
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		return Response.created(URI.create("v1/resource/components/" + tag.getComponentId() + "/tags/" + tag.getTagId())).entity(tag).build();
	}
	// </editor-fold>

	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource Relationships section">
	@GET
	@APIDescription("Get all direct relationship for a specified component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentRelationshipView.class)
	@Path("/{id}/relationships")
	public Response getComponentRelationships(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentRelationship componentRelationship = new ComponentRelationship();
		componentRelationship.setComponentId(componentId);
		componentRelationship.setActiveStatus(filterQueryParams.getStatus());
		List<ComponentRelationship> relationships = componentRelationship.findByExample();
		relationships = filterQueryParams.filter(relationships);
		List<ComponentRelationshipView> views = ComponentRelationshipView.toViewList(relationships);

		GenericEntity<List<ComponentRelationshipView>> entity = new GenericEntity<List<ComponentRelationshipView>>(views)
		{
		};

		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets approved relationships (direct and indirect) for a specified component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentRelationshipView.class)
	@Path("/{id}/relationships/all")
	public Response getComponentAllRelationships(
			@PathParam("id")
			@RequiredParam String componentId
	)
	{
		List<ComponentRelationshipView> views = new ArrayList<>();

		//Pull relationships direct relationships
		ComponentRelationship componentRelationshipExample = new ComponentRelationship();
		componentRelationshipExample.setActiveStatus(ComponentRelationship.ACTIVE_STATUS);
		componentRelationshipExample.setComponentId(componentId);
		views.addAll(ComponentRelationshipView.toViewList(componentRelationshipExample.findByExample()));
		views = views.stream().filter(r -> r.getTargetApproved()).collect(Collectors.toList());

		//Pull indirect
		componentRelationshipExample = new ComponentRelationship();
		componentRelationshipExample.setActiveStatus(ComponentRelationship.ACTIVE_STATUS);
		componentRelationshipExample.setRelatedComponentId(componentId);
		List<ComponentRelationshipView> relationshipViews = ComponentRelationshipView.toViewList(componentRelationshipExample.findByExample());
		relationshipViews = relationshipViews.stream().filter(r -> r.getOwnerApproved()).collect(Collectors.toList());
		views.addAll(relationshipViews);

		GenericEntity<List<ComponentRelationshipView>> entity = new GenericEntity<List<ComponentRelationshipView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Get a relationship entity for a specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentRelationship.class)
	@Path("/{id}/relationships/{relationshipId}")
	public Response getComponentRelationship(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("relationshipId")
			@RequiredParam String relationshipId)
	{
		ComponentRelationship relationshipExample = new ComponentRelationship();
		relationshipExample.setComponentRelationshipId(relationshipId);
		relationshipExample.setComponentId(componentId);
		return sendSingleEntityResponse(relationshipExample.find());
	}

	@POST
	@APIDescription("Save a new component relationship")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentRelationship.class)
	@Path("/{id}/relationships")
	public Response addComponentRelationship(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentRelationship relationship)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}
		relationship.setComponentId(componentId);

		return handleSaveRelationship(relationship, true);
	}

	private Response handleSaveRelationship(ComponentRelationship relationship, boolean post)
	{
		ValidationResult validationResult = relationship.validate(true);
		if (validationResult.valid()) {
			relationship = service.getComponentService().saveComponentRelationship(relationship);

			if (post) {
				return Response.created(URI.create("v1/resource/components/"
						+ relationship.getComponentId()
						+ "/relationships/"
						+ relationship.getComponentRelationshipId())).entity(relationship).build();
			} else {
				return Response.ok(relationship).build();
			}
		} else {
			return sendSingleEntityResponse(validationResult.toRestError());
		}
	}

	@PUT
	@APIDescription("Updates a component relationship")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentRelationship.class)
	@Path("/{id}/relationships/{relationshipId}")
	public Response updateComponentRelationship(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("relationshipId")
			@RequiredParam String relationshipId,
			@RequiredParam ComponentRelationship relationship)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		response = Response.status(Response.Status.NOT_FOUND).build();

		ComponentRelationship existing = new ComponentRelationship();
		existing.setComponentId(componentId);
		existing.setComponentRelationshipId(relationshipId);
		existing = (ComponentRelationship) existing.find();
		if (existing != null) {
			relationship.setComponentId(componentId);
			relationship.setComponentRelationshipId(relationshipId);
			response = handleSaveRelationship(relationship, false);
		}

		return response;
	}

	@DELETE
	@APIDescription("Deletes a relationship for a specified component")
	@Path("/{id}/relationships/{relationshipId}")
	public Response deleteRelationship(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("relationshipId")
			@RequiredParam String relationshipId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		if (response != null) {
			return response;
		}

		service.getComponentService().deleteBaseComponent(ComponentRelationship.class, relationshipId);
		return Response.ok().build();
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed"  desc="ComponentRESTResource TRACKING section">
	@GET
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING)
	@APIDescription("Get the list of tracking details on a specified component. Always sorts by create date.")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentTrackingWrapper.class)
	@Path("/{id}/tracking")
	public Response getActiveComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentTracking trackingExample = new ComponentTracking();
		trackingExample.setComponentId(componentId);
		trackingExample.setActiveStatus(filterQueryParams.getStatus());

		QueryByExample<ComponentTracking> queryByExample = new QueryByExample(trackingExample);
		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());

		ComponentTracking trackingOrderExample = new ComponentTracking();
		trackingOrderExample.setCreateDts(QueryByExample.DATE_FLAG);
		queryByExample.setOrderBy(trackingOrderExample);
		queryByExample.setSortDirection(OpenStorefrontConstant.SORT_DESCENDING);

		List<ComponentTracking> componentTrackings = service.getPersistenceService().queryByExample(queryByExample);

		long total = service.getPersistenceService().countByExample(new QueryByExample(QueryType.COUNT, trackingExample));
		return sendSingleEntityResponse(new ComponentTrackingWrapper(componentTrackings, total));
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING)
	@APIDescription("Get the list of tracking details on a specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentTracking.class)
	@Path("/{id}/tracking/{trackingId}")
	public Response getComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("id")
			@RequiredParam String trackingId)
	{
		ComponentTracking componentTrackingExample = new ComponentTracking();
		componentTrackingExample.setComponentId(componentId);
		componentTrackingExample.setComponentTrackingId(trackingId);
		ComponentTracking componentTracking = service.getPersistenceService().queryOneByExample(componentTrackingExample);
		return sendSingleEntityResponse(componentTracking);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING)
	@APIDescription("Remove a tracking entry from the specified component")
	@Path("/{id}/tracking/{trackingId}")
	public void deleteComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("id")
			@RequiredParam String trackingId)
	{
		ComponentTracking componentTrackingExample = new ComponentTracking();
		componentTrackingExample.setComponentId(componentId);
		componentTrackingExample.setComponentTrackingId(trackingId);
		ComponentTracking componentTracking = service.getPersistenceService().queryOneByExample(componentTrackingExample);
		if (componentTracking != null) {
			service.getComponentService().deactivateBaseComponent(ComponentTracking.class, trackingId);
		}
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING)
	@APIDescription("Remove all tracking entries from the specified component")
	@Path("/{id}/tracking")
	public void deleteAllComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		service.getComponentService().deleteAllBaseComponent(ComponentTracking.class, componentId);
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed"  desc="Evaluation section">
//	@GET
//	@APIDescription("Get a complete evaluation for a ")
//	@Produces({MediaType.APPLICATION_JSON})
//	@DataType(ComponentRelationshipView.class)
//	@Path("/{id}/evaluation/{evaluationId}")
//	public Response getEvaluation (
//			@PathParam("id")
//			@RequiredParam String componentId,
//			@BeanParam FilterQueryParams filterQueryParams)
//	{
//
//	}
	// </editor-fold>
	// <editor-fold defaultstate="collapsed"  desc="Integrations">
	@GET
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Gets all integration models from the database.")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentIntegration.class)
	@Path("/integration")
	public List<ComponentIntegrationView> getIntegrations(
			@QueryParam("status")
			@DefaultValue("A")
			@APIDescription("Pass 'ALL' to view active and inactive") String status)
	{
		if (OpenStorefrontConstant.STATUS_VIEW_ALL.equals(status)) {
			status = null;
		}
		List<ComponentIntegration> integrationModels = service.getComponentService().getComponentIntegrationModels(status);
		List<ComponentIntegrationView> views = new ArrayList<>();
		for (ComponentIntegration temp : integrationModels) {
			views.add(ComponentIntegrationView.toView(temp));
		}
		return views;
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Gets a integration model")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentIntegration.class)
	@Path("/{id}/integration")
	public Response getIntegration(
			@PathParam("id") String componentId)
	{
		ComponentIntegration integrationExample = new ComponentIntegration();
		integrationExample.setComponentId(componentId);
		ComponentIntegration integration = service.getPersistenceService().queryOneByExample(integrationExample);
		ComponentIntegrationView view = null;
		if (integration != null) {
			view = ComponentIntegrationView.toView(integration);
		}
		return sendSingleEntityResponse(view);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Saves a component integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration")
	public Response saveIntegration(
			@PathParam("componentId")
			@RequiredParam String componentId,
			ComponentIntegration integration)
	{
		ValidationModel validationModel = new ValidationModel(integration);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentIntegration(integration);
			return Response.created(URI.create("v1/resource/components/" + componentId + "/integration")).entity(integration).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Updates a component integration refresh Time")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/cron")
	public Response saveRefreshRate(
			@PathParam("componentId")
			@RequiredParam String componentId,
			String cron)
	{
		ComponentIntegration integration = service.getPersistenceService().findById(ComponentIntegration.class, componentId);
		if (integration != null) {
			integration.setRefreshRate(cron);
			ValidationModel validationModel = new ValidationModel(integration);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (validationResult.valid()) {
				service.getComponentService().saveComponentIntegration(integration);
				return Response.created(URI.create("v1/resource/components/" + componentId + "/integration")).entity(integration).build();
			} else {
				return Response.ok(validationResult.toRestError()).build();
			}
		} else {
			return Response.ok().build();
		}

	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Removes the integration override time")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/cron")
	public Response deleteRefreshRate(
			@PathParam("componentId")
			@RequiredParam String componentId)
	{
		ComponentIntegration integration = service.getPersistenceService().findById(ComponentIntegration.class, componentId);
		if (integration != null) {
			integration.setRefreshRate(null);
			ValidationModel validationModel = new ValidationModel(integration);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (validationResult.valid()) {
				service.getComponentService().saveComponentIntegration(integration);
				return Response.created(URI.create("v1/resource/components/" + componentId + "/integration")).entity(integration).build();
			} else {
				return Response.ok(validationResult.toRestError()).build();
			}
		} else {
			return Response.ok().build();
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Activates  a component integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/activate")
	public Response activateIntegration(
			@PathParam("componentId")
			@RequiredParam String componentId)
	{
		ComponentIntegration componentIntegration = service.getPersistenceService().findById(ComponentIntegration.class, componentId);
		if (componentIntegration != null) {
			service.getComponentService().setStatusOnComponentIntegration(componentId, ComponentIntegration.ACTIVE_STATUS);
		}
		return sendSingleEntityResponse(componentIntegration, Response.Status.NOT_MODIFIED);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Inactivates a component integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/inactivate")
	public Response inactiveIntegration(
			@PathParam("componentId")
			@RequiredParam String componentId)
	{
		ComponentIntegration componentIntegration = service.getPersistenceService().findById(ComponentIntegration.class, componentId);
		if (componentIntegration != null) {
			service.getComponentService().setStatusOnComponentIntegration(componentId, ComponentIntegration.INACTIVE_STATUS);
		}
		return sendSingleEntityResponse(componentIntegration, Response.Status.NOT_MODIFIED);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Toggle status for multiple component integration models. Consumes a list of componentId strings")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/integration/togglemultiple")
	public void toggleMultipleIntegrations(
			@RequiredParam
			@DataType(String.class) List<String> componentIds)
	{
		for (String componentId : componentIds) {
			ComponentIntegration componentIntegration = service.getPersistenceService().findById(ComponentIntegration.class, componentId);
			if (componentIntegration != null) {
				if (componentIntegration.getActiveStatus().equals(ComponentIntegration.ACTIVE_STATUS)) {
					service.getComponentService().setStatusOnComponentIntegration(componentId, ComponentIntegration.INACTIVE_STATUS);
				} else {
					service.getComponentService().setStatusOnComponentIntegration(componentId, ComponentIntegration.ACTIVE_STATUS);
				}
			}
		}

	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Removes component integration and all child configs.")
	@Path("/{componentId}/integration")
	public Response deleteComponentConfig(
			@PathParam("componentId")
			@RequiredParam String componentId)
	{
		ComponentIntegration integration = service.getPersistenceService().findById(ComponentIntegration.class, componentId);
		if (integration.getActiveStatus().equals(ComponentIntegration.ACTIVE_STATUS) && integration.getStatus().equals(RunStatus.WORKING)) {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		} else {
			service.getComponentService().deleteComponentIntegration(componentId);
			return Response.ok().build();
		}
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Runs a full component integration")
	@Path("/{componentId}/integration/run")
	public Response runComponentIntegration(
			@PathParam("componentId")
			@RequiredParam String componentId)
	{
		ComponentIntegration integration = service.getPersistenceService().findById(ComponentIntegration.class, componentId);
		if (integration != null) {
			if (integration.getActiveStatus().equals(ComponentIntegration.INACTIVE_STATUS)) {
				if (integration.getStatus().equals(RunStatus.WORKING)) {
					integration.setStatus(RunStatus.COMPLETE);
					service.getComponentService().saveComponentIntegration(integration);

				}
				return Response.status(Response.Status.NOT_MODIFIED).build();
			} else {
				if (!integration.getStatus().equals(RunStatus.WORKING)) {
					JobManager.runComponentIntegrationNow(componentId, null);
					return Response.ok().build();
				}
				return Response.status(Response.Status.NOT_MODIFIED).build();
			}
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Runs all active component integrations")
	@Path("/integrations/run")
	public Response runAllComponentIntegration()
	{
		List<ComponentIntegration> componentIntegrations = service.getComponentService().getComponentIntegrationModels(ComponentIntegration.ACTIVE_STATUS);
		for (ComponentIntegration componentIntegration : componentIntegrations) {
			JobManager.runComponentIntegrationNow(componentIntegration.getComponentId(), null);
		}
		return Response.ok().build();
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Gets all component integration configs")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentIntegrationConfig.class)
	@Path("/{componentId}/integration/configs")
	public List<ComponentIntegrationConfig> getIntegrationConfigs(
			@PathParam("componentId") String componentId)
	{
		List<ComponentIntegrationConfig> configs;
		ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
		integrationConfigExample.setComponentId(componentId);
		configs = service.getPersistenceService().queryByExample(integrationConfigExample);
		return configs;
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Gets all component integration configs")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentIntegrationConfig.class)
	@Path("/{componentId}/integration/configs/{configId}")
	public Response getIntegrationConfigs(
			@PathParam("componentId") String componentId,
			@PathParam("configId") String configId)
	{
		ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
		integrationConfigExample.setComponentId(componentId);
		integrationConfigExample.setIntegrationConfigId(configId);
		ComponentIntegrationConfig integrationConfig = service.getPersistenceService().queryOneByExample(integrationConfigExample);
		return sendSingleEntityResponse(integrationConfig);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Saves a component integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/configs")
	public Response saveIntegrationConfig(
			@PathParam("componentId")
			@RequiredParam String componentId,
			ComponentIntegrationConfig integrationConfig)
	{
		integrationConfig.setComponentId(componentId);

		ValidationModel validationModel = new ValidationModel(integrationConfig);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);

		if (validationResult.valid()) {
			//check for exsiting config with the same ticket
			ComponentIntegrationConfig configExample = new ComponentIntegrationConfig();
			configExample.setComponentId(integrationConfig.getComponentId());
			configExample.setIntegrationType(integrationConfig.getIntegrationType());
			configExample.setProjectType(integrationConfig.getProjectType());
			configExample.setIssueType(integrationConfig.getIssueType());
			configExample.setIssueNumber(integrationConfig.getIssueNumber());

			GenerateStatementOption option = new GenerateStatementOption();
			option.setMethod(GenerateStatementOption.METHOD_UPPER_CASE);

			QueryByExample configQueryExample = new QueryByExample();
			configQueryExample.getFieldOptions().put(ComponentIntegrationConfig.FIELD_ISSUENUMBER, option);
			configQueryExample.setExample(configExample);

			long count = service.getPersistenceService().countByExample(configQueryExample);
			if (count > 0) {
				RestErrorModel restErrorModel = new RestErrorModel();
				restErrorModel.getErrors().put(ComponentIntegrationConfig.FIELD_ISSUENUMBER, "Issue number needs to be unique per project.");
				return Response.status(Response.Status.CONFLICT).entity(restErrorModel).build();
			} else {
				integrationConfig.setActiveStatus(ComponentIntegrationConfig.ACTIVE_STATUS);
				integrationConfig = service.getComponentService().saveComponentIntegrationConfig(integrationConfig);
				return Response.created(URI.create("v1/resource/components/" + componentId + "/integration/configs/" + integrationConfig.getIntegrationConfigId())).entity(integrationConfig).build();
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Updates a component integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/configs/{integrationConfigId}")
	public Response updateIntegrationConfig(
			@PathParam("componentId")
			@RequiredParam String componentId,
			@PathParam("integrationConfigId")
			@RequiredParam String integrationConfigId,
			ComponentIntegrationConfig integrationConfig)
	{
		integrationConfig.setComponentId(componentId);
		integrationConfig.setIntegrationConfigId(integrationConfigId);

		ComponentIntegrationConfig configExample = new ComponentIntegrationConfig();
		configExample.setComponentId(componentId);
		configExample.setIntegrationConfigId(integrationConfigId);

		configExample = service.getPersistenceService().queryOneByExample(configExample);
		if (configExample != null) {
			ValidationModel validationModel = new ValidationModel(integrationConfig);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);

			if (validationResult.valid()) {
				//check for exsiting config with the same ticket
				configExample = new ComponentIntegrationConfig();
				configExample.setComponentId(integrationConfig.getComponentId());
				configExample.setIntegrationConfigId(integrationConfig.getIntegrationConfigId());
				configExample.setIntegrationType(integrationConfig.getIntegrationType());
				configExample.setProjectType(integrationConfig.getProjectType());
				configExample.setIssueType(integrationConfig.getIssueType());
				configExample.setIssueNumber(integrationConfig.getIssueNumber());

				GenerateStatementOption configIdOption = new GenerateStatementOption();
				configIdOption.setOperation(GenerateStatementOption.OPERATION_NOT_EQUALS);

				GenerateStatementOption issueNumberOption = new GenerateStatementOption();
				issueNumberOption.setMethod(GenerateStatementOption.METHOD_UPPER_CASE);

				QueryByExample configQueryExample = new QueryByExample();
				configQueryExample.getFieldOptions().put("integrationConfigId", configIdOption);
				configQueryExample.getFieldOptions().put("issueNumber", issueNumberOption);
				configQueryExample.setExample(configExample);

				long count = service.getPersistenceService().countByExample(configQueryExample);
				if (count > 0) {
					RestErrorModel restErrorModel = new RestErrorModel();
					restErrorModel.getErrors().put("issueNumber", "Issue number needs to be unique per project.");
					return Response.status(Response.Status.CONFLICT).entity(restErrorModel).build();
				} else {
					integrationConfig.setActiveStatus(ComponentIntegrationConfig.ACTIVE_STATUS);
					integrationConfig = service.getComponentService().saveComponentIntegrationConfig(integrationConfig);
					return Response.ok(integrationConfig).build();
				}
			} else {
				return Response.ok(validationResult.toRestError()).build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Activates a component integration config")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/configs/{configId}/activate")
	public Response activateIntegrationConfig(
			@PathParam("componentId")
			@RequiredParam String componentId,
			@PathParam("configId") String configId)
	{
		ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
		integrationConfigExample.setComponentId(componentId);
		integrationConfigExample.setIntegrationConfigId(configId);
		ComponentIntegrationConfig integrationConfig = service.getPersistenceService().queryOneByExample(integrationConfigExample);

		if (integrationConfig != null) {
			service.getComponentService().setStatusOnComponentIntegrationConfig(configId, ComponentIntegrationConfig.ACTIVE_STATUS);
		}
		return sendSingleEntityResponse(integrationConfig, Response.Status.NOT_MODIFIED);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Saves a component integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/configs/{configId}/inactivate")
	public Response inactiveIntegrationConfig(
			@PathParam("componentId")
			@RequiredParam String componentId,
			@PathParam("configId") String configId)
	{
		ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
		integrationConfigExample.setComponentId(componentId);
		integrationConfigExample.setIntegrationConfigId(configId);
		ComponentIntegrationConfig integrationConfig = service.getPersistenceService().queryOneByExample(integrationConfigExample);

		if (integrationConfig != null) {
			service.getComponentService().setStatusOnComponentIntegrationConfig(configId, ComponentIntegrationConfig.INACTIVE_STATUS);
		}
		return sendSingleEntityResponse(integrationConfig, Response.Status.NOT_MODIFIED);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Deletes component integration config")
	@Path("/{componentId}/integration/configs/{configId}")
	public void deleteComponentIntegrationConfig(
			@PathParam("componentId")
			@RequiredParam String componentId,
			@PathParam("configId") String configId)
	{
		ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
		integrationConfigExample.setComponentId(componentId);
		integrationConfigExample.setIntegrationConfigId(configId);
		ComponentIntegrationConfig integrationConfig = service.getPersistenceService().queryOneByExample(integrationConfigExample);

		if (integrationConfig != null) {
			service.getComponentService().deleteComponentIntegrationConfig(configId);
		}
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Runs a component integration config.")
	@Path("/{componentId}/integration/configs/{configId}/run")
	public Response runComponentIntegrationConfig(
			@PathParam("componentId")
			@RequiredParam String componentId,
			@PathParam("configId") String configId)
	{
		ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
		integrationConfigExample.setComponentId(componentId);
		integrationConfigExample.setIntegrationConfigId(configId);
		ComponentIntegrationConfig integrationConfig = service.getPersistenceService().queryOneByExample(integrationConfigExample);

		if (integrationConfig != null) {
			JobManager.runComponentIntegrationNow(componentId, configId);
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed"  desc="Private Utils">
	private void checkBaseComponentBelongsToComponent(BaseComponent component, String componentId)
	{
		if (component.getComponentId().equals(componentId) == false) {
			throw new OpenStorefrontRuntimeException("Entity does not belong to component", "Check input.");
		}
	}

	private Response checkComponentOwner(String componentId, String permission)
	{
		return checkComponentOwner(componentId, permission, false);
	}

	private Response checkComponentOwner(String componentId, String permission, boolean skipApproveCheck)
	{
		Response response;

		Component component = new Component();
		component.setComponentId(componentId);
		component = component.find();
		if (component != null) {
			response = ownerCheck(component, permission);
			if (response == null) {
				if (!SecurityUtil.hasPermission(permission)) {
					if (skipApproveCheck == false) {
						if (ApprovalStatus.APPROVED.equals(component.getApprovalState())) {
							response = Response.status(Response.Status.FORBIDDEN).build();
						}
					}
				}
			}
		} else {
			response = Response.status(Response.Status.NOT_FOUND).build();
		}
		return response;
	}

	// </editor-fold>
}
