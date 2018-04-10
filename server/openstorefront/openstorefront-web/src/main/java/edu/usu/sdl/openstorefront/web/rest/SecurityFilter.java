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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import java.io.IOException;
import java.util.Set;
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
@RequireSecurity
public class SecurityFilter
		implements ContainerRequestFilter
{

	private static final Logger LOG = Logger.getLogger(SecurityFilter.class.getName());

	@Context
	ResourceInfo resourceInfo;

	@Context
	HttpServletRequest httpServletRequest;

	@Override
	@SuppressWarnings("squid:S1872")
	public void filter(ContainerRequestContext requestContext) throws IOException
	{
		boolean doAdminCheck = true;
		RequireSecurity requireSecurity = resourceInfo.getResourceMethod().getAnnotation(RequireSecurity.class);
		if (requireSecurity.specialCheck() != null) {
			try {
				doAdminCheck = requireSecurity.specialCheck().newInstance().specialSecurityCheck(resourceInfo, requestContext, requireSecurity);
			} catch (InstantiationException | IllegalAccessException ex) {
				throw new OpenStorefrontRuntimeException(ex);
			}
		}

		if (doAdminCheck) {
			boolean hasPermission = false;

			Set<String> userPermissions = SecurityUtil.getUserContext().permissions();
			Set<String> userRoles = SecurityUtil.getUserContext().roles();
			int matchPermissions = 0;
			for (String permissions : requireSecurity.value()) {
				if (userPermissions.contains(permissions)) {
					matchPermissions++;
				}
			}

			int matchRoles = 0;
			for (String role : requireSecurity.roles()) {
				if (userRoles.contains(role)) {
					matchRoles++;
				}
			}

			if (null == requireSecurity.logicOperator()) {
				throw new OpenStorefrontRuntimeException("Logic operation not supported.");
			} else {
				switch (requireSecurity.logicOperator()) {
					case OR:
						if (requireSecurity.value().length > 0 && matchPermissions > 0) {
							if (requireSecurity.roles().length > 0 && matchRoles > 0) {
								hasPermission = true;
							} else if (requireSecurity.roles().length == 0) {
								hasPermission = true;
							}
						} else if (requireSecurity.roles().length > 0 && matchRoles > 0) {
							if (requireSecurity.value().length == 0) {
								hasPermission = true;
							}
						} else if (requireSecurity.value().length == 0
								&& requireSecurity.roles().length == 0) {
							hasPermission = true;
						}
						break;
					case AND:
						if (requireSecurity.value().length == matchPermissions) {
							if (requireSecurity.roles().length == matchRoles) {
								hasPermission = true;
							} else if (requireSecurity.roles().length == 0) {
								hasPermission = true;
							}
						} else if (requireSecurity.roles().length == matchRoles) {
							if (requireSecurity.value().length == 0) {
								hasPermission = true;
							}
						} else if (requireSecurity.value().length == 0
								&& requireSecurity.roles().length == 0) {
							hasPermission = true;
						}
						break;
					default:
						throw new OpenStorefrontRuntimeException("Logic operation not supported.");
				}
			}

			if (hasPermission == false) {
				requestContext.abortWith(Response
						.status(Response.Status.UNAUTHORIZED)
						.type(MediaType.TEXT_PLAIN)
						.entity("User cannot access the resource.")
						.build());
			} else {
				LOG.log(Level.INFO, SecurityUtil.adminAuditLogMessage(httpServletRequest));
			}
		}
	}

}
