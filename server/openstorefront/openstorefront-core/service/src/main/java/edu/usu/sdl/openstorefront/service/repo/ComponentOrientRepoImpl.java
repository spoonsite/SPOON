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
import edu.usu.sdl.openstorefront.core.api.repo.ComponentRepo;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.filter.ComponentSensitivityModel;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.statistic.ComponentRecordStatistic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ComponentOrientRepoImpl
		extends BaseRepo
		implements ComponentRepo
{

	@Override
	public Map<String, List<Component>> getComponentByOrganization(Set<String> componentIds)
	{

		Map<String, Object> params = new HashMap<>();
		String componentFilter = "";
		if (componentIds != null && !componentIds.isEmpty()) {
			params = new HashMap<>();
			params.put("idlistParam", componentIds);
			componentFilter = " and componentId in :idlistParam";
		}

		String restrictionQuery = filterEngine.queryComponentRestriction();

		List<ODocument> documents = service.getPersistenceService().query("Select organization, name, name.toLowerCase() as sortname, securityMarkingType, lastActivityDts, approvalState from " + Component.class.getSimpleName()
				+ " where approvalState='" + ApprovalStatus.APPROVED + "' and "
				+ (StringUtils.isNotBlank(restrictionQuery) ? restrictionQuery + " and " : "")
				+ " activeStatus= '" + Component.ACTIVE_STATUS + "' " + componentFilter + " order by sortname", params);

		//group by org
		Map<String, List<Component>> orgMap = new HashMap<>();

		documents.forEach(
				document -> {
					String org = document.field("organization");
					if (StringUtils.isBlank(org)) {
						org = "No Organization Specified";
					}
					if (orgMap.containsKey(org)) {
						orgMap.get(org).add(populateOrganizationComponent(document));
					} else {

						List<Component> records = new ArrayList<>();
						records.add(populateOrganizationComponent(document));
						orgMap.put(org, records);
					}
				});

		return orgMap;
	}

	private Component populateOrganizationComponent(ODocument document)
	{
		Component component = new Component();
		component.setName(document.field("name"));
		component.setLastActivityDts(document.field("lastActivityDts"));
		component.setSecurityMarkingType(document.field("securityMarkingType"));
		component.setApprovalState(document.field("approvalState"));
		return component;
	}

	@Override
	public Map<String, Integer> findAverageUserRatingForComponents()
	{
		Map<String, Integer> componentRatingsMap = new HashMap<>();

		String query = "select componentId, avg(rating) as rating from " + ComponentReview.class.getSimpleName() + " group by componentId ";
		List<ODocument> resultsRatings = service.getPersistenceService().query(query, new HashMap<>());
		resultsRatings.forEach(document -> {
			componentRatingsMap.put(document.field("componentId"), document.field("rating"));
		});

		return componentRatingsMap;
	}

	@Override
	public Map<String, ComponentSearchView> getIntermidateSearchResults(Set<String> componentIds)
	{
		String dataFilterRestriction = getFilterEngine().queryComponentRestriction();
		if (StringUtils.isNotBlank(dataFilterRestriction)) {
			dataFilterRestriction += " and ";
		}

		String query = "select componentId, componentType, name, lastUpdateDts, activeStatus, approvalState from "
				+ Component.class.getSimpleName()
				+ " where "
				+ dataFilterRestriction
				+ " componentId in :idList";

		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("idList", componentIds);
		List<ODocument> results = service.getPersistenceService().query(query, parameterMap);

		Map<String, ComponentSearchView> resultMap = new HashMap<>();
		for (ODocument doc : results) {
			if (Component.ACTIVE_STATUS.equals(doc.field("activeStatus"))
					&& ApprovalStatus.APPROVED.equals(doc.field("approvalState"))) {
				ComponentSearchView view = new ComponentSearchView();
				view.setComponentId(doc.field("componentId"));
				view.setName(doc.field("name"));
				view.setComponentType(doc.field("componentType"));
				view.setLastActivityDts(doc.field("lastUpdateDts"));
				resultMap.put(view.getComponentId(), view);
			}
		}
		return resultMap;
	}

	@Override
	public Map<Integer, List<String>> getAverageRatingForComponents(int maxRating, SearchOperation.NumberOperation numberOperation)
	{
		Map<Integer, List<String>> ratingMap = new HashMap<>();

		String query = "select componentId, avg(rating) as rating from " + ComponentReview.class.getSimpleName() + " where activeStatus='" + ComponentReview.ACTIVE_STATUS + "' group by componentId ";

		List<ODocument> queryResults = service.getPersistenceService().query(query, new HashMap<>());

		for (ODocument oDocument : queryResults) {
			Integer value = oDocument.field("rating");
			String componentId = oDocument.field("componentId");
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
	public List<ComponentTracking> searchComponentTractor(FilterQueryParams filter, String componentId)
	{
		List<String> componentIdInResults = new ArrayList<>();
		if (StringUtils.isNotBlank(filter.getComponentName())) {
			String query = "select componentId from " + Component.class.getSimpleName() + " where name.toLowerCase() like :componentName ";
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("componentName", "%" + filter.getComponentName().toLowerCase().trim() + "%");

			List<ODocument> documents = service.getPersistenceService().query(query, parameterMap);
			for (ODocument document : documents) {
				componentIdInResults.add(document.field("componentId"));
			}
		}

		Map<String, Object> parameterMap = new HashMap<>();
		StringBuilder primaryQuery = new StringBuilder();
		primaryQuery.append("select from ").append(ComponentTracking.class.getSimpleName()).append(" where ");
		primaryQuery.append(" activeStatus = :activeStatus ");

		parameterMap.put("activeStatus", filter.getStatus());

		if (StringUtils.isNotBlank(componentId)) {
			primaryQuery.append(" and componentId = :componentId ");
			parameterMap.put("componentId", componentId);
		}

		if (filter.getStartDts() != null && filter.getStartDts().getDate() != null) {
			primaryQuery.append(" and eventDts >= :startDts ");
			parameterMap.put("startDts", filter.getStartDts().getDate());
		}

		if (filter.getEndDts() != null && filter.getEndDts().getDate() != null) {
			primaryQuery.append(" and eventDts <= :endDts ");
			parameterMap.put("endDts", filter.getEndDts().getDate());
		}

		if (StringUtils.isNotBlank(filter.getName())) {
			primaryQuery.append(" and updateUser.toLowerCase() like :nameSearch ");
			parameterMap.put("nameSearch", "%" + filter.getName().toLowerCase().trim() + "%");
		}

		if (!componentIdInResults.isEmpty()) {
			primaryQuery.append(" and componentId IN :componentIdList ");
			parameterMap.put("componentIdList", componentIdInResults);
		}

		if (StringUtils.isNotBlank(filter.getSortField()) && StringUtils.isNotBlank(filter.getSortOrder())) {
			primaryQuery.append(" ORDER BY ");
			primaryQuery.append(filter.getSortField());
			primaryQuery.append(" ");
			primaryQuery.append(filter.getSortOrder());
		}

		List<ComponentTracking> componentTrackings = service.getPersistenceService().query(primaryQuery.toString(), parameterMap);
		return componentTrackings;
	}

	@Override
	public Map<String, ComponentSensitivityModel> findComponentsWithDataRestrictions()
	{
		Map<String, ComponentSensitivityModel> componentRestrictionMap = new HashMap<>();

		String query = "select componentId, dataSource, dataSensitivity from " + Component.class.getSimpleName() + " where dataSource IS NOT NULL OR dataSensitivity IS NOT NULL";
		Map<String, Object> parameters = new HashMap<>();

		List<ODocument> documents = service.getPersistenceService().query(query, parameters);
		for (ODocument document : documents) {
			ComponentSensitivityModel cacheSensitivityModel = new ComponentSensitivityModel();
			cacheSensitivityModel.setComponentId(document.field("componentId"));
			cacheSensitivityModel.setDataSensitivity(document.field("dataSensitivity"));
			cacheSensitivityModel.setDataSource(document.field("dataSource"));

			componentRestrictionMap.put(document.field("componentId"), cacheSensitivityModel);
		}

		return componentRestrictionMap;
	}

	@Override
	public Map<String, ComponentSensitivityModel> findComponentsWithNoDataRestrictions()
	{
		Map<String, ComponentSensitivityModel> componentRestrictionMap = new HashMap<>();

		String query = "select componentId from " + Component.class.getSimpleName() + " where dataSource IS NULL AND dataSensitivity IS NULL";
		List<ODocument> documents = service.getPersistenceService().query(query, null);
		for (ODocument document : documents) {
			String foundId = document.field("componentId");
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

		String limit = "";
		if (maxRecords != null) {
			limit = "LIMIT " + maxRecords;
		}

		String query = "select count(*) as views, componentId from " + ComponentTracking.class.getSimpleName() + " group by componentId order by views DESC " + limit;

		List<ODocument> documents = service.getPersistenceService().query(query, null);
		for (ODocument document : documents) {
			ComponentRecordStatistic componentRecordStatistic = new ComponentRecordStatistic();
			componentRecordStatistic.setComponentId(document.field("componentId"));
			componentRecordStatistic.setViews(document.field("views"));
			componentRecordStatistic.setComponentName(service.getComponentService().getComponentName(componentRecordStatistic.getComponentId()));
			recordStatistics.add(componentRecordStatistic);
		}

		return recordStatistics;
	}

	@Override
	public List<Component> findRecentlyAdded(int maxResults)
	{
		String dataFilterRestriction = filterEngine.queryComponentRestriction();
		if (StringUtils.isNotBlank(dataFilterRestriction)) {
			dataFilterRestriction = " and " + dataFilterRestriction;
		}

		String query = "select from Component where activeStatus = :activeStatusParam "
				+ " and approvalState = :approvedStateParam "
				+ dataFilterRestriction
				+ " order by approvedDts DESC LIMIT " + maxResults;

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("activeStatusParam", Component.ACTIVE_STATUS);
		parameters.put("approvedStateParam", ApprovalStatus.APPROVED);

		return service.getPersistenceService().query(query, parameters);
	}

	@Override
	public List<Component> searchComponentByName(String search, int maxResults)
	{
		Map<String, Object> params = new HashMap<>();
		search = "%" + search.toLowerCase() + "%";
		params.put("search", search);
		String query = "SELECT FROM " + Component.class.getSimpleName() + " WHERE name.toLowerCase() LIKE :search LIMIT " + maxResults;
		List<Component> components = service.getPersistenceService().query(query, params);
		return components;
	}

}
