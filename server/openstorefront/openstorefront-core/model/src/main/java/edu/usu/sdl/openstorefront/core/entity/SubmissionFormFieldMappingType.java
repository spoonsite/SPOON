/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
@APIDescription("Defines the available field type; the UI will determine how it is rendered")
public class SubmissionFormFieldMappingType
		extends LookupEntity<SubmissionFormFieldMappingType>
{

	private static final long serialVersionUID = 1L;

	public static final String FIELD = "FIELD";
	public static final String COMPLEX = "COMPLEX";
	public static final String ATTRIBUTE = "ATTRIBUTE";
	public static final String SUBMISSION = "SUBMISSION";
	public static final String NONE = "NONE";

	@SuppressWarnings({"squid:S1186"})
	public SubmissionFormFieldMappingType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(FIELD, newLookup(SubmissionFormFieldMappingType.class, FIELD, "Field"));
		codeMap.put(COMPLEX, newLookup(SubmissionFormFieldType.class, COMPLEX, "Complex"));
		codeMap.put(ATTRIBUTE, newLookup(SubmissionFormFieldType.class, ATTRIBUTE, "Attribute"));
		codeMap.put(SUBMISSION, newLookup(SubmissionFormFieldType.class, SUBMISSION, "Submission"));
		codeMap.put(NONE, newLookup(SubmissionFormFieldType.class, NONE, "None"));
		return codeMap;
	}

}
