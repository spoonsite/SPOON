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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.openstorefront.storage.model.ApplicationProperty;
import edu.usu.sdl.openstorefront.storage.model.Highlight;
import edu.usu.sdl.openstorefront.util.StringProcessor;
import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class HighlightImporter
		extends BaseDirImporter
{

	private static final Logger log = Logger.getLogger(HighlightImporter.class.getName());
	private static final String HIGHLIGHT_FILE_NAME = "highlights";

	@Override
	protected String getSyncProperty()
	{
		return ApplicationProperty.HIGHLIGHT_IMPORTER_LAST_SYNC_DTS;
	}

	@Override
	protected void processFile(File file)
	{
		if (file.getName().startsWith(HIGHLIGHT_FILE_NAME)) {
			log.log(Level.INFO, MessageFormat.format("Processing HightlightFile: ", file));

			try {
				ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();
				List<Highlight> highlights = objectMapper.readValue(file, new TypeReference<List<Highlight>>()
				{
				});
				serviceProxy.getSystemService().syncHighlights(highlights);

			} catch (Exception e) {
				log.log(Level.SEVERE, "Unable to process highlight file.  File should conform to JSON format for a Highlight type.", e);
			}
		} else {
			log.log(Level.WARNING, MessageFormat.format("Skipping unknown hightlight file: {0}  (File should be named hightlights.json)", file));
		}
	}

}
