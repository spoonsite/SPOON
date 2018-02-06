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
import static edu.usu.sdl.openstorefront.core.entity.FileFormat.ATTRIBUTE_STANDARD;
import edu.usu.sdl.openstorefront.core.entity.FileHistory;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.model.ImportContext;
import edu.usu.sdl.openstorefront.web.test.BaseDataImportTest;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author ccummings
 */
public class AttributeStandardTest extends BaseDataImportTest
{

	private String fileHistoryId = null;
	private String attrTypeId01 = null;
	private String attrTypeId02 = null;

	@Override
	public String getDescription()
	{
		return "Attribute Standard Test";
	}

	@Override
	protected void runInternalTest()
	{
		ImportContext importContext = new ImportContext();
		importContext.setInput(FileSystemManager.getInstance().getApplicationResourceFile("/data/test/attributes.json"));

		FileHistory fileHistory = new FileHistory();
		fileHistory.setFileFormat(ATTRIBUTE_STANDARD);
		fileHistory.setFilename("attributes");
		fileHistory.setOriginalFilename("attributes.json");
		fileHistory.setMimeType("application/json");
		FileHistoryOption options = new FileHistoryOption();
		options.setSkipRequiredAttributes(true);
		fileHistory.setFileHistoryOption(options);
		importContext.getFileHistoryAll().setFileHistory(fileHistory);

		fileHistoryId = service.getImportService().importData(importContext);

		waitForImport(fileHistoryId);
		handleResults(fileHistoryId);

		AttributeType attributeType = new AttributeType();
		attributeType.setDescription("AttributeStandard01");
		attributeType = attributeType.find();

		attrTypeId01 = attributeType.getAttributeType();

		if (attributeType.getAttributeType().equals("ATTRSTD")) {
			results.append("Attribute01 found<br>");
		} else {
			failureReason.append("Attribute not found<br>");
		}

		attributeType = new AttributeType();
		attributeType.setDescription("AttributeStandard02");
		attributeType = attributeType.find();

		if (attributeType.getAttributeType().equals("ATTRSTD2")) {
			results.append("Attribute02 found<br>");
		} else {
			failureReason.append("Attribute not found<br>");
		}
		attrTypeId02 = attributeType.getAttributeType();

	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();

		if (StringUtils.isNotBlank(fileHistoryId)) {
			service.getImportService().rollback(fileHistoryId);
		}

		if (StringUtils.isNotBlank(attrTypeId01)) {
			service.getAttributeService().cascadeDeleteAttributeType(attrTypeId01);
		}

		if (StringUtils.isNotBlank(attrTypeId02)) {
			service.getAttributeService().cascadeDeleteAttributeType(attrTypeId02);
		}
	}

}
