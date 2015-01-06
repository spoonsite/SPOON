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
package edu.usu.sdl.openstorefront.web.viewmodel;

import com.atlassian.jira.rest.client.api.domain.ServerInfo;
import java.io.Serializable;
import java.util.Date;

/**
 * Holds the Jira Manager Stats
 *
 * @author dshurtleff
 */
public class JiraStats
		implements Serializable
{

	private String baseUri;
	private String version;
	private int buildNumber;
	private Date buildDate;
	private Date serverTime;
	private String scmInfo;
	private String serverTitle;

	private int maxConnections;
	private int remainingConnections;

	public JiraStats()
	{
	}

	public void popluateServerInfo(ServerInfo serverInfo)
	{
		if (serverInfo != null) {
			baseUri = serverInfo.getBaseUri().toString();
			version = serverInfo.getVersion();
			buildNumber = serverInfo.getBuildNumber();
			buildDate = serverInfo.getBuildDate().toDate();
			if (serverInfo.getServerTime() != null) {
				serverTime = serverInfo.getServerTime().toDate();
			}
			scmInfo = serverInfo.getScmInfo();
			serverTitle = serverInfo.getServerTitle();
		}
	}

	public int getMaxConnections()
	{
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections)
	{
		this.maxConnections = maxConnections;
	}

	public int getRemainingConnections()
	{
		return remainingConnections;
	}

	public void setRemainingConnections(int remainingConnections)
	{
		this.remainingConnections = remainingConnections;
	}

	public String getBaseUri()
	{
		return baseUri;
	}

	public void setBaseUri(String baseUri)
	{
		this.baseUri = baseUri;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public int getBuildNumber()
	{
		return buildNumber;
	}

	public void setBuildNumber(int buildNumber)
	{
		this.buildNumber = buildNumber;
	}

	public Date getBuildDate()
	{
		return buildDate;
	}

	public void setBuildDate(Date buildDate)
	{
		this.buildDate = buildDate;
	}

	public Date getServerTime()
	{
		return serverTime;
	}

	public void setServerTime(Date serverTime)
	{
		this.serverTime = serverTime;
	}

	public String getScmInfo()
	{
		return scmInfo;
	}

	public void setScmInfo(String scmInfo)
	{
		this.scmInfo = scmInfo;
	}

	public String getServerTitle()
	{
		return serverTitle;
	}

	public void setServerTitle(String serverTitle)
	{
		this.serverTitle = serverTitle;
	}

}
