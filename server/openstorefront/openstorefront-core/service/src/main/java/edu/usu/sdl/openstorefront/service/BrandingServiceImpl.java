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
			branding.setApplicationName(PropertiesManager.getValue(PropertiesManager.KEY_APPLICATION_TITLE, "DI2E ClearingHouse"));
		}

		if (branding.getLandingPageTitle() == null) {
			branding.setLandingPageTitle(branding.getLandingPageTitle());
		}

		return BrandingView.toView(branding, topicSearchItems);
	}

	@Override
	public void setBrandingAsCurrent(String brandingId)
	{
		Objects.requireNonNull(brandingId);

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
