/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.sort.LookupComparator;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Defines workflow states")
public class WorkflowStatus
		extends LookupEntity<WorkflowStatus>
{

	public WorkflowStatus()
	{
	}

	public static WorkflowStatus initalStatus()
	{
		List<WorkflowStatus> status = allActiveStatuses();
		if (status.isEmpty()) {
			return null;
		} else {
			return status.get(0);
		}
	}

	public static WorkflowStatus finalStatus()
	{
		List<WorkflowStatus> status = allActiveStatuses();
		if (status.isEmpty()) {
			return null;
		} else {
			return status.get(status.size() - 1);
		}
	}

	private static List<WorkflowStatus> allActiveStatuses()
	{
		Service service = ServiceProxyFactory.getServiceProxy();
		List<WorkflowStatus> status = service.getLookupService().findLookup(WorkflowStatus.class);
		status.sort(new LookupComparator<>());
		return status;
	}

}
