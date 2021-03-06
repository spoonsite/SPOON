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
package edu.usu.sdl.openstorefront.web.rest.service;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.model.TaskRequest;
import edu.usu.sdl.openstorefront.core.entity.ApplicationProperty;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.model.AdminMessage;
import edu.usu.sdl.openstorefront.core.model.ContactVendorMessage;
import edu.usu.sdl.openstorefront.core.view.RecentChangesStatus;
import edu.usu.sdl.openstorefront.core.view.SimpleRestError;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import org.codemonkey.simplejavamail.email.Email;

import javax.mail.Message;

/**
 * Notification Services
 *
 * @author dshurtleff
 */
@Path("v1/service/notification")
@APIDescription("Notification Services")
public class NotificationService
		extends BaseResource
{
	private static final Logger LOG = Logger.getLogger(NotificationService.class.getName());

	@POST
	@APIDescription("Sends an admin message to all active user with emails.")
	@RequireSecurity(SecurityPermission.ADMIN_MESSAGE_MANAGEMENT_CREATE)
	@Path("/admin-message")
	public Response adminMessage(
			@RequiredParam AdminMessage adminMessage)
	{
		ValidationModel validationModel = new ValidationModel(adminMessage);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			TaskRequest taskRequest = new TaskRequest();
			taskRequest.setAllowMultiple(true);
			taskRequest.setName("Sending Admin Message");
			taskRequest.setDetails("Emailing: " + Arrays.toString(adminMessage.getUsersToEmail().toArray(new String[0])));
			service.getAsyncProxy(service.getUserService(), taskRequest).sendAdminMessage(adminMessage);
			return Response.ok().build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@POST
	@APIDescription("Sends recent change email to all users that are flagged to be notified or an email via the query param")
	@RequireSecurity(SecurityPermission.ADMIN_MESSAGE_MANAGEMENT_CREATE)
	@Path("/recent-changes")
	public Response recentChanges(
			@QueryParam("lastRunDts")
			@APIDescription("MM/dd/yyyy or Unix Epoch - (Defaults to the beginning of the current day in server time)") String lastRunDtsValue,
			@QueryParam("emailAddress")
			@APIDescription("Set to send a preview email just to that address") String emailAddress)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

		Date lastRunDts;
		if (StringUtils.isBlank(lastRunDtsValue)) {
			lastRunDts = TimeUtil.beginningOfDay(TimeUtil.currentDate());
		} else {
			lastRunDts = simpleDateFormat.parse(lastRunDtsValue, new ParsePosition(0));
			if (lastRunDts == null) {
				if (StringUtils.isNumeric(lastRunDtsValue)) {
					lastRunDts = new Date(Convert.toLong(lastRunDtsValue));
				}
			}
		}

		if (lastRunDts != null) {
			TaskRequest taskRequest = new TaskRequest();
			taskRequest.setAllowMultiple(true);
			taskRequest.setName("Send Recent Change Email");
			String email = "";
			if (StringUtils.isNotBlank(emailAddress)) {
				email = " Email: " + emailAddress;
			}
			taskRequest.setDetails("Start Date: " + lastRunDts + email);
			service.getAsyncProxy(service.getUserService(), taskRequest).sendRecentChangeEmail(lastRunDts, emailAddress);
		} else {
			throw new OpenStorefrontRuntimeException("Unable to parse last run dts", "Check last run dts param format (MM/dd/yyyy) ");
		}
		return Response.ok().build();
	}

	@POST
	@APIDescription("Sends an email to a vendor")
	@RequireSecurity(SecurityPermission.USER_PROFILE_PAGE)
	@Path("/contact-vendor-template")
	public Response contactVendorTemplate(
			@RequiredParam ContactVendorMessage contactVendorMessage)
	{
		ValidationModel validationModel = new ValidationModel(contactVendorMessage);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			contactVendorMessage.updateConfigs();
			Email email = MailManager.newTemplateEmail(MailManager.Templates.CONTACT_VENDOR.toString(), contactVendorMessage, false);

			email.addRecipient("", contactVendorMessage.getUserToEmail(), Message.RecipientType.TO);
			email.setFromAddress(contactVendorMessage.getUserName(), contactVendorMessage.getUserFromEmail());
			email.setSubject("SPOON - Request for Information");
			try {
				MailManager.send(email, true);
			} catch (Exception e) {
				SimpleRestError simpleRestError = new SimpleRestError();
				simpleRestError.setError(e.toString());
				return Response.ok(simpleRestError).build();
			}
			return Response.ok().build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@GET
	@APIDescription("Gets the status of the recent change email.")
	@RequireSecurity(SecurityPermission.ADMIN_MESSAGE_MANAGEMENT_READ)
	@DataType(RecentChangesStatus.class)
	@Path("/recent-changes/status")
	public Response recentChangesStatus()
	{
		long days = Convert.toLong(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MESSAGE_RECENT_CHANGE_DAYS, OpenStorefrontConstant.DEFAULT_RECENT_CHANGE_EMAIL_INTERVAL));
		long daysInMillis = TimeUtil.daysToMillis(days);

		String lastRunDtsString = service.getSystemService().getPropertyValue(ApplicationProperty.RECENT_CHANGE_EMAIL_LAST_DTS);
		Date lastRunDts = null;
		if (lastRunDtsString != null) {
			lastRunDts = TimeUtil.fromString(lastRunDtsString);
		}

		Date nextSendDate = new Date(System.currentTimeMillis() + daysInMillis);
		if (lastRunDts != null) {
			nextSendDate = new Date(lastRunDts.getTime() + daysInMillis);
		}

		RecentChangesStatus recentChangesStatus = new RecentChangesStatus();
		recentChangesStatus.setLastSentDts(lastRunDts);
		recentChangesStatus.setNextSendDts(nextSendDate);

		return sendSingleEntityResponse(recentChangesStatus);
	}

	private String getByteArrayFromImageURL(String url) {
		try {
			URL imageUrl = new URL(url);
			URLConnection ucon = imageUrl.openConnection();
			InputStream is = ucon.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int read = 0;
			while ((read = is.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, read);
			}
			baos.flush();
			return Base64.getEncoder().encodeToString(baos.toByteArray());
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.toString());
		}
		return null;
	}
}
