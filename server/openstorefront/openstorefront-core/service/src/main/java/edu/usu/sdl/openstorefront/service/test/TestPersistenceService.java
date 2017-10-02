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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author kbair
 */
public class TestPersistenceService implements PersistenceService
{

	// <editor-fold defaultstate="collapsed" desc="Methods for managing the Test data set">
	private static Map<Class<?>, Queue<List>> listResultsMap = new HashMap<>();
	private static Map<Class<?>, Queue<BaseEntity>> singleResultsMap = new HashMap<>();
	private static Map<String, Queue<List>> listQueryResultsMap = new HashMap<>();
	private static Map<Class<?>, Queue<BaseEntity>> listExampleMap = new HashMap<>();
	
public static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
    try {
        return clazz.cast(o);
    } catch(ClassCastException e) {
        return null;
    }
}
	public void clear()
	{
		listResultsMap = new HashMap<>();
		listExampleMap = new HashMap<>();
		listQueryResultsMap = new HashMap<>();
		singleResultsMap = new HashMap<>();
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
		if (!listResultsMap.containsKey(cls)) {
			listResultsMap.put(cls, new LinkedList<>());
		}
		listResultsMap.get(cls).add(queryResults);
	}
	
	public void addObject(BaseEntity baseEntity)
	{
		if (!singleResultsMap.containsKey(baseEntity.getClass())) {
			singleResultsMap.put(baseEntity.getClass(), new LinkedList<>());
		}
		singleResultsMap.get(baseEntity.getClass()).add(baseEntity);
	}


	private void addListExample(BaseEntity example)
	{
		if (!listExampleMap.containsKey(example.getClass())) {
			listExampleMap.put(example.getClass(), new LinkedList<>());
		}
		listExampleMap.get(example.getClass()).add(example);
	}

	public Queue<BaseEntity> getListExamples(Class<?> cls)
	{
		Queue<BaseEntity> result = new LinkedList<>();
		if (listExampleMap.containsKey(cls)
				&& (!listExampleMap.get(cls).isEmpty())) {
			result = listExampleMap.get(cls);
		}
		return result;
	}
	// </editor-fold>

	@Override
	public <T> List<T> queryByExample(BaseEntity baseEntity)
	{
		addListExample(baseEntity);
		List<T> result = new ArrayList<>();
		if (listResultsMap.containsKey(baseEntity.getClass())
				&& (!listResultsMap.get(baseEntity.getClass()).isEmpty())) {
			result = listResultsMap.get(baseEntity.getClass()).poll();
		}
		return result;
	}

	@Override
	public <T> List<T> queryByExample(QueryByExample queryByExample)
	{
		return queryByExample(queryByExample.getExample());
	}
	
	@Override
	public <T> T queryOneByExample(BaseEntity baseEntity)
	{		
		Object result = null;
		if (singleResultsMap.containsKey(baseEntity.getClass())
				&& (!singleResultsMap.get(baseEntity.getClass()).isEmpty())) {
			result = singleResultsMap.get(baseEntity.getClass()).poll();
		}
		return (T)(Object)result;
	}
	
	
	@Override
	public <T> T queryOneByExample(QueryByExample queryByExample)
	{
		return queryOneByExample(queryByExample.getExample());
	}
	
	@Override
	public <T> List<T> query(String query, Map<String, Object> parameterMap)
	{
		List<T> result = new ArrayList<>();
		if (listQueryResultsMap.containsKey(query)
				&& (!listQueryResultsMap.get(query).isEmpty())) {
			result = listQueryResultsMap.get(query).poll();
		}
		return result;
	}

	@Override
	public <T> List<T> query(String query, Map<String, Object> parameterMap, boolean unwrap)
	{
		// NOTE: (KB) currently I don't see a difference between wrapped and unwrapped 
		// objects in testing if that changes then this may need refactored
		return query(query, parameterMap);
	}

	// <editor-fold defaultstate="collapsed" desc="PersistenceService Interface methods that are not supported yet.">
	@Override
	public void begin()
	{
		throw new UnsupportedOperationException("Not supported yet. begin()");
	}

	@Override
	public void commit()
	{
		throw new UnsupportedOperationException("Not supported yet. commit()");
	}

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
	public <T> int deleteByExample(BaseEntity example)
	{
		throw new UnsupportedOperationException("Not supported yet. deleteByExample(BaseEntity example)");
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
	public void endTransaction()
	{
		throw new UnsupportedOperationException("Not supported yet. endTransaction()");
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey)
	{
		throw new UnsupportedOperationException("Not supported yet. find(Class<T> entityClass, Object primaryKey)");
	}

	@Override
	public <T> T findById(Class<T> entity, Object id)
	{
		throw new UnsupportedOperationException("Not supported yet. findById(Class<T> entity, Object id)");
	}

	@Override
	public String generateId()
	{
		throw new UnsupportedOperationException("Not supported yet. generateId()");
	}

	@Override
	public boolean isManaged(BaseEntity baseEntity)
	{
		throw new UnsupportedOperationException("Not supported yet. isManaged(BaseEntity baseEntity)");
	}

	@Override
	public boolean isProxy(BaseEntity baseEntity)
	{
		throw new UnsupportedOperationException("Not supported yet. isProxy(BaseEntity baseEntity)");
	}

	@Override
	public boolean isTransactionActive()
	{
		throw new UnsupportedOperationException("Not supported yet. isTransactionActive()");
	}

	@Override
	public <T extends BaseEntity> T persist(T entity)
	{
		throw new UnsupportedOperationException("Not supported yet. persist(T entity)");
	}

	@Override
	public void rollback()
	{
		throw new UnsupportedOperationException("Not supported yet. rollback()");
	}

	@Override
	public <T> int runDbCommand(String query, Map<String, Object> queryParams)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public <T> T saveNonBaseEntity(T entity)
	{
		throw new UnsupportedOperationException("Not supported yet. saveNonBaseEntity(T entity)");
	}

	@Override
	public <T extends BaseEntity> T saveNonPkEntity(T entity)
	{
		throw new UnsupportedOperationException("Not supported yet. saveNonPkEntity(T entity)");
	}

	@Override
	public <T extends StandardEntity> T setStatusOnEntity(Class<T> entity, Object id, String activeStatus)
	{
		throw new UnsupportedOperationException("Not supported yet. setStatusOnEntity(Class<T> entity, Object id, String activeStatus)");
	}

	@Override
	public <T> List<T> unwrapProxy(List<T> data)
	{
		throw new UnsupportedOperationException("Not supported yet. unwrapProxy(List<T> data)");
	}

	@Override
	public <T> T unwrapProxyObject(T data)
	{
		throw new UnsupportedOperationException("Not supported yet. unwrapProxyObject(T data)");
	}

	@Override
	public <T> int updateByExample(Class<T> entityClass, T exampleSet, T exampleWhere)
	{
		throw new UnsupportedOperationException("Not supported yet. updateByExample(Class<T> entityClass, T exampleSet, T exampleWhere)");
	}
	// </editor-fold>

}
