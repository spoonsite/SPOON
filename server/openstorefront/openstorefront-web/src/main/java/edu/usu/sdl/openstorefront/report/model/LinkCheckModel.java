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
package edu.usu.sdl.openstorefront.report.model;

/**
 *
 * @author dshurtleff
 */
public class LinkCheckModel
{

	private String status;
	private String componentName;
	private String id;
	private String resourceType;
	private String networkOfLink;
	private String checkResults;
	private String link;
	private String httpStatus;
	private String securityMarking;

	public LinkCheckModel()
	{
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getComponentName()
	{
		return componentName;
	}

	public void setComponentName(String componentName)
	{
		this.componentName = componentName;
	}

	public String getResourceType()
	{
		return resourceType;
	}

	public void setResourceType(String resourceType)
	{
		this.resourceType = resourceType;
	}

	public String getNetworkOfLink()
	{
		return networkOfLink;
	}

	public void setNetworkOfLink(String networkOfLink)
	{
		this.networkOfLink = networkOfLink;
	}

	public String getCheckResults()
	{
		return checkResults;
	}

	public void setCheckResults(String checkResults)
	{
		this.checkResults = checkResults;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getHttpStatus()
	{
		return httpStatus;
	}

	public void setHttpStatus(String httpStatus)
	{
		this.httpStatus = httpStatus;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getSecurityMarking()
	{
		return securityMarking;
	}

	public void setSecurityMarking(String securityMarking)
	{
		this.securityMarking = securityMarking;
	}

}
