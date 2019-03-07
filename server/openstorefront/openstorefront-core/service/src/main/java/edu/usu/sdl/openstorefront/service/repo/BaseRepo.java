/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.repo;

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.service.ServiceProxy;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseRepo
{

	protected FilterEngine filterEngine;
	protected Service service;

	public BaseRepo()
	{
		service = ServiceProxy.getProxy();
		filterEngine = FilterEngine.getInstance();
	}

	public BaseRepo(Service service, FilterEngine filterEngine)
	{
		this.service = service;
		this.filterEngine = filterEngine;
	}

	public FilterEngine getFilterEngine()
	{
		return filterEngine;
	}

	public void setFilterEngine(FilterEngine filterEngine)
	{
		this.filterEngine = filterEngine;
	}

}
