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
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentMetadata;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
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

	public ComponentMapper(DataTemplateEntity<ComponentAll> templateFactory, FileHistoryAll fileHistoryAll, Map<String, DataMapper> dataMappers, AttributeDataMapper attributeDataMapper)
	{
		super(templateFactory, fileHistoryAll, dataMappers, attributeDataMapper);
	}

	public ComponentMapper(DataTemplateEntity<ComponentAll> templateFactory, FileHistoryAll fileHistoryAll)
	{
		super(templateFactory, fileHistoryAll);
	}

	@Override
	public List<ComponentAll> multiMapData(MapModel input)
	{
		List<ComponentAll> mappedComponents = new ArrayList<>();
		Map<String, ComponentAll> componentMap = new HashMap<>();
		doMapping(componentMap, input, dataMappers, "", null);
		mappedComponents.addAll(componentMap.values());

		//apply attribute mappings
		if (attributeDataMapper != null) {
			for (ComponentAll componentAll : mappedComponents) {
				doAttributeMapping(componentAll);
			}
		}

		return mappedComponents;
	}

	private void doAttributeMapping(ComponentAll componentAll)
	{
		for (ComponentAttribute componentAttribute : componentAll.getAttributes()) {
			AttributeTypeMapper attributeTypeMapper = attributeDataMapper.getAttributeMap().get(componentAttribute.getComponentAttributePk().getAttributeType());
			if (attributeTypeMapper != null) {
				componentAttribute.getComponentAttributePk().setAttributeType(attributeTypeMapper.getAttributeType());
				String ourAttributeCode = attributeTypeMapper.getCodeMap().get(componentAttribute.getComponentAttributePk().getAttributeCode());
				if (StringUtils.isNotBlank(ourAttributeCode)) {
					componentAttribute.getComponentAttributePk().setAttributeCode(ourAttributeCode);
				} else if (attributeTypeMapper.getAddMissingCode()) {
					AttributeCode attributeCode = createAttributeCode(componentAttribute.getComponentAttributePk().getAttributeType(), componentAttribute.getComponentAttributePk().getAttributeCode());
					componentAttribute.getComponentAttributePk().setAttributeType(attributeCode.getAttributeCodePk().getAttributeType());
					componentAttribute.getComponentAttributePk().setAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode());
				} else if (StringUtils.isNotBlank(attributeTypeMapper.getDefaultMappedCode())) {
					componentAttribute.getComponentAttributePk().setAttributeCode(attributeTypeMapper.getDefaultMappedCode());
				}

			} else if (attributeDataMapper.getAddMissingAttributeTypeFlg()) {
				AttributeType attributeType = createAttributeType(componentAttribute.getComponentAttributePk().getAttributeType());
				AttributeCode attributeCode = createAttributeCode(attributeType.getAttributeType(), componentAttribute.getComponentAttributePk().getAttributeCode());

				componentAttribute.getComponentAttributePk().setAttributeType(attributeCode.getAttributeCodePk().getAttributeType());
				componentAttribute.getComponentAttributePk().setAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode());
			}
		}
	}

	@SuppressWarnings({"squid:S3923", "squid:S1872"})
	private void doMapping(
			Map<String, ComponentAll> componentMap,
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

			boolean add = false;
			Map<String, Object> entityMap = new HashMap<>();
			for (MapField field : root.getMapFields()) {
				String pathToField = fieldPath + root.getName() + "." + field.getName();
				DataMapper fieldMapper = dataMappers.get(pathToField);
				if (fieldMapper != null) {
					add = mapField(componentAll, dataMappers, entityMap, field, pathToField);
				}
			}

			if (add) {
				componentMap.put(componentAll.toString(), componentAll);
			}
		}

		for (MapModel child : root.getArrayFields()) {
			String newParent = root.getName() + ".";
			if (StringUtils.isNotBlank(fieldPath)) {
				newParent = fieldPath + root.getName() + ".";
			}
			doMapping(componentMap, child, dataMappers, newParent, componentAll);
		}

	}

	@Override
	public ComponentAll singleMapData(MapModel input)
	{
		Map<String, DataMapper> dataMapper = this.dataMappers;

		ComponentAll componentAll = null;
		if (input != null) {
			componentAll = templateFactory.createNewEntity();

			Map<String, Object> entityMap = new HashMap<>();
			for (MapField mapField : input.getMapFields()) {
				String pathToField = input.getName() + "." + mapField.getName();
				if (dataMapper.containsKey(pathToField)) {
					mapField(componentAll, dataMappers, entityMap, mapField, pathToField);
				}
			}

			//apply attribute mappings
			if (attributeDataMapper != null) {
				doAttributeMapping(componentAll);
			}

		}
		return componentAll;
	}

	@SuppressWarnings({"squid:S3923", "squid:S1872"})
	private boolean mapField(
			ComponentAll componentAll,
			Map<String, DataMapper> dataMappers,
			Map<String, Object> entityMap,
			MapField mapField,
			String pathToField
	)
	{
		boolean add = false;

		DataMapper fieldMapper = dataMappers.get(pathToField);
		Object entity = entityMap.get(fieldMapper.getEntityClass().getName());

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
			entity = new ComponentTag();
			entityMap.put(fieldMapper.getEntityClass().getName(), entity);
			componentAll.getTags().add((ComponentTag) entity);
		} else if (ComponentMetadata.class.getName().equals(fieldMapper.getEntityClass().getName())) {
			entity = new ComponentMetadata();
			entityMap.put(fieldMapper.getEntityClass().getName(), entity);
			componentAll.getMetadata().add((ComponentMetadata) entity);

		} else if (ComponentAttribute.class.getName().equals(fieldMapper.getEntityClass().getName())
				|| ComponentAttributePk.class.getName().equals(fieldMapper.getEntityClass().getName())) {
			//create everytime

			ComponentAttribute componentAttribute = new ComponentAttribute();
			entity = new ComponentAttributePk();
			componentAttribute.setComponentAttributePk((ComponentAttributePk) entity);

			componentAll.getAttributes().add(componentAttribute);
		}

		if (entity != null) {
			try {
				if (StringUtils.isNotBlank(fieldMapper.getPathToEnityField())) {
					Object processedPath = fieldMapper.applyPathTransforms(pathToField);
					BeanUtils.setProperty(entity, fieldMapper.getPathToEnityField(), processedPath);
					add = true;
				}
				Object processedValue = fieldMapper.applyTransforms(mapField.getValue());

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
		return add;
	}

}
