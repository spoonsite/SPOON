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
public class ReviewRating 
    extends BaseEntity
{
    private String componentReviewId;
    private String ratingTypeCode;
    private Integer value;
    private String comment;
    private Character activeStatus;
    private String createUser;
    private Date createDts;
    private String updateUser;
    private Date updateDts;

    /**
     * @return the componentReviewId
     */
    public String getComponentReviewId() {
        return componentReviewId;
    }

    /**
     * @param componentReviewId the componentReviewId to set
     */
    public void setComponentReviewId(String componentReviewId) {
        this.componentReviewId = componentReviewId;
    }

    /**
     * @return the ratingTypeCode
     */
    public String getRatingTypeCode() {
        return ratingTypeCode;
    }

    /**
     * @param ratingTypeCode the ratingTypeCode to set
     */
    public void setRatingTypeCode(String ratingTypeCode) {
        this.ratingTypeCode = ratingTypeCode;
    }

    /**
     * @return the value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
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

    @Override
    public void setActiveStatus(String activeStatus) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
