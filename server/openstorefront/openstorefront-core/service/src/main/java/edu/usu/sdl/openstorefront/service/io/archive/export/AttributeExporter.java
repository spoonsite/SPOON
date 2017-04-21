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
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.model.AttributeAll;
import edu.usu.sdl.openstorefront.service.io.archive.BaseExporter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.java.truevfs.access.TFile;

/**
 *
 * @author dshurtleff
 */
public class AttributeExporter
		extends BaseExporter
{

	private List<AttributeType> attributesToExport = new ArrayList<>();
	private static final String DATA_DIR = "/attribute/";

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
	public void exporterInit()
	{
		super.exporterInit();

		AttributeType attributeType = new AttributeType();
		attributeType.setActiveStatus(AttributeType.ACTIVE_STATUS);
		attributesToExport = attributeType.findByExample();
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
		for (AttributeType attributeType : attributesToExport) {

			AttributeAll attributeAll = new AttributeAll();
			attributeAll.setAttributeType(attributeType);
			attributeAll.setAttributeCodes(service.getAttributeService().findCodesForType(attributeType.getAttributeType()));
			File attributeFile = new TFile(archiveBasePath + DATA_DIR + attributeType.getAttributeType());

			try {
				StringProcessor.defaultObjectMapper().writeValue(attributeFile, attributeAll);
			} catch (IOException ex) {
				addError("Unable to export: " + attributeType.getDescription());
			}

			archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
			archive.setStatusDetails("Exported " + attributeType.getDescription());
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
		return attributesToExport.size();
	}

}
