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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;

/**
 *
 * @author dshurtleff
 */
@APIDescription("This is used to queue up slow updates and avoid contention")
public class ComponentUpdateQueue
		extends StandardEntity<ComponentUpdateQueue>
{

	@PK(generated = true)
	private String updateId;

	@FK(Component.class)
	private String componentId;
	private String nodeId;

	@ValidValueType(value = {}, lookupClass = ModificationType.class)
	@FK(ModificationType.class)
	private String modificationType;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ComponentUpdateQueue()
	{
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getNodeId()
	{
		return nodeId;
	}

	public void setNodeId(String nodeId)
	{
		this.nodeId = nodeId;
	}

	public String getUpdateId()
	{
		return updateId;
	}

	public void setUpdateId(String updateId)
	{
		this.updateId = updateId;
	}

	public String getModificationType()
	{
		return modificationType;
	}

	public void setModificationType(String modificationType)
	{
		this.modificationType = modificationType;
	}

}
