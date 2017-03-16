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
