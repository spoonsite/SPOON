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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.DashboardWidget;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.UserDashboard;
import edu.usu.sdl.openstorefront.core.model.Dashboard;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/userdashboard")
@APIDescription("Holds user dashboard information")
public class UserDashboardResource
	extends BaseResource
{

	@GET
	@APIDescription("Gets user dashboard for the current user")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Dashboard.class)
	public Response getUserdashboard()
	{
		Dashboard dashboard = service.getUserService().getDashboard(SecurityUtil.getCurrentUserName());
		return sendSingleEntityResponse(dashboard);
	}
	
	@PUT
	@APIDescription("Save user dashboard for the current user")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)	
	@DataType(Dashboard.class)
	@Path("/{dashboardId}")
	public Response saveUserdashboard(
			@PathParam("dashboardId") String dashboardId,
			Dashboard dashboard
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		
		UserDashboard userdashboard = new UserDashboard();
		userdashboard.setDashboardId(dashboardId);
		userdashboard = userdashboard.find();
		
		if (userdashboard != null)
		{
			response  = ownerCheck(userdashboard, SecurityPermission.ADMIN_SYSTEM_MANAGEMENT);
			if (response == null) {
				
				dashboard.getDashboard().setDashboardId(dashboardId);				
				dashboard.getDashboard().setUsername(userdashboard.getUsername());
				
				ValidationResult validationResult = dashboard.getDashboard().validate();
				for (DashboardWidget  widget : dashboard.getWidgets()) {
					validationResult.merge(widget.validate());					
				}
				
				if (validationResult.valid()) {
					dashboard = service.getUserService().saveDashboard(dashboard);
					response = sendSingleEntityResponse(dashboard);
				} else {
					response = sendSingleEntityResponse(validationResult.toRestError());
				}
			} 
		}
		return response;
	}
}
