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
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.service.io.archive.BaseExporter;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.truevfs.access.TFile;

/**
 *
 * @author dshurtleff
 */
public class EntryTypeExporter
		extends BaseExporter
{

	private static final Logger LOG = Logger.getLogger(EntryTypeExporter.class.getName());
	private static final String DATA_DIR = "/entrytypes/";

	@Override
	public int getPriority()
	{
		return 4;
	}

	@Override
	public String getExporterSupportEntity()
	{
		return ComponentType.class.getSimpleName();
	}

	@Override
	public List<BaseExporter> getAllRequiredExports()
	{
		List<BaseExporter> exporters = new ArrayList<>();
		exporters.add(new EntryTemplateExporter());
		exporters.add(this);
		return exporters;
	}

	@Override
	public void exportRecords()
	{
		ComponentType componentType = new ComponentType();
		componentType.setActiveStatus(ComponentType.ACTIVE_STATUS);
		List<ComponentType> types = componentType.findByExample();

		File dataFile = new TFile(archiveBasePath + DATA_DIR + "types.json");

		try {
			StringProcessor.defaultObjectMapper().writeValue(dataFile, types);
		} catch (IOException ex) {
			LOG.log(Level.FINE, MessageFormat.format("Unable to export entry types.{0}", ex));
			addError("Unable to entry types");
		}

		archive.setRecordsProcessed(archive.getRecordsProcessed() + types.size());
		archive.setStatusDetails("Exported " + types.size() + " entry types");
		archive.save();
	}

	@Override
	public void importRecords()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public long getTotalRecords()
	{
		ComponentType componentType = new ComponentType();
		componentType.setActiveStatus(ComponentType.ACTIVE_STATUS);
		return service.getPersistenceService().countByExample(componentType);
	}

}
