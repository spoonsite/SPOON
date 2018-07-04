/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
@APIDescription("Defines the reports")
public class ReportType
		extends LookupEntity<ReportType>
{

	public static final String USAGE = "USAGE";
	public static final String LINK_VALIDATION = "LINKVALID";
	public static final String COMPONENT = "COMPONENT";
	public static final String USER = "USER";
	public static final String ORGANIZATION = "ORGANIZATION";
	public static final String COMPONENT_ORGANIZATION = "CMPORG";
	public static final String SUBMISSION = "SUBMISSION";
	public static final String CATEGORY_COMPONENT = "CATCOMP";
	public static final String COMPONENT_DETAIL = "TYPECOMP";
	public static final String EVALUATION_STATUS = "EVALSTAT";
	public static final String ACTION_REPORT = "ACTION";
	public static final String ENTRY_LISTING = "ENTRYLIST";
	public static final String ENTRY_STATUS = "ENTRYSTATUS";

	private String requiredPermission;
	private boolean componentReport;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ReportType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(USAGE, newLookup(ReportType.class, USAGE, "Usage", "Reports on overall application usage."));
		codeMap.put(LINK_VALIDATION, newLookup(ReportType.class, LINK_VALIDATION, "Link Validation", "Reports on potentially broken external url links."));
		codeMap.put(COMPONENT, newLookup(ReportType.class, COMPONENT, "Entry", "Reports on entry statistic and usage."));
		codeMap.put(USER, newLookup(ReportType.class, USER, "User", "Reports on users and their usage of the application."));
		codeMap.put(ORGANIZATION, newLookup(ReportType.class, ORGANIZATION, "User Organization", "Reports on the user organizations in the application and their usage."));
		codeMap.put(COMPONENT_ORGANIZATION, newLookup(ReportType.class, COMPONENT_ORGANIZATION, "Entries by Organization", "Reports on entries that belong to an organization"));
		codeMap.put(SUBMISSION, newLookup(ReportType.class, SUBMISSION, "Submissions", "Reports on entry submissions."));
		codeMap.put(CATEGORY_COMPONENT, newLookup(ReportType.class, CATEGORY_COMPONENT, "Entries by Category", "Reports on entries in a category."));
		codeMap.put(EVALUATION_STATUS, newLookup(ReportType.class, EVALUATION_STATUS, "Evaluation Status", "Reports on the status of unpublished evaluations."));
		codeMap.put(ACTION_REPORT, newLookup(ReportType.class, ACTION_REPORT, "Action", "Report on items that need administrative attention."));
		codeMap.put(COMPONENT_DETAIL, newLookup(ReportType.class, COMPONENT_DETAIL, "Entry Detail", "Exports entry details"));
		codeMap.put(ENTRY_LISTING, newLookup(ReportType.class, ENTRY_LISTING, "Entry Listing", "List approved entries in a summary"));
		codeMap.put(ENTRY_STATUS, newLookup(ReportType.class, ENTRY_STATUS, "Entry Status", "Gathers information about entry status for a time period."));

		//update metadata
		((ReportType) codeMap.get(USAGE)).setRequiredPermission(SecurityPermission.RUN_USAGE_REPORT);
		((ReportType) codeMap.get(LINK_VALIDATION)).setRequiredPermission(SecurityPermission.RUN_LINK_VALIDATION_REPORT);
		((ReportType) codeMap.get(USER)).setRequiredPermission(SecurityPermission.RUN_USER_ORG_REPORT);
		((ReportType) codeMap.get(SUBMISSION)).setRequiredPermission(SecurityPermission.RUN_SUBMISSIONS_REPORT);
		((ReportType) codeMap.get(ORGANIZATION)).setRequiredPermission(SecurityPermission.RUN_ENTRIES_BY_ORG_REPORT);
		((ReportType) codeMap.get(EVALUATION_STATUS)).setRequiredPermission(SecurityPermission.RUN_EVAL_STATUS_REPORT);
		((ReportType) codeMap.get(ACTION_REPORT)).setRequiredPermission(SecurityPermission.RUN_ACTION_REPORT);
		((ReportType) codeMap.get(ENTRY_LISTING)).setRequiredPermission(SecurityPermission.RUN_ENTRY_LISTING_REPORT);
		((ReportType) codeMap.get(ENTRY_STATUS)).setRequiredPermission(SecurityPermission.RUN_ENTRY_STATUS_REPORT);

		//update metadata for component type reports
		((ReportType) codeMap.get(COMPONENT_ORGANIZATION)).setComponentReport(true);
		((ReportType) codeMap.get(CATEGORY_COMPONENT)).setComponentReport(true);
		((ReportType) codeMap.get(COMPONENT)).setComponentReport(true);
		((ReportType) codeMap.get(COMPONENT_DETAIL)).setComponentReport(true);
		((ReportType) codeMap.get(ENTRY_LISTING)).setComponentReport(true);
		((ReportType) codeMap.get(ENTRY_STATUS)).setComponentReport(true);

		return codeMap;
	}

	public boolean getComponentReport()
	{
		return componentReport;
	}

	public void setComponentReport(boolean componentReport)
	{
		this.componentReport = componentReport;
	}

	public String getRequiredPermission()
	{
		return requiredPermission;
	}

	public void setRequiredPermission(String requiredPermission)
	{
		this.requiredPermission = requiredPermission;
	}

}
