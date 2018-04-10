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
package edu.usu.sdl.openstorefront.validation;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import javax.validation.constraints.Max;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Checks value; Allows null to be allowed.
 *
 * @author dshurtleff
 */
public class MaxValueRule
		extends BaseRule
{

	@Override
	protected boolean validate(Field field, Object dataObject)
	{
		boolean valid = true;

		Max max = field.getAnnotation(Max.class);
		if (max != null) {
			try {
				String value = BeanUtils.getProperty(dataObject, field.getName());
				if (value != null) {
					try {
						BigDecimal numberValue = new BigDecimal(value);
						if (numberValue.compareTo(BigDecimal.valueOf(max.value())) > 0) {
							valid = false;
						}
					} catch (NumberFormatException e) {
						throw new OpenStorefrontRuntimeException("This annotation is for numbers only", "Programming error");
					}
				}
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
				throw new OpenStorefrontRuntimeException("Unexpected error occur trying to get value from object.", ex);
			}
		}
		return valid;
	}

	@Override
	protected String getMessage(Field field)
	{
		return "Value exceeded MAX allowed";
	}

	@Override
	protected String getValidationRule(Field field)
	{
		StringBuilder sb = new StringBuilder();
		Max max = field.getAnnotation(Max.class);
		sb.append("Max value allowed: ").append(max.value());
		return sb.toString();
	}

}
