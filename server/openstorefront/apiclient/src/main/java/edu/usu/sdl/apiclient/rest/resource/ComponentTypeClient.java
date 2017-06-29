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

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.APIResponse;
import edu.usu.sdl.apiclient.AbstractService;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ccummings
 */
public class ComponentTypeClient extends AbstractService
{
	String basePath = "api/v1/resource/componenttypes";

	public ComponentTypeClient(ClientAPI client)
	{
		super(client);
	}
	
	public ComponentTypeClient()
	{
		this(new ClientAPI(new ObjectMapper()));
	}

	public Response activateComponentType(String type)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public ComponentType createNewComponentType(ComponentType componentType)
	{
		APIResponse response = client.httpPost(basePath, componentType, null);
		return response.getResponse(ComponentType.class);
	}

	public void deleteComponentType(String type, String newType)
	{
		System.out.println("#($*($ What is new type: " + newType + " The type is " + type);
		client.httpDelete(basePath + "/" + type + "?newtype=" + newType, null);
	}

	public Response getComponentType(String status, boolean all)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getComponentTypeById(String type)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getComponentTypeLookup(String status, boolean all)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getSubmissionComponentTyped()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response updateComponentType(String type, ComponentType componentType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
