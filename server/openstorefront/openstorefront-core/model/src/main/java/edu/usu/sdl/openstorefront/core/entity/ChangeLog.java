/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Internal record of changes that have been applied to an entity. Mainly for Components and Evaluations.")
public class ChangeLog
		extends StandardEntity<ChangeLog>
{

	public static final String REMOVED_ALL_ID = "-1";
	public static final String PK_FIELD = "PK";

	@PK(generated = true)
	@NotNull
	private String changeLogId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	private String entityId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	private String entity;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	private String parentEntityId;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	private String parentEntity;

	private String field;
	private String oldValue;
	private String newValue;
	private Integer version;
	private String archivedExternalResourceId;
	private String archivedEntity;

	@ValidValueType(value = {}, lookupClass = ChangeType.class)
	@FK(ChangeType.class)
	private String changeType;

	public ChangeLog()
	{
	}

	public String getChangeLogId()
	{
		return changeLogId;
	}

	public void setChangeLogId(String changeLogId)
	{
		this.changeLogId = changeLogId;
	}

	public String getEntityId()
	{
		return entityId;
	}

	public void setEntityId(String entityId)
	{
		this.entityId = entityId;
	}

	public String getEntity()
	{
		return entity;
	}

	public void setEntity(String entity)
	{
		this.entity = entity;
	}

	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field = field;
	}

	public String getOldValue()
	{
		return oldValue;
	}

	public void setOldValue(String oldValue)
	{
		this.oldValue = oldValue;
	}

	public String getNewValue()
	{
		return newValue;
	}

	public void setNewValue(String newValue)
	{
		this.newValue = newValue;
	}

	public Integer getVersion()
	{
		return version;
	}

	public void setVersion(Integer version)
	{
		this.version = version;
	}

	public String getArchivedExternalResourceId()
	{
		return archivedExternalResourceId;
	}

	public void setArchivedExternalResourceId(String archivedExternalResourceId)
	{
		this.archivedExternalResourceId = archivedExternalResourceId;
	}

	public String getArchivedEntity()
	{
		return archivedEntity;
	}

	public void setArchivedEntity(String archivedEntity)
	{
		this.archivedEntity = archivedEntity;
	}

	public String getChangeType()
	{
		return changeType;
	}

	public void setChangeType(String changeType)
	{
		this.changeType = changeType;
	}

	public String getParentEntityId()
	{
		return parentEntityId;
	}

	public void setParentEntityId(String parentEntityId)
	{
		this.parentEntityId = parentEntityId;
	}

	public String getParentEntity()
	{
		return parentEntity;
	}

	public void setParentEntity(String parentEntity)
	{
		this.parentEntity = parentEntity;
	}

}
