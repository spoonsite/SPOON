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
import edu.usu.sdl.openstorefront.service.sanitize.detector.DocumentDetector;
import edu.usu.sdl.openstorefront.service.sanitize.detector.ExcelDocumentDetectorImpl;
import edu.usu.sdl.openstorefront.service.sanitize.detector.PdfDocumentDetectorImpl;
import edu.usu.sdl.openstorefront.service.sanitize.detector.PowerpointDocumentDetectorImpl;
import edu.usu.sdl.openstorefront.service.sanitize.detector.WordDocumentDetectorImpl;
import edu.usu.sdl.openstorefront.service.sanitize.sanitizer.DocumentSanitizer;
import edu.usu.sdl.openstorefront.service.sanitize.sanitizer.ImageDocumentSanitizerImpl;

/**
 * @author Jake Cook
 * Handler for running the sanitizer process on files
 */
public class SanitizeMediaService {
    File file;
    String mimeType;
    DocumentDetector documentDetector = null;
    DocumentSanitizer documentSanitizer = null;

    public SanitizeMediaService(File file, String mimeType) {
        this.file = file;
        this.mimeType = mimeType;

    }
    
    /**
     * Attempts to assign a sanitizer method to the file
     * @return boolean
     */
    public boolean sanitizable() {
        if (mimeType.contains("image")) {
            documentSanitizer = new ImageDocumentSanitizerImpl();
        } else if (mimeType.contains("pdf")) {
            documentDetector = new PdfDocumentDetectorImpl();
        } else if (mimeType.contains("msword")) {
            documentDetector = new WordDocumentDetectorImpl();
        } else if (mimeType.contains("excel")) {
            documentDetector = new ExcelDocumentDetectorImpl();
        } else if (mimeType.contains("powerpoint")) {
            documentDetector = new PowerpointDocumentDetectorImpl();
        } else {
            return false;
        }
        return true;
        
    }

    /**
     * Runs the sanitation on the file
     * @return boolean
     */
    public boolean sanitize() {
        // if (this.documentSanitizer != null) {
        //     return this.documentSanitizer.madeSafe(this.file);
        // }
        // if (this.documentDetector != null) {
        //     return this.documentDetector.isSafe(this.file);
        // }
        return (documentSanitizer == null) ? documentDetector.isSafe(this.file) : documentSanitizer.madeSafe(this.file);
    }

    /**
     * Returns the file object as modified by the sanitized process
     * @return File
     */
    public File getSanitzedFile() {
        return this.file;
    }
}
