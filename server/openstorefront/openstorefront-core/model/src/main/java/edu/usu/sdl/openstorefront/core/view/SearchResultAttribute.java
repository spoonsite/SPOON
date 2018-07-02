/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class SearchResultAttribute
{

	private String type;
	private String typeLabel;
	private String code;
	private String badgeUrl;
	private String label;

	public static SearchResultAttribute toView(ComponentAttribute attribute)
	{
		Service service = ServiceProxyFactory.getServiceProxy();
		SearchResultAttribute view = new SearchResultAttribute();
		view.setCode(attribute.getComponentAttributePk().getAttributeCode());
		view.setType(attribute.getComponentAttributePk().getAttributeType());
		AttributeCodePk temp = new AttributeCodePk();
		temp.setAttributeCode(attribute.getComponentAttributePk().getAttributeCode());
		temp.setAttributeType(attribute.getComponentAttributePk().getAttributeType());
		AttributeCode code = service.getAttributeService().findCodeForType(temp);
		if (code != null) {
			view.setBadgeUrl(code.getBadgeUrl());
			view.setLabel(code.getLabel());
		} else {
			view.setLabel(OpenStorefrontConstant.NOT_AVAILABLE);
		}

		AttributeType type = service.getAttributeService().findType(attribute.getComponentAttributePk().getAttributeType());
		if (type != null) {
			view.setTypeLabel(type.getDescription());
		} else {
			view.setTypeLabel(OpenStorefrontConstant.NOT_AVAILABLE);
		}

		return view;
	}

	public static List<SearchResultAttribute> toViewList(List<ComponentAttribute> attributes)
	{
		List<SearchResultAttribute> list = new ArrayList<>();
		attributes.stream().forEach((attribute) -> {
			list.add(SearchResultAttribute.toView(attribute));
		});
		return list;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getBadgeUrl()
	{
		return badgeUrl;
	}

	public void setBadgeUrl(String badgeUrl)
	{
		this.badgeUrl = badgeUrl;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getTypeLabel()
	{
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel)
	{
		this.typeLabel = typeLabel;
	}

}
