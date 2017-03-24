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
package edu.usu.sdl.openstorefront.web.action;

import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.web.model.PageModel;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseToolAction
		extends BaseAction
{

	protected String load;
	protected Map<String, PageModel> pageMap = new HashMap<>();
	protected String headerPage;

	protected Resolution handleLoadPage(String headerPage)
	{
		String newPage = "/WEB-INF/securepages/shared/dashboard.jsp";
		
		this.setHeaderPage(headerPage);

		PageModel page = getPageMap().get(load);
		if (page != null && page.getRoles() != null) 
		{
			if (SecurityUtil.hasPermission(page.getRoles()) == false) {
				return new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, "Missing permisson to access page.");
			}
		}
		
		if (page != null && StringUtils.isNotBlank(page.getPage())) {
			newPage = page.getPage();
		}

		return new ForwardResolution(newPage);
	}

	public abstract Map<String, PageModel> getPageMap();

	public void setPageMap(Map<String, PageModel> pageMap)
	{
		this.pageMap = pageMap;
	}

	public String getLoad()
	{
		return load;
	}

	public void setLoad(String load)
	{
		this.load = load;
	}

	public String getHeaderPage()
	{
		return headerPage;
	}

	public void setHeaderPage(String headerPage)
	{
		this.headerPage = headerPage;
	}

}
