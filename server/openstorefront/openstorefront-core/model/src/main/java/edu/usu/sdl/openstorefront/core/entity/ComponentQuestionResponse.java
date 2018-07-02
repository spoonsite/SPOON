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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.model.FieldChangeModel;
import edu.usu.sdl.openstorefront.validation.BasicHTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds question responses")
public class ComponentQuestionResponse
		extends BaseComponent<ComponentQuestionResponse>
		implements OrganizationModel, LoggableModel<ComponentQuestionResponse>
{

	@PK(generated = true)
	@NotNull
	private String responseId;

	@NotNull
	@ConsumeField
	private String questionId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_RESPONSE)
	@ConsumeField
	@Sanitize(BasicHTMLSanitizer.class)
	private String response;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = UserTypeCode.class)
	@FK(UserTypeCode.class)
	private String userTypeCode;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_ORGANIZATION)
	@ConsumeField
	@Sanitize(TextSanitizer.class)
	@FK(value = Organization.class, softReference = true, referencedField = "name")
	private String organization;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ComponentQuestionResponse()
	{
	}

	@Override
	public String uniqueKey()
	{
		return getQuestionId() + OpenStorefrontConstant.GENERAL_KEY_SEPARATOR + getResponse();
	}

	@Override
	protected void customKeyClear()
	{
		setResponseId(null);
	}

	@Override
	public void updateFields(StandardEntity entity)
	{
		ComponentQuestionResponse questionResponse = (ComponentQuestionResponse) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, questionResponse);
		super.updateFields(entity);

		this.setOrganization(questionResponse.getOrganization());
		this.setQuestionId(questionResponse.getQuestionId());
		this.setResponse(questionResponse.getResponse());
		this.setUserTypeCode(questionResponse.getUserTypeCode());

	}

	@Override
	public List<FieldChangeModel> findChanges(ComponentQuestionResponse updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("responseId");
		excludeFields.add("questionId");
		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		return getResponse();
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

	@Override
	public String getOrganization()
	{
		return organization;
	}

	@Override
	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

}
