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

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
public class SearchResult
{

	@NotNull
	private String listingType; //Componet or Article
	private String componentId;
	private String articleAttributeType;
	private String articleAttributeCode;

	@NotNull
	private String name;

	@NotNull
	private String description;

	@NotNull
	private String organization;

	@NotNull
	private Date lastActivityDts;

	@NotNull
	private Date updateDts;

	@NotNull
	private float averageRating;

	@NotNull
	private long views;

	@NotNull
	private long totalNumberOfReviews;

	@NotNull
	private String resourceLocation;

	//  @DataType(SearchResultAttribute.class)
	// private List<SearchResultAttribute> attributes = new ArrayList<>();
	@DataType(ElasticsearchAttributeCodeTypeModel.class)
	private List<ElasticsearchAttributeCodeTypeModel> attributes = new ArrayList<>();

	@DataType(ComponentTag.class)
	private List<ComponentTag> tags = new ArrayList<>();

	public String getListingType()
	{
		return listingType;
	}

	public void setListingType(String listingType)
	{
		this.listingType = listingType;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getArticleAttributeType()
	{
		return articleAttributeType;
	}

	public void setArticleAttributeType(String articleAttributeType)
	{
		this.articleAttributeType = articleAttributeType;
	}

	public String getArticleAttributeCode()
	{
		return articleAttributeCode;
	}

	public void setArticleAttributeCode(String articleAttributeCode)
	{
		this.articleAttributeCode = articleAttributeCode;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public Date getLastActivityDts()
	{
		return lastActivityDts;
	}

	public void setLastActivityDts(Date lastActivityDts)
	{
		this.lastActivityDts = lastActivityDts;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public float getAverageRating()
	{
		return averageRating;
	}

	public void setAverageRating(float averageRating)
	{
		this.averageRating = averageRating;
	}

	public long getViews()
	{
		return views;
	}

	public void setViews(long views)
	{
		this.views = views;
	}

	public long getTotalNumberOfReviews()
	{
		return totalNumberOfReviews;
	}

	public void setTotalNumberOfReviews(long totalNumberOfReviews)
	{
		this.totalNumberOfReviews = totalNumberOfReviews;
	}

	public List<ElasticsearchAttributeCodeTypeModel> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(List<ElasticsearchAttributeCodeTypeModel> attributes)
	{
		this.attributes = attributes;
	}

	public List<ComponentTag> getTags()
	{
		return tags;
	}

	public void setTags(List<ComponentTag> tags)
	{
		this.tags = tags;
	}

	public String getResourceLocation()
	{
		return resourceLocation;
	}

	public void setResourceLocation(String resourceLocation)
	{
		this.resourceLocation = resourceLocation;
	}

}
