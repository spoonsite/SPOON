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

import edu.usu.sdl.openstorefront.service.api.SystemService;
import edu.usu.sdl.openstorefront.storage.model.ApplicationProperty;
import edu.usu.sdl.openstorefront.storage.model.Highlight;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.ServiceUtil;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class SystemServiceImpl
		extends ServiceProxy
		implements SystemService
{

	private static final Logger log = Logger.getLogger(SystemServiceImpl.class.getName());

	@Override
	public ApplicationProperty getProperty(String key)
	{
		ApplicationProperty applicationProperty = null;
		applicationProperty = persistenceService.findById(ApplicationProperty.class, key);
		return applicationProperty;
	}

	@Override
	public String getPropertyValue(String key)
	{
		ApplicationProperty property = getProperty(key);
		if (property != null) {
			return property.getValue();
		}
		return null;
	}

	@Override
	public void saveProperty(String key, String value)
	{
		ApplicationProperty existingProperty = persistenceService.findById(ApplicationProperty.class, key);
		if (existingProperty != null) {
			existingProperty.setValue(value);
			existingProperty.setUpdateDts(TimeUtil.currentDate());
			existingProperty.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
			persistenceService.persist(existingProperty);
		} else {
			ApplicationProperty property = new ApplicationProperty();
			property.setKey(key);
			property.setValue(value);
			property.setActiveStatus(ApplicationProperty.ACTIVE_STATUS);
			property.setCreateDts(TimeUtil.currentDate());
			property.setUpdateDts(TimeUtil.currentDate());
			property.setCreateUser(OpenStorefrontConstant.SYSTEM_USER);
			property.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
			persistenceService.persist(property);
		}
	}

	@Override
	public void saveHightlight(List<Highlight> highlights)
	{
		for (Highlight hightlight : highlights) {
			saveHightlight(hightlight);
		}
	}

	@Override
	public void saveHightlight(Highlight highlight)
	{
		Highlight existing = null;
		if (StringUtils.isNotBlank(highlight.getHighlightId())) {
			existing = persistenceService.findById(Highlight.class, highlight.getHighlightId());
		}
		if (existing != null) {
			existing.setDescription(highlight.getDescription());
			existing.setHighlightType(highlight.getHighlightType());
			existing.setLink(highlight.getLink());
			existing.setTitle(highlight.getTitle());
			existing.setActiveStatus(Highlight.ACTIVE_STATUS);
			existing.setUpdateDts(TimeUtil.currentDate());
			existing.setUpdateUser(highlight.getUpdateUser());
			persistenceService.persist(existing);

		} else {
			highlight.setHighlightId(persistenceService.generateId());
			highlight.setActiveStatus(Highlight.ACTIVE_STATUS);
			highlight.setCreateDts(TimeUtil.currentDate());
			highlight.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(highlight);
		}
	}

	@Override
	public void removeHighlight(String hightlightId)
	{
		Highlight highlight = persistenceService.findById(Highlight.class, hightlightId);
		if (highlight != null) {
			highlight.setActiveStatus(Highlight.INACTIVE_STATUS);
			highlight.setUpdateUser(ServiceUtil.getCurrentUserName());
			highlight.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(highlight);
		}
	}

	@Override
	public void syncHighlights(List<Highlight> highlights)
	{
		int removeCount = persistenceService.deleteByExample(new Highlight());
		log.log(Level.FINE, MessageFormat.format("Old Highlights removed: {0}", removeCount));

		for (Highlight highlight : highlights) {
			try {
				ValidationModel validationModel = new ValidationModel(highlight);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult validationResult = ValidationUtil.validate(validationModel);
				if (validationResult.valid()) {
					highlight.setCreateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
					highlight.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
					getSystemService().saveHightlight(highlight);
				}

			} catch (Exception e) {
				log.log(Level.SEVERE, "Unable to save highlight.  Title: " + highlight.getTitle(), e);
			}
		}
	}

}
