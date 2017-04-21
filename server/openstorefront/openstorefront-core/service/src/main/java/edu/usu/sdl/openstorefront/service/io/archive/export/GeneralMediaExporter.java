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

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.GeneralMedia;
import edu.usu.sdl.openstorefront.service.io.archive.BaseExporter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.truevfs.access.TFile;

/**
 *
 * @author dshurtleff
 */
public class GeneralMediaExporter
		extends BaseExporter
{

	private static final Logger LOG = Logger.getLogger(GeneralMediaExporter.class.getName());

	private List<GeneralMedia> generalMediaRecords = new ArrayList<>();
	private static final String GENERAL_MEDIA_DIR = "/generalmedia/";
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
		File mediaRecordFile = new TFile(archiveBasePath + GENERAL_MEDIA_DIR + "records.json");
		try {
			StringProcessor.defaultObjectMapper().writeValue(mediaRecordFile, generalMediaRecords);
		} catch (IOException ex) {
			LOG.log(Level.FINE, "Unable to export Media Records.", ex);
			addError("Unable to export Media Records.");
		}

		for (GeneralMedia media : generalMediaRecords) {

			File mediaFile = new TFile(archiveBasePath + MEDIA_DIR + media.getName());
			Path path = media.pathToMedia();
			try {
				Files.copy(path, mediaFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ex) {
				LOG.log(Level.FINE, "Unable to copy media file: " + media.getName(), ex);
				addError("Unable to copy media file: " + media.getName());
			}

			archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
			archive.setStatusDetails("Exported " + media.getName());
			archive.save();
		}
	}

	@Override
	public void importRecords()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public long getTotalRecords()
	{
		return generalMediaRecords.size();
	}

}
