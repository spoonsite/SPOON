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
package edu.usu.sdl.openstorefront.web.rest.service;

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import edu.usu.sdl.openstorefront.service.manager.model.JobModel;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import edu.usu.sdl.openstorefront.web.rest.model.JobSchedulerStatus;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Job Management
 *
 * @author dshurtleff
 */
@Path("v1/service/jobs")
@APIDescription("Job Management")
public class JobService
		extends BaseResource
{

	@GET
	@RequireAdmin
	@APIDescription("Retrieves all jobs in scheduler")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(JobModel.class)
	public List<JobModel> getJobs(@BeanParam FilterQueryParams filter)
	{
		List<JobModel> jobModels = JobManager.getAllJobs();
		jobModels = filter.filter(jobModels);
		return jobModels;
	}

	@GET
	@RequireAdmin
	@APIDescription("Retrieves current scheduler status")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(JobSchedulerStatus.class)
	@Path("/status")
	public Response getSchedulerStatus()
	{
		JobSchedulerStatus jobSchedulerStatus = new JobSchedulerStatus();
		jobSchedulerStatus.setStatus(JobManager.status());
		return sendSingleEnityResponse(jobSchedulerStatus);
	}

	@GET
	@RequireAdmin
	@APIDescription("Retrieves a system job name")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(JobModel.class)
	@Path("/{jobname}")
	public Response getJobModel(
			@PathParam("jobname")
			@RequiredParam String jobName)
	{
		List<JobModel> jobModels = JobManager.getAllJobs();
		JobModel jobFound = null;
		for (JobModel job : jobModels) {
			if (job.getJobName().equals(jobName)) {
				jobFound = job;
			}
		}
		return sendSingleEnityResponse(jobFound);
	}

	@POST
	@RequireAdmin
	@APIDescription("Pauses a system Job  (Note this is not persisted.  Restarting the application will restart the scheduler.)")
	@Path("/{jobname}/pause")
	public Response pauseSystmJob(
			@PathParam("jobname")
			@RequiredParam String jobName)
	{
		JobManager.pauseSystemJob(jobName);
		return Response.ok().build();
	}

	@POST
	@RequireAdmin
	@APIDescription("Resumes a system Job")
	@Path("/{jobname}/resume")
	public Response resumeScheduler(
			@PathParam("jobname")
			@RequiredParam String jobName)
	{
		JobManager.resumeSystemJob(jobName);
		return Response.ok().build();
	}

	@POST
	@RequireAdmin
	@APIDescription("Pauses Scheduler  (Note this is not persisted.  Restarting the application will restart the scheduler.)")
	@Path("/pause")
	public Response pauseScheduler()
	{
		JobManager.pauseScheduler();
		return Response.ok().build();
	}

	@POST
	@RequireAdmin
	@APIDescription("Resumes Scheduler")
	@Path("/resume")
	public Response resumeScheduler()
	{
		JobManager.resumeScheduler();
		return Response.ok().build();
	}

}
