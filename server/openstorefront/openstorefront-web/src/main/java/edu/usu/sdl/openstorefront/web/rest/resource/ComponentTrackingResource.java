/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import au.com.bytecode.opencsv.CSVWriter;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.view.ComponentTrackingCompleteWrapper;
import edu.usu.sdl.openstorefront.core.view.ComponentTrackingResult;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.StringWriter;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 * ComponentTrackingResource Resource
 *
 * @author dshurtleff
 * @author jlaw
 */
@Path("v1/resource/componenttracking")
@APIDescription("Track component data.")
public class ComponentTrackingResource
		extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING)
	@APIDescription("Get the list of tracking details on a specified component passing in a filter.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTrackingResult.class)
	public Response getActiveComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentTrackingResult result = service.getComponentService().getComponentTracking(filterQueryParams, componentId);
		return sendSingleEntityResponse(result);
	}

	@GET
	@APIDescription("Exports component tracking information in csv formt (Requires Admin)")
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING)
	@Produces("text/csv")
	@Path("/export")
	public Response exportEntityValues(
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		StringBuilder data = new StringBuilder();
		ComponentTrackingResult result = service.getComponentService().getComponentTracking(filterQueryParams, null);

		StringWriter stringWriter = new StringWriter();
		CSVWriter writer = new CSVWriter(stringWriter);
		writer.writeNext(new String[]{"Name",
			"Component Type",
			"Component ID",
			"Tracking ID",
			"Create Date",
			"Client IP",
			"Event",
			"Resource Link",
			"Resource Type",
			"Restricted Resource",
			"Create User"
		});
		data.append(stringWriter.toString());

		for (ComponentTrackingCompleteWrapper wrapper : result.getResult()) {
			data.append(wrapper.export());
		}

		ResponseBuilder response = Response.ok(data.toString());
		response.header("Content-Type", "application/csv");
		response.header("Content-Disposition", "attachment; filename=\"componentTrackingExport.csv\"");
		return response.build();
	}
}
