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

import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.core.entity.SystemArchive;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.truevfs.access.TFile;

/**
 * Export the DB and all files in the file system
 *
 * @author dshurtleff
 */
public class FullArchiveHandler
		extends AbstractArchiveHandler
{

	private static final Logger LOG = Logger.getLogger(FullArchiveHandler.class.getName());

	private static final String DBEXPORT_FILENAME = "/dbexport.json.gz";

	private DBArchiveHandler dbArchiveHandler;

	public FullArchiveHandler(SystemArchive archive)
	{
		super(archive);
		dbArchiveHandler = new DBArchiveHandler(archive);
	}

	@Override
	protected void generateExport()
	{
		Set<String> excludeDir = new HashSet<>();
		excludeDir.add(FileSystemManager.DB_DIR);
		List<String> directoriesToPull = FileSystemManager.getTopLevelDirectories(excludeDir);

		long totalRecords = (long) directoriesToPull.size() + 1;
		archive.setTotalRecords(totalRecords);
		archive.setStatusDetails("Exporting database...");
		archive.save();

		File dbExportFile = new TFile(fullArchiveName + DBEXPORT_FILENAME);
		dbArchiveHandler.performExport(dbExportFile);

		archive.setRecordsProcessed(1L);
		archive.setStatusDetails("Done");
		archive.save();

		for (String dir : directoriesToPull) {
			archive.setStatusDetails("Exporting directory: " + dir);
			archive.save();
			try {
				new TFile(dir).cp_r(new TFile(fullArchiveName));
			} catch (IOException ex) {
				LOG.log(Level.WARNING, "Unable to copy dir: " + dir, ex);
				addError("Unable to copy dir: " + dir);
			}
			archive.setStatusDetails("Finished Exporting: " + dir);
			archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
			archive.save();
		}

		ArchiveManifest manifest = new ArchiveManifest();
		manifest.setTotalRecords(totalRecords);
		createManifest(manifest);

	}

	@Override
	protected void processImport(ArchiveManifest manifest)
	{
		archive.setStatusDetails("Importing database...");
		archive.save();

		File dbImportFile = new TFile(fullArchiveName + DBEXPORT_FILENAME);
		dbArchiveHandler.performImport(dbImportFile);

		archive.setRecordsProcessed(1L);
		archive.setStatusDetails("Done");
		archive.save();

	}

}
