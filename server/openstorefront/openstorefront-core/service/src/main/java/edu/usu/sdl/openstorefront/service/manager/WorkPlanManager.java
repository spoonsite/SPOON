/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.manager;

import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.model.EntityEventRegistrationModel;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.job.WorkPlanSyncJob;
import edu.usu.sdl.openstorefront.service.manager.model.AddJobModel;
import edu.usu.sdl.openstorefront.service.workplan.WorkPlanEngine;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang.StringUtils;

/**
 * Handles Workplan Initializing
 *
 * @author dshurtleff
 */
public class WorkPlanManager
		implements Initializable
{

	private static final String JOB_NAME = "WorkPlanSync";
	private static final Integer JOB_INTERVAL_MINUTES = 10;

	private static WorkPlanManager singleton = null;
	private AtomicBoolean started = new AtomicBoolean(false);
	private String registrationId;

	//private JobManager jobManager;
	private WorkPlanEngine workPlanEngine;

	private WorkPlanManager()
	{
		this.workPlanEngine = WorkPlanEngine.newWorkPlanEngine();
	}

	public static WorkPlanManager getInstance()
	{
		if (singleton == null) {
			singleton = new WorkPlanManager();
		}
		return singleton;
	}

	@Override
	public void initialize()
	{
		ServiceProxy serviceProxy = ServiceProxy.getProxy();

		//add listener
		EntityEventRegistrationModel registrationModel = new EntityEventRegistrationModel();
		registrationModel.setRegistrationId(StringProcessor.uniqueId());
		registrationModel.setName("WorkPlan Event Handler");
		registrationModel.setListener((entityModel) -> {
			//add hook to handling
			//workPlanEngine
			return true;
		});

		serviceProxy.getEntityEventService().registerEventListener(registrationModel);
		registrationId = registrationModel.getRegistrationId();

		//add job
		AddJobModel addJobModel = new AddJobModel();
		addJobModel.setDescription("Syncs data with workplans");
		addJobModel.setJobGroup(JobManager.JOB_GROUP_SYSTEM);
		addJobModel.setJobName(JOB_NAME);
		addJobModel.setJobClass(WorkPlanSyncJob.class);
		addJobModel.setRepeatForever(true);
		addJobModel.setMinutes(JOB_INTERVAL_MINUTES);
		JobManager.addJob(addJobModel);
	}

	@Override
	public void shutdown()
	{
		ServiceProxy serviceProxy = ServiceProxy.getProxy();

		if (StringUtils.isNotBlank(registrationId)) {
			serviceProxy.getEntityEventService().unregisterEventListener(registrationId);
		}

		JobManager.removeJob(JOB_NAME, JobManager.JOB_GROUP_SYSTEM);
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

}
