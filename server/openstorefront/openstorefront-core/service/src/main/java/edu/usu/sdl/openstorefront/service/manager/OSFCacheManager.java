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
import org.ehcache.core.spi.service.StatisticsService;
import org.ehcache.core.statistics.DefaultStatisticsService;

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
	public static StatisticsService statisticsService;
	public static String[] cacheNames = { 
		"applicationCache", 
		"attributeCache", 
		"attributeCodeAllCache", 
		"attributeTypeCache", 
		"checklistQuestionCache", 
		"componentApprovalCache", 
		"componentCache", 
		"componentDataRestrictionCache", 
		"componentIconCache", 
		"componentLookupCache", 
		"componentTypeCache", 
		"componentTypeComponentCache", 
		"contactCache", 
		"lookupCache", 
		"searchCache", 
		"userAgentCache", 
		"userSearchCache", 
		"workPlanTypeCache" 
	};

	private static Cache<String, Object> applicationCache;
	private static Cache<String, Object> attributeCache;
	private static Cache<String, Object> attributeCodeAllCache;
	private static Cache<String, Object> attributeTypeCache;
	private static Cache<String, Object> checklistQuestionCache;
	private static Cache<String, Object> componentApprovalCache;
	private static Cache<String, Object> componentCache;
	private static Cache<String, Object> componentDataRestrictionCache;
	private static Cache<String, Object> componentIconCache;
	private static Cache<String, Object> componentLookupCache;
	private static Cache<String, Object> componentTypeCache;
	private static Cache<String, Object> componentTypeComponentCache;
	private static Cache<String, Object> contactCache;
	private static Cache<String, Object> lookupCache;
	private static Cache<String, Object> searchCache;
	private static Cache<String, Object> userAgentCache;
	private static Cache<String, Object> userSearchCache;
	private static Cache<String, Object> workPlanTypeCache;

	private static AtomicBoolean started = new AtomicBoolean(false);

	private static final ReentrantLock LOCK = new ReentrantLock();

	private static <K, V> Cache<K, V> createCache(Class<K> K, Class<V> V, String name, int maxElementsInMemory, boolean eternal, long timeToLiveSeconds, long timeToIdleSeconds) {
		CacheConfiguration<K, V> configuration =
		CacheConfigurationBuilder.newCacheConfigurationBuilder(K, V,
				ResourcePoolsBuilder.heap(maxElementsInMemory))
			.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(timeToLiveSeconds)))
			.withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(timeToIdleSeconds)))
			.build();

		return cacheManager.createCache(name, configuration);
	}

	private static <K, V> Cache<K, V> createCache(Class<K> K, Class<V> V, String name, int maxElementsInMemory, boolean eternal) {
		CacheConfiguration<K, V> configuration =
		CacheConfigurationBuilder.newCacheConfigurationBuilder(K, V,
				ResourcePoolsBuilder.heap(maxElementsInMemory))
			.withExpiry(ExpiryPolicyBuilder.noExpiration())
			.build();
		return cacheManager.createCache(name, configuration);
	}

	public static void init()
	{
		LOCK.lock();
		try {
			// Eternal caches
			applicationCache              = createCache(String.class, Object.class, "applicationCache", 100, true);
			attributeCache                = createCache(String.class, Object.class, "attributeCache", 30000, true);
			attributeCodeAllCache         = createCache(String.class, Object.class, "attributeCodeAllCache", 1, true);
			attributeTypeCache            = createCache(String.class, Object.class, "attributeTypeCache", 5000, true);

			// Non-eternal caches
			checklistQuestionCache        = createCache(String.class, Object.class, "checklistQuestionCache", 1000, false, 300, 300);
			componentApprovalCache        = createCache(String.class, Object.class, "componentApprovalCache", 50000, false, 300, 300);
			componentCache                = createCache(String.class, Object.class, "componentCache", 200, false, 300, 300);
			componentDataRestrictionCache = createCache(String.class, Object.class, "componentDataRestrictionCache", 50000, false, 1800, 1800);
			componentIconCache            = createCache(String.class, Object.class, "componentIconCache", 50000, false, 1800, 1800);
			componentLookupCache          = createCache(String.class, Object.class, "componentLookupCache", 50000, false, 300, 300);
			componentTypeCache            = createCache(String.class, Object.class, "componentTypeCache", 1, false, 300, 300);
			componentTypeComponentCache   = createCache(String.class, Object.class, "componentTypeComponentCache", 50000, false, 300, 300);
			contactCache                  = createCache(String.class, Object.class, "contactCache", 5000, false, 1800, 1800);
			lookupCache                   = createCache(String.class, Object.class, "lookupCache", 500, false, 600, 600);
			searchCache                   = createCache(String.class, Object.class, "searchCache", 250, false, 1800, 1800);
			userAgentCache                = createCache(String.class, Object.class, "userAgentCache", 100, false, 7200, 7200);
			userSearchCache               = createCache(String.class, Object.class, "userSearchCache", 250, false, 1800, 1800);
			workPlanTypeCache             = createCache(String.class, Object.class, "workPlanTypeCache", 1000, false, 7200, 7200);

		} finally {
			LOCK.unlock();
		}

	}

	private static CacheManager initCacheManager() {
		statisticsService = new DefaultStatisticsService();
		cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
			.withCache("preConfigured",
				CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10)))
			.using(statisticsService)
			.build();
		cacheManager.init();
		init();
		return cacheManager;
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

	public static void cleanUp()
	{
		if(started.get()) {
			cacheManager.close();
			started.set(false);
		}
	}
	
	public static void initializeStatic(){
		cacheManager = initCacheManager();
		started.set(true);
	}

	public static <K, V> boolean isEmpty(Cache<K, V> cache)
	{
		AtomicBoolean isCacheEmpty = new AtomicBoolean(true);
		cache.forEach(a -> {if(a != null) isCacheEmpty.set(false);});
		return isCacheEmpty.get();
	}

	public static StatisticsService getStatisticsService() 
	{
		return statisticsService;
	}

	public static String[] getCacheNames()
	{
		return cacheNames;
	}

	public static CacheManager getCacheManager()
	{
		return cacheManager;
	}

	public static Cache<String, Object> getChecklistQuestionCache()
	{
		return checklistQuestionCache;
	}

	public static Cache<String, Object> getComponentApprovalCache()
	{
		return componentApprovalCache;
	}

	public static Cache<String, Object> getAttributeCache()
	{
		return attributeCache;
	}

	public static Cache<String, Object> getAttributeTypeCache()
	{
		return attributeTypeCache;
	}

	public static Cache<String, Object> getAttributeCodeAllCache()
	{
		return attributeCodeAllCache;
	}

	public static Cache<String, Object> getContactCache()
	{
		return contactCache;
	}

	public static Cache<String, Object> getApplicationCache() {
		return applicationCache;
	}

	public static Cache<String, Object> getComponentCache() {
		return componentCache;
	}

	public static Cache<String, Object> getComponentDataRestrictionCache() {
		return componentDataRestrictionCache;
	}

	public static Cache<String, Object> getComponentIconCache() {
		return componentIconCache;
	}

	public static Cache<String, Object> getComponentLookupCache() {
		return componentLookupCache;
	}

	public static Cache<String, Object> getComponentTypeCache() {
		return componentTypeCache;
	}

	public static Cache<String, Object> getComponentTypeComponentCache() {
		return componentTypeComponentCache;
	}

	public static Cache<String, Object> getLookupCache() {
		return lookupCache;
	}

	public static Cache<String, Object> getSearchCache() {
		return searchCache;
	}

	public static Cache<String, Object> getUserAgentCache() {
		return userAgentCache;
	}

	public static Cache<String, Object> getUserSearchCache() {
		return userSearchCache;
	}

	public static Cache<String, Object> getWorkPlanTypeCache() {
		return workPlanTypeCache;
	}
}
