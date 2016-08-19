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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.FileDataMap;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.model.ImportContext;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class EquipmentImportingTest
	extends BaseTestCase
{

	public EquipmentImportingTest()
	{
		this.description = "Equipment Import";
	}

	@Override
	protected void runInternalTest()
	{
		File dir = new File("T:\\DI2E\\spoon\\mapping");

		for (File originFile : dir.listFiles()) {
			if (originFile.isFile()) {
				try {
					String fileNameWithOutExt = originFile.getName().replace(".xml", "");
					
					FileDataMap fileDataMap = new FileDataMap();
					fileDataMap.setName(fileNameWithOutExt);
					fileDataMap = fileDataMap.find();
					
					if (fileDataMap != null) {
						ImportContext importContext = new ImportContext();
						importContext.setInput(new FileInputStream(originFile));

						FileHistoryOption componentUploadOptions = new FileHistoryOption();
						componentUploadOptions.setSkipRequiredAttributes(true);

						FileHistory fileHistory = new FileHistory();
						fileHistory.setMimeType(OpenStorefrontConstant.getMimeForFileExtension(".xml"));
						fileHistory.setOriginalFilename(originFile.getName());
						fileHistory.setFileFormat("SPOONCMP");
						fileHistory.setFileHistoryOption(componentUploadOptions);
						fileHistory.setFileDataMapId(fileDataMap.getFileDataMapId());
						importContext.getFileHistoryAll().setFileHistory(fileHistory);

						service.getImportService().importData(importContext);
						results.append("Queued Import: ")
								.append(originFile.getName())
								.append("<br>");					
					} else {
						results.append("Unable to find mapping for: ")
								.append(fileNameWithOutExt)
								.append("<br>");
					}
				} catch (FileNotFoundException ex) {
					Logger.getLogger(EquipmentImportingTest.class.getName()).log(Level.SEVERE, null, ex);
					failureReason.append(ex.getMessage()).append("<br>");
				}
				
			}	
		}

		
	}	
	
}
