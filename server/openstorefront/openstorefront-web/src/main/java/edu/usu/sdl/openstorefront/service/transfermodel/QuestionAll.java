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
package edu.usu.sdl.openstorefront.service.transfermodel;

import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestion;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestionResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class QuestionAll
{

	private ComponentQuestion question;

	@DataType(ComponentQuestionResponse.class)
	private List<ComponentQuestionResponse> responds = new ArrayList<>();

	public QuestionAll()
	{
	}

	public ComponentQuestion getQuestion()
	{
		return question;
	}

	public void setQuestion(ComponentQuestion question)
	{
		this.question = question;
	}

	public List<ComponentQuestionResponse> getResponds()
	{
		return responds;
	}

	public void setResponds(List<ComponentQuestionResponse> responds)
	{
		this.responds = responds;
	}

}
