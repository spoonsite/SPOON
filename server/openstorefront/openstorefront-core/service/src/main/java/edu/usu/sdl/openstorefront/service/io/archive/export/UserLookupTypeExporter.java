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

import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import edu.usu.sdl.openstorefront.core.entity.LookupEntity;
import edu.usu.sdl.openstorefront.service.io.archive.BaseExporter;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.java.truevfs.access.TFile;

/**
 *
 * @author dshurtleff
 */
public class UserLookupTypeExporter
	extends BaseExporter	
{

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
	public List<BaseExporter> getAllRequiredExports()
	{
		List<BaseExporter> exporters = new ArrayList<>();
		exporters.add(this);		
		return exporters;
	}

	@Override
	public void exportRecords()
	{
		//export all user lookups
		List<Class> lookups = new ArrayList<>();
		
		Collection<Class<?>> entityClasses = DBManager.getConnection().getEntityManager().getRegisteredEntities();
		for (Class entityClass : entityClasses) {
			if (ReflectionUtil.LOOKUP_ENTITY.equals(entityClass.getSimpleName()) == false) {
				if (ReflectionUtil.isSubLookupEntity(entityClass)) {
					SystemTable systemTable = (SystemTable) entityClass.getAnnotation(SystemTable.class);
					if (systemTable == null) {
						lookups.add(entityClass);
					}
				}
			}
		}
		
		archive.setTotalRecords(archive.getTotalRecords() + lookups.size());
		archive.setStatusDetails("Exporting Lookups...");
		archive.save();
		
		for (Class lookup : lookups) {
			
			List<LookupEntity> lookupData = service.getLookupService().findLookup(lookup);			
			File lookupFile = new TFile(archiveBasePath + "/lookup/" + lookup.getSimpleName());
			
			try
			{
				StringProcessor.defaultObjectMapper().writeValue(lookupFile, lookupData);
			}
			catch (IOException ex)
			{
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
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
