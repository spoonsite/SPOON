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
package edu.usu.sdl.apiclient.rest.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.APIResponse;
import edu.usu.sdl.apiclient.AbstractService;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.view.AttributeCodeSave;
import edu.usu.sdl.openstorefront.core.view.AttributeTypeSave;
import edu.usu.sdl.openstorefront.core.view.AttributeTypeView;
import edu.usu.sdl.openstorefront.core.view.AttributeXRefView;
import edu.usu.sdl.openstorefront.core.view.AttributeXrefMapView;
import edu.usu.sdl.openstorefront.core.view.ComponentView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;

/**
 *
 * @author ccummings
 */
public class AttributeClient
		extends AbstractService
{

	String basePath = "api/v1/resource/attributes";

	public AttributeClient(ClientAPI client)
	{
		super(client);
	}

	public AttributeClient()
	{
		this(new ClientAPI(new ObjectMapper()));
	}

	public void activateCode(String type, String code)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void activateType(String type)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void deleteAttributeCode(String type, String code)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void deleteAttributeCodeAttachment(String type, String code)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void deleteAttributeType(String type)
	{
		client.httpDelete(basePath + "/attributetypes/" + type, null);
	}

	public void deleteMappingType(String type)
	{
		client.httpDelete(basePath + "/attributexreftypes/" + type, null);
	}

	public Response downloadAttributeCodeAttachment(String type, String code)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response exportAttributes(List<String> types)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getArchitecture(String type)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getAttributeCode(String type, String code)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getAttributeCodeById(String type, String code)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getAttributeCodeViewById(String type, String code)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getAttributeCodeViews(String type, FilterQueryParams filterQueryParams)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<AttributeCode> getAttributeCodes(String type, FilterQueryParams filterQueryParams)
	{
		Map<String, String> parameters = client.translateFilterQueryParams(filterQueryParams);
		APIResponse response = client.httpGet(basePath + "/attributetypes/" + type + "/attributecodes", parameters);
		List<AttributeCode> attrCodeList = response.getList(new TypeReference<List<AttributeCode>>()
		{
		});
		return attrCodeList;
	}

	public Response getAttributeRelationships(String filterAttributeType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public AttributeType getAttributeTypeById(String type, Boolean view, Boolean all)
	{
		Map<String, String> params = new HashMap<>();

		if (view != null) {
			params.put("view", view.toString());
		}
		if (all != null) {
			params.put("all", all.toString());
		}

		APIResponse response = client.httpGet(basePath + "/attributetypes/" + type, params);
		return response.getResponse(AttributeType.class);
	}

	public Response getAttributeTypes(FilterQueryParams filterQueryParams)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<AttributeTypeView> getAttributeView(boolean all)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<ComponentView> getComponentsWithAttributeCode(String type, String code)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<AttributeXrefMapView> getDistinctProjectMappings()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getMappingType(String attributeType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<AttributeXrefMapView> getMappingTypes()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<AttributeType> getRequiredAttributeTypes(String componentType)
	{
		APIResponse response = client.httpGet(basePath + "/attributetypes/required?componentType=" + componentType, null);
		List<AttributeType> attrTypeList = response.getList(new TypeReference<List<AttributeType>>()
		{
		});
		return attrTypeList;
	}

	public void hardDeleteAttributeCode(String type, String code)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void hardDeleteAttributeType(String type)
	{
		client.httpDelete(basePath + "/attributetypes/" + type + "/force", null);
	}

	public AttributeCode postAttributeCode(String type, AttributeCode attributeCode)
	{
		APIResponse response = client.httpPost(basePath + "/attributetypes/" + type + "/attributecodes", attributeCode, null);
		return response.getResponse(AttributeCode.class);
	}

	public AttributeType postAttributeType(AttributeTypeSave attributeTypeSave)
	{
		APIResponse response = client.httpPost(basePath + "/attributetypes", attributeTypeSave, null);
		return response.getResponse(AttributeType.class);
	}

	public Response postUserAttributeCode(AttributeCodeSave attributeCodeSave)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void saveMapping(AttributeXRefView attributeXref)
	{
		client.httpPost(basePath + "/attributexreftypes/detail", attributeXref, null);
	}

	public Response updateAttributeCode(String type, AttributeTypeView attributeType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response updateAttributeCode(String type, String code, AttributeCode attributeCode)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response updateAttributeType(String type, AttributeTypeSave attributeTypeSave)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
