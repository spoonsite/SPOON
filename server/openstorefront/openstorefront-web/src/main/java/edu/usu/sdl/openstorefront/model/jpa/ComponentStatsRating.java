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

/**
 *
 * @author jlaw
 */
public class ComponentStatsRating {
    private String ratingTypeCode;
    private String componentId;
    private Float averageRating;
    private Integer ratingsCount;

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
     * @return the averageRating
     */
    public Float getAverageRating() {
        return averageRating;
    }

    /**
     * @param averageRating the averageRating to set
     */
    public void setAverageRating(Float averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * @return the ratingsCount
     */
    public Integer getRatingsCount() {
        return ratingsCount;
    }

    /**
     * @param ratingsCount the ratingsCount to set
     */
    public void setRatingsCount(Integer ratingsCount) {
        this.ratingsCount = ratingsCount;
    }
}
