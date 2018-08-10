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

import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.ErrorTicket;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.view.ErrorTicketWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.MultipleIds;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.lang.reflect.Field;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
@Path("v1/resource/errortickets")
@APIDescription("Allows for retrieval of error tickets")
public class ErrorTicketResource
		extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT_ERROR_TICKET)
	@APIDescription("Gets all error tickets.  Always sorts by create date.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ErrorTicketWrapper.class)
	public Response getErrorTickets(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ErrorTicket errorTicketExample = new ErrorTicket();
		errorTicketExample.setActiveStatus(filterQueryParams.getStatus());

		ErrorTicket errorTicketStartExample = new ErrorTicket();
		errorTicketStartExample.setUpdateDts(filterQueryParams.getStart());

		ErrorTicket errorTicketEndExample = new ErrorTicket();
		errorTicketEndExample.setUpdateDts(filterQueryParams.getEnd());

		QueryByExample<ErrorTicket> queryByExample = new QueryByExample(errorTicketExample);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(errorTicketStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(errorTicketEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());

		queryByExample.setFirstResult(filterQueryParams.getOffset());
		queryByExample.setSortDirection(filterQueryParams.getSortOrder());

		ErrorTicket errorTicketSortExample = new ErrorTicket();
		Field sortField = ReflectionUtil.getField(errorTicketSortExample, filterQueryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), errorTicketSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(errorTicketSortExample);
		}

		List<ErrorTicket> errorTickets = service.getPersistenceService().queryByExample(queryByExample);
		long total = service.getPersistenceService().countByExample(queryByExample);
		return sendSingleEntityResponse(new ErrorTicketWrapper(errorTickets, total));
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT_ERROR_TICKET)
	@APIDescription("Gets an error ticket entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ErrorTicket.class)
	@Path("/{id}")
	public Response getErrorTicket(
			@PathParam("id")
			@RequiredParam String id)
	{
		ErrorTicket errorTicket = service.getPersistenceService().findById(ErrorTicket.class, id);
		return sendSingleEntityResponse(errorTicket);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT_ERROR_TICKET)
	@APIDescription("Gets error ticket info")
	@Produces({MediaType.WILDCARD})
	@Path("/{id}/ticket")
	public Response getErrorTicketInfo(
			@PathParam("id")
			@RequiredParam String id)
	{
		String ticketData = service.getSystemService().errorTicketInfo(id);
		if (ticketData != null) {
			ticketData = ticketData.replace("\n", "<br>");
			ticketData = ticketData.replace("edu.usu.sdl.openstorefront", "<span style='background: yellow;'>edu.usu.sdl.openstorefront</span>");
		}
		return sendSingleEntityResponse(ticketData);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT_ERROR_TICKET)
	@APIDescription("Deletes error tickets")
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(MultipleIds.class)
	public void deleteErrorTickets(
			@RequiredParam MultipleIds multipleIds)
	{
		service.getSystemService().deleteErrorTickets(multipleIds.getIds());
	}

}
