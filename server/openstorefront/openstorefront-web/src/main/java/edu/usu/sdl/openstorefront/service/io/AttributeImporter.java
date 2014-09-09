/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.io;

import edu.usu.sdl.openstorefront.service.io.parser.BaseAttributeParser;
import edu.usu.sdl.openstorefront.service.io.parser.MainAttributeParser;
import edu.usu.sdl.openstorefront.service.io.parser.SvcAttributeParser;
import edu.usu.sdl.openstorefront.service.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.service.manager.Initializable;
import edu.usu.sdl.openstorefront.storage.model.ApplicationProperty;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class AttributeImporter
		extends BaseDirImporter
		implements Initializable
{

	private static final Logger log = Logger.getLogger(AttributeImporter.class.getName());

	@Override
	public void initialize()
	{
		String lastSyncDts = serviceProxy.getSystemService().getPropertyValue(ApplicationProperty.ATTRIBUTE_IMPORTER_LAST_SYNC_DTS);
		if (lastSyncDts == null) {
			//get the files and process.
			List<File> attributeFiles = new ArrayList<>();
			for (FileMap fileMap : FileMap.values()) {
				attributeFiles.add(FileSystemManager.getImportAttribute(fileMap.getFilename()));
			}
			filesUpdatedOrAdded(attributeFiles.toArray(new File[0]));
		} else {
			//load cache
			serviceProxy.getAttributeService().refreshCache();
		}
	}

	@Override
	public void shutdown()
	{
	}

	@Override
	protected String getSyncProperty()
	{
		return ApplicationProperty.ATTRIBUTE_IMPORTER_LAST_SYNC_DTS;
	}

	@Override
	protected void processFile(File file)
	{
		//log
		log.log(Level.INFO, MessageFormat.format("Syncing Attributes: {0}", file));
		for (FileMap fileMap : FileMap.values()) {
			if (fileMap.getFilename().equals(file.getName())) {
				try {
					Map<AttributeType, List<AttributeCode>> attributeMap = fileMap.getParser().parse(new FileInputStream(file));
					serviceProxy.getAttributeService().syncAttribute(attributeMap);
				} catch (FileNotFoundException ex) {
					log.log(Level.SEVERE, "Failed processing file: " + file, ex);
				}
			}
		}
	}

	private enum FileMap
	{

		ATTIBUTES("allattributes.csv", new MainAttributeParser()),
		SVCV4("svcv-4_export.csv", new SvcAttributeParser());

		private final String filename;
		private final BaseAttributeParser parser;

		private FileMap(String filename, BaseAttributeParser parser)
		{
			this.filename = filename;
			this.parser = parser;
		}

		public String getFilename()
		{
			return filename;
		}

		public BaseAttributeParser getParser()
		{
			return parser;
		}

	}

}
