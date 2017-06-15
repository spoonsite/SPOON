/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.apiclient.usecase;

import edu.usu.sdl.apiclient.rest.resource.AttributeResourceImpl;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.view.AttributeTypeSave;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ccummings
 */
public class AttributeResourceUseCase
{

	private static AttributeResourceImpl apiAttribute = new AttributeResourceImpl();
	
	@BeforeClass
	public static void setupTest()
	{
		String server = "http://localhost:8080/openstorefront/";
		String username = "admin";
		String password = "Secret1@";
		apiAttribute.connect(username, password, server);
	}

	@Test
	public void postAttribute()
	{
		AttributeType type = new AttributeType();
		type.setAttributeType("AAA-KING-TEST");
		type.setDescription("King Test Attribute Storefront");
		type.setVisibleFlg(Boolean.TRUE);
		type.setImportantFlg(Boolean.TRUE);
		AttributeTypeSave attributeTypeSave = new AttributeTypeSave();
		attributeTypeSave.setAttributeType(type);

		AttributeType apiType = apiAttribute.postAttributeType(attributeTypeSave);
	}
}
