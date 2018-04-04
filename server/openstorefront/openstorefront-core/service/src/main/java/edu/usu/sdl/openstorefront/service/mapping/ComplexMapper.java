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
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormFieldType;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionField;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import java.util.ArrayList;
import java.util.List;

/**
 * This maps complex object back into entities; use for contacts, media etc.
 *
 * @author dshurtleff
 */
public class ComplexMapper
		extends BaseMapper
{

	@Override
	public List<ComponentAll> mapField(ComponentAll componentAll, SubmissionFormField submissionField, UserSubmissionField userSubmissionField)
	{
		List<ComponentAll> childComponents = new ArrayList<>();

		String fieldType = submissionField.getFieldType();
		switch (fieldType) {

			case SubmissionFormFieldType.ATTRIBUTE:
				addAttribute(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.ATTRIBUTE_MULTI:
				addAttribute(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.ATTRIBUTE_RADIO:
				addAttribute(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.ATTRIBUTE_MULTI_CHECKBOX:
				addAttribute(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.CONTACT:
				addContact(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.CONTACT_MULTI:
				addContact(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.EXT_DEPENDENCY:
				addDependency(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.EXT_DEPENDENCY_MULTI:
				addDependency(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.MEDIA:
				addMedia(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.MEDIA_MULTI:
				addMedia(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.RESOURCE:
				addResource(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.RESOURCE_MULTI:
				addResource(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.TAG:
				addTag(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.TAG_MULTI:
				addTag(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.RELATIONSHIPS:
				addRelationships(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.RELATIONSHIPS_MULTI:
				addRelationships(componentAll, fieldType, userSubmissionField);
				break;

			case SubmissionFormFieldType.CHILD_SUBMISSIONS:
				addChildSubmission(componentAll, fieldType, userSubmissionField);
				break;
		}

		return childComponents;
	}

	private void addAttribute(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField)
	{

	}

	private void addContact(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField)
	{

	}

	private void addDependency(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField)
	{

	}

	private void addMedia(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField)
	{

	}

	private void addResource(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField)
	{

	}

	private void addTag(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField)
	{

	}

	private void addRelationships(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField)
	{

	}

	private void addChildSubmission(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField)
	{

	}

}
