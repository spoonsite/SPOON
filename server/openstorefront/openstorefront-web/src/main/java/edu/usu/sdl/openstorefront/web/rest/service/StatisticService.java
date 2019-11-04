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
package edu.usu.sdl.openstorefront.web.rest.service;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.Alert;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Branding;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.ErrorTicket;
import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.UserMessage;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserTracking;
import edu.usu.sdl.openstorefront.core.entity.UserWatch;
import edu.usu.sdl.openstorefront.core.view.statistic.ComponentRecordStatistic;
import edu.usu.sdl.openstorefront.core.view.statistic.ComponentStatisticView;
import edu.usu.sdl.openstorefront.core.view.statistic.SystemStatisticView;
import edu.usu.sdl.openstorefront.core.view.statistic.UserRecordStatistic;
import edu.usu.sdl.openstorefront.core.view.statistic.UserStatisticView;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.service.manager.AsyncTaskManager;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@Path("v1/service/statistic")
@APIDescription("Provides access to search listing in the application")
public class StatisticService
		extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_READ)
	@APIDescription("Gets component statistics")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentStatisticView.class)
	@Path("/component")
	public Response getComponentStatistic(
			@QueryParam("max")
			@DefaultValue("5") Integer max)
	{
		ComponentStatisticView componentStatisticView = new ComponentStatisticView();

		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
		componentExample.setApprovalState(ApprovalStatus.APPROVED);
		componentStatisticView.setNumberOfApprovedEntries(service.getPersistenceService().countByExample(componentExample));

		componentExample.setApprovalState(ApprovalStatus.PENDING);
		componentStatisticView.setNumberOfPendingEntries(service.getPersistenceService().countByExample(componentExample));

		componentExample.setApprovalState(ApprovalStatus.NOT_SUBMITTED);
		componentStatisticView.setNumberOfNotSubmited(service.getPersistenceService().countByExample(componentExample));

		AttributeType attributeTypeExample = new AttributeType();
		attributeTypeExample.setActiveStatus(AttributeType.ACTIVE_STATUS);
		componentStatisticView.setNumberOfAttributesTypes(service.getPersistenceService().countByExample(attributeTypeExample));

		AttributeCode attributeCodeExample = new AttributeCode();
		attributeCodeExample.setActiveStatus(AttributeCode.ACTIVE_STATUS);
		componentStatisticView.setNumberOfAttributesCodes(service.getPersistenceService().countByExample(attributeCodeExample));

		ComponentTracking componentTracking = new ComponentTracking();
		componentTracking.setActiveStatus(ComponentTracking.ACTIVE_STATUS);

		QueryByExample<ComponentTracking> queryByExample = new QueryByExample<>(componentTracking);
		ComponentTracking componentTrackingOrderBy = new ComponentTracking();
		componentTrackingOrderBy.setEventDts(QueryByExample.DATE_FLAG);
		queryByExample.setOrderBy(componentTrackingOrderBy);
		queryByExample.setSortDirection(OpenStorefrontConstant.SORT_DESCENDING);
		queryByExample.setMaxResults(max);

		ComponentTracking componentTrackingGroupBy = new ComponentTracking();
		componentTrackingGroupBy.setComponentId(QueryByExample.STRING_FLAG);
		queryByExample.setGroupBy(componentTrackingGroupBy);

		List<ComponentTracking> trackingRecords = service.getPersistenceService().queryByExample(queryByExample);
		for (ComponentTracking tracking : trackingRecords) {
			ComponentRecordStatistic recordStatistic = new ComponentRecordStatistic();
			recordStatistic.setComponentId(tracking.getComponentId());
			recordStatistic.setComponentName(service.getComponentService().getComponentName(tracking.getComponentId()));
			recordStatistic.setViewedUsername(tracking.getCreateUser());
			recordStatistic.setViewDts(tracking.getEventDts());
			componentStatisticView.getRecentlyViewed().add(recordStatistic);
		}
		componentStatisticView.setTopViewed(service.getComponentService().findTopViewedComponents(max));

		return sendSingleEntityResponse(componentStatisticView);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT_READ)
	@APIDescription("Gets user statistics")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserStatisticView.class)
	@Path("/user")
	public Response getUserStatistic(
			@QueryParam("max")
			@DefaultValue("5") Integer max)
	{
		UserStatisticView userStatisticView = new UserStatisticView();

		UserProfile userProfile = new UserProfile();
		userProfile.setActiveStatus(UserProfile.ACTIVE_STATUS);
		userStatisticView.setActiveUsers(service.getPersistenceService().countByExample(userProfile));

		ComponentReview componentReview = new ComponentReview();
		componentReview.setActiveStatus(ComponentReview.ACTIVE_STATUS);
		userStatisticView.setActiveUserReviews(service.getPersistenceService().countByExample(componentReview));

		ComponentQuestion componentQuestion = new ComponentQuestion();
		componentQuestion.setActiveStatus(ComponentQuestion.ACTIVE_STATUS);
		userStatisticView.setActiveUserQuestions(service.getPersistenceService().countByExample(componentQuestion));

		ComponentQuestionResponse componentQuestionResponse = new ComponentQuestionResponse();
		componentQuestionResponse.setActiveStatus(ComponentQuestionResponse.ACTIVE_STATUS);
		userStatisticView.setActiveUserQuestionResponses(service.getPersistenceService().countByExample(componentQuestionResponse));

		UserWatch userWatch = new UserWatch();
		userWatch.setActiveStatus(UserWatch.ACTIVE_STATUS);
		userStatisticView.setActiveUserWatches(service.getPersistenceService().countByExample(userWatch));

		UserTracking userTracking = new UserTracking();
		userTracking.setActiveStatus(ComponentTracking.ACTIVE_STATUS);

		QueryByExample<UserTracking> queryByExample = new QueryByExample<>(userTracking);
		UserTracking trackingOrderBy = new UserTracking();
		trackingOrderBy.setEventDts(QueryByExample.DATE_FLAG);
		queryByExample.setOrderBy(trackingOrderBy);
		queryByExample.setSortDirection(OpenStorefrontConstant.SORT_DESCENDING);
		queryByExample.setMaxResults(max);

		UserTracking userTrackingGroupBy = new UserTracking();
		userTrackingGroupBy.setCreateUser(QueryByExample.STRING_FLAG);
		queryByExample.setGroupBy(userTrackingGroupBy);

		List<UserTracking> trackingRecords = service.getPersistenceService().queryByExample(queryByExample);
		for (UserTracking tracking : trackingRecords) {
			UserRecordStatistic recordStatistic = new UserRecordStatistic();
			recordStatistic.setUsername(tracking.getCreateUser());
			recordStatistic.setLoginDts(tracking.getEventDts());
			recordStatistic.setBrowser(tracking.getBrowser());
			recordStatistic.setBrowserVersion(tracking.getBrowserVersion());
			userStatisticView.getRecentLogins().add(recordStatistic);
		}

		return sendSingleEntityResponse(userStatisticView);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@APIDescription("Gets system statistics")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SystemStatisticView.class)
	@Path("/system")
	public Response getSystemStatistic()
	{
		SystemStatisticView systemStatisticView = new SystemStatisticView();

		ErrorTicket errorTicket = new ErrorTicket();
		errorTicket.setActiveStatus(ErrorTicket.ACTIVE_STATUS);
		systemStatisticView.setErrorTickets(service.getPersistenceService().countByExample(errorTicket));

		ScheduledReport scheduledReport = new ScheduledReport();
		scheduledReport.setActiveStatus(ErrorTicket.ACTIVE_STATUS);
		systemStatisticView.setScheduleReports(service.getPersistenceService().countByExample(scheduledReport));

		UserMessage userMessage = new UserMessage();
		userMessage.setActiveStatus(ErrorTicket.ACTIVE_STATUS);
		systemStatisticView.setQueuedMessages(service.getPersistenceService().countByExample(userMessage));

		Alert alert = new Alert();
		alert.setActiveStatus(ErrorTicket.ACTIVE_STATUS);
		systemStatisticView.setAlertsSetup(service.getPersistenceService().countByExample(alert));

		systemStatisticView.setTasksRunning(AsyncTaskManager.managerStatus().getActiveCount());

		Branding brandingView = service.getBrandingService().getCurrentBrandingView();
		systemStatisticView.setCurrentBranding(brandingView.getName());

		return sendSingleEntityResponse(systemStatisticView);
	}

}
