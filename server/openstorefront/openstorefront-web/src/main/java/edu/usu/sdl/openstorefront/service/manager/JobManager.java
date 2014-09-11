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

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.io.ArticleImporter;
import edu.usu.sdl.openstorefront.service.io.AttributeImporter;
import edu.usu.sdl.openstorefront.service.io.HighlightImporter;
import edu.usu.sdl.openstorefront.service.io.LookupImporter;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;
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

	private static final Logger log = Logger.getLogger(JobManager.class.getName());

	private static final String JOB_GROUP_SYSTEM = "SYSTEM";
	private static Scheduler scheduler;

	public static void init()
	{
		try {
			StdSchedulerFactory factory = new StdSchedulerFactory(FileSystemManager.getConfig("quartz.properties").getPath());
			scheduler = factory.getScheduler();
			initSystemJobs();
			scheduler.start();

		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Failed to init quartz.", ex);
		}
	}

	private static void initSystemJobs() throws SchedulerException
	{
		log.log(Level.FINEST, "Setting up Import Jobs...");
		addImportJob(new LookupImporter(), FileSystemManager.IMPORT_LOOKUP_DIR);
		addImportJob(new AttributeImporter(), FileSystemManager.IMPORT_ATTRIBUTE_DIR);
		addImportJob(new ArticleImporter(), FileSystemManager.IMPORT_ARTICLE_DIR);
		addImportJob(new HighlightImporter(), FileSystemManager.IMPORT_HIGHLIGHT_DIR);
	}

	private static void addImportJob(DirectoryScanListener directoryScanListener, String dirToWatch) throws SchedulerException
	{
		String jobName = directoryScanListener.getClass().getName();
		log.log(Level.INFO, MessageFormat.format("Adding DIRWatch Job: {0}", directoryScanListener.getClass().getName()));

		JobDetail job = JobBuilder.newJob(DirectoryScanJob.class)
				.withIdentity(jobName, JOB_GROUP_SYSTEM)
				.build();

		FileSystemManager.getDir(dirToWatch);
		job.getJobDataMap().put(DirectoryScanJob.DIRECTORY_NAME, dirToWatch);
		job.getJobDataMap().put(DirectoryScanJob.DIRECTORY_SCAN_LISTENER_NAME, directoryScanListener.getClass().getName());
		scheduler.getContext().put(directoryScanListener.getClass().getName(), directoryScanListener);
		Trigger trigger = newTrigger()
				.withIdentity(jobName + "Trigger", JOB_GROUP_SYSTEM)
				.startNow()
				.withSchedule(simpleSchedule()
						.withIntervalInSeconds(30)
						.repeatForever())
				.build();

		scheduler.scheduleJob(job, trigger);

	}

	public static void cleanup()
	{
		try {
			scheduler.shutdown();
		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Failed to init quartz.", ex);
		}
	}

	@Override
	public void initialize()
	{
		JobManager.init();
	}

	@Override
	public void shutdown()
	{
		JobManager.cleanup();
	}

}
