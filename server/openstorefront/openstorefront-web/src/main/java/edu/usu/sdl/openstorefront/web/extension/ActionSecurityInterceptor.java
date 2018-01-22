/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.web.extension;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.web.util.StripesContainerRequestContext;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

/**
 * Handles the security check
 *
 * @author dshurtleff
 */
@Intercepts({LifecycleStage.BindingAndValidation})
public class ActionSecurityInterceptor
		implements Interceptor
{

	private static final Logger LOG = Logger.getLogger(ActionSecurityInterceptor.class.getName());

	@Override
	public Resolution intercept(ExecutionContext context) throws Exception
	{
		RequireSecurity requireSecurity = context.getHandler().getAnnotation(RequireSecurity.class);
		if (requireSecurity != null) {
			boolean doAdminCheck = true;
			if (requireSecurity.specialCheck() != null) {
				try {
					ResourceInfo resourceInfo = new ResourceInfo()
					{
						@Override
						public Method getResourceMethod()
						{
							return context.getHandler();
						}

						@Override
						public Class<?> getResourceClass()
						{
							return context.getActionBean().getClass();
						}
					};

					ContainerRequestContext containerRequestContext = new StripesContainerRequestContext(context);
					doAdminCheck = requireSecurity.specialCheck().newInstance().specialSecurityCheck(resourceInfo, containerRequestContext, requireSecurity);
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
					return new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, "Access denied");
				} else {
					LOG.log(Level.INFO, SecurityUtil.adminAuditLogMessage(context.getActionBeanContext().getRequest()));
				}
			}
		}
		return context.proceed();
	}
}
