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
package edu.usu.sdl.openstorefront.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationStrength;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.QueryType;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.EntityEventType;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.model.EntityEventModel;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.manager.MongoDBManager;
import edu.usu.sdl.openstorefront.service.repo.MongoQueryUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.validation.exception.OpenStorefrontValidationException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author dshurtleff
 */
public class MongoPersistenceServiceImpl
		implements PersistenceService
{

	private static final Logger LOG = Logger.getLogger(MongoPersistenceServiceImpl.class.getName());

	private MongoDBManager dbManager;
	private MongoQueryUtil queryUtil;
	private ClientSession session;

	public MongoPersistenceServiceImpl(MongoDBManager dbManager)
	{
		this.dbManager = dbManager;
		this.queryUtil = new MongoQueryUtil(dbManager);
	}

	public MongoPersistenceServiceImpl(MongoDBManager dbManager, MongoQueryUtil queryUtil)
	{
		this.dbManager = dbManager;
		this.queryUtil = queryUtil;
	}

	public MongoQueryUtil getQueryUtil()
	{
		return queryUtil;
	}

	@Override
	public String generateId()
	{
		return StringProcessor.uniqueId();
	}

	@Override
	public void begin()
	{
		if (dbManager.supportTransactions()) {
			if (session == null) {
				session = dbManager.getClient().startSession();
			} else {
				throw new OpenStorefrontRuntimeException("Already in a Transaction", "Commit or rollback transaction action before beginning a new one.");
			}
		}

	}

	@Override
	public void commit()
	{
		if (dbManager.supportTransactions()) {
			if (session != null) {
				session.commitTransaction();
			} else {
				throw new OpenStorefrontRuntimeException("Not in a transaction", "Begin a new one.");
			}
		}
	}

	@Override
	public void rollback()
	{
		if (dbManager.supportTransactions()) {
			if (session != null) {
				session.abortTransaction();
			} else {
				throw new OpenStorefrontRuntimeException("Not in a transaction", "Begin a new one.");
			}
		}
	}

	@Override
	public void endTransaction()
	{
		if (dbManager.supportTransactions()) {
			if (session != null) {
				session.close();
				session = null;
			}
		}
	}

	@Override
	public boolean isTransactionActive()
	{
		boolean active = false;
		if (session != null) {
			active = session.hasActiveTransaction();
		}
		return active;
	}

	@Override
	public long countByExample(BaseEntity example)
	{
		QueryByExample queryByExample = new QueryByExample<>(example);
		queryByExample.setQueryType(QueryType.COUNT);
		return countByExample(queryByExample);
	}

	@Override
	public long countByExample(QueryByExample queryByExample)
	{
		MongoCollection collection = queryUtil.getCollectionForEntity(queryByExample.getExample());

		@SuppressWarnings("unchecked")
		Bson filter = queryUtil.generateFilters(queryByExample);

		return collection.countDocuments(filter);
	}

	@Override
	public long countClass(Class entityClass)
	{
		MongoCollection collection = dbManager.getConnection().getCollection(entityClass.getSimpleName());
		return collection.countDocuments();
	}

	@Override
	public <T> void delete(T entity)
	{
		if (entity != null && (entity instanceof BaseEntity)) {
			MongoCollection collection = dbManager.getConnection().getCollection(entity.getClass().getSimpleName());
			collection.deleteOne(queryUtil.constructPKFilter((BaseEntity) entity));

			EntityEventModel entityModel = new EntityEventModel();
			entityModel.setEntityChanged(entity);
			entityModel.setEventType(EntityEventType.DELETE);
			if (ServiceProxy.getProxy().getEntityEventService() != null) {
				ServiceProxy.getProxy().getEntityEventService().processEvent(entityModel);
			}
		}
	}

	@Override
	public <T> int deleteByExample(BaseEntity example)
	{
		return deleteByExample(new QueryByExample<>(example));
	}

	@Override
	public <T> int deleteByExample(QueryByExample queryByExample)
	{
		@SuppressWarnings("unchecked")
		MongoCollection<T> collection = queryUtil.getCollectionForEntity((T) queryByExample.getExample());

		@SuppressWarnings("unchecked")
		Bson filter = queryUtil.generateFilters(queryByExample);

		DeleteResult deleteResult = collection.deleteMany(filter);
		return (int) deleteResult.getDeletedCount();
	}

	@Override
	public int deleteByQuery(Class entityClass, String whereClause, Map<String, Object> queryParams)
	{
		//REPLACE usage and Remove
		throw new UnsupportedOperationException("Not supported With Mongo.");
	}

	@Override
	public <T> T findById(Class<T> entity, Object id)
	{
		MongoCollection<T> collection = queryUtil.getCollectionForEntity(entity);
		return collection.find(queryUtil.constructPKFilter(entity, id)).first();
	}

	@Override
	public <T> List<T> query(String query, Map<String, Object> parameterMap)
	{
		throw new UnsupportedOperationException("Not supported with Mongo.");
	}

	@Override
	public <T> List<T> query(String query, Map<String, Object> parameterMap, boolean unwrap)
	{
		throw new UnsupportedOperationException("Not supported with Mongo.");
	}

	@Override
	public <T> List<T> queryByExample(BaseEntity baseEntity)
	{
		return queryByExample(new QueryByExample<>(baseEntity));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> queryByExample(QueryByExample queryByExample)
	{
		//Only handle select
		if (!QueryType.SELECT.equals(queryByExample.getQueryType())) {
			throw new UnsupportedOperationException("This method only support Read/Select.");
		}

		MongoCollection<T> collection = queryUtil.getCollectionForEntity((T) queryByExample.getExample());

		Bson filter = queryUtil.generateFilters(queryByExample);

		if (queryByExample.isParallelQuery()) {
			LOG.log(Level.FINER, "Parallel Queries not supported in Mongo");
		}

		if (StringUtils.isNotBlank(queryByExample.getDistinctField())) {
			//distinct only return one field however, the expect behavior is a full entity
			//Note this set to String...for custom case outside of this case; use the repos.
			@SuppressWarnings("unchecked")
			DistinctIterable<String> distinctIterable = collection.distinct(queryByExample.getDistinctField(), filter, String.class);
			if (queryByExample.getTimeout() != null && queryByExample.getTimeout() > 0) {
				distinctIterable.maxTime(queryByExample.getTimeout(), TimeUnit.SECONDS);
			}

			List<T> entities = new ArrayList<>();
			for (String value : distinctIterable) {
				try {
					T entity = (T) queryByExample.getExample().getClass().newInstance();
					BeanUtils.setProperty(entity, queryByExample.getDistinctField(), value);
					entities.add(entity);
				} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
					throw new OpenStorefrontRuntimeException(ex);
				}
			}
			return entities;
		} else if (queryByExample.getGroupBy() != null) {
			List<Bson> pipeline = new ArrayList<>();

			if (queryByExample.getExample() != null) {
				pipeline.add(Aggregates.match(queryUtil.generateFilters(queryByExample)));
			}
			pipeline.add(queryUtil.generateGroupByFilter(queryByExample));

			if (queryByExample.getOrderBy() != null) {
				pipeline.add(Aggregates.sort(queryUtil.generateSortFilter(queryByExample)));
			}

			if (queryByExample.getFirstResult() != null && queryByExample.getFirstResult() > 0) {
				pipeline.add(Aggregates.skip(queryByExample.getFirstResult()));
			}

			if (queryByExample.getMaxResults() != null && queryByExample.getMaxResults() > 0) {
				pipeline.add(Aggregates.limit(queryByExample.getMaxResults()));
			}

			AggregateIterable<T> aggregateIterable = collection.aggregate(pipeline, (Class<T>) queryByExample.getExample().getClass());
			if (queryByExample.getTimeout() != null && queryByExample.getTimeout() > 0) {
				aggregateIterable.maxTime(queryByExample.getTimeout(), TimeUnit.SECONDS);
			}

			List<T> results = aggregateIterable.into(new ArrayList<>());
			return results;
		} else {
			FindIterable<T> findIterable = collection.find(filter);

			if (queryByExample.getFirstResult() != null && queryByExample.getFirstResult() > 0) {
				findIterable.skip(queryByExample.getFirstResult());
			}
			if (queryByExample.getMaxResults() != null && queryByExample.getMaxResults() > 0) {
				findIterable.limit(queryByExample.getMaxResults());
			}
			if (queryByExample.getTimeout() != null && queryByExample.getTimeout() > 0) {
				findIterable.maxTime(queryByExample.getTimeout(), TimeUnit.SECONDS);
			}
			if (queryByExample.getOrderBy() != null) {
				findIterable.sort(queryUtil.generateSortFilter(queryByExample));
				Collation caseInsensitive = Collation.builder()
						.locale("en")
						.collationStrength(CollationStrength.SECONDARY)
						.build();
				findIterable.collation(caseInsensitive);
			}

			return findIterable.into(new ArrayList<>());
		}

	}

	@Override

	public <T> T queryOneByExample(BaseEntity baseEntity)
	{
		return queryOneByExample(new QueryByExample<>(baseEntity));
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
	 * Run any database query; Avoid usage except in special cases; (Consider
	 * Replacing and Removing); DB manager should handle database specific
	 * commands.
	 *
	 * @param <T>
	 * @param query
	 * @param queryParams
	 * @return 1 on success or else will it likely throw an error. However it
	 * may not include the failed result.
	 */
	@Override
	public <T> int runDbCommand(String query, Map<String, Object> queryParams)
	{
		int successFulCall = 1;

		/* http://mongodb.github.io/mongo-java-driver/3.9/driver/tutorials/commands/
		   https://docs.mongodb.com/manual/reference/command/
		 */
		Document results = dbManager.getConnection().runCommand(new BasicDBObject(queryParams));

		if (LOG.isLoggable(Level.FINEST)) {
			LOG.log(Level.FINEST, results.toJson());
		}

		return successFulCall;
	}

	/**
	 * Saves a part to the Mongo database.
	 * 
	 * Whenever ANYTHING is saved to the database (be it an entry, a log, etc), an EntityEventModel 
	 * is filled out to record the Event and then passed to event handlers. This emulates
	 * event based programming. 
	 * @param <T>
	 * @param entity
	 * @return 
	 */
	@Override
	public <T extends BaseEntity> T persist(T entity)
	{
		Objects.requireNonNull(entity, "Unable to persist a NULL entity.");

		try {
			updatePKFieldValue(entity);

			ValidationModel validationModel = new ValidationModel(entity);
			validationModel.setSanitize(false);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (validationResult.valid()) {
				MongoCollection<T> collection = queryUtil.getCollectionForEntity(entity);

				UpdateResult updateResult = collection.replaceOne(
						queryUtil.constructPKFilter(entity),
						entity,
						ReplaceOptions.createReplaceOptions(new UpdateOptions().upsert(true))
				);
				if (LOG.isLoggable(Level.FINEST)) {
					LOG.log(Level.FINEST, ()
							-> "Updated Collection: " + entity.getClass().getSimpleName()
							+ " Modification Count: " + updateResult.getModifiedCount()
							+ " Match Count: " + updateResult.getMatchedCount()
							+ " Mongo Id: " + updateResult.getUpsertedId());
				}

				//This obviously won't catch everything but it will catch all common cases
				EntityEventModel entityModel = new EntityEventModel();
				entityModel.setEntityChanged(entity);
				entityModel.setOldEntity(entity);
				if (isManaged(entity)) {
					entityModel.setEventType(EntityEventType.UPDATE);
				} else {
					entityModel.setEventType(EntityEventType.CREATE);
				}
				//To make mocking easier
				if (ServiceProxy.getProxy().getEntityEventService() != null) {
					ServiceProxy.getProxy().getEntityEventService().processEvent(entityModel);
				}

			} else {
				throw new OpenStorefrontValidationException("Unable to save record. (See stacktrace cause) \n Field Values: \n" + StringProcessor.printObject(entity), validationResult, "Check the data to make sure it conforms to the rules. Recored type: " + entity.getClass().getName());
			}
		} catch (OpenStorefrontRuntimeException e) {
			throw e;
		} catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
			throw new OpenStorefrontRuntimeException("Unable to save record. (See stacktrace cause) \n Field Values: \n" + StringProcessor.printObject(entity), e);
		}
		return entity;
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

	@Override
	public <T> int updateByExample(Class<T> entityClass, T exampleSet, T exampleWhere)
	{
		MongoCollection<T> collection = queryUtil.getCollectionForEntity(entityClass);

		@SuppressWarnings("unchecked")
		Map<String, Object> exampleMap = queryUtil.generateFieldMap(exampleWhere);
		Bson updateMatch = new BasicDBObject(exampleMap);

		Bson updateSet = queryUtil.generateUpdateSet(exampleSet);
		UpdateResult updateResult = collection.updateMany(updateMatch, updateSet);

		if (LOG.isLoggable(Level.FINEST)) {
			LOG.log(Level.FINEST, () -> "Matched Count:" + updateResult.getMatchedCount());
			LOG.log(Level.FINEST, () -> "Modified Count:" + updateResult.getModifiedCount());
		}

		return (int) updateResult.getModifiedCount();
	}

	private <T extends BaseEntity> void updatePKFieldValue(T entity) throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException, SecurityException, InvocationTargetException
	{
		String pkValue = EntityUtil.getPKFieldValue(entity);
		if (pkValue == null) {
			if (EntityUtil.isPKFieldGenerated(entity)) {
				EntityUtil.updatePKFieldValue(entity, generateId());
			}
		}
		List<Field> fields = ReflectionUtil.getAllFields(entity.getClass());
		for (Field field : fields) {
			if (BaseEntity.class.isAssignableFrom(field.getType())) {
				Method method = entity.getClass().getMethod("get" + StringUtils.capitalize(field.getName()), (Class<?>[]) null);
				Object nestaedObj = method.invoke(entity, (Object[]) null);
				if (nestaedObj != null) {
					updatePKFieldValue(BaseEntity.class.cast(nestaedObj));
				}
			}
		}
	}

	@Override
	public <T> T detach(T entity)
	{
		//Mongo doesn't have a proxy concept; pass-through
		return entity;
	}

	@Override
	public <T> T deattachAll(T entity)
	{
		//Mongo doesn't have a proxy concept; pass-through
		return entity;
	}

	/**
	 * The purpose of this function is to help identify what EntityEventType should be set on the EntityEventModel
	 * of the entity that is being saved to the db (in the persist() function). 
	 * 
	 * In v1.12, the sole purpose of isManaged is to determine whether a component already exists in the db (and hence, 
	 * the EntityEventModel should have a EntityEventType of kind "UPDATE"), or not. Most other Event kind ( such as
	 *  Activate, Deactivated, New SUbmission (Not Submitted), Pending, Pending Change Request )
	 * gets cast as if they were CREATE. 
	 * 
	 * SPOON's original design imagined that each of these different events would trigger the appropriate EntityEventType's assignment. 
	 * However the needs of SPOOn are met with just CREATE and UPDATE. This is why there isManaged returns a boolean. 
	 * In the future, if the original designs are to be realized, this function will be replaced with logic to a certain all the 
	 * different EntityEventTypes. 
	 * 
	 * @param baseEntity BaseEntity that is being saved to the database presently
	 * @return 
	 */
	@Override
	public boolean isManaged(BaseEntity baseEntity)
	{
		if( baseEntity instanceof Component ){
			Component example = new Component();
			example.setComponentId(((Component)baseEntity).getComponentId());
			if (example.find() != null){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isProxy(BaseEntity baseEntity)
	{
		return false;
	}

	@Override
	public <T> List<T> unwrapProxy(List<T> data)
	{
		//Mongo doesn't have a proxy concept; pass-through
		return data;
	}

	@Override
	public <T> T unwrapProxyObject(T data)
	{
		//Mongo doesn't have a proxy concept; pass-through
		return data;
	}

}
