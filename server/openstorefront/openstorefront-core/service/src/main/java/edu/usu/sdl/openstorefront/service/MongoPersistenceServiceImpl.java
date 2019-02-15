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
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.QueryType;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import edu.usu.sdl.openstorefront.core.entity.EntityEventType;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.model.EntityEventModel;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.manager.MongoDBManager;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.validation.exception.OpenStorefrontValidationException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
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
	private ClientSession session;

	public MongoPersistenceServiceImpl(MongoDBManager dbManager)
	{
		this.dbManager = dbManager;
	}

	@Override
	public String generateId()
	{
		return StringProcessor.uniqueId();
	}

	@Override
	public void begin()
	{
		if (session == null) {
			session = dbManager.getClient().startSession();
		} else {
			throw new OpenStorefrontRuntimeException("Already in a Transaction", "Commit or rollback transaction action before beginning a new one.");
		}

	}

	@Override
	public void commit()
	{
		if (session != null) {
			session.commitTransaction();
		} else {
			throw new OpenStorefrontRuntimeException("Not in a transaction", "Begin a new one.");
		}
	}

	@Override
	public void rollback()
	{
		if (session != null) {
			session.abortTransaction();
		} else {
			throw new OpenStorefrontRuntimeException("Not in a transaction", "Begin a new one.");
		}
	}

	@Override
	public void endTransaction()
	{
		if (session != null) {
			session.close();
		} else {
			throw new OpenStorefrontRuntimeException("Not in a transaction", "Begin a new one.");
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
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
			collection.deleteOne(constructPKFilter((BaseEntity) entity));

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
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public int deleteByQuery(Class entityClass, String whereClause, Map<String, Object> queryParams)
	{
		//REPLACE usage and Remove

		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public <T> T findById(Class<T> entity, Object id)
	{
		MongoCollection<T> collection = getCollectionForEntity(entity);
		return collection.find(constructPKFilter(entity, id)).first();
	}

	@Override
	public <T> List<T> query(String query, Map<String, Object> parameterMap)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public <T> List<T> query(String query, Map<String, Object> parameterMap, boolean unwrap)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public <T> List<T> queryByExample(BaseEntity baseEntity)
	{
		return queryByExample(new QueryByExample<>(baseEntity));
	}

	@Override
	public <T> List<T> queryByExample(QueryByExample queryByExample)
	{
		//MongoCollection<T> collection = getCollectionForEntity(queryByExample.get);
		//collection.

		//determine query type
		//generate param (field coord query construction)
		//grouping* and sorting
		//apply results restrictions
		//queryByExample.
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

	private <T extends BaseEntity> Bson constructPKFilter(T entity)
	{
		return constructPKFilter(entity.getClass(), EntityUtil.getPKFieldObjectValue(entity));
	}

	private <T> Bson constructPKFilter(Class<T> entity, Object id)
	{
		Map<String, Object> pkFields = EntityUtil.findIdField(entity, id);
		return new BasicDBObject(pkFields);
	}

	private <T> MongoCollection<T> getCollectionForEntity(T entity)
	{
		return getCollectionForEntity(entity.getClass());
	}

	@SuppressWarnings("unchecked")
	private <T> MongoCollection<T> getCollectionForEntity(Class entityClass)
	{
		return dbManager.getConnection().getCollection(entityClass.getSimpleName(), entityClass);
	}

	@Override
	public <T extends BaseEntity> T persist(T entity)
	{
		Objects.requireNonNull(entity, "Unable to persist a NULL entity.");

		try {
			updatePKFieldValue(entity);

			ValidationModel validationModel = new ValidationModel(entity);
			validationModel.setSantize(false);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (validationResult.valid()) {
				MongoCollection<T> collection = getCollectionForEntity(entity);

				UpdateResult updateResult = collection.replaceOne(
						constructPKFilter(entity),
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
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

	@Override
	public boolean isManaged(BaseEntity baseEntity)
	{
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
