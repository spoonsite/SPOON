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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.Size;
import javax.ws.rs.QueryParam;

/**
 *
 * @author dshurtleff
 */
public class EvaluationFilterParams
	extends  FilterQueryParams
{
	
	@QueryParam("workflowStatus")
	@Size(min = 0, max = 255)
	@Sanitize(TextSanitizer.class)
	private String workflowStatus;
	
	@QueryParam("assignedUser")
	@Size(min = 0, max = 255)
	private String assignedUser;
	
	@QueryParam("assignedGroup")
	@Size(min = 0, max = 255)	
	private String assignedGroup;
	
	@QueryParam("published")
	private Boolean published;
	
			
	@QueryParam("templateId")
	@Size(min = 0, max = 255)	
	private String templateId;
	
	@QueryParam("checklistTemplateId")
	@Size(min = 0, max = 255)	
	private String checklistTemplateId;
	
	
	public EvaluationFilterParams()
	{
	}

	public String getWorkflowStatus()
	{
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus)
	{
		this.workflowStatus = workflowStatus;
	}

	public String getAssignedUser()
	{
		return assignedUser;
	}

	public void setAssignedUser(String assignedUser)
	{
		this.assignedUser = assignedUser;
	}

	public String getAssignedGroup()
	{
		return assignedGroup;
	}

	public void setAssignedGroup(String assignedGroup)
	{
		this.assignedGroup = assignedGroup;
	}

	public Boolean getPublished()
	{
		return published;
	}

	public void setPublished(Boolean published)
	{
		this.published = published;
	}

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public String getChecklistTemplateId()
	{
		return checklistTemplateId;
	}

	public void setChecklistTemplateId(String checklistTemplateId)
	{
		this.checklistTemplateId = checklistTemplateId;
	}
}
