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

import edu.usu.sdl.openstorefront.service.api.AttributeService;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class AttributeServiceImpl
	extends ServiceProxy
	implements AttributeService
{
	private static final Logger log = Logger.getLogger(AttributeServiceImpl.class.getName());

	@Override
	public List<AttributeType> getRequiredAttributes()
	{
		AttributeType temp = new AttributeType();
		temp.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		temp.setRequiredFlg(Boolean.TRUE);
		List<AttributeType> required = persistenceService.queryByExample(AttributeType.class, new QueryByExample(temp));
		return required;
	}
	
}
