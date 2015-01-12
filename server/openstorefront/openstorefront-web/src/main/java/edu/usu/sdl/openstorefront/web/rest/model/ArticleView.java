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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.validation.BasicHTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

/**
 * Topic landing page/article
 *
 * @author dshurtleff
 */
public class ArticleView
{

	private String attributeCode;
	private String attributeType;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String title;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_DETAILED_DESCRIPTION)
	@Sanitize(BasicHTMLSanitizer.class)
	private String description;

	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_ARTICLE_SIZE)
	private String html;
	private Date updateDts;

	public ArticleView()
	{
	}

	public static ArticleView toView(AttributeCode attributeCode)
	{
		ArticleView articleView = new ArticleView();
		articleView.setAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode());
		articleView.setAttributeType(attributeCode.getAttributeCodePk().getAttributeType());
		if (attributeCode.getArticle() != null) {
			if (StringUtils.isNotBlank(attributeCode.getArticle().getTitle())) {
				articleView.setTitle(attributeCode.getArticle().getTitle());
			} else {
				articleView.setTitle(attributeCode.getLabel());
			}

			if (StringUtils.isNotBlank(attributeCode.getArticle().getDescription())) {
				articleView.setDescription(attributeCode.getArticle().getDescription());
			} else {
				articleView.setDescription(attributeCode.getDescription());
			}

			articleView.setUpdateDts(attributeCode.getArticle().getUpdateDts());
		}

		return articleView;
	}

	public static ArticleView toViewHtml(AttributeCode attributeCode, String content)
	{
		ArticleView articleView = toView(attributeCode);
		articleView.setHtml(content);
		return articleView;
	}

	public String getAttributeCode()
	{
		return attributeCode;
	}

	public void setAttributeCode(String attributeCode)
	{
		this.attributeCode = attributeCode;
	}

	public String getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	public String getHtml()
	{
		return html;
	}

	public void setHtml(String html)
	{
		this.html = html;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}
