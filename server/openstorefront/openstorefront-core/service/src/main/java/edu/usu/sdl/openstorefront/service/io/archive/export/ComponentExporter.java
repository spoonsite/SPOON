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
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.entity.SystemArchiveOption;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.service.io.archive.BaseExporter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import net.java.truevfs.access.TFileOutputStream;
import net.java.truevfs.access.TPath;

/**
 *
 * @author dshurtleff
 */
public class ComponentExporter
		extends BaseExporter
{

	private static final Logger LOG = Logger.getLogger(ComponentExporter.class.getName());

	private static final String DATA_DIR = "/components/";
	private static final String DATA_MEDIA_DIR = "/components/media/";
	private static final String DATA_RESOURCE_DIR = "/components/resources/";

	@Override
	public int getPriority()
	{
		return 6;
	}

	@Override
	public String getExporterSupportEntity()
	{
		return Component.class.getSimpleName();
	}

	@Override
	public List<BaseExporter> getAllRequiredExports()
	{
		List<BaseExporter> exporters = new ArrayList<>();

		exporters.add(new UserLookupTypeExporter());
		exporters.add(new GeneralMediaExporter());
		exporters.add(new EntryTypeExporter());
		exporters.add(new EntryTemplateExporter());
		exporters.add(new AttributeExporter());

		exporters.add(this);

		exporters.add(new ChecklistQuestionExporter());
		exporters.add(new ChecklistTemplateExporter());
		exporters.add(new SectionTemplateExporter());

		exporters.add(new EvaluationTemplateExporter());
		exporters.add(new EvaluationExporter());

		return exporters;
	}

	@Override
	public void exportRecords()
	{
		for (SystemArchiveOption option : archive.getArchiveOptions()) {
			if (Component.class.getSimpleName().equals(option.getPrimaryEntity())) {
				ComponentAll componentAll = service.getComponentService().getFullComponent(option.getEntityId());
				if (componentAll != null) {
					exportComponent(componentAll);

					archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
					archive.setStatusDetails("Exported entry " + componentAll.getComponent().getName());
					archive.save();
				} else {
					LOG.log(Level.WARNING, MessageFormat.format("Unable to find component: {0}", option.getEntityId()));
				}
			}
		}

	}

	public void exportComponent(ComponentAll componentAll)
	{
		File entryFile = new TFile(archiveBasePath + DATA_DIR + "/comp-" + componentAll.getComponent().getComponentId() + ".json");
		try (OutputStream out = new TFileOutputStream(entryFile)) {
			StringProcessor.defaultObjectMapper().writeValue(out, componentAll);
		} catch (IOException ex) {
			LOG.log(Level.WARNING, MessageFormat.format("Unable to export entry. {0}", componentAll.getComponent().getName()), ex);
			addError("Unable to export entry: " + componentAll.getComponent().getName());
		}

		for (ComponentMedia componentMedia : componentAll.getMedia()) {
			java.nio.file.Path mediaPath = componentMedia.pathToMedia();
			if (mediaPath != null) {
				if (mediaPath.toFile().exists()) {
					String name = mediaPath.getFileName().toString();

					java.nio.file.Path archiveMediaPath = new TPath(archiveBasePath + DATA_MEDIA_DIR + name);
					try (OutputStream out = new TFileOutputStream(archiveMediaPath.toFile())) {
						Files.copy(mediaPath, out);
					} catch (IOException ex) {
						LOG.log(Level.WARNING, null, ex);
						addError("Unable to copy media for entry: " + componentAll.getComponent().getName());
					}

				} else {
					LOG.log(Level.WARNING, MessageFormat.format("Media not found (Not included in export) filename: {0}", componentMedia.getFileName()));
					addError("Media not found (Not included in export) filename: " + componentMedia.getFileName());
				}
			}
		}

		for (ComponentResource componentResource : componentAll.getResources()) {
			java.nio.file.Path resourcePath = componentResource.pathToResource();
			if (resourcePath != null) {
				if (resourcePath.toFile().exists()) {
					String name = resourcePath.getFileName().toString();

					java.nio.file.Path archiveResourcePath = new TPath(archiveBasePath + DATA_RESOURCE_DIR + name);
					try (OutputStream out = new TFileOutputStream(archiveResourcePath.toFile())) {
						Files.copy(resourcePath, out);
					} catch (IOException ex) {
						LOG.log(Level.SEVERE, null, ex);
						addError("Unable to copy resources for entry: " + componentAll.getComponent().getName());
					}

				} else {
					LOG.log(Level.WARNING, MessageFormat.format("Resource not found (Not included in export) filename: {0}", componentResource.getFileName()));
					addError("Resource not found (Not included in export) filename: " + componentResource.getFileName());
				}
			}
		}
	}

	@Override
	public void importRecords()
	{

		File dataDir = new TFile(archiveBasePath + DATA_DIR);
		File files[] = dataDir.listFiles();
		if (files != null) {
			for (File dataFile : files) {
				if (dataFile.isFile()) {
					try (InputStream in = new TFileInputStream(dataFile)) {
						archive.setStatusDetails("Importing: " + dataFile.getName());
						archive.save();

						ComponentAll componentAll = StringProcessor.defaultObjectMapper().readValue(in, ComponentAll.class);

						FileHistoryOption options = new FileHistoryOption();
						options.setSkipRequiredAttributes(Boolean.TRUE);
						service.getComponentService().saveFullComponent(componentAll, options);

						archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
						archive.save();

					} catch (Exception ex) {
						LOG.log(Level.WARNING, "Failed to Load component", ex);
						addError("Unable to load component: " + dataFile.getName());
					}
				}
			}

			archive.setStatusDetails("Importing entry media");
			archive.save();

			TFile mediaDir = new TFile(archiveBasePath + DATA_MEDIA_DIR);
			TFile media[] = mediaDir.listFiles();
			if (media != null) {
				for (TFile mediaFile : media) {
					try {
						Files.copy(mediaFile.toPath(), FileSystemManager.getDir(FileSystemManager.MEDIA_DIR).toPath().resolve(mediaFile.getName()), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException ex) {
						LOG.log(Level.WARNING, MessageFormat.format("Failed to copy media to path file: {0}", mediaFile.getName()), ex);
						addError(MessageFormat.format("Failed to copy media to path file: {0}", mediaFile.getName()));
					}
				}
			}

			archive.setStatusDetails("Importing entry resources");
			archive.save();

			TFile resourceDir = new TFile(archiveBasePath + DATA_RESOURCE_DIR);
			TFile resources[] = resourceDir.listFiles();
			if (resources != null) {
				for (TFile resourceFile : resources) {
					try {
						Files.copy(resourceFile.toPath(), FileSystemManager.getDir(FileSystemManager.RESOURCE_DIR).toPath().resolve(resourceFile.getName()), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException ex) {
						LOG.log(Level.WARNING, MessageFormat.format("Failed to copy resource to path file: {0}", resourceFile.getName()), ex);
						addError(MessageFormat.format("Failed to copy resource to path file: {0}", resourceFile.getName()));
					}
				}
			}
		} else {
			LOG.log(Level.FINE, "No components to load.");
		}

	}

	@Override
	public long getTotalRecords()
	{
		long records = 0;
		for (SystemArchiveOption option : archive.getArchiveOptions()) {
			if (Component.class.getSimpleName().equals(option.getPrimaryEntity())) {
				records++;
			}
		}
		return records;
	}

}
