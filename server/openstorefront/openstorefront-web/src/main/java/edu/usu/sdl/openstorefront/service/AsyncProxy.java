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

import edu.usu.sdl.openstorefront.service.manager.AsyncTaskManager;
import edu.usu.sdl.openstorefront.service.manager.model.TaskRequest;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proxy wraps a call and make is run as a background task
 *
 * @author dshurtleff
 * @param <T>
 */
public class AsyncProxy<T>
		implements InvocationHandler
{

	private static final Logger log = Logger.getLogger(AsyncProxy.class.getName());

	private final T originalObject;
	private final boolean allowMultiple;
	private final String name;

	public static <T> T newInstance(T obj, boolean allowMultiple, String name)
	{
		return (T) java.lang.reflect.Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj
				.getClass().getInterfaces(), new AsyncProxy(obj, allowMultiple, name));
	}

	private AsyncProxy(T obj, boolean allowMultiple, String name)
	{
		this.originalObject = obj;
		this.allowMultiple = allowMultiple;
		this.name = name;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		log.log(Level.FINE, MessageFormat.format("Calling Method (Asyncronously): {0} on {1}", new Object[]{method.getName(), originalObject.getClass().getName()}));

		AsyncProxyTask asyncProxyTask = new AsyncProxyTask(originalObject, proxy, method, args);
		TaskRequest taskRequest = new TaskRequest();
		taskRequest.setAllowMultiple(allowMultiple);
		taskRequest.setName(name);
		taskRequest.setTask(asyncProxyTask);
		return AsyncTaskManager.submitTask(taskRequest);
	}

}
