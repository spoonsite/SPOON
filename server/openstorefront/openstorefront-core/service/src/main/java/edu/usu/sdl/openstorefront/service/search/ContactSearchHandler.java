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
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOptionBuilder;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
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

	public ContactSearchHandler(SearchElement searchElement)
	{
		super(searchElement);
	}

	@Override
	protected ValidationResult internalValidate()
	{
		ValidationResult validationResult = new ValidationResult();

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

		return validationResult;
	}

	@Override
	public List<String> processSearch()
	{

		ComponentContact componentContact = new ComponentContact();
		componentContact.setActiveStatus(ComponentContact.ACTIVE_STATUS);

		Field field = ReflectionUtil.getField(new ComponentContact(), searchElement.getField());
		field.setAccessible(true);
		QueryByExample<ComponentContact> queryByExample = new QueryByExample<>(componentContact);
		try {
			//all fields are strings
			String likeValue = null;
			switch (searchElement.getStringOperation()) {
				case EQUALS:
					String value = searchElement.getValue();
					if (searchElement.getCaseInsensitive()) {
						queryByExample.getFieldOptions().put(field.getName(),
								new GenerateStatementOptionBuilder().setMethod(GenerateStatementOption.METHOD_LOWER_CASE).build());

						value = value.toLowerCase();
					}
					field.set(componentContact, value);
					break;
				default:
					likeValue = searchElement.getStringOperation().toQueryString(searchElement.getValue());
					break;
			}

			if (likeValue != null) {
				ComponentContact componentContactLike = new ComponentContact();
				if (searchElement.getCaseInsensitive()) {
					likeValue = likeValue.toLowerCase();
					queryByExample.getLikeExampleOption().setMethod(GenerateStatementOption.METHOD_LOWER_CASE);
				}
				field.set(componentContactLike, likeValue);
				queryByExample.setLikeExample(componentContactLike);
			}
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			throw new OpenStorefrontRuntimeException("Unable to set value on field for a contact search.", ex);
		}

		List<ComponentContact> componentContacts = serviceProxy.getPersistenceService().queryByExample(queryByExample);
		List<String> results = new ArrayList<>();
		for (ComponentContact contact : componentContacts) {
			results.add(contact.getComponentId());
		}
		return results;
	}

}
