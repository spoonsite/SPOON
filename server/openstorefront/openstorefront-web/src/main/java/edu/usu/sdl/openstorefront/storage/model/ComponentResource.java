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

package edu.usu.sdl.openstorefront.storage.model;

import java.util.Date;

/**
 *
 * @author jlaw
 */
public class ComponentResource 
    extends BaseEntity
{
    private String resourceId;
    private String componentId;
    private String resourceFileId;
    private String link;
    private String name;
    private String description;
    private Character activeStatus;
    private String createUser;
    private Date createDts;
    private String updateUser;
    private Date updateDts;
    private String componentResourceCol;

    @Override
    public void setActiveStatus(String activeStatus) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the resourceId
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * @param resourceId the resourceId to set
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
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
     * @return the resourceFileId
     */
    public String getResourceFileId() {
        return resourceFileId;
    }

    /**
     * @param resourceFileId the resourceFileId to set
     */
    public void setResourceFileId(String resourceFileId) {
        this.resourceFileId = resourceFileId;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
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
     * @return the componentResourceCol
     */
    public String getComponentResourceCol() {
        return componentResourceCol;
    }

    /**
     * @param componentResourceCol the componentResourceCol to set
     */
    public void setComponentResourceCol(String componentResourceCol) {
        this.componentResourceCol = componentResourceCol;
    }
}
