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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
@SystemTable
@APIDescription("Use to display type of work linke")
public class WorkPlanLinkType
		extends LookupEntity<WorkPlanLinkType>
{

	private static final long serialVersionUID = 1L;

	public static final String COMPONENT = "COMPONENT";
	public static final String EVALUTION = "EVALUATION";
	public static final String SUBMISSION = "SUBMISSION";

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public WorkPlanLinkType()
	{
	}

	public static String typeForWorkLink(WorkPlanLink workPlanLink)
	{
		if (workPlanLink.getComponentId() != null) {
			return COMPONENT;
		} else if (workPlanLink.getEvaluationId() != null) {
			return EVALUTION;
		} else if (workPlanLink.getUserSubmissionId() != null) {
			return SUBMISSION;
		}
		return OpenStorefrontConstant.NOT_AVAILABLE;
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(COMPONENT, newLookup(WorkPlanStepActionType.class, COMPONENT, "Entry"));
		codeMap.put(EVALUTION, newLookup(WorkPlanStepActionType.class, EVALUTION, "Evaluation"));
		codeMap.put(SUBMISSION, newLookup(WorkPlanStepActionType.class, SUBMISSION, "Submission"));
		return codeMap;
	}

}
