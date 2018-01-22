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

import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.UserWatch;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.MultipleIds;
import edu.usu.sdl.openstorefront.core.view.UserWatchView;
import edu.usu.sdl.openstorefront.core.view.UserWatchWrapper;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/userwatches")
@APIDescription("Watches on entries")
public class UserWatchResource
		extends BaseResource
{

	@GET
	@APIDescription("Get a list of user watches")
	@RequireSecurity(SecurityPermission.ADMIN_WATCHES)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserWatchWrapper.class)
	public Response getWatches(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		UserWatch userWatchExample = new UserWatch();
		if (Convert.toBoolean(filterQueryParams.getAll()) == false) {
			userWatchExample.setActiveStatus(filterQueryParams.getStatus());
		}

		UserWatch userWatchStartExample = new UserWatch();
		userWatchStartExample.setCreateDts(filterQueryParams.getStart());

		UserWatch userProfileEndExample = new UserWatch();
		userProfileEndExample.setCreateDts(filterQueryParams.getEnd());

		QueryByExample queryByExample = new QueryByExample(userWatchExample);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(userWatchStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(userProfileEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		List<UserWatch> userWatches = service.getPersistenceService().queryByExample(queryByExample);

		UserWatchWrapper userWatchWrapper = new UserWatchWrapper();
		userWatchWrapper.getData().addAll(UserWatchView.toViewList(userWatches));
		filterQueryParams.filter(userWatchWrapper.getData());

		userWatchWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));

		return sendSingleEntityResponse(userWatchWrapper);
	}

	@PUT
	@APIDescription("Activates a set of watches")
	@RequireSecurity(SecurityPermission.ADMIN_WATCHES)
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/activate")
	public void activateWatches(
			MultipleIds mulitpleIds
	)
	{
		if (mulitpleIds != null) {
			for (String watchId : mulitpleIds.getIds()) {
				UserWatch userWatch = new UserWatch();
				userWatch.setUserWatchId(watchId);
				userWatch = (UserWatch) userWatch.find();
				if (userWatch != null) {
					userWatch.setActiveStatus(UserWatch.ACTIVE_STATUS);
					service.getUserService().saveWatch(userWatch);
				}
			}
		}
	}

	@PUT
	@APIDescription("Inactivates a set of watches")
	@RequireSecurity(SecurityPermission.ADMIN_WATCHES)
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/inactivate")
	public void inactivateWatches(
			MultipleIds mulitpleIds
	)
	{
		if (mulitpleIds != null) {
			for (String watchId : mulitpleIds.getIds()) {
				UserWatch userWatch = new UserWatch();
				userWatch.setUserWatchId(watchId);
				userWatch = (UserWatch) userWatch.find();
				if (userWatch != null) {
					userWatch.setActiveStatus(UserWatch.INACTIVE_STATUS);
					service.getUserService().saveWatch(userWatch);
				}
			}
		}
	}

}
