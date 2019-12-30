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

import java.util.Date;

import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.UserSubmission;

/**
 * This is a wrapper class of the basic component. It includes the workPlanId of which workPlan it is in.
 *
 * @author gfowler
 */
public class ComponentSubmissionView
{
    @ConsumeField
    private String name;

    @ConsumeField
    private String status;

    @ConsumeField
    private String type;

    @ConsumeField
	private Date approveDts;

    @ConsumeField
    private Date lastActivityDts;

    @ConsumeField
    private String submissionId;

    @ConsumeField
    private String componentId;

    @ConsumeField
    private Boolean isChangeRequest;

    @ConsumeField
    private String workPlanID;

    @ConsumeField
    private String stepId;

    public ComponentSubmissionView(){}

    public ComponentSubmissionView(Component component, String workPlanId, String stepId){
        this.name = component.getName();
        this.status = component.getApprovalState();
        this.type = component.getComponentType();
        this.approveDts = component.getApprovedDts();
        this.lastActivityDts = component.getLastActivityDts();
        this.submissionId = null;
        this.componentId = component.getComponentId();
        this.isChangeRequest = false;
        this.workPlanID = workPlanId;
        this.stepId = stepId;
    }

    public ComponentSubmissionView(UserSubmission userSubmission){
        this.name = userSubmission.getSubmissionName();
        this.status = "N";
        this.type = userSubmission.getComponentType();
        this.approveDts = null;
        this.lastActivityDts = userSubmission.getUpdateDts();
        this.submissionId = userSubmission.getUserSubmissionId();
        this.componentId = null;
        this.isChangeRequest = (userSubmission.getOriginalComponentId() != null ? true : false);
        this.workPlanID = null;
        this.stepId = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getApproveDts() {
        return approveDts;
    }

    public void setApproveDts(Date approveDts) {
        this.approveDts = approveDts;
    }

    public Date getLastActivityDts() {
        return lastActivityDts;
    }

    public void setLastActivityDts(Date lastActivityDts) {
        this.lastActivityDts = lastActivityDts;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
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