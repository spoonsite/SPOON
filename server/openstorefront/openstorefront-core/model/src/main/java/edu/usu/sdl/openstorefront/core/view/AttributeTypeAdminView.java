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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class AttributeTypeAdminView
		extends AttributeType
{

	private static final long serialVersionUID = 1L;

	private boolean configurationWarning;

	public static AttributeTypeAdminView toView(AttributeType attributeType)
	{
		AttributeTypeAdminView adminView = new AttributeTypeAdminView();
		try {
			BeanUtils.copyProperties(adminView, attributeType);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		Service service = ServiceProxyFactory.getServiceProxy();

		// if (adminView.getRequiredRestrictions() != null
		// 		&& !adminView.getRequiredRestrictions().isEmpty()) {

			List<AttributeCode> codes = service.getAttributeService().findCodesForType(attributeType.getAttributeType());
			if (codes.isEmpty() && !Convert.toBoolean(attributeType.getAllowUserGeneratedCodes())) {
				adminView.setConfigurationWarning(true);
			}
		// }

		return adminView;
	}

	public static List<AttributeTypeAdminView> toView(List<AttributeType> attributeTypes)
	{
		List<AttributeTypeAdminView> views = new ArrayList<>();

		for (AttributeType attributeType : attributeTypes) {
			views.add(toView(attributeType));
		}
		return views;
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 59 * hash + (this.configurationWarning ? 1 : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AttributeTypeAdminView other = (AttributeTypeAdminView) obj;
		return this.configurationWarning == other.configurationWarning;
	}

	public boolean getConfigurationWarning()
	{
		return configurationWarning;
	}

	public void setConfigurationWarning(boolean configurationWarning)
	{
		this.configurationWarning = configurationWarning;
	}

}
