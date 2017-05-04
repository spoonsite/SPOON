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

import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.model.TaskRequest;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.UserMessage;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.UserMessageWrapper;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.lang.reflect.Field;
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
import net.sourceforge.stripes.util.bean.BeanUtil;

/**
 * User messages (Queued) used for email
 *
 * @author dshurtleff
 */
@Path("v1/resource/usermessages")
@APIDescription("User messages are queued messages for the user.")
public class UserMessageResource
		extends BaseResource
{

	@GET
	@APIDescription("Get a list of user messages")
	@RequireSecurity(SecurityPermission.ADMIN_MESSAGE_MANAGEMENT)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserMessage.class)
	public Response userMessages(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		UserMessage userMessageExample = new UserMessage();
		userMessageExample.setActiveStatus(filterQueryParams.getStatus());

		UserMessage userMessageStartExample = new UserMessage();
		userMessageStartExample.setCreateDts(filterQueryParams.getStart());

		UserMessage userMessageEndExample = new UserMessage();
		userMessageEndExample.setCreateDts(filterQueryParams.getEnd());

		QueryByExample queryByExample = new QueryByExample(userMessageExample);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(userMessageStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(userMessageEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());
		queryByExample.setSortDirection(filterQueryParams.getSortOrder());

		UserMessage userMessageSortExample = new UserMessage();
		Field sortField = ReflectionUtil.getField(userMessageSortExample, filterQueryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), userMessageSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(userMessageSortExample);
		}

		List<UserMessage> userMessages = service.getPersistenceService().queryByExample(queryByExample);

		UserMessageWrapper userMessageWrapper = new UserMessageWrapper();
		userMessageWrapper.setData(userMessages);
		userMessageWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));

		return sendSingleEntityResponse(userMessageWrapper);
	}

	@GET
	@APIDescription("Gets a user message")
	@RequireSecurity(SecurityPermission.ADMIN_MESSAGE_MANAGEMENT)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserMessage.class)
	@Path("/{id}")
	public Response findUserMessage(
			@PathParam("id") String userMessageId)
	{
		UserMessage userMessageExample = new UserMessage();
		userMessageExample.setUserMessageId(userMessageId);
		UserMessage userMessage = service.getPersistenceService().queryOneByExample(userMessageExample);
		return sendSingleEntityResponse(userMessage);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_MESSAGE_MANAGEMENT)
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
	@RequireSecurity(SecurityPermission.ADMIN_MESSAGE_MANAGEMENT)
	@Path("/processnow")
	public Response processUserMessages()
	{
		TaskRequest taskRequest = new TaskRequest();
		taskRequest.setAllowMultiple(false);
		taskRequest.setName("Process All User Messages Now");

		UserMessage userMessageExample = new UserMessage();
		userMessageExample.setActiveStatus(UserMessage.ACTIVE_STATUS);
		taskRequest.setDetails("Sending messages: " + service.getPersistenceService().countByExample(userMessageExample));
		service.getAsyncProxy(service.getUserService(), taskRequest).processAllUserMessages(true);
		return Response.ok().build();
	}

	@POST
	@APIDescription("Cleanup old user messages according to archive rules")
	@RequireSecurity(SecurityPermission.ADMIN_MESSAGE_MANAGEMENT)
	@Path("/cleanold")
	public Response cleanOld()
	{
		service.getUserService().cleanupOldUserMessages();
		return Response.ok().build();
	}

}
