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

/**
 *
 * @author jlaw
 */
public class UserWatch 
    extends BaseEntity
{
    private String userWatchId;
    private Date lastUpdatedDts;
    private Character activeStatus;
    private Date createDts;
    private Date lastViewDts;
    private String username;
    private String componentId;
    private Boolean sendAlert;
    
    @Override
    public void setActiveStatus(String activeStatus) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the userWatchId
     */
    public String getUserWatchId() {
        return userWatchId;
    }

    /**
     * @param userWatchId the userWatchId to set
     */
    public void setUserWatchId(String userWatchId) {
        this.userWatchId = userWatchId;
    }

    /**
     * @return the lastUpdatedDts
     */
    public Date getLastUpdatedDts() {
        return lastUpdatedDts;
    }

    /**
     * @param lastUpdatedDts the lastUpdatedDts to set
     */
    public void setLastUpdatedDts(Date lastUpdatedDts) {
        this.lastUpdatedDts = lastUpdatedDts;
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
     * @return the lastViewDts
     */
    public Date getLastViewDts() {
        return lastViewDts;
    }

    /**
     * @param lastViewDts the lastViewDts to set
     */
    public void setLastViewDts(Date lastViewDts) {
        this.lastViewDts = lastViewDts;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
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
     * @return the sendAlert
     */
    public Boolean getSendAlert() {
        return sendAlert;
    }

    /**
     * @param sendAlert the sendAlert to set
     */
    public void setSendAlert(Boolean sendAlert) {
        this.sendAlert = sendAlert;
    }
}
