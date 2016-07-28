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
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import java.util.List;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
public class FileDataMap 
	extends StandardEntity<FileDataMap>
{
	@PK(generated = true)
	@NotNull
	private String fileDataMapId;
	
	@FK(value = FileFormat.class, referencedField="code")
	@ValidValueType(value={}, lookupClass = FileFormat.class)
	@ConsumeField
	@NotNull
	private String fileFormat;
	
	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String name;
	
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
	private String setPathToEnityField;

	@ConsumeField
	@DataType(DataMapTransform.class)
	@OneToMany(orphanRemoval = true)	
	private List<DataMapTransform> transforms;
	
	@ConsumeField
	@DataType(DataMapTransform.class)
	@OneToMany(orphanRemoval = true)
	private List<DataMapTransform> pathTransforms;

	public FileDataMap()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity); 
		
		FileDataMap fileDataMap = (FileDataMap) entity;
		
		this.setAddEndPathToValue(fileDataMap.getAddEndPathToValue());
		this.setConcatenate(fileDataMap.getConcatenate());
		this.setEntityClass(fileDataMap.getEntityClass());
		this.setEntityField(fileDataMap.getEntityField());
		this.setField(fileDataMap.getField());
		this.setFileFormat(fileDataMap.getFileFormat());
		this.setPathTransforms(fileDataMap.getPathTransforms());
		this.setSetPathToEnityField(fileDataMap.getSetPathToEnityField());
		this.setTransforms(fileDataMap.getTransforms());
				
	}
	
	public String getFileDataMapId()
	{
		return fileDataMapId;
	}

	public void setFileDataMapId(String fileDataMapId)
	{
		this.fileDataMapId = fileDataMapId;
	}

	public String getFileFormat()
	{
		return fileFormat;
	}

	public void setFileFormat(String fileFormat)
	{
		this.fileFormat = fileFormat;
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

	public String getSetPathToEnityField()
	{
		return setPathToEnityField;
	}

	public void setSetPathToEnityField(String setPathToEnityField)
	{
		this.setPathToEnityField = setPathToEnityField;
	}

	public List<DataMapTransform> getPathTransforms()
	{
		return pathTransforms;
	}

	public void setPathTransforms(List<DataMapTransform> pathTransforms)
	{
		this.pathTransforms = pathTransforms;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
