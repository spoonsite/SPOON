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
package edu.usu.sdl.openstorefront.web.test.attribute;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class FindAttributeTest
		extends BaseTestCase
{

	AttributeType attributeType = null;

	@Override
	protected void runInternalTest()
	{

		results.append("<br>Save attribute type").append("<br>");
		attributeType = new AttributeType();
		attributeType.setAttributeType("TEST-CASE-TMP");
		attributeType.setDescription("This is a temp test attribute");
		attributeType.setAllowMultipleFlg(true);
		attributeType.setArchitectureFlg(false);
		attributeType.setImportantFlg(true);
		attributeType.setRequiredFlg(false);
		attributeType.setAllowUserGeneratedCodes(false);
		attributeType.setVisibleFlg(true);
		attributeType.setHideOnSubmission(false);
		attributeType.setCreateUser(SecurityUtil.getCurrentUserName());
		attributeType.setUpdateUser(SecurityUtil.getCurrentUserName());
		service.getAttributeService().saveAttributeType(attributeType, false);

		results.append("Save attribute code").append("<br>");
		AttributeCode attributeCode = new AttributeCode();
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeCode("A");
		attributeCodePk.setAttributeType(attributeType.getAttributeType());
		attributeCode.setAttributeCodePk(attributeCodePk);
		attributeCode.setDescription("Test");
		attributeCode.setLabel("A");
		attributeCode.setCreateUser(SecurityUtil.getCurrentUserName());
		attributeCode.setUpdateUser(SecurityUtil.getCurrentUserName());
		ValidationResult validationResult = service.getAttributeService().saveAttributeCode(attributeCode, false);
		if (validationResult.valid() == false) {
			throw new OpenStorefrontRuntimeException(validationResult.toString());
		}
		List<AttributeCode> attributeCodes = service.getAttributeService().findCodesForType(attributeType.getAttributeType());
		results.append("<br>Found Codes (New Type)").append("<br>");
		attributeCodes.forEach(code -> {
			results.append(code.getAttributeCodePk().getAttributeType()).append(" - ").append(code.getAttributeCodePk().getAttributeCode());
		});

		results.append("<br>Delete attribute").append("<br>");
	}

	@Override
	public String getDescription()
	{
		return "Find Attributes";
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
		if (attributeType != null) {
			service.getAttributeService().cascadeDeleteAttributeType(attributeType.getAttributeType());
		}
	}
}
