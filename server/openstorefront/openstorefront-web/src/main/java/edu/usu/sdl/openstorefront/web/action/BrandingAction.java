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

import edu.usu.sdl.openstorefront.core.entity.Branding;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles Branding (dynamic pages)
 *
 * @author dshurtleff
 */
public class BrandingAction
		extends BaseAction
{

	@Validate(required = true, on = "CSS")
	private String template;

	private Branding branding;

	@HandlesEvent("CSS")
	public Resolution cssPage()
	{
		branding = loadBranding();
		if (branding != null) {
			return new ForwardResolution("/WEB-INF/securepages/css/" + template);
		} else {
			return new ErrorResolution(404);
		}
	}

	@HandlesEvent("Override")
	public Resolution brandingCssOverride()
	{
		branding = loadBranding();
		if (branding != null) {
			String overrideCss = "";
			if (StringUtils.isNotBlank(branding.getOverrideCSS())) {
				overrideCss = branding.getOverrideCSS();
			}
			return new StreamingResolution("text/css", overrideCss);
		} else {
			return new ErrorResolution(404);
		}
	}

	@HandlesEvent("Preview")
	public Resolution previewBranding()
	{
		return new ForwardResolution("/WEB-INF/securepages/admin/application/brandingPreview.jsp");
	}

	public String getTemplate()
	{
		return template;
	}

	public void setTemplate(String template)
	{
		this.template = template;
	}

	public Branding getBranding()
	{
		return branding;
	}

	public void setBranding(Branding branding)
	{
		this.branding = branding;
	}

}
