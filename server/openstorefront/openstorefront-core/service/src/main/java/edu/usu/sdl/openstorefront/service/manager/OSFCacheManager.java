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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.ChecklistQuestion;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.Contact;

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


	private static Cache<String, Object> applicationCache;
	private static Cache<String, List<AttributeCode>> attributeCache;
	private static Cache<Integer, String> attributeCodeAllCache;
	private static Cache<Integer, String> attributeTypeCache;
	private static Cache<String, ChecklistQuestion> checklistQuestionCache;
	private static Cache<String, String> componentApprovalCache;
	private static Cache<Integer, String> componentCache;
	private static Cache<Integer, String> componentDataRestrictionCache;
	private static Cache<Integer, String> componentIconCache;
	private static Cache<Integer, String> componentLookupCache;
	private static Cache<Integer, String> componentTypeCache;
	private static Cache<Integer, String> componentTypeComponentCache;
	private static Cache<String, Contact> contactCache;
	private static Cache<Integer, String> lookupCache;
	private static Cache<Integer, String> searchCache;
	private static Cache<Integer, String> userAgentCache;
	private static Cache<Integer, String> userSearchCache;
	private static Cache<Integer, String> workPlanTypeCache;

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
			attributeCache                = createCache(String.class, (AttributeCode.class) List.class, "attributeCache", 30000, true);
			attributeCodeAllCache         = createCache(Integer.class, String.class, "attributeCodeAllCache", 1, true);
			attributeTypeCache            = createCache(Integer.class, String.class, "attributeTypeCache", 5000, true);

			// Non-eternal caches
			checklistQuestionCache        = createCache(String.class, ChecklistQuestion.class, "checklistQuestionCache", 1000, false, 300, 300);
			componentApprovalCache        = createCache(String.class, String.class, "componentApprovalCache", 50000, false, 300, 300);
			componentCache                = createCache(Integer.class, String.class, "componentCache", 200, false, 300, 300);
			componentDataRestrictionCache = createCache(Integer.class, String.class, "componentDataRestrictionCache", 50000, false, 1800, 1800);
			componentIconCache            = createCache(Integer.class, String.class, "componentIconCache", 50000, false, 1800, 1800);
			componentLookupCache          = createCache(Integer.class, String.class, "componentLookupCache", 50000, false, 300, 300);
			componentTypeCache            = createCache(Integer.class, String.class, "componentTypeCache", 1, false, 300, 300);
			componentTypeComponentCache   = createCache(Integer.class, String.class, "componentTypeComponentCache", 50000, false, 300, 300);
			contactCache                  = createCache(String.class, Contact.class, "contactCache", 5000, false, 1800, 1800);
			lookupCache                   = createCache(Integer.class, String.class, "lookupCache", 500, false, 600, 600);
			searchCache                   = createCache(Integer.class, String.class, "searchCache", 250, false, 1800, 1800);
			userAgentCache                = createCache(Integer.class, String.class, "userAgentCache", 100, false, 7200, 7200);
			userSearchCache               = createCache(Integer.class, String.class, "userSearchCache", 250, false, 1800, 1800);
			workPlanTypeCache             = createCache(Integer.class, String.class, "workPlanTypeCache", 1000, false, 7200, 7200);


			attributeCache = cacheManager.createCache("attributeCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, AttributeCodeList.class,
				ResourcePoolsBuilder.heap(30000))
			.withExpiry(ExpiryPolicyBuilder.noExpiration())
			.build());
		} finally {
			LOCK.unlock();
		}

	}

	private class AttributeCodeList extends ArrayList<AttributeCode> {
		public AttributeCodeList(final Collection<? extends AttributeCode> c){
			super(c);
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

	public static <K, V> boolean isEmpty(Cache<K, V> cache)
	{
		AtomicBoolean isCacheEmpty = new AtomicBoolean(true);
		cache.forEach(a -> {if(a != null) isCacheEmpty.set(false);});
		return isCacheEmpty.get();
	}

	public static Cache<String, ChecklistQuestion> getChecklistQuestionCache()
	{
		return checklistQuestionCache;
	}

	public static void setChecklistQuestionCache(Cache<String, ChecklistQuestion> checklistQuestionCache)
	{
		OSFCacheManager.checklistQuestionCache = checklistQuestionCache;
	}

	public static Cache<String, String> getComponentApprovalCache()
	{
		return componentApprovalCache;
	}

	public static void setComponentApprovalCache(Cache<String, String> componentApprovalCache)
	{
		OSFCacheManager.componentApprovalCache = componentApprovalCache;
	}

	public static Cache<String, AttributeType> getAttributeCache()
	{
		return attributeCache;
	}

	public static void setAttributeCache(Cache<String, AttributeType> attributeCache)
	{
		OSFCacheManager.attributeCache = attributeCache;
	}

	public static Cache<Integer, String> getAttributeCodeAllCache()
	{
		return attributeCodeAllCache;
	}

	public static void setAttributeCodeAllCache(Cache<Integer, String> attributeCodeAllCache)
	{
		OSFCacheManager.attributeCodeAllCache = attributeCodeAllCache;
	}

	public static Cache<String, Contact> getContactCache()
	{
		return contactCache;
	}

	public static void setContactCache(Cache<String, Contact> contactCache)
	{
		OSFCacheManager.contactCache = contactCache;
	}

	public static Cache<String, Object> getApplicationCache() {
		return applicationCache;
	}

	public static void setApplicationCache(Cache<String, Object> applicationCache) {
		OSFCacheManager.applicationCache = applicationCache;
	}

}
