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

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.filter.ComponentSensitivityModel;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.ListingStats;
import edu.usu.sdl.openstorefront.core.view.statistic.ComponentRecordStatistic;
import edu.usu.sdl.openstorefront.service.repo.api.ComponentRepo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.bson.conversions.Bson;

/**
 *
 * @author dshurtleff
 */
public class ComponentMongoRepoImpl
		extends BaseMongoRepo
		implements ComponentRepo
{

	@Override
	public Map<String, Integer> findAverageUserRatingForComponents()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Map<String, List<Component>> getComponentByOrganization(Set<String> componentIds)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Map<String, ComponentSearchView> getIntermidateSearchResults(Set<String> componentIds)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Map<Integer, List<String>> getAverageRatingForComponents(int maxRating, SearchOperation.NumberOperation numberOperation)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<ComponentTracking> searchComponentTractor(FilterQueryParams filter, String componentId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Map<String, ComponentSensitivityModel> findComponentsWithDataRestrictions()
	{
		Map<String, ComponentSensitivityModel> componentRestrictionMap = new HashMap<>();

		MongoCollection<Component> collection = getQueryUtil().getCollectionForEntity(Component.class);
		Bson filter = Filters.or(
				Filters.ne(Component.FIELD_DATA_SOURCE, null),
				Filters.ne(Component.FIELD_DATA_SENSITIVITY, null)
		);
		//TODO: FINSIH

		return componentRestrictionMap;
	}

	@Override
	public Map<String, ComponentSensitivityModel> findComponentsWithNoDataRestrictions()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<ComponentRecordStatistic> findTopViewedComponents(Integer maxRecords)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<Component> findRecentlyAdded(int maxResults)
	{
		MongoCollection<Component> collection = getQueryUtil().getCollectionForEntity(Component.class);

		Bson filter = Filters.and(
				Filters.eq(Component.FIELD_ACTIVE_STATUS, Component.ACTIVE_STATUS),
				Filters.eq(Component.FIELD_APPROVAL_STATE, ApprovalStatus.APPROVED)
		);
		filter = Filters.and(filter, getQueryUtil().componentRestrictionFilter());

		BasicDBObject sort = new BasicDBObject();
		sort.append(Component.FIELD_APPROVED_DTS, MongoQueryUtil.MONGO_SORT_DESCENDING);

		FindIterable<Component> findIterable = collection.find(filter)
				.sort(filter)
				.limit(maxResults);

		return findIterable.into(new ArrayList<>());
	}

	@Override
	public List<Component> searchComponentByName(String search, int maxResults)
	{
		MongoCollection<Component> collection = getQueryUtil().getCollectionForEntity(Component.class);

		FindIterable<Component> findIterable = collection.find(
				Filters.regex(
						Component.FIELD_NAME,
						Pattern.compile(getQueryUtil().convertSQLLikeCharacterToRegex(search), Pattern.CASE_INSENSITIVE)
				)
		).limit(maxResults);

		return findIterable.into(new ArrayList<>());
	}

	@Override
	public ListingStats getComponentListingStats()
	{
		ListingStats listingStats = new ListingStats();

		MongoCollection<Component> collection = getQueryUtil().getCollectionForEntity(Component.class);

		Bson filter = Filters.and(
				Filters.eq(Component.FIELD_ACTIVE_STATUS, Component.ACTIVE_STATUS),
				Filters.eq(Component.FIELD_APPROVAL_STATE, ApprovalStatus.APPROVED)
		);
		filter = Filters.and(filter, getQueryUtil().componentRestrictionFilter());
		long numberOfActiveComponents = collection.countDocuments(filter);
		listingStats.setNumberOfComponents(numberOfActiveComponents);

		return listingStats;
	}

}
