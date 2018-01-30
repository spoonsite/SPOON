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
public class AdminToolAction
		extends BaseToolAction
{

	@DefaultHandler
	public Resolution loadPage()
	{
		return handleLoadPage("adminheader.jsp");
	}

	@Override
	public Map<String, PageModel> getPageMap()
	{
		pageMap.put("Attributes", new PageModel("/WEB-INF/securepages/admin/data/attributes.jsp", new String[]{SecurityPermission.ADMIN_ATTRIBUTE_MANAGEMENT}));
		pageMap.put("Dashboard", new PageModel("/WEB-INF/securepages/shared/dashboard.jsp"));
		pageMap.put("Contacts", new PageModel("/WEB-INF/securepages/admin/data/contacts.jsp", new String[]{SecurityPermission.ADMIN_CONTACT_MANAGEMENT}));
		pageMap.put("Entries", new PageModel("/WEB-INF/securepages/admin/data/components.jsp", new String[]{SecurityPermission.ADMIN_ENTRY_MANAGEMENT}));
		pageMap.put("Entry-Types", new PageModel("/WEB-INF/securepages/admin/data/entryType.jsp", new String[]{SecurityPermission.ADMIN_ENTRY_MANAGEMENT}));
		pageMap.put("Highlights", new PageModel("/WEB-INF/securepages/admin/data/highlights.jsp", new String[]{SecurityPermission.ADMIN_HIGHLIGHTS}));
		pageMap.put("Integrations", new PageModel("/WEB-INF/securepages/admin/data/integrations.jsp", new String[]{SecurityPermission.ADMIN_INTEGRATION}));
		pageMap.put("Imports", new PageModel("/WEB-INF/securepages/admin/data/imports.jsp", new String[]{SecurityPermission.ADMIN_DATA_IMPORT_EXPORT}));
		pageMap.put("Lookups", new PageModel("/WEB-INF/securepages/admin/data/lookup.jsp", new String[]{SecurityPermission.ADMIN_LOOKUPS}));
		pageMap.put("Media", new PageModel("/WEB-INF/securepages/admin/data/media.jsp", new String[]{SecurityPermission.ADMIN_MEDIA}));
		pageMap.put("Metadata", new PageModel("/WEB-INF/securepages/admin/data/metadata.jsp", new String[]{SecurityPermission.ADMIN_ENTRY_MANAGEMENT}));
		pageMap.put("FAQ", new PageModel("/WEB-INF/securepages/admin/data/faq.jsp", new String[]{SecurityPermission.ADMIN_FAQ}));
		pageMap.put("Organizations", new PageModel("/WEB-INF/securepages/admin/data/organizations.jsp", new String[]{SecurityPermission.ADMIN_ORGANIZATION}));
		pageMap.put("Questions", new PageModel("/WEB-INF/securepages/admin/data/user-data/questions.jsp", new String[]{SecurityPermission.ADMIN_QUESTIONS}));
		pageMap.put("Reviews", new PageModel("/WEB-INF/securepages/admin/data/user-data/reviews.jsp", new String[]{SecurityPermission.ADMIN_REVIEW}));
		pageMap.put("Watches", new PageModel("/WEB-INF/securepages/admin/data/user-data/watches.jsp", new String[]{SecurityPermission.ADMIN_WATCHES}));
		pageMap.put("Tags", new PageModel("/WEB-INF/securepages/admin/data/user-data/tags.jsp", new String[]{SecurityPermission.ADMIN_ENTRY_MANAGEMENT}));
		pageMap.put("User-Profiles", new PageModel("/WEB-INF/securepages/admin/data/user-data/userProfiles.jsp", new String[]{SecurityPermission.ADMIN_USER_MANAGEMENT_PROFILES}));
		pageMap.put("Alerts", new PageModel("/WEB-INF/securepages/admin/application/alerts.jsp", new String[]{SecurityPermission.ADMIN_ALERT_MANAGEMENT}));
		pageMap.put("Branding", new PageModel("/WEB-INF/securepages/admin/application/branding.jsp", new String[]{SecurityPermission.ADMIN_BRANDING}));
		pageMap.put("Jobs", new PageModel("/WEB-INF/securepages/admin/application/jobs.jsp", new String[]{SecurityPermission.ADMIN_JOB_MANAGEMENT}));
		pageMap.put("Reports", new PageModel("/WEB-INF/securepages/shared/reports.jsp", new String[]{SecurityPermission.REPORTS}));
		pageMap.put("System", new PageModel("/WEB-INF/securepages/admin/application/system.jsp", new String[]{SecurityPermission.ADMIN_SYSTEM_MANAGEMENT}));
		pageMap.put("Tracking", new PageModel("/WEB-INF/securepages/admin/application/tracking.jsp", new String[]{SecurityPermission.ADMIN_TRACKING}));
		pageMap.put("Messages", new PageModel("/WEB-INF/securepages/admin/application/messages.jsp", new String[]{SecurityPermission.ADMIN_MESSAGE_MANAGEMENT}));
		pageMap.put("Entry-Template", new PageModel("/WEB-INF/securepages/admin/data/entryTemplate.jsp", new String[]{SecurityPermission.ADMIN_ENTRY_TEMPLATES}));
		pageMap.put("Searches", new PageModel("/WEB-INF/securepages/admin/data/searches.jsp", new String[]{SecurityPermission.ADMIN_SEARCH}));
		pageMap.put("Feedback", new PageModel("/WEB-INF/securepages/admin/application/feedback.jsp", new String[]{SecurityPermission.ADMIN_FEEDBACK}));
		pageMap.put("Relationships", new PageModel("/WEB-INF/securepages/admin/data/relationships.jsp", new String[]{SecurityPermission.ADMIN_ENTRY_MANAGEMENT}));
		pageMap.put("System-Archives", new PageModel("/WEB-INF/securepages/admin/application/systemArchive.jsp", new String[]{SecurityPermission.ADMIN_SYSTEM_MANAGEMENT}));
		pageMap.put("Support-Media", new PageModel("/WEB-INF/securepages/admin/data/help/supportMedia.jsp", new String[]{SecurityPermission.ADMIN_SUPPORT_MEDIA}));

		//security
		pageMap.put("Security", new PageModel("/WEB-INF/securepages/admin/application/security.jsp", new String[]{SecurityPermission.ADMIN_SECURITY}));
		pageMap.put("Security-Roles", new PageModel("/WEB-INF/securepages/admin/application/roleManagement.jsp", new String[]{SecurityPermission.ADMIN_ROLE_MANAGEMENT}));
		pageMap.put("User-Management", new PageModel("/WEB-INF/securepages/admin/application/userManagement.jsp", new String[]{SecurityPermission.ADMIN_USER_MANAGEMENT}));

		//Evaluation
		pageMap.put("Evaluations", new PageModel("/WEB-INF/securepages/admin/evaluation/evaluations.jsp", new String[]{SecurityPermission.ADMIN_EVALUATION_MANAGEMENT}));
		pageMap.put("Evaluation-Templates", new PageModel("/WEB-INF/securepages/admin/evaluation/evaluationsTemplates.jsp", new String[]{SecurityPermission.ADMIN_EVALUATION_TEMPLATE}));
		pageMap.put("Checklist-Templates", new PageModel("/WEB-INF/securepages/admin/evaluation/checklistTemplates.jsp", new String[]{SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST}));
		pageMap.put("Checklist-Questions", new PageModel("/WEB-INF/securepages/admin/evaluation/checklistQuestions.jsp", new String[]{SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION}));
		pageMap.put("Section-Templates", new PageModel("/WEB-INF/securepages/admin/evaluation/sectionTemplates.jsp", new String[]{SecurityPermission.ADMIN_EVALUATION_TEMPLATE_SECTION}));

		//Custom Submission Form Sandbox
		pageMap.put("Custom-Submission-Form-Sandbox", new PageModel("/WEB-INF/securepages/admin/application/customSubmissionSandbox.jsp", new String[]{SecurityPermission.ADMIN_SEARCH}));

		return pageMap;
	}

}
