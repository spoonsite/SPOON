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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jlaw
 */
@APIDescription("Holds error ticket information")
public class ErrorTicket
		extends StandardEntity<ErrorTicket>
{

	@PK
	@NotNull
	private String errorTicketId;

	@APIDescription("What was called")
	private String calledAction;

	@APIDescription("What was data in the call")
	private String input;

	@NotNull
	@APIDescription("Pointer to the detail record")
	private String ticketFile;
	private String clientIp;

	@APIDescription("Message from the error")
	private String message;

	@APIDescription("Potential fix for the error, if available")
	private String potentialResolution;

	@NotNull
	@ValidValueType(value = {}, lookupClass = ErrorTypeCode.class)
	@FK(ErrorTypeCode.class)
	private String errorTypeCode;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ErrorTicket()
	{
	}

	public String getErrorTicketId()
	{
		return errorTicketId;
	}

	public void setErrorTicketId(String errorTicketId)
	{
		this.errorTicketId = errorTicketId;
	}

	public String getCalledAction()
	{
		return calledAction;
	}

	public void setCalledAction(String calledAction)
	{
		this.calledAction = calledAction;
	}

	public String getInput()
	{
		return input;
	}

	public void setInput(String input)
	{
		this.input = input;
	}

	public String getTicketFile()
	{
		return ticketFile;
	}

	public void setTicketFile(String ticketFile)
	{
		this.ticketFile = ticketFile;
	}

	public String getClientIp()
	{
		return clientIp;
	}

	public void setClientIp(String clientIp)
	{
		this.clientIp = clientIp;
	}

	public String getErrorTypeCode()
	{
		return errorTypeCode;
	}

	public void setErrorTypeCode(String errorTypeCode)
	{
		this.errorTypeCode = errorTypeCode;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getPotentialResolution()
	{
		return potentialResolution;
	}

	public void setPotentialResolution(String potentialResolution)
	{
		this.potentialResolution = potentialResolution;
	}

}
