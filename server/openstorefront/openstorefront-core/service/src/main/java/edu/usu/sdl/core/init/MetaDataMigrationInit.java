/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.core.init;

import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.AttributeValueType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentMetadata;
import edu.usu.sdl.openstorefront.validation.CleanKeySanitizer;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * Remove after 2.4
 *
 * @author dshurtleff
 */
public class MetaDataMigrationInit
		extends ApplyOnceInit
{

	private static final Logger LOG = Logger.getLogger(MetaDataMigrationInit.class.getName());

	public MetaDataMigrationInit()
	{
		super("Migrate Metadata");
	}

	@Override
	protected String internalApply()
	{
		StringBuilder results = new StringBuilder();

		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);

		List<Component> components = componentExample.findByExample();
		Set<String> addedAttributes = new HashSet<>();
		for (Component component : components) {
			LOG.log(Level.INFO, MessageFormat.format("Working on: {0}", component.getName()));

			ComponentMetadata componentMetadataExample = new ComponentMetadata();
			componentMetadataExample.setActiveStatus(ComponentMetadata.ACTIVE_STATUS);
			componentMetadataExample.setComponentId(component.getComponentId());

			List<ComponentMetadata> componentMetaData = componentMetadataExample.findByExampleProxy();
			for (ComponentMetadata metaData : componentMetaData) {

				String fullKey = metaData.uniqueKey();

				CleanKeySanitizer sanitizer = new CleanKeySanitizer();

				String value = metaData.getValue().trim();
				String label = metaData.getLabel();
				if (value.contains("%")) {
					value = value.replace("%", "");
					label += " %";
				}

				String attributeType = sanitizer.santize(StringUtils.left(label.toUpperCase(), OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)).toString();
				String attributeCode = sanitizer.santize(StringUtils.left(value.toUpperCase(), OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)).toString();

				if (addedAttributes.contains(fullKey) == false) {
					//create attribute
					AttributeType attributeTypeFull = new AttributeType();
					attributeTypeFull.setAttributeType(attributeType);
					attributeTypeFull.setDescription(label);
					attributeTypeFull.setAllowUserGeneratedCodes(Boolean.TRUE);
					attributeTypeFull.setAllowMultipleFlg(Boolean.TRUE);
					attributeTypeFull.setArchitectureFlg(Boolean.FALSE);
					attributeTypeFull.setHideOnSubmission(Boolean.FALSE);
					attributeTypeFull.setImportantFlg(Boolean.FALSE);
					attributeTypeFull.setRequiredFlg(Boolean.FALSE);
					attributeTypeFull.setVisibleFlg(Boolean.FALSE);

					BigDecimal numberValue = Convert.toBigDecimal(value);
					if (numberValue != null) {
						attributeTypeFull.setAttributeValueType(AttributeValueType.NUMBER);
					} else {
						attributeTypeFull.setAttributeValueType(AttributeValueType.TEXT);
					}
					service.getAttributeService().saveAttributeType(attributeTypeFull, false);

					AttributeCode attributeCodeFull = new AttributeCode();
					AttributeCodePk attributeCodePk = new AttributeCodePk();
					attributeCodePk.setAttributeCode(attributeCode);
					attributeCodePk.setAttributeType(attributeType);
					attributeCodeFull.setAttributeCodePk(attributeCodePk);
					attributeCodeFull.setLabel(value);
					service.getAttributeService().saveAttributeCode(attributeCodeFull, false);

					addedAttributes.add(fullKey);
				}

				ComponentAttribute componentAttribute = new ComponentAttribute();
				ComponentAttributePk componentAttributePk = new ComponentAttributePk();
				componentAttributePk.setAttributeType(attributeType);
				componentAttributePk.setAttributeCode(attributeCode);
				componentAttributePk.setComponentId(component.getComponentId());
				componentAttribute.setComponentAttributePk(componentAttributePk);
				componentAttribute.setComponentId(component.getComponentId());
				service.getComponentService().saveComponentAttribute(componentAttribute, false);

				service.getPersistenceService().delete(metaData);
			}
		}
		LOG.log(Level.INFO, "Updating Index");
		service.getSearchService().resetIndexer();

		results.append("Migrated ")
				.append(components.size())
				.append(" Component. Added ")
				.append(addedAttributes.size())
				.append(" Attributes ");

		LOG.log(Level.INFO, results.toString());
		return results.toString();

	}

}
