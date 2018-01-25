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
package edu.usu.sdl.openstorefront.service.io.archive.export;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.GeneralMedia;
import edu.usu.sdl.openstorefront.service.io.archive.BaseExporter;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import net.java.truevfs.access.TFileOutputStream;

/**
 *
 * @author dshurtleff
 */
public class GeneralMediaExporter
		extends BaseExporter
{

	private static final Logger LOG = Logger.getLogger(GeneralMediaExporter.class.getName());

	private List<GeneralMedia> generalMediaRecords = new ArrayList<>();
	private static final String DATA_DIR = "/generalmedia/";
	private static final String MEDIA_DIR = "/generalmedia/media/";

	@Override
	public int getPriority()
	{
		return 2;
	}

	@Override
	public String getExporterSupportEntity()
	{
		return GeneralMedia.class.getSimpleName();
	}

	@Override
	public void exporterInit()
	{
		super.exporterInit();

		GeneralMedia generalMedia = new GeneralMedia();
		generalMedia.setActiveStatus(GeneralMedia.ACTIVE_STATUS);
		generalMediaRecords = generalMedia.findByExample();
	}

	@Override
	public List<BaseExporter> getAllRequiredExports()
	{
		List<BaseExporter> exporters = new ArrayList<>();
		exporters.add(this);
		return exporters;
	}

	@Override
	public void exportRecords()
	{
		File mediaRecordFile = new TFile(archiveBasePath + DATA_DIR + "records.json");
		try (OutputStream out = new TFileOutputStream(mediaRecordFile)) {
			StringProcessor.defaultObjectMapper().writeValue(out, generalMediaRecords);
		} catch (IOException ex) {
			LOG.log(Level.WARNING, "Unable to export Media Records.", ex);
			addError("Unable to export Media Records.");
		}

		for (GeneralMedia media : generalMediaRecords) {

			File mediaFile = new TFile(archiveBasePath + MEDIA_DIR + media.getName());
			Path path = media.pathToMedia();
			if (path != null) {
				try {
					Files.copy(path, mediaFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException ex) {
					LOG.log(Level.WARNING, "Unable to copy media file: " + media.getName(), ex);
					addError("Unable to copy media file: " + media.getName());
				}
			} else {
				LOG.log(Level.WARNING, MessageFormat.format("No Media found for General Media: {0} (Skipping)", media.getName()));
				addError("Unable to copy media file (No file found for record): " + media.getName());
			}

			archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
			archive.setStatusDetails("Processed " + media.getName());
			archive.save();
		}
	}

	@Override
	public void importRecords()
	{
		File dataFile = new TFile(archiveBasePath + DATA_DIR + "records.json");
		try (InputStream in = new TFileInputStream(dataFile)) {
			archive.setStatusDetails("Importing: " + dataFile.getName());
			archive.save();

			List<GeneralMedia> generalMedia = StringProcessor.defaultObjectMapper().readValue(in, new TypeReference<List<GeneralMedia>>()
			{
			});
			GeneralMedia generalMediaExample = new GeneralMedia();
			List<GeneralMedia> existingMedia = generalMediaExample.findByExample();
			Map<String, List<GeneralMedia>> mediaMap = existingMedia.stream()
					.collect(Collectors.groupingBy(GeneralMedia::getName));
			
			List<String> errors = new ArrayList<>();
			for (GeneralMedia generalMediaRecord : generalMedia) {
				try {
					if (mediaMap.containsKey(generalMediaRecord.getName())) {
						service.getSystemService().removeGeneralMedia(generalMediaRecord.getName());
					}
					ValidationResult vr = generalMediaRecord.validate();
					if (vr.valid()) {
						generalMediaRecord.save();
					} else {
						errors.add(MessageFormat.format("Failed to save general media: {0}<br>{1}", generalMediaRecord.getName(), vr.toHtmlString()));
					}
				} catch (OpenStorefrontRuntimeException ex) {
					LOG.log(Level.WARNING, MessageFormat.format("Failed to save media: {0}", generalMediaRecord.getName()), ex);
					errors.add(MessageFormat.format("Failed to save general media: {0}", generalMediaRecord.getName()));
				}
			}
			if (errors.size() > 0) {
				addError(String.join("<br>", errors));
			}

			TFile mediaDir = new TFile(archiveBasePath + MEDIA_DIR);
			TFile media[] = mediaDir.listFiles();
			if (media != null) {
				for (TFile mediaFile : media) {
					try {
						Files.copy(mediaFile.toPath(), FileSystemManager.getInstance().getDir(FileSystemManager.getInstance().GENERAL_MEDIA_DIR).toPath().resolve(mediaFile.getName()), StandardCopyOption.REPLACE_EXISTING);

						archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
						archive.save();
					} catch (IOException ex) {
						LOG.log(Level.WARNING, MessageFormat.format("Failed to copy media to path file: {0}", mediaFile.getName()), ex);
						addError(MessageFormat.format("Failed to copy media to path file: {0}", mediaFile.getName()));
					}
				}
			}
		} catch (Exception ex) {
			LOG.log(Level.WARNING, "Failed to Load general media", ex);
			addError("Unable to load general media: " + dataFile.getName());
		}

	}

	@Override
	public long getTotalRecords()
	{
		return generalMediaRecords.size();
	}

}
