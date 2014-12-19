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

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Callable for aync Proxy
 *
 * @author dshurtleff
 */
public class AsyncProxyTask
		implements Callable<Object>
{

	private Object originalObject;
	private Object proxy;
	private Method method;
	private Object[] args;

	public AsyncProxyTask(Object originalObject, Object proxy, Method method, Object[] args)
	{
		this.originalObject = originalObject;
		this.proxy = proxy;
		this.method = method;
		this.args = args;
	}

	@Override
	public Object call() throws Exception
	{
		Object result = method.invoke(originalObject, args);
		return result;
	}

}
