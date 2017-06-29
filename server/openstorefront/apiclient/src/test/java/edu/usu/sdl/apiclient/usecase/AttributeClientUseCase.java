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

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.apiclient.rest.resource.AttributeClient;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.view.AttributeTypeSave;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ccummings
 */
public class AttributeClientUseCase
{

	protected static ClientAPI client;
	protected static AttributeClient apiAttribute;
		
	@BeforeClass
	public static void setupTest()
	{
		client = new ClientAPI(new ObjectMapper());
		client.connect("admin", "Secret1@", "http://localhost:8080/openstorefront/");
		apiAttribute = new AttributeClient(client);
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
