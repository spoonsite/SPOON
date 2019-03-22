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
import com.mongodb.client.MongoCollection;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.api.query.ComplexFieldStack;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOptionBuilder;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.service.manager.MongoDBManager;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.conversions.Bson;

/**
 *
 * @author dshurtleff
 */
public class MongoQueryUtil
{

	private static final String PARAM_NAME_SEPARATOR = ".";
	private static final int SORT_ASCENDING = 1;
	private static final int SORT_DESCENDING = -1;

	private UserContext userContext;
	private MongoDBManager dbManager;

	public MongoQueryUtil(UserContext userContext, MongoDBManager dbManager)
	{
		this.userContext = userContext;
		this.dbManager = dbManager;
	}

	//generate example query filter / document
	public <T extends BaseEntity> Bson generateFilters(QueryByExample<T> queryRequest)
	{
		Map<String, Object> exampleMap = generateFieldMap(queryRequest.getExample(), new ComplexFieldStack(), queryRequest.getExampleOption(), queryRequest.getFieldOptions());

		return null;
	}

	public <T extends BaseEntity> Bson generateSortFilter(QueryByExample<T> queryRequest)
	{
		Map<String, Object> exampleMap = generateFieldMap(queryRequest.getOrderBy());

		for (String key : exampleMap.keySet()) {
			int sortDirection = SORT_ASCENDING;
			if (OpenStorefrontConstant.SORT_DESCENDING.equals(queryRequest.getSortDirection())) {
				sortDirection = SORT_DESCENDING;
			}
			exampleMap.put(key, sortDirection);
		}

		return new BasicDBObject(exampleMap);
	}

	//find field name; for complexy <parent_field>.<child field>
	public Map<String, Object> generateFieldMap(Object example)
	{
		return generateFieldMap(example, new ComplexFieldStack(PARAM_NAME_SEPARATOR), new GenerateStatementOptionBuilder().build(), new HashMap<>());
	}

	public <T> Map<String, Object> generateFieldMap(T example, ComplexFieldStack complexFieldStack, GenerateStatementOption generateStatementOption, Map<String, GenerateStatementOption> fieldOptions)
	{
		Map<String, Object> parameterMap = new HashMap<>();
		try {
			List<Field> fields = ReflectionUtil.getAllFields(example.getClass());
			for (Field field : fields) {

				if ("class".equalsIgnoreCase(field.getName()) == false) {
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

	public <T extends BaseEntity> Bson componentRestrictionFilter()
	{

//		StringBuilder query = new StringBuilder();
//		query.append(standardRestrictionFilter(userContext));
//
//		if (userContext != null) {
//
//			if (query.length() > 0) {
//				query.append(" and ");
//			}
//
//			query.append("(");
//
//			if (userContext.allowUnspecifiedDataSources()) {
//				query.append(" " + FIELD_DATA_SOURCE + " IS NULL ");
//			} else {
//				query.append(" " + FIELD_DATA_SOURCE + " IS NOT NULL ");
//			}
//
//			Set<String> datasources = userContext.dataSources();
//			if (!datasources.isEmpty()) {
//				if (userContext.allowUnspecifiedDataSources()) {
//					query.append(" OR ");
//				} else {
//					query.append(" AND ");
//				}
//
//				query.append(FIELD_DATA_SOURCE + " IN [");
//
//				List<String> dataSourceList = new ArrayList<>();
//				datasources.forEach((dataSource) -> {
//					dataSourceList.add("'" + dataSource + "'");
//				});
//				query.append(String.join(",", dataSourceList));
//				query.append("]");
//			}
//			query.append(")");
//		}
		return null;
	}

	public <T extends BaseEntity> Bson standardRestrictionFilter()
	{
//		StringBuilder query = new StringBuilder();
//
//		if (userContext != null) {
//			query.append("(");
//
//			if (userContext.allowUnspecifiedDataSensitivty()) {
//				query.append(" " + FIELD_DATA_SENSITIVITY + " IS NULL ");
//			} else {
//				query.append(" " + FIELD_DATA_SENSITIVITY + " IS NOT NULL ");
//			}
//
//			Set<String> dataSensitivity = userContext.dataSensitivity();
//			if (!dataSensitivity.isEmpty()) {
//				if (userContext.allowUnspecifiedDataSensitivty()) {
//					query.append(" OR ");
//				} else {
//					query.append(" AND ");
//				}
//
//				query.append(FIELD_DATA_SENSITIVITY + " IN [");
//
//				List<String> dataSensitivityList = new ArrayList<>();
//				dataSensitivity.forEach((dsCode) -> {
//					dataSensitivityList.add("'" + dsCode + "'");
//				});
//				query.append(String.join(",", dataSensitivityList));
//				query.append("]");
//			}
//			query.append(")");
//		}
//
//		return query.toString();
		return null;
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
