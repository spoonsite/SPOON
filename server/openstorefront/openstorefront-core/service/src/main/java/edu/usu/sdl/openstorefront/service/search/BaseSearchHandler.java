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

import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation.MergeCondition;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseSearchHandler
{

	protected SearchElement searchElement;

	protected ServiceProxy serviceProxy = ServiceProxy.getProxy();

	public BaseSearchHandler(SearchElement searchElement)
	{
		this.searchElement = searchElement;
	}

	public MergeCondition getTopMergeCondition()
	{
		return searchElement.getMergeCondition();
	}

	public MergeCondition getNextMergeCondition()
	{
		if (searchElement.getSearchElements().isEmpty()) {
			return searchElement.getMergeCondition();
		}
		return searchElement.getSearchElements().get(searchElement.getSearchElements().size() - 1).getMergeCondition();
	}

	public List<SearchElement> getChildren()
	{
		return searchElement.getSearchElements();
	}

	protected ValidationResult validateDefaults()
	{
		ValidationResult validationResult = new ValidationResult();
		validationResult.merge(ValidationUtil.validate(new ValidationModel(searchElement)));
		return validationResult;
	}

	public ValidationResult validate()
	{
		ValidationResult validationResult = new ValidationResult();
		validationResult.merge(validateDefaults());
		validationResult.merge(internalValidate());
		return validationResult;
	}

	protected RuleResult getRuleResult(String field, String message)
	{
		RuleResult result = new RuleResult();
		result.setEntityClassName(SearchElement.class.getSimpleName());
		result.setFieldName(field);
		result.setMessage(message);
		result.setValidationRule("Invaild Value");
		return result;
	}

	protected abstract ValidationResult internalValidate();

	public abstract List<String> processSearch();

}
