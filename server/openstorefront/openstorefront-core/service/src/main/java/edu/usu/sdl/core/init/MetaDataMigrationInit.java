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

import java.util.logging.Logger;

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

//		Component componentExample = new Component();
//		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
//
//		List<Component> components = componentExample.findByExample();
//		Set<String> addedAttributes = new HashSet<>();
//		for (Component component : components) {
//			LOG.log(Level.INFO, "Working on: " + component.getName());
//
//			ComponentMetadata componentMetadataExample = new ComponentMetadata();
//			componentMetadataExample.setActiveStatus(ComponentMetadata.ACTIVE_STATUS);
//			componentMetadataExample.setComponentId(component.getComponentId());
//
//			List<ComponentMetadata> componentMetaData = componentMetadataExample.findByExampleProxy();
//			for (ComponentMetadata metaData : componentMetaData) {
//
//				String fullKey = metaData.uniqueKey();
//
//				CleanKeySanitizer sanitizer = new CleanKeySanitizer();
//
//				String attributeType = sanitizer.santize(StringUtils.left(metaData.getLabel().toUpperCase(), OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)).toString();
//				String attributeCode = sanitizer.santize(StringUtils.left(metaData.getValue().toUpperCase(), OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)).toString();
//
//				if (addedAttributes.contains(fullKey) == false) {
//					//create attribute
//					AttributeType attributeTypeFull = new AttributeType();
//					attributeTypeFull.setAttributeType(attributeType);
//					attributeTypeFull.setDescription(metaData.getLabel());
//					attributeTypeFull.setAllowUserGeneratedCodes(Boolean.TRUE);
//					attributeTypeFull.setAllowMultipleFlg(Boolean.TRUE);
//					attributeTypeFull.setArchitectureFlg(Boolean.FALSE);
//					attributeTypeFull.setHideOnSubmission(Boolean.FALSE);
//					attributeTypeFull.setImportantFlg(Boolean.FALSE);
//					attributeTypeFull.setRequiredFlg(Boolean.FALSE);
//					attributeTypeFull.setVisibleFlg(Boolean.FALSE);
//
//					//determine value type
//					BigDecimal numberValue = Convert.toBigDecimal(metaData.getValue());
//					if (numberValue != null) {
//						//attributeTypeFull.setAttributeValueType(Attribute);
//					}
//
//					service.getAttributeService().saveAttributeType(attributeTypeFull, false);
//
//					addedAttributes.add(fullKey);
//				}
//
//				service.getPersistenceService().delete(metaData);
//
//				ComponentAttribute componentAttribute = new ComponentAttribute();
//				ComponentAttributePk componentAttributePk = new ComponentAttributePk();
//				componentAttributePk.setAttributeType(attributeType);
//				componentAttributePk.setAttributeCode(attributeCode);
//				componentAttributePk.setComponentId(component.getComponentId());
//				componentAttribute.setComponentAttributePk(componentAttributePk);
//				service.getPersistenceService().persist(componentAttribute);
//			}
//		}
//		service.getSearchService().resetIndexer();
//
//		results.append("Migrated ")
//				.append(components.size())
//				.append(" Component. Added ")
//				.append(addedAttributes.size())
//				.append(" Attributes ");
		return results.toString();

	}

}
