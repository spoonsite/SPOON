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

import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ArticleTest
		extends BaseTestCase
{

	public ArticleTest()
	{
		this.description = "Article Test";
	}

	@Override
	protected void runInternalTest()
	{

		results.append("<br>Save attribute type").append("<br>");
		AttributeType attributeType = new AttributeType();
		attributeType.setAttributeType("TEST-CASE-TMP");
		attributeType.setDescription("This is a temp test attribute");
		attributeType.setAllowMutlipleFlg(true);
		attributeType.setArchitectureFlg(false);
		attributeType.setImportantFlg(true);
		attributeType.setRequiredFlg(false);
		attributeType.setVisibleFlg(true);
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
		service.getAttributeService().saveAttributeCode(attributeCode, false);

		List<AttributeCode> attributeCodes = service.getAttributeService().findCodesForType(attributeType.getAttributeType());
		results.append("<br>Found Codes (New Type)").append("<br>");
		attributeCodes.forEach(code -> {
			results.append(code.getAttributeCodePk().getAttributeType()).append(" - ").append(code.getAttributeCodePk().getAttributeCode());
		});

		results.append("Save Article").append("<br>");
		service.getAttributeService().saveArticle(attributeCodePk, "This is an article.");

		results.append("Get Article").append("<br>");
		String content = service.getAttributeService().getArticle(attributeCodePk);
		if ("This is an article.".equals(content) == false) {
			failureReason.append("Article content doesn't match");
		}

		results.append("Remove Article").append("<br>");
		service.getAttributeService().deleteArticle(attributeCodePk);

		results.append("<br>Remove attribute").append("<br>");
		service.getAttributeService().removeAttributeCode(attributeCodePk);
		service.getAttributeService().removeAttributeType(attributeType.getAttributeType());
	}

}
