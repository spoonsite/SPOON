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
package edu.usu.sdl.openstorefront.service.manager;

import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 * Handling Application level caching
 *
 * @author dshurtleff
 */
public class OSFCacheManager
		implements Initializable
{

	private static final Logger log = Logger.getLogger(OSFCacheManager.class.getName());

	private static Cache lookupCache;
	private static Cache attributeCache;
	private static Cache attributeTypeCache;
	private static Cache userAgentCache;
	private static Cache componentCache;

	public static void init()
	{
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try {
			CacheManager singletonManager = CacheManager.create();

			Cache memoryOnlyCache = new Cache("lookupCache", 500, false, false, 600, 600);
			singletonManager.addCache(memoryOnlyCache);
			lookupCache = singletonManager.getCache("lookupCache");

			memoryOnlyCache = new Cache("attributeCache", 500, false, false, 300, 300);
			singletonManager.addCache(memoryOnlyCache);
			attributeCache = singletonManager.getCache("attributeCache");

			memoryOnlyCache = new Cache("attributeTypeCache", 500, false, false, 300, 300);
			singletonManager.addCache(memoryOnlyCache);
			attributeTypeCache = singletonManager.getCache("attributeTypeCache");

			memoryOnlyCache = new Cache("userAgentCache", 100, false, false, 7200, 7200);
			singletonManager.addCache(memoryOnlyCache);
			userAgentCache = singletonManager.getCache("userAgentCache");

			memoryOnlyCache = new Cache("componentCache", 200, false, false, 300, 300);
			singletonManager.addCache(memoryOnlyCache);
			componentCache = singletonManager.getCache("componentCache");

		} finally {
			lock.unlock();
		}

	}

	public static void cleanUp()
	{
		CacheManager.getInstance().shutdown();
	}

	public static Cache getLookupCache()
	{
		return lookupCache;
	}

	public static Cache getAttributeCache()
	{
		return attributeCache;
	}

	public static Cache getAttributeTypeCache()
	{
		return attributeTypeCache;
	}

	public static Cache getUserAgentCache()
	{
		return userAgentCache;
	}

	public static Cache getComponentCache()
	{
		return componentCache;
	}

	@Override
	public void initialize()
	{
		OSFCacheManager.init();
	}

	@Override
	public void shutdown()
	{
		OSFCacheManager.cleanUp();
	}

}
