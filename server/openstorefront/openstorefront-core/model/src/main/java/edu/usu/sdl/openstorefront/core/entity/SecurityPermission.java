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
 */
@SystemTable
@APIDescription("Defines Security Permissions")
public class SecurityPermission
		extends LookupEntity<SecurityPermission>
{

	public static final String ENTRY_TAG = "ENTRY-TAG";
	public static final String REPORTS_SCHEDULE = "REPORTS-SCHEDULE";
	public static final String REPORTS = "REPORTS";
	public static final String REPORTS_ALL = "REPORTS-ALL";
	public static final String USER_SUBMISSIONS = "USER-SUBMISSIONS";
	public static final String EVALUATIONS = "EVALUATIONS";
	public static final String ALLOW_USER_ATTRIBUTE_TYPE_CREATION = "ALLOW-USER-ATTRIBUTE-TYPE-CREATION";

	public static final String REPORT_ACTION_REPORT = "REPORT-ACTION-REPORT";
	public static final String REPORT_ENTRY_LISTING_REPORT = "REPORT-ENTRYLISTING-REPORT";
	public static final String REPORT_OUTPUT_EMAIL_ATTACH = "REPORT-OUTPUT-EMAIL-ATTACH";
	public static final String REPORT_OUTPUT_EMAIL_BODY = "REPORT-OUTPUT-EMAIL-BODY";

	public static final String RELATIONSHIP_VIEW_TOOL = "RELATION-VIEW-TOOL";
	public static final String ADMIN_USER_MANAGEMENT = "ADMIN-USER-MANAGEMENT";
	public static final String ADMIN_SYSTEM_MANAGEMENT = "ADMIN-SYSTEM-MANAGEMENT";
	public static final String ADMIN_ENTRY_MANAGEMENT = "ADMIN-ENTRY-MANAGEMENT";
	public static final String ADMIN_MESSAGE_MANAGEMENT = "ADMIN-MESSAGE-MANAGEMENT";
	public static final String ADMIN_JOB_MANAGEMENT = "ADMIN-JOB-MANAGEMENT";
	public static final String ADMIN_INTEGRATION = "ADMIN-INTEGRATION";
	public static final String ADMIN_DATA_IMPORT_EXPORT = "ADMIN-DATA-IMPORT-EXPORT";
	public static final String ADMIN_WATCHES = "ADMIN-WATCHES";
	public static final String ADMIN_TRACKING = "ADMIN-TRACKING";
	public static final String ADMIN_SEARCH = "ADMIN-SEARCH";
	public static final String ADMIN_USER_MANAGEMENT_PROFILES = "ADMIN-USER-MANAGEMENT-PROFILES";
	public static final String ADMIN_TEMPMEDIA_MANAGEMENT = "ADMIN-TEMPMEDIA-MANAGEMENT";
	public static final String ADMIN_ORGANIZATION = "ADMIN-ORGANIZATION";
	public static final String ADMIN_ORGANIZATION_EXTRACTION = "ADMIN-ORGANIZATION-EXTRACTION";
	public static final String ADMIN_LOOKUPS = "ADMIN-LOOKUPS";
	public static final String ADMIN_HIGHLIGHTS = "ADMIN-HIGHLIGHTS";
	public static final String ADMIN_MEDIA = "ADMIN-MEDIA";
	public static final String ADMIN_FEEDBACK = "ADMIN-FEEDBACK";
	public static final String ADMIN_EVALUATION_TEMPLATE = "ADMIN-EVALUATION-TEMPLATE";
	public static final String API_DOCS = "API-DOCS";
	public static final String ADMIN_BRANDING = "ADMIN-BRANDING";
	public static final String ADMIN_EVALUATION_TEMPLATE_SECTION = "ADMIN-EVALUATION-TEMPLATE-SECTION";
	public static final String ADMIN_CONTACT_MANAGEMENT = "ADMIN-CONTACT-MANAGEMENT";
	public static final String ADMIN_ENTRY_TEMPLATES = "ADMIN-ENTRY-TEMPLATES";
	public static final String ADMIN_ENTRY_TYPES = "ADMIN-ENTRY-TYPES";
	public static final String ADMIN_QUESTIONS = "ADMIN-QUESTIONS";
	public static final String ADMIN_REVIEW = "ADMIN-REVIEW";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST";
	public static final String ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION = "ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION";
	public static final String ADMIN_ATTRIBUTE_MANAGEMENT = "ADMIN-ATTRIBUTE-MANAGEMENT";
	public static final String ADMIN_ALERT_MANAGEMENT = "ADMIN-ALERT-MANAGEMENT";
	public static final String ADMIN_EVALUATION_MANAGEMENT = "ADMIN-EVALUATION-MANAGEMENT";
	public static final String ADMIN_ROLE_MANAGEMENT = "ADMIN-ROLE-MANAGEMENT";
	public static final String ADMIN_SECURITY = "ADMIN-SECURITY";

	public SecurityPermission()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(ENTRY_TAG, newLookup(SecurityPermission.class, ENTRY_TAG, "Entry tag"));
		codeMap.put(REPORTS_SCHEDULE, newLookup(SecurityPermission.class, REPORTS_SCHEDULE, "Reports schedule"));
		codeMap.put(REPORTS, newLookup(SecurityPermission.class, REPORTS, "Reports"));
		codeMap.put(REPORTS_ALL, newLookup(SecurityPermission.class, REPORTS_ALL, "Reports All - Allows viewing reports from all users"));
		codeMap.put(USER_SUBMISSIONS, newLookup(SecurityPermission.class, USER_SUBMISSIONS, "User submissions"));
		codeMap.put(EVALUATIONS, newLookup(SecurityPermission.class, EVALUATIONS, "Evaluations"));
		codeMap.put(RELATIONSHIP_VIEW_TOOL, newLookup(SecurityPermission.class, RELATIONSHIP_VIEW_TOOL, "Relationship View Tool"));
		codeMap.put(ALLOW_USER_ATTRIBUTE_TYPE_CREATION, newLookup(SecurityPermission.class, ALLOW_USER_ATTRIBUTE_TYPE_CREATION, "Allow User Attribute Type Creation"));

		codeMap.put(REPORT_ACTION_REPORT, newLookup(SecurityPermission.class, REPORT_ACTION_REPORT, "Allows running action report"));
		codeMap.put(REPORT_OUTPUT_EMAIL_ATTACH, newLookup(SecurityPermission.class, REPORT_OUTPUT_EMAIL_ATTACH, "Allows attaching the report"));
		codeMap.put(REPORT_OUTPUT_EMAIL_BODY, newLookup(SecurityPermission.class, REPORT_OUTPUT_EMAIL_BODY, "Allows email to body"));

		codeMap.put(ADMIN_USER_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT, "Admin user management"));
		codeMap.put(ADMIN_SYSTEM_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_SYSTEM_MANAGEMENT, "Admin system management"));
		codeMap.put(ADMIN_ENTRY_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ENTRY_MANAGEMENT, "Admin entry management"));
		codeMap.put(ADMIN_MESSAGE_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_MESSAGE_MANAGEMENT, "Admin message management"));
		codeMap.put(ADMIN_JOB_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_JOB_MANAGEMENT, "Admin job management"));
		codeMap.put(ADMIN_INTEGRATION, newLookup(SecurityPermission.class, ADMIN_INTEGRATION, "Admin integration"));
		codeMap.put(ADMIN_DATA_IMPORT_EXPORT, newLookup(SecurityPermission.class, ADMIN_DATA_IMPORT_EXPORT, "Admin data import export"));
		codeMap.put(ADMIN_WATCHES, newLookup(SecurityPermission.class, ADMIN_WATCHES, "Admin watches"));
		codeMap.put(ADMIN_TRACKING, newLookup(SecurityPermission.class, ADMIN_TRACKING, "Admin tracking"));
		codeMap.put(ADMIN_SEARCH, newLookup(SecurityPermission.class, ADMIN_SEARCH, "Admin search"));
		codeMap.put(ADMIN_USER_MANAGEMENT_PROFILES, newLookup(SecurityPermission.class, ADMIN_USER_MANAGEMENT_PROFILES, "Admin user management profiles"));
		codeMap.put(ADMIN_TEMPMEDIA_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_TEMPMEDIA_MANAGEMENT, "Admin tempmedia management"));
		codeMap.put(ADMIN_ORGANIZATION, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION, "Admin organization"));
		codeMap.put(ADMIN_ORGANIZATION_EXTRACTION, newLookup(SecurityPermission.class, ADMIN_ORGANIZATION_EXTRACTION, "Allow running organization extraction"));
		codeMap.put(ADMIN_LOOKUPS, newLookup(SecurityPermission.class, ADMIN_LOOKUPS, "Admin lookups"));
		codeMap.put(ADMIN_HIGHLIGHTS, newLookup(SecurityPermission.class, ADMIN_HIGHLIGHTS, "Admin highlights"));
		codeMap.put(ADMIN_MEDIA, newLookup(SecurityPermission.class, ADMIN_MEDIA, "Admin media"));
		codeMap.put(ADMIN_FEEDBACK, newLookup(SecurityPermission.class, ADMIN_FEEDBACK, "Admin feedback"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE, "Admin evalution template"));
		codeMap.put(API_DOCS, newLookup(SecurityPermission.class, API_DOCS, "Api docs"));
		codeMap.put(ADMIN_BRANDING, newLookup(SecurityPermission.class, ADMIN_BRANDING, "Admin branding"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_SECTION, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_SECTION, "Admin evalution template section"));
		codeMap.put(ADMIN_CONTACT_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_CONTACT_MANAGEMENT, "Admin contact management"));
		codeMap.put(ADMIN_ENTRY_TEMPLATES, newLookup(SecurityPermission.class, ADMIN_ENTRY_TEMPLATES, "Admin entry templates"));
		codeMap.put(ADMIN_ENTRY_TYPES, newLookup(SecurityPermission.class, ADMIN_ENTRY_TYPES, "Admin entry types"));
		codeMap.put(ADMIN_QUESTIONS, newLookup(SecurityPermission.class, ADMIN_QUESTIONS, "Admin questions"));
		codeMap.put(ADMIN_REVIEW, newLookup(SecurityPermission.class, ADMIN_REVIEW, "Admin review"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST, "Admin evalution template checklist"));
		codeMap.put(ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION, newLookup(SecurityPermission.class, ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION, "Admin evalution template checklist question"));
		codeMap.put(ADMIN_ATTRIBUTE_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ATTRIBUTE_MANAGEMENT, "Admin attribute management"));
		codeMap.put(ADMIN_ALERT_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ALERT_MANAGEMENT, "Admin alert management"));
		codeMap.put(ADMIN_EVALUATION_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_EVALUATION_MANAGEMENT, "Admin evaluation management"));
		codeMap.put(ADMIN_SECURITY, newLookup(SecurityPermission.class, ADMIN_SECURITY, "Admin security"));
		codeMap.put(ADMIN_ROLE_MANAGEMENT, newLookup(SecurityPermission.class, ADMIN_ROLE_MANAGEMENT, "Admin security role management"));

		return codeMap;
	}
}
