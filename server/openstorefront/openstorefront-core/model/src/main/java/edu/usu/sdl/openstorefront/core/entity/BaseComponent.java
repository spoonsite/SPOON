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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jlaw
 * @param <T>
 */
@APIDescription("Common shared point for all component related entities")
public abstract class BaseComponent<T>
		extends StandardEntity<T>
{

	@NotNull
	@FK(Component.class)
	private String componentId;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public BaseComponent()
	{
	}

	/**
	 * This is typically a non-pk field or combination of fields that makes the
	 * record unique
	 *
	 * @return
	 */
	public abstract String uniqueKey();

	@Override
	public int compareTo(T o)
	{
		int value = super.compareTo(o);
		if (value == 0) {
			value = ReflectionUtil.compareObjects(getComponentId(), ((BaseComponent) o).getComponentId());
		}
		if (value == 0) {
			value = EntityUtil.compareConsumeFields(this, o);
		}
		if (value == 0) {
			value = customCompareTo(o);
		}
		return value;
	}

	public int customCompareTo(T o)
	{
		return 0;
	}

	/**
	 * Resets the primary keys and component key This is useful for copy
	 * entities.
	 */
	public void clearKeys()
	{
		setComponentId(null);
		customKeyClear();
	}

	@Override
	protected Set<String> excludedChangeFields()
	{
		Set<String> excludeFields = super.excludedChangeFields();
		excludeFields.add("componentId");
		return excludeFields;
	}

	public void updateChangeParent(ChangeLog changeLog)
	{
		changeLog.setParentEntity(Component.class.getSimpleName());
		changeLog.setParentEntityId(getComponentId());
	}

	//Override to add primary key clearing
	protected abstract void customKeyClear();

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

}
