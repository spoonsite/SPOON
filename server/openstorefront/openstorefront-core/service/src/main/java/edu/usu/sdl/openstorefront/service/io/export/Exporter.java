/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.io.export;

import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileOutputStream;
import net.java.truevfs.access.TVFS;
import net.java.truevfs.kernel.spec.FsSyncException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public abstract class Exporter
{

	private static final Logger LOG = Logger.getLogger(Exporter.class.getName());

	protected ServiceProxy service = ServiceProxy.getProxy();

	public File export(List<ComponentAll> components)
	{
		//open temp file
		String archiveName = FileSystemManager.SYSTEM_TEMP_DIR + "/export-" + System.currentTimeMillis() + ".zip";

		for (ComponentAll componentAll : components) {
			String name = StringUtils.left(StringProcessor.cleanFileName(componentAll.getComponent().getName()), 15);
			name = name.replace(" ", "_");

			File entry = new TFile(archiveName + "/describe-" + name + ".xml");
			try (OutputStream out = new TFileOutputStream(entry)) {
				writeRecord(out, componentAll);
			} catch (IOException ioe) {
				LOG.log(Level.WARNING, MessageFormat.format("Failed writing record: {0}", componentAll.getComponent().getName()));
			}
		}

		try {
			TVFS.umount();
		} catch (FsSyncException ex) {
			LOG.log(Level.SEVERE, "Unable to unmount zip.  It may not be readable.", ex);
		}

		File entry = new File(archiveName);
		return entry;
	}

	protected abstract void writeRecord(OutputStream out, ComponentAll record);

}
