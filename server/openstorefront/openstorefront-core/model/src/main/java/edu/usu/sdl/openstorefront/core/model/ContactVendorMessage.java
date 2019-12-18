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
package edu.usu.sdl.openstorefront.core.model;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.entity.Branding;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Holds information for users contacting vendors.
 *
 * @author gfowler
 */
public class ContactVendorMessage
{

	protected Service service;

	@APIDescription("Email addresses to send message to (To Field)")
	@ConsumeField
    private String userToEmail;

    @APIDescription("Email addresses that the message is coming from (From Field)")
	@ConsumeField
	private String userFromEmail;

    @APIDescription("Name of user sending the message")
	@ConsumeField
	private String userName;

	@APIDescription("Name of the part")
	@ConsumeField
	private String partName;

	@APIDescription("Url of the parts entry detail page")
	@ConsumeField
	private String partId;

	private String partUrl;

	private String hostUrl;

	private String applicationName;

	private String supportEmail;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_ADMIN_MESSAGE)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String message;

	public void updateConfigs()
	{
		this.partUrl = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_EXTERNAL_HOST_URL) + "/Landing.action#/entry-detail/" + this.partId;
		this.hostUrl = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_EXTERNAL_HOST_URL);
		this.applicationName = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_APPLICATION_TITLE);
		this.supportEmail = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_FEEDBACK_EMAIL);
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getUserToEmail()
	{
		return userToEmail;
	}

	public void setUserToEmail(String userToEmail)
	{
		this.userToEmail = userToEmail;
    }

    public String getUserFromEmail()
	{
		return userFromEmail;
	}

	public void setUserFromEmail(String userFromEmail)
	{
		this.userFromEmail = userFromEmail;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartUrl() {
		return partUrl;
	}

	public void setPartUrl(String partUrl) {
		this.partUrl = partUrl;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public String getHostUrl() {
		return hostUrl;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getSupportEmail() {
		return supportEmail;
	}

	public void setSupportEmail(String supportEmail) {
		this.supportEmail = supportEmail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPartId() {
		return partId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}
}
