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

import java.util.Map;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;

/**
 *
 * @author dshurtleff
 */
public class UserToolAction
		extends BaseToolAction
{

	@DefaultHandler
	public Resolution loadPage()
	{
		return handleLoadPage(true);
	}

	@Override
	public Map<String, String> getPageMap()
	{
		pageMap.put("Dashboard", "/WEB-INF/securepages/shared/dashboard.jsp");
		pageMap.put("User-Profile", "/WEB-INF/securepages/user/userProfile.jsp");
		pageMap.put("Watches", "/WEB-INF/securepages/user/watches.jsp");
		pageMap.put("Reviews", "/WEB-INF/securepages/user/reviews.jsp");
		pageMap.put("Questions", "/WEB-INF/securepages/user/questions.jsp");
		pageMap.put("Submissions", "/WEB-INF/securepages/user/submissionManagement.jsp");
		pageMap.put("Reports", "/WEB-INF/securepages/shared/reports.jsp");
		pageMap.put("Searches", "/WEB-INF/securepages/user/searches.jsp");
		pageMap.put("Relationships", "/WEB-INF/securepages/user/visualSearch.jsp");

		return pageMap;
	}

}
