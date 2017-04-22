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
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.SystemArchiveOption;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.service.io.archive.BaseExporter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.truevfs.access.TFile;
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

				File entryFile = new TFile(archiveBasePath + DATA_DIR + "/comp-" + option.getEntityId() + ".json");
				try (OutputStream out = new TFileOutputStream(entryFile)) {
					StringProcessor.defaultObjectMapper().writeValue(out, componentAll);
				} catch (IOException ex) {
					LOG.log(Level.FINE, MessageFormat.format("Unable to export templates.{0}", ex));
					addError("Unable to export templates");
				}				
				
				for (ComponentMedia componentMedia : componentAll.getMedia()) {
					java.nio.file.Path mediaPath = componentMedia.pathToMedia();
					if (mediaPath != null) {
						if (mediaPath.toFile().exists()) {
							String name = mediaPath.getFileName().toString();
							
							java.nio.file.Path archiveMediaPath = new TPath(archiveBasePath + DATA_MEDIA_DIR + name);
							try (OutputStream out = new TFileOutputStream(archiveMediaPath.toFile())) {
								Files.copy(mediaPath, out);								
							}
							catch (IOException ex)
							{
								LOG.log(Level.SEVERE, null, ex);
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
							}
							catch (IOException ex)
							{
								LOG.log(Level.SEVERE, null, ex);
								addError("Unable to copy resources for entry: " + componentAll.getComponent().getName());
							}
							
						} else {
							LOG.log(Level.WARNING, MessageFormat.format("Resource not found (Not included in export) filename: {0}", componentResource.getFileName()));
							addError("Resource not found (Not included in export) filename: " + componentResource.getFileName());
						}
					}
				}
				
				archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
				archive.setStatusDetails("Exported entry " + componentAll.getComponent().getName());
				archive.save();
			}
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
		long records = 0;
		for (SystemArchiveOption option : archive.getArchiveOptions()) {
			if (Component.class.getSimpleName().equals(option.getPrimaryEntity())) {
				records++;
			}
		}
		return records;
	}

}
