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
package edu.usu.sdl.openstorefront.service.job;

import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseJob
		implements Job
{

	private static final Logger log = Logger.getLogger(BaseJob.class.getName());

	protected ServiceProxy service = new ServiceProxy();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		try {
			SecurityUtil.initSystemUser();
			log.log(Level.FINEST, MessageFormat.format("Running job: ", new Object[]{context.getJobDetail().getKey().getName()}));
			executeInternaljob(context);
		} catch (Exception e) {
			//According the quartz best practise the job shouldn't throw an error.
			log.log(Level.SEVERE, "Job failed unexpectly to run", e);
		}
	}

	protected abstract void executeInternaljob(JobExecutionContext context);
}
