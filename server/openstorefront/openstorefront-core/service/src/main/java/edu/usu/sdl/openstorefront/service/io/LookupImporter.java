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

import au.com.bytecode.opencsv.CSVReader;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import edu.usu.sdl.openstorefront.core.entity.ApplicationProperty;
import edu.usu.sdl.openstorefront.core.entity.LookupEntity;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class LookupImporter
		extends BaseDirImporter
		implements Initializable
{

	private static final Logger LOG = Logger.getLogger(LookupImporter.class.getName());

	@Override
	@SuppressWarnings("squid:S1872")
	public void initialize()
	{
		String lastSyncDts = serviceProxy.getSystemService().getPropertyValue(ApplicationProperty.LOOKUP_IMPORTER_LAST_SYNC_DTS);
		if (lastSyncDts == null) {
			//get the files and process.

			List<File> lookupCodeFiles = new ArrayList<>();

			Collection<Class<?>> entityClasses = DBManager.getInstance().getConnection().getEntityManager().getRegisteredEntities();
			for (Class entityClass : entityClasses) {
				if (ReflectionUtil.LOOKUP_ENTITY.equals(entityClass.getSimpleName()) == false) {
					if (ReflectionUtil.isSubLookupEntity(entityClass)) {
						@SuppressWarnings("unchecked")
						SystemTable systemTable = (SystemTable) entityClass.getAnnotation(SystemTable.class);
						if (systemTable != null) {
							LOG.log(Level.WARNING, "Skipping System Table.   System Tables are not user-definable.");
						} else {
							File codeFile = FileSystemManager.getInstance().getImportLookup(entityClass.getSimpleName() + ".csv");
							if (codeFile.exists()) {
								lookupCodeFiles.add(codeFile);
							}
						}
					}
				}
			}

			filesUpdatedOrAdded(lookupCodeFiles.toArray(new File[0]));
		} else {
			//Put in defaults, if needed
			Collection<Class<?>> entityClasses = DBManager.getInstance().getConnection().getEntityManager().getRegisteredEntities();
			for (Class entityClass : entityClasses) {
				if (ReflectionUtil.LOOKUP_ENTITY.equals(entityClass.getSimpleName()) == false) {
					if (ReflectionUtil.isSubLookupEntity(entityClass)) {
						@SuppressWarnings("unchecked")
						SystemTable systemTable = (SystemTable) entityClass.getAnnotation(SystemTable.class);
						if (systemTable == null) {
							FileSystemManager.getInstance().getImportLookup(entityClass.getSimpleName() + ".csv", (File newFile) -> {
								File files[] = new File[1];
								files[0] = newFile;
								filesUpdatedOrAdded(files, true);
							});
						}
					}
				}
			}
		}
	}

	@Override
	public void shutdown()
	{
	}

	@Override
	public boolean isStarted()
	{
		return true;
	}

	@Override
	protected String getSyncProperty()
	{
		return ApplicationProperty.LOOKUP_IMPORTER_LAST_SYNC_DTS;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void processFile(File file)
	{
		//log
		LOG.log(Level.INFO, () -> MessageFormat.format("Syncing lookup: {0}", file));

		//parse
		List<LookupEntity> lookupEntities = new ArrayList<>();
		String className = file.getName().replace(".csv", "");
		Class lookupClass = null;
		try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file)))) {

			lookupClass = Class.forName(DBManager.getInstance().getEntityModelPackage() + "." + className);
			@SuppressWarnings("unchecked")
			SystemTable systemTable = (SystemTable) lookupClass.getAnnotation(SystemTable.class);
			if (systemTable == null) {
				List<String[]> allData = reader.readAll();
				for (String data[] : allData) {
					try {
						LookupEntity lookupEntity = (LookupEntity) lookupClass.newInstance();
						lookupEntity.importData(data);
						lookupEntities.add(lookupEntity);
					} catch (IllegalAccessException | InstantiationException e) {
						LOG.log(Level.WARNING, () -> MessageFormat.format("{0} -  Unable Process line: {1} in file: {2}", e.toString(), Arrays.toString(data), file));
					}
				}
			} else {
				LOG.log(Level.WARNING, "Skipping System Table.   System Tables are not user-definable.");
			}
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Unable to read file: " + file, ex);
		} catch (ClassNotFoundException ex) {
			LOG.log(Level.SEVERE, "Unable to find Lookup Class for file:  " + file, ex);
		}

		//call sync
		if (lookupClass != null) {
			serviceProxy.getLookupService().syncLookupImport(lookupClass, lookupEntities);
		}
	}

}
