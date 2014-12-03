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
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.storage.model.UserMessage;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * User messages (Queued) used for email
 *
 * @author dshurtleff
 */
@Path("v1/resource/usermessages")
@APIDescription("User message are queued message for the user.")
public class UserMessageResource
		extends BaseResource
{

	@GET
	@APIDescription("Get a list of user messages")
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserMessage.class)
	public List<UserMessage> userMessages(@BeanParam FilterQueryParams filterQueryParams)
	{
		List<UserMessage> userMessages = service.getUserService().findUserMessages(filterQueryParams);
		return userMessages;
	}

	@GET
	@APIDescription("Get an user message")
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserMessage.class)
	@Path("/{id}")
	public Response findUserMessage(
			@PathParam("id") String userMessageId)
	{
		UserMessage userMessageExample = new UserMessage();
		userMessageExample.setUserMessageId(userMessageId);
		UserMessage userMessage = service.getPersistenceService().queryOneByExample(UserMessage.class, userMessageExample);
		return sendSingleEntityResponse(userMessage);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes a user message")
	@Path("/{id}")
	public void deleteUseMessage(
			@PathParam("id")
			@RequiredParam String id)
	{
		service.getUserService().removeUserMessage(id);
	}

	@POST
	@APIDescription("Processes all active user messages now")
	@RequireAdmin
	@Path("/processnow")
	public Response processUserMessages()
	{
		service.getUserService().processAllUserMessages(true);
		return Response.ok().build();
	}

	@POST
	@APIDescription("Cleanup old user messages according to archive rules")
	@RequireAdmin
	@Path("/cleanold")
	public Response cleanOld()
	{
		service.getUserService().cleanupOldUserMessages();
		return Response.ok().build();
	}

}
