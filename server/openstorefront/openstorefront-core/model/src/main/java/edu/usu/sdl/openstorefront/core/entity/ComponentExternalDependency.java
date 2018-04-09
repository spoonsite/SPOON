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
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.validation.BasicHTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Dependency need external to package component")
public class ComponentExternalDependency
		extends BaseComponent<ComponentExternalDependency>
{

	@PK(generated = true)
	@NotNull
	private String dependencyId;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String dependencyName;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String version;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_URL)
	@Sanitize(TextSanitizer.class)
	@APIDescription("External URL to the dependency")
	private String dependancyReferenceLink;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_DETAILED_DESCRIPTION)
	@Sanitize(BasicHTMLSanitizer.class)
	private String comment;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ComponentExternalDependency()
	{
	}

	@Override
	public String uniqueKey()
	{
		String key = getDependencyName()
				+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR
				+ getVersion()
				+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR
				+ getDependancyReferenceLink()
				+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR
				+ getComment();
		return key;
	}

	@Override
	protected void customKeyClear()
	{
		setDependencyId(null);
	}

	@Override
	public void updateFields(StandardEntity entity)
	{
		super.updateFields(entity);

		ComponentExternalDependency dependency = (ComponentExternalDependency) entity;
		this.setComment(dependency.getComment());
		this.setDependancyReferenceLink(dependency.getDependancyReferenceLink());
		this.setDependencyName(dependency.getDependencyName());
		this.setVersion(dependency.getVersion());

	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getDependancyReferenceLink()
	{
		return dependancyReferenceLink;
	}

	public void setDependancyReferenceLink(String dependancyReferenceLink)
	{
		this.dependancyReferenceLink = dependancyReferenceLink;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public String getDependencyId()
	{
		return dependencyId;
	}

	public void setDependencyId(String dependencyId)
	{
		this.dependencyId = dependencyId;
	}

	public String getDependencyName()
	{
		return dependencyName;
	}

	public void setDependencyName(String dependencyName)
	{
		this.dependencyName = dependencyName;
	}

}
