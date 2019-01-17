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
	
	public static final String ACTION_REPORT = "ACTION";
	public static final String ENTRIES_BY_CAT_REPORT = "CATCOMP";
	public static final String ENTRIES_BY_ORG_REPORT = "CMPORG";
	public static final String ENTRY_DETAIL_REPORT = "TYPECOMP";
	public static final String ENTRY_LISTING_REPORT = "ENTRYLIST";
	public static final String ENTRY_REPORT = "COMPONENT";
	public static final String ENTRY_STATUS_REPORT = "ENTRYSTATUS";
	public static final String EVAL_STATUS_REPORT = "EVALSTAT";
	public static final String LINK_VALIDATION_REPORT = "LINKVALID";
	public static final String SUBMISSIONS_REPORT = "SUBMISSION";
	public static final String USAGE_REPORT = "USAGE";
	public static final String USER_ORG_REPORT = "ORGANIZATION";
	public static final String USER_REPORT = "USER";
	public static final String WORKPLAN_STATUS = "WORKPLANSTATUS";

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
		
		codeMap.put(ACTION_REPORT, newLookup(ReportType.class, ACTION_REPORT, "Action", "Report on items that need administrative attention."));
		codeMap.put(ENTRIES_BY_CAT_REPORT, newLookup(ReportType.class, ENTRIES_BY_CAT_REPORT, "Entries by Category", "Reports on entries in a category."));
		codeMap.put(ENTRIES_BY_ORG_REPORT, newLookup(ReportType.class, ENTRIES_BY_ORG_REPORT, "Entries by Organization", "Reports on entries that belong to an organization"));	
		codeMap.put(ENTRY_DETAIL_REPORT, newLookup(ReportType.class, ENTRY_DETAIL_REPORT, "Entry Detail", "Exports entry details"));
		codeMap.put(ENTRY_LISTING_REPORT, newLookup(ReportType.class, ENTRY_LISTING_REPORT, "Entry Listing", "List approved entries in a summary"));
		codeMap.put(ENTRY_REPORT, newLookup(ReportType.class, ENTRY_REPORT, "Entry", "Reports on entry statistic and usage."));
		codeMap.put(ENTRY_STATUS_REPORT, newLookup(ReportType.class, ENTRY_STATUS_REPORT, "Entry Status", "Gathers information about entry status for a time period."));
		codeMap.put(EVAL_STATUS_REPORT, newLookup(ReportType.class, EVAL_STATUS_REPORT, "Evaluation Status", "Reports on the status of unpublished evaluations."));
		codeMap.put(LINK_VALIDATION_REPORT, newLookup(ReportType.class, LINK_VALIDATION_REPORT, "Link Validation", "Reports on potentially broken external url links."));
		codeMap.put(SUBMISSIONS_REPORT, newLookup(ReportType.class, SUBMISSIONS_REPORT, "Submissions", "Reports on entry submissions."));
		codeMap.put(USAGE_REPORT, newLookup(ReportType.class, USAGE_REPORT, "Usage", "Reports on overall application usage."));
		codeMap.put(USER_ORG_REPORT, newLookup(ReportType.class, USER_ORG_REPORT, "User Organization", "Reports on the user organizations in the application and their usage."));
		codeMap.put(USER_REPORT, newLookup(ReportType.class, USER_REPORT, "User", "Reports on users and their usage of the application."));
		codeMap.put(WORKPLAN_STATUS, newLookup(ReportType.class, WORKPLAN_STATUS, "Workplan Status", "Shows the current status of items in workplans"));

		//update metadata
		((ReportType) codeMap.get(ACTION_REPORT)).setRequiredPermission(SecurityPermission.RUN_ACTION_REPORT);
		((ReportType) codeMap.get(ENTRIES_BY_CAT_REPORT)).setRequiredPermission(SecurityPermission.RUN_ENTRIES_BY_CAT_REPORT);
		((ReportType) codeMap.get(ENTRIES_BY_ORG_REPORT)).setRequiredPermission(SecurityPermission.RUN_ENTRIES_BY_ORG_REPORT);
		((ReportType) codeMap.get(ENTRY_DETAIL_REPORT)).setRequiredPermission(SecurityPermission.RUN_ENTRY_DETAIL_REPORT);
		((ReportType) codeMap.get(ENTRY_LISTING_REPORT)).setRequiredPermission(SecurityPermission.RUN_ENTRY_LISTING_REPORT);
		((ReportType) codeMap.get(ENTRY_REPORT)).setRequiredPermission(SecurityPermission.RUN_ENTRY_REPORT);
		((ReportType) codeMap.get(ENTRY_STATUS_REPORT)).setRequiredPermission(SecurityPermission.RUN_ENTRY_STATUS_REPORT);	
		((ReportType) codeMap.get(EVAL_STATUS_REPORT)).setRequiredPermission(SecurityPermission.RUN_EVAL_STATUS_REPORT);
		((ReportType) codeMap.get(LINK_VALIDATION_REPORT)).setRequiredPermission(SecurityPermission.RUN_LINK_VALIDATION_REPORT);
		((ReportType) codeMap.get(SUBMISSIONS_REPORT)).setRequiredPermission(SecurityPermission.RUN_SUBMISSIONS_REPORT);
		((ReportType) codeMap.get(USAGE_REPORT)).setRequiredPermission(SecurityPermission.RUN_USAGE_REPORT);
		((ReportType) codeMap.get(USER_ORG_REPORT)).setRequiredPermission(SecurityPermission.RUN_USER_ORG_REPORT);
		((ReportType) codeMap.get(USER_REPORT)).setRequiredPermission(SecurityPermission.RUN_USER_REPORT);
		((ReportType) codeMap.get(WORKPLAN_STATUS)).setRequiredPermission(SecurityPermission.RUN_WORKPLAN_STATUS);

		//update metadata for component type reports
		((ReportType) codeMap.get(ENTRIES_BY_ORG_REPORT)).setComponentReport(true);
		((ReportType) codeMap.get(ENTRIES_BY_CAT_REPORT)).setComponentReport(true);
		((ReportType) codeMap.get(ENTRY_REPORT)).setComponentReport(true);
		((ReportType) codeMap.get(ENTRY_DETAIL_REPORT)).setComponentReport(true);
		((ReportType) codeMap.get(ENTRY_LISTING_REPORT)).setComponentReport(true);
		((ReportType) codeMap.get(ENTRY_STATUS_REPORT)).setComponentReport(true);

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
