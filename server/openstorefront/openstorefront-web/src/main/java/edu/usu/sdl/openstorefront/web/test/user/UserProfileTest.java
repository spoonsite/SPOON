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
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;

/**
 *
 * @author dshurtleff
 */
public class UserProfileTest
		extends BaseTestCase
{

	@Override
	protected void runInternalTest()
	{
		results.append("Getting profile").append("<br>");
		UserProfile userProfile = getTestUserProfile();

		results.append("Re-lookup profile").append("<br>");
		UserProfile userProfileFound = service.getUserService().getUserProfile(TEST_USER);
		if (userProfileFound == null || TEST_USER.equals(userProfile.getUsername()) == false) {
			failureReason.append("Unable to find user just saved");
		} else {
			results.append("User profile: ").append(userProfile.getFirstName()).append(" " + userProfile.getLastName()).append("<br>");
		}
	}

	@Override
	public String getDescription()
	{
		return "User Profile";
	}

}
