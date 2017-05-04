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
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Field value needs to exist in foreign Table Doesn't support compound keys
 *
 * @author dshurtleff
 */
public class ForeignKeyRule
		extends BaseRule
{

	@Override
	protected boolean validate(Field field, Object dataObject)
	{
		boolean valid = true;

		if (dataObject != null) {
			FK fk = field.getAnnotation(FK.class);
			if (fk != null) {
				if (fk.enforce()) {
					try {
						String value = BeanUtils.getProperty(dataObject, field.getName());
						if (value != null) {
							Class fkClass = fk.value();
							Service serviceProxy = ServiceProxyFactory.getServiceProxy();
							if (StringUtils.isNotBlank(fk.referencedField())) {
								Object referenceEntity = fkClass.newInstance();
								Field referenceField = ReflectionUtil.getField(referenceEntity, fk.referencedField());
								if (referenceField != null) {
									if (referenceEntity instanceof BaseEntity) {
										referenceField.setAccessible(true);
										referenceField.set(referenceEntity, value);
										Object entity = ((BaseEntity) referenceEntity).find();
										if (entity == null) {
											valid = false;
										}
									} else {
										throw new OpenStorefrontRuntimeException("Reference Class is not a Base Entity: " + fkClass.getName());
									}
								} else {
									throw new OpenStorefrontRuntimeException("Reference field: " + fk.referencedField() + " not on Class: " + fkClass.getSimpleName(), "check code");
								}
							} else {
								Object entity = serviceProxy.getPersistenceService().findById(fkClass, value);
								if (entity == null) {
									valid = false;
								}
							}
						}
					} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
						throw new OpenStorefrontRuntimeException("Unexpected error occur trying get original field value.", ex);
					}
				}
			}
		}

		return valid;
	}

	@Override
	protected String getMessage(Field field)
	{
		return "Value is not exist foreign relationship";
	}

	@Override
	protected String getValidationRule(Field field)
	{
		FK fk = field.getAnnotation(FK.class);
		if (fk != null) {
			return "Field is required to be in foreign key: " + fk.value().getSimpleName();
		}
		return "Field is required in foreign key (FK annotation not found).";
	}

}
