/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.core.entity.ComponentEvaluationSectionPk;
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
public class EvaluationScoreSearchHandler
		extends BaseSearchHandler
{

	public EvaluationScoreSearchHandler(List<SearchElement> searchElements)
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
			
			if (StringUtils.isBlank(searchElement.getValue())) {
				validationResult.getRuleResults().add(getRuleResult("value", "Required"));
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
			
			try {
				ComponentEvaluationSection evaluationSection = new ComponentEvaluationSection();
				ComponentEvaluationSectionPk evaluationSectionPk = new ComponentEvaluationSectionPk();
				evaluationSectionPk.setEvaluationSection(searchElement.getKeyField());
				evaluationSection.setComponentEvaluationSectionPk(evaluationSectionPk);
				evaluationSection.setActiveStatus(ComponentEvaluationSection.ACTIVE_STATUS);
				
				QueryByExample queryByExample = new QueryByExample(evaluationSection);

				List<ComponentEvaluationSection> sections = serviceProxy.getPersistenceService().queryByExample(queryByExample);
				List<String> results = new ArrayList<>();
				for (ComponentEvaluationSection section : sections) {
					if (Convert.toBoolean(section.getNotAvailable()) == false) {
						if (searchElement.getNumberOperation().pass(section.getActualScore(), Convert.toBigDecimal(searchElement.getValue()))) {
							results.add(section.getComponentId());
						}
					}
				}
				foundIds = mergeCondition.apply(foundIds, results);
				mergeCondition = searchElement.getMergeCondition();

			} catch (SecurityException | IllegalArgumentException | OpenStorefrontRuntimeException e) {
				throw new OpenStorefrontRuntimeException("Unable to handle search request", e);
			}			
		}
		return foundIds;
	}
	
}
