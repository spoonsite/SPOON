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

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.SystemArchiveOption;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.service.io.archive.BaseExporter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class ComponentExporter
		extends BaseExporter
{

	private static final Logger LOG = Logger.getLogger(ComponentExporter.class.getName());

	private static final String DATA_DIR = "/component/";
	private static final String DATA_MEDIA_DIR = "/component/media/";
	private static final String DATA_RESOURCE_DIR = "/component/resources/";

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

//				File entry = new TFile(archiveName + "/components.json");
//				try (Writer writer = new TFileWriter(entry)) {
//					writer.write(componentJson);
//				} catch (IOException io) {
//					throw new OpenStorefrontRuntimeException("Unable to export components.", io);
//				}
//				//media
//				for (ComponentMedia componentMedia : componentAll.getMedia()) {
//					java.nio.file.Path mediaPath = componentMedia.pathToMedia();
//					if (mediaPath != null) {
//						if (mediaPath.toFile().exists()) {
//							String name = mediaPath.getFileName().toString();
//							if (fileNameMediaSet.contains(name) == false) {
//								java.nio.file.Path archiveMediaPath = new TPath(archiveName + "/media/" + name);
//								Files.copy(mediaPath, archiveMediaPath);
//								fileNameMediaSet.add(name);
//							}
//						} else {
//							LOG.log(Level.WARNING, MessageFormat.format("Media not found (Not included in export) filename: {0}", componentMedia.getFileName()));
//						}
//					}
//				}
//
//				//localreources
//				for (ComponentResource componentResource : componentAll.getResources()) {
//					java.nio.file.Path resourcePath = componentResource.pathToResource();
//					if (resourcePath != null) {
//						if (resourcePath.toFile().exists()) {
//							String name = resourcePath.getFileName().toString();
//							if (fileNameResourceSet.contains(name) == false) {
//								java.nio.file.Path archiveResourcePath = new TPath(archiveName + "/resources/" + name);
//								Files.copy(resourcePath, archiveResourcePath);
//								fileNameResourceSet.add(name);
//							}
//						} else {
//							LOG.log(Level.WARNING, MessageFormat.format("Resource not found (Not included in export) filename: {0}", componentResource.getFileName()));
//						}
//					}
//				}
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
