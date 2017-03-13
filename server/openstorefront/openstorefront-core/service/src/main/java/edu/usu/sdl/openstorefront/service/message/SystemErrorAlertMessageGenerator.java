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

import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.ErrorTicket;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.codemonkey.simplejavamail.email.Email;

/**
 *
 * @author dshurtleff
 */
public class SystemErrorAlertMessageGenerator
		extends BaseMessageGenerator
{

	private static final Logger log = Logger.getLogger(SystemErrorAlertMessageGenerator.class.getName());

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

		//We need to back up a bit to capture the error that triggered the message. It's possible it will pick old ones if they'are are close.
		ErrorTicket errorTicketStartExample = new ErrorTicket();
		Instant instant = Instant.ofEpochMilli(messageContext.getUserMessage().getCreateDts().getTime());
		instant = instant.minusSeconds(1);
		errorTicketStartExample.setCreateDts(new Date(instant.toEpochMilli()));

		QueryByExample queryByExample = new QueryByExample(errorTicketExample);
		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
		specialOperatorModel.setExample(errorTicketStartExample);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		List<ErrorTicket> tickets = serviceProxy.getPersistenceService().queryByExample(queryByExample);
		if (!tickets.isEmpty()) {
			message.append("System errors have occured.  ")
					.append(tickets.size()).append("  error(s) since: ").append(sdf.format(messageContext.getUserMessage().getCreateDts())).append("<hr>");

			message.append("<ul>");
			for (ErrorTicket ticket : tickets) {
				message.append("  <li> ").append(TranslateUtil.translate(ErrorTypeCode.class, ticket.getErrorTypeCode()))
						.append(" - ").append(ticket.getMessage())
						.append(" - ").append(ticket.getErrorTicketId()).append("</li><br>");
			}
			message.append("</ul>");
			message.append("See attached for details.<br>");

			int max = tickets.size();
			if (tickets.size() > MAX_TICKETS_TO_ATTACH) {
				message.append("(Only ").append(MAX_TICKETS_TO_ATTACH).append(" are attached login to view more.)<br>");
				max = MAX_TICKETS_TO_ATTACH;
			}

			for (int i = 0; i < max; i++) {
				ErrorTicket ticket = tickets.get(i);
				String ticketData = serviceProxy.getSystemService().errorTicketInfo(ticket.getErrorTicketId());
				if (StringUtils.isNotBlank(ticketData)) {
					email.addAttachment("error-" + ticket.getErrorTicketId() + ".txt", ticketData.getBytes(Charset.defaultCharset()), "text/plain");
				}
			}
		} else {
			log.log(Level.WARNING, MessageFormat.format("System Error Message was queue...however no error tickets found within window. "
					+ " Likely, the error occur before message time window.  "
					+ "Use the System tool to find error.  Message Time: {0}", sdf.format(messageContext.getUserMessage().getCreateDts())));
		}

		return message.toString();
	}

	@Override
	protected String getUnsubscribe()
	{
		return "To stop receiving this message, please contact an administrator.";
	}

}
