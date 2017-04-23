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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.IODirectionType;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.entity.SystemArchive;
import edu.usu.sdl.openstorefront.core.entity.SystemArchiveType;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import net.java.truevfs.access.TFileOutputStream;
import net.java.truevfs.access.TVFS;
import net.java.truevfs.kernel.spec.FsSyncException;

/**
 *
 * @author dshurtleff
 */
public abstract class AbstractArchiveHandler
{

	private static final Logger LOG = Logger.getLogger(AbstractArchiveHandler.class.getName());

	private static final String MANIFEST_FILENAME = "/manifest.json";

	protected SystemArchive archive;
	private boolean addedError = false;
	protected ServiceProxy service = new ServiceProxy();
	protected String fullArchiveName;

	public AbstractArchiveHandler(SystemArchive archive)
	{
		this.archive = archive;
	}

	public static void processArchive(SystemArchive archive)
	{
		AbstractArchiveHandler handler;

		if (archive.getSystemArchiveType() == null) {
			Path path = archive.pathToArchive();
			if (path != null) {
				try (InputStream in = new TFileInputStream(path.toString() + MANIFEST_FILENAME)) {
					ArchiveManifest manifest = StringProcessor.defaultObjectMapper().readValue(in, ArchiveManifest.class);

					archive.setRecordsProcessed(0L);
					archive.setTotalRecords(manifest.getTotalRecords());
					archive.setSystemArchiveType(manifest.getSystemArchiveType());			
					archive.save();

				} catch (IOException ex) {					
					throw new OpenStorefrontRuntimeException("Failed to read archive manifest.", "Make sure file is a valid archive", ex);
				}				
			}
		}
		
		//determine handler
		switch (archive.getSystemArchiveType()) {
			case SystemArchiveType.DBEXPORT:
				handler = new DBArchiveHandler(archive);
				break;

			case SystemArchiveType.GENERAL:
				handler = new GeneralArchiveHandler(archive);
				break;

			default:
				throw new OpenStorefrontRuntimeException("Archive type not supported.", "Archive Type: " + archive.getSystemArchiveType());
		}

		handler.startWork();
		switch (archive.getIoDirectionType()) {
			case IODirectionType.EXPORT:
				handler.handleExport();
				break;
			case IODirectionType.IMPORT:
				handler.handleImport();
				break;

			default:
				throw new OpenStorefrontRuntimeException("IO type not supported.", "IO Type: " + archive.getIoDirectionType());
		}
		handler.completeWork();

	}

	protected void handleExport()
	{

		String archiveName = "archive-" + archive.getArchiveId() + ".zip";
		fullArchiveName = FileSystemManager.getDir(FileSystemManager.ARCHIVE_DIR) + "/" + archiveName;

		archive.setArchiveFilename(archiveName);
		archive.save();

		try {
			generateExport();
		} finally {
			try {
				TVFS.umount();
			} catch (FsSyncException ex) {
				LOG.log(Level.SEVERE, "Unable to unmount archive; system archive may be not available.", ex);
				addError("Unable to unmount archive; system archive may be not available. See log.");
			}
		}
	}

	protected abstract void generateExport();

	protected void handleImport()
	{
		Objects.requireNonNull(archive.pathToArchive(), "Import file must be set.");

		fullArchiveName = archive.pathToArchive().toString();

		ArchiveManifest manifest = readManifest();
		try {
			processImport(manifest);
		} finally {
			try {
				TVFS.umount();
			} catch (FsSyncException ex) {
				LOG.log(Level.SEVERE, "Unable to unmount archive; may have not have release resources", ex);
				addError("Unable to unmount archive; system archive may be not available. See log.");
			}
		}
	}

	protected ArchiveManifest readManifest()
	{
		ArchiveManifest manifest = null;

		File manifestFile = new TFile(fullArchiveName + MANIFEST_FILENAME);
		try (InputStream in = new TFileInputStream(manifestFile)) {
			manifest = StringProcessor.defaultObjectMapper().readValue(in, ArchiveManifest.class);

			archive.setRecordsProcessed(0L);			
			archive.setTotalRecords(manifest.getTotalRecords());
			archive.setSystemArchiveType(manifest.getSystemArchiveType());			
			archive.save();

		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Failed to read archive manifest.", ex);
			addError("Unable to read manifest. The archive may not be valid.");
		}
		return manifest;
	}

	protected abstract void processImport(ArchiveManifest manifest);

	private void startWork()
	{
		archive.setRunStatus(RunStatus.WORKING);
		archive.setStartDts(TimeUtil.currentDate());
		archive.save();
	}

	private void completeWork()
	{
		if (addedError) {
			archive.setRunStatus(RunStatus.ERROR);
		} else {
			archive.setRunStatus(RunStatus.COMPLETE);
		}
		archive.setCompletedDts(TimeUtil.currentDate());
		archive.save();
	}

	protected void addError(String message)
	{
		addedError = true;
		service.getSystemArchiveServicePrivate().addErrorMessage(archive.getArchiveId(), message);
	}

	protected void createManifest(ArchiveManifest manifest)
	{
		manifest.setSystemArchiveType(archive.getSystemArchiveType());
		manifest.setApplicationVersion(PropertiesManager.getApplicationVersion());
		
		File manifestFile = new TFile(fullArchiveName + MANIFEST_FILENAME);

		try (OutputStream out = new TFileOutputStream(manifestFile)) {
			StringProcessor.defaultObjectMapper().writeValue(out, manifest);
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Failed to create archive manifest.", ex);
			addError("Unable to add manifest to archive. The archive may not be valid.");
		}
	}

}
