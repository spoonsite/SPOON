/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.web.test.system;

import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.TestEntity;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class DBInTest
		extends BaseTestCase
{

	@Override
	protected void initializeTest()
	{
		super.initializeTest();
		Arrays.asList("A", "B", "Apple", "andy", "arrAnge", "Test", "Arch", "BaseTest", "Orange").forEach(item -> {
			TestEntity testEntity = new TestEntity();
			testEntity.setCode(item);
			testEntity.setDescription(item + " - Description");
			testEntity.setCreateUser(TEST_USER);
			testEntity.setUpdateUser(TEST_USER);

			service.getLookupService().saveLookupValue(testEntity);
		});
		results.append("Saved Test Data").append("<br>");

	}

	@Override
	protected void runInternalTest()
	{
		TestEntity testEntityExample = new TestEntity();
		testEntityExample.setCreateUser(TEST_USER);

		QueryByExample<TestEntity> queryByExample = new QueryByExample<>(testEntityExample);

		TestEntity testEntityINExample = new TestEntity();
		testEntityINExample.setCode(QueryByExample.STRING_FLAG);

		queryByExample.setInExample(testEntityINExample);
		queryByExample.getInExampleOption().getParameterValues().add("Apple");
		queryByExample.getInExampleOption().getParameterValues().add("andy");

		List<TestEntity> testEntities = service.getPersistenceService().queryByExample(queryByExample);
		results.append("Results Entities of In :<br>");
		testEntities.forEach(entity -> {
			results.append(entity.getCode()).append("<br>");
		});
		if (testEntities.size() > 2 || testEntities.size() < 2) {
			failureReason.append("Failed to find expected results");
		}

	}

	@Override
	public String getDescription()
	{
		return "DB In Query";
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();

		results.append("Clean up records").append("<br>");
		results.append(service.getPersistenceService().deleteByExample(new TestEntity())).append(" records removed.<br>");
	}

}
