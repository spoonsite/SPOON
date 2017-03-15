/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.core.annotation.FK;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Part of Evaluation Template")
@Embeddable
public class EvaluationSectionTemplate
		implements Serializable
{

	@NotNull
	@FK(ContentSectionTemplate.class)
	private String sectionTemplateId;

	@Version
	private String storageVersion;

	public EvaluationSectionTemplate()
	{
	}

	public String getSectionTemplateId()
	{
		return sectionTemplateId;
	}

	public void setSectionTemplateId(String sectionTemplateId)
	{
		this.sectionTemplateId = sectionTemplateId;
	}

	public String getStorageVersion()
	{
		return storageVersion;
	}

	public void setStorageVersion(String storageVersion)
	{
		this.storageVersion = storageVersion;
	}

}
