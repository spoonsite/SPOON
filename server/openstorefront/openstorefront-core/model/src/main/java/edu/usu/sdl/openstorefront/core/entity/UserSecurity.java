/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import java.util.Date;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Security account; used in the builtin security realm. This shouldn't passed back to the user.")
public class UserSecurity
		extends StandardEntity<UserSecurity>
{

	@SuppressWarnings("squid:S2068")
	public static final String PASSWORD_FIELD = "password";
	public static final String FIELD_USERNAME = "username";

	@PK
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	private String username;

	@NotNull
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@APIDescription("Only Applicatble when using internal security; This is hashed")
	private String password;

	@NotNull
	@Min(0)
	@APIDescription("This will reset upon successful login")
	private Integer failedLoginAttempts;

	private Date lastLoginAttempt;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	private String tempPassword;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	private String passwordChangeApprovalCode;

	@NotNull
	private Date passwordUpdateDts;

	@APIDescription("This is used to flag the built in admin account; on first creation")
	private Boolean usingDefaultPassword;

	@NotNull
	@ValidValueType(value = {}, lookupClass = UserApprovalStatus.class)
	@ConsumeField
	@APIDescription("Status of an approval")
	@FK(UserApprovalStatus.class)
	private String approvalStatus;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public UserSecurity()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		UserSecurity userSecurity = (UserSecurity) entity;
		this.setFailedLoginAttempts(userSecurity.getFailedLoginAttempts());
		this.setLastLoginAttempt(userSecurity.getLastLoginAttempt());
		this.setApprovalStatus(userSecurity.getApprovalStatus());
		this.setPasswordUpdateDts(userSecurity.getPasswordUpdateDts());
		this.setUsingDefaultPassword(userSecurity.getUsingDefaultPassword());

	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Integer getFailedLoginAttempts()
	{
		return failedLoginAttempts;
	}

	public void setFailedLoginAttempts(Integer failedLoginAttempts)
	{
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public String getTempPassword()
	{
		return tempPassword;
	}

	public void setTempPassword(String tempPassword)
	{
		this.tempPassword = tempPassword;
	}

	public String getPasswordChangeApprovalCode()
	{
		return passwordChangeApprovalCode;
	}

	public void setPasswordChangeApprovalCode(String passwordChangeApprovalCode)
	{
		this.passwordChangeApprovalCode = passwordChangeApprovalCode;
	}

	public String getApprovalStatus()
	{
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus)
	{
		this.approvalStatus = approvalStatus;
	}

	public Date getLastLoginAttempt()
	{
		return lastLoginAttempt;
	}

	public void setLastLoginAttempt(Date lastLoginAttempt)
	{
		this.lastLoginAttempt = lastLoginAttempt;
	}

	public Boolean getUsingDefaultPassword()
	{
		return usingDefaultPassword;
	}

	public void setUsingDefaultPassword(Boolean usingDefaultPassword)
	{
		this.usingDefaultPassword = usingDefaultPassword;
	}

	public Date getPasswordUpdateDts()
	{
		return passwordUpdateDts;
	}

	public void setPasswordUpdateDts(Date passwordUpdateDts)
	{
		this.passwordUpdateDts = passwordUpdateDts;
	}

}
