/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.test;

import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

/**
 * TODO: look into using orient with in memory option depends on
 * startup/teardown time
 *
 * @author kbair
 */
public class TestPersistenceService implements PersistenceService
{

	// <editor-fold defaultstate="collapsed" desc="Methods for managing the Test data set">
	private static Map<String, Queue<List>> listResultsMap = new HashMap<>();
	private static Map<String, Queue<BaseEntity>> singleResultsMap = new HashMap<>();
	private static Map<String, Queue<List>> listQueryResultsMap = new HashMap<>();
	private static Map<String, Queue<BaseEntity>> listExampleMap = new HashMap<>();
	private static Map<String, Map<Object, Queue<BaseEntity>>> byIdMap = new HashMap<>();

	public void clear()
	{
		listResultsMap = new HashMap<>();
		listExampleMap = new HashMap<>();
		listQueryResultsMap = new HashMap<>();
		singleResultsMap = new HashMap<>();
		byIdMap = new HashMap<>();
	}

	public void addQuery(String query, List queryResults)
	{
		if (!listQueryResultsMap.containsKey(query)) {
			listQueryResultsMap.put(query, new LinkedList<>());
		}
		listQueryResultsMap.get(query).add(queryResults);
	}

	public void addQuery(Class<?> cls, List queryResults)
	{
		if ((queryResults.size() > 0) && (!cls.isInstance(queryResults.get(0)))) {
			throw new IllegalArgumentException("List Contents do not match expected type: " + cls.getName());
		}
		if (!listResultsMap.containsKey(cls.getName())) {
			listResultsMap.put(cls.getName(), new LinkedList<>());
		}
		listResultsMap.get(cls.getName()).add(queryResults);
	}

	public void addObject(BaseEntity baseEntity)
	{
		if (!singleResultsMap.containsKey(baseEntity.getClass().getName())) {
			singleResultsMap.put(baseEntity.getClass().getName(), new LinkedList<>());
		}
		singleResultsMap.get(baseEntity.getClass().getName()).add(baseEntity);
	}

	public void addObjectWithId(Class<?> cls, Object id, BaseEntity baseEntity)
	{
		if (!byIdMap.containsKey(cls.getName())) {
			byIdMap.put(cls.getName(), new HashMap<>());
		}
		if (!byIdMap.get(cls.getName()).containsKey(id)) {
			byIdMap.get(cls.getName()).put(id, new LinkedList<>());
		}
		byIdMap.get(cls.getName()).get(id).add(baseEntity);
	}

	private void addListExample(BaseEntity example)
	{
		if (!listExampleMap.containsKey(example.getClass().getName())) {
			listExampleMap.put(example.getClass().getName(), new LinkedList<>());
		}
		listExampleMap.get(example.getClass().getName()).add(example);
	}

	public Queue<BaseEntity> getListExamples(Class<?> cls)
	{
		return (listExampleMap.containsKey(cls.getName()) && (!listExampleMap.get(cls.getName()).isEmpty())) ? listExampleMap.get(cls.getName()) : null;
	}
	// </editor-fold>

	@Override
	public <T> List<T> queryByExample(BaseEntity baseEntity)
	{
		addListExample(baseEntity);
		return (listResultsMap.containsKey(baseEntity.getClass().getName()) && (!listResultsMap.get(baseEntity.getClass().getName()).isEmpty())) ? listResultsMap.get(baseEntity.getClass().getName()).poll() : null;
	}

	@Override
	public <T> List<T> queryByExample(QueryByExample queryByExample)
	{
		return queryByExample(queryByExample.getExample());
	}

	@Override
	public <T> T queryOneByExample(BaseEntity baseEntity)
	{
		return (singleResultsMap.containsKey(baseEntity.getClass().getName()) && (!singleResultsMap.get(baseEntity.getClass().getName()).isEmpty())) ? (T) singleResultsMap.get(baseEntity.getClass().getName()).poll() : null;
	}

	@Override
	public <T> T queryOneByExample(QueryByExample queryByExample)
	{
		return queryOneByExample(queryByExample.getExample());
	}

	@Override
	public <T> List<T> query(String query, Map<String, Object> parameterMap)
	{
		return (listQueryResultsMap.containsKey(query) && (!listQueryResultsMap.get(query).isEmpty())) ? listQueryResultsMap.get(query).poll() : null;
	}

	@Override
	public <T> List<T> query(String query, Map<String, Object> parameterMap, boolean unwrap)
	{
		// NOTE: (KB) currently I don't see a difference between wrapped and unwrapped
		// objects in testing if that changes then this may need refactored
		return query(query, parameterMap);
	}

	@Override
	public <T> T findById(Class<T> entity, Object id)
	{
		return (byIdMap.containsKey(entity.getName()) && byIdMap.get(entity.getName()).containsKey(id) && !byIdMap.get(entity.getName()).get(id).isEmpty()) ? (T) byIdMap.get(entity.getName()).get(id).poll() : null;
	}

	@Override
	public <T> int deleteByExample(BaseEntity example)
	{
		// We may want to save the examples so we can check them in Asserts
		return 0;
	}

	@Override
	public <T extends BaseEntity> T persist(T entity)
	{
		// We may want to save the entities so we can check them in Asserts
		return entity;
	}

	@Override
	public String generateId()
	{
		return UUID.randomUUID().toString();
	}

	// <editor-fold defaultstate="collapsed" desc="Transactions: Currently no need to support transactions">
	@Override
	public void begin()
	{
		// Do Nothing
	}

	@Override
	public void endTransaction()
	{
		// Do Nothing
	}

	@Override
	public void commit()
	{
		// Do Nothing
	}

	@Override
	public void rollback()
	{
		// Do Nothing
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Proxy: No difference between wrapped and unwrapped objects in testing">
	@Override
	public <T> List<T> unwrapProxy(List<T> data)
	{
		return data;
	}

	@Override
	public <T> T unwrapProxyObject(T data)
	{
		return data;
	}

	@Override
	public boolean isProxy(BaseEntity baseEntity)
	{
		return false;
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="PersistenceService Interface methods that are not supported yet.">
	@Override
	public long countByExample(BaseEntity example)
	{
		throw new UnsupportedOperationException("Not supported yet. countByExample(BaseEntity example)");
	}

	@Override
	public long countByExample(QueryByExample queryByExample)
	{
		throw new UnsupportedOperationException("Not supported yet. countByExample(QueryByExample queryByExample)");
	}

	@Override
	public long countClass(Class entityClass)
	{
		throw new UnsupportedOperationException("Not supported yet. countClass(Class entityClass)");
	}

	@Override
	public <T> T deattachAll(T entity)
	{
		throw new UnsupportedOperationException("Not supported yet. deattachAll(T entity)");
	}

	@Override
	public <T> void delete(T entity)
	{
		throw new UnsupportedOperationException("Not supported yet. delete(T entity)");
	}

	@Override
	public <T> int deleteByExample(QueryByExample queryByExample)
	{
		throw new UnsupportedOperationException("Not supported yet. deleteByExample(QueryByExample queryByExample)");
	}

	@Override
	public int deleteByQuery(Class entityClass, String whereClause, Map<String, Object> queryParams)
	{
		throw new UnsupportedOperationException("Not supported yet. deleteByQuery(Class entityClass, String whereClause, Map<String, Object> queryParams)");
	}

	@Override
	public <T> T detach(T entity)
	{
		throw new UnsupportedOperationException("Not supported yet. detach(T entity)");
	}

	@Override
	public boolean isManaged(BaseEntity baseEntity)
	{
		throw new UnsupportedOperationException("Not supported yet. isManaged(BaseEntity baseEntity)");
	}

	@Override
	public boolean isTransactionActive()
	{
		throw new UnsupportedOperationException("Not supported yet. isTransactionActive()");
	}

	@Override
	public <T> int runDbCommand(String query, Map<String, Object> queryParams)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public <T extends StandardEntity> T setStatusOnEntity(Class<T> entity, Object id, String activeStatus)
	{
		throw new UnsupportedOperationException("Not supported yet. setStatusOnEntity(Class<T> entity, Object id, String activeStatus)");
	}

	@Override
	public <T> int updateByExample(Class<T> entityClass, T exampleSet, T exampleWhere)
	{
		throw new UnsupportedOperationException("Not supported yet. updateByExample(Class<T> entityClass, T exampleSet, T exampleWhere)");
	}
	// </editor-fold>

}
