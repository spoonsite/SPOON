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

import edu.usu.sdl.openstorefront.core.api.LookupService;
import edu.usu.sdl.openstorefront.core.api.model.AsyncTaskCallback;
import edu.usu.sdl.openstorefront.core.api.model.TaskFuture;
import edu.usu.sdl.openstorefront.core.api.model.TaskRequest;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.service.manager.AsyncTaskManager;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author dshurtleff
 */
public class AsyncProxyTest
		extends BaseTestCase
{

	private List<ErrorTypeCode> errorTypeCodes = new ArrayList<>();

	public AsyncProxyTest()
	{
		this.description = "Async Proxy Test";
	}

	@Override
	protected void runInternalTest()
	{
		results.append("Call back style: <br>");
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
				try {
					results.append("Runnning in callback: <br>");
					List<ErrorTypeCode> errorTypeCodesLocal = (List<ErrorTypeCode>) taskFuture.getFuture().get();
					errorTypeCodesLocal.forEach(code -> {
						results.append(code.getCode()).append(" - ").append(code.getDescription()).append("<br>");
					});

				} catch (InterruptedException | ExecutionException ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		LookupService asyncLookup = service.getAsyncProxy(service.getLookupService(), taskRequest);
		asyncLookup.findLookup(ErrorTypeCode.class);

		results.append("Lookup style: <br>");
		TaskFuture taskFuture = AsyncTaskManager.getTaskByName(taskRequest.getName());
		try {
			errorTypeCodes = (List<ErrorTypeCode>) taskFuture.getFuture().get();
			errorTypeCodes.forEach(code -> {
				results.append(code.getCode()).append(" - ").append(code.getDescription()).append("<br>");
			});
			Thread.sleep(100);
		} catch (InterruptedException | ExecutionException ex) {
			throw new RuntimeException(ex);
		}

	}

}
