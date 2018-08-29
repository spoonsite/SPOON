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
 */
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dshurtleff
 * @author cyearsley
 * @author rfrazier
 *
 */
@SystemTable
@APIDescription("Defines Security Permissions")
public class SecurityPermission
		extends LookupEntity<SecurityPermission>
{

	private static final long serialVersionUID = 1L;

	@APIDescription("A string representation of the permission predecessor of the current permission (without this permission predecessor, you cannot activate this permission)")
	private String permissionPredecessor;

	@APIDescription("A list of default roles")
	private List<String> defaultRoles;

	// Alerts
	public static final String GROUPBY_ALERT = "Alerts";
	public static final String ADMIN_ALERTS_PAGE = "ADMIN-ALERTS-PAGE";
	public static final String ADMIN_ALERT_MANAGEMENT_READ = "ADMIN-ALERT-MANAGEMENT-READ";
	public static final String ADMIN_ALERT_MANAGEMENT_CREATE = "ADMIN-ALERT-MANAGEMENT-CREATE";
	public static final String ADMIN_ALERT_MANAGEMENT_UPDATE = "ADMIN-ALERT-MANAGEMENT-UPDATE";
	public static final String ADMIN_ALERT_MANAGEMENT_DELETE = "ADMIN-ALERT-MANAGEMENT-DELETE";

	//API
	public static final String GROUPBY_API = "API";
	public static final String ADMIN_API_PAGE = "ADMIN-API-PAGE";

	// Attributes
	public static final String GROUPBY_ATTRIBUTE = "Attributes";
	public static final String ADMIN_ATTRIBUTE_PAGE = "ADMIN-ATTRIBUTE-PAGE";
	public static final String ADMIN_ATTRIBUTE_MANAGEMENT_CREATE = "ADMIN-ATTRIBUTE-MANAGEMENT-CREATE";
	public static final String ADMIN_ATTRIBUTE_MANAGEMENT_UPDATE = "ADMIN-ATTRIBUTE-MANAGEMENT-UPDATE";
	public static final String ADMIN_ATTRIBUTE_MANAGEMENT_DELETE = "ADMIN-ATTRIBUTE-MANAGEMENT-DELETE";
	public static final String ADMIN_COMPONENT_ATTRIBUTE_MANAGEMENT = "ADMIN-COMPONENT-ATTRIBUTE-MANAGEMENT";
    public static final String ALLOW_USER_ATTRIBUTE_TYPE_CREATION = "ALLOW-USER-ATTRIBUTE-TYPE-CREATION";

	// Branding
	public static final String GROUPBY_BRANDING = "Branding";
	public static final String ADMIN_BRANDING_PAGE = "ADMIN-BRANDING-PAGE";
	public static final String ADMIN_BRANDING_CREATE = "ADMIN-BRANDING-CREATE";
	public static final String ADMIN_BRANDING_UPDATE = "ADMIN-BRANDING-UPDATE";
	public static final String ADMIN_BRANDING_DELETE = "ADMIN-BRANDING-DELETE";

	// Contacts
	public static final String GROUPBY_CONTACT = "Contacts";
	public static final String ADMIN_CONTACTS_PAGE = "ADMIN-CONTACTS-PAGE";
	public static final String ADMIN_CONTACT_MANAGEMENT_CREATE = "ADMIN-CONTACT-MANAGEMENT-CREATE";
	public static final String ADMIN_CONTACT_MANAGEMENT_UPDATE = "ADMIN-CONTACT-MANAGEMENT-UPDATE";
	public static final String ADMIN_CONTACT_MANAGEMENT_DELETE = "ADMIN-CONTACT-MANAGEMENT-DELETE";

	//Dashboard
	public static final String GROUPBY_DASHBOARD = "Dashboard";
	public static final String DASHBOARD_PAGE = "DASHBOARD-PAGE";
	public static final String DASHBOARD_WIDGET_ENTRY_STATS = "DASHBOARD-WIDGET-ENTRY-STATS";
	public static final String DASHBOARD_WIDGET_EVALUATION_STATS = "DASHBOARD-WIDGET-EVALUATION-STATS";
	public static final String DASHBOARD_WIDGET_NOTIFICATIONS = "DASHBOARD-WIDGET-NOTIFICATIONS";
	public static final String DASHBOARD_WIDGET_OUTSTANDING_FEEDBACK = "DASHBOARD-WIDGET-OUTSTANDING-FEEDBACK";
	public static final String DASHBOARD_WIDGET_PENDING_REQUESTS = "DASHBOARD-WIDGET-PENDING-REQUESTS";
	public static final String DASHBOARD_WIDGET_QUESTIONS = "DASHBOARD-WIDGET-QUESTIONS";
	public static final String DASHBOARD_WIDGET_USER_DATA = "DASHBOARD-WIDGET-USER-DATA";
	public static final String DASHBOARD_WIDGET_REPORTS = "DASHBOARD-WIDGET-REPORTS";
	public static final String DASHBOARD_WIDGET_SAVED_SEARCH = "DASHBOARD-WIDGET-SAVED-SEARCH";
	public static final String DASHBOARD_WIDGET_SUBMISSION_STATUS = "DASHBOARD-WIDGET-SUBMISSION-STATUS";
	public static final String DASHBOARD_WIDGET_SYSTEM_STATUS = "DASHBOARD-WIDGET-SYSTEM-STATUS";
	public static final String DASHBOARD_WIDGET_USER_STATS = "DASHBOARD-WIDGET-USER-STATS";
	public static final String DASHBOARD_WIDGET_WATCHES = "DASHBOARD-WIDGET-WATCHES";

	// Entry Management
	public static final String GROUPBY_ENTRY_MANAGEMENT = "Entries";
	public static final String ADMIN_ENTRIES_PAGE = "ADMIN-ENTRIES-PAGE";
	public static final String ADMIN_ENTRY_CREATE = "ADMIN-ENTRY-CREATE";
	public static final String ADMIN_ENTRY_READ = "ADMIN-ENTRY-READ";
	public static final String ADMIN_ENTRY_UPDATE = "ADMIN-ENTRY-UPDATE";
	public static final String ADMIN_ENTRY_DELETE = "ADMIN-ENTRY-DELETE";
	public static final String ADMIN_ENTRY_TOGGLE_STATUS = "ADMIN-ENTRY-TOGGLE-STATUS";
	public static final String ADMIN_ENTRY_MERGE = "ADMIN-ENTRY-MERGE";
	public static final String ADMIN_ENTRY_VERSION_DELETE = "ADMIN-ENTRY-VERSION-DELETE";
	public static final String ADMIN_ENTRY_VERSION_READ = "ADMIN-ENTRY-VERSION-READ";
	public static final String ADMIN_ENTRY_VERSION_CREATE = "ADMIN-ENTRY-VERSION-CREATE";
	public static final String ADMIN_ENTRY_VERSION_RESTORE = "ADMIN-ENTRY-VERSION-RESTORE";
	public static final String ADMIN_ENTRY_APPROVE = "ADMIN-ENTRY-APPROVE";
	public static final String ADMIN_ENTRY_CHANGETYPE = "ADMIN-ENTRY-CHANGETYPE";
	public static final String ADMIN_ENTRY_ATTR_MANAGEMENT = "ADMIN-ENTRY-ATTR-MANAGEMENT";
	public static final String ADMIN_ENTRY_CONTACT_MANAGEMENT = "ADMIN-ENTRY-CONTACT-MANAGEMENT";
	public static final String ADMIN_ENTRY_DEPENDENCY_MANAGEMENT = "ADMIN-ENTRY-DEPENDENCY-MANAGEMENT";
	public static final String ADMIN_ENTRY_MEDIA_MANAGEMENT = "ADMIN-ENTRY-MEDIA-MANAGEMENT";
	public static final String ADMIN_ENTRY_RELATIONSHIP_MANAGEMENT = "ADMIN-ENTRY-RELATIONSHIP-MANAGEMENT";
	public static final String ADMIN_ENTRY_RESOURCE_MANAGEMENT = "ADMIN-ENTRY-RESOURCE-MANAGEMENT";
	public static final String ADMIN_ENTRY_EVALSECTION_MANAGEMENT = "ADMIN-ENTRY-EVALSECTION-MANAGEMENT";
	public static final String ADMIN_ENTRY_TAG_MANAGEMENT = "ADMIN-ENTRY-TAG-MANAGEMENT";
	public static final String ADMIN_ENTRY_CHANGEREQUEST_MANAGEMENT = "ADMIN-ENTRY-CHANGEREQUEST-MANAGEMENT";
	public static final String ADMIN_ENTRY_COMMENT_MANAGEMENT = "ADMIN-ENTRY-COMMENT-MANAGEMENT";
	public static final String ADMIN_ENTRY_CHANGEOWNER = "ADMIN-ENTRY-CHANGEOWNER";
	public static final String ADMIN_ENTRY_EXPORT = "ADMIN-ENTRY-EXPORT";
	public static final String ADMIN_ENTRY_PENDINGCHANGE_READ = "ADMIN-ENTRY-PENDINGCHANGE-READ";
	public static final String ADMIN_ENTRY_ASSIGNUSER = "ADMIN-ENTRY-ASSIGNUSER";

	// Entry Templates
	public static final String GROUPBY_ENTRY_TEMPLATES = "Entry Templates";
	public static final String ADMIN_ENTRYTEMPLATES_PAGE = "ADMIN-ENTRYTEMPLATES-PAGE";
	public static final String ADMIN_ENTRY_TEMPLATES_READ = "ADMIN-ENTRY-TEMPLATES-READ";
	public static final String ADMIN_ENTRY_TEMPLATES_CREATE = "ADMIN-ENTRY-TEMPLATES-CREATE";
	public static final String ADMIN_ENTRY_TEMPLATES_UPDATE = "ADMIN-ENTRY-TEMPLATES-UPDATE";
	public static final String ADMIN_ENTRY_TEMPLATES_DELETE = "ADMIN-ENTRY-TEMPLATES-DELETE";

	// Entry Types
	public static final String GROUPBY_ENTRY_TYPES = "Entry Types";
	public static final String ADMIN_ENTRYTYPE_PAGE = "ADMIN-ENTRYTYPE-PAGE";
	public static final String ADMIN_ENTRY_TYPES_CREATE = "ADMIN-ENTRY-TYPES-CREATE";
	public static final String ADMIN_ENTRY_TYPES_UPDATE = "ADMIN-ENTRY-TYPES-UPDATE";
	public static final String ADMIN_ENTRY_TYPES_DELETE = "ADMIN-ENTRY-TYPES-DELETE";

	//Evaluation Templates
	public static final String GROUPBY_EVALUATION_TEMPLATE = "Evaluation Templates";
	public static final String ADMIN_EVAL_TEMPLATES_PAGE = "ADMIN-EVAL-TEMPLATES-PAGE";
	public static final String ADMIN_EVALUATION_TEMPLATE_READ = "ADMIN-EVALUATION-TEMPLATE-READ";
	public static final String ADMIN_EVALUATION_TEMPLATE_CREATE = "ADMIN-EVALUATION-TEMPLATE-CREATE";
	public static final String ADMIN_EVALUATION_TEMPLATE_UPDATE = "ADMIN-EVALUATION-TEMPLATE-UPDATE";
	public static final String ADMIN_EVALUATION_TEMPLATE_DELETE = "ADMIN-EVALUATION-TEMPLATE-DELETE";

	// Evaluation Template Checklist Questions
	public static final String GROUPBY_EVALUATION_TEMPLATE_CHECKLIST_QUESTION = "Evaluation Templates Checklist Questions";
	public static final String ADMIN_EVAL_CHECKLIST_QUESTIONS_PAGE = "ADMIN-EVAL-CHECKLIST-QUESTIONS-PAGE";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION-READ";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_CREATE = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION-CREATE";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_UPDATE = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION-UPDATE";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_DELETE = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION-DELETE";

	//Evaluation Template Checklists
	public static final String GROUPBY_EVALUATION_TEMPLATE_CHECKLIST = "Evaluation Templates Checklists";
	public static final String ADMIN_EVAL_CHECKLIST_TEMPLATES_PAGE = "ADMIN-EVAL-CHECKLIST-TEMPLATES-PAGE";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-READ";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_CREATE = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-CREATE";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_UPDATE = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-UPDATE";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_DELETE = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-DELETE";

	//Evaluation Templates Sections
	public static final String GROUPBY_EVALUATION_TEMPLATE_SECTION = "Evaluation Templates Sections";
	public static final String ADMIN_EVAL_SECTION_PAGE = "ADMIN-EVAL-SECTION-PAGE";
	public static final String ADMIN_EVALUATION_TEMPLATE_SECTION_READ = "ADMIN-EVALUATION-TEMPLATE-SECTION-READ";
	public static final String ADMIN_EVALUATION_TEMPLATE_SECTION_CREATE = "ADMIN-EVALUATION-TEMPLATE-SECTION-CREATE";
	public static final String ADMIN_EVALUATION_TEMPLATE_SECTION_UPDATE = "ADMIN-EVALUATION-TEMPLATE-SECTION-UPDATE";
	public static final String ADMIN_EVALUATION_TEMPLATE_SECTION_DELETE = "ADMIN-EVALUATION-TEMPLATE-SECTION-DELETE";

	// Evaluation Management (Admin)
	public static final String GROUPBY_EVALUATION_ADMIN = "Evaluation (Admin)";
	public static final String ADMIN_EVAL_PAGE = "ADMIN-EVAL-PAGE";
	public static final String ADMIN_EVALUATION_MANAGEMENT_CREATE = "ADMIN-EVALUATION-MANAGEMENT-CREATE";
	public static final String ADMIN_EVALUATION_MANAGEMENT_DELETE = "ADMIN-EVALUATION-MANAGEMENT-DELETE";
	public static final String ADMIN_EVALUATION_ACTIVATE = "ADMIN-EVALUATION-ACTIVATE";
	public static final String ADMIN_EVALUATION_ALLOW_NEW_SECTIONS = "ADMIN-EVALUATION-ALLOW-NEW-SECTIONS";
	public static final String ADMIN_EVALUATION_ALLOW_QUESTION_MANAGEMENT = "ADMIN-EVALUATION-ALLOW-QUESTION-MANAGEMENT";
	public static final String ADMIN_EVALUATION_TOGGLE_PUBLISH = "ADMIN-EVALUATION-TOGGLE-PUBLISH";
	public static final String ADMIN_EVALUATION_PUBLISH_SUMMARY = "ADMIN-EVALUATION-PUBLISH-SUMMARY";
	public static final String ADMIN_EVALUATION_DELETE_COMMENT = "ADMIN-EVALUATION-DELETE-COMMENT";

	//Evaluation Management (User)
	public static final String GROUPBY_EVALUATION_USER = "Evaluation (User)";
	public static final String EVAL_PAGE = "EVAL-PAGE";
	public static final String USER_EVALUATIONS_READ = "USER-EVALUATIONS-READ";
	public static final String USER_EVALUATIONS_UPDATE = "USER-EVALUATIONS-UPDATE";
	public static final String USER_EVALUATIONS_ASSIGN_USER = "USER-EVALUATIONS-ASSIGN-USER";

	// FAQ
	public static final String GROUPBY_FAQ = "FAQs";
	public static final String ADMIN_FAQ_PAGE = "ADMIN-FAQ-PAGE";
	public static final String ADMIN_FAQ_READ = "ADMIN-FAQ-READ";
	public static final String ADMIN_FAQ_CREATE = "ADMIN-FAQ-CREATE";
	public static final String ADMIN_FAQ_UPDATE = "ADMIN-FAQ-UPDATE";
	public static final String ADMIN_FAQ_DELETE = "ADMIN-FAQ-DELETE";

	// Feedback
	public static final String GROUPBY_FEEDBACK = "Feedback";
	public static final String ADMIN_FEEDBACK_PAGE = "ADMIN-FEEDBACK-PAGE";
	public static final String ADMIN_FEEDBACK_READ = "ADMIN-FEEDBACK-READ";
	public static final String ADMIN_FEEDBACK_UPDATE = "ADMIN-FEEDBACK-UPDATE";
	public static final String ADMIN_FEEDBACK_DELETE = "ADMIN-FEEDBACK-DELETE";

	// Highlights
	public static final String GROUPBY_HIGHLIGHT = "Highlights";
	public static final String ADMIN_HIGHLIGHTS_PAGE = "ADMIN-HIGHLIGHTS-PAGE";
	public static final String ADMIN_HIGHLIGHTS_CREATE = "ADMIN-HIGHLIGHTS-CREATE";
	public static final String ADMIN_HIGHLIGHTS_UPDATE = "ADMIN-HIGHLIGHTS-UPDATE";
	public static final String ADMIN_HIGHLIGHTS_DELETE = "ADMIN-HIGHLIGHTS-DELETE";

	// Import/Export
	public static final String GROUPBY_IMPORTEXPORT = "Import/Export";
	public static final String ADMIN_IMPORT_PAGE = "ADMIN-IMPORT-PAGE";
	public static final String ADMIN_DATA_IMPORT_EXPORT = "ADMIN-DATA-IMPORT-EXPORT";

	// Integrations
	public static final String GROUPBY_INTEGRATION = "Integrations";
	public static final String ADMIN_INTEGRATION_PAGE = "ADMIN-INTEGRATION-PAGE";
	public static final String ADMIN_INTEGRATION_READ = "ADMIN-INTEGRATION-READ";
	public static final String ADMIN_INTEGRATION_CREATE = "ADMIN-INTEGRATION-CREATE";
	public static final String ADMIN_INTEGRATION_UPDATE = "ADMIN-INTEGRATION-UPDATE";
	public static final String ADMIN_INTEGRATION_DELETE = "ADMIN-INTEGRATION-DELETE";
	public static final String ADMIN_INTEGRATION_RUNALL = "ADMIN-INTEGRATION-RUNALL";
	public static final String ADMIN_INTEGRATION_RUNCONFIG = "ADMIN-INTEGRATION-RUNCONFIG";
	public static final String ADMIN_INTEGRATION_RUNINTEGRATION = "ADMIN-INTEGRATION-RUNINTEGRATION";
	public static final String ADMIN_INTEGRATION_TOGGLESTATUS = "ADMIN-INTEGRATION-TOGGLESTATUS";
	public static final String ADMIN_INTEGRATION_EXTERNAL = "ADMIN-INTEGRATION-EXTERNAL";

	// Job Management
	public static final String GROUPBY_JOB_MANAGEMENT = "Jobs";
	public static final String ADMIN_JOBS_PAGE = "ADMIN-JOBS-PAGE";
	public static final String ADMIN_JOB_MANAGEMENT_READ = "ADMIN-JOB-MANAGEMENT-READ";
	public static final String ADMIN_JOB_MANAGEMENT_ACTION = "ADMIN-JOB-MANAGEMENT-ACTION";
	public static final String ADMIN_JOB_MANAGEMENT_DELETE = "ADMIN-JOB-MANAGEMENT-DELETE";

	// Lookups
	public static final String GROUPBY_LOOKUP = "Lookups";
	public static final String ADMIN_LOOKUPS_PAGE = "ADMIN-LOOKUPS-PAGE";
	public static final String ADMIN_LOOKUPS_READ = "ADMIN-LOOKUPS-READ";
	public static final String ADMIN_LOOKUPS_CREATE_CODE = "ADMIN-LOOKUPS-CREATE-CODE";
	public static final String ADMIN_LOOKUPS_UPDATE_CODE = "ADMIN-LOOKUPS-UPDATE-CODE";
	public static final String ADMIN_LOOKUPS_DELETE_CODE = "ADMIN-LOOKUPS-DELETE-CODE";

	//Media
	public static final String GROUPBY_MEDIA = "Media";
	public static final String ADMIN_SUPPORTMEDIA_PAGE = "ADMIN-SUPPORTMEDIA-PAGE";
	public static final String ADMIN_MEDIA_PAGE = "ADMIN-MEDIA-PAGE";
	public static final String ADMIN_MEDIA_DELETE = "ADMIN-MEDIA-DELETE";
	public static final String ADMIN_MEDIA_UPLOAD = "ADMIN-MEDIA-UPLOAD";
	public static final String ADMIN_TEMPMEDIA_MANAGEMENT_READ = "ADMIN-TEMPMEDIA-MANAGEMENT-READ";
	public static final String ADMIN_TEMPMEDIA_MANAGEMENT_DELETE = "ADMIN-TEMPMEDIA-MANAGEMENT-DELETE";
	public static final String ADMIN_SUPPORT_MEDIA_CREATE = "ADMIN-SUPPORT-MEDIA-CREATE";
	public static final String ADMIN_SUPPORT_MEDIA_UPDATE = "ADMIN-SUPPORT-MEDIA-UPDATE";
	public static final String ADMIN_SUPPORT_MEDIA_DELETE = "ADMIN-SUPPORT-MEDIA-DELETE";

	//Message Management
	public static final String GROUPBY_MESSAGE_MANAGEMENT = "Messages";
	public static final String ADMIN_MESSAGES_PAGE = "ADMIN-MESSAGES-PAGE";
	public static final String ADMIN_MESSAGE_MANAGEMENT_READ = "ADMIN-MESSAGE-MANAGEMENT-READ";
	public static final String ADMIN_MESSAGE_MANAGEMENT_CREATE = "ADMIN-MESSAGE-MANAGEMENT-CREATE";
	public static final String ADMIN_MESSAGE_MANAGEMENT_UPDATE = "ADMIN-MESSAGE-MANAGEMENT-UPDATE";
	public static final String ADMIN_MESSAGE_MANAGEMENT_DELETE = "ADMIN-MESSAGE-MANAGEMENT-DELETE";

	//Notification Events
	public static final String GROUPBY_NOTIFICATION_EVENT_MANAGEMENT = "Notification Events";
	public static final String ADMIN_NOTIFICATION_EVENT_CREATE = "ADMIN-NOTIFICATION-EVENT-CREATE";
	public static final String ADMIN_NOTIFICATION_EVENT_DELETE = "ADMIN-NOTIFICATION-EVENT-DELETE";

	// Organization
	public static final String GROUPBY_ORGANIZATION = "Organizations";
	public static final String ADMIN_ORGANIZATION_PAGE = "ADMIN-ORGANIZATION-PAGE";
	public static final String ADMIN_ORGANIZATION_CREATE = "ADMIN-ORGANIZATION-CREATE";
	public static final String ADMIN_ORGANIZATION_UPDATE = "ADMIN-ORGANIZATION-UPDATE";
	public static final String ADMIN_ORGANIZATION_DELETE = "ADMIN-ORGANIZATION-DELETE";
	public static final String ADMIN_ORGANIZATION_MERGE = "ADMIN-ORGANIZATION-MERGE";
	public static final String ADMIN_ORGANIZATION_EXTRACTION = "ADMIN-ORGANIZATION-EXTRACTION";

	// Profile Management
	public static final String GROUPBY_PROFILE_MANAGEMENT = "Profile Management";
	public static final String USER_PROFILE_PAGE = "USER-PROFILE-PAGE";
	public static final String ADMIN_USERPROFILES_PAGE = "ADMIN-USERPROFILES-PAGE";
	public static final String ADMIN_USER_MANAGEMENT_PROFILES_READ = "ADMIN-USER-MANAGEMENT-PROFILES-READ";
	public static final String ADMIN_USER_MANAGEMENT_PROFILES_UPDATE = "ADMIN-USER-MANAGEMENT-PROFILES-UPDATE";
	public static final String ADMIN_USER_MANAGEMENT_PROFILES_DELETE = "ADMIN-USER-MANAGEMENT-PROFILES-DELETE";

	// Questions
	public static final String GROUPBY_QUESTION = "Questions";
	public static final String USER_QUESTIONS_PAGE = "USER-QUESTIONS-PAGE";
	public static final String ADMIN_QUESTION_PAGE = "ADMIN-QUESTIONS-PAGE";
	public static final String ADMIN_QUESTIONS_READ = "ADMIN-QUESTIONS-READ";
	public static final String ADMIN_QUESTIONS_UPDATE = "ADMIN-QUESTIONS-UPDATE";
	public static final String ADMIN_QUESTIONS_DELETE = "ADMIN-QUESTIONS-DELETE";

	//Relationships
	public static final String GROUPBY_RELATIONSHIPS = "Relationships";
	public static final String ADMIN_RELATIONSHIPS_PAGE = "ADMIN-RELATIONSHIPS-PAGE";
	public static final String USER_RELATIONSHIPS_PAGE = "USER-RELATIONSHIPS-PAGE";

	// Reports
	public static final String GROUPBY_REPORTS = "Reports";
	public static final String REPORTS_READ = "REPORTS-READ";
	public static final String REPORTS_PAGE = "REPORTS-PAGE";
	public static final String REPORTS_CREATE = "REPORTS-CREATE"; // not even using this?
	public static final String REPORTS_DELETE = "REPORTS-DELETE"; // not even using this?
	public static final String REPORTS_ALL = "REPORTS-ALL";
	public static final String REPORT_OUTPUT_EMAIL_ATTACH = "REPORT-OUTPUT-EMAIL-ATTACH";
	public static final String REPORT_OUTPUT_EMAIL_BODY = "REPORT-OUTPUT-EMAIL-BODY";
	public static final String RUN_ACTION_REPORT = "RUN-ACTION-REPORT";
	public static final String RUN_ENTRIES_BY_CAT_REPORT = "RUN-ENTRIES-BY-CAT-REPORT";
	public static final String RUN_ENTRIES_BY_ORG_REPORT = "RUN-ENTRIES-BY-ORG-REPORT";
	public static final String RUN_ENTRY_REPORT = "RUN-ENTRY-REPORT";
	public static final String RUN_ENTRY_DETAIL_REPORT = "RUN-ENTRY-DETAIL-REPORT";
	public static final String RUN_ENTRY_LISTING_REPORT = "RUN-ENTRY-LISTING-REPORT";
	public static final String RUN_ENTRY_STATUS_REPORT = "RUN-ENTRY-STATUS-REPORT";
	public static final String RUN_EVAL_STATUS_REPORT = "RUN-EVAL-STATUS-REPORT";
	public static final String RUN_LINK_VALIDATION_REPORT = "RUN-LINK-VALIDATION-REPORT";
	public static final String RUN_SUBMISSIONS_REPORT = "RUN-SUBMISSIONS-REPORT";
	public static final String RUN_USAGE_REPORT = "RUN-USAGE-REPORT";
	public static final String RUN_USER_REPORT = "RUN-USER-REPORT";
	public static final String RUN_USER_ORG_REPORT = "RUN-USER-ORG-REPORT";
	public static final String RUN_WORKPLAN_STATUS = "RUN-WORKPLAN-STATUS";

	// Reports Schedule
	public static final String GROUPBY_REPORTS_SCHEDULE = "Reports Schedule";	
	public static final String REPORTS_SCHEDULE_CREATE = "REPORTS-SCHEDULE-CREATE";
	public static final String REPORTS_SCHEDULE_READ = "REPORTS-SCHEDULE-READ";
	public static final String REPORTS_SCHEDULE_UPDATE = "REPORTS-SCHEDULE-UPDATE";
	public static final String REPORTS_SCHEDULE_DELETE = "REPORTS-SCHEDULE-DELETE";

	// Reviews
	public static final String GROUPBY_REVIEW = "Reviews";
	public static final String USER_REVIEW_PAGE = "USER-REVIEW-PAGE";
	public static final String ADMIN_REVIEW_PAGE = "ADMIN-REVIEW-PAGE";
	public static final String ADMIN_REVIEW_READ = "ADMIN-REVIEW-READ";
	public static final String ADMIN_REVIEW_UPDATE = "ADMIN-REVIEW-UPDATE";
	public static final String ADMIN_REVIEW_DELETE = "ADMIN-REVIEW-DELETE";

	// Role
	public static final String GROUPBY_ROLE = "Roles";
	public static final String ADMIN_ROLES_PAGE = "ADMIN-ROLES-PAGE";
	public static final String ADMIN_ROLE_MANAGEMENT_READ = "ADMIN-ROLE-MANAGEMENT-READ";
	public static final String ADMIN_ROLE_MANAGEMENT_CREATE = "ADMIN-ROLE-MANAGEMENT-CREATE";
	public static final String ADMIN_ROLE_MANAGEMENT_UPDATE = "ADMIN-ROLE-MANAGEMENT-UPDATE";
	public static final String ADMIN_ROLE_MANAGEMENT_DELETE = "ADMIN-ROLE-MANAGEMENT-DELETE";

	// Search
	public static final String GROUPBY_SEARCH = "Search Management";
	public static final String USER_SEARCHES_PAGE = "USER-SEARCHES-PAGE";
	public static final String ADMIN_SEARCHES_PAGE = "ADMIN-SEARCHES-PAGE";
	public static final String ADMIN_SEARCH_READ = "ADMIN-SEARCH-READ";
	public static final String ADMIN_SEARCH_CREATE = "ADMIN-SEARCH-CREATE";
	public static final String ADMIN_SEARCH_UPDATE = "ADMIN-SEARCH-UPDATE";
	public static final String ADMIN_SEARCH_DELETE = "ADMIN-SEARCH-DELETE";

	//Security
	public static final String GROUPBY_ADMIN_SECURITY = "Security";
	public static final String ADMIN_SECURITY_PAGE = "ADMIN-SECURITY-PAGE";
	public static final String ADMIN_SECURITY_POLICY = "ADMIN-SECURITY-POLICY";
	public static final String ADMIN_SECURITY_SHIRO_CONFIG = "ADMIN-SECURITY-SHIRO-CONFIG";

	//Submissions (Admin)
	public static final String GROUPBY_ADMIN_USER_SUBMISSION = "Submissions (Admin)";
	public static final String ADMIN_PARTIAL_SUBMISSIONS_PAGE = "ADMIN-PARTIAL-SUBMISSIONS-PAGE";
	public static final String ADMIN_SUBMISSION_FORM_TEMPLATE_PAGE = "ADMIN-SUBMISSION-FORM-TEMPLATE-PAGE";
	public static final String ADMIN_USER_SUBMISSIONS_READ = "ADMIN-USER-SUBMISSIONS-READ";
	public static final String ADMIN_USER_SUBMISSIONS_UPDATE = "ADMIN-USER-SUBMISSIONS-UPDATE";
	public static final String ADMIN_USER_SUBMISSIONS_DELETE = "ADMIN-USER-SUBMISSIONS-DELETE";
	public static final String ADMIN_SUBMISSION_FORM_TEMPLATE_READ = "ADMIN-SUBMISSION-FORM-TEMPLATE-READ";
	public static final String ADMIN_SUBMISSION_FORM_TEMPLATE_CREATE = "ADMIN-SUBMISSION-FORM-TEMPLATE-CREATE";
	public static final String ADMIN_SUBMISSION_FORM_TEMPLATE_UPDATE = "ADMIN-SUBMISSION-FORM-TEMPLATE-UPDATE";
	public static final String ADMIN_SUBMISSION_FORM_TEMPLATE_DELETE = "ADMIN-SUBMISSION-FORM-TEMPLATE-DELETE";

	//Submissions (User)
	public static final String GROUPBY_USER_SUBMISSION = "Submissions (User)";
	public static final String USER_SUBMISSIONS_PAGE = "USER-SUBMISSIONS-PAGE";
	public static final String USER_SUBMISSIONS_READ = "USER-SUBMISSIONS-READ";
	public static final String USER_SUBMISSIONS_CREATE = "USER-SUBMISSIONS-CREATE";
	public static final String USER_SUBMISSIONS_UPDATE = "USER-SUBMISSIONS-UPDATE";
	public static final String USER_SUBMISSIONS_DELETE = "USER-SUBMISSIONS-DELETE";
	public static final String USER_SUBMISSIONS_CHANGEREQUEST = "USER-SUBMISSIONS-CHANGEREQUEST";

	//System Management
	public static final String GROUPBY_SYSTEM_MANAGEMENT = "System";
	public static final String ADMIN_SYSTEM_PAGE = "ADMIN-SYSTEM-PAGE";
	public static final String ADMIN_SYSTEM_ARCHIVES_PAGE = "ADMIN-SYSTEM-ARCHIVES-PAGE";
	public static final String ADMIN_SYSTEM_MANAGEMENT = "ADMIN-SYSTEM-MANAGEMENT"; // looks like a legacy permission
	public static final String ADMIN_SYSTEM_MANAGEMENT_APP_PROP = "ADMIN-SYSTEM-MANAGEMENT-APP-PROP";
	public static final String ADMIN_SYSTEM_MANAGEMENT_PLUGIN = "ADMIN-SYSTEM-MANAGEMENT-PLUGIN";
	public static final String ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_READ = "ADMIN-SYSTEM-MANAGEMENT-ARCHIVE-READ";
	public static final String ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_CREATE = "ADMIN-SYSTEM-MANAGEMENT-ARCHIVE-CREATE";
	public static final String ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_DELETE = "ADMIN-SYSTEM-MANAGEMENT-ARCHIVE-DELETE";
	public static final String ADMIN_SYSTEM_MANAGEMENT_ERROR_TICKET = "ADMIN-SYSTEM-MANAGEMENT-ERROR-TICKET";
	public static final String ADMIN_SYSTEM_MANAGEMENT_SEARCH_CONTROL = "ADMIN-SYSTEM-MANAGEMENT-SEARCH-CONTROL";
	public static final String ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_READ = "ADMIN-SYSTEM-MANAGEMENT-CONFIG-PROP-READ";
	public static final String ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_UPDATE = "ADMIN-SYSTEM-MANAGEMENT-CONFIG-PROP-UPDATE";
	public static final String ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_DELETE = "ADMIN-SYSTEM-MANAGEMENT-CONFIG-PROP-DELETE";
	public static final String ADMIN_SYSTEM_MANAGEMENT_LOGGING = "ADMIN-SYSTEM-MANAGEMENT-LOGGING";
	public static final String ADMIN_SYSTEM_MANAGEMENT_MANAGERS = "ADMIN-SYSTEM-MANAGEMENT-MANAGERS";
	public static final String ADMIN_SYSTEM_MANAGEMENT_CACHE = "ADMIN-SYSTEM-MANAGEMENT-CACHE";
	public static final String ADMIN_SYSTEM_MANAGEMENT_RECENT_CHANGES = "ADMIN-SYSTEM-MANAGEMENT-RECENT-CHANGES";
	public static final String ADMIN_SYSTEM_MANAGEMENT_STATUS = "ADMIN-SYSTEM-MANAGEMENT-STATUS";
	public static final String ADMIN_SYSTEM_MANAGEMENT_STAND_BY = "ADMIN-SYSTEM-MANAGEMENT-STAND-BY";

	//Tags
	public static final String GROUPBY_TAGS = "Tags";
	public static final String ADMIN_TAGS_PAGE = "ADMIN-TAGS-PAGE";

	//Tracking
	public static final String GROUPBY_TRACKING = "Tracking";
	public static final String ADMIN_TRACKING_PAGE = "ADMIN-TRACKING-PAGE";
	public static final String ADMIN_TRACKING_READ = "ADMIN-TRACKING-READ";
	public static final String ADMIN_TRACKING_UPDATE = "ADMIN-TRACKING-UPDATE";
	public static final String ADMIN_TRACKING_DELETE = "ADMIN-TRACKING-DELETE";

	//Users (Admin)
	public static final String GROUPBY_USER_MANAGEMENT = "User Managment";
	public static final String ADMIN_USER_MANAGEMENT_PAGE = "ADMIN-USER-MANAGEMENT-PAGE";
	public static final String ADMIN_USER_MANAGEMENT_READ = "ADMIN-USER-MANAGEMENT-READ";
	public static final String ADMIN_USER_MANAGEMENT_CREATE = "ADMIN-USER-MANAGEMENT-CREATE";
	public static final String ADMIN_USER_MANAGEMENT_UPDATE = "ADMIN-USER-MANAGEMENT-UPDATE";
	public static final String ADMIN_USER_MANAGEMENT_DELETE = "ADMIN-USER-MANAGEMENT-DELETE";
	public static final String ADMIN_USER_MANAGEMENT_APPROVE = "ADMIN-USER-MANAGEMENT-APPROVE";

	// Watches
	public static final String GROUPBY_WATCHES = "Watches";
	public static final String ADMIN_WATCHES_PAGE = "ADMIN-WATCHES-PAGE";
	public static final String USER_WATCHES_PAGE = "USER-WATCHES-PAGE";
	public static final String ADMIN_WATCHES_DELETE = "ADMIN-WATCHES-DELETE";
	public static final String ADMIN_WATCHES_UPDATE = "ADMIN-WATCHES-UPDATE";
	public static final String ADMIN_WATCHES_READ = "ADMIN-WATCHES-READ";

	// Workplan
	public static final String GROUPBY_WORKPLAN = "Workplan";
	public static final String ADMIN_WORKPLAN_PAGE = "ADMIN-WORKPLAN-PAGE";
	public static final String ADMIN_WORKPLAN_READ = "ADMIN-WORKPLAN-READ";
	public static final String ADMIN_WORKPLAN_CREATE = "ADMIN-WORKPLAN-CREATE";
	public static final String ADMIN_WORKPLAN_UPDATE = "ADMIN-WORKPLAN-UPDATE";
	public static final String ADMIN_WORKPLAN_DELETE = "ADMIN-WORKPLAN-DELETE";
	public static final String USER_WORKPLAN_READ = "USER-WORKPLAN-READ";

	// Workplan Progress Management
	public static final String GROUPBY_WORKPLAN_PROGRESS_MANAGEMENT = "Workplan Progress Management";
	public static final String WORKPLAN_PROGRESS_MANAGEMENT_PAGE = "WORKPLAN-PROGRESS-MANAGEMENT-PAGE";
	public static final String WORKFLOW_LINK_READ = "WORKFLOW-LINK-READ";
	public static final String WORKFLOW_LINK_READ_ALL = "WORKFLOW-LINK-READ-ALL";
	public static final String WORKFLOW_LINK_UPDATE = "WORKFLOW-LINK-UPDATE";
	public static final String WORKFLOW_LINK_DELETE = "WORKFLOW-LINK-DELETE";
	public static final String WORKFLOW_LINK_ASSIGN = "WORKFLOW-LINK-ASSIGN";
	public static final String WORKFLOW_LINK_ASSIGN_ANY = "WORKFLOW-LINK-ASSIGN-ANY";
	public static final String WORKFLOW_ADMIN_SUBMISSION_COMMENTS = "WORKFLOW-ADMIN-SUBMISSION-COMMENTS";

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();

		// Alerts
		codeMap.put(ADMIN_ALERTS_PAGE, newLookup(SecurityPermission.class, ADMIN_ALERTS_PAGE, "Provides access to the admin Alerts page", null, GROUPBY_ALERT));
		((SecurityPermission) codeMap.get(ADMIN_ALERTS_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ALERTS_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ALERT_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT_READ, "Gives ability to read all alerts", null, GROUPBY_ALERT));
		((SecurityPermission) codeMap.get(ADMIN_ALERT_MANAGEMENT_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ALERT_MANAGEMENT_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ALERT_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT_CREATE, "Gives ability to create new alerts", null, GROUPBY_ALERT));
		((SecurityPermission) codeMap.get(ADMIN_ALERT_MANAGEMENT_CREATE)).setPermissionPredecessor(ADMIN_ALERT_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_ALERT_MANAGEMENT_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ALERT_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT_UPDATE, "Gives ability to update existing alerts", null, GROUPBY_ALERT));
		((SecurityPermission) codeMap.get(ADMIN_ALERT_MANAGEMENT_UPDATE)).setPermissionPredecessor(ADMIN_ALERT_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_ALERT_MANAGEMENT_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ALERT_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT_DELETE, "Give ability to delete alerts", null, GROUPBY_ALERT));
		((SecurityPermission) codeMap.get(ADMIN_ALERT_MANAGEMENT_DELETE)).setPermissionPredecessor(ADMIN_ALERT_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_ALERT_MANAGEMENT_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		//API
		codeMap.put(ADMIN_API_PAGE, newLookup(SecurityPermission.class, ADMIN_API_PAGE, "Give ability to see API docs", null, GROUPBY_API));
		((SecurityPermission) codeMap.get(ADMIN_API_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_API_PAGE)).setDefaultRoles(null);

		//Attributes
		codeMap.put(ADMIN_ATTRIBUTE_PAGE, newLookup(SecurityPermission.class, ADMIN_ATTRIBUTE_PAGE, "Provides access to the admin attribute page", null, GROUPBY_ATTRIBUTE));
		((SecurityPermission) codeMap.get(ADMIN_ATTRIBUTE_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ATTRIBUTE_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ATTRIBUTE_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_ATTRIBUTE_MANAGEMENT_CREATE, "Gives ability to create new attributes", null, GROUPBY_ATTRIBUTE));
		((SecurityPermission) codeMap.get(ADMIN_ATTRIBUTE_MANAGEMENT_CREATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ATTRIBUTE_MANAGEMENT_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ATTRIBUTE_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_ATTRIBUTE_MANAGEMENT_UPDATE, "Gives ability to update existing attributes", null, GROUPBY_ATTRIBUTE));
		((SecurityPermission) codeMap.get(ADMIN_ATTRIBUTE_MANAGEMENT_UPDATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ATTRIBUTE_MANAGEMENT_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ATTRIBUTE_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_ATTRIBUTE_MANAGEMENT_DELETE, "Gives ability to delete existing attributes", null, GROUPBY_ATTRIBUTE));
		((SecurityPermission) codeMap.get(ADMIN_ATTRIBUTE_MANAGEMENT_DELETE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ATTRIBUTE_MANAGEMENT_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_COMPONENT_ATTRIBUTE_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_COMPONENT_ATTRIBUTE_MANAGEMENT, "Gives ability to manage attributes on entries from the admin tools manage assignments tool", null, GROUPBY_ATTRIBUTE));
		((SecurityPermission) codeMap.get(ADMIN_COMPONENT_ATTRIBUTE_MANAGEMENT)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_COMPONENT_ATTRIBUTE_MANAGEMENT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));
			
		codeMap.put(ALLOW_USER_ATTRIBUTE_TYPE_CREATION, newLookup(SecurityPermission.class, ALLOW_USER_ATTRIBUTE_TYPE_CREATION, "Allows for creating attribute types", null, GROUPBY_ATTRIBUTE));
		((SecurityPermission) codeMap.get(ALLOW_USER_ATTRIBUTE_TYPE_CREATION)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ALLOW_USER_ATTRIBUTE_TYPE_CREATION)).setDefaultRoles(null);

		// Branding
		codeMap.put(ADMIN_BRANDING_PAGE, newLookup(SecurityPermission.class, ADMIN_BRANDING_PAGE, "Provides access to the admin Branding page", null, GROUPBY_BRANDING));
		((SecurityPermission) codeMap.get(ADMIN_BRANDING_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_BRANDING_PAGE)).setDefaultRoles(null);

		codeMap.put(ADMIN_BRANDING_CREATE, newLookup(SecurityPermission.class, ADMIN_BRANDING_CREATE, "Gives ability to create new brandings", null, GROUPBY_BRANDING));
		((SecurityPermission) codeMap.get(ADMIN_BRANDING_CREATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_BRANDING_CREATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_BRANDING_UPDATE, newLookup(SecurityPermission.class, ADMIN_BRANDING_UPDATE, "Gives ability to update brandings", null, GROUPBY_BRANDING));
		((SecurityPermission) codeMap.get(ADMIN_BRANDING_UPDATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_BRANDING_UPDATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_BRANDING_DELETE, newLookup(SecurityPermission.class, ADMIN_BRANDING_DELETE, "Gives ability to delete brandings", null, GROUPBY_BRANDING));
		((SecurityPermission) codeMap.get(ADMIN_BRANDING_DELETE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_BRANDING_DELETE)).setDefaultRoles(null);

		// Contacts
		codeMap.put(ADMIN_CONTACTS_PAGE, newLookup(SecurityPermission.class, ADMIN_CONTACTS_PAGE, "Provides access to the admin contacts page", null, GROUPBY_CONTACT));
		((SecurityPermission) codeMap.get(ADMIN_CONTACTS_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_CONTACTS_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_CONTACT_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_CONTACT_MANAGEMENT_CREATE, "Gives ability to create contacts", null, GROUPBY_CONTACT));
		((SecurityPermission) codeMap.get(ADMIN_CONTACT_MANAGEMENT_CREATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_CONTACT_MANAGEMENT_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_CONTACT_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_CONTACT_MANAGEMENT_UPDATE, "Gives ability to update existing contacts", null, GROUPBY_CONTACT));
		((SecurityPermission) codeMap.get(ADMIN_CONTACT_MANAGEMENT_UPDATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_CONTACT_MANAGEMENT_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_CONTACT_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_CONTACT_MANAGEMENT_DELETE, "Gives ability delete contacts", null, GROUPBY_CONTACT));
		((SecurityPermission) codeMap.get(ADMIN_CONTACT_MANAGEMENT_DELETE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_CONTACT_MANAGEMENT_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		//Dashboard
		codeMap.put(DASHBOARD_PAGE, newLookup(SecurityPermission.class, DASHBOARD_PAGE, "Provides access to the dashboard page", null, GROUPBY_DASHBOARD));
		((SecurityPermission) codeMap.get(DASHBOARD_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(DASHBOARD_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(DASHBOARD_WIDGET_ENTRY_STATS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_ENTRY_STATS, "Allows user to view the Entry Stats widget (only effects UI)", null, GROUPBY_DASHBOARD));
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_ENTRY_STATS)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_ENTRY_STATS)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(DASHBOARD_WIDGET_EVALUATION_STATS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_EVALUATION_STATS, "Allows user to view the Evaluation Stats widget (only effects UI)", null, GROUPBY_DASHBOARD));
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_EVALUATION_STATS)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_EVALUATION_STATS)).setDefaultRoles(Arrays.asList(SecurityRole.EVALUATOR_ROLE, SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(DASHBOARD_WIDGET_NOTIFICATIONS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_NOTIFICATIONS, "Allows user to view the Notifications widget (only effects UI)", null, GROUPBY_DASHBOARD));
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_NOTIFICATIONS)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_NOTIFICATIONS)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(DASHBOARD_WIDGET_OUTSTANDING_FEEDBACK, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_OUTSTANDING_FEEDBACK, "Allows user to view the Outstanding Feedback widget (only effects UI)", null, GROUPBY_DASHBOARD));
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_OUTSTANDING_FEEDBACK)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_OUTSTANDING_FEEDBACK)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(DASHBOARD_WIDGET_PENDING_REQUESTS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_PENDING_REQUESTS, "Allows user to view the Pending Approval Requests widget (only effects UI)", null, GROUPBY_DASHBOARD));
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_PENDING_REQUESTS)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_PENDING_REQUESTS)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(DASHBOARD_WIDGET_QUESTIONS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_QUESTIONS, "Allows user to view the Questions widget (only effects UI)", null, GROUPBY_DASHBOARD));
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_QUESTIONS)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_QUESTIONS)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(DASHBOARD_WIDGET_USER_DATA, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_USER_DATA, "Allows user to view the Recent User Data widget (only effects UI)", null, GROUPBY_DASHBOARD));
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_USER_DATA)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_USER_DATA)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(DASHBOARD_WIDGET_REPORTS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_REPORTS, "Allows user to view the Reports widget (only effects UI)", null, GROUPBY_DASHBOARD));
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_REPORTS)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_REPORTS)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(DASHBOARD_WIDGET_SAVED_SEARCH, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_SAVED_SEARCH, "Allows user to view the Saved Search widget (only effects UI)", null, GROUPBY_DASHBOARD));
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_SAVED_SEARCH)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_SAVED_SEARCH)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(DASHBOARD_WIDGET_SUBMISSION_STATUS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_SUBMISSION_STATUS, "Allows user to view the Submission Status widget (only effects UI)", null, GROUPBY_DASHBOARD));
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_SUBMISSION_STATUS)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_SUBMISSION_STATUS)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(DASHBOARD_WIDGET_SYSTEM_STATUS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_SYSTEM_STATUS, "Allows user to view the System Status widget (only effects UI)", null, GROUPBY_DASHBOARD));
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_SYSTEM_STATUS)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_SYSTEM_STATUS)).setDefaultRoles(null);

		codeMap.put(DASHBOARD_WIDGET_USER_STATS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_USER_STATS, "Allows user to view the User Stats widget (only effects UI)", null, GROUPBY_DASHBOARD));
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_USER_STATS)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_USER_STATS)).setDefaultRoles(null);

		codeMap.put(DASHBOARD_WIDGET_WATCHES, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_WATCHES, "Allows user to view the Watches widget (only effects UI)", null, GROUPBY_DASHBOARD));
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_WATCHES)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(DASHBOARD_WIDGET_WATCHES)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		// Entry Management
		codeMap.put(ADMIN_ENTRIES_PAGE, newLookup(SecurityPermission.class, ADMIN_ENTRIES_PAGE, "Provides access to the admin entry page", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRIES_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ENTRIES_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_READ, newLookup(SecurityPermission.class, ADMIN_ENTRY_READ, "Allows admin to read entries", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_CREATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_CREATE, "Allows admin to create entries", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_CREATE)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_UPDATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_UPDATE, "Allows admin to update an existing entry", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_UPDATE)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_DELETE, newLookup(SecurityPermission.class, ADMIN_ENTRY_DELETE, "Allows admin to DELETE an existing entry", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_DELETE)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_TOGGLE_STATUS, newLookup(SecurityPermission.class, ADMIN_ENTRY_TOGGLE_STATUS, "Allows the admin to toggle the status of an entry", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TOGGLE_STATUS)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TOGGLE_STATUS)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_MERGE, newLookup(SecurityPermission.class, ADMIN_ENTRY_MERGE, "Allows the admin to merge two entries together", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_MERGE)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_MERGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_VERSION_DELETE, newLookup(SecurityPermission.class, ADMIN_ENTRY_VERSION_DELETE, "Allows the admin to delete the version", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_VERSION_DELETE)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_VERSION_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_VERSION_READ, newLookup(SecurityPermission.class, ADMIN_ENTRY_VERSION_READ, "Allows the admin to read the version", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_VERSION_READ)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_VERSION_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_VERSION_CREATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_VERSION_CREATE, "Allows the admin to create an entry version", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_VERSION_CREATE)).setPermissionPredecessor(ADMIN_ENTRY_CREATE);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_VERSION_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_VERSION_RESTORE, newLookup(SecurityPermission.class, ADMIN_ENTRY_VERSION_RESTORE, "Allows the admin to restore a version of an entry", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_VERSION_RESTORE)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_VERSION_RESTORE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_APPROVE, newLookup(SecurityPermission.class, ADMIN_ENTRY_APPROVE, "Allows an admin the ability to approve an entry", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_APPROVE)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_APPROVE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_CHANGETYPE, newLookup(SecurityPermission.class, ADMIN_ENTRY_CHANGETYPE, "Gives the admin the ability to change an entry's entry type", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_CHANGETYPE)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_CHANGETYPE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_ATTR_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_ATTR_MANAGEMENT, "Gives admin attribute management", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_ATTR_MANAGEMENT)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_ATTR_MANAGEMENT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_CONTACT_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_CONTACT_MANAGEMENT, "Gives an admin contact management permissions", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_CONTACT_MANAGEMENT)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_CONTACT_MANAGEMENT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_DEPENDENCY_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_DEPENDENCY_MANAGEMENT, "Gives an admin dependency management", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_DEPENDENCY_MANAGEMENT)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_DEPENDENCY_MANAGEMENT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_MEDIA_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_MEDIA_MANAGEMENT, "Gives an admin media management", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_MEDIA_MANAGEMENT)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_MEDIA_MANAGEMENT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_RELATIONSHIP_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_RELATIONSHIP_MANAGEMENT, "Gives an admin relationship management for entries", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_RELATIONSHIP_MANAGEMENT)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_RELATIONSHIP_MANAGEMENT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_RESOURCE_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_RESOURCE_MANAGEMENT, "Gives an admin resource management for entries", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_RESOURCE_MANAGEMENT)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_RESOURCE_MANAGEMENT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_EVALSECTION_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_EVALSECTION_MANAGEMENT, "Gives an admin the ability to manage eval sections", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_EVALSECTION_MANAGEMENT)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_EVALSECTION_MANAGEMENT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_TAG_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_TAG_MANAGEMENT, "Allows for tag management", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TAG_MANAGEMENT)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TAG_MANAGEMENT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_CHANGEREQUEST_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_CHANGEREQUEST_MANAGEMENT, "Allows for management change requests", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_CHANGEREQUEST_MANAGEMENT)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_CHANGEREQUEST_MANAGEMENT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_COMMENT_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_COMMENT_MANAGEMENT, "Allows for management entry comments", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_COMMENT_MANAGEMENT)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_COMMENT_MANAGEMENT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_CHANGEOWNER, newLookup(SecurityPermission.class, ADMIN_ENTRY_CHANGEOWNER, "Allows admin to change the owner of an entry", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_CHANGEOWNER)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_CHANGEOWNER)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_EXPORT, newLookup(SecurityPermission.class, ADMIN_ENTRY_EXPORT, "Allows admin to export an entry", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_EXPORT)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_EXPORT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_PENDINGCHANGE_READ, newLookup(SecurityPermission.class, ADMIN_ENTRY_PENDINGCHANGE_READ, "Allows admin to read pending changes", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_PENDINGCHANGE_READ)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_PENDINGCHANGE_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_ASSIGNUSER, newLookup(SecurityPermission.class, ADMIN_ENTRY_ASSIGNUSER, "Allows admin to assign libarian to a component", null, GROUPBY_ENTRY_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_ASSIGNUSER)).setPermissionPredecessor(ADMIN_ENTRY_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_ASSIGNUSER)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		// Entry Templates
		codeMap.put(ADMIN_ENTRYTEMPLATES_PAGE, newLookup(SecurityPermission.class, ADMIN_ENTRYTEMPLATES_PAGE, "Provides access to the entry templates page", null, GROUPBY_ENTRY_TEMPLATES));
		((SecurityPermission) codeMap.get(ADMIN_ENTRYTEMPLATES_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ENTRYTEMPLATES_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_TEMPLATES_READ, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES_READ, "Allows for the user to read entry templates", null, GROUPBY_ENTRY_TEMPLATES));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TEMPLATES_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TEMPLATES_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_TEMPLATES_CREATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES_CREATE, "Allows for the creation of entry templates", null, GROUPBY_ENTRY_TEMPLATES));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TEMPLATES_CREATE)).setPermissionPredecessor(ADMIN_ENTRY_TEMPLATES_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TEMPLATES_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_TEMPLATES_UPDATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES_UPDATE, "Allows the user to update entry templates", null, GROUPBY_ENTRY_TEMPLATES));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TEMPLATES_UPDATE)).setPermissionPredecessor(ADMIN_ENTRY_TEMPLATES_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TEMPLATES_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_TEMPLATES_DELETE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES_DELETE, "Allows the user to delete an entry template", null, GROUPBY_ENTRY_TEMPLATES));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TEMPLATES_DELETE)).setPermissionPredecessor(ADMIN_ENTRY_TEMPLATES_READ);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TEMPLATES_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		// Entry Types
		codeMap.put(ADMIN_ENTRYTYPE_PAGE, newLookup(SecurityPermission.class, ADMIN_ENTRYTYPE_PAGE, "Provides access to the admin entry types page", null, GROUPBY_ENTRY_TYPES));
		((SecurityPermission) codeMap.get(ADMIN_ENTRYTYPE_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ENTRYTYPE_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_TYPES_CREATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TYPES_CREATE, "Allows for the creation of entry types", null, GROUPBY_ENTRY_TYPES));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TYPES_CREATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TYPES_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_TYPES_UPDATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TYPES_UPDATE, "Allows for the ability to update entry types", null, GROUPBY_ENTRY_TYPES));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TYPES_UPDATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TYPES_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ENTRY_TYPES_DELETE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TYPES_DELETE, "Allow for deleting entry types", null, GROUPBY_ENTRY_TYPES));
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TYPES_DELETE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ENTRY_TYPES_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		//Evaluation Template
		codeMap.put(ADMIN_EVAL_TEMPLATES_PAGE, newLookup(SecurityPermission.class, ADMIN_EVAL_TEMPLATES_PAGE, "Provides access to the Evaluation Templates page", null, GROUPBY_EVALUATION_TEMPLATE));
		((SecurityPermission) codeMap.get(ADMIN_EVAL_TEMPLATES_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_EVAL_TEMPLATES_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_READ, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_READ, "Gives the ability to read evaluation templates", null, GROUPBY_EVALUATION_TEMPLATE));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CREATE, "Gives the ability to create evaluation templates", null, GROUPBY_EVALUATION_TEMPLATE));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CREATE)).setPermissionPredecessor(ADMIN_EVALUATION_TEMPLATE_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_UPDATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_UPDATE, "Gives the ability to update evaluation templates", null, GROUPBY_EVALUATION_TEMPLATE));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_UPDATE)).setPermissionPredecessor(ADMIN_EVALUATION_TEMPLATE_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_DELETE, "Gives the ability to delete evaluation templates", null, GROUPBY_EVALUATION_TEMPLATE));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_DELETE)).setPermissionPredecessor(ADMIN_EVALUATION_TEMPLATE_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		//Evaluation Template Checklist Questions
		codeMap.put(ADMIN_EVAL_CHECKLIST_QUESTIONS_PAGE, newLookup(SecurityPermission.class, ADMIN_EVAL_CHECKLIST_QUESTIONS_PAGE, "Provides access to the Evaluation Checklist Questions page", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST_QUESTION));
		((SecurityPermission) codeMap.get(ADMIN_EVAL_CHECKLIST_QUESTIONS_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_EVAL_CHECKLIST_QUESTIONS_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ, "Provides the ability to read eval template checklist question items", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST_QUESTION));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE, SecurityRole.EVALUATOR_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_CREATE, "Provides the ability to create eval template checklist questions", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST_QUESTION));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_CREATE)).setPermissionPredecessor(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_UPDATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_UPDATE, "Provides the ability to update eval template checklist questions", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST_QUESTION));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_UPDATE)).setPermissionPredecessor(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_DELETE, "Provides the ability to delete eval template checklist question items", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST_QUESTION));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_DELETE)).setPermissionPredecessor(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		//Evaluation Template Checklist
		codeMap.put(ADMIN_EVAL_CHECKLIST_TEMPLATES_PAGE, newLookup(SecurityPermission.class, ADMIN_EVAL_CHECKLIST_TEMPLATES_PAGE, "Provides access to the Evaluation Checklist Templates page", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST));
		((SecurityPermission) codeMap.get(ADMIN_EVAL_CHECKLIST_TEMPLATES_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_EVAL_CHECKLIST_TEMPLATES_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ, "Gives the ability to read eval template checklist items", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_CREATE, "Gives the ability to create eval template checklist items", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_CREATE)).setPermissionPredecessor(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_UPDATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_UPDATE, "Gives the ability to update eval template checklist items", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_UPDATE)).setPermissionPredecessor(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_DELETE, "Gives the ability to delete a eval template checklist item", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_DELETE)).setPermissionPredecessor(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		//Evaluation Template Section
		codeMap.put(ADMIN_EVAL_SECTION_PAGE, newLookup(SecurityPermission.class, ADMIN_EVAL_SECTION_PAGE, "Provides access to the Evaluation Section page", null, GROUPBY_EVALUATION_TEMPLATE_SECTION));
		((SecurityPermission) codeMap.get(ADMIN_EVAL_SECTION_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_EVAL_SECTION_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION_READ, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION_READ, "Allows for reading eval template sections", null, GROUPBY_EVALUATION_TEMPLATE_SECTION));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_SECTION_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_SECTION_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION_CREATE, "Allows for creation eval template sections", null, GROUPBY_EVALUATION_TEMPLATE_SECTION));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_SECTION_CREATE)).setPermissionPredecessor(ADMIN_EVALUATION_TEMPLATE_SECTION_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_SECTION_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION_UPDATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION_UPDATE, "Allows for update eval template sections", null, GROUPBY_EVALUATION_TEMPLATE_SECTION));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_SECTION_UPDATE)).setPermissionPredecessor(ADMIN_EVALUATION_TEMPLATE_SECTION_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_SECTION_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION_DELETE, "Allows for deleting eval template sections", null, GROUPBY_EVALUATION_TEMPLATE_SECTION));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_SECTION_DELETE)).setPermissionPredecessor(ADMIN_EVALUATION_TEMPLATE_SECTION_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TEMPLATE_SECTION_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		//Evaluation Management (Admin)
		codeMap.put(ADMIN_EVAL_PAGE, newLookup(SecurityPermission.class, ADMIN_EVAL_PAGE, "Provides access to the admin evaluator management page", null, GROUPBY_EVALUATION_ADMIN));
		((SecurityPermission) codeMap.get(ADMIN_EVAL_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_EVAL_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_MANAGEMENT_CREATE, "Allows the user to create an evaluation", null, GROUPBY_EVALUATION_ADMIN));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_MANAGEMENT_CREATE)).setPermissionPredecessor(USER_EVALUATIONS_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_MANAGEMENT_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_MANAGEMENT_DELETE, "Allows the user to delete an evaluation", null, GROUPBY_EVALUATION_ADMIN));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_MANAGEMENT_DELETE)).setPermissionPredecessor(USER_EVALUATIONS_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_MANAGEMENT_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_ACTIVATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_ACTIVATE, "Allows the user to activate an evaluation", null, GROUPBY_EVALUATION_ADMIN));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_ACTIVATE)).setPermissionPredecessor(USER_EVALUATIONS_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_ACTIVATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_ALLOW_NEW_SECTIONS, newLookup(SecurityPermission.class, ADMIN_EVALUATION_ALLOW_NEW_SECTIONS, "Allows the user to specify whether the eval should allow new sections", null, GROUPBY_EVALUATION_ADMIN));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_ALLOW_NEW_SECTIONS)).setPermissionPredecessor(USER_EVALUATIONS_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_ALLOW_NEW_SECTIONS)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_ALLOW_QUESTION_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_EVALUATION_ALLOW_QUESTION_MANAGEMENT, "Allows the user to specify whether the eval should allow question management", null, GROUPBY_EVALUATION_ADMIN));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_ALLOW_QUESTION_MANAGEMENT)).setPermissionPredecessor(USER_EVALUATIONS_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_ALLOW_QUESTION_MANAGEMENT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_TOGGLE_PUBLISH, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TOGGLE_PUBLISH, "Allows the user to publish an evaluation", null, GROUPBY_EVALUATION_ADMIN));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TOGGLE_PUBLISH)).setPermissionPredecessor(USER_EVALUATIONS_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_TOGGLE_PUBLISH)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_PUBLISH_SUMMARY, newLookup(SecurityPermission.class, ADMIN_EVALUATION_PUBLISH_SUMMARY, "Allows the user to publish just the summary under the view menu", null, GROUPBY_EVALUATION_ADMIN));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_PUBLISH_SUMMARY)).setPermissionPredecessor(USER_EVALUATIONS_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_PUBLISH_SUMMARY)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_EVALUATION_DELETE_COMMENT, newLookup(SecurityPermission.class, ADMIN_EVALUATION_DELETE_COMMENT, "Allows the user to delete a comment on said evaluation", null, GROUPBY_EVALUATION_ADMIN));
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_DELETE_COMMENT)).setPermissionPredecessor(USER_EVALUATIONS_READ);
		((SecurityPermission) codeMap.get(ADMIN_EVALUATION_DELETE_COMMENT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		//Evaluation Management (User)
		codeMap.put(EVAL_PAGE, newLookup(SecurityPermission.class, EVAL_PAGE, "Provides access to the Evaluator Management page", null, GROUPBY_EVALUATION_USER));
		((SecurityPermission) codeMap.get(EVAL_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(EVAL_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.EVALUATOR_ROLE, SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(USER_EVALUATIONS_READ, newLookup(SecurityPermission.class, USER_EVALUATIONS_READ, "Allows an evaluator to 'view' an evaluation", null, GROUPBY_EVALUATION_USER));
		((SecurityPermission) codeMap.get(USER_EVALUATIONS_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(USER_EVALUATIONS_READ)).setDefaultRoles(Arrays.asList(SecurityRole.EVALUATOR_ROLE, SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(USER_EVALUATIONS_UPDATE, newLookup(SecurityPermission.class, USER_EVALUATIONS_UPDATE, "Allows an evaluator  'edit' an evaluation", null, GROUPBY_EVALUATION_USER));
		((SecurityPermission) codeMap.get(USER_EVALUATIONS_UPDATE)).setPermissionPredecessor(USER_EVALUATIONS_READ);
		((SecurityPermission) codeMap.get(USER_EVALUATIONS_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.EVALUATOR_ROLE, SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(USER_EVALUATIONS_ASSIGN_USER, newLookup(SecurityPermission.class, USER_EVALUATIONS_ASSIGN_USER, "Allows an evaluator to assign a user to an evaluation (ONLY HIDES BUTTON)", null, GROUPBY_EVALUATION_USER));
		((SecurityPermission) codeMap.get(USER_EVALUATIONS_ASSIGN_USER)).setPermissionPredecessor(USER_EVALUATIONS_READ);
		((SecurityPermission) codeMap.get(USER_EVALUATIONS_ASSIGN_USER)).setDefaultRoles(Arrays.asList(SecurityRole.EVALUATOR_ROLE, SecurityRole.LIBRARIAN_ROLE));

		//FAQ
		codeMap.put(ADMIN_FAQ_PAGE, newLookup(SecurityPermission.class, ADMIN_FAQ_PAGE, "Provides access to the admin faq page", null, GROUPBY_FAQ));
		((SecurityPermission) codeMap.get(ADMIN_FAQ_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_FAQ_PAGE)).setDefaultRoles(null);

		codeMap.put(ADMIN_FAQ_READ, newLookup(SecurityPermission.class, ADMIN_FAQ_READ, "Allows for reading FAQs", null, GROUPBY_FAQ));
		((SecurityPermission) codeMap.get(ADMIN_FAQ_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_FAQ_READ)).setDefaultRoles(null);

		codeMap.put(ADMIN_FAQ_CREATE, newLookup(SecurityPermission.class, ADMIN_FAQ_CREATE, "Gives ability to creater FAQs", null, GROUPBY_FAQ));
		((SecurityPermission) codeMap.get(ADMIN_FAQ_CREATE)).setPermissionPredecessor(ADMIN_FAQ_READ);
		((SecurityPermission) codeMap.get(ADMIN_FAQ_CREATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_FAQ_UPDATE, newLookup(SecurityPermission.class, ADMIN_FAQ_UPDATE, "Allows for update FAQs", null, GROUPBY_FAQ));
		((SecurityPermission) codeMap.get(ADMIN_FAQ_UPDATE)).setPermissionPredecessor(ADMIN_FAQ_READ);
		((SecurityPermission) codeMap.get(ADMIN_FAQ_UPDATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_FAQ_DELETE, newLookup(SecurityPermission.class, ADMIN_FAQ_DELETE, "Allows for deleting FAQs", null, GROUPBY_FAQ));
		((SecurityPermission) codeMap.get(ADMIN_FAQ_DELETE)).setPermissionPredecessor(ADMIN_FAQ_READ);
		((SecurityPermission) codeMap.get(ADMIN_FAQ_DELETE)).setDefaultRoles(null);

		// Feedback
		codeMap.put(ADMIN_FEEDBACK_PAGE, newLookup(SecurityPermission.class, ADMIN_FEEDBACK_PAGE, "Provides access to the admin Feedback page", null, GROUPBY_FEEDBACK));
		((SecurityPermission) codeMap.get(ADMIN_FEEDBACK_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_FEEDBACK_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_FEEDBACK_READ, newLookup(SecurityPermission.class, ADMIN_FEEDBACK_READ, "allows for reading feedback", null, GROUPBY_FEEDBACK));
		((SecurityPermission) codeMap.get(ADMIN_FEEDBACK_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_FEEDBACK_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_FEEDBACK_UPDATE, newLookup(SecurityPermission.class, ADMIN_FEEDBACK_UPDATE, "allows for modifying feedback", null, GROUPBY_FEEDBACK));
		((SecurityPermission) codeMap.get(ADMIN_FEEDBACK_UPDATE)).setPermissionPredecessor(ADMIN_FEEDBACK_READ);
		((SecurityPermission) codeMap.get(ADMIN_FEEDBACK_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_FEEDBACK_DELETE, newLookup(SecurityPermission.class, ADMIN_FEEDBACK_DELETE, "allows for removing feedback", null, GROUPBY_FEEDBACK));
		((SecurityPermission) codeMap.get(ADMIN_FEEDBACK_DELETE)).setPermissionPredecessor(ADMIN_FEEDBACK_READ);
		((SecurityPermission) codeMap.get(ADMIN_FEEDBACK_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		// Highlights
		codeMap.put(ADMIN_HIGHLIGHTS_PAGE, newLookup(SecurityPermission.class, ADMIN_HIGHLIGHTS_PAGE, "Provides access to the Highlights page", null, GROUPBY_HIGHLIGHT));
		((SecurityPermission) codeMap.get(ADMIN_HIGHLIGHTS_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_HIGHLIGHTS_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_HIGHLIGHTS_CREATE, newLookup(SecurityPermission.class, ADMIN_HIGHLIGHTS_CREATE, "allows for creating highlights", null, GROUPBY_HIGHLIGHT));
		((SecurityPermission) codeMap.get(ADMIN_HIGHLIGHTS_CREATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_HIGHLIGHTS_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_HIGHLIGHTS_UPDATE, newLookup(SecurityPermission.class, ADMIN_HIGHLIGHTS_UPDATE, "allows for update highlights", null, GROUPBY_HIGHLIGHT));
		((SecurityPermission) codeMap.get(ADMIN_HIGHLIGHTS_UPDATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_HIGHLIGHTS_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_HIGHLIGHTS_DELETE, newLookup(SecurityPermission.class, ADMIN_HIGHLIGHTS_DELETE, "allows for deleting highlights", null, GROUPBY_HIGHLIGHT));
		((SecurityPermission) codeMap.get(ADMIN_HIGHLIGHTS_DELETE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_HIGHLIGHTS_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		//Import Export
		codeMap.put(ADMIN_IMPORT_PAGE, newLookup(SecurityPermission.class, ADMIN_IMPORT_PAGE, "Provides access to the admin Import page", null, GROUPBY_IMPORTEXPORT));
		((SecurityPermission) codeMap.get(ADMIN_IMPORT_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_IMPORT_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_DATA_IMPORT_EXPORT, newLookup(SecurityPermission.class, ADMIN_DATA_IMPORT_EXPORT, "export import", null, GROUPBY_IMPORTEXPORT));
		((SecurityPermission) codeMap.get(ADMIN_DATA_IMPORT_EXPORT)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_DATA_IMPORT_EXPORT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		//Integrations
		codeMap.put(ADMIN_INTEGRATION_PAGE, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_PAGE, "Provides access to the Integration page", null, GROUPBY_INTEGRATION));
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_INTEGRATION_READ, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_READ, "Allows for reading integration", null, GROUPBY_INTEGRATION));
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_INTEGRATION_CREATE, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_CREATE, "create integration", null, GROUPBY_INTEGRATION));
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_CREATE)).setPermissionPredecessor(ADMIN_INTEGRATION_READ);
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_INTEGRATION_UPDATE, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_UPDATE, "Allows for updating integration", null, GROUPBY_INTEGRATION));
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_UPDATE)).setPermissionPredecessor(ADMIN_INTEGRATION_READ);
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_INTEGRATION_DELETE, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_DELETE, "Allows for the removal of integration", null, GROUPBY_INTEGRATION));
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_DELETE)).setPermissionPredecessor(ADMIN_INTEGRATION_READ);
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_INTEGRATION_RUNALL, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_RUNALL, "allows for running all integrations", null, GROUPBY_INTEGRATION));
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_RUNALL)).setPermissionPredecessor(ADMIN_INTEGRATION_READ);
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_RUNALL)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_INTEGRATION_RUNCONFIG, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_RUNCONFIG, "allows for running config", null, GROUPBY_INTEGRATION));
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_RUNCONFIG)).setPermissionPredecessor(ADMIN_INTEGRATION_READ);
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_RUNCONFIG)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_INTEGRATION_RUNINTEGRATION, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_RUNINTEGRATION, "allows for running one integration", null, GROUPBY_INTEGRATION));
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_RUNINTEGRATION)).setPermissionPredecessor(ADMIN_INTEGRATION_READ);
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_RUNINTEGRATION)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_INTEGRATION_TOGGLESTATUS, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_TOGGLESTATUS, "allows for the ability to toggle the status of an integration", null, GROUPBY_INTEGRATION));
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_TOGGLESTATUS)).setPermissionPredecessor(ADMIN_INTEGRATION_READ);
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_TOGGLESTATUS)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_INTEGRATION_EXTERNAL, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_EXTERNAL, "Allows ability to integration abilities with external sources", null, GROUPBY_INTEGRATION));
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_EXTERNAL)).setPermissionPredecessor(ADMIN_INTEGRATION_READ);
		((SecurityPermission) codeMap.get(ADMIN_INTEGRATION_EXTERNAL)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		// Job Management
		codeMap.put(ADMIN_JOBS_PAGE, newLookup(SecurityPermission.class, ADMIN_JOBS_PAGE, "Provides access to the admin Jobs page", null, GROUPBY_JOB_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_JOBS_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_JOBS_PAGE)).setDefaultRoles(null);

		codeMap.put(ADMIN_JOB_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_JOB_MANAGEMENT_READ, "Allows user to read jobs", null, GROUPBY_JOB_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_JOB_MANAGEMENT_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_JOB_MANAGEMENT_READ)).setDefaultRoles(null);

		codeMap.put(ADMIN_JOB_MANAGEMENT_ACTION, newLookup(SecurityPermission.class, ADMIN_JOB_MANAGEMENT_ACTION, "Allows user to perform some action with a job", null, GROUPBY_JOB_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_JOB_MANAGEMENT_ACTION)).setPermissionPredecessor(ADMIN_JOB_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_JOB_MANAGEMENT_ACTION)).setDefaultRoles(null);

		codeMap.put(ADMIN_JOB_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_JOB_MANAGEMENT_DELETE, "Allows user to remove a job", null, GROUPBY_JOB_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_JOB_MANAGEMENT_DELETE)).setPermissionPredecessor(ADMIN_JOB_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_JOB_MANAGEMENT_DELETE)).setDefaultRoles(null);

		// Lookups
		codeMap.put(ADMIN_LOOKUPS_PAGE, newLookup(SecurityPermission.class, ADMIN_LOOKUPS_PAGE, "Provides access to the Lookups page", null, GROUPBY_LOOKUP));
		((SecurityPermission) codeMap.get(ADMIN_LOOKUPS_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_LOOKUPS_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_LOOKUPS_READ, newLookup(SecurityPermission.class, ADMIN_LOOKUPS_READ, "Provides the ability to read lookups", null, GROUPBY_LOOKUP));
		((SecurityPermission) codeMap.get(ADMIN_LOOKUPS_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_LOOKUPS_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_LOOKUPS_CREATE_CODE, newLookup(SecurityPermission.class, ADMIN_LOOKUPS_CREATE_CODE, "Provides the ability to create lookups", null, GROUPBY_LOOKUP));
		((SecurityPermission) codeMap.get(ADMIN_LOOKUPS_CREATE_CODE)).setPermissionPredecessor(ADMIN_LOOKUPS_READ);
		((SecurityPermission) codeMap.get(ADMIN_LOOKUPS_CREATE_CODE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_LOOKUPS_UPDATE_CODE, newLookup(SecurityPermission.class, ADMIN_LOOKUPS_UPDATE_CODE, "Provides the ability to update a lookup(s)", null, GROUPBY_LOOKUP));
		((SecurityPermission) codeMap.get(ADMIN_LOOKUPS_UPDATE_CODE)).setPermissionPredecessor(ADMIN_LOOKUPS_READ);
		((SecurityPermission) codeMap.get(ADMIN_LOOKUPS_UPDATE_CODE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_LOOKUPS_DELETE_CODE, newLookup(SecurityPermission.class, ADMIN_LOOKUPS_DELETE_CODE, "Provides the ability to delete lookups", null, GROUPBY_LOOKUP));
		((SecurityPermission) codeMap.get(ADMIN_LOOKUPS_DELETE_CODE)).setPermissionPredecessor(ADMIN_LOOKUPS_READ);
		((SecurityPermission) codeMap.get(ADMIN_LOOKUPS_DELETE_CODE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		//Media
		codeMap.put(ADMIN_MEDIA_PAGE, newLookup(SecurityPermission.class, ADMIN_MEDIA_PAGE, "Provides access to the admin Media page", null, GROUPBY_MEDIA));
		((SecurityPermission) codeMap.get(ADMIN_MEDIA_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_MEDIA_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_SUPPORTMEDIA_PAGE, newLookup(SecurityPermission.class, ADMIN_SUPPORTMEDIA_PAGE, "Provides access to the Support Media page", null, GROUPBY_MEDIA));
		((SecurityPermission) codeMap.get(ADMIN_SUPPORTMEDIA_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SUPPORTMEDIA_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_MEDIA_DELETE, newLookup(SecurityPermission.class, ADMIN_MEDIA_DELETE, "Allows for deleting media", null, GROUPBY_MEDIA));
		((SecurityPermission) codeMap.get(ADMIN_MEDIA_DELETE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_MEDIA_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_MEDIA_UPLOAD, newLookup(SecurityPermission.class, ADMIN_MEDIA_UPLOAD, "Allows for uploading media as an admin", null, GROUPBY_MEDIA));
		((SecurityPermission) codeMap.get(ADMIN_MEDIA_UPLOAD)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_MEDIA_UPLOAD)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_TEMPMEDIA_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_TEMPMEDIA_MANAGEMENT_READ, "Provides the ability to read temp media", null, GROUPBY_MEDIA));
		((SecurityPermission) codeMap.get(ADMIN_TEMPMEDIA_MANAGEMENT_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_TEMPMEDIA_MANAGEMENT_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_TEMPMEDIA_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_TEMPMEDIA_MANAGEMENT_DELETE, "Provides the ability to delete temp media", null, GROUPBY_MEDIA));
		((SecurityPermission) codeMap.get(ADMIN_TEMPMEDIA_MANAGEMENT_DELETE)).setPermissionPredecessor(ADMIN_TEMPMEDIA_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_TEMPMEDIA_MANAGEMENT_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_SUPPORT_MEDIA_CREATE, newLookup(SecurityPermission.class, ADMIN_SUPPORT_MEDIA_CREATE, "Allows for the uploading of support medias", null, GROUPBY_MEDIA));
		((SecurityPermission) codeMap.get(ADMIN_SUPPORT_MEDIA_CREATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SUPPORT_MEDIA_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_SUPPORT_MEDIA_UPDATE, newLookup(SecurityPermission.class, ADMIN_SUPPORT_MEDIA_UPDATE, "Allows for updating support medias", null, GROUPBY_MEDIA));
		((SecurityPermission) codeMap.get(ADMIN_SUPPORT_MEDIA_UPDATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SUPPORT_MEDIA_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_SUPPORT_MEDIA_DELETE, newLookup(SecurityPermission.class, ADMIN_SUPPORT_MEDIA_DELETE, "Allows for the deletion of support medias", null, GROUPBY_MEDIA));
		((SecurityPermission) codeMap.get(ADMIN_SUPPORT_MEDIA_DELETE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SUPPORT_MEDIA_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		// Message Management
		codeMap.put(ADMIN_MESSAGES_PAGE, newLookup(SecurityPermission.class, ADMIN_MESSAGES_PAGE, "Provides access to the admin Messages/Notification page", null, GROUPBY_MESSAGE_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_MESSAGES_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_MESSAGES_PAGE)).setDefaultRoles(null);

		codeMap.put(ADMIN_MESSAGE_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT_READ, "Allows for the ability to read notification data", null, GROUPBY_MESSAGE_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_MESSAGE_MANAGEMENT_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_MESSAGE_MANAGEMENT_READ)).setDefaultRoles(null);

		codeMap.put(ADMIN_MESSAGE_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT_CREATE, "Allows for the ability to post a new notification event", null, GROUPBY_MESSAGE_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_MESSAGE_MANAGEMENT_CREATE)).setPermissionPredecessor(ADMIN_MESSAGE_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_MESSAGE_MANAGEMENT_CREATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_MESSAGE_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT_UPDATE, "Allow for updating a notification event", null, GROUPBY_MESSAGE_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_MESSAGE_MANAGEMENT_UPDATE)).setPermissionPredecessor(ADMIN_MESSAGE_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_MESSAGE_MANAGEMENT_UPDATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_MESSAGE_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT_DELETE, "Allows for deleting a notification event", null, GROUPBY_MESSAGE_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_MESSAGE_MANAGEMENT_DELETE)).setPermissionPredecessor(ADMIN_MESSAGE_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_MESSAGE_MANAGEMENT_DELETE)).setDefaultRoles(null);

		// Notification Events
		codeMap.put(ADMIN_NOTIFICATION_EVENT_CREATE, newLookup(SecurityPermission.class, ADMIN_NOTIFICATION_EVENT_CREATE, "Allows for the ability to post a new notification event", null, GROUPBY_NOTIFICATION_EVENT_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_NOTIFICATION_EVENT_CREATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_NOTIFICATION_EVENT_CREATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_NOTIFICATION_EVENT_DELETE, newLookup(SecurityPermission.class, ADMIN_NOTIFICATION_EVENT_DELETE, "Allows for deleting a notification event", null, GROUPBY_NOTIFICATION_EVENT_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_NOTIFICATION_EVENT_DELETE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_NOTIFICATION_EVENT_DELETE)).setDefaultRoles(null);

		// Organization
		codeMap.put(ADMIN_ORGANIZATION_PAGE, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_PAGE, "Provides access to the admin Organization page", null, GROUPBY_ORGANIZATION));
		((SecurityPermission) codeMap.get(ADMIN_ORGANIZATION_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ORGANIZATION_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ORGANIZATION_CREATE, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_CREATE, "Allows for creating orgs", null, GROUPBY_ORGANIZATION));
		((SecurityPermission) codeMap.get(ADMIN_ORGANIZATION_CREATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ORGANIZATION_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ORGANIZATION_UPDATE, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_UPDATE, "Allows for update orgs", null, GROUPBY_ORGANIZATION));
		((SecurityPermission) codeMap.get(ADMIN_ORGANIZATION_UPDATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ORGANIZATION_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ORGANIZATION_DELETE, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_DELETE, "Allows for deleting organizations", null, GROUPBY_ORGANIZATION));
		((SecurityPermission) codeMap.get(ADMIN_ORGANIZATION_DELETE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ORGANIZATION_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ORGANIZATION_MERGE, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_MERGE, "Allows for merging two organizations", null, GROUPBY_ORGANIZATION));
		((SecurityPermission) codeMap.get(ADMIN_ORGANIZATION_MERGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ORGANIZATION_MERGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_ORGANIZATION_EXTRACTION, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_EXTRACTION, "Allows for organization extraction", null, GROUPBY_ORGANIZATION));
		((SecurityPermission) codeMap.get(ADMIN_ORGANIZATION_EXTRACTION)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ORGANIZATION_EXTRACTION)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		// Profile Management
		codeMap.put(USER_PROFILE_PAGE, newLookup(SecurityPermission.class, USER_PROFILE_PAGE, "Provides access to the Profile page", null, GROUPBY_PROFILE_MANAGEMENT));
		((SecurityPermission) codeMap.get(USER_PROFILE_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(USER_PROFILE_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(ADMIN_USERPROFILES_PAGE, newLookup(SecurityPermission.class, ADMIN_USERPROFILES_PAGE, "Provides access to the admin User Profiles page", null, GROUPBY_PROFILE_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_USERPROFILES_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_USERPROFILES_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_USER_MANAGEMENT_PROFILES_READ, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_PROFILES_READ, "Allow for the reading of user profiles", null, GROUPBY_PROFILE_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_PROFILES_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_PROFILES_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_USER_MANAGEMENT_PROFILES_UPDATE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_PROFILES_UPDATE, "Allows for the ability to update a user profile", null, GROUPBY_PROFILE_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_PROFILES_UPDATE)).setPermissionPredecessor(ADMIN_USER_MANAGEMENT_PROFILES_READ);
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_PROFILES_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_USER_MANAGEMENT_PROFILES_DELETE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_PROFILES_DELETE, "Allows for the ability to delete a user profile", null, GROUPBY_PROFILE_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_PROFILES_DELETE)).setPermissionPredecessor(ADMIN_USER_MANAGEMENT_PROFILES_READ);
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_PROFILES_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		// Questions
		codeMap.put(USER_QUESTIONS_PAGE, newLookup(SecurityPermission.class, USER_QUESTIONS_PAGE, "Provides access to the Questions page", null, GROUPBY_QUESTION));
		((SecurityPermission) codeMap.get(USER_QUESTIONS_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(USER_QUESTIONS_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(ADMIN_QUESTION_PAGE, newLookup(SecurityPermission.class, ADMIN_QUESTION_PAGE, "Provides access to the admin Questions page", null, GROUPBY_QUESTION));
		((SecurityPermission) codeMap.get(ADMIN_QUESTION_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_QUESTION_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_QUESTIONS_READ, newLookup(SecurityPermission.class, ADMIN_QUESTIONS_READ, "Gives admin ability to read questions on entries", null, GROUPBY_QUESTION));
		((SecurityPermission) codeMap.get(ADMIN_QUESTIONS_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_QUESTIONS_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_QUESTIONS_UPDATE, newLookup(SecurityPermission.class, ADMIN_QUESTIONS_UPDATE, "Gives admin ability to update questions on entries", null, GROUPBY_QUESTION));
		((SecurityPermission) codeMap.get(ADMIN_QUESTIONS_UPDATE)).setPermissionPredecessor(ADMIN_QUESTIONS_READ);
		((SecurityPermission) codeMap.get(ADMIN_QUESTIONS_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_QUESTIONS_DELETE, newLookup(SecurityPermission.class, ADMIN_QUESTIONS_DELETE, "Gives admin ability to delete questions on entries", null, GROUPBY_QUESTION));
		((SecurityPermission) codeMap.get(ADMIN_QUESTIONS_DELETE)).setPermissionPredecessor(ADMIN_QUESTIONS_READ);
		((SecurityPermission) codeMap.get(ADMIN_QUESTIONS_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		// Relationships
		codeMap.put(ADMIN_RELATIONSHIPS_PAGE, newLookup(SecurityPermission.class, ADMIN_RELATIONSHIPS_PAGE, "Provides access to the admin Relationships page", null, GROUPBY_RELATIONSHIPS));
		((SecurityPermission) codeMap.get(ADMIN_RELATIONSHIPS_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_RELATIONSHIPS_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(USER_RELATIONSHIPS_PAGE, newLookup(SecurityPermission.class, USER_RELATIONSHIPS_PAGE, "Provides access to the Relationships page", null, GROUPBY_RELATIONSHIPS));
		((SecurityPermission) codeMap.get(USER_RELATIONSHIPS_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(USER_RELATIONSHIPS_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		// Reports
		codeMap.put(REPORTS_SCHEDULE_CREATE, newLookup(SecurityPermission.class, REPORTS_SCHEDULE_CREATE, "Allows reading of scheduled reports", null, GROUPBY_REPORTS_SCHEDULE));
		((SecurityPermission) codeMap.get(REPORTS_SCHEDULE_CREATE)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(REPORTS_SCHEDULE_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(REPORTS_SCHEDULE_READ, newLookup(SecurityPermission.class, REPORTS_SCHEDULE_READ, "Allows reading of scheduled reports", null, GROUPBY_REPORTS_SCHEDULE));
		((SecurityPermission) codeMap.get(REPORTS_SCHEDULE_READ)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(REPORTS_SCHEDULE_READ)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(REPORTS_SCHEDULE_UPDATE, newLookup(SecurityPermission.class, REPORTS_SCHEDULE_UPDATE, "Allows editing of scheduled reports", null, GROUPBY_REPORTS_SCHEDULE));
		((SecurityPermission) codeMap.get(REPORTS_SCHEDULE_UPDATE)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(REPORTS_SCHEDULE_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(REPORTS_SCHEDULE_DELETE, newLookup(SecurityPermission.class, REPORTS_SCHEDULE_DELETE, "Allows editing of scheduled reports", null, GROUPBY_REPORTS_SCHEDULE));
		((SecurityPermission) codeMap.get(REPORTS_SCHEDULE_DELETE)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(REPORTS_SCHEDULE_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		// codeMap.put(REPORTS, newLookup(SecurityPermission.class, REPORTS, "General Reports", null, GROUPBY_REPORTS));
		// ((SecurityPermission) codeMap.get(REPORTS)).setPermissionPredecessor(null);
		// ((SecurityPermission) codeMap.get(REPORTS)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(REPORTS_READ, newLookup(SecurityPermission.class, REPORTS_READ, "General Reports", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(REPORTS_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(REPORTS_READ)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));


		codeMap.put(REPORTS_PAGE, newLookup(SecurityPermission.class, REPORTS_PAGE, "Provides access to the Reports page", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(REPORTS_PAGE)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(REPORTS_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(REPORTS_CREATE, newLookup(SecurityPermission.class, REPORTS_CREATE, "Allows to create a new report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(REPORTS_CREATE)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(REPORTS_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(REPORTS_DELETE, newLookup(SecurityPermission.class, REPORTS_DELETE, "Allows to delete a report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(REPORTS_DELETE)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(REPORTS_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(REPORTS_ALL, newLookup(SecurityPermission.class, REPORTS_ALL, "Reports All - Allows viewing reports from all users", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(REPORTS_ALL)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(REPORTS_ALL)).setDefaultRoles(null);

		codeMap.put(REPORT_OUTPUT_EMAIL_ATTACH, newLookup(SecurityPermission.class, REPORT_OUTPUT_EMAIL_ATTACH, "Allow user to run report as attached email", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(REPORT_OUTPUT_EMAIL_ATTACH)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(REPORT_OUTPUT_EMAIL_ATTACH)).setDefaultRoles(null);

		codeMap.put(REPORT_OUTPUT_EMAIL_BODY, newLookup(SecurityPermission.class, REPORT_OUTPUT_EMAIL_BODY, "Allow user to run report as the content of an email body", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(REPORT_OUTPUT_EMAIL_BODY)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(REPORT_OUTPUT_EMAIL_BODY)).setDefaultRoles(null);

		// Reports - run reports
		codeMap.put(RUN_ACTION_REPORT, newLookup(SecurityPermission.class, RUN_ACTION_REPORT, "Allows user to run the Action report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(RUN_ACTION_REPORT)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(RUN_ACTION_REPORT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(RUN_ENTRIES_BY_CAT_REPORT, newLookup(SecurityPermission.class, RUN_ENTRIES_BY_CAT_REPORT, "Allows user to run the Entries by Category report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(RUN_ENTRIES_BY_CAT_REPORT)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(RUN_ENTRIES_BY_CAT_REPORT)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(RUN_ENTRIES_BY_ORG_REPORT, newLookup(SecurityPermission.class, RUN_ENTRIES_BY_ORG_REPORT, "Allows user to run the Entries by Organization report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(RUN_ENTRIES_BY_ORG_REPORT)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(RUN_ENTRIES_BY_ORG_REPORT)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(RUN_ENTRY_REPORT, newLookup(SecurityPermission.class, RUN_ENTRY_REPORT, "Allows user to run the Entry report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(RUN_ENTRY_REPORT)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(RUN_ENTRY_REPORT)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(RUN_ENTRY_DETAIL_REPORT, newLookup(SecurityPermission.class, RUN_ENTRY_DETAIL_REPORT, "Allows user to run the Entry Detail report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(RUN_ENTRY_DETAIL_REPORT)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(RUN_ENTRY_DETAIL_REPORT)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(RUN_ENTRY_LISTING_REPORT, newLookup(SecurityPermission.class, RUN_ENTRY_LISTING_REPORT, "Allows user to run the Entry Listing report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(RUN_ENTRY_LISTING_REPORT)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(RUN_ENTRY_LISTING_REPORT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(RUN_ENTRY_STATUS_REPORT, newLookup(SecurityPermission.class, RUN_ENTRY_STATUS_REPORT, "Allows user to run the Entry Status report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(RUN_ENTRY_STATUS_REPORT)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(RUN_ENTRY_STATUS_REPORT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(RUN_EVAL_STATUS_REPORT, newLookup(SecurityPermission.class, RUN_EVAL_STATUS_REPORT, "Allows user to run the Evaluation Status report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(RUN_EVAL_STATUS_REPORT)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(RUN_EVAL_STATUS_REPORT)).setDefaultRoles(Arrays.asList(SecurityRole.EVALUATOR_ROLE, SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(RUN_LINK_VALIDATION_REPORT, newLookup(SecurityPermission.class, RUN_LINK_VALIDATION_REPORT, "Allows user to run the Link Validation report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(RUN_LINK_VALIDATION_REPORT)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(RUN_LINK_VALIDATION_REPORT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(RUN_SUBMISSIONS_REPORT, newLookup(SecurityPermission.class, RUN_SUBMISSIONS_REPORT, "Allows user to run the Sumbissions report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(RUN_SUBMISSIONS_REPORT)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(RUN_SUBMISSIONS_REPORT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(RUN_USAGE_REPORT, newLookup(SecurityPermission.class, RUN_USAGE_REPORT, "Allows user to run the Usage report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(RUN_USAGE_REPORT)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(RUN_USAGE_REPORT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(RUN_USER_REPORT, newLookup(SecurityPermission.class, RUN_USER_REPORT, "Allows user to run the User report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(RUN_USER_REPORT)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(RUN_USER_REPORT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(RUN_USER_ORG_REPORT, newLookup(SecurityPermission.class, RUN_USER_ORG_REPORT, "Allows user to run the User by Organization report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(RUN_USER_ORG_REPORT)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(RUN_USER_ORG_REPORT)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(RUN_WORKPLAN_STATUS, newLookup(SecurityPermission.class, RUN_WORKPLAN_STATUS, "Allows user to run the Workplan Status report", null, GROUPBY_REPORTS));
		((SecurityPermission) codeMap.get(RUN_WORKPLAN_STATUS)).setPermissionPredecessor(REPORTS_READ);
		((SecurityPermission) codeMap.get(RUN_WORKPLAN_STATUS)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		// Reviews
		codeMap.put(USER_REVIEW_PAGE, newLookup(SecurityPermission.class, USER_REVIEW_PAGE, "Provides access to the Reviews page", null, GROUPBY_REVIEW));
		((SecurityPermission) codeMap.get(USER_REVIEW_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(USER_REVIEW_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(ADMIN_REVIEW_PAGE, newLookup(SecurityPermission.class, ADMIN_REVIEW_PAGE, "Provides access to the admin Reviews page", null, GROUPBY_REVIEW));
		((SecurityPermission) codeMap.get(ADMIN_REVIEW_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_REVIEW_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_REVIEW_READ, newLookup(SecurityPermission.class, ADMIN_REVIEW_READ, "Gives ability to read reviews", null, GROUPBY_REVIEW));
		((SecurityPermission) codeMap.get(ADMIN_REVIEW_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_REVIEW_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_REVIEW_UPDATE, newLookup(SecurityPermission.class, ADMIN_REVIEW_UPDATE, "Gives ability to update reviews", null, GROUPBY_REVIEW));
		((SecurityPermission) codeMap.get(ADMIN_REVIEW_UPDATE)).setPermissionPredecessor(ADMIN_REVIEW_READ);
		((SecurityPermission) codeMap.get(ADMIN_REVIEW_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_REVIEW_DELETE, newLookup(SecurityPermission.class, ADMIN_REVIEW_DELETE, "Gives ability to delete reviews", null, GROUPBY_REVIEW));
		((SecurityPermission) codeMap.get(ADMIN_REVIEW_DELETE)).setPermissionPredecessor(ADMIN_REVIEW_READ);
		((SecurityPermission) codeMap.get(ADMIN_REVIEW_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		// Role
		codeMap.put(ADMIN_ROLES_PAGE, newLookup(SecurityPermission.class, ADMIN_ROLES_PAGE, "Provides access to the admin Roles page", null, GROUPBY_ROLE));
		((SecurityPermission) codeMap.get(ADMIN_ROLES_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ROLES_PAGE)).setDefaultRoles(null);

		codeMap.put(ADMIN_ROLE_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT_READ, "Gives ability to read roles", null, GROUPBY_ROLE));
		((SecurityPermission) codeMap.get(ADMIN_ROLE_MANAGEMENT_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_ROLE_MANAGEMENT_READ)).setDefaultRoles(null);

		codeMap.put(ADMIN_ROLE_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT_CREATE, "Gives ability to create roles", null, GROUPBY_ROLE));
		((SecurityPermission) codeMap.get(ADMIN_ROLE_MANAGEMENT_CREATE)).setPermissionPredecessor(ADMIN_ROLE_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_ROLE_MANAGEMENT_CREATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_ROLE_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT_UPDATE, "Gives ability to update roles", null, GROUPBY_ROLE));
		((SecurityPermission) codeMap.get(ADMIN_ROLE_MANAGEMENT_UPDATE)).setPermissionPredecessor(ADMIN_ROLE_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_ROLE_MANAGEMENT_UPDATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_ROLE_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT_DELETE, "Gives ability to delete roles", null, GROUPBY_ROLE));
		((SecurityPermission) codeMap.get(ADMIN_ROLE_MANAGEMENT_DELETE)).setPermissionPredecessor(ADMIN_ROLE_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_ROLE_MANAGEMENT_DELETE)).setDefaultRoles(null);

		// Search
		codeMap.put(USER_SEARCHES_PAGE, newLookup(SecurityPermission.class, USER_SEARCHES_PAGE, "Provides access to the Searches page", null, GROUPBY_SEARCH));
		((SecurityPermission) codeMap.get(USER_SEARCHES_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(USER_SEARCHES_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(ADMIN_SEARCHES_PAGE, newLookup(SecurityPermission.class, ADMIN_SEARCHES_PAGE, "Provides access to the admin Searches page", null, GROUPBY_SEARCH));
		((SecurityPermission) codeMap.get(ADMIN_SEARCHES_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SEARCHES_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_SEARCH_READ, newLookup(SecurityPermission.class, ADMIN_SEARCH_READ, "read all searches", null, GROUPBY_SEARCH));
		((SecurityPermission) codeMap.get(ADMIN_SEARCH_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SEARCH_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_SEARCH_CREATE, newLookup(SecurityPermission.class, ADMIN_SEARCH_CREATE, "create a search", null, GROUPBY_SEARCH));
		((SecurityPermission) codeMap.get(ADMIN_SEARCH_CREATE)).setPermissionPredecessor(ADMIN_SEARCH_READ);
		((SecurityPermission) codeMap.get(ADMIN_SEARCH_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_SEARCH_UPDATE, newLookup(SecurityPermission.class, ADMIN_SEARCH_UPDATE, "update a search", null, GROUPBY_SEARCH));
		((SecurityPermission) codeMap.get(ADMIN_SEARCH_UPDATE)).setPermissionPredecessor(ADMIN_SEARCH_READ);
		((SecurityPermission) codeMap.get(ADMIN_SEARCH_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_SEARCH_DELETE, newLookup(SecurityPermission.class, ADMIN_SEARCH_DELETE, "delete a search", null, GROUPBY_SEARCH));
		((SecurityPermission) codeMap.get(ADMIN_SEARCH_DELETE)).setPermissionPredecessor(ADMIN_SEARCH_READ);
		((SecurityPermission) codeMap.get(ADMIN_SEARCH_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		//Security
		codeMap.put(ADMIN_SECURITY_PAGE, newLookup(SecurityPermission.class, ADMIN_SECURITY_PAGE, "Provides access to the admin Security page", null, GROUPBY_ADMIN_SECURITY));
		((SecurityPermission) codeMap.get(ADMIN_SECURITY_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SECURITY_PAGE)).setDefaultRoles(null);

		codeMap.put(ADMIN_SECURITY_POLICY, newLookup(SecurityPermission.class, ADMIN_SECURITY_POLICY, "Access the security policy tab", null, GROUPBY_ADMIN_SECURITY));
		((SecurityPermission) codeMap.get(ADMIN_SECURITY_POLICY)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SECURITY_POLICY)).setDefaultRoles(null);

		codeMap.put(ADMIN_SECURITY_SHIRO_CONFIG, newLookup(SecurityPermission.class, ADMIN_SECURITY_SHIRO_CONFIG, "Access the Shiro Config", null, GROUPBY_ADMIN_SECURITY));
		((SecurityPermission) codeMap.get(ADMIN_SECURITY_SHIRO_CONFIG)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SECURITY_SHIRO_CONFIG)).setDefaultRoles(null);

		//Submissions (Admin)
		codeMap.put(ADMIN_PARTIAL_SUBMISSIONS_PAGE, newLookup(SecurityPermission.class, ADMIN_PARTIAL_SUBMISSIONS_PAGE, "Provides access to the partial submission page", null, GROUPBY_ADMIN_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(ADMIN_PARTIAL_SUBMISSIONS_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_PARTIAL_SUBMISSIONS_PAGE)).setDefaultRoles(null);

		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE_PAGE, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE_PAGE, "Provides access to the admin custom submission form template page", null, GROUPBY_ADMIN_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(ADMIN_SUBMISSION_FORM_TEMPLATE_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SUBMISSION_FORM_TEMPLATE_PAGE)).setDefaultRoles(null);

		codeMap.put(ADMIN_USER_SUBMISSIONS_READ, newLookup(SecurityPermission.class, ADMIN_USER_SUBMISSIONS_READ, "Allows for reading user submissions", null, GROUPBY_ADMIN_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(ADMIN_USER_SUBMISSIONS_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_USER_SUBMISSIONS_READ)).setDefaultRoles(null);

		codeMap.put(ADMIN_USER_SUBMISSIONS_UPDATE, newLookup(SecurityPermission.class, ADMIN_USER_SUBMISSIONS_UPDATE, "Allows for update user submissions", null, GROUPBY_ADMIN_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(ADMIN_USER_SUBMISSIONS_UPDATE)).setPermissionPredecessor(ADMIN_USER_SUBMISSIONS_READ);
		((SecurityPermission) codeMap.get(ADMIN_USER_SUBMISSIONS_UPDATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_USER_SUBMISSIONS_DELETE, newLookup(SecurityPermission.class, ADMIN_USER_SUBMISSIONS_DELETE, "Allows for deleting user submissions", null, GROUPBY_ADMIN_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(ADMIN_USER_SUBMISSIONS_DELETE)).setPermissionPredecessor(ADMIN_USER_SUBMISSIONS_READ);
		((SecurityPermission) codeMap.get(ADMIN_USER_SUBMISSIONS_DELETE)).setDefaultRoles(null);

		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE_READ, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE_READ, "Allows for reading submission form templates", null, GROUPBY_ADMIN_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(ADMIN_SUBMISSION_FORM_TEMPLATE_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SUBMISSION_FORM_TEMPLATE_READ)).setDefaultRoles(null);

		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE_CREATE, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE_CREATE, "Allows for creating submission form templates", null, GROUPBY_ADMIN_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(ADMIN_SUBMISSION_FORM_TEMPLATE_CREATE)).setPermissionPredecessor(ADMIN_SUBMISSION_FORM_TEMPLATE_READ);
		((SecurityPermission) codeMap.get(ADMIN_SUBMISSION_FORM_TEMPLATE_CREATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE_UPDATE, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE_UPDATE, "Allows for updating submission form templates", null, GROUPBY_ADMIN_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(ADMIN_SUBMISSION_FORM_TEMPLATE_UPDATE)).setPermissionPredecessor(ADMIN_SUBMISSION_FORM_TEMPLATE_READ);
		((SecurityPermission) codeMap.get(ADMIN_SUBMISSION_FORM_TEMPLATE_UPDATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE_DELETE, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE_DELETE, "Allows for deleting submission form templates", null, GROUPBY_ADMIN_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(ADMIN_SUBMISSION_FORM_TEMPLATE_DELETE)).setPermissionPredecessor(ADMIN_SUBMISSION_FORM_TEMPLATE_READ);
		((SecurityPermission) codeMap.get(ADMIN_SUBMISSION_FORM_TEMPLATE_DELETE)).setDefaultRoles(null);

		//Submissions (User)
		codeMap.put(USER_SUBMISSIONS_PAGE, newLookup(SecurityPermission.class, USER_SUBMISSIONS_PAGE, "Provides access to the Submission page", null, GROUPBY_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(USER_SUBMISSIONS_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(USER_SUBMISSIONS_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(USER_SUBMISSIONS_READ, newLookup(SecurityPermission.class, USER_SUBMISSIONS_READ, "Allow user submission reading", null, GROUPBY_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(USER_SUBMISSIONS_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(USER_SUBMISSIONS_READ)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(USER_SUBMISSIONS_CREATE, newLookup(SecurityPermission.class, USER_SUBMISSIONS_CREATE, "Allow user submission creation", null, GROUPBY_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(USER_SUBMISSIONS_CREATE)).setPermissionPredecessor(USER_SUBMISSIONS_READ);
		((SecurityPermission) codeMap.get(USER_SUBMISSIONS_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(USER_SUBMISSIONS_UPDATE, newLookup(SecurityPermission.class, USER_SUBMISSIONS_UPDATE, "Allow user submission updates", null, GROUPBY_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(USER_SUBMISSIONS_UPDATE)).setPermissionPredecessor(USER_SUBMISSIONS_READ);
		((SecurityPermission) codeMap.get(USER_SUBMISSIONS_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(USER_SUBMISSIONS_DELETE, newLookup(SecurityPermission.class, USER_SUBMISSIONS_DELETE, "Allow user submission deleting", null, GROUPBY_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(USER_SUBMISSIONS_DELETE)).setPermissionPredecessor(USER_SUBMISSIONS_READ);
		((SecurityPermission) codeMap.get(USER_SUBMISSIONS_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(USER_SUBMISSIONS_CHANGEREQUEST, newLookup(SecurityPermission.class, USER_SUBMISSIONS_CHANGEREQUEST, "Allow user to create change requests", null, GROUPBY_USER_SUBMISSION));
		((SecurityPermission) codeMap.get(USER_SUBMISSIONS_CHANGEREQUEST)).setPermissionPredecessor(USER_SUBMISSIONS_READ);
		((SecurityPermission) codeMap.get(USER_SUBMISSIONS_CHANGEREQUEST)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		// System Management
		codeMap.put(ADMIN_SYSTEM_PAGE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_PAGE, "Provides access to the admin System page (ONLY FOR ADMINS)", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_PAGE)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_ARCHIVES_PAGE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_ARCHIVES_PAGE, "Provides access to the admin System Archives page", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_ARCHIVES_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_ARCHIVES_PAGE)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT, "General sys admin permission", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_APP_PROP, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_APP_PROP, "Allows for updating app properties", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_APP_PROP)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_APP_PROP)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_PLUGIN, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_PLUGIN, "Allows for management of plugins", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_PLUGIN)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_PLUGIN)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_READ, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_READ, "Allows for sys archive management", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_READ)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_CREATE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_CREATE, "Allows for sys archive creation", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_CREATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_CREATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_DELETE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_DELETE, "Allows for sys archive deletion", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_DELETE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_DELETE)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_ERROR_TICKET, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_ERROR_TICKET, "Allows for sys error ticket management", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_ERROR_TICKET)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_ERROR_TICKET)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_SEARCH_CONTROL, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_SEARCH_CONTROL, "Allows for sys search management", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_SEARCH_CONTROL)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_SEARCH_CONTROL)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_READ, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_READ, "Allows for reading application meta data", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_READ)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_UPDATE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_UPDATE, "Allows for updating system configs", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_UPDATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_UPDATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_DELETE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_DELETE, "Allows for removal/clearing operations for the sys", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_DELETE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_DELETE)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_LOGGING, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_LOGGING, "Access to permission logging", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_LOGGING)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_LOGGING)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_MANAGERS, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_MANAGERS, "System managers", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_MANAGERS)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_MANAGERS)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_CACHE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_CACHE, "Access to system cache", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_CACHE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_CACHE)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_RECENT_CHANGES, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_RECENT_CHANGES, "System recent changes", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_RECENT_CHANGES)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_RECENT_CHANGES)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_STATUS, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_STATUS, "Access to system status", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_STATUS)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_STATUS)).setDefaultRoles(null);

		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_STAND_BY, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_STAND_BY, "Access to system stand by", null, GROUPBY_SYSTEM_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_STAND_BY)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_SYSTEM_MANAGEMENT_STAND_BY)).setDefaultRoles(null);

		//Tags
		codeMap.put(ADMIN_TAGS_PAGE, newLookup(SecurityPermission.class, ADMIN_TAGS_PAGE, "Provides access to the admin Tags page", null, GROUPBY_TAGS));
		((SecurityPermission) codeMap.get(ADMIN_TAGS_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_TAGS_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		// Tracking
		codeMap.put(ADMIN_TRACKING_PAGE, newLookup(SecurityPermission.class, ADMIN_TRACKING_PAGE, "Provides access to the admin Tracking page", null, GROUPBY_TRACKING));
		((SecurityPermission) codeMap.get(ADMIN_TRACKING_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_TRACKING_PAGE)).setDefaultRoles(null);

		codeMap.put(ADMIN_TRACKING_READ, newLookup(SecurityPermission.class, ADMIN_TRACKING_READ, "read tracking", null, GROUPBY_TRACKING));
		((SecurityPermission) codeMap.get(ADMIN_TRACKING_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_TRACKING_READ)).setDefaultRoles(null);

		codeMap.put(ADMIN_TRACKING_UPDATE, newLookup(SecurityPermission.class, ADMIN_TRACKING_UPDATE, "update tracking on an item", null, GROUPBY_TRACKING));
		((SecurityPermission) codeMap.get(ADMIN_TRACKING_UPDATE)).setPermissionPredecessor(ADMIN_TRACKING_READ);
		((SecurityPermission) codeMap.get(ADMIN_TRACKING_UPDATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_TRACKING_DELETE, newLookup(SecurityPermission.class, ADMIN_TRACKING_DELETE, "delete tracking from an item", null, GROUPBY_TRACKING));
		((SecurityPermission) codeMap.get(ADMIN_TRACKING_DELETE)).setPermissionPredecessor(ADMIN_TRACKING_READ);
		((SecurityPermission) codeMap.get(ADMIN_TRACKING_DELETE)).setDefaultRoles(null);

		//Users (Admin)
		codeMap.put(ADMIN_USER_MANAGEMENT_PAGE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_PAGE, "Provides access to the admin User Management page", null, GROUPBY_USER_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_PAGE)).setDefaultRoles(null);

		codeMap.put(ADMIN_USER_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_READ, "Allow admin user submission reading", null, GROUPBY_USER_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_READ)).setDefaultRoles(null);

		codeMap.put(ADMIN_USER_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_CREATE, "Allow admin user submission creation", null, GROUPBY_USER_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_CREATE)).setPermissionPredecessor(ADMIN_USER_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_CREATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_USER_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_UPDATE, "Allow admin user submission updating", null, GROUPBY_USER_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_UPDATE)).setPermissionPredecessor(ADMIN_USER_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_UPDATE)).setDefaultRoles(null);

		codeMap.put(ADMIN_USER_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_DELETE, "Allow admin user submission deleting", null, GROUPBY_USER_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_DELETE)).setPermissionPredecessor(ADMIN_USER_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_DELETE)).setDefaultRoles(null);

		codeMap.put(ADMIN_USER_MANAGEMENT_APPROVE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_APPROVE, "Allow admin user submission deleting", null, GROUPBY_USER_MANAGEMENT));
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_APPROVE)).setPermissionPredecessor(ADMIN_USER_MANAGEMENT_READ);
		((SecurityPermission) codeMap.get(ADMIN_USER_MANAGEMENT_APPROVE)).setDefaultRoles(null);

		// Watches
		codeMap.put(ADMIN_WATCHES_PAGE, newLookup(SecurityPermission.class, ADMIN_WATCHES_PAGE, "Provides access to the admin Watches page", null, GROUPBY_WATCHES));
		((SecurityPermission) codeMap.get(ADMIN_WATCHES_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_WATCHES_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(USER_WATCHES_PAGE, newLookup(SecurityPermission.class, USER_WATCHES_PAGE, "Provides access to the Watches page", null, GROUPBY_WATCHES));
		((SecurityPermission) codeMap.get(USER_WATCHES_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(USER_WATCHES_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		codeMap.put(ADMIN_WATCHES_READ, newLookup(SecurityPermission.class, ADMIN_WATCHES_READ, "ability to read watches", null, GROUPBY_WATCHES));
		((SecurityPermission) codeMap.get(ADMIN_WATCHES_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_WATCHES_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_WATCHES_UPDATE, newLookup(SecurityPermission.class, ADMIN_WATCHES_UPDATE, "update watches", null, GROUPBY_WATCHES));
		((SecurityPermission) codeMap.get(ADMIN_WATCHES_UPDATE)).setPermissionPredecessor(ADMIN_WATCHES_READ);
		((SecurityPermission) codeMap.get(ADMIN_WATCHES_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_WATCHES_DELETE, newLookup(SecurityPermission.class, ADMIN_WATCHES_DELETE, "delete watches", null, GROUPBY_WATCHES));
		((SecurityPermission) codeMap.get(ADMIN_WATCHES_DELETE)).setPermissionPredecessor(ADMIN_WATCHES_READ);
		((SecurityPermission) codeMap.get(ADMIN_WATCHES_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		// Workplan
		codeMap.put(ADMIN_WORKPLAN_PAGE, newLookup(SecurityPermission.class, ADMIN_WORKPLAN_PAGE, "Provides access to the 'Workplans' page", null, GROUPBY_WORKPLAN));
		((SecurityPermission) codeMap.get(ADMIN_WORKPLAN_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_WORKPLAN_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_WORKPLAN_READ, newLookup(SecurityPermission.class, ADMIN_WORKPLAN_READ, "Provides access to read workplans as admin", null, GROUPBY_WORKPLAN));
		((SecurityPermission) codeMap.get(ADMIN_WORKPLAN_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_WORKPLAN_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_WORKPLAN_CREATE, newLookup(SecurityPermission.class, ADMIN_WORKPLAN_CREATE, "Provides access to create workplans", null, GROUPBY_WORKPLAN));
		((SecurityPermission) codeMap.get(ADMIN_WORKPLAN_CREATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_WORKPLAN_CREATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_WORKPLAN_UPDATE, newLookup(SecurityPermission.class, ADMIN_WORKPLAN_UPDATE, "Provides access to update workplans", null, GROUPBY_WORKPLAN));
		((SecurityPermission) codeMap.get(ADMIN_WORKPLAN_UPDATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_WORKPLAN_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(ADMIN_WORKPLAN_DELETE, newLookup(SecurityPermission.class, ADMIN_WORKPLAN_DELETE, "Provides access to delete workplans", null, GROUPBY_WORKPLAN));
		((SecurityPermission) codeMap.get(ADMIN_WORKPLAN_DELETE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(ADMIN_WORKPLAN_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(USER_WORKPLAN_READ, newLookup(SecurityPermission.class, USER_WORKPLAN_READ, "Allows users to view current 'status' of selected record in a workplan", null, GROUPBY_WORKPLAN));
		((SecurityPermission) codeMap.get(USER_WORKPLAN_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(USER_WORKPLAN_READ)).setDefaultRoles(Arrays.asList(SecurityRole.DEFAULT_GROUP));

		// Workplan Progress
		codeMap.put(WORKPLAN_PROGRESS_MANAGEMENT_PAGE, newLookup(SecurityPermission.class, WORKPLAN_PROGRESS_MANAGEMENT_PAGE, "Enable viewing of the page", null, GROUPBY_WORKPLAN_PROGRESS_MANAGEMENT));
		((SecurityPermission) codeMap.get(WORKPLAN_PROGRESS_MANAGEMENT_PAGE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(WORKPLAN_PROGRESS_MANAGEMENT_PAGE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(WORKFLOW_LINK_READ, newLookup(SecurityPermission.class, WORKFLOW_LINK_READ, "View your group workflow links", null, GROUPBY_WORKPLAN_PROGRESS_MANAGEMENT));
		((SecurityPermission) codeMap.get(WORKFLOW_LINK_READ)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(WORKFLOW_LINK_READ)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(WORKFLOW_LINK_READ_ALL, newLookup(SecurityPermission.class, WORKFLOW_LINK_READ_ALL, "View All workflow links", null, GROUPBY_WORKPLAN_PROGRESS_MANAGEMENT));
		((SecurityPermission) codeMap.get(WORKFLOW_LINK_READ_ALL)).setPermissionPredecessor(WORKFLOW_LINK_READ);
		((SecurityPermission) codeMap.get(WORKFLOW_LINK_READ_ALL)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(WORKFLOW_LINK_UPDATE, newLookup(SecurityPermission.class, WORKFLOW_LINK_UPDATE, "Enable updating of workflows", null, GROUPBY_WORKPLAN_PROGRESS_MANAGEMENT));
		((SecurityPermission) codeMap.get(WORKFLOW_LINK_UPDATE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(WORKFLOW_LINK_UPDATE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(WORKFLOW_LINK_DELETE, newLookup(SecurityPermission.class, WORKFLOW_LINK_DELETE, "Enable updating of workflows", null, GROUPBY_WORKPLAN_PROGRESS_MANAGEMENT));
		((SecurityPermission) codeMap.get(WORKFLOW_LINK_DELETE)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(WORKFLOW_LINK_DELETE)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(WORKFLOW_LINK_ASSIGN, newLookup(SecurityPermission.class, WORKFLOW_LINK_ASSIGN, "Enable assigning and unassigning and assign to admin", null, GROUPBY_WORKPLAN_PROGRESS_MANAGEMENT));
		((SecurityPermission) codeMap.get(WORKFLOW_LINK_ASSIGN)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(WORKFLOW_LINK_ASSIGN)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(WORKFLOW_LINK_ASSIGN_ANY, newLookup(SecurityPermission.class, WORKFLOW_LINK_ASSIGN_ANY, "Assign any entry to any user", null, GROUPBY_WORKPLAN_PROGRESS_MANAGEMENT));
		((SecurityPermission) codeMap.get(WORKFLOW_LINK_ASSIGN_ANY)).setPermissionPredecessor(WORKFLOW_LINK_ASSIGN);
		((SecurityPermission) codeMap.get(WORKFLOW_LINK_ASSIGN_ANY)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		codeMap.put(WORKFLOW_ADMIN_SUBMISSION_COMMENTS, newLookup(SecurityPermission.class, WORKFLOW_ADMIN_SUBMISSION_COMMENTS, "Get comments for role", null, GROUPBY_WORKPLAN_PROGRESS_MANAGEMENT));
		((SecurityPermission) codeMap.get(WORKFLOW_ADMIN_SUBMISSION_COMMENTS)).setPermissionPredecessor(null);
		((SecurityPermission) codeMap.get(WORKFLOW_ADMIN_SUBMISSION_COMMENTS)).setDefaultRoles(Arrays.asList(SecurityRole.LIBRARIAN_ROLE));

		return codeMap;
	}

	public String getPermissionPredecessor()
	{
		return permissionPredecessor;
	}

	public void setPermissionPredecessor(String permissionPredecessor)
	{
		this.permissionPredecessor = permissionPredecessor;
	}

	public List<String> getDefaultRoles()
	{
		return defaultRoles;
	}

	public void setDefaultRoles(List<String> defaultRoles)
	{
		this.defaultRoles = defaultRoles;
	}

}
