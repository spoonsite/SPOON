/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.SubmissionFormResource;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormTemplate;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.InputStream;

/**
 *
 * @author dshurtleff
 */
public interface SubmissionFormService
		extends AsyncService
{

	/**
	 * Saves a template and validates it to determine status
	 *
	 * @param template
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public SubmissionFormTemplate saveSubmissionFormTemplate(SubmissionFormTemplate template);

	/**
	 * Deletes a Submission Form Template
	 *
	 * @param templateId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteSubmissionFormTemplate(String templateId);

	/**
	 * Save Submission Resource
	 *
	 * @param resource
	 * @param in
	 */
	public SubmissionFormResource saveSubmissionFormResource(SubmissionFormResource resource, InputStream in);

	/**
	 * Delete Submission Resource this will remove it from the file system
	 *
	 * @param resourceId
	 */
	public void deleteSubmissionFormResource(String resourceId);

	/**
	 * Checks the template mappings to make sure they represent a complete
	 * mapping to a valid entry.
	 *
	 * @param template
	 * @return
	 */
	public ValidationResult validateTemplate(SubmissionFormTemplate template);

}
