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
package edu.usu.sdl.openstorefront.validation;

import edu.usu.sdl.openstorefront.core.view.RestErrorModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Hold the validation results
 *
 * @author dshurtleff
 */
public class ValidationResult
{

	private List<RuleResult> ruleResults = new ArrayList<>();

	public ValidationResult()
	{
	}

	public boolean valid()
	{
		return ruleResults.isEmpty();
	}

	public void merge(ValidationResult result)
	{
		ruleResults.addAll(result.getRuleResults());
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		ruleResults.forEach(result -> {
			sb.append(result.toString()).append("\n");
		});
		return sb.toString();
	}

	public String toHtmlString()
	{
		return toString().replace("\n", "<br>");
	}

	public RestErrorModel toRestError()
	{
		RestErrorModel restErrorModel = new RestErrorModel();
		for (RuleResult ruleResult : ruleResults) {
			if (restErrorModel.getErrors().containsKey(ruleResult.getFieldName())) {
				String message = restErrorModel.getErrors().get(ruleResult.getFieldName());
				message += ", " + ruleResult.getMessage();
				restErrorModel.getErrors().put(ruleResult.getFieldName(), message);
			} else {
				restErrorModel.getErrors().put(ruleResult.getFieldName(), ruleResult.getMessage());
			}
		}
		if (valid()) {
			restErrorModel.setSuccess(true);			
		}
		return restErrorModel;
	}
	
	public void addToErrors(Map<String, String> errors) 
	{
		for (RuleResult ruleResult : ruleResults) {
			errors.put(ruleResult.getEntityClassName() + "." + ruleResult.getFieldName(), ruleResult.getMessage());
		}
	}

	public List<RuleResult> getRuleResults()
	{
		return ruleResults;
	}

	public void setRuleResults(List<RuleResult> ruleResults)
	{
		this.ruleResults = ruleResults;
	}

}
