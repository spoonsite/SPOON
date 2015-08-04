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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.storage.model.SecurityMarkingType;
import edu.usu.sdl.openstorefront.storage.model.StandardEntity;

/**
 *
 * @author dshurtleff
 */
public abstract class StandardEntityView
{

	private String securityMarkingCode;
	private String securityMarkingDescription;
	private Integer securityMarkingRank;
	private String securityMarkingStyle;

	public StandardEntityView()
	{
	}

	public void toStandardView(StandardEntity... entities)
	{
		if (entities != null) {
			ServiceProxy serviceProxy = new ServiceProxy();
			//Set to the highest classification
			for (StandardEntity entity : entities) {
				if (entity != null) {
					SecurityMarkingType marking = serviceProxy.getLookupService().getLookupEnity(SecurityMarkingType.class, entity.getSecurityMarkingType());
					if (marking != null
							&& marking.getSortOrder() != null
							&& (securityMarkingRank == null || marking.getSortOrder() > securityMarkingRank)) {
						securityMarkingCode = entity.getSecurityMarkingType();
						securityMarkingDescription = marking.getDescription();
						securityMarkingRank = marking.getSortOrder();
						securityMarkingStyle = marking.getHighlightStyle();
					}
				}
			}
		}
	}

	public String getSecurityMarkingCode()
	{
		return securityMarkingCode;
	}

	public void setSecurityMarkingCode(String securityMarkingCode)
	{
		this.securityMarkingCode = securityMarkingCode;
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

}
