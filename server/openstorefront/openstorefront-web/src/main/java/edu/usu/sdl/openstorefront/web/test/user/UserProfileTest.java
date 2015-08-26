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
package edu.usu.sdl.openstorefront.web.test.user;

import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;

/**
 *
 * @author dshurtleff
 */
public class UserProfileTest
		extends BaseTestCase
{

	public UserProfileTest()
	{
		this.description = "User Profile";
	}

	@Override
	protected void runInternalTest()
	{
		results.append("Saving profile").append("<br>");
		UserProfile userProfile = new UserProfile();
		userProfile.setUsername(TEST_USER);
		userProfile.setUserTypeCode(UserTypeCode.END_USER);
		userProfile.setFirstName("Test");
		userProfile.setLastName("Last");
		userProfile.setEmail("Email@test.com");
		userProfile.setOrganization("Test");
		userProfile.setExternalGuid("5555-5555");
		userProfile.setCreateUser(TEST_USER);
		userProfile.setUpdateUser(TEST_USER);
		service.getUserService().saveUserProfile(userProfile, false);

		results.append("Get profile").append("<br>");
		UserProfile userProfileFound = service.getUserService().getUserProfile(TEST_USER);
		if (userProfileFound == null || "Test".equals(userProfile.getFirstName()) == false) {
			failureReason.append("Unable to find user just saved");
		} else {
			results.append("User profile: ").append(userProfile.getFirstName()).append(userProfile.getLastName()).append("<br>");;
		}

		results.append("Removing profile").append("<br>");
		service.getUserService().deleteProfile(TEST_USER);
	}

}
