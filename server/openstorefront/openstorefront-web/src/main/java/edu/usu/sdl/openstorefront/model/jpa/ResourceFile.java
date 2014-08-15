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
public class ResourceFile {
    private String resourceFileId;
    private String fileName;
    private Date uploadDts;
    private String createUser;
    private Integer fileSizeBytes;

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
     * @return the uploadDts
     */
    public Date getUploadDts() {
        return uploadDts;
    }

    /**
     * @param uploadDts the uploadDts to set
     */
    public void setUploadDts(Date uploadDts) {
        this.uploadDts = uploadDts;
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
     * @return the fileSizeBytes
     */
    public Integer getFileSizeBytes() {
        return fileSizeBytes;
    }

    /**
     * @param fileSizeBytes the fileSizeBytes to set
     */
    public void setFileSizeBytes(Integer fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }
    
}
