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
package edu.usu.sdl.openstorefront.validation;

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.TemporaryMedia;
import java.lang.reflect.Field;

public class TemporaryMediaUniqueHandler
		implements UniqueHandler<TemporaryMedia>
{

	@Override
	public boolean isUnique(Field field, Object value, TemporaryMedia fullDataObject)
	{
		boolean unique = true;
		Service serviceProxy = ServiceProxyFactory.getServiceProxy();

		TemporaryMedia temporaryMediaExample = new TemporaryMedia();
		temporaryMediaExample.setName((String) value);

		TemporaryMedia media = serviceProxy.getPersistenceService().queryOneByExample(temporaryMediaExample);
		if (media != null) {
			unique = false;
		}

		return unique;
	}

	@Override
	public String getMessage()
	{
		return "Provided name already exists, and duplication is not permitted.  Please choose another name.";
	}

}
