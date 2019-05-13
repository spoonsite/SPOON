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
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.api.query.ComplexFieldStack;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOptionBuilder;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.api.query.WhereClause;
import edu.usu.sdl.openstorefront.core.api.query.WhereClauseGroup;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.service.manager.MongoDBManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.bson.conversions.Bson;

/**
 *
 * @author dshurtleff
 */
public class MongoQueryUtil
{

	private static final Logger LOG = Logger.getLogger(MongoQueryUtil.class.getName());

	private static final String JAVA_CLASS = "class";
	private static final String PARAM_NAME_SEPARATOR = ".";
	private static final String LIKE_QUERY_CHARACTER = "%";
	public static final int MONGO_SORT_ASCENDING = 1;
	public static final int MONGO_SORT_DESCENDING = -1;

	private MongoDBManager dbManager;

	public MongoQueryUtil(MongoDBManager dbManager)
	{
		this.dbManager = dbManager;
	}

	//generate example query filter / document
	public <T extends BaseEntity> Bson generateFilters(QueryByExample<T> queryRequest)
	{
		Map<String, Object> exampleMap = generateFieldMap(queryRequest.getExample(), new ComplexFieldStack(), queryRequest.getExampleOption(), queryRequest.getFieldOptions());

		//pull out the field that have options on them handle them seperately
		Map<String, Object> specialFieldHandleMap = new HashMap<>();
		for (String fieldName : exampleMap.keySet()) {
			if (queryRequest.getFieldOptions().containsKey(fieldName)) {
				specialFieldHandleMap.put(fieldName, exampleMap.get(fieldName));
			}
		}
		//remove all fields with special handling
		for (String fieldName : specialFieldHandleMap.keySet()) {
			exampleMap.remove(fieldName);
		}

		Bson query = new BasicDBObject(exampleMap);

		query = handleSpecialFields(queryRequest, query, specialFieldHandleMap);
		query = handleInExample(queryRequest, query);
		query = handleLikeExample(queryRequest, query);
		query = handleSpecialWhereClause(queryRequest, query);

//		//$and/$or/$nor must be a nonempty array  (See if this is need...it seems most cases the query can adjusted)
//		BsonDocument andOrFilterDoc = query.toBsonDocument(BsonDocument.class, MongoClientSettings.getDefaultCodecRegistry());
//		if (andOrFilterDoc.get("$and") != null) {
//			if (andOrFilterDoc.get("$and").asArray().isEmpty()) {
//				//send back an empty doc
//				query = new BasicDBObject();
//			}
//		} else if (andOrFilterDoc.get("$or") != null) {
//			if (andOrFilterDoc.get("$or").asArray().isEmpty()) {
//				//send back an empty doc
//				query = new BasicDBObject();
//			}
//		}
		return query;
	}

	@SuppressWarnings("unchecked")
	private <T extends BaseEntity> Bson handleSpecialFields(QueryByExample<T> queryRequest, Bson query, Map<String, Object> specialFieldHandleMap)
	{
		if (specialFieldHandleMap.isEmpty()) {
			return query;
		}

		Bson specialFilters = new BasicDBObject();

		//handle operators (all AND)
		for (String fieldName : specialFieldHandleMap.keySet()) {

			GenerateStatementOption option = queryRequest.getFieldOptions().get(fieldName);

			Bson internalFilter = new BasicDBObject();
			switch (option.getOperation()) {
				case GenerateStatementOption.OPERATION_EQUALS:
					//handled cases where just case insensitivity is needed.
					if (GenerateStatementOption.METHOD_LOWER_CASE.equals(option.getMethod())
							|| GenerateStatementOption.METHOD_UPPER_CASE.equals(option.getMethod())) {
						internalFilter = Filters.regex(fieldName, Pattern.compile(convertSQLLikeCharacterToRegex(specialFieldHandleMap.get(fieldName).toString()), Pattern.CASE_INSENSITIVE));
					} else {
						internalFilter = Filters.eq(fieldName, specialFieldHandleMap.get(fieldName));
					}

					break;
				case GenerateStatementOption.OPERATION_NOT_EQUALS:
					internalFilter = Filters.ne(fieldName, specialFieldHandleMap.get(fieldName));
					break;
				case GenerateStatementOption.OPERATION_LIKE:
					if (GenerateStatementOption.METHOD_LOWER_CASE.equals(option.getMethod())
							|| GenerateStatementOption.METHOD_UPPER_CASE.equals(option.getMethod())) {
						internalFilter = Filters.regex(fieldName, Pattern.compile(convertSQLLikeCharacterToRegex(specialFieldHandleMap.get(fieldName).toString()), Pattern.CASE_INSENSITIVE));
					} else {
						internalFilter = Filters.regex(fieldName, convertSQLLikeCharacterToRegex(specialFieldHandleMap.get(fieldName).toString()));
					}
					break;
				case GenerateStatementOption.OPERATION_NOT_NULL:
					internalFilter = Filters.ne(fieldName, null);
					break;
				case GenerateStatementOption.OPERATION_NULL:
					internalFilter = Filters.eq(fieldName, null);
					break;
				case GenerateStatementOption.OPERATION_LESS_THAN_EQUAL:
					internalFilter = Filters.lte(fieldName, specialFieldHandleMap.get(fieldName));
					break;
				case GenerateStatementOption.OPERATION_LESS_THAN:
					internalFilter = Filters.lt(fieldName, specialFieldHandleMap.get(fieldName));
					break;
				case GenerateStatementOption.OPERATION_GREATER_THAN:
					internalFilter = Filters.gt(fieldName, specialFieldHandleMap.get(fieldName));
					break;
				case GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL:
					internalFilter = Filters.gte(fieldName, specialFieldHandleMap.get(fieldName));
					break;
				case GenerateStatementOption.OPERATION_IN:
					internalFilter = Filters.in(fieldName, (Iterable) specialFieldHandleMap.get(fieldName));
					break;
				case GenerateStatementOption.OPERATION_NOT_IN:
					internalFilter = Filters.nin(fieldName, (Iterable) specialFieldHandleMap.get(fieldName));
					break;
			}

			specialFilters = Filters.and(specialFilters, internalFilter);
		}
		return specialFilters;
	}

	private <T extends BaseEntity> Bson handleSpecialWhereClause(QueryByExample<T> queryRequest, Bson query)
	{
		for (WhereClause whereClause : queryRequest.getExtraWhereCauses()) {
			if (whereClause instanceof SpecialOperatorModel) {
				SpecialOperatorModel specialOperatorModel = (SpecialOperatorModel) whereClause;
				Map<String, Object> specialMap = generateFieldMap(specialOperatorModel.getExample(), new ComplexFieldStack(), specialOperatorModel.getGenerateStatementOption(), new HashMap<>());

				if (!specialMap.isEmpty()) {
					query = handleSpecialOperator(query, specialOperatorModel);
				}
			} else if (whereClause instanceof WhereClauseGroup) {
				WhereClauseGroup clauseGroup = (WhereClauseGroup) whereClause;

				for (Object operatorModel : clauseGroup.getExtraWhereClause()) {
					SpecialOperatorModel specialOperatorModel = (SpecialOperatorModel) operatorModel;
					Map<String, Object> specialMap = generateFieldMap(specialOperatorModel.getExample(), new ComplexFieldStack(), specialOperatorModel.getGenerateStatementOption(), new HashMap<>());

					if (!specialMap.isEmpty()) {
						if (GenerateStatementOption.CONDITION_OR.equals(clauseGroup.getStatementOption().getCondition())) {
							query = Filters.or(query, handleSpecialOperator(query, specialOperatorModel));
						} else {
							query = Filters.and(query, handleSpecialOperator(query, specialOperatorModel));
						}
					}
				}
			}
		}
		return query;
	}

	private <T extends BaseEntity> Bson handleLikeExample(QueryByExample<T> queryRequest, Bson query)
	{
		if (queryRequest.getLikeExample() != null) {
			Map<String, Object> likeMap = generateFieldMap(queryRequest.getLikeExample(), new ComplexFieldStack(), queryRequest.getLikeExampleOption(), new HashMap<>());

			for (String fieldName : likeMap.keySet()) {
				String value = likeMap.get(fieldName).toString();
				value = convertSQLLikeCharacterToRegex(value);

				Pattern pattern;
				if (GenerateStatementOption.METHOD_LOWER_CASE.equals(queryRequest.getLikeExampleOption().getMethod())
						|| GenerateStatementOption.METHOD_UPPER_CASE.equals(queryRequest.getLikeExampleOption().getMethod())) {
					pattern = Pattern.compile(value, Pattern.CASE_INSENSITIVE);
				} else {
					pattern = Pattern.compile(value);
				}

				if (GenerateStatementOption.CONDITION_OR.equals(queryRequest.getLikeExampleOption().getCondition())) {
					query = Filters.or(query, Filters.regex(fieldName, pattern));
				} else {
					query = Filters.and(query, Filters.regex(fieldName, pattern));
				}
			}
		}
		return query;
	}

	@SuppressWarnings("unchecked")
	private <T extends BaseEntity> Bson handleInExample(QueryByExample<T> queryRequest, Bson query)
	{
		if (queryRequest.getInExample() != null) {
			Map<String, Object> inMap = generateFieldMap(queryRequest.getInExample(), new ComplexFieldStack(), queryRequest.getInExampleOption(), new HashMap<>());

			for (String fieldName : inMap.keySet()) {
				if (inMap.get(fieldName) instanceof Iterable) {
					if (GenerateStatementOption.CONDITION_OR.equals(queryRequest.getInExampleOption().getCondition())) {
						query = Filters.or(query, Filters.in(fieldName, (Iterable) inMap.get(fieldName)));
					} else {
						query = Filters.and(query, Filters.in(fieldName, (Iterable) inMap.get(fieldName)));
					}
				} else {
					LOG.log(Level.WARNING, () -> "IN query with empty set of values; Query may not work as expect. (Set log level to finest to throw error) FieldName: " + fieldName);

					if (LOG.isLoggable(Level.FINEST)) {
						throw new OpenStorefrontRuntimeException("IN query with empty set of value", "See Trace to know what cause it");
					}
				}
			}
		}
		return query;
	}

	public String convertSQLLikeCharacterToRegex(String value)
	{
		//deterime starts with (a% = ^a), endwith (%a = a$) and contains (a)
		if (value.startsWith(LIKE_QUERY_CHARACTER) && value.endsWith(LIKE_QUERY_CHARACTER)) {
			value = value.replace(LIKE_QUERY_CHARACTER, "");
		} else if (value.startsWith(LIKE_QUERY_CHARACTER)) {
			//end
			value = value.replace(LIKE_QUERY_CHARACTER, "");
			value = value + "$";
		} else if (value.endsWith(LIKE_QUERY_CHARACTER)) {
			//start
			value = value.replace(LIKE_QUERY_CHARACTER, "");
			value = "^" + value;
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	private Bson handleSpecialOperator(Bson query, SpecialOperatorModel specialOperatorModel)
	{
		Map<String, Object> specialMap = generateFieldMap(specialOperatorModel.getExample(), new ComplexFieldStack(), specialOperatorModel.getGenerateStatementOption(), new HashMap<>());

		//typically there only one field set in this cases of mulitple set option on all
		Bson specialFilters = new BasicDBObject();

		//handle operators (all AND)
		for (String key : specialMap.keySet()) {

			Bson internalFilter = new BasicDBObject();
			switch (specialOperatorModel.getGenerateStatementOption().getOperation()) {
				case GenerateStatementOption.OPERATION_EQUALS:
					internalFilter = Filters.eq(key, specialMap.get(key));
					break;
				case GenerateStatementOption.OPERATION_NOT_EQUALS:
					internalFilter = Filters.ne(key, specialMap.get(key));
					break;
				case GenerateStatementOption.OPERATION_LIKE:
					if (GenerateStatementOption.METHOD_LOWER_CASE.equals(specialOperatorModel.getGenerateStatementOption().getMethod())
							|| GenerateStatementOption.METHOD_UPPER_CASE.equals(specialOperatorModel.getGenerateStatementOption().getMethod())) {
						internalFilter = Filters.regex(key, Pattern.compile(convertSQLLikeCharacterToRegex(specialMap.get(key).toString()), Pattern.CASE_INSENSITIVE));
					} else {
						internalFilter = Filters.regex(key, convertSQLLikeCharacterToRegex(specialMap.get(key).toString()));
					}
					break;
				case GenerateStatementOption.OPERATION_NOT_NULL:
					internalFilter = Filters.ne(key, null);
					break;
				case GenerateStatementOption.OPERATION_NULL:
					internalFilter = Filters.eq(key, null);
					break;
				case GenerateStatementOption.OPERATION_LESS_THAN_EQUAL:
					internalFilter = Filters.lte(key, specialMap.get(key));
					break;
				case GenerateStatementOption.OPERATION_LESS_THAN:
					internalFilter = Filters.lt(key, specialMap.get(key));
					break;
				case GenerateStatementOption.OPERATION_GREATER_THAN:
					internalFilter = Filters.gt(key, specialMap.get(key));
					break;
				case GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL:
					internalFilter = Filters.gte(key, specialMap.get(key));
					break;
				case GenerateStatementOption.OPERATION_IN:
					internalFilter = Filters.in(key, (Iterable) specialMap.get(key));
					break;
				case GenerateStatementOption.OPERATION_NOT_IN:
					internalFilter = Filters.nin(key, (Iterable) specialMap.get(key));
					break;
			}
			specialFilters = Filters.and(specialFilters, internalFilter);
		}

		//condition with current query
		if (GenerateStatementOption.CONDITION_OR.equals(specialOperatorModel.getGenerateStatementOption().getCondition())) {
			query = Filters.or(query, specialFilters);
		} else {
			query = Filters.and(query, specialFilters);
		}
		return query;
	}

	public <T extends BaseEntity> Bson generateSortFilter(QueryByExample<T> queryRequest)
	{
		Map<String, Object> exampleMap = generateFieldMap(queryRequest.getOrderBy());

		for (String key : exampleMap.keySet()) {
			int sortDirection = MONGO_SORT_ASCENDING;
			if (OpenStorefrontConstant.SORT_DESCENDING.equals(queryRequest.getSortDirection())) {
				sortDirection = MONGO_SORT_DESCENDING;
			}
			exampleMap.put(key, sortDirection);
		}

		return new BasicDBObject(exampleMap);
	}

	public <T extends BaseEntity> Bson generateGroupByFilter(QueryByExample<T> queryRequest)
	{
		Map<String, Object> exampleMap = generateFieldMap(queryRequest.getGroupBy());

		BasicDBObject groupQuery = new BasicDBObject();

		BasicDBObject groupFields = new BasicDBObject();
		if (!exampleMap.keySet().isEmpty()) {
			DBObject fields = new BasicDBObject();
			for (String key : exampleMap.keySet()) {
				fields.put(key, "$" + key);
			}
			groupFields = new BasicDBObject("_id", fields);
		} else if (exampleMap.keySet().size() == 1) {
			groupFields = new BasicDBObject("_id", "$" + exampleMap.keySet().stream().findFirst().get());
		}

		//add all fields as that is the expected behavior of the application
		//activeStatus: { $first: "$activeStatus" },
		//clientIp: { $first: "$clientIp" },
		//complex should be pull by this as expected
		if (!exampleMap.keySet().isEmpty()) {
			List<Field> fields = ReflectionUtil.getAllFields(queryRequest.getExample().getClass());
			for (Field field : fields) {
				if (JAVA_CLASS.equalsIgnoreCase(field.getName()) == false) {
					BasicDBObject fieldProjection = new BasicDBObject();
					fieldProjection.put("$first", "$" + field.getName());
					groupFields.put(field.getName(), fieldProjection);
				}
			}
			groupQuery.put("$group", groupFields);
		}

		return groupQuery;
	}

	public Bson generateUpdateSet(Object example)
	{
		Map<String, Object> exampleSetMap = generateFieldMap(example);

		Bson updateQuery;
		List<Bson> updateSetFields = new ArrayList<>();
		for (String key : exampleSetMap.keySet()) {
			updateSetFields.add(Updates.set(key, exampleSetMap.get(key)));
		}
		updateQuery = Updates.combine(updateSetFields);
		return updateQuery;
	}

	//find field name; for complexy <parent_field>.<child field>
	public Map<String, Object> generateFieldMap(Object example)
	{
		return generateFieldMap(example, new ComplexFieldStack(PARAM_NAME_SEPARATOR), new GenerateStatementOptionBuilder().build(), new HashMap<>());
	}

	public <T> Map<String, Object> generateFieldMap(T example, ComplexFieldStack complexFieldStack, GenerateStatementOption generateStatementOption, Map<String, GenerateStatementOption> fieldOptions)
	{
		Map<String, Object> parameterMap = new HashMap<>();
		if (example == null) {
			return parameterMap;
		}
		try {
			List<Field> fields = ReflectionUtil.getAllFields(example.getClass());
			for (Field field : fields) {

				if (JAVA_CLASS.equalsIgnoreCase(field.getName()) == false) {
					field.setAccessible(true);

					Object value = field.get(example);
					if (value != null) {
						GenerateStatementOption fieldOperation = generateStatementOption;
						if (fieldOptions != null && fieldOptions.containsKey(field.toString())) {
							fieldOperation = fieldOptions.get(field.toString());
						}

						if (ReflectionUtil.isComplexClass(value.getClass())) {
							complexFieldStack.getFieldStack().push(field.getName());
							parameterMap.putAll(generateFieldMap(value, complexFieldStack, generateStatementOption, fieldOptions));
							complexFieldStack.getFieldStack().pop();
						} else if ((GenerateStatementOption.OPERATION_IN.equals(fieldOperation.getOperation())
								|| GenerateStatementOption.OPERATION_NOT_IN.equals(fieldOperation.getOperation()))
								&& !generateStatementOption.getParameterValues().isEmpty()) {
							String fieldName = complexFieldStack.getQueryFieldName() + field.getName();
							parameterMap.put(fieldName, generateStatementOption.getParameterValues());
						} else {
							String fieldName = complexFieldStack.getQueryFieldName() + field.getName();
							parameterMap.put(fieldName, value);
						}
					}
				}
			}
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
		return parameterMap;
	}

	public <T extends BaseEntity> Bson componentRestrictionFilter(UserContext userContext)
	{
		Bson query = standardRestrictionFilter(userContext);

		if (userContext != null && !userContext.isSystemUser()) {

			if (userContext.allowUnspecifiedDataSources()) {
				query = Filters.and(query, Filters.eq(Component.FIELD_DATA_SOURCE, null));
			} else {
				query = Filters.and(query, Filters.ne(Component.FIELD_DATA_SOURCE, null));
			}

			Set<String> datasources = userContext.dataSources();
			if (!datasources.isEmpty()) {
				if (userContext.allowUnspecifiedDataSources()) {
					query = Filters.or(query, Filters.in(Component.FIELD_DATA_SOURCE, datasources));
				} else {
					query = Filters.and(query, Filters.in(Component.FIELD_DATA_SOURCE, datasources));
				}
			}
		}
		return query;
	}

	public <T extends BaseEntity> Bson standardRestrictionFilter(UserContext userContext)
	{
		Bson query = new BasicDBObject();

		if (userContext != null && !userContext.isSystemUser()) {
			if (userContext.allowUnspecifiedDataSensitivty()) {
				query = Filters.eq(StandardEntity.FIELD_DATA_SENSITIVITY, null);
			} else {
				query = Filters.ne(StandardEntity.FIELD_DATA_SENSITIVITY, null);
			}

			Set<String> dataSensitivity = userContext.dataSensitivity();
			if (!dataSensitivity.isEmpty()) {
				if (userContext.allowUnspecifiedDataSensitivty()) {
					query = Filters.or(query, Filters.in(StandardEntity.FIELD_DATA_SENSITIVITY, dataSensitivity));
				} else {
					query = Filters.and(query, Filters.in(StandardEntity.FIELD_DATA_SENSITIVITY, dataSensitivity));
				}
			}
		}
		return query;
	}

	public <T extends BaseEntity> Bson constructPKFilter(T entity)
	{
		return constructPKFilter(entity.getClass(), EntityUtil.getPKFieldObjectValue(entity));
	}

	public <T> Bson constructPKFilter(Class<T> entity, Object id)
	{
		Map<String, Object> pkFields = EntityUtil.findIdField(entity, id);
		return new BasicDBObject(pkFields);
	}

	public <T> MongoCollection<T> getCollectionForEntity(T entity)
	{
		return getCollectionForEntity(entity.getClass());
	}

	@SuppressWarnings("unchecked")
	public <T> MongoCollection<T> getCollectionForEntity(Class entityClass)
	{
		return dbManager.getConnection().getCollection(entityClass.getSimpleName(), entityClass);
	}

}
