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

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

/**
 *
 * @author dshurtleff
 */
public class LandingAction
		extends BaseAction
{

	private String landingTemplate;
	private String appVersion;

	/**
	 * Sets the file that will be used as the landing page.
	 * @return 
	 */
	@DefaultHandler
	public Resolution landingPage()
	{
		return new ForwardResolution("desktop/index.html");
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
