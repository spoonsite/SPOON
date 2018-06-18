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

import edu.usu.sdl.openstorefront.core.entity.MediaFile;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormTemplate;
import edu.usu.sdl.openstorefront.core.entity.UserSubmission;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionMedia;
import edu.usu.sdl.openstorefront.core.model.EditSubmissionOptions;
import edu.usu.sdl.openstorefront.core.model.VerifySubmissionTemplateResult;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.InputStream;
import java.util.List;

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
	 * @return submission form
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
	 * Checks the template mappings to make sure they represent a complete
	 * mapping to a valid entry.
	 *
	 * @param template
	 * @param componentType to verify against
	 * @return
	 */
	public ValidationResult validateTemplate(SubmissionFormTemplate template, String componentType);

	/**
	 * Finds all user submission for a given user
	 *
	 * @param ownerUsername
	 * @return
	 */
	public List<UserSubmission> getUserSubmissions(String ownerUsername);

	/**
	 * Saves a user submission (does not convert the submission at this time)
	 *
	 * @param userSubmission
	 * @return
	 */
	public UserSubmission saveUserSubmission(UserSubmission userSubmission);

	/**
	 * Save Submission Media for user (it will be moved to actual records)
	 *
	 * @param userSubmissionId
	 * @param templateFieldId
	 * @param in
	 * @param mediaFile
	 * @return saved form media metadata
	 */
	public UserSubmissionMedia saveSubmissionFormMedia(String userSubmissionId, String templateFieldId, MediaFile mediaFile, InputStream in);

	/**
	 * Convert submission to Components but it does not save the results
	 *
	 * @param userSubmission
	 * @return ComponentFormSet with the entries
	 */
	public VerifySubmissionTemplateResult verifySubmission(UserSubmission userSubmission);

	/**
	 * Save template and marks as default and sets to Verified.
	 *
	 * @param template
	 */
	public void saveSubmissionTemplateAsDefault(SubmissionFormTemplate template);

	/**
	 * Convert and saves component The user submission is then removed.
	 *
	 * @param userSubmission
	 */
	public void submitUserSubmissionForApproval(UserSubmission userSubmission);

	/**
	 * Convert an Entry and related sub-entries into a UserSubmission This fill
	 * in what it can based on the template.
	 *
	 * It will resolve the appropriate template base on the entry type.
	 *
	 * @param componentId
	 * @param forChangeRequest
	 * @param removeComponent
	 * @return
	 */
	public UserSubmission editComponentForSubmission(String componentId, EditSubmissionOptions options);

	/**
	 * Creates a change request from a submission
	 *
	 * @param userSubmission
	 */
	public void submitChangeRequestForApproval(UserSubmission userSubmission);

	/**
	 * Reassign Ownership
	 *
	 * @param userSubmissionId
	 * @param newOwnerUsername
	 */
	public void reassignUserSubmission(String userSubmissionId, String newOwnerUsername);

	/**
	 * Delete a submission and all media
	 *
	 * @param userSubmissionId
	 */
	public void deleteUserSubmission(String userSubmissionId);

	/**
	 * Delete just the media from a submission
	 *
	 * @param submissionMediaId
	 */
	public void deleteUserSubmissionMedia(String submissionMediaId);

}
