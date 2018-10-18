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
package edu.usu.sdl.openstorefront.core.model.search;

import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class AdvanceSearchResult
{

	private List<ComponentSearchView> results = new ArrayList<>();
	private ValidationResult validationResult = new ValidationResult();
	private long totalNumber;
	private AdvancedSearchMetaData meta = new AdvancedSearchMetaData();

	public AdvanceSearchResult()
	{
	}

	public List<ComponentSearchView> getResults()
	{
		return results;
	}

	public void setResults(List<ComponentSearchView> results)
	{
		this.results = results;
	}

	public ValidationResult getValidationResult()
	{
		return validationResult;
	}

	public void setValidationResult(ValidationResult validationResult)
	{
		this.validationResult = validationResult;
	}

	public long getTotalNumber()
	{
		return totalNumber;
	}

	public void setTotalNumber(long totalNumber)
	{
		this.totalNumber = totalNumber;
	}

	public AdvancedSearchMetaData getMeta()
	{
		return meta;
	}

	public void setMeta(AdvancedSearchMetaData meta)
	{
		this.meta = meta;
	}
	
	

}
