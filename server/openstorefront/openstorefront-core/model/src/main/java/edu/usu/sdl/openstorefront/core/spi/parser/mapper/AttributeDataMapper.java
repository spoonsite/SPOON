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
package edu.usu.sdl.openstorefront.core.spi.parser.mapper;

import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.entity.FileAttributeCodeXrefMap;
import edu.usu.sdl.openstorefront.core.entity.FileAttributeMap;
import edu.usu.sdl.openstorefront.core.entity.FileAttributeTypeXrefMap;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
public class AttributeDataMapper
{
	private boolean addMissingAttributeTypeFlg;
	private Map<String, AttributeTypeMapper> attributeMap = new HashMap<>();

	public AttributeDataMapper()
	{
	}
	
	public static AttributeDataMapper toDataMapper(FileAttributeMap fileAttributeMap)
	{
		AttributeDataMapper attributeDataMapper = new AttributeDataMapper();
		
		attributeDataMapper.setAddMissingAttributeTypeFlg(Convert.toBoolean(fileAttributeMap.getAddMissingAttributeTypeFlg()));
		
		if (fileAttributeMap.getAttributeTypeXrefMap() != null) {
			for (FileAttributeTypeXrefMap fileAttributeTypeXrefMap :  fileAttributeMap.getAttributeTypeXrefMap()) 
			{
				AttributeTypeMapper  attributeTypeMapper = new AttributeTypeMapper();
				attributeTypeMapper.setAddMissingCode(Convert.toBoolean(fileAttributeTypeXrefMap.getAddMissingCode()));
				attributeTypeMapper.setAttributeType(fileAttributeTypeXrefMap.getAttributeType());
				attributeTypeMapper.setDefaultMappedCode(fileAttributeTypeXrefMap.getDefaultMappedCode());
				
				if (fileAttributeTypeXrefMap.getAttributeCodeXrefMap() != null) {
					
					for (FileAttributeCodeXrefMap codeXrefMap : fileAttributeTypeXrefMap.getAttributeCodeXrefMap()) {
						attributeTypeMapper.getCodeMap().put(codeXrefMap.getExternalCode(), codeXrefMap.getAttributeCode());
					}					
				}
								
				attributeDataMapper.getAttributeMap().put(fileAttributeTypeXrefMap.getExternalType(), attributeTypeMapper);
			}	
		}
		return attributeDataMapper;
	}
	
	public Map<String, AttributeTypeMapper> getAttributeMap()
	{
		return attributeMap;
	}

	public void setAttributeMap(Map<String, AttributeTypeMapper> attributeMap)
	{
		this.attributeMap = attributeMap;
	}

	public boolean getAddMissingAttributeTypeFlg()
	{
		return addMissingAttributeTypeFlg;
	}

	public void setAddMissingAttributeTypeFlg(boolean addMissingAttributeTypeFlg)
	{
		this.addMissingAttributeTypeFlg = addMissingAttributeTypeFlg;
	}

}
