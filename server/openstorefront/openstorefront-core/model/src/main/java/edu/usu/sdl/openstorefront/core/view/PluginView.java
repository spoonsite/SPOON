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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.Plugin;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class PluginView
		extends Plugin
{

	private boolean coreModule;
	private String pluginRuntimeState;
	private Date lastModifed;
	private String name;
	private String description;
	private String version;
	private Long runtimeId;

	public static PluginView toView(Plugin plugin)
	{
		PluginView view = new PluginView();
		try {
			BeanUtils.copyProperties(view, plugin);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		return view;
	}

	public static List<PluginView> toView(List<Plugin> plugins)
	{
		List<PluginView> views = new ArrayList<>();
		plugins.forEach(plugin -> {
			views.add(PluginView.toView(plugin));
		});
		return views;
	}

	public boolean isCoreModule()
	{
		return coreModule;
	}

	public void setCoreModule(boolean coreModule)
	{
		this.coreModule = coreModule;
	}

	public String getPluginRuntimeState()
	{
		return pluginRuntimeState;
	}

	public void setPluginRuntimeState(String pluginRuntimeState)
	{
		this.pluginRuntimeState = pluginRuntimeState;
	}

	public Date getLastModifed()
	{
		return lastModifed;
	}

	public void setLastModifed(Date lastModifed)
	{
		this.lastModifed = lastModifed;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public Long getRuntimeId()
	{
		return runtimeId;
	}

	public void setRuntimeId(Long runtimeId)
	{
		this.runtimeId = runtimeId;
	}

}
