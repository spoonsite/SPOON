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
package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
public class ComponentQuestionResponse
	extends BaseComponent
{

	@PK
	@NotNull
	private String responseId;

	@NotNull
	@ConsumeField
	private String questionId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_RESPONSE)
	@ConsumeField
	private String response;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ConsumeField
	private String userTypeCode;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_ORGANIZATION)
	@ConsumeField
	private String organization;

	public ComponentQuestionResponse()
	{
	}

	@Override
	public void setPrimaryKey(String itemId, String itemCode, String componentId)
	{
		responseId = itemId;
	}

	@Override
	public void setPrimaryKey(Object pk)
	{
		responseId = (String)pk;
	}

	@Override
	public Object getPrimaryKey()
	{
		return responseId;
	}
	
	public String getResponseId()
	{
		return responseId;
	}

	public void setResponseId(String responseId)
	{
		this.responseId = responseId;
	}

	public String getQuestionId()
	{
		return questionId;
	}

	public void setQuestionId(String questionId)
	{
		this.questionId = questionId;
	}

	public String getResponse()
	{
		return response;
	}

	public void setResponse(String response)
	{
		this.response = response;
	}

	public String getUserTypeCode()
	{
		return userTypeCode;
	}

	public void setUserTypeCode(String userTypeCode)
	{
		this.userTypeCode = userTypeCode;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

}
