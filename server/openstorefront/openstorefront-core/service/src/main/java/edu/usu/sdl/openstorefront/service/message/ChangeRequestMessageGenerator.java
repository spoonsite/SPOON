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
package edu.usu.sdl.openstorefront.service.message;

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.codemonkey.simplejavamail.email.Email;


/**
 *
 * @author dshurtleff
 */
public class ChangeRequestMessageGenerator
		extends BaseMessageGenerator
{

	public ChangeRequestMessageGenerator(MessageContext messageContext)
	{
		super(messageContext);
	}

	@Override
	protected String getSubject()
	{
		return "Entry Change Requests";
	}

	@Override
	protected String generateMessageInternal(Email email)
	{
		StringBuilder message = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");

		Instant instant = Instant.ofEpochMilli(messageContext.getUserMessage().getCreateDts().getTime());
		instant = instant.minusSeconds(10);
		Date checkDate = new Date(instant.toEpochMilli());

		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.PENDING_STATUS);
		componentExample.setApprovalState(ApprovalStatus.PENDING);

		Component componentStartExample = new Component();
		componentStartExample.setUpdateDts(checkDate);

		QueryByExample queryByExample = new QueryByExample(componentExample);
		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
		specialOperatorModel.setExample(componentStartExample);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		//submitted
		List<Component> components = serviceProxy.getPersistenceService().queryByExample(queryByExample);
		if (components.isEmpty() == false) {
			message.append(components.size())
					.append(" ")
					.append(StringProcessor.puralize(components.size(), "entry", "entries"))
					.append(" submitted changes for <b>Approval</b> since:  ").append(sdf.format(messageContext.getUserMessage().getCreateDts())).append("<hr>");
			message.append("<ul>");
			for (Component component : components) {
				message.append(" <li>").append(TranslateUtil.translateComponentType(component.getComponentType()))
						.append(" &mdash; ")
						.append(component.getName())
						.append("  submitted by:  ").append(component.getCreateUser())
						.append(" at ").append(sdf.format(component.getSubmittedDts()))
						.append("</li>");
			}
			message.append("</ul><br>");
		}

		//canceled
		componentExample.setApprovalState(ApprovalStatus.NOT_SUBMITTED);
		components = serviceProxy.getPersistenceService().queryByExample(queryByExample);
		if (components.isEmpty() == false) {
			message.append(components.size())
					.append(" ")
					.append(StringProcessor.puralize(components.size(), "entry", "entries"))
					.append(" ")
					.append(StringProcessor.puralize(components.size(), "change request", null))
					.append(" <b>Canceled</b> for approval since:  ").append(sdf.format(messageContext.getUserMessage().getCreateDts())).append("<hr>");
			message.append("<ul>");
			for (Component component : components) {
				message.append(" <li>").append(component.getName())
						.append("  originally submitted by:  ").append(component.getCreateUser())
						.append(" at ").append(sdf.format(component.getSubmittedDts()))
						.append("</li>");
			}
			message.append("</ul><br>");
		}

		return message.toString();
	}

	@Override
	protected String getUnsubscribe()
	{
		return "To stop receiving this message, please contact an administrator.";
	}

}
