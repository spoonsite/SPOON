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
package edu.usu.sdl.openstorefront.service.job;

import au.com.bytecode.opencsv.CSVReader;
import cern.colt.Arrays;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.service.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.storage.model.ApplicationProperty;
import edu.usu.sdl.openstorefront.storage.model.LookupEntity;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.jobs.DirectoryScanListener;

/**
 *
 * @author dshurtleff
 */
public class LookupImporter
		implements DirectoryScanListener
{

	private static final Logger log = Logger.getLogger(LookupImporter.class.getName());

	private static final int CODE = 0;
	private static final int DESCRIPTION = 1;
	private static final int DETAILED_DESCRIPTION = 2;

	private final ServiceProxy serviceProxy = new ServiceProxy();

	@Override
	public void filesUpdatedOrAdded(File[] updatedFiles)
	{
		String lastSyncDts = serviceProxy.getSystemService().getPropertyValue(ApplicationProperty.LOOKUP_IMPORTER_LAST_SYNC_DTS);
		for (File file : updatedFiles) {

			boolean process = true;
			Date lastSyncDate = null;
			if (lastSyncDts != null) {
				lastSyncDate = TimeUtil.fromString(lastSyncDts);
			}
			if (lastSyncDate != null) {
				if (file.lastModified() <= lastSyncDate.getTime()) {
					process = false;
				}
			}

			if (process) {
				//log
				log.log(Level.INFO, "Syncing lookup: {0}", file);

				//parse
				List<LookupEntity> lookupEntities = new ArrayList<>();
				String className = file.getName().replace(".csv", "");
				Class lookupClass = null;
				try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file)));) {

					lookupClass = Class.forName("edu.usu.sdl.openstorefront.storage.model." + className);
					List<String[]> allData = reader.readAll();
					for (String data[] : allData) {
						if (data.length >= 2) {

							LookupEntity lookupEntity = (LookupEntity) lookupClass.newInstance();
							lookupEntity.setCode(data[CODE].trim().toUpperCase());
							lookupEntity.setDescription(data[DESCRIPTION].trim());

							if (data.length >= 3) {
								lookupEntity.setDetailedDecription(data[DETAILED_DESCRIPTION].trim());
							}

							lookupEntities.add(lookupEntity);
						} else {
							log.log(Level.WARNING, "(Missing Required Fields  -Code,Description) Unable Process line: {0} in file: {1}", new Object[]{Arrays.toString(data), file});
						}
					}

				} catch (IOException ex) {
					log.log(Level.SEVERE, "Unable to read file: " + file, ex);
				} catch (ClassNotFoundException ex) {
					log.log(Level.SEVERE, "Unable to find Lookup Class for file:  " + file, ex);
				} catch (InstantiationException ex) {
					log.log(Level.SEVERE, "System error on:  " + file, ex);
				} catch (IllegalAccessException ex) {
					log.log(Level.SEVERE, "System error on:   " + file, ex);
				}

				//call sync
				if (lookupClass != null) {
					serviceProxy.getLookupService().syncLookupImport(lookupClass, lookupEntities);
				}
			}
		}
		serviceProxy.getSystemService().saveProperty(ApplicationProperty.LOOKUP_IMPORTER_LAST_SYNC_DTS, TimeUtil.dateToString(TimeUtil.currentDate()));
	}

	public void initImport()
	{
		String lastSyncDts = serviceProxy.getSystemService().getPropertyValue(ApplicationProperty.LOOKUP_IMPORTER_LAST_SYNC_DTS);
		if (lastSyncDts == null) {
			//get the files and process.

			List<File> lookupCodeFiles = new ArrayList<>();

			Collection<Class<?>> entityClasses = DBManager.getConnection().getEntityManager().getRegisteredEntities();
			for (Class entityClass : entityClasses) {
				if ("LookupEntity".equals(entityClass.getSimpleName()) == false) {
					if (isSubLookupEntity(entityClass)) {
						File codeFile = FileSystemManager.getImportLookup(entityClass.getSimpleName() + ".csv");
						if (codeFile.exists()) {
							lookupCodeFiles.add(codeFile);
						}
					}
				}
			}

			filesUpdatedOrAdded((File[]) lookupCodeFiles.toArray(new File[0]));
		}
	}

	private boolean isSubLookupEntity(Class entityClass)
	{
		if (entityClass == null) {
			return false;
		}

		if ("LookupEntity".equals(entityClass.getSimpleName())) {
			return true;
		} else {
			return isSubLookupEntity(entityClass.getSuperclass());
		}
	}
}
