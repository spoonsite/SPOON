/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.example.importer;

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.FileFormat;
import edu.usu.sdl.openstorefront.core.entity.FileType;
import java.util.logging.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator
		implements BundleActivator
{

	private static final Logger LOG = Logger.getLogger(Activator.class.getName());

	@Override
	public void start(BundleContext context) throws Exception
	{
		LOG.info("Starting Example Import plugin");
		//Register new parser format
		Service service = ServiceProxyFactory.getServiceProxy();

		if (service.getSystemService().isSystemReady()
				|| service.getSystemService().isLoadingPluginsReady()) {
			FileFormat format = new FileFormat();
			format.setCode(ExampleComponentImporter.FORMAT_CODE);
			format.setFileType(FileType.COMPONENT);
			format.setDescription("Example Map Import (txt)");
			format.setSupportsDataMap(true);
			format.setParserClass(ExampleComponentImporter.class.getName());

			service.getImportService().registerFormat(format, ExampleComponentImporter.class);
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception
	{
		Service service = ServiceProxyFactory.getServiceProxy();

		if (service.getSystemService().isSystemReady()
				|| service.getSystemService().isLoadingPluginsReady()) {
			//unregister parsers
			service.getImportService().unregisterFormat(ExampleComponentImporter.class.getName());
		}
	}

}
