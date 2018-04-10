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
import edu.usu.sdl.apiclient.rest.resource.ComponentRESTClient;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import static edu.usu.sdl.openstorefront.core.entity.UserTypeCode.END_USER;

/**
 *
 * @author rfrazier
 */
public class ComponentAnswerProvider
{

	ComponentRESTClient client;

	public ComponentAnswerProvider(ClientAPI apiClient)
	{
		client = new ComponentRESTClient(apiClient);
	}

	public ComponentQuestionResponse addComponentQuestionAnswer(String answer, ComponentQuestion question, Component component)
	{
		ComponentQuestionResponse compAnswer = new ComponentQuestionResponse();

		//create compAnswer according to QuestionResponse
		compAnswer.setResponse(answer);
		compAnswer.setOrganization(component.getOrganization());
		compAnswer.setUserTypeCode(END_USER);
		compAnswer.setQuestionId(question.getQuestionId()); //not sure where the id comes from

		compAnswer = client.addComponentQuestionResponse(component.getComponentId(), question.getQuestionId(), compAnswer);
		return compAnswer;
	}
}
