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
package edu.usu.sdl.openstorefront.core.model;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dshurtleff
 */
public class ComponentDeleteOptions
{

	private Set<String> ignoreClasses = new HashSet<>();
	private boolean removeWatches = true;
	private boolean keepMediaFiles = false;

	public ComponentDeleteOptions()
	{
	}

	public boolean getRemoveWatches()
	{
		return removeWatches;
	}

	public void setRemoveWatches(boolean removeWatches)
	{
		this.removeWatches = removeWatches;
	}

	public Set<String> getIgnoreClasses()
	{
		return ignoreClasses;
	}

	public void setIgnoreClasses(Set<String> ignoreClasses)
	{
		this.ignoreClasses = ignoreClasses;
	}

	public boolean getKeepMediaFile()
	{
		return keepMediaFiles;
	}

	public void setKeepMediaFiles(boolean keepMediaFiles)
	{
		this.keepMediaFiles = keepMediaFiles;
	}

}
