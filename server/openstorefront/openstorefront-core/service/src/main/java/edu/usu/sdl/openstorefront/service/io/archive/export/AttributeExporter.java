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

import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.model.AttributeAll;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import net.java.truevfs.access.TFileOutputStream;

/**
 *
 * @author dshurtleff
 */
public class AttributeExporter
		extends BaseExporter
{

	private static final Logger LOG = Logger.getLogger(AttributeExporter.class.getName());
	private static final String DATA_DIR = "/attribute/";
	private static final String ATTACHMENT_DIR = "/attribute/attachments/";

	@Override
	public int getPriority()
	{
		return 5;
	}

	@Override
	public String getExporterSupportEntity()
	{
		return AttributeType.class.getSimpleName();
	}

	@Override
	public List<BaseExporter> getAllRequiredExports()
	{
		List<BaseExporter> exporters = new ArrayList<>();
		exporters.add(new GeneralMediaExporter());
		exporters.add(this);
		return exporters;
	}

	@Override
	public void exportRecords()
	{
		AttributeType attributeTypeExample = new AttributeType();
		attributeTypeExample.setActiveStatus(AttributeType.ACTIVE_STATUS);
		List<AttributeType> attributesToExport = attributeTypeExample.findByExample();

		for (AttributeType attributeType : attributesToExport) {

			AttributeAll attributeAll = new AttributeAll();
			attributeAll.setAttributeType(attributeType);
			attributeAll.setAttributeCodes(service.getAttributeService().findCodesForType(attributeType.getAttributeType()));
			File attributeFile = new TFile(archiveBasePath + DATA_DIR + attributeType.getAttributeType() + ".json");

			try (OutputStream out = new TFileOutputStream(attributeFile)) {
				StringProcessor.defaultObjectMapper().writeValue(out, attributeAll);
			} catch (IOException ex) {
				LOG.log(Level.WARNING, "Failed to export attibute", ex);
				addError("Unable to export: " + attributeType.getDescription());
			}

			//save any attachments
			for (AttributeCode attributeCode : attributeAll.getAttributeCodes()) {
				File mediaFile = new TFile(archiveBasePath + ATTACHMENT_DIR + attributeCode.getAttachmentFileName());
				Path path = attributeCode.pathToAttachment();
				if (path != null) {
					try {
						Files.copy(path, mediaFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException ex) {
						LOG.log(Level.WARNING, "Unable to copy media file: " + mediaFile.getName(), ex);
						addError("Unable to copy media file: " + mediaFile.getName());
					}
				}
			}

			archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
			archive.setStatusDetails("Exported " + attributeType.getDescription());
			archive.save();
		}

	}

	@Override
	public void importRecords()
	{
		File dataDir = new TFile(archiveBasePath + DATA_DIR);
		File files[] = dataDir.listFiles();
		if (files != null) {
			for (File dataFile : files) {
				String className = dataFile.getName().replace(".json", "");
				try (InputStream in = new TFileInputStream(dataFile)) {
					archive.setStatusDetails("Importing: " + className);
					archive.save();

					AttributeAll attributeAll = StringProcessor.defaultObjectMapper().readValue(in, AttributeAll.class);

					List<AttributeAll> attributeAlls = new ArrayList<>();
					attributeAlls.add(attributeAll);
					FileHistoryOption options = new FileHistoryOption();
					ValidationResult validationResult = service.getAttributeService().importAttributes(attributeAlls, options);
					if (validationResult.valid() == false) {
						String message = "Failed to import Attribute: {0}<br><br>All imported entries will be missing this attribute<br>Details:<br>{1}";
						addError(MessageFormat.format(message, className, validationResult.toHtmlString()));
					}

					archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
					archive.save();

				} catch (Exception ex) {
					LOG.log(Level.WARNING, "Failed to Load attibutes", ex);
					addError("Unable to load attributes: " + className);
				}
			}
		} else {
			LOG.log(Level.FINE, "No attibutes to load.");
		}

		//load attachments
		TFile mediaDir = new TFile(archiveBasePath + ATTACHMENT_DIR);
		TFile media[] = mediaDir.listFiles();
		if (media != null) {
			archive.setStatusDetails("Saving Attachments (" + media.length + ")...");
			archive.save();
			for (TFile mediaFile : media) {
				try {
					Files.copy(mediaFile.toPath(), FileSystemManager.getInstance().getDir(FileSystemManager.getInstance().ATTACHMENT_DIR).toPath().resolve(mediaFile.getName()), StandardCopyOption.REPLACE_EXISTING);

					archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
					archive.save();
				} catch (IOException ex) {
					LOG.log(Level.WARNING, MessageFormat.format("Failed to copy media to path file: {0}", mediaFile.getName()), ex);
					addError(MessageFormat.format("Failed to copy media to path file: {0}", mediaFile.getName()));
				}
			}
		}

	}

	@Override
	public long getTotalRecords()
	{
		AttributeType attributeType = new AttributeType();
		attributeType.setActiveStatus(AttributeType.ACTIVE_STATUS);
		return service.getPersistenceService().countByExample(attributeType);
	}

}
