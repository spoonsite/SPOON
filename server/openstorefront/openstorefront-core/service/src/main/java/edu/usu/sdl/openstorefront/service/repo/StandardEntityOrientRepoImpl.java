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
package edu.usu.sdl.openstorefront.service.repo;

import com.orientechnologies.orient.core.record.impl.ODocument;
import edu.usu.sdl.openstorefront.core.api.repo.StandardEntityRepo;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jstrong
 */
public class StandardEntityOrientRepoImpl
		extends BaseRepo
		implements StandardEntityRepo
{

	@Override
	public long getRecordCountsByUsers(Class recordClass, List<UserProfile> userProfiles, String trackCodeType)
	{
		long count = 0;

		List<String> userIds = new ArrayList<>();
		for (UserProfile userProfile : userProfiles) {
			userIds.add(userProfile.getUsername());
		}

		if (userIds.isEmpty() == false) {
			StringBuilder query = new StringBuilder();
			query.append("select count(*) from ")
					.append(recordClass.getSimpleName())
					.append(" where ").append(" createUser IN :createUserListParam ");

			if (StringUtils.isNotBlank(trackCodeType)) {
				query.append(" AND trackEventTypeCode = :eventCodeParam ");
			}

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("createUserListParam", userIds);
			paramMap.put("eventCodeParam", trackCodeType);

			List<ODocument> documents = service.getPersistenceService().query(query.toString(), paramMap);
			if (documents.isEmpty() == false) {
				count = documents.get(0).field("count");
			}
		}

		return count;
	}

}
