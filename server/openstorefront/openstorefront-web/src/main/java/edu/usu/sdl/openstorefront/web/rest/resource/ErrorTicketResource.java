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
import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.storage.model.ErrorTicket;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.web.rest.model.ErrorTicketWrapper;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	@RequireAdmin
	@APIDescription("Gets all error tickets.  Always sorts by create date.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ErrorTicket.class)
	public Response getErrorTickets(@BeanParam FilterQueryParams filterQueryParams)
	{
		ErrorTicket errorTicketExample = new ErrorTicket();
		errorTicketExample.setActiveStatus(filterQueryParams.getStatus());

		QueryByExample<ErrorTicket> queryByExample = new QueryByExample(errorTicketExample);
		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());

		ErrorTicket errorTicketOrderExample = new ErrorTicket();
		errorTicketOrderExample.setCreateDts(QueryByExample.DATE_FLAG);
		queryByExample.setOrderBy(errorTicketOrderExample);
		queryByExample.setSortDirection(OpenStorefrontConstant.SORT_DESCENDING);

		List<ErrorTicket> errorTickets = service.getPersistenceService().queryByExample(ErrorTicket.class, queryByExample);
		long total = service.getPersistenceService().countClass(ErrorTicket.class);
		return sendSingleEnityResponse(new ErrorTicketWrapper(errorTickets, total));
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets an error ticket entity")
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{id}")
	public Response getErrorTicket(
			@PathParam("id")
			@RequiredParam String id)
	{
		ErrorTicket errorTicket = service.getPersistenceService().findById(ErrorTicket.class, id);
		return sendSingleEnityResponse(errorTicket);
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets an error ticket info")
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
		return sendSingleEnityResponse(ticketData);
	}
}
