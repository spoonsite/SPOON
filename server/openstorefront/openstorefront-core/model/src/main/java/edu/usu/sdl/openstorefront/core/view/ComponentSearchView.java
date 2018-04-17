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
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.SecurityMarkingType;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeNestedModel;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeOptions;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jlaw
 */
public class ComponentSearchView
		extends StandardEntityView
{

	public static final String FIELD_NAME = "name";
	public static final String FIELD_DESCRIPTION = "description";

	public static final String FIELD_ORGANIZATION = "organization";
	public static final String FIELD_SEARCH_SCORE = "searchScore";

	private String listingType;
	private String componentId;
	private String name;
	private String description;
	private String parentComponentId;
	private String guid;
	private String organization;
	private String version;
	private String approvalState;
	private String approvedUser;
	private String articleAttributeType;
	private String articleAttributeCode;
	private String componentType;
	private String componentTypeDescription;
	private String componentIconId;
	private String componentTypeIconUrl;
	private Integer averageRating;
	private Date releaseDate;
	private Date approvedDts;
	private Date lastActivityDts;
	private String listingSecurityMarkingType;
	private String listingSecurityMarkingDescription;
	private Integer listingSecurityMarkingRank;
	private String listingSecurityMarkingStyle;
	private float searchScore;
	private String dataSource;
	private String dataSensitivity;

	@APIDescription("This is the whole hiearchy for the entry type")
	private ComponentTypeNestedModel componentTypeNestedModel;
	private boolean includeIconInSearch;

	@DataType(ComponentTag.class)
	private List<ComponentTag> tags = new ArrayList<>();

	private String activeStatus;
	private String createUser;
	private Date createDts;
	private String updateUser;
	private Date updateDts;

	@DataType(SearchResultAttribute.class)
	private List<SearchResultAttribute> attributes = new ArrayList<>();

	public static ComponentSearchView toView(Component component)
	{
		Service service = ServiceProxyFactory.getServiceProxy();
		ComponentAttribute example = new ComponentAttribute();
		ComponentAttributePk pk = new ComponentAttributePk();
		pk.setComponentId(component.getComponentId());
		example.setComponentAttributePk(pk);
		List<ComponentAttribute> attributes = service.getPersistenceService().queryByExample(new QueryByExample<>(example));
		List<ComponentReview> reviews = service.getComponentService().getBaseComponent(ComponentReview.class, component.getComponentId());
		List<ComponentTag> tags = service.getComponentService().getBaseComponent(ComponentTag.class, component.getComponentId());
		return toView(component, attributes, reviews, tags);
	}

	public static ComponentSearchView toView(Component component, List<ComponentAttribute> attributes, List<ComponentReview> reviews, List<ComponentTag> tags)
	{
		ComponentSearchView view = new ComponentSearchView();
		view.setListingType(OpenStorefrontConstant.ListingType.COMPONENT.getDescription());
		view.setComponentId(component.getComponentId());
		view.setName(component.getName());
		view.setDescription(component.getDescription());
		view.setGuid(component.getGuid());
		view.setApprovalState(component.getApprovalState());
		view.setApprovedDts(component.getApprovedDts());
		view.setApprovedUser(component.getApprovedUser());
		view.setLastActivityDts(component.getLastActivityDts());
		view.setOrganization(component.getOrganization());
		view.setReleaseDate(component.getReleaseDate());
		view.setVersion(component.getVersion());
		view.setComponentType(component.getComponentType());
		view.setComponentTypeDescription(TranslateUtil.translateComponentType(component.getComponentType()));
		view.setDataSource(component.getDataSource());
		view.setDataSensitivity(component.getDataSensitivity());

		Service service = ServiceProxyFactory.getServiceProxy();
		view.setComponentIconId(service.getComponentService().resolveComponentIcon(component.getComponentId()));
		view.setComponentTypeIconUrl(service.getComponentService().resolveComponentTypeIcon(component.getComponentType()));
		view.setIncludeIconInSearch(service.getComponentService().resolveComponentTypeIncludeIconInSearch(component.getComponentType()));

		ComponentTypeOptions options = new ComponentTypeOptions(component.getComponentType());
		options.setPullParents(true);
		view.setComponentTypeNestedModel(service.getComponentService().getComponentType(options));

		List<SearchResultAttribute> componentAttributes = new ArrayList<>();
		for (ComponentAttribute attribute : attributes) {

			componentAttributes.add(SearchResultAttribute.toView(attribute));
			view.toStandardView(attribute);
		}
		view.setAttributes(componentAttributes);

		for (ComponentTag tag : tags) {
			view.toStandardView(tag);
		}

		view.setTags(tags);
		Integer total = 0;
		for (ComponentReview review : reviews) {
			total = total + review.getRating();
			view.toStandardView(review);
		}
		if (reviews.size() > 0) {
			view.setAverageRating(total / reviews.size());
		} else {
			view.setAverageRating(0);
		}

		view.setActiveStatus(component.getActiveStatus());
		view.setCreateUser(component.getCreateUser());
		view.setUpdateDts(component.getUpdateDts());
		view.setCreateDts(component.getCreateDts());
		view.setUpdateUser(component.getUpdateUser());
		view.toStandardView(component);

		view.setListingSecurityMarkingType(component.getSecurityMarkingType());

		if (StringUtils.isNotBlank(component.getSecurityMarkingType())) {
			SecurityMarkingType securityMarkingType = service.getLookupService().getLookupEnity(SecurityMarkingType.class, component.getSecurityMarkingType());

			if (securityMarkingType != null) {
				view.setListingSecurityMarkingDescription(securityMarkingType.getDescription());
				view.setListingSecurityMarkingRank(securityMarkingType.getSortOrder());
				view.setListingSecurityMarkingStyle(securityMarkingType.getHighlightStyle());
			}
		}

		return view;
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

	public String getParentComponentId()
	{
		return parentComponentId;
	}

	public void setParentComponentId(String parentComponentId)
	{
		this.parentComponentId = parentComponentId;
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

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getApprovalState()
	{
		return approvalState;
	}

	public void setApprovalState(String approvalState)
	{
		this.approvalState = approvalState;
	}

	public String getApprovedUser()
	{
		return approvedUser;
	}

	public void setApprovedUser(String approvedUser)
	{
		this.approvedUser = approvedUser;
	}

	public Date getApprovedDts()
	{
		return approvedDts;
	}

	public void setApprovedDts(Date approvedDts)
	{
		this.approvedDts = approvedDts;
	}

	public Date getLastActivityDts()
	{
		return lastActivityDts;
	}

	public void setLastActivityDts(Date lastActivityDts)
	{
		this.lastActivityDts = lastActivityDts;
	}

	public Date getReleaseDate()
	{
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate)
	{
		this.releaseDate = releaseDate;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	/**
	 * @return the attributes
	 */
	public List<SearchResultAttribute> getAttributes()
	{
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<SearchResultAttribute> attributes)
	{
		this.attributes = attributes;
	}

	/**
	 * @return the activeStatus
	 */
	public String getActiveStatus()
	{
		return activeStatus;
	}

	/**
	 * @param activeStatus the activeStatus to set
	 */
	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	/**
	 * @return the createUser
	 */
	public String getCreateUser()
	{
		return createUser;
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	/**
	 * @return the createDts
	 */
	public Date getCreateDts()
	{
		return createDts;
	}

	/**
	 * @param createDts the createDts to set
	 */
	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	/**
	 * @return the updateUser
	 */
	public String getUpdateUser()
	{
		return updateUser;
	}

	/**
	 * @param updateUser the updateUser to set
	 */
	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}

	/**
	 * @return the updateDts
	 */
	public Date getUpdateDts()
	{
		return updateDts;
	}

	/**
	 * @param updateDts the updateDts to set
	 */
	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	/**
	 * @return the tags
	 */
	public List<ComponentTag> getTags()
	{
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<ComponentTag> tags)
	{
		this.tags = tags;
	}

	/**
	 * @return the listingType
	 */
	public String getListingType()
	{
		return listingType;
	}

	/**
	 * @param listingType the listingType to set
	 */
	public void setListingType(String listingType)
	{
		this.listingType = listingType;
	}

	/**
	 * @return the articleAttributeType
	 */
	public String getArticleAttributeType()
	{
		return articleAttributeType;
	}

	/**
	 * @param articleAttributeType the articleAttributeType to set
	 */
	public void setArticleAttributeType(String articleAttributeType)
	{
		this.articleAttributeType = articleAttributeType;
	}

	/**
	 * @return the articleAttributeCode
	 */
	public String getArticleAttributeCode()
	{
		return articleAttributeCode;
	}

	/**
	 * @param articleAttributeCode the articleAttributeCode to set
	 */
	public void setArticleAttributeCode(String articleAttributeCode)
	{
		this.articleAttributeCode = articleAttributeCode;
	}

	/**
	 * @return the averageRating
	 */
	public Integer getAverageRating()
	{
		return averageRating;
	}

	/**
	 * @param averageRating the averageRating to set
	 */
	public void setAverageRating(Integer averageRating)
	{
		this.averageRating = averageRating;
	}

	/**
	 * @return the componentType
	 */
	public String getComponentType()
	{
		return componentType;
	}

	/**
	 * @param componentType the componentType to set
	 */
	public void setComponentType(String componentType)
	{
		this.componentType = componentType;
	}

	public String getListingSecurityMarkingType()
	{
		return listingSecurityMarkingType;
	}

	public void setListingSecurityMarkingType(String listingSecurityMarkingType)
	{
		this.listingSecurityMarkingType = listingSecurityMarkingType;
	}

	public String getListingSecurityMarkingDescription()
	{
		return listingSecurityMarkingDescription;
	}

	public void setListingSecurityMarkingDescription(String listingSecurityMarkingDescription)
	{
		this.listingSecurityMarkingDescription = listingSecurityMarkingDescription;
	}

	public Integer getListingSecurityMarkingRank()
	{
		return listingSecurityMarkingRank;
	}

	public void setListingSecurityMarkingRank(Integer listingSecurityMarkingRank)
	{
		this.listingSecurityMarkingRank = listingSecurityMarkingRank;
	}

	public String getListingSecurityMarkingStyle()
	{
		return listingSecurityMarkingStyle;
	}

	public void setListingSecurityMarkingStyle(String listingSecurityMarkingStyle)
	{
		this.listingSecurityMarkingStyle = listingSecurityMarkingStyle;
	}

	public String getComponentTypeDescription()
	{
		return componentTypeDescription;
	}

	public void setComponentTypeDescription(String componentTypeDescription)
	{
		this.componentTypeDescription = componentTypeDescription;
	}

	public float getSearchScore()
	{
		return searchScore;
	}

	public void setSearchScore(float searchScore)
	{
		this.searchScore = searchScore;
	}

	public String getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(String dataSource)
	{
		this.dataSource = dataSource;
	}

	@Override
	public String getDataSensitivity()
	{
		return dataSensitivity;
	}

	@Override
	public void setDataSensitivity(String dataSensitivity)
	{
		this.dataSensitivity = dataSensitivity;
	}

	public String getComponentIconId()
	{
		return componentIconId;
	}

	public void setComponentIconId(String componentIconId)
	{
		this.componentIconId = componentIconId;
	}

	public String getComponentTypeIconUrl()
	{
		return componentTypeIconUrl;
	}

	public void setComponentTypeIconUrl(String componentTypeIconUrl)
	{
		this.componentTypeIconUrl = componentTypeIconUrl;
	}

	public ComponentTypeNestedModel getComponentTypeNestedModel()
	{
		return componentTypeNestedModel;
	}

	public void setComponentTypeNestedModel(ComponentTypeNestedModel componentTypeNestedModel)
	{
		this.componentTypeNestedModel = componentTypeNestedModel;
	}

	public Boolean getIncludeIconInSearch()
	{
		return includeIconInSearch;
	}

	public void setIncludeIconInSearch(Boolean includeIconInSearch)
	{
		this.includeIconInSearch = includeIconInSearch;
	}

}
