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

import edu.usu.sdl.openstorefront.web.rest.model.RestListResponse;
import java.util.List;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * Base Resource for all other resources
 *
 * @author dshurtleff
 */
public abstract class BaseResource
{

	protected final edu.usu.sdl.openstorefront.service.ServiceProxy service = new edu.usu.sdl.openstorefront.service.ServiceProxy();

	protected Response sendSingleEnityResponse(Object entity)
	{
		if (entity == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			return Response.ok(entity).build();
		}
	}

	protected <T> RestListResponse<T> sendListResponse(List<GenericType<T>> data)
	{
		return sendListResponse(data, data.size());
	}

	protected <T> RestListResponse sendListResponse(List<GenericType<T>> data, long totalResults)
	{
		RestListResponse<T> restListResponse = new RestListResponse<>();
		restListResponse.setData(data);
		restListResponse.setResults(data.size());
		restListResponse.setTotalResults(totalResults);
		return restListResponse;
	}

}
