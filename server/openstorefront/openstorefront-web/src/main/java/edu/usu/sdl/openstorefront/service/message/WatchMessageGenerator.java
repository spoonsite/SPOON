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

		if (componentAll.getComponent().getUpdateDts().after(userWatch.getLastViewDts())) {
			message.append(" - Component Summary<br>");
		}

		if (changed(componentAll.getAttributes(), userWatch.getLastViewDts())) {
			message.append(" - Component Vitals<br>");
		}

		if (changed(componentAll.getContacts(), userWatch.getLastViewDts())) {
			message.append(" - Contacts<br>");
		}

		if (changed(componentAll.getResources(), userWatch.getLastViewDts())) {
			message.append(" - Resources<br>");
		}

		if (changed(componentAll.getExternalDependencies(), userWatch.getLastViewDts())) {
			message.append(" - Dependancies<br>");
		}

		if (changed(componentAll.getMedia(), userWatch.getLastViewDts())) {
			message.append(" - Media<br>");
		}

		if (changed(componentAll.getMetadata(), userWatch.getLastViewDts())) {
			message.append(" - Metadata<br>");
		}

		if (changed(componentAll.getTags(), userWatch.getLastViewDts())) {
			message.append(" - Tags<br>");
		}

		if (changed(componentAll.getEvaluationSections(), userWatch.getLastViewDts())) {
			message.append(" - Evaluation Information<br>");
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

		message.append("There has also been: <br> <b>").append(newReviews).append(" review(s)</b><br> <b>")
				.append(newQuestions).append(" questions</b><br>")
				.append(newResponds).append(" questions responds</b><br>")
				.append(" update since last viewed.");

		message.append("<br>");
		message.append("Last viewed on: ").append(sdf.format(userWatch.getLastViewDts())).append("<br>");
		message.append("Please login to view the changes.<br>");

		return message.toString();
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
