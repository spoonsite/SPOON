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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant.ListingType;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.Component;
import java.util.Date;

/**
 * Holds recently added information
 *
 * @author dshurtleff
 */
public class RecentlyAddedView
		extends StandardEntityView
{

	private ListingType listingType = ListingType.COMPONENT;
	private String name;
	private String description;
	private String componentId;
	private String articleAttributeType;
	private String articleAttributeCode;
	private Date addedDts;

	public static RecentlyAddedView toView(Component component)
	{
		RecentlyAddedView recentlyAddedView = new RecentlyAddedView();
		recentlyAddedView.setListingType(OpenStorefrontConstant.ListingType.COMPONENT);
		recentlyAddedView.setComponentId(component.getComponentId());
		recentlyAddedView.setName(component.getName());
		recentlyAddedView.setDescription(component.getDescription());
		recentlyAddedView.setAddedDts(component.getApprovedDts());
		recentlyAddedView.toStandardView(component);
		return recentlyAddedView;
	}

	public static RecentlyAddedView toView(AttributeCode attributeCode)
	{
		RecentlyAddedView recentlyAddedView = new RecentlyAddedView();
		recentlyAddedView.setListingType(OpenStorefrontConstant.ListingType.ARTICLE);
		recentlyAddedView.setArticleAttributeType(attributeCode.getAttributeCodePk().getAttributeType());
		recentlyAddedView.setArticleAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode());

		return recentlyAddedView;
	}

	public ListingType getListingType()
	{
		return listingType;
	}

	public void setListingType(ListingType listingType)
	{
		this.listingType = listingType;
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

	public Date getAddedDts()
	{
		return addedDts;
	}

	public void setAddedDts(Date addedDts)
	{
		this.addedDts = addedDts;
	}

}
