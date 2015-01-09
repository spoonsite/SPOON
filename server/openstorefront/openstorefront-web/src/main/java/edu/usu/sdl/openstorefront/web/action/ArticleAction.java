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
package edu.usu.sdl.openstorefront.web.action;

import edu.usu.sdl.openstorefront.service.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;

/**
 * Used for special article handling
 *
 * @author dshurtleff
 */
public class ArticleAction
		extends BaseAction
{

	@Validate(required = true, on = "Preview")
	private String html;

	@Validate(required = true, on = {"Preview", "View"})
	private String attributeType;

	@Validate(required = true, on = {"Preview", "View"})
	private String attributeCode;

	@HandlesEvent("Preview")
	public Resolution previewArticle() throws IOException
	{
		String filename = previewFileName();
		Files.write(Paths.get(FileSystemManager.SYSTEM_TEMP_DIR, filename), html.getBytes(), StandardOpenOption.CREATE);

		return new StreamingResolution(MediaType.TEXT_PLAIN, "Article.action?View&attributeType="
				+ URLEncoder.encode(attributeType, "UTF-8")
				+ "&attributeCode=" + URLEncoder.encode(attributeCode, "UTF-8"))
				{
				};
	}

	private String previewFileName()
	{
		return SecurityUtil.getCurrentUserName() + "_" + attributeType + "_" + attributeCode + ".html";
	}

	@HandlesEvent("View")
	public Resolution viewArticle()
	{
		return new StreamingResolution(MediaType.TEXT_HTML)
		{

			@Override
			protected void stream(HttpServletResponse response) throws Exception
			{
				if (Paths.get(FileSystemManager.SYSTEM_TEMP_DIR, previewFileName()).toFile().exists()) {
					Files.copy(Paths.get(FileSystemManager.SYSTEM_TEMP_DIR, previewFileName()), response.getOutputStream());
				} else {
					response.getOutputStream().println("Preview not available.");
				}
			}

		};
	}

	public String getHtml()
	{
		return html;
	}

	public void setHtml(String html)
	{
		this.html = html;
	}

	public String getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	public String getAttributeCode()
	{
		return attributeCode;
	}

	public void setAttributeCode(String attributeCode)
	{
		this.attributeCode = attributeCode;
	}

}
