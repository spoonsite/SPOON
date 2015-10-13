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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.TopicSearchItem;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class TopicSearchView
		extends TopicSearchItem
{

	private String attributeTypeDescription;

	public TopicSearchView()
	{
	}

	public static TopicSearchView toView(TopicSearchItem topicSearchItem)
	{
		TopicSearchView view = new TopicSearchView();
		try {
			BeanUtils.copyProperties(view, topicSearchItem);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		AttributeType attributeType = ServiceProxyFactory.getServiceProxy().getAttributeService().findType(topicSearchItem.getAttributeType());
		if (attributeType != null) {
			view.setAttributeTypeDescription(attributeType.getDescription());
		} else {

		}
		return view;
	}

	public static List<TopicSearchView> toView(List<TopicSearchItem> topicSearchItem)
	{
		List<TopicSearchView> views = new ArrayList<>();
		topicSearchItem.forEach(item -> {
			views.add(toView(item));
		});
		return views;
	}

	public String getAttributeTypeDescription()
	{
		return attributeTypeDescription;
	}

	public void setAttributeTypeDescription(String attributeTypeDescription)
	{
		this.attributeTypeDescription = attributeTypeDescription;
	}

}
