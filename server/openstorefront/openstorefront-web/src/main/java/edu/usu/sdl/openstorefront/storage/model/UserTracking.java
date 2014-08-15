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
public class UserTracking {
    private String trackingId;
    private String trackEventTypeCode;
    private String browser;
    private String userAgent;
    private String browserVersion;
    private Date eventDts;
    private Integer screenWidth;
    private Integer screenHeight;
    private Boolean mobileDevice;
    private String osPlatform;
    private String clientIp;
    private String userProfileUsername;

    /**
     * @return the trackingId
     */
    public String getTrackingId() {
        return trackingId;
    }

    /**
     * @param trackingId the trackingId to set
     */
    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
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
     * @return the browser
     */
    public String getBrowser() {
        return browser;
    }

    /**
     * @param browser the browser to set
     */
    public void setBrowser(String browser) {
        this.browser = browser;
    }

    /**
     * @return the userAgent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * @param userAgent the userAgent to set
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * @return the browserVersion
     */
    public String getBrowserVersion() {
        return browserVersion;
    }

    /**
     * @param browserVersion the browserVersion to set
     */
    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
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
     * @return the screenWidth
     */
    public Integer getScreenWidth() {
        return screenWidth;
    }

    /**
     * @param screenWidth the screenWidth to set
     */
    public void setScreenWidth(Integer screenWidth) {
        this.screenWidth = screenWidth;
    }

    /**
     * @return the screenHeight
     */
    public Integer getScreenHeight() {
        return screenHeight;
    }

    /**
     * @param screenHeight the screenHeight to set
     */
    public void setScreenHeight(Integer screenHeight) {
        this.screenHeight = screenHeight;
    }

    /**
     * @return the mobileDevice
     */
    public Boolean getMobileDevice() {
        return mobileDevice;
    }

    /**
     * @param mobileDevice the mobileDevice to set
     */
    public void setMobileDevice(Boolean mobileDevice) {
        this.mobileDevice = mobileDevice;
    }

    /**
     * @return the osPlatform
     */
    public String getOsPlatform() {
        return osPlatform;
    }

    /**
     * @param osPlatform the osPlatform to set
     */
    public void setOsPlatform(String osPlatform) {
        this.osPlatform = osPlatform;
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
    
}
