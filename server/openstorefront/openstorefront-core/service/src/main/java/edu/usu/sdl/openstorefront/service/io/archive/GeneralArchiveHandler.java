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

import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.entity.SystemArchive;
import edu.usu.sdl.openstorefront.core.entity.SystemArchiveOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class GeneralArchiveHandler
		extends AbstractArchiveHandler
{

	private static final Logger LOG = Logger.getLogger(GeneralArchiveHandler.class.getName());

	public GeneralArchiveHandler(SystemArchive archive)
	{
		super(archive);
	}

	@Override
	protected void generateExport()
	{
		ArchiveManifest manifest = new ArchiveManifest();

		List<BaseExporter> allExporters = getExporters();
		long totalRecords = 0;
		for (BaseExporter exporter : allExporters) {
			exporter.exporterInit();
			long entityRecords = exporter.getTotalRecords();
			manifest.getEntityRecords().add(new EntityManifestRecord(exporter.getExporterSupportEntity(), entityRecords));
			totalRecords += entityRecords;
		}
		manifest.setTotalRecords(totalRecords);
		archive.setTotalRecords(totalRecords);
		archive.setRecordsProcessed(0L);
		archive.save();

		for (BaseExporter exporter : allExporters) {
			try {
				archive.setStatusDetails("Exporting: " + exporter.getExporterSupportEntity() + "...");
				archive.save();
				exporter.exportRecords();
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Unable to complete exporter: " + exporter.getExporterSupportEntity(), e);
				addError("Unable to complete exporter (May not have all records): " + exporter.getExporterSupportEntity());
			}
		}
		createManifest(manifest);

		archive.setStatusDetails("Done");
		archive.save();
	}

	@Override
	protected void processImport(ArchiveManifest manifest)
	{
		List<BaseExporter> allExporters = new ArrayList<>();
		Map<String, BaseExporter> exporterMap = new HashMap<>();

		for (EntityManifestRecord record : manifest.getEntityRecords()) {
			BaseExporter exporter = BaseExporter.getExporter(record.getEntityName());
			if (exporter != null) {
				List<BaseExporter> dependantExports = exporter.getAllRequiredExports();
				for (BaseExporter dependantExport : dependantExports) {
					exporterMap.put(dependantExport.getExporterSupportEntity(), dependantExport);
				}
			} else {
				addError("Entity not supported: " + record.getEntityName());
			}
		}
		allExporters.addAll(exporterMap.values());
		allExporters.sort((o1, o2) -> {
			return Integer.compare(o1.getPriority(), o2.getPriority());
		});
		for (BaseExporter exporter : allExporters) {
			exporter.init(archive, fullArchiveName);
		}

		for (BaseExporter exporter : allExporters) {
			try {
				archive.setStatusDetails("Importing: " + exporter.getExporterSupportEntity() + "...");
				archive.save();

				exporter.importRecords();
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Unable to complete importing: " + exporter.getExporterSupportEntity(), e);
				addError("Unable to complete importing (May not have all records): " + exporter.getExporterSupportEntity());
			}
		}

		archive.setRecordsProcessed(archive.getTotalRecords());

		archive.setStatusDetails("Done");
		archive.save();
	}

	private List<BaseExporter> getExporters()
	{
		List<BaseExporter> allExporters = new ArrayList<>();
		Map<String, BaseExporter> exporterMap = new HashMap<>();

		for (SystemArchiveOption option : archive.getArchiveOptions()) {
			BaseExporter exporter = BaseExporter.getExporter(option.getPrimaryEntity());
			if (exporter != null) {
				if(Convert.toBoolean(archive.getIncludeRelatedEntities())){
					List<BaseExporter> dependantExports = exporter.getAllRequiredExports();
					for (BaseExporter dependantExport : dependantExports) {
						exporterMap.put(dependantExport.getExporterSupportEntity(), dependantExport);
					}
				}
				else {
					exporterMap.put(exporter.getExporterSupportEntity(), exporter);
				}
			} else {
				addError("Entity not supported: " + option.getPrimaryEntity());
			}
		}
		allExporters.addAll(exporterMap.values());
		allExporters.sort((o1, o2) -> {
			return Integer.compare(o1.getPriority(), o2.getPriority());
		});

		for (BaseExporter exporter : allExporters) {
			exporter.init(archive, fullArchiveName);
		}

		return allExporters;
	}

}
