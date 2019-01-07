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
package edu.usu.sdl.openstorefront.service.io.archive;

import edu.usu.sdl.core.CoreSystem;
import edu.usu.sdl.openstorefront.core.entity.SystemArchive;
import edu.usu.sdl.openstorefront.service.manager.OrientDBManager;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import net.java.truevfs.access.TFileOutputStream;

/**
 *
 * @author dshurtleff
 */
public class DBArchiveHandler
		extends AbstractArchiveHandler
{

	private static final Logger LOG = Logger.getLogger(DBArchiveHandler.class.getName());

	private static final String DBEXPORT_FILENAME = "/dbexport.json.gz";

	public DBArchiveHandler(SystemArchive archive)
	{
		super(archive);
	}

	@Override
	protected void generateExport()
	{
		File exportFile = new TFile(fullArchiveName + DBEXPORT_FILENAME);
		archive.setTotalRecords(1L);
		archive.setStatusDetails("Exporting database...");
		archive.save();

		performExport(exportFile);

		archive.setRecordsProcessed(1L);
		archive.setStatusDetails("Done");
		archive.save();

		ArchiveManifest manifest = new ArchiveManifest();
		manifest.setTotalRecords(1);
		createManifest(manifest);
	}

	public void performExport(File exportFile)
	{
		try {
			OrientDBManager.getInstance().exportDB(new TFileOutputStream(exportFile));
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "DB Export failed", ex);
			addError("Fail to create export. See log for more details.");
		}
	}

	@Override
	protected void processImport(ArchiveManifest manifest)
	{
		File importFile = new TFile(fullArchiveName + DBEXPORT_FILENAME);
		archive.setStatusDetails("Importing database...");
		archive.save();

		performImport(importFile);

		archive.setRecordsProcessed(1L);
		archive.setStatusDetails("Done");
		archive.save();
	}

	public void performImport(File importFile)
	{
		try {
			CoreSystem.standby("Importing Database...(This may take several minutes)");
			JobManager.pauseScheduler();
			try {
				//Give the application a bit of time to complete any running job
				//Obvisiously this will not catch every thing. This is expected.
				//The user has been warned.
				Thread.sleep(2000);
			} catch (InterruptedException ex) {
				LOG.log(Level.WARNING, "Interrupted DB Import", ex);
				Thread.currentThread().interrupt();
			}

			OrientDBManager.getInstance().importDB(new TFileInputStream(importFile));
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "DB Import failed", ex);
			addError("Failed to import database. See log for more details.");
		} finally {
			JobManager.resumeScheduler();
			CoreSystem.resume("Completed Import Database");
		}
	}

}
