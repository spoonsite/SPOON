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
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionResponseView;
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
@APIDescription("User question about a component")
public class ComponentQuestion
		extends BaseComponent<ComponentQuestion>
		implements OrganizationModel, LoggableModel<ComponentQuestion>
{

	@PK(generated = true)
	@NotNull
	private String questionId;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_QUESTION)
	@Sanitize(BasicHTMLSanitizer.class)
	private String question;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = UserTypeCode.class)
	@FK(UserTypeCode.class)
	private String userTypeCode;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_ORGANIZATION)
	@Sanitize(TextSanitizer.class)
	@FK(value = Organization.class, softReference = true, referencedField = "name")
	private String organization;

	public ComponentQuestion()
	{
	}

	@Override
	public String uniqueKey()
	{
		return getQuestion();
	}

	@Override
	protected void customKeyClear()
	{
		setQuestionId(null);
	}

	@Override
	public void updateFields(StandardEntity entity)
	{
		ComponentQuestion componentQuestion = (ComponentQuestion) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, componentQuestion);
		super.updateFields(entity);

		this.setOrganization(componentQuestion.getOrganization());
		this.setQuestion(componentQuestion.getQuestion());
		this.setUserTypeCode(componentQuestion.getUserTypeCode());

	}

	@Override
	public List<FieldChangeModel> findChanges(ComponentQuestion updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("questionId");
		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		return getQuestion();
	}

	public String getQuestion()
	{
		return question;
	}

	public void setQuestion(String question)
	{
		this.question = question;
	}

	public String getUserTypeCode()
	{
		return userTypeCode;
	}

	public void setUserTypeCode(String userType)
	{
		this.userTypeCode = userType;
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

	public String getQuestionId()
	{
		return questionId;
	}

	public void setQuestionId(String questionId)
	{
		this.questionId = questionId;
	}

	public void figureUpdateDts(List<ComponentQuestionResponseView> responseViews)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
