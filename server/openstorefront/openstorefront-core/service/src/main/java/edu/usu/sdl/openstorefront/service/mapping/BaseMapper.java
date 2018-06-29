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
package edu.usu.sdl.openstorefront.service.mapping;

import edu.usu.sdl.openstorefront.core.entity.SubmissionFormField;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionField;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.ComponentFormSet;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseMapper
{

	/**
	 * Updates the main component based on mapping This put in stub values which
	 * is useful for template validation.
	 *
	 * @param componentAll
	 * @param submissionField
	 * @return new child components
	 */
	public List<ComponentAll> mapField(ComponentAll componentAll, SubmissionFormField submissionField)
			throws MappingException
	{
		return mapField(componentAll, submissionField, null);
	}

	/**
	 * Updates the main component based on mapping
	 *
	 * @param componentAll
	 * @param submissionField
	 * @param userSubmission
	 * @return new child component or null if no child was created.
	 */
	public abstract List<ComponentAll> mapField(ComponentAll componentAll, SubmissionFormField submissionField, UserSubmissionField userSubmissionField)
			throws MappingException;

	/**
	 * Map the component or sub-entity back to a user submission
	 *
	 * @param submissionField
	 * @param componentFormSet
	 * @return
	 * @throws MappingException
	 */
	public abstract UserSubmissionFieldMedia mapComponentToSubmission(SubmissionFormField submissionField, ComponentFormSet componentFormSet) throws MappingException;

}
