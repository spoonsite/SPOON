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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author cyearsley
 */
public class Faq
		extends StandardEntity<Faq>
{
	@PK(generated = true)
	@NotNull
	private String faqId;
	
	@NotNull
	@ConsumeField
	private String question;
	
	@NotNull
	@ConsumeField
	private String answer;
	
	@NotNull
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = FaqCategoryType.class)
	@FK(FaqCategoryType.class)
	@APIDescription("Category of the FAQ")
	private String category;

	public String getFaqId()
	{
		return faqId;
	}

	public void setFaqId(String faqId)
	{
		this.faqId = faqId;
	}

	public String getQuestion()
	{
		return question;
	}

	public void setQuestion(String question)
	{
		this.question = question;
	}

	public String getAnswer()
	{
		return answer;
	}

	public void setAnswer(String answer)
	{
		this.answer = answer;
	}
	
	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}
}
