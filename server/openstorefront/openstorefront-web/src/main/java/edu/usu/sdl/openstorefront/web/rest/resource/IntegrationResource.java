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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.view.GlobalIntegrationModel;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.net.URI;
import java.text.ParseException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.redhogs.cronparser.CronExpressionDescriptor;

/**
 *
 * @author jlaw
 */
@Path("v1/resource/integrations")
@APIDescription("Integration models are resources used to connect to external sources.")
public class IntegrationResource
		extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Gets the global integration model from the database.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(GlobalIntegrationModel.class)
	@Path("/global")
	public Response getGlobalConfig()
	{
		GlobalIntegrationModel model = service.getSystemService().getGlobalIntegrationConfig();
		try {
			model.setCronExpressionDescription(CronExpressionDescriptor.getDescription(model.getJiraRefreshRate()));
		} catch (ParseException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		return sendSingleEntityResponse(model);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Saves a global integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/global")
	public Response updateGlobalConfig(
			@RequiredParam GlobalIntegrationModel integration
	)
	{
		ValidationModel validationModel = new ValidationModel(integration);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getSystemService().saveGlobalIntegrationConfig(integration);
			return Response.created(URI.create("v1/resource/integrations/global")).entity(integration).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

}
