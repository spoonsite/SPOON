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
package edu.usu.sdl.openstorefront.core.view.statistic;

import java.util.Date;

/**
 *
 * @author dshurtleff
 */
public class SystemStatisticView
{

	private long errorTickets;
	private Date dateOfLastErrorTicket;
	private long scheduleReports;
	private long tasksRunning;
	private String currentBranding;
	private long queuedMessages;
	private long alertsSetup;

	public SystemStatisticView()
	{
	}

	public long getErrorTickets()
	{
		return errorTickets;
	}

	public void setErrorTickets(long errorTickets)
	{
		this.errorTickets = errorTickets;
	}

	public Date getDateOfLastErrorTicket()
	{
		return dateOfLastErrorTicket;
	}

	public void setDateOfLastErrorTicket(Date dateOfLastErrorTicket)
	{
		this.dateOfLastErrorTicket = dateOfLastErrorTicket;
	}

	public long getScheduleReports()
	{
		return scheduleReports;
	}

	public void setScheduleReports(long scheduleReports)
	{
		this.scheduleReports = scheduleReports;
	}

	public long getTasksRunning()
	{
		return tasksRunning;
	}

	public void setTasksRunning(long tasksRunning)
	{
		this.tasksRunning = tasksRunning;
	}

	public String getCurrentBranding()
	{
		return currentBranding;
	}

	public void setCurrentBranding(String currentBranding)
	{
		this.currentBranding = currentBranding;
	}

	public long getQueuedMessages()
	{
		return queuedMessages;
	}

	public void setQueuedMessages(long queuedMessages)
	{
		this.queuedMessages = queuedMessages;
	}

	public long getAlertsSetup()
	{
		return alertsSetup;
	}

	public void setAlertsSetup(long alertsSetup)
	{
		this.alertsSetup = alertsSetup;
	}

}
