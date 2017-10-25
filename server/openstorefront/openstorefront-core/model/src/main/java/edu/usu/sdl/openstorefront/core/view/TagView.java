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

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.security.UserContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.jsoup.helper.StringUtil;

/**
 *
 * @author dshurtleff
 */
public class TagView
		extends StandardEntityView
{

	private String tagId;
	private String componentId;
	private String componentName;
	private String text;
	private String createUser;
	private Date createDts;

	public TagView()
	{
	}

	public static TagView toView(ComponentTag tag)
	{
		Service service = ServiceProxyFactory.getServiceProxy();

		TagView tagView = new TagView();
		tagView.setTagId(tag.getTagId());
		tagView.setText(tag.getText());
		tagView.setCreateDts(tag.getCreateDts());
		tagView.setCreateUser(tag.getCreateUser());
		tagView.setComponentId(tag.getComponentId());
		tagView.toStandardView(tag);
		String componentName = service.getComponentService().getComponentName(tag.getComponentId());
		if (componentName != null) {
			tagView.setComponentName(componentName);
		} else {
			tagView.setComponentName("Missing Component (Orphaned Tag)");
		}
		return tagView;
	}

	public static List<TagView> toView(List<ComponentTag> tags)
	{
		List<TagView> views = new ArrayList<>();
		if (tags != null) {
			UserContext userContext = SecurityUtil.getUserContext();

			Set<String> acceptedDataSources = userContext.dataSources();
			Set<String> acceptedDataSensitivity = userContext.dataSensitivity();
			tags.forEach(tag -> {
				Component example = new Component();
				example.setActiveStatus(Component.ACTIVE_STATUS);
				example.setComponentId(tag.getComponentId());
				example = example.find();
				if ((StringUtil.isBlank(example.getDataSource()) || (acceptedDataSources != null && acceptedDataSources.contains(example.getDataSource())))
						&& (StringUtil.isBlank(example.getDataSensitivity()) || (acceptedDataSensitivity != null && acceptedDataSensitivity.contains(example.getDataSensitivity())))) {
					views.add(toView(tag));
				}
			});
		}
		return views;
	}

	public String getTagId()
	{
		return tagId;
	}

	public void setTagId(String tagId)
	{
		this.tagId = tagId;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getComponentName()
	{
		return componentName;
	}

	public void setComponentName(String componentName)
	{
		this.componentName = componentName;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public Date getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

}
