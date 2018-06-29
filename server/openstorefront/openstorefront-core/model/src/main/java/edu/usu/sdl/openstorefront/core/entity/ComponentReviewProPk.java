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

import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Primary Key")
@Embeddable
public class ComponentReviewProPk
		extends BasePK<ComponentReviewProPk>
{

	public static final String FIELD_REVIEW_PRO = "reviewPro";

	@NotNull
	@FK(ComponentReview.class)
	private String componentReviewId;

	@NotNull
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = ReviewPro.class)
	@FK(ReviewPro.class)
	private String reviewPro;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ComponentReviewProPk()
	{
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || (obj instanceof ComponentReviewProPk == false)) {
			return false;
		}
		ComponentReviewProPk compareObj = (ComponentReviewProPk) obj;
		return pkValue().equals(compareObj.pkValue());
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 59 * hash + Objects.hashCode(getComponentReviewId());
		hash = 59 * hash + Objects.hashCode(getReviewPro());
		return hash;
	}

	@Override
	public String pkValue()
	{
		return getComponentReviewId() + ReflectionUtil.COMPOSITE_KEY_SEPERATOR + getReviewPro();
	}

	@Override
	public String toString()
	{
		return pkValue();
	}

	public String getComponentReviewId()
	{
		return componentReviewId;
	}

	public void setComponentReviewId(String componentReviewId)
	{
		this.componentReviewId = componentReviewId;
	}

	public String getReviewPro()
	{
		return reviewPro;
	}

	public void setReviewPro(String reviewPro)
	{
		this.reviewPro = reviewPro;
	}

}
