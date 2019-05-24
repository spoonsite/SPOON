/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.model;

import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dshurtleff
 */
public class SearchHandlingResult
{

	private Set<String> foundEntriesIds = new HashSet<>();
	private ValidationResult validationResult = new ValidationResult();
	private List<SearchElement> indexSearchElements = new ArrayList<>();

	public SearchHandlingResult()
	{
	}

	public void merge(SearchHandlingResult searchHandlingResult)
	{
		this.getFoundEntriesIds().addAll(searchHandlingResult.getFoundEntriesIds());
		this.getValidationResult().merge(searchHandlingResult.getValidationResult());
		this.getIndexSearchElements().addAll(searchHandlingResult.getIndexSearchElements());
	}

	public Set<String> getFoundEntriesIds()
	{
		return foundEntriesIds;
	}

	public void setFoundEntriesIds(Set<String> foundEntriesIds)
	{
		this.foundEntriesIds = foundEntriesIds;
	}

	public ValidationResult getValidationResult()
	{
		return validationResult;
	}

	public void setValidationResult(ValidationResult validationResult)
	{
		this.validationResult = validationResult;
	}

	public List<SearchElement> getIndexSearchElements()
	{
		return indexSearchElements;
	}

	public void setIndexSearchElements(List<SearchElement> indexSearchElements)
	{
		this.indexSearchElements = indexSearchElements;
	}

}
