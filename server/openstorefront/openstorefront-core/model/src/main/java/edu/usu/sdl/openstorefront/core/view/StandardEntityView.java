/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.DataSensitivity;
import edu.usu.sdl.openstorefront.core.entity.SecurityMarkingType;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;

/**
 *
 * @author dshurtleff
 */
public abstract class StandardEntityView
{

	private String securityMarkingType;
	private String securityMarkingDescription;
	private Integer securityMarkingRank;
	private String securityMarkingStyle;
	private String dataSensitivity;
	private String dataSensitivityDescription;
	private Map<String, String> dataSensitivityDescriptionMap = new HashMap<>();

	public <T extends StandardEntity> void toStandardViewBaseEntities(List<T>... entities)
	{
		for (List<T> views : entities) {
			for (StandardEntity entity : views) {
				toStandardView(entity);
			}
		}
	}

	public void toStandardView(StandardEntity... entities)
	{
		if (entities != null) {
			Service service = ServiceProxyFactory.getServiceProxy();
			//Set to the highest classification
			for (StandardEntity entity : entities) {
				if (entity != null) {
					SecurityMarkingType marking = service.getLookupService().getLookupEnity(SecurityMarkingType.class, entity.getSecurityMarkingType());
					if (marking != null
							&& marking.getSortOrder() != null
							&& (securityMarkingRank == null || marking.getSortOrder() > securityMarkingRank)) {
						securityMarkingType = entity.getSecurityMarkingType();
						securityMarkingDescription = marking.getDescription();
						securityMarkingRank = marking.getSortOrder();
						securityMarkingStyle = marking.getHighlightStyle();
					}
					dataSensitivity = entity.getDataSensitivity();
					dataSensitivityDescription = TranslateUtil.translate(DataSensitivity.class, dataSensitivity);
					if (!StringUtil.isBlank(dataSensitivity) && !dataSensitivityDescriptionMap.containsKey(dataSensitivity)) {
						dataSensitivityDescriptionMap.put(dataSensitivity, dataSensitivityDescription);
					}
				}
			}
		}
	}

	public <T extends StandardEntityView> void toStandardView(List<T>... entities)
	{
		for (List<T> views : entities) {
			for (StandardEntityView view : views) {
				toStandardView(view);
			}
		}
	}

	public void toStandardView(StandardEntityView... entities)
	{
		if (entities != null) {
			Service service = ServiceProxyFactory.getServiceProxy();
			//Set to the highest classification
			for (StandardEntityView entity : entities) {
				if (entity != null) {
					SecurityMarkingType marking = service.getLookupService().getLookupEnity(SecurityMarkingType.class, entity.getSecurityMarkingType());
					if (marking != null
							&& marking.getSortOrder() != null
							&& (securityMarkingRank == null || marking.getSortOrder() > securityMarkingRank)) {
						securityMarkingType = entity.getSecurityMarkingType();
						securityMarkingDescription = marking.getDescription();
						securityMarkingRank = marking.getSortOrder();
						securityMarkingStyle = marking.getHighlightStyle();
					}
					dataSensitivity = entity.getDataSensitivity();
					dataSensitivityDescription = TranslateUtil.translate(DataSensitivity.class, dataSensitivity);
					if (!StringUtil.isBlank(dataSensitivity) && !dataSensitivityDescriptionMap.containsKey(dataSensitivity)) {
						dataSensitivityDescriptionMap.put(dataSensitivity, dataSensitivityDescription);
					}
				}
			}
		}
	}

	public String getSecurityMarkingType()
	{
		return securityMarkingType;
	}

	public void setSecurityMarkingType(String securityMarkingType)
	{
		this.securityMarkingType = securityMarkingType;
	}

	public String getSecurityMarkingDescription()
	{
		return securityMarkingDescription;
	}

	public void setSecurityMarkingDescription(String securityMarkingDescription)
	{
		this.securityMarkingDescription = securityMarkingDescription;
	}

	public Integer getSecurityMarkingRank()
	{
		return securityMarkingRank;
	}

	public void setSecurityMarkingRank(Integer securityMarkingRank)
	{
		this.securityMarkingRank = securityMarkingRank;
	}

	public String getSecurityMarkingStyle()
	{
		return securityMarkingStyle;
	}

	public void setSecurityMarkingStyle(String securityMarkingStyle)
	{
		this.securityMarkingStyle = securityMarkingStyle;
	}

	public String getDataSensitivity()
	{
		return dataSensitivity;
	}

	public void setDataSensitivity(String dataSensitivity)
	{
		this.dataSensitivity = dataSensitivity;
	}

	public String getDataSensitivityDescription()
	{
		return dataSensitivityDescription;
	}

	public void setDataSensitivityDescription(String dataSensitivityDescription)
	{
		this.dataSensitivityDescription = dataSensitivityDescription;
	}

	/**
	 * creates a CSV of each of the data sensitivities of the given components
	 * in a Collection View such as ComponentDetailView
	 *
	 * @return comma separated list of sensitivity values
	 */
	protected String getDataSensitivityList()
	{
		return StringUtils.join(this.dataSensitivityDescriptionMap.keySet(), ",");
	}

	/**
	 * adds the values values in the provided dataSensitivity string to the
	 * DataSensitivityDescriptionMap with the value as the map key and an empty
	 * string a the map value. if the key exists no change is made to the map
	 *
	 * @param dataSensitivity comma separated list of sensitivity values
	 */
	protected void setDataSensitivityDescriptionMapFromDataSensitivityString(String dataSensitivity)
	{
		if (!StringUtil.isBlank(dataSensitivity)) {
			Arrays.asList(dataSensitivity.split(",")).forEach((item) -> {
				if (!this.dataSensitivityDescriptionMap.containsKey(item)) {
					this.dataSensitivityDescriptionMap.put(item.trim(), "");
				}
			});
		}
	}

	/**
	 * creates a string of each of the data sensitivities and their descriptions
	 * of the given components in a Collection View such as ComponentDetailView
	 *
	 * @return A string containing data sensitivity the values in the format
	 * "key1:value1,key2:value2,..."
	 */
	protected String getDataSensitivityDescriptionMapAsString()
	{
		StringBuilder result = new StringBuilder();
		this.dataSensitivityDescriptionMap.forEach((k, v) -> {
			result.append(",").append(k).append(":").append(v);
		});
		return result.toString();
	}

	/**
	 * populates the DataSensitivityDescriptionMap with the values provided
	 *
	 * @param dataSensitivityDescription A string containing data sensitivity
	 * the values in the format "key1:value1,key2:value2,..."
	 */
	protected void setDataSensitivityDescriptionMapFromString(String dataSensitivityDescription)
	{
		if (!StringUtil.isBlank(dataSensitivityDescription)) {
			Arrays.asList(dataSensitivityDescription.split(",")).forEach((mapItem) -> {
				String[] data = mapItem.split(":");
				if (data.length == 2) {
					this.dataSensitivityDescriptionMap.put(data[0].trim(), data[1].trim());
				}
			});
		}
	}
}
