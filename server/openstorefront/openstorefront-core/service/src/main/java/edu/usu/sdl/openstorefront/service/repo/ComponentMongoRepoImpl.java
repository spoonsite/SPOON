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
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.filter.ComponentSensitivityModel;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.ListingStats;
import edu.usu.sdl.openstorefront.core.view.statistic.ComponentRecordStatistic;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.repo.api.ComponentRepo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
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
		Map<String, Integer> componentRatingsMap = new HashMap<>();

		//query all active and handle grouping and average post
		//There data set is small here
		MongoCollection<ComponentReview> collection = getQueryUtil().getCollectionForEntity(ComponentReview.class);

		Bson filter = Filters.eq(ComponentReview.FIELD_ACTIVE_STATUS, ComponentReview.ACTIVE_STATUS);

		FindIterable<ComponentReview> reviews = collection.find(filter).projection(
				Projections.include(Component.FIELD_COMPONENT_ID, ComponentReview.FIELD_RATING)
		);

		Map<String, List<ComponentReview>> componentIdReviewGroup = new HashMap<>();
		for (ComponentReview review : reviews) {
			if (componentIdReviewGroup.containsKey(review.getComponentId())) {
				componentIdReviewGroup.get(review.getComponentId()).add(review);
			} else {
				List<ComponentReview> reviewList = new ArrayList<>();
				reviewList.add(review);
				componentIdReviewGroup.put(review.getComponentId(), reviewList);
			}
		}
		for (String componentId : componentIdReviewGroup.keySet()) {
			double averageValue = componentIdReviewGroup.get(componentId).stream()
					.collect(Collectors.averagingInt(r -> r.getRating()));

			Integer value = BigDecimal.valueOf(averageValue).intValue();

			componentRatingsMap.put(componentId, value);
		}

		return componentRatingsMap;
	}

	@Override
	public Map<String, List<Component>> getComponentByOrganization(Set<String> componentIds)
	{
		MongoCollection<Component> collection = getQueryUtil().getCollectionForEntity(Component.class);

		Bson filter = Filters.and(
				Filters.eq(Component.FIELD_ACTIVE_STATUS, Component.ACTIVE_STATUS),
				Filters.eq(Component.FIELD_APPROVAL_STATE, ApprovalStatus.APPROVED)
		);
		filter = Filters.and(filter, getQueryUtil().componentRestrictionFilter(SecurityUtil.getUserContext()));

		if (componentIds != null && !componentIds.isEmpty()) {
			filter = Filters.and(filter, Filters.in(Component.FIELD_COMPONENT_ID, componentIds));
		}

		FindIterable<Component> findIterable = collection.find(filter);

		//post sort (case in-sensitive sort)
		List<Component> components = new ArrayList<>();
		for (Component component : findIterable) {
			components.add(component);
		}
		components.sort((a, b) -> {
			return a.getName().toLowerCase().compareTo(b.getName().toLowerCase());
		});

		//group by org
		Map<String, List<Component>> orgMap = new HashMap<>();
		for (Component component : components) {
			String org = component.getOrganization();
			if (StringUtils.isBlank(org)) {
				org = "No Organization Specified";
			}
			if (orgMap.containsKey(org)) {
				orgMap.get(org).add(component);
			} else {

				List<Component> records = new ArrayList<>();
				records.add(component);
				orgMap.put(org, records);
			}
		}

		return orgMap;
	}

	@Override
	public Map<String, ComponentSearchView> getIntermidateSearchResults(Set<String> componentIds)
	{
		MongoCollection<Component> collection = getQueryUtil().getCollectionForEntity(Component.class);

		Bson filter = Filters.and(
				getQueryUtil().componentRestrictionFilter(SecurityUtil.getUserContext()),
				Filters.in(Component.FIELD_COMPONENT_ID, componentIds)
		);
		filter = Filters.and(filter, getQueryUtil().componentRestrictionFilter(SecurityUtil.getUserContext()));

		FindIterable<Component> findIterable = collection.find(filter);

		Map<String, ComponentSearchView> resultMap = new HashMap<>();
		for (Component component : findIterable) {
			if (Component.ACTIVE_STATUS.equals(component.getActiveStatus())
					&& ApprovalStatus.APPROVED.equals(component.getApprovalState())) {

				ComponentSearchView view = new ComponentSearchView();
				view.setComponentId(component.getComponentId());
				view.setName(component.getName());
				view.setComponentType(component.getComponentType());
				view.setLastActivityDts(component.getLastActivityDts());

				resultMap.put(view.getComponentId(), view);
			}

		}
		return resultMap;
	}

	@Override
	public Map<Integer, List<String>> getAverageRatingForComponents(int maxRating, SearchOperation.NumberOperation numberOperation)
	{
		Map<Integer, List<String>> ratingMap = new HashMap<>();

		//query all active and handle grouping and average post
		//There data set is small here
		MongoCollection<ComponentReview> collection = getQueryUtil().getCollectionForEntity(ComponentReview.class);

		Bson filter = Filters.eq(ComponentReview.FIELD_ACTIVE_STATUS, ComponentReview.ACTIVE_STATUS);

		FindIterable<ComponentReview> reviews = collection.find(filter).projection(
				Projections.include(Component.FIELD_COMPONENT_ID, ComponentReview.FIELD_RATING)
		);

		Map<String, List<ComponentReview>> componentIdReviewGroup = new HashMap<>();
		for (ComponentReview review : reviews) {
			if (componentIdReviewGroup.containsKey(review.getComponentId())) {
				componentIdReviewGroup.get(review.getComponentId()).add(review);
			} else {
				List<ComponentReview> reviewList = new ArrayList<>();
				reviewList.add(review);
				componentIdReviewGroup.put(review.getComponentId(), reviewList);
			}
		}
		for (String componentId : componentIdReviewGroup.keySet()) {
			double averageValue = componentIdReviewGroup.get(componentId).stream()
					.collect(Collectors.averagingInt(r -> r.getRating()));

			Integer value = BigDecimal.valueOf(averageValue).intValue();

			if (numberOperation.pass(value, maxRating)) {
				if (ratingMap.containsKey(value)) {
					ratingMap.get(value).add(componentId);
				} else {
					List<String> componentIds = new ArrayList<>();
					componentIds.add(componentId);
					ratingMap.put(value, componentIds);
				}
			}
		}

		return ratingMap;
	}

	@Override
	public List<ComponentTracking> searchComponentTracking(FilterQueryParams filter, String componentId)
	{
		MongoCollection<Component> collection = getQueryUtil().getCollectionForEntity(Component.class);

		List<String> componentIdInResults = new ArrayList<>();
		if (StringUtils.isNotBlank(filter.getComponentName())) {
			Bson queryFilter = Filters.regex(
					Component.FIELD_NAME,
					Pattern.compile(getQueryUtil().convertSQLLikeCharacterToRegex(filter.getComponentName().toLowerCase().trim()), Pattern.CASE_INSENSITIVE)
			);

			FindIterable<Component> foundComponents = collection.find(queryFilter);
			for (Component component : foundComponents) {
				componentIdInResults.add(component.getComponentId());
			}
		}
		MongoCollection<ComponentTracking> trackingCollection = getQueryUtil().getCollectionForEntity(ComponentTracking.class);
		Bson queryFilter = Filters.eq(ComponentTracking.FIELD_ACTIVE_STATUS, filter.getStatus());

		if (StringUtils.isNotBlank(componentId)) {
			queryFilter = Filters.and(
					queryFilter,
					Filters.eq(Component.FIELD_COMPONENT_ID, componentId)
			);
		}

		if (filter.getStartDts() != null && filter.getStartDts().getDate() != null) {
			queryFilter = Filters.and(
					queryFilter,
					Filters.gte(ComponentTracking.FIELD_EVENT_DTS, filter.getStartDts().getDate())
			);
		}

		if (filter.getEndDts() != null && filter.getEndDts().getDate() != null) {
			queryFilter = Filters.and(
					queryFilter,
					Filters.lte(ComponentTracking.FIELD_EVENT_DTS, filter.getEndDts().getDate())
			);
		}

		if (StringUtils.isNotBlank(filter.getName())) {
			queryFilter = Filters.and(
					queryFilter,
					Filters.regex(
							ComponentTracking.FIELD_UPDATE_USER,
							Pattern.compile(getQueryUtil().convertSQLLikeCharacterToRegex(filter.getName().toLowerCase().trim()), Pattern.CASE_INSENSITIVE)
					)
			);
		}

		if (!componentIdInResults.isEmpty()) {
			queryFilter = Filters.and(
					queryFilter,
					Filters.in(Component.FIELD_COMPONENT_ID, componentIdInResults)
			);
		}

		BasicDBObject sort = new BasicDBObject();
		sort.append(filter.getSortField(), MongoQueryUtil.MONGO_SORT_DESCENDING);

		FindIterable<ComponentTracking> findIterable = trackingCollection.find(queryFilter)
				.sort(sort);

		return findIterable.into(new ArrayList<>());
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
		FindIterable<Component> findIterable = collection.find(filter);

		for (Component component : findIterable) {
			ComponentSensitivityModel cacheSensitivityModel = new ComponentSensitivityModel();
			cacheSensitivityModel.setComponentId(component.getComponentId());
			cacheSensitivityModel.setDataSensitivity(component.getDataSensitivity());
			cacheSensitivityModel.setDataSource(component.getDataSource());

			componentRestrictionMap.put(component.getComponentId(), cacheSensitivityModel);
		}

		return componentRestrictionMap;
	}

	@Override
	public Map<String, ComponentSensitivityModel> findComponentsWithNoDataRestrictions()
	{
		Map<String, ComponentSensitivityModel> componentRestrictionMap = new HashMap<>();

		MongoCollection<Component> collection = getQueryUtil().getCollectionForEntity(Component.class);
		Bson filter = Filters.and(
				Filters.eq(Component.FIELD_DATA_SOURCE, null),
				Filters.eq(Component.FIELD_DATA_SENSITIVITY, null)
		);
		FindIterable<Component> findIterable = collection.find(filter);

		for (Component component : findIterable) {
			String foundId = component.getComponentId();
			ComponentSensitivityModel cacheSensitivityModel = new ComponentSensitivityModel();
			cacheSensitivityModel.setComponentId(foundId);
			componentRestrictionMap.put(foundId, cacheSensitivityModel);
		}

		return componentRestrictionMap;
	}

	@Override
	public List<ComponentRecordStatistic> findTopViewedComponents(Integer maxRecords)
	{
		List<ComponentRecordStatistic> recordStatistics = new ArrayList<>();

		MongoCollection<ComponentTracking> collection = getQueryUtil().getCollectionForEntity(ComponentTracking.class);

		BasicDBObject groupFields = new BasicDBObject("_id", "$" + Component.FIELD_COMPONENT_ID);

		// we use the $sum operator to increment the "count"
		// for each unique component ID
		groupFields.put("count", new BasicDBObject("$sum", 1));
		BasicDBObject group = new BasicDBObject("$group", groupFields);

		BasicDBObject sortFields = new BasicDBObject("count", MongoQueryUtil.MONGO_SORT_DESCENDING);
		BasicDBObject sort = new BasicDBObject("$sort", sortFields);

		List<Bson> pipeline = new ArrayList<>();
		pipeline.add(group);
		pipeline.add(sort);

		if (maxRecords != null) {
			pipeline.add(Aggregates.limit(maxRecords));
		}

		AggregateIterable<BasicDBObject> aggregateIterable = collection.aggregate(pipeline, BasicDBObject.class);

		for (BasicDBObject dBObject : aggregateIterable) {
			ComponentRecordStatistic componentRecordStatistic = new ComponentRecordStatistic();
			componentRecordStatistic.setComponentId(dBObject.getString("_id"));
			componentRecordStatistic.setViews(Integer.valueOf(dBObject.getString("count")));
			componentRecordStatistic.setComponentName(service.getComponentService().getComponentName(componentRecordStatistic.getComponentId()));
			recordStatistics.add(componentRecordStatistic);
		}

		return recordStatistics;
	}

	@Override
	public List<Component> findRecentlyAdded(int maxResults)
	{
		MongoCollection<Component> collection = getQueryUtil().getCollectionForEntity(Component.class);

		Bson filter = Filters.and(
				Filters.eq(Component.FIELD_ACTIVE_STATUS, Component.ACTIVE_STATUS),
				Filters.eq(Component.FIELD_APPROVAL_STATE, ApprovalStatus.APPROVED)
		);
		filter = Filters.and(filter, getQueryUtil().componentRestrictionFilter(SecurityUtil.getUserContext()));

		BasicDBObject sort = new BasicDBObject();
		sort.append(Component.FIELD_APPROVED_DTS, MongoQueryUtil.MONGO_SORT_DESCENDING);

		FindIterable<Component> findIterable = collection.find(filter)
				.sort(sort)
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
						Pattern.compile(getQueryUtil().convertSQLLikeCharacterToRegex(search.toLowerCase()), Pattern.CASE_INSENSITIVE)
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
		filter = Filters.and(filter, getQueryUtil().componentRestrictionFilter(SecurityUtil.getUserContext()));
		long numberOfActiveComponents = collection.countDocuments(filter);
		listingStats.setNumberOfComponents(numberOfActiveComponents);

		return listingStats;
	}

}
