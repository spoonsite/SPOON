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

import edu.usu.sdl.openstorefront.service.api.AsyncService;
import edu.usu.sdl.openstorefront.service.api.AttributeService;
import edu.usu.sdl.openstorefront.service.api.AttributeServicePrivate;
import edu.usu.sdl.openstorefront.service.api.ComponentService;
import edu.usu.sdl.openstorefront.service.api.ComponentServicePrivate;
import edu.usu.sdl.openstorefront.service.api.LookupService;
import edu.usu.sdl.openstorefront.service.api.SearchService;
import edu.usu.sdl.openstorefront.service.api.SystemService;
import edu.usu.sdl.openstorefront.service.api.UserService;
import edu.usu.sdl.openstorefront.service.api.UserServicePrivate;
import edu.usu.sdl.openstorefront.service.manager.model.TaskRequest;
import java.util.Objects;

/**
 * Entry point to the service layer; Expecting one Service Proxy per thread. Not
 * thread Safe;
 *
 * @author dshurtleff
 */
public class ServiceProxy
{

	protected PersistenceService persistenceService = new PersistenceService();
	protected LookupService lookupService;
	protected AttributeService attributeService;
	protected AttributeServicePrivate attributeServicePrivate;
	protected ComponentService componentService;
	protected ComponentServicePrivate componentServicePrivate;
	protected SearchService searchService;
	protected UserService userService;
	protected UserServicePrivate userServicePrivate;
	protected SystemService systemService;

	public ServiceProxy()
	{
	}

	public static ServiceProxy getProxy()
	{
		return new ServiceProxy();
	}

	public PersistenceService getPersistenceService()
	{
		return persistenceService;
	}

	public LookupService getLookupService()
	{
		if (lookupService == null) {
			lookupService = DynamicProxy.newInstance(new LookupServiceImpl());
		}
		return lookupService;
	}

	public AttributeService getAttributeService()
	{
		if (attributeService == null) {
			attributeService = DynamicProxy.newInstance(new AttributeServiceImpl());
		}
		return attributeService;
	}

	public ComponentService getComponentService()
	{
		if (componentService == null) {
			componentService = DynamicProxy.newInstance(new ComponentServiceImpl());
		}
		return componentService;
	}

	public ComponentServicePrivate getComponentServicePrivate()
	{
		if (componentServicePrivate == null) {
			componentServicePrivate = DynamicProxy.newInstance(new ComponentServiceImpl());
		}
		return componentServicePrivate;
	}

	public SearchService getSearchService()
	{
		if (searchService == null) {
			searchService = DynamicProxy.newInstance(new SearchServiceImpl());
		}
		return searchService;
	}

	public UserService getUserService()
	{
		if (userService == null) {
			userService = DynamicProxy.newInstance(new UserServiceImpl());
		}
		return userService;
	}

	public UserServicePrivate getUserServicePrivate()
	{
		if (userService == null) {
			userServicePrivate = DynamicProxy.newInstance(new UserServiceImpl());
		}
		return userServicePrivate;
	}

	public SystemService getSystemService()
	{
		if (systemService == null) {
			systemService = DynamicProxy.newInstance(new SystemServiceImpl());
		}
		return systemService;
	}

	public AttributeServicePrivate getAttributeServicePrivate()
	{
		if (attributeServicePrivate == null) {
			attributeServicePrivate = DynamicProxy.newInstance(new AttributeServiceImpl());
		}
		return attributeServicePrivate;
	}

	public <T extends AsyncService> T getAyncProxy(T originalProxy)
	{
		TaskRequest taskRequest = new TaskRequest();
		taskRequest.setAllowMultiple(true);
		taskRequest.setName("Aync Service Call");
		return getAyncProxy(originalProxy, taskRequest);
	}

	public <T extends AsyncService> T getAyncProxy(T originalProxy, TaskRequest taskRequest)
	{
		Objects.requireNonNull(originalProxy, "Original Service is required");
		T asyncService = AsyncProxy.newInstance(originalProxy, taskRequest);
		return asyncService;
	}

}
