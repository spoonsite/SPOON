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
public class AttributeType 
    extends BaseEntity
{
    private String attributeType;
    private String description;
    private String iconFile;
    private Boolean visibleFlg;
    private Boolean requiredFlg;
    private Boolean architectureFlg;
    private Character activeStatus;
    private String createUser;
    private Date createDts;
    private String updateUser;
    private Date updateDts;

    @Override
    public void setActiveStatus(String activeStatus) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the attributeType
     */
    public String getAttributeType() {
        return attributeType;
    }

    /**
     * @param attributeType the attributeType to set
     */
    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
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
     * @return the iconFile
     */
    public String getIconFile() {
        return iconFile;
    }

    /**
     * @param iconFile the iconFile to set
     */
    public void setIconFile(String iconFile) {
        this.iconFile = iconFile;
    }

    /**
     * @return the visibleFlg
     */
    public Boolean getVisibleFlg() {
        return visibleFlg;
    }

    /**
     * @param visibleFlg the visibleFlg to set
     */
    public void setVisibleFlg(Boolean visibleFlg) {
        this.visibleFlg = visibleFlg;
    }

    /**
     * @return the requiredFlg
     */
    public Boolean getRequiredFlg() {
        return requiredFlg;
    }

    /**
     * @param requiredFlg the requiredFlg to set
     */
    public void setRequiredFlg(Boolean requiredFlg) {
        this.requiredFlg = requiredFlg;
    }

    /**
     * @return the architectureFlg
     */
    public Boolean getArchitectureFlg() {
        return architectureFlg;
    }

    /**
     * @param architectureFlg the architectureFlg to set
     */
    public void setArchitectureFlg(Boolean architectureFlg) {
        this.architectureFlg = architectureFlg;
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
    
}
