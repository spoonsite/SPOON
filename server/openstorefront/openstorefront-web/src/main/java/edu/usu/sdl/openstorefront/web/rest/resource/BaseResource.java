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

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Base Resource for all other resources
 *
 * @author dshurtleff
 */
public abstract class BaseResource
{

	protected final Service service = ServiceProxy.getProxy();

	protected final FilterEngine filterEngine = new FilterEngine();

	protected Response sendSingleEntityResponse(Object entity)
	{
		return sendSingleEntityResponse(entity, Response.Status.NOT_FOUND);
	}

	protected Response sendSingleEntityResponse(Object entity, Response.StatusType status)
	{
		return sendSingleEnityResponse(entity, Response.Status.NOT_FOUND);
	}

	protected Response sendSingleEnityResponse(Object entity, Response.StatusType status)
	{
		if (entity == null) {
			return Response.status(status).build();
		} else {
			return Response.ok(entity).build();
		}
	}

	protected Response ownerCheck(StandardEntity entity, String permission)
	{
		if (SecurityUtil.isCurrentUserTheOwner(entity)
				|| SecurityUtil.hasPermission(permission)) {
			return null;
		} else {
			Response response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN)
					.entity("User cannot modify resource.")
					.build();

			//Coming from component check
			if (entity instanceof Component) {
				response = checkEvaluator((Component) entity);
			}
			return response;
		}
	}

	private Response checkEvaluator(Component component)
	{
		//Evaluator should be able to modify change requests on evaluations
		if (component.getPendingChangeId() != null
				&& SecurityUtil.hasPermission(SecurityPermission.USER_EVALUATIONS_UPDATE)) {
			return null;
		} else {
			return Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN)
					.entity("User cannot modify resource.")
					.build();
		}
	}

}
