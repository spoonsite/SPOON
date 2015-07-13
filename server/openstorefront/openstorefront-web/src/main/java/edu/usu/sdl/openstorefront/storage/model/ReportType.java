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
package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.doc.APIDescription;
import static edu.usu.sdl.openstorefront.storage.model.LookupEntity.newLookup;
import edu.usu.sdl.openstorefront.util.SystemTable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
@SystemTable
@APIDescription("Defines the reports")
public class ReportType
		extends LookupEntity
{

	public static final String USAGE = "USAGE";
	public static final String LINK_VALIDATION = "LINKVALID";
	public static final String COMPONENT = "COMPONENT";
	public static final String USER = "USER";
	public static final String ORGANIZATION = "ORGANIZATION";
	public static final String SUBMISSION = "SUBMISSION";

	public ReportType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(USAGE, newLookup(ReportType.class, USAGE, "Usage", "Reports on overall application usage."));
		codeMap.put(LINK_VALIDATION, newLookup(ReportType.class, LINK_VALIDATION, "Link Validation", "Reports on potentially broken external url links."));
		codeMap.put(COMPONENT, newLookup(ReportType.class, COMPONENT, "Component", "Reports on component statistic and usage."));
		codeMap.put(USER, newLookup(ReportType.class, USER, "User", "Reports on users and thier usage of the application."));
		codeMap.put(ORGANIZATION, newLookup(ReportType.class, ORGANIZATION, "Organization", "Reports on the organizations in the appliaction and thier usage."));
		codeMap.put(SUBMISSION, newLookup(ReportType.class, SUBMISSION, "Submissions", "Reports on component submissions."));
		return codeMap;
	}

}
