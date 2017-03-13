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
@APIDescription("Used to store type mapping between external attributes and internal")
@Embeddable
public class FileAttributeTypeXrefMap
		implements Serializable
{

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@FK(AttributeType.class)
	private String attributeType;

	@ConsumeField
	private String defaultMappedCode;

	@ConsumeField
	private Boolean addMissingCode;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String externalType;

	@ConsumeField
	@Embedded
	@OneToMany(orphanRemoval = true)
	@DataType(FileAttributeCodeXrefMap.class)
	private List<FileAttributeCodeXrefMap> attributeCodeXrefMap;
	
	@Version
	private String storageVersion;	
	
	public FileAttributeTypeXrefMap()
	{
	}

	public String getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	public String getDefaultMappedCode()
	{
		return defaultMappedCode;
	}

	public void setDefaultMappedCode(String defaultMappedCode)
	{
		this.defaultMappedCode = defaultMappedCode;
	}

	public Boolean getAddMissingCode()
	{
		return addMissingCode;
	}

	public void setAddMissingCode(Boolean addMissingCode)
	{
		this.addMissingCode = addMissingCode;
	}

	public String getExternalType()
	{
		return externalType;
	}

	public void setExternalType(String externalType)
	{
		this.externalType = externalType;
	}

	public List<FileAttributeCodeXrefMap> getAttributeCodeXrefMap()
	{
		return attributeCodeXrefMap;
	}

	public void setAttributeCodeXrefMap(List<FileAttributeCodeXrefMap> attributeCodeXrefMap)
	{
		this.attributeCodeXrefMap = attributeCodeXrefMap;
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
