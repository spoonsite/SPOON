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

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.api.LookupService;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.storage.model.LookupEntity;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;

/**
 * Handles all lookup entities
 *
 * @author dshurtleff
 */
public class LookupServiceImpl
		extends ServiceProxy
		implements LookupService
{

	private static final Logger log = Logger.getLogger(LookupServiceImpl.class.getName());

	@Override
	public <T extends LookupEntity> List<T> findLookup(Class<T> lookTableClass)
	{
		Element element = OSFCacheManager.getLookupCache().get(lookTableClass.getName());
		List<T> lookupList;
		boolean updateCache = false;
		if (element != null) {
			Map<String, T> lookupCacheMap = (Map<String, T>) element.getObjectValue();
			if (lookupCacheMap != null) {
				lookupList = new ArrayList<>(lookupCacheMap.values());
			} else {
				lookupList = findLookup(lookTableClass, LookupEntity.ACTIVE_STATUS);
				updateCache = true;
			}
		} else {
			lookupList = findLookup(lookTableClass, LookupEntity.ACTIVE_STATUS);
			updateCache = true;
		}
		if (updateCache) {
			Map<String, T> lookupCacheMap = new HashMap<>();
			for (T lookup : lookupList) {
				if (lookupCacheMap.containsKey(lookup.getCode())) {
					//remove any duplicates from the db
					T existing = persistenceService.findById(lookTableClass, lookup.getCode());
					persistenceService.delete(existing);
				} else {
					lookupCacheMap.put(lookup.getCode(), lookup);
				}
			}
			Element cachedLookup = new Element(lookTableClass.getName(), lookupCacheMap);
			OSFCacheManager.getLookupCache().put(cachedLookup);
		}
		return lookupList;
	}

	@Override
	public <T extends LookupEntity> List<T> findLookup(Class<T> lookTableClass, String activeStatus)
	{
		try {
			T testExample = lookTableClass.newInstance();
			testExample.setActiveStatus(activeStatus);
			return persistenceService.queryByExample(lookTableClass, new QueryByExample(testExample));
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

	@Override
	public void saveLookupValue(LookupEntity lookupEntity)
	{
		LookupEntity oldEntity = persistenceService.findById(lookupEntity.getClass(), lookupEntity.getCode());
		if (oldEntity != null) {
			oldEntity.setDescription(lookupEntity.getDescription());
			oldEntity.setDetailedDecription(lookupEntity.getDetailedDecription());
			oldEntity.setActiveStatus(lookupEntity.getActiveStatus());
			oldEntity.setUpdateUser(lookupEntity.getUpdateUser());
			oldEntity.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(oldEntity);
		} else {
			lookupEntity.setActiveStatus(LookupEntity.ACTIVE_STATUS);
			lookupEntity.setCreateDts(TimeUtil.currentDate());
			lookupEntity.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(lookupEntity);
		}
	}

	@Override
	public <T extends LookupEntity> void syncLookupImport(Class<T> lookupClass, List<LookupEntity> lookupValues)
	{
		List<T> existingLookups = findLookup(lookupClass, null);
		Map<String, T> lookupMap = new HashMap<>();
		for (T lookup : existingLookups) {
			lookupMap.put(lookup.getCode(), lookup);
		}

		Set<String> newCodeSet = new HashSet<>();
		for (LookupEntity lookupEntity : lookupValues) {
			try {
				ValidationModel validationModel = new ValidationModel(lookupEntity);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult validationResult = ValidationUtil.validate(validationModel);
				if (validationResult.valid()) {
					LookupEntity existing = lookupMap.get(lookupEntity.getCode());
					if (existing != null) {
						existing.setDescription(lookupEntity.getDescription());
						existing.setDetailedDecription(lookupEntity.getDetailedDecription());
						existing.setActiveStatus(LookupEntity.ACTIVE_STATUS);
						existing.setCreateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
						existing.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
						getLookupService().saveLookupValue(existing);
					} else {
						lookupEntity.setActiveStatus(LookupEntity.ACTIVE_STATUS);
						lookupEntity.setCreateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
						lookupEntity.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
						getLookupService().saveLookupValue(lookupEntity);
					}

					newCodeSet.add(lookupEntity.getCode());
				} else {
					log.log(Level.WARNING, MessageFormat.format("(Data Sync) Unable to Add Code:  {0} Validation Issues:\n{1}", new Object[]{lookupEntity.getCode(), validationResult.toString()}));
				}

			} catch (Exception e) {
				log.log(Level.SEVERE, "Unable to save value for lookup:" + lookupEntity.toString(), e);
			}
		}

		//Inactive missing codes
		for (LookupEntity lookupEntity : existingLookups) {
			if (newCodeSet.contains(lookupEntity.getCode()) == false) {
				lookupEntity.setActiveStatus(LookupEntity.INACTIVE_STATUS);
				lookupEntity.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
				getLookupService().saveLookupValue(lookupEntity);
			}
		}
	}

	@Override
	public <T extends LookupEntity> void removeValue(Class<T> lookTableClass, String code)
	{
		LookupEntity lookupEntity = persistenceService.findById(lookTableClass, code);
		if (lookupEntity != null) {
			lookupEntity.setActiveStatus(LookupEntity.INACTIVE_STATUS);
			lookupEntity.setUpdateDts(TimeUtil.currentDate());
			lookupEntity.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(lookupEntity);
		}
	}

	@Override
	public <T extends LookupEntity> T getLookupEnity(Class<T> lookupClass, String code)
	{
		T lookupEntity = null;
		if (StringUtils.isNotBlank(code)) {
			Element element = OSFCacheManager.getLookupCache().get(lookupClass.getName());
			if (element == null) {
				Map<String, T> lookupCacheMap = new HashMap<>();
				List<T> lookupList = findLookup(lookupClass, LookupEntity.ACTIVE_STATUS);
				for (T lookup : lookupList) {
					lookupCacheMap.put(lookup.getCode(), lookup);
				}
				element = new Element(lookupClass.getName(), lookupCacheMap);
				OSFCacheManager.getLookupCache().put(element);
			}
			Map<String, T> lookupCacheMap = (Map<String, T>) element.getObjectValue();
			lookupEntity = lookupCacheMap.get(code);
			if (lookupEntity == null) {
				//cache miss
				try {
					T example = lookupClass.newInstance();
					example.setCode(code);
					example.setActiveStatus(LookupEntity.ACTIVE_STATUS);
					lookupEntity = persistenceService.queryOneByExample(lookupClass, new QueryByExample(example));
					lookupCacheMap.put(code, lookupEntity);
				} catch (InstantiationException | IllegalAccessException e) {
					throw new OpenStorefrontRuntimeException(e);
				}
			}
		}
		return lookupEntity;
	}

	@Override
	public LookupEntity getLookupEnity(String lookClassName, String code)
	{
		LookupEntity lookupEntity = null;
		try {
			Class lookupClass = Class.forName(DBManager.ENTITY_MODEL_PACKAGE + "." + lookClassName);
			lookupEntity = getLookupEnity(lookupClass, code);
		} catch (ClassNotFoundException ex) {
			throw new OpenStorefrontRuntimeException("Lookup Type not found", "Check entity name passed in. (Case-Sensitive and should be Camel-Cased)");
		}
		return lookupEntity;
	}

	@Override
	public <T extends LookupEntity> T getLookupEnityByDesc(Class<T> lookupClass, String description)
	{
		T lookupEntity = null;
		if (StringUtils.isNotBlank(description)) {
			try {
				T example = lookupClass.newInstance();
				example.setDescription(description);
				example.setActiveStatus(LookupEntity.ACTIVE_STATUS);
				lookupEntity = persistenceService.queryOneByExample(lookupClass, new QueryByExample(example));

			} catch (InstantiationException | IllegalAccessException e) {
				throw new OpenStorefrontRuntimeException(e);
			}
		}
		return lookupEntity;
	}

	@Override
	public LookupEntity getLookupEnityByDesc(String lookClassName, String description)
	{
		LookupEntity lookupEntity = null;
		try {
			Class lookupClass = Class.forName(DBManager.ENTITY_MODEL_PACKAGE + "." + lookClassName);
			lookupEntity = getLookupEnityByDesc(lookupClass, description);
		} catch (ClassNotFoundException ex) {
			throw new OpenStorefrontRuntimeException("Lookup Type not found", "Check entity name passed in. (Case-Sensitive and should be Camel-Cased)");
		}
		return lookupEntity;
	}

}
