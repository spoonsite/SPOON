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
package edu.usu.sdl.openstorefront.web.rest;

import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.web.rest.resource.UserProfileResource;
import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Handles the Required Admin check
 *
 * @author dshurtleff
 */
@Provider
@RequireAdmin
public class SecurityFilter
		implements ContainerRequestFilter
{

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException
	{
		boolean doAdminCheck = true;
		for (Object resource : requestContext.getUriInfo().getMatchedResources()) {
			if (resource instanceof UserProfileResource) {
				String useridPassIn = requestContext.getUriInfo().getPathParameters().getFirst("id");
				if (SecurityUtil.getCurrentUserName().equals(useridPassIn)) {
					doAdminCheck = false;
				}
			}
		}

		if (doAdminCheck) {
			if (SecurityUtil.isAdminUser() == false) {
				requestContext.abortWith(Response
						.status(Response.Status.UNAUTHORIZED)
						.type(MediaType.TEXT_PLAIN)
						.entity("User cannot access the resource.")
						.build());
			}
		}
	}

}
