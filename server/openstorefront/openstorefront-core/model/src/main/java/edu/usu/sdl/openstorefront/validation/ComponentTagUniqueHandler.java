/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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


import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author cyearsley
 */
public class ComponentTagUniqueHandler implements UniqueHandler<ComponentTag>
{

	@Override
	public boolean isUnique(Field field, Object value, ComponentTag fullDataObject)
	{
		boolean unique = true;
		ComponentTag componentTagExample = new ComponentTag();
		componentTagExample.setComponentId(fullDataObject.getComponentId());
		List<ComponentTag> componentTags = componentTagExample.findByExample();

		for (ComponentTag tag : componentTags) {
			if (ComponentTag.INACTIVE_STATUS.equals(tag.getActiveStatus())) {
				tag.delete();
			}
			else if (!((fullDataObject.getTagId() != null && tag.getTagId() != null) && fullDataObject.getTagId().equals(tag.getTagId()))) {
				if (tag.getText().toLowerCase().equals(fullDataObject.getText().toLowerCase())) {
					unique = false;
					break;
				}
			}
		}

		return unique;
	}

	@Override
	public String getMessage()
	{
		return "Provided tag already exists on this entry, and duplicate tags on an entry are not permitted.";
	}
}
