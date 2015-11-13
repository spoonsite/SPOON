/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.SearchService;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.model.search.AdvanceSearchResult;
import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
import edu.usu.sdl.openstorefront.core.model.search.SearchModel;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation.MergeCondition;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation.SearchType;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.view.ArticleView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.SearchQuery;
import edu.usu.sdl.openstorefront.service.manager.SolrManager;
import edu.usu.sdl.openstorefront.service.manager.SolrManager.SolrAndOr;
import edu.usu.sdl.openstorefront.service.manager.SolrManager.SolrEquals;
import edu.usu.sdl.openstorefront.service.search.ArchitectureSearchHandler;
import edu.usu.sdl.openstorefront.service.search.AttributeSearchHandler;
import edu.usu.sdl.openstorefront.service.search.BaseSearchHandler;
import edu.usu.sdl.openstorefront.service.search.ComponentSearchHandler;
import edu.usu.sdl.openstorefront.service.search.ContactSearchHandler;
import edu.usu.sdl.openstorefront.service.search.MetaDataSearchHandler;
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
		implements SearchService
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
		list.addAll(service.getAttributeService().getArticlesSearchView());
		return list;
	}

	@Override
	public ComponentSearchWrapper getSearchItems(SearchQuery query, FilterQueryParams filter)
	{
		ComponentSearchWrapper componentSearchWrapper = new ComponentSearchWrapper();

		// use for advanced search with And - Or combinations on separate fields
		String queryOperator = " " + SolrAndOr.OR + " ";
		String myQueryString;

		// If incoming query string is blank, default to solar *:* for the full query
		if (StringUtils.isNotBlank(query.getQuery())) {
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
							.append(query.getQuery())
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
		List<SolrComponentModel> resultsList = new ArrayList<>();
		long totalFound = 0;
		try {
			SolrQuery solrQuery = new SolrQuery();
			solrQuery.setQuery(myQueryString);

			// fields to be returned back from solr
			solrQuery.setFields(SolrComponentModel.ID_FIELD, SolrComponentModel.ISCOMPONENT_FIELD);
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

		List<ComponentSearchView> articleViews = getAttributeService().getArticlesSearchView();
		Map<String, ComponentSearchView> allViews = new HashMap<>();
		for (ComponentSearchView componentSearchView : articleViews) {
			AttributeCodePk attributeCodePk = new AttributeCodePk();
			attributeCodePk.setAttributeType(componentSearchView.getArticleAttributeType());
			attributeCodePk.setAttributeCode(componentSearchView.getArticleAttributeCode());
			allViews.put(attributeCodePk.toKey(), componentSearchView);
		}
		for (SolrComponentModel result : resultsList) {

			if (result.getIsComponent() == false) {
				ComponentSearchView view = allViews.get(result.getId());
				if (view != null) {
					views.add(view);
				} else {
					log.log(Level.FINE, MessageFormat.format("Removing bad index: {0}", result.getId()));
					deleteById(result.getId());
				}
			}
		}

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
			for (ComponentAttribute componentAttribute : allAttributes) {
				if (attributeMap.containsKey(componentAttribute.getComponentAttributePk().getComponentId())) {
					attributeMap.get(componentAttribute.getComponentAttributePk().getComponentId()).add(componentAttribute);
				} else {
					List<ComponentAttribute> componentAttributes = new ArrayList<>();
					componentAttributes.add(componentAttribute);
					attributeMap.put(componentAttribute.getComponentAttributePk().getComponentId(), componentAttributes);
				}
			}

			ComponentTag componentTagExample = new ComponentTag();
			componentTagExample.setActiveStatus(ComponentTag.ACTIVE_STATUS);
			List<ComponentTag> allTags = persistenceService.queryByExample(ComponentTag.class, componentTagExample);
			for (ComponentTag tag : allTags) {
				if (tagMap.containsKey(tag.getComponentId())) {
					tagMap.get(tag.getComponentId()).add(tag);
				} else {
					List<ComponentTag> componentTags = new ArrayList<>();
					componentTags.add(tag);
					tagMap.put(tag.getComponentId(), componentTags);
				}
			}
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

			String tagList = "";
			String attributeList = "";

			for (ComponentTag tag : tags) {
				tagList = tagList + tag.getText() + ",";
			}

			for (ComponentAttribute attribute : attributes) {
				ComponentAttributePk pk = attribute.getComponentAttributePk();
				AttributeCodePk codePk = new AttributeCodePk();
				codePk.setAttributeCode(pk.getAttributeCode());
				codePk.setAttributeType(pk.getAttributeType());
				AttributeCode code = getAttributeService().findCodeForType(codePk);
				AttributeType type = getAttributeService().findType(pk.getAttributeType());
				attributeList = attributeList + pk.getAttributeCode() + ",";
				attributeList = attributeList + pk.getAttributeType() + ",";

				if (code != null && type != null) {
					attributeList = attributeList + code.getLabel() + ",";
					if (StringUtils.isNotBlank(code.getDescription())) {
						attributeList = attributeList + code.getDescription() + ",";
					}
					if (StringUtils.isNotBlank(type.getDescription())) {
						attributeList = attributeList + type.getDescription() + ",";
					}
				}
			}

			solrDocModel.setTags(tagList);
			solrDocModel.setAttributes(attributeList);
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

	@Override
	public void indexArticles(List<ArticleView> articles)
	{

		// initialize solr server
		SolrServer solrService = SolrManager.getServer();

		List<SolrComponentModel> solrDocs = new ArrayList<>();
		for (ArticleView article : articles) {
			if (StringUtils.isNotBlank(article.getHtml())) {

				//add document using the example schema
				SolrComponentModel solrDocModel = new SolrComponentModel();
				AttributeCodePk pk = new AttributeCodePk();
				pk.setAttributeCode(article.getAttributeCode());
				pk.setAttributeType(article.getAttributeType());
				AttributeCode code = getAttributeService().findCodeForType(pk);
				AttributeType type = getAttributeService().findType(article.getAttributeType());
				if (type == null || code == null) {
					log.log(Level.FINE, MessageFormat.format("Unable to find attribute type and/or code (Skipping Index) for:  {0}", pk.toString()));
				} else {

					solrDocModel.setIsComponent(Boolean.FALSE);

					solrDocModel.setId(pk.toKey());
					solrDocModel.setNameString(article.getTitle());
					solrDocModel.setName(article.getTitle());
					solrDocModel.setDescription(article.getDescription());
					solrDocModel.setUpdateDts(article.getUpdateDts());

					String attributeList = type.getAttributeType() + "," + StringProcessor.blankIfNull(type.getDescription()) + "," + code.getLabel() + "," + StringProcessor.blankIfNull(code.getDescription());
					solrDocModel.setTags("");
					solrDocModel.setAttributes(attributeList);

					String htmlArticle = article.getHtml();
					String plainText = StringProcessor.stripHtml(htmlArticle);

					solrDocModel.setArticleHtml(plainText.replace("<>", "").replace("\n", ""));
					solrDocs.add(solrDocModel);
				}
			} else {
				log.log(Level.FINE, "Html content is required to index article. (skipping)");
			}
		}

		if (solrDocs.isEmpty() == false) {
			try {
				solrService.addBeans(solrDocs);
				solrService.commit();
			} catch (IOException | SolrServerException ex) {
				throw new OpenStorefrontRuntimeException("Failed Adding Article", ex);
			}
		}
	}

	@Override
	public List<ComponentSearchView> architectureSearch(AttributeCodePk pk, FilterQueryParams filter)
	{
		List<ArticleView> articles = this.getAttributeService().getArticlesForCodeLike(pk);

		List<ComponentSearchView> views = new ArrayList<>();
		for (ArticleView article : articles) {
			views.add(ComponentSearchView.toView(article));
		}

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
		List<ArticleView> articles = getAttributeService().getArticles();

		indexComponents(components);
		indexArticles(articles);
	}

	@Override
	public void indexArticlesAndComponents(List<ArticleView> articles, List<Component> components)
	{
		indexComponents(components);
		indexArticles(articles);
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

			//resolve results
			List<ComponentSearchView> views = getComponentService().getSearchComponentList(new ArrayList<>(masterResults));

			//sort and window
			if (StringUtils.isNotBlank(searchModel.getSortField())) {
				Collections.sort(views, new BeanComparator<>(searchModel.getSortDirection(), searchModel.getSortField()));
			}
			searchResult.setTotalNumber(views.size());

			//window
			if (searchModel.getStartOffset() < views.size() && searchModel.getMax() > 0) {
				int count = 0;
				for (int i = searchModel.getStartOffset(); i < views.size(); i++) {
					searchResult.getResults().add(views.get(i));
					count++;
					if (count >= searchModel.getMax()) {
						break;
					}
				}
			}
		}
		searchResult.setValidationResult(validationResultMain);

		return searchResult;
	}
}
