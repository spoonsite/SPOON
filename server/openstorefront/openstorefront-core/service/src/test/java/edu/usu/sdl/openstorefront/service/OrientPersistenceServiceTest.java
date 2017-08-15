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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.QueryType;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.TestEntity;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author kbair
 */
public class OrientPersistenceServiceTest
{

	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQueryBasicSelect()
	{
		System.out.println("generateQuery");
		TestEntity example = new TestEntity();
		QueryByExample queryByExample = new QueryByExample(example);
		String expQuery = "select   from TestEntity";
		Map<String, Object> expPrams = new HashMap<>();
		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		Assert.assertEquals(expPrams, result.getValue());
	}

	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQueryCount()
	{
		System.out.println("generateQuery");
		TestEntity example = new TestEntity();
		QueryByExample queryByExample = new QueryByExample(example);
		queryByExample.setQueryType(QueryType.COUNT);
		try {
			AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
			Assert.fail("Expected OpenStorefrontRuntimeException");
		} catch (OpenStorefrontRuntimeException e) {
			Assert.assertEquals("Query Type unsupported: COUNT", e.getMessage());
			Assert.assertEquals("Only supports select", e.getPotentialResolution());
		}
	}

	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQuerySimpleWhere()
	{
		System.out.println("generateQuery");
		TestEntity example = new TestEntity();
		example.setCode("TestTestEntity");
		QueryByExample queryByExample = new QueryByExample(example);
		String expQuery = "select   from TestEntity where  code = :codeParam";
		Map<String, Object> expPrams = new HashMap<>();
		expPrams.put("codeParam", "TestTestEntity");
		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		Assert.assertEquals(expPrams, result.getValue());
	}

	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQueryLike()
	{
		System.out.println("generateQuery");
		TestEntity example = new TestEntity();
		example.setCode("A" + QueryByExample.LIKE_SYMBOL);

		QueryByExample queryByExample = new QueryByExample(new TestEntity());
		queryByExample.setLikeExample(example);

		String expQuery = "select   from TestEntity where  code LIKE :codeParam";
		Map<String, Object> expPrams = new HashMap<>();
		expPrams.put("codeParam", "A%");

		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		Assert.assertEquals(expPrams, result.getValue());
	}

	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQuerySpecialOperatorLike()
	{
		System.out.println("generateQuery");
		QueryByExample queryByExample = new QueryByExample(new TestEntity());

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		// Define A Special Lookup Operation (ILIKE)
		TestEntity componentLikeExample = new TestEntity();
		componentLikeExample.setCode("A" + QueryByExample.LIKE_SYMBOL);
		specialOperatorModel.setExample(componentLikeExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LIKE);

		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		String expQuery = "select   from TestEntity where  code LIKE :codeParam";
		Map<String, Object> expPrams = new HashMap<>();
		expPrams.put("codeParam", "A%");

		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		Assert.assertEquals(expPrams, result.getValue());
	}

	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQueryComplexWhere()
	{
		System.out.println("generateQuery");
		TestEntity example = new TestEntity();
		example.setActiveStatus("A");
		QueryByExample queryByExample = new QueryByExample(example);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		// Define A Special Lookup Operation (ILIKE)
		TestEntity componentLikeExample = new TestEntity();
		componentLikeExample.setCode(QueryByExample.LIKE_SYMBOL + "Test" + QueryByExample.LIKE_SYMBOL);
		specialOperatorModel.setExample(componentLikeExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LIKE);

		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		String expQuery = "select   from TestEntity where  activeStatus = :activeStatusParam AND  code LIKE :codeParam";
		Map<String, Object> expPrams = new HashMap<>();
		expPrams.put("activeStatusParam", "A");
		expPrams.put("codeParam", "%Test%");
		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		Assert.assertEquals(expPrams, result.getValue());
	}



	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQueryIn()
	{
		System.out.println("generateQuery");
		TestEntity componentInExample = new TestEntity();
		componentInExample.setCode(QueryByExample.STRING_FLAG);
		
		QueryByExample queryByExample = new QueryByExample(new TestEntity());
		List<String> values = new ArrayList<>();
		values.add("CODE1");
		values.add("CODE2");
		values.add("CODE3");
		queryByExample.setInExample(componentInExample);
		queryByExample.getInExampleOption().setParameterValues(values);
		String expQuery = "select   from TestEntity where  code  IN ( :codeParam0, :codeParam1, :codeParam2 )";
		Map<String, Object> expPrams = new HashMap<>();
		expPrams.put("codeParam0", "CODE1");
		expPrams.put("codeParam1", "CODE2");
		expPrams.put("codeParam2", "CODE3");

		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		Assert.assertEquals(expPrams, result.getValue());
	}

	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQuerySpecialOperatorIn()
	{
		System.out.println("generateQuery");
		List<String> values = new ArrayList<>();
		values.add("CODE1");
		values.add("CODE2");
		values.add("CODE3");
		
		QueryByExample queryByExample = new QueryByExample(new TestEntity());

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		// Define A Special Lookup Operation (IN)
		TestEntity componentInExample = new TestEntity();
		componentInExample.setCode(QueryByExample.STRING_FLAG);
		specialOperatorModel.setExample(componentInExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_IN);
		specialOperatorModel.getGenerateStatementOption().setParameterValues(values);

		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		String expQuery = "select   from TestEntity where  code  IN ( :codeParam0, :codeParam1, :codeParam2 )";
		Map<String, Object> expPrams = new HashMap<>();
		expPrams.put("codeParam0", "CODE1");
		expPrams.put("codeParam1", "CODE2");
		expPrams.put("codeParam2", "CODE3");

		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		Assert.assertEquals(expPrams, result.getValue());
	}

	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQueryGroupBy()
	{
		System.out.println("generateQuery");
		QueryByExample queryByExample = new QueryByExample(new TestEntity());
		TestEntity groupByExample = new TestEntity();
		groupByExample.setCode(QueryByExample.STRING_FLAG);
		queryByExample.setGroupBy(groupByExample);

		String expQuery = "select   from TestEntity group by  code";
		Map<String, Object> expPrams = new HashMap<>();
		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		Assert.assertEquals(expPrams, result.getValue());
	}

	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQueryOrderBy()
	{
		System.out.println("generateQuery");
		QueryByExample queryByExample = new QueryByExample(new TestEntity());
		TestEntity groupByExample = new TestEntity();
		groupByExample.setCode(QueryByExample.STRING_FLAG);
		queryByExample.setOrderBy(groupByExample);
		String expQuery = "select   from TestEntity order by  code ASC";
		Map<String, Object> expPrams = new HashMap<>();
		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		Assert.assertEquals(expPrams, result.getValue());
	}
	
	
	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQueryPaging()
	{
		System.out.println("generateQuery");
		QueryByExample queryByExample = new QueryByExample(new TestEntity());
		queryByExample.setFirstResult(10);
		queryByExample.setMaxResults(100);
		String expQuery = "select   from TestEntity SKIP 10 LIMIT 100";
		Map<String, Object> expPrams = new HashMap<>();
		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		Assert.assertEquals(expPrams, result.getValue());
	}
	
	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQuerySetTimeout()
	{
		System.out.println("generateQuery");
		QueryByExample queryByExample = new QueryByExample(new TestEntity());
		queryByExample.setTimeout(1000);
		String expQuery = "select   from TestEntity TIMEOUT 1000 RETURN";
		Map<String, Object> expPrams = new HashMap<>();
		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		Assert.assertEquals(expPrams, result.getValue());
	}
	
	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQueryParallel()
	{
		System.out.println("generateQuery");
		QueryByExample queryByExample = new QueryByExample(new TestEntity());
		queryByExample.setParallelQuery(true);
		String expQuery = "select   from TestEntity PARALLEL ";
		Map<String, Object> expPrams = new HashMap<>();
		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		Assert.assertEquals(expPrams, result.getValue());
	}
}
