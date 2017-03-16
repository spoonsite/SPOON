/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.security;

import edu.usu.sdl.openstorefront.core.api.ExternalUserManager;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import java.util.ArrayList;
import java.util.List;

/**
 * Use to test the user syncing job
 * @author dshurtleff
 */
public class MockUserManager
	extends ExternalUserManager
{

	@Override
	public UserRecord findUser(String username)
	{
		UserProfile userProfile = new UserProfile();
		userProfile.setUsername(username);
		userProfile = userProfile.find();

		return profileToRecord(userProfile);
	}

	@Override
	public List<UserRecord> findUsers(List<String> users)
	{
		UserProfile userProfileExample = new UserProfile();
		userProfileExample.setActiveStatus(UserProfile.ACTIVE_STATUS);		
		List<UserProfile> UserProfile = userProfileExample.findByExample();
		
		List<UserRecord> userRecords = new ArrayList<>();
		for (UserProfile userProfile : UserProfile) {
			UserRecord userRecord = profileToRecord(userProfile);
			if ("admin".equalsIgnoreCase(userRecord.getUsername())) {
				userRecord.setOrganization("CHANGED");				
				
			}
			userRecords.add(userRecord);
		}
		return userRecords;
	}
	
	private UserRecord profileToRecord(UserProfile userProfile) 
	{
		UserRecord userRecord = null;
		
		if (userProfile != null) {
			userRecord = new UserRecord();
			userRecord.setFirstName(userProfile.getFirstName());
			userRecord.setLastName(userProfile.getLastName());
			userRecord.setEmail(userProfile.getEmail());
			userRecord.setPhone(userProfile.getPhone());
			userRecord.setOrganization(userProfile.getOrganization());
			userRecord.setUsername(userProfile.getUsername());						
		}
		
		return userRecord;
	}
	
}
