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
package edu.usu.sdl.openstorefront.selenium.provider;

import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.apiclient.rest.resource.AttributeClient;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.AttributeXRefMap;
import edu.usu.sdl.openstorefront.core.entity.AttributeXRefType;
import edu.usu.sdl.openstorefront.core.entity.IntegrationType;
import edu.usu.sdl.openstorefront.core.view.AttributeTypeSave;
import edu.usu.sdl.openstorefront.core.view.AttributeXRefView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class AttributeProvider
{

	private AttributeClient client;
	private List<String> attributeTypes;
	private List<String> attributeXRefTypes;

	private static final String TEST_ATTRIBUTE_TYPE = "MAPPINGTESTATTR";
	private static final String TEST_ATTRIBUTE_CODE = "MAPPINGATTRTEST";
	private static final String TEST_ATTRIBUTE_MAPPING_CODE = "MAPPINGATTR";

	public AttributeProvider(ClientAPI apiClient)
	{
		client = new AttributeClient(apiClient);
		attributeTypes = new ArrayList<>();
		attributeXRefTypes = new ArrayList();
	}

	public AttributeType createAttribute(String attributeType, String attributeDefaultCode, String codeLabel)
	{
		AttributeType type = new AttributeType();
		type.setAttributeType(attributeType);
		type.setDescription("The Star Betelgeuse");
		type.setVisibleFlg(Boolean.TRUE);
		type.setImportantFlg(Boolean.TRUE);
		type.setRequiredFlg(Boolean.TRUE);
		type.setDefaultAttributeCode(attributeDefaultCode);
		AttributeTypeSave attributeTypeSave = new AttributeTypeSave();
		attributeTypeSave.setAttributeType(type);

		AttributeType apiAttrType = client.postAttributeType(attributeTypeSave);
		addAttributeCode(apiAttrType.getAttributeType(), attributeDefaultCode, codeLabel);

		attributeTypes.add(apiAttrType.getAttributeType());

		return apiAttrType;
	}

	public List<AttributeType> getReqAttributeTypes(String componentType)
	{
		return client.getRequiredAttributeTypes(componentType);
	}

	public List<AttributeCode> getListAttributeCodes(String attrType, FilterQueryParams params)
	{
		return client.getAttributeCodes(attrType, params);
	}

	public AttributeCode addAttributeCode(String attributeType, String codeLabel, String code)
	{
		AttributeType type = client.getAttributeTypeById(attributeType, null, null);

		AttributeCodePk codePk = new AttributeCodePk();
		codePk.setAttributeCode(code);
		codePk.setAttributeType(type.getAttributeType());

		AttributeCode attrCode = new AttributeCode();
		attrCode.setLabel(codeLabel);
		attrCode.setAttributeCodePk(codePk);

		return client.postAttributeCode(attributeType, attrCode);
	}

	public void cleanup()
	{
		for (String type : attributeTypes) {

			client.deleteAttributeType(type);
		}

		for (String type : attributeXRefTypes) {
			client.deleteMappingType(type);
		}
	}

}
