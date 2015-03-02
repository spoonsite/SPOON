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

package edu.usu.sdl.openstorefront.web.rest.model;

import au.com.bytecode.opencsv.CSVWriter;
import edu.usu.sdl.openstorefront.service.io.ExportImport;
import edu.usu.sdl.openstorefront.storage.model.Article;
import edu.usu.sdl.openstorefront.storage.model.ArticleTracking;
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
	private Article article;
	private ArticleTracking data;

	public ArticleTrackingCompleteWrapper(){
	}

	/**
	 * @return the data
	 */
	public ArticleTracking getData()
	{
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(ArticleTracking data)
	{
		this.data = data;
	}

	/**
	 * @return the article
	 */
	public Article getArticle()
	{
		return article;
	}

	/**
	 * @param article the article to set
	 */
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
		
		writer.writeNext(new String[]{getArticle().getTitle(),
			getData().getAttributeType(),
			getData().getAttributeCode(),
			df.format(getData().getCreateDts()),
			getData().getTrackEventTypeCode(),
			getData().getArticleTrackingId(),
			getData().getCreateUser(),
			getData().getClientIp()
		});
		return stringWriter.toString();
	}

	@Override
	public void importData(String[] data)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
