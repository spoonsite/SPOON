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
package edu.usu.sdl.openstorefront.service.job;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.UserTracking;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.JobExecutionContext;

/**
 *
 * @author dshurtleff
 */
public class TrackingCleanupJob
		extends BaseJob
{

	private static final Logger log = Logger.getLogger(TrackingCleanupJob.class.getName());

	@Override
	protected void executeInternaljob(JobExecutionContext context)
	{
		String maxDays = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MAX_AGE_TRACKING_RECORDS, "365");

		Instant maxInstant = Instant.now();
		maxInstant = maxInstant.minus(Long.parseLong(maxDays), ChronoUnit.DAYS);

		log.log(Level.FINEST, MessageFormat.format("Deleting old tracking records.  Older than: {0}", maxInstant.toString()));

		log.log(Level.FINEST, "Deleting User Tracking Records");
		UserTracking userTrackingExample = new UserTracking();

		UserTracking userTrackingEndExample = new UserTracking();
		userTrackingEndExample.setEventDts(new Date(maxInstant.toEpochMilli()));

		QueryByExample queryByExample = new QueryByExample(userTrackingExample);
		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel(userTrackingEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		int recordsRemoved = service.getPersistenceService().deleteByExample(queryByExample);
		log.log(Level.FINEST, MessageFormat.format("Records removed: {0}", recordsRemoved));

		log.log(Level.FINEST, "Deleting Component Tracking Records");
		ComponentTracking componentTrackingExample = new ComponentTracking();

		ComponentTracking componentTrackingEndExample = new ComponentTracking();
		componentTrackingEndExample.setEventDts(new Date(maxInstant.toEpochMilli()));

		queryByExample = new QueryByExample(componentTrackingExample);
		specialOperatorModel = new SpecialOperatorModel(componentTrackingEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		recordsRemoved = service.getPersistenceService().deleteByExample(queryByExample);
		log.log(Level.FINEST, MessageFormat.format("Component records removed: {0}", recordsRemoved));

	}

}
