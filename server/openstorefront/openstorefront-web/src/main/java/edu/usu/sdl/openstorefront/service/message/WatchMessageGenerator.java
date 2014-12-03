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
package edu.usu.sdl.openstorefront.service.message;

import edu.usu.sdl.openstorefront.service.transfermodel.ComponentAll;
import edu.usu.sdl.openstorefront.service.transfermodel.QuestionAll;
import edu.usu.sdl.openstorefront.service.transfermodel.ReviewAll;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.storage.model.UserMessage;
import edu.usu.sdl.openstorefront.storage.model.UserWatch;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.codemonkey.simplejavamail.Email;

/**
 *
 * @author dshurtleff
 */
public class WatchMessageGenerator
		extends BaseMessageGenerator
{

	public WatchMessageGenerator(MessageContext messageContext)
	{
		super(messageContext);
	}

	@Override
	protected String getSubject()
	{
		return "Update";
	}

	@Override
	protected String generateMessageInternal(Email email)
	{
		StringBuilder message = new StringBuilder();

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");

		UserMessage userMessage = messageContext.getUserMessage();
		ComponentAll componentAll = serviceProxy.getComponentService().getFullComponent(userMessage.getComponentId());

		UserWatch userWatchExample = new UserWatch();
		userWatchExample.setUsername(userMessage.getUsername());
		userWatchExample.setComponentId(userMessage.getComponentId());
		UserWatch userWatch = serviceProxy.getPersistenceService().queryOneByExample(UserWatch.class, userWatchExample);

		message.append("The following items on <b>").append(componentAll.getComponent().getName()).append("</b> were updated:<br><br>");

		boolean changes = false;
		if (componentAll.getComponent().getUpdateDts().after(userWatch.getLastViewDts())) {
			message.append(" - Component Summary<br>");
			changes = true;
		}

		if (changed(componentAll.getAttributes(), userWatch.getLastViewDts())) {
			message.append(" - Component Vitals<br>");
			changes = true;
		}

		if (changed(componentAll.getContacts(), userWatch.getLastViewDts())) {
			message.append(" - Contacts<br>");
			changes = true;
		}

		if (changed(componentAll.getResources(), userWatch.getLastViewDts())) {
			message.append(" - Resources<br>");
			changes = true;
		}

		if (changed(componentAll.getExternalDependencies(), userWatch.getLastViewDts())) {
			message.append(" - Dependancies<br>");
			changes = true;
		}

		if (changed(componentAll.getMedia(), userWatch.getLastViewDts())) {
			message.append(" - Media<br>");
			changes = true;
		}

		if (changed(componentAll.getMetadata(), userWatch.getLastViewDts())) {
			message.append(" - Metadata<br>");
			changes = true;
		}

		if (changed(componentAll.getTags(), userWatch.getLastViewDts())) {
			message.append(" - Tags<br>");
			changes = true;
		}

		if (changed(componentAll.getEvaluationSections(), userWatch.getLastViewDts())) {
			message.append(" - Evaluation Information<br>");
			changes = true;
		}
		message.append("<br>");

		int newReviews = 0;
		for (ReviewAll reviewAll : componentAll.getReviews()) {
			if (reviewAll.getComponentReview().getUpdateDts().after(userWatch.getLastViewDts())) {
				newReviews++;
			}
		}

		int newQuestions = 0;
		int newResponds = 0;
		for (QuestionAll questionAll : componentAll.getQuestions()) {
			if (questionAll.getQuestion().getUpdateDts().after(userWatch.getLastViewDts())) {
				newQuestions++;
			}
			for (ComponentQuestionResponse response : questionAll.getResponds()) {
				if (response.getUpdateDts().after(userWatch.getLastViewDts())) {
					newResponds++;
				}
			}
		}

		if (newReviews > 0
				|| newQuestions > 0
				|| newResponds > 0) {
			changes = true;
		}

		message.append("There has been: <br> <b>").append(newReviews).append(" review(s)</b><br> <b>")
				.append(newQuestions).append(" question(s)</b><br>")
				.append("<b>").append(newResponds).append(" question response(s)</b><br>")
				.append(" updated since last viewed on: <b>").append(sdf.format(userWatch.getLastViewDts())).append("</b><br>");

		message.append("<br>");
		message.append("Please login to view the changes.<br>");

		if (changes) {
			return message.toString();
		} else {
			return null;
		}
	}

	@Override
	protected String getUnsubscribe()
	{
		return "To stop receiving updates on this component, please login and uncheck the notify flag for this component from your \"Watches\". ";
	}

	private <T extends BaseEntity> boolean changed(List<T> entities, Date lastViewDts)
	{
		boolean changed = false;

		for (BaseEntity baseEntity : entities) {
			if (baseEntity.getUpdateDts().after(lastViewDts)) {
				changed = true;
				break;
			}
		}
		return changed;
	}

}
