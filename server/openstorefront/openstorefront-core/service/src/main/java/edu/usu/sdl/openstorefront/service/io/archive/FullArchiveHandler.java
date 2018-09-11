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
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.entity.SystemArchive;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
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
		excludeDir.add(FileSystemManager.DB_DIR.replace("/", File.separator));
		List<String> directoriesToPull = FileSystemManager.getInstance().getTopLevelDirectories(excludeDir);

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
				TFile originDir = new TFile(dir);
				boolean copy = true;
				if(originDir.getPath().endsWith(FileSystemManager.DB_DIR.replace("/", File.separator))){
					copy = false;
				}
				if (originDir.getPath().endsWith(FileSystemManager.MAIN_PERM_DIR.replace("/", File.separator))) {
					copy = false;

					//copy all children (expect the archive itself)
					File permDir = new TFile(FileSystemManager.MAIN_PERM_DIR);
					File subDirs[] = permDir.listFiles();
					if (subDirs != null) {
						for (File subDir : subDirs) {
							//skip all archives (can not copy itself)
							//pulling existing archive record is not supported (likely noise anyway)
							if ("archive".equals(subDir.getName()) == false) {
								TFile originSubDir = new TFile(subDir);
								originSubDir.cp_r(new TFile(fullArchiveName + "/" + originDir.getName() + "/" + originSubDir.getName()));
							}
						}
					}
				}

				if (copy) {
					originDir.cp_r(new TFile(fullArchiveName + "/" + originDir.getName()));
				}
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

		//import db
		File dbImportFile = new TFile(fullArchiveName + DBEXPORT_FILENAME);
		dbArchiveHandler.performImport(dbImportFile);
		archive.setRecordsProcessed(1L);
		archive.setStatusDetails("Done");
		archive.save();

		//delete any working archives (this would be the carry over from the export process)
		SystemArchive archiveExample = new SystemArchive();
		archiveExample.setRunStatus(RunStatus.WORKING);
		List<SystemArchive> archives = archiveExample.findByExample();
		for (SystemArchive archiveLocal : archives) {
			if (archive.getArchiveId().equals(archiveLocal.getArchiveId()) == false) {
				archiveLocal.setRunStatus(RunStatus.COMPLETE);
				archiveLocal.save();
				service.getSystemArchiveService().deleteArchive(archiveLocal.getArchiveId());
			}
		}

		//import file system
		TFile archiveDir = new TFile(fullArchiveName);

		//pause plugin job
		JobManager.pauseScheduler();
		try {
			File filesInArchive[] = archiveDir.listFiles();
			if (filesInArchive != null) {
				for (File dir : filesInArchive) {
					if (dir.isDirectory()) {
						archive.setStatusDetails("Importing directory: " + dir);
						archive.save();
						try {
							File existingDir = FileSystemManager.getInstance().getDir(FileSystemManager.getInstance().getBaseDirectory() + "/" + dir.getName());
							new TFile(dir).cp_r(new TFile(existingDir));
						} catch (IOException ex) {
							LOG.log(Level.WARNING, "Unable to copy dir: " + dir, ex);
							addError("Unable to copy dir: " + dir);
						}
						archive.setStatusDetails("Finished Importing: " + dir);
						archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
						archive.save();
					}
				}
			}

		} finally {
			JobManager.resumeScheduler();
		}

		archive.setRecordsProcessed(archive.getTotalRecords());
		archive.setStatusDetails("Done");
		archive.save();
	}

}
