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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.api.ProxyContext;
import edu.usu.sdl.openstorefront.core.api.ProxyInterceptor;
import edu.usu.sdl.openstorefront.core.api.ServiceInterceptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 * @param <T>
 */
public class DynamicProxy<T>
		implements InvocationHandler
{

	private static final Logger LOG = Logger.getLogger(DynamicProxy.class.getName());

	private final T originalObject;

	public static <T> T newInstance(T obj)
	{
		return (T) java.lang.reflect.Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj
				.getClass().getInterfaces(), new DynamicProxy(obj));
	}

	private DynamicProxy(T obj)
	{
		this.originalObject = obj;
	}

	@Override
	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable
	{
		Object result = null;
		List<ProxyInterceptor> proxyInterceptors = new ArrayList<>();

		//populate Interceptors
		ServiceInterceptor interceptors[] = m.getAnnotationsByType(ServiceInterceptor.class);
		if (interceptors != null) {
			for (ServiceInterceptor interceptor : interceptors) {
				proxyInterceptors.add((ProxyInterceptor) interceptor.value().newInstance());
			}
		}

		long startTime = System.currentTimeMillis();
		if (LOG.isLoggable(Level.FINEST)) {
			LOG.log(Level.FINEST, "Calling Method: {0} on {1}", new Object[]{m.getName(), proxy.getClass().getName()});
		}

		ProxyContext proxyContext = new ProxyContext();
		proxyContext.setPersistenceService(((ServiceProxy) originalObject).getPersistenceService());
		try {

			boolean runMethod = true;
			for (ProxyInterceptor proxyInterceptor : proxyInterceptors) {
				boolean skip = proxyInterceptor.before(proxy, m, args, proxyContext);
				if (skip) {
					runMethod = false;
					if (LOG.isLoggable(Level.FINEST)) {
						LOG.log(Level.FINEST, "Interceptor: {0} Aborted method: {1} call.", new Object[]{proxyInterceptor.getClass().getName(), m.getName()});
					}
				}
			}

			if (runMethod) {
				result = m.invoke(originalObject, args);
			}

			for (ProxyInterceptor proxyInterceptor : proxyInterceptors) {
				proxyInterceptor.after(proxy, m, args, proxyContext);
			}

			if (LOG.isLoggable(Level.FINEST)) {
				LOG.log(Level.FINEST, "Completed Method: {0} on {1} time: {2}", new Object[]{m.getName(), proxy.getClass().getName(), System.currentTimeMillis() - startTime});
			}

		} catch (InvocationTargetException e) {
			for (ProxyInterceptor proxyInterceptor : proxyInterceptors) {
				proxyInterceptor.handleException(proxy, m, args, proxyContext);
			}
			if (LOG.isLoggable(Level.FINEST)) {
				LOG.log(Level.FINEST, "Call Method FAILED: {0} on {1} time: {2}", new Object[]{m.getName(), proxy.getClass().getName(), System.currentTimeMillis() - startTime});
			}
			throw e.getTargetException();
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new OpenStorefrontRuntimeException("Unexpected invocation exception: " + e.getMessage());
		} finally {
			for (ProxyInterceptor proxyInterceptor : proxyInterceptors) {
				proxyInterceptor.requiredAfterRun(proxy, m, args, proxyContext);
			}
		}
		return result;
	}

}
