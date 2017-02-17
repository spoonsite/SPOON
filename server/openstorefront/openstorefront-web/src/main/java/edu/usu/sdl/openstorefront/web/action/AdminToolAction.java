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
public class AdminToolAction
		extends BaseToolAction
{

	@DefaultHandler
	public Resolution loadPage()
	{
		return handleLoadPage(false);
	}

	@Override
	public Map<String, String> getPageMap()
	{
		pageMap.put("Attributes", "/WEB-INF/securepages/admin/data/attributes.jsp");
		pageMap.put("Dashboard", "/WEB-INF/securepages/shared/dashboard.jsp");
		pageMap.put("Contacts", "/WEB-INF/securepages/admin/data/contacts.jsp");
		pageMap.put("Entries", "/WEB-INF/securepages/admin/data/components.jsp");
		pageMap.put("Entry-Types", "/WEB-INF/securepages/admin/data/entryType.jsp");
		pageMap.put("Highlights", "/WEB-INF/securepages/admin/data/highlights.jsp");
		pageMap.put("Integrations", "/WEB-INF/securepages/admin/data/integrations.jsp");
		pageMap.put("Imports", "/WEB-INF/securepages/admin/data/imports.jsp");
		pageMap.put("Lookups", "/WEB-INF/securepages/admin/data/lookup.jsp");
		pageMap.put("Media", "/WEB-INF/securepages/admin/data/media.jsp");
		pageMap.put("Metadata", "/WEB-INF/securepages/admin/data/metadata.jsp");
		pageMap.put("Organizations", "/WEB-INF/securepages/admin/data/organizations.jsp");
		pageMap.put("Questions", "/WEB-INF/securepages/admin/data/user-data/questions.jsp");
		pageMap.put("Reviews", "/WEB-INF/securepages/admin/data/user-data/reviews.jsp");
		pageMap.put("Watches", "/WEB-INF/securepages/admin/data/user-data/watches.jsp");
		pageMap.put("Tags", "/WEB-INF/securepages/admin/data/user-data/tags.jsp");
		pageMap.put("User-Profiles", "/WEB-INF/securepages/admin/data/user-data/userProfiles.jsp");
		pageMap.put("Alerts", "/WEB-INF/securepages/admin/application/alerts.jsp");
		pageMap.put("Branding", "/WEB-INF/securepages/admin/application/branding.jsp");
		pageMap.put("Jobs", "/WEB-INF/securepages/admin/application/jobs.jsp");
		pageMap.put("Reports", "/WEB-INF/securepages/shared/reports.jsp");
		pageMap.put("System", "/WEB-INF/securepages/admin/application/system.jsp");
		pageMap.put("Tracking", "/WEB-INF/securepages/admin/application/tracking.jsp");
		pageMap.put("Messages", "/WEB-INF/securepages/admin/application/messages.jsp");
		pageMap.put("Entry-Template", "/WEB-INF/securepages/admin/data/entryTemplate.jsp");
		pageMap.put("Searches", "/WEB-INF/securepages/admin/data/searches.jsp");
		pageMap.put("Feedback", "/WEB-INF/securepages/admin/application/feedback.jsp");
		pageMap.put("Relationships", "/WEB-INF/securepages/admin/data/relationships.jsp");
		//security
		pageMap.put("Security", "/WEB-INF/securepages/admin/application/security.jsp");
		pageMap.put("Security-Roles", "/WEB-INF/securepages/admin/application/roleManagement.jsp");
		pageMap.put("User-Management", "/WEB-INF/securepages/admin/application/userManagement.jsp");
		

		//Evaluation
		pageMap.put("Evaluations", "/WEB-INF/securepages/admin/evaluation/evaluations.jsp");
		pageMap.put("Evaluation-Templates", "/WEB-INF/securepages/admin/evaluation/evaluationsTemplates.jsp");
		pageMap.put("Checklist-Templates", "/WEB-INF/securepages/admin/evaluation/checklistTemplates.jsp");
		pageMap.put("Checklist-Questions", "/WEB-INF/securepages/admin/evaluation/checklistQuestions.jsp");
		pageMap.put("Section-Templates", "/WEB-INF/securepages/admin/evaluation/sectionTemplates.jsp");

		return pageMap;
	}

}
