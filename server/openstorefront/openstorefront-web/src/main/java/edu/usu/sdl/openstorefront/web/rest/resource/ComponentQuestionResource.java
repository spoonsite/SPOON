/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionResponseView;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionView;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/componentquestions")
@APIDescription("Questions and Answers on entries/components")
public class ComponentQuestionResource
		extends BaseResource
{

	private static final Logger LOG = Logger.getLogger(ComponentQuestionResource.class.getSimpleName());

	@GET
	@APIDescription("Gets all active questions posed by a user")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentQuestionView.class)
	@Path("/{username}")
	public Response getQuestionsForUser(
			@PathParam("username") String username
	)
	{
		ComponentQuestion componentQuestionExample = new ComponentQuestion();
		componentQuestionExample.setActiveStatus(ComponentQuestion.ACTIVE_STATUS);
		componentQuestionExample.setCreateUser(username);

		List<ComponentQuestion> questions = componentQuestionExample.findByExample();

		GenericEntity<List<ComponentQuestionView>> entity = new GenericEntity<List<ComponentQuestionView>>(ComponentQuestionView.toViewList(questions, new HashMap<>()))
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets all active answers for a user.")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentQuestionResponseView.class)
	@Path("/responses/{username}")
	public Response getResponseForUser(
			@PathParam("username") String username
	)
	{
		ComponentQuestionResponse componentQuestionResponseExample = new ComponentQuestionResponse();
		componentQuestionResponseExample.setActiveStatus(ComponentQuestion.ACTIVE_STATUS);
		componentQuestionResponseExample.setCreateUser(username);

		List<ComponentQuestionResponse> responses = componentQuestionResponseExample.findByExample();

		GenericEntity<List<ComponentQuestionResponseView>> entity = new GenericEntity<List<ComponentQuestionResponseView>>(ComponentQuestionResponseView.toViewList(responses))
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets all responses for a question")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentQuestionResponseView.class)
	@Path("/{questionId}/responses")
	public Response getResponseForQuestion(
			@PathParam("questionId") String questionId
	)
	{
		ComponentQuestionResponse componentQuestionResponseExample = new ComponentQuestionResponse();
		componentQuestionResponseExample.setActiveStatus(ComponentQuestion.ACTIVE_STATUS);
		componentQuestionResponseExample.setQuestionId(questionId);

		List<ComponentQuestionResponse> responses = componentQuestionResponseExample.findByExample();

		GenericEntity<List<ComponentQuestionResponseView>> entity = new GenericEntity<List<ComponentQuestionResponseView>>(ComponentQuestionResponseView.toViewList(responses))
		{
		};
		return sendSingleEntityResponse(entity);
	}

}
