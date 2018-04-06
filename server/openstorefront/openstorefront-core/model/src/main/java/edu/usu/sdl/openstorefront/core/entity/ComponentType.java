/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.validation.CleanKeySanitizer;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Actual type of component")
public class ComponentType
		extends StandardEntity<ComponentType>
{

	public static final String FIELD_LABEL = "label";

	private static final long serialVersionUID = 1L;

	@PK(generated = false)
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@NotNull
	@Sanitize(CleanKeySanitizer.class)
	@ConsumeField
	private String componentType;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@Sanitize(CleanKeySanitizer.class)
	private String parentComponentType;

	@DataType(RoleLink.class)
	@ConsumeField
	@Embedded
	@OneToMany(orphanRemoval = true)
	private List<RoleLink> assignedGroups;

	@DataType(UserLink.class)
	@ConsumeField
	@Embedded
	@OneToMany(orphanRemoval = true)
	private List<UserLink> assignedUsers;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String label;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_64K)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String description;

	@ConsumeField
	private Boolean allowOnSubmission;

	@ConsumeField
	private Boolean dataEntryAttributes;

	@ConsumeField
	private Boolean dataEntryRelationships;

	@ConsumeField
	private Boolean dataEntryContacts;

	@ConsumeField
	private Boolean dataEntryResources;

	@ConsumeField
	private Boolean dataEntryMedia;

	@ConsumeField
	private Boolean dataEntryDependencies;

	@ConsumeField
	private Boolean dataEntryMetadata;

	@ConsumeField
	private Boolean dataEntryEvaluationInformation;

	@ConsumeField
	private Boolean dataEntryReviews;

	@ConsumeField
	private Boolean dataEntryQuestions;

	@FK(value = ComponentTypeTemplate.class, enforce = true)
	@ConsumeField
	private String componentTypeTemplate;

	@FK(value = SubmissionFormTemplate.class, enforce = true)
	@ConsumeField
	private String submissionTemplateId;

	@ConsumeField
	@APIDescription("This url to an icon for a type; Typically points to general media")
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String iconUrl;

	@ConsumeField
	@APIDescription("Used to indicate whether or not to display the component type icon in the search results")
	private Boolean includeIconInSearch;

	public static final String COMPONENT = "COMP";
	public static final String ARTICLE = "ARTICLE";

	//Dummy Value for filtering
	public static final String ALL = "ALL";

	public ComponentType()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		ComponentType componentTypeLocal = (ComponentType) entity;

		this.setLabel(componentTypeLocal.getLabel());
		this.setDescription(componentTypeLocal.getDescription());
		this.setComponentTypeTemplate(componentTypeLocal.getComponentTypeTemplate());
		this.setDataEntryAttributes(componentTypeLocal.getDataEntryAttributes());
		this.setDataEntryContacts(componentTypeLocal.getDataEntryContacts());
		this.setDataEntryDependencies(componentTypeLocal.getDataEntryDependencies());
		this.setDataEntryEvaluationInformation(componentTypeLocal.getDataEntryEvaluationInformation());
		this.setDataEntryMedia(componentTypeLocal.getDataEntryMedia());
		this.setDataEntryMetadata(componentTypeLocal.getDataEntryMetadata());
		this.setDataEntryRelationships(componentTypeLocal.getDataEntryRelationships());
		this.setDataEntryResources(componentTypeLocal.getDataEntryResources());
		this.setDataEntryReviews(componentTypeLocal.getDataEntryReviews());
		this.setDataEntryQuestions(componentTypeLocal.getDataEntryQuestions());
		this.setAllowOnSubmission(componentTypeLocal.getAllowOnSubmission());
		this.setIconUrl(componentTypeLocal.getIconUrl());
		this.setAssignedGroups(componentTypeLocal.getAssignedGroups());
		this.setAssignedUsers(componentTypeLocal.getAssignedUsers());
		this.setParentComponentType(componentTypeLocal.getParentComponentType());
		this.setIncludeIconInSearch(componentTypeLocal.getIncludeIconInSearch());
		this.setSubmissionTemplateId(componentTypeLocal.getSubmissionTemplateId());

	}

	public String getComponentType()
	{
		return componentType;
	}

	public void setComponentType(String componentType)
	{
		this.componentType = componentType;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Boolean getDataEntryAttributes()
	{
		return dataEntryAttributes;
	}

	public void setDataEntryAttributes(Boolean dataEntryAttributes)
	{
		this.dataEntryAttributes = dataEntryAttributes;
	}

	public Boolean getDataEntryRelationships()
	{
		return dataEntryRelationships;
	}

	public void setDataEntryRelationships(Boolean dataEntryRelationships)
	{
		this.dataEntryRelationships = dataEntryRelationships;
	}

	public Boolean getDataEntryContacts()
	{
		return dataEntryContacts;
	}

	public void setDataEntryContacts(Boolean dataEntryContacts)
	{
		this.dataEntryContacts = dataEntryContacts;
	}

	public Boolean getDataEntryResources()
	{
		return dataEntryResources;
	}

	public void setDataEntryResources(Boolean dataEntryResources)
	{
		this.dataEntryResources = dataEntryResources;
	}

	public Boolean getDataEntryMedia()
	{
		return dataEntryMedia;
	}

	public void setDataEntryMedia(Boolean dataEntryMedia)
	{
		this.dataEntryMedia = dataEntryMedia;
	}

	public Boolean getDataEntryDependencies()
	{
		return dataEntryDependencies;
	}

	public void setDataEntryDependencies(Boolean dataEntryDependencies)
	{
		this.dataEntryDependencies = dataEntryDependencies;
	}

	public Boolean getDataEntryMetadata()
	{
		return dataEntryMetadata;
	}

	public void setDataEntryMetadata(Boolean dataEntryMetadata)
	{
		this.dataEntryMetadata = dataEntryMetadata;
	}

	public Boolean getDataEntryEvaluationInformation()
	{
		return dataEntryEvaluationInformation;
	}

	public void setDataEntryEvaluationInformation(Boolean dataEntryEvaluationInformation)
	{
		this.dataEntryEvaluationInformation = dataEntryEvaluationInformation;
	}

	public String getComponentTypeTemplate()
	{
		return componentTypeTemplate;
	}

	public void setComponentTypeTemplate(String componentTypeTemplate)
	{
		this.componentTypeTemplate = componentTypeTemplate;
	}

	public Boolean getDataEntryReviews()
	{
		return dataEntryReviews;
	}

	public void setDataEntryReviews(Boolean dataEntryReviews)
	{
		this.dataEntryReviews = dataEntryReviews;
	}

	public Boolean getDataEntryQuestions()
	{
		return dataEntryQuestions;
	}

	public void setDataEntryQuestions(Boolean dataEntryQuestions)
	{
		this.dataEntryQuestions = dataEntryQuestions;
	}

	public Boolean getAllowOnSubmission()
	{
		return allowOnSubmission;
	}

	public void setAllowOnSubmission(Boolean allowOnSubmission)
	{
		this.allowOnSubmission = allowOnSubmission;
	}

	public String getParentComponentType()
	{
		return parentComponentType;
	}

	public void setParentComponentType(String parentComponentType)
	{
		this.parentComponentType = parentComponentType;
	}

	public String getIconUrl()
	{
		return iconUrl;
	}

	public void setIconUrl(String iconUrl)
	{
		this.iconUrl = iconUrl;
	}

	public List<RoleLink> getAssignedGroups()
	{
		return assignedGroups;
	}

	public void setAssignedGroups(List<RoleLink> assignedGroups)
	{
		this.assignedGroups = assignedGroups;
	}

	public List<UserLink> getAssignedUsers()
	{
		return assignedUsers;
	}

	public void setAssignedUsers(List<UserLink> assignedUsers)
	{
		this.assignedUsers = assignedUsers;
	}

	public Boolean getIncludeIconInSearch()
	{
		return includeIconInSearch;
	}

	public void setIncludeIconInSearch(Boolean includeIconInSearch)
	{
		this.includeIconInSearch = includeIconInSearch;
	}

	public String getSubmissionTemplateId()
	{
		return submissionTemplateId;
	}

	public void setSubmissionTemplateId(String submissionTemplateId)
	{
		this.submissionTemplateId = submissionTemplateId;
	}

}
