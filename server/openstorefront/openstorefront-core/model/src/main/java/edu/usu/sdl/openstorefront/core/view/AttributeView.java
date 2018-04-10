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
import edu.usu.sdl.openstorefront.core.entity.Attribute;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class AttributeView
		extends StandardEntityView
{

	private String type;
	private String typeDescription;
	private String code;
	private String codeDescription;
	private String ownerId;

	public static <T extends Attribute> AttributeView toView(T attribute)
	{
		Service service = ServiceProxyFactory.getServiceProxy();
		AttributeView view = new AttributeView();
		view.setOwnerId(attribute.ownerId());

		AttributeCodePk pk = new AttributeCodePk();
		pk.setAttributeCode(attribute.getAttributeCode());
		pk.setAttributeType(attribute.getAttributeType());
		AttributeCode code = service.getAttributeService().findCodeForType(pk);
		if (code == null) {
			code = service.getPersistenceService().findById(AttributeCode.class, pk);
		}
		AttributeType type = service.getAttributeService().findType(attribute.getAttributeType());
		if (type == null) {
			type = service.getPersistenceService().findById(AttributeType.class, attribute.getAttributeType());
		}

		if (code != null && type != null) {
			view.setCodeDescription(code.getLabel());
			view.setTypeDescription(type.getDescription());
			view.setType(type.getAttributeType());
			view.setCode(code.getAttributeCodePk().getAttributeCode());
			view.toStandardView(code, type);
		} else {
			//For snapshots which are frozen in time; attributes may not exist any more
			//create stub
			view.setCodeDescription("Missing Code: " + attribute.getAttributeCode());
			view.setTypeDescription("Missing Type: " + attribute.getAttributeType());
			view.setType(attribute.getAttributeType());
			view.setCode(attribute.getAttributeCode());
		}
		return view;
	}

	public static <T extends Attribute> List<AttributeView> toViewList(List<T> attributes)
	{
		List<AttributeView> views = new ArrayList<>();
		for (Attribute attribute : attributes) {
			views.add(toView(attribute));
		}
		return views;
	}

	public String getOwnerId()
	{
		return ownerId;
	}

	public void setOwnerId(String ownerId)
	{
		this.ownerId = ownerId;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getTypeDescription()
	{
		return typeDescription;
	}

	public void setTypeDescription(String typeDescription)
	{
		this.typeDescription = typeDescription;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getCodeDescription()
	{
		return codeDescription;
	}

	public void setCodeDescription(String codeDescription)
	{
		this.codeDescription = codeDescription;
	}

}
