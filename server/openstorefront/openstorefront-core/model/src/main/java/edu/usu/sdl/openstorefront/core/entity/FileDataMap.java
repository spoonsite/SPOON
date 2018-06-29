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
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds a list of field mappings")
public class FileDataMap
		extends StandardEntity<FileDataMap>
{

	@PK(generated = true)
	@NotNull
	private String fileDataMapId;

	@NotNull
	@ConsumeField
	@FK(value = FileFormat.class, referencedField = "code")
	@APIDescription("May be a built in format or a format from a plugin which may not be loaded.")
	private String fileFormat;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String name;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@FK(value = ComponentType.class, referencedField = "componentType")
	private String defaultComponentType;

	@ConsumeField
	@Embedded
	@OneToMany(orphanRemoval = true)
	@DataType(FileDataMapField.class)
	private List<FileDataMapField> dataMapFields;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public FileDataMap()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		FileDataMap fileDataMap = (FileDataMap) entity;

		this.setName(fileDataMap.getName());
		this.setDefaultComponentType(fileDataMap.getDefaultComponentType());
		this.setDataMapFields(fileDataMap.getDataMapFields());

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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<FileDataMapField> getDataMapFields()
	{
		return dataMapFields;
	}

	public void setDataMapFields(List<FileDataMapField> dataMapFields)
	{
		this.dataMapFields = dataMapFields;
	}

	public String getDefaultComponentType()
	{
		return defaultComponentType;
	}

	public void setDefaultComponentType(String defaultComponentType)
	{
		this.defaultComponentType = defaultComponentType;
	}

}
