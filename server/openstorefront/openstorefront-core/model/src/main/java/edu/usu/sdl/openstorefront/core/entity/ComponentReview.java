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
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
@APIDescription("Holds a review for a component")
public class ComponentReview
		extends BaseComponent<ComponentReview>
		implements OrganizationModel, LoggableModel<ComponentReview>
{

	@PK(generated = true)
	@NotNull
	private String componentReviewId;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = UserTypeCode.class)
	@FK(UserTypeCode.class)
	private String userTypeCode;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_REVIEW_COMMENT)
	@Sanitize(BasicHTMLSanitizer.class)
	private String comment;

	@NotNull
	@Min(0)
	@Max(5)
	@ConsumeField
	private Integer rating;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@ConsumeField
	@Sanitize(TextSanitizer.class)
	private String title;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = ExperienceTimeType.class)
	@FK(ExperienceTimeType.class)
	private String userTimeCode;

	@NotNull
	@ConsumeField
	private Date lastUsed;

	@NotNull
	@ConsumeField
	private Boolean recommend;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_ORGANIZATION)
	@ConsumeField
	@Sanitize(TextSanitizer.class)
	@FK(value = Organization.class, softReference = true, referencedField = "name")
	private String organization;

	public ComponentReview()
	{
	}

	@Override
	public String uniqueKey()
	{
		return getTitle() + OpenStorefrontConstant.GENERAL_KEY_SEPARATOR + getCreateUser();
	}

	@Override
	protected void customKeyClear()
	{
		setComponentReviewId(null);
	}

	@Override
	public void updateFields(StandardEntity entity)
	{
		ComponentReview review = (ComponentReview) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, review);
		super.updateFields(entity);

		this.setComment(review.getComment());
		this.setUserTimeCode(review.getUserTimeCode());
		this.setLastUsed(review.getLastUsed());
		this.setOrganization(review.getOrganization());
		this.setRating(review.getRating());
		this.setRecommend(review.getRecommend());
		this.setTitle(review.getTitle());
		this.setUserTypeCode(review.getUserTypeCode());

	}

	@Override
	public List<FieldChangeModel> findChanges(ComponentReview updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("componentReviewId");
		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		return getTitle();
	}

	public String getComponentReviewId()
	{
		return componentReviewId;
	}

	public void setComponentReviewId(String componentReviewId)
	{
		this.componentReviewId = componentReviewId;
	}

	public String getUserTypeCode()
	{
		return userTypeCode;
	}

	public void setUserTypeCode(String userTypeCode)
	{
		this.userTypeCode = userTypeCode;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public Integer getRating()
	{
		return rating;
	}

	public void setRating(Integer rating)
	{
		this.rating = rating;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getUserTimeCode()
	{
		return userTimeCode;
	}

	public void setUserTimeCode(String userTimeCode)
	{
		this.userTimeCode = userTimeCode;
	}

	public Boolean getRecommend()
	{
		return recommend;
	}

	public void setRecommend(Boolean recommend)
	{
		this.recommend = recommend;
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

	public Date getLastUsed()
	{
		return lastUsed;
	}

	public void setLastUsed(Date lastUsed)
	{
		this.lastUsed = lastUsed;
	}

}
