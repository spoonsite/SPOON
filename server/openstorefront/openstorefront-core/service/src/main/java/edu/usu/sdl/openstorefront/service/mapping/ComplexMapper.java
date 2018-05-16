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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentRelationship;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormField;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormFieldType;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionField;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionMedia;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.ComponentFormSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This maps complex object back into entities; use for contacts, media etc.
 *
 * @author dshurtleff
 */
public class ComplexMapper
		extends BaseMapper
{

	private static final Logger LOG = Logger.getLogger(ComplexMapper.class.getName());
	private ObjectMapper objectMapper;

	public ComplexMapper()
	{
		super();
		objectMapper = StringProcessor.defaultObjectMapper();
	}

	public ComplexMapper(ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
	}

	@Override
	public List<ComponentAll> mapField(ComponentAll componentAll, SubmissionFormField submissionField, UserSubmissionField userSubmissionField)
			throws MappingException
	{
		List<ComponentAll> childComponents = new ArrayList<>();

		if (userSubmissionField != null) {

			String fieldType = submissionField.getFieldType();
			try {
				switch (fieldType) {

					case SubmissionFormFieldType.ATTRIBUTE:
					case SubmissionFormFieldType.ATTRIBUTE_SINGLE:
					case SubmissionFormFieldType.ATTRIBUTE_MULTI:
					case SubmissionFormFieldType.ATTRIBUTE_RADIO:
					case SubmissionFormFieldType.ATTRIBUTE_MULTI_CHECKBOX:
						addAttribute(componentAll, userSubmissionField);
						break;

					case SubmissionFormFieldType.CONTACT:
					case SubmissionFormFieldType.CONTACT_MULTI:
						addContact(componentAll, userSubmissionField);
						break;

					case SubmissionFormFieldType.EXT_DEPENDENCY:
					case SubmissionFormFieldType.EXT_DEPENDENCY_MULTI:
						addDependency(componentAll, userSubmissionField);
						break;

					case SubmissionFormFieldType.MEDIA:
					case SubmissionFormFieldType.MEDIA_MULTI:
						addMedia(componentAll, userSubmissionField);
						break;

					case SubmissionFormFieldType.RESOURCE:
					case SubmissionFormFieldType.RESOURCE_SIMPLE:
					case SubmissionFormFieldType.RESOURCE_MULTI:
						addResource(componentAll, userSubmissionField);
						break;

					case SubmissionFormFieldType.TAG:
					case SubmissionFormFieldType.TAG_MULTI:
						addTag(componentAll, userSubmissionField);
						break;

					case SubmissionFormFieldType.RELATIONSHIPS:
					case SubmissionFormFieldType.RELATIONSHIPS_MULTI:
						addRelationships(componentAll, userSubmissionField);
						break;

					default:
						throw new UnsupportedOperationException(fieldType + " not supported");
				}
			} catch (IOException ioe) {
				String value = userSubmissionField.getRawValue();

				if (LOG.isLoggable(Level.FINER)) {
					LOG.log(Level.FINER, "Field Mapping exception", ioe);
				}
				MappingException mappingException = new MappingException("Unable to mapping form field to entry.");
				mappingException.setFieldLabel(submissionField.getLabel());
				mappingException.setFieldType(fieldType);
				mappingException.setMappingType(submissionField.getMappingType());
				mappingException.setRawValue(value);
				throw mappingException;
			}
		}

		return childComponents;
	}

	private void addAttribute(ComponentAll componentAll, UserSubmissionField userSubmissionField) throws IOException
	{
		if (userSubmissionField != null) {
			List<ComponentAttribute> componentAttributes = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentAttribute>>()
			{
			});
			componentAll.getAttributes().addAll(componentAttributes);
		}
	}

	private void addContact(ComponentAll componentAll, UserSubmissionField userSubmissionField) throws IOException
	{
		if (userSubmissionField != null) {
			List<ComponentContact> contacts = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentContact>>()
			{
			});
			componentAll.getContacts().addAll(contacts);
		}
	}

	private void addDependency(ComponentAll componentAll, UserSubmissionField userSubmissionField) throws IOException
	{
		if (userSubmissionField != null) {
			List<ComponentExternalDependency> dependencies = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentExternalDependency>>()
			{
			});
			componentAll.getExternalDependencies().addAll(dependencies);
		}
	}

	private void addMedia(ComponentAll componentAll, UserSubmissionField userSubmissionField) throws IOException
	{
		if (userSubmissionField != null) {

			List<ComponentMedia> mediaRecords = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentMedia>>()
			{
			});
			mapMedia(userSubmissionField, mediaRecords);

			componentAll.getMedia().addAll(mediaRecords);
		}

	}

	private void mapMedia(UserSubmissionField userSubmissionField, List<ComponentMedia> mediaRecords)
	{
		Map<String, UserSubmissionMedia> mediaMap = new HashMap<>();

		UserSubmissionMedia fieldMediaExample = new UserSubmissionMedia();
		fieldMediaExample.setTemplateFieldId(userSubmissionField.getTemplateFieldId());
		List<UserSubmissionMedia> fieldMedia = fieldMediaExample.findByExample();

		for (UserSubmissionMedia userSubmissionMedia : fieldMedia) {
			mediaMap.put(userSubmissionMedia.getFile().getMediaFileId(), userSubmissionMedia);
		}

		for (ComponentMedia media : mediaRecords) {
			if (media.getFile() != null) {
				UserSubmissionMedia userSubmissionMedia = mediaMap.get(media.getFile().getMediaFileId());
				if (userSubmissionMedia != null) {
					media.setFile(userSubmissionMedia.getFile().copy());
				} else {
					media.setFile(null);
				}
			}
		}
	}

	private void addResource(ComponentAll componentAll, UserSubmissionField userSubmissionField) throws IOException
	{
		if (userSubmissionField != null) {

			List<ComponentResource> resourceRecords = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentResource>>()
			{
			});
			mapResource(userSubmissionField, resourceRecords);

			componentAll.getResources().addAll(resourceRecords);

		}

	}

	private void mapResource(UserSubmissionField userSubmissionField, List<ComponentResource> resources)
	{
		Map<String, UserSubmissionMedia> mediaMap = new HashMap<>();

		UserSubmissionMedia fieldMediaExample = new UserSubmissionMedia();
		fieldMediaExample.setTemplateFieldId(userSubmissionField.getTemplateFieldId());
		List<UserSubmissionMedia> fieldMedia = fieldMediaExample.findByExample();

		for (UserSubmissionMedia userSubmissionMedia : fieldMedia) {
			mediaMap.put(userSubmissionMedia.getFile().getMediaFileId(), userSubmissionMedia);
		}

		for (ComponentResource resource : resources) {
			if (resource.getFile() != null) {
				UserSubmissionMedia userSubmissionMedia = mediaMap.get(resource.getFile().getMediaFileId());
				if (userSubmissionMedia != null) {
					resource.setFile(userSubmissionMedia.getFile().copy());
				} else {
					resource.setFile(null);
				}
			}
		}
	}

	private void addTag(ComponentAll componentAll, UserSubmissionField userSubmissionField) throws IOException
	{
		if (userSubmissionField != null) {
			List<ComponentTag> tags = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentTag>>()
			{
			});
			componentAll.getTags().addAll(tags);
		}
	}

	private void addRelationships(ComponentAll componentAll, UserSubmissionField userSubmissionField) throws IOException
	{
		if (userSubmissionField != null) {
			List<ComponentRelationship> relationships = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentRelationship>>()
			{
			});
			componentAll.getRelationships().addAll(relationships);
		}
	}

	@Override
	public UserSubmissionFieldMedia mapComponentToSubmission(SubmissionFormField submissionField, ComponentFormSet componentFormSet) throws MappingException
	{
		UserSubmissionFieldMedia userSubmissionFieldMedia = new UserSubmissionFieldMedia();

		UserSubmissionField userSubmissionField = new UserSubmissionField();
		userSubmissionFieldMedia.setUserSubmissionField(userSubmissionField);

		userSubmissionField.setTemplateFieldId(submissionField.getFieldId());

		String fieldType = submissionField.getFieldType();
		try {
			switch (fieldType) {

				case SubmissionFormFieldType.ATTRIBUTE:
				case SubmissionFormFieldType.ATTRIBUTE_SINGLE:
				case SubmissionFormFieldType.ATTRIBUTE_MULTI:
				case SubmissionFormFieldType.ATTRIBUTE_RADIO:
				case SubmissionFormFieldType.ATTRIBUTE_MULTI_CHECKBOX:
					mapAttributes(userSubmissionField, componentFormSet);
					break;

				case SubmissionFormFieldType.CONTACT:
				case SubmissionFormFieldType.CONTACT_MULTI:
					mapContacts(userSubmissionField, componentFormSet);
					break;

				case SubmissionFormFieldType.EXT_DEPENDENCY:
				case SubmissionFormFieldType.EXT_DEPENDENCY_MULTI:
					mapDependencies(userSubmissionField, componentFormSet);
					break;

				case SubmissionFormFieldType.MEDIA:
				case SubmissionFormFieldType.MEDIA_MULTI:
					mapMediaForForm(userSubmissionField, componentFormSet);
					break;

				case SubmissionFormFieldType.RESOURCE:
				case SubmissionFormFieldType.RESOURCE_SIMPLE:
				case SubmissionFormFieldType.RESOURCE_MULTI:
					mapResourcesForForm(userSubmissionField, componentFormSet);
					break;

				case SubmissionFormFieldType.TAG:
				case SubmissionFormFieldType.TAG_MULTI:
					mapTags(userSubmissionField, componentFormSet);
					break;

				case SubmissionFormFieldType.RELATIONSHIPS:
				case SubmissionFormFieldType.RELATIONSHIPS_MULTI:
					mapRelationships(userSubmissionField, componentFormSet);
					break;

				default:
					throw new UnsupportedOperationException(fieldType + " not supported");
			}
		} catch (IOException ioe) {
			if (LOG.isLoggable(Level.FINER)) {
				LOG.log(Level.FINER, "Field Mapping exception", ioe);
			}
			MappingException mappingException = new MappingException("Unable to mapping entry to form field");
			mappingException.setFieldLabel(submissionField.getLabel());
			mappingException.setFieldType(fieldType);
			mappingException.setMappingType(submissionField.getMappingType());
			throw mappingException;
		}

		return userSubmissionFieldMedia;
	}

	private void mapAttributes(UserSubmissionField userSubmissionField, ComponentFormSet componentFormSet) throws JsonProcessingException
	{
		String value = objectMapper.writeValueAsString(componentFormSet.getPrimary().getAttributes());
		userSubmissionField.setRawValue(value);
	}

	private void mapContacts(UserSubmissionField userSubmissionField, ComponentFormSet componentFormSet) throws JsonProcessingException
	{
		String value = objectMapper.writeValueAsString(componentFormSet.getPrimary().getContacts());
		userSubmissionField.setRawValue(value);
	}

	private void mapDependencies(UserSubmissionField userSubmissionField, ComponentFormSet componentFormSet) throws JsonProcessingException
	{
		String value = objectMapper.writeValueAsString(componentFormSet.getPrimary().getExternalDependencies());
		userSubmissionField.setRawValue(value);
	}

	private void mapMediaForForm(UserSubmissionField userSubmissionField, ComponentFormSet componentFormSet) throws JsonProcessingException
	{
		String value = objectMapper.writeValueAsString(componentFormSet.getPrimary().getMedia());
		userSubmissionField.setRawValue(value);

		for (ComponentMedia media : componentFormSet.getPrimary().getMedia()) {
			if (media.getFile() != null) {
				UserSubmissionMedia userSubmissionMedia = new UserSubmissionMedia();
				userSubmissionMedia.setTemplateFieldId(userSubmissionField.getTemplateFieldId());
				userSubmissionMedia.setFile(media.getFile().copy());

			}
		}

	}

	private void mapResourcesForForm(UserSubmissionField userSubmissionField, ComponentFormSet componentFormSet) throws JsonProcessingException
	{
		String value = objectMapper.writeValueAsString(componentFormSet.getPrimary().getResources());
		userSubmissionField.setRawValue(value);

		for (ComponentResource resource : componentFormSet.getPrimary().getResources()) {
			if (resource.getFile() != null) {
				UserSubmissionMedia userSubmissionMedia = new UserSubmissionMedia();
				userSubmissionMedia.setTemplateFieldId(userSubmissionField.getTemplateFieldId());
				userSubmissionMedia.setFile(resource.getFile().copy());

			}
		}
	}

	private void mapTags(UserSubmissionField userSubmissionField, ComponentFormSet componentFormSet) throws JsonProcessingException
	{
		String value = objectMapper.writeValueAsString(componentFormSet.getPrimary().getTags());
		userSubmissionField.setRawValue(value);
	}

	private void mapRelationships(UserSubmissionField userSubmissionField, ComponentFormSet componentFormSet) throws JsonProcessingException
	{
		String value = objectMapper.writeValueAsString(componentFormSet.getPrimary().getRelationships());
		userSubmissionField.setRawValue(value);
	}

}
