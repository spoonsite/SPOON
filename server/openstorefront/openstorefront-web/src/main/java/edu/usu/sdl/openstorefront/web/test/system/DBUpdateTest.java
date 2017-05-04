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
package edu.usu.sdl.openstorefront.web.test.system;

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.TestEntity;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class DBUpdateTest
		extends BaseTestCase
{

	@Override
	protected void runInternalTest()
	{
		Arrays.asList("A", "B").forEach(item -> {
			TestEntity testEntity = new TestEntity();
			testEntity.setCode(item);
			testEntity.setDescription(item + " - Description");
			testEntity.setActiveStatus(TestEntity.ACTIVE_STATUS);
			testEntity.setCreateUser(TEST_USER);
			testEntity.setUpdateUser(TEST_USER);

			service.getLookupService().saveLookupValue(testEntity);
		});
		results.append("Saved A, B").append("<br>");

		TestEntity testEntityExampleWhere = new TestEntity();
		testEntityExampleWhere.setCode("A");

		TestEntity testEntityUpdate = new TestEntity();
		testEntityUpdate.setActiveStatus(TestEntity.INACTIVE_STATUS);
		testEntityUpdate.setUpdateUser(TEST_USER);
		testEntityUpdate.setUpdateDts(TimeUtil.currentDate());
		service.getPersistenceService().updateByExample(TestEntity.class, testEntityUpdate, testEntityExampleWhere);

		List<TestEntity> tests = service.getPersistenceService().queryByExample(new TestEntity());
		tests.forEach(test -> {
			results.append(StringProcessor.printObject(test)).append("<br><br>");
		});

	}

	@Override
	public String getDescription()
	{
		return "DB Update Test";
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
		results.append("Clean up records").append("<br>");
		results.append(service.getPersistenceService().deleteByExample(new TestEntity())).append(" records removed.<br>");
	}

}
