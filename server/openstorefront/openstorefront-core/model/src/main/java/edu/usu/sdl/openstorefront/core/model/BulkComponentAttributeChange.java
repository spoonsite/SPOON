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
package edu.usu.sdl.openstorefront.core.model;

import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class BulkComponentAttributeChange
{

	public enum OpertionType
	{

		DELETE,
		ACTIVATE,
		INACTIVE
	}
	private OpertionType opertionType = OpertionType.INACTIVE;
	private List<ComponentAttribute> attributes = new ArrayList<>();

	public BulkComponentAttributeChange()
	{
	}

	public OpertionType getOpertionType()
	{
		return opertionType;
	}

	public void setOpertionType(OpertionType opertionType)
	{
		this.opertionType = opertionType;
	}

	public List<ComponentAttribute> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(List<ComponentAttribute> attributes)
	{
		this.attributes = attributes;
	}

}
