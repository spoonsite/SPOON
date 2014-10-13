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
import edu.usu.sdl.openstorefront.service.transfermodel.ComponentAll;
import edu.usu.sdl.openstorefront.storage.model.ApplicationProperty;
import edu.usu.sdl.openstorefront.util.StringProcessor;
import java.io.File;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class ComponentImporter
		extends BaseDirImporter
{

	private static final Logger log = Logger.getLogger(ComponentImporter.class.getName());

	@Override
	protected String getSyncProperty()
	{
		return ApplicationProperty.COMPONENT_IMPORTER_LAST_SYNC_DTS;
	}

	@Override
	protected void processFile(File file)
	{
		log.log(Level.INFO, "Processing Component: " + file);

		try {
			ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();
			ComponentAll componentAll = objectMapper.readValue(file, new TypeReference<ComponentAll>()
			{
			});
			componentAll = serviceProxy.getComponentService().saveFullComponent(componentAll);
			serviceProxy.getSearchService().addIndex(componentAll.getComponent());

			objectMapper.writeValue(file, componentAll);
			//set it to the past so we don't keep picking it up.
			file.setLastModified(file.lastModified() - 10000);

		} catch (Exception e) {
			log.log(Level.SEVERE, "Unable to process component file.  File should conform to JSON format for a component type.", e);
		}
		log.log(Level.INFO, MessageFormat.format("Saved: ", file));
	}

}
