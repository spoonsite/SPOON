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

import com.fasterxml.jackson.databind.JsonNode;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import edu.usu.sdl.openstorefront.core.entity.LookupEntity;
import edu.usu.sdl.openstorefront.service.io.archive.BaseExporter;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import net.java.truevfs.access.TFileOutputStream;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class UserLookupTypeExporter
		extends BaseExporter
{

	private static final Logger LOG = Logger.getLogger(UserLookupTypeExporter.class.getName());
	private static final String LOOKUP_DIR = "/lookup/";

	private final List<Class> lookupsToExport = new ArrayList<>();

	@Override
	public int getPriority()
	{
		return 1;
	}

	@Override
	public String getExporterSupportEntity()
	{
		return LookupEntity.class.getSimpleName();
	}

	@Override
	public void exporterInit()
	{
		super.exporterInit();

		Collection<Class<?>> entityClasses = DBManager.getConnection().getEntityManager().getRegisteredEntities();
		for (Class entityClass : entityClasses) {
			if (ReflectionUtil.LOOKUP_ENTITY.equals(entityClass.getSimpleName()) == false) {
				if (ReflectionUtil.isSubLookupEntity(entityClass)) {
					SystemTable systemTable = (SystemTable) entityClass.getAnnotation(SystemTable.class);
					if (systemTable == null) {
						lookupsToExport.add(entityClass);
					}
				}
			}
		}
	}

	@Override
	public List<BaseExporter> getAllRequiredExports()
	{
		List<BaseExporter> exporters = new ArrayList<>();
		exporters.add(this);
		return exporters;
	}

	@Override
	public void exportRecords()
	{
		for (Class lookup : lookupsToExport) {

			List<LookupEntity> lookupData = service.getLookupService().findLookup(lookup);
			File lookupFile = new TFile(archiveBasePath + LOOKUP_DIR + lookup.getSimpleName());

			try (OutputStream out = new TFileOutputStream(lookupFile)) {
				StringProcessor.defaultObjectMapper().writeValue(out, lookupData);
			} catch (IOException ex) {
				addError("Unable to export: " + lookup.getSimpleName());
			}

			archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
			archive.setStatusDetails("Exported " + lookup.getSimpleName());
			archive.save();
		}

	}

	@Override
	public void importRecords()
	{
		//this doesn't support replace as that could cause issues.
//		if (archive.getImportModeType().equals(ImportModeType.REPLACE)) {
//			LOG.log(Level.FINE, "Lookup import doesn't support replace...continuing as merge");
//		}

		File lookupDir = new TFile(archiveBasePath + LOOKUP_DIR);
		File files[] = lookupDir.listFiles();
		if (files != null) {
			for (File lookupFile : files) {
				try (InputStream in = new TFileInputStream(lookupFile)) {
					archive.setStatusDetails("Importing: " + lookupFile.getName());
					archive.save();

					Class lookupClass = Class.forName("edu.usu.sdl.openstorefront.core.entity." + lookupFile.getName());

					List<LookupEntity> lookupData = new ArrayList<>();
					JsonNode nodeArray = StringProcessor.defaultObjectMapper().readTree(in);

					Iterator<JsonNode> nodes = nodeArray.elements();
					while (nodes.hasNext()) {
						JsonNode node = nodes.next();
						LookupEntity lookup = (LookupEntity) lookupClass.newInstance();

						Iterator<String> fieldNames = node.fieldNames();
						while (fieldNames.hasNext()) {
							String fieldName = fieldNames.next();

							if (node.get(fieldName).isTextual()) {
								BeanUtils.setProperty(lookup, fieldName, node.get(fieldName).asText());
							} else if (node.get(fieldName).isNumber()) {
								if (node.get(fieldName).canConvertToInt()) {
									BeanUtils.setProperty(lookup, fieldName, node.get(fieldName).asInt());
								}
							}
						}
						lookupData.add(lookup);
					}

					service.getLookupService().syncLookupImport(lookupClass, lookupData);

					archive.setRecordsProcessed(archive.getRecordsProcessed() + 1);
					archive.save();

				} catch (InvocationTargetException | IllegalAccessException | InstantiationException | ClassNotFoundException | IOException ex) {
					LOG.log(Level.WARNING, "Failed to Load loookup", ex);
					addError("Unable to load lookup: " + lookupFile.getName());
				}
			}
		} else {
			LOG.log(Level.FINE, "No Lookups to load.");
		}
	}

	@Override
	public long getTotalRecords()
	{
		return lookupsToExport.size();
	}

}
