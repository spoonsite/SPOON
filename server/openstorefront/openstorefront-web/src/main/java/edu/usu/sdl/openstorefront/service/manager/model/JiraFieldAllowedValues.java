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

import com.atlassian.jira.rest.client.domain.CustomFieldOption;

/**
 *
 * @author jlaw
 */
public class JiraFieldAllowedValues
{
	private Long id;
	private String value;
	private String name;

	public JiraFieldAllowedValues(){
		
	}
	
	public static JiraFieldAllowedValues toView(CustomFieldOption option){
		JiraFieldAllowedValues view = new JiraFieldAllowedValues();
		view.setId(option.getId());
		view.setValue(option.getValue());
		view.setName(null);
		return view;
	}
	
	public static JiraFieldAllowedValues toView(JiraStatus status){
		JiraFieldAllowedValues view = new JiraFieldAllowedValues();
		view.setId(status.getId());
		view.setValue(status.getDescription());
		view.setName(status.getName());
		return view;
	}
	
	
	/**
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
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
}
