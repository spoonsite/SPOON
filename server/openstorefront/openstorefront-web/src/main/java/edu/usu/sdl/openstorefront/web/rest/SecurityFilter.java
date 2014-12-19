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
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
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

	private static final Logger log = Logger.getLogger(SecurityFilter.class.getName());

	@Context
	ResourceInfo resourceInfo;

	@Context
	HttpServletRequest httpServletRequest;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException
	{
		boolean doAdminCheck = true;
		RequireAdmin requireAdmin = resourceInfo.getResourceMethod().getAnnotation(RequireAdmin.class);
		try {
			doAdminCheck = requireAdmin.value().newInstance().requireAdminCheck(resourceInfo, requestContext);
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}

		if (doAdminCheck) {
			if (SecurityUtil.isAdminUser() == false) {
				requestContext.abortWith(Response
						.status(Response.Status.UNAUTHORIZED)
						.type(MediaType.TEXT_PLAIN)
						.entity("User cannot access the resource.")
						.build());
			} else {
				log.log(Level.INFO, SecurityUtil.adminAuditLogMessage(httpServletRequest));
			}
		}
	}

}
