/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.FeedbackTicket;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.view.FeedbackTicketWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sourceforge.stripes.util.bean.BeanUtil;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/feedbacktickets")
@APIDescription("Handles user feedback")
public class FeedbackResource
		extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_FEEDBACK_READ)
	@APIDescription("Gets all error tickets.  Always sorts by create date.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(FeedbackTicketWrapper.class)
	public Response getFeedbackTickets(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		FeedbackTicket feedbackTicketExample = new FeedbackTicket();
		feedbackTicketExample.setActiveStatus(filterQueryParams.getStatus());

		FeedbackTicket ticketStartExample = new FeedbackTicket();
		ticketStartExample.setUpdateDts(filterQueryParams.getStart());

		FeedbackTicket ticketEndExample = new FeedbackTicket();
		ticketEndExample.setUpdateDts(filterQueryParams.getEnd());

		QueryByExample<FeedbackTicket> queryByExample = new QueryByExample(feedbackTicketExample);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(ticketStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(ticketEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());

		queryByExample.setFirstResult(filterQueryParams.getOffset());
		queryByExample.setSortDirection(filterQueryParams.getSortOrder());

		FeedbackTicket ticketSortExample = new FeedbackTicket();
		Field sortField = ReflectionUtil.getField(ticketSortExample, filterQueryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), ticketSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(ticketSortExample);
		}

		List<FeedbackTicket> tickets = service.getPersistenceService().queryByExample(queryByExample);
		long total = service.getPersistenceService().countByExample(queryByExample);
		return sendSingleEntityResponse(new FeedbackTicketWrapper(tickets, total));
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_FEEDBACK_READ)
	@APIDescription("Gets a feedback ticket entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(FeedbackTicket.class)
	@Path("/{feedbackId}")
	public Response getFeedbackTicket(
			@PathParam("feedbackId")
			@RequiredParam String id
	)
	{
		FeedbackTicket ticket = service.getPersistenceService().findById(FeedbackTicket.class, id);
		return sendSingleEntityResponse(ticket);
	}

	@POST
	@APIDescription("Submit Feedback Ticket")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(FeedbackTicket.class)
	public Response submitTicket(
			FeedbackTicket feedbackTicket
	)
	{
		ValidationResult validationResult = feedbackTicket.validate(true);
		if (validationResult.valid()) {
			feedbackTicket = service.getFeedbackService().submitFeedback(feedbackTicket);
			return Response.created(URI.create("v1/resource/feedbacktickets/" + feedbackTicket.getFeedbackId())).entity(feedbackTicket).build();
		} else {
			return sendSingleEntityResponse(validationResult.toRestError());
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_FEEDBACK_UPDATE)
	@APIDescription("Marks a feedback ticket complete")
	@Path("/{feedbackId}/markcomplete")
	public Response markComplete(
			@PathParam("feedbackId")
			@RequiredParam String id
	)
	{
		return markTicket(id, true);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_FEEDBACK_UPDATE)
	@APIDescription("Marks a feedback ticket outstanding")
	@Path("/{feedbackId}/markoutstanding")
	public Response markOutstanding(
			@PathParam("feedbackId")
			@RequiredParam String id
	)
	{
		return markTicket(id, false);
	}

	private Response markTicket(String id, boolean complete)
	{
		FeedbackTicket feedbackTicket = new FeedbackTicket();
		feedbackTicket.setFeedbackId(id);
		feedbackTicket = feedbackTicket.find();
		if (feedbackTicket != null) {
			if (complete) {
				feedbackTicket = service.getFeedbackService().markAsComplete(id);
			} else {
				feedbackTicket = service.getFeedbackService().markAsOutstanding(id);
			}
		}
		return sendSingleEntityResponse(feedbackTicket);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_FEEDBACK_DELETE)
	@APIDescription("Deletes a feedback ticket")
	@Path("/{feedbackId}")
	public Response deleteFeedbackTicket(
			@PathParam("feedbackId")
			@RequiredParam String id
	)
	{
		service.getFeedbackService().deleteFeedback(id);
		return Response.ok().build();
	}

}
