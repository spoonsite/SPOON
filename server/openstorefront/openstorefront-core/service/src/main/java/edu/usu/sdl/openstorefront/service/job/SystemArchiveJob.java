/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.entity.SystemArchive;
import edu.usu.sdl.openstorefront.core.model.ErrorInfo;
import edu.usu.sdl.openstorefront.core.view.SystemErrorModel;
import edu.usu.sdl.openstorefront.service.io.archive.AbstractArchiveHandler;
import java.util.List;
import java.util.logging.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 *
 * @author dshurtleff
 */
@DisallowConcurrentExecution
public class SystemArchiveJob
		extends BaseJob
{

	private static final Logger LOG = Logger.getLogger(SystemArchiveJob.class.getName());
	private static final long DEFAULT_MAX_PROCESSING_MINUTES = 60L;

	@Override
	protected void executeInternaljob(JobExecutionContext context)
	{
		//check for stuck working archives that exceed processing time limits
		Long maxWorkingMinutes = Convert.toLong(PropertiesManager.getValueDefinedDefault(PropertiesManager.KEY_SYSTEM_ARCHIVE_MAX_PROCESSMINTUES));
		if (maxWorkingMinutes == null) {
			maxWorkingMinutes = DEFAULT_MAX_PROCESSING_MINUTES;
		}

		SystemArchive systemArchive = new SystemArchive();
		systemArchive.setRunStatus(RunStatus.WORKING);

		List<SystemArchive> workingArchives = systemArchive.findByExample();
		for (SystemArchive archive : workingArchives) {

			long timeSinceLastUpdate = System.currentTimeMillis() - archive.getUpdateDts().getTime();
			if (timeSinceLastUpdate > maxWorkingMinutes) {
				//fail the archive
				service.getSystemArchiveServicePrivate().addErrorMessage(archive.getArchiveId(), "Archive failed to make progress; may have been stuck.");
				archive.setRunStatus(RunStatus.ERROR);
				archive.save();
			}
		}
		systemArchive = new SystemArchive();
		systemArchive.setRunStatus(RunStatus.PENDING);

		List<SystemArchive> pendingArchives = systemArchive.findByExample();
		for (SystemArchive archive : pendingArchives) {
			//critial loop
			try {
				AbstractArchiveHandler.processArchive(archive);
			} catch(Exception e) {
				ErrorInfo errorInfo = new ErrorInfo(e, null);
				SystemErrorModel errorModel = service.getSystemService().generateErrorTicket(errorInfo);
				
				service.getSystemArchiveServicePrivate().addErrorMessage(archive.getArchiveId(), "Archive failed unexpectly see Error ticket:  " + errorModel.getErrorTicketNumber());
				archive.setRunStatus(RunStatus.ERROR);
				archive.save();				
			}
		}
	}

}
