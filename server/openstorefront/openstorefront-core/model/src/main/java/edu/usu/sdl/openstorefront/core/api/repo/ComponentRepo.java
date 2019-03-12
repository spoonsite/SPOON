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
package edu.usu.sdl.openstorefront.core.api.repo;

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.filter.ComponentSensitivityModel;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.statistic.ComponentRecordStatistic;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author dshurtleff
 */
public interface ComponentRepo
{

	/**
	 * Finds average rating for All Entries
	 *
	 * @return ComponentId, rating
	 */
	public Map<String, Integer> findAverageUserRatingForComponents();

	/**
	 * Groups components by Org (Only Active and Approved) Warning: Component
	 * may not by completely populated
	 *
	 * @param componentIds
	 * @return
	 */
	public Map<String, List<Component>> getComponentByOrganization(Set<String> componentIds);

	/**
	 * This pull components (may be partially populated)
	 *
	 * @param componentIds
	 * @return map grouped by ComponentId, View
	 */
	public Map<String, ComponentSearchView> getIntermidateSearchResults(Set<String> componentIds);

	/**
	 * Groups all active reviews but rating
	 *
	 * @param maxRating
	 * @param numberOperation
	 * @return Rating, List of ComponentIds
	 */
	public Map<Integer, List<String>> getAverageRatingForComponents(int maxRating, SearchOperation.NumberOperation numberOperation);

	/**
	 * Searches component tracking record based on filter
	 *
	 * @param filter
	 * @param componentId
	 * @return
	 */
	public List<ComponentTracking> searchComponentTractor(FilterQueryParams filter, String componentId);

	/**
	 * Find all components that have datasources or datasensitivity
	 *
	 * @return componentId, componentSensitivityModel
	 */
	public Map<String, ComponentSensitivityModel> findComponentsWithDataRestrictions();

	/**
	 * Find all components that have no data restriction
	 *
	 * @return componentId, componentSensitivityModel
	 */
	public Map<String, ComponentSensitivityModel> findComponentsWithNoDataRestrictions();

	/**
	 * Find the most view components
	 *
	 * @param maxRecords
	 * @return
	 */
	public List<ComponentRecordStatistic> findTopViewedComponents(Integer maxRecords);

	/**
	 * Finds recently Approved components
	 *
	 * @param maxResults
	 * @return
	 */
	public List<Component> findRecentlyAdded(int maxResults);

	/**
	 * find component names that contain the search name
	 *
	 * @param search
	 * @return
	 */
	public List<Component> searchComponentByName(String search, int maxResults);

}
