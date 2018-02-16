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

import edu.usu.sdl.openstorefront.core.entity.SubmissionFormFieldMappingType;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author dshurtleff
 */
@RunWith(Parameterized.class)
public class MapperFactoryTest
{

	@Parameters
	public static Collection<Object[]> data()
	{
		return Arrays.asList(new Object[][]{
			{SubmissionFormFieldMappingType.FIELD, EntityFieldMapper.class},
			{SubmissionFormFieldMappingType.ATTRIBUTE, AttributeFormMapper.class},
			{SubmissionFormFieldMappingType.COMPLEX, ComplexMapper.class},
			{SubmissionFormFieldMappingType.SUBMISSION, SubmissionMapper.class}
		});
	}

	private String mappingType;
	private Class expectedClass;

	public MapperFactoryTest(String mappingType, Class expectedClass)
	{
		this.mappingType = mappingType;
		this.expectedClass = expectedClass;
	}

	/**
	 * Test of getMapperForField method, of class MapperFactory.
	 */
	@Test
	public void testGetMapperForField()
	{
		System.out.println("getMapperForField: " + mappingType);

		MapperFactory instance = new MapperFactory();
		BaseMapper result = instance.getMapperForField(mappingType);

		if (!expectedClass.isInstance(result)) {
			fail("Mapping didn't create expected class");
		}

	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetMapperForFieldBad()
	{
		System.out.println("getMapperForField:  BAD");

		MapperFactory instance = new MapperFactory();
		instance.getMapperForField("BAD");
	}

}
