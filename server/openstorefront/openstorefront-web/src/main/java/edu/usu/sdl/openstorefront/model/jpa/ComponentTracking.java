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
public class ComponentTracking {
    private String componentTrackingId;
    private String trackEventTypeCode;
    private String componentId;
    private String userProfileUsername;
    private Date eventDts;
    private String clientIp;

    /**
     * @return the componentTrackingId
     */
    public String getComponentTrackingId() {
        return componentTrackingId;
    }

    /**
     * @param componentTrackingId the componentTrackingId to set
     */
    public void setComponentTrackingId(String componentTrackingId) {
        this.componentTrackingId = componentTrackingId;
    }

    /**
     * @return the trackEventTypeCode
     */
    public String getTrackEventTypeCode() {
        return trackEventTypeCode;
    }

    /**
     * @param trackEventTypeCode the trackEventTypeCode to set
     */
    public void setTrackEventTypeCode(String trackEventTypeCode) {
        this.trackEventTypeCode = trackEventTypeCode;
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
     * @return the userProfileUsername
     */
    public String getUserProfileUsername() {
        return userProfileUsername;
    }

    /**
     * @param userProfileUsername the userProfileUsername to set
     */
    public void setUserProfileUsername(String userProfileUsername) {
        this.userProfileUsername = userProfileUsername;
    }

    /**
     * @return the eventDts
     */
    public Date getEventDts() {
        return eventDts;
    }

    /**
     * @param eventDts the eventDts to set
     */
    public void setEventDts(Date eventDts) {
        this.eventDts = eventDts;
    }

    /**
     * @return the clientIp
     */
    public String getClientIp() {
        return clientIp;
    }

    /**
     * @param clientIp the clientIp to set
     */
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
}
