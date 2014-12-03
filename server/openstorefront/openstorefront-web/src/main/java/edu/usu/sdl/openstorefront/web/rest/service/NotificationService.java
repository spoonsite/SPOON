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

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.transfermodel.AdminMessage;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;

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

	@POST
	@APIDescription("Sends an admin message to all active user with emails.")
	@RequireAdmin
	@Path("/admin-message")
	public Response adminMessage(
			@RequiredParam AdminMessage adminMessage)
	{
		ValidationModel validationModel = new ValidationModel(adminMessage);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {

			service.getUserService().sendAdminMessage(adminMessage);
			return Response.ok().build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@POST
	@APIDescription("Sends recent change email to all user that are flag to be notified or an email via the query param")
	@RequireAdmin
	@Path("/recent-changes")
	public Response recentChanges(
			@QueryParam("lastRunDts")
			@APIDescription("MM/dd/yyyy - (Defaults to the beginning of the current day in server time)") String lastRunDtsValue,
			@QueryParam("emailAddress")
			@APIDescription("Set to send a preview email just to that address") String emailAddress)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

		Date lastRunDts;
		if (StringUtils.isBlank(lastRunDtsValue)) {
			lastRunDts = TimeUtil.beginningOfDay(TimeUtil.currentDate());
		} else {
			lastRunDts = simpleDateFormat.parse(lastRunDtsValue, new ParsePosition(0));
		}

		if (lastRunDts != null) {
			service.getUserService().sendRecentChangeEmail(lastRunDts, emailAddress);
		} else {
			throw new OpenStorefrontRuntimeException("Unable to parse last run dts", "Check last run dts param format (MM/dd/yyyy) ");
		}
		return Response.ok().build();
	}

}
