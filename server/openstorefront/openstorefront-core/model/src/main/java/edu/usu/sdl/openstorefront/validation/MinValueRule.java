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
import javax.validation.constraints.Min;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Checks value; Allows null to be allowed.
 *
 * @author dshurtleff
 */
public class MinValueRule
		extends BaseRule
{

	@Override
	protected boolean validate(Field field, Object dataObject)
	{
		boolean valid = true;

		Min min = field.getAnnotation(Min.class);
		if (min != null) {
			try {
				String value = BeanUtils.getProperty(dataObject, field.getName());
				if (value != null) {
					try {
						BigDecimal numberValue = new BigDecimal(value);
						if (numberValue.compareTo(BigDecimal.valueOf(min.value())) < 0) {
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
		return "Value is less than minimum allowed";
	}

	@Override
	protected String getValidationRule(Field field)
	{
		StringBuilder sb = new StringBuilder();
		Min min = field.getAnnotation(Min.class);
		sb.append("Min value allowed: ").append(min.value());
		return sb.toString();
	}

}
