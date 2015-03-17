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
import edu.usu.sdl.openstorefront.storage.model.ErrorTicket;
import edu.usu.sdl.openstorefront.storage.model.ErrorTypeCode;
import edu.usu.sdl.openstorefront.util.TranslateUtil;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.codemonkey.simplejavamail.Email;

/**
 *
 * @author dshurtleff
 */
public class SystemErrorAlertMessageGenerator
		extends BaseMessageGenerator
{

	private static final int MAX_TICKETS_TO_ATTACH = 20;

	public SystemErrorAlertMessageGenerator(MessageContext messageContext)
	{
		super(messageContext);
	}

	@Override
	protected String getSubject()
	{
		return "System Error(s)";
	}

	@Override
	protected String generateMessageInternal(Email email)
	{
		StringBuilder message = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");

		ErrorTicket errorTicketExample = new ErrorTicket();
		errorTicketExample.setActiveStatus(ErrorTicket.ACTIVE_STATUS);

		//We need to back up a bit to capture the error that triggered the message. It's possible it will pick old ones if there are close.
		ErrorTicket errorTicketStartExample = new ErrorTicket();
		Instant instant = Instant.ofEpochMilli(messageContext.getUserMessage().getCreateDts().getTime());
		instant = instant.minusSeconds(1);
		errorTicketStartExample.setCreateDts(new Date(instant.toEpochMilli()));

		QueryByExample queryByExample = new QueryByExample(errorTicketExample);
		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
		specialOperatorModel.setExample(errorTicketStartExample);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		List<ErrorTicket> tickets = serviceProxy.getPersistenceService().queryByExample(ErrorTicket.class, queryByExample);
		if (!tickets.isEmpty()) {
			message.append("System error have occured.  ")
					.append(tickets.size()).append("  error(s) since: ").append(sdf.format(messageContext.getUserMessage().getCreateDts())).append("<hr>");

		}
		for (ErrorTicket ticket : tickets) {
			message.append("  ").append(TranslateUtil.translate(ErrorTypeCode.class, ticket.getErrorTypeCode()))
					.append(" - ").append(ticket.getErrorTicketId()).append("<br>");
		}
		message.append(" See attached for details.<br>");

		int max = tickets.size();
		if (tickets.size() > MAX_TICKETS_TO_ATTACH) {
			message.append("(Only ").append(MAX_TICKETS_TO_ATTACH).append(" are attached login to view more.<br>");
			max = MAX_TICKETS_TO_ATTACH;
		}

		for (int i = 0; i < max; i++) {
			ErrorTicket ticket = tickets.get(i);
			String ticketData = serviceProxy.getSystemService().errorTicketInfo(ticket.getErrorTicketId());
			email.addAttachment("error-" + ticket.getErrorTicketId() + ".txt", ticketData.getBytes(Charset.defaultCharset()), "text/plain");
		}

		return message.toString();
	}

	@Override
	protected String getUnsubscribe()
	{
		return "To stop receiving this message, please contact an administrator.";
	}

}
