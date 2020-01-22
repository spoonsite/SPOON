/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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


import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;

/**
 * This is a wrapper class of the basic component. It includes the workPlanId of which workPlan it is in.
 *
 * @author gfowler
 */
public class ComponentSubmissionView
    extends SubmissionView
{

    @ConsumeField
    private String workPlanID;

    @ConsumeField
    private String stepId;
	
	@ConsumeField
    private Boolean isChangeRequest;

    public ComponentSubmissionView(){}

    public ComponentSubmissionView(SubmissionView submissionView, String workPlanId, String stepId){		
		super.setComponentTypeLabel(submissionView.getComponentTypeLabel());
		super.setApprovalStateLabel(submissionView.getApprovalStateLabel());
		super.setNumberOfPendingChanges(submissionView.getNumberOfPendingChanges());
		super.setStatusOfPendingChange(submissionView.getStatusOfPendingChange());
		super.setPendingChangeComponentId(submissionView.getPendingChangeComponentId());
		super.setPendingChangeSubmitDts(submissionView.getPendingChangeSubmitDts());
		super.setUserSubmissionId(submissionView.getUserSubmissionId());
		super.setName(submissionView.getName());
		super.setApprovalState(submissionView.getApprovalState());
		super.setApprovedDts(submissionView.getApprovedDts());
		super.setDescription(submissionView.getDescription());
		super.setLastActivityDts(submissionView.getLastActivityDts());
		super.setComponentId(submissionView.getComponentId());
		super.setUpdateDts(submissionView.getUpdateDts());
		super.setSubmissionOriginalComponentId(submissionView.getSubmissionOriginalComponentId());
		super.setEvaluationsAttached(submissionView.getEvaluationsAttached());
        this.workPlanID = workPlanId;
        this.stepId = stepId;
    }


    public String getWorkPlanID() {
        return workPlanID;
    }

    public void setWorkPlanID(String workPlanID) {
        this.workPlanID = workPlanID;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }
	
	public Boolean getIsChangeRequest() {
        return isChangeRequest;
    }

    public void setIsChangeRequest(Boolean isChangeRequest) {
        this.isChangeRequest = isChangeRequest;
    }
}
