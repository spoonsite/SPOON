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
package edu.usu.sdl.openstorefront.web.test.dataimport;

import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import static edu.usu.sdl.openstorefront.core.entity.FileFormat.ATTRIBUTE_SVCV4;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.model.ImportContext;
import edu.usu.sdl.openstorefront.web.test.BaseDataImportTest;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author ccummings
 */
public class SVCV4Test extends BaseDataImportTest
{
	private String fileHistoryId = null;
	private String attrId = null;

	@Override
	public String getDescription()
	{
		return "SVCV4 Test";
	}

	@Override
	protected void runInternalTest()
	{
		ImportContext importContext = new ImportContext();
		importContext.setInput(FileSystemManager.getApplicationResourceFile("/data/test/svcv-4_exportsample.csv"));

		FileHistory fileHistory = new FileHistory();
		fileHistory.setFileFormat(ATTRIBUTE_SVCV4);
		fileHistory.setFilename("svcv-4_exportsample");
		fileHistory.setOriginalFilename("svcv-4_exportsample.csv");
		fileHistory.setMimeType("application/csv");
		FileHistoryOption options = new FileHistoryOption();
		options.setSkipRequiredAttributes(true);
		fileHistory.setFileHistoryOption(options);
		importContext.getFileHistoryAll().setFileHistory(fileHistory);

		fileHistoryId = service.getImportService().importData(importContext);

		waitForImport(fileHistoryId);
		handleResults(fileHistoryId);

		AttributeType type = new AttributeType();
		type.setDescription("DI2E SvcV-4 Alignment");
		type = type.find();

		attrId = type.getAttributeType();

		if (type.getAttributeType().equals("DI2E-SVCV4-A")) {
			addResultsLines("Found SVCV4 Attribute");
		} else {
			addFailLines("Unable to find SVCV4 Attribute");
		}
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();

		if (StringUtils.isNotBlank(fileHistoryId)) {
			service.getImportService().rollback(fileHistoryId);
		}
		if (StringUtils.isNotBlank(attrId)) {
			service.getAttributeService().cascadeDeleteAttributeType(attrId);
		}
	}

}
