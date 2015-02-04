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

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.storage.model.Article;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.web.rest.model.ArticleView;
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

		results.append("Save attribute code").append("<br>");
		AttributeCode attributeCode = ArticleTest.createTestAttributeCode();

		List<AttributeCode> attributeCodes = service.getAttributeService().findCodesForType(attributeCode.getAttributeCodePk().getAttributeType());
		results.append("<br>Found Codes (New Type)").append("<br>");
		attributeCodes.forEach(code -> {
			results.append(code.getAttributeCodePk().getAttributeType()).append(" - ").append(code.getAttributeCodePk().getAttributeCode());
		});

		results.append("Save Article").append("<br>");
		attributeCode.setArticle(new Article());
		attributeCode.getArticle().setTitle("Test Title");
		attributeCode.getArticle().setDescription("Test Description");
		service.getAttributeService().saveArticle(attributeCode, "This is an article.");

		results.append("Get Article").append("<br>");
		ArticleView articleView = service.getAttributeService().getArticle(attributeCode.getAttributeCodePk());
		if ("This is an article.".equals(articleView.getHtml()) == false) {
			failureReason.append("Article content doesn't match");
		}

		results.append("Remove Article").append("<br>");
		service.getAttributeService().deleteArticle(attributeCode.getAttributeCodePk());

		results.append("<br>Remove attribute").append("<br>");
		ArticleTest.deleteTestAttributeCode();
	}

	public static AttributeCode createTestAttributeCode()
	{
		ServiceProxy serviceProxy = new ServiceProxy();
		AttributeType attributeType = new AttributeType();
		attributeType.setAttributeType("TEST-CASE-TMP");
		attributeType.setDescription("This is a temp test attribute");
		attributeType.setAllowMultipleFlg(true);
		attributeType.setArchitectureFlg(false);
		attributeType.setImportantFlg(true);
		attributeType.setRequiredFlg(false);
		attributeType.setVisibleFlg(true);
		attributeType.setCreateUser(SecurityUtil.getCurrentUserName());
		attributeType.setUpdateUser(SecurityUtil.getCurrentUserName());
		serviceProxy.getAttributeService().saveAttributeType(attributeType, false);

		AttributeCode attributeCode = new AttributeCode();
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeCode("A");
		attributeCodePk.setAttributeType(attributeType.getAttributeType());
		attributeCode.setAttributeCodePk(attributeCodePk);
		attributeCode.setDescription("Test");
		attributeCode.setLabel("A");
		attributeCode.setCreateUser(SecurityUtil.getCurrentUserName());
		attributeCode.setUpdateUser(SecurityUtil.getCurrentUserName());
		serviceProxy.getAttributeService().saveAttributeCode(attributeCode, false);
		return attributeCode;
	}

	public static void deleteTestAttributeCode()
	{
		ServiceProxy serviceProxy = new ServiceProxy();

		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeCode("A");
		attributeCodePk.setAttributeType("TEST-CASE-TMP");
		serviceProxy.getAttributeService().removeAttributeCode(attributeCodePk);
		serviceProxy.getAttributeService().removeAttributeType(attributeCodePk.getAttributeType());

	}
}
