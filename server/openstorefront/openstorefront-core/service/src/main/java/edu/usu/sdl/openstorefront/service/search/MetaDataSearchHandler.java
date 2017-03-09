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

import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOptionBuilder;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.ComponentMetadata;
import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class MetaDataSearchHandler
		extends BaseSearchHandler
{

	public MetaDataSearchHandler(List<SearchElement> searchElements)
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

			ComponentMetadata componentMetadata = new ComponentMetadata();
			componentMetadata.setActiveStatus(ComponentMetadata.ACTIVE_STATUS);
			
			QueryByExample queryByExample = new QueryByExample(componentMetadata);

			String label = searchElement.getKeyField();
			if (searchElement.getCaseInsensitive()) {
				label = label.toLowerCase();
				queryByExample.getFieldOptions().put(ComponentMetadata.FIELD_LABEL, 
						new GenerateStatementOptionBuilder().setMethod(GenerateStatementOption.METHOD_LOWER_CASE).build());

			}
			componentMetadata.setLabel(label);

			if (StringUtils.isNotBlank(searchElement.getKeyValue())) {
				String likeValue = null;
				switch (searchElement.getStringOperation()) {
					case EQUALS:
						componentMetadata.setValue(searchElement.getKeyValue());
						break;
					default:
						likeValue = searchElement.getStringOperation().toQueryString(searchElement.getKeyValue());
						break;
				}

				if (likeValue != null) {
					ComponentMetadata componentMetadataLike = new ComponentMetadata();
					if (searchElement.getCaseInsensitive()) {
						likeValue = likeValue.toLowerCase();
						queryByExample.getLikeExampleOption().setMethod(GenerateStatementOption.METHOD_LOWER_CASE);
					}
					componentMetadataLike.setValue(likeValue);
					queryByExample.setLikeExample(componentMetadataLike);
				}
			}

			List<ComponentMetadata> metadata = serviceProxy.getPersistenceService().queryByExample(queryByExample);
			List<String> results = new ArrayList<>();
			for (ComponentMetadata item : metadata) {
				results.add(item.getComponentId());
			}
			foundIds = mergeCondition.apply(foundIds, results);
			mergeCondition = searchElement.getMergeCondition();
		}
		return foundIds;
	}

}
