/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.entity.UserApprovalStatus;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

/**
 *
 * @author dshurtleff
 */
public class UserFilterParams
		extends FilterQueryParams
{

	@QueryParam("approvalState")
	@DefaultValue(UserApprovalStatus.APPROVED)
	@Size(min = 0, max = 3)
	@Sanitize(TextSanitizer.class)
	private String approvalState;

	@QueryParam("searchField")
	@Size(min = 0, max = 255)
	@Sanitize(TextSanitizer.class)
	private String searchField;

	@QueryParam("searchValue")
	@Size(min = 0, max = 255)
	private String searchValue;

	public String getApprovalState()
	{
		return approvalState;
	}

	public void setApprovalState(String approvalState)
	{
		this.approvalState = approvalState;
	}

	public String getSearchField()
	{
		return searchField;
	}

	public void setSearchField(String searchField)
	{
		this.searchField = searchField;
	}

	public String getSearchValue()
	{
		return searchValue;
	}

	public void setSearchValue(String searchValue)
	{
		this.searchValue = searchValue;
	}

}
