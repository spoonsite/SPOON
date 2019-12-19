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

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import edu.usu.sdl.openstorefront.common.manager.Initializable;

/**
 * Handling Application level caching
 *
 * @author dshurtleff
 */
public class OSFCacheManager
		implements Initializable
{

	private static final Logger LOG = Logger.getLogger(OSFCacheManager.class.getName());

	public static final String ALLCODE_KEY = "ALLCODES";
	public static CacheManager cacheManager;

	private static Cache lookupCache;
	private static Cache attributeCache;
	private static Cache attributeTypeCache;
	private static Cache attributeCodeAllCache;
	private static Cache userAgentCache;
	private static Cache componentCache;
	private static Cache componentLookupCache;
	private static Cache componentApprovalCache;
	private static Cache componentDataRestrictionCache;
	private static Cache componentTypeCache;
	private static Cache componentTypeComponentCache;
	private static Cache componentIconCache;
	private static Cache applicationCache;
	private static Cache contactCache;
	private static Cache userSearchCache;
	private static Cache searchCache;
	private static Cache checklistQuestionCache;
	private static Cache workPlanTypeCache;

	private static AtomicBoolean started = new AtomicBoolean(false);

	private static final ReentrantLock LOCK = new ReentrantLock();

	private static Cache createCache(String name, int maxElementsInMemory, boolean overflowToDisk, boolean eternal, long timeToLiveSeconds, long timeToIdleSeconds) {
		CacheConfiguration<Long, String> configuration =
		CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
				ResourcePoolsBuilder.heap(maxElementsInMemory))
			.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(timeToLiveSeconds)))
			.withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(timeToIdleSeconds)))
			.build();

		return cacheManager.createCache(name, configuration);
	}

	public static void init()
	{
		LOCK.lock();
		try {
			lookupCache                   = createCache("lookupCache", 500, false, false, 600, 600);
			attributeCache                = createCache("attributeCache", 30000, false, true, 0, 0);
			attributeTypeCache            = createCache("attributeTypeCache", 5000, false, true, 0, 0);
			attributeCodeAllCache         = createCache("attributeCodeAllCache", 1, false, true, 0, 0);
			userAgentCache                = createCache("userAgentCache", 100, false, false, 7200, 7200);
			componentCache                = createCache("componentCache", 200, false, false, 300, 300);
			componentLookupCache          = createCache("componentLookupCache", 50000, false, false, 300, 300);
			componentApprovalCache        = createCache("componentApprovalCache", 50000, false, false, 300, 300);
			componentIconCache            = createCache("componentIconCache", 50000, false, false, 1800, 1800);
			componentDataRestrictionCache = createCache("componentDataRestrictionCache", 50000, false, false, 1800, 1800);
			componentTypeCache            = createCache("componentTypeCache", 1, false, false, 300, 300);
			componentTypeComponentCache   = createCache("componentTypeComponentCache", 50000, false, false, 300, 300);
			applicationCache              = createCache("applicationCache", 100, false, true, 0, 0);
			contactCache                  = createCache("contactCache", 5000, false, false, 1800, 1800);
			userSearchCache               = createCache("userSearchCache", 250, false, false, 1800, 1800);
			searchCache                   = createCache("searchCache", 250, false, false, 1800, 1800);
			checklistQuestionCache        = createCache("checklistQuestionCache", 1000, false, false, 300, 300);
			workPlanTypeCache             = createCache("workPlanTypeCache", 1000, false, false, 7200, 7200);
		} finally {
			LOCK.unlock();
		}

	}

	private static CacheManager initCacheManager() {
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder() 
			.withCache("preConfigured",
				CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10))) 
			.build(); 
		cacheManager.init();
		return cacheManager;
	}

	public static CacheManager getCacheManager()
	{
		return cacheManager;
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

	public static Cache getComponentLookupCache()
	{
		return componentLookupCache;
	}

	public static Cache getAttributeCodeAllCache()
	{
		return attributeCodeAllCache;
	}

	public static void setAttributeCodeAllCache(Cache aAttributeCodeAllCache)
	{
		attributeCodeAllCache = aAttributeCodeAllCache;
	}

	public static Cache getComponentApprovalCache()
	{
		return componentApprovalCache;
	}

	public static Cache getComponentDataRestrictionCache()
	{
		return componentDataRestrictionCache;
	}

	public static Cache getApplicationCache()
	{
		return applicationCache;
	}

	public static Cache getComponentTypeCache()
	{
		return componentTypeCache;
	}

	public static Cache getContactCache()
	{
		return contactCache;
	}

	public static Cache getUserSearchCache()
	{
		return userSearchCache;
	}

	public static Cache getSearchCache()
	{
		return searchCache;
	}

	public static Cache getChecklistQuestionCache()
	{
		return checklistQuestionCache;
	}

	public static Cache getComponentIconCache()
	{
		return componentIconCache;
	}

	public static Cache getComponentTypeComponentCache()
	{
		return componentTypeComponentCache;
	}

	public static Cache getWorkPlanTypeCache()
	{
		return workPlanTypeCache;
	}

	@Override
	public void initialize()
	{
		cacheManager = initCacheManager();
		started.set(true);
	}

	@Override
	public void shutdown()
	{
		cacheManager.close();
		started.set(false);
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

	public static boolean isActive()
	{
		return started.get();
	}

}
