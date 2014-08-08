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

import com.orientechnologies.orient.core.id.OClusterPositionFactory;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

/**
 *
 * @author dshurtleff
 */
public class ODBEntityManager
	implements EntityManager
{
	private static final Logger log = Logger.getLogger(ODBEntityManager.class.getName());	
	
	private final OObjectDatabaseTx database;
	private FlushModeType flushMode = FlushModeType.AUTO;

	public ODBEntityManager(OObjectDatabaseTx db)
	{
		this.database = db;
	}
		
	@Override
	public void persist(Object entity)
	{
		database.save(entity);
	}

	@Override
	public <T> T merge(T entity)
	{
		return database.save(entity);		
	}

	@Override
	public void remove(Object entity)
	{
		database.delete(entity);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey)
	{
		final ORecordId rid;

		if (primaryKey instanceof ORecordId) {
			rid = (ORecordId) primaryKey;
		} else if (primaryKey instanceof String) {
			rid = new ORecordId((String) primaryKey);
		} else if (primaryKey instanceof Number) {
			// COMPOSE THE RID
			OClass cls = database.getMetadata().getSchema().getClass(entityClass);
			if (cls == null) {
				throw new IllegalArgumentException("Class '" + entityClass + "' is not configured in the database");
			}
			rid = new ORecordId(cls.getDefaultClusterId(), OClusterPositionFactory.INSTANCE.valueOf(((Number) primaryKey).longValue()));
		} else {
			throw new IllegalArgumentException("PrimaryKey '" + primaryKey + "' type (" + primaryKey.getClass() + ") is not supported");
		}

		return (T) database.load(rid);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public <T> T getReference(Class<T> entityClass, Object primaryKey)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void flush()
	{
		if (flushMode == FlushModeType.COMMIT) {
			database.commit();
			if (log.isLoggable(Level.FINEST)) {
				log.log(Level.INFO, "EntityManager flushed. {0}", toString());
			}
		}
	}

	@Override
	public void setFlushMode(FlushModeType flushMode)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public FlushModeType getFlushMode()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void lock(Object entity, LockModeType lockMode)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void refresh(Object entity)
	{
		database.load(entity);
		if (log.isLoggable(Level.FINEST)) {
			log.log(Level.INFO, "EntityManager refreshed. {0}", toString());
		}
	}

	@Override
	public void refresh(Object entity, Map<String, Object> properties)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void refresh(Object entity, LockModeType lockMode)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void clear()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void detach(Object entity)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean contains(Object entity)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public LockModeType getLockMode(Object entity)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setProperty(String propertyName, Object value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Map<String, Object> getProperties()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Query createQuery(String qlString)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Query createNamedQuery(String name)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Query createNativeQuery(String sqlString)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Query createNativeQuery(String sqlString, Class resultClass)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Query createNativeQuery(String sqlString, String resultSetMapping)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void joinTransaction()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public <T> T unwrap(Class<T> cls)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Object getDelegate()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void close()
	{
		database.close();
	}

	@Override
	public boolean isOpen()
	{
		return !database.isClosed();
	}

	@Override
	public EntityTransaction getTransaction()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Metamodel getMetamodel()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public String toString() {
		return "EntityManager for User@Database:" + database.getUser() + "@" + database.getURL() + ", " + super.toString();
	}		
	
}

//
//package edu.usu.sdl.coeevaluator.service;
//
//import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
//import com.orientechnologies.orient.object.db.OObjectDatabasePool;
//import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
//import edu.usu.sdl.coeevaluator.model.Artifact;
//import edu.usu.sdl.coeevaluator.model.Dependancy;
//import edu.usu.sdl.coeevaluator.model.Integration;
//import edu.usu.sdl.coeevaluator.model.Narrative;
//import edu.usu.sdl.coeevaluator.model.Project;
//import edu.usu.sdl.coeevaluator.model.Question;
//import edu.usu.sdl.coeevaluator.model.Recommendation;
//import edu.usu.sdl.coeevaluator.model.Review;
//import edu.usu.sdl.coeevaluator.model.Score;
//import edu.usu.sdl.coeevaluator.model.TestCase;
//import edu.usu.sdl.coeevaluator.util.ServiceUtil;
//import java.io.File;
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.apache.commons.beanutils.BeanUtils;
//
///**
// *
// * @author dshurtleff
// */
//public class StorageService
//{
//	private static final Logger log = Logger.getLogger(StorageService.class.getName());
//	
//	private static final String DB_LOGIN_USER = "admin";
//	private static final String DB_LOGIN_PW = "admin";
//	
//	private static String dbConnection;
//	private static String dbStoragePath;	
//	
//	/**
//	 * Called once at application Startup
//	 */
//	public static void initialize()
//	{
//		dbConnection = System.getProperty("dbconnection.url", "remote:localhost/db");
//		dbStoragePath = System.getProperty("dbconnection.path", "/development/orientdb-1.5.1/databases/db");
//		File f = new File(dbStoragePath);
//		if (f.exists() == false)
//		{
//			f.mkdirs();	
//			log.log(Level.INFO, "Creating DB at {0}", dbConnection);
//			OObjectDatabaseTx db =  new OObjectDatabaseTx(dbConnection).create();
//			db.close();
//			log.log(Level.INFO, "Done");
//		}
//		
//		//register Entities
//		OObjectDatabaseTx db =  OObjectDatabasePool.global().acquire(dbConnection, DB_LOGIN_USER, DB_LOGIN_PW);
//		//db.getEntityManager().registerEntityClasses("edu.usu.sdl.coeevaluator.model");
//		db.getEntityManager().registerEntityClass(Artifact.class);
//		db.getEntityManager().registerEntityClass(Dependancy.class);
//		db.getEntityManager().registerEntityClass(Integration.class);
//		db.getEntityManager().registerEntityClass(Narrative.class);
//		db.getEntityManager().registerEntityClass(Project.class);
//		db.getEntityManager().registerEntityClass(Question.class);
//		db.getEntityManager().registerEntityClass(Recommendation.class);
//		db.getEntityManager().registerEntityClass(Review.class);
//		db.getEntityManager().registerEntityClass(Score.class);
//		db.getEntityManager().registerEntityClass(TestCase.class);
//		
//	}
//	
//	/**
//	 * Called on application shutdown
//	 */
//	public static void cleanup()
//	{	
//		 OObjectDatabasePool.global().close();
//	}	
//
//	public OObjectDatabaseTx getConnection()
//	{
//		OObjectDatabaseTx db =  OObjectDatabasePool.global().acquire(dbConnection, DB_LOGIN_USER, DB_LOGIN_PW);
//		return db;
//	}
//
//	public void closeConnection(OObjectDatabaseTx db)
//	{
//		db.close();
//	}
//			
//	public <T> T findById(Class<T> entity, Object id)
//	{
//		OObjectDatabaseTx db = getConnection();	
//		try
//		{
//			List<T> results = db.query(new OSQLSynchQuery<Project>("select * from " + entity.getSimpleName() + " where pk = '" + id + "'"));
//
//			if (!results.isEmpty())
//			{
//				return results.get(0);
//			}
//		} finally
//		{
//			closeConnection(db);
//		}
//		
//		return null;
//	}
//	
//	public <T> List<T> queryByExample(T example)
//	{
//		String queryString = "select * from " +example.getClass().getSimpleName()+" where " + generateWhereClause(example);
//		return query(queryString);		
//	}
//	
//	public <T> T queryByOneExample(T example)
//	{		
//		List<T> results = queryByExample(example);
//		if (results.size() > 0)
//		{
//			return results.get(0);
//		}
//		return null;		
//	}
//	
//	public <T> List<T> query(String query)
//	{
//		OObjectDatabaseTx db = getConnection();	
//		List<T> results  = new ArrayList<>();
//		try
//		{		
//			results = db.query(new OSQLSynchQuery<T>(query));			
//		} finally
//		{
//			closeConnection(db);
//		}		
//		return results;
//	}
//	
//	public <T> T persist(T entity)
//	{
//		OObjectDatabaseTx db = getConnection();		
//		T t = null;
//		try
//		{		
//			 t = db.save(entity);		
//		} finally
//		{
//			closeConnection(db);
//		}		
//		
//		return t;
//	}
//	
//	public<T> void delete(T entity)
//	{
//		OObjectDatabaseTx db = getConnection();				
//		try
//		{		
//			db.delete(entity);		
//		} finally
//		{
//			closeConnection(db);
//		}		
//	}	
//	
//	private <T> String generateWhereClause (T example)
//	{
//		StringBuilder where = new StringBuilder();
//		
//		try
//		{
//			Map fieldMap = BeanUtils.describe(example);			
//			boolean addAnd = false;
//			for (Object field :  fieldMap.keySet())
//			{
//				
//				if ("class".equalsIgnoreCase(field.toString()) == false)
//				{
//					Object value = fieldMap.get(field);
//					if (value != null)
//					{
//						if (addAnd)
//						{
//							where.append(" AND ");
//						}						
//						else
//						{
//							addAnd = true;
//							where.append(" ");
//						}						
//						
//						if (value instanceof Number)
//						{
//							where.append(field).append(" = ").append(value);
//						}
//						else
//						{
//							where.append(field).append(" = '").append(value).append("'");
//						}
//													
//					}
//				}
//			}
//		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex)
//		{
//			log.log(Level.SEVERE, null, ex);
//		}
//		return where.toString();
//	}
//	
//	
//	public <T> List<T> unwrapProxy(Class<T> origClass, List<T> data)
//	{
//		List<T> nonProxyList = new ArrayList<>();
//		for (T dbproxy : data)
//		{			
//			try
//			{
//				T nonProxy = origClass.newInstance();
//				BeanUtils.copyProperties(nonProxy, dbproxy);
//				nonProxyList.add(nonProxy);
//			} catch (	IllegalAccessException | InvocationTargetException | InstantiationException ex)
//			{
//				log.log(Level.SEVERE, null, ex);
//			}
//		}		
//		return nonProxyList;
//	}
//	
//	public <T> T unwrapProxyObject(Class<T> origClass, T data)
//	{
//		T nonProxy = data;
//		try
//		{
//			nonProxy = origClass.newInstance();
//			BeanUtils.copyProperties(nonProxy, data);
//			
//		} catch (	IllegalAccessException | InvocationTargetException | InstantiationException ex)
//		{
//			log.log(Level.SEVERE, null, ex);
//		}
//		return nonProxy;
//	}
//	
//	protected  String getProjectDirectory(String projectPk)	
//	{
//		return projectPk;
//	}
//	
//	protected String generateId()
//	{
//		return ServiceUtil.generateId();
//	}		
//
//}
//
