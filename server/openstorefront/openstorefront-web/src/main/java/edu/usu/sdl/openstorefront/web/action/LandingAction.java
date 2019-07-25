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

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.entity.Branding;
import edu.usu.sdl.openstorefront.core.entity.LandingTemplate;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class LandingAction
		extends BaseAction
{

	private static final String LANDING_PAGE_PREVIEW = "LANDING-PAGE-PREVIEW";

	private String landingTemplate;
	private String appVersion;

	/**
	 * This functions returns a Stripes library object, Forward Resolution, which is 
	 * given the file location of a .jsp file that serves to be a wrapper for the 
	 * actual template. The actual template of the landingPage is set as a property
	 * of this LandingAction object within this function. 
	 * @return anonymous Forward Resolution object.  
	 */
	@DefaultHandler
	public Resolution landingPage()
	{
		/**
		 * As of v2.10, the custom Landing page feature has been deprecated, 
		 * therefore the logic for that feature in this function has been removed.
		 */
		// appVersion = getApplicationVersion();
		// Branding branding = loadBranding();
		// LandingTemplate landingTemplateFull = branding.getLandingTemplate();
		// boolean useDefault = true;
		// if (getLandingTemplate() != null
		// 		|| Convert.toBoolean(branding.getUseDefaultLandingPage()) == false) {

		// 	if (landingTemplateFull != null) {
		// 		String fullTemplate = landingTemplateFull.fullTemplate();
		// 		if (StringUtils.isNotBlank(fullTemplate)) {
		// 			useDefault = false;
		// 		}
		// 	}
		// }
		// if (!useDefault) {
		// 	String fullTemplate = landingTemplateFull.fullTemplate();
		// 	setLandingTemplate(fullTemplate);
		// } else {
			String defaultLanding = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_UI_DEFAULTLANDING_TEMPLATE, "defaultLanding.jsp");
			if (StringUtils.isBlank(defaultLanding)) {
				defaultLanding = "defaultLanding.jsp";
			}
			setLandingTemplate(getPageOutput("/WEB-INF/securepages/template/" + defaultLanding));
		// }

		return new ForwardResolution("/WEB-INF/securepages/shared/index.jsp");
	}

	@HandlesEvent("Preview")
	public Resolution previewLandingPage()
	{
		appVersion = getApplicationVersion();
		if (StringUtils.isBlank(landingTemplate)) {
			landingTemplate = (String) getContext().getRequest().getSession().getAttribute(LANDING_PAGE_PREVIEW);
		} else {
			getContext().getRequest().getSession().setAttribute(LANDING_PAGE_PREVIEW, landingTemplate);
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
