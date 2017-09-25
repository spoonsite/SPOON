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
package edu.usu.sdl.core.init;

import edu.usu.sdl.core.CoreSystem;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 * remove in 2.5
 *
 * @author dshurtleff
 */
public class DBDataMigration
		extends ApplyOnceInit
{

	private static final Logger LOG = Logger.getLogger(DBDataMigration.class.getName());

	public DBDataMigration()
	{
		super("DB-MIGRATION-2.1");
	}

	@Override
	protected String internalApply()
	{
		StringBuilder results = new StringBuilder();

		File exportDir = FileSystemManager.getDir(FileSystemManager.SYSTEM_TEMP_DIR);

		try {
			CoreSystem.standby("Running DB Export/Import");
			JobManager.pauseScheduler();

			LOG.log(Level.INFO, MessageFormat.format("Exporting Database to {0}/2-1dbexport.gz ...standy by", exportDir.getPath()));
			DBManager.exportDB(new FileOutputStream(exportDir.getPath() + "/2-1dbexport.gz"), (String status) -> {
				CoreSystem.setDetailedStatus(status);
			});

			service.getSystemService().toggleDBlogger(false);
			DBManager.cleanup();
			LOG.log(Level.INFO, "Clearing orient directory");
			String dataDir = FileSystemManager.getDir(FileSystemManager.DB_DIR).getPath() + "/databases/openstorefront";
			FileUtils.deleteDirectory(FileSystemManager.getDir(dataDir));

			LOG.log(Level.INFO, "restarting orient");
			DBManager.init();

			LOG.log(Level.INFO, MessageFormat.format("Importing Database to {0}/2-1dbexport.gz ...standy by", exportDir.getPath()));
			DBManager.importDB(new FileInputStream(exportDir.getPath() + "/2-1dbexport.gz"), (String status) -> {
				CoreSystem.setDetailedStatus(status);
			});

			LOG.log(Level.INFO, "Clean up temp file");
			File tempFile = new File(exportDir.getPath() + "/2-1dbexport.gz");
			if (tempFile.exists()) {
				if (!tempFile.delete()) {
					LOG.log(Level.WARNING, MessageFormat.format("Unable to remove temp db export file: {0}", tempFile.getPath()));
				}
			}
			LOG.log(Level.INFO, "Finishing data migration");
			results.append("Export/Imported DB successfully");

		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Unable to migrate data.  See log.", ex);
			results.append("Unable to migrate data.  See log. There may be a permission or disk space issue.");
		} finally {
			JobManager.resumeScheduler();
			CoreSystem.resume("Completed Export/Import Database");
		}

		return results.toString();
	}

}
