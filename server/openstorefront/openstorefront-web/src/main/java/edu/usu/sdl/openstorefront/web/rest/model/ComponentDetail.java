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

import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.ParamTypeDescription;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * ComponentDetail Resource
 *
 * @author dshurtleff
 */
public class ComponentDetail
{

	@NotNull
	@ParamTypeDescription("Key")
	private Long componentId;

	@NotNull
	private String guid;

	@NotNull
	private String name;

	@NotNull
	private String description;

	private ComponentRelationship parentComponent;

	@DataType(ComponentRelationship.class)
	private List<ComponentRelationship> subComponents = new ArrayList<>();

	@NotNull
	private String organization;

	private Date releaseDate;
	private String version;

	@NotNull
	private String activeStatus;

	@NotNull
	private Date lastActivityDts;

	@NotNull
	private String createUser;

	@NotNull
	private Date createDts;

	@NotNull
	private String updateUser;

	@NotNull
	private Date updateDts;

	@NotNull
	private Date approvedDate;
	private String approvalState;

	@NotNull
	private String approvedUser;

	private ComponentEvaluation evaluation = new ComponentEvaluation();

	@DataType(ComponentQuestion.class)
	private List<ComponentQuestion> questions = new ArrayList<>();

	@DataType(ComponentAttribute.class)
	private List<ComponentAttribute> attributes = new ArrayList<>();

	@DataType(ComponentTag.class)
	private List<ComponentTag> tags = new ArrayList<>();

	@DataType(ComponentMetadata.class)
	private List<ComponentMetadata> metadata = new ArrayList<>();

	@DataType(ComponentMedia.class)
	private List<ComponentMedia> componentMedia = new ArrayList<>();

	@DataType(ComponentContact.class)
	private List<ComponentContact> contacts = new ArrayList<>();

	@DataType(ComponentResource.class)
	private List<ComponentResource> resources = new ArrayList<>();

	@DataType(ComponentReview.class)
	private List<ComponentReview> reviews = new ArrayList<>();

	@DataType(ComponentExternalDependancy.class)
	private List<ComponentExternalDependancy> dependencies = new ArrayList<>();

	public ComponentDetail()
	{
	}

	public Long getComponentId()
	{
		return componentId;
	}

	public void setComponentId(Long componentId)
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

	public Date getReleaseDate()
	{
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate)
	{
		this.releaseDate = releaseDate;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public Date getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	public String getUpdateUser()
	{
		return updateUser;
	}

	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public ComponentEvaluation getEvaluation()
	{
		return evaluation;
	}

	public void setEvaluation(ComponentEvaluation evaluation)
	{
		this.evaluation = evaluation;
	}

	public List<ComponentAttribute> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(List<ComponentAttribute> attributes)
	{
		this.attributes = attributes;
	}

	public List<ComponentMetadata> getMetadata()
	{
		return metadata;
	}

	public void setMetadata(List<ComponentMetadata> metadata)
	{
		this.metadata = metadata;
	}

	public List<ComponentMedia> getComponentMedia()
	{
		return componentMedia;
	}

	public void setComponentMedia(List<ComponentMedia> componentMedia)
	{
		this.componentMedia = componentMedia;
	}

	public List<ComponentContact> getContacts()
	{
		return contacts;
	}

	public void setContacts(List<ComponentContact> contacts)
	{
		this.contacts = contacts;
	}

	public List<ComponentResource> getResources()
	{
		return resources;
	}

	public void setResources(List<ComponentResource> resources)
	{
		this.resources = resources;
	}

	public List<ComponentReview> getReviews()
	{
		return reviews;
	}

	public void setReviews(List<ComponentReview> reviews)
	{
		this.reviews = reviews;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public Date getApprovedDate()
	{
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate)
	{
		this.approvedDate = approvedDate;
	}

	public String getApprovalState()
	{
		return approvalState;
	}

	public void setApprovalState(String approvalState)
	{
		this.approvalState = approvalState;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public String getApprovedUser()
	{
		return approvedUser;
	}

	public void setApprovedUser(String approvedUser)
	{
		this.approvedUser = approvedUser;
	}

	public List<ComponentQuestion> getQuestions()
	{
		return questions;
	}

	public void setQuestions(List<ComponentQuestion> questions)
	{
		this.questions = questions;
	}

	public ComponentRelationship getParentComponent()
	{
		return parentComponent;
	}

	public void setParentComponent(ComponentRelationship parentComponent)
	{
		this.parentComponent = parentComponent;
	}

	public List<ComponentRelationship> getSubComponents()
	{
		return subComponents;
	}

	public void setSubComponents(List<ComponentRelationship> subComponents)
	{
		this.subComponents = subComponents;
	}

	public List<ComponentExternalDependancy> getDependencies()
	{
		return dependencies;
	}

	public void setDependencies(List<ComponentExternalDependancy> dependencies)
	{
		this.dependencies = dependencies;
	}

	public List<ComponentTag> getTags()
	{
		return tags;
	}

	public void setTags(List<ComponentTag> tags)
	{
		this.tags = tags;
	}

	public Date getLastActivityDts()
	{
		return lastActivityDts;
	}

	public void setLastActivityDts(Date lastActivityDts)
	{
		this.lastActivityDts = lastActivityDts;
	}

}
