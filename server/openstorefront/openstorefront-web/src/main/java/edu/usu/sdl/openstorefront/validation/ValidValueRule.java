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

import edu.usu.sdl.openstorefront.doc.ValidValueType;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.storage.model.LookupEntity;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sf.ehcache.Element;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class ValidValueRule
		extends BaseRule
{

	@Override
	protected boolean validate(Field field, Object dataObject)
	{
		boolean valid = true;

		ValidValueType validValueType = field.getAnnotation(ValidValueType.class);
		if (validValueType != null) {
			try {
				String value = BeanUtils.getProperty(dataObject, field.getName());

				Set<String> validValueSet = new HashSet<>();
				if (validValueType.lookupClass().length > 1) {
					for (Class lookupClass : validValueType.lookupClass()) {
						Element cachedLookup = OSFCacheManager.getLookupCache().get(lookupClass.getName());
						List<LookupEntity> lookups;
						if (cachedLookup == null) {
							ServiceProxy serviceProxy = new ServiceProxy();
							lookups = serviceProxy.getLookupService().findLookup(lookupClass);
						} else {
							lookups = (List<LookupEntity>) cachedLookup.getObjectValue();
						}
						lookups.forEach(item -> {
							validValueSet.add(item.getCode());
						});
					}
				}
				validValueSet.addAll(Arrays.asList(validValueType.value()));

				if (validValueSet.contains(value) == false) {
					valid = false;
				}
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
				throw new OpenStorefrontRuntimeException("Unexpected error occur trying to get value from object.", ex);
			}
		}
		return valid;
	}

	@Override
	protected String getMessage()
	{
		return "Value not in the list of valid values";
	}

	@Override
	protected String getValidationRule(Field field)
	{
		StringBuilder sb = new StringBuilder();
		ValidValueType validValueType = field.getAnnotation(ValidValueType.class);
		sb.append("Set of valid values: ").append(Arrays.toString(validValueType.value()));
		return sb.toString();
	}

}
