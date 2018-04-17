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
@APIDescription("Defines report output types")
public class ReportTransmissionType
		extends LookupEntity<ReportTransmissionType>
{

	public static final String VIEW = "VIEW";
	public static final String EMAIL = "EMAIL";
	public static final String CONFLUENCE = "CONFLUENCE";

	private boolean supportsMultiple = false;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ReportTransmissionType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();

		codeMap.put(VIEW, newLookup(ReportTransmissionType.class, VIEW, "View"));
		codeMap.put(EMAIL, newLookup(ReportTransmissionType.class, EMAIL, "Email"));
		codeMap.put(CONFLUENCE, newLookup(ReportTransmissionType.class, CONFLUENCE, "Confluence"));

		((ReportTransmissionType) codeMap.get(EMAIL)).setSupportsMultiple(true);
		((ReportTransmissionType) codeMap.get(CONFLUENCE)).setSupportsMultiple(true);

		return codeMap;
	}

	public boolean getSupportsMultiple()
	{
		return supportsMultiple;
	}

	public void setSupportsMultiple(boolean supportsMultiple)
	{
		this.supportsMultiple = supportsMultiple;
	}

}
