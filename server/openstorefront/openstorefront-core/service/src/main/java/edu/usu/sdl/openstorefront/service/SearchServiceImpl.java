/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service;

import com.orientechnologies.orient.core.record.impl.ODocument;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.SearchService;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.SearchOptions;
import edu.usu.sdl.openstorefront.core.entity.SystemSearch;
import edu.usu.sdl.openstorefront.core.model.search.AdvanceSearchResult;
import edu.usu.sdl.openstorefront.core.model.search.ResultAttributeStat;
import edu.usu.sdl.openstorefront.core.model.search.ResultOrganizationStat;
import edu.usu.sdl.openstorefront.core.model.search.ResultTagStat;
import edu.usu.sdl.openstorefront.core.model.search.ResultTypeStat;
import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
import edu.usu.sdl.openstorefront.core.model.search.SearchModel;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation.MergeCondition;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation.SearchType;
import edu.usu.sdl.openstorefront.core.model.search.SearchSuggestion;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.sort.RelevanceComparator;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.SearchQuery;
import edu.usu.sdl.openstorefront.service.api.SearchServicePrivate;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.service.manager.SearchServerManager;
import edu.usu.sdl.openstorefront.service.search.ArchitectureSearchHandler;
import edu.usu.sdl.openstorefront.service.search.AttributeSearchHandler;
import edu.usu.sdl.openstorefront.service.search.AttributeSetSearchHandler;
import edu.usu.sdl.openstorefront.service.search.BaseSearchHandler;
import edu.usu.sdl.openstorefront.service.search.ComponentSearchHandler;
import edu.usu.sdl.openstorefront.service.search.ContactSearchHandler;
import edu.usu.sdl.openstorefront.service.search.EntryTypeSearchHandler;
import edu.usu.sdl.openstorefront.service.search.EvaluationScoreSearchHandler;
import edu.usu.sdl.openstorefront.service.search.IndexSearchHandler;
import edu.usu.sdl.openstorefront.service.search.IndexSearchResult;
import edu.usu.sdl.openstorefront.service.search.MetaDataSearchHandler;
import edu.usu.sdl.openstorefront.service.search.QuestionResponseSearchHandler;
import edu.usu.sdl.openstorefront.service.search.QuestionSearchHandler;
import edu.usu.sdl.openstorefront.service.search.ReviewProConSearchHandler;
import edu.usu.sdl.openstorefront.service.search.ReviewSearchHandler;
import edu.usu.sdl.openstorefront.service.search.SearchStatTable;
import edu.usu.sdl.openstorefront.service.search.TagSearchHandler;
import edu.usu.sdl.openstorefront.service.search.UserRatingSearchHandler;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;

/**
 * Handles Searching the data set and sync the indexes
 *
 * @author gbagley
 * @author dshurleff
 */
public class SearchServiceImpl
		extends ServiceProxy
		implements SearchService, SearchServicePrivate
{

	private static final Logger LOG = Logger.getLogger(SearchServiceImpl.class.getName());

	private static final String SPECIAL_ARCH_SEARCH_CODE = "0";
	private static final int MAX_SEARCH_DESCRIPTION = 500;

	@Override
	public List<ComponentSearchView> getAll()
	{
		ServiceProxy service = new ServiceProxy();
		List<ComponentSearchView> list = new ArrayList<>();
		List<ComponentSearchView> components = service.getComponentService().getComponents();
		list.addAll(components);
		return list;
	}
	
	@Override
	public SearchOptions getGlobalSearchOptions(){
		SearchOptions searchOptionsExample = new SearchOptions();
		searchOptionsExample.setGlobalFlag(Boolean.TRUE);
		searchOptionsExample.setActiveStatus(SearchOptions.ACTIVE_STATUS);
		SearchOptions searchOptions = searchOptionsExample.find();
		
		if(searchOptions == null){
			// Return the default.
			searchOptions = new SearchOptions();
			searchOptions.setCanUseDescriptionInSearch(Boolean.TRUE);
			searchOptions.setCanUseNameInSearch(Boolean.TRUE);
			searchOptions.setCanUseOrganizationsInSearch(Boolean.TRUE);
			searchOptions.setCanUseTagsInSearch(Boolean.TRUE);
			searchOptions.setCanUseAttributesInSearch(Boolean.TRUE);
		}		
		return searchOptions;		
	}

	public SearchOptions saveGlobalSearchOptions(SearchOptions searchOptions){
		
		OSFCacheManager.getSearchCache().removeAll();

		SearchOptions searchOptionsExample = new SearchOptions();
		searchOptionsExample.setGlobalFlag(Boolean.TRUE);                     
		SearchOptions existing = searchOptionsExample.findProxy();                   
		
		if (existing != null) {
						searchOptions.setActiveStatus(SearchOptions.ACTIVE_STATUS);
						searchOptions.setGlobalFlag(true);
						existing.updateFields(searchOptions);
						persistenceService.persist(existing);
		} else {
						searchOptions.setSearchOptionsId(persistenceService.generateId());
						searchOptions.setGlobalFlag(true);
						searchOptions.setDefaultSearchOptions();
						existing = persistenceService.persist(searchOptions);
		}
		return existing;
}


	@Override
	public ComponentSearchWrapper getSearchItems(SearchQuery query, FilterQueryParams filter)
	{
		return SearchServerManager.getInstance().getSearchServer().search(query, filter);
	}

	@Override
	public void indexComponents(List<Component> components)
	{
		if (!components.isEmpty()) {
			SearchServerManager.getInstance().getSearchServer().index(components);
			OSFCacheManager.getSearchCache().removeAll();
		}
	}

	@Override
	public List<ComponentSearchView> architectureSearch(AttributeCodePk pk, FilterQueryParams filter)
	{
		List<ComponentSearchView> views = new ArrayList<>();

		AttributeCode attributeCodeExample = new AttributeCode();
		AttributeCodePk attributeCodePkExample = new AttributeCodePk();
		attributeCodePkExample.setAttributeType(pk.getAttributeType());
		attributeCodeExample.setAttributeCodePk(attributeCodePkExample);
		attributeCodeExample.setArchitectureCode(pk.getAttributeCode());

		AttributeCode attributeCode = persistenceService.queryOneByExample(attributeCodeExample);
		if (attributeCode == null) {
			attributeCode = persistenceService.findById(AttributeCode.class, pk);
		}

		AttributeCode attributeExample = new AttributeCode();
		AttributeCodePk attributePkExample = new AttributeCodePk();
		attributePkExample.setAttributeType(pk.getAttributeType());
		attributeExample.setAttributeCodePk(attributePkExample);

		AttributeCode attributeCodeLikeExample = new AttributeCode();
		if (attributeCode != null && StringUtils.isNotBlank(attributeCode.getArchitectureCode())) {
			attributeCodeLikeExample.setArchitectureCode(attributeCode.getArchitectureCode() + "%");
		} else {
			AttributeCodePk attributePkLikeExample = new AttributeCodePk();
			attributePkLikeExample.setAttributeCode(pk.getAttributeCode() + "%");
			attributeCodeLikeExample.setAttributeCodePk(attributePkLikeExample);
		}

		QueryByExample queryByExample = new QueryByExample(attributeExample);

		//check for like skip
		if (SPECIAL_ARCH_SEARCH_CODE.equals(pk.getAttributeCode()) == false) {
			queryByExample.setLikeExample(attributeCodeLikeExample);
		}

		List<AttributeCode> attributeCodes = persistenceService.queryByExample(queryByExample);
		List<String> ids = new ArrayList();
		attributeCodes.forEach(code -> {
			ids.add(code.getAttributeCodePk().getAttributeCode());
		});

		if (ids.isEmpty() == false) {

			String componentAttributeQuery = "select from " + ComponentAttribute.class.getSimpleName() + " where componentAttributePk.attributeType = :attributeType and componentAttributePk.attributeCode IN :attributeCodeIdListParam";

			Map<String, Object> params = new HashMap<>();
			params.put("attributeType", pk.getAttributeType());
			params.put("attributeCodeIdListParam", ids);
			List<ComponentAttribute> componentAttributes = persistenceService.query(componentAttributeQuery, params);
			Set<String> uniqueComponents = new HashSet<>();
			componentAttributes.forEach(componentAttribute -> {
				uniqueComponents.add(componentAttribute.getComponentId());
			});

			views.addAll(getComponentService().getSearchComponentList(new ArrayList<>(uniqueComponents)));
		}

		return views;
	}

	@Override
	public void deleteById(String id)
	{
		SearchServerManager.getInstance().getSearchServer().deleteById(id);
		OSFCacheManager.getSearchCache().removeAll();
	}

	@Override
	public void deleteAll()
	{
		SearchServerManager.getInstance().getSearchServer().deleteAll();
		OSFCacheManager.getSearchCache().removeAll();
	}

	@Override
	public void saveAll()
	{
		SearchServerManager.getInstance().getSearchServer().saveAll();
		OSFCacheManager.getSearchCache().removeAll();
	}

	@Override
	public void resetIndexer()
	{
		SearchServerManager.getInstance().getSearchServer().resetIndexer();
		OSFCacheManager.getSearchCache().removeAll();
	}

	@Override
	public AdvanceSearchResult advanceSearch(SearchModel searchModel)
	{
		Objects.requireNonNull(searchModel, "Search Model Required");

		AdvanceSearchResult searchResult = new AdvanceSearchResult();

		//each user may get different results depending on security roles
		if (StringUtils.isNotBlank(searchModel.getUserSessionKey())) {
			Element element = OSFCacheManager.getSearchCache().get(searchModel.getUserSessionKey() + searchModel.searchKey());
			if (element != null) {
				searchResult = (AdvanceSearchResult) element.getObjectValue();
				return searchResult;
			}
		}

		//group
		Map<SearchType, List<SearchElement>> searchGroup = new HashMap<>();
		for (SearchElement searchElement : searchModel.getSearchElements()) {
			if (searchGroup.containsKey(searchElement.getSearchType())) {
				searchGroup.get(searchElement.getSearchType()).add(searchElement);
			} else {
				List<SearchElement> searchElements = new ArrayList<>();
				searchElements.add(searchElement);
				searchGroup.put(searchElement.getSearchType(), searchElements);
			}
		}

		List<SearchElement> indexSearches = new ArrayList<>();
		List<BaseSearchHandler> handlers = new ArrayList<>();
		for (SearchType searchType : searchGroup.keySet()) {
			List<SearchElement> searchElements = searchGroup.get(searchType);
			switch (searchType) {
				case ARCHITECTURE:
					handlers.add(new ArchitectureSearchHandler(searchElements));
					break;
				case ATTRIBUTE:
					handlers.add(new AttributeSearchHandler(searchElements));
					break;
				case ATTRIBUTESET:
					handlers.add(new AttributeSetSearchHandler(searchElements));
					break;
				case COMPONENT:
					handlers.add(new ComponentSearchHandler(searchElements));
					break;
				case CONTACT:
					handlers.add(new ContactSearchHandler(searchElements));
					break;
				case INDEX:
					indexSearches.addAll(searchElements);
					handlers.add(new IndexSearchHandler(searchElements));
					break;
				case METADATA:
					handlers.add(new MetaDataSearchHandler(searchElements));
					break;
				case REVIEW:
					handlers.add(new ReviewSearchHandler(searchElements));
					break;
				case TAG:
					handlers.add(new TagSearchHandler(searchElements));
					break;
				case USER_RATING:
					handlers.add(new UserRatingSearchHandler(searchElements));
					break;
				case EVALUTATION_SCORE:
					handlers.add(new EvaluationScoreSearchHandler(searchElements));
					break;
				case QUESTION:
					handlers.add(new QuestionSearchHandler(searchElements));
					break;
				case QUESTION_RESPONSE:
					handlers.add(new QuestionResponseSearchHandler(searchElements));
					break;
				case REVIEWCON:
				case REVIEWPRO:
					handlers.add(new ReviewProConSearchHandler(searchElements));
					break;
				case ENTRYTYPE:
					handlers.add(new EntryTypeSearchHandler(searchElements));
					break;
				default:
					throw new OpenStorefrontRuntimeException("No handler defined for Search Type: " + searchType, "Add support; programming error");
			}
		}

		//validate
		ValidationResult validationResultMain = new ValidationResult();
		for (BaseSearchHandler handler : handlers) {
			ValidationResult validationResult = handler.validate();
			validationResultMain.merge(validationResult);
		}

		if (validationResultMain.valid()) {
			//process groups and aggregate
			List<String> componentIds = new ArrayList<>();
			MergeCondition mergeCondition = SearchOperation.MergeCondition.OR;
			for (BaseSearchHandler handler : handlers) {
				List<String> foundIds = handler.processSearch();

				componentIds = mergeCondition.apply(componentIds, foundIds);

				//merge
				mergeCondition = handler.getNextMergeCondition();
			}
			Set<String> masterResults = new HashSet<>();
			masterResults.addAll(componentIds);

			//get intermediate Results
			if (!masterResults.isEmpty()) {
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
				parameterMap.put("idList", masterResults);
				List<ODocument> results = persistenceService.query(query, parameterMap);

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
				searchResult.setTotalNumber(resultMap.size());

				//get review average
				query = "select componentId, avg(rating) as rating from " + ComponentReview.class.getSimpleName() + " group by componentId ";
				List<ODocument> resultsRatings = persistenceService.query(query, new HashMap<>());
				for (ODocument doc : resultsRatings) {
					ComponentSearchView view = resultMap.get(doc.field("componentId").toString());
					if (view != null) {
						view.setAverageRating(doc.field("rating"));
					}
				}

				//gather stats
				Map<String, ResultTypeStat> stats = new HashMap<>();
				for (ComponentSearchView view : resultMap.values()) {
					if (stats.containsKey(view.getComponentType())) {
						ResultTypeStat stat = stats.get(view.getComponentType());
						stat.setCount(stat.getCount() + 1);
					} else {
						ResultTypeStat stat = new ResultTypeStat();
						stat.setComponentType(view.getComponentType());
						stat.setComponentTypeDescription(getComponentService().getComponentTypeParentsString(view.getComponentType(), true));
						stat.setCount(1);
						stats.put(view.getComponentType(), stat);
					}
				}
				searchResult.getMeta().getResultTypeStats().addAll(stats.values());
				SearchStatTable statTable = new SearchStatTable();
				List<ResultOrganizationStat> organizationStats = statTable.getOrganizationStats(componentIds);
				List<ResultTagStat> tagStats = statTable.getTagStats(componentIds);
				List<ResultAttributeStat> attributeStats = statTable.getAttributeStats(componentIds);

				searchResult.getMeta().getResultOrganizationStats().addAll(organizationStats);
				searchResult.getMeta().getResultTagStats().addAll(tagStats);
				searchResult.getMeta().getResultAttributeStats().addAll(attributeStats);
				
				List<ComponentSearchView> intermediateViews = new ArrayList<>(resultMap.values());

				//then sort/window
				if (StringUtils.isNotBlank(searchModel.getSortField())) {
					Collections.sort(intermediateViews, new BeanComparator<>(searchModel.getSortDirection(), searchModel.getSortField()));
				}

				List<String> idsToResolve = new ArrayList<>();
				if (indexSearches.isEmpty()) {
					if (searchModel.getStartOffset() < intermediateViews.size() && searchModel.getMax() > 0) {
						int count = 0;
						for (int i = searchModel.getStartOffset(); i < intermediateViews.size(); i++) {
							idsToResolve.add(intermediateViews.get(i).getComponentId());
							count++;
							if (count >= searchModel.getMax()) {
								break;
							}
						}
					}
				} else {
					for (ComponentSearchView view : intermediateViews) {
						idsToResolve.add(view.getComponentId());
					}
				}

				//resolve results
				List<ComponentSearchView> views = getComponentService().getSearchComponentList(idsToResolve);

				if (!indexSearches.isEmpty()) {
					//only the first one counts
					String indexQuery = indexSearches.get(0).getValue();
					SearchServerManager.getInstance().getSearchServer().updateSearchScore(indexQuery, views);
				}

				if (StringUtils.isNotBlank(searchModel.getSortField())) {
					Collections.sort(views, new BeanComparator<>(searchModel.getSortDirection(), searchModel.getSortField()));
				}

				//	Order by relevance then name
				if (StringUtils.isNotBlank(searchModel.getSortField()) && ComponentSearchView.FIELD_SEARCH_SCORE.equals(searchModel.getSortField())) {
					Collections.sort(views, new RelevanceComparator<>());
				}

				if (!indexSearches.isEmpty()) {
					views = windowData(views, searchModel.getStartOffset(), searchModel.getMax());
				}

				//trim descriptions to max length
				for (ComponentSearchView view : views) {
					String description = StringProcessor.stripHtml(view.getDescription());
					view.setDescription(StringProcessor.ellipseString(description, MAX_SEARCH_DESCRIPTION));
				}

				searchResult.getResults().addAll(views);
			}
		}
		searchResult.setValidationResult(validationResultMain);

		if (StringUtils.isNotBlank(searchModel.getUserSessionKey())) {
			Element element = new Element(searchModel.getUserSessionKey() + searchModel.searchKey(), searchResult);
			OSFCacheManager.getSearchCache().put(element);
		}
		return searchResult;
	}

	private List<ComponentSearchView> windowData(List<ComponentSearchView> data, int offset, int max)
	{
		List<ComponentSearchView> results = new ArrayList<>();

		//window
		if (offset < data.size() && max > 0) {
			int count = 0;
			for (int i = offset; i < data.size(); i++) {
				results.add(data.get(i));
				count++;
				if (count >= max) {
					break;
				}
			}
		}
		return results;
	}

	@Override
	public IndexSearchResult doIndexSearch(String query, FilterQueryParams filter)
	{
		return SearchServerManager.getInstance().getSearchServer().doIndexSearch(query, filter);
	}

	public IndexSearchResult doIndexSearch(String query, FilterQueryParams filter, String[] addtionalFieldsToReturn)
	{
		return SearchServerManager.getInstance().getSearchServer().doIndexSearch(query, filter, addtionalFieldsToReturn);
	}

	@Override
	public List<SearchSuggestion> searchSuggestions(String query, int maxResult, String componentType)
	{
		return SearchServerManager.getInstance().getSearchServer().searchSuggestions(query, maxResult, componentType);
	}

	@Override
	public SystemSearch saveSearch(SystemSearch systemSearch)
	{
		Objects.requireNonNull(systemSearch);

		SystemSearch existing = persistenceService.findById(SystemSearch.class, systemSearch.getSearchId());
		if (existing != null) {
			existing.updateFields(systemSearch);
			systemSearch = persistenceService.persist(existing);
		} else {
			if (StringUtil.isBlank(systemSearch.getSearchId())) {
				systemSearch.setSearchId(persistenceService.generateId());
			}
			systemSearch.populateBaseCreateFields();
			systemSearch = persistenceService.persist(systemSearch);
		}
		return systemSearch;
	}

	@Override
	public void inactivateSearch(String searchId)
	{
		toggleStatusOnSearch(searchId, SystemSearch.INACTIVE_STATUS);
	}

	private void toggleStatusOnSearch(String searchId, String newStatus)
	{
		Objects.requireNonNull(searchId);

		SystemSearch existing = persistenceService.findById(SystemSearch.class, searchId);
		if (existing != null) {
			existing.setActiveStatus(newStatus);
			existing.populateBaseUpdateFields();
			persistenceService.persist(existing);
		} else {
			throw new OpenStorefrontRuntimeException("Search not found", "Check Id: " + searchId);
		}
	}

	@Override
	public void activateSearch(String searchId)
	{
		toggleStatusOnSearch(searchId, SystemSearch.ACTIVE_STATUS);
	}

}
