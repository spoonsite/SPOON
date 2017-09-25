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
package edu.usu.sdl.openstorefront.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DefaultFieldValue;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.Unique;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.model.FieldChangeModel;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ComponentUniqueHandler;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@APIDescription("This is the main listing item that represents a component, asset, topic landing page, etc.")
public class Component
		extends StandardEntity<Component>
		implements OrganizationModel, LoggableModel<Component>
{

	public static final String FIELD_NAME = "name";

	@PK(generated = true)
	@NotNull
	private String componentId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_COMPONENT_NAME)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	@Unique(ComponentUniqueHandler.class)
	private String name;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_64K)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String description;

	@NotNull
	@ConsumeField
	@DefaultFieldValue(ComponentType.COMPONENT)
	@APIDescription("Type of listing")
	@FK(value = ComponentType.class, enforce = true)
	private String componentType;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@ConsumeField
	@APIDescription("External system guid")
	private String guid;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@ConsumeField
	@APIDescription("External system id")
	private String externalId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_ORGANIZATION)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	@APIDescription("Component organization")
	@FK(value = Organization.class, referencedField = "name", softReference = true)
	private String organization;

	@ConsumeField
	@APIDescription("The component's release date")
	private Date releaseDate;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	@APIDescription("Version of the component")
	private String version;

	@NotNull
	@ValidValueType(value
			= {}, lookupClass = ApprovalStatus.class)
	@ConsumeField
	@APIDescription("Status of an approval")
	@FK(ApprovalStatus.class)
	private String approvalState;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	@APIDescription("Who approved the component")
	private String approvedUser;

	@APIDescription("When the component was approved for the site")
	private Date approvedDts;

	@ValidValueType(value
			= {}, lookupClass = ApprovalMode.class)
	@ConsumeField
	@APIDescription("Sets the approval method of changes")
	@FK(ApprovalMode.class)
	private String changeApprovalMode;

	@NotNull
	@APIDescription("Updated when any of the component's related data has changed.  Used for watches.")
	private Date lastActivityDts;

	@APIDescription("Indicates a user submission and when it was submitted")
	private Date submittedDts;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_EMAIL)
	@APIDescription("Email to notify of approval.")
	private String notifyOfApprovalEmail;

	@ConsumeField
	@ValidValueType(value
			= {}, lookupClass = DataSource.class)
	@FK(ApprovalStatus.class)
	private String dataSource;

	@ValidValueType(value
			= {}, lookupClass = ModificationType.class)
	@FK(ModificationType.class)
	private String lastModificationType;

	@FK(ModificationType.class)
	private String fileHistoryId;

	@APIDescription("Overall record version for the component")
	private Integer recordVersion;

	@APIDescription("Component Id of the pending change component.")
	private String pendingChangeId;

	public Component()
	{
	}

	@Override
	public int compareTo(Component o)
	{
		int value = super.compareTo(o);

		if (value == 0) {
			value = EntityUtil.compareConsumeFields(this, o);
		}
		if (value == 0) {
			value = ReflectionUtil.compareObjects(this.getApprovedUser(), o.getApprovedUser());
		}
		if (value == 0) {
			value = ReflectionUtil.compareObjects(this.getApprovedDts(), o.getApprovedDts());
		}

		return value;
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		Component component = (Component) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, component);

		super.updateFields(entity);

		this.setName(component.getName());
		if ((ApprovalStatus.PENDING.equals(this.getApprovalState()) || ApprovalStatus.NOT_SUBMITTED.equals(this.getApprovalState()))
				&& ApprovalStatus.APPROVED.equals(component.getApprovalState())) {
			component.setApprovedUser(SecurityUtil.getCurrentUserName());
			component.setApprovedDts(TimeUtil.currentDate());

			this.setApprovedUser(component.getApprovedUser());
			this.setApprovedDts(component.getApprovedDts());

		} else if (ApprovalStatus.APPROVED.equals(this.getApprovalState())
				&& (ApprovalStatus.PENDING.equals(component.getApprovalState())) || ApprovalStatus.NOT_SUBMITTED.equals(component.getApprovalState())) {
			this.setApprovedUser(null);
			this.setApprovedDts(null);
		}

		this.setApprovalState(component.getApprovalState());
		this.setDescription(component.getDescription());
		this.setGuid(component.getGuid());
		this.setLastActivityDts(TimeUtil.currentDate());
		this.setOrganization(component.getOrganization());
		this.setComponentType(component.getComponentType());
		this.setReleaseDate(component.getReleaseDate());
		this.setVersion(component.getVersion());
		this.setNotifyOfApprovalEmail(component.getNotifyOfApprovalEmail());
		this.setSubmittedDts(component.getSubmittedDts());
		this.setDataSource(component.getDataSource());
		this.setExternalId(component.getExternalId());
		this.setFileHistoryId(component.getFileHistoryId());
		this.setLastModificationType(component.getLastModificationType());
		this.setChangeApprovalMode(component.getChangeApprovalMode());
		this.setRecordVersion(component.getRecordVersion());

	}

	@Override
	public List<FieldChangeModel> findChanges(Component updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("fileHistoryId");
		excludeFields.add("recordVersion");
		excludeFields.add("pendingChangeId");
		excludeFields.add("lastModificationType");
		excludeFields.add("lastActivityDts");
		excludeFields.add("componentId");

		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		return getName();
	}

	@Override
	public void setChangeParent(ChangeLog changeLog)
	{
		//top-level
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

	public String getGuid()
	{
		return guid;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	@Override
	public String getOrganization()
	{
		return organization;
	}

	@Override
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

	public String getComponentType()
	{
		return componentType;
	}

	public void setComponentType(String componentType)
	{
		this.componentType = componentType;
	}

	public String getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(String dataSource)
	{
		this.dataSource = dataSource;
	}

	public String getLastModificationType()
	{
		return lastModificationType;
	}

	public void setLastModificationType(String lastModificationType)
	{
		this.lastModificationType = lastModificationType;
	}

	public String getFileHistoryId()
	{
		return fileHistoryId;
	}

	public void setFileHistoryId(String fileHistoryId)
	{
		this.fileHistoryId = fileHistoryId;
	}

	public String getExternalId()
	{
		return externalId;
	}

	public void setExternalId(String externalId)
	{
		this.externalId = externalId;
	}

	public String getChangeApprovalMode()
	{
		return changeApprovalMode;
	}

	public void setChangeApprovalMode(String changeApprovalMode)
	{
		this.changeApprovalMode = changeApprovalMode;
	}

	public Integer getRecordVersion()
	{
		return recordVersion;
	}

	public void setRecordVersion(Integer recordVersion)
	{
		this.recordVersion = recordVersion;
	}

	public String getPendingChangeId()
	{
		return pendingChangeId;
	}

	public void setPendingChangeId(String pendingChangeId)
	{
		this.pendingChangeId = pendingChangeId;
	}

}
