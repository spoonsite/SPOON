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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Used to store the mapping between external attributes and internal")
public class FileAttributeMap
	extends StandardEntity<FileAttributeMap>
{
	@PK(generated = true)
	@NotNull
	private String fileAttributeMapId;
	
	
	@NotNull
	@FK(FileDataMap.class)
	private String fileDataMapId;
	
	@NotNull
	@ConsumeField	
	private Boolean addMissingAttributeTypeFlg;
	
	@ConsumeField
	@Embedded
	@OneToMany(orphanRemoval = true)
	@DataType(FileAttributeTypeXrefMap.class)
	private List<FileAttributeTypeXrefMap> attributeTypeXrefMap;

	public FileAttributeMap()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity); 
		
		FileAttributeMap fileAttributeMap = (FileAttributeMap) entity;
		
		fileAttributeMap.setAddMissingAttributeTypeFlg(fileAttributeMap.getAddMissingAttributeTypeFlg());
		fileAttributeMap.setAttributeTypeXrefMap(fileAttributeMap.getAttributeTypeXrefMap());
				
	}
	

	public Boolean getAddMissingAttributeTypeFlg()
	{
		return addMissingAttributeTypeFlg;
	}

	public void setAddMissingAttributeTypeFlg(Boolean addMissingAttributeTypeFlg)
	{
		this.addMissingAttributeTypeFlg = addMissingAttributeTypeFlg;
	}

	public String getFileAttributeMapId()
	{
		return fileAttributeMapId;
	}

	public void setFileAttributeMapId(String fileAttributeMapId)
	{
		this.fileAttributeMapId = fileAttributeMapId;
	}

	public String getFileDataMapId()
	{
		return fileDataMapId;
	}

	public void setFileDataMapId(String fileDataMapId)
	{
		this.fileDataMapId = fileDataMapId;
	}

	public List<FileAttributeTypeXrefMap> getAttributeTypeXrefMap()
	{
		return attributeTypeXrefMap;
	}

	public void setAttributeTypeXrefMap(List<FileAttributeTypeXrefMap> attributeTypeXrefMap)
	{
		this.attributeTypeXrefMap = attributeTypeXrefMap;
	}

}
