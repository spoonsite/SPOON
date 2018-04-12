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
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormField;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionField;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is is expected to create child components
 *
 * @author dshurtleff
 */
public class SubmissionMapper
		extends BaseMapper
{

	private static final Logger LOG = Logger.getLogger(ComplexMapper.class.getName());
	private ObjectMapper objectMapper;

	public SubmissionMapper()
	{
		super();
		objectMapper = StringProcessor.defaultObjectMapper();
	}

	public SubmissionMapper(ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
	}

	@Override
	public List<ComponentAll> mapField(ComponentAll componentAll, SubmissionFormField submissionField, UserSubmissionField userSubmissionField)
			throws MappingException
	{
		List<ComponentAll> childComponents = new ArrayList<>();

		try {
			//Note: entry type is assume to be set in the data
			//Assume media is all correctly handled and transfered.
			//UI can leverage the mapping cabilities to help create the records.
			//From this angle there is a lot ambiguities.
			childComponents = objectMapper.readValue(userSubmissionField.getRawValue(), new TypeReference<List<ComponentAll>>()
			{
			});

		} catch (IOException ex) {
			String value = null;
			if (userSubmissionField != null) {
				value = userSubmissionField.getRawValue();
			}
			if (LOG.isLoggable(Level.FINER)) {
				LOG.log(Level.FINER, "Field Mapping exception", ex);
			}

			MappingException mappingException = new MappingException("Unable to mapping form field to entry.");
			mappingException.setFieldLabel(submissionField.getLabel());
			mappingException.setFieldName(submissionField.getFieldName());
			mappingException.setMappingType(submissionField.getMappingType());
			mappingException.setRawValue(value);
			throw mappingException;
		}

		return childComponents;
	}

}
