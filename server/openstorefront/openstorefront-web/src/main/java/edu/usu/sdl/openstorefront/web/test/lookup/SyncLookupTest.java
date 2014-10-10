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
package edu.usu.sdl.openstorefront.web.test.lookup;

import edu.usu.sdl.openstorefront.storage.model.LookupEntity;
import edu.usu.sdl.openstorefront.storage.model.TestEntity;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class SyncLookupTest
		extends BaseTestCase
{

	public SyncLookupTest()
	{
		this.description = "Sync Lookup Test";
	}

	@Override
	protected void runInternalTest()
	{
		List<LookupEntity> testEntities = new ArrayList<>();
		Arrays.asList("A", "B").forEach(item -> {
			TestEntity testEntity = new TestEntity();
			testEntity.setCode(item);
			testEntity.setDescription(item + " - Description");
			testEntity.setCreateUser(TEST_USER);
			testEntity.setUpdateUser(TEST_USER);

			testEntities.add(testEntity);
		});
		service.getLookupService().syncLookupImport(TestEntity.class, testEntities);
		LookupEntity lookupEntity = service.getLookupService().getLookupEnity(TestEntity.class, "A");
		if (lookupEntity == null) {
			failureReason.append("Unable to find look up - A");
		}

		List<LookupEntity> testEntities2 = new ArrayList<>();
		Arrays.asList("A", "C").forEach(item -> {
			TestEntity testEntity = new TestEntity();
			testEntity.setCode(item);
			testEntity.setDescription(item + " - Description");
			testEntity.setCreateUser(TEST_USER);
			testEntity.setUpdateUser(TEST_USER);

			testEntities2.add(testEntity);
		});
		service.getLookupService().syncLookupImport(TestEntity.class, testEntities2);
		lookupEntity = service.getLookupService().getLookupEnity(TestEntity.class, "C");
		if (lookupEntity == null) {
			failureReason.append("Unable to find look up - C");
		}
		lookupEntity = service.getLookupService().getLookupEnity(TestEntity.class, "B");
		if (lookupEntity != null) {
			failureReason.append("Found look up - B");
		}

		results.append("Clean up records").append("<br>");
		results.append(service.getPersistenceService().deleteByExample(new TestEntity())).append(" records removed.<br>");
	}

}
