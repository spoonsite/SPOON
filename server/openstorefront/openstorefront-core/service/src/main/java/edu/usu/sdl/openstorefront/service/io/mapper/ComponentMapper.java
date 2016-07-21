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
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ComponentMapper
	extends BaseMapper<ComponentAll>
{	

	public ComponentMapper(DataTemplateEntity<ComponentAll> templateFactory)
	{
		super(templateFactory);
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
