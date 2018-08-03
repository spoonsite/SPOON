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
package edu.usu.sdl.openstorefront.validation;

import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import java.lang.reflect.Field;

/**
 *
 * @author dshurtleff
 */
public class WorkPlanLinkSubmissionUniqueHandler
		implements UniqueHandler<WorkPlanLink>
{

	@Override
	public boolean isUnique(Field field, Object value, WorkPlanLink fullDataObject)
	{
		boolean unique = true;
		WorkPlanLink existing = new WorkPlanLink();
		existing.setWorkPlanId(fullDataObject.getWorkPlanId());
		existing.setUserSubmissionId(fullDataObject.getUserSubmissionId());
		existing = existing.find();
		if (existing != null && !existing.getWorkPlanLinkId().equals(fullDataObject.getWorkPlanLinkId())) {
			unique = false;
		}
		return unique;
	}

	@Override
	public String getMessage()
	{
		return "User Submission may only be associated with one workflow at a time";
	}

}
