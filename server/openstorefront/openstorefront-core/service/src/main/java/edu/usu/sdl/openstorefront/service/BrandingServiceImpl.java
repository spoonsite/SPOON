/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.core.api.BrandingService;
import edu.usu.sdl.openstorefront.core.entity.Branding;
import edu.usu.sdl.openstorefront.core.entity.FeedbackHandleType;
import edu.usu.sdl.openstorefront.core.model.BrandingModel;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import java.util.List;
import java.util.Objects;
import net.sf.ehcache.Element;

/**
 * Handle branding related items
 *
 * @author jlaw
 * @author dshurtleff
 */
public class BrandingServiceImpl
		extends ServiceProxy
		implements BrandingService
{

	private static final String CURRENT_BRANDING = "CURRENTBRANDING";

	@Override
	public Branding getCurrentBrandingView()
	{
		Branding branding = null;
		Element element = OSFCacheManager.getApplicationCache().get(CURRENT_BRANDING);
		if (element != null) {
			branding = (Branding) element.getObjectValue();
		}

		if (branding == null) {

			branding = new Branding();
			branding.setActiveStatus(Branding.ACTIVE_STATUS);
			branding = branding.find();

			boolean setDefaults = false;
			if (branding == null) {
				branding = new Branding();
				branding.setName("Default");
				setDefaults = true;
			}

			//set defaults if not set
			if (setDefaults) {
				if (branding.getAllowSecurityMarkingsFlg() == null) {
					branding.setAllowSecurityMarkingsFlg(Boolean.FALSE);
				}

				if (branding.getApplicationName() == null) {
					branding.setApplicationName(PropertiesManager.getValue(PropertiesManager.KEY_APPLICATION_TITLE, "DI2E Clearinghouse"));
				}

				if (branding.getLandingPageTitle() == null) {
					branding.setLandingPageTitle("DI2E Storefront");
				}

				if (branding.getLandingPageBanner() == null) {
					branding.setLandingPageBanner("One Registry for the IC, One Registry for the DOD, One Registry to bind them all");
				}

				if (branding.getPrimaryLogoUrl() == null) {
					branding.setPrimaryLogoUrl("images/di2elogo-lg.png");
				}

				if (branding.getSecondaryLogoUrl() == null) {
					branding.setSecondaryLogoUrl("images/di2elogo-sm.png");
				}

				if (branding.getHomebackSplashUrl() == null) {
					branding.setHomebackSplashUrl("images/background_globe.jpg");
				}

				if (branding.getLandingStatsText() == null) {
					branding.setLandingStatsText("DI2E Components");
				}

				if (branding.getLandingPageFooter() == null) {
					branding.setLandingPageFooter(
							"<table style='width: 100%;'>"
							+ "	<tr>"
							+ "		<td style='flex: 1;padding-left: 30px;' valign='top'><img src='images/di2elogo-sm.png' /></td>"
							+ "		<td style='width: 300px;' class='home-footer-item'>"
							+ "			<a href='https://devtools.di2e.net' rel='nofollow'>DI2E Dev tools</a><br>"
							+ "			<a href='https://www.di2e.net/display/DI2E/Support'>Contact Support</a><br>"
							+ "			<a href='https://www.di2e.net/display/DI2E/Our+Service+Commitment'>Our Service Commitment</a><br>"
							+ "			<a href='https://www.di2e.net/display/DI2E/Onboarding'>Our Onboarding Process</a><br>"
							+ "			<a href='https://www.di2e.net/display/DI2E/Privacy+Policy' class='current ancestor-link'>Our Privacy Policy</a><br>"
							+ "			<a href='https://www.di2e.net/display/DI2E/Terms+of+Service'>Our Terms of Service</a><br>"
							+ "			<a href='https://www.di2e.net/display/DI2E/International+Traffic+in+Arms+Regulations+%28ITAR%29+and+Controlled+Unclassified+Information+%28CUI%29+Guidance'>Our ITAR and CUI Guidance</a><br>"
							+ "		</td>"
							+ "	</tr>"
							+ "</table>"
							+ "<div style='text-align: center;' class='home-footer-item'>"
							+ " <blockquote>One Registry for the IC, One Registry for the DOD, One Registry to bind them all</blockquote><br>"
							+ " © DI2E&nbsp;|&nbsp;<a href='https://www.di2e.net/display/DI2E/Consent+to+Monitoring'>Consent to Monitoring</a> | <a href='https://www.di2e.net/display/DI2E/Rules+of+Behavior'>Rules of Behavior</a> | <a href='https://www.di2e.net/display/DI2E/Approved+Use'>Approved Use</a>&nbsp;|&nbsp;All rights reserved</div>"
					);
				}

				if (branding.getLoginFooter() == null) {
					branding.setLoginFooter(
							"			<div class=\"disclaimer\">\n"
							+ "			  <span >By logging in you are consenting to these conditions</span>\n"
							+ "			  <h1>WARNING:</h1>\n"
							+ "			  <p>\n"
							+ "				You are accessing a U.S. Government (USG) Information System (IS) that is provided for USG-authorized use only. By using this IS (which includes any device attached to this IS), you consent to the following conditions: 1) The USG routinely intercepts and monitors communication on this IS for purposes including, but not limited to, penetration testing, COMSEC monitoring, network operations and defense, personnel misconduct (PM), law enforcement (LE), and counterintelligence (CI) investigations, 2) At any time, the ISG may inspect and seize data stored on this IS, 3) Communications using, or data stored on this IS are not private, are subject to routine monitoring, interception, and search, and may be disclosed or used for any USG -authorized purpose. 4) This IS includes security measures (e.g. authentication and access controls) to protect USG interests not for your personal benefit or privacy. 5) Notwithstanding the above, using this IS does not constitute consent to PM, LE, or CI investigative searching or monitoring of the content of privileged communication, or work product, related to personal representation or services by attorneys, psychotherapists, or clergy, and their assistants. Such communication and work product are private and confidential.\n"
							+ "			  </p>\n"
							+ "			</div>"
					);
				}

				if (branding.getLoginContentBlock() == null) {
					String loginLogo = branding.getPrimaryLogoUrl().replace("Media.action", "Branding.action");
					branding.setLoginContentBlock(
							"<img src='" + loginLogo + "' style='width:100%' />"
					);
				}

				if (branding.getUserInputWarning() == null) {
					branding.setUserInputWarning("Do not enter any ITAR restricted, FOUO, or otherwise sensitive information.");
				}

				if (branding.getSubmissionFormWarning() == null) {
					branding.setSubmissionFormWarning("This form will submit a component to the DI2E Framework PMO for review and consideration.A DI2E Storefront Manager will contact you regarding your submission.For help, contact <a href=\"mailto:helpdesk@di2e.net\">helpdesk@di2e.net</a>");
				}

				if (branding.getChangeRequestWarning() == null) {
					branding.setChangeRequestWarning("This form will submit a change request to the DI2E Framework PMO for review and consideration. "
							+ "A DI2E Storefront Manager may contact you regarding your submission."
							+ "For help, contact <a href=\"mailto:helpdesk@di2e.net\">helpdesk@di2e.net</a>");
				}

				if (branding.getHideArchitectureSearchFlg() == null) {
					branding.setHideArchitectureSearchFlg(true);
				}

				if (branding.getFeedbackHandler() == null) {
					branding.setFeedbackHandler(FeedbackHandleType.JIRA);
				}

				//Colors
				if (branding.getPrimaryColor() == null) {
					branding.setPrimaryColor("#441e60"); //#1a0227
				}

				if (branding.getPrimaryTextColor() == null) {
					branding.setPrimaryTextColor("#FFFFFF");
				}

				if (branding.getSecondaryColor() == null) {
					branding.setSecondaryColor("#2D0D41");
				}

				if (branding.getAccentColor() == null) {
					branding.setAccentColor("#58326f");
				}

				if (branding.getLinkColor() == null) {
					branding.setLinkColor("#3C3B3B");
				}

				if (branding.getLinkVisitedColor() == null) {
					branding.setLinkVisitedColor("#3C3B3B");
				}

				if (branding.getLinkhoverColor() == null) {
					branding.setLinkhoverColor("#2f2f2f");
				}

				if (branding.getQuoteColor() == null) {
					branding.setQuoteColor("#745189");
				}

				if (branding.getPanelHeaderColor() == null) {
					branding.setPanelHeaderColor("#555555");
				}

				if (branding.getPanelHeaderTextColor() == null) {
					branding.setPanelHeaderTextColor("#FFFFFF");
				}
			}

			element = new Element(CURRENT_BRANDING, branding);
			OSFCacheManager.getApplicationCache().put(element);
		} else {
			if (branding.getAllowSecurityMarkingsFlg() == null) {
				branding.setAllowSecurityMarkingsFlg(Boolean.FALSE);
			}
		}

		return branding;
	}

	@Override
	public void setBrandingAsCurrent(String brandingId)
	{
		Branding brandingExample = new Branding();
		List<Branding> brandings = brandingExample.findByExampleProxy();
		for (Branding branding : brandings) {
			if (branding.getBrandingId().equals(brandingId)) {
				branding.setActiveStatus(Branding.ACTIVE_STATUS);
			} else {
				branding.setActiveStatus(Branding.INACTIVE_STATUS);
			}
			branding.populateBaseUpdateFields();
			persistenceService.persist(branding);
		}
		OSFCacheManager.getApplicationCache().remove(CURRENT_BRANDING);
	}

	@Override
	public BrandingModel saveFullBranding(BrandingModel brandingModel)
	{
		Branding branding = saveBranding(brandingModel.getBranding());
		brandingModel.setBranding(branding);
		return brandingModel;
	}

	@Override
	public Branding saveBranding(Branding branding)
	{
		Objects.requireNonNull(branding);

		Branding brandingExisting = persistenceService.findById(Branding.class, branding.getBrandingId());
		if (brandingExisting != null) {
			brandingExisting.updateFields(branding);
			branding = persistenceService.persist(brandingExisting);
		} else {
			branding.setBrandingId(persistenceService.generateId());
			branding.populateBaseCreateFields();
			branding.setActiveStatus(Branding.INACTIVE_STATUS);
			branding = persistenceService.persist(branding);
		}
		OSFCacheManager.getApplicationCache().remove(CURRENT_BRANDING);

		return branding;
	}

	@Override
	public void deleteBranding(String brandingId)
	{
		Branding branding = persistenceService.findById(Branding.class, brandingId);

		if (branding != null) {
			persistenceService.delete(branding);
		}
		OSFCacheManager.getApplicationCache().remove(CURRENT_BRANDING);
	}

}
