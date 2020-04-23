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
package edu.usu.sdl.openstorefront.service.job;

import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.UnableToInterruptJobException;

/**
 * Sync Workplans with entities
 * 
 * <code>@see{WorkPlanServiceImpl.java#removeWorkPlan}</code> has a need to kill
 * this job so that it does not interfere with it's operation. 
 *
 * @author dshurtleff
 */
@DisallowConcurrentExecution
public class WorkPlanSyncJob
		extends BaseJob
		implements InterruptableJob
{

	private static final Logger LOG = Logger.getLogger(WorkPlanSyncJob.class.getName());
	
	private volatile boolean interrupted = false;

	@Override
	protected void executeInternaljob(JobExecutionContext context)
	{
		//must have at least one workPlan
		WorkPlan workPlanExample = new WorkPlan();
		long count = service.getPersistenceService().countByExample(workPlanExample);
		if (count > 0) {
			LOG.log(Level.FINER, "Starting WorkPlan Sync");
			service.getWorkPlanService().syncWorkPlanLinks(this);
			LOG.log(Level.FINER, "Done WorkPlan Sync");
		} else {
			LOG.log(Level.FINE, "No WorkPlan Availble; Make sure defaults exist.");
		}
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException
	{
		LOG.log(Level.FINE, "A WorkPlanSync Job has been interrupted and ordered to die! This may happen if someone deletes a WorkPlan while the Job is running");
		interrupted = true;
	}
	
	public boolean getIsInterrupted(){
		return interrupted;
	}
	
	public void resetInterruption(){
		interrupted = true;
	}

}
