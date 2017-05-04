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
import javax.validation.constraints.Size;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class SizeRule
		extends BaseRule
{

	@Override
	protected boolean validate(Field field, Object dataObject)
	{
		boolean valid = true;

		Size size = field.getAnnotation(Size.class);
		if (size != null) {
			try {
				String value = BeanUtils.getProperty(dataObject, field.getName());
				if (value != null) {
					if (value.length() < size.min()
							|| value.length() > size.max()) {
						valid = false;
					}
				} else {
					if (size.min() > 0) {
						valid = false;
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
		return "Value is out of range";
	}

	@Override
	protected String getValidationRule(Field field)
	{
		StringBuilder sb = new StringBuilder();
		Size size = field.getAnnotation(Size.class);
		sb.append("Min Length: ").append(size.min()).append(" Max Length: ").append(size.max());
		return sb.toString();
	}

}
