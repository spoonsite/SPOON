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

import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
public class ComponentExternalDependency
{

	@PK
	@NotNull
	private String dependencyId;

	@NotNull
	private String componentId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String dependencyName;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String version;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_URL)
	private String dependancyReferenceLink;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String comment;

	public ComponentExternalDependency()
	{
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
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
