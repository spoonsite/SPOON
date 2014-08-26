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
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.storage.model.LookupEntity;
import edu.usu.sdl.openstorefront.storage.model.TestEntity;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.ehcache.Element;

/**
 *
 * @author dshurtleff
 */
public class LookupServiceImpl
		extends ServiceProxy
		implements LookupService
{

	private static final Logger log = Logger.getLogger(LookupServiceImpl.class.getName());

	@Override
	public <T extends BaseEntity> List<T> findLookup(Class<T> lookTableClass)
	{
		return findLookup(lookTableClass, false);
	}

	@Override
	public <T extends BaseEntity> List<T> findLookup(Class<T> lookTableClass, boolean all)
	{
		try {
			T testExample = lookTableClass.newInstance();
			if (all == false) {
				testExample.setActiveStatus(TestEntity.ACTIVE_STATUS);
			}
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
			lookupEntity.setCreateDts(TimeUtil.currentDate());
			lookupEntity.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(lookupEntity);
		}
	}

	@Override
	public <T extends LookupEntity> void syncLookupImport(Class<T> lookupClass, List<LookupEntity> lookupValues)
	{
		List<T> existingLookups = findLookup(lookupClass, true);
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
					log.log(Level.WARNING, "(Data Sync) Unable to Add Code:  {0} Validation Issues:\n{1}", new Object[]{lookupEntity.getCode(), validationResult.toString()});
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

		List<T> newLookups = findLookup(lookupClass);
		Element cacheLookup = new Element(lookupClass.getName(), newLookups);
		OSFCacheManager.getLookupCache().put(cacheLookup);
	}

}
