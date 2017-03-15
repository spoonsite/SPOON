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

import edu.usu.sdl.openstorefront.core.entity.SecurityPolicy;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
@Provider
public class CustomHeaderFilter implements ContainerResponseFilter
{

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext response)
	{
		ServiceProxy service = ServiceProxy.getProxy();
		SecurityPolicy securityPolicy = service.getSecurityService().getSecurityPolicy();
		if (StringUtils.isNotBlank(securityPolicy.getCorsOrigins())) {
			response.getHeaders().putSingle("Access-Control-Allow-Origin", securityPolicy.getCorsOrigins());
		}
		if (StringUtils.isNotBlank(securityPolicy.getCorsMethods())) {
			response.getHeaders().putSingle("Access-Control-Allow-Methods", securityPolicy.getCorsMethods());
		}
		if (StringUtils.isNotBlank(securityPolicy.getCorsHeaders())) {
			response.getHeaders().putSingle("Access-Control-Allow-Headers", securityPolicy.getCorsHeaders());
		}
		if (StringUtils.isNotBlank(securityPolicy.getCustomHeaders())) {
			String headers[] = securityPolicy.getCustomHeaders().split("\n");
			for (String header : headers) {
				String headerParts[] = header.split(":");
				if (headerParts.length > 1) {
					response.getHeaders().putSingle(headerParts[0], headerParts[1]);
				}
			}
		}
	}
	
}
