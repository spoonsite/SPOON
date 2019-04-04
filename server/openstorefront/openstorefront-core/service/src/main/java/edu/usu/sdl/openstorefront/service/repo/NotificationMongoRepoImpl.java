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

import com.mongodb.client.MongoCollection;
import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventReadStatus;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.service.repo.api.NotificationRepo;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dshurtleff
 */
public class NotificationMongoRepoImpl
		extends BaseMongoRepo
		implements NotificationRepo
{

	@Override
	public long getTotalNotificationsForUser(String username)
	{
		long totalCount = 0;

		MongoCollection<NotificationEvent> collection = getQueryUtil().getCollectionForEntity(NotificationEvent.class);

		return totalCount;
	}

	@Override
	public List<NotificationEvent> getNotificationsForUser(String username, FilterQueryParams queryParams)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<NotificationEventReadStatus> getReadNoficationsForUser(String username, Set<String> eventIds)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void deleteNotificationBeforeDate(Date archiveDts)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
