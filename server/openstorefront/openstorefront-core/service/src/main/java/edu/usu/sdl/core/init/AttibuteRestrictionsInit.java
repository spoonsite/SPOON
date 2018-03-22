/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeRestriction;
import java.util.ArrayList;
import java.util.List;

/**
 * Remove After 2.6 - This will adjusted the required and option attributes to
 * match old behavior so they can be adjusted later.
 *
 * @author dshurtleff
 */
public class AttibuteRestrictionsInit
		extends ApplyOnceInit
{

	public AttibuteRestrictionsInit()
	{
		super("AttributeType-Restrictions-Init-2.6");
	}

	@Override
	protected String internalApply()
	{
		StringBuilder results = new StringBuilder();

		List<ComponentType> componentTypes = service.getComponentService().getAllComponentTypes();

		AttributeType attributeExample = new AttributeType();
		List<AttributeType> attributes = attributeExample.findByExampleProxy();

		int count = 0;
		for (AttributeType attributeType : attributes) {

			boolean updateAttribute = false;
			//no associated; that means optional for all types
			if (!attributeType.getRequiredFlg()
					&& (attributeType.getAssociatedComponentTypes() == null
					|| attributeType.getAssociatedComponentTypes().isEmpty())) {
				attributeType.setOptionalRestrictions(getRestrictions(componentTypes));
				updateAttribute = true;
			}

			if (attributeType.getRequiredFlg()) {
				attributeType.setOptionalRestrictions(new ArrayList<>());
				updateAttribute = true;
			}

			if (attributeType.getRequiredFlg()
					&& (attributeType.getRequiredRestrictions() == null
					|| attributeType.getRequiredRestrictions().isEmpty())) {
				attributeType.setRequiredRestrictions(getRestrictions(componentTypes));
				updateAttribute = true;
			}

			if (updateAttribute) {
				service.getPersistenceService().persist(attributeType);
				count++;
			}
		}
		results.append("Attributes Updated: ")
				.append(count)
				.append("\n");

		//if require and no restriction that means it's required for all
		return results.toString();
	}

	private List<ComponentTypeRestriction> getRestrictions(List<ComponentType> componentTypes)
	{
		List<ComponentTypeRestriction> restrictions = new ArrayList<>();
		for (ComponentType componentType : componentTypes) {
			ComponentTypeRestriction restriction = new ComponentTypeRestriction(componentType.getComponentType());
			restrictions.add(restriction);
		}
		return restrictions;
	}

	@Override
	public int getPriority()
	{
		return 100;
	}

}
