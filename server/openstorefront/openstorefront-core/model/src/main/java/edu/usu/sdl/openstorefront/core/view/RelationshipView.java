/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

/**
 *
 * @author dshurtleff
 */
public class RelationshipView
{

	public static final String ATTRIBUTE_TYPE_RELATION = "ATTRIBUTE_TYPE";
	public static final String ATTRIBUTE_CODE_RELATION = "ATTRIBUTE_CODE";

	public static final String ENTITY_TYPE_COMPONENT = "component";
	public static final String ENTITY_TYPE_ATTRIBUTE = "attribute";
	public static final String ENTITY_TYPE_TAG = "tag";
	public static final String ENTITY_TYPE_ORGANIZATION = "organization";

	private String key;
	private String name;
	private String entityType;
	private String relationType;
	private String relationshipLabel;
	private String targetKey;
	private String targetName;
	private String targetEntityType;
	private String unit;

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getRelationType()
	{
		return relationType;
	}

	public void setRelationType(String relationType)
	{
		this.relationType = relationType;
	}

	public String getTargetKey()
	{
		return targetKey;
	}

	public void setTargetKey(String targetKey)
	{
		this.targetKey = targetKey;
	}

	public String getTargetName()
	{
		return targetName;
	}

	public void setTargetName(String targetName)
	{
		this.targetName = targetName;
	}

	public String getEntityType()
	{
		return entityType;
	}

	public void setEntityType(String entityType)
	{
		this.entityType = entityType;
	}

	public String getTargetEntityType()
	{
		return targetEntityType;
	}

	public void setTargetEntityType(String targetEntityType)
	{
		this.targetEntityType = targetEntityType;
	}

	public String getRelationshipLabel()
	{
		return relationshipLabel;
	}

	public void setRelationshipLabel(String relationshipLabel)
	{
		this.relationshipLabel = relationshipLabel;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

}
