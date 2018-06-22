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
package edu.usu.sdl.openstorefront.web.test;

import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryError;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ccummings
 */
public abstract class BaseDataImportTest extends BaseTestCase
{

	protected static final String TEST_COMPONENT_TYPE = "AUTO_TEST_TYPE";

	private static final Logger LOG = Logger.getLogger(BaseDataImportTest.class.getName());

	protected void checkComponentType()
	{
		//need to make sure componentType exists
		ComponentType componentType = new ComponentType();
		componentType.setComponentType(TEST_COMPONENT_TYPE);
		componentType = componentType.find();
		if (componentType == null) {
			componentType = new ComponentType();
			componentType.setComponentType(TEST_COMPONENT_TYPE);
			componentType.setLabel("AUTO-TEST");
			componentType.setDescription("This was created for auto tests");
			componentType.save();

			CleanupTestData cleanupTestData = () -> {
				ComponentType componentTypeLocal = new ComponentType();
				componentTypeLocal.setComponentType(TEST_COMPONENT_TYPE);
				componentTypeLocal.delete();
			};
			cleanTestDataList.add(cleanupTestData);

		}
	}

	protected void waitForImport(String fileHistoryId)
	{
		boolean isComplete = false;
		int maxWaitTime = 25;
		int count = 0;

		while (!isComplete || count < maxWaitTime) {

			FileHistory fileHistory = new FileHistory();
			fileHistory.setFileHistoryId(fileHistoryId);
			fileHistory = fileHistory.find();

			if (fileHistory.getCompleteDts() != null) {
				isComplete = true;
			} else {
				waitDelay();
			}
			count++;

		}
	}

	public void waitDelay()
	{
		try {
			Thread.sleep(200);
		} catch (InterruptedException ex) {
			if (LOG.isLoggable(Level.FINEST)) {
				LOG.log(Level.FINEST, null, ex);
			}
			Thread.currentThread().interrupt();
		}
	}

	protected void handleResults(String fileHistoryId)
	{
		FileHistoryError fileHistoryError = new FileHistoryError();
		fileHistoryError.setFileHistoryId(fileHistoryId);
		List<FileHistoryError> errors = fileHistoryError.findByExample();

		long warningCount = errors.stream()
				.filter(error -> error.getFileHistoryErrorType().equals(FileHistoryErrorType.WARNING))
				.count();
		addResultsLines("Warnings: " + warningCount);
		errors.stream()
				.filter(error -> error.getFileHistoryErrorType().equals(FileHistoryErrorType.WARNING))
				.forEach(error -> {
					addResultsLines(error.getErrorMessage());
				});

		long errorCount = errors.size() - warningCount;
		addResultsLines("Errors: " + errorCount);
		if (errorCount != 0) {
			for (FileHistoryError error : errors) {
				addFailLines("Type: " + error.getFileHistoryErrorType() + " - " + error.getErrorMessage());
			}
		}
	}
}
