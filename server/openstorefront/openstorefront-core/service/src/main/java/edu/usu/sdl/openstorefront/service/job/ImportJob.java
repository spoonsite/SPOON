/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.entity.ModificationType;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 *
 * @author dshurtleff
 */
@DisallowConcurrentExecution
public class ImportJob
		extends BaseJob
{

	private static final Logger log = Logger.getLogger(ImportJob.class.getName());

	@Override
	protected void executeInternaljob(JobExecutionContext context)
	{
		//batch file uploads to avoid conflicts
		FileHistory fileHistoryExample = new FileHistory();
		fileHistoryExample.setActiveStatus(FileHistory.ACTIVE_STATUS);
		QueryByExample queryByExample = new QueryByExample(fileHistoryExample);

		FileHistory fileHistoryNotExample = new FileHistory();
		fileHistoryNotExample.setStartDts(QueryByExample.DATE_FLAG);
		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel(fileHistoryNotExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_NULL);

		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		List<FileHistory> fileHistories = service.getPersistenceService().queryByExample(queryByExample);

		if (fileHistories.isEmpty() == false) {
			log.log(Level.FINE, MessageFormat.format("Found {0} pending uploads", fileHistories.size()));
		}
		for (FileHistory fileHistory : fileHistories) {
			log.log(Level.FINE, MessageFormat.format("Found upload to process: {0}", fileHistory.getOriginalFilename()));
			try {
				ServiceProxy.getProxy(ModificationType.IMPORT).getImportServicePrivate().processImport(fileHistory.getFileHistoryId());
			} catch (Exception e) {
				log.log(Level.SEVERE, "Failed to process file.", e);
			}
		}
	}

}
