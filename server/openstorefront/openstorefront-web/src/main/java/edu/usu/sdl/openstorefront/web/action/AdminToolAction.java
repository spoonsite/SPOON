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
		pageMap.put("Attributes", new PageModel("/WEB-INF/securepages/admin/data/attributes.jsp", new String[]{SecurityPermission.ADMIN_ATTRIBUTE_PAGE}));
		pageMap.put("Contacts", new PageModel("/WEB-INF/securepages/admin/data/contacts.jsp", new String[]{SecurityPermission.ADMIN_CONTACTS_PAGE}));
		pageMap.put("Dashboard", new PageModel("/WEB-INF/securepages/shared/dashboard.jsp", new String[]{SecurityPermission.DASHBOARD_PAGE}));
		pageMap.put("WorkPlan-Progress", new PageModel("/WEB-INF/securepages/admin/data/workplanProgressManagement.jsp", new String[]{SecurityPermission.WORKPLAN_PROGRESS_MANAGEMENT_PAGE}));
		pageMap.put("Entries", new PageModel("/WEB-INF/securepages/admin/data/components.jsp", new String[]{SecurityPermission.ADMIN_ENTRIES_PAGE}));
		pageMap.put("Entry-Types", new PageModel("/WEB-INF/securepages/admin/data/entryType.jsp", new String[]{SecurityPermission.ADMIN_ENTRYTYPE_PAGE}));
		pageMap.put("Highlights", new PageModel("/WEB-INF/securepages/admin/data/highlights.jsp", new String[]{SecurityPermission.ADMIN_HIGHLIGHTS_PAGE}));
		pageMap.put("Integrations", new PageModel("/WEB-INF/securepages/admin/data/integrations.jsp", new String[]{SecurityPermission.ADMIN_INTEGRATION_PAGE}));
		pageMap.put("Imports", new PageModel("/WEB-INF/securepages/admin/data/imports.jsp", new String[]{SecurityPermission.ADMIN_IMPORT_PAGE}));
		pageMap.put("Lookups", new PageModel("/WEB-INF/securepages/admin/data/lookup.jsp", new String[]{SecurityPermission.ADMIN_LOOKUPS_PAGE}));
		pageMap.put("Media", new PageModel("/WEB-INF/securepages/admin/data/media.jsp", new String[]{SecurityPermission.ADMIN_MEDIA_PAGE}));
		pageMap.put("Metadata", new PageModel("/WEB-INF/securepages/admin/data/metadata.jsp", new String[]{SecurityPermission.ADMIN_ENTRIES_PAGE}));
		pageMap.put("FAQ", new PageModel("/WEB-INF/securepages/admin/data/faq.jsp", new String[]{SecurityPermission.ADMIN_FAQ_PAGE}));
		pageMap.put("Organizations", new PageModel("/WEB-INF/securepages/admin/data/organizations.jsp", new String[]{SecurityPermission.ADMIN_ORGANIZATION_PAGE}));
		pageMap.put("Questions", new PageModel("/WEB-INF/securepages/admin/data/user-data/questions.jsp", new String[]{SecurityPermission.ADMIN_QUESTION_PAGE}));
		pageMap.put("Reviews", new PageModel("/WEB-INF/securepages/admin/data/user-data/reviews.jsp", new String[]{SecurityPermission.ADMIN_REVIEW_PAGE}));
		pageMap.put("Watches", new PageModel("/WEB-INF/securepages/admin/data/user-data/watches.jsp", new String[]{SecurityPermission.ADMIN_WATCHES_PAGE}));
		pageMap.put("Tags", new PageModel("/WEB-INF/securepages/admin/data/user-data/tags.jsp", new String[]{SecurityPermission.ADMIN_TAGS_PAGE}));
		pageMap.put("User-Profiles", new PageModel("/WEB-INF/securepages/admin/data/user-data/userProfiles.jsp", new String[]{SecurityPermission.ADMIN_USERPROFILES_PAGE}));
		pageMap.put("Alerts", new PageModel("/WEB-INF/securepages/admin/application/alerts.jsp", new String[]{SecurityPermission.ADMIN_ALERTS_PAGE}));
		pageMap.put("Branding", new PageModel("/WEB-INF/securepages/admin/application/branding.jsp", new String[]{SecurityPermission.ADMIN_BRANDING_PAGE}));
		pageMap.put("Jobs", new PageModel("/WEB-INF/securepages/admin/application/jobs.jsp", new String[]{SecurityPermission.ADMIN_JOBS_PAGE}));
		pageMap.put("Reports", new PageModel("/WEB-INF/securepages/shared/reports.jsp", new String[]{SecurityPermission.REPORTS_PAGE}));
		pageMap.put("System", new PageModel("/WEB-INF/securepages/admin/application/system.jsp", new String[]{SecurityPermission.ADMIN_SYSTEM_PAGE}));
		pageMap.put("Tracking", new PageModel("/WEB-INF/securepages/admin/application/tracking.jsp", new String[]{SecurityPermission.ADMIN_TRACKING_PAGE}));
		pageMap.put("Messages", new PageModel("/WEB-INF/securepages/admin/application/messages.jsp", new String[]{SecurityPermission.ADMIN_MESSAGES_PAGE}));
		pageMap.put("Entry-Template", new PageModel("/WEB-INF/securepages/admin/data/entryTemplate.jsp", new String[]{SecurityPermission.ADMIN_ENTRYTEMPLATES_PAGE}));
		pageMap.put("Partial-Submissions", new PageModel("/WEB-INF/securepages/admin/data/submission/partialSubmissions.jsp", new String[]{SecurityPermission.ADMIN_PARTIAL_SUBMISSIONS_PAGE}));
		pageMap.put("Searches", new PageModel("/WEB-INF/securepages/admin/data/searches.jsp", new String[]{SecurityPermission.ADMIN_SEARCHES_PAGE}));
		pageMap.put("Feedback", new PageModel("/WEB-INF/securepages/admin/application/feedback.jsp", new String[]{SecurityPermission.ADMIN_FEEDBACK_PAGE}));
		pageMap.put("Relationships", new PageModel("/WEB-INF/securepages/admin/data/relationships.jsp", new String[]{SecurityPermission.ADMIN_RELATIONSHIPS_PAGE}));
		pageMap.put("System-Archives", new PageModel("/WEB-INF/securepages/admin/application/systemArchive.jsp", new String[]{SecurityPermission.ADMIN_SYSTEM_ARCHIVES_PAGE}));
		pageMap.put("Support-Media", new PageModel("/WEB-INF/securepages/admin/data/help/supportMedia.jsp", new String[]{SecurityPermission.ADMIN_SUPPORTMEDIA_PAGE}));
		pageMap.put("Custom-Forms", new PageModel("/WEB-INF/securepages/admin/data/submission/customSubmissionForm.jsp", new String[]{SecurityPermission.ADMIN_SUBMISSION_FORM_TEMPLATE_PAGE}));
		pageMap.put("Workplans", new PageModel("/WEB-INF/securepages/admin/data/workplan/workplan.jsp", new String[]{SecurityPermission.ADMIN_WORKPLAN_PAGE}));

		//security
		pageMap.put("Security", new PageModel("/WEB-INF/securepages/admin/application/security.jsp", new String[]{SecurityPermission.ADMIN_SECURITY_PAGE}));
		pageMap.put("Security-Roles", new PageModel("/WEB-INF/securepages/admin/application/roleManagement.jsp", new String[]{SecurityPermission.ADMIN_ROLES_PAGE}));
		pageMap.put("User-Management", new PageModel("/WEB-INF/securepages/admin/application/userManagement.jsp", new String[]{SecurityPermission.ADMIN_USER_MANAGEMENT_PAGE}));

		//Evaluation
		pageMap.put("Evaluations", new PageModel("/WEB-INF/securepages/admin/evaluation/evaluations.jsp", new String[]{SecurityPermission.ADMIN_EVAL_PAGE}));
		pageMap.put("Evaluation-Templates", new PageModel("/WEB-INF/securepages/admin/evaluation/evaluationsTemplates.jsp", new String[]{SecurityPermission.ADMIN_EVAL_TEMPLATES_PAGE}));
		pageMap.put("Checklist-Templates", new PageModel("/WEB-INF/securepages/admin/evaluation/checklistTemplates.jsp", new String[]{SecurityPermission.ADMIN_EVAL_CHECKLIST_TEMPLATES_PAGE}));
		pageMap.put("Checklist-Questions", new PageModel("/WEB-INF/securepages/admin/evaluation/checklistQuestions.jsp", new String[]{SecurityPermission.ADMIN_EVAL_CHECKLIST_QUESTIONS_PAGE}));
		pageMap.put("Section-Templates", new PageModel("/WEB-INF/securepages/admin/evaluation/sectionTemplates.jsp", new String[]{SecurityPermission.ADMIN_EVAL_SECTION_PAGE}));

		return pageMap;
	}

}
