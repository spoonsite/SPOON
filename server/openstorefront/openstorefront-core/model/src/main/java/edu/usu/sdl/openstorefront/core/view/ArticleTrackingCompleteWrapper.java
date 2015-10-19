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
package edu.usu.sdl.openstorefront.core.view;

import au.com.bytecode.opencsv.CSVWriter;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.Article;
import edu.usu.sdl.openstorefront.core.entity.ArticleTracking;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.core.util.ExportImport;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author jlaw
 */
public class ArticleTrackingCompleteWrapper
		implements ExportImport
{

	public static final String FIELD_TITLE = "title";

	private Article article;
	private ArticleTracking data;

	public ArticleTrackingCompleteWrapper()
	{
	}

	public ArticleTracking getData()
	{
		return data;
	}

	public void setData(ArticleTracking data)
	{
		this.data = data;
	}

	public Article getArticle()
	{
		return article;
	}

	public void setArticle(Article article)
	{
		this.article = article;
	}

	@Override
	public String export()
	{
		StringWriter stringWriter = new StringWriter();
		CSVWriter writer = new CSVWriter(stringWriter);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		if (getData() != null) {
			String title;
			if (getArticle() != null) {
				title = getArticle().getTitle();
			} else {
				AttributeCodePk attributeCodePk = new AttributeCodePk();
				attributeCodePk.setAttributeCode(getData().getAttributeCode());
				attributeCodePk.setAttributeType(getData().getAttributeType());

				AttributeCode attributeCode = ServiceProxyFactory.getServiceProxy().getAttributeService().findCodeForType(attributeCodePk);
				title = attributeCode.getLabel();
			}

			writer.writeNext(new String[]{title,
										  getData().getAttributeType(),
										  getData().getAttributeCode(),
										  df.format(getData().getCreateDts()),
										  TranslateUtil.translate(TrackEventCode.class, getData().getTrackEventTypeCode()),
										  getData().getArticleTrackingId(),
										  getData().getCreateUser(),
										  getData().getClientIp()
			});
		}
		return stringWriter.toString();
	}

	@Override
	public void importData(String[] data)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
