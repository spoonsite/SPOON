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
package edu.usu.sdl.openstorefront.storage.model;

import javax.validation.constraints.NotNull;

/**
 *
 * @author jlaw
 */
public abstract class BaseComponent
		extends BaseEntity
{

	@NotNull
	private String componentId;

	public BaseComponent()
	{
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}
	
	public void setPrimaryKey(String itemId)
	{
		setPrimaryKey(itemId, "", "");
	}

	public void setPrimaryKey(String itemId, String componentId)
	{
		setPrimaryKey(itemId, "", componentId);
	}
	
	abstract public void setPrimaryKey(Object pk);
	
	abstract public void setPrimaryKey(String itemId, String itemCode, String componentId);	
	
	abstract public Object getPrimaryKey();

}
