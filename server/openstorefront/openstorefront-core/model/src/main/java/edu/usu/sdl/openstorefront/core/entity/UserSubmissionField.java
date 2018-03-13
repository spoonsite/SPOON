/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
@APIDescription("This is part of the submission form")
@Embeddable
public class UserSubmissionField
		implements Serializable
{

	@PK(generated = true)
	@NotNull
	private String fieldId;

	@NotNull
	@FK(SubmissionTemplateStatus.class)
	private String templateFieldId;

	@ConsumeField
	@APIDescription("This is a complex value;"
			+ " It can be a String or JSON encode object;"
			+ " The template field controls what is accepted here and the mapping")
	private String value;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public UserSubmissionField()
	{
	}

	public String getFieldId()
	{
		return fieldId;
	}

	public void setFieldId(String fieldId)
	{
		this.fieldId = fieldId;
	}

	public String getTemplateFieldId()
	{
		return templateFieldId;
	}

	public void setTemplateFieldId(String templateFieldId)
	{
		this.templateFieldId = templateFieldId;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

}
