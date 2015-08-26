/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.manager.resource;

import java.io.IOException;
import java.util.Base64;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Handles basic Auth
 *
 * @author dshurtleff
 */
public class BasicAuthenticator
		implements ClientRequestFilter
{

	private final String user;
	private final String password;

	public BasicAuthenticator(String user, String password)
	{
		this.user = user;
		this.password = password;
	}

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException
	{
		MultivaluedMap<String, Object> headers = requestContext.getHeaders();
		String basicAuthentication = getBasicAuthenication();
		headers.add("Authorization", basicAuthentication);
	}

	private String getBasicAuthenication()
	{
		String token = this.user + ":" + this.password;
		return "Basic " + Base64.getEncoder().encodeToString(token.getBytes());
	}

}
