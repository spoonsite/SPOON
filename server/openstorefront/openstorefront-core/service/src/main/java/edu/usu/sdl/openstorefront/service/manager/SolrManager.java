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
package edu.usu.sdl.openstorefront.service.manager;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.model.search.SearchSuggestion;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.SearchQuery;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.search.IndexSearchResult;
import edu.usu.sdl.openstorefront.service.search.SolrComponentModel;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;

/**
 * Handles the connection to Solr
 *
 * @author dshurtleff
 */
public class SolrManager
		extends BaseSearchManager
		implements Initializable
{

	private static final Logger log = Logger.getLogger(SolrManager.class.getName());

	public static final String SOLR_ALL_QUERY = "*:*";
	public static final String SOLR_QUERY_SEPERATOR = ":";
	private static AtomicBoolean started = new AtomicBoolean(false);

	public static enum SolrAndOr
	{

		AND,
		OR
	}

	public static enum SolrEquals
	{

		EQUAL(""),
		NOTEQUAL("-");

		private final String solrOperator;

		private SolrEquals(String solrOperator)
		{
			this.solrOperator = solrOperator;
		}

		public String getSolrOperator()
		{
			return solrOperator;
		}
	}

	//Should reuse server to avoid leaks according to docs.
	private static SolrClient solrServer;

	public static void init()
	{
		String url = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_SOLR_URL);
		if (StringUtils.isNotBlank(url)) {
			log.log(Level.INFO, MessageFormat.format("Connecting to Solr at {0}", url));

			//use the xml instead of binary
			String xml = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_SOLR_USE_XML);
			if (StringUtils.isNotBlank(xml)) {
				solrServer = new HttpSolrClient.Builder(url)
						.allowCompression(true)
						.withResponseParser(new XMLResponseParser())
						.build();
			} else {
				solrServer = new HttpSolrClient.Builder(url)
						.allowCompression(true)
						.build();
			}

		} else {
			log.log(Level.WARNING, "Solr property (" + PropertiesManager.KEY_SOLR_URL + ") is not set in openstorefront.properties. Search service unavailible. Using Mock");
			solrServer = new SolrClient()
			{

				@Override
				public NamedList<Object> request(SolrRequest request, String string) throws SolrServerException, IOException
				{
					NamedList<Object> results = new NamedList<>();
					log.log(Level.INFO, MessageFormat.format("Mock Solr recieved request: {0}", request));
					return results;
				}

				@Override
				public void close() throws IOException
				{
					//nothing to do
				}

			};
		}
	}

	public static void cleanup()
	{
		if (solrServer != null) {
			try {
				solrServer.close();
			} catch (IOException ex) {
				log.log(Level.WARNING, "Unable to close connection to Solr. Connection may be unstable.", ex);
			}
		}
	}

	public static SolrClient getServer()
	{
		return solrServer;
	}

	private ServiceProxy service = ServiceProxy.getProxy();

	@Override
	public void initialize()
	{
		SolrManager.init();
		started.set(true);
	}

	@Override
	public void shutdown()
	{
		SolrManager.cleanup();
		started.set(false);
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

	@Override
	public ComponentSearchWrapper search(SearchQuery searchQuery, FilterQueryParams filter)
	{
		ComponentSearchWrapper componentSearchWrapper = new ComponentSearchWrapper();

		IndexSearchResult indexSearchResult = doIndexSearch(searchQuery.getQuery(), filter);
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
		List<ComponentSearchView> componentSearchViews = service.getComponentService().getSearchComponentList(componentIds);
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
		updateSearchScore(searchQuery.getQuery(), componentSearchViews);

		views.addAll(componentSearchViews);

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
	public void index(List<Component> components)
	{
		// initialize solr server
		SolrClient solrService = SolrManager.getServer();

		Map<String, List<ComponentAttribute>> attributeMap = new HashMap<>();
		Map<String, List<ComponentTag>> tagMap = new HashMap<>();

		if (components.size() > 1) {
			ComponentAttribute componentAttributeExample = new ComponentAttribute();
			componentAttributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
			List<ComponentAttribute> allAttributes = service.getPersistenceService().queryByExample(componentAttributeExample);
			attributeMap = allAttributes.stream().collect(Collectors.groupingBy(ComponentAttribute::getComponentId));

			ComponentTag componentTagExample = new ComponentTag();
			componentTagExample.setActiveStatus(ComponentTag.ACTIVE_STATUS);
			List<ComponentTag> allTags = service.getPersistenceService().queryByExample(componentTagExample);
			tagMap = allTags.stream().collect(Collectors.groupingBy(ComponentTag::getComponentId));

		}

		List<SolrComponentModel> solrDocs = new ArrayList<>();
		for (Component component : components) {

			//add document using the example schema
			SolrComponentModel solrDocModel = new SolrComponentModel();

			solrDocModel.setIsComponent(Boolean.TRUE);
			solrDocModel.setId(component.getComponentId());
			solrDocModel.setNameString(component.getName());
			solrDocModel.setComponentType(component.getComponentType());
			solrDocModel.setDataSource(component.getDataSource());
			solrDocModel.setDataSensitivity(component.getDataSensitivity());
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
				tags = service.getComponentService().getBaseComponent(ComponentTag.class, component.getComponentId());
				attributes = service.getComponentService().getBaseComponent(ComponentAttribute.class, component.getComponentId());
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
		AttributeCode code = service.getAttributeService().findCodeForType(codePk);
		AttributeType type = service.getAttributeService().findType(codePk.getAttributeType());
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
	public IndexSearchResult doIndexSearch(String query, FilterQueryParams filter)
	{
		return doIndexSearch(query, filter, null);
	}

	@Override
	public IndexSearchResult doIndexSearch(String query, FilterQueryParams filter, String[] addtionalFieldsToReturn)
	{
		IndexSearchResult indexSearchResult = new IndexSearchResult();

		List<SolrComponentModel> resultsList = new ArrayList<>();

		// use for advanced search with And - Or combinations on separate fields
		String queryOperator = " " + SolrAndOr.OR + " ";
		String myQueryString;

		// If incoming query string is blank, default to solar *:* for the full query
		if (StringUtils.isNotBlank(query)) {
//			StringBuilder queryData = new StringBuilder();
//
//			Field fields[] = SolrComponentModel.class.getDeclaredFields();
//			for (Field field : fields) {
//				org.apache.solr.client.solrj.beans.Field fieldAnnotation = field.getAnnotation(org.apache.solr.client.solrj.beans.Field.class);
//				if (fieldAnnotation != null && field.getType() == String.class) {
//					String name = field.getName();
//					if (StringUtils.isNotBlank(fieldAnnotation.value())
//							&& "#default".equals(fieldAnnotation.value()) == false) {
//						name = fieldAnnotation.value();
//					}
//
//					queryData.append(SolrEquals.EQUAL.getSolrOperator())
//							.append(name)
//							.append(SolrManager.SOLR_QUERY_SEPERATOR)
//							.append(query)
//							.append(queryOperator);
//				}
//			}
//			myQueryString = queryData.toString();
//			if (myQueryString.endsWith(queryOperator)) {
//				queryData.delete((myQueryString.length() - (queryOperator.length())), myQueryString.length());
//				myQueryString = queryData.toString();
//			}
			myQueryString = query;
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

//			DocumentObjectBinder binder = new DocumentObjectBinder();
//			resultsList = binder.getBeans(SolrComponentModel.class, results);
			for (SolrDocument document : results) {
				SolrComponentModel solrComponentModel = new SolrComponentModel();
				solrComponentModel.setComponentId((String) document.get(SolrComponentModel.ID_FIELD));
				solrComponentModel.setId((String) document.get(SolrComponentModel.ID_FIELD));
				solrComponentModel.setIsComponent(Convert.toBoolean(document.get(SolrComponentModel.ISCOMPONENT_FIELD)));

				if (document.get(SolrComponentModel.FIELD_NAME) instanceof ArrayList) {
					List<String> names = (ArrayList<String>) document.get(SolrComponentModel.FIELD_NAME);
					solrComponentModel.setName((names != null ? names.get(0) : null));
				} else if (document.get(SolrComponentModel.FIELD_NAME) instanceof String) {
					solrComponentModel.setName((String) document.get(SolrComponentModel.FIELD_NAME));
				}

				solrComponentModel.setComponentType((String) document.get(SolrComponentModel.FIELD_COMPONENTTYPE));
				solrComponentModel.setOrganization((String) document.get(SolrComponentModel.FIELD_ORGANIZATION));
				solrComponentModel.setDescription((String) document.get(SolrComponentModel.FIELD_DESCRIPTION));
				solrComponentModel.setQueryScore((float) document.get("score"));
				resultsList.add(solrComponentModel);
			}

		} catch (SolrServerException ex) {
			throw new OpenStorefrontRuntimeException("Search Failed", "Contact System Admin.  Seach server maybe Unavailable", ex);
		} catch (Exception ex) {
			log.log(Level.WARNING, "Solr query failed unexpectly; likely bad input.", ex);
		}
		indexSearchResult.getResultsList().addAll(resultsList);
		indexSearchResult.setTotalResults(totalFound);
		indexSearchResult.applyDataFilter();

		return indexSearchResult;
	}

	@Override
	public List<SearchSuggestion> searchSuggestions(String query, int maxResult, String componentType)
	{
		List<SearchSuggestion> suggestions = new ArrayList<>();

		FilterQueryParams filter = FilterQueryParams.defaultFilter();

		//query everything we can
		String extraFields[] = {
			SolrComponentModel.FIELD_NAME,
			SolrComponentModel.FIELD_ORGANIZATION,
			SolrComponentModel.FIELD_DESCRIPTION,};
		query = "*" + query + "*";
		IndexSearchResult indexSearchResult = doIndexSearch(query, filter, extraFields);

		if (org.apache.commons.lang.StringUtils.isNotBlank(componentType)) {
			indexSearchResult.setResultsList(
					indexSearchResult.getResultsList()
							.stream()
							.filter((result) -> componentType.equals(result.getComponentType()))
							.collect(Collectors.toList())
			);
			indexSearchResult.setSearchViews(
					indexSearchResult.getSearchViews()
							.stream()
							.filter((result) -> componentType.equals(result.getComponentType()))
							.collect(Collectors.toList())
			);
		}

		//apply weight to items
		if (StringUtils.isBlank(query)) {
			query = "";
		}

		String queryNoWild = query.replace("*", "").toLowerCase();
		for (SolrComponentModel model : indexSearchResult.getResultsList()) {
			int score = 0;

			if (StringUtils.isNotBlank(model.getName())
					&& model.getName().toLowerCase().contains(queryNoWild)) {
				score += 100;
			}

			if (StringUtils.isNotBlank(model.getOrganization())
					&& model.getOrganization().toLowerCase().contains(queryNoWild)) {
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
			if (service.getComponentService().checkComponentApproval(suggestion.getComponentId())) {
				suggestions.add(suggestion);
			}
		}

		return suggestions;
	}

	@Override
	public void deleteById(String id)
	{
		SolrClient solrService = SolrManager.getServer();

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
		SolrClient solrService = SolrManager.getServer();
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
		Component component = new Component();
		component.setActiveStatus(Component.ACTIVE_STATUS);
		component.setApprovalState(ApprovalStatus.APPROVED);
		List<Component> components = component.findByExample();

		index(components);
	}

	@Override
	public void resetIndexer()
	{
		deleteAll();
		saveAll();
	}

}
