/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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

/**
 * Used to display status
 *
 * @author dshurtleff
 */
public class SystemStatusView
{

	private boolean started;
	private String systemStatus;
	private String detailedStatus;

	public SystemStatusView()
	{
	}

	public boolean getStarted()
	{
		return started;
	}

	public void setStarted(boolean started)
	{
		this.started = started;
	}

	public String getSystemStatus()
	{
		return systemStatus;
	}

	public void setSystemStatus(String systemStatus)
	{
		this.systemStatus = systemStatus;
	}

	public String getDetailedStatus()
	{
		return detailedStatus;
	}

	public void setDetailedStatus(String detailedStatus)
	{
		this.detailedStatus = detailedStatus;
	}

}
