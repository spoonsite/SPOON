/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.repo.api;

import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public interface StandardEntityRepo
{

	/**
	 * Finds record count for a set of users
	 *
	 * @param recordClass (Entity Class)
	 * @param userProfiles
	 * @param trackCodeType (only applicable when searching track records
	 * @return count
	 */
	public long getRecordCountsByUsers(Class recordClass, List<UserProfile> userProfiles, String trackCodeType);

}
