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
import static edu.usu.sdl.openstorefront.core.entity.LookupEntity.newLookup;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 * @author cyearsley
 */
@SystemTable
@APIDescription("Defines Security Permissions")
public class SecurityPermission
		extends LookupEntity<SecurityPermission>
{

	private static final long serialVersionUID = 1L;
	//========================================================================================================================
	public static final String ENTRY_TAG = "ENTRY-TAG";
	// public static final String REPORTS_SCHEDULE = "REPORTS-SCHEDULE";
	// public static final String REPORTS = "REPORTS";
	// public static final String REPORTS_ALL = "REPORTS-ALL";
	public static final String USER_SUBMISSIONS = "USER-SUBMISSIONS";
	public static final String EVALUATIONS = "EVALUATIONS";
	// public static final String ALLOW_USER_ATTRIBUTE_TYPE_CREATION = "ALLOW-USER-ATTRIBUTE-TYPE-CREATION";

	// public static final String REPORT_ACTION_REPORT = "REPORT-ACTION-REPORT";
	public static final String REPORT_ENTRY_LISTING_REPORT = "REPORT-ENTRYLISTING-REPORT";
	// public static final String REPORT_OUTPUT_EMAIL_ATTACH = "REPORT-OUTPUT-EMAIL-ATTACH";
	// public static final String REPORT_OUTPUT_EMAIL_BODY = "REPORT-OUTPUT-EMAIL-BODY";

	public static final String RELATIONSHIP_VIEW_TOOL = "RELATION-VIEW-TOOL";
	public static final String ADMIN_USER_MANAGEMENT = "ADMIN-USER-MANAGEMENT";
	// public static final String ADMIN_SYSTEM_MANAGEMENT = "ADMIN-SYSTEM-MANAGEMENT";
	public static final String ADMIN_ENTRY_MANAGEMENT = "ADMIN-ENTRY-MANAGEMENT";
	public static final String ADMIN_MESSAGE_MANAGEMENT = "ADMIN-MESSAGE-MANAGEMENT";
	public static final String ADMIN_JOB_MANAGEMENT = "ADMIN-JOB-MANAGEMENT";
	public static final String ADMIN_INTEGRATION = "ADMIN-INTEGRATION";
	// public static final String ADMIN_DATA_IMPORT_EXPORT = "ADMIN-DATA-IMPORT-EXPORT";
	public static final String ADMIN_WATCHES = "ADMIN-WATCHES";
	public static final String ADMIN_TRACKING = "ADMIN-TRACKING";
	public static final String ADMIN_SEARCH = "ADMIN-SEARCH";
	public static final String ADMIN_USER_MANAGEMENT_PROFILES = "ADMIN-USER-MANAGEMENT-PROFILES";
	public static final String ADMIN_TEMPMEDIA_MANAGEMENT = "ADMIN-TEMPMEDIA-MANAGEMENT";
	public static final String ADMIN_ORGANIZATION = "ADMIN-ORGANIZATION";
	// public static final String ADMIN_ORGANIZATION_EXTRACTION = "ADMIN-ORGANIZATION-EXTRACTION";
	public static final String ADMIN_LOOKUPS = "ADMIN-LOOKUPS";
	public static final String ADMIN_HIGHLIGHTS = "ADMIN-HIGHLIGHTS";
	public static final String ADMIN_MEDIA = "ADMIN-MEDIA";
	public static final String ADMIN_FEEDBACK = "ADMIN-FEEDBACK";
	public static final String ADMIN_EVALUATION_TEMPLATE = "ADMIN-EVALUATION-TEMPLATE";
	// public static final String API_DOCS = "API-DOCS";
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
	// public static final String ADMIN_SECURITY = "ADMIN-SECURITY";
	public static final String ADMIN_FAQ = "ADMIN-FAQ";
	public static final String ADMIN_SUBMISSION_FORM_TEMPLATE = "ADMIN-SUBMISSION-FORM-TEMPLATE";

	// public static final String ADMIN_SUPPORT_MEDIA = "ADMIN-SUPPORT-MEDIA";
	// ========================================================================================================================

	// Reports
	public static final String REPORTS_SCHEDULE = "REPORTS-SCHEDULE";
	public static final String REPORTS = "REPORTS";
	public static final String REPORTS_ALL = "REPORTS-ALL";

	// User submissions/management permissions
	public static final String USER_SUBMISSIONS_UPDATE = "USER-SUBMISSIONS-UPDATE";
	public static final String USER_SUBMISSIONS_CREATE = "USER-SUBMISSIONS-CREATE";
	public static final String USER_SUBMISSIONS_READ = "USER-SUBMISSIONS-READ";
	public static final String USER_SUBMISSIONS_DELETE = "USER-SUBMISSIONS-DELETE";

	public static final String ADMIN_USER_MANAGEMENT_DELETE = "ADMIN-USER-MANAGEMENT-DELETE";
	public static final String ADMIN_USER_MANAGEMENT_READ = "ADMIN-USER-MANAGEMENT-READ";
	public static final String ADMIN_USER_MANAGEMENT_UPDATE = "ADMIN-USER-MANAGEMENT-UPDATE";
	public static final String ADMIN_USER_MANAGEMENT_CREATE = "ADMIN-USER-MANAGEMENT-CREATE";

	// Report permissions
	public static final String REPORT_ACTION_REPORT = "REPORT-ACTION-REPORT";
	public static final String REPORT_ENTRYLISTING_REPORT = "REPORT-ENTRYLISTING-REPORT";
	public static final String REPORT_OUTPUT_EMAIL_ATTACH = "REPORT-OUTPUT-EMAIL-ATTACH";
	public static final String REPORT_OUTPUT_EMAIL_BODY = "REPORT-OUTPUT-EMAIL-BODY";

	// System Management
	public static final String ADMIN_SYSTEM_MANAGEMENT = "ADMIN-SYSTEM-MANAGEMENT";
	public static final String ADMIN_SYSTEM_MANAGEMENT_UPDATE_APPPROP = "ADMIN-SYSTEM-MANAGEMENT-UPDATE-APPPROP";
	public static final String ADMIN_SYSTEM_MANAGEMENT_PLUGIN = "ADMIN-SYSTEM-MANAGEMENT-PLUGIN";
	public static final String ADMIN_SYSTEM_MANAGEMENT_ARCHIVE = "ADMIN-SYSTEM-MANAGEMENT-ARCHIVE";
	public static final String ADMIN_SYSTEM_MANAGEMENT_ERROR_TICKET = "ADMIN-SYSTEM-MANAGEMENT-ERROR-TICKET";
	public static final String ADMIN_SYSTEM_MANAGEMENT_SEARCH = "ADMIN-SYSTEM-MANAGEMENT-SEARCH";
	public static final String ADMIN_SYSTEM_MANAGEMENT_APP_READ = "ADMIN-SYSTEM-MANAGEMENT-APP-READ";
	public static final String ADMIN_SYSTEM_MANAGEMENT_APP_DELETE = "ADMIN-SYSTEM-MANAGEMENT-APP-DELETE";
	public static final String ADMIN_SYSTEM_MANAGEMENT_APP_UPDATE = "ADMIN-SYSTEM-MANAGEMENT-APP-UPDATE";

	// Entry Management
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
	public static final String ADMIN_ENTRY_CHANGEREQUEST_APPROVE = "ADMIN-ENTRY-CHANGEREQUEST-APPROVE";
	public static final String ADMIN_ENTRY_CHANGEOWNER = "ADMIN-ENTRY-CHANGEOWNER";
	public static final String ADMIN_ENTRY_EXPORT = "ADMIN-ENTRY-EXPORT";
	public static final String ADMIN_ENTRY_PENDINGCHANGE_READ = "ADMIN-ENTRY-PENDINGCHANGE-READ";
	public static final String ADMIN_ENTRY_ASSIGNUSER = "ADMIN-ENTRY-ASSIGNUSER";
	
	// Entry Templates
	public static final String ADMIN_ENTRY_TEMPLATES_CREATE = "ADMIN-ENTRY-TEMPLATES-CREATE";
	public static final String ADMIN_ENTRY_TEMPLATES_UPDATE = "ADMIN-ENTRY-TEMPLATES-UPDATE";
	public static final String ADMIN_ENTRY_TEMPLATES_DELETE = "ADMIN-ENTRY-TEMPLATES-DELETE";
	public static final String ADMIN_ENTRY_TEMPLATES_READ = "ADMIN-ENTRY-TEMPLATES-READ";

	// Entry Types
	public static final String ADMIN_ENTRY_TYPES_CREATE = "ADMIN-ENTRY-TYPES-CREATE";
	public static final String ADMIN_ENTRY_TYPES_DELETE = "ADMIN-ENTRY-TYPES-DELETE";
	public static final String ADMIN_ENTRY_TYPES_UPDATE = "ADMIN-ENTRY-TYPES-UPDATE";

	// Message Management
	public static final String ADMIN_MESSAGE_MANAGEMENT_READ = "ADMIN-MESSAGE-MANAGEMENT-READ";
	public static final String ADMIN_MESSAGE_MANAGEMENT_CREATE = "ADMIN-MESSAGE-MANAGEMENT-CREATE";
	public static final String ADMIN_MESSAGE_MANAGEMENT_DELETE = "ADMIN-MESSAGE-MANAGEMENT-DELETE";
	public static final String ADMIN_MESSAGE_MANAGEMENT_UPDATE = "ADMIN-MESSAGE-MANAGEMENT-UPDATE";

	// Job Management
	public static final String ADMIN_JOB_MANAGEMENT_ACTION = "ADMIN-JOB-MANAGEMENT-ACTION";
	public static final String ADMIN_JOB_MANAGEMENT_DELETE = "ADMIN-JOB-MANAGEMENT-DELETE";
	public static final String ADMIN_JOB_MANAGEMENT_READ = "ADMIN-JOB-MANAGEMENT-READ";

	// Integration
	public static final String ADMIN_INTEGRATION_DELETE = "ADMIN-INTEGRATION-DELETE";
	public static final String ADMIN_INTEGRATION_UPDATE = "ADMIN-INTEGRATION-UPDATE";
	public static final String ADMIN_INTEGRATION_READ = "ADMIN-INTEGRATION-READ";
	public static final String ADMIN_INTEGRATION_CREATE = "ADMIN-INTEGRATION-CREATE";
	public static final String ADMIN_INTEGRATION_RUNALL = "ADMIN-INTEGRATION-RUNALL";
	public static final String ADMIN_INTEGRATION_RUNCONFIG = "ADMIN-INTEGRATION-RUNCONFIG";
	public static final String ADMIN_INTEGRATION_RUNINTEGRATION = "ADMIN-INTEGRATION-RUNINTEGRATION";
	public static final String ADMIN_INTEGRATION_TOGGLESTATUS = "ADMIN-INTEGRATION-TOGGLESTATUS";

	// Watches
	public static final String ADMIN_WATCHES_DELETE = "ADMIN-WATCHES-DELETE";
	public static final String ADMIN_WATCHES_UPDATE = "ADMIN-WATCHES-UPDATE";
	public static final String ADMIN_WATCHES_READ = "ADMIN-WATCHES-READ";

	// Tracking
	public static final String ADMIN_TRACKING_READ = "ADMIN-TRACKING-READ";
	public static final String ADMIN_TRACKING_DELETE = "ADMIN-TRACKING-DELETE";
	public static final String ADMIN_TRACKING_UPDATE = "ADMIN-TRACKING-UPDATE";

	// Search
	public static final String ADMIN_SEARCH_DELETE = "ADMIN-SEARCH-DELETE";
	public static final String ADMIN_SEARCH_CREATE = "ADMIN-SEARCH-CREATE";
	public static final String ADMIN_SEARCH_UPDATE = "ADMIN-SEARCH-UPDATE";
	public static final String ADMIN_SEARCH_READ = "ADMIN-SEARCH-READ";

	// Profile Management
	public static final String ADMIN_USER_MANAGEMENT_PROFILES_READ = "ADMIN-USER-MANAGEMENT-PROFILES-READ";
	public static final String ADMIN_USER_MANAGEMENT_PROFILES_UPDATE = "ADMIN-USER-MANAGEMENT-PROFILES-UPDATE";
	public static final String ADMIN_USER_MANAGEMENT_PROFILES_DELETE = "ADMIN-USER-MANAGEMENT-PROFILES-DELETE";

	// User Submissions
	public static final String ADMIN_USER_SUBMISSIONS_READ = "ADMIN-USER-SUBMISSIONS-READ";
	public static final String ADMIN_USER_SUBMISSIONS_UPDATE = "ADMIN-USER-SUBMISSIONS-UPDATE";
	public static final String ADMIN_USER_SUBMISSIONS_DELETE = "ADMIN-USER-SUBMISSIONS-DELETE";

	// Temp Media
	public static final String ADMIN_TEMPMEDIA_MANAGEMENT_READ = "ADMIN-TEMPMEDIA-MANAGEMENT-READ";
	public static final String ADMIN_TEMPMEDIA_MANAGEMENT_DELETE = "ADMIN-TEMPMEDIA-MANAGEMENT-DELETE";

	// Organization
	public static final String ADMIN_ORGANIZATION_CREATE = "ADMIN-ORGANIZATION-CREATE";
	public static final String ADMIN_ORGANIZATION_UPDATE = "ADMIN-ORGANIZATION-UPDATE";
	public static final String ADMIN_ORGANIZATION_DELETE = "ADMIN-ORGANIZATION-DELETE";
	public static final String ADMIN_ORGANIZATION_MERGE = "ADMIN-ORGANIZATION-MERGE";

	// Lookups
	public static final String ADMIN_LOOKUPS_CREATE = "ADMIN-LOOKUPS-CREATE";
	public static final String ADMIN_LOOKUPS_READ = "ADMIN-LOOKUPS-READ";
	public static final String ADMIN_LOOKUPS_UPDATE = "ADMIN-LOOKUPS-UPDATE";
	public static final String ADMIN_LOOKUPS_DELETE = "ADMIN-LOOKUPS-DELETE";

	// Highlights
	public static final String ADMIN_HIGHLIGHTS_CREATE = "ADMIN-HIGHLIGHTS-CREATE";
	public static final String ADMIN_HIGHLIGHTS_UPDATE = "ADMIN-HIGHLIGHTS-UPDATE";
	public static final String ADMIN_HIGHLIGHTS_DELETE = "ADMIN-HIGHLIGHTS-DELETE";

	// Feedback
	public static final String ADMIN_FEEDBACK_READ = "ADMIN-FEEDBACK-READ";
	public static final String ADMIN_FEEDBACK_UPDATE = "ADMIN-FEEDBACK-UPDATE";
	public static final String ADMIN_FEEDBACK_DELETE = "ADMIN-FEEDBACK-DELETE";
	
	// Evaluation
	public static final String ADMIN_EVALUATION_TEMPLATE_CREATE = "ADMIN-EVALUATION-TEMPLATE-CREATE";
	public static final String ADMIN_EVALUATION_TEMPLATE_READ = "ADMIN-EVALUATION-TEMPLATE-READ";
	public static final String ADMIN_EVALUATION_TEMPLATE_UPDATE = "ADMIN-EVALUATION-TEMPLATE-UPDATE";
	public static final String ADMIN_EVALUATION_TEMPLATE_DELETE = "ADMIN-EVALUATION-TEMPLATE-DELETE";

	public static final String ADMIN_EVALUATION_TEMPLATE_SECTION_CREATE = "ADMIN-EVALUATION-TEMPLATE-SECTION-CREATE";
	public static final String ADMIN_EVALUATION_TEMPLATE_SECTION_READ = "ADMIN-EVALUATION-TEMPLATE-SECTION-READ";
	public static final String ADMIN_EVALUATION_TEMPLATE_SECTION_UPDATE = "ADMIN-EVALUATION-TEMPLATE-SECTION-UPDATE";
	public static final String ADMIN_EVALUATION_TEMPLATE_SECTION_DELETE = "ADMIN-EVALUATION-TEMPLATE-SECTION-DELETE";

	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_DELETE = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-DELETE";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-READ";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_CREATE = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-CREATE";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_UPDATE = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-UPDATE";

	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_DELETE = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION-DELETE";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION-READ";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_CREATE = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION-CREATE";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_UPDATE = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION-UPDATE";

	// Evaluation Management
	public static final String ADMIN_EVALUATION_MANAGEMENT_CREATE = "ADMIN-EVALUATION-MANAGEMENT-CREATE";
	public static final String ADMIN_EVALUATION_MANAGEMENT_DELETE = "ADMIN-EVALUATION-MANAGEMENT-DELETE";
	public static final String ADMIN_EVALUATION_ACTIVATE = "ADMIN-EVALUATION-ACTIVATE";
	public static final String ADMIN_EVALUATION_ALLOW_NEW_SECTIONS = "ADMIN-EVALUATION-ALLOW-NEW-SECTIONS";
	public static final String ADMIN_EVALUATION_ALLOW_QUESTION_MANAGEMENT = "ADMIN-EVALUATION-ALLOW-QUESTION-MANAGEMENT";
	public static final String ADMIN_EVALUATION_TOGGLE_PUBLISH = "ADMIN-EVALUATION-TOGGLE-PUBLISH";
	public static final String ADMIN_EVALUATION_PUBLISH_SUMMARY = "ADMIN-EVALUATION-PUBLISH-SUMMARY";
	public static final String ADMIN_EVALUATION_DELETE_COMMENT = "ADMIN-EVALUATION-DELETE-COMMENT";

	// Branding
	public static final String ADMIN_BRANDING_DELETE = "ADMIN-BRANDING-DELETE";
	public static final String ADMIN_BRANDING_CREATE = "ADMIN-BRANDING-CREATE";
	public static final String ADMIN_BRANDING_UPDATE = "ADMIN-BRANDING-UPDATE";

	// Contact
	public static final String ADMIN_CONTACT_MANAGEMENT_CREATE = "ADMIN-CONTACT-MANAGEMENT-CREATE";
	public static final String ADMIN_CONTACT_MANAGEMENT_UPDATE = "ADMIN-CONTACT-MANAGEMENT-UPDATE";
	public static final String ADMIN_CONTACT_MANAGEMENT_DELETE = "ADMIN-CONTACT-MANAGEMENT-DELETE";

	// Questions
	public static final String ADMIN_QUESTIONS_READ = "ADMIN-QUESTIONS-READ";
	public static final String ADMIN_QUESTIONS_UPDATE = "ADMIN-QUESTIONS-UPDATE";

	// Reviews
	public static final String ADMIN_REVIEW_READ = "ADMIN-REVIEW-READ";
	public static final String ADMIN_REVIEW_UPDATE = "ADMIN-REVIEW-UPDATE";
	public static final String ADMIN_REVIEW_DELETE = "ADMIN-REVIEW-DELETE";

	// Attribute
	public static final String ADMIN_ATTRIBUTE_MANAGEMENT_UPDATE = "ADMIN-ATTRIBUTE-MANAGEMENT-UPDATE";
	public static final String ADMIN_ATTRIBUTE_MANAGEMENT_DELETE = "ADMIN-ATTRIBUTE-MANAGEMENT-DELETE";
	public static final String ADMIN_ATTRIBUTE_MANAGEMENT_CREATE = "ADMIN-ATTRIBUTE-MANAGEMENT-CREATE";

	// Alert
	public static final String ADMIN_ALERT_MANAGEMENT_DELETE = "ADMIN-ALERT-MANAGEMENT-DELETE";
	public static final String ADMIN_ALERT_MANAGEMENT_READ = "ADMIN-ALERT-MANAGEMENT-READ";
	public static final String ADMIN_ALERT_MANAGEMENT_UPDATE = "ADMIN-ALERT-MANAGEMENT-UPDATE";
	public static final String ADMIN_ALERT_MANAGEMENT_CREATE = "ADMIN-ALERT-MANAGEMENT-CREATE";

	// Role
	public static final String ADMIN_ROLE_MANAGEMENT_CREATE = "ADMIN-ROLE-MANAGEMENT-CREATE";
	public static final String ADMIN_ROLE_MANAGEMENT_READ = "ADMIN-ROLE-MANAGEMENT-READ";
	public static final String ADMIN_ROLE_MANAGEMENT_UPDATE = "ADMIN-ROLE-MANAGEMENT-UPDATE";
	public static final String ADMIN_ROLE_MANAGEMENT_DELETE = "ADMIN-ROLE-MANAGEMENT-DELETE";

	// FAQ
	public static final String ADMIN_FAQ_CREATE = "ADMIN-FAQ-CREATE";
	public static final String ADMIN_FAQ_UPDATE = "ADMIN-FAQ-UPDATE";
	public static final String ADMIN_FAQ_DELETE = "ADMIN-FAQ-DELETE";
	public static final String ADMIN_FAQ_READ = "ADMIN-FAQ-READ";

	// Submissions Form Template
	public static final String ADMIN_SUBMISSION_FORM_TEMPLATE_CREATE = "ADMIN-SUBMISSION-FORM-TEMPLATE-CREATE";
	public static final String ADMIN_SUBMISSION_FORM_TEMPLATE_READ = "ADMIN-SUBMISSION-FORM-TEMPLATE-READ";
	public static final String ADMIN_SUBMISSION_FORM_TEMPLATE_UPDATE = "ADMIN-SUBMISSION-FORM-TEMPLATE-UPDATE";
	public static final String ADMIN_SUBMISSION_FORM_TEMPLATE_DELETE = "ADMIN-SUBMISSION-FORM-TEMPLATE-DELETE";

	// Other
	public static final String ALLOW_USER_ATTRIBUTE_TYPE_CREATION = "ALLOW-USER-ATTRIBUTE-TYPE-CREATION";
	public static final String RELATION_VIEW_TOOL = "RELATION-VIEW-TOOL";
	public static final String ADMIN_DATA_IMPORT_EXPORT = "ADMIN-DATA-IMPORT-EXPORT";
	public static final String ADMIN_ORGANIZATION_EXTRACTION = "ADMIN-ORGANIZATION-EXTRACTION";
	public static final String ADMIN_MEDIA_DELETE = "ADMIN-MEDIA-DELETE";
	public static final String API_DOCS = "API_DOCS";
	public static final String ADMIN_SECURITY = "ADMIN-SECURITY";
	public static final String ADMIN_SUPPORT_MEDIA = "ADMIN-SUPPORT-MEDIA";

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();

		// Reports
		codeMap.put(REPORTS_SCHEDULE, newLookup(SecurityPermission.class, REPORTS_SCHEDULE, "Reports schedule"));
		codeMap.put(REPORTS, newLookup(SecurityPermission.class, REPORTS, "Reports"));
		codeMap.put(REPORTS_ALL, newLookup(SecurityPermission.class, REPORTS_ALL, "Reports All - Allows viewing reports from all users"));

		// User submissions/management permissions
		codeMap.put(USER_SUBMISSIONS_UPDATE, newLookup(SecurityPermission.class, USER_SUBMISSIONS_UPDATE, "Allow user submission updates"));
		codeMap.put(USER_SUBMISSIONS_CREATE, newLookup(SecurityPermission.class, USER_SUBMISSIONS_CREATE, "Allow user submission creation"));
		codeMap.put(USER_SUBMISSIONS_READ, newLookup(SecurityPermission.class, USER_SUBMISSIONS_READ, "Allow user submission reading"));
		codeMap.put(USER_SUBMISSIONS_DELETE, newLookup(SecurityPermission.class, USER_SUBMISSIONS_DELETE, "Allow user submission deleting"));

		codeMap.put(ADMIN_USER_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_DELETE, "Allow admin user submission deleting"));
		codeMap.put(ADMIN_USER_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_READ, "Allow admin user submission reading"));
		codeMap.put(ADMIN_USER_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_UPDATE, "Allow admin user submission updating"));
		codeMap.put(ADMIN_USER_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_CREATE, "Allow admin user submission creation"));

		// Report permissions
		codeMap.put(REPORT_ACTION_REPORT, newLookup(SecurityPermission.class, REPORT_ACTION_REPORT, "Allow user to run action report"));
		codeMap.put(REPORT_ENTRYLISTING_REPORT, newLookup(SecurityPermission.class, REPORT_ENTRYLISTING_REPORT, "Allow user to run entry listin report"));
		codeMap.put(REPORT_OUTPUT_EMAIL_ATTACH, newLookup(SecurityPermission.class, REPORT_OUTPUT_EMAIL_ATTACH, "Allow user to run report as attached email"));
		codeMap.put(REPORT_OUTPUT_EMAIL_BODY, newLookup(SecurityPermission.class, REPORT_OUTPUT_EMAIL_BODY, "Allow user to run report as the content of an email body"));

		// System Management
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT, "General sys admin permission"));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_UPDATE_APPPROP, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_UPDATE_APPPROP, "Allows for updating app properties"));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_PLUGIN, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_PLUGIN, "Allows for management of plugins"));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_ARCHIVE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_ARCHIVE, "Allows for sys archive management"));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_ERROR_TICKET, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_ERROR_TICKET, "Allows for sys error ticket management"));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_SEARCH, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_SEARCH, "Allows for sys search management"));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_APP_READ, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_APP_READ, "Allows for reading application meta data"));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_APP_DELETE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_APP_DELETE, "Allows for removal/clearing operations for the sys"));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT_APP_UPDATE, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT_APP_UPDATE, "Allows for updating system configs"));

		// Entry Management
		codeMap.put(ADMIN_ENTRY_CREATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_CREATE, "Allows admin to create entries"));
		codeMap.put(ADMIN_ENTRY_READ, newLookup(SecurityPermission.class, ADMIN_ENTRY_READ, "Allows admin to read entries"));
		codeMap.put(ADMIN_ENTRY_UPDATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_UPDATE, "Allows admin to update an existing entry"));
		codeMap.put(ADMIN_ENTRY_DELETE, newLookup(SecurityPermission.class, ADMIN_ENTRY_DELETE, "Allows admin to DELETE an existing entry"));
		codeMap.put(ADMIN_ENTRY_TOGGLE_STATUS, newLookup(SecurityPermission.class, ADMIN_ENTRY_TOGGLE_STATUS, "Allows the admin to toggle the status of an entry"));
		codeMap.put(ADMIN_ENTRY_MERGE, newLookup(SecurityPermission.class, ADMIN_ENTRY_MERGE, "Allows the admin to merge two entries together"));
		codeMap.put(ADMIN_ENTRY_VERSION_DELETE, newLookup(SecurityPermission.class, ADMIN_ENTRY_VERSION_DELETE, "Allows the admin to delete the version"));
		codeMap.put(ADMIN_ENTRY_VERSION_READ, newLookup(SecurityPermission.class, ADMIN_ENTRY_VERSION_READ, "Allows the admin to read the version"));
		codeMap.put(ADMIN_ENTRY_VERSION_RESTORE, newLookup(SecurityPermission.class, ADMIN_ENTRY_VERSION_RESTORE, "Allows the admin to restore a version of an entry"));
		codeMap.put(ADMIN_ENTRY_APPROVE, newLookup(SecurityPermission.class, ADMIN_ENTRY_APPROVE, "Allows an admin the ability to approve an entry"));
		codeMap.put(ADMIN_ENTRY_CHANGETYPE, newLookup(SecurityPermission.class, ADMIN_ENTRY_CHANGETYPE, "Gives the admin the ability to change an entry's entry type"));
		codeMap.put(ADMIN_ENTRY_ATTR_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_ATTR_MANAGEMENT, "Gives admin attribute management"));
		codeMap.put(ADMIN_ENTRY_CONTACT_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_CONTACT_MANAGEMENT, "Gives an admin contact management permissions"));
		codeMap.put(ADMIN_ENTRY_DEPENDENCY_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_DEPENDENCY_MANAGEMENT, "Gives an admin dependency management"));
		codeMap.put(ADMIN_ENTRY_MEDIA_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_MEDIA_MANAGEMENT, "Gives an admin media management"));
		codeMap.put(ADMIN_ENTRY_RELATIONSHIP_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_RELATIONSHIP_MANAGEMENT, "Gives an admin relationship management for entries"));
		codeMap.put(ADMIN_ENTRY_RESOURCE_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_RESOURCE_MANAGEMENT, "Gives an admin resource management for entries"));
		codeMap.put(ADMIN_ENTRY_EVALSECTION_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_EVALSECTION_MANAGEMENT, "Gives an admin the ability to manage eval sections"));
		codeMap.put(ADMIN_ENTRY_TAG_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_TAG_MANAGEMENT, "Allows for tag management"));
		codeMap.put(ADMIN_ENTRY_CHANGEREQUEST_APPROVE, newLookup(SecurityPermission.class, ADMIN_ENTRY_CHANGEREQUEST_APPROVE, "Allows for change requests to be approved"));
		codeMap.put(ADMIN_ENTRY_CHANGEOWNER, newLookup(SecurityPermission.class, ADMIN_ENTRY_CHANGEOWNER, "Allows admin to change the owner of an entry"));
		codeMap.put(ADMIN_ENTRY_EXPORT, newLookup(SecurityPermission.class, ADMIN_ENTRY_EXPORT, "Allows admin to export an entry"));
		codeMap.put(ADMIN_ENTRY_PENDINGCHANGE_READ, newLookup(SecurityPermission.class, ADMIN_ENTRY_PENDINGCHANGE_READ, "Allows admin to read pending changes"));
		codeMap.put(ADMIN_ENTRY_ASSIGNUSER, newLookup(SecurityPermission.class, ADMIN_ENTRY_ASSIGNUSER, "Allows admin to assign libarian to a component"));
		
		// Entry Templates
		codeMap.put(ADMIN_ENTRY_TEMPLATES_CREATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES_CREATE, "Allows for the creation of entry templates"));
		codeMap.put(ADMIN_ENTRY_TEMPLATES_UPDATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES_UPDATE, "Allows the user to update entry templates"));
		codeMap.put(ADMIN_ENTRY_TEMPLATES_DELETE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES_DELETE, "Allows the user to delete an entry template"));
		codeMap.put(ADMIN_ENTRY_TEMPLATES_READ, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES_READ, "Allows for the user to read entry templates"));

		// Entry Types
		codeMap.put(ADMIN_ENTRY_TYPES_CREATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TYPES_CREATE, "Allows for the creation of entry types"));
		codeMap.put(ADMIN_ENTRY_TYPES_DELETE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TYPES_DELETE, "Allow for deleting entry types"));
		codeMap.put(ADMIN_ENTRY_TYPES_UPDATE, newLookup(SecurityPermission.class, ADMIN_ENTRY_TYPES_UPDATE, "Allows for the ability to update entry types"));

		// Message Management
		codeMap.put(ADMIN_MESSAGE_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT_READ, "Allows for the ability to read notification data"));
		codeMap.put(ADMIN_MESSAGE_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT_CREATE, "Allows for the ability to post a new notification event"));
		codeMap.put(ADMIN_MESSAGE_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT_DELETE, "Allows for deleting a notification event"));
		codeMap.put(ADMIN_MESSAGE_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT_UPDATE, "Allow for updating a notification event"));

		// Job Management
		codeMap.put(ADMIN_JOB_MANAGEMENT_ACTION, newLookup(SecurityPermission.class, ADMIN_JOB_MANAGEMENT_ACTION, "Allows user to perform some action with a job"));
		codeMap.put(ADMIN_JOB_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_JOB_MANAGEMENT_DELETE, "Allows user to remove a job"));
		codeMap.put(ADMIN_JOB_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_JOB_MANAGEMENT_READ, "Allows user to read jobs"));

		//Integration
		codeMap.put(ADMIN_INTEGRATION_DELETE, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_DELETE, "Allows for the removal of integration"));
		codeMap.put(ADMIN_INTEGRATION_UPDATE, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_UPDATE, "Allows for updating integration"));
		codeMap.put(ADMIN_INTEGRATION_READ, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_READ, "Allows for reading integration"));
		codeMap.put(ADMIN_INTEGRATION_CREATE, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_CREATE, "create integration"));
		codeMap.put(ADMIN_INTEGRATION_RUNALL, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_RUNALL, "allows for running all integrations"));
		codeMap.put(ADMIN_INTEGRATION_RUNCONFIG, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_RUNCONFIG, "allows for running config"));
		codeMap.put(ADMIN_INTEGRATION_RUNINTEGRATION, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_RUNINTEGRATION, "allows for running one integration"));
		codeMap.put(ADMIN_INTEGRATION_TOGGLESTATUS, newLookup(SecurityPermission.class, ADMIN_INTEGRATION_TOGGLESTATUS, "allows for the ability to toggle the status of an integration"));

		// Watches
		codeMap.put(ADMIN_WATCHES_DELETE, newLookup(SecurityPermission.class, ADMIN_WATCHES_DELETE, "delete watches"));
		codeMap.put(ADMIN_WATCHES_UPDATE, newLookup(SecurityPermission.class, ADMIN_WATCHES_UPDATE, "update watches"));
		codeMap.put(ADMIN_WATCHES_READ, newLookup(SecurityPermission.class, ADMIN_WATCHES_READ, "ability to read watches"));

		// Tracking
		codeMap.put(ADMIN_TRACKING_READ, newLookup(SecurityPermission.class, ADMIN_TRACKING_READ, "read tracking"));
		codeMap.put(ADMIN_TRACKING_DELETE, newLookup(SecurityPermission.class, ADMIN_TRACKING_DELETE, "delete tracking from an item"));
		codeMap.put(ADMIN_TRACKING_UPDATE, newLookup(SecurityPermission.class, ADMIN_TRACKING_UPDATE, "update tracking on an item"));

		// Search
		codeMap.put(ADMIN_SEARCH_DELETE, newLookup(SecurityPermission.class, ADMIN_SEARCH_DELETE, "delete a search"));
		codeMap.put(ADMIN_SEARCH_CREATE, newLookup(SecurityPermission.class, ADMIN_SEARCH_CREATE, "create a search"));
		codeMap.put(ADMIN_SEARCH_UPDATE, newLookup(SecurityPermission.class, ADMIN_SEARCH_UPDATE, "update a search"));
		codeMap.put(ADMIN_SEARCH_READ, newLookup(SecurityPermission.class, ADMIN_SEARCH_READ, "read all searches"));

		// Profile Management
		codeMap.put(ADMIN_USER_MANAGEMENT_PROFILES_READ, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_PROFILES_READ, "Allow for the reading of user profiles"));
		codeMap.put(ADMIN_USER_MANAGEMENT_PROFILES_UPDATE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_PROFILES_UPDATE, "Allows for the ability to update a user profile"));
		codeMap.put(ADMIN_USER_MANAGEMENT_PROFILES_DELETE, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_PROFILES_DELETE, "Allows for the ability to delete a user profile"));

		// User Submissions
		codeMap.put(ADMIN_USER_SUBMISSIONS_READ, newLookup(SecurityPermission.class, ADMIN_USER_SUBMISSIONS_READ, "Allows for reading user submissions"));
		codeMap.put(ADMIN_USER_SUBMISSIONS_UPDATE, newLookup(SecurityPermission.class, ADMIN_USER_SUBMISSIONS_UPDATE, "Allows for update user submissions"));
		codeMap.put(ADMIN_USER_SUBMISSIONS_DELETE, newLookup(SecurityPermission.class, ADMIN_USER_SUBMISSIONS_DELETE, "Allows for deleting user submissions"));

		// Temp Media
		codeMap.put(ADMIN_TEMPMEDIA_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_TEMPMEDIA_MANAGEMENT_READ, "Provides the ability to read temp media"));
		codeMap.put(ADMIN_TEMPMEDIA_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_TEMPMEDIA_MANAGEMENT_DELETE, "Provides the ability to delete temp media"));

		// Organization
		codeMap.put(ADMIN_ORGANIZATION_CREATE, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_CREATE, "Allows for creating orgs"));
		codeMap.put(ADMIN_ORGANIZATION_UPDATE, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_UPDATE, "Allows for update orgs"));
		codeMap.put(ADMIN_ORGANIZATION_DELETE, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_DELETE, "Allows for deleting organizations"));
		codeMap.put(ADMIN_ORGANIZATION_MERGE, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_MERGE, "Allows for merging two organizations"));

		// Lookups
		codeMap.put(ADMIN_LOOKUPS_CREATE, newLookup(SecurityPermission.class, ADMIN_LOOKUPS_CREATE, "Provides the ability to create lookups"));
		codeMap.put(ADMIN_LOOKUPS_READ, newLookup(SecurityPermission.class, ADMIN_LOOKUPS_READ, "Provides the ability to read lookups"));
		codeMap.put(ADMIN_LOOKUPS_UPDATE, newLookup(SecurityPermission.class, ADMIN_LOOKUPS_UPDATE, "Provides the ability to update a lookup(s)"));
		codeMap.put(ADMIN_LOOKUPS_DELETE, newLookup(SecurityPermission.class, ADMIN_LOOKUPS_DELETE, "Provides the ability to delete lookups"));

		// Highlights
		codeMap.put(ADMIN_HIGHLIGHTS_CREATE, newLookup(SecurityPermission.class, ADMIN_HIGHLIGHTS_CREATE, "allows for creating highlights"));
		codeMap.put(ADMIN_HIGHLIGHTS_UPDATE, newLookup(SecurityPermission.class, ADMIN_HIGHLIGHTS_UPDATE, "allows for update highlights"));
		codeMap.put(ADMIN_HIGHLIGHTS_DELETE, newLookup(SecurityPermission.class, ADMIN_HIGHLIGHTS_DELETE, "allows for deleting highlights"));

		// Feedback
		codeMap.put(ADMIN_FEEDBACK_READ, newLookup(SecurityPermission.class, ADMIN_FEEDBACK_READ, "allows for reading feedback"));
		codeMap.put(ADMIN_FEEDBACK_UPDATE, newLookup(SecurityPermission.class, ADMIN_FEEDBACK_UPDATE, "allows for modifying feedback"));
		codeMap.put(ADMIN_FEEDBACK_DELETE, newLookup(SecurityPermission.class, ADMIN_FEEDBACK_DELETE, "allows for removing feedback"));

		// Evaluation
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CREATE, "Gives the ability to create evaluation templates"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_READ, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_READ, "Gives the ability to read evaluation templates"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_UPDATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_UPDATE, "Gives the ability to update evaluation templates"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_DELETE, "Gives the ability to delete evaluation templates"));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION_CREATE, "Allows for creation eval template sections"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION_READ, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION_READ, "Allows for reading eval template sections"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION_UPDATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION_UPDATE, "Allows for update eval template sections"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION_DELETE, "Allows for deleting eval template sections"));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_DELETE, "Gives the ability to delete a eval template checklist item"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_READ, "Gives the ability to read eval template checklist items"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_CREATE, "Gives the ability to create eval template checklist items"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_UPDATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_UPDATE, "Gives the ability to update eval template checklist items"));

		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_DELETE, "Provides the ability to delete eval template checklist question items"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_READ, "Provides the ability to read eval template checklist question items"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_CREATE, "Provides the ability to create eval template checklist questions"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_UPDATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION_UPDATE, "Provides the ability to update eval template checklist questions"));

		// Evaluation Management
		codeMap.put(ADMIN_EVALUATION_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_MANAGEMENT_CREATE, "Allows the user to create an evaluation"));
		codeMap.put(ADMIN_EVALUATION_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_MANAGEMENT_DELETE, "Allows the user to delete an evaluation"));
		codeMap.put(ADMIN_EVALUATION_ACTIVATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_ACTIVATE, "Allows the user to activate an evaluation"));
		codeMap.put(ADMIN_EVALUATION_ALLOW_NEW_SECTIONS, newLookup(SecurityPermission.class, ADMIN_EVALUATION_ALLOW_NEW_SECTIONS, "Allows the user to specify whether the eval should allow new sections"));
		codeMap.put(ADMIN_EVALUATION_ALLOW_QUESTION_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_EVALUATION_ALLOW_QUESTION_MANAGEMENT, "Allows the user to specify whether the eval should allow question management"));
		codeMap.put(ADMIN_EVALUATION_TOGGLE_PUBLISH, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TOGGLE_PUBLISH, "Allows the user to publish an evaluation"));
		codeMap.put(ADMIN_EVALUATION_PUBLISH_SUMMARY, newLookup(SecurityPermission.class, ADMIN_EVALUATION_PUBLISH_SUMMARY, "Allows the user to publish a summary"));
		codeMap.put(ADMIN_EVALUATION_DELETE_COMMENT, newLookup(SecurityPermission.class, ADMIN_EVALUATION_DELETE_COMMENT, "Allows the user to delete a comment on said evaluation"));

		// Branding
		codeMap.put(ADMIN_BRANDING_DELETE, newLookup(SecurityPermission.class, ADMIN_BRANDING_DELETE, "Gives ability to delete brandings"));
		codeMap.put(ADMIN_BRANDING_CREATE, newLookup(SecurityPermission.class, ADMIN_BRANDING_CREATE, "Gives ability to create new brandings"));
		codeMap.put(ADMIN_BRANDING_UPDATE, newLookup(SecurityPermission.class, ADMIN_BRANDING_UPDATE, "Gives ability to update brandings"));

		// Contact
		codeMap.put(ADMIN_CONTACT_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_CONTACT_MANAGEMENT_CREATE, "Gives ability to create contacts"));
		codeMap.put(ADMIN_CONTACT_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_CONTACT_MANAGEMENT_UPDATE, "Gives ability to update existing contacts"));
		codeMap.put(ADMIN_CONTACT_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_CONTACT_MANAGEMENT_DELETE, "Gives ability delete contacts"));

		// Questions
		codeMap.put(ADMIN_QUESTIONS_READ, newLookup(SecurityPermission.class, ADMIN_QUESTIONS_READ, "Gives ability to read questions"));
		codeMap.put(ADMIN_QUESTIONS_UPDATE, newLookup(SecurityPermission.class, ADMIN_QUESTIONS_UPDATE, "Gives ability to update questions"));

		// Reviews
		codeMap.put(ADMIN_REVIEW_READ, newLookup(SecurityPermission.class, ADMIN_REVIEW_READ, "Gives ability to read reviews"));
		codeMap.put(ADMIN_REVIEW_UPDATE, newLookup(SecurityPermission.class, ADMIN_REVIEW_UPDATE, "Gives ability to update reviews"));
		codeMap.put(ADMIN_REVIEW_DELETE, newLookup(SecurityPermission.class, ADMIN_REVIEW_DELETE, "Gives ability to delete reviews"));

		// Attribute
		codeMap.put(ADMIN_ATTRIBUTE_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_ATTRIBUTE_MANAGEMENT_UPDATE, "Gives ability to update existing attributes"));
		codeMap.put(ADMIN_ATTRIBUTE_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_ATTRIBUTE_MANAGEMENT_DELETE, "Gives ability to delete existing attributes"));
		codeMap.put(ADMIN_ATTRIBUTE_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_ATTRIBUTE_MANAGEMENT_CREATE, "Gives ability to create new attributes"));

		// Alert
		codeMap.put(ADMIN_ALERT_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT_DELETE, "Give ability to delete alerts"));
		codeMap.put(ADMIN_ALERT_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT_READ, "Gives ability to read all alerts"));
		codeMap.put(ADMIN_ALERT_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT_UPDATE, "Gives ability to update existing alerts"));
		codeMap.put(ADMIN_ALERT_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT_CREATE, "Gives ability to create new alerts"));

		// Role
		codeMap.put(ADMIN_ROLE_MANAGEMENT_CREATE, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT_CREATE, "Gives ability to create roles"));
		codeMap.put(ADMIN_ROLE_MANAGEMENT_READ, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT_READ, "Gives ability to read roles"));
		codeMap.put(ADMIN_ROLE_MANAGEMENT_UPDATE, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT_UPDATE, "Gives ability to update roles"));
		codeMap.put(ADMIN_ROLE_MANAGEMENT_DELETE, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT_DELETE, "Gives ability to delete roles"));

		// FAQ
		codeMap.put(ADMIN_FAQ_CREATE, newLookup(SecurityPermission.class, ADMIN_FAQ_CREATE, "Gives ability to creater FAQs"));
		codeMap.put(ADMIN_FAQ_UPDATE, newLookup(SecurityPermission.class, ADMIN_FAQ_UPDATE, "Allows for update FAQs"));
		codeMap.put(ADMIN_FAQ_DELETE, newLookup(SecurityPermission.class, ADMIN_FAQ_DELETE, "Allows for deleting FAQs"));
		codeMap.put(ADMIN_FAQ_READ, newLookup(SecurityPermission.class, ADMIN_FAQ_READ, "Allows for reading FAQs"));

		// Submissions Form Template
		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE_CREATE, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE_CREATE, "Allows for creating submission form templates"));
		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE_READ, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE_READ, "Allows for reading submission form templates"));
		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE_UPDATE, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE_UPDATE, "Allows for updating submission form templates"));
		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE_DELETE, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE_DELETE, "Allows for deleting submission form templates"));

		// Other
		codeMap.put(ALLOW_USER_ATTRIBUTE_TYPE_CREATION, newLookup(SecurityPermission.class, ALLOW_USER_ATTRIBUTE_TYPE_CREATION, "Allows for creating attribute types"));
		codeMap.put(RELATION_VIEW_TOOL, newLookup(SecurityPermission.class, RELATION_VIEW_TOOL, "Allows for use of the view tool?"));
		codeMap.put(ADMIN_DATA_IMPORT_EXPORT, newLookup(SecurityPermission.class, ADMIN_DATA_IMPORT_EXPORT, "export import"));
		codeMap.put(ADMIN_ORGANIZATION_EXTRACTION, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_EXTRACTION, "Allows for organization extraction"));
		codeMap.put(ADMIN_MEDIA_DELETE, newLookup(SecurityPermission.class, ADMIN_MEDIA_DELETE, "Allows for deleting media"));
		codeMap.put(API_DOCS, newLookup(SecurityPermission.class, API_DOCS, "Allows said user to view API docs"));
		codeMap.put(ADMIN_SECURITY, newLookup(SecurityPermission.class, ADMIN_SECURITY, "security general permission"));
		codeMap.put(ADMIN_SUPPORT_MEDIA, newLookup(SecurityPermission.class, ADMIN_SUPPORT_MEDIA, "Support media general permission"));
		
		// ========================================================================================================================
		codeMap.put(ENTRY_TAG, newLookup(SecurityPermission.class, ENTRY_TAG, "Entry tag"));
		// codeMap.put(REPORTS_SCHEDULE, newLookup(SecurityPermission.class, REPORTS_SCHEDULE, "Reports schedule"));
		// codeMap.put(REPORTS, newLookup(SecurityPermission.class, REPORTS, "Reports"));
		// codeMap.put(REPORTS_ALL, newLookup(SecurityPermission.class, REPORTS_ALL, "Reports All - Allows viewing reports from all users"));
		codeMap.put(USER_SUBMISSIONS, newLookup(SecurityPermission.class, USER_SUBMISSIONS, "User submissions"));
		codeMap.put(EVALUATIONS, newLookup(SecurityPermission.class, EVALUATIONS, "Evaluations"));
		codeMap.put(RELATIONSHIP_VIEW_TOOL, newLookup(SecurityPermission.class, RELATIONSHIP_VIEW_TOOL, "Relationship View Tool"));
		// codeMap.put(ALLOW_USER_ATTRIBUTE_TYPE_CREATION, newLookup(SecurityPermission.class, ALLOW_USER_ATTRIBUTE_TYPE_CREATION, "Allow User Attribute Type Creation"));

		// codeMap.put(REPORT_ACTION_REPORT, newLookup(SecurityPermission.class, REPORT_ACTION_REPORT, "Allows running action report"));
		codeMap.put(REPORT_ENTRY_LISTING_REPORT, newLookup(SecurityPermission.class, REPORT_ENTRY_LISTING_REPORT, "Allows running entry listing report"));
		// codeMap.put(REPORT_OUTPUT_EMAIL_ATTACH, newLookup(SecurityPermission.class, REPORT_OUTPUT_EMAIL_ATTACH, "Allows attaching the report"));
		// codeMap.put(REPORT_OUTPUT_EMAIL_BODY, newLookup(SecurityPermission.class, REPORT_OUTPUT_EMAIL_BODY, "Allows email to body"));

		codeMap.put(ADMIN_USER_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT, "Admin user management"));
		// codeMap.put(ADMIN_SYSTEM_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT, "Admin system management"));
		codeMap.put(ADMIN_ENTRY_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_MANAGEMENT, "Admin entry management"));
		codeMap.put(ADMIN_MESSAGE_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT, "Admin message management"));
		codeMap.put(ADMIN_JOB_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_JOB_MANAGEMENT, "Admin job management"));
		codeMap.put(ADMIN_INTEGRATION, newLookup(SecurityPermission.class, ADMIN_INTEGRATION, "Admin integration"));
		// codeMap.put(ADMIN_DATA_IMPORT_EXPORT, newLookup(SecurityPermission.class, ADMIN_DATA_IMPORT_EXPORT, "Admin data import export"));
		codeMap.put(ADMIN_WATCHES, newLookup(SecurityPermission.class, ADMIN_WATCHES, "Admin watches"));
		codeMap.put(ADMIN_TRACKING, newLookup(SecurityPermission.class, ADMIN_TRACKING, "Admin tracking"));
		codeMap.put(ADMIN_SEARCH, newLookup(SecurityPermission.class, ADMIN_SEARCH, "Admin search"));
		codeMap.put(ADMIN_USER_MANAGEMENT_PROFILES, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_PROFILES, "Admin user management profiles"));
		codeMap.put(ADMIN_TEMPMEDIA_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_TEMPMEDIA_MANAGEMENT, "Admin tempmedia management"));
		codeMap.put(ADMIN_ORGANIZATION, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION, "Admin organization"));
		// codeMap.put(ADMIN_ORGANIZATION_EXTRACTION, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_EXTRACTION, "Allow running organization extraction"));
		codeMap.put(ADMIN_LOOKUPS, newLookup(SecurityPermission.class, ADMIN_LOOKUPS, "Admin lookups"));
		codeMap.put(ADMIN_HIGHLIGHTS, newLookup(SecurityPermission.class, ADMIN_HIGHLIGHTS, "Admin highlights"));
		codeMap.put(ADMIN_MEDIA, newLookup(SecurityPermission.class, ADMIN_MEDIA, "Admin media"));
		codeMap.put(ADMIN_FEEDBACK, newLookup(SecurityPermission.class, ADMIN_FEEDBACK, "Admin feedback"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE, "Admin evalution template"));
		// codeMap.put(API_DOCS, newLookup(SecurityPermission.class, API_DOCS, "Api docs"));
		codeMap.put(ADMIN_BRANDING, newLookup(SecurityPermission.class, ADMIN_BRANDING, "Admin branding"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION, "Admin evalution template section"));
		codeMap.put(ADMIN_CONTACT_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_CONTACT_MANAGEMENT, "Admin contact management"));
		codeMap.put(ADMIN_ENTRY_TEMPLATES, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES, "Admin entry templates"));
		codeMap.put(ADMIN_USER_SUBMISSIONS, newLookup(SecurityPermission.class, ADMIN_USER_SUBMISSIONS, "Admin user submissions"));
		codeMap.put(ADMIN_ENTRY_TYPES, newLookup(SecurityPermission.class, ADMIN_ENTRY_TYPES, "Admin entry types"));
		codeMap.put(ADMIN_QUESTIONS, newLookup(SecurityPermission.class, ADMIN_QUESTIONS, "Admin questions"));
		codeMap.put(ADMIN_REVIEW, newLookup(SecurityPermission.class, ADMIN_REVIEW, "Admin review"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST, "Admin evalution template checklist"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION, "Admin evalution template checklist question"));
		codeMap.put(ADMIN_ATTRIBUTE_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ATTRIBUTE_MANAGEMENT, "Admin attribute management"));
		codeMap.put(ADMIN_ALERT_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT, "Admin alert management"));
		codeMap.put(ADMIN_EVALUATION_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_EVALUATION_MANAGEMENT, "Admin evaluation management"));
		// codeMap.put(ADMIN_SECURITY, newLookup(SecurityPermission.class, ADMIN_SECURITY, "Admin security"));
		codeMap.put(ADMIN_ROLE_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT, "Admin security role management"));
		codeMap.put(ADMIN_FAQ, newLookup(SecurityPermission.class, ADMIN_FAQ, "Admin FAQ management"));
		codeMap.put(ADMIN_SUBMISSION_FORM_TEMPLATE, newLookup(SecurityPermission.class, ADMIN_SUBMISSION_FORM_TEMPLATE, "Admin Custom Submission Form management"));

		codeMap.put(ADMIN_SUPPORT_MEDIA, newLookup(SecurityPermission.class, ADMIN_SUPPORT_MEDIA, "Admin Support Media"));
		// ========================================================================================================================

		return codeMap;
	}
}
