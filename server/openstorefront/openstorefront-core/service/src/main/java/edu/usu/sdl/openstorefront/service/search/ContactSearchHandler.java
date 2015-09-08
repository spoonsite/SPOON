/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.search;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ContactSearchHandler
		extends BaseSearchHandler
{

	public ContactSearchHandler(List<SearchElement> searchElements)
	{
		super(searchElements);
	}

	@Override
	protected ValidationResult internalValidate()
	{
		ValidationResult validationResult = new ValidationResult();

		for (SearchElement searchElement : searchElements) {
			if (StringUtils.isBlank(searchElement.getField())) {
				validationResult.getRuleResults().add(getRuleResult("field", "Required"));
			}
			if (StringUtils.isBlank(searchElement.getValue())) {
				validationResult.getRuleResults().add(getRuleResult("value", "Required"));
			}

			Field field = ReflectionUtil.getField(new ComponentContact(), searchElement.getField());
			if (field == null) {
				validationResult.getRuleResults().add(getRuleResult("field", "Doesn't exist on contact"));
			}
		}

		return validationResult;
	}

	@Override
	public List<String> processSearch()
	{
		List<String> foundIds = new ArrayList<>();
		SearchOperation.MergeCondition mergeCondition = SearchOperation.MergeCondition.OR;
		for (SearchElement searchElement : searchElements) {

			ComponentContact componentContact = new ComponentContact();
			Field field = ReflectionUtil.getField(new ComponentContact(), searchElement.getField());
			field.setAccessible(true);
			QueryByExample queryByExample = new QueryByExample(componentContact);
			try {
				//all fields are strings
				String fieldValue = searchElement.getValue();
				if (searchElement.getCaseInsensitive()) {
					fieldValue = fieldValue.toLowerCase();
					queryByExample.getExampleOption().setMethod(GenerateStatementOption.METHOD_LOWER_CASE);
				}
				field.set(componentContact, fieldValue);
			} catch (IllegalArgumentException | IllegalAccessException ex) {
				throw new OpenStorefrontRuntimeException("Unable to set value on field for a contact search.", ex);
			}

			List<ComponentContact> componentContacts = serviceProxy.getPersistenceService().queryByExample(ComponentContact.class, queryByExample);
			List<String> results = new ArrayList<>();
			for (ComponentContact contact : componentContacts) {
				results.add(contact.getComponentId());
			}
			foundIds = mergeCondition.apply(foundIds, results);
			mergeCondition = searchElement.getMergeCondition();
		}
		return foundIds;
	}

}
