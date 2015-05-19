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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import javax.ws.rs.QueryParam;

/**
 *
 * @author dshurtleff
 */
public class ComponentFilterParams
		extends FilterQueryParams
{

	@QueryParam("approvalState")
	private String approvalState;

	public static ComponentFilterParams defaultFilter()
	{
		ComponentFilterParams filterQueryParams = new ComponentFilterParams();
		filterQueryParams.setMax(Integer.MAX_VALUE);
		filterQueryParams.setOffset(0);
		filterQueryParams.setStatus(BaseEntity.ACTIVE_STATUS);
		filterQueryParams.setAll(false);
		filterQueryParams.setSortField("description");
		filterQueryParams.setSortOrder(OpenStorefrontConstant.SORT_DESCENDING);
		return filterQueryParams;
	}

	public ComponentFilterParams()
	{
	}

	public String getApprovalState()
	{
		return approvalState;
	}

	public void setApprovalState(String approvalState)
	{
		this.approvalState = approvalState;
	}

}
