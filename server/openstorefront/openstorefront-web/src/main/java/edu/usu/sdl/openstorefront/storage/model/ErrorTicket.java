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
package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.doc.ValidValueType;
import edu.usu.sdl.openstorefront.util.PK;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jlaw
 */
public class ErrorTicket
		extends BaseEntity
{

	@PK
	@NotNull
	private String errorTicketId;
	private String calledAction;
	private String input;

	@NotNull
	private String ticketFile;
	private String clientIp;

	@NotNull
	@ValidValueType(value = {}, lookupClass = ErrorTypeCode.class)
	private String errorTypeCode;

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

}
