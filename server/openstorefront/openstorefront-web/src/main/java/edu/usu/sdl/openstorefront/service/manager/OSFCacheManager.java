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
{

	private static final Logger log = Logger.getLogger(OSFCacheManager.class.getName());

	private static Cache lookupCache;

	public static void initialize()
	{
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try {
			CacheManager singletonManager = CacheManager.create();

			Cache memoryOnlyCache = new Cache("lookupCache", 500, false, true, 0, 0);
			singletonManager.addCache(memoryOnlyCache);
			lookupCache = singletonManager.getCache("lookupCache");

		} finally {
			lock.unlock();
		}

	}

	public static void shutdown()
	{
		CacheManager.getInstance().shutdown();
	}

	public static Cache getLookupCache()
	{
		return lookupCache;
	}

}
