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
package edu.usu.sdl.openstorefront.report.model;

import java.math.BigDecimal;

/**
 *
 * @author dshurtleff
 */
public class EvaluationStatusReportLineModel
{

	private String entryName;
	private String evaluationVersion;
	private String evaluationTemplate;
	private String assignedGroup;
	private String assignedUser;
	private String workflowStatus;
	private BigDecimal percentageComplete;

	public EvaluationStatusReportLineModel()
	{
	}

	public String getEntryName()
	{
		return entryName;
	}

	public void setEntryName(String entryName)
	{
		this.entryName = entryName;
	}

	public String getEvaluationVersion()
	{
		return evaluationVersion;
	}

	public void setEvaluationVersion(String evaluationVersion)
	{
		this.evaluationVersion = evaluationVersion;
	}

	public String getEvaluationTemplate()
	{
		return evaluationTemplate;
	}

	public void setEvaluationTemplate(String evaluationTemplate)
	{
		this.evaluationTemplate = evaluationTemplate;
	}

	public String getAssignedGroup()
	{
		return assignedGroup;
	}

	public void setAssignedGroup(String assignedGroup)
	{
		this.assignedGroup = assignedGroup;
	}

	public String getAssignedUser()
	{
		return assignedUser;
	}

	public void setAssignedUser(String assignedUser)
	{
		this.assignedUser = assignedUser;
	}

	public String getWorkflowStatus()
	{
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus)
	{
		this.workflowStatus = workflowStatus;
	}

	public BigDecimal getPercentageComplete()
	{
		return percentageComplete;
	}

	public void setPercentageComplete(BigDecimal percentageComplete)
	{
		this.percentageComplete = percentageComplete;
	}

}
