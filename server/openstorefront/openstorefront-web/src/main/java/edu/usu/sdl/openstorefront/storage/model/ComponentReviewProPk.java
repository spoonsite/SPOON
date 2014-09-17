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
import edu.usu.sdl.openstorefront.doc.ValidValueType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
public class ComponentReviewProPk
		extends BasePK
{

	@NotNull
	@ConsumeField
	private String componentReviewId;

	@NotNull
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = ReviewPro.class)
	private String reviewPro;

	public ComponentReviewProPk()
	{
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
