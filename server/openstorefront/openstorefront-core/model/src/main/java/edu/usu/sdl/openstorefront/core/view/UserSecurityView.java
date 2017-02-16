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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserSecurity;
import java.util.Date;

/**
 *
 * @author dshurtleff
 */
public class UserSecurityView
{
	private String username;	
	private String firstname;	
	private String lastname;	
	private String email;	
	private String approvalStatus;	
	private String approvalStatusDescription;	
	private Date lastLoginAttempt;	
	private Integer failedLoginAttempts;	
	private boolean pendingUserPasswordReset;

	public UserSecurityView()
	{
	}
	
	public static UserSecurityView toView(UserSecurity userSecurity, UserProfile userProfile)
	{
		UserSecurityView view = new UserSecurityView();
		
		return view;
	}
	
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	public String getLastname()
	{
		return lastname;
	}

	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getApprovalStatus()
	{
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus)
	{
		this.approvalStatus = approvalStatus;
	}

	public String getApprovalStatusDescription()
	{
		return approvalStatusDescription;
	}

	public void setApprovalStatusDescription(String approvalStatusDescription)
	{
		this.approvalStatusDescription = approvalStatusDescription;
	}

	public Date getLastLoginAttempt()
	{
		return lastLoginAttempt;
	}

	public void setLastLoginAttempt(Date lastLoginAttempt)
	{
		this.lastLoginAttempt = lastLoginAttempt;
	}

	public Integer getFailedLoginAttempts()
	{
		return failedLoginAttempts;
	}

	public void setFailedLoginAttempts(Integer failedLoginAttempts)
	{
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public boolean isPendingUserPasswordReset()
	{
		return pendingUserPasswordReset;
	}

	public void setPendingUserPasswordReset(boolean pendingUserPasswordReset)
	{
		this.pendingUserPasswordReset = pendingUserPasswordReset;
	}
	
}
