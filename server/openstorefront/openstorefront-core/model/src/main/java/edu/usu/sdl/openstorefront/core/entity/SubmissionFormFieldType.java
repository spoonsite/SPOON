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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
@SystemTable
@APIDescription("Defines the available field type; the UI will determine how it is rendered")
public class SubmissionFormFieldType
		extends LookupEntity<SubmissionFormFieldType>
{

	private static final long serialVersionUID = 1L;

	public static final String TEXT = "TEXT";
	public static final String TEXT_AREA = "TEXTAREA";
	public static final String RICH_TEXT = "RICHTEXT";
	public static final String NUMBER = "NUMBER";
	public static final String DATE = "DATE";
	public static final String ORGANIZATION = "ORGANIZATION";
	public static final String STATIC_CONTENT = "CONTENT";
	public static final String ATTRIBUTE = "ATTRIBUTE";
	public static final String ATTRIBUTE_MULTI = "ATTRIBUTE_MULTI";
	public static final String ATTRIBUTE_SINGLE = "ATTRIBUTE_SINGLE";
	public static final String ATTRIBUTE_RADIO = "ATTRIBUTE_RADIO";
	public static final String ATTRIBUTE_REQUIRED = "ATTRIBUTE_REQUIRED";
	public static final String ATTRIBUTE_SUGGESTED = "ATTRIBUTE_SUGGESTED";
	public static final String ATTRIBUTE_MULTI_CHECKBOX = "ATTRIBUTE_MCHECKBOX";
	public static final String CONTACT = "CONTACT";
	public static final String CONTACT_MULTI = "CONTACT_MULTI";
	public static final String EXT_DEPENDENCY = "EXT_DEPEND";
	public static final String EXT_DEPENDENCY_MULTI = "EXT_DEPEND_MULTI";
	public static final String MEDIA = "MEDIA";
	public static final String MEDIA_MULTI = "MEDIA_MULTI";
	public static final String RESOURCE = "RESOURCE";
	public static final String RESOURCE_SIMPLE = "RESOURCE_SIMPLE";
	public static final String RESOURCE_MULTI = "RESOURCE_MULTI";
	public static final String TAG = "TAG";
	public static final String TAG_MULTI = "TAG_MULTI";
	public static final String RELATIONSHIPS = "RELATIONSHIPS";
	public static final String RELATIONSHIPS_MULTI = "RELATIONSHIPS_MULTI";
	public static final String CHILD_SUBMISSIONS = "SUBMISSIONS";

	@SuppressWarnings({"squid:S1186"})

	public SubmissionFormFieldType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();

		codeMap.put(TEXT, newLookup(SubmissionFormFieldType.class, TEXT, "Text"));
		codeMap.put(TEXT_AREA, newLookup(SubmissionFormFieldType.class, TEXT_AREA, "Text Area"));
		codeMap.put(RICH_TEXT, newLookup(SubmissionFormFieldType.class, RICH_TEXT, "RichText"));
		codeMap.put(NUMBER, newLookup(SubmissionFormFieldType.class, NUMBER, "Number"));
		codeMap.put(DATE, newLookup(SubmissionFormFieldType.class, DATE, "Date"));
		codeMap.put(STATIC_CONTENT, newLookup(SubmissionFormFieldType.class, STATIC_CONTENT, "Static Content"));

		codeMap.put(ORGANIZATION, newLookup(SubmissionFormFieldType.class, ORGANIZATION, "Organization"));

		codeMap.put(ATTRIBUTE, newLookup(SubmissionFormFieldType.class, ATTRIBUTE, "Attribute"));
		codeMap.put(ATTRIBUTE_MULTI, newLookup(SubmissionFormFieldType.class, ATTRIBUTE_MULTI, "Attribute Multiple"));
		codeMap.put(ATTRIBUTE_RADIO, newLookup(SubmissionFormFieldType.class, ATTRIBUTE_RADIO, "Attribute Radio"));
		codeMap.put(ATTRIBUTE_REQUIRED, newLookup(SubmissionFormFieldType.class, ATTRIBUTE_REQUIRED, "Attribute Required"));
		codeMap.put(ATTRIBUTE_SUGGESTED, newLookup(SubmissionFormFieldType.class, ATTRIBUTE_SUGGESTED, "Attribute Suggested"));
		codeMap.put(ATTRIBUTE_SINGLE, newLookup(SubmissionFormFieldType.class, ATTRIBUTE_SINGLE, "Attribute Single"));
		codeMap.put(ATTRIBUTE_MULTI_CHECKBOX, newLookup(SubmissionFormFieldType.class, ATTRIBUTE_MULTI_CHECKBOX, "Attribute Multiple Checkbox"));

		codeMap.put(CONTACT, newLookup(SubmissionFormFieldType.class, CONTACT, "Contact"));
		codeMap.put(CONTACT_MULTI, newLookup(SubmissionFormFieldType.class, CONTACT_MULTI, "Contact Multiple"));
		codeMap.put(EXT_DEPENDENCY, newLookup(SubmissionFormFieldType.class, EXT_DEPENDENCY, "Dependency"));
		codeMap.put(EXT_DEPENDENCY_MULTI, newLookup(SubmissionFormFieldType.class, EXT_DEPENDENCY_MULTI, "Dependency Multiple"));
		codeMap.put(MEDIA, newLookup(SubmissionFormFieldType.class, MEDIA, "Media"));
		codeMap.put(MEDIA_MULTI, newLookup(SubmissionFormFieldType.class, MEDIA_MULTI, "Media Multiple"));
		codeMap.put(RESOURCE, newLookup(SubmissionFormFieldType.class, RESOURCE, "Resource"));
		codeMap.put(RESOURCE_SIMPLE, newLookup(SubmissionFormFieldType.class, RESOURCE_SIMPLE, "Resource Simple"));
		codeMap.put(RESOURCE_MULTI, newLookup(SubmissionFormFieldType.class, RESOURCE_MULTI, "Resource Multiple"));
		codeMap.put(TAG, newLookup(SubmissionFormFieldType.class, TAG, "Tag"));
		codeMap.put(TAG_MULTI, newLookup(SubmissionFormFieldType.class, TAG_MULTI, "Tag Multiple"));
		codeMap.put(RELATIONSHIPS, newLookup(SubmissionFormFieldType.class, RELATIONSHIPS, "Relationships"));
		codeMap.put(RELATIONSHIPS_MULTI, newLookup(SubmissionFormFieldType.class, RELATIONSHIPS_MULTI, "Relationships Multiple"));

		codeMap.put(CHILD_SUBMISSIONS, newLookup(SubmissionFormFieldType.class, CHILD_SUBMISSIONS, "Child Submissions"));

		return codeMap;
	}

}
