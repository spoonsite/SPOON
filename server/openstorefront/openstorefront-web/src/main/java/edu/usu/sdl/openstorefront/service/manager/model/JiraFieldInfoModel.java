/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.manager.model;

import com.atlassian.jira.rest.client.IdentifiableEntity;
import com.atlassian.jira.rest.client.domain.BasicIssueType;
import com.atlassian.jira.rest.client.domain.CimFieldInfo;
import com.atlassian.jira.rest.client.domain.CustomFieldOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jlaw
 */
public class JiraFieldInfoModel
{

	private String key;
	private String id;
	private String name;
	private List<JiraFieldAllowedValues> allowedValues;

	public JiraFieldInfoModel()
	{

	}

	public static JiraFieldInfoModel toView(String key, CimFieldInfo info)
	{
		JiraFieldInfoModel view = new JiraFieldInfoModel();

		view.setKey(key);
		view.setId(info.getId());
		view.setName(info.getName());
		List<JiraFieldAllowedValues> values = new ArrayList<>();
		if (info.getAllowedValues() != null) {
			List<Object> allowedValues = ((List<Object>) info.getAllowedValues());
			if (allowedValues.size() > 0 && allowedValues.get(0) instanceof CustomFieldOption) {
				for (Object allowedValue : allowedValues) {
					if (allowedValue instanceof CustomFieldOption) {
						values.add(JiraFieldAllowedValues.toView((CustomFieldOption) allowedValue));
					}
				}
			}
			else {
				return null;
			}
		}

		view.setAllowedValues(values);

		return view;
	}
	public static JiraFieldInfoModel toView(JiraIssueType temp)
	{
		JiraFieldInfoModel view = new JiraFieldInfoModel();

		view.setKey(temp.toString());
		view.setId(temp.getId());
		view.setName("Workflow Status");
		List<JiraFieldAllowedValues> values = new ArrayList<>();
		if (temp.getStatuses() != null) {
			List<JiraStatus> statuses = temp.getStatuses();
			if (statuses.size() > 0) {
				for (JiraStatus status : statuses) {
					values.add(JiraFieldAllowedValues.toView(status));
				}
			}
			else {
				return null;
			}
		}

		view.setAllowedValues(values);

		return view;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the allowedValues
	 */
	public List<JiraFieldAllowedValues> getAllowedValues()
	{
		return allowedValues;
	}

	/**
	 * @param allowedValues the allowedValues to set
	 */
	public void setAllowedValues(List<JiraFieldAllowedValues> allowedValues)
	{
		this.allowedValues = allowedValues;
	}

	/**
	 * @return the key
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key)
	{
		this.key = key;
	}

}
