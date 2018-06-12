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
package edu.usu.sdl.openstorefront.doc.model;

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds information on an entity
 *
 * @author dshurtleff
 */
public class EntityDocModel
{

	private String name;
	private String description;

	//Note: these will cause circular loops if processed in the API Docs
	//skip
	private List<EntityDocModel> parentEntities = new ArrayList<>();
	private List<EntityDocModel> implementedEntities = new ArrayList<>();

	@DataType(EntityFieldModel.class)
	private List<EntityFieldModel> fieldModels = new ArrayList<>();

	public EntityDocModel()
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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public List<EntityDocModel> getParentEntities()
	{
		return parentEntities;
	}

	public void setParentEntities(List<EntityDocModel> parentEntities)
	{
		this.parentEntities = parentEntities;
	}

	public List<EntityDocModel> getImplementedEntities()
	{
		return implementedEntities;
	}

	public void setImplementedEntities(List<EntityDocModel> implementedEntities)
	{
		this.implementedEntities = implementedEntities;
	}

	public List<EntityFieldModel> getFieldModels()
	{
		return fieldModels;
	}

	public void setFieldModels(List<EntityFieldModel> fieldModels)
	{
		this.fieldModels = fieldModels;
	}

}
