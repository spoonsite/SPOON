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

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import edu.usu.sdl.openstorefront.core.entity.OrganizationModel;
import edu.usu.sdl.openstorefront.service.repo.api.OrganizationRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class OrganizationMongoRepoImpl
		extends BaseMongoRepo
		implements OrganizationRepo
{

	private static final Logger LOG = Logger.getLogger(OrganizationMongoRepoImpl.class.getName());

	@Override
	public <T> List<T> findReferencesNoOrg(T entity)
	{
		MongoCollection<T> collection = getQueryUtil().getCollectionForEntity(entity);
		FindIterable<T> findIterable = collection.find(Filters.eq(OrganizationModel.FIELD_ORGANIZATION, null));
		return findIterable.into(new ArrayList<>());
	}

	@Override
	public Organization handleOrganizationUpdate(PersistenceService persistenceService, Organization existing, Organization updateOrganization)
	{
		String existingOrganizationId = existing.getOrganizationId();

		existing.updateFields(updateOrganization);

		MongoCollection<Organization> collection = getQueryUtil().getCollectionForEntity(existing);

		UpdateResult updateResult = collection.replaceOne(
				Filters.eq(Organization.FIELD_ORGANIZATION_ID, existingOrganizationId),
				existing,
				ReplaceOptions.createReplaceOptions(new UpdateOptions().upsert(true)));

		if (LOG.isLoggable(Level.FINER)) {
			LOG.log(Level.FINER, ()
					-> "Updated Collection Organization "
					+ " Modification Count: " + updateResult.getModifiedCount()
					+ " Match Count: " + updateResult.getMatchedCount()
					+ " Mongo Id: " + updateResult.getUpsertedId());
		}

		return existing;
	}

}
