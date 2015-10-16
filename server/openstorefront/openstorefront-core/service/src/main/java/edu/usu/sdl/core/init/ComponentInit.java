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
package edu.usu.sdl.core.init;

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import java.util.HashMap;

/**
 * Applies a default (Remove after 1.5)
 *
 * @author dshurtleff
 */
public class ComponentInit
		extends ApplyOnceInit
{

	public ComponentInit()
	{
		super("COMPONENT_TYPE_DEFAULT");
	}

	@Override
	protected String internalApply()
	{
		String query = "update " + Component.class.getSimpleName() + " set componentType = '" + ComponentType.COMPONENT + "' where componentType IS NULL";
		long updateCount = service.getPersistenceService().runDbCommand(query, new HashMap<>());
		return "Updated " + updateCount + " components";
	}

}
