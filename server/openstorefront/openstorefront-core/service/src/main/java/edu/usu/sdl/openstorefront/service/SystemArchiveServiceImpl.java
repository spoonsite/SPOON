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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.api.SystemArchiveService;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.entity.SystemArchive;
import edu.usu.sdl.openstorefront.core.entity.SystemArchiveError;
import edu.usu.sdl.openstorefront.service.api.SystemArchiveServicePrivate;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * Handles System Archives
 *
 * @author dshurtleff
 */
public class SystemArchiveServiceImpl
		extends ServiceProxy
		implements SystemArchiveService, SystemArchiveServicePrivate
{

	private static final Logger LOG = Logger.getLogger(SystemArchiveServiceImpl.class.getName());

	@Override
	public void queueArchiveRequest(SystemArchive archive)
	{
		archive.setRunStatus(RunStatus.PENDING);
		archive.save();
	}

	@Override
	public void deleteArchive(String archiveId)
	{
		SystemArchive archive = persistenceService.findById(SystemArchive.class, archiveId);
		if (archive != null) {

			if (RunStatus.COMPLETE.equals(archive.getRunStatus())
					|| RunStatus.ERROR.equals(archive.getRunStatus())) {
				Path path = archive.pathToArchive();
				if (path != null) {
					if (path.toFile().exists()) {
						path.toFile().delete();
					}
				}
				SystemArchiveError systemArchiveError = new SystemArchiveError();
				systemArchiveError.setArchiveId(archiveId);
				persistenceService.deleteByExample(systemArchiveError);

				persistenceService.delete(archive);
			} else {
				throw new OpenStorefrontRuntimeException("Unable delete system archive that is currently in process.",
						"Check status on archive.  If the archive is stuck (meaning application was stopped in the middle. Wait for the system to auto-correct after processing timeout has occured)."
				);
			}
		}
	}

	@Override
	public void addErrorMessage(String archiveId, String message)
	{
		if (StringUtils.isNotBlank(message)) {

			SystemArchiveError systemArchiveError = new SystemArchiveError();
			systemArchiveError.setArchiveErrorId(persistenceService.generateId());
			systemArchiveError.setArchiveId(archiveId);
			systemArchiveError.setMessage(message);
			systemArchiveError.populateBaseCreateFields();
			persistenceService.persist(systemArchiveError);

		} else {
			LOG.log(Level.FINEST, "Unable to add empty error message to system archive. SKIPING");
		}
	}

}
