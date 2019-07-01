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

import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.entity.UserMessage;
import edu.usu.sdl.openstorefront.core.entity.UserWatch;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.QuestionAll;
import edu.usu.sdl.openstorefront.core.model.ReviewAll;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codemonkey.simplejavamail.email.Email;

/**
 *
 * @author dshurtleff
 */
public class ComponentWatchMessageGenerator
		extends BaseMessageGenerator
{

	private static final Logger log = Logger.getLogger(ComponentWatchMessageGenerator.class.getName());

	public ComponentWatchMessageGenerator(MessageContext messageContext)
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

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm z");

		UserMessage userMessage = messageContext.getUserMessage();
		ComponentAll componentAll = serviceProxy.getComponentService().getFullComponent(userMessage.getComponentId());
		if (componentAll == null) {
			log.log(Level.WARNING, MessageFormat.format("Unable to find component that the user message is pointed to (Not Sending message).  ID: {0}", userMessage.getComponentId()));
			return null;
		}

		UserWatch userWatchExample = new UserWatch();
		userWatchExample.setUsername(userMessage.getUsername());
		userWatchExample.setComponentId(userMessage.getComponentId());
		UserWatch userWatch = serviceProxy.getPersistenceService().queryOneByExample(userWatchExample);

		message.append("The following item(s) on <b>").append(componentAll.getComponent().getName()).append("</b> were updated:<br><br>");

		boolean changes = false;
		if (componentAll.getComponent().getUpdateDts().after(userWatch.getLastViewDts())) {
			message.append(" - Summary<br>");
			changes = true;
		}

		if (changed(componentAll.getAttributes(), userWatch.getLastViewDts())) {
			message.append(" - Vitals<br>");
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

		if (changed(componentAll.getRelationships(), userWatch.getLastViewDts())) {
			message.append(" - Relationships<br>");
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

		if (newReviews > 0) {
			message.append("<b>").append(newReviews).append(" review(s) added/updated</b><br>");
		}
		if (newQuestions > 0) {
			message.append("<b>").append(newQuestions).append(" question(s) added/updated</b><br>");
		}
		if (newResponds > 0) {
			message.append("<b>").append(newResponds).append(" question response(s) added/updated</b><br>");
		}
		message.append("<br>Last viewed on: <b>").append(sdf.format(userWatch.getLastViewDts())).append("</b><br>");

		message.append("<br>");

		if (changes) {
			return message.toString();
		} else {
			return null;
		}
	}

	@Override
	protected String getUnsubscribe()
	{
		return "To stop receiving updates on this entry, please login and uncheck the notify flag for this entry from your \"Watches\". ";
	}

	private <T extends StandardEntity> boolean changed(List<T> entities, Date lastViewDts)
	{
		boolean changed = false;

		for (StandardEntity standardEntity : entities) {
			if (standardEntity.getUpdateDts().after(lastViewDts)) {
				changed = true;
				break;
			}
		}
		return changed;
	}

}
