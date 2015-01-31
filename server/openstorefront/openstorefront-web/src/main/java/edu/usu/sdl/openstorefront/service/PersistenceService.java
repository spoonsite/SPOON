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
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.service.query.ComplexFieldStack;
import edu.usu.sdl.openstorefront.service.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.util.PK;
import edu.usu.sdl.openstorefront.util.ServiceUtil;
import edu.usu.sdl.openstorefront.util.StringProcessor;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
public class PersistenceService
{

	private static final Logger log = Logger.getLogger(PersistenceService.class.getName());
	private static final String PARAM_NAME_SEPARATOR = "1";

	private OObjectDatabaseTx transaction;

	public PersistenceService()
	{
	}

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

	public void begin()
	{
		if (transaction == null) {
			transaction = DBManager.getConnection();
			transaction.begin();
		} else {
			throw new OpenStorefrontRuntimeException("Already in a Transaction", "Commit or rollback transaction action before beginning a new one.");
		}
	}

	public void commit()
	{
		if (transaction == null) {
			throw new OpenStorefrontRuntimeException("Not in a transaction", "Begin a new one.");
		} else {
			transaction.commit();
		}
	}

	public void rollback()
	{
		if (transaction == null) {
			throw new OpenStorefrontRuntimeException("Not in a transaction", "Begin a new one.");
		} else {
			transaction.rollback();
		}
	}

	public void endTransaction()
	{
		if (transaction != null) {
			transaction.close();
			transaction = null;
		}
	}

	public boolean isTransactionActive()
	{
		boolean active = false;
		if (transaction != null) {
			active = transaction.getTransaction().isActive();
		}
		return active;
	}

	/**
	 * This only works on managed objects
	 *
	 * @param <T>
	 * @param entityClass
	 * @param primaryKey (DB RID not our Entity PK
	 * @return
	 */
	@SuppressWarnings("unchecked")
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

	public <T> T findById(Class<T> entity, Object id)
	{
		if (id == null) {
			log.log(Level.FINEST, "Id is null so return null");
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

		//Start at the root (The fist Id found wins ...there should only be one)
		if (entityClass.getSuperclass() != null) {
			fieldValueMap = findIdField(entityClass.getSuperclass(), id);
		}
		if (fieldValueMap.isEmpty()) {
			for (Field field : entityClass.getDeclaredFields()) {
				PK idAnnotation = field.getAnnotation(PK.class);
				if (idAnnotation != null) {
					if (ServiceUtil.isComplexClass(field.getType())) {
						//PK class should only be one level deep
						for (Field pkField : field.getType().getDeclaredFields()) {
							try {
//								Method pkMethod = id.getClass().getMethod("get" + StringUtils.capitalize(field.getName()), (Class<?>[]) null);
//								Object pkObj = pkMethod.invoke(id, (Object[]) null);
//
//								Method method = pkObj.getClass().getMethod("get" + StringUtils.capitalize(pkField.getName()), (Class<?>[]) null);
//								Object returnObj = method.invoke(pkObj, (Object[]) null);
								Method method = id.getClass().getMethod("get" + StringUtils.capitalize(pkField.getName()), (Class<?>[]) null);
								Object returnObj = method.invoke(id, (Object[]) null);
								fieldValueMap.put(field.getName() + "." + pkField.getName(), returnObj);
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

	public <T> int deleteByExample(T example)
	{
		int deleteCount = 0;
		StringBuilder queryString = new StringBuilder();

		queryString.append("delete from ").append(example.getClass().getSimpleName());

		String whereClause = generateWhereClause(example);
		if (StringUtils.isNotBlank(whereClause)) {
			queryString.append(" where ").append(whereClause);
		}

		OObjectDatabaseTx db = getConnection();
		try {
			deleteCount = db.command(new OCommandSQL(queryString.toString())).execute(mapParameters(example));
		} finally {
			closeConnection(db);
		}

		return deleteCount;
	}

	public int deleteByQuery(Class entityClass, String whereClause, Map<String, Object> queryParams)
	{
		int deleteCount = 0;
		StringBuilder queryString = new StringBuilder();
		queryString.append("delete from ").append(entityClass.getSimpleName());
		queryString.append(" where ").append(whereClause);

		OObjectDatabaseTx db = getConnection();
		try {
			deleteCount = db.command(new OCommandSQL(queryString.toString())).execute(queryParams);
		} finally {
			closeConnection(db);
		}

		return deleteCount;
	}

	public <T> int updateByExample(Class<T> entityClass, T exampleSet, T exampleWhere)
	{
		int updateCount = 0;
		StringBuilder queryString = new StringBuilder();
		queryString.append("update ").append(entityClass.getSimpleName());

		GenerateStatementOption generateStatementOption = new GenerateStatementOption();
		generateStatementOption.setCondition(GenerateStatementOption.CONDITION_COMMA);
		generateStatementOption.setParamaterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_SET);
		queryString.append(" set ").append(generateWhereClause(exampleSet, new ComplexFieldStack(), generateStatementOption));
		queryString.append(" where ").append(generateWhereClause(exampleWhere));

		Map<String, Object> queryParams = new HashMap<>();
		queryParams.putAll(mapParameters(exampleSet, new ComplexFieldStack(), generateStatementOption));
		queryParams.putAll(mapParameters(exampleWhere));

		OObjectDatabaseTx db = getConnection();
		try {
			updateCount = db.command(new OCommandSQL(queryString.toString())).execute(queryParams);
		} finally {
			closeConnection(db);
		}
		return updateCount;
	}

	public long countByExample(QueryByExample queryByExample)
	{
		long count = 0;
		StringBuilder queryString = new StringBuilder();
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

		String whereClause = generateWhereClause(queryByExample.getExample());
		if (StringUtils.isNotBlank(whereClause)) {
			queryString.append(" where ").append(whereClause);
		}

		OObjectDatabaseTx db = getConnection();
		try {
			List<ODocument> documents = db.command(new OCommandSQL(queryString.toString())).execute(mapParameters(queryByExample.getExample()));
			if (documents.isEmpty() == false) {
				count = documents.get(0).field("count");
			}
		} finally {
			closeConnection(db);
		}
		return count;
	}

	/**
	 * This will run the example with the default options
	 *
	 * @param <T>
	 * @param exampleClass
	 * @param baseEntity
	 * @return
	 */
	public <T> List<T> queryByExample(Class<T> exampleClass, BaseEntity baseEntity)
	{
		return queryByExample(exampleClass, new QueryByExample(baseEntity));
	}

	public <T> List<T> queryByExample(Class<T> exampleClass, QueryByExample queryByExample)
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
		String whereClause = generateWhereClause(queryByExample.getExample());
		if (StringUtils.isNotBlank(whereClause)) {
			queryString.append(" where ").append(whereClause);
			mappedParams.putAll(mapParameters(queryByExample.getExample()));
		}
		if (queryByExample.getLikeExample() != null) {
			GenerateStatementOption generateStatementOption = new GenerateStatementOption();
			generateStatementOption.setOperation(GenerateStatementOption.OPERATION_LIKE);
			String likeClause = generateWhereClause(queryByExample.getLikeExample(), new ComplexFieldStack(), generateStatementOption);
			if (StringUtils.isNotBlank(likeClause)) {
				if (StringUtils.isNotBlank(whereClause)) {
					queryString.append(" AND ");
				} else {
					queryString.append(" where ");
				}
				queryString.append(likeClause);
				mappedParams.putAll(mapParameters(queryByExample.getLikeExample()));
			}
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
		if (queryByExample.getFirstResult() != null) {
			queryString.append(" SKIP ").append(queryByExample.getFirstResult());
		}
		if (queryByExample.getMaxResults() != null) {
			queryString.append(" LIMIT ").append(queryByExample.getMaxResults());
		}
		if (queryByExample.getTimeout() != null) {
			queryString.append(" TIMEOUT ").append(queryByExample.getTimeout()).append(" ").append(queryByExample.getTimeoutStrategy());
		}
		if (queryByExample.isParallelQuery()) {
			queryString.append(" PARALLEL ");
		}

		List<T> results = query(queryString.toString(), mappedParams, exampleClass, queryByExample.isReturnNonProxied());
		return results;
	}

	private <T> String generateWhereClause(T example)
	{
		return generateWhereClause(example, new ComplexFieldStack(), new GenerateStatementOption());
	}

	private <T> String generateWhereClause(T example, ComplexFieldStack complexFieldStack, GenerateStatementOption generateStatementOption)
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
						if (ServiceUtil.isComplexClass(returnObj.getClass())) {
							complexFieldStack.getFieldStack().push(field.toString());
							if (addAnd) {
								where.append(generateStatementOption.getCondition());
							} else {
								addAnd = true;
								where.append(" ");
							}

							where.append(generateWhereClause(returnObj, complexFieldStack, generateStatementOption));
							complexFieldStack.getFieldStack().pop();
						} else {
							if (addAnd) {
								where.append(generateStatementOption.getCondition());
							} else {
								addAnd = true;
								where.append(" ");
							}

							String fieldName = complexFieldStack.getQueryFieldName() + field.toString();
							where.append(fieldName)
									.append(" ").append(generateStatementOption.getOperation()).append(" :")
									.append(fieldName.replace(".", PARAM_NAME_SEPARATOR))
									.append(generateStatementOption.getParamaterSuffix());
						}
					}
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
			throw new RuntimeException(ex);
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
						if (ServiceUtil.isComplexClass(returnObj.getClass())) {
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
			throw new RuntimeException(ex);
		}
		return where.toString();
	}

	private <T> Map<String, Object> mapParameters(T example)
	{
		return mapParameters(example, new ComplexFieldStack(PARAM_NAME_SEPARATOR), new GenerateStatementOption());
	}

	private <T> Map<String, Object> mapParameters(T example, ComplexFieldStack complexFieldStack, GenerateStatementOption generateStatementOption)
	{
		Map<String, Object> parameterMap = new HashMap<>();
		try {
			List<Field> fields = ServiceUtil.getAllFields(example.getClass());
			for (Field field : fields) {

				if ("class".equalsIgnoreCase(field.getName()) == false) {
					field.setAccessible(true);
					//Note: this may not work for proxy object....they may need to be call through a get method
					Object value = field.get(example);
					if (value != null) {
						if (ServiceUtil.isComplexClass(value.getClass())) {
							complexFieldStack.getFieldStack().push(field.getName());
							parameterMap.putAll(mapParameters(value, complexFieldStack, generateStatementOption));
							complexFieldStack.getFieldStack().pop();
						} else {
							String fieldName = complexFieldStack.getQueryFieldName() + field.getName();
							parameterMap.put(fieldName + generateStatementOption.getParamaterSuffix(), value);
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
	public <T> T queryOneByExample(Class<T> exampleClass, BaseEntity baseEnity)
	{
		return queryOneByExample(exampleClass, new QueryByExample(baseEnity));
	}

	public <T> T queryOneByExample(Class<T> exampleClass, QueryByExample queryByExample)
	{
		List<T> results = queryByExample(exampleClass, queryByExample);
		if (results.size() > 0) {
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
	public <T> List<T> query(String query, Map<String, Object> parameterMap)
	{
		return query(query, parameterMap, null, false);
	}

	/**
	 * This will run the query and then unwrap if desired
	 *
	 * @param <T>
	 * @param query
	 * @param parameterMap
	 * @param dataClass
	 * @param unwrap
	 * @return
	 */
	public <T> List<T> query(String query, Map<String, Object> parameterMap, Class<T> dataClass, boolean unwrap)
	{
		OObjectDatabaseTx db = getConnection();
		List<T> results = new ArrayList<>();
		try {
			results = db.query(new OSQLSynchQuery<>(query), parameterMap);
			if (unwrap) {
				results = unwrapProxy(db, dataClass, results);
			}
		} finally {
			closeConnection(db);
		}
		return results;
	}

	public <T extends BaseEntity> T persist(T entity)
	{
		OObjectDatabaseTx db = getConnection();
		T t = null;
		try {
			String pkValue = ServiceUtil.getPKFieldValue(entity);
			if (pkValue == null) {
				if (ServiceUtil.isPKFieldGenerated(entity)) {
					ServiceUtil.updatePKFieldValue(entity, generateId());
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
			throw new OpenStorefrontRuntimeException("Unable to save record: " + StringProcessor.printObject(entity), e);
		} finally {
			closeConnection(db);
		}

		return t;
	}

	public <T extends BaseEntity> T saveNonPkEntity(T entity)
	{
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

	public <T> List<T> unwrapProxy(Class<T> origClass, List<T> data)
	{
		OObjectDatabaseTx db = getConnection();
		List<T> nonProxyData = null;
		try {
			nonProxyData = unwrapProxy(db, origClass, data);
		} finally {
			closeConnection(db);
		}
		return nonProxyData;
	}

	public <T> List<T> unwrapProxy(OObjectDatabaseTx db, Class<T> origClass, List<T> data)
	{
		List<T> nonProxyList = new ArrayList<>();
		for (T dbproxy : data) {
			T nonProxied = db.detachAll(dbproxy, true);
			nonProxyList.add(nonProxied);
		}
		return nonProxyList;
	}

	public <T> T unwrapProxyObject(Class<T> origClass, T data)
	{
		OObjectDatabaseTx db = getConnection();
		T nonProxyData = null;
		try {
			nonProxyData = unwrapProxyObject(db, origClass, data);
		} finally {
			closeConnection(db);
		}
		return nonProxyData;
	}

	public <T> T unwrapProxyObject(OObjectDatabaseTx db, Class<T> origClass, T data)
	{
		T nonProxy = db.detachAll(data, true);
		return nonProxy;
	}

}
