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
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Record of a component version history")
public class ComponentVersionHistory
		extends StandardEntity<ComponentVersionHistory>
{

	@PK(generated = true)
	@NotNull
	private String versionHistoryId;

	@NotNull
	@FK(Component.class)
	private String componentId;

	@NotNull
	@APIDescription("Based on the DB storage verison; so it not necessarily sequential.")
	private String version;

	@APIDescription("If version was based on file history (Taken before batch was applied)")
	private String fileHistoryId;

	public ComponentVersionHistory()
	{
	}

	public String getVersionHistoryId()
	{
		return versionHistoryId;
	}

	public void setVersionHistoryId(String versionHistoryId)
	{
		this.versionHistoryId = versionHistoryId;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getFileHistoryId()
	{
		return fileHistoryId;
	}

	public void setFileHistoryId(String fileHistoryId)
	{
		this.fileHistoryId = fileHistoryId;
	}

}
