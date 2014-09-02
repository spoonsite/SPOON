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

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.manager.Initializable;
import java.io.File;
import java.util.logging.Logger;
import org.quartz.jobs.DirectoryScanListener;

/**
 *
 * @author dshurtleff
 */
public class AttributeImporter
		implements DirectoryScanListener, Initializable
{

	private static final Logger log = Logger.getLogger(AttributeImporter.class.getName());
	private final ServiceProxy serviceProxy = new ServiceProxy();

	@Override
	public void filesUpdatedOrAdded(File[] updatedFiles)
	{
//		String lastSyncDts = serviceProxy.getSystemService().getPropertyValue(ApplicationProperty.ATTRIBUTE_IMPORTER_LAST_SYNC_DTS);
//		for (File file : updatedFiles) {
//
//			boolean process = true;
//			Date lastSyncDate = null;
//			if (lastSyncDts != null) {
//				lastSyncDate = TimeUtil.fromString(lastSyncDts);
//			}
//			if (lastSyncDate != null) {
//				if (file.lastModified() <= lastSyncDate.getTime()) {
//					process = false;
//				}
//			}
//
//			if (process) {
//				//log
//				log.log(Level.INFO, MessageFormat.format("Syncing lookup: {0}", file));
//
//			}
//		}
//		serviceProxy.getSystemService().saveProperty(ApplicationProperty.ATTRIBUTE_IMPORTER_LAST_SYNC_DTS, TimeUtil.dateToString(TimeUtil.currentDate()));
	}

	@Override
	public void initialize()
	{
//		String lastSyncDts = serviceProxy.getSystemService().getPropertyValue(ApplicationProperty.ATTRIBUTE_IMPORTER_LAST_SYNC_DTS);
//		if (lastSyncDts == null) {
//			//get the files and process.
//
//
//		} else {
//			//load cache
//
//		}
	}

	@Override
	public void shutdown()
	{
	}

}
