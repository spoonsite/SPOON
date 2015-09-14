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
package edu.usu.sdl.openstorefront.doc.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class APITypeModel
{

	private String name;
	private String object;
	private String description;
	private List<APIValueFieldModel> fields = new ArrayList<>();

	public APITypeModel()
	{
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getObject()
	{
		return object;
	}

	public void setObject(String object)
	{
		this.object = object;
	}

	public List<APIValueFieldModel> getFields()
	{
		return fields;
	}

	public void setFields(List<APIValueFieldModel> fields)
	{
		this.fields = fields;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}
