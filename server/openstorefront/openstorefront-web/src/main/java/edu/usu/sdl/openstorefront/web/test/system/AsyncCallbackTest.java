/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.api.model.AsyncTaskCallback;
import edu.usu.sdl.openstorefront.core.api.model.TaskFuture;
import edu.usu.sdl.openstorefront.core.api.model.TaskRequest;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.service.manager.AsyncTaskManager;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class AsyncCallbackTest
		extends BaseTestCase
{

	private static final Logger LOG = Logger.getLogger(AsyncCallbackTest.class.getName());

	private List<String> taskIds = null;

	@Override
	protected void initializeTest()
	{
		super.initializeTest();
		taskIds = new ArrayList<>();
	}

	@Override
	protected void runInternalTest()
	{
		LookupModel lookupModel = new LookupModel();
		lookupModel.setCode(StandardEntity.PENDING_STATUS);

		results.append("Before: ").append(lookupModel.getCode()).append("<br>");

		TaskRequest taskRequest = new TaskRequest();
		taskRequest.setAllowMultiple(true);
		taskRequest.setName(UUID.randomUUID().toString());
		taskRequest.setCallback(new AsyncTaskCallback()
		{

			@Override
			public void beforeExecute(TaskFuture taskFuture)
			{
			}

			@Override
			public void afterExecute(TaskFuture taskFuture)
			{
				if (OpenStorefrontConstant.TaskStatus.FAILED.equals(taskFuture.getStatus())) {
					lookupModel.setCode(StandardEntity.ACTIVE_STATUS);
				}
			}
		});
		taskRequest.setTask((Callable) () -> {
			lookupModel.setCode(StandardEntity.INACTIVE_STATUS);
			return true;
		});

		TaskFuture taskFuture = AsyncTaskManager.submitTask(taskRequest);

		try {
			//wait for it to complete
			taskFuture.getFuture().get();
		} catch (InterruptedException | ExecutionException ex) {
			LOG.log(Level.SEVERE, null, ex);
			Thread.currentThread().interrupt();
		}
		results.append("After Setting: ").append(lookupModel.getCode()).append("<br>");

		// Used to delete the task in cleanup
		taskIds.add(taskFuture.getTaskId());

		taskRequest.setTask((Callable) () -> {
			throw new OpenStorefrontRuntimeException("Auto Test Failure (This is an Intentional Failure).");
		});
		taskFuture = AsyncTaskManager.submitTask(taskRequest);

		try {
			//wait for it to complete
			taskFuture.getFuture().get();
		} catch (InterruptedException | ExecutionException ex) {
			LOG.log(Level.SEVERE, null, ex);
			Thread.currentThread().interrupt();
		}
		results.append("After Fail: ").append(lookupModel.getCode()).append("<br>");

		// Used to delete the task in cleanup
		taskIds.add(taskFuture.getTaskId());
	}

	@Override
	public String getDescription()
	{
		return "Async Callback Test";
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();

		if (!taskIds.isEmpty()) {
			for (String taskId : taskIds) {
				AsyncTaskManager.deleteTask(taskId);
			}
		}

	}
}
