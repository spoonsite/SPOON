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

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.storage.model.UserProfile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class UserProfileView
{

	@NotNull
	private String username;

	@ConsumeField
	private String firstName;

	@ConsumeField
	private String lastName;

	@ConsumeField
	private String email;

	@ConsumeField
	private String organization;

	@NotNull
	@ConsumeField
	private String userTypeCode;

	@NotNull
	private Date createDts;

	@NotNull
	private Date updateDts;

	@NotNull
	private String updateUser;
	private String guid;
	private Boolean notifyOfNew;

	private String activeStatus;

	@NotNull
	@APIDescription("The application doesn't store this information it's passed in from the idam system hence this is only correct for the current logged in user.")
	private boolean admin;
	private Date lastLoginDts;

	public UserProfileView()
	{
	}

	public static UserProfileView toView(UserProfile profile)
	{
		return toView(profile, false);
	}

	public static UserProfileView toView(UserProfile profile, Boolean includeLogin)
	{
		Map<String, Date> loginMap = null;
		if (includeLogin) {
			List<UserProfile> temp = new ArrayList<>();
			temp.add(profile);
			loginMap = ServiceProxy.getProxy().getUserService().getLastLogin(temp);
		}
		return toView(profile, loginMap);
	}

	private static UserProfileView toView(UserProfile profile, Map<String, Date> loginMap)
	{
		UserProfileView view = new UserProfileView();
		view.setEmail(profile.getEmail());
		view.setFirstName(profile.getFirstName());
		view.setLastName(profile.getLastName());
		view.setOrganization(profile.getOrganization());
		view.setUserTypeCode(profile.getUserTypeCode());
		view.setUsername(profile.getUsername());
		view.setCreateDts(profile.getCreateDts());
		view.setUpdateDts(profile.getUpdateDts());
		view.setUpdateUser(profile.getUpdateUser());
		view.setNotifyOfNew(profile.getNotifyOfNew());
		view.setActiveStatus(profile.getActiveStatus());

		if (StringUtils.isNotBlank(profile.getExternalGuid())) {
			view.setGuid(profile.getExternalGuid());
		} else {
			view.setGuid(profile.getInternalGuid());
		}
		if (loginMap != null && !loginMap.isEmpty()) {
			Date loginDate = loginMap.get(view.getUsername());
			view.setLastLoginDts(loginDate);
		}
		return view;
	}

	public static List<UserProfileView> toViewList(List<UserProfile> profiles)
	{
		Map<String, Date> loginMap = ServiceProxy.getProxy().getUserService().getLastLogin(profiles);
		List<UserProfileView> views = new ArrayList<>();
		profiles.forEach(profile -> {
			views.add(UserProfileView.toView(profile, loginMap));
		});

		return views;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getUserTypeCode()
	{
		return userTypeCode;
	}

	public void setUserTypeCode(String userTypeCode)
	{
		this.userTypeCode = userTypeCode;
	}

	public Date getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public String getUpdateUser()
	{
		return updateUser;
	}

	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}

	public boolean getAdmin()
	{
		return admin;
	}

	public void setAdmin(boolean admin)
	{
		this.admin = admin;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public String getGuid()
	{
		return guid;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	public Boolean getNotifyOfNew()
	{
		return notifyOfNew;
	}

	public void setNotifyOfNew(Boolean notifyOfNew)
	{
		this.notifyOfNew = notifyOfNew;
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

	public Date getLastLoginDts()
	{
		return lastLoginDts;
	}

	public void setLastLoginDts(Date lastLoginDts)
	{
		this.lastLoginDts = lastLoginDts;
	}

}
