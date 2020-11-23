/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service;

import java.io.File;
import edu.usu.sdl.openstorefront.service.sanitize.sanitizer.DocumentSanitizer;
import edu.usu.sdl.openstorefront.service.sanitize.sanitizer.ImageDocumentSanitizerImpl;

/**
 * @author Jake Cook
 * Handler for running the sanitizer process on files
 */
public class SanitizeMediaService {
    File file;
    String mimeType;
    DocumentSanitizer documentSanitizer = null;
    Boolean sanitized = false;

    public SanitizeMediaService(File file, String mimeType) {
        this.file = file;
        this.mimeType = mimeType;
    }
    
    /**
     * Attempts to assign a sanitizer method to the file
     * @return boolean
     */
    public boolean setSanitizer() {
        if (mimeType.contains("image")) {
            documentSanitizer = new ImageDocumentSanitizerImpl();
            return true;
        }
        return false;
    }

    /**
     * Runs the sanitation on the file
     * @return boolean
     */
    public void sanitize() {
        this.sanitized = documentSanitizer.madeSafe(this.file);
    }

    /**
     * @return boolean
     */
    public boolean isSanitized() {
        return this.sanitized;
    }

    /**
     * Returns the file object as modified by the sanitized process
     * @return File
     */
    public File getSanitzedFile() {
        return this.file;
    }
}
