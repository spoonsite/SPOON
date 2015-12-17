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
import edu.usu.sdl.openstorefront.core.view.ArticleTrackingCompleteWrapper;
import edu.usu.sdl.openstorefront.core.view.ArticleTrackingResult;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.doc.security.RequireAdmin;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.StringWriter;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
@Path("v1/resource/articletracking")
@APIDescription("Track article data.")
public class ArticleTrackingResource
		extends BaseResource
{

	@GET
	@RequireAdmin
	@APIDescription("Get the list of tracking details on a specified article passing in a filter.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ArticleTrackingResult.class)
	public Response getActiveArticleTracking(
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ArticleTrackingResult result = service.getAttributeService().getAttributeTracking(filterQueryParams, null);
		return sendSingleEntityResponse(result);
	}

	@GET
	@APIDescription("Exports article tracking information in csv formt (Requires Admin)")
	@RequireAdmin
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
		ArticleTrackingResult result = service.getAttributeService().getAttributeTracking(filterQueryParams, null);

		StringWriter stringWriter = new StringWriter();
		CSVWriter writer = new CSVWriter(stringWriter);
		writer.writeNext(new String[]{"Title",
									  "Attribute Type",
									  "Attribute Code",
									  "Create Date",
									  "Event",
									  "Tracking ID",
									  "Create User",
									  "Client IP"
		});
		data.append(stringWriter.toString());

		for (ArticleTrackingCompleteWrapper wrapper : result.getResult()) {
			data.append(wrapper.export());
		}

		ResponseBuilder response = Response.ok(data.toString());
		response.header("Content-Disposition", "attachment; filename=\"articleTrackingExport.csv\"");
		return response.build();
	}

}
