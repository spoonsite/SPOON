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

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentContact;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSchedule;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.storage.model.ComponentMetadata;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestion;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.storage.model.ComponentResource;
import edu.usu.sdl.openstorefront.storage.model.ComponentReview;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewCon;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewPro;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import edu.usu.sdl.openstorefront.storage.model.ComponentTracking;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentDetailView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentView;
import edu.usu.sdl.openstorefront.web.rest.model.RestListResponse;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/components")
@APIDescription("Components are the central resource of the system.  The majority of the listing are components.")
public class Component
		extends BaseResource
{
	@GET
	@APIDescription("Get a list of components <br>(Note: this only the top level component object, See Componet Detail for composite resource.)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentView.class)
	public RestListResponse getComponents()
	{
		List<ComponentView> componentViews = service.getComponentService().getComponents();
		return sendListResponse(componentViews);
	}

	@GET
	@APIDescription("Gets a component <br>(Note: this only the top level component object)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentView.class)
	@Path("/{id}")
	public ComponentView getComponentSingle(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		ComponentView componentView = service.getComponentService().getComponent(componentId);
		return componentView;
	}

	@GET
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{id}/detail")
	public ComponentDetailView getComponentDetails(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		ComponentDetailView componentDetail = service.getComponentService().getComponentDetails(componentId);
		return componentDetail;
	}
	
	@POST
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/attribute/")
	public ComponentAttribute addComponentAttribute(
			@PathParam("id")
			@RequiredParam ComponentAttribute attribute)
	{
		service.getComponentService().saveComponentAttribute(attribute);
		return attribute;
	}
	
	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/attribute")
	public ComponentAttribute updateComponentAttribute(
			@PathParam("id")
			@RequiredParam ComponentAttribute attribute)
	{
		service.getComponentService().saveComponentAttribute(attribute);
		return attribute;
	}
	
	@POST
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/contact")
	public ComponentContact addComponentContact(
			@PathParam("id")
			@RequiredParam ComponentContact contact)
	{
		service.getComponentService().saveComponentContact(contact);
		return contact;
	}
	
	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/contact")
	public ComponentContact updateComponentContact(
			@PathParam("id")
			@RequiredParam ComponentContact contact)
	{
		service.getComponentService().saveComponentContact(contact);
		return contact;
	}
	
	@POST
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/section")
	public ComponentEvaluationSection addComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam ComponentEvaluationSection section)
	{
		service.getComponentService().saveComponentEvaluationSection(section);
		return section;
	}
	
	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/section")
	public ComponentEvaluationSection updateComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam ComponentEvaluationSection section)
	{
		service.getComponentService().saveComponentEvaluationSection(section);
		return section;
	}
	
	@POST
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/schedule")
	public ComponentEvaluationSchedule addComponentEvaluationSchedule(
			@PathParam("id")
			@RequiredParam ComponentEvaluationSchedule schedule)
	{
		service.getComponentService().saveComponentEvaluationSchedule(schedule);
		return schedule;
	}
	
	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/schedule")
	public ComponentEvaluationSchedule updateComponentEvaluationSchedule(
			@PathParam("id")
			@RequiredParam ComponentEvaluationSchedule schedule)
	{
		service.getComponentService().saveComponentEvaluationSchedule(schedule);
		return schedule;
	}
	
	@POST
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/media")
	public ComponentMedia addComponentMedia(
			@PathParam("id")
			@RequiredParam ComponentMedia media)
	{
		service.getComponentService().saveComponentMedia(media);
		return media;
	}
	
	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/media")
	public ComponentMedia updateComponentMedia(
			@PathParam("id")
			@RequiredParam ComponentMedia media)
	{
		service.getComponentService().saveComponentMedia(media);
		return media;
	}
	
	@POST
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/metadata")
	public ComponentMetadata addComponentMetadata(
			@PathParam("id")
			@RequiredParam ComponentMetadata metadata)
	{
		service.getComponentService().saveComponentMetadata(metadata);
		return metadata;
	}
	
	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/metadata")
	public ComponentMetadata updateComponentMetadata(
			@PathParam("id")
			@RequiredParam ComponentMetadata metadata)
	{
		service.getComponentService().saveComponentMetadata(metadata);
		return metadata;
	}
	
	@POST
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/question")
	public ComponentQuestion addComponentQuestion(
			@PathParam("id")
			@RequiredParam ComponentQuestion question)
	{
		service.getComponentService().saveComponentQuestion(question);
		return question;
	}
	
	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/question")
	public ComponentQuestion updateComponentQuestion(
			@PathParam("id")
			@RequiredParam ComponentQuestion question)
	{
		service.getComponentService().saveComponentQuestion(question);
		return question;
	}
	
	@POST
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/response")
	public ComponentQuestionResponse addComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam ComponentQuestionResponse response)
	{
		service.getComponentService().saveComponentQuestionResponse(response);
		return response;
	}
	
	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/response")
	public ComponentQuestionResponse updateComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam ComponentQuestionResponse response)
	{
		service.getComponentService().saveComponentQuestionResponse(response);
		return response;
	}
	
	@POST
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/resource")
	public ComponentResource addComponentResource(
			@PathParam("id")
			@RequiredParam ComponentResource resource)
	{
		service.getComponentService().saveComponentResource(resource);
		return resource;
	}
	
	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/resource")
	public ComponentResource updateComponentResource(
			@PathParam("id")
			@RequiredParam ComponentResource resource)
	{
		service.getComponentService().saveComponentResource(resource);
		return resource;
	}
	
	@POST
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/review")
	public ComponentReview addComponentReview(
			@PathParam("id")
			@RequiredParam ComponentReview review)
	{
		service.getComponentService().saveComponentReview(review);
		return review;
	}
	
	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/review")
	public ComponentReview updateComponentReview(
			@PathParam("id")
			@RequiredParam ComponentReview review)
	{
		service.getComponentService().saveComponentReview(review);
		return review;
	}
	
	@POST
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/con")
	public ComponentReviewCon addComponentReviewCon(
			@PathParam("id")
			@RequiredParam ComponentReviewCon con)
	{
		service.getComponentService().saveComponentReviewCon(con);
		return con;
	}
	
	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/con")
	public ComponentReviewCon updateComponentReviewCon(
			@PathParam("id")
			@RequiredParam ComponentReviewCon con)
	{
		service.getComponentService().saveComponentReviewCon(con);
		return con;
	}
	
	@POST
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/pro")
	public ComponentReviewPro addComponentReviewPro(
			@PathParam("id")
			@RequiredParam ComponentReviewPro pro)
	{
		service.getComponentService().saveComponentReviewPro(pro);
		return pro;
	}
	
	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/pro")
	public ComponentReviewPro updateComponentReviewPro(
			@PathParam("id")
			@RequiredParam ComponentReviewPro pro)
	{
		service.getComponentService().saveComponentReviewPro(pro);
		return pro;
	}
	
	@POST
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/tag")
	public ComponentTag addComponentTag(
			@PathParam("id")
			@RequiredParam ComponentTag tag)
	{
		service.getComponentService().saveComponentTag(tag);
		return tag;
	}
	
	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/tag")
	public ComponentTag updateComponentTag(
			@PathParam("id")
			@RequiredParam ComponentTag tag)
	{
		service.getComponentService().saveComponentTag(tag);
		return tag;
	}
	
	@POST
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/tracking")
	public ComponentTracking addComponentTracking(
			@PathParam("id")
			@RequiredParam ComponentTracking tracking)
	{
		service.getComponentService().saveComponentTracking(tracking);
		return tracking;
	}
	
	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/detail/tracking")
	public ComponentTracking updateComponentTracking(
			@PathParam("id")
			@RequiredParam ComponentTracking tracking)
	{
		service.getComponentService().saveComponentTracking(tracking);
		return tracking;
	}

}
