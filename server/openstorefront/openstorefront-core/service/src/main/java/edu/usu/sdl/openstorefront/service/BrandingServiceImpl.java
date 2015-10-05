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

import edu.usu.sdl.openstorefront.core.api.BrandingService;
import edu.usu.sdl.openstorefront.core.entity.TopicSearchItem;
import edu.usu.sdl.openstorefront.core.view.BrandingView;
import java.util.List;

/**
 *
 * @author jlaw
 */
public class BrandingServiceImpl
		extends ServiceProxy
		implements BrandingService
{

	@Override
	public BrandingView getBrandingView(String brandingId)
	{
		BrandingView view = new BrandingView();

		view.setTopicSearchItems(getTopicSearchItems(brandingId));

		return view;
	}

	@Override
	public List<TopicSearchItem> getTopicSearchItems()
	{
		return getTopicSearchItems("");
	}

	@Override
	public List<TopicSearchItem> getTopicSearchItems(String brandingId)
	{
		TopicSearchItem example = new TopicSearchItem();
		if (!brandingId.isEmpty()) {
			example.setBrandingId(brandingId);
		}

		List<TopicSearchItem> temp = example.findByExample();
		return temp;
	}

	@Override
	public TopicSearchItem getTopicSearchItem(String entityId)
	{
		return ServiceProxy.getProxy().persistenceService.findById(TopicSearchItem.class, entityId);
	}

	@Override
	public TopicSearchItem addTopicSearchItem(TopicSearchItem item)
	{
		item.setEntityId(ServiceProxy.getProxy().persistenceService.generateId());
		ServiceProxy.getProxy().persistenceService.persist(item);
		return item;
	}

	@Override
	public void deleteTopicSearchItem(String entityId)
	{
		TopicSearchItem temp = ServiceProxy.getProxy().persistenceService.findById(TopicSearchItem.class, entityId);
		if (temp != null) {
			ServiceProxy.getProxy().persistenceService.delete(temp);
		}
	}

	@Override
	public void deleteTopicSearchItems(String brandingId)
	{
		TopicSearchItem example = new TopicSearchItem();
		example.setBrandingId(brandingId);
		List<TopicSearchItem> list = ServiceProxy.getProxy().persistenceService.queryByExample(TopicSearchItem.class, example);
		for (TopicSearchItem item : list) {
			if (item != null) {
				TopicSearchItem temp = ServiceProxy.getProxy().persistenceService.findById(TopicSearchItem.class, item.getEntityId());
				ServiceProxy.getProxy().persistenceService.delete(temp);
			}
		}
	}

}
