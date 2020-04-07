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
package edu.usu.sdl.openstorefront.core.api;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This handles transaction transparently to the service
 *
 * @author dshurtleff
 */
public class TransactionInterceptor
		implements ProxyInterceptor
{

	private static final Logger log = Logger.getLogger(TransactionInterceptor.class.getName());

	@Override
	public boolean before(Object proxy, Method m, Object[] args, ProxyContext context)
	{
		log.log(Level.FINER, "Beginning transaction");
		context.getPersistenceService().begin();
		return false;
	}

	@Override
	public void after(Object proxy, Method m, Object[] args, ProxyContext context)
	{
		log.log(Level.FINER, "Committing transaction");
		context.getPersistenceService().commit();
	}

	@Override
	public void handleException(Object proxy, Method m, Object[] args, ProxyContext context)
	{
		log.log(Level.WARNING, "Rolling back transaction");
		context.getPersistenceService().rollback();
	}

	@Override
	public void requiredAfterRun(Object proxy, Method m, Object[] args, ProxyContext context)
	{
		log.log(Level.FINER, "Ending transaction");
		context.getPersistenceService().endTransaction();
	}
}
