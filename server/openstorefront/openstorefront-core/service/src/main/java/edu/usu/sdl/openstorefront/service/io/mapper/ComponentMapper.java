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
package edu.usu.sdl.openstorefront.service.io.mapper;

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentMetadata;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.FileHistoryAll;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ComponentMapper
	extends BaseMapper<ComponentAll>
{	
	private static final Logger LOG = Logger.getLogger(ComponentMapper.class.getName());
	
	public ComponentMapper(DataTemplateEntity<ComponentAll> templateFactory, FileHistoryAll fileHistoryAll)
	{
		super(templateFactory, fileHistoryAll);
	}
	
	@Override
	public List<ComponentAll> mapData(MapModel input, Map<String, DataMapper> dataMappers)
	{
		List<ComponentAll> mappedComponents = new ArrayList<>();		
		doMapping(mappedComponents, input, dataMappers, "", null);		
		return mappedComponents;
	}
	
	private void doMapping(
			List<ComponentAll> mappedComponents, 
			MapModel root, 
			Map<String, DataMapper> dataMappers, 
			String fieldPath,
			ComponentAll parentComponentAll
	)
	{
		String rootPath = fieldPath + root.getName();
		DataMapper rootMapper = dataMappers.get(rootPath);
		
		ComponentAll componentAll = parentComponentAll;
		if (rootMapper != null) {
			
			
			if (ComponentAll.class.getName().equals(rootMapper.getEntityClass().getName())) {
				componentAll = templateFactory.createNewEntity();
			}			

			if (StringUtils.isNotBlank(fieldPath)) {
				fieldPath = fieldPath + ".";
			}
			
			boolean add = false;
			Map<String, StandardEntity> entityMap = new HashMap<>();
			for (MapField field : root.getMapFields()) {
				String pathToField = fieldPath + root.getName() + "." + field.getName();
				DataMapper fieldMapper = dataMappers.get(pathToField);
				if (fieldMapper != null) {
					
					StandardEntity entity = entityMap.get(fieldMapper.getEntityClass().getName());					
					
					if (Component.class.getName().equals(fieldMapper.getEntityClass().getName())) {
						if (entity == null) {
							entity = componentAll.getComponent();
							entityMap.put(fieldMapper.getEntityClass().getName(), entity);							 
						}
						
					} else if (ComponentContact.class.getName().equals(fieldMapper.getEntityClass().getName())) {
						if (entity == null) {
							entity = new ComponentContact();
							entityMap.put(fieldMapper.getEntityClass().getName(), entity);							 
							componentAll.getContacts().add((ComponentContact) entity);
						}
					} else if (ComponentResource.class.getName().equals(fieldMapper.getEntityClass().getName())) {
						if (entity == null) {
							entity = new ComponentResource();
							entityMap.put(fieldMapper.getEntityClass().getName(), entity);							 
							componentAll.getResources().add((ComponentResource) entity);
						}						
					} else if (ComponentMedia.class.getName().equals(fieldMapper.getEntityClass().getName())) {
						if (entity == null) {
							entity = new ComponentMedia();
							entityMap.put(fieldMapper.getEntityClass().getName(), entity);							 
							componentAll.getMedia().add((ComponentMedia) entity);
						}						
					} else if (ComponentTag.class.getName().equals(fieldMapper.getEntityClass().getName())) {
						if (entity == null) {
							entity = new ComponentTag();
							entityMap.put(fieldMapper.getEntityClass().getName(), entity);							 
							componentAll.getTags().add((ComponentTag) entity);
						}						
					} else if (ComponentMetadata.class.getName().equals(fieldMapper.getEntityClass().getName())) {
						if (entity == null) {
							entity = new ComponentMetadata();
							entityMap.put(fieldMapper.getEntityClass().getName(), entity);							 
							componentAll.getMetadata().add((ComponentMetadata) entity);
						}						
					} else if (ComponentMetadata.class.getName().equals(fieldMapper.getEntityClass().getName())) {
						if (entity == null) {
							entity = new ComponentMetadata();
							entityMap.put(fieldMapper.getEntityClass().getName(), entity);							 
							componentAll.getMetadata().add((ComponentMetadata) entity);
						}						
					} 
					
					if (entity != null) {					
						Object processedValue = fieldMapper.applyTransforms(field.getValue());					
					
						try {
							BeanUtils.setProperty(entity, fieldMapper.getEntityField(), processedValue);
						} catch (IllegalAccessException | InvocationTargetException ex) {
							fileHistoryAll.addError(FileHistoryErrorType.MAPPING, pathToField);						
						}
					} else {
						fileHistoryAll.addError(FileHistoryErrorType.MAPPING, "Entity: " + fieldMapper.getEntityClass().getName() + " is not supported.");	
					}
						
				}	
			}

			if (add) {
				mappedComponents.add(componentAll);
			}
		}

		for (MapModel child : root.getArrayFields()) {
			String newParent = root.getName();
			if (StringUtils.isNotBlank(fieldPath)) {
				newParent = fieldPath + root.getName();
			}
			doMapping(mappedComponents, child, dataMappers, newParent, componentAll);
		}
		
	}

}
