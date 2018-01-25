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

import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds Plugin Metadata")
public class Plugin
		extends StandardEntity<Plugin>
{

	@PK(generated = true)
	@NotNull
	private String pluginId;

	@NotNull
	private String location;

	@NotNull
	private String originalFilename;

	@NotNull
	private String actualFilename;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@APIDescription("Indentifies the plugin")
	private String pluginType;

	public Plugin()
	{
	}

	public String fullPath()
	{
		return FileSystemManager.getInstance().getDir(FileSystemManager.PLUGIN_DIR) + "/" + getActualFilename();
	}

	public String getPluginId()
	{
		return pluginId;
	}

	public void setPluginId(String pluginId)
	{
		this.pluginId = pluginId;
	}

	public String getPluginType()
	{
		return pluginType;
	}

	public void setPluginType(String pluginType)
	{
		this.pluginType = pluginType;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getOriginalFilename()
	{
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename)
	{
		this.originalFilename = originalFilename;
	}

	public String getActualFilename()
	{
		return actualFilename;
	}

	public void setActualFilename(String actualFilename)
	{
		this.actualFilename = actualFilename;
	}

}
