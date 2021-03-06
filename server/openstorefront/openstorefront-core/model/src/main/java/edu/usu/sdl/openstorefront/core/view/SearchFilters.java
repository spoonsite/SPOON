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
package edu.usu.sdl.openstorefront.core.view;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.entity.AttributeSearchType;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;

/**
 *
 * @author gfowler
 */
public class SearchFilters {
	@ConsumeField
    @QueryParam("query")
    @DefaultValue("")
	@Size(min = 0, max = 255)
	@Sanitize(TextSanitizer.class)
    private String query;

	@ConsumeField
    @QueryParam("page")
	@DefaultValue("0")
    @Min(0)
	@NotNull
    private int page;

	@ConsumeField
    @QueryParam("pageSize")
	@DefaultValue("20")
    @Min(0)
	@NotNull
    private int pageSize;

	@ConsumeField
    @QueryParam("componentTypes")
    private String componentType;

	@ConsumeField
    @QueryParam("includeChildren")
    private Boolean includeChildren;

	@ConsumeField
    @QueryParam("organization")
    private String organization;

	@ConsumeField
    @QueryParam("attributes")
    private String attributes;

	@ConsumeField
	@QueryParam("tags")
	private List<String> tags;

	@ConsumeField
	@QueryParam("sortOrder")
	@NotNull
	private String sortOrder;

	@ConsumeField
	@QueryParam("sortField")
	@NotNull
	private String sortField;

	private SearchFilterOptions searchFilterOptions;

	private List<AttributeSearchType> attributeSearchType;

	public SearchFilters() {
	}

    public ValidationResult validate()
	{
		ValidationModel validationModel = new ValidationModel(this);
		return ValidationUtil.validate(validationModel);
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public Boolean getIncludeChildren() {
		return includeChildren;
	}

	public void setIncludeChildren(Boolean includeChildren) {
		this.includeChildren = includeChildren;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public List<AttributeSearchType> getAttributeSearchType() {
		return attributeSearchType;
	}

	public void setAttributeSearchType(List<AttributeSearchType> attributeSearchType) {
		this.attributeSearchType = attributeSearchType;
	}

	public SearchFilterOptions getSearchFilterOptions() {
		return searchFilterOptions;
	}

	public void setSearchFilterOptions(SearchFilterOptions searchFilterOptions) {
		this.searchFilterOptions = searchFilterOptions;
	}
}