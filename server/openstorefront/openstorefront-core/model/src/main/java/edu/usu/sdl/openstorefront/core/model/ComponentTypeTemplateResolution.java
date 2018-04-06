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
package edu.usu.sdl.openstorefront.core.model;

/**
 *
 * @author dshurtleff
 */
public class ComponentTypeTemplateResolution
{

	private String templateId;
	private String templateName;
	private String ancestorComponentType;
	private String ancestorComponentTypeLabel;
	private boolean cameFromAncestor;

	public ComponentTypeTemplateResolution()
	{
	}

	public String getAncestorComponentType()
	{
		return ancestorComponentType;
	}

	public void setAncestorComponentType(String ancestorComponentType)
	{
		this.ancestorComponentType = ancestorComponentType;
	}

	public String getAncestorComponentTypeLabel()
	{
		return ancestorComponentTypeLabel;
	}

	public void setAncestorComponentTypeLabel(String ancestorComponentTypeLabel)
	{
		this.ancestorComponentTypeLabel = ancestorComponentTypeLabel;
	}

	public boolean isCameFromAncestor()
	{
		return cameFromAncestor;
	}

	public void setCameFromAncestor(boolean cameFromAncestor)
	{
		this.cameFromAncestor = cameFromAncestor;
	}

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public String getTemplateName()
	{
		return templateName;
	}

	public void setTemplateName(String templateName)
	{
		this.templateName = templateName;
	}

}
