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
public class EvaluationArtifact {
    private String reviewArtifactId;
    private String externalUrl;
    private String fileName;
    private String name;
    private String componentReviewId;

    /**
     * @return the reviewArtifactId
     */
    public String getReviewArtifactId() {
        return reviewArtifactId;
    }

    /**
     * @param reviewArtifactId the reviewArtifactId to set
     */
    public void setReviewArtifactId(String reviewArtifactId) {
        this.reviewArtifactId = reviewArtifactId;
    }

    /**
     * @return the externalUrl
     */
    public String getExternalUrl() {
        return externalUrl;
    }

    /**
     * @param externalUrl the externalUrl to set
     */
    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
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
}
