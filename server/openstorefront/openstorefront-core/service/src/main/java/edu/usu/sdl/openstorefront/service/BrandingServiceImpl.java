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
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.api.BrandingService;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Branding;
import edu.usu.sdl.openstorefront.core.entity.TopicSearchItem;
import edu.usu.sdl.openstorefront.core.model.BrandingModel;
import edu.usu.sdl.openstorefront.core.view.BrandingView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

	@Override
	public BrandingView getCurrentBrandingView()
	{
		Branding branding = new Branding();
		branding.setActiveStatus(Branding.ACTIVE_STATUS);
		branding = branding.find();

		List<TopicSearchItem> topicSearchItems = new ArrayList<>();

		if (branding == null) {
			branding = new Branding();
			branding.setName("Default");

			AttributeType attributeType = new AttributeType();
			attributeType.setVisibleFlg(Boolean.TRUE);
			attributeType.setActiveStatus(AttributeType.ACTIVE_STATUS);

			List<AttributeType> attributeTypes = attributeType.findByExample();
			for (AttributeType attributeTypeLocal : attributeTypes) {
				TopicSearchItem topicSearchItem = new TopicSearchItem();
				topicSearchItem.setAttributeType(attributeTypeLocal.getAttributeType());
				topicSearchItems.add(topicSearchItem);
			}

		} else {
			TopicSearchItem topicSearchItemExample = new TopicSearchItem();
			topicSearchItemExample.setBrandingId(branding.getBrandingId());
			topicSearchItems = topicSearchItemExample.findByExample();
		}

		//set defaults if not set
		if (branding.getAllowJiraFeedbackFlg() == null) {
			branding.setAllowJiraFeedbackFlg(Convert.toBoolean(PropertiesManager.getValue(PropertiesManager.KEY_ALLOW_JIRA_FEEDBACK, "true")));
		}

		if (branding.getAllowSecurityMarkingsFlg() == null) {
			branding.setAllowSecurityMarkingsFlg(Boolean.FALSE);
		}

		if (branding.getShowComponentTypeSearchFlg() == null) {
			branding.setAllowSecurityMarkingsFlg(Boolean.TRUE);
		}

		if (branding.getApplicationName() == null) {
			branding.setApplicationName(PropertiesManager.getValue(PropertiesManager.KEY_APPLICATION_TITLE, "DI2E Clearinghouse"));
		}

		if (branding.getLandingPageTitle() == null) {
			branding.setLandingPageTitle("DI2E Clearinghouse");			
		}
		
		if (branding.getLandingPageBanner()== null) {
			branding.setLandingPageBanner("One Registry for the IC, One Registry for the DOD, One Registry to bind them all");			
		}

		if (branding.getPrimaryLogoUrl()== null) {
			branding.setPrimaryLogoUrl("images/di2elogo-lg.png");			
		}
		
		if (branding.getSecondaryLogoUrl()== null) {
			branding.setSecondaryLogoUrl("images/di2elogo-sm.png");			
		}		

		if (branding.getLandingStatsText() == null) {
			branding.setLandingStatsText("DI2E Components");			
		}
		
		if (branding.getLandingPageFooter() == null) {
			branding.setLandingPageFooter("<div class=\"footer_content\">\n" +
			"        <div class=\"column\">\n" +
			"            <div class=\"title\">About Us</div>\n" +
			"            <ul>\n" +
			"                <li><a href=\"https://intellipedia.intelink.gov/wiki/Defense_Intelligence_Information_Enterprise\" title=\"Requires Intellipedia authentication\">What is DI2E?</a>&nbsp;<i class=\"icon-lock\" title=\"Authentication Required\"></i></li>\n" +
			"                <li><a href=\"https://intellipedia.intelink.gov/wiki/DI2E_Framework\" title=\"Requires Intellipedia authentication\">What is DI2E Framework?</a>&nbsp;<i class=\"icon-lock\"></i></li>\n" +
			"            </ul>\n" +
			"        </div>\n" +
			"        <div class=\"column\">\n" +
			"            <div class=\"title\">Links</div>\n" +
			"            <ul>\n" +
			"                <li><a href=\"https://devtools.di2e.net\">DI2E Dev tools</a></li>\n" +
			"            </ul>\n" +
			"        </div>\n" +
			"        <div class=\"column\">\n" +
			"            <div class=\"title\">F.A.Q.</div>\n" +
			"            <ul>\n" +
			"                <li><a href=\"https://devtools.di2e.net/sla.php\">Our Service Commitment</a></li>\n" +
			"                <li><a href=\"https://devtools.di2e.net/onboarding.php\">Our Onboarding Process</a></li>\n" +
			"                <li><a href=\"https://devtools.di2e.net/privacy.php\">Our Privacy Policy</a></li>\n" +
			"                <li><a href=\"https://devtools.di2e.net/terms_of_service.php\">Our Terms of Service</a></li>\n" +
			"            </ul>\n" +
			"        </div>\n" +
			"        <div class=\"column\">\n" +
			"            <div class=\"title\">Contact Us</div>\n" +
			"            <ul>\n" +
			"                <li><a href=\"https://devtools.di2e.net/accounts.php\">Request an Account</a></li>\n" +
			"                <li><a href=\"mailto:di2eframeworkinfo@di2e.net\">General Questions</a></li>\n" +
			"                <li><a href=\"https://devtools.di2e.net/support.php\">Contact Support</a></li>\n" +
			"            </ul>\n" +
			"        </div>\n" +
			"        <div style=\"clear:both\"></div>    \n" +
			"    </div>\n" +
			"    <div class=\"copyright\">&#169; DI2E&nbsp;|&nbsp;<a href=\"https://devtools.di2e.net/consent.php\">Consent to Monitoring</a>&nbsp;|&nbsp;All rights reserved</div>"
			);			
		}	
		
		if (branding.getLoginWarning() == null) {
			branding.setLoginWarning(
			"<span >By logging in you are consenting to these conditions</span>\n" +
			"			<div class=\"disclaimer\">\n" +
			"			  <h1>WARNING:</h1>\n" +
			"			  <p>\n" +
			"				You are accessing a U.S. Government (USG) Information System (IS) that is provided for USG-authorized use only. By using this IS (which includes any device attached to this IS), you consent to the following conditions: 1) The USG routinely intercepts and monitors communication on this IS for purposes including, but not limited to, penetration testing, COMSEC monitoring, network operations and defense, personnel misconduct (PM), law enforcement (LE), and counterintelligence (CI) investigations, 2) At any time, the ISG may inspect and seize data stored on this IS, 3) Communications using, or data stored on this IS are not private, are subject to routine monitoring, interception, and search, and may be disclosed or used for any USG -authorized purpose. 4) This IS includes security measures (e.g. authentication and access controls) to protect USG interests not for your personal benefit or privacy. 5) Notwithstanding the above, using this IS does not constitute consent to PM, LE, or CI investigative searching or monitoring of the content of privileged communication, or work product, related to personal representation or services by attorneys, psychotherapists, or clergy, and their assistants. Such communication and work product are private and confidential.\n" +
			"			  </p>\n" +
			"			</div>"
			);			
		}
		

		return BrandingView.toView(branding, topicSearchItems);
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
	}

	@Override
	public BrandingModel saveFullBanding(BrandingModel brandingModel)
	{
		Branding branding = saveBanding(brandingModel.getBranding());
		brandingModel.setBranding(branding);
		saveTopicSearchItems(branding.getBrandingId(), brandingModel.getTopicSearchItems());
		return brandingModel;
	}

	@Override
	public Branding saveBanding(Branding branding)
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
		return branding;
	}

	@Override
	public void saveTopicSearchItems(String brandingId, List<TopicSearchItem> items)
	{
		Objects.requireNonNull(brandingId, "Branding Id is Required");

		TopicSearchItem topicSearchItemExample = new TopicSearchItem();
		topicSearchItemExample.setBrandingId(brandingId);
		persistenceService.deleteByExample(topicSearchItemExample);

		for (TopicSearchItem item : items) {
			item.setTopicSearchItemId(persistenceService.generateId());
			item.setBrandingId(brandingId);
			item.populateBaseCreateFields();
			persistenceService.persist(item);
		}
	}

	@Override
	public void deleteBranding(String brandingId)
	{
		Branding branding = persistenceService.findById(Branding.class, brandingId);

		if (branding != null) {
			TopicSearchItem topicSearchItemExample = new TopicSearchItem();
			topicSearchItemExample.setBrandingId(brandingId);
			persistenceService.deleteByExample(topicSearchItemExample);

			persistenceService.delete(branding);
		}
	}

}
