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
package edu.usu.sdl.openstorefront.web.action.client;

import edu.usu.sdl.openstorefront.web.action.BaseAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.apache.commons.lang.StringUtils;

/**
 * Simple server router
 *
 * @author dshurtleff
 */
public class RouterAction
		extends BaseAction
{

	private String page;
	private String content;
	private boolean print;

	@DefaultHandler
	public Resolution loadPage()
	{
		return new ForwardResolution("/WEB-INF/securepages/" + page);
	}

	@HandlesEvent("Echo")
	public Resolution echo() {
		if (StringUtils.isBlank(content)) {
			content = "No Data";		
		}
		if (print) {
			content += " <script type='text/javascript'> window.print(); </script>";
		}		
		return new StreamingResolution("text/html", content);
	}	
	
	public String getPage()
	{
		return page;
	}

	public void setPage(String page)
	{
		this.page = page;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public boolean getPrint()
	{
		return print;
	}

	public void setPrint(boolean print)
	{
		this.print = print;
	}

}
