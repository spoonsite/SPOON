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

import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.web.model.PageModel;
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
		return handleLoadPage("userheader.jsp");
	}

	@Override
	public Map<String, PageModel> getPageMap()
	{
		pageMap.put("Dashboard", new PageModel("/WEB-INF/securepages/shared/dashboard.jsp", new String[]{SecurityPermission.DASHBOARD_PAGE}));
		pageMap.put("User-Profile", new PageModel("/WEB-INF/securepages/user/userProfile.jsp", new String[]{SecurityPermission.USER_PROFILE_PAGE}));
		pageMap.put("Watches", new PageModel("/WEB-INF/securepages/user/watches.jsp", new String[]{SecurityPermission.USER_WATCHES_PAGE}));
		pageMap.put("Reviews", new PageModel("/WEB-INF/securepages/user/reviews.jsp", new String[]{SecurityPermission.USER_REVIEW_PAGE}));
		pageMap.put("Questions", new PageModel("/WEB-INF/securepages/user/questions.jsp", new String[]{SecurityPermission.USER_QUESTIONS_PAGE}));
		pageMap.put("Submissions", new PageModel("/WEB-INF/securepages/user/submissionManagement.jsp", new String[]{SecurityPermission.USER_SUBMISSIONS_PAGE}));
		pageMap.put("Reports", new PageModel("/WEB-INF/securepages/shared/reports.jsp", new String[]{SecurityPermission.REPORTS_PAGE}));
		pageMap.put("Searches", new PageModel("/WEB-INF/securepages/user/searches.jsp", new String[]{SecurityPermission.USER_SEARCHES_PAGE}));
		pageMap.put("Relationships", new PageModel("/WEB-INF/securepages/user/visualSearch.jsp", new String[]{SecurityPermission.USER_RELATIONSHIPS_PAGE}));
		pageMap.put("Change-Password", new PageModel("/WEB-INF/securepages/user/changePassword.jsp"));		

		return pageMap;
	}

}
