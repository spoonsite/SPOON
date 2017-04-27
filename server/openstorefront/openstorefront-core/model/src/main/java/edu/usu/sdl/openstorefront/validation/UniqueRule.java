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
package edu.usu.sdl.openstorefront.validation;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.annotation.Unique;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Field valid need to be unique
 *
 * @author dshurtleff
 */
public class UniqueRule
		extends BaseRule
{

	@Override
	protected boolean validate(Field field, Object dataObject)
	{
		boolean valid = true;

		if (dataObject != null) {
			Unique unique = field.getAnnotation(Unique.class);
			if (unique != null) {
				try {
					String value = BeanUtils.getProperty(dataObject, field.getName());
					if (value != null) {
						UniqueHandler handler = unique.value().newInstance();
						valid = handler.isUnique(field, value, dataObject);
					}
				} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
					throw new OpenStorefrontRuntimeException("Unexpected error occur trying open the handler.", ex);
				}
			}
		}

		return valid;
	}

	@Override
	protected String getMessage(Field field)
	{
		Unique unique = field.getAnnotation(Unique.class);

		if (unique != null) {
			try {
				return unique.value().newInstance().getMessage();
			} catch (InstantiationException | IllegalAccessException e) {
				//ignore
			}
		}
		return "Value is not unique";
	}

	@Override
	protected String getValidationRule(Field field)
	{
		Unique unique = field.getAnnotation(Unique.class);
		if (unique != null) {
			return "Field is required to be unique according to handler: " + unique.value().getSimpleName();
		}
		return "Field is required to be unique according to handler.";
	}

}
