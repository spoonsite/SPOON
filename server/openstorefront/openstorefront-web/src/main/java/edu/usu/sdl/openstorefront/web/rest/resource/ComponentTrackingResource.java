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

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.service.query.QueryType;
import edu.usu.sdl.openstorefront.storage.model.ComponentTracking;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentTrackingResult;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentTrackingWrapper;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import edu.usu.sdl.openstorefront.web.viewmodel.RestErrorModel;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * ComponentTrackingResource Resource
 *
 * @author dshurtleff
 * @author jlaw
 */
@Path("v1/resource/componenttracking")
@APIDescription("Track component data.")
public class ComponentTrackingResource
		extends BaseResource
{
	@GET
	@RequireAdmin
	@APIDescription("Get the list of tracking details on a specified component. Always sorts by create date.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTrackingWrapper.class)
	public Response getActiveComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

//		ComponentTracking trackingExample = new ComponentTracking();
//		if (!filterQueryParams.getAll()) {
//			trackingExample.setActiveStatus(filterQueryParams.getStatus());
//		}
//
//		QueryByExample<ComponentTracking> queryByExample = new QueryByExample(trackingExample);
//		queryByExample.setMaxResults(filterQueryParams.getMax());
//		queryByExample.setFirstResult(filterQueryParams.getOffset());
//
//		ComponentTracking trackingOrderExample = new ComponentTracking();
//		trackingOrderExample.setCreateDts(QueryByExample.DATE_FLAG);
//		queryByExample.setOrderBy(trackingOrderExample);
//		queryByExample.setSortDirection(OpenStorefrontConstant.SORT_DESCENDING);
//
//		List<ComponentTracking> componentTrackings = service.getPersistenceService().queryByExample(ComponentTracking.class, queryByExample);
//
//		int index = 0;
//
//		for(ComponentTracking temp : componentTrackings){
//			if (temp.getEventDts().compareTo(filterQueryParams.getEnd()) < 0) {
//			index++;
//		}
//		}
//		List<ComponentTracking> result = componentTrackings.subList(0, index);
//		
//		long total = service.getPersistenceService().countByExample(new QueryByExample(QueryType.COUNT, trackingExample));
		ComponentTrackingResult result = service.getComponentService().getComponentTracking(filterQueryParams, componentId);
		return sendSingleEntityResponse(result);
	}
}
