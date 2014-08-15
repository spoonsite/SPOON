/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.model.jpa;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jlaw
 */
public class Component 
    extends BaseEntity
{

    @NotNull
    private String componentId;
    private String name;
    private String description;
    private String parentComponentId;
    private List<String> subComponentIds;
    private String guid;
    private String organization;
    private Date release_date;
    private Date post_date;
    private String version;
    private Character activeStatus;
    private Character approvalState;
    private String approvedUser;
    private Date approvedDts;
    private String createUser;
    private Date createDts;
    private String updateUser;
    private Date updateDts;
    private Date lastActivityDts;
    
    
    
    /**
     *
     * @param activeStatus
     */
    @Override
    public void setActiveStatus(String activeStatus) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the componentId
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * @param componentId the componentId to set
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the parentComponentId
     */
    public String getParentComponentId() {
        return parentComponentId;
    }

    /**
     * @param parentComponentId the parentComponentId to set
     */
    public void setParentComponentId(String parentComponentId) {
        this.parentComponentId = parentComponentId;
    }

    /**
     * @return the subComponentIds
     */
    public List<String> getSubComponentIds() {
        return subComponentIds;
    }

    /**
     * @param subComponentIds the subComponentIds to set
     */
    public void setSubComponentIds(List<String> subComponentIds) {
        this.subComponentIds = subComponentIds;
    }

    /**
     * @return the guid
     */
    public String getGuid() {
        return guid;
    }

    /**
     * @param guid the guid to set
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * @return the organization
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * @param organization the organization to set
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * @return the release_date
     */
    public Date getRelease_date() {
        return release_date;
    }

    /**
     * @param release_date the release_date to set
     */
    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    /**
     * @return the post_date
     */
    public Date getPost_date() {
        return post_date;
    }

    /**
     * @param post_date the post_date to set
     */
    public void setPost_date(Date post_date) {
        this.post_date = post_date;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the activeStatus
     */
    public Character getActiveStatus() {
        return activeStatus;
    }

    /**
     * @param activeStatus the activeStatus to set
     */
    public void setActiveStatus(Character activeStatus) {
        this.activeStatus = activeStatus;
    }

    /**
     * @return the approvalState
     */
    public Character getApprovalState() {
        return approvalState;
    }

    /**
     * @param approvalState the approvalState to set
     */
    public void setApprovalState(Character approvalState) {
        this.approvalState = approvalState;
    }

    /**
     * @return the approvedUser
     */
    public String getApprovedUser() {
        return approvedUser;
    }

    /**
     * @param approvedUser the approvedUser to set
     */
    public void setApprovedUser(String approvedUser) {
        this.approvedUser = approvedUser;
    }

    /**
     * @return the approvedDts
     */
    public Date getApprovedDts() {
        return approvedDts;
    }

    /**
     * @param approvedDts the approvedDts to set
     */
    public void setApprovedDts(Date approvedDts) {
        this.approvedDts = approvedDts;
    }

    /**
     * @return the createUser
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser the createUser to set
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * @return the createDts
     */
    public Date getCreateDts() {
        return createDts;
    }

    /**
     * @param createDts the createDts to set
     */
    public void setCreateDts(Date createDts) {
        this.createDts = createDts;
    }

    /**
     * @return the updateUser
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser the updateUser to set
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return the updateDts
     */
    public Date getUpdateDts() {
        return updateDts;
    }

    /**
     * @param updateDts the updateDts to set
     */
    public void setUpdateDts(Date updateDts) {
        this.updateDts = updateDts;
    }

    /**
     * @return the lastActivityDts
     */
    public Date getLastActivityDts() {
        return lastActivityDts;
    }

    /**
     * @param lastActivityDts the lastActivityDts to set
     */
    public void setLastActivityDts(Date lastActivityDts) {
        this.lastActivityDts = lastActivityDts;
    }

}
