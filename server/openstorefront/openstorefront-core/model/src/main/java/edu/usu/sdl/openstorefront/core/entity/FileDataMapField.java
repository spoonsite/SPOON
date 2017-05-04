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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Hold an external field to internal entity field mapping")
@Embeddable
public class FileDataMapField
	implements Serializable
{
	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	private String field;

	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String entityClass;
	
	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)	
	private String entityField;
	
	@ConsumeField
	private Boolean useAsAttributeLabel;
	
	@ConsumeField
	private Boolean concatenate;
	
	@ConsumeField
	private Boolean addEndPathToValue;
	
	@ConsumeField
	private Boolean fileAttachment;
	
	@ConsumeField
	private String pathToEnityField;

	@ConsumeField
	@Embedded
	@DataType(DataMapTransform.class)
	@OneToMany(orphanRemoval = true)	
	private List<DataMapTransform> transforms;
	
	@ConsumeField
	@Embedded
	@DataType(DataMapTransform.class)
	@OneToMany(orphanRemoval = true)
	private List<DataMapTransform> pathTransforms;
	
	@Version
	private String storageVersion;

	public FileDataMapField()
	{
	}

	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field = field;
	}

	public List<DataMapTransform> getTransforms()
	{
		return transforms;
	}

	public void setTransforms(List<DataMapTransform> transforms)
	{
		this.transforms = transforms;
	}

	public String getEntityClass()
	{
		return entityClass;
	}

	public void setEntityClass(String entityClass)
	{
		this.entityClass = entityClass;
	}

	public String getEntityField()
	{
		return entityField;
	}

	public void setEntityField(String entityField)
	{
		this.entityField = entityField;
	}

	public Boolean getUseAsAttributeLabel()
	{
		return useAsAttributeLabel;
	}

	public void setUseAsAttributeLabel(Boolean useAsAttributeLabel)
	{
		this.useAsAttributeLabel = useAsAttributeLabel;
	}

	public Boolean getConcatenate()
	{
		return concatenate;
	}

	public void setConcatenate(Boolean concatenate)
	{
		this.concatenate = concatenate;
	}

	public Boolean getAddEndPathToValue()
	{
		return addEndPathToValue;
	}

	public void setAddEndPathToValue(Boolean addEndPathToValue)
	{
		this.addEndPathToValue = addEndPathToValue;
	}

	public String getPathToEnityField()
	{
		return pathToEnityField;
	}

	public void setPathToEnityField(String pathToEnityField)
	{
		this.pathToEnityField = pathToEnityField;
	}

	public List<DataMapTransform> getPathTransforms()
	{
		return pathTransforms;
	}

	public void setPathTransforms(List<DataMapTransform> pathTransforms)
	{
		this.pathTransforms = pathTransforms;
	}

	public Boolean getFileAttachment()
	{
		return fileAttachment;
	}

	public void setFileAttachment(Boolean fileAttachment)
	{
		this.fileAttachment = fileAttachment;
	}

	public String getStorageVersion()
	{
		return storageVersion;
	}

	public void setStorageVersion(String storageVersion)
	{
		this.storageVersion = storageVersion;
	}
	
}
