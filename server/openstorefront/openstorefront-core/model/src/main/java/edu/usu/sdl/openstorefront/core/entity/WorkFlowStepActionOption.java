/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.DefaultFieldValue;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@Embeddable
public class WorkFlowStepActionOption
		implements Serializable
{

	private static final long serialVersionUID = 1L;

	@DataType(EmailAddress.class)
	@ConsumeField
	@Embedded
	@OneToMany(orphanRemoval = true)
	private List<EmailAddress> fixedEmails;

	@ConsumeField
	private String emailGroup;

	@ConsumeField
	@DefaultFieldValue("false")
	private Boolean emailEntryTypeGroup;

	@ConsumeField
	@DefaultFieldValue("false")
	private Boolean emailOwner;

	@ConsumeField
	@DefaultFieldValue("false")
	private Boolean emailAssignee;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	private String emailMessage;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	private String emailSubject;

	@ConsumeField
	private String assignUser;

	@ConsumeField
	private String assignGroup;

	@ConsumeField
	private Boolean unassignGroup;
	
	@Version
	private String storageVersion;

	public WorkFlowStepActionOption()
	{
	}

	public List<EmailAddress> getFixedEmails()
	{
		return fixedEmails;
	}

	public void setFixedEmails(List<EmailAddress> fixedEmails)
	{
		this.fixedEmails = fixedEmails;
	}

	public String getEmailGroup()
	{
		return emailGroup;
	}

	public void setEmailGroup(String emailGroup)
	{
		this.emailGroup = emailGroup;
	}

	public Boolean getEmailEntryTypeGroup()
	{
		return emailEntryTypeGroup;
	}

	public void setEmailEntryTypeGroup(Boolean emailEntryTypeGroup)
	{
		this.emailEntryTypeGroup = emailEntryTypeGroup;
	}

	public Boolean getEmailOwner()
	{
		return emailOwner;
	}

	public void setEmailOwner(Boolean emailOwner)
	{
		this.emailOwner = emailOwner;
	}

	public String getEmailMessage()
	{
		return emailMessage;
	}

	public void setEmailMessage(String emailMessage)
	{
		this.emailMessage = emailMessage;
	}

	public String getAssignUser()
	{
		return assignUser;
	}

	public void setAssignUser(String assignUser)
	{
		this.assignUser = assignUser;
	}

	public String getAssignGroup()
	{
		return assignGroup;
	}

	public void setAssignGroup(String assignGroup)
	{
		this.assignGroup = assignGroup;
	}

	public Boolean getUnassignGroup()
	{
		return unassignGroup;
	}

	public void setUnassignGroup(Boolean unassignGroup)
	{
		this.unassignGroup = unassignGroup;
	}

	public Boolean getEmailAssignee()
	{
		return emailAssignee;
	}

	public void setEmailAssignee(Boolean emailAssignee)
	{
		this.emailAssignee = emailAssignee;
	}

	public String getEmailSubject()
	{
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject)
	{
		this.emailSubject = emailSubject;
	}

	public String getStorageVersion()
	{
		return storageVersion;
	}

	public void setStorageVersion(String storageVersion)
	{
		this.storageVersion = storageVersion;
	}
}
