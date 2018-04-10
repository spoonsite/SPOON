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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;

/**
 *
 * @author dshurtleff
 */
public class AttributeDetail
{

	private String attributeType;
	private String typeLabel;
	private String typeDetailDescription;
	private String code;
	private String codeLabel;
	private String codeDetailDescription;
	private String badgeUrl;
	private String highlightStyle;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public AttributeDetail()
	{
	}

	public static AttributeDetail toView(AttributeCode attributeCode)
	{
		Service service = ServiceProxyFactory.getServiceProxy();
		AttributeDetail detail = new AttributeDetail();

		AttributeType type = service.getAttributeService().findType(attributeCode.getAttributeCodePk().getAttributeType());
		if (type == null) {
			type = service.getPersistenceService().findById(AttributeType.class, attributeCode.getAttributeCodePk().getAttributeType());
		}

		detail.setAttributeType(type.getAttributeType());
		detail.setTypeLabel(type.getDescription());
		detail.setTypeDetailDescription(type.getDetailedDescription());
		detail.setCode(attributeCode.getAttributeCodePk().getAttributeCode());
		detail.setCodeLabel(attributeCode.getLabel());
		detail.setCodeDetailDescription(attributeCode.getDescription());
		detail.setBadgeUrl(attributeCode.getBadgeUrl());
		detail.setHighlightStyle(attributeCode.getHighlightStyle());

		return detail;
	}

	public String getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	public String getTypeLabel()
	{
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel)
	{
		this.typeLabel = typeLabel;
	}

	public String getTypeDetailDescription()
	{
		return typeDetailDescription;
	}

	public void setTypeDetailDescription(String typeDetailDescription)
	{
		this.typeDetailDescription = typeDetailDescription;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getCodeLabel()
	{
		return codeLabel;
	}

	public void setCodeLabel(String codeLabel)
	{
		this.codeLabel = codeLabel;
	}

	public String getCodeDetailDescription()
	{
		return codeDetailDescription;
	}

	public void setCodeDetailDescription(String codeDetailDescription)
	{
		this.codeDetailDescription = codeDetailDescription;
	}

	public String getBadgeUrl()
	{
		return badgeUrl;
	}

	public void setBadgeUrl(String badgeUrl)
	{
		this.badgeUrl = badgeUrl;
	}

	public String getHighlightStyle()
	{
		return highlightStyle;
	}

	public void setHighlightStyle(String highlightStyle)
	{
		this.highlightStyle = highlightStyle;
	}

}
