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
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.model.search.ResultAttributeStat;
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
	
	public SearchStatTable() {
		// pull hashMaps from the cache
		Cache appCache = OSFCacheManager.getApplicationCache();
		Element element = appCache.get(CACHE_KEY);
		if (element != null) {
			SearchStatTable cachedTable = (SearchStatTable)element.getObjectValue();
			this.attributeMap = cachedTable.getAttributeMap();
			this.organizationMap = cachedTable.getOrganizationMap();
			this.tagMap = cachedTable.getTagMap();
		} else {
			index();
		}
	}
	
	public SearchStatTable(Map<String, List<ComponentAttribute>> attributeMap, Map<String, List<ComponentTag>> tagMap) {
		this.attributeMap = attributeMap;
		this.tagMap = tagMap;
	}
	
	public SearchStatTable(Map<String, List<ComponentAttribute>> attributeMap, Map<String, List<ComponentTag>> tagMap, Map<String, String> organizationMap) {
		this.attributeMap = attributeMap;
		this.tagMap = tagMap;
		this.organizationMap = organizationMap;
	}
	
	public SearchStatTable getTable() {
		Cache appCache = OSFCacheManager.getApplicationCache();
		Element element = appCache.get(CACHE_KEY);
		if (element == null) {
			index();
			element = appCache.get(CACHE_KEY); // refetch after the index
		}
		return (SearchStatTable) element.getObjectValue();
	}
	/**
	 * Creates an index of tags, attributes, and organizations.
	 * The StatTable is then stored in the applicationCache.
	 * To be called with the search engine index.
	 */
	public void index()
	{
		if(tagMap.isEmpty()) {
			ComponentTag tagExample = new ComponentTag();
			tagExample.setActiveStatus(ComponentTag.ACTIVE_STATUS);
			List<ComponentTag> tags = tagExample.findByExample();
			tagMap = buildMap(tags, ComponentTag::getComponentId);
		}

		if (attributeMap.isEmpty()) {
			ComponentAttribute attributeExample = new ComponentAttribute();
			attributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
			List<ComponentAttribute> attributes = attributeExample.findByExample();
			attributeMap = buildMap(attributes, ComponentAttribute::getComponentId);
		}
		
		if (organizationMap.isEmpty()) {
			Component componentExample = new Component();
			componentExample.setActiveStatus(Component.ACTIVE_STATUS);
			componentExample.setApprovalState(ApprovalStatus.APPROVED);
			List<Component> components = componentExample.findByExample(); // for organizations -- get approved status
			
			for(Component component : components) {
				organizationMap.put(component.getComponentId(), component.getOrganization());
			}
		}

		Element element = new Element(CACHE_KEY, this);
		Cache appCache = OSFCacheManager.getApplicationCache();
		appCache.remove(CACHE_KEY);
		OSFCacheManager.getApplicationCache().put(element);	
	}

	private <T> Map<String, List<T>> buildMap(List<T> components, Function<T, String> getComponentId) {
		Map<String, List<T>> result = new HashMap<>();
		for(T component : components) {
			if(result.containsKey(getComponentId.apply(component))) {
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
	
	public List<ResultAttributeStat> getAttributeStats(List<String> components)
	{
		Map<String, ResultAttributeStat> resultMap = new HashMap<>();
		
		for(String component : components) {
			List<ComponentAttribute> attributes = attributeMap.get(component);
			if (attributes != null) {
				for (ComponentAttribute attribute : attributes) {
					String key = attribute.uniqueKey();
					if(resultMap.containsKey(key)) {
						ResultAttributeStat attrStat = resultMap.get(key);
						attrStat.incrementCount();
					} else {
						ResultAttributeStat attrStat = new ResultAttributeStat();
						Service service = ServiceProxyFactory.getServiceProxy();

						AttributeType type = service.getAttributeService().findType(attribute.getComponentAttributePk().getAttributeType());
						if (type == null) {
							type = service.getPersistenceService().findById(AttributeType.class, attribute.getComponentAttributePk().getAttributeType());
						}

						if (type != null) {
							attrStat.setAttributeTypeLabel(type.getDescription());
						}
						attrStat.setAttributeCode(attribute.getComponentAttributePk().getAttributeCode());
						attrStat.setAttributeType(attribute.getComponentAttributePk().getAttributeType());

						AttributeCodePk codePk = new AttributeCodePk();
						codePk.setAttributeCode(attrStat.getAttributeCode());
						codePk.setAttributeType(attrStat.getAttributeType());
						AttributeCode attrCode = service.getAttributeService().findCodeForType(codePk);
						if(attrCode == null){
							attrStat.setAttributeCodeLabel("LabelNotAvailable");
							LOG.log(Level.WARNING, () -> "Could not find Code Label for Component: " 
									+ service.getComponentService().getComponentName(attribute.getComponentId()) 
									+ " Component Code: " + attribute.getComponentAttributePk().getAttributeCode() 
									+ " Component Type: " + attribute.getComponentAttributePk().getAttributeType());	
						} else {
							attrStat.setAttributeCodeLabel(attrCode.getLabel());					
						}
						resultMap.put(key, attrStat);
					}
				}
			}
		}

		return new ArrayList<ResultAttributeStat>(resultMap.values());	
	}
	
	public List<ResultOrganizationStat> getOrganizationStats(List<String> components)
	{
		Map<String, ResultOrganizationStat> resultMap = new HashMap<>();
		
		for(String component : components) {
			String key = organizationMap.get(component);
			if(resultMap.containsKey(key)) {
				ResultOrganizationStat orgStat = resultMap.get(key);
				orgStat.incrementCount();
			} else {
				ResultOrganizationStat orgStat = new ResultOrganizationStat();
				orgStat.setOrganization(key);
				resultMap.put(key, orgStat);
			}
		}

		return new ArrayList<ResultOrganizationStat>(resultMap.values());
	}
	
	public List<ResultTagStat> getTagStats(List<String> components)
	{
		Map<String, ResultTagStat> resultMap = new HashMap<>();
		
		for(String component : components) {
			List<ComponentTag> tags = tagMap.get(component);
			if (tags != null) {
				for (ComponentTag tag : tags) {
					String key = tag.getText();
					if(resultMap.containsKey(key)) {
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

		return new ArrayList<ResultTagStat>(resultMap.values());	
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

}
