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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 * @author cyearsley
 * 
 */
@SystemTable
@APIDescription("Defines Security Permissions")
public class SecurityPermission
		extends LookupEntity<SecurityPermission>
{

	private static final long serialVersionUID = 1L;
	//===OLD Permissions===========================================================================================================
	public static final String GROUPBY_OLD = "Old permissions (to be replaced/renamed)";
	public static final String ENTRY_TAG = "ENTRY-TAG";
	public static final String USER_SUBMISSIONS = "USER-SUBMISSIONS";
	public static final String EVALUATIONS = "EVALUATIONS";
	public static final String REPORT_ENTRY_LISTING_REPORT = "REPORT-ENTRYLISTING-REPORT";
	public static final String RELATIONSHIP_VIEW_TOOL = "RELATION-VIEW-TOOL";
	public static final String ADMIN_USER_MANAGEMENT = "ADMIN-USER-MANAGEMENT";
	public static final String ADMIN_ENTRY_MANAGEMENT = "ADMIN-ENTRY-MANAGEMENT";
	public static final String ADMIN_MESSAGE_MANAGEMENT = "ADMIN-MESSAGE-MANAGEMENT";
	public static final String ADMIN_JOB_MANAGEMENT = "ADMIN-JOB-MANAGEMENT";
	public static final String ADMIN_INTEGRATION = "ADMIN-INTEGRATION";
	public static final String ADMIN_WATCHES = "ADMIN-WATCHES";
	public static final String ADMIN_TRACKING = "ADMIN-TRACKING";
	public static final String ADMIN_SEARCH = "ADMIN-SEARCH";
	public static final String ADMIN_USER_MANAGEMENT_PROFILES = "ADMIN-USER-MANAGEMENT-PROFILES";
	public static final String ADMIN_TEMPMEDIA_MANAGEMENT = "ADMIN-TEMPMEDIA-MANAGEMENT";
	public static final String ADMIN_ORGANIZATION = "ADMIN-ORGANIZATION";
	public static final String ADMIN_LOOKUPS = "ADMIN-LOOKUPS";
	public static final String ADMIN_HIGHLIGHTS = "ADMIN-HIGHLIGHTS";
	public static final String ADMIN_MEDIA = "ADMIN-MEDIA";
	public static final String ADMIN_FEEDBACK = "ADMIN-FEEDBACK";
	public static final String ADMIN_EVALUATION_TEMPLATE = "ADMIN-EVALUATION-TEMPLATE";
	public static final String ADMIN_BRANDING = "ADMIN-BRANDING";
	public static final String ADMIN_EVALUATION_TEMPLATE_SECTION = "ADMIN-EVALUATION-TEMPLATE-SECTION";
	public static final String ADMIN_CONTACT_MANAGEMENT = "ADMIN-CONTACT-MANAGEMENT";
	public static final String ADMIN_ENTRY_TEMPLATES = "ADMIN-ENTRY-TEMPLATES";
	public static final String ADMIN_USER_SUBMISSIONS = "ADMIN-USER-SUBMISSIONS";
	public static final String ADMIN_ENTRY_TYPES = "ADMIN-ENTRY-TYPES";
	public static final String ADMIN_QUESTIONS = "ADMIN-QUESTIONS";
	public static final String ADMIN_REVIEW = "ADMIN-REVIEW";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION";
	public static final String ADMIN_ATTRIBUTE_MANAGEMENT = "ADMIN-ATTRIBUTE-MANAGEMENT";
	public static final String ADMIN_ALERT_MANAGEMENT = "ADMIN-ALERT-MANAGEMENT";
	public static final String ADMIN_EVALUATION_MANAGEMENT = "ADMIN-EVALUATION-MANAGEMENT";
	public static final String ADMIN_ROLE_MANAGEMENT = "ADMIN-ROLE-MANAGEMENT";
	public static final String ADMIN_FAQ = "ADMIN-FAQ";
	public static final String ADMIN_SUBMISSION_FORM_TEMPLATE = "ADMIN-SUBMISSION-FORM-TEMPLATE";
	public static final String ADMIN_SUPPORT_MEDIA = "ADMIN-SUPPORT-MEDIA";
	public static final String ALLOW_USER_ATTRIBUTE_TYPE_CREATION = "ALLOW-USER-ATTRIBUTE-TYPE-CREATION";
	public static final String RELATION_VIEW_TOOL = "RELATION-VIEW-TOOL";
	public static final String API_DOCS = "API_DOCS";
	public static final String REPORTS = "REPORTS";
	public static final String REPORTS_SCHEDULE = "REPORTS-SCHEDULE";
	public static final String REPORTS_SCHEDULE_UPDATE = "REPORTS-SCHEDULE-UPDATE";
	public static final String ADMIN_SECURITY = "ADMIN-SECURITY";

	// ========================================================================================================================
	
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
	public static final String ADMIN_QUESTION_PAGE = "ADMIN-QUESTION-PAGE";
	public static final String ADMIN_QUESTIONS_READ = "ADMIN-QUESTIONS-READ";
	public static final String ADMIN_QUESTIONS_UPDATE = "ADMIN-QUESTIONS-UPDATE";

	//Relationships
	public static final String GROUPBY_RELATIONSHIPS = "Relationships";
	public static final String ADMIN_RELATIONSHIPS_PAGE = "ADMIN-RELATIONSHIPS-PAGE";
	public static final String USER_RELATIONSHIPS_PAGE = "USER-RELATIONSHIPS-PAGE";

	// Reports
	public static final String GROUPBY_REPORTS = "Reports";
	public static final String REPORTS_PAGE = "REPORTS-PAGE";
	public static final String REPORTS_CREATE = "REPORTS-CREATE";
	public static final String REPORTS_DELETE = "REPORTS-DELETE";
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

	// Reviews
	public static final String GROUPBY_REVIEW = "Reviews";
	public static final String USER_REVIEWS_PAGE = "USER-REVIEWS-PAGE";
	public static final String ADMIN_REVIEWS_PAGE = "ADMIN-REVIEWS-PAGE";
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
	public static final String ADMIN_PARTIAL_SUBMISSIONS_PAGE ="ADMIN-PARTIAL-SUBMISSIONS-PAGE";
	public static final String ADMIN_SUBMISSION_FORM_SANDBOX_PAGE = "ADMIN-SUBMISSION-FORM-SANDBOX-PAGE";
	public static final String ADMIN_SUBMISSION_FORM_TEMPLATE_PAGE = "ADMIN-SUBMISSION-FORM-TEMPLATE_PAGE";
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
	public static final String ADMIN_SYSTEM_MANAGEMENT = "ADMIN-SYSTEM-MANAGEMENT";
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

	// Watches
	public static final String GROUPBY_WATCHES = "Watches";
	public static final String ADMIN_WATCHES_PAGE = "ADMIN-WATCHES-PAGE";
	public static final String USER_WATCHES_PAGE = "USER-WATCHES-PAGE";
	public static final String ADMIN_WATCHES_DELETE = "ADMIN-WATCHES-DELETE";
	public static final String ADMIN_WATCHES_UPDATE = "ADMIN-WATCHES-UPDATE";
	public static final String ADMIN_WATCHES_READ = "ADMIN-WATCHES-READ";


	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();

		// Alerts
		codeMap.put(ADMIN_ALERTS_PAGE, newLookup(SecurityPermission.class, ADMIN_ALERTS_PAGE, "Provides access to the admin Alerts page", null, GROUPBY_ALERT, null));
		codeMap.put(ADMIN_ALERT_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT_READ, "Gives ability to read all alerts", null, GROUPBY_ALERT, null));
		codeMap.put(ADMIN_ALERT_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT_CREATE, "Gives ability to create new alerts", null, GROUPBY_ALERT, ADMIN_ALERT_MANAGEMENT_READ));
		codeMap.put(ADMIN_ALERT_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT_UPDATE, "Gives ability to update existing alerts", null, GROUPBY_ALERT, ADMIN_ALERT_MANAGEMENT_READ));
		codeMap.put(ADMIN_ALERT_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT_DELETE, "Give ability to delete alerts", null, GROUPBY_ALERT, ADMIN_ALERT_MANAGEMENT_READ));
		
		//API
		codeMap.put(ADMIN_API_PAGE, newLookup(SecurityPermission.class, ADMIN_API_PAGE, "Give ability to see API docs", null, GROUPBY_API, null));
		
		//Attributes
		codeMap.put(ADMIN_ATTRIBUTE_PAGE, newLookup(SecurityPermission.class, ADMIN_ATTRIBUTE_PAGE, "Provides access to the admin attribute page", null, GROUPBY_ATTRIBUTE, null));
		codeMap.put(ADMIN_ATTRIBUTE_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_ATTRIBUTE_MANAGEMENT_CREATE, "Gives ability to create new attributes", null, GROUPBY_ATTRIBUTE, null));
		codeMap.put(ADMIN_ATTRIBUTE_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_ATTRIBUTE_MANAGEMENT_UPDATE, "Gives ability to update existing attributes", null, GROUPBY_ATTRIBUTE, null));
		codeMap.put(ADMIN_ATTRIBUTE_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_ATTRIBUTE_MANAGEMENT_DELETE, "Gives ability to delete existing attributes", null, GROUPBY_ATTRIBUTE, null));
		codeMap.put(ADMIN_COMPONENT_ATTRIBUTE_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_COMPONENT_ATTRIBUTE_MANAGEMENT, "Gives ability to manage attributes on entries from the admin tools manage assignments tool", null, GROUPBY_ATTRIBUTE, null));

		// Branding
		codeMap.put(ADMIN_BRANDING_PAGE, newLookup(SecurityPermission.class, ADMIN_BRANDING_PAGE, "Provides access to the admin Branding page", null, GROUPBY_BRANDING, null));
		codeMap.put(ADMIN_BRANDING_CREATE, newLookup(SecurityPermission.class, ADMIN_BRANDING_CREATE, "Gives ability to create new brandings", null, GROUPBY_BRANDING, null));
		codeMap.put(ADMIN_BRANDING_UPDATE, newLookup(SecurityPermission.class, ADMIN_BRANDING_UPDATE, "Gives ability to update brandings", null, GROUPBY_BRANDING, null));
		codeMap.put(ADMIN_BRANDING_DELETE, newLookup(SecurityPermission.class, ADMIN_BRANDING_DELETE, "Gives ability to delete brandings", null, GROUPBY_BRANDING, null));
		
		// Contacts
		codeMap.put(ADMIN_CONTACTS_PAGE, newLookup(SecurityPermission.class, ADMIN_CONTACTS_PAGE, "Provides access to the admin contacts page", null, GROUPBY_CONTACT, null));
		codeMap.put(ADMIN_CONTACT_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_CONTACT_MANAGEMENT_CREATE, "Gives ability to create contacts", null, GROUPBY_CONTACT, null));
		codeMap.put(ADMIN_CONTACT_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_CONTACT_MANAGEMENT_UPDATE, "Gives ability to update existing contacts", null, GROUPBY_CONTACT, null));
		codeMap.put(ADMIN_CONTACT_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_CONTACT_MANAGEMENT_DELETE, "Gives ability delete contacts", null, GROUPBY_CONTACT, null));

		//Dashboard
		codeMap.put(DASHBOARD_PAGE, newLookup(SecurityPermission.class, DASHBOARD_PAGE, "Provides access to the dashboard page", null, GROUPBY_DASHBOARD, null));
		codeMap.put(DASHBOARD_WIDGET_ENTRY_STATS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_ENTRY_STATS, "Allows user to view the Entry Stats widget (only effects UI)", null, GROUPBY_DASHBOARD, null));
		codeMap.put(DASHBOARD_WIDGET_EVALUATION_STATS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_EVALUATION_STATS, "Allows user to view the Evaluation Stats widget (only effects UI)", null, GROUPBY_DASHBOARD, null));
		codeMap.put(DASHBOARD_WIDGET_NOTIFICATIONS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_NOTIFICATIONS, "Allows user to view the Notifications widget (only effects UI)", null, GROUPBY_DASHBOARD, null));
		codeMap.put(DASHBOARD_WIDGET_OUTSTANDING_FEEDBACK, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_OUTSTANDING_FEEDBACK, "Allows user to view the Outstanding Feedback widget (only effects UI)", null, GROUPBY_DASHBOARD, null));
		codeMap.put(DASHBOARD_WIDGET_PENDING_REQUESTS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_PENDING_REQUESTS, "Allows user to view the Pending Approval Requests widget (only effects UI)", null, GROUPBY_DASHBOARD, null));
		codeMap.put(DASHBOARD_WIDGET_QUESTIONS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_QUESTIONS, "Allows user to view the Questions widget (only effects UI)", null, GROUPBY_DASHBOARD, null));
		codeMap.put(DASHBOARD_WIDGET_USER_DATA, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_USER_DATA, "Allows user to view the Recent User Data widget (only effects UI)", null, GROUPBY_DASHBOARD, null));
		codeMap.put(DASHBOARD_WIDGET_REPORTS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_REPORTS, "Allows user to view the Reports widget (only effects UI)", null, GROUPBY_DASHBOARD, null));
		codeMap.put(DASHBOARD_WIDGET_SAVED_SEARCH, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_SAVED_SEARCH, "Allows user to view the Saved Search widget (only effects UI)", null, GROUPBY_DASHBOARD, null));
		codeMap.put(DASHBOARD_WIDGET_SUBMISSION_STATUS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_SUBMISSION_STATUS, "Allows user to view the Submission Status widget (only effects UI)", null, GROUPBY_DASHBOARD, null));
		codeMap.put(DASHBOARD_WIDGET_SYSTEM_STATUS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_SYSTEM_STATUS, "Allows user to view the System Status widget (only effects UI)", null, GROUPBY_DASHBOARD, null));
		codeMap.put(DASHBOARD_WIDGET_USER_STATS, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_USER_STATS, "Allows user to view the User Stats widget (only effects UI)", null, GROUPBY_DASHBOARD, null));
		codeMap.put(DASHBOARD_WIDGET_WATCHES, newLookup(SecurityPermission.class, DASHBOARD_WIDGET_WATCHES, "Allows user to view the Watches widget (only effects UI)", null, GROUPBY_DASHBOARD, null));
		
		// Entry Management
		codeMap.put(ADMIN_ENTRIES_PAGE, newLookup(SecurityPermission.class, ADMIN_ENTRIES_PAGE, "Provides access to the admin entry page", null, GROUPBY_ENTRY_MANAGEMENT, null));
		codeMap.put(ADMIN_ENTRY_READ, newLookup(SecurityPermission.class, ADMIN_ENTRY_READ, "Allows admin to read entries", null, GROUPBY_ENTRY_MANAGEMENT, null));
		codeMap.put(ADMIN_ENTRY_CREATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_CREATE, "Allows admin to create entries", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_UPDATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_UPDATE, "Allows admin to update an existing entry", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_DELETE, newLookup(SecurityPermission.class, ADMIN_ENTRY_DELETE, "Allows admin to DELETE an existing entry", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_TOGGLE_STATUS, newLookup(SecurityPermission.class, ADMIN_ENTRY_TOGGLE_STATUS, "Allows the admin to toggle the status of an entry", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_MERGE, newLookup(SecurityPermission.class, ADMIN_ENTRY_MERGE, "Allows the admin to merge two entries together", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_VERSION_DELETE, newLookup(SecurityPermission.class, ADMIN_ENTRY_VERSION_DELETE, "Allows the admin to delete the version", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_VERSION_READ, newLookup(SecurityPermission.class, ADMIN_ENTRY_VERSION_READ, "Allows the admin to read the version", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_VERSION_RESTORE, newLookup(SecurityPermission.class, ADMIN_ENTRY_VERSION_RESTORE, "Allows the admin to restore a version of an entry", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_APPROVE, newLookup(SecurityPermission.class, ADMIN_ENTRY_APPROVE, "Allows an admin the ability to approve an entry", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_CHANGETYPE, newLookup(SecurityPermission.class, ADMIN_ENTRY_CHANGETYPE, "Gives the admin the ability to change an entry's entry type", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_ATTR_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_ATTR_MANAGEMENT, "Gives admin attribute management", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_CONTACT_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_CONTACT_MANAGEMENT, "Gives an admin contact management permissions", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_DEPENDENCY_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_DEPENDENCY_MANAGEMENT, "Gives an admin dependency management", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_MEDIA_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_MEDIA_MANAGEMENT, "Gives an admin media management", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_RELATIONSHIP_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_RELATIONSHIP_MANAGEMENT, "Gives an admin relationship management for entries", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_RESOURCE_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_RESOURCE_MANAGEMENT, "Gives an admin resource management for entries", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_EVALSECTION_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_EVALSECTION_MANAGEMENT, "Gives an admin the ability to manage eval sections", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_TAG_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_TAG_MANAGEMENT, "Allows for tag management", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_CHANGEREQUEST_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_CHANGEREQUEST_MANAGEMENT, "Allows for management change requests", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_CHANGEOWNER, newLookup(SecurityPermission.class, ADMIN_ENTRY_CHANGEOWNER, "Allows admin to change the owner of an entry", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_EXPORT, newLookup(SecurityPermission.class, ADMIN_ENTRY_EXPORT, "Allows admin to export an entry", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_PENDINGCHANGE_READ, newLookup(SecurityPermission.class, ADMIN_ENTRY_PENDINGCHANGE_READ, "Allows admin to read pending changes", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		codeMap.put(ADMIN_ENTRY_ASSIGNUSER, newLookup(SecurityPermission.class, ADMIN_ENTRY_ASSIGNUSER, "Allows admin to assign libarian to a component", null, GROUPBY_ENTRY_MANAGEMENT, ADMIN_ENTRY_READ));
		
		// Entry Templates
		codeMap.put(ADMIN_ENTRYTEMPLATES_PAGE, newLookup(SecurityPermission.class, ADMIN_ENTRYTEMPLATES_PAGE, "Provides access to the entry templates page", null, GROUPBY_ENTRY_TEMPLATES, null));
		codeMap.put(ADMIN_ENTRY_TEMPLATES_READ, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES_READ, "Allows for the user to read entry templates", null, GROUPBY_ENTRY_TEMPLATES, null));
		codeMap.put(ADMIN_ENTRY_TEMPLATES_CREATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES_CREATE, "Allows for the creation of entry templates", null, GROUPBY_ENTRY_TEMPLATES, ADMIN_ENTRY_TEMPLATES_READ));
		codeMap.put(ADMIN_ENTRY_TEMPLATES_UPDATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES_UPDATE, "Allows the user to update entry templates", null, GROUPBY_ENTRY_TEMPLATES, ADMIN_ENTRY_TEMPLATES_READ));
		codeMap.put(ADMIN_ENTRY_TEMPLATES_DELETE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES_DELETE, "Allows the user to delete an entry template", null, GROUPBY_ENTRY_TEMPLATES, ADMIN_ENTRY_TEMPLATES_READ));
		
		// Entry Types
		codeMap.put(ADMIN_ENTRYTYPE_PAGE, newLookup(SecurityPermission.class, ADMIN_ENTRYTYPE_PAGE, "Provides access to the admin entry types page", null, GROUPBY_ENTRY_TYPES, null));		
		codeMap.put(ADMIN_ENTRY_TYPES_CREATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TYPES_CREATE, "Allows for the creation of entry types", null, GROUPBY_ENTRY_TYPES, null));
		codeMap.put(ADMIN_ENTRY_TYPES_UPDATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TYPES_UPDATE, "Allows for the ability to update entry types", null, GROUPBY_ENTRY_TYPES, null));
		codeMap.put(ADMIN_ENTRY_TYPES_DELETE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TYPES_DELETE, "Allow for deleting entry types", null, GROUPBY_ENTRY_TYPES, null));
		
		//Evaluation Template
		codeMap.put(ADMIN_EVAL_TEMPLATES_PAGE, newLookup(SecurityPermission.class, ADMIN_EVAL_TEMPLATES_PAGE, "Provides access to the Evaluation Templates page", null, GROUPBY_EVALUATION_TEMPLATE, null));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_READ, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_READ, "Gives the ability to read evaluation templates", null, GROUPBY_EVALUATION_TEMPLATE, null));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CREATE, "Gives the ability to create evaluation templates", null, GROUPBY_EVALUATION_TEMPLATE, ADMIN_EVALUATION_TEMPLATE_READ));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_UPDATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_UPDATE, "Gives the ability to update evaluation templates", null, GROUPBY_EVALUATION_TEMPLATE, ADMIN_EVALUATION_TEMPLATE_READ));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_DELETE, "Gives the ability to delete evaluation templates", null, GROUPBY_EVALUATION_TEMPLATE, ADMIN_EVALUATION_TEMPLATE_READ));

		//Evaluation Template Checklist Questions
		codeMap.put(ADMIN_EVAL_CHECKLIST_QUESTIONS_PAGE, newLookup(SecurityPermission.class, ADMIN_EVAL_CHECKLIST_QUESTIONS_PAGE, "Provides access to the Evaluation Checklist Questions page", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST_QUESTION, null));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ, "Provides the ability to read eval template checklist question items", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST_QUESTION, null));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_CREATE, "Provides the ability to create eval template checklist questions", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST_QUESTION, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_UPDATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_UPDATE, "Provides the ability to update eval template checklist questions", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST_QUESTION, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_DELETE, "Provides the ability to delete eval template checklist question items", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST_QUESTION, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ));
		
		//Evaluation Template Checklist
		codeMap.put(ADMIN_EVAL_CHECKLIST_TEMPLATES_PAGE, newLookup(SecurityPermission.class, ADMIN_EVAL_CHECKLIST_TEMPLATES_PAGE, "Provides access to the Evaluation Checklist Templates page", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST, null));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ, "Gives the ability to read eval template checklist items", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST, null));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_CREATE, "Gives the ability to create eval template checklist items", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_UPDATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_UPDATE, "Gives the ability to update eval template checklist items", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_DELETE, "Gives the ability to delete a eval template checklist item", null, GROUPBY_EVALUATION_TEMPLATE_CHECKLIST, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ));
		
		//Evaluation Template Section
		codeMap.put(ADMIN_EVAL_SECTION_PAGE, newLookup(SecurityPermission.class, ADMIN_EVAL_SECTION_PAGE, "Provides access to the Evaluation Section page", null, GROUPBY_EVALUATION_TEMPLATE_SECTION, null));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION_READ, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION_READ, "Allows for reading eval template sections", null, GROUPBY_EVALUATION_TEMPLATE_SECTION, null));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION_CREATE, "Allows for creation eval template sections", null, GROUPBY_EVALUATION_TEMPLATE_SECTION, ADMIN_EVALUATION_TEMPLATE_SECTION_READ));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION_UPDATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION_UPDATE, "Allows for update eval template sections", null, GROUPBY_EVALUATION_TEMPLATE_SECTION, ADMIN_EVALUATION_TEMPLATE_SECTION_READ));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION_DELETE, "Allows for deleting eval template sections", null, GROUPBY_EVALUATION_TEMPLATE_SECTION, ADMIN_EVALUATION_TEMPLATE_SECTION_READ));

		//Evaluation Management (Admin)
		codeMap.put(ADMIN_EVAL_PAGE, newLookup(SecurityPermission.class, ADMIN_EVAL_PAGE, "Provides access to the admin evaluator management page", null, GROUPBY_EVALUATION_ADMIN, null));
		codeMap.put(ADMIN_EVALUATION_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_MANAGEMENT_CREATE, "Allows the user to create an evaluation", null, GROUPBY_EVALUATION_ADMIN, USER_EVALUATIONS_READ));
		codeMap.put(ADMIN_EVALUATION_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_MANAGEMENT_DELETE, "Allows the user to delete an evaluation", null, GROUPBY_EVALUATION_ADMIN, USER_EVALUATIONS_READ));
		codeMap.put(ADMIN_EVALUATION_ACTIVATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_ACTIVATE, "Allows the user to activate an evaluation", null, GROUPBY_EVALUATION_ADMIN, USER_EVALUATIONS_READ));
		codeMap.put(ADMIN_EVALUATION_ALLOW_NEW_SECTIONS, newLookup(SecurityPermission.class, ADMIN_EVALUATION_ALLOW_NEW_SECTIONS, "Allows the user to specify whether the eval should allow new sections", null, GROUPBY_EVALUATION_ADMIN, USER_EVALUATIONS_READ));
		codeMap.put(ADMIN_EVALUATION_ALLOW_QUESTION_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_EVALUATION_ALLOW_QUESTION_MANAGEMENT, "Allows the user to specify whether the eval should allow question management", null, GROUPBY_EVALUATION_ADMIN, USER_EVALUATIONS_READ));
		codeMap.put(ADMIN_EVALUATION_TOGGLE_PUBLISH, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TOGGLE_PUBLISH, "Allows the user to publish an evaluation", null, GROUPBY_EVALUATION_ADMIN, USER_EVALUATIONS_READ));
		codeMap.put(ADMIN_EVALUATION_PUBLISH_SUMMARY, newLookup(SecurityPermission.class, ADMIN_EVALUATION_PUBLISH_SUMMARY, "Allows the user to publish a summary", null, GROUPBY_EVALUATION_ADMIN, USER_EVALUATIONS_READ));
		codeMap.put(ADMIN_EVALUATION_DELETE_COMMENT, newLookup(SecurityPermission.class, ADMIN_EVALUATION_DELETE_COMMENT, "Allows the user to delete a comment on said evaluation", null, GROUPBY_EVALUATION_ADMIN, USER_EVALUATIONS_READ));

		//Evaluation Management (User)
		codeMap.put(EVAL_PAGE, newLookup(SecurityPermission.class, EVAL_PAGE, "Provides access to the Evaluator Management page", null, GROUPBY_EVALUATION_USER, null));
		codeMap.put(USER_EVALUATIONS_READ, newLookup(SecurityPermission.class, USER_EVALUATIONS_READ, "Allows an evaluator to 'view' an evaluation", null, GROUPBY_EVALUATION_USER, null));
		codeMap.put(USER_EVALUATIONS_UPDATE, newLookup(SecurityPermission.class, USER_EVALUATIONS_UPDATE, "Allows an evaluator  'edit' an evaluation", null, GROUPBY_EVALUATION_USER, USER_EVALUATIONS_READ));
		codeMap.put(USER_EVALUATIONS_ASSIGN_USER, newLookup(SecurityPermission.class, USER_EVALUATIONS_ASSIGN_USER, "Allows an evaluator to assign a user to an evaluation (ONLY HIDES BUTTON)", null, GROUPBY_EVALUATION_USER, USER_EVALUATIONS_READ));

		//FAQ
		codeMap.put(ADMIN_FAQ_PAGE, newLookup(SecurityPermission.class, ADMIN_FAQ_PAGE, "Provides access to the admin faq page", null, GROUPBY_FAQ, null));
		codeMap.put(ADMIN_FAQ_READ, newLookup(SecurityPermission.class, ADMIN_FAQ_READ, "Allows for reading FAQs", null, GROUPBY_FAQ, null));
		codeMap.put(ADMIN_FAQ_CREATE, newLookup(SecurityPermission.class, ADMIN_FAQ_CREATE, "Gives ability to creater FAQs", null, GROUPBY_FAQ, ADMIN_FAQ_READ));
		codeMap.put(ADMIN_FAQ_UPDATE, newLookup(SecurityPermission.class, ADMIN_FAQ_UPDATE, "Allows for update FAQs", null, GROUPBY_FAQ, ADMIN_FAQ_READ));
		codeMap.put(ADMIN_FAQ_DELETE, newLookup(SecurityPermission.class, ADMIN_FAQ_DELETE, "Allows for deleting FAQs", null, GROUPBY_FAQ, ADMIN_FAQ_READ));
		
		// Feedback
		codeMap.put(ADMIN_FEEDBACK_PAGE, newLookup(SecurityPermission.class, ADMIN_FEEDBACK_PAGE, "Provides access to the admin Feedback page", null, GROUPBY_FEEDBACK, null));
		codeMap.put(ADMIN_FEEDBACK_READ, newLookup(SecurityPermission.class, ADMIN_FEEDBACK_READ, "allows for reading feedback", null, GROUPBY_FEEDBACK, null));
		codeMap.put(ADMIN_FEEDBACK_UPDATE, newLookup(SecurityPermission.class, ADMIN_FEEDBACK_UPDATE, "allows for modifying feedback", null, GROUPBY_FEEDBACK, ADMIN_FEEDBACK_READ));
		codeMap.put(ADMIN_FEEDBACK_DELETE, newLookup(SecurityPermission.class, ADMIN_FEEDBACK_DELETE, "allows for removing feedback", null, GROUPBY_FEEDBACK, ADMIN_FEEDBACK_READ));

		// Highlights
		codeMap.put(ADMIN_HIGHLIGHTS_PAGE, newLookup(SecurityPermission.class, ADMIN_HIGHLIGHTS_PAGE, "Provides access to the Highlights page", null, GROUPBY_HIGHLIGHT, null));
		codeMap.put(ADMIN_HIGHLIGHTS_CREATE, newLookup(SecurityPermission.class, ADMIN_HIGHLIGHTS_CREATE, "allows for creating highlights", null, GROUPBY_HIGHLIGHT, null));
		codeMap.put(ADMIN_HIGHLIGHTS_UPDATE, newLookup(SecurityPermission.class, ADMIN_HIGHLIGHTS_UPDATE, "allows for update highlights", null, GROUPBY_HIGHLIGHT, null));
		codeMap.put(ADMIN_HIGHLIGHTS_DELETE, newLookup(SecurityPermission.class, ADMIN_HIGHLIGHTS_DELETE, "allows for deleting highlights", null, GROUPBY_HIGHLIGHT, null));

		//Import Export
		codeMap.put(ADMIN_IMPORT_PAGE, newLookup(SecurityPermission.class, ADMIN_IMPORT_PAGE, "Provides access to the admin Import page", null, GROUPBY_IMPORTEXPORT, null));
		codeMap.put(ADMIN_DATA_IMPORT_EXPORT, newLookup(SecurityPermission.class, ADMIN_DATA_IMPORT_EXPORT, "export import", null, GROUPBY_IMPORTEXPORT, null));
		
		//Integrations
		codeMap.put(ADMIN_INTEGRATION_PAGE, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_PAGE, "Provides access to the Integration page", null, GROUPBY_INTEGRATION, null));
		codeMap.put(ADMIN_INTEGRATION_READ, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_READ, "Allows for reading integration", null, GROUPBY_INTEGRATION, null));
		codeMap.put(ADMIN_INTEGRATION_CREATE, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_CREATE, "create integration", null, GROUPBY_INTEGRATION, ADMIN_INTEGRATION_READ));
		codeMap.put(ADMIN_INTEGRATION_UPDATE, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_UPDATE, "Allows for updating integration", null, GROUPBY_INTEGRATION, ADMIN_INTEGRATION_READ));
		codeMap.put(ADMIN_INTEGRATION_DELETE, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_DELETE, "Allows for the removal of integration", null, GROUPBY_INTEGRATION, ADMIN_INTEGRATION_READ));
		codeMap.put(ADMIN_INTEGRATION_RUNALL, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_RUNALL, "allows for running all integrations", null, GROUPBY_INTEGRATION, ADMIN_INTEGRATION_READ));
		codeMap.put(ADMIN_INTEGRATION_RUNCONFIG, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_RUNCONFIG, "allows for running config", null, GROUPBY_INTEGRATION, ADMIN_INTEGRATION_READ));
		codeMap.put(ADMIN_INTEGRATION_RUNINTEGRATION, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_RUNINTEGRATION, "allows for running one integration", null, GROUPBY_INTEGRATION, ADMIN_INTEGRATION_READ));
		codeMap.put(ADMIN_INTEGRATION_TOGGLESTATUS, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_TOGGLESTATUS, "allows for the ability to toggle the status of an integration", null, GROUPBY_INTEGRATION, ADMIN_INTEGRATION_READ));
		codeMap.put(ADMIN_INTEGRATION_EXTERNAL, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_EXTERNAL, "Allows ability to integration abilities with external sources", null, GROUPBY_INTEGRATION, ADMIN_INTEGRATION_READ));

		// Job Management
		codeMap.put(ADMIN_JOBS_PAGE, newLookup(SecurityPermission.class, ADMIN_JOBS_PAGE, "Provides access to the admin Jobs page", null, GROUPBY_JOB_MANAGEMENT, null));
		codeMap.put(ADMIN_JOB_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_JOB_MANAGEMENT_READ, "Allows user to read jobs", null, GROUPBY_JOB_MANAGEMENT, null));
		codeMap.put(ADMIN_JOB_MANAGEMENT_ACTION, newLookup(SecurityPermission.class, ADMIN_JOB_MANAGEMENT_ACTION, "Allows user to perform some action with a job", null, GROUPBY_JOB_MANAGEMENT, ADMIN_JOB_MANAGEMENT_READ));
		codeMap.put(ADMIN_JOB_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_JOB_MANAGEMENT_DELETE, "Allows user to remove a job", null, GROUPBY_JOB_MANAGEMENT, ADMIN_JOB_MANAGEMENT_READ));
		
		// Lookups
		codeMap.put(ADMIN_LOOKUPS_PAGE, newLookup(SecurityPermission.class, ADMIN_LOOKUPS_PAGE, "Provides access to the Lookups page", null, GROUPBY_LOOKUP, null));
		codeMap.put(ADMIN_LOOKUPS_READ, newLookup(SecurityPermission.class, ADMIN_LOOKUPS_READ, "Provides the ability to read lookups", null, GROUPBY_LOOKUP, null));
		codeMap.put(ADMIN_LOOKUPS_CREATE_CODE, newLookup(SecurityPermission.class, ADMIN_LOOKUPS_CREATE_CODE, "Provides the ability to create lookups", null, GROUPBY_LOOKUP, ADMIN_LOOKUPS_READ));
		codeMap.put(ADMIN_LOOKUPS_UPDATE_CODE, newLookup(SecurityPermission.class, ADMIN_LOOKUPS_UPDATE_CODE, "Provides the ability to update a lookup(s)", null, GROUPBY_LOOKUP, ADMIN_LOOKUPS_READ));
		codeMap.put(ADMIN_LOOKUPS_DELETE_CODE, newLookup(SecurityPermission.class, ADMIN_LOOKUPS_DELETE_CODE, "Provides the ability to delete lookups", null, GROUPBY_LOOKUP, ADMIN_LOOKUPS_READ));
		
		//Media
		codeMap.put(ADMIN_MEDIA_PAGE, newLookup(SecurityPermission.class, ADMIN_MEDIA_PAGE, "Provides access to the admin Media page", null, GROUPBY_MEDIA, null));
		codeMap.put(ADMIN_SUPPORTMEDIA_PAGE, newLookup(SecurityPermission.class, ADMIN_SUPPORTMEDIA_PAGE, "Provides access to the Support Media page", null, GROUPBY_MEDIA, null));
		codeMap.put(ADMIN_MEDIA_DELETE, newLookup(SecurityPermission.class, ADMIN_MEDIA_DELETE, "Allows for deleting media", null, GROUPBY_MEDIA, null));		
		codeMap.put(ADMIN_TEMPMEDIA_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_TEMPMEDIA_MANAGEMENT_READ, "Provides the ability to read temp media", null, GROUPBY_MEDIA, null));
		codeMap.put(ADMIN_TEMPMEDIA_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_TEMPMEDIA_MANAGEMENT_DELETE, "Provides the ability to delete temp media", null, GROUPBY_MEDIA, ADMIN_TEMPMEDIA_MANAGEMENT_READ));
		codeMap.put(ADMIN_SUPPORT_MEDIA_CREATE, newLookup(SecurityPermission.class, ADMIN_SUPPORT_MEDIA_CREATE, "Allows for the uploading of support medias", null, GROUPBY_MEDIA, null));
 		codeMap.put(ADMIN_SUPPORT_MEDIA_UPDATE, newLookup(SecurityPermission.class, ADMIN_SUPPORT_MEDIA_UPDATE, "Allows for updating support medias", null, GROUPBY_MEDIA, null));
		codeMap.put(ADMIN_SUPPORT_MEDIA_DELETE, newLookup(SecurityPermission.class, ADMIN_SUPPORT_MEDIA_DELETE, "Allows for the deletion of support medias", null, GROUPBY_MEDIA, null));
		
		// Message Management
		codeMap.put(ADMIN_MESSAGES_PAGE, newLookup(SecurityPermission.class, ADMIN_MESSAGES_PAGE, "Provides access to the admin Messages/Notification page", null, GROUPBY_MESSAGE_MANAGEMENT, null));
		codeMap.put(ADMIN_MESSAGE_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT_READ, "Allows for the ability to read notification data", null, GROUPBY_MESSAGE_MANAGEMENT, null));
		codeMap.put(ADMIN_MESSAGE_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT_CREATE, "Allows for the ability to post a new notification event", null, GROUPBY_MESSAGE_MANAGEMENT, ADMIN_MESSAGE_MANAGEMENT_READ));
		codeMap.put(ADMIN_MESSAGE_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT_UPDATE, "Allow for updating a notification event", null, GROUPBY_MESSAGE_MANAGEMENT, ADMIN_MESSAGE_MANAGEMENT_READ));
		codeMap.put(ADMIN_MESSAGE_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT_DELETE, "Allows for deleting a notification event", null, GROUPBY_MESSAGE_MANAGEMENT, ADMIN_MESSAGE_MANAGEMENT_READ));
		
		// Notification Events
		codeMap.put(ADMIN_NOTIFICATION_EVENT_CREATE, newLookup(SecurityPermission.class, ADMIN_NOTIFICATION_EVENT_CREATE, "Allows for the ability to post a new notification event", null, GROUPBY_NOTIFICATION_EVENT_MANAGEMENT, null));
		codeMap.put(ADMIN_NOTIFICATION_EVENT_DELETE, newLookup(SecurityPermission.class, ADMIN_NOTIFICATION_EVENT_DELETE, "Allows for deleting a notification event", null, GROUPBY_NOTIFICATION_EVENT_MANAGEMENT, null));
		
		// Organization
		codeMap.put(ADMIN_ORGANIZATION_PAGE, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_PAGE, "Provides access to the admin Organization page", null, GROUPBY_ORGANIZATION, null));
		codeMap.put(ADMIN_ORGANIZATION_CREATE, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_CREATE, "Allows for creating orgs", null, GROUPBY_ORGANIZATION, null));
		codeMap.put(ADMIN_ORGANIZATION_UPDATE, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_UPDATE, "Allows for update orgs", null, GROUPBY_ORGANIZATION, null));
		codeMap.put(ADMIN_ORGANIZATION_DELETE, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_DELETE, "Allows for deleting organizations", null, GROUPBY_ORGANIZATION, null));
		codeMap.put(ADMIN_ORGANIZATION_MERGE, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_MERGE, "Allows for merging two organizations", null, GROUPBY_ORGANIZATION, null));
		codeMap.put(ADMIN_ORGANIZATION_EXTRACTION, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_EXTRACTION, "Allows for organization extraction", null, GROUPBY_ORGANIZATION, null));
		
		// Profile Management
		codeMap.put(USER_PROFILE_PAGE, newLookup(SecurityPermission.class, USER_PROFILE_PAGE, "Provides access to the Profile page", null, GROUPBY_PROFILE_MANAGEMENT, null));
		codeMap.put(ADMIN_USERPROFILES_PAGE, newLookup(SecurityPermission.class, ADMIN_USERPROFILES_PAGE, "Provides access to the admin User Profiles page", null, GROUPBY_PROFILE_MANAGEMENT, null));
		codeMap.put(ADMIN_USER_MANAGEMENT_PROFILES_READ, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_PROFILES_READ, "Allow for the reading of user profiles", null, GROUPBY_PROFILE_MANAGEMENT, null));
		codeMap.put(ADMIN_USER_MANAGEMENT_PROFILES_UPDATE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_PROFILES_UPDATE, "Allows for the ability to update a user profile", null, GROUPBY_PROFILE_MANAGEMENT, ADMIN_USER_MANAGEMENT_PROFILES_READ));
		codeMap.put(ADMIN_USER_MANAGEMENT_PROFILES_DELETE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_PROFILES_DELETE, "Allows for the ability to delete a user profile", null, GROUPBY_PROFILE_MANAGEMENT, ADMIN_USER_MANAGEMENT_PROFILES_READ));

		// Questions
		codeMap.put(USER_QUESTIONS_PAGE, newLookup(SecurityPermission.class, USER_QUESTIONS_PAGE, "Provides access to the Questions page", null, GROUPBY_QUESTION, null));
		codeMap.put(ADMIN_QUESTION_PAGE, newLookup(SecurityPermission.class, ADMIN_QUESTION_PAGE, "Provides access to the admin Questions page", null, GROUPBY_QUESTION, null));
		codeMap.put(ADMIN_QUESTIONS_READ, newLookup(SecurityPermission.class, ADMIN_QUESTIONS_READ, "Gives admin ability to read questions on entries", null, GROUPBY_QUESTION, null));
		codeMap.put(ADMIN_QUESTIONS_UPDATE, newLookup(SecurityPermission.class, ADMIN_QUESTIONS_UPDATE, "Gives admin ability to update questions on entries", null, GROUPBY_QUESTION, ADMIN_QUESTIONS_READ));

		// Relationships
		codeMap.put(ADMIN_RELATIONSHIPS_PAGE, newLookup(SecurityPermission.class, ADMIN_RELATIONSHIPS_PAGE, "Provides access to the admin Relationships page", null, GROUPBY_RELATIONSHIPS, null));
		codeMap.put(USER_RELATIONSHIPS_PAGE, newLookup(SecurityPermission.class, USER_RELATIONSHIPS_PAGE, "Provides access to the Relationships page", null, GROUPBY_RELATIONSHIPS, null));
		
		// Reports
		codeMap.put(REPORTS, newLookup(SecurityPermission.class, REPORTS, "General Reports", null, GROUPBY_REPORTS, null));
		codeMap.put(REPORTS_SCHEDULE, newLookup(SecurityPermission.class, REPORTS_SCHEDULE, "Reports schedule", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(REPORTS_SCHEDULE_UPDATE, newLookup(SecurityPermission.class, REPORTS_SCHEDULE_UPDATE, "Allows editing of scheduled reports", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(REPORTS_PAGE, newLookup(SecurityPermission.class, REPORTS_PAGE, "Provides access to the Reports page", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(REPORTS_CREATE, newLookup(SecurityPermission.class, REPORTS_CREATE, "Allows to create a new report", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(REPORTS_DELETE, newLookup(SecurityPermission.class, REPORTS_DELETE, "Allows to delete a report", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(REPORTS_ALL, newLookup(SecurityPermission.class, REPORTS_ALL, "Reports All - Allows viewing reports from all users", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(REPORT_OUTPUT_EMAIL_ATTACH, newLookup(SecurityPermission.class, REPORT_OUTPUT_EMAIL_ATTACH, "Allow user to run report as attached email", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(REPORT_OUTPUT_EMAIL_BODY, newLookup(SecurityPermission.class, REPORT_OUTPUT_EMAIL_BODY, "Allow user to run report as the content of an email body", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(RUN_ACTION_REPORT, newLookup(SecurityPermission.class, RUN_ACTION_REPORT, "Allows user to run the Action report", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(RUN_ENTRIES_BY_CAT_REPORT, newLookup(SecurityPermission.class, RUN_ENTRIES_BY_CAT_REPORT, "Allows user to run the Entries by Category report", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(RUN_ENTRIES_BY_ORG_REPORT, newLookup(SecurityPermission.class, RUN_ENTRIES_BY_ORG_REPORT, "Allows user to run the Entries by Organization report", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(RUN_ENTRY_REPORT, newLookup(SecurityPermission.class, RUN_ENTRY_REPORT, "Allows user to run the Entry report", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(RUN_ENTRY_DETAIL_REPORT, newLookup(SecurityPermission.class, RUN_ENTRY_DETAIL_REPORT, "Allows user to run the Entry Detail report", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(RUN_ENTRY_LISTING_REPORT, newLookup(SecurityPermission.class, RUN_ENTRY_LISTING_REPORT, "Allows user to run the Entry Listing report", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(RUN_ENTRY_STATUS_REPORT, newLookup(SecurityPermission.class, RUN_ENTRY_STATUS_REPORT, "Allows user to run the Entry Status report", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(RUN_EVAL_STATUS_REPORT, newLookup(SecurityPermission.class, RUN_EVAL_STATUS_REPORT, "Allows user to run the Evaluation Status report", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(RUN_LINK_VALIDATION_REPORT, newLookup(SecurityPermission.class, RUN_LINK_VALIDATION_REPORT, "Allows user to run the Link Validation report", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(RUN_SUBMISSIONS_REPORT, newLookup(SecurityPermission.class, RUN_SUBMISSIONS_REPORT, "Allows user to run the Sumbissions report", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(RUN_USAGE_REPORT, newLookup(SecurityPermission.class, RUN_USAGE_REPORT, "Allows user to run the Usage report", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(RUN_USER_REPORT, newLookup(SecurityPermission.class, RUN_USER_REPORT, "Allows user to run the User report", null, GROUPBY_REPORTS, REPORTS));
		codeMap.put(RUN_USER_ORG_REPORT, newLookup(SecurityPermission.class, RUN_USER_ORG_REPORT, "Allows user to run the User by Organization report", null, GROUPBY_REPORTS, REPORTS));

		// Reviews
		codeMap.put(USER_REVIEWS_PAGE, newLookup(SecurityPermission.class, USER_REVIEWS_PAGE, "Provides access to the Reviews page", null, GROUPBY_REVIEW, null));
		codeMap.put(ADMIN_REVIEWS_PAGE, newLookup(SecurityPermission.class, ADMIN_REVIEWS_PAGE, "Provides access to the admin Reviews page", null, GROUPBY_REVIEW, null));
		codeMap.put(ADMIN_REVIEW_READ, newLookup(SecurityPermission.class, ADMIN_REVIEW_READ, "Gives ability to read reviews", null, GROUPBY_REVIEW, null));
		codeMap.put(ADMIN_REVIEW_UPDATE, newLookup(SecurityPermission.class, ADMIN_REVIEW_UPDATE, "Gives ability to update reviews", null, GROUPBY_REVIEW, ADMIN_REVIEW_READ));
		codeMap.put(ADMIN_REVIEW_DELETE, newLookup(SecurityPermission.class, ADMIN_REVIEW_DELETE, "Gives ability to delete reviews", null, GROUPBY_REVIEW, ADMIN_REVIEW_READ));

		// Role
		codeMap.put(ADMIN_ROLES_PAGE, newLookup(SecurityPermission.class, ADMIN_ROLES_PAGE, "Provides access to the admin Roles page", null, GROUPBY_ROLE, null));
		codeMap.put(ADMIN_ROLE_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT_READ, "Gives ability to read roles", null, GROUPBY_ROLE, null));
		codeMap.put(ADMIN_ROLE_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT_CREATE, "Gives ability to create roles", null, GROUPBY_ROLE, ADMIN_ROLE_MANAGEMENT_READ));
		codeMap.put(ADMIN_ROLE_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT_UPDATE, "Gives ability to update roles", null, GROUPBY_ROLE, ADMIN_ROLE_MANAGEMENT_READ));
		codeMap.put(ADMIN_ROLE_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT_DELETE, "Gives ability to delete roles", null, GROUPBY_ROLE, ADMIN_ROLE_MANAGEMENT_READ));

		// Search
		codeMap.put(USER_SEARCHES_PAGE, newLookup(SecurityPermission.class, USER_SEARCHES_PAGE, "Provides access to the Searches page", null, GROUPBY_SEARCH, null));
		codeMap.put(ADMIN_SEARCHES_PAGE, newLookup(SecurityPermission.class, ADMIN_SEARCHES_PAGE, "Provides access to the admin Searches page", null, GROUPBY_SEARCH, null));
		codeMap.put(ADMIN_SEARCH_READ, newLookup(SecurityPermission.class, ADMIN_SEARCH_READ, "read all searches", null, GROUPBY_SEARCH, null));
		codeMap.put(ADMIN_SEARCH_CREATE, newLookup(SecurityPermission.class, ADMIN_SEARCH_CREATE, "create a search", null, GROUPBY_SEARCH, ADMIN_SEARCH_READ));
		codeMap.put(ADMIN_SEARCH_UPDATE, newLookup(SecurityPermission.class, ADMIN_SEARCH_UPDATE, "update a search", null, GROUPBY_SEARCH, ADMIN_SEARCH_READ));
		codeMap.put(ADMIN_SEARCH_DELETE, newLookup(SecurityPermission.class, ADMIN_SEARCH_DELETE, "delete a search", null, GROUPBY_SEARCH, ADMIN_SEARCH_READ));
		
		//Security
		codeMap.put(ADMIN_SECURITY_PAGE, newLookup(SecurityPermission.class, ADMIN_SECURITY_PAGE, "Provides access to the admin Security page", null, GROUPBY_ADMIN_SECURITY, null));
		codeMap.put(ADMIN_SECURITY_POLICY, newLookup(SecurityPermission.class, ADMIN_SECURITY_POLICY, "Access the security policy tab", null, GROUPBY_ADMIN_SECURITY, null));
		codeMap.put(ADMIN_SECURITY_SHIRO_CONFIG, newLookup(SecurityPermission.class, ADMIN_SECURITY_SHIRO_CONFIG, "Access the Shiro Config", null, GROUPBY_ADMIN_SECURITY, null));

		//Submissions (Admin)
		codeMap.put(ADMIN_PARTIAL_SUBMISSIONS_PAGE, newLookup(SecurityPermission.class, ADMIN_PARTIAL_SUBMISSIONS_PAGE, "Provides access to the partial submission page", null, GROUPBY_ADMIN_USER_SUBMISSION, null));
		codeMap.put(ADMIN_SUBMISSION_FORM_SANDBOX_PAGE, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_SANDBOX_PAGE, "Provides access to the sandbox submission page", null, GROUPBY_ADMIN_USER_SUBMISSION, null));
		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE_PAGE, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE_PAGE, "Provides access to the admin custom submission form template page", null, GROUPBY_ADMIN_USER_SUBMISSION, null));
		codeMap.put(ADMIN_USER_SUBMISSIONS_READ, newLookup(SecurityPermission.class, ADMIN_USER_SUBMISSIONS_READ, "Allows for reading user submissions", null, GROUPBY_ADMIN_USER_SUBMISSION, null));
		codeMap.put(ADMIN_USER_SUBMISSIONS_UPDATE, newLookup(SecurityPermission.class, ADMIN_USER_SUBMISSIONS_UPDATE, "Allows for update user submissions", null, GROUPBY_ADMIN_USER_SUBMISSION, ADMIN_USER_SUBMISSIONS_READ));
		codeMap.put(ADMIN_USER_SUBMISSIONS_DELETE, newLookup(SecurityPermission.class, ADMIN_USER_SUBMISSIONS_DELETE, "Allows for deleting user submissions", null, GROUPBY_ADMIN_USER_SUBMISSION, ADMIN_USER_SUBMISSIONS_READ));
		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE_READ, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE_READ, "Allows for reading submission form templates", null, GROUPBY_ADMIN_USER_SUBMISSION, null));
		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE_CREATE, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE_CREATE, "Allows for creating submission form templates", null, GROUPBY_ADMIN_USER_SUBMISSION, ADMIN_SUBMISSION_FORM_TEMPLATE_READ));
		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE_UPDATE, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE_UPDATE, "Allows for updating submission form templates", null, GROUPBY_ADMIN_USER_SUBMISSION, ADMIN_SUBMISSION_FORM_TEMPLATE_READ));
		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE_DELETE, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE_DELETE, "Allows for deleting submission form templates", null, GROUPBY_ADMIN_USER_SUBMISSION, ADMIN_SUBMISSION_FORM_TEMPLATE_READ));

		//Submissions (User)
		codeMap.put(USER_SUBMISSIONS_PAGE, newLookup(SecurityPermission.class, USER_SUBMISSIONS_PAGE, "Provides access to the Submission page", null, GROUPBY_USER_SUBMISSION, null));
		codeMap.put(USER_SUBMISSIONS_READ, newLookup(SecurityPermission.class, USER_SUBMISSIONS_READ, "Allow user submission reading", null, GROUPBY_USER_SUBMISSION, null));
		codeMap.put(USER_SUBMISSIONS_CREATE, newLookup(SecurityPermission.class, USER_SUBMISSIONS_CREATE, "Allow user submission creation", null, GROUPBY_USER_SUBMISSION, USER_SUBMISSIONS_READ));
		codeMap.put(USER_SUBMISSIONS_UPDATE, newLookup(SecurityPermission.class, USER_SUBMISSIONS_UPDATE, "Allow user submission updates", null, GROUPBY_USER_SUBMISSION, USER_SUBMISSIONS_READ));
		codeMap.put(USER_SUBMISSIONS_DELETE, newLookup(SecurityPermission.class, USER_SUBMISSIONS_DELETE, "Allow user submission deleting", null, GROUPBY_USER_SUBMISSION, USER_SUBMISSIONS_READ));
		codeMap.put(USER_SUBMISSIONS_CHANGEREQUEST, newLookup(SecurityPermission.class, USER_SUBMISSIONS_CHANGEREQUEST, "Allow user to create change requests", null, GROUPBY_USER_SUBMISSION, USER_SUBMISSIONS_READ));

		// System Management
		codeMap.put(ADMIN_SYSTEM_PAGE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_PAGE, "Provides access to the admin System page (ONLY FOR ADMINS)", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_ARCHIVES_PAGE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_ARCHIVES_PAGE, "Provides access to the admin System Archives page", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT, "General sys admin permission", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_APP_PROP, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_APP_PROP, "Allows for updating app properties", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_PLUGIN, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_PLUGIN, "Allows for management of plugins", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_READ, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_READ, "Allows for sys archive management", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_CREATE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_CREATE, "Allows for sys archive creation", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_DELETE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_ARCHIVE_DELETE, "Allows for sys archive deletion", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_ERROR_TICKET, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_ERROR_TICKET, "Allows for sys error ticket management", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_SEARCH_CONTROL, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_SEARCH_CONTROL, "Allows for sys search management", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_READ, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_READ, "Allows for reading application meta data", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_UPDATE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_UPDATE, "Allows for updating system configs", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_DELETE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_CONFIG_PROP_DELETE, "Allows for removal/clearing operations for the sys", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_LOGGING, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_LOGGING, "Access to permission logging", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_MANAGERS, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_MANAGERS, "System managers", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_CACHE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_CACHE, "Access to system cache", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_RECENT_CHANGES, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_RECENT_CHANGES, "System recent changes", null, GROUPBY_SYSTEM_MANAGEMENT, null));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_STATUS, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_STATUS, "Access to system status", null, GROUPBY_SYSTEM_MANAGEMENT, null));

		//Tags
		codeMap.put(ADMIN_TAGS_PAGE, newLookup(SecurityPermission.class, ADMIN_TAGS_PAGE, "Provides access to the admin Tags page", null, GROUPBY_TAGS, null));
		
		// Tracking
		codeMap.put(ADMIN_TRACKING_PAGE, newLookup(SecurityPermission.class, ADMIN_TRACKING_PAGE, "Provides access to the admin Tracking page", null, GROUPBY_TRACKING, null));
		codeMap.put(ADMIN_TRACKING_READ, newLookup(SecurityPermission.class, ADMIN_TRACKING_READ, "read tracking", null, GROUPBY_TRACKING, null));
		codeMap.put(ADMIN_TRACKING_UPDATE, newLookup(SecurityPermission.class, ADMIN_TRACKING_UPDATE, "update tracking on an item", null, GROUPBY_TRACKING, ADMIN_TRACKING_READ));
		codeMap.put(ADMIN_TRACKING_DELETE, newLookup(SecurityPermission.class, ADMIN_TRACKING_DELETE, "delete tracking from an item", null, GROUPBY_TRACKING, ADMIN_TRACKING_READ));
		
		//Users (Admin)
		codeMap.put(ADMIN_USER_MANAGEMENT_PAGE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_PAGE, "Provides access to the admin User Management page", null, GROUPBY_USER_MANAGEMENT, null));
		codeMap.put(ADMIN_USER_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_READ, "Allow admin user submission reading", null, GROUPBY_USER_MANAGEMENT, null));
		codeMap.put(ADMIN_USER_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_CREATE, "Allow admin user submission creation", null, GROUPBY_USER_MANAGEMENT, ADMIN_USER_MANAGEMENT_READ));
		codeMap.put(ADMIN_USER_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_UPDATE, "Allow admin user submission updating", null, GROUPBY_USER_MANAGEMENT, ADMIN_USER_MANAGEMENT_READ));
		codeMap.put(ADMIN_USER_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_DELETE, "Allow admin user submission deleting", null, GROUPBY_USER_MANAGEMENT, ADMIN_USER_MANAGEMENT_READ));
		
		// Watches
		codeMap.put(ADMIN_WATCHES_PAGE, newLookup(SecurityPermission.class, ADMIN_WATCHES_PAGE, "Provides access to the admin Watches page", null, GROUPBY_WATCHES, null));
		codeMap.put(USER_WATCHES_PAGE, newLookup(SecurityPermission.class, USER_WATCHES_PAGE, "Provides access to the Watches page", null, GROUPBY_WATCHES, null));
		codeMap.put(ADMIN_WATCHES_READ, newLookup(SecurityPermission.class, ADMIN_WATCHES_READ, "ability to read watches", null, GROUPBY_WATCHES, null));
		codeMap.put(ADMIN_WATCHES_UPDATE, newLookup(SecurityPermission.class, ADMIN_WATCHES_UPDATE, "update watches", null, GROUPBY_WATCHES, ADMIN_WATCHES_READ));
		codeMap.put(ADMIN_WATCHES_DELETE, newLookup(SecurityPermission.class, ADMIN_WATCHES_DELETE, "delete watches", null, GROUPBY_WATCHES, ADMIN_WATCHES_READ));
	
		
		// ========================================================================================================================
		codeMap.put(ADMIN_SUPPORT_MEDIA, newLookup(SecurityPermission.class, ADMIN_SUPPORT_MEDIA, "Support media general permission", null, GROUPBY_OLD, null));
		codeMap.put(ENTRY_TAG, newLookup(SecurityPermission.class, ENTRY_TAG, "Entry tag", null, GROUPBY_OLD, null));
		codeMap.put(USER_SUBMISSIONS, newLookup(SecurityPermission.class, USER_SUBMISSIONS, "User submissions", null, GROUPBY_OLD, null));
		codeMap.put(EVALUATIONS, newLookup(SecurityPermission.class, EVALUATIONS, "General Evaluations", null, GROUPBY_OLD, null));
		codeMap.put(RELATIONSHIP_VIEW_TOOL, newLookup(SecurityPermission.class, RELATIONSHIP_VIEW_TOOL, "Relationship View Tool", null, GROUPBY_OLD, null));
		codeMap.put(REPORT_ENTRY_LISTING_REPORT, newLookup(SecurityPermission.class, REPORT_ENTRY_LISTING_REPORT, "Allows running entry listing report", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_USER_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT, "Admin user management", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_ENTRY_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_MANAGEMENT, "Admin entry management", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_MESSAGE_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT, "Admin message management", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_JOB_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_JOB_MANAGEMENT, "Admin job management", null,GROUPBY_OLD, null));
		codeMap.put(ADMIN_INTEGRATION, newLookup(SecurityPermission.class, ADMIN_INTEGRATION, "Admin integration", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_WATCHES, newLookup(SecurityPermission.class, ADMIN_WATCHES, "Admin watches", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_TRACKING, newLookup(SecurityPermission.class, ADMIN_TRACKING, "Admin tracking", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_SEARCH, newLookup(SecurityPermission.class, ADMIN_SEARCH, "Admin search", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_USER_MANAGEMENT_PROFILES, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_PROFILES, "Admin user management profiles", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_TEMPMEDIA_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_TEMPMEDIA_MANAGEMENT, "Admin tempmedia management", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_ORGANIZATION, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION, "Admin organization", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_LOOKUPS, newLookup(SecurityPermission.class, ADMIN_LOOKUPS, "Admin lookups", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_HIGHLIGHTS, newLookup(SecurityPermission.class, ADMIN_HIGHLIGHTS, "Admin highlights", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_MEDIA, newLookup(SecurityPermission.class, ADMIN_MEDIA, "Admin media", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_FEEDBACK, newLookup(SecurityPermission.class, ADMIN_FEEDBACK, "Admin feedback", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE, "Admin evalution template", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_BRANDING, newLookup(SecurityPermission.class, ADMIN_BRANDING, "Admin branding", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION, "Admin evalution template section", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_CONTACT_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_CONTACT_MANAGEMENT, "Admin contact management", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_ENTRY_TEMPLATES, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES, "Admin entry templates", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_USER_SUBMISSIONS, newLookup(SecurityPermission.class, ADMIN_USER_SUBMISSIONS, "Admin user submissions", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_ENTRY_TYPES, newLookup(SecurityPermission.class, ADMIN_ENTRY_TYPES, "Admin entry types", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_QUESTIONS, newLookup(SecurityPermission.class, ADMIN_QUESTIONS, "Admin questions", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_REVIEW, newLookup(SecurityPermission.class, ADMIN_REVIEW, "Admin review", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST, "Admin evalution template checklist", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION, "Admin evalution template checklist question", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_ATTRIBUTE_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ATTRIBUTE_MANAGEMENT, "Admin attribute management", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_ALERT_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT, "Admin alert management", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_EVALUATION_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_EVALUATION_MANAGEMENT, "Admin evaluation management", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_ROLE_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT, "Admin security role management", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_FAQ, newLookup(SecurityPermission.class, ADMIN_FAQ, "Admin FAQ management", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE, "Admin Custom Submission Form management", null, GROUPBY_OLD, null));
		codeMap.put(ADMIN_SECURITY, newLookup(SecurityPermission.class, ADMIN_SECURITY, "Security general permission", null, GROUPBY_OLD, null));
		codeMap.put(ALLOW_USER_ATTRIBUTE_TYPE_CREATION, newLookup(SecurityPermission.class, ALLOW_USER_ATTRIBUTE_TYPE_CREATION, "Allows for creating attribute types", null, GROUPBY_OLD, null));
		codeMap.put(RELATION_VIEW_TOOL, newLookup(SecurityPermission.class, RELATION_VIEW_TOOL, "Allows for use of the view tool?", null, GROUPBY_OLD, null));
		codeMap.put(API_DOCS, newLookup(SecurityPermission.class, API_DOCS, "Allows said user to view API docs", null, GROUPBY_OLD, null));
		
		// ========================================================================================================================

		return codeMap;
	}
}
