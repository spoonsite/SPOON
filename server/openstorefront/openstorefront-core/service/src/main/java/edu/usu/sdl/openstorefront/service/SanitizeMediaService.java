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
import java.util.Optional;

import org.apache.poi.util.TempFile;

import edu.usu.sdl.openstorefront.service.sanitize.sanitizer.DocumentSanitizer;
import edu.usu.sdl.openstorefront.service.sanitize.sanitizer.ImageDocumentSanitizerImpl;
 
 /**
 * @author Jake Cook
 * Handler for running the sanitizer process on files
 */
public class SanitizeMediaService {

    public SanitizeMediaService() {}
    
    /**
     * returns a sanitizer or error if file is not a sanitizable type
     * @return Optional<DocumentSanitizer>
     */
    public Optional<DocumentSanitizer> getSanitizer(String mimeType) {
        DocumentSanitizer documentSanitizer = null;
        if (mimeType.contains("image")) {
            documentSanitizer = new ImageDocumentSanitizerImpl();
        }
        return Optional.ofNullable(documentSanitizer);
    }

    /**
     * Runs the sanitation on the file
     * @return Optional<File>
     */
    public Optional<File> sanitize(DocumentSanitizer sanitizer, File file) {
        return sanitizer.makeSafe(file);  // TODO: have 'makeSafe' return a file rather than change in place;
    }

}
