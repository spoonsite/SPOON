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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ErrorTicket;
import java.util.ArrayList;
import java.util.List;

/**
 * Wraps error tickets
 *
 * @author dshurtleff
 */
public class ErrorTicketWrapper
		extends ListWrapper
{

	@DataType(ErrorTicket.class)
	private List<ErrorTicket> errorTickets = new ArrayList<>();

	public ErrorTicketWrapper(List<ErrorTicket> errorTickets, long totalNumber)
	{
		this.errorTickets = errorTickets;
		this.totalNumber = totalNumber;
		this.results = errorTickets.size();
	}

	public List<ErrorTicket> getErrorTickets()
	{
		return errorTickets;
	}

	public void setErrorTickets(List<ErrorTicket> errorTickets)
	{
		this.errorTickets = errorTickets;
	}

}
