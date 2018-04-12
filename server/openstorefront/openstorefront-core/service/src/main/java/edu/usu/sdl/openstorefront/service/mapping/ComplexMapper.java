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

		String fieldType = submissionField.getFieldType();
		try {
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

				default:
					throw new UnsupportedOperationException(fieldType + " not supported");
			}
		} catch (IOException ioe) {
			String value = null;
			if (userSubmissionField != null) {
				value = userSubmissionField.getRawValue();
			}
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

		return childComponents;
	}

	private void addAttribute(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField) throws IOException
	{
		if (userSubmissionField != null) {
			//handling sub types
			if (SubmissionFormFieldType.ATTRIBUTE.equals(fieldType)
					|| SubmissionFormFieldType.ATTRIBUTE_RADIO.equals(fieldType)) {
				ComponentAttribute componentAttribute = objectMapper.readValue(userSubmissionField.getRawValue(), ComponentAttribute.class);
				componentAll.getAttributes().add(componentAttribute);
			} else if (SubmissionFormFieldType.ATTRIBUTE.equals(fieldType)
					|| SubmissionFormFieldType.ATTRIBUTE_RADIO.equals(fieldType)) {
				List<ComponentAttribute> componentAttributes = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentAttribute>>()
				{
				});
				componentAll.getAttributes().addAll(componentAttributes);
			}
		}
	}

	private void addContact(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField) throws IOException
	{
		if (userSubmissionField != null) {

			if (SubmissionFormFieldType.CONTACT.equals(fieldType)) {
				ComponentContact componentContact = objectMapper.readValue(userSubmissionField.getRawValue(), ComponentContact.class);
				componentAll.getContacts().add(componentContact);
			} else if (SubmissionFormFieldType.CONTACT_MULTI.equals(fieldType)) {
				List<ComponentContact> contacts = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentContact>>()
				{
				});
				componentAll.getContacts().addAll(contacts);
			}
		}
	}

	private void addDependency(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField) throws IOException
	{
		if (userSubmissionField != null) {

			if (SubmissionFormFieldType.EXT_DEPENDENCY.equals(fieldType)) {
				ComponentExternalDependency componentExternalDependency = objectMapper.readValue(userSubmissionField.getRawValue(), ComponentExternalDependency.class);
				componentAll.getExternalDependencies().add(componentExternalDependency);
			} else if (SubmissionFormFieldType.EXT_DEPENDENCY_MULTI.equals(fieldType)) {
				List<ComponentExternalDependency> dependencies = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentExternalDependency>>()
				{
				});
				componentAll.getExternalDependencies().addAll(dependencies);
			}
		}
	}

	private void addMedia(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField) throws IOException
	{
		if (userSubmissionField != null) {

			if (SubmissionFormFieldType.MEDIA.equals(fieldType)) {
				ComponentMedia media = objectMapper.readValue(userSubmissionField.getRawValue(), ComponentMedia.class);
				if (userSubmissionField.getMedia() != null
						&& !userSubmissionField.getMedia().isEmpty()) {
					media.setFile(userSubmissionField.getMedia().get(0).getFile());
				}
				componentAll.getMedia().add(media);
			} else if (SubmissionFormFieldType.MEDIA_MULTI.equals(fieldType)) {
				List<ComponentMedia> mediaRecords = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentMedia>>()
				{
				});
				mapMedia(userSubmissionField, mediaRecords);

				componentAll.getMedia().addAll(mediaRecords);
			}
		}

	}

	private void mapMedia(UserSubmissionField userSubmissionField, List<ComponentMedia> mediaRecords)
	{
		Map<String, UserSubmissionMedia> mediaMap = new HashMap<>();
		if (userSubmissionField.getMedia() != null) {
			for (UserSubmissionMedia userSubmissionMedia : userSubmissionField.getMedia()) {
				mediaMap.put(userSubmissionMedia.getFile().getMediaFileId(), userSubmissionMedia);
			}
		}

		for (ComponentMedia media : mediaRecords) {
			if (media.getFile() != null) {
				UserSubmissionMedia userSubmissionMedia = mediaMap.get(media.getFile().getMediaFileId());
				if (userSubmissionMedia != null) {
					media.setFile(userSubmissionMedia.getFile());
				} else {
					media.setFile(null);
				}
			}
		}
	}

	private void addResource(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField) throws IOException
	{
		if (userSubmissionField != null) {

			if (SubmissionFormFieldType.RESOURCE.equals(fieldType)) {
				ComponentResource resource = objectMapper.readValue(userSubmissionField.getRawValue(), ComponentResource.class);
				if (userSubmissionField.getMedia() != null
						&& !userSubmissionField.getMedia().isEmpty()) {
					resource.setFile(userSubmissionField.getMedia().get(0).getFile());
				}
				componentAll.getResources().add(resource);
			} else if (SubmissionFormFieldType.RESOURCE_MULTI.equals(fieldType)) {
				List<ComponentResource> resourceRecords = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentResource>>()
				{
				});
				mapResource(userSubmissionField, resourceRecords);

				componentAll.getResources().addAll(resourceRecords);
			}
		}

	}

	private void mapResource(UserSubmissionField userSubmissionField, List<ComponentResource> resources)
	{
		Map<String, UserSubmissionMedia> mediaMap = new HashMap<>();
		if (userSubmissionField.getMedia() != null) {
			for (UserSubmissionMedia userSubmissionMedia : userSubmissionField.getMedia()) {
				mediaMap.put(userSubmissionMedia.getFile().getMediaFileId(), userSubmissionMedia);
			}
		}

		for (ComponentResource resource : resources) {
			if (resource.getFile() != null) {
				UserSubmissionMedia userSubmissionMedia = mediaMap.get(resource.getFile().getMediaFileId());
				if (userSubmissionMedia != null) {
					resource.setFile(userSubmissionMedia.getFile());
				} else {
					resource.setFile(null);
				}
			}
		}
	}

	private void addTag(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField) throws IOException
	{
		if (userSubmissionField != null) {

			if (SubmissionFormFieldType.TAG.equals(fieldType)) {
				ComponentTag tag = objectMapper.readValue(userSubmissionField.getRawValue(), ComponentTag.class);
				componentAll.getTags().add(tag);
			} else if (SubmissionFormFieldType.TAG_MULTI.equals(fieldType)) {
				List<ComponentTag> tags = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentTag>>()
				{
				});
				componentAll.getTags().addAll(tags);
			}
		}
	}

	private void addRelationships(ComponentAll componentAll, String fieldType, UserSubmissionField userSubmissionField) throws IOException
	{
		if (userSubmissionField != null) {

			if (SubmissionFormFieldType.RELATIONSHIPS.equals(fieldType)) {
				ComponentRelationship relationship = objectMapper.readValue(userSubmissionField.getRawValue(), ComponentRelationship.class);
				componentAll.getRelationships().add(relationship);
			} else if (SubmissionFormFieldType.RELATIONSHIPS_MULTI.equals(fieldType)) {
				List<ComponentRelationship> relationships = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentRelationship>>()
				{
				});
				componentAll.getRelationships().addAll(relationships);
			}
		}
	}

}
