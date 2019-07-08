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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.ParamTypeDescription;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.SecurityMarkingType;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeNestedModel;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeOptions;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeTemplateResolution;
import edu.usu.sdl.openstorefront.core.model.EvaluationAll;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

/**
 * ComponentDetailView Resource
 *
 * @author dshurtleff
 */
public class ComponentDetailView
		extends StandardEntityView
{

	@NotNull
	@ParamTypeDescription("Key")
	private String componentId;

	@NotNull
	private String guid;

	@NotNull
	private String name;

	@NotNull
	private String description;

	@DataType(ComponentRelationshipView.class)
	private List<ComponentRelationshipView> relationships = new ArrayList<>();

	@NotNull
	private String organization;

	private Date releaseDate;
	private String version;

	@NotNull
	private String activeStatus;

	@NotNull
	private Date lastActivityDts;

	private Date lastViewedDts;

	private Date today;

	private Date lastSubmitDts;

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

	private String componentType;

	@NotNull
	private String approvedUser;

	private Date submittedDts;
	private String notifyOfApprovalEmail;

	private String componentSecurityMarkingType;
	private String componentSecurityMarkingDescription;
	private Integer componentSecurityMarkingRank;
	private String componentSecurityMarkingStyle;
	private String dataSource;
	private String storageVersion;
	private Integer recordVersion;
	private String componentTypeLabel;
	private String approvalStateLabel;
	private String componentIconId;

	@APIDescription("This is the resolved icon")
	private String componentTypeIconUrl;

	@APIDescription("This is the whole hiearchy for the entry type")
	private ComponentTypeNestedModel componentTypeNestedModel;

	@APIDescription("This entries direct type")
	private ComponentType componentTypeFull;

	@APIDescription("This is the display template that should be showing")
	private String componentTemplateId;

	private ComponentEvaluationView evaluation = new ComponentEvaluationView();

	@DataType(ComponentQuestionView.class)
	private List<ComponentQuestionView> questions = new ArrayList<>();

	@DataType(ComponentAttributeView.class)
	private List<ComponentAttributeView> attributes = new ArrayList<>();

	@DataType(ComponentTag.class)
	private List<ComponentTag> tags = new ArrayList<>();

	@DataType(ComponentMetadataView.class)
	private List<ComponentMetadataView> metadata = new ArrayList<>();

	@DataType(ComponentMediaView.class)
	private List<ComponentMediaView> componentMedia = new ArrayList<>();

	@DataType(ComponentContactView.class)
	private List<ComponentContactView> contacts = new ArrayList<>();

	@DataType(ComponentResourceView.class)
	private List<ComponentResourceView> resources = new ArrayList<>();

	@DataType(ComponentReviewView.class)
	private List<ComponentReviewView> reviews = new ArrayList<>();

	@DataType(ComponentExternalDependencyView.class)
	private List<ComponentExternalDependencyView> dependencies = new ArrayList<>();

	@DataType(EvaluationAll.class)
	private List<EvaluationAll> fullEvaluations = new ArrayList<>();

	private long componentViews = 0;

	public static ComponentDetailView toView(ComponentAll componentAll)
	{
		return toView(componentAll, new ArrayList<>());
	}

	public static ComponentDetailView toView(ComponentAll componentAll, List<EvaluationAll> evaluations)
	{
		ComponentDetailView detailView = new ComponentDetailView();

		detailView.setRelationships(ComponentRelationshipView.toViewList(componentAll.getRelationships()));
		detailView.setAttributes(ComponentAttributeView.toViewList(componentAll.getAttributes()));
		detailView.setContacts(ComponentContactView.toViewList(componentAll.getContacts()));
		detailView.setComponentMedia(ComponentMediaView.toViewList(componentAll.getMedia()));
		detailView.setResources(ComponentResourceView.toViewList(componentAll.getResources()));
		detailView.setReviews(ComponentReviewView.toViewListAll(componentAll.getReviews()));
		detailView.setQuestions(ComponentQuestionView.toViewListAll(componentAll.getQuestions()));
		detailView.setDependencies(ComponentExternalDependencyView.toViewList(componentAll.getExternalDependencies()));
		detailView.setMetadata(ComponentMetadataView.toViewList(componentAll.getMetadata()));
		detailView.setTags(componentAll.getTags());
		detailView.setEvaluation(ComponentEvaluationView.toViewFromStorage(componentAll.getEvaluationSections()));
		detailView.setFullEvaluations(evaluations);

		detailView.setComponentDetails(componentAll.getComponent());
		return detailView;
	}

	@SuppressWarnings("unchecked")
	public void setComponentDetails(Component component)
	{
		name = component.getName();
		guid = component.getGuid();
		description = component.getDescription();
		approvedDate = component.getApprovedDts();
		approvedUser = component.getApprovedUser();
		approvalState = component.getApprovalState();
		createUser = component.getCreateUser();
		createDts = component.getCreateDts();
		version = component.getVersion();
		activeStatus = component.getActiveStatus();
		releaseDate = component.getReleaseDate();
		organization = component.getOrganization();
		submittedDts = component.getSubmittedDts();
		notifyOfApprovalEmail = component.getNotifyOfApprovalEmail();
		lastActivityDts = component.getLastActivityDts();
		lastSubmitDts = component.getSubmittedDts();
		componentType = component.getComponentType();
		dataSource = component.getDataSource();
		storageVersion = component.getStorageVersion();
		recordVersion = component.getRecordVersion();
		if (recordVersion == null) {
			recordVersion = 1;
		}
		approvalStateLabel = TranslateUtil.translate(ApprovalStatus.class, component.getApprovalState());
		Service service = ServiceProxyFactory.getServiceProxy();
		
		componentTypeLabel = service.getComponentService().getComponentTypeParentsString(componentType, true);
		
		componentIconId = service.getComponentService().resolveComponentIcon(componentId);
		componentTypeIconUrl = service.getComponentService().resolveComponentTypeIcon(componentType);

		ComponentTypeTemplateResolution componentTypeTemplateResolution = service.getComponentService().findTemplateForComponentType(componentType);
		if (componentTypeTemplateResolution != null) {
			componentTemplateId = componentTypeTemplateResolution.getTemplateId();
		}
		ComponentTypeOptions options = new ComponentTypeOptions(component.getComponentType());
		options.setPullParents(false);
		options.setPullChildren(false);
		componentTypeFull = service.getComponentService().getComponentType(options).getComponentType();

		options = new ComponentTypeOptions(component.getComponentType());
		options.setPullParents(true);
		componentTypeNestedModel = service.getComponentService().getComponentType(options);
		componentSecurityMarkingType = component.getSecurityMarkingType();

		if (StringUtils.isNotBlank(component.getSecurityMarkingType())) {

			SecurityMarkingType securityMarkingType = service.getLookupService().getLookupEnity(SecurityMarkingType.class, componentSecurityMarkingType);

			if (securityMarkingType != null) {
				componentSecurityMarkingDescription = securityMarkingType.getDescription();
				componentSecurityMarkingRank = securityMarkingType.getSortOrder();
				componentSecurityMarkingStyle = securityMarkingType.getHighlightStyle();
			}
		}

		this.toStandardView(component);
		this.toStandardViewBaseEntities(tags);
		this.toStandardView(questions);
		this.toStandardView(attributes);
		this.toStandardView(metadata);
		this.toStandardView(componentMedia);
		this.toStandardView(contacts);
		this.toStandardView(resources);
		this.toStandardView(relationships);
		this.toStandardView(reviews);
		this.toStandardView(dependencies);
		this.toStandardView(evaluation.getEvaluationSections());
		for (EvaluationAll evaluationAll : fullEvaluations) {
			this.toStandardView(evaluationAll.getEvaluation());
		}

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

	public Date getLastSubmitDts() {
		return this.lastSubmitDts;
	}

	public void setLastSubmitDts(Date lastSubmitDts) {
		this.lastSubmitDts = lastSubmitDts;
	}


	public ComponentEvaluationView getEvaluation()
	{
		return evaluation;
	}

	public void setEvaluation(ComponentEvaluationView evaluation)
	{
		this.evaluation = evaluation;
	}

	public List<ComponentAttributeView> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(List<ComponentAttributeView> attributes)
	{
		this.attributes = attributes;
	}

	public List<ComponentMetadataView> getMetadata()
	{
		return metadata;
	}

	public void setMetadata(List<ComponentMetadataView> metadata)
	{
		this.metadata = metadata;
	}

	public List<ComponentMediaView> getComponentMedia()
	{
		return componentMedia;
	}

	public void setComponentMedia(List<ComponentMediaView> componentMedia)
	{
		this.componentMedia = componentMedia;
	}

	public List<ComponentContactView> getContacts()
	{
		return contacts;
	}

	public void setContacts(List<ComponentContactView> contacts)
	{
		this.contacts = contacts;
	}

	public List<ComponentResourceView> getResources()
	{
		return resources;
	}

	public void setResources(List<ComponentResourceView> resources)
	{
		this.resources = resources;
	}

	public List<ComponentReviewView> getReviews()
	{
		return reviews;
	}

	public void setReviews(List<ComponentReviewView> reviews)
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

	public List<ComponentQuestionView> getQuestions()
	{
		return questions;
	}

	public void setQuestions(List<ComponentQuestionView> questions)
	{
		this.questions = questions;
	}

	public List<ComponentExternalDependencyView> getDependencies()
	{
		return dependencies;
	}

	public void setDependencies(List<ComponentExternalDependencyView> dependencies)
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

	public long getComponentViews()
	{
		return componentViews;
	}

	public void setComponentViews(long componentViews)
	{
		this.componentViews = componentViews;
	}

	public Date getLastViewedDts()
	{
		return lastViewedDts;
	}

	public void setLastViewedDts(Date lastViewedDts)
	{
		this.lastViewedDts = lastViewedDts;
	}

	public Date getToday()
	{
		return today;
	}

	public void setToday(Date today)
	{
		this.today = today;
	}

	public Date getSubmittedDts()
	{
		return submittedDts;
	}

	public void setSubmittedDts(Date submittedDts)
	{
		this.submittedDts = submittedDts;
	}

	public String getNotifyOfApprovalEmail()
	{
		return notifyOfApprovalEmail;
	}

	public void setNotifyOfApprovalEmail(String notifyOfApprovalEmail)
	{
		this.notifyOfApprovalEmail = notifyOfApprovalEmail;
	}

	public List<ComponentRelationshipView> getRelationships()
	{
		return relationships;
	}

	public void setRelationships(List<ComponentRelationshipView> relationships)
	{
		this.relationships = relationships;
	}

	public String getComponentType()
	{
		return componentType;
	}

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

	public String getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(String dataSource)
	{
		this.dataSource = dataSource;
	}

	public String getStorageVersion()
	{
		return storageVersion;
	}

	public void setStorageVersion(String storageVersion)
	{
		this.storageVersion = storageVersion;
	}

	public Integer getRecordVersion()
	{
		return recordVersion;
	}

	public void setRecordVersion(Integer recordVersion)
	{
		this.recordVersion = recordVersion;
	}

	public String getComponentTypeLabel()
	{
		return componentTypeLabel;
	}

	public void setComponentTypeLabel(String componentTypeLabel)
	{
		this.componentTypeLabel = componentTypeLabel;
	}

	public String getApprovalStateLabel()
	{
		return approvalStateLabel;
	}

	public void setApprovalStateLabel(String approvalStateLabel)
	{
		this.approvalStateLabel = approvalStateLabel;
	}

	public List<EvaluationAll> getFullEvaluations()
	{
		return fullEvaluations;
	}

	public void setFullEvaluations(List<EvaluationAll> fullEvaluations)
	{
		this.fullEvaluations = fullEvaluations;
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

	@Override
	public String getDataSensitivity()
	{
		return getDataSensitivityList();
	}

	@Override
	public void setDataSensitivity(String dataSensitivity)
	{
		setDataSensitivityDescriptionMapFromDataSensitivityString(dataSensitivity);
	}

	@Override
	public String getDataSensitivityDescription()
	{
		return getDataSensitivityDescriptionMapAsString();
	}

	@Override
	public void setDataSensitivityDescription(String dataSensitivityDescription)
	{
		setDataSensitivityDescriptionMapFromString(dataSensitivityDescription);
	}

	public ComponentTypeNestedModel getComponentTypeNestedModel()
	{
		return componentTypeNestedModel;
	}

	public void setComponentTypeNestedModel(ComponentTypeNestedModel componentTypeNestedModel)
	{
		this.componentTypeNestedModel = componentTypeNestedModel;
	}

	public ComponentType getComponentTypeFull()
	{
		return componentTypeFull;
	}

	public void setComponentTypeFull(ComponentType componentTypeFull)
	{
		this.componentTypeFull = componentTypeFull;
	}

	public String getComponentTemplateId()
	{
		return componentTemplateId;
	}

	public void setComponentTemplateId(String componentTemplateId)
	{
		this.componentTemplateId = componentTemplateId;
	}
}
