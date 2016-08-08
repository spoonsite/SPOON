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

import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
import edu.usu.sdl.openstorefront.core.model.AttributeAll;
import edu.usu.sdl.openstorefront.core.model.FileHistoryAll;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class AttributeMapper
	extends BaseMapper<AttributeAll>
{

	public AttributeMapper(DataTemplateEntity<AttributeAll> templateFactory, FileHistoryAll fileHistoryAll, Map<String, DataMapper> dataMappers, AttributeDataMapper attributeDataMapper)
	{
		super(templateFactory, fileHistoryAll, dataMappers, attributeDataMapper);
	}
	
	public AttributeMapper(DataTemplateEntity<AttributeAll> templateFactory, FileHistoryAll fileHistoryAll) 
	{
		super(templateFactory, fileHistoryAll);
	}

	@Override
	public List<AttributeAll> multiMapData(MapModel input)
	{
		List<AttributeAll> mappedAttributes = new ArrayList<>();
		doMapping(mappedAttributes, input, dataMappers, "", null);		
		return mappedAttributes;
	}
	
	private void doMapping(
			List<AttributeAll> mappedAttributes, 
			MapModel root, 
			Map<String, DataMapper> dataMappers, 
			String fieldPath,
			AttributeAll parentAttributeAll)
	{
		String rootPath = fieldPath + root.getName();
		DataMapper rootMapper = dataMappers.get(rootPath);
		AttributeAll attributeAll = parentAttributeAll;
		if (rootMapper != null) {
		
			if (AttributeType.class.getName().equals(rootMapper.getEntityClass().getName())) {
				attributeAll = templateFactory.createNewEntity();
				String typeCode = (String)rootMapper.applyTransforms(rootPath);
				attributeAll.getAttributeType().setAttributeType(typeCode);
				attributeAll.getAttributeType().setDescription(typeCode);				
			}	
			
			boolean add = false;
			Map<String, Object> entityMap = new HashMap<>();
			for (MapField field : root.getMapFields()) {
				String pathToField = fieldPath + root.getName() + "." + field.getName();
				DataMapper fieldMapper = dataMappers.get(pathToField);
				if (fieldMapper != null) {
					
					Object entity = entityMap.get(fieldMapper.getEntityClass().getName());					
					
					if (AttributeCode.class.getName().equals(fieldMapper.getEntityClass().getName())) {
						if (entity == null) {
							entity = new AttributeCode();
							AttributeCodePk attributeCodePk = new AttributeCodePk();
							attributeCodePk.setAttributeType(attributeAll.getAttributeType().getAttributeType());
							((AttributeCode)entity).setAttributeCodePk(attributeCodePk);
														
							attributeAll.getAttributeCodes().add(((AttributeCode)entity));
							entityMap.put(fieldMapper.getEntityClass().getName(), entity);							 
						}						
					} else if  (AttributeCodePk.class.getName().equals(fieldMapper.getEntityClass().getName())) {
						if (entity == null) {
							AttributeCode attributeCode = (AttributeCode) entityMap.get(AttributeCode.class.getName());
							if (attributeCode == null) {
								attributeCode = new AttributeCode();
								AttributeCodePk attributeCodePk = new AttributeCodePk();
								attributeCodePk.setAttributeType(attributeAll.getAttributeType().getAttributeType());
								attributeCode.setAttributeCodePk(attributeCodePk);
								entity = attributeCodePk;
								
								attributeAll.getAttributeCodes().add(attributeCode);
								entityMap.put(AttributeCode.class.getName(), attributeCode);	
							} else {
								AttributeCodePk attributeCodePk = attributeCode.getAttributeCodePk();
								entity = attributeCodePk;
							}
							entityMap.put(fieldMapper.getEntityClass().getName(), entity);	
						}						
					}
					
					if (entity != null) {					
						Object processedValue = fieldMapper.applyTransforms(field.getValue());
						if (fieldMapper.getAddEndPathToValue()) {
							Object fieldName = fieldMapper.applyPathTransforms(field.getName());
							processedValue = fieldName + ": " + processedValue;
						}
					
						try {
							if (fieldMapper.getUseAsAttributeLabel()) {
								BeanUtils.setProperty(entity, AttributeCode.FIELD_LABEL, processedValue);
							}
							if (fieldMapper.getConcatenate()) {
								String existing = BeanUtils.getProperty(entity, fieldMapper.getEntityField());
								if (existing != null) {
									processedValue = existing + " <br>" + processedValue;
								}
							}
							
							BeanUtils.setProperty(entity, fieldMapper.getEntityField(), processedValue);
							add = true;
						} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
							fileHistoryAll.addError(FileHistoryErrorType.MAPPING, pathToField);						
						} 
					} else {
						fileHistoryAll.addError(FileHistoryErrorType.MAPPING, "Entity: " + fieldMapper.getEntityClass().getName() + " is not supported.");	
					}
						
				}	
			}

			if (add) {
				mappedAttributes.add(attributeAll);
			}
		}
		
		for (MapModel child : root.getArrayFields()) {
			String newParent = root.getName() + ".";
			if (StringUtils.isNotBlank(fieldPath)) {				
				newParent = fieldPath + root.getName() + ".";
			}
			doMapping(mappedAttributes, child, dataMappers, newParent, attributeAll);
		}		
	}

}
