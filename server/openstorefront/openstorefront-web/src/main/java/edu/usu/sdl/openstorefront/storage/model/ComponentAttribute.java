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

import edu.usu.sdl.openstorefront.util.PK;

/**
 *
 * @author dshurtleff
 */
public class ComponentAttribute
		extends BaseComponent
{

	@PK
	private ComponentAttributePk componentAttributePk;
	
	private boolean important;

	public ComponentAttribute()
	{
	}

	public ComponentAttributePk getComponentAttributePk()
	{
		return componentAttributePk;
	}

	public void setComponentAttributePk(ComponentAttributePk componentAttributePk)
	{
		this.componentAttributePk = componentAttributePk;
	}

	/**
	 * @return the important
	 */
	public boolean isImportant()
	{
		return important;
	}

	/**
	 * @param important the important to set
	 */
	public void setImportant(boolean important)
	{
		this.important = important;
	}

}
