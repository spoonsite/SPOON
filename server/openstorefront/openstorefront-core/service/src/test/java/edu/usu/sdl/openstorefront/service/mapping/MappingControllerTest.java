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
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormFieldMappingType;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormSection;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormTemplate;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionField;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class MappingControllerTest
{

	public MappingControllerTest()
	{
	}

	/**
	 * Test of verifyTemplate method, of class MappingController.
	 */
	@Test
	public void testVerifyTemplateWithSteps()
	{
		SubmissionFormTemplate template = new SubmissionFormTemplate();
		template.setSections(new ArrayList<>());
		template.getSections().add(new SubmissionFormSection());

		MapperFactory mockMapperFactory = getMockMapperFactory();
		MappingController instance = new MappingController(mockMapperFactory);
		ValidationResult result = instance.verifyTemplate(template);
		assertFalse(result.valid());
	}

	private MapperFactory getMockMapperFactory()
	{
		MapperFactory mockMapperFactory = new MapperFactory()
		{
			@Override
			public BaseMapper getMapperForField(String mappingType)
			{
				return new BaseMapper()
				{
					@Override
					public ComponentAll mapField(ComponentAll componentAll, SubmissionFormField submissionField, UserSubmissionField userSubmissionField)
					{
						throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
					}
				};
			}

		};
		return mockMapperFactory;
	}

	@Test(expected = NullPointerException.class)
	public void testVerifyTemplateNullTemplate()
	{
		MappingController instance = new MappingController();
		instance.verifyTemplate(null);
	}

	@Test
	public void testVerifyTemplateWithoutSteps()
	{
		SubmissionFormTemplate template = new SubmissionFormTemplate();

		MapperFactory mockMapperFactory = getMockMapperFactory();
		MappingController instance = new MappingController(mockMapperFactory);
		ValidationResult result = instance.verifyTemplate(template);
		assertFalse(result.valid());
	}

	@Test
	public void testVerifyTemplateWithFields()
	{
		SubmissionFormTemplate template = new SubmissionFormTemplate();
		template.setSections(new ArrayList<>());
		SubmissionFormSection formStep = new SubmissionFormSection();
		formStep.setFields(new ArrayList<>());

		SubmissionFormField submissionFormField = new SubmissionFormField();
		submissionFormField.setMappingType(SubmissionFormFieldMappingType.FIELD);
		formStep.getFields().add(submissionFormField);

		template.getSections().add(new SubmissionFormSection());

		MapperFactory mockMapperFactory = getMockMapperFactory();
		MappingController instance = new MappingController(mockMapperFactory);
		ValidationResult result = instance.verifyTemplate(template);
		assertFalse(result.valid());
	}

}
