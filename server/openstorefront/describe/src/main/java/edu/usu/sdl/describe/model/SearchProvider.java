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
package edu.usu.sdl.describe.model;

import edu.usu.sdl.describe.parser.SearchProviderConverter;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
@Convert(SearchProviderConverter.class)
public class SearchProvider
{
	@Attribute(required = false)
	private String classification;
	
	@Element(name="generalInfo", required = false)
	private GeneralInfo generalInfo;
	
	private List<RelatedResource> relatedResources = new ArrayList<>();
	private List<SearchInterface> searchInterfaces = new ArrayList<>();
	private List<SearchableField> searchableFields = new ArrayList<>();

	public SearchProvider()
	{
	}

	public String getClassification()
	{
		return classification;
	}

	public void setClassification(String classification)
	{
		this.classification = classification;
	}

	public List<RelatedResource> getRelatedResources()
	{
		return relatedResources;
	}

	public void setRelatedResources(List<RelatedResource> relatedResources)
	{
		this.relatedResources = relatedResources;
	}

	public List<SearchInterface> getSearchInterfaces()
	{
		return searchInterfaces;
	}

	public void setSearchInterfaces(List<SearchInterface> searchInterfaces)
	{
		this.searchInterfaces = searchInterfaces;
	}

	public List<SearchableField> getSearchableFields()
	{
		return searchableFields;
	}

	public void setSearchableFields(List<SearchableField> searchableFields)
	{
		this.searchableFields = searchableFields;
	}

	public GeneralInfo getGeneralInfo()
	{
		return generalInfo;
	}

	public void setGeneralInfo(GeneralInfo generalInfo)
	{
		this.generalInfo = generalInfo;
	}
			
}
