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

import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * FYI, 'Architechure' is a concept of certain data having very special data restriction rules.
 * No one understands it or knows what is exactly except the programmer who put it 
 * in here.
 * Since v2.10 all support for the Architecture thing has been completely 
 * discontinued. If you are looking to clean stuff up, delete this file and
 * all other files related to this concept.
 * @author dshurtleff
 */
public class ArchitectureSearchHandler
		extends BaseSearchHandler
{

	public ArchitectureSearchHandler(List<SearchElement> searchElements)
	{
		super(searchElements);
	}

	@Override
	protected ValidationResult internalValidate()
	{
		ValidationResult validationResult = new ValidationResult();

		for (SearchElement searchElement : searchElements) {
			if (StringUtils.isBlank(searchElement.getKeyField())) {
				validationResult.getRuleResults().add(getRuleResult("keyField", "Required"));
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

			AttributeCode attributeCodeExample = new AttributeCode();
			AttributeCodePk attributeCodePkExample = new AttributeCodePk();
			attributeCodePkExample.setAttributeType(searchElement.getKeyField());
			attributeCodeExample.setAttributeCodePk(attributeCodePkExample);

			QueryByExample<AttributeCode> queryByExample = new QueryByExample<>(attributeCodeExample);

			if (StringUtils.isNotBlank(searchElement.getKeyValue())) {
				String likeValue = null;
				switch (searchElement.getStringOperation()) {
					case EQUALS:
						attributeCodeExample.setArchitectureCode(searchElement.getKeyValue());
						break;
					default:
						likeValue = searchElement.getStringOperation().toQueryString(searchElement.getKeyValue());
						break;
				}

				if (likeValue != null) {
					AttributeCode attributeCodeLike = new AttributeCode();
					attributeCodeLike.setArchitectureCode(likeValue);
					queryByExample.setLikeExample(attributeCodeLike);
				}
			}

			List<AttributeCode> attributeCodes = serviceProxy.getPersistenceService().queryByExample(queryByExample);
			List<String> ids = new ArrayList<>();
			attributeCodes.forEach(code -> {
				ids.add(code.getAttributeCodePk().getAttributeCode());
			});

			List<ComponentAttribute> attributes = new ArrayList<>();
			if (ids.isEmpty() == false) {

				ComponentAttribute componentAttributeExample = new ComponentAttribute();
				componentAttributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
				ComponentAttributePk componentAttributePk = new ComponentAttributePk();
				componentAttributePk.setAttributeType(searchElement.getKeyField());
				componentAttributeExample.setComponentAttributePk(componentAttributePk);

				ComponentAttribute componentAttributeInExample = new ComponentAttribute();
				ComponentAttributePk componentAttributeInPk = new ComponentAttributePk();
				componentAttributeInPk.setAttributeCode(QueryByExample.STRING_FLAG);
				componentAttributeInExample.setComponentAttributePk(componentAttributeInPk);

				QueryByExample<ComponentAttribute> queryByExampleCodes = new QueryByExample<>(componentAttributeExample);
				queryByExampleCodes.setInExample(componentAttributeInExample);
				queryByExampleCodes.getInExampleOption().getParameterValues().addAll(ids);

				attributes = serviceProxy.getPersistenceService().queryByExample(queryByExampleCodes);
			}

			List<String> results = new ArrayList<>();
			for (ComponentAttribute attribute : attributes) {
				results.add(attribute.getComponentId());
			}
			foundIds = mergeCondition.apply(foundIds, results);
			mergeCondition = searchElement.getMergeCondition();

		}
		return foundIds;
	}

}
