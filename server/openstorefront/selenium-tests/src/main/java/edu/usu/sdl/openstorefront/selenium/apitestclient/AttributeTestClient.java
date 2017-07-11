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
package edu.usu.sdl.openstorefront.selenium.apitestclient;

import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.apiclient.rest.resource.AttributeClient;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.view.AttributeTypeSave;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class AttributeTestClient extends BaseTestClient
{

	private AttributeClient apiAttribute;
	private static List<String> attributeIDs = new ArrayList<>();
	
	public AttributeTestClient(ClientAPI client, APIClient apiClient)
	{
		super(client, apiClient);
		apiAttribute = new AttributeClient(client);
	}
	
	public AttributeType createAPIAttribute()
	{
		AttributeType type = new AttributeType();
		type.setAttributeType("AAA-KING-TEST");
		type.setDescription("A King Test Attribute-Storefront");
		type.setVisibleFlg(Boolean.TRUE);
		type.setImportantFlg(Boolean.TRUE);
		AttributeTypeSave attributeTypeSave = new AttributeTypeSave();
		attributeTypeSave.setAttributeType(type);
		
		AttributeType apiType = apiAttribute.postAttributeType(attributeTypeSave);
		attributeIDs.add(apiType.getAttributeType());
		return apiType;
	}
	
	public void deleteAPIAttribute(String type)
	{
		apiAttribute.hardDeleteAttributeType(type);
	}
	
	public List<AttributeType> getReqAttributeTypes(String componentType)
	{
		return apiAttribute.getRequiredAttributeTypes(componentType);
	}
	
	public List<AttributeCode> getListAttributeCodes(String attrType, FilterQueryParams params)
	{
		return apiAttribute.getAttributeCodes(attrType, params);
	}

	@Override
	public void cleanup()
	{
		for (String id : attributeIDs) {
			deleteAPIAttribute(id);
		}
	}	
}
