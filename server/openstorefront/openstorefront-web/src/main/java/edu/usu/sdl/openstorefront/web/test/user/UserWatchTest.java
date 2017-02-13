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
package edu.usu.sdl.openstorefront.web.test.user;

import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.UserWatch;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;

/**
 *
 * @author dshurtleff
 */
public class UserWatchTest
		extends BaseTestCase
{

	private UserWatch userWatch = null;
	ComponentAll componentAll = null;

	@Override
	protected void initializeTest()
	{
		super.initializeTest();
		results.append("Create component").append("<br>");
		componentAll = getTestComponent();
	}

	@Override
	protected void runInternalTest()
	{
		results.append("Create watch").append("<br>");
		userWatch = new UserWatch();
		userWatch.setUsername(TEST_USER);
		userWatch.setComponentId(componentAll.getComponent().getComponentId());
		userWatch.setNotifyFlg(true);
		userWatch.setLastViewDts(TimeUtil.currentDate());
		userWatch.setCreateUser(TEST_USER);
		userWatch.setUpdateUser(TEST_USER);
		userWatch.setActiveStatus(UserWatch.ACTIVE_STATUS);
		userWatch = service.getUserService().saveWatch(userWatch);

		//read watch
		results.append("Read watch").append("<br>");
		userWatch = service.getUserService().getWatch(userWatch.getUserWatchId());

	}

	@Override
	public String getDescription()
	{
		return "User Watch";
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
		if (userWatch != null) {
			results.append("Delete watch").append("<br>");
			service.getUserService().deleteWatch(userWatch.getUserWatchId());
		}
	}

}
