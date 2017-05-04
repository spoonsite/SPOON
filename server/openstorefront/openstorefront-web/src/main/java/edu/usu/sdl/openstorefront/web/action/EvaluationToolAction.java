/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
 *
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
public class EvaluationToolAction
		extends BaseToolAction
{

	@DefaultHandler
	public Resolution loadPage()
	{
		return handleLoadPage("evalheader.jsp");
	}

	@Override
	public Map<String, PageModel> getPageMap()
	{
		pageMap.put("Dashboard", new PageModel("/WEB-INF/securepages/shared/dashboard.jsp"));
		pageMap.put("Evaluations", new PageModel("/WEB-INF/securepages/eval/evaluations.jsp", new String[]{SecurityPermission.EVALUATIONS}));
		pageMap.put("Reports", new PageModel("/WEB-INF/securepages/shared/reports.jsp", new String[]{SecurityPermission.REPORTS}));
		

		return pageMap;
	}

}
