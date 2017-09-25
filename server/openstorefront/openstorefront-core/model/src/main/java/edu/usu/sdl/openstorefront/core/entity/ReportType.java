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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	private String requiredPermission;
	private boolean componentReport;
	private List<String> supportedFormats = new ArrayList<>();

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

		//update metadata
		((ReportType) codeMap.get(USAGE)).setRequiredPermission(SecurityPermission.ADMIN_TRACKING);
		((ReportType) codeMap.get(LINK_VALIDATION)).setRequiredPermission(SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		((ReportType) codeMap.get(USER)).setRequiredPermission(SecurityPermission.ADMIN_USER_MANAGEMENT);
		((ReportType) codeMap.get(SUBMISSION)).setRequiredPermission(SecurityPermission.ADMIN_ENTRY_MANAGEMENT);
		((ReportType) codeMap.get(ORGANIZATION)).setRequiredPermission(SecurityPermission.ADMIN_USER_MANAGEMENT);
		((ReportType) codeMap.get(EVALUATION_STATUS)).setRequiredPermission(SecurityPermission.EVALUATIONS);

		//update metadata for component type reports
		((ReportType) codeMap.get(COMPONENT_ORGANIZATION)).setComponentReport(true);
		((ReportType) codeMap.get(CATEGORY_COMPONENT)).setComponentReport(true);
		((ReportType) codeMap.get(COMPONENT)).setComponentReport(true);

		//Add  formats
		for (LookupEntity lookupEntity : codeMap.values()) {
			((ReportType) lookupEntity).getSupportedFormats().add(ReportFormat.CSV);
		}
		codeMap.put(COMPONENT_DETAIL, newLookup(ReportType.class, COMPONENT_DETAIL, "Entry Detail", "Exports entry details"));
		((ReportType) codeMap.get(COMPONENT_DETAIL)).setComponentReport(true);
		((ReportType) codeMap.get(COMPONENT_DETAIL)).getSupportedFormats().add(ReportFormat.HTML);
		
		codeMap.put(ACTION_REPORT, newLookup(ReportType.class, ACTION_REPORT, "Action Report", "Emails a TODO report"));
		((ReportType) codeMap.get(ACTION_REPORT)).setComponentReport(true);
		((ReportType) codeMap.get(ACTION_REPORT)).getSupportedFormats().add(ReportFormat.HTML);
		((ReportType) codeMap.get(ACTION_REPORT)).getSupportedFormats().add(ReportFormat.PDF);


		return codeMap;
	}

	public List<String> getSupportedFormats()
	{
		return supportedFormats;
	}

	public void setSupportedFormats(List<String> supportedFormats)
	{
		this.supportedFormats = supportedFormats;
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
