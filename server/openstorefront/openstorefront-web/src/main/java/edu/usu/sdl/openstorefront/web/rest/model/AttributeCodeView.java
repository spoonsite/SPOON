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
package edu.usu.sdl.openstorefront.web.rest.model;


import java.util.ArrayList;
import java.util.List;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;

/**
 *
 * @author dshurtleff
 */
public class AttributeCodeView
{

	private String code;
	private String label;
	private String description;
	private String detailUrl;
	private String badgeUrl;
	private String architectureCode;
	private String groupCode;
	private Integer sortOrder;
	private String activeStatus;
	private String highlightStyle;

	public AttributeCodeView()
	{
	}

	public static AttributeCodeView toView(AttributeCode attributeCode)
	{
		AttributeCodeView attributeCodeView = new AttributeCodeView();
		attributeCodeView.setCode(attributeCode.getAttributeCodePk().getAttributeCode());
		attributeCodeView.setLabel(attributeCode.getLabel());
		attributeCodeView.setDescription(attributeCode.getDescription());
		attributeCodeView.setGroupCode(attributeCode.getGroupCode());
		attributeCodeView.setSortOrder(attributeCode.getSortOrder());
		attributeCodeView.setHighlightStyle(attributeCode.getHighlightStyle());
		attributeCodeView.setBadgeUrl(attributeCode.getBadgeUrl());
		attributeCodeView.setArchitectureCode(attributeCode.getArchitectureCode());
		attributeCodeView.setActiveStatus(attributeCode.getActiveStatus());
		if (attributeCode.getArticle() != null) {
			attributeCodeView.setDetailUrl("api/v1/resource/attributes/" + attributeCode.getAttributeCodePk().getAttributeType() + "/" + attributeCode.getAttributeCodePk().getAttributeCode() + "/article");
		} else {
			attributeCodeView.setDetailUrl(attributeCode.getDetailUrl());
		}

		return attributeCodeView;
	}
	
	public static List<AttributeCodeView> toViews(List<AttributeCode> codes)
	{
		List<AttributeCodeView> views = new ArrayList<>();
		for(AttributeCode code: codes) {
			views.add(AttributeCodeView.toView(code));
		}
		return views;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getDetailUrl()
	{
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl)
	{
		this.detailUrl = detailUrl;
	}

	public Integer getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	public String getGroupCode()
	{
		return groupCode;
	}

	public void setGroupCode(String groupCode)
	{
		this.groupCode = groupCode;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getBadgeUrl()
	{
		return badgeUrl;
	}

	public void setBadgeUrl(String badgeUrl)
	{
		this.badgeUrl = badgeUrl;
	}

	public String getArchitectureCode()
	{
		return architectureCode;
	}

	public void setArchitectureCode(String architectureCode)
	{
		this.architectureCode = architectureCode;
	}

	public String getHighlightStyle()
	{
		return highlightStyle;
	}

	public void setHighlightStyle(String highlightStyle)
	{
		this.highlightStyle = highlightStyle;
	}

}
