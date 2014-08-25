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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.service.api.SystemService;
import edu.usu.sdl.openstorefront.storage.model.ApplicationProperty;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class SystemServiceImpl
		extends ServiceProxy
		implements SystemService
{

	private static final Logger log = Logger.getLogger(SystemServiceImpl.class.getName());

	@Override
	public ApplicationProperty getProperty(String key)
	{
		ApplicationProperty applicationProperty = null;
		applicationProperty = persistenceService.findById(ApplicationProperty.class, key);
		return applicationProperty;
	}

	@Override
	public String getPropertyValue(String key)
	{
		ApplicationProperty property = getProperty(key);
		if (property != null) {
			return property.getValue();
		}
		return null;
	}

	@Override
	public void saveProperty(String key, String value)
	{
		ApplicationProperty existingProperty = persistenceService.findById(ApplicationProperty.class, key);
		if (existingProperty != null) {
			existingProperty.setValue(value);
			existingProperty.setUpdateDts(TimeUtil.currentDate());
			existingProperty.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
			persistenceService.persist(existingProperty);
		} else {
			ApplicationProperty property = new ApplicationProperty();
			property.setKey(key);
			property.setValue(value);
			property.setActiveStatus(ApplicationProperty.ACTIVE_STATUS);
			property.setCreateDts(TimeUtil.currentDate());
			property.setUpdateDts(TimeUtil.currentDate());
			property.setCreateUser(OpenStorefrontConstant.SYSTEM_USER);
			property.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
			persistenceService.persist(property);
		}
	}

}
