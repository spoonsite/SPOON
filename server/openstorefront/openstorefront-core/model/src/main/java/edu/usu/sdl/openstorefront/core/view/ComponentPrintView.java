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
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.SecurityMarkingType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * ComponentDetailView Resource
 *
 * @author dshurtleff
 */
public class ComponentPrintView
		extends StandardEntityView
{

	private String componentId;
	private String componentName;
	private String description;

	@DataType(ComponentRelationshipView.class)
	private List<ComponentRelationshipView> relationships = new ArrayList<>();

	private String organization;
	private Date releaseDate;
	private String version;
	private String componentType;
	private Date lastActivityDate;
	private Date lastViewedDate;
	private String componentSecurityMarkingType;
	private String componentSecurityMarkingDescription;
	private Integer componentSecurityMarkingRank;
	private String componentSecurityMarkingStyle;
	private String componentIconId;
	private String componentTypeIconUrl;

	private ComponentEvaluationView evaluation = new ComponentEvaluationView();
	private List<ComponentQuestionView> questions = new ArrayList<>();
	private List<ComponentAttributeView> attributes = new ArrayList<>();
	private List<ComponentTag> tags = new ArrayList<>();
	private List<ComponentMetadataView> metadata = new ArrayList<>();
	private List<ComponentMediaView> componentMedia = new ArrayList<>();
	private List<ComponentContactView> contacts = new ArrayList<>();
	private List<ComponentResourceView> resources = new ArrayList<>();
	private List<ComponentReviewView> reviews = new ArrayList<>();
	private List<ComponentExternalDependencyView> dependencies = new ArrayList<>();

	private long componentViews = 0;

	public ComponentPrintView()
	{
	}

	public void setComponentDetails(Component component)
	{
		setComponentId(component.getComponentId());
		setComponentName(component.getName());
		setDescription(component.getDescription());
		setVersion(component.getVersion());
		setReleaseDate(component.getReleaseDate());
		setOrganization(component.getOrganization());
		setLastActivityDate(component.getLastActivityDts());
		setComponentType(component.getComponentType());

		Service service = ServiceProxyFactory.getServiceProxy();
		setComponentIconId(service.getComponentService().resolveComponentIcon(component.getComponentId()));
		setComponentTypeIconUrl(service.getComponentService().resolveComponentTypeIcon(component.getComponentType()));

		toStandardView(component);

		componentSecurityMarkingType = component.getSecurityMarkingType();

		if (StringUtils.isNotBlank(component.getSecurityMarkingType())) {
			SecurityMarkingType securityMarkingType = service.getLookupService().getLookupEnity(SecurityMarkingType.class, componentSecurityMarkingType);

			if (securityMarkingType != null) {
				componentSecurityMarkingDescription = securityMarkingType.getDescription();
				componentSecurityMarkingRank = securityMarkingType.getSortOrder();
				componentSecurityMarkingStyle = securityMarkingType.getHighlightStyle();
			}
		}
	}

	public static ComponentPrintView toView(ComponentDetailView component)
	{
		ComponentPrintView view = new ComponentPrintView();

		view.setComponentId(component.getComponentId());
		view.setComponentViews(component.getComponentViews());
		view.setComponentName(component.getName());
		view.setDescription(component.getDescription());
		view.setVersion(component.getVersion());
		view.setReleaseDate(component.getReleaseDate());
		view.setOrganization(component.getOrganization());
		view.setLastActivityDate(component.getLastActivityDts());
		view.setComponentType(component.getComponentType());
		view.setComponentIconId(component.getComponentIconId());
		view.setComponentTypeIconUrl(component.getComponentTypeIconUrl());
		view.setLastViewedDate(component.getLastViewedDts());
		view.setEvaluation(component.getEvaluation());
		view.setQuestions(component.getQuestions());
		view.setAttributes(component.getAttributes());
		view.setTags(component.getTags());
		view.setMetadata(component.getMetadata());
		view.setComponentMedia(component.getComponentMedia());
		view.setContacts(component.getContacts());
		view.setResources(component.getResources());
		view.setReviews(component.getReviews());
		view.setDependencies(component.getDependencies());
		view.setRelationships(component.getRelationships());
		view.toStandardView(component);

		view.setComponentSecurityMarkingType(component.getComponentSecurityMarkingType());
		view.setComponentSecurityMarkingDescription(component.getComponentSecurityMarkingDescription());
		view.setComponentSecurityMarkingRank(component.getComponentSecurityMarkingRank());
		view.setComponentSecurityMarkingStyle(component.getComponentSecurityMarkingStyle());

		return view;
	}

	/**
	 * @return the componentName
	 */
	public String getComponentName()
	{
		return componentName;
	}

	/**
	 * @param componentName the componentName to set
	 */
	public void setComponentName(String componentName)
	{
		this.componentName = componentName;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the organization
	 */
	public String getOrganization()
	{
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	/**
	 * @return the releaseDate
	 */
	public Date getReleaseDate()
	{
		return releaseDate;
	}

	/**
	 * @param releaseDate the releaseDate to set
	 */
	public void setReleaseDate(Date releaseDate)
	{
		this.releaseDate = releaseDate;
	}

	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

	/**
	 * @return the lastActivityDate
	 */
	public Date getLastActivityDate()
	{
		return lastActivityDate;
	}

	/**
	 * @param lastActivityDate the lastActivityDate to set
	 */
	public void setLastActivityDate(Date lastActivityDate)
	{
		this.lastActivityDate = lastActivityDate;
	}

	/**
	 * @return the lastViewedDate
	 */
	public Date getLastViewedDate()
	{
		return lastViewedDate;
	}

	/**
	 * @param lastViewedDate the lastViewedDate to set
	 */
	public void setLastViewedDate(Date lastViewedDate)
	{
		this.lastViewedDate = lastViewedDate;
	}

	/**
	 * @return the evaluation
	 */
	public ComponentEvaluationView getEvaluation()
	{
		return evaluation;
	}

	/**
	 * @param evaluation the evaluation to set
	 */
	public void setEvaluation(ComponentEvaluationView evaluation)
	{
		this.evaluation = evaluation;
	}

	/**
	 * @return the questions
	 */
	public List<ComponentQuestionView> getQuestions()
	{
		return questions;
	}

	/**
	 * @param questions the questions to set
	 */
	public void setQuestions(List<ComponentQuestionView> questions)
	{
		this.questions = questions;
	}

	/**
	 * @return the attributes
	 */
	public List<ComponentAttributeView> getAttributes()
	{
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<ComponentAttributeView> attributes)
	{
		this.attributes = attributes;
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
	 * @return the metadata
	 */
	public List<ComponentMetadataView> getMetadata()
	{
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(List<ComponentMetadataView> metadata)
	{
		this.metadata = metadata;
	}

	/**
	 * @return the componentMedia
	 */
	public List<ComponentMediaView> getComponentMedia()
	{
		return componentMedia;
	}

	/**
	 * @param componentMedia the componentMedia to set
	 */
	public void setComponentMedia(List<ComponentMediaView> componentMedia)
	{
		this.componentMedia = componentMedia;
	}

	/**
	 * @return the contacts
	 */
	public List<ComponentContactView> getContacts()
	{
		return contacts;
	}

	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(List<ComponentContactView> contacts)
	{
		this.contacts = contacts;
	}

	/**
	 * @return the resources
	 */
	public List<ComponentResourceView> getResources()
	{
		return resources;
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(List<ComponentResourceView> resources)
	{
		this.resources = resources;
	}

	/**
	 * @return the reviews
	 */
	public List<ComponentReviewView> getReviews()
	{
		return reviews;
	}

	/**
	 * @param reviews the reviews to set
	 */
	public void setReviews(List<ComponentReviewView> reviews)
	{
		this.reviews = reviews;
	}

	/**
	 * @return the dependencies
	 */
	public List<ComponentExternalDependencyView> getDependencies()
	{
		return dependencies;
	}

	/**
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(List<ComponentExternalDependencyView> dependencies)
	{
		this.dependencies = dependencies;
	}

	/**
	 * @return the componentViews
	 */
	public long getComponentViews()
	{
		return componentViews;
	}

	/**
	 * @param componentViews the componentViews to set
	 */
	public void setComponentViews(long componentViews)
	{
		this.componentViews = componentViews;
	}

	/**
	 * @return the componentId
	 */
	public String getComponentId()
	{
		return componentId;
	}

	/**
	 * @param componentId the componentId to set
	 */
	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public List<ComponentRelationshipView> getRelationships()
	{
		return relationships;
	}

	public void setRelationships(List<ComponentRelationshipView> relationships)
	{
		this.relationships = relationships;
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

	public String getComponentSecurityMarkingType()
	{
		return componentSecurityMarkingType;
	}

	public void setComponentSecurityMarkingType(String componentSecurityMarkingType)
	{
		this.componentSecurityMarkingType = componentSecurityMarkingType;
	}

	public String getComponentSecurityMarkingDescription()
	{
		return componentSecurityMarkingDescription;
	}

	public void setComponentSecurityMarkingDescription(String componentSecurityMarkingDescription)
	{
		this.componentSecurityMarkingDescription = componentSecurityMarkingDescription;
	}

	public Integer getComponentSecurityMarkingRank()
	{
		return componentSecurityMarkingRank;
	}

	public void setComponentSecurityMarkingRank(Integer componentSecurityMarkingRank)
	{
		this.componentSecurityMarkingRank = componentSecurityMarkingRank;
	}

	public String getComponentSecurityMarkingStyle()
	{
		return componentSecurityMarkingStyle;
	}

	public void setComponentSecurityMarkingStyle(String componentSecurityMarkingStyle)
	{
		this.componentSecurityMarkingStyle = componentSecurityMarkingStyle;
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

}
