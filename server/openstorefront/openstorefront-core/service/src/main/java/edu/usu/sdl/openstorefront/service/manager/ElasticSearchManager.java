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
package edu.usu.sdl.openstorefront.service.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.model.search.SearchSuggestion;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.SearchQuery;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.search.IndexSearchResult;
import edu.usu.sdl.openstorefront.service.search.SearchServer;
import edu.usu.sdl.openstorefront.service.search.SolrComponentModel;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

/**
 *
 * @author dshurtleff
 */
public class ElasticSearchManager
		implements Initializable, SearchServer
{

	private static final Logger LOG = Logger.getLogger(ElasticSearchManager.class.getName());

	private static final String INDEX = "openstorefront";
	private static final String INDEX_TYPE = "component";
	private static final String ELASTICSEARCH_ALL_FIELDS = "_all";

	private static AtomicBoolean started = new AtomicBoolean(false);
	private static Client client;
	private static AtomicBoolean checkIndex = new AtomicBoolean(true);

	public static void init()
	{
		String host = PropertiesManager.getValue(PropertiesManager.KEY_ELASTIC_HOST, "localhost");
		Integer port = Convert.toInteger(PropertiesManager.getValue(PropertiesManager.KEY_ELASTIC_PORT, "9300"));

		try {
			LOG.log(Level.INFO, MessageFormat.format("Connecting to ElasticSearch at {0}", host + ":" + port));

			client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));

		} catch (UnknownHostException ex) {
			throw new OpenStorefrontRuntimeException("Unable to find elastic search server host", "Check configuration;  Check Host and Port;   property: " + PropertiesManager.KEY_ELASTIC_HOST + " current value: " + host);
		}

	}

	public static Client getClient()
	{
		if (checkIndex.get() && client != null) {
			IndicesExistsResponse response = client.admin().indices()
					.exists(new IndicesExistsRequest(INDEX))
					.actionGet();

			if (response.isExists() == false) {
				CreateIndexResponse createIndexResponse = client.admin().indices()
						.create(new CreateIndexRequest(INDEX))
						.actionGet();
				if (createIndexResponse.isAcknowledged()) {
					LOG.log(Level.INFO, "Search index: " + INDEX + " was created.");
				} else {
					LOG.log(Level.WARNING, "Search index: " + INDEX + " FAILED to create. Add a record to create");
				}
			}

			checkIndex.set(true);
		}

		return client;
	}

	public static void cleanup()
	{
		if (client != null) {
			client.close();
		}
	}

	@Override
	public void initialize()
	{
		ElasticSearchManager.init();
		started.set(true);
	}

	@Override
	public void shutdown()
	{
		ElasticSearchManager.cleanup();
		started.set(false);
	}

	private ServiceProxy service = ServiceProxy.getProxy();

	@Override
	public ComponentSearchWrapper search(SearchQuery searchQuery, FilterQueryParams filter)
	{
		ComponentSearchWrapper componentSearchWrapper = new ComponentSearchWrapper();

		IndexSearchResult indexSearchResult = doIndexSearch(searchQuery.getQuery(), filter);
		
		

		SearchServerManager.updateSearchScore(searchQuery.getQuery(), indexSearchResult.getSearchViews());

		componentSearchWrapper.setData(indexSearchResult.getSearchViews());
		componentSearchWrapper.setResults(indexSearchResult.getSearchViews().size());
		componentSearchWrapper.setTotalNumber(indexSearchResult.getTotalResults());

		return componentSearchWrapper;
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

		int maxSearchResults = 10000;
		if (filter.getMax() < maxSearchResults) {
			maxSearchResults = filter.getMax();
		}

		if (StringUtils.isBlank(query)) {
			query = "*";
		}

		// Initialize Search Phrases
		// (Searching Different Phrases [Quoted Words])
		List<String> searchPhrases = new ArrayList<>();

		// Convert Query To StringBuilder
		StringBuilder queryString = new StringBuilder(query);

		// Search For Quotes
		while (queryString.indexOf("\"") != -1) {

			// Store Index Of First Quote
			int quoteStartIndex = queryString.indexOf("\"");

			// Store Index Of Next Quote
			int quoteEndIndex = queryString.indexOf("\"", quoteStartIndex + 1);

			// Check If We Failed To Find Next Quote
			if (quoteEndIndex == -1) {

				// Remove First Quote
				queryString.deleteCharAt(quoteStartIndex);
			} else {

				// Create Sub-String Of Quoted Phrase
				String subQueryString = queryString.substring(quoteStartIndex, quoteEndIndex + 1);

				// Remove Sub-String From Query
				queryString.delete(quoteStartIndex, quoteEndIndex + 1);

				// Add Sub-String To Search Phrases
				searchPhrases.add(subQueryString);

				//////////
				// TRIM //
				//////////
				// Replace All Double Spaces
				while (queryString.indexOf("  ") != -1) {

					// Get Index Of Double Space
					int doubleSpaceIndex = queryString.indexOf("  ");

					queryString.replace(doubleSpaceIndex, doubleSpaceIndex + 2, " ");
				}

				// Search For Beginning Whitespace
				if (queryString.length() > 0 && queryString.charAt(0) == ' ') {

					// Remove Whitespace
					queryString.deleteCharAt(0);
				}

				// Search For Ending Whitespace
				if (queryString.length() > 0 && queryString.charAt(queryString.length() - 1) == ' ') {

					// Remove Whitespace
					queryString.deleteCharAt(queryString.length() - 1);
				}
			}
		}

		// Initialize ElasticSearch Query
		BoolQueryBuilder esQuery = QueryBuilders.boolQuery();

		// Check For Remaining Query Items
		if (queryString.length() > 0) {

			// Create Standard Query
			esQuery.should(QueryBuilders.matchQuery(ComponentSearchView.FIELD_NAME, queryString.toString()));
			esQuery.should(QueryBuilders.matchQuery(ComponentSearchView.FIELD_ORGANIZATION, queryString.toString()));
                        esQuery.should(QueryBuilders.matchPhraseQuery("description", queryString.toString()));
			esQuery.should(QueryBuilders.wildcardQuery(ELASTICSEARCH_ALL_FIELDS, queryString.toString()));
			esQuery.should(QueryBuilders.fuzzyQuery(ELASTICSEARCH_ALL_FIELDS, queryString.toString()));
		}

		// Loop Through Search Phrases
		for (String phrase : searchPhrases) {

			esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_NAME, phrase));
			esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_ORGANIZATION, phrase));
                        esQuery.should(QueryBuilders.matchPhraseQuery("description", phrase));
		}
		
		SearchResponse response = ElasticSearchManager.getClient()
				.prepareSearch(INDEX)
				.setQuery(esQuery)
				.setFrom(filter.getOffset())
				.setSize(maxSearchResults)				
				.addSort(filter.getSortField(), OpenStorefrontConstant.SORT_ASCENDING.equals(filter.getSortOrder()) ? SortOrder.ASC : SortOrder.DESC)
				.execute()
				.actionGet();

		indexSearchResult.setTotalResults(response.getHits().getTotalHits());
		indexSearchResult.setMaxScore(response.getHits().getMaxScore());

		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();
		for (SearchHit hit : response.getHits().getHits()) {
			try {
				ComponentSearchView view = objectMapper.readValue(hit.getSourceAsString(), new TypeReference<ComponentSearchView>()
				{
				});
				if (service.getComponentService().checkComponentApproval(view.getComponentId())) {
					view.setSearchScore(hit.getScore());
					indexSearchResult.getSearchViews().add(view);
					indexSearchResult.getResultsList().add(SolrComponentModel.fromComponentSearchView(view));
				} else {
					LOG.log(Level.FINER, MessageFormat.format("Component is no long approved and active.  Removing index.  {0}", view.getComponentId()));
					indexSearchResult.setTotalResults(indexSearchResult.getTotalResults()-1);
					deleteById(view.getComponentId());
				}
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException("Unable to handle search result", "check index database", ex);
			}
		}
		indexSearchResult.applyDataFilter();
		
		return indexSearchResult;
	}

	@Override
	public List<SearchSuggestion> searchSuggestions(String query, int maxResult)
	{
		List<SearchSuggestion> searchSuggestions = new ArrayList<>();

		FilterQueryParams filter = FilterQueryParams.defaultFilter();

		//ignore case
		query = "*" + query.toLowerCase() + "*";

		//query everything we can
		String extraFields[] = {
			SolrComponentModel.FIELD_NAME,
			SolrComponentModel.FIELD_ORGANIZATION,
			SolrComponentModel.FIELD_DESCRIPTION
		};
		IndexSearchResult indexSearchResult = doIndexSearch(query, filter, extraFields);

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
				searchSuggestions.add(suggestion);
			}
		}

		return searchSuggestions;
	}

	@Override
	public void index(List<Component> components)
	{
		Objects.requireNonNull(components);

		if (!components.isEmpty()) {
			ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();
			BulkRequestBuilder bulkRequest = ElasticSearchManager.getClient().prepareBulk();

			//pull attribute and map
			ComponentAttribute componentAttribute = new ComponentAttribute();
			componentAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
			List<ComponentAttribute> componentAttributes = componentAttribute.findByExample();
			Map<String, List<ComponentAttribute>> attributeMap = componentAttributes.stream().collect(Collectors.groupingBy(ComponentAttribute::getComponentId));

			//pull reviews and map
			ComponentReview componentReview = new ComponentReview();
			componentReview.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			List<ComponentReview> componentReviews = componentReview.findByExample();
			Map<String, List<ComponentReview>> reviewMap = componentReviews.stream().collect(Collectors.groupingBy(ComponentReview::getComponentId));

			//pull tags and map
			ComponentTag componentTag = new ComponentTag();
			componentTag.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			List<ComponentTag> componentTags = componentTag.findByExample();
			Map<String, List<ComponentTag>> tagMap = componentTags.stream().collect(Collectors.groupingBy(ComponentTag::getComponentId));

			for (Component component : components) {

				//convert to search result object
				componentAttributes = attributeMap.getOrDefault(component.getComponentId(), new ArrayList<>());
				componentReviews = reviewMap.getOrDefault(component.getComponentId(), new ArrayList<>());
				componentTags = tagMap.getOrDefault(component.getComponentId(), new ArrayList<>());

				ComponentSearchView componentSearchView = ComponentSearchView.toView(component, componentAttributes, componentReviews, componentTags);

				try {
					bulkRequest.add(ElasticSearchManager.getClient().prepareIndex(INDEX, INDEX_TYPE, componentSearchView.getComponentId())
							.setSource(objectMapper.writeValueAsBytes(componentSearchView)));
				} catch (JsonProcessingException ex) {
					LOG.log(Level.SEVERE, MessageFormat.format("Unable to index component: {0}  Component will be missing from search.", componentSearchView.getName()));
				}
			}

			BulkResponse bulkResponse = bulkRequest.get();
			if (bulkResponse.hasFailures()) {
				bulkResponse.forEach(response -> {
					if (StringUtils.isNotBlank(response.getFailureMessage())) {
						LOG.log(Level.WARNING, MessageFormat.format("A component failed to index: {0}", response.getFailureMessage()));
					}
				});
			} else {
				LOG.log(Level.FINE, "Index components successfully");
			}
		}
	}

	@Override
	public void deleteById(String id)
	{
		DeleteResponse response = client.prepareDelete(INDEX, INDEX_TYPE, id).get();
		LOG.log(Level.FINER, MessageFormat.format("Found Record to delete: {0}", response.isFound()));
	}

	@Override
	public void deleteAll()
	{
		//query all (delete in groups)
		int start = 0;
		int max = 1000;
		long total = max;

		while (start < total) {
			SearchResponse response = ElasticSearchManager.getClient()
					.prepareSearch()
					.setQuery(QueryBuilders.matchAllQuery())
					.setFrom(start)
					.setSize(max)
					.execute()
					.actionGet();

			SearchHits searchHits = response.getHits();
			if (searchHits.getTotalHits() > 0) {
				//bulk delete results
				BulkRequestBuilder bulkRequest = ElasticSearchManager.getClient().prepareBulk();
				searchHits.forEach(hit -> {
					bulkRequest.add(ElasticSearchManager.getClient().prepareDelete(INDEX, INDEX_TYPE, hit.getId()));
				});

				BulkResponse bulkResponse = bulkRequest.get();
				if (bulkResponse.hasFailures()) {
					bulkResponse.forEach(br -> {
						if (StringUtils.isNotBlank(br.getFailureMessage())) {
							LOG.log(Level.WARNING, MessageFormat.format("A component failed to delete: {0}", br.getFailureMessage()));
						}
					});
				}
			}
			start += searchHits.getHits().length;
			total = searchHits.getTotalHits();
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

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

}
