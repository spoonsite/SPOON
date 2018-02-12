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

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.ApplicationProperty;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.JobExecutionContext;

/**
 * Handles run the recent change job
 *
 * @author dshurtleff
 */
public class RecentChangeNotifyJob
		extends BaseJob
{

	private static final Logger log = Logger.getLogger(RecentChangeNotifyJob.class.getName());

	@Override
	protected void executeInternaljob(JobExecutionContext context)
	{
		long days = Convert.toLong(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MESSAGE_RECENT_CHANGE_DAYS, OpenStorefrontConstant.DEFAULT_RECENT_CHANGE_EMAIL_INTERVAL));
		long daysInMillis = TimeUtil.daysToMillis(days);
		String lastRunDtsString = service.getSystemService().getPropertyValue(ApplicationProperty.RECENT_CHANGE_EMAIL_LAST_DTS);

		boolean process = false;
		Date lastRunDts = null;
		if (lastRunDtsString != null) {
			lastRunDts = TimeUtil.fromString(lastRunDtsString);

			if (lastRunDts == null || System.currentTimeMillis() > (lastRunDts.getTime() + daysInMillis)) {
				process = true;
			}
		}

		if (process) {
			log.log(Level.INFO, "Sending out Recent Changes email.");
			service.getUserService().sendRecentChangeEmail(lastRunDts);
			log.log(Level.INFO, "Completed sending out Recent Changes email.");
			service.getSystemService().saveProperty(ApplicationProperty.RECENT_CHANGE_EMAIL_LAST_DTS, TimeUtil.dateToString(TimeUtil.currentDate()));
		} else {
			Date nextSendDate = new Date(System.currentTimeMillis() + daysInMillis);
			if (lastRunDts != null) {
				nextSendDate = new Date(lastRunDts.getTime() + daysInMillis);
			} else {
				service.getSystemService().saveProperty(ApplicationProperty.RECENT_CHANGE_EMAIL_LAST_DTS, TimeUtil.dateToString(TimeUtil.currentDate()));
			}

			log.log(Level.FINE, MessageFormat.format("Not time yet to send recent change email.  Next send time: {0}", nextSendDate));
		}

	}

}
