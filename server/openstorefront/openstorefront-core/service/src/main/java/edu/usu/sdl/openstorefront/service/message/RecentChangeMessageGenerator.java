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

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import java.text.SimpleDateFormat;
import org.codemonkey.simplejavamail.Email;

/**
 *
 * @author dshurtleff
 */
public class RecentChangeMessageGenerator
		extends BaseMessageGenerator
{

	public RecentChangeMessageGenerator(MessageContext messageContext)
	{
		super(messageContext);
	}

	@Override
	protected String getSubject()
	{
		return "Recently Updated";
	}

	@Override
	protected String generateMessageInternal(Email email)
	{
		StringBuilder message = new StringBuilder();

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String lastRunDate = sdf.format(messageContext.getRecentChangeMessage().getLastRunDts());

		message.append("Changes since: <b>").append(lastRunDate).append("</b><br><br>");
		boolean changes = false;
		if (messageContext.getRecentChangeMessage().getComponentsAdded().isEmpty() == false) {

			message.append(messageContext.getRecentChangeMessage().getComponentsAdded().size())
					.append(" ")
					.append(StringProcessor.puralize(messageContext.getRecentChangeMessage().getComponentsAdded().size(), "entry", "entries"))
					.append(" added.<br>");
			changes = true;
		}

		if (messageContext.getRecentChangeMessage().getComponentsUpdated().isEmpty() == false) {
			message.append(messageContext.getRecentChangeMessage().getComponentsUpdated().size())
					.append(" ")
					.append(StringProcessor.puralize(messageContext.getRecentChangeMessage().getComponentsUpdated().size(), "entry", "entries"))
					.append(" updated.<br>");
			changes = true;
		}

		if (messageContext.getRecentChangeMessage().getArticlesAdded().isEmpty() == false) {
			message.append(messageContext.getRecentChangeMessage().getArticlesAdded().size())
					.append(" ")
					.append(StringProcessor.puralize(messageContext.getRecentChangeMessage().getArticlesAdded().size(), "article", null))
					.append(" added.<br>");
			changes = true;
		}

		if (messageContext.getRecentChangeMessage().getArticlesUpdated().isEmpty() == false) {
			message.append(messageContext.getRecentChangeMessage().getArticlesUpdated().size())
					.append(" ")
					.append(StringProcessor.puralize(messageContext.getRecentChangeMessage().getArticlesAdded().size(), "article", null))
					.append(" updated.<br>");
			changes = true;
		}

		if (messageContext.getRecentChangeMessage().getHighlightsAdded().isEmpty() == false) {
			message.append(messageContext.getRecentChangeMessage().getHighlightsAdded().size())
					.append(" ")
					.append(StringProcessor.puralize(messageContext.getRecentChangeMessage().getHighlightsAdded().size(), "hightlight", null))
					.append(" added.<br>");
			changes = true;
		}

		if (messageContext.getRecentChangeMessage().getHighlightsUpdated().isEmpty() == false) {
			message.append(messageContext.getRecentChangeMessage().getHighlightsUpdated().size())
					.append(" ")
					.append(StringProcessor.puralize(messageContext.getRecentChangeMessage().getHighlightsUpdated().size(), "hightlight", null))
					.append(" updated.<br>");
			changes = true;
		}

		if (changes == false) {
			message.append("No recent changes have occured.");
		}

		return message.toString();
	}

	@Override
	protected String getUnsubscribe()
	{
		return "To stop receiving updates, please login and uncheck the notify of updates flag in your user profile.";
	}

}
