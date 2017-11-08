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
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import edu.usu.sdl.openstorefront.core.api.LookupService;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.LookupEntity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

	private static final Logger LOG = Logger.getLogger(LookupServiceImpl.class.getName());

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
			SystemTable systemTable = (SystemTable) lookTableClass.getAnnotation(SystemTable.class);
			T testExample = lookTableClass.newInstance();
			if (systemTable != null) {
				return testExample.systemValues();
			} else {
				testExample.setActiveStatus(activeStatus);
				return persistenceService.queryByExample(new QueryByExample(testExample));
			}
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

	@Override
	public void saveLookupValue(LookupEntity lookupEntity)
	{
		LookupEntity oldEntity = persistenceService.findById(lookupEntity.getClass(), lookupEntity.getCode());
		if (StringUtils.isBlank(lookupEntity.getActiveStatus())) {
			lookupEntity.setActiveStatus(LookupEntity.ACTIVE_STATUS);
		}
		if (oldEntity != null) {
			oldEntity.setDescription(lookupEntity.getDescription());
			oldEntity.setDetailedDescription(lookupEntity.getDetailedDescription());
			oldEntity.setSortOrder(lookupEntity.getSortOrder());
			oldEntity.setHighlightStyle(lookupEntity.getHighlightStyle());
			oldEntity.setActiveStatus(lookupEntity.getActiveStatus());
			oldEntity.populateBaseUpdateFields();
			persistenceService.persist(oldEntity);
		} else {
			lookupEntity.populateBaseCreateFields();
			persistenceService.persist(lookupEntity);
		}
		OSFCacheManager.getLookupCache().remove((Object) lookupEntity.getClass().getName());
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
						existing.setDetailedDescription(lookupEntity.getDetailedDescription());
						existing.setSortOrder(lookupEntity.getSortOrder());
						existing.setHighlightStyle(lookupEntity.getHighlightStyle());
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
					LOG.log(Level.WARNING, MessageFormat.format("(Data Sync) Unable to Add Code:  {0} Validation Issues:\n{1}", new Object[]{lookupEntity.getCode(), validationResult.toString()}));
				}

			} catch (Exception e) {
				LOG.log(Level.SEVERE, "Unable to save value for lookup:" + lookupEntity.toString(), e);
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

			OSFCacheManager.getLookupCache().remove((Object) lookupEntity.getClass().getName());
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
					SystemTable systemTable = (SystemTable) lookupClass.getAnnotation(SystemTable.class);
					T example = lookupClass.newInstance();
					if (systemTable != null) {
						lookupEntity = (T) example.systemValue(code);
					} else {
						example.setCode(code);
						example.setActiveStatus(LookupEntity.ACTIVE_STATUS);
						lookupEntity = persistenceService.queryOneByExample(new QueryByExample(example));
					}
					if (lookupEntity != null) {
						lookupCacheMap.put(code, lookupEntity);
					}
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
			Class lookupClass = Class.forName(DBManager.getInstance().getEntityModelPackage() + "." + lookClassName);
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
				SystemTable systemTable = (SystemTable) lookupClass.getAnnotation(SystemTable.class);
				T example = lookupClass.newInstance();					
				if (systemTable != null) {
					for (Object lookup : example.systemValues()) {						
						if (((LookupEntity)lookup).getDescription().equals(description)) {
							lookupEntity = (T) lookup;
						}
					}
				} else {
					example.setDescription(description);
					example.setActiveStatus(LookupEntity.ACTIVE_STATUS);
					lookupEntity = persistenceService.queryOneByExample(new QueryByExample(example));
				}

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
			Class lookupClass = Class.forName(DBManager.getInstance().getEntityModelPackage() + "." + lookClassName);
			lookupEntity = getLookupEnityByDesc(lookupClass, description);
		} catch (ClassNotFoundException ex) {
			throw new OpenStorefrontRuntimeException("Lookup Type not found", "Check entity name passed in. (Case-Sensitive and should be Camel-Cased)");
		}
		return lookupEntity;
	}

	@Override
	public <T extends LookupEntity> void updateLookupStatus(T lookupEntity, String status)
	{
		Objects.requireNonNull(lookupEntity, "Lookup Enity required");

		LookupEntity lookupEntityFound = persistenceService.findById(lookupEntity.getClass(), lookupEntity.getCode());
		if (lookupEntityFound != null) {
			lookupEntityFound.setActiveStatus(status);
			lookupEntityFound.setUpdateDts(TimeUtil.currentDate());
			lookupEntityFound.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(lookupEntityFound);

			OSFCacheManager.getLookupCache().remove((Object) lookupEntityFound.getClass().getName());
		}
	}

}
