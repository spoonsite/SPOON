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
import edu.usu.sdl.openstorefront.core.api.query.WhereClauseGroup;
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
		String expQuery = "select   from TestEntity where  code  IN [ :codeParam0, :codeParam1, :codeParam2 ]";
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

		String expQuery = "select   from TestEntity where  code  IN [ :codeParam0, :codeParam1, :codeParam2 ]";
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
	public void testGenerateQueryNotIn()
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
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_NOT_IN);
		specialOperatorModel.getGenerateStatementOption().setParameterValues(values);

		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		String expQuery = "select   from TestEntity where  code  NOT IN [ :codeParam0, :codeParam1, :codeParam2 ]";
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
		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		assertMapEquals(new HashMap<>(), result.getValue());
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
		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		assertMapEquals(new HashMap<>(), result.getValue());
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
		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		assertMapEquals(new HashMap<>(), result.getValue());
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
		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		assertMapEquals(new HashMap<>(), result.getValue());
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
		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		assertMapEquals(new HashMap<>(), result.getValue());
	}

	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQueryWhereClauseGroup()
	{
		System.out.println("generateQuery");
		String expQuery = "select   from TestEntity where  activeStatus = :activeStatusParam AND ( code = :codeParam OR code = :codeParam2 )";
		Map<String, String> expPrams = new HashMap<>();
		expPrams.put("activeStatusParam", "A");
		expPrams.put("codeParam", "TestCode");
		expPrams.put("codeParam2", "TestCode2");

		TestEntity example = new TestEntity();
		example.setActiveStatus(TestEntity.ACTIVE_STATUS);

		SpecialOperatorModel groupItem1 = new SpecialOperatorModel();
		TestEntity codeExample1 = new TestEntity();
		codeExample1.setCode("TestCode");
		groupItem1.setExample(codeExample1);

		SpecialOperatorModel groupItem2 = new SpecialOperatorModel();
		TestEntity codeExample2 = new TestEntity();
		codeExample2.setCode("TestCode2");
		groupItem2.setExample(codeExample2);
		groupItem2.getGenerateStatementOption().setParameterSuffix("Param2");

		WhereClauseGroup group = new WhereClauseGroup();
		group.getStatementOption().setCondition(GenerateStatementOption.CONDITION_OR);

		group.getExtraWhereClause().add(groupItem1);
		group.getExtraWhereClause().add(groupItem2);

		QueryByExample queryByExample = new QueryByExample(example);
		queryByExample.getExtraWhereCauses().add(group);

		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		assertMapEquals(expPrams, result.getValue());
	}

	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQueryNestedWhereClauseGroups()
	{
		System.out.println("generateQuery");
		String expQuery = "select   from TestEntity where  activeStatus = :activeStatusParam AND (( code = :codeParam OR code = :codeParam2 ) AND ( sortOrder  >= :sortOrderParam AND  sortOrder <= :sortOrderParam2 ) )";
		Map<String, String> expPrams = new HashMap<>();
		expPrams.put("activeStatusParam", "A");
		expPrams.put("codeParam", "TestCode");
		expPrams.put("codeParam2", "TestCode2");
		expPrams.put("sortOrderParam", "1");
		expPrams.put("sortOrderParam2", "10");

		TestEntity example = new TestEntity();
		example.setActiveStatus(TestEntity.ACTIVE_STATUS);

		SpecialOperatorModel group1Item1 = new SpecialOperatorModel();
		TestEntity codeExample1 = new TestEntity();
		codeExample1.setCode("TestCode");
		group1Item1.setExample(codeExample1);

		SpecialOperatorModel group1Item2 = new SpecialOperatorModel();
		TestEntity codeExample2 = new TestEntity();
		codeExample2.setCode("TestCode2");
		group1Item2.setExample(codeExample2);
		group1Item2.getGenerateStatementOption().setParameterSuffix("Param2");

		WhereClauseGroup innerGroup1 = new WhereClauseGroup();
		innerGroup1.getStatementOption().setCondition(GenerateStatementOption.CONDITION_OR);
		innerGroup1.getExtraWhereClause().add(group1Item1);
		innerGroup1.getExtraWhereClause().add(group1Item2);

		SpecialOperatorModel group2Item1 = new SpecialOperatorModel();
		TestEntity example1 = new TestEntity();
		example1.setSortOrder(1);
		group2Item1.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
		group2Item1.setExample(example1);

		SpecialOperatorModel group2Item2 = new SpecialOperatorModel();
		TestEntity example2 = new TestEntity();
		example2.setSortOrder(10);
		group2Item2.setExample(example2);
		group2Item2.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		group2Item2.getGenerateStatementOption().setParameterSuffix("Param2");

		WhereClauseGroup innerGroup2 = new WhereClauseGroup();
		innerGroup2.getExtraWhereClause().add(group2Item1);
		innerGroup2.getExtraWhereClause().add(group2Item2);

		WhereClauseGroup outerGroup = new WhereClauseGroup();
		outerGroup.getExtraWhereClause().add(innerGroup1);
		outerGroup.getExtraWhereClause().add(innerGroup2);

		QueryByExample queryByExample = new QueryByExample(example);
		queryByExample.getExtraWhereCauses().add(outerGroup);

		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		assertMapEquals(expPrams, result.getValue());
	}

	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQueryMultipleWhereClauseGroups()
	{
		System.out.println("generateQuery");
		String expQuery = "select   from TestEntity where  activeStatus = :activeStatusParam AND ( code = :codeParam OR code = :codeParam2 ) AND ( sortOrder  >= :sortOrderParam AND  sortOrder <= :sortOrderParam2 )";
		Map<String, String> expPrams = new HashMap<>();
		expPrams.put("activeStatusParam", "A");
		expPrams.put("codeParam", "TestCode");
		expPrams.put("codeParam2", "TestCode2");
		expPrams.put("sortOrderParam", "1");
		expPrams.put("sortOrderParam2", "10");

		TestEntity example = new TestEntity();
		example.setActiveStatus(TestEntity.ACTIVE_STATUS);

		SpecialOperatorModel group1Item1 = new SpecialOperatorModel();
		TestEntity codeExample1 = new TestEntity();
		codeExample1.setCode("TestCode");
		group1Item1.setExample(codeExample1);

		SpecialOperatorModel group1Item2 = new SpecialOperatorModel();
		TestEntity codeExample2 = new TestEntity();
		codeExample2.setCode("TestCode2");
		group1Item2.setExample(codeExample2);
		group1Item2.getGenerateStatementOption().setParameterSuffix("Param2");

		WhereClauseGroup group1 = new WhereClauseGroup();
		group1.getStatementOption().setCondition(GenerateStatementOption.CONDITION_OR);
		group1.getExtraWhereClause().add(group1Item1);
		group1.getExtraWhereClause().add(group1Item2);

		SpecialOperatorModel group2Item1 = new SpecialOperatorModel();
		TestEntity example1 = new TestEntity();
		example1.setSortOrder(1);
		group2Item1.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
		group2Item1.setExample(example1);

		SpecialOperatorModel group2Item2 = new SpecialOperatorModel();
		TestEntity example2 = new TestEntity();
		example2.setSortOrder(10);
		group2Item2.setExample(example2);
		group2Item2.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		group2Item2.getGenerateStatementOption().setParameterSuffix("Param2");

		WhereClauseGroup group2 = new WhereClauseGroup();
		group2.getExtraWhereClause().add(group2Item1);
		group2.getExtraWhereClause().add(group2Item2);

		QueryByExample queryByExample = new QueryByExample(example);
		queryByExample.getExtraWhereCauses().add(group1);
		queryByExample.getExtraWhereCauses().add(group2);

		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		assertMapEquals(expPrams, result.getValue());
	}

	/**
	 * Test of generateQuery method, of class OrientPersistenceService.
	 */
	@Test
	public void testGenerateQueryMultipleWhereClauses()
	{
		System.out.println("generateQuery");
		String expQuery = "select   from TestEntity where  activeStatus = :activeStatusParam AND  code = :codeParam AND  createUser = :createUserParam";
		Map<String, String> expPrams = new HashMap<>();
		expPrams.put("activeStatusParam", "A");
		expPrams.put("codeParam", "TestCode");
		expPrams.put("createUserParam", "admin");

		TestEntity example = new TestEntity();
		example.setActiveStatus(TestEntity.ACTIVE_STATUS);

		SpecialOperatorModel extraItem1 = new SpecialOperatorModel();
		TestEntity codeExample = new TestEntity();
		codeExample.setCode("TestCode");
		extraItem1.setExample(codeExample);

		SpecialOperatorModel extraItem2 = new SpecialOperatorModel();
		TestEntity userExample = new TestEntity();
		userExample.setCreateUser("admin");
		extraItem2.setExample(userExample);

		QueryByExample queryByExample = new QueryByExample(example);
		queryByExample.getExtraWhereCauses().add(extraItem1);
		queryByExample.getExtraWhereCauses().add(extraItem2);

		AbstractMap.SimpleEntry<String, Map<String, Object>> result = new OrientPersistenceService().generateQuery(queryByExample);
		Assert.assertEquals(expQuery, result.getKey());
		assertMapEquals(expPrams, result.getValue());
	}

	private void assertMapEquals(Map<String, String> expPrams, Map<String, Object> result)
	{
		//assertEquals does not force the same order on a Map.
		// assert Key set is the same size
		Assert.assertEquals(expPrams.keySet().size(), result.keySet().size());
		expPrams.keySet().forEach(item -> {
			// assert each key is in the results
			Assert.assertTrue(result.containsKey(item));
			// assert values for each key match
			Assert.assertEquals(expPrams.get(item), result.get(item).toString());
		});
	}
}
