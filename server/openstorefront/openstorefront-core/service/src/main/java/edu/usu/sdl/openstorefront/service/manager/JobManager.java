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
package edu.usu.sdl.openstorefront.service.manager;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegration;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.io.ComponentImporter;
import edu.usu.sdl.openstorefront.service.io.HighlightImporter;
import edu.usu.sdl.openstorefront.service.io.LookupImporter;
import edu.usu.sdl.openstorefront.service.job.BaseJob;
import edu.usu.sdl.openstorefront.service.job.ComponentUpdateJob;
import edu.usu.sdl.openstorefront.service.job.ImportJob;
import edu.usu.sdl.openstorefront.service.job.IntegrationJob;
import edu.usu.sdl.openstorefront.service.job.NotificationJob;
import edu.usu.sdl.openstorefront.service.job.RecentChangeNotifyJob;
import edu.usu.sdl.openstorefront.service.job.ScheduledReportCronJob;
import edu.usu.sdl.openstorefront.service.job.ScheduledReportJob;
import edu.usu.sdl.openstorefront.service.job.SystemArchiveJob;
import edu.usu.sdl.openstorefront.service.job.SystemCleanupJob;
import edu.usu.sdl.openstorefront.service.job.TrackingCleanupJob;
import edu.usu.sdl.openstorefront.service.job.UserProfileSyncJob;
import edu.usu.sdl.openstorefront.service.manager.model.AddJobModel;
import edu.usu.sdl.openstorefront.service.manager.model.JobModel;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.jobs.DirectoryScanJob;
import org.quartz.jobs.DirectoryScanListener;

/**
 * Handles Automation jobs
 *
 * @author dshurtleff
 */
public class JobManager
		implements Initializable
{

	private static final Logger LOG = Logger.getLogger(JobManager.class.getName());

	public static final String JOB_GROUP_SYSTEM = AddJobModel.JOB_GROUP_SYSTEM;
	public static final String JOB_GROUP_REPORT = AddJobModel.JOB_GROUP_REPORT;
	private static Scheduler scheduler;
	private static AtomicBoolean started = new AtomicBoolean(false);

	public static void init()
	{
		try {
			StdSchedulerFactory factory;
			try {
				factory = new StdSchedulerFactory(FileSystemManager.getInstance().getConfig("quartz.properties").getPath());
			} catch (Exception ex) {
				LOG.log(Level.CONFIG, "Unable to load quartz.properties...using defaults");
				LOG.log(Level.FINER, null, ex);

				//If it can't load the properties ...fall back to default
				Properties defaultProperties = new Properties();
				defaultProperties.setProperty("org.quartz.scheduler.instanceName", "JobScheduler");
				defaultProperties.setProperty("org.quartz.threadPool.threadCount", "10");
				defaultProperties.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
				factory = new StdSchedulerFactory(defaultProperties);
			}

			scheduler = factory.getScheduler();
			initSystemJobs();
			scheduler.start();
		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Failed to init quartz.", ex);
		}
	}

	private static void initSystemJobs() throws SchedulerException
	{
		LOG.log(Level.FINEST, "Setting up Import Jobs...");
		addImportJob(new LookupImporter(), FileSystemManager.IMPORT_LOOKUP_DIR);
		addImportJob(new HighlightImporter(), FileSystemManager.IMPORT_HIGHLIGHT_DIR);
		addImportJob(new ComponentImporter(), FileSystemManager.IMPORT_COMPONENT_DIR);

		addCleanUpErrorsJob();
		addTrackingCleanUpJob();
		addNotificationJob();
		addRecentChangeNotifyJob();
		addScheduledReportJob();
		addComponentUpdate();
		addComponentIntegrationJobs();
		addUserProfileSyncjob();
		addImportJob();
		addArchiveJob();
	}

	private static void addComponentIntegrationJobs() throws SchedulerException
	{
		LOG.log(Level.INFO, "Adding Integration Jobs");

		ServiceProxy serviceProxy = new ServiceProxy();
		List<ComponentIntegration> integrations = serviceProxy.getComponentService().getComponentIntegrationModels(ComponentIntegration.ACTIVE_STATUS);
		for (ComponentIntegration integration : integrations) {
			addComponentIntegrationJob(integration);
		}
	}

	private static void addComponentIntegrationJob(ComponentIntegration componentIntegration) throws SchedulerException
	{
		ServiceProxy serviceProxy = new ServiceProxy();
		String jobName = "ComponentJob-" + componentIntegration.getComponentId();

		JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_SYSTEM);
		if (scheduler.checkExists(jobKey)) {
			LOG.log(Level.WARNING, MessageFormat.format("Job already Exist: {0} check data", jobName));
		} else {
			JobDetail job = JobBuilder.newJob(IntegrationJob.class)
					.withIdentity("ComponentJob-" + componentIntegration.getComponentId(), JOB_GROUP_SYSTEM)
					.withDescription("Component Integration Job for " + serviceProxy.getComponentService().getComponentName(componentIntegration.getComponentId()))
					.build();

			job.getJobDataMap().put(IntegrationJob.COMPONENT_ID, componentIntegration.getComponentId());
			String cron = componentIntegration.getRefreshRate();
			if (cron == null) {
				cron = serviceProxy.getSystemService().getGlobalIntegrationConfig().getJiraRefreshRate();
			}
			Trigger trigger = newTrigger()
					.withIdentity("ComponentTrigger-" + componentIntegration.getComponentId(), JOB_GROUP_SYSTEM)
					.startNow()
					.withSchedule(cronSchedule(cron))
					.build();

			scheduler.scheduleJob(job, trigger);
		}
	}

	/**
	 * Add or Update job
	 *
	 * @param componentIntegration
	 */
	public static void updateComponentIntegrationJob(ComponentIntegration componentIntegration)
	{
		try {
			removeComponentIntegrationJob(componentIntegration.getComponentId());
			addComponentIntegrationJob(componentIntegration);
		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Unable update Job: " + componentIntegration.getComponentId(), ex);
		}
	}

	public static void removeComponentIntegrationJob(String componentId)
	{
		JobKey jobKey = JobKey.jobKey("ComponentJob-" + componentId, JOB_GROUP_SYSTEM);
		try {
			if (scheduler.checkExists(jobKey)) {
				scheduler.deleteJob(jobKey);
			}
		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Unable remove Job.", ex);
		}
	}

	public static void runComponentIntegrationNow(String componentId, String integrationConfigId)
	{
		JobKey jobKey = JobKey.jobKey("ComponentJob-" + componentId, JOB_GROUP_SYSTEM);
		try {
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put(IntegrationJob.COMPONENT_ID, componentId);
			if (StringUtils.isNotBlank(integrationConfigId)) {
				jobDataMap.put(IntegrationJob.CONFIG_ID, integrationConfigId);
			}
			scheduler.triggerJob(jobKey, jobDataMap);
		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Unable run Job.", ex);
		}
	}

	public static void unscheduleSystemJob(String triggerName)
	{
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, JOB_GROUP_SYSTEM);
			if (scheduler.checkExists(triggerKey)) {
				scheduler.unscheduleJob(triggerKey);
			}
		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Unable unschedule Job.", ex);
		}
	}

	public static void addReportJob(String scheduledReportId, String reportType, String cronExpression) throws SchedulerException
	{
		Objects.requireNonNull(scheduledReportId);
		Objects.requireNonNull(reportType);
		Objects.requireNonNull(cronExpression);

		String jobName = REPORT_JOB_PREFIX + scheduledReportId;

		JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_REPORT);
		if (scheduler.checkExists(jobKey)) {
			LOG.log(Level.FINER, MessageFormat.format("Job already Exist: {0} removing job; adding changed", jobName));
			scheduler.deleteJob(jobKey);
		}
		JobDetail job = JobBuilder.newJob(ScheduledReportCronJob.class)
				.withIdentity(jobKey)
				.withDescription("Report Job for " + reportType)
				.build();

		job.getJobDataMap().put(ScheduledReportCronJob.SCHEDULED_REPORT_ID, scheduledReportId);
		Trigger trigger = newTrigger()
				.withIdentity(REPORT_JOB_PREFIX + scheduledReportId, JOB_GROUP_REPORT)
				.startNow()
				.withSchedule(cronSchedule(cronExpression))
				.build();

		scheduler.scheduleJob(job, trigger);
	}
	private static final String REPORT_JOB_PREFIX = "ReportJob-";

	public static void removeReportJob(String reportJobName)
	{
		if (reportJobName.startsWith(REPORT_JOB_PREFIX) == false) {
			reportJobName = REPORT_JOB_PREFIX + reportJobName;
		}
		try {
			JobKey jobKey = JobKey.jobKey(reportJobName, JOB_GROUP_REPORT);
			if (scheduler.checkExists(jobKey)) {
				scheduler.deleteJob(jobKey);
			}
		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Unable unschedule Job.", ex);
		}
	}

	public static void removeJob(String jobName, String jobGroup)
	{
		try {
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
			if (scheduler.checkExists(jobKey)) {
				scheduler.deleteJob(jobKey);
			}
		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Unable unschedule Job.", ex);
		}
	}

	private static void addComponentUpdate() throws SchedulerException
	{
		LOG.log(Level.INFO, "Adding Component Update Job");

		JobDetail job = JobBuilder.newJob(ComponentUpdateJob.class)
				.withIdentity("ComponentUpdateJob", JOB_GROUP_SYSTEM)
				.withDescription("Applies component updates index and watches.")
				.build();

		Trigger trigger = newTrigger()
				.withIdentity("ComponentUpdateJobTrigger", JOB_GROUP_SYSTEM)
				.startNow()
				.withSchedule(simpleSchedule()
						.withIntervalInSeconds(5)
						.repeatForever())
				.build();

		scheduler.scheduleJob(job, trigger);
	}

	private static void addImportJob() throws SchedulerException
	{
		LOG.log(Level.INFO, "Adding ImportJob");

		JobDetail job = JobBuilder.newJob(ImportJob.class)
				.withIdentity("ImportJob", JOB_GROUP_SYSTEM)
				.withDescription("This batches the uploads to allow for better data consistency.")
				.build();

		Trigger trigger = newTrigger()
				.withIdentity("ImportJobTrigger", JOB_GROUP_SYSTEM)
				.startNow()
				.withSchedule(simpleSchedule()
						.withIntervalInSeconds(5)
						.repeatForever())
				.build();

		scheduler.scheduleJob(job, trigger);
	}

	private static void addScheduledReportJob() throws SchedulerException
	{
		LOG.log(Level.INFO, "Adding Scheduled Job");

		JobDetail job = JobBuilder.newJob(ScheduledReportJob.class)
				.withIdentity("ScheduledReportJob", JOB_GROUP_SYSTEM)
				.withDescription("Run scheduled reports")
				.build();

		Trigger trigger = newTrigger()
				.withIdentity("ScheduledReportJobTrigger", JOB_GROUP_SYSTEM)
				.startNow()
				.withSchedule(simpleSchedule()
						.withIntervalInMinutes(1)
						.repeatForever())
				.build();

		scheduler.scheduleJob(job, trigger);
	}

	private static void addUserProfileSyncjob() throws SchedulerException
	{
		LOG.log(Level.INFO, "Adding User Profile Sync Job");

		JobDetail job = JobBuilder.newJob(UserProfileSyncJob.class)
				.withIdentity("UserProfileSyncJob", JOB_GROUP_SYSTEM)
				.withDescription("Run User Profile Sync")
				.build();

		Trigger trigger = newTrigger()
				.withIdentity("SUserProfileSyncJobrigger", JOB_GROUP_SYSTEM)
				.startNow()
				.withSchedule(simpleSchedule()
						.withIntervalInHours(24)
						.repeatForever())
				.build();

		scheduler.scheduleJob(job, trigger);
	}

	private static void addCleanUpErrorsJob() throws SchedulerException
	{
		LOG.log(Level.INFO, "Adding System Clean up Job");

		JobDetail job = JobBuilder.newJob(SystemCleanupJob.class)
				.withIdentity("SystemCleanupJob", JOB_GROUP_SYSTEM)
				.withDescription("Removes old error tickets and db log cleanup")
				.build();

		Trigger trigger = newTrigger()
				.withIdentity("SystemCleanupJob", JOB_GROUP_SYSTEM)
				.startNow()
				.withSchedule(simpleSchedule()
						.withIntervalInMinutes(5)
						.repeatForever())
				.build();

		scheduler.scheduleJob(job, trigger);
	}

	private static void addTrackingCleanUpJob() throws SchedulerException
	{
		LOG.log(Level.INFO, "Adding Tracking Cleanup Job");

		JobDetail job = JobBuilder.newJob(TrackingCleanupJob.class)
				.withIdentity("TrackingCleanupJob", JOB_GROUP_SYSTEM)
				.withDescription("Removes old tracking records")
				.build();

		Trigger trigger = newTrigger()
				.withIdentity("TrackingCleanupJobTrigger", JOB_GROUP_SYSTEM)
				.startNow()
				.withSchedule(simpleSchedule()
						.withIntervalInHours(24)
						.repeatForever())
				.build();

		scheduler.scheduleJob(job, trigger);
	}

	public static void addJob(AddJobModel addjob)
	{
		LOG.log(Level.FINE, MessageFormat.format("Adding Job: {0}", addjob.getJobName()));
		try {
			JobDetail job = JobBuilder.newJob(addjob.getJobClass())
					.withIdentity(addjob.getJobName(), addjob.getJobGroup())
					.withDescription(addjob.getDescription())
					.build();

			SimpleScheduleBuilder scheduleBuilder = simpleSchedule();

			if (addjob.getHours() != null) {
				scheduleBuilder.withIntervalInHours(addjob.getHours());
			} else if (addjob.getMinutes() != null) {
				scheduleBuilder.withIntervalInMinutes(addjob.getMinutes());
			} else if (addjob.getSeconds() != null) {
				scheduleBuilder.withIntervalInSeconds(addjob.getSeconds());
			} else if (addjob.getMilliseconds() != null) {
				scheduleBuilder.withIntervalInMilliseconds(addjob.getMilliseconds());
			}

			if (addjob.isRepeatForever()) {
				scheduleBuilder.repeatForever();
			} else {
				scheduleBuilder.withRepeatCount(addjob.getRepeatCount());
			}

			Trigger trigger = newTrigger()
					.withIdentity(addjob.getJobName() + "Trigger", addjob.getJobGroup())
					.startNow()
					.withSchedule(scheduleBuilder)
					.build();

			scheduler.scheduleJob(job, Collections.singleton(trigger), true); // Replace job if it already exists
			if (addjob.isPause()) {
				scheduler.pauseTrigger(trigger.getKey());
			}
		} catch (SchedulerException se) {
			throw new OpenStorefrontRuntimeException(se);
		}
	}

	private static void addNotificationJob() throws SchedulerException
	{
		LOG.log(Level.INFO, "Adding Notification Job");

		JobDetail job = JobBuilder.newJob(NotificationJob.class)
				.withIdentity("NotificationJob", JOB_GROUP_SYSTEM)
				.withDescription("User Message Notifications")
				.build();

		Trigger trigger = newTrigger()
				.withIdentity("NotificationJobTrigger", JOB_GROUP_SYSTEM)
				.startNow()
				.withSchedule(simpleSchedule()
						.withIntervalInMinutes(1)
						.repeatForever())
				.build();

		scheduler.scheduleJob(job, trigger);
	}

	private static void addRecentChangeNotifyJob() throws SchedulerException
	{
		LOG.log(Level.INFO, "Adding Recent Change Job");

		JobDetail job = JobBuilder.newJob(RecentChangeNotifyJob.class)
				.withIdentity("RecentChangeJob", JOB_GROUP_SYSTEM)
				.withDescription("Recent Change Notifications")
				.build();

		Trigger trigger = newTrigger()
				.withIdentity("RecentChangeJobTrigger", JOB_GROUP_SYSTEM)
				.startNow()
				.withSchedule(simpleSchedule()
						.withIntervalInHours(24)
						.repeatForever())
				.build();

		scheduler.scheduleJob(job, trigger);
	}

	public static void addImportJob(DirectoryScanListener directoryScanListener, String dirToWatch) throws SchedulerException
	{
		addImportJob(directoryScanListener, dirToWatch, false);
	}

	public static void addImportJob(DirectoryScanListener directoryScanListener, String dirToWatch, boolean activateJob) throws SchedulerException
	{
		String jobName = directoryScanListener.getClass().getName();
		LOG.log(Level.INFO, MessageFormat.format("Adding DIRWatch Job: {0}", directoryScanListener.getClass().getName()));

		JobDetail job = JobBuilder.newJob(DirectoryScanJob.class)
				.withIdentity(jobName, JOB_GROUP_SYSTEM)
				.withDescription("Directory Watch Job")
				.build();

		FileSystemManager.getInstance().getDir(dirToWatch);
		job.getJobDataMap().put(DirectoryScanJob.DIRECTORY_NAME, dirToWatch);
		job.getJobDataMap().put(DirectoryScanJob.DIRECTORY_SCAN_LISTENER_NAME, directoryScanListener.getClass().getName());
		scheduler.getContext().put(directoryScanListener.getClass().getName(), directoryScanListener);
		Trigger trigger = newTrigger()
				.withIdentity(jobName + "Trigger", JOB_GROUP_SYSTEM)
				.withSchedule(simpleSchedule()
						.withIntervalInSeconds(30)
						.repeatForever())
				.build();

		scheduler.scheduleJob(job, trigger);
		if (activateJob == false) {
			scheduler.pauseTrigger(trigger.getKey());
		}
	}

	private static void addArchiveJob() throws SchedulerException
	{
		LOG.log(Level.INFO, "Adding Archive Job");

		JobDetail job = JobBuilder.newJob(SystemArchiveJob.class)
				.withIdentity("ArchiveJob", JOB_GROUP_SYSTEM)
				.withDescription("This batches the archives so they run one at time.")
				.build();

		Trigger trigger = newTrigger()
				.withIdentity("ArchiveJobTrigger", JOB_GROUP_SYSTEM)
				.startNow()
				.withSchedule(simpleSchedule()
						.withIntervalInSeconds(5)
						.repeatForever())
				.build();

		scheduler.scheduleJob(job, trigger);
	}

	public static void runJobNow(String jobName, String groupName)
	{
		try {
			scheduler.triggerJob(JobKey.jobKey(jobName, groupName));
		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Unable to run job", "Make sure job exists", ex);
		}
	}

	public static void pauseJob(String jobName, String group)
	{
		try {
			scheduler.pauseJob(JobKey.jobKey(jobName, group));
		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Unable to pause job", "Make sure job exists", ex);
		}
	}

	public static void resumeJob(String jobName, String group)
	{
		try {
			scheduler.resumeJob(JobKey.jobKey(jobName, group));
		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Unable to resume job", "Make sure job exists", ex);
		}
	}

	public static void pauseScheduler()
	{
		try {
			scheduler.standby();
			LOG.info("Job Manager in Standby");
		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Unable to pause scheduler", ex);
		}
	}

	public static void resumeScheduler()
	{
		try {
			scheduler.start();
			LOG.info("Job Manager restarted");
		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Unable to resume scheduler", ex);
		}
	}

	public static String status()
	{
		String status = "Running";
		try {
			if (scheduler.isInStandbyMode()) {
				status = "In Standby";
			}
		} catch (SchedulerException ex) {
			Logger.getLogger(JobManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		return status;
	}

	public static List<JobModel> getAllJobs()
	{
		List<JobModel> jobs = new ArrayList<>();
		try {
			Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
			for (JobKey jobKey : jobKeys) {
				JobModel jobModel = new JobModel();

				JobDetail jobDetail = scheduler.getJobDetail(jobKey);
				jobModel.setJobClass(jobDetail.getJobClass().getName());
				jobModel.setJobName(jobKey.getName());
				jobModel.setGroupName(jobKey.getGroup());
				jobModel.setDescription(jobDetail.getDescription());
				jobModel.setConcurrentExectionDisallowed(jobDetail.isConcurrentExectionDisallowed());
				jobModel.setDurable(jobDetail.isDurable());
				jobModel.setPersistJobDataAfterExecution(jobDetail.isPersistJobDataAfterExecution());
				jobModel.setRequestsRecovery(jobDetail.requestsRecovery());
				StringBuilder dataMap = new StringBuilder();
				for (String dataKey : jobDetail.getJobDataMap().getKeys()) {
					dataMap.append(dataKey).append(" : ").append(jobDetail.getJobDataMap().get(dataKey)).append(" | ");
				}
				jobModel.setJobData(dataMap.toString());

				List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
				if (triggers.isEmpty() == false) {
					//just grab the first trigger as we should only have one
					Trigger trigger = triggers.get(0);
					jobModel.setPrimaryTrigger(trigger.getKey().getName() + " - " + trigger.getKey().getGroup());
					jobModel.setPerviousFiredTime(trigger.getPreviousFireTime());
					jobModel.setNextFiredTime(trigger.getNextFireTime());
					jobModel.setStatus(scheduler.getTriggerState(trigger.getKey()).toString());
				}
				jobs.add(jobModel);
			}
		} catch (SchedulerException ex) {
			Logger.getLogger(JobManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		return jobs;
	}

	public static void runDynamicJob(BaseJob baseJob)
	{
		//TODO: Add the ability to run a temp background job
		throw new UnsupportedOperationException("Method is not supported yet");
//		String job
//		JobDetail job = JobBuilder.newJob(SystemCleanupJob.class)
//				.withIdentity("DynamicJob-" + UUID.randomUUID().toString(), JOB_GROUP_SYSTEM)
//				.build();
//
//		Trigger trigger = newTrigger()
//				.withIdentity("CleanUpErrorsJobTrigger", JOB_GROUP_SYSTEM)
//				.startNow()
//				.withSchedule(simpleSchedule()
//						.withIntervalInMinutes(5)
//						.repeatForever())
//				.build();
//
//		scheduler.scheduleJob(job, trigger);
	}

	public static void cleanup()
	{
		if (scheduler != null) {
			try {
				scheduler.shutdown();
			} catch (SchedulerException ex) {
				throw new OpenStorefrontRuntimeException("Failed to init quartz.", ex);
			}
		}
	}

	@Override
	public void initialize()
	{
		JobManager.init();
		started.set(true);
	}

	@Override
	public void shutdown()
	{
		JobManager.cleanup();
		started.set(false);
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

}
