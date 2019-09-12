/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.search;

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.model.search.ResultAttributeStat;
import edu.usu.sdl.openstorefront.core.model.search.ResultCodeStat;
import edu.usu.sdl.openstorefront.core.model.search.ResultOrganizationStat;
import edu.usu.sdl.openstorefront.core.model.search.ResultTagStat;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 *
 * @author rfrazier
 */
public class SearchStatTable
{

	private final static Logger LOG = Logger.getLogger(SearchStatTable.class.getName());
	private Map<String, List<ComponentAttribute>> attributeMap = new HashMap<>();
	private Map<String, String> organizationMap = new HashMap<>();
	private Map<String, List<ComponentTag>> tagMap = new HashMap<>();

	private static final String CACHE_KEY = "SearchStatTable";
	private static final int MAX_ATTRIBUTE_QUERY_RESULTS = 5000;
	private static final int MAX_TAG_QUERY_RESULTS = 1000;

	/**
	 * Flag of when a Component is approved for knowing when to refresh the cache
	 * Refreshing the cache every time a new component is approved allows for tags, Units/Attributes,
	 *  & organizations to be instantly searchable, with search performance tradeoff if components are
	 * approved right in the middle of it. 
	 * 
	 */
	private static boolean isThereNewAttributeTypeSaved = false;
	private static boolean isThereNewTagSaved = false;

	public SearchStatTable()
	{
		// pull hashMaps from the cache
		Cache appCache = OSFCacheManager.getApplicationCache();
		Element element = appCache.get(CACHE_KEY);
		if (element != null) {
			SearchStatTable cachedTable = (SearchStatTable) element.getObjectValue();
			
			// Return cached info unless data has changed
			// Attribute Types 
			if (isThereNewAttributeTypeSaved){
				isThereNewAttributeTypeSaved = false;
				index(false, true, false);
			} else {
				this.attributeMap = cachedTable.getAttributeMap();
			}
			
			// Tags
			if (isThereNewTagSaved){
				isThereNewTagSaved = false;
				index(true, false, false);
			} else {
				this.tagMap = cachedTable.getTagMap();
			}
			
			// Organization are currently not a filterable category on the search page, therefore we don't check if
			// there are new organizations added since the server started.
			this.organizationMap = cachedTable.getOrganizationMap();
			
		} else {
			// First time search, index entire search results
			index();
		}
	}

	public SearchStatTable(Map<String, List<ComponentAttribute>> attributeMap, Map<String, List<ComponentTag>> tagMap)
	{
		this.attributeMap = attributeMap;
		this.tagMap = tagMap;
	}

	public SearchStatTable(Map<String, List<ComponentAttribute>> attributeMap, Map<String, List<ComponentTag>> tagMap, Map<String, String> organizationMap)
	{
		this.attributeMap = attributeMap;
		this.tagMap = tagMap;
		this.organizationMap = organizationMap;
	}

	public SearchStatTable getTable()
	{
		Cache appCache = OSFCacheManager.getApplicationCache();
		Element element = appCache.get(CACHE_KEY);
		if (element == null) {
			index();
			element = appCache.get(CACHE_KEY); // refetch after the index
		}
		return (SearchStatTable) element.getObjectValue();
	}

	/**
	 * Creates an index of tags, attributes, and organizations. The StatTable is
	 * then stored in the applicationCache. To be called with the search engine
	 * index.
	 */
	public void index()
	{
		index(true,true,true);
	}

	public void index(boolean doTags, boolean doAttributes, boolean doOrganizations)
	{
		boolean changed = false;

		Service service = ServiceProxyFactory.getServiceProxy();

		if (tagMap.isEmpty() && doTags) {
			//window query 0 to total 1000 at a time

			ComponentTag tagExample = new ComponentTag();
			tagExample.setActiveStatus(ComponentTag.ACTIVE_STATUS);

			long maxTags = service.getPersistenceService().countByExample(tagExample);

			int startIndex = 0;
			while (startIndex < maxTags) {
				QueryByExample<ComponentTag> queryByExample = new QueryByExample<>(tagExample);
				queryByExample.setFirstResult(startIndex);
				queryByExample.setMaxResults(MAX_ATTRIBUTE_QUERY_RESULTS);
				List<ComponentTag> tags = service.getPersistenceService().queryByExample(queryByExample);
				tagMap.putAll(buildMap(tags, ComponentTag::getComponentId));

				startIndex += MAX_ATTRIBUTE_QUERY_RESULTS;
			}

			//List<ComponentTag> tags = tagExample.findByExample();
			//tagMap = buildMap(tags, ComponentTag::getComponentId);
			changed = true;
		}

		if (attributeMap.isEmpty() && doAttributes) {
			//window query 0 to total 5000 at a time
			ComponentAttribute attributeExample = new ComponentAttribute();
			attributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
			long maxAttributes = service.getPersistenceService().countByExample(attributeExample);

			int startIndex = 0;
			while (startIndex < maxAttributes) {
				QueryByExample<ComponentAttribute> queryByExample = new QueryByExample<>(attributeExample);
				queryByExample.setFirstResult(startIndex);
				queryByExample.setMaxResults(MAX_ATTRIBUTE_QUERY_RESULTS);
				List<ComponentAttribute> attributes = service.getPersistenceService().queryByExample(queryByExample);
				attributeMap.putAll(buildMap(attributes, ComponentAttribute::getComponentId));

				startIndex += MAX_ATTRIBUTE_QUERY_RESULTS;
			}
			changed = true;
		}

		if (organizationMap.isEmpty() && doOrganizations) {
			Component componentExample = new Component();
			componentExample.setActiveStatus(Component.ACTIVE_STATUS);
			componentExample.setApprovalState(ApprovalStatus.APPROVED);
			List<Component> components = componentExample.findByExample(); // for organizations -- get approved status

			for (Component component : components) {
				organizationMap.put(component.getComponentId(), component.getOrganization());
			}
			changed = true;
		}

		if (changed) {
			Element element = new Element(CACHE_KEY, this);
			Cache appCache = OSFCacheManager.getApplicationCache();
			appCache.remove(CACHE_KEY);
			OSFCacheManager.getApplicationCache().put(element);
		}
	}

	private <T> Map<String, List<T>> buildMap(List<T> components, Function<T, String> getComponentId)
	{
		Map<String, List<T>> result = new HashMap<>();
		for (T component : components) {
			if (result.containsKey(getComponentId.apply(component))) {
				List<T> tempList = result.get(getComponentId.apply(component));
				tempList.add(component);
				result.put(getComponentId.apply(component), tempList);
			} else {
				result.put(getComponentId.apply(component), new ArrayList<>(Arrays.asList(component)));
			}
		}
		return result;
	}

	public void clear()
	{
		tagMap.clear();
		organizationMap.clear();
		attributeMap.clear();
	}

	/**
	 * @return map of attribute stats where the attribute uniqueKey is the key
	 */
	public Map<String, ResultAttributeStat> getAttributeStats(List<String> components)
	{
		Map<String, ResultAttributeStat> resultMap = new HashMap<>();

		for (String component : components) {
			List<ComponentAttribute> attributes = attributeMap.get(component);
			if (attributes != null) {
				for (ComponentAttribute attribute : attributes) {
					// for every attribute store the stat
					// the codes(i.e. attribute values) are stored in a separate map inside ResultAttributeStat
					String key = attribute.getComponentAttributePk().getAttributeType(); //gather stats by attributeType
					if (resultMap.containsKey(key)) {
						ResultAttributeStat attrStat = resultMap.get(key);
						Map<String, ResultCodeStat> codeMap = attrStat.getCodeMap();

						attrStat.setCodeMap(updateCodeStatMap(codeMap, attribute));
						attrStat.incrementCount();
					} else {
						// create a new attribute stat
						ResultAttributeStat attrStat = new ResultAttributeStat();

						Service service = ServiceProxyFactory.getServiceProxy();
						AttributeType type = service.getAttributeService().findType(attribute.getComponentAttributePk().getAttributeType());
						if (type == null) {
							type = service.getPersistenceService().findById(AttributeType.class, attribute.getComponentAttributePk().getAttributeType());
						} else {
							attrStat.setAttributeTypeLabel(type.getDescription());
						}
						String attributeUnit = type.getAttributeUnit();
						String attributeType = attribute.getComponentAttributePk().getAttributeType();

						attrStat.setAttributeUnit(attributeUnit);
						attrStat.setAttributeType(attributeType);

						// for every attribute code (i.e. attribute value) store the result in the map
						Map<String, ResultCodeStat> codeMap = attrStat.getCodeMap();
						attrStat.setCodeMap(updateCodeStatMap(codeMap, attribute));

						resultMap.put(key, attrStat);
					}
				}
			}
		}

		return resultMap;
	}

	private Map<String, ResultCodeStat> updateCodeStatMap(Map<String, ResultCodeStat> codeMap, ComponentAttribute attribute)
	{
		Service service = ServiceProxyFactory.getServiceProxy();
		String attributeCode = attribute.getComponentAttributePk().getAttributeCode();
		String attributeType = attribute.getComponentAttributePk().getAttributeType();

		ResultCodeStat codeStat;
		if (codeMap.containsKey(attributeCode)) {
			codeStat = codeMap.get(attributeCode);
			codeStat.incrementCount();
		} else {
			codeStat = new ResultCodeStat();
			// populate the new code stat with the value
			AttributeCodePk codePk = new AttributeCodePk();
			codePk.setAttributeCode(attributeCode);
			codePk.setAttributeType(attributeType);

			AttributeCode attrCode = service.getAttributeService().findCodeForType(codePk);

			if (attrCode == null) {
				codeStat.setCodeLabel("LabelNotAvailable");
				LOG.log(Level.WARNING, ()
						-> "Could not find Code Label for Component: "
						+ " Component Code: " + attribute.getComponentAttributePk().getAttributeCode()
						+ " Component Type: " + attribute.getComponentAttributePk().getAttributeType()
				);
			} else {
				codeStat.setCodeLabel(attrCode.getLabel());
			}

			codeMap.put(attributeCode, codeStat);
		}

		return codeMap;
	}

	public List<ResultOrganizationStat> getOrganizationStats(List<String> components)
	{
		Map<String, ResultOrganizationStat> resultMap = new HashMap<>();

		for (String component : components) {
			String key = organizationMap.get(component);
			if (resultMap.containsKey(key)) {
				ResultOrganizationStat orgStat = resultMap.get(key);
				orgStat.incrementCount();
			} else {
				ResultOrganizationStat orgStat = new ResultOrganizationStat();
				orgStat.setOrganization(key);
				resultMap.put(key, orgStat);
			}
		}

		return new ArrayList<>(resultMap.values());
	}

	public List<ResultTagStat> getTagStats(List<String> components)
	{
		Map<String, ResultTagStat> resultMap = new HashMap<>();

		for (String component : components) {
			List<ComponentTag> tags = tagMap.get(component);
			if (tags != null) {
				for (ComponentTag tag : tags) {
					String key = tag.getText();
					if (resultMap.containsKey(key)) {
						resultMap.get(key).incrementCount();
					} else {
						ResultTagStat tagStat = new ResultTagStat();
						tagStat.setTagId(tag.getTagId());
						tagStat.setTagLabel(tag.getText());
						resultMap.put(key, tagStat);
					}
				}
			}
		}

		return new ArrayList<>(resultMap.values());
	}

	public Map<String, List<ComponentAttribute>> getAttributeMap()
	{
		return attributeMap;
	}

	public Map<String, String> getOrganizationMap()
	{
		return organizationMap;
	}

	public Map<String, List<ComponentTag>> getTagMap()
	{
		return tagMap;
	}

	public static boolean isThereNewAttributeTypeSaved() {
		return isThereNewAttributeTypeSaved;
	}

	public static void setThereIsNewAttributeTypeSaved(boolean isNewApprovedComponentsFlag) {
		isThereNewAttributeTypeSaved = isNewApprovedComponentsFlag;
	}

	public static boolean getIsThereNewTagSaved() {
		return isThereNewTagSaved;
	}

	public static void setThereIsNewTagSaved(boolean NewTagSaved) {
		isThereNewTagSaved = NewTagSaved;
	}
}
