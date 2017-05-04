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

import edu.usu.sdl.openstorefront.core.entity.TestEntity;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.Arrays;

/**
 *
 * @author dshurtleff
 */
public class DBSaveTest
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

		Arrays.asList("C", "D", "E").forEach(item -> {
			TestEntity testEntity = new TestEntity();
			testEntity.setCode(item);
			testEntity.setDescription(item + " - Description");
			testEntity.setActiveStatus(TestEntity.INACTIVE_STATUS);
			testEntity.setCreateUser(TEST_USER);
			testEntity.setUpdateUser(TEST_USER);

			TestEntity newSave = testEntity.save();
			addResultsLines("New Save - " + newSave.getDescription());
		});
		results.append("Saved C, D, E").append("<br>");

	}

	@Override
	public String getDescription()
	{
		return "DB Save Test";
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
		results.append("Clean up records").append("<br>");
		results.append(service.getPersistenceService().deleteByExample(new TestEntity())).append(" records removed.<br>");
	}
}
