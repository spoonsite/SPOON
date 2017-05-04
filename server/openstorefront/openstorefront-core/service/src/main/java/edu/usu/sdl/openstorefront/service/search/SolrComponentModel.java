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
package edu.usu.sdl.openstorefront.service.search;

import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import java.util.Date;
import org.apache.solr.client.solrj.beans.Field;

/**
 *
 * @author gbagley
 */
public class SolrComponentModel
{

	public static final String ID_FIELD = "id";
	public static final String ISCOMPONENT_FIELD = "isComponentSearch_b_is";
	public static final String FIELD_NAME = "title";
	public static final String FIELD_ORGANIZATION = "organization_s_is";
	public static final String FIELD_DESCRIPTION = "content_text";
		
	public static final String FIELD_SEARCH_WEIGHT = "searchWeight";

	@Field
	private String id;

	private String componentId;

	@Field("isComponentSearch_b_is")
	private Boolean isComponent;

	@Field("title")
	private String name;

	@Field("content_text")
	private String description;

	@Field("content_tags")
	private String tags;

	@Field("content_raw")
	private String attributes;

	@Field("articleHtml_text")
	private String articleHtml;

	private String queryString;

	private String guid;

	@Field("organization_s_is")
	private String organization;

	private Date releaseDate;

	@Field("modified")
	private Date updateDts;

	private String version;

	@Field("idInt_i_is")
	private int idInt;

	@Field("name_s_is")
	private String nameString;
	
	@Field("datasource_s_is")
	private String dataSource;
	
	@Field("datasensitivy_s_is")	
	private String dataSensitivity;
	
	
	private int searchWeight;
	private float queryScore;

	public SolrComponentModel()
	{
	}

	public static SolrComponentModel fromComponentSearchView(ComponentSearchView view)
	{
		SolrComponentModel solrComponentModel = new SolrComponentModel();
		
		solrComponentModel.setId(view.getComponentId());
		solrComponentModel.setComponentId(view.getComponentId());
		solrComponentModel.setDescription(view.getDescription());
		solrComponentModel.setName(view.getName());
		solrComponentModel.setOrganization(view.getOrganization());
		solrComponentModel.setVersion(view.getVersion());
		solrComponentModel.setReleaseDate(view.getReleaseDate());
		solrComponentModel.setUpdateDts(view.getUpdateDts());
		solrComponentModel.setGuid(view.getGuid());
		solrComponentModel.setDataSource(view.getDataSource());
		solrComponentModel.setDataSensitivity(view.getDataSensitivity());
				
		return solrComponentModel;		
	}
	
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public int getIdInt()
	{
		return idInt;
	}

	public void setIdInt(Integer idInt)
	{
		this.idInt = idInt;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNameString()
	{
		return nameString;
	}

	public void setNameString(String nameString)
	{
		this.nameString = nameString;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getArticleHtml()
	{
		return articleHtml;
	}

	public void setArticleHtml(String articleHtml)
	{
		this.articleHtml = articleHtml;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getGuid()
	{
		return guid;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public Date getReleaseDate()
	{
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate)
	{
		this.releaseDate = releaseDate;
	}

	public String getQueryString()
	{
		return queryString;
	}

	public void setQueryString(String queryString)
	{
		this.queryString = queryString;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getTags()
	{
		return tags;
	}

	public void setTags(String tags)
	{
		this.tags = tags;
	}

	public String getAttributes()
	{
		return attributes;
	}

	public void setAttributes(String attributes)
	{
		this.attributes = attributes;
	}

	public Boolean getIsComponent()
	{
		return isComponent;
	}

	public void setIsComponent(Boolean isComponent)
	{
		this.isComponent = isComponent;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public int getSearchWeight()
	{
		return searchWeight;
	}

	public void setSearchWeight(int searchWeight)
	{
		this.searchWeight = searchWeight;
	}

	public float getQueryScore()
	{
		return queryScore;
	}

	public void setQueryScore(float queryScore)
	{
		this.queryScore = queryScore;
	}

	public String getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(String dataSource)
	{
		this.dataSource = dataSource;
	}

	public String getDataSensitivity()
	{
		return dataSensitivity;
	}

	public void setDataSensitivity(String dataSensitivity)
	{
		this.dataSensitivity = dataSensitivity;
	}

}
