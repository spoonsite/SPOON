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
import static edu.usu.sdl.openstorefront.core.entity.FileFormat.COMPONENT_STANDARD;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.model.ImportContext;
import edu.usu.sdl.openstorefront.web.test.BaseDataImportTest;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author ccummings
 */
public class ComponentStandardTest extends BaseDataImportTest
{
	private String fileHistoryId01 = null;
//	private String fileHistoryId02 = null;

	@Override
	public String getDescription()
	{
		return "Component Standard Tests";
	}

	@Override
	protected void runInternalTest()
	{
		results.append("Component Standard Test With No Media<br>");
		ImportContext importContextNoMedia = new ImportContext();
		importContextNoMedia.setInput(FileSystemManager.getInstance().getApplicationResourceFile("/data/test/standardtest.json"));

		FileHistory fileHistoryNoMedia = new FileHistory();
		fileHistoryNoMedia.setFileFormat(COMPONENT_STANDARD);
		fileHistoryNoMedia.setFilename("standardtest.json");
		fileHistoryNoMedia.setOriginalFilename("standardtest.json");
		fileHistoryNoMedia.setMimeType("application/json");
		FileHistoryOption optionsNoMedia = new FileHistoryOption();
		optionsNoMedia.setSkipRequiredAttributes(true);
		fileHistoryNoMedia.setFileHistoryOption(optionsNoMedia);
		importContextNoMedia.getFileHistoryAll().setFileHistory(fileHistoryNoMedia);

		fileHistoryId01 = service.getImportService().importData(importContextNoMedia);

		waitForImport(fileHistoryId01);
		handleResults(fileHistoryId01);

		/*
		Zip file continuously corrupted.  Cause may be related to Maven
		 */
//		results.append("<br>Component Standard Test With Media<br>");
//
//		ImportContext importContextMedia = new ImportContext();
//		importContextMedia.setInput(FileSystemManager.getInstance().getApplicationResourceFile("/data/test/standardwithmedia.zip"));
//
//		FileHistory fileHistoryMedia = new FileHistory();
//		fileHistoryMedia.setFileFormat(COMPONENT_STANDARD);
//		fileHistoryMedia.setFilename("standardwithmedia.zip");
//		fileHistoryMedia.setOriginalFilename("standardwithmedia.zip");
//		fileHistoryMedia.setMimeType("application/zip");
//		FileHistoryOption optionsMedia = new FileHistoryOption();
//		optionsMedia.setSkipRequiredAttributes(true);
//		fileHistoryMedia.setFileHistoryOption(optionsMedia);
//		importContextMedia.getFileHistoryAll().setFileHistory(fileHistoryMedia);
//
//		fileHistoryId02 = service.getImportService().importData(importContextMedia);
//
//		waitForImport(fileHistoryId02);
//		handleResults(fileHistoryId02);


	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();

		if (StringUtils.isNotBlank(fileHistoryId01)) {
			service.getImportService().rollback(fileHistoryId01);
		}

//		if (StringUtils.isNotBlank(fileHistoryId02)) {
//			service.getImportService().rollback(fileHistoryId02);
//		}
	}

}
