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
import edu.usu.sdl.openstorefront.core.entity.AttributeSearchType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.core.entity.SearchOptions;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.search.SearchSuggestion;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.SearchFilters;
import edu.usu.sdl.openstorefront.core.view.SearchQuery;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.manager.resource.ElasticSearchClient;
import edu.usu.sdl.openstorefront.service.search.IndexSearchResult;
import edu.usu.sdl.openstorefront.service.search.ElasticsearchComponentModel;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

/**
 * Manages elasticsearch, specifically:
 * 	-Adding components to index
 * 	-Removing components from index
 * 	-Reset the index (delete all from index and save all active and approved parts)
 * 	-Search
 *
 * @author dshurtleff & gfowler
 */
public class ElasticSearchManager
		extends BaseSearchManager
		implements Initializable, PooledResourceManager<ElasticSearchClient>
{

	private static final Logger LOG = Logger.getLogger(ElasticSearchManager.class.getName());

	private static final String INDEX = "openstorefront";
	private static final String INDEX_TYPE = "component";
	private static final int MAX_DESCRIPTION_INDEX_SIZE = 8000;

	private static final String INDEX_INNER_WINDOW = "index.max_inner_result_window";

	//These numbers MAY need to changed based on number of components
	private static final int MAX_SEARCH_RESULTS = 10000;
	private static final int NUMBER_INNER_WINDOW_RETURN = 100000;

	private static final String DEFAULT_POOL_SIZE = "40";

	//TODO: Add back search all field as an option
	private static final String ELASTICSEARCH_ALL_FIELDS = "_all";

	private static AtomicBoolean started = new AtomicBoolean(false);
	private static AtomicBoolean indexCreated = new AtomicBoolean(false);

	private BlockingQueue<ElasticSearchClient> clientPool;
	private int maxPoolSize;
	private static ElasticSearchManager singleton = null;
	private PropertiesManager propertiesManager;
	private ServiceProxy service;

	protected ElasticSearchManager(ServiceProxy service, PropertiesManager propertiesManager, BlockingQueue<ElasticSearchClient> clientPool)
	{
		this.clientPool = clientPool;
		this.propertiesManager = propertiesManager;
		this.service = service;
	}

	/**
	 * Check if the search manager is started
	 * @return boolean as to the state of the search manager
	 */
	@Override
	public boolean isStarted()
	{
		return started.get();
	}

	public static ElasticSearchManager getInstance()
	{
		return getInstance(ServiceProxy.getProxy(), PropertiesManager.getInstance(), null);
	}

	public static ElasticSearchManager getInstance(PropertiesManager propertiesManager)
	{
		if (singleton == null) {
			singleton = new ElasticSearchManager(ServiceProxy.getProxy(), propertiesManager, null);
		}
		return singleton;
	}

	public static ElasticSearchManager getInstance(ServiceProxy service, PropertiesManager propertiesManager, BlockingQueue<ElasticSearchClient> clientPool)
	{
		if (singleton == null) {
			singleton = new ElasticSearchManager(service, propertiesManager, clientPool);
		}
		return singleton;
	}

	private void init()
	{
		String poolSize = propertiesManager.getValue(PropertiesManager.KEY_ELASTIC_SEARCH_POOL, DEFAULT_POOL_SIZE);
		maxPoolSize = Convert.toInteger(poolSize);
		LOG.log(Level.INFO, () -> "Initializing Elasticsearch Pool size: " + maxPoolSize);

		String host = propertiesManager.getValue(PropertiesManager.KEY_ELASTIC_HOST, "localhost");
		Integer port = Convert.toInteger(propertiesManager.getValue(PropertiesManager.KEY_ELASTIC_PORT, "9200"));
		LOG.log(Level.INFO, () -> "Initializing Elasticsearch Connection to server: " + host + " port: " + port);
		clientPool = new ArrayBlockingQueue<>(maxPoolSize, true);

		for (int i = 0; i < maxPoolSize; i++) {
			ElasticSearchClient client = createClient(host, port);
			if (!clientPool.offer(client)) {
				LOG.log(Level.FINER, "Client not added to Pool; Pool was full.");
			}
		}
	}

	protected ElasticSearchClient createClient(String host, Integer port)
	{
		return new ElasticSearchClient(host, port, this);
	}

	/**
	 * Close client when done. This will create a new client for each call.
	 *
	 * @return a new client
	 */
	@Override
	public ElasticSearchClient getClient()
	{
		checkSearchIndexCreation();
		ElasticSearchClient client = justGetClient();
		return client;
	}

	private ElasticSearchClient justGetClient()
	{
		if (!isStarted()) {
			throw new OpenStorefrontRuntimeException("Search Manager is not started", "Restart search manager and try again (system admin)");
		}

		if (clientPool.isEmpty()) {
			throw new OpenStorefrontRuntimeException("No elasticsearch client available for searching", "Check pool size and restart search server (system admin)");
		}

		int waitTimeSeconds = Convert.toInteger(propertiesManager.getValue(PropertiesManager.KEY_ELASTIC_CONNECTION_WAIT_TIME, "60"));
		try {
			ElasticSearchClient client = clientPool.poll(waitTimeSeconds, TimeUnit.SECONDS);
			if (client == null) {
				throw new OpenStorefrontRuntimeException("Unable to retrieve Elasticsearch Connection in time.  No resource available.", "Adjust Elasticsearch pool size appropriate to load or try again", ErrorTypeCode.INTEGRATION);
			}
			return client;
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			throw new OpenStorefrontRuntimeException("Unable to retrieve Elasticsearch Connection - wait interrupted.  No resource available.", "Adjust Elasticsearch pool size appropriate to load. (system admin)", ex, ErrorTypeCode.INTEGRATION);
		}
	}

	/**
	 * Check if the index exists, if it does not exist, create an index. 
	 */
	private void checkSearchIndexCreation()
	{
		try (ElasticSearchClient client = justGetClient();) {

			GetIndexRequest indexRequest = new GetIndexRequest(INDEX);
			boolean exists = client.getInstance().indices().exists(indexRequest, RequestOptions.DEFAULT);
			if (!exists){
				try{
					CreateIndexRequest createIndexRequest = new CreateIndexRequest(INDEX);
					client.getInstance().indices().create(createIndexRequest, RequestOptions.DEFAULT);
					LOG.log(Level.INFO, "Created index: " + INDEX);
				} catch (ElasticsearchException e){
					LOG.log(Level.SEVERE, "Unable to connect to elasticsearch", e);
				}
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Unable to connect to elasticsearch", e);
			indexCreated.set(false);
		} catch (OpenStorefrontRuntimeException e){
			LOG.log(Level.SEVERE, "Unable to connect to elasticsearch", e);
			indexCreated.set(false);
		}
	}

	@Override
	public void releaseClient(ElasticSearchClient client)
	{
		if (!clientPool.offer(client)) {
			LOG.log(Level.FINER, "Client not added to Pool; Pool was full.");
		}
	}

	@Override
	public int getMaxConnections()
	{
		return maxPoolSize;
	}

	@Override
	public int getAvailableConnections()
	{
		return maxPoolSize - clientPool.remainingCapacity();
	}

	@Override
	public void shutdownPool()
	{
		for (ElasticSearchClient client : clientPool) {
			client.close();
		}
		clientPool.clear();
	}

	private void cleanup()
	{
		if (singleton != null) {
			if (singleton.getAvailableConnections() != singleton.getMaxConnections()) {
				LOG.log(Level.WARNING, () -> singleton.getAvailableConnections() + " elasticsearch connections were in process. ");
			}
			LOG.log(Level.FINE, "Stopping pool.");
			singleton.shutdownPool();
		}
	}

	@Override
	public void initialize()
	{
		init();
		started.set(true);
	}

	@Override
	public void shutdown()
	{
		cleanup();
		started.set(false);
	}

	/**
	 * Parses the elasticsearch return object into a readable form
	 * 
	 * @param searchQuery object holding the search query
	 * @param filter to apply to the returned elasticsearch result
	 * @return ComponentSearchWrapper 
	 */
	@Override
	public ComponentSearchWrapper search(SearchQuery searchQuery, FilterQueryParams filter)
	{
		ComponentSearchWrapper componentSearchWrapper = new ComponentSearchWrapper();

		IndexSearchResult indexSearchResult = doIndexSearch(searchQuery.getQuery(), filter);

		updateSearchScore(searchQuery.getQuery(), indexSearchResult.getSearchViews());

		componentSearchWrapper.setData(indexSearchResult.getSearchViews());
		componentSearchWrapper.setResults(indexSearchResult.getSearchViews().size());
		componentSearchWrapper.setTotalNumber(indexSearchResult.getTotalResults());

		return componentSearchWrapper;
	}

	/**
	 * Version 2 of index search, specifically for Vue frontend usage
	 * 
	 * @param searchFilters all necessary information needed for search
	 * @return string of search response
	 */
	@Override
	public SearchResponse indexSearchV2(SearchFilters searchFilters)
	{
		int maxSearchResults = MAX_SEARCH_RESULTS;
		if (searchFilters.getPageSize() < maxSearchResults) {
			maxSearchResults = searchFilters.getPageSize();
		}

		searchFilters.setQuery("*"+searchFilters.getQuery()+"*");

		BoolQueryBuilder esQuery = getSearchQuery(searchFilters, null);

		FieldSortBuilder sort = new FieldSortBuilder(searchFilters.getSortField())
				.order(OpenStorefrontConstant.SORT_ASCENDING.equals(searchFilters.getSortOrder()) ? SortOrder.ASC
						: SortOrder.DESC);

		// Get all categories from search result
		TermsAggregationBuilder categoryAggregationBuilder = AggregationBuilders
				.terms("by_category")
				.field("componentType.keyword")
				.size(MAX_SEARCH_RESULTS);

		// Get all tags from search result
		TermsAggregationBuilder tagAggregationBuilder = AggregationBuilders
				.terms("by_tag")
				.field("tags.text.keyword")
				.size(MAX_SEARCH_RESULTS);

		// Get all organizations from search result
		TermsAggregationBuilder orgAggregationBuilder = AggregationBuilders
				.terms("by_organization")
				.field("organization.keyword")
				.size(MAX_SEARCH_RESULTS);

		
		String [] include = new String[]{"attributes"};

		TopHitsAggregationBuilder topHitsAggregationBuilder = AggregationBuilders
				.topHits("attribute")
				.fetchSource(include, null)
				.size(NUMBER_INNER_WINDOW_RETURN);

		// Gets list of all attribute labels from search as well as all the whole attribute object
		NestedAggregationBuilder nestedAttributeLabelAggregationBuilder = AggregationBuilders
				.nested("by_attribute_type", "attributes")
				.subAggregation(topHitsAggregationBuilder);
			

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
				.query(esQuery)
				.from(0)
				.from((searchFilters.getPage() -1) * searchFilters.getPageSize())
				.size(maxSearchResults)
				.sort(sort)
				.aggregation(categoryAggregationBuilder)
				.aggregation(tagAggregationBuilder)
				.aggregation(orgAggregationBuilder)
				.aggregation(nestedAttributeLabelAggregationBuilder);

		SearchRequest searchRequest = new SearchRequest(INDEX).source(searchSourceBuilder);

		SearchResponse response;
		try (ElasticSearchClient client = singleton.getClient()) {
			checkSearchIndexCreation();
			response = client.getInstance().search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, null, ex);
			response = new SearchResponse();
		}
		return response;
	}

	/**
	 * Function for backwards compatibility to old version of search
	 * for building the query for the search
	 * 
	 * @param query the query string from search bar
	 * @return a BoolQueryBuilder to create the search request from
	 */
	public BoolQueryBuilder getSearchQuery(String query){
		SearchFilters searchFilters = new SearchFilters();
		searchFilters.setQuery(query);
		return getSearchQuery(searchFilters, null);
	}

	/**
	 * Function to build query for search
	 * 
	 * @param searchFilters all info necessary for creating the search request
	 * @param searchOptions currently not used but will be used in a future implementation
	 * @return a BoolQueryBuilder to create the search request from
	 */
	public BoolQueryBuilder getSearchQuery(SearchFilters searchFilters, SearchOptions searchOptions){

		if(searchOptions == null){
			searchOptions = service.getSearchService().getUserSearchOptions();
			if (searchOptions == null) {
				searchOptions = service.getSearchService().getGlobalSearchOptions();
			}
		}
		if (searchOptions == null) {
			searchOptions = new SearchOptions();
			searchOptions.setDefaultSearchOptions();
		}

		BoolQueryBuilder esQuery = QueryBuilders.boolQuery();

		if (searchOptions.areAllOptionsOff()) {
			return esQuery;
		}

		String query = searchFilters.getQuery();
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

		// Check For Remaining Query Items
		if (queryString.length() > 0) {

			String actualQuery;
			String allUpperQuery;
			String allLowerQuery;
			String properCaseQuery;

			// This is done because searching doesn't properly deal with the hyphens.
			if (isHyphenatedWithoutSpaces(queryString.toString())) {
				// Create custom queries
				allUpperQuery = createSubstringOfQuery(queryString.toString().toUpperCase());
				allLowerQuery = createSubstringOfQuery(queryString.toString().toLowerCase());
				properCaseQuery = createSubstringOfQuery(toProperCase(queryString.toString()));
				actualQuery = createSubstringOfQuery(queryString.toString());

			} else {
				// Create custom queries
				allUpperQuery = queryString.toString().toUpperCase();
				allLowerQuery = queryString.toString().toLowerCase();
				properCaseQuery = toProperCase(queryString.toString());
				actualQuery = queryString.toString();
			}

			if (Convert.toBoolean(searchOptions.getCanUseOrganizationsInSearch())) {
				// Custom query for entry organization
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_ORGANIZATION, allUpperQuery));
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_ORGANIZATION, allLowerQuery));
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_ORGANIZATION, properCaseQuery));
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_ORGANIZATION, actualQuery));
			}

			if (Convert.toBoolean(searchOptions.getCanUseNameInSearch())) {
				// Custom query for entry name
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_NAME, allUpperQuery));
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_NAME, allLowerQuery));
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_NAME, properCaseQuery));
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_NAME, actualQuery));

				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_NAME, allUpperQuery));
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_NAME, allLowerQuery));
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_NAME, properCaseQuery));
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_NAME, actualQuery));
			}

			if (Convert.toBoolean(searchOptions.getCanUseDescriptionInSearch())) {
				// Custom query for description
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_DESCRIPTION, actualQuery));
			}

			if (Convert.toBoolean(searchOptions.getCanUseTagsInSearch())) {
				// Custom query for Tags
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_TAGS, allUpperQuery));
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_TAGS, allLowerQuery));
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_TAGS, properCaseQuery));
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_TAGS, actualQuery));

				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_TAGS, allUpperQuery));
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_TAGS, allLowerQuery));
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_TAGS, properCaseQuery));
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_TAGS, actualQuery));
			}

			if (Convert.toBoolean(searchOptions.getCanUseAttributesInSearch())) {
				// Custom query for Attributes
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_ATTRIBUTES, allUpperQuery));
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_ATTRIBUTES, allLowerQuery));
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_ATTRIBUTES, properCaseQuery));
				esQuery.should(QueryBuilders.wildcardQuery(ComponentSearchView.FIELD_ATTRIBUTES, actualQuery));

				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_ATTRIBUTES, allUpperQuery));
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_ATTRIBUTES, allLowerQuery));
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_ATTRIBUTES, properCaseQuery));
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_ATTRIBUTES, actualQuery));
			}
		}

		// Loop Through Search Phrases
		for (String phrase : searchPhrases) {

			if (Convert.toBoolean(searchOptions.getCanUseOrganizationsInSearch())) {
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_ORGANIZATION, phrase));
			}

			if (Convert.toBoolean(searchOptions.getCanUseNameInSearch())) {
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_NAME, phrase));
			}

			if (Convert.toBoolean(searchOptions.getCanUseDescriptionInSearch())) {
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_DESCRIPTION, phrase.toLowerCase()));
			}

			if (Convert.toBoolean(searchOptions.getCanUseTagsInSearch())) {
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_TAGS, phrase));
			}

			if (Convert.toBoolean(searchOptions.getCanUseAttributesInSearch())) {
				esQuery.should(QueryBuilders.matchPhraseQuery(ComponentSearchView.FIELD_ATTRIBUTES, phrase));
			}
		}

		BoolQueryBuilder boolQueryBuilderOrganization = QueryBuilders.boolQuery();
		if(searchFilters.getOrganization() != null && searchFilters.getOrganization() != ""){
			boolQueryBuilderOrganization.must(QueryBuilders.matchPhraseQuery("organization", searchFilters.getOrganization()));
		}

		BoolQueryBuilder boolQueryBuilderComponentTypes = QueryBuilders.boolQuery();
		if(searchFilters.getComponentTypes() != null){
			if(!searchFilters.getComponentTypes().isEmpty()){
				for(String type : searchFilters.getComponentTypes()){
					boolQueryBuilderComponentTypes.should(QueryBuilders.matchPhraseQuery("componentType", type));
					if(searchFilters.getIncludeChildren()){
						boolQueryBuilderComponentTypes.should(QueryBuilders.matchPhraseQuery("componentTypeNestedModel.componentType.componentType", type));
					}
				}
			}
		}

		BoolQueryBuilder boolQueryBuilderAttributes = QueryBuilders.boolQuery();
		if(searchFilters.getAttributes() != null){
			if(!searchFilters.getAttributes().isEmpty()){
				for(AttributeSearchType type : searchFilters.getAttributeSearchType()){
					QueryBuilder boolQueryBuilderAttributeTypes = QueryBuilders.boolQuery()
								.must(QueryBuilders.matchQuery("attributes.type", type.getType()))
								.must(QueryBuilders.matchQuery("attributes.code", type.getCode()));
					NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attributes", boolQueryBuilderAttributeTypes, ScoreMode.Avg);
					boolQueryBuilderAttributes.must(nestedQueryBuilder);
				}
			}
		}

		BoolQueryBuilder boolQueryBuilderTags = QueryBuilders.boolQuery();
		if (searchFilters.getTags() != null) {
			if (!searchFilters.getTags().isEmpty()) {
				for (String tag : searchFilters.getTags()) {
					boolQueryBuilderTags.should(QueryBuilders.matchPhraseQuery("tags.text", tag));
				}
			}
		}

		BoolQueryBuilder finalQuery = QueryBuilders.boolQuery();

		if(boolQueryBuilderOrganization.hasClauses()){
			finalQuery.must(boolQueryBuilderOrganization);
		}

		if(boolQueryBuilderComponentTypes.hasClauses()){
			finalQuery.must(boolQueryBuilderComponentTypes);
		}

		if(boolQueryBuilderTags.hasClauses()){
			finalQuery.must(boolQueryBuilderTags);
		}

		if(boolQueryBuilderAttributes.hasClauses()){
			finalQuery.must(boolQueryBuilderAttributes);
		}

		finalQuery.must(esQuery);
		
		return finalQuery;
	} 

	/**
	 * doIndexSearch with no additionalFieldsToReturn
	 * 
	 * @param query the string from user
	 * @param FilterQueryParams any additional query filters
	 * @return IndexSearchResult result from elasticsearch
	 */
	@Override
	public IndexSearchResult doIndexSearch(String query, FilterQueryParams filter)
	{
		return doIndexSearch(query, filter, null);
	}

	/**
	 * Function for basic search
	 * 
	 * @param query the string from search bar
	 * @param filter fields to filter on
	 * @param additionalFieldsToReturn other fields to return from search
	 */
	@Override
	public IndexSearchResult doIndexSearch(String query, FilterQueryParams filter, String[] additionalFieldsToReturn)
	{

		IndexSearchResult indexSearchResult = new IndexSearchResult();

		int maxSearchResults = MAX_SEARCH_RESULTS;
		if (filter.getMax() < maxSearchResults) {
			maxSearchResults = filter.getMax();
		}

		BoolQueryBuilder esQuery = getSearchQuery(query);

		FieldSortBuilder sort = new FieldSortBuilder(filter.getSortField())
				// .unmappedType("String") // currently the only fields we are searching/sorting
				// on are strings
				.order(OpenStorefrontConstant.SORT_ASCENDING.equals(filter.getSortOrder()) ? SortOrder.ASC
						: SortOrder.DESC);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(esQuery).from(filter.getOffset())
				.size(maxSearchResults).sort(sort);

		SearchRequest searchRequest = new SearchRequest(INDEX).source(searchSourceBuilder);

		try {
			performIndexSearch(searchRequest, indexSearchResult);
		} catch (IOException ex) {
			Logger.getLogger(ElasticSearchManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ElasticsearchStatusException e) {

			//	if a status exception occurs, it is likely the fielddata == false for description.
			//		Thus, update the mapping.
			updateMapping();

			//	try to perform the index search one more time...
			try {
				performIndexSearch(searchRequest, indexSearchResult);
			} catch (IOException | ElasticsearchStatusException ex) {
				throw new OpenStorefrontRuntimeException("Unable to perform search!", "check index [" + INDEX + "] mapping", ex);
			}
		}

		return indexSearchResult;
	}

	private void performIndexSearch(SearchRequest searchRequest, IndexSearchResult indexSearchResult) throws IOException, ElasticsearchStatusException
	{
		// perform search
		SearchResponse response;
		try (ElasticSearchClient client = singleton.getClient()) {
			response = client.getInstance().search(searchRequest, RequestOptions.DEFAULT);
		}

		indexSearchResult.setTotalResults(response.getHits().getTotalHits().value);
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
					indexSearchResult.getResultsList().add(ElasticsearchComponentModel.fromComponentSearchView(view));
				} else {
					LOG.log(Level.FINER, MessageFormat.format("Component is no longer approved and active.  Removing index.  {0}", view.getComponentId()));
					indexSearchResult.setTotalResults(indexSearchResult.getTotalResults() - 1);
					deleteById(view.getComponentId());
				}
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException("Unable to handle search result", "check index database", ex);
			}
		}
		indexSearchResult.applyDataFilter();
	}

	protected String toProperCase(String query)
	{
		String delimiters = "'-/*";

		StringBuilder searchQuery = new StringBuilder();
		boolean capNext = true;
		for (char c : query.toCharArray()) {

			if (capNext && Character.isLetter(c)) {
				c = Character.toUpperCase(c);
				searchQuery.append(c);
				capNext = (delimiters.indexOf((int) c) >= 0);
			} else {
				searchQuery.append(c);
				capNext = (delimiters.indexOf((int) c) >= 0);
			}
		}
		return searchQuery.toString();
	}

	protected boolean isHyphenatedWithoutSpaces(String query)
	{
		if (query.indexOf('-') >= 0 && query.charAt(0) != '"') {

			for (int i = 1; i < query.length() - 1; i++) {
				if (query.charAt(i) == '-' && query.charAt(i - 1) != ' ' && query.charAt(i + 1) != ' ') {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	protected String createSubstringOfQuery(String query)
	{
		int indexOfHyphen = query.indexOf('-');

		return query.substring(0, indexOfHyphen);
	}

	@Override
	public List<SearchSuggestion> searchSuggestions(String query, int maxResult, String componentType)
	{
		List<SearchSuggestion> searchSuggestions = new ArrayList<>();

		FilterQueryParams filter = FilterQueryParams.defaultFilter();
		filter.setSortField(ComponentSearchView.FIELD_NAME);

		//ignore case
		query = "*" + query.toLowerCase() + "*";

		//query everything we can
		String extraFields[] = {
			ElasticsearchComponentModel.FIELD_NAME,
			ElasticsearchComponentModel.FIELD_ORGANIZATION,
			ElasticsearchComponentModel.FIELD_DESCRIPTION
		};
		IndexSearchResult indexSearchResult = doIndexSearch(query, filter, extraFields);

		if (StringUtils.isNotBlank(componentType)) {
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

		if (StringUtils.isBlank(query)) {
			query = "";
		}

		//apply weight to items
		String queryNoWild = query.replace("*", "").toLowerCase();
		for (ElasticsearchComponentModel model : indexSearchResult.getResultsList()) {
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
		indexSearchResult.getResultsList().sort((ElasticsearchComponentModel o1, ElasticsearchComponentModel o2) -> Integer.compare(o2.getSearchWeight(), o1.getSearchWeight()));

		//window
		List<ElasticsearchComponentModel> topItems = indexSearchResult.getResultsList().stream().limit(maxResult).collect(Collectors.toList());

		for (ElasticsearchComponentModel model : topItems) {

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

	/**
	 * Index add a list of components to elasticsearch
	 * @param components a list of components
	 */
	@Override
	public void index(List<Component> components)
	{
		Objects.requireNonNull(components);

		if (!components.isEmpty()) {
			ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();
			BulkRequest bulkRequest = new BulkRequest();

			//pull reviews and map
			ComponentReview componentReview = new ComponentReview();
			componentReview.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			List<ComponentReview> componentReviews = componentReview.findByExample();
			Map<String, List<ComponentReview>> reviewMap = componentReviews.stream().collect(Collectors.groupingBy(ComponentReview::getComponentId));

			for (Component component : components) {

				// fetch all component attributes
				ComponentAttribute componentAttribute = new ComponentAttribute();
				componentAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
				componentAttribute.setComponentId(component.getComponentId());
				List<ComponentAttribute> componentAttributes = componentAttribute.findByExample();

				// fetch all component attributes
				ComponentTag componentTag = new ComponentTag();
				componentTag.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
				componentTag.setComponentId(component.getComponentId());
				List<ComponentTag> componentTags = componentTag.findByExample();

				componentReviews = reviewMap.getOrDefault(component.getComponentId(), new ArrayList<>());
				component.setDescription(StringProcessor.stripHtml(component.getDescription()));
				component.setDescription(StringProcessor.ellipseString(component.getDescription(), MAX_DESCRIPTION_INDEX_SIZE));
				ComponentSearchView componentSearchView = ComponentSearchView.toView(component, componentAttributes, componentReviews, componentTags);
				try {
					bulkRequest.add(new IndexRequest(INDEX)
							.id(componentSearchView.getComponentId())
							.source(objectMapper.writeValueAsString(componentSearchView), XContentType.JSON));
				} catch (JsonProcessingException ex) {
					LOG.log(Level.SEVERE, null, ex);
				}
			}

			executeIndexRequest(bulkRequest);
		}
	}

	@Override
	public UpdateResponse updateSingleComponent(String componentId){
		Objects.requireNonNull(componentId, "Requires Component ID");

		Component component = service.getPersistenceService().findById(Component.class, componentId);

		return updateSingleComponent(component);
	}

	@Override
	public UpdateResponse updateSingleComponent(Component component){
		Objects.requireNonNull(component, "Requires Component");

		UpdateResponse updateResponse = new UpdateResponse();
		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();

		if(component.getApprovalState() == ApprovalStatus.APPROVED){
			ComponentSearchView componentSearchView = ComponentSearchView.toView(component);
			
			try (ElasticSearchClient client = singleton.getClient()) {

				UpdateRequest updateRequest = new UpdateRequest(INDEX, component.getComponentId());
				updateRequest.doc(objectMapper.writeValueAsString(componentSearchView), XContentType.JSON);
				updateResponse = client.getInstance().update(updateRequest, RequestOptions.DEFAULT);
				
			} catch(JsonProcessingException ex){
				LOG.log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				LOG.log(Level.SEVERE, null, ex);
			}
		}
		return updateResponse;
	}

	private void executeIndexRequest(BulkRequest bulkRequest)
	{
		BulkResponse bulkResponse;
		try (ElasticSearchClient client = singleton.getClient()) {
			bulkResponse = client.getInstance().bulk(bulkRequest, RequestOptions.DEFAULT);

			if (bulkResponse.hasFailures()) {
				bulkResponse.forEach(response -> {
					if (StringUtils.isNotBlank(response.getFailureMessage())) {
						LOG.log(Level.WARNING, MessageFormat.format("A component failed to index: {0}", response.getFailureMessage()));
					}
				});
			} else {
				LOG.log(Level.INFO, "Index components successfully");
			}
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void indexFullComponents(List<ComponentAll> componentAlls)
	{
		Objects.requireNonNull(componentAlls);

		if (!componentAlls.isEmpty()) {
			ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();
			BulkRequest bulkRequest = new BulkRequest();
			for (ComponentAll componentAll : componentAlls) {

				Component component = componentAll.getComponent();

				List<ComponentReview> reviews = componentAll.getReviews().stream()
						.map(r -> r.getComponentReview())
						.collect(Collectors.toList());

				//convert to search result object
				component.setDescription(StringProcessor.stripHtml(component.getDescription()));
				component.setDescription(StringProcessor.ellipseString(component.getDescription(), MAX_DESCRIPTION_INDEX_SIZE));
				ComponentSearchView componentSearchView = ComponentSearchView.toView(
						component,
						componentAll.getAttributes(),
						reviews,
						componentAll.getTags()
				);
				try {
					bulkRequest.add(new IndexRequest(INDEX)
							.id(componentSearchView.getComponentId())
							.source(objectMapper.writeValueAsString(componentSearchView), XContentType.JSON));
				} catch (JsonProcessingException ex) {
					LOG.log(Level.SEVERE, null, ex);
				}
			}
			executeIndexRequest(bulkRequest);
		}
	}

	/**
	 * Delete a single item from the index using an id. 
	 * 
	 * @param id: the unique identifier for the component
	 */
	@Override
	public void deleteById(String id)
	{
		try (ElasticSearchClient client = justGetClient();) {

			GetIndexRequest indexRequest = new GetIndexRequest(INDEX);
			boolean exists = client.getInstance().indices().exists(indexRequest, RequestOptions.DEFAULT);
			if (exists) {
				DeleteRequest deleteRequest = new DeleteRequest(INDEX, id);
				DeleteResponse response;
				response = client.getInstance().delete(deleteRequest, RequestOptions.DEFAULT);
				LOG.log(Level.FINER, MessageFormat.format("Found Record to delete: {0}", response.getId()));
			}
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Delete then index and then recreate it. All information in the index is gone. 
	 */
	@Override
	public void deleteAll()
	{
		try (ElasticSearchClient client = justGetClient();) {
			DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(INDEX);
			deleteByQueryRequest.setQuery(QueryBuilders.matchAllQuery());

			BulkByScrollResponse response = client.getInstance().deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
			LOG.log(Level.INFO, "Deletion of index: " + response.getStatus().toString());

		} catch (ElasticsearchException e) {
			LOG.log(Level.SEVERE, "Unable to connect to elasticsearch", e);
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Load all active and approved components into elasticsearch
	 */
	@Override
	public void saveAll()
	{
		Component component = new Component();
		component.setActiveStatus(Component.ACTIVE_STATUS);
		component.setApprovalState(ApprovalStatus.APPROVED);
		List<Component> components = component.findByExample();

		index(components);
	}

	/**
	 * Delete the index, repopulate the index, and update the mapping for the index.
	 */
	@Override
	public void resetIndexer()
	{
		checkSearchIndexCreation();
		deleteAll();
		updateElasticsearchSettings();
		updateMapping();
		updateMappingAttributes();
		saveAll();
	}

	/**
	 * Update the mapping of the index
	 */
	private void updateMapping()
	{
		// Update description field to use fielddata=true
		//	Here, we must update all types
		try (ElasticSearchClient client = singleton.getClient()) {

			List<String> fieldsToUpdate = Arrays.asList(
					ComponentSearchView.FIELD_NAME,
					ComponentSearchView.FIELD_DESCRIPTION
			);

			for (String field : fieldsToUpdate) {

				XContentBuilder source = JsonXContent
						.contentBuilder()
						.startObject()
						.startObject("properties")
						.startObject(field)
						.field("type", "text")
						.startObject("fields")
						.startObject("keyword")
						.field("type", "keyword")
						.endObject()
						.endObject()
						.field("fielddata", true)
						.endObject()
						.endObject()
						.endObject();

				try{
					PutMappingRequest putMappingRequest = new PutMappingRequest(INDEX);
					putMappingRequest.source(source);

					AcknowledgedResponse putMappingResponse = client.getInstance().indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
					LOG.log(Level.INFO, "Updated Mapping of " + field + " : " + Boolean.toString(putMappingResponse.isAcknowledged()));
				} catch (IOException ex){
					LOG.log(Level.SEVERE, null, ex);
				} 
			}

		} catch (IOException ex) {
			LOG.log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Updates the settings of the elasticsearch index
	 * @return Boolean if settings were updated
	*/
	public Boolean updateElasticsearchSettings() {
		try (ElasticSearchClient client = justGetClient();) {
			UpdateSettingsRequest request = new UpdateSettingsRequest(INDEX);
			String settingKey = INDEX_INNER_WINDOW;
			int settingValue = NUMBER_INNER_WINDOW_RETURN;
			Settings settings =
					Settings.builder()
					.put(settingKey, settingValue)
					.build();

			request.settings(settings);
			AcknowledgedResponse updateSettingsResponse = client.getInstance().indices().putSettings(request, RequestOptions.DEFAULT);
			LOG.log(Level.INFO, "Updated settings: " + Boolean.toString(updateSettingsResponse.isAcknowledged()));
			return updateSettingsResponse.isAcknowledged();
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Unable to connect to elasticsearch", e);
		} catch (OpenStorefrontRuntimeException e){
			LOG.log(Level.SEVERE, "Unable to connect to elasticsearch", e);
		}
		return false;
	}

	/**
	 * Updates the mapping of the index. Specifically for attributes, non-nested -> nested
	 */
	public void updateMappingAttributes(){
		try (ElasticSearchClient client = singleton.getClient()) {

				String source = 
				"{\n" +
				"  \"properties\": {\n" +
				"    \"attributes\": {\n" +
				"      \"type\": \"nested\"\n" +
				"    }\n" +
				"  }\n" +
				"}";

				try{
					PutMappingRequest putMappingRequest = new PutMappingRequest(INDEX);
					putMappingRequest.source(source, XContentType.JSON);

					AcknowledgedResponse putMappingResponse = client.getInstance().indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
					LOG.log(Level.INFO, "Updated Mapping of attributes: " + Boolean.toString(putMappingResponse.isAcknowledged()));
				} catch (IOException ex){
					LOG.log(Level.SEVERE, null, ex);
				} 
			}
	}

	/**
	 * The test for deleteById()
	 */
	public void deleteByIdTest(){
		SearchResponse searchResponse = getAll();
		long firstHits = searchResponse.getHits().getTotalHits().value;
		LOG.log(Level.INFO, "GetAll() returned " + firstHits + " Hits.");
		String idToDelete = searchResponse.getHits().getAt(1).getId();
		deleteById(idToDelete);
		searchResponse = getAll();
		long secondHits = searchResponse.getHits().getTotalHits().value;
		LOG.log(Level.INFO, "GetAll() returned " + secondHits + " Hits.");
		if(firstHits - 1 == secondHits){
			LOG.log(Level.INFO, "Delete By Id worked.");
		}
	}

	/**
	 * Get every object in the index.
	 * @return SearchResponse object with all objects
	 */
	public SearchResponse getAll(){
		SearchResponse searchResponse = new SearchResponse();
		try (ElasticSearchClient client = singleton.getClient()) {
			SearchRequest searchRequest = new SearchRequest(INDEX); 
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
			searchSourceBuilder.query(QueryBuilders.matchAllQuery()); 
			searchSourceBuilder.size(MAX_SEARCH_RESULTS);
			searchRequest.source(searchSourceBuilder);

			searchResponse = client.getInstance().search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, null, ex);
		}
		return searchResponse;
	}

	/**
	 * Delete the index
	 */
	public void deleteIndex(){
		try (ElasticSearchClient client = singleton.getClient()) {
			DeleteIndexRequest request = new DeleteIndexRequest(INDEX);

			AcknowledgedResponse deleteIndexResponse = client.getInstance().indices().delete(request, RequestOptions.DEFAULT);
			LOG.log(Level.INFO, deleteIndexResponse.toString());
			
		} catch (ElasticsearchException exception) {
			LOG.log(Level.SEVERE, null, exception);
		} catch (IOException ex){
			LOG.log(Level.SEVERE, null, ex);
		} 
	}
}
