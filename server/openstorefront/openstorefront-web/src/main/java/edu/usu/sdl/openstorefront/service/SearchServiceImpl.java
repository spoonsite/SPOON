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

package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.service.api.SearchService;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentSearchView;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class SearchServiceImpl
	extends ServiceProxy
	implements SearchService
{
	private static final Logger log = Logger.getLogger(SearchServiceImpl.class.getName());
	@Override
	public List<ComponentSearchView> getAll()
	{
		ServiceProxy service = new ServiceProxy();
		List<ComponentSearchView> list = new ArrayList<>();
		list.addAll(service.getComponentService().getComponents());
		list.addAll(service.getAttributeService().getAllArticles());
		return list;
	}
}
