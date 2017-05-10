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
package edu.usu.sdl.openstorefront.web.action;

import edu.usu.sdl.openstorefront.core.entity.Branding;
import edu.usu.sdl.openstorefront.core.entity.LandingTemplate;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class LandingAction
		extends BaseAction
{

	private String landingTemplate;
	private String appVersion;

	@DefaultHandler
	public Resolution landingPage()
	{
		appVersion = getApplicationVersion();
		Branding branding = loadBranding();
		LandingTemplate landingTemplateFull = branding.getLandingTemplate();
		if (getLandingTemplate() != null && StringUtils.isNotBlank(landingTemplateFull.getTemplate())) {
			setLandingTemplate(landingTemplateFull.getTemplate());
		} else {
			setLandingTemplate(getPageOutput("/WEB-INF/securepages/template/defaultLanding.jsp"));
		}

		return new ForwardResolution("/WEB-INF/securepages/shared/index.jsp");
	}

	public String getLandingTemplate()
	{
		return landingTemplate;
	}

	public void setLandingTemplate(String landingTemplate)
	{
		this.landingTemplate = landingTemplate;
	}

	public String getAppVersion()
	{
		return appVersion;
	}

	public void setAppVersion(String appVersion)
	{
		this.appVersion = appVersion;
	}

}
