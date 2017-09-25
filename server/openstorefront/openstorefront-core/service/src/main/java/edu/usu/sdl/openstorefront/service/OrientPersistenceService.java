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

import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.api.query.ComplexFieldStack;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOptionBuilder;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.QueryType;
import static edu.usu.sdl.openstorefront.core.api.query.QueryType.COUNT;
import static edu.usu.sdl.openstorefront.core.api.query.QueryType.COUNT_DISTINCT;
import static edu.usu.sdl.openstorefront.core.api.query.QueryType.SELECT;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.api.query.WhereClauseGroup;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.util.proxy.Proxy;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles all interaction with the database
 *
 * @author dshurtleff
 */
public class OrientPersistenceService
		implements PersistenceService
{

	private static final Logger LOG = Logger.getLogger(OrientPersistenceService.class.getName());
	private static final String PARAM_NAME_SEPARATOR = "1";

	private OObjectDatabaseTx transaction;

	public OrientPersistenceService()
	{
		//Nothing to set
	}

	@Override
	public String generateId()
	{
		return UUID.randomUUID().toString();
	}

	public OObjectDatabaseTx getConnection()
	{
		OObjectDatabaseTx db;
		if (transaction == null) {
			db = DBManager.getConnection();
		} else {
			db = transaction;
		}
		db.setLazyLoading(false);
		return db;
	}

	public void closeConnection(OObjectDatabaseTx db)
	{
		if (transaction == null) {
			db.close();
		}
	}

	@Override
	public void begin()
	{
		if (transaction == null) {
			transaction = DBManager.getConnection();
			transaction.begin();
		} else {
			throw new OpenStorefrontRuntimeException("Already in a Transaction", "Commit or rollback transaction action before beginning a new one.");
		}
	}

	@Override
	public void commit()
	{
		if (transaction == null) {
			throw new OpenStorefrontRuntimeException("Not in a transaction", "Begin a new one.");
		} else {
			transaction.commit();
		}
	}

	@Override
	public void rollback()
	{
		if (transaction == null) {
			throw new OpenStorefrontRuntimeException("Not in a transaction", "Begin a new one.");
		} else {
			transaction.rollback();
		}
	}

	@Override
	public void endTransaction()
	{
		if (transaction != null) {
			transaction.close();
			transaction = null;
		}
	}

	@Override
	public boolean isTransactionActive()
	{
		boolean active = false;
		if (transaction != null) {
			active = transaction.getTransaction().isActive();
		}
		return active;
	}

	@Override
	public boolean isManaged(BaseEntity baseEntity)
	{
		boolean attached = false;
		OObjectDatabaseTx database = getConnection();
		try {
			attached = database.isManaged(baseEntity);
		} finally {
			closeConnection(database);
		}
		return attached;
	}

	@Override
	public boolean isProxy(BaseEntity baseEntity)
	{
		boolean proxied = false;
		if (baseEntity instanceof Proxy) {
			proxied = true;
		}
		return proxied;
	}

	@Override
	public <T extends StandardEntity> T setStatusOnEntity(Class<T> entity, Object id, String activeStatus)
	{
		T found = findById(entity, id);
		if (found != null) {
			found.setUpdateUser(SecurityUtil.getCurrentUserName());
			found.populateBaseUpdateFields();
			found.setActiveStatus(activeStatus);
			persist(found);
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find entity to set status on.", "Check Input: " + entity.getSimpleName() + " id: " + id);
		}
		return found;
	}

	/**
	 * This only works on managed objects
	 *
	 * @param <T>
	 * @param entityClass
	 * @param primaryKey (DB RID not our Entity PK)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey)
	{
		final ORecordId rid;

		OObjectDatabaseTx database = getConnection();
		try {
			if (primaryKey instanceof ORecordId) {
				rid = (ORecordId) primaryKey;
			} else if (primaryKey instanceof String) {
				rid = new ORecordId((String) primaryKey);
			} else {
				throw new IllegalArgumentException("PrimaryKey '" + primaryKey + "' type (" + primaryKey.getClass() + ") is not supported");
			}

			return (T) database.load(rid);
		} finally {
			closeConnection(database);
		}
	}

	@Override
	public <T> T findById(Class<T> entity, Object id)
	{
		if (id == null) {
			LOG.log(Level.FINEST, "Id is null so return null");
			return null;
		}

		OObjectDatabaseTx db = getConnection();
		T returnEntity = null;
		try {
			if (checkPkObject(db, entity, id)) {
				Map<String, Object> pkFields = findIdField(entity, id);

				if (pkFields.isEmpty()) {
					throw new OpenStorefrontRuntimeException("Unable to find PK field", "Mark the Primary Key field (@PK) on the entity: " + entity.getName());
				}

				StringBuilder whereClause = new StringBuilder();
				Map<String, Object> parameters = new HashMap<>();
				for (String fieldName : pkFields.keySet()) {
					parameters.put(fieldName.replace(".", PARAM_NAME_SEPARATOR) + "Param", pkFields.get(fieldName));
					whereClause.append(" ").append(fieldName).append(" = :").append(fieldName.replace(".", PARAM_NAME_SEPARATOR)).append("Param").append(" AND");
				}
				String whereClauseString = whereClause.substring(0, whereClause.length() - 3);

				List<T> results = db.query(new OSQLSynchQuery<>("select * from " + entity.getSimpleName() + " where " + whereClauseString), parameters);

				if (!results.isEmpty()) {
					returnEntity = results.get(0);
				}
			} else {
				throw new OpenStorefrontRuntimeException("Id passed in doesn't match the PK type of the entity", "Make sure you are passing the right PK");
			}
		} finally {
			closeConnection(db);
		}

		return returnEntity;
	}

	/**
	 * Check to make sure the id is the same type as the pk. If not it's a
	 * programming error.
	 *
	 * @param entityClass
	 * @param id required
	 * @return
	 */
	private boolean checkPkObject(OObjectDatabaseTx db, Class entityClass, Object id)
	{
		boolean pkValid = true;
		Objects.requireNonNull(id, "Id must not be null");

		if (entityClass.getSuperclass() != null) {
			pkValid = checkPkObject(db, entityClass.getSuperclass(), id);
		}
		if (pkValid) {
			for (Field field : entityClass.getDeclaredFields()) {
				PK idAnnotation = field.getAnnotation(PK.class);
				if (idAnnotation != null) {
					if (id.getClass().getName().equals(field.getType().getName()) == false) {
						//Manage PK  has likely been pull by the DB and passed back in ...let it through
						if ((id instanceof Proxy) == false) {
							pkValid = false;
						}
					}
				}
			}
		}

		return pkValid;
	}

	private Map<String, Object> findIdField(Class entityClass, Object id)
	{
		Map<String, Object> fieldValueMap = new HashMap<>();

		//Start at the root (The first Id found wins ...there should only be one)
		if (entityClass.getSuperclass() != null) {
			fieldValueMap = findIdField(entityClass.getSuperclass(), id);
		}
		if (fieldValueMap.isEmpty()) {
			for (Field field : entityClass.getDeclaredFields()) {
				PK idAnnotation = field.getAnnotation(PK.class);
				if (idAnnotation != null) {
					if (ReflectionUtil.isComplexClass(field.getType())) {
						//PK class should only be one level deep
						for (Field pkField : field.getType().getDeclaredFields()) {
							try {
								if (Modifier.isStatic(pkField.getModifiers()) == false
										&& Modifier.isFinal(pkField.getModifiers()) == false) {
									Method method = id.getClass().getMethod("get" + StringUtils.capitalize(pkField.getName()), (Class<?>[]) null);
									Object returnObj = method.invoke(id, (Object[]) null);
									fieldValueMap.put(field.getName() + "." + pkField.getName(), returnObj);
								}
							} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
								throw new OpenStorefrontRuntimeException(ex);
							}
						}
					} else {
						fieldValueMap.put(field.getName(), id);
						break;
					}
				}
			}
		}
		return fieldValueMap;
	}

	@Override
	public long countClass(Class entityClass)
	{
		OObjectDatabaseTx db = getConnection();
		long count = 0;
		try {
			count = db.countClass(entityClass);
		} finally {
			closeConnection(db);
		}
		return count;
	}

	@Override
	public int deleteByExample(BaseEntity example)
	{
		return deleteByExample(new QueryByExample(example));
	}

	@Override
	public int deleteByExample(QueryByExample queryByExample)
	{
		int deleteCount = 0;
		StringBuilder queryString = new StringBuilder();

		queryString.append("delete from ").append(queryByExample.getExample().getClass().getSimpleName());

		Map<String, Object> mappedParams = new HashMap<>();
		String whereClause = generateWhereClause(queryByExample.getExample(), new ComplexFieldStack(), queryByExample.getExampleOption(), queryByExample.getFieldOptions());
		if (StringUtils.isNotBlank(whereClause)) {
			queryString.append(" where ").append(whereClause);
			mappedParams.putAll(mapParameters(queryByExample.getExample(), new ComplexFieldStack(PARAM_NAME_SEPARATOR), queryByExample.getExampleOption(), queryByExample.getFieldOptions()));
		}

		SimpleEntry<String, Map<String, Object>> extraWhere = processExtraWhereClauses(queryByExample);
		if (StringUtils.isNotBlank(extraWhere.getKey())) {
			appendToWhere(queryString, extraWhere.getKey());
			mappedParams.putAll(extraWhere.getValue());
		}

		if (queryByExample.getAdditionalWhere() != null) {
			appendToWhere(queryString, queryByExample.getAdditionalWhere());
		}
		mappedParams.putAll(queryByExample.getExtraParamMapping());

		OObjectDatabaseTx db = getConnection();
		try {
			deleteCount = db.command(new OCommandSQL(queryString.toString())).execute(mappedParams);
		} finally {
			closeConnection(db);
		}

		return deleteCount;
	}

	@Override
	public int deleteByQuery(Class entityClass, String whereClause, Map<String, Object> queryParams)
	{
		int deleteCount = 0;
		StringBuilder queryString = new StringBuilder();
		queryString.append("delete from ").append(entityClass.getSimpleName());
		if (StringUtils.isNotBlank(whereClause)) {
			queryString.append(" where ").append(whereClause);
		}

		OObjectDatabaseTx db = getConnection();
		try {
			deleteCount = db.command(new OCommandSQL(queryString.toString())).execute(queryParams);
		} finally {
			closeConnection(db);
		}

		return deleteCount;
	}

	@Override
	public <T> int updateByExample(Class<T> entityClass, T exampleSet, T exampleWhere)
	{
		int updateCount = 0;
		StringBuilder queryString = new StringBuilder();
		queryString.append("update ").append(entityClass.getSimpleName());

		GenerateStatementOption generateStatementOption = new GenerateStatementOptionBuilder().build();
		generateStatementOption.setCondition(GenerateStatementOption.CONDITION_COMMA);
		generateStatementOption.setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_SET);
		String setQuery = generateWhereClause(exampleSet, new ComplexFieldStack(), generateStatementOption, new HashMap<>());
		if (StringUtils.isNotBlank(setQuery)) {
			queryString.append(" set ").append(setQuery);
		} else {
			throw new OpenStorefrontRuntimeException("Update query requires a SET clause.", "Make sure to set the example set");
		}

		String whereClause = generateWhereClause(exampleWhere);
		if (StringUtils.isNotBlank(whereClause)) {
			queryString.append(" where ").append(generateWhereClause(exampleWhere));
		}

		Map<String, Object> queryParams = new HashMap<>();
		queryParams.putAll(mapParameters(exampleSet, new ComplexFieldStack(), generateStatementOption, new HashMap<>()));
		queryParams.putAll(mapParameters(exampleWhere));

		OObjectDatabaseTx db = getConnection();
		try {
			updateCount = db.command(new OCommandSQL(queryString.toString())).execute(queryParams);
		} finally {
			closeConnection(db);
		}
		return updateCount;
	}

	@Override
	public <T> int runDbCommand(String query, Map<String, Object> queryParams)
	{
		int updateCount = 0;
		OObjectDatabaseTx db = getConnection();
		try {
			updateCount = db.command(new OCommandSQL(query)).execute(queryParams);
		} finally {
			closeConnection(db);
		}
		return updateCount;
	}

	@Override
	public long countByExample(BaseEntity example)
	{
		QueryByExample queryByExample = new QueryByExample(example);
		queryByExample.setQueryType(QueryType.COUNT);
		return countByExample(new QueryByExample(example));
	}

	@Override
	public long countByExample(QueryByExample queryByExample)
	{
		long count = 0;
		StringBuilder queryString = new StringBuilder();
		if (QueryType.SELECT.equals(queryByExample.getQueryType())) {
			queryByExample.setQueryType(QueryType.COUNT);
		}
		switch (queryByExample.getQueryType()) {
			case COUNT:
				queryString.append("select count(*) ");
				break;
			case COUNT_DISTINCT:
				queryString.append("select count(distinct(").append(queryByExample.getDistinctField()).append(") ");
				break;
			default:
				throw new OpenStorefrontRuntimeException("Query Type unsupported: " + queryByExample.getQueryType(), "Only supports Count types");
		}
		queryString.append("from ").append(queryByExample.getExample().getClass().getSimpleName());

		Map<String, Object> mappedParams = new HashMap<>();
		String whereClause = generateWhereClause(queryByExample.getExample(), new ComplexFieldStack(), queryByExample.getExampleOption(), queryByExample.getFieldOptions());
		if (StringUtils.isNotBlank(whereClause)) {
			queryString.append(" where ").append(whereClause);
			mappedParams.putAll(mapParameters(queryByExample.getExample(), new ComplexFieldStack(PARAM_NAME_SEPARATOR), queryByExample.getExampleOption(), queryByExample.getFieldOptions()));
		}

		SimpleEntry<String, Map<String, Object>> extraWhere = processExtraWhereClauses(queryByExample);
		if (StringUtils.isNotBlank(extraWhere.getKey())) {
			appendToWhere(queryString, extraWhere.getKey());
			mappedParams.putAll(extraWhere.getValue());
		}

		if (queryByExample.getAdditionalWhere() != null) {
			appendToWhere(queryString, queryByExample.getAdditionalWhere());
		}
		mappedParams.putAll(queryByExample.getExtraParamMapping());

		OObjectDatabaseTx db = getConnection();
		try {
			List<ODocument> documents = db.command(new OCommandSQL(queryString.toString())).execute(mappedParams);
			if (documents.isEmpty() == false) {
				count = documents.get(0).field("count");
			}
		} finally {
			closeConnection(db);
		}
		return count;
	}

	public List<ODocument> dbCommandQuery(String query, Map<String, Object> params)
	{
		List<ODocument> documents = new ArrayList<>();
		OObjectDatabaseTx db = getConnection();
		try {
			documents = db.command(new OCommandSQL(query)).execute(params);
		} finally {
			closeConnection(db);
		}
		return documents;
	}

	/**
	 * This will run the example with the default options
	 *
	 * @param <T>
	 * @param exampleClass
	 * @param baseEntity
	 * @return
	 */
	@Override
	public <T> List<T> queryByExample(BaseEntity baseEntity)
	{
		return queryByExample(new QueryByExample(baseEntity));
	}

	@Override
	public <T> List<T> queryByExample(QueryByExample queryByExample)
	{
		SimpleEntry<String, Map<String, Object>> query = generateQuery(queryByExample);
		List<T> results = query(query.getKey(), query.getValue(), queryByExample.isReturnNonProxied());
		return results;
	}

	public SimpleEntry<String, Map<String, Object>> generateQuery(QueryByExample queryByExample)
	{

		StringBuilder queryString = new StringBuilder();

		switch (queryByExample.getQueryType()) {
			case SELECT:
				queryString.append("select  ");
				break;
			default:
				throw new OpenStorefrontRuntimeException("Query Type unsupported: " + queryByExample.getQueryType(), "Only supports select");
		}
		queryString.append(" from ").append(queryByExample.getExample().getClass().getSimpleName());

		Map<String, Object> mappedParams = new HashMap<>();
		String whereClause = generateWhereClause(queryByExample.getExample(), new ComplexFieldStack(), queryByExample.getExampleOption(), queryByExample.getFieldOptions());
		if (StringUtils.isNotBlank(whereClause)) {
			queryString.append(" where ").append(whereClause);
			mappedParams.putAll(mapParameters(queryByExample.getExample(), new ComplexFieldStack(PARAM_NAME_SEPARATOR), queryByExample.getExampleOption(), queryByExample.getFieldOptions()));
		}
		if (queryByExample.getLikeExample() != null) {
			String likeClause = generateWhereClause(queryByExample.getLikeExample(), new ComplexFieldStack(), queryByExample.getLikeExampleOption(), queryByExample.getFieldOptions());
			if (StringUtils.isNotBlank(likeClause)) {
				appendToWhere(queryString, likeClause);
				mappedParams.putAll(mapParameters(queryByExample.getLikeExample(), new ComplexFieldStack(PARAM_NAME_SEPARATOR), queryByExample.getLikeExampleOption(), queryByExample.getFieldOptions()));
			}
		}
		if (queryByExample.getInExample() != null) {
			String inClause = generateWhereClause(queryByExample.getInExample(), new ComplexFieldStack(), queryByExample.getInExampleOption(), queryByExample.getFieldOptions());
			if (StringUtils.isNotBlank(inClause)) {
				appendToWhere(queryString, inClause);
				mappedParams.putAll(mapParameters(queryByExample.getInExample(), new ComplexFieldStack(PARAM_NAME_SEPARATOR), queryByExample.getInExampleOption(), queryByExample.getFieldOptions()));
			}
		}
		SimpleEntry<String, Map<String, Object>> extraWhere = processExtraWhereClauses(queryByExample);
		if (StringUtils.isNotBlank(extraWhere.getKey())) {
			appendToWhere(queryString, extraWhere.getKey());
			mappedParams.putAll(extraWhere.getValue());
		}

		if (queryByExample.getAdditionalWhere() != null) {
			appendToWhere(queryString, queryByExample.getAdditionalWhere());
		}

		if (queryByExample.getGroupBy() != null) {
			String names = generateExampleNames(queryByExample.getGroupBy());
			if (StringUtils.isNotBlank(names)) {
				queryString.append(" group by ").append(names);
			}
		}
		if (queryByExample.getOrderBy() != null) {
			String names = generateExampleNames(queryByExample.getOrderBy());
			if (StringUtils.isNotBlank(names)) {
				queryString.append(" order by ").append(names).append(" ").append(queryByExample.getSortDirection());
			}
		}
		if (queryByExample.getFirstResult() != null && queryByExample.getFirstResult() > 0) {
			queryString.append(" SKIP ").append(queryByExample.getFirstResult());
		}
		if (queryByExample.getMaxResults() != null && queryByExample.getMaxResults() > 0) {
			queryString.append(" LIMIT ").append(queryByExample.getMaxResults());
		}
		if (queryByExample.getTimeout() != null) {
			queryString.append(" TIMEOUT ").append(queryByExample.getTimeout()).append(" ").append(queryByExample.getTimeoutStrategy());
		}
		if (queryByExample.isParallelQuery()) {
			queryString.append(" PARALLEL ");
		}
		mappedParams.putAll(queryByExample.getExtraParamMapping());
		return new SimpleEntry(queryString.toString(), mappedParams);
	}

	private SimpleEntry<String, Map<String, Object>> processExtraWhereClauses(QueryByExample queryByExample)
	{
		StringBuilder queryString = new StringBuilder();
		Map<String, Object> mappedParams = new HashMap<>();
		queryByExample.getExtraWhereCauses().forEach(item -> {
			if (item.getClass() == SpecialOperatorModel.class) {
				SimpleEntry<String, Map<String, Object>> extraWhere = ProcessSpecialOperator(queryByExample, (SpecialOperatorModel) item);
				if (StringUtils.isNotBlank(extraWhere.getKey())) {
					if (StringUtils.isNotBlank(queryString)) {
						queryString.append(" AND ");
					}
					queryString.append(extraWhere.getKey());
					mappedParams.putAll(extraWhere.getValue());
				}
			} else if (item.getClass() == WhereClauseGroup.class) {
				SimpleEntry<String, Map<String, Object>> extraWhere = ProcessWhereClauseGroup(queryByExample, (WhereClauseGroup) item);
				if (StringUtils.isNotBlank(extraWhere.getKey())) {
					if (StringUtils.isNotBlank(queryString)) {
						queryString.append(" AND ");
					}
					queryString.append(extraWhere.getKey());
					mappedParams.putAll(extraWhere.getValue());
				}
			} else {
				throw new OpenStorefrontRuntimeException("Where Clause unsupported: " + item.getClass(), "Only supports select");
			}
		});
		return new SimpleEntry(queryString.toString(), mappedParams);
	}

	private SimpleEntry<String, Map<String, Object>> ProcessSpecialOperator(QueryByExample queryByExample, SpecialOperatorModel special)
	{
		Map<String, Object> mappedParams = new HashMap<>();
		String extraWhere = generateWhereClause(special.getExample(), new ComplexFieldStack(), special.getGenerateStatementOption(), queryByExample.getFieldOptions());
		if (StringUtils.isNotBlank(extraWhere)) {
			mappedParams = mapParameters(special.getExample(), new ComplexFieldStack(), special.getGenerateStatementOption(), queryByExample.getFieldOptions());
		}
		return new SimpleEntry(extraWhere, mappedParams);
	}

	private SimpleEntry<String, Map<String, Object>> ProcessWhereClauseGroup(QueryByExample queryByExample, WhereClauseGroup group)
	{
		StringBuilder queryString = new StringBuilder();
		Map<String, Object> mappedParams = new HashMap<>();
		group.getExtraWhereClause().forEach(item -> {
			if (item.getClass() == SpecialOperatorModel.class) {
				SimpleEntry<String, Map<String, Object>> extraWhere = ProcessSpecialOperator(queryByExample, (SpecialOperatorModel) item);
				if (StringUtils.isNotBlank(extraWhere.getKey())) {
					if (queryString.length() > 0) {
						queryString.append(group.getStatementOption().getCondition());
					}
					queryString.append(extraWhere.getKey());
					mappedParams.putAll(extraWhere.getValue());
				}
			} else if (item.getClass() == WhereClauseGroup.class) {
				SimpleEntry<String, Map<String, Object>> extraWhere = ProcessWhereClauseGroup(queryByExample, (WhereClauseGroup) item);
				if (StringUtils.isNotBlank(extraWhere.getKey())) {
					if (queryString.length() > 0) {
						queryString.append(group.getStatementOption().getCondition());
					}
					queryString.append(extraWhere.getKey());
					mappedParams.putAll(extraWhere.getValue());
				}
			} else {
				throw new OpenStorefrontRuntimeException("Where Clause unsupported: " + item.getClass(), "Only supports select");
			}
		});
		if (queryString.length() > 0) {
			// wrap the group in ()
			queryString.insert(0, "(");
			queryString.append(" )");
		}
		return new SimpleEntry(queryString.toString(), mappedParams);
	}

	private void appendToWhere(StringBuilder queryString, String conditionClause)
	{
		if (queryString.indexOf(" where ") != -1) {
			queryString.append(" AND ");
		} else {
			queryString.append(" where ");
		}
		queryString.append(conditionClause);
	}

	private <T> String generateWhereClause(T example)
	{
		return generateWhereClause(example, new ComplexFieldStack(), new GenerateStatementOptionBuilder().build(), new HashMap<>());
	}

	private <T> String generateWhereClause(T example, ComplexFieldStack complexFieldStack, GenerateStatementOption generateStatementOption, Map<String, GenerateStatementOption> fieldOptions)
	{
		StringBuilder where = new StringBuilder();

		try {
			Map fieldMap = BeanUtils.describe(example);
			boolean addAnd = false;
			for (Object field : fieldMap.keySet()) {

				if ("class".equalsIgnoreCase(field.toString()) == false) {
					Object value = fieldMap.get(field);
					if (value != null) {
						GenerateStatementOption fieldOperation = generateStatementOption;
						if (fieldOptions != null && fieldOptions.containsKey(field.toString())) {
							fieldOperation = fieldOptions.get(field.toString());
						}

						Method method = example.getClass().getMethod("get" + StringUtils.capitalize(field.toString()), (Class<?>[]) null);
						Object returnObj = method.invoke(example, (Object[]) null);
						if (ReflectionUtil.isComplexClass(returnObj.getClass())) {
							complexFieldStack.getFieldStack().push(field.toString());
							if (addAnd) {
								where.append(fieldOperation.getCondition());
							} else {
								addAnd = true;
								where.append(" ");
							}

							where.append(generateWhereClause(returnObj, complexFieldStack, generateStatementOption, fieldOptions));
							complexFieldStack.getFieldStack().pop();
						} else {

							if (addAnd) {
								where.append(fieldOperation.getCondition());
							} else {
								addAnd = true;
								where.append(" ");
							}

							String fieldName = complexFieldStack.getQueryFieldName() + field.toString() + fieldOperation.getMethod();
							String fieldParamName = complexFieldStack.getQueryFieldName() + field.toString();
							where.append(fieldName)
									.append(" ").append(fieldOperation.getOperation());

							boolean addParameter = true;
							if (GenerateStatementOption.OPERATION_NULL.equals(fieldOperation.getOperation())
									|| GenerateStatementOption.OPERATION_NOT_NULL.equals(fieldOperation.getOperation())) {
								addParameter = false;
							}
							if (GenerateStatementOption.OPERATION_IN.equals(fieldOperation.getOperation())
									|| GenerateStatementOption.OPERATION_NOT_IN.equals(fieldOperation.getOperation())) {
								addParameter = false;
								where.append(" [ :")
										.append(fieldParamName.replace(".", PARAM_NAME_SEPARATOR))
										.append(fieldOperation.getParameterSuffix())
										.append(0);
								for (int i = 1; i < generateStatementOption.getParameterValues().size(); i++) {
									where.append(", :")
											.append(fieldParamName.replace(".", PARAM_NAME_SEPARATOR))
											.append(fieldOperation.getParameterSuffix())
											.append(i);
								}
								where.append(" ]");
							}

							if (addParameter) {
								where.append(" :")
										.append(fieldParamName.replace(".", PARAM_NAME_SEPARATOR))
										.append(fieldOperation.getParameterSuffix());
							}
						}
					}
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		return where.toString();
	}

	private <T> String generateExampleNames(T example)
	{
		return generateExampleNames(example, new ComplexFieldStack());
	}

	private <T> String generateExampleNames(T example, ComplexFieldStack complexFieldStack)
	{
		StringBuilder where = new StringBuilder();

		try {
			Map fieldMap = BeanUtils.describe(example);
			boolean addAnd = false;
			for (Object field : fieldMap.keySet()) {

				if ("class".equalsIgnoreCase(field.toString()) == false) {
					Object value = fieldMap.get(field);
					if (value != null) {

						Method method = example.getClass().getMethod("get" + StringUtils.capitalize(field.toString()), (Class<?>[]) null);
						Object returnObj = method.invoke(example, (Object[]) null);
						if (ReflectionUtil.isComplexClass(returnObj.getClass())) {
							complexFieldStack.getFieldStack().push(field.toString());
							if (addAnd) {
								where.append(",");
							} else {
								addAnd = true;
								where.append(" ");
							}

							where.append(generateExampleNames(returnObj, complexFieldStack));
							complexFieldStack.getFieldStack().pop();
						} else {
							if (addAnd) {
								where.append(",");
							} else {
								addAnd = true;
								where.append(" ");
							}
							String fieldName = complexFieldStack.getQueryFieldName() + field.toString();

							where.append(fieldName);
						}
					}
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		return where.toString();
	}

	private <T> Map<String, Object> mapParameters(T example)
	{
		return mapParameters(example, new ComplexFieldStack(PARAM_NAME_SEPARATOR), new GenerateStatementOptionBuilder().build(), new HashMap<>());
	}

	private <T> Map<String, Object> mapParameters(T example, ComplexFieldStack complexFieldStack, GenerateStatementOption generateStatementOption, Map<String, GenerateStatementOption> fieldOptions)
	{
		Map<String, Object> parameterMap = new HashMap<>();
		try {
			List<Field> fields = ReflectionUtil.getAllFields(example.getClass());
			for (Field field : fields) {

				if ("class".equalsIgnoreCase(field.getName()) == false) {
					field.setAccessible(true);
					//Note: this may not work for proxy object....they may need to be call through a get method
					Object value = field.get(example);
					if (value != null) {
						GenerateStatementOption fieldOperation = generateStatementOption;
						if (fieldOptions != null && fieldOptions.containsKey(field.toString())) {
							fieldOperation = fieldOptions.get(field.toString());
						}

						if (ReflectionUtil.isComplexClass(value.getClass())) {
							complexFieldStack.getFieldStack().push(field.getName());
							parameterMap.putAll(mapParameters(value, complexFieldStack, generateStatementOption, fieldOptions));
							complexFieldStack.getFieldStack().pop();
						} else if ((GenerateStatementOption.OPERATION_IN.equals(fieldOperation.getOperation())
									|| GenerateStatementOption.OPERATION_NOT_IN.equals(fieldOperation.getOperation()))
								&& !generateStatementOption.getParameterValues().isEmpty()) {
							String fieldName = complexFieldStack.getQueryFieldName() + field.getName();
							for (Integer i = 0; i < generateStatementOption.getParameterValues().size(); i++) {
								parameterMap.put(fieldName + fieldOperation.getParameterSuffix() + i.toString(), generateStatementOption.getParameterValues().get(i));
							}
						} else {
							String fieldName = complexFieldStack.getQueryFieldName() + field.getName();
							parameterMap.put(fieldName + fieldOperation.getParameterSuffix(), value);
						}
					}
				}
			}
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
		return parameterMap;
	}

	/**
	 * This just returns one result. Typically the query results in only one
	 * entity.
	 *
	 * @param <T>
	 * @param exampleClass
	 * @param baseEnity
	 * @return the entity or null if not found
	 */
	@Override
	public <T> T queryOneByExample(BaseEntity baseEnity)
	{
		return queryOneByExample(new QueryByExample(baseEnity));
	}

	@Override
	public <T> T queryOneByExample(QueryByExample queryByExample)
	{
		List<T> results = queryByExample(queryByExample);
		if (!results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * This will run the query and returns the Proxy Objects
	 *
	 * @param <T>
	 * @param query
	 * @param parameterMap
	 * @return
	 */
	@Override
	public <T> List<T> query(String query, Map<String, Object> parameterMap)
	{
		return query(query, parameterMap, false);
	}

	/**
	 * This will run the query and then unwrap if desired
	 *
	 * @param <T>
	 * @param query
	 * @param parameterMap
	 * @param unwrap
	 * @return
	 */
	@Override
	public <T> List<T> query(String query, Map<String, Object> parameterMap, boolean unwrap)
	{
		OObjectDatabaseTx db = getConnection();
		List<T> results = new ArrayList<>();
		try {
			if (LOG.isLoggable(Level.FINEST)) {
				LOG.log(Level.FINEST, query);
			}
			//look for empty collection
			if (parameterMap != null) {
				for (Object value : parameterMap.values()) {
					if (value != null && value instanceof Collection) {
						if (((Collection) value).isEmpty()) {
							throw new OpenStorefrontRuntimeException("Unable to complete query with a empty collection.", "Check query and parameter map");
						}
					}
				}
			}

			results = db.query(new OSQLSynchQuery<>(query), parameterMap);
			if (unwrap) {
				results = unwrapProxy(db, results);
			}
		} finally {
			closeConnection(db);
		}
		return results;
	}

	@Override
	public <T extends BaseEntity> T persist(T entity)
	{
		Objects.requireNonNull(entity, "Unable to persist a NULL entity.");

		OObjectDatabaseTx db = getConnection();
		T t = null;
		try {
			String pkValue = EntityUtil.getPKFieldValue(entity);
			if (pkValue == null) {
				if (EntityUtil.isPKFieldGenerated(entity)) {
					EntityUtil.updatePKFieldValue(entity, generateId());
				}
			}

			ValidationModel validationModel = new ValidationModel(entity);
			validationModel.setSantize(false);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (validationResult.valid()) {
				t = db.save(entity);
			} else {
				throw new OpenStorefrontRuntimeException(validationResult.toString(), "Check the data to make sure it conforms to the rules. Recored type: " + entity.getClass().getName());
			}
		} catch (Exception e) {
			throw new OpenStorefrontRuntimeException("Unable to save record. (See stacktrace cause) \n Field Values: \n" + StringProcessor.printObject(entity), e);
		} finally {
			closeConnection(db);
		}

		return t;
	}

	@Override
	public <T extends BaseEntity> T saveNonPkEntity(T entity)
	{
		return saveNonBaseEntity(entity);
	}

	/**
	 * This is used for non-base entity object. Keep in mind they still need to
	 * be registered with the DB.
	 *
	 * @param <T>
	 * @param entity
	 * @return
	 */
	@Override
	public <T> T saveNonBaseEntity(T entity)
	{
		Objects.requireNonNull(entity, "Unable to persist a NULL entity.");

		OObjectDatabaseTx db = getConnection();
		T t = null;
		try {
			ValidationModel validationModel = new ValidationModel(entity);
			validationModel.setSantize(false);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (validationResult.valid()) {
				t = db.save(entity);
			} else {
				throw new OpenStorefrontRuntimeException(validationResult.toString(), "Check the data to make sure it conforms to the rules. Recored type: " + entity.getClass().getName());
			}
		} catch (Exception e) {
			throw new OpenStorefrontRuntimeException("Unable to save record: " + StringProcessor.printObject(entity), e);
		} finally {
			closeConnection(db);
		}
		return t;
	}

	@Override
	public <T> void delete(T entity)
	{
		OObjectDatabaseTx db = getConnection();
		try {
			if (entity != null) {
				db.delete(entity);
			}
		} finally {
			closeConnection(db);
		}
	}

	/**
	 * Detaches and removes the proxy object
	 *
	 * @param <T>
	 * @param entity
	 * @return
	 */
	@Override
	public <T> T detach(T entity)
	{
		OObjectDatabaseTx db = getConnection();
		T nonProxy = null;
		try {
			nonProxy = db.detach(entity, true);
		} finally {
			closeConnection(db);
		}
		return nonProxy;
	}

	/**
	 * Detaches the whole object tree and removes the proxy object
	 *
	 * @param <T>
	 * @param entity
	 * @return
	 */
	@Override
	public <T> T deattachAll(T entity)
	{
		OObjectDatabaseTx db = getConnection();
		T nonProxy = null;
		try {
			nonProxy = db.detachAll(entity, true);
		} finally {
			closeConnection(db);
		}
		return nonProxy;
	}

	@Override
	public <T> List<T> unwrapProxy(List<T> data)
	{
		OObjectDatabaseTx db = getConnection();
		List<T> nonProxyData = null;
		try {
			nonProxyData = unwrapProxy(db, data);
		} finally {
			closeConnection(db);
		}
		return nonProxyData;
	}

	public <T> List<T> unwrapProxy(OObjectDatabaseTx db, List<T> data)
	{
		List<T> nonProxyList = new ArrayList<>();
		for (T dbproxy : data) {
			T nonProxied = db.detachAll(dbproxy, true);
			nonProxyList.add(nonProxied);
		}
		return nonProxyList;
	}

	@Override
	public <T> T unwrapProxyObject(T data)
	{
		OObjectDatabaseTx db = getConnection();
		T nonProxyData = null;
		try {
			nonProxyData = unwrapProxyObject(db, data);
		} finally {
			closeConnection(db);
		}
		return nonProxyData;
	}

	public <T> T unwrapProxyObject(OObjectDatabaseTx db, T data)
	{
		T nonProxy = db.detachAll(data, true);
		return nonProxy;
	}

}
