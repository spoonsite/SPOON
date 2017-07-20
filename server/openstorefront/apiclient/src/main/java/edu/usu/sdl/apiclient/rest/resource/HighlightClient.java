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
import edu.usu.sdl.openstorefront.core.entity.Highlight;
import edu.usu.sdl.openstorefront.core.view.HighlightFilter;
import javax.ws.rs.core.Response;

/**
 *
 * @author ccummings
 */
public class HighlightClient
		extends AbstractService
{

	String basePath = "api/v1/resource/highlights";

	public HighlightClient(ClientAPI client)
	{
		super(client);
	}

	public HighlightClient()
	{
		this(new ClientAPI(new ObjectMapper()));
	}

	public void activateHighlight(String id)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void deactivateHighlight(String id)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void deleteHighlight(String id)
	{
		client.httpDelete(basePath + "/" + id + "/delete", null);
	}

	public Response getHighlightById(String id)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getHighlights(HighlightFilter filterQueryParams, boolean all)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Highlight postHighlight(Highlight highlight)
	{
		APIResponse response = client.httpPost(basePath, highlight, null);
		return response.getResponse(Highlight.class);
	}

	public Response updateEntityValue(String id, Highlight highlight)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
