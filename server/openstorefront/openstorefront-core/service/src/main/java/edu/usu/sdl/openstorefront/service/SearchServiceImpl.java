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
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.SearchService;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.SystemSearch;
import edu.usu.sdl.openstorefront.core.model.search.AdvanceSearchResult;
import edu.usu.sdl.openstorefront.core.model.search.ResultTypeStat;
import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
import edu.usu.sdl.openstorefront.core.model.search.SearchModel;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation.MergeCondition;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation.SearchType;
import edu.usu.sdl.openstorefront.core.model.search.SearchSuggestion;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.SearchQuery;
import edu.usu.sdl.openstorefront.service.api.SearchServicePrivate;
import edu.usu.sdl.openstorefront.service.manager.SolrManager;
import edu.usu.sdl.openstorefront.service.manager.SolrManager.SolrAndOr;
import edu.usu.sdl.openstorefront.service.manager.SolrManager.SolrEquals;
import edu.usu.sdl.openstorefront.service.search.ArchitectureSearchHandler;
import edu.usu.sdl.openstorefront.service.search.AttributeSearchHandler;
import edu.usu.sdl.openstorefront.service.search.BaseSearchHandler;
import edu.usu.sdl.openstorefront.service.search.ComponentSearchHandler;
import edu.usu.sdl.openstorefront.service.search.ContactSearchHandler;
import edu.usu.sdl.openstorefront.service.search.EvaluationScoreSearchHandler;
import edu.usu.sdl.openstorefront.service.search.IndexSearchHandler;
import edu.usu.sdl.openstorefront.service.search.IndexSearchResult;
import edu.usu.sdl.openstorefront.service.search.MetaDataSearchHandler;
import edu.usu.sdl.openstorefront.service.search.QuestionResponseSearchHandler;
import edu.usu.sdl.openstorefront.service.search.QuestionSearchHandler;
import edu.usu.sdl.openstorefront.service.search.ReviewProConSeatchHandler;
import edu.usu.sdl.openstorefront.service.search.ReviewSearchHandler;
import edu.usu.sdl.openstorefront.service.search.SolrComponentModel;
import edu.usu.sdl.openstorefront.service.search.TagSearchHandler;
import edu.usu.sdl.openstorefront.service.search.UserRatingSearchHandler;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

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

	private static final Logger log = Logger.getLogger(SearchServiceImpl.class.getName());

	private static final String SPECIAL_ARCH_SEARCH_CODE = "0";

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
	public ComponentSearchWrapper getSearchItems(SearchQuery query, FilterQueryParams filter)
	{
		ComponentSearchWrapper componentSearchWrapper = new ComponentSearchWrapper();

		IndexSearchResult indexSearchResult = doIndexSearch(query.getQuery(), filter);
		List<SolrComponentModel> resultsList = indexSearchResult.getResultsList();
		long totalFound = indexSearchResult.getTotalResults();

		//Pulling the full object on the return
		List<ComponentSearchView> views = new ArrayList<>();

		List<String> componentIds = new ArrayList<>();
		for (SolrComponentModel result : resultsList) {
			if (result.getIsComponent()) {
				componentIds.add(result.getId());
			}
		}

		//remove bad indexes, if any
		List<ComponentSearchView> componentSearchViews = getComponentService().getSearchComponentList(componentIds);
		Set<String> goodComponentIdSet = new HashSet<>();
		for (ComponentSearchView view : componentSearchViews) {
			goodComponentIdSet.add(view.getComponentId());
		}

		for (String componentId : componentIds) {
			if (goodComponentIdSet.contains(componentId) == false) {
				log.log(Level.FINE, MessageFormat.format("Removing bad index: {0}", componentId));
				deleteById(componentId);
				totalFound--;
			}
		}
		views.addAll(componentSearchViews);

		//TODO: Get the score and sort by score
		componentSearchWrapper.setData(views);
		componentSearchWrapper.setResults(views.size());

		//This could happen if the index were all bad
		if (totalFound < 0) {
			totalFound = 0;
		}
		componentSearchWrapper.setTotalNumber(totalFound);
		return componentSearchWrapper;
	}

	@Override
	public void indexComponents(List<Component> components)
	{
		// initialize solr server
		SolrServer solrService = SolrManager.getServer();

		Map<String, List<ComponentAttribute>> attributeMap = new HashMap<>();
		Map<String, List<ComponentTag>> tagMap = new HashMap<>();

		if (components.size() > 1) {
			ComponentAttribute componentAttributeExample = new ComponentAttribute();
			componentAttributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
			List<ComponentAttribute> allAttributes = persistenceService.queryByExample(ComponentAttribute.class, componentAttributeExample);
			attributeMap = allAttributes.stream().collect(Collectors.groupingBy(ComponentAttribute::getComponentId));
			
			ComponentTag componentTagExample = new ComponentTag();
			componentTagExample.setActiveStatus(ComponentTag.ACTIVE_STATUS);
			List<ComponentTag> allTags = persistenceService.queryByExample(ComponentTag.class, componentTagExample);
			tagMap = allTags.stream().collect(Collectors.groupingBy(ComponentTag::getComponentId));
			
		}

		List<SolrComponentModel> solrDocs = new ArrayList<>();
		for (Component component : components) {

			//add document using the example schema
			SolrComponentModel solrDocModel = new SolrComponentModel();

			solrDocModel.setIsComponent(Boolean.TRUE);
			solrDocModel.setId(component.getComponentId());
			solrDocModel.setNameString(component.getName());
			solrDocModel.setName(component.getName());
			String description = StringProcessor.stripHtml(component.getDescription());
			solrDocModel.setDescription(description.replace("<>", "").replace("\n", ""));
			solrDocModel.setUpdateDts(component.getUpdateDts());
			solrDocModel.setOrganization(component.getOrganization());

			
			List<ComponentTag> tags;
			List<ComponentAttribute> attributes;
			if (components.size() > 1) {
				tags = tagMap.get(component.getComponentId());
				if (tags == null) {
					tags = new ArrayList<>();
				}
				attributes = attributeMap.get(component.getComponentId());
				if (attributes == null) {
					attributes = new ArrayList<>();
				}
			} else {
				tags = getComponentService().getBaseComponent(ComponentTag.class, component.getComponentId());
				attributes = getComponentService().getBaseComponent(ComponentAttribute.class, component.getComponentId());
			}

			StringBuilder tagList = new StringBuilder();
			StringBuilder attributeList = new StringBuilder();

			for (ComponentTag tag : tags) {
				tagList.append(tag.getText()).append(" ");
			}

			for (ComponentAttribute attribute : attributes) {
				ComponentAttributePk pk = attribute.getComponentAttributePk();
				attributeList.append(attributesToString(pk.getAttributeType(), pk.getAttributeCode()));
			}

			solrDocModel.setTags(tagList.toString());
			solrDocModel.setAttributes(attributeList.toString());
			solrDocModel.setArticleHtml("");
			solrDocs.add(solrDocModel);
		}

		if (solrDocs.isEmpty() == false) {
			try {
				solrService.addBeans(solrDocs);
				solrService.commit();
			} catch (IOException | SolrServerException ex) {
				throw new OpenStorefrontRuntimeException("Failed Adding Component", ex);
			}
		}
	}
	
	private String attributesToString(String typeKey, String codeKey)
	{
		StringBuilder attributeList = new StringBuilder();
		AttributeCodePk codePk = new AttributeCodePk();
		codePk.setAttributeCode(typeKey);
		codePk.setAttributeType(codeKey);
		AttributeCode code = getAttributeService().findCodeForType(codePk);
		AttributeType type = getAttributeService().findType(codePk.getAttributeType());
		attributeList.append(codePk.getAttributeCode()).append(" ");
		attributeList.append(codePk.getAttributeType()).append(" ");

		if (code != null && type != null) {
			attributeList.append(code.getLabel()).append(" ");
			if (StringUtils.isNotBlank(code.getDescription())) {
				attributeList.append(code.getDescription()).append(" ");
			}
			if (StringUtils.isNotBlank(type.getDescription())) {
				attributeList.append(type.getDescription()).append(" ");
			}
		}
		return attributeList.toString();
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

		AttributeCode attributeCode = persistenceService.queryOneByExample(AttributeCode.class, attributeCodeExample);
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

		List<AttributeCode> attributeCodes = persistenceService.queryByExample(AttributeCode.class, queryByExample);
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
		SolrServer solrService = SolrManager.getServer();

		try {
			solrService.deleteById(id);
			solrService.commit();
		} catch (IOException | SolrServerException ex) {
			throw new OpenStorefrontRuntimeException("Failed Deleting Index", "Make sure Search server is active and can be reached", ex);
		}
	}

	@Override
	public void deleteAll()
	{
		SolrServer solrService = SolrManager.getServer();
		try {
			// CAUTION: deletes everything!
			solrService.deleteByQuery(SolrManager.SOLR_ALL_QUERY);
		} catch (SolrServerException | IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to clear all indexes", "Make sure Search server is active and can be reached", ex);
		}
	}

	@Override
	public void saveAll()
	{
		Component temp = new Component();
		temp.setActiveStatus(Component.ACTIVE_STATUS);
		List<Component> components = persistenceService.queryByExample(Component.class, new QueryByExample(temp));

		indexComponents(components);
	}

	@Override
	public void resetIndexer()
	{
		deleteAll();
		saveAll();
	}

	@Override
	public AdvanceSearchResult advanceSearch(SearchModel searchModel)
	{
		Objects.requireNonNull(searchModel, "Search Model Required");

		AdvanceSearchResult searchResult = new AdvanceSearchResult();

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
				case COMPONENT:
					handlers.add(new ComponentSearchHandler(searchElements));
					break;
				case CONTACT:
					handlers.add(new ContactSearchHandler(searchElements));
					break;
				case INDEX:
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
					handlers.add(new ReviewProConSeatchHandler(searchElements));
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
			//process groups and aggergate
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
				String query = "select componentId, componentType, name, lastUpdateDts, activeStatus, approvalState from " + Component.class.getSimpleName() + " where componentId in :idList";
				Map<String, Object> parameterMap = new HashMap<>();
				parameterMap.put("idList", masterResults);	
				List<ODocument> results = persistenceService.query(query, parameterMap);
				
				Map<String, ComponentSearchView> resultMap = new HashMap<>();
				for (ODocument doc : results) {
					if(Component.ACTIVE_STATUS.equals(doc.field("activeStatus")) &&
						ApprovalStatus.APPROVED.equals(doc.field("approvalState")))
					{
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
					if(stats.containsKey(view.getComponentType())) {
						ResultTypeStat stat = stats.get(view.getComponentType());
						stat.setCount(stat.getCount() + 1);						
					} else {
						ResultTypeStat stat = new ResultTypeStat();
						stat.setComponentType(view.getComponentType());
						stat.setComponentTypeDescription(TranslateUtil.translateComponentType(view.getComponentType()));
						stat.setCount(1);						
						stats.put(view.getComponentType(), stat);
					}
				}
				searchResult.getResultTypeStats().addAll(stats.values());				
				List<ComponentSearchView> intermediateViews = new ArrayList<>(resultMap.values());
								
				//then sort/window
				if (StringUtils.isNotBlank(searchModel.getSortField())) {
					Collections.sort(intermediateViews, new BeanComparator<>(searchModel.getSortDirection(), searchModel.getSortField()));
				}				
				
				List<String> idsToResolve = new ArrayList<>();
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
				
				//resolve results
				List<ComponentSearchView> views = getComponentService().getSearchComponentList(idsToResolve);
				if (StringUtils.isNotBlank(searchModel.getSortField())) {
					Collections.sort(views, new BeanComparator<>(searchModel.getSortDirection(), searchModel.getSortField()));
				}				
				searchResult.getResults().addAll(views);				
			}
		}
		searchResult.setValidationResult(validationResultMain);

		return searchResult;
	}
	
	@Override
	public IndexSearchResult doIndexSearch(String query, FilterQueryParams filter)
	{
		return doIndexSearch(query, filter, null);
	}	
	
	public IndexSearchResult doIndexSearch(String query, FilterQueryParams filter, String[] addtionalFieldsToReturn)
	{
		IndexSearchResult indexSearchResult = new IndexSearchResult();

		List<SolrComponentModel> resultsList = new ArrayList<>();

		// use for advanced search with And - Or combinations on separate fields
		String queryOperator = " " + SolrAndOr.OR + " ";
		String myQueryString;

		// If incoming query string is blank, default to solar *:* for the full query
		if (StringUtils.isNotBlank(query)) {
			StringBuilder queryData = new StringBuilder();

			Field fields[] = SolrComponentModel.class.getDeclaredFields();
			for (Field field : fields) {
				org.apache.solr.client.solrj.beans.Field fieldAnnotation = field.getAnnotation(org.apache.solr.client.solrj.beans.Field.class);
				if (fieldAnnotation != null && field.getType() == String.class) {
					String name = field.getName();
					if (StringUtils.isNotBlank(fieldAnnotation.value())
							&& org.apache.solr.client.solrj.beans.Field.DEFAULT.equals(fieldAnnotation.value()) == false) {
						name = fieldAnnotation.value();
					}

					queryData.append(SolrEquals.EQUAL.getSolrOperator())
							.append(name)
							.append(SolrManager.SOLR_QUERY_SEPERATOR)
							.append(query)
							.append(queryOperator);
				}
			}
			myQueryString = queryData.toString();
			if (myQueryString.endsWith(queryOperator)) {
				queryData.delete((myQueryString.length() - (queryOperator.length())), myQueryString.length());
				myQueryString = queryData.toString();
			}
		} else {
			myQueryString = SolrManager.SOLR_ALL_QUERY;
		}
		log.log(Level.FINER, myQueryString);

		// execute the searchComponent method and bring back from solr a list array
		long totalFound = 0;
		try {
			SolrQuery solrQuery = new SolrQuery();
			solrQuery.setQuery(myQueryString);

			// fields to be returned back from solr			
			solrQuery.setFields(SolrComponentModel.ID_FIELD, SolrComponentModel.ISCOMPONENT_FIELD);
			if (addtionalFieldsToReturn != null) {
				for (String field : addtionalFieldsToReturn) {
					solrQuery.addField(field);
				}
			}
			
			solrQuery.setStart(filter.getOffset());
			solrQuery.setRows(filter.getMax());

			Field sortField = ReflectionUtil.getField(new SolrComponentModel(), filter.getSortField());
			if (sortField != null) {
				String sortFieldText = filter.getSortField();
				org.apache.solr.client.solrj.beans.Field fieldAnnotation = sortField.getAnnotation(org.apache.solr.client.solrj.beans.Field.class);
				if (fieldAnnotation != null) {
					sortFieldText = fieldAnnotation.value();
				}
				SolrQuery.ORDER order = SolrQuery.ORDER.desc;
				if (OpenStorefrontConstant.SORT_ASCENDING.equalsIgnoreCase(filter.getSortOrder())) {
					order = SolrQuery.ORDER.asc;
				}
				solrQuery.addSort(sortFieldText, order);
			} 

			solrQuery.setIncludeScore(true);

			QueryResponse response = SolrManager.getServer().query(solrQuery);
			SolrDocumentList results = response.getResults();
			totalFound = results.getNumFound();				
						
			DocumentObjectBinder binder = new DocumentObjectBinder();
			resultsList = binder.getBeans(SolrComponentModel.class, results);		
			
		} catch (SolrServerException ex) {
			throw new OpenStorefrontRuntimeException("Search Failed", "Contact System Admin.  Seach server maybe Unavailable", ex);
		} catch (Exception ex) {
			log.log(Level.WARNING, "Solr query failed unexpectly; likely bad input.", ex);
		}
		indexSearchResult.getResultsList().addAll(resultsList);
		indexSearchResult.setTotalResults(totalFound);

		return indexSearchResult;
	}

	@Override
	public List<SearchSuggestion> searchSuggestions(String query, int maxResult)
	{
		List<SearchSuggestion> suggestions = new ArrayList<>();
		
		FilterQueryParams filter = FilterQueryParams.defaultFilter();
		
		//query everything we can
		String extraFields[] = {
			SolrComponentModel.FIELD_NAME, 
			SolrComponentModel.FIELD_ORGANIZATION, 
			SolrComponentModel.FIELD_DESCRIPTION, 
		};
		IndexSearchResult indexSearchResult = doIndexSearch(query, filter, extraFields);
		
		//apply weight to items
		if (StringUtils.isBlank(query)) {
			query = "";
		}
		
		String queryNoWild = query.replace("*", "").toLowerCase();
		for (SolrComponentModel model : indexSearchResult.getResultsList()) {
			int score = 0;
						
			if (StringUtils.isNotBlank(model.getName()) &&
					model.getName().toLowerCase().contains(queryNoWild)) {
				score += 100;
			}
			
			if (StringUtils.isNotBlank(model.getOrganization()) &&
					model.getOrganization().toLowerCase().contains(queryNoWild)) {
				score += 50;
			}
			
			int count = StringUtils.countMatches(model.getDescription().toLowerCase(), queryNoWild);
			score += count * 5;	
			
			model.setSearchWeight(score);			
		}
		
		//sort
		indexSearchResult.getResultsList().sort((SolrComponentModel o1, SolrComponentModel o2) -> Integer.compare(o2.getSearchWeight(), o1.getSearchWeight()));
		
		//window
		List<SolrComponentModel> topItems = indexSearchResult.getResultsList().stream().limit(maxResult).collect(Collectors.toList());
		
		for (SolrComponentModel model : topItems) {
			
			SearchSuggestion suggestion = new SearchSuggestion();
			suggestion.setName(model.getName());
			suggestion.setComponentId(model.getId());
			suggestion.setQuery("\"" + model.getName() + "\"");
			
			// Only include approved components.
			if (getComponentService().checkComponentApproval(suggestion.getComponentId())) {
				suggestions.add(suggestion);
			}
		}
				
		return suggestions;
	}

	@Override
	public SystemSearch saveSearch(SystemSearch systemSearch)
	{
		SystemSearch existing = persistenceService.findById(SystemSearch.class, systemSearch.getSearchId());
		if (existing != null) {
			existing.updateFields(systemSearch);
			systemSearch = persistenceService.persist(existing);
		} else {
			systemSearch.setSearchId(persistenceService.generateId());
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
			throw new OpenStorefrontRuntimeException("Search not found", "Check Id: "  + searchId);
		}		
	}

	@Override
	public void activateSearch(String searchId)
	{
		toggleStatusOnSearch(searchId, SystemSearch.ACTIVE_STATUS);
	}

}
