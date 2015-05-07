/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.usecase;

import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.util.StringProcessor;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class PersistenceUseCase
{

	@Test
	public void testDefaultFieldValue()
	{
		AttributeType attributeType = null;
		for (int i = 0; i < 100; i++) {
			attributeType = new AttributeType();
			long startTime = System.currentTimeMillis();
			ReflectionUtil.setDefaultsOnFields(attributeType);
			System.out.println("Time: " + (System.currentTimeMillis() - startTime));
		}

		System.out.println(StringProcessor.printObject(attributeType));

	}

}
