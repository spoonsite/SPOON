/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeNestedModel;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeOptions;
import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation.MergeCondition;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation.StringOperation;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author cyearsley
 */
public class EntryTypeSearchHandler
		extends BaseSearchHandler
{

	public EntryTypeSearchHandler(List<SearchElement> searchElements)
	{
		super(searchElements);
	}

	@Override
	protected ValidationResult internalValidate()
	{
		ValidationResult validationResult = new ValidationResult();

		for (SearchElement searchElement : searchElements) {
			if (StringUtils.isBlank(searchElement.getValue())) {
				validationResult.getRuleResults().add(getRuleResult("value", "Required"));
			}

			if (searchElement.getCaseInsensitive()) {
				validationResult.getRuleResults().add(getRuleResult("caseInsensitive", "Not Supported"));
			}

			if (!searchElement.getStringOperation().equals(StringOperation.EQUALS)) {
				validationResult.getRuleResults().add(getRuleResult("stringOperation", "Only supports EQUALS"));
			}
		}

		return validationResult;
	}

	@Override
	public List<String> processSearch()
	{
		List<String> foundIds = new ArrayList<>();
		MergeCondition mergeCondition = SearchOperation.MergeCondition.OR;

		for (SearchElement searchElement : searchElements) {

			// Get children componentTypes (including the parent componentType)
			ComponentTypeOptions componentTypeOptions = new ComponentTypeOptions();
			componentTypeOptions.setComponentType(searchElement.getValue());
			componentTypeOptions.setPullParents(Boolean.FALSE);

			List<String> componentTypes;
			if (searchElement.getSearchChildren()) {
				ComponentTypeNestedModel nestedModel = serviceProxy.getComponentService().getComponentType(componentTypeOptions);
				componentTypes = nestedModel.getComponentTypeChildren();
			} else {
				componentTypes = Arrays.asList(searchElement.getValue());
			}

			// set component example
			Component component = new Component();
			component.setActiveStatus(Component.ACTIVE_STATUS);
			component.setApprovalState(ApprovalStatus.APPROVED);

			QueryByExample<Component> queryByExample = new QueryByExample<>(component);

			Component componentInExample = new Component();
			componentInExample.setComponentType(QueryByExample.STRING_FLAG);
			queryByExample.setInExample(componentInExample);

			// sets the parameter (componetType in this case) values
			queryByExample.getInExampleOption().setParameterValues(componentTypes);

			// query components
			List<Component> components = serviceProxy.getPersistenceService().queryByExample(queryByExample);
			List<String> results = new ArrayList<>();
			for (Component queriedComponent : components) {
				results.add(queriedComponent.getComponentId());
			}
			foundIds = mergeCondition.apply(foundIds, results);
		}

		return foundIds;
	}
}
