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
package edu.usu.sdl.openstorefront.core.sort;

import edu.usu.sdl.openstorefront.core.entity.ErrorTicket;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Error Ticket sorter
 *
 * @author dshurtleff
 * @param <T>
 */
public class ErrorTicketComparator<T extends ErrorTicket>
		implements Comparator<T>, Serializable
{

	public static final int SORTBY_CREATE_DATE = 0;

	private int sortBy = SORTBY_CREATE_DATE;

	public ErrorTicketComparator()
	{
	}

	public ErrorTicketComparator(int sortBy)
	{
		this.sortBy = sortBy;
	}

	@Override
	public int compare(T o1, T o2)
	{
		switch (sortBy) {
			case SORTBY_CREATE_DATE:
				return o2.getCreateDts().compareTo(o1.getCreateDts());
		}
		return 0;
	}

}
