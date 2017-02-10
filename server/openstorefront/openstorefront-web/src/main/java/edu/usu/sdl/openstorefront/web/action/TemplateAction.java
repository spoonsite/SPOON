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
package edu.usu.sdl.openstorefront.web.action;

import edu.usu.sdl.openstorefront.core.entity.ComponentTypeTemplate;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

/**
 * Handles custom entry template 
 * @author dshurtleff
 */
public class TemplateAction
	extends BaseAction
{
	@Validate(required = true, on="LoadTemplate")
	private String templateId;
	
	@Validate(required = true, on="PreviewTemplate")
	private String templateContents;

	@HandlesEvent("LoadTemplate")
	public Resolution loadTemplate()
	{
		ComponentTypeTemplate componentTypeTemplate = new ComponentTypeTemplate();
		componentTypeTemplate.setTemplateId(templateId);
		componentTypeTemplate = componentTypeTemplate.find();
		if (componentTypeTemplate != null)
		{
			templateContents = componentTypeTemplate.getTemplate();
			return new ForwardResolution("/WEB-INF/securepages/template/custom.jsp");
		} 
		return new ErrorResolution(404);
	}
	
	@HandlesEvent("PreviewTemplate")
	public Resolution previewTemplate()
	{
		return new ForwardResolution("/WEB-INF/securepages/template/custom.jsp");
	}	
	
	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public String getTemplateContents()
	{
		return templateContents;
	}

	public void setTemplateContents(String templateContents)
	{
		this.templateContents = templateContents;
	}

}
