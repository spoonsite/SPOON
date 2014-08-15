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

import com.orientechnologies.orient.core.id.OClusterPositionFactory;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.util.PK;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class PersistenceService
{
	private static final Logger log = Logger.getLogger(PersistenceService.class.getName());
	
	private  OObjectDatabaseTx transaction;
	
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
		if (transaction == null)
		{
			db =  DBManager.getConnection();
		}
		else
		{
			db = transaction;
		}
		return db;
	}

	public void closeConnection(OObjectDatabaseTx db)
	{
		if (transaction == null)
		{
			db.close();
		}
	}
	
	public void begin()
	{
		if (transaction == null)
		{
			transaction = getConnection();
			transaction.getTransaction().begin();
		}
		else
		{
			throw new OpenStorefrontRuntimeException("Already in a Transaction", "Commit or rollback transaction action before beginning a new one.");
		}
	}
	
	public void commit()			
	{
		if (transaction == null)
		{
			throw new OpenStorefrontRuntimeException("Not in a transaction", "Begin a new one.");
		}
		else
		{
			transaction.getTransaction().commit();
		}				
	}
	
	public void rollback()
	{
		if (transaction == null)
		{
			throw new OpenStorefrontRuntimeException("Not in a transaction", "Begin a new one.");
		}
		else
		{		
			transaction.getTransaction().rollback();
		}
	}	

	public void endTransaction()
	{
		if (transaction != null)
		{
			transaction.getTransaction().close();
			transaction.close();
			transaction = null;
		}
	}	
		
	public boolean isTransactionActive() {
		boolean active = false;
		if (transaction != null)
		{
			active = transaction.getTransaction().isActive();
		}		
		return active;
	}	
	
	/**
	 * This only works on managed objects
	 * @param <T>
	 * @param entityClass
	 * @param primaryKey
	 * @return 
	 */
	@SuppressWarnings("unchecked")	
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		final ORecordId rid;

		OObjectDatabaseTx database = getConnection();
		try
		{
			if (primaryKey instanceof ORecordId)
			{
				rid = (ORecordId) primaryKey;
			} else if (primaryKey instanceof String)
			{
				rid = new ORecordId((String) primaryKey);
			} else if (primaryKey instanceof Number)
			{
				// COMPOSE THE RID
				OClass cls = database.getMetadata().getSchema().getClass(entityClass);
				if (cls == null)
				{
					throw new IllegalArgumentException("Class '" + entityClass + "' is not configured in the database");
				}
				rid = new ORecordId(cls.getDefaultClusterId(), OClusterPositionFactory.INSTANCE.valueOf(((Number) primaryKey).longValue()));
			} else
			{
				throw new IllegalArgumentException("PrimaryKey '" + primaryKey + "' type (" + primaryKey.getClass() + ") is not supported");
			}

			return (T) database.load(rid);
		} finally
		{
			closeConnection(database);
		}
	}
	
	public <T> T findById(Class<T> entity, Object id)
	{
		OObjectDatabaseTx db = getConnection();	
		T returnEntity = null;
		try
		{
			String pkFieldName = findIdField(entity);
			
			if (pkFieldName == null)
			{
				throw new OpenStorefrontRuntimeException("Unable to find PK field", "Mark the Primary Key field (@PK) on the entity: " + entity.getName());
			}
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("idParam", id);
			
			List<T> results = db.query(new OSQLSynchQuery<>("select * from " + entity.getSimpleName() + " where " + pkFieldName + " = :idParam"), parameters);

			if (!results.isEmpty())
			{
				returnEntity =  results.get(0);
			}
		} 
		finally
		{
			closeConnection(db);
		}
		
		return returnEntity;
	}
	
	private String findIdField(Class entityClass)
	{
		String idField = null;
		//Start at the root (The last Id found wins ...there should only be one)
		if(entityClass.getSuperclass() != null)
		{
			idField = findIdField(entityClass.getSuperclass());
		}
		if (idField == null)
		{
			for (Field field : entityClass.getDeclaredFields())
			{
				PK idAnnotation = field.getAnnotation(PK.class);
				if (idAnnotation != null)
				{
					idField = field.getName();
					break;
				}
			}
		}
		return idField;		
	}
	
	public long countClass(Class entityClass)
	{
		OObjectDatabaseTx db = getConnection();	
		long count = 0;
		try
		{
			count = db.countClass(entityClass);
		} 
		finally
		{
			closeConnection(db);
		}
		return count;
	}	
	
	public<T>  long deleteByExample(T example)
	{
		long deleteCount = 0;
		StringBuilder queryString = new StringBuilder();
		
		queryString.append("delete from ").append(example.getClass().getSimpleName());
		
		String whereClause = generateWhereClause(example);
		if (StringUtils.isNotBlank(whereClause))
		{
			queryString.append(" where ").append(whereClause);		
		}
		
		OObjectDatabaseTx db = getConnection();		
		try
		{		
			deleteCount  = db.command(new OCommandSQL(queryString.toString()).execute(mapParameters(example)));			
		} finally
		{
			closeConnection(db);
		}		
		
		return deleteCount;
	}	
	
	public<T>  void updateByExample(T exampleSet, T exampleWhere)
	{
		throw new OpenStorefrontRuntimeException("Unsupported operation", "Add support");
	}	
	
	public long countByExample(QueryByExample queryByExample)
	{
		long count = 0;
		StringBuilder queryString = new StringBuilder();
		switch (queryByExample.getQueryType())
		{
			case COUNT:
				queryString.append("select sum(*) ");
				break;
			case COUNT_DISTINCT:
				queryString.append("select sum(distinct(").append(queryByExample.getDistinctField()).append(") ");
				break;
			default:
				throw new OpenStorefrontRuntimeException("Query Type unsupported: " + queryByExample.getQueryType(), "Only supports Count types");						
		}
		queryString.append("from ").append(queryByExample.getExample().getClass().getSimpleName());
		
		String whereClause = generateWhereClause(queryByExample.getExample());
		if (StringUtils.isNotBlank(whereClause))
		{
			queryString.append(" where ").append(whereClause);		
		}
		
		OObjectDatabaseTx db = getConnection();		
		try
		{		
			count  = db.command(new OCommandSQL(queryString.toString()).execute(mapParameters(queryByExample.getExample())));			
		} finally
		{
			closeConnection(db);
		}			
		return count;
	}	
	
	public <T> List<T> queryByExample(QueryByExample queryByExample)
	{
		StringBuilder queryString = new StringBuilder();
		
		switch (queryByExample.getQueryType())
		{
			case SELECT:
				queryString.append("select  ");
				break;
			default:
				throw new OpenStorefrontRuntimeException("Query Type unsupported: " + queryByExample.getQueryType(), "Only supports select");			
		}
		queryString.append(" from ").append(queryByExample.getExample().getClass().getSimpleName());
		
		String whereClause = generateWhereClause(queryByExample.getExample());
		if (StringUtils.isNotBlank(whereClause))
		{
			queryString.append(" where ").append(whereClause);		
		}
		
		List<T> results  = query(queryString.toString(), mapParameters(queryByExample.getExample()));
		return results;
	}	
	
	private <T> String generateWhereClause (T example)
	{
		StringBuilder where = new StringBuilder();
		
		try
		{
			Map fieldMap = BeanUtils.describe(example);			
			boolean addAnd = false;
			for (Object field :  fieldMap.keySet())
			{
				
				if ("class".equalsIgnoreCase(field.toString()) == false)
				{
					Object value = fieldMap.get(field);
					if (value != null)
					{
						if (addAnd)
						{
							where.append(" AND ");
						}						
						else
						{
							addAnd = true;
							where.append(" ");
						}						
						
						where.append(field).append(" = :").append(field).append("Param");
					}
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex)
		{
			log.log(Level.SEVERE, null, ex);
		}
		return where.toString();
	}
	
	private <T>  Map<String, Object> mapParameters(T example)
	{
		Map<String, Object> parameterMap = new HashMap<>();
		try
		{
			Map fieldMap = BeanUtils.describe(example);	
			fieldMap.keySet().stream().filter((field) -> ("class".equalsIgnoreCase(field.toString()) == false)).forEach((field) ->
			{
				Object value = fieldMap.get(field);
				if (value != null)
				{
					parameterMap.put(field + "Param", value);					
				}
			});
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex)
		{
			log.log(Level.SEVERE, null, ex);
		}		
		return parameterMap;
	}	
	
	public <T> T queryByOneExample(QueryByExample queryByExample)
	{		
		List<T> results = queryByExample(queryByExample);
		if (results.size() > 0)
		{
			return results.get(0);
		}
		return null;		
	}
	
	public <T> List<T> query(String query, Map<String, Object> parameterMap)
	{
		OObjectDatabaseTx db = getConnection();	
		List<T> results  = new ArrayList<>();
		try
		{			 
			results = db.query(new OSQLSynchQuery<>(query), parameterMap);			
		} finally
		{
			closeConnection(db);
		}		
		return results;
	}
	
	public <T> T persist(T entity)
	{
		OObjectDatabaseTx db = getConnection();		
		T t = null;
		try
		{	
			ValidationResult validationResult =  ValidationUtil.validate(new ValidationModel(entity));
			if (validationResult.valid())
			{
				 t = db.save(entity);		
			}
			else
			{
				throw new OpenStorefrontRuntimeException(validationResult.toString(), "Check the data to make sure it conforms to the rules.");
			}	
		} finally
		{
			closeConnection(db);
		}		
		
		return t;
	}
	
	public<T> void delete(T entity)
	{
		OObjectDatabaseTx db = getConnection();				
		try
		{		
			db.delete(entity);		
		} finally
		{
			closeConnection(db);
		}		
	}	
	
	/**
	 * Detaches and removes the proxy object
	 * @param <T>
	 * @param entity 
	 */
	public<T> void detach(T entity)
	{
		OObjectDatabaseTx db = getConnection();				
		try
		{		
			db.detach(entity, true);
		} finally
		{
			closeConnection(db);
		}		
	}
	
	/**
	 *  Detaches the whole object tree and removes the proxy object
	 * @param <T>
	 * @param entity 
	 */
	public<T> void deattachAll(T entity)
	{
		OObjectDatabaseTx db = getConnection();				
		try
		{		
			db.detachAll(entity, true);
		} finally
		{
			closeConnection(db);
		}		
	}	
	
	public <T> List<T> unwrapProxy(Class<T> origClass, List<T> data)
	{
		List<T> nonProxyList = new ArrayList<>();
		for (T dbproxy : data)
		{			
			try
			{
				T nonProxy = origClass.newInstance();
				BeanUtils.copyProperties(nonProxy, dbproxy);
				nonProxyList.add(nonProxy);
			} catch (	IllegalAccessException | InvocationTargetException | InstantiationException ex)
			{
				log.log(Level.SEVERE, null, ex);
			}
		}		
		return nonProxyList;
	}
	
	public <T> T unwrapProxyObject(Class<T> origClass, T data)
	{
		T nonProxy = data;
		try
		{
			nonProxy = origClass.newInstance();
			BeanUtils.copyProperties(nonProxy, data);
			
		} catch (	IllegalAccessException | InvocationTargetException | InstantiationException ex)
		{
			log.log(Level.SEVERE, null, ex);
		}
		return nonProxy;
	}	
	
	
}
