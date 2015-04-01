/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.message;

import edu.usu.sdl.openstorefront.service.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.service.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.storage.model.Alert;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestion;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.storage.model.ComponentReview;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import edu.usu.sdl.openstorefront.util.Convert;
import edu.usu.sdl.openstorefront.util.StringProcessor;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.codemonkey.simplejavamail.Email;

/**
 *
 * @author dshurtleff
 */
public class UserDataAlertMessageGenerator
		extends BaseMessageGenerator
{

	private static final int MAX_SENSTIVE_DATA_LENGTH = 50;

	public UserDataAlertMessageGenerator(MessageContext messageContext)
	{
		super(messageContext);
	}

	@Override
	protected String getSubject()
	{
		return "User Data Modified";
	}

	@Override
	protected String generateMessageInternal(Email email)
	{
		StringBuilder message = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");

		Instant instant = Instant.ofEpochMilli(messageContext.getUserMessage().getCreateDts().getTime());
		instant = instant.minusSeconds(3);

		Date checkDate = new Date(instant.toEpochMilli());

		Alert alert = messageContext.getAlert();
		if (Convert.toBoolean(alert.getUserDataAlertOption().getAlertOnTags())) {
			ComponentTag componentTagExample = new ComponentTag();
			componentTagExample.setActiveStatus(ComponentTag.ACTIVE_STATUS);

			ComponentTag componentTagStartExample = new ComponentTag();
			componentTagStartExample.setUpdateDts(checkDate);

			QueryByExample queryByExample = new QueryByExample(componentTagExample);
			SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
			specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
			specialOperatorModel.setExample(componentTagStartExample);
			queryByExample.getExtraWhereCauses().add(specialOperatorModel);

			List<ComponentTag> tags = serviceProxy.getPersistenceService().queryByExample(ComponentTag.class, queryByExample);
			tags = tags.stream()
					.filter(tag -> Convert.toBoolean(tag.getAdminModified()) == false)
					.collect(Collectors.toList());

			if (!tags.isEmpty()) {
				message.append("<b>Tags</b> added: ").append(tags.size()).append("<hr>");
				message.append("<ul>");
			}
			for (ComponentTag tag : tags) {
				message.append("   <li>'").append(tag.getText())
						.append("' was added on  ")
						.append(serviceProxy.getComponentService().getComponentName(tag.getComponentId()))
						.append(" by ").append(tag.getUpdateUser())
						.append("</li>");
			}
			if (!tags.isEmpty()) {
				message.append("</ul><br>");
			}
		}

		if (Convert.toBoolean(alert.getUserDataAlertOption().getAlertOnReviews())) {
			ComponentReview componentReviewExample = new ComponentReview();
			componentReviewExample.setActiveStatus(ComponentReview.ACTIVE_STATUS);

			ComponentReview componentReviewStartExample = new ComponentReview();
			componentReviewStartExample.setUpdateDts(checkDate);

			QueryByExample queryByExample = new QueryByExample(componentReviewExample);
			SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
			specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
			specialOperatorModel.setExample(componentReviewStartExample);
			queryByExample.getExtraWhereCauses().add(specialOperatorModel);

			List<ComponentReview> reviews = serviceProxy.getPersistenceService().queryByExample(ComponentReview.class, queryByExample);
			reviews = reviews.stream()
					.filter(review -> Convert.toBoolean(review.getAdminModified()) == false)
					.collect(Collectors.toList());

			if (!reviews.isEmpty()) {
				message.append("User <b>Reviews</b> modified: ").append(reviews.size()).append("<hr>");
				message.append("<ul>");
			}
			for (ComponentReview review : reviews) {
				message.append(" <li>'").append(review.getTitle())
						.append("' modified by ").append(review.getUpdateUser())
						.append(" on component ").append(serviceProxy.getComponentService().getComponentName(review.getComponentId()))
						.append("</li>");
			}
			if (!reviews.isEmpty()) {
				message.append("</ul><br>");
			}
		}

		if (Convert.toBoolean(alert.getUserDataAlertOption().getAlertOnQuestions())) {
			ComponentQuestion componentQuestionExample = new ComponentQuestion();
			componentQuestionExample.setActiveStatus(ComponentQuestion.ACTIVE_STATUS);

			ComponentQuestion componentQuestionStartExample = new ComponentQuestion();
			componentQuestionStartExample.setUpdateDts(checkDate);

			QueryByExample queryByExample = new QueryByExample(componentQuestionExample);
			SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
			specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
			specialOperatorModel.setExample(componentQuestionStartExample);
			queryByExample.getExtraWhereCauses().add(specialOperatorModel);

			List<ComponentQuestion> questions = serviceProxy.getPersistenceService().queryByExample(ComponentQuestion.class, queryByExample);
			questions = questions.stream()
					.filter(question -> Convert.toBoolean(question.getAdminModified()) == false)
					.collect(Collectors.toList());

			if (!questions.isEmpty()) {
				message.append("User <b>Questions</b> modified: ").append(questions.size()).append("<hr>");
				message.append("<ul>");
			}
			for (ComponentQuestion question : questions) {
				message.append("  <li>'").append(StringProcessor.eclipseString(question.getQuestion(), MAX_SENSTIVE_DATA_LENGTH))
						.append("' modified by ").append(question.getUpdateUser())
						.append(" on component ").append(serviceProxy.getComponentService().getComponentName(question.getComponentId()))
						.append("</li>");
			}
			if (!questions.isEmpty()) {
				message.append("</ul><br>");
			}

			ComponentQuestionResponse componentQuestionResponseExample = new ComponentQuestionResponse();
			componentQuestionResponseExample.setActiveStatus(ComponentQuestion.ACTIVE_STATUS);

			ComponentQuestionResponse componentQuestionResponseStartExample = new ComponentQuestionResponse();
			componentQuestionResponseStartExample.setUpdateDts(checkDate);

			queryByExample = new QueryByExample(componentQuestionResponseExample);
			specialOperatorModel = new SpecialOperatorModel();
			specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
			specialOperatorModel.setExample(componentQuestionResponseStartExample);
			queryByExample.getExtraWhereCauses().add(specialOperatorModel);

			List<ComponentQuestionResponse> questionReponses = serviceProxy.getPersistenceService().queryByExample(ComponentQuestionResponse.class, queryByExample);
			questionReponses = questionReponses.stream()
					.filter(questionResponse -> Convert.toBoolean(questionResponse.getAdminModified()) == false)
					.collect(Collectors.toList());

			if (!questionReponses.isEmpty()) {
				message.append("User <b>Question Responses</b> modified: ").append(questionReponses.size()).append("<hr>");
				message.append("<ul>");
			}
			for (ComponentQuestionResponse question : questionReponses) {
				message.append("  <li>'").append(StringProcessor.eclipseString(question.getResponse(), MAX_SENSTIVE_DATA_LENGTH))
						.append("' modified by ").append(question.getUpdateUser())
						.append(" on component ").append(serviceProxy.getComponentService().getComponentName(question.getComponentId()))
						.append("</li>");
			}
			if (!questionReponses.isEmpty()) {
				message.append("</ul><br>");
			}
		}

		return message.toString();
	}

	@Override
	protected String getUnsubscribe()
	{
		return "To stop receiving this message, please contact an administrator.";
	}

}
