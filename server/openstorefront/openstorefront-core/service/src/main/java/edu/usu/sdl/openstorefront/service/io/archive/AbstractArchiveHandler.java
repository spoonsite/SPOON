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
package edu.usu.sdl.openstorefront.service.io.archive;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.IODirectionType;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.entity.SystemArchive;
import edu.usu.sdl.openstorefront.core.entity.SystemArchiveType;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public abstract class AbstractArchiveHandler
{
	private static final Logger LOG = Logger.getLogger(AbstractArchiveHandler.class.getName());
	
	protected SystemArchive archive;
	private boolean addedError = false;
	protected ServiceProxy service = new ServiceProxy();

	public AbstractArchiveHandler(SystemArchive archive)
	{
		this.archive = archive;
	}
	
	public static void processArchive(SystemArchive archive)
	{
		AbstractArchiveHandler handler;
		
		//determine handler
		switch (archive.getSystemArchiveType()) {
			case SystemArchiveType.DBEXPORT:
				handler = new DBArchiveHandler(archive);
				break;
				
			case SystemArchiveType.GENERAL:
				handler = new GeneralArchiveHandler(archive);
				break;				
				
			default:
				throw new OpenStorefrontRuntimeException("Archive type not supported.", "Archive Type: " + archive.getSystemArchiveType());
		}

		handler.startWork();
		switch (archive.getIoDirectionType()) {
			case IODirectionType.EXPORT:
				handler.handleExport();
				break; 
			case IODirectionType.IMPORT:
				handler.handleImport();
				break; 
				
			default:
				throw new OpenStorefrontRuntimeException("IO type not supported.", "IO Type: " + archive.getIoDirectionType());					
		}
		handler.completeWork();
	}

	protected void handleExport() 
	{
		
		//create archive 
		
		//generateExport
		generateExport();
		
	}
	
	protected abstract void generateExport();
	
	protected void handleImport() 
	{
		
		//open archive 
		
		
		processImport();
	}	
	
	protected abstract void processImport();
	
	private void startWork()
	{
		archive.setRunStatus(RunStatus.WORKING);
		archive.setStartDts(TimeUtil.currentDate());		
		archive.save();
	}
	
	private void completeWork() 
	{
		if (addedError) {
			archive.setRunStatus(RunStatus.ERROR);
		} else {
			archive.setRunStatus(RunStatus.COMPLETE);
		}
		archive.setCompletedDts(TimeUtil.currentDate());		
		archive.save();			
	}
	
	protected void addError(String message)
	{
		addedError = true;
		service.getSystemArchiveServicePrivate().addErrorMessage(archive.getArchiveId(), message);		
	}

	
}
