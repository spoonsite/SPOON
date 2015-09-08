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
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.QueryType;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserTracking;
import edu.usu.sdl.openstorefront.core.entity.UserWatch;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.UserProfileView;
import edu.usu.sdl.openstorefront.core.view.UserProfileWrapper;
import edu.usu.sdl.openstorefront.core.view.UserTrackingWrapper;
import edu.usu.sdl.openstorefront.core.view.UserWatchView;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireAdmin;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.security.UserProfileRequireHandler;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sourceforge.stripes.util.bean.BeanUtil;

/**
 * User Profile Resource
 *
 * @author dshurtleff
 */
@Path("v1/resource/userprofiles")
@APIDescription("A user profile contain information about the user and user specific data. A user profile is created at the time the user logins in.<br>"
		+ "Note: id if set to the current user it will won't require admin rights.")
public class UserProfileResource
		extends BaseResource
{

	@GET
	@APIDescription("Get a list of user profiles")
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserProfileView.class)
	public Response userProfiles(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		UserProfile userProfileExample = new UserProfile();
		userProfileExample.setActiveStatus(filterQueryParams.getStatus());

		UserProfile userProfileStartExample = new UserProfile();
		userProfileStartExample.setCreateDts(filterQueryParams.getStart());

		UserProfile userProfileEndExample = new UserProfile();
		userProfileEndExample.setCreateDts(filterQueryParams.getEnd());

		QueryByExample queryByExample = new QueryByExample(userProfileExample);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(userProfileStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(userProfileEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());
		queryByExample.setSortDirection(filterQueryParams.getSortOrder());

		UserProfile userProfileSortExample = new UserProfile();
		Field sortField = ReflectionUtil.getField(userProfileSortExample, filterQueryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), userProfileSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(userProfileSortExample);
		}

		List<UserProfile> userProfiles = service.getPersistenceService().queryByExample(UserProfile.class, queryByExample);

		UserProfileWrapper userProfileWrapper = new UserProfileWrapper();
		userProfileWrapper.getData().addAll(UserProfileView.toViewList(userProfiles));
		userProfileWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));

		return sendSingleEntityResponse(userProfileWrapper);
	}

	@GET
	@APIDescription("Gets the current user profile from the session.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserProfileView.class)
	@Path("/currentuser")
	public Response getCurrentUser()
	{
		UserProfileView userProfileView = null;

		UserContext userContext = SecurityUtil.getUserContext();
		if (userContext != null) {
			userProfileView = UserProfileView.toView(userContext.getUserProfile());
			userProfileView.setAdmin(SecurityUtil.isAdminUser());
		}
		return sendSingleEntityResponse(userProfileView);
	}

	@GET
	@APIDescription("Gets user profile specified by id.")
	@RequireAdmin(UserProfileRequireHandler.class)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserProfileView.class)
	@Path("/{id}")
	public UserProfileView userProfile(
			@PathParam(UserProfileRequireHandler.USERNAME_ID_PARAM)
			@RequiredParam String userId)
	{
		UserProfileView userProfileView = null;

		UserProfile userProfile = service.getUserService().getUserProfile(userId);
		if (userProfile != null) {
			userProfileView = UserProfileView.toView(userProfile);
			if (SecurityUtil.getCurrentUserName().equals(userId)) {
				userProfileView.setAdmin(SecurityUtil.isAdminUser());
			}
		}
		return userProfileView;
	}

	@POST
	@APIDescription("Update user profile returns updated profile.")
	@RequireAdmin(UserProfileRequireHandler.class)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProile(
			@RequiredParam UserProfile inputProfile)
	{
		inputProfile.setActiveStatus(UserProfile.ACTIVE_STATUS);
		ValidationModel validationModel = new ValidationModel(inputProfile);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (inputProfile.getUsername().isEmpty()) {
			RuleResult result = new RuleResult();
			result.setEntityClassName("User Profile");
			result.setFieldName("username");
			result.setInvalidData(inputProfile.getUsername());
			result.setMessage("The username must be set when posting a new user.");
			result.setValidationRule("username is a NotNull field");
			validationResult.getRuleResults().add(result);
			return Response.ok(validationResult.toRestError()).build();
		} else if (validationResult.valid()) {
			UserProfile userProfileSaved = service.getUserService().saveUserProfile(inputProfile);
			String username = StringProcessor.urlEncode(userProfileSaved.getUsername());
			return Response.created(URI.create("v1/resource/userprofiles/" + username)).entity(inputProfile).build();
		}
		return Response.ok(validationResult.toRestError()).build();
	}

	@PUT
	@APIDescription("Update user profile returns updated profile.")
	@RequireAdmin(UserProfileRequireHandler.class)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response updateProfile(
			@PathParam(UserProfileRequireHandler.USERNAME_ID_PARAM)
			@RequiredParam String userId,
			@RequiredParam UserProfile inputProfile)
	{
		inputProfile.setUsername(userId);
		ValidationModel validationModel = new ValidationModel(inputProfile);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			return Response.ok(service.getUserService().saveUserProfile(inputProfile)).build();
		}
		return Response.ok(validationResult.toRestError()).build();
	}

	@DELETE
	@APIDescription("Inactivates a user profile.  Note: if the user logs in their profile will be reactivated.")
	@RequireAdmin
	@Path("/{username}")
	public void deleteUserProfile(
			@PathParam("username")
			@RequiredParam String username)
	{
		service.getUserService().deleteProfile(username);
	}

	@PUT
	@APIDescription("Reactives a user profile.")
	@RequireAdmin
	@Path("/{username}/reactivate")
	public Response reactivateUserProfile(
			@PathParam("username")
			@RequiredParam String username)
	{
		Response response = Response.ok().build();
		UserProfile userProfile = service.getPersistenceService().findById(UserProfile.class, username);
		if (userProfile != null) {
			service.getUserService().reactiveProfile(username);
		} else {
			response = Response.status(Response.Status.NOT_FOUND).build();
		}
		return response;
	}

	@POST
	@APIDescription("Sends test email to user id")
	@RequireAdmin(UserProfileRequireHandler.class)
	@Path("/{id}/test-email")
	public Response sendTestEmail(
			@PathParam(UserProfileRequireHandler.USERNAME_ID_PARAM) String username
	)
	{
		service.getUserService().sendTestEmail(username);
		return Response.ok().build();
	}

	@GET
	@APIDescription("Retrieves Active User Watches.")
	@RequireAdmin(UserProfileRequireHandler.class)
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{id}/watches")
	@DataType(UserWatchView.class)
	public List<UserWatchView> getWatches(
			@PathParam(UserProfileRequireHandler.USERNAME_ID_PARAM)
			@RequiredParam String userId)
	{
		List<UserWatch> watches = service.getUserService().getWatches(userId);
		List<UserWatchView> views = new ArrayList<>();
		watches.forEach(watch -> {
			Component component = service.getPersistenceService().findById(Component.class, watch.getComponentId());
			views.add(UserWatchView.toView(watch, component));
		});
		return views;
	}

	@GET
	@APIDescription("Retrieves an user watch by id.")
	@RequireAdmin(UserProfileRequireHandler.class)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserWatchView.class)
	@Path("/{id}/watches/{watchId}")
	public UserWatchView getWatch(
			@PathParam(UserProfileRequireHandler.USERNAME_ID_PARAM)
			@RequiredParam String userId,
			@PathParam("watchId")
			@RequiredParam String watchId)
	{
		UserWatch watch = service.getUserService().getWatch(watchId);
		Component component = service.getPersistenceService().findById(Component.class, watch.getComponentId());
		return UserWatchView.toView(watch, component);
	}

	@POST
	@APIDescription("Add a new watch to an existing user.")
	@RequireAdmin(UserProfileRequireHandler.class)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/watches")
	public Response addWatch(
			@PathParam(UserProfileRequireHandler.USERNAME_ID_PARAM)
			@RequiredParam String userId,
			@RequiredParam UserWatch userWatch)
	{
		userWatch.setActiveStatus(UserProfile.ACTIVE_STATUS);
		userWatch.setUsername(userId);
		userWatch.setLastViewDts(TimeUtil.currentDate());

		Boolean check = Boolean.TRUE;
		List<UserWatch> watches = service.getUserService().getWatches(userId);
		for (int i = 0; i < watches.size() && check; i++) {
			check = !watches.get(i).getComponentId().equals(userWatch.getComponentId());
			if (!check) {
				userWatch = watches.get(i);
			}
		}
		if (check) {
			return saveWatch(userWatch, Boolean.TRUE);
		} else {
			ValidationResult validationResult = new ValidationResult();
			RuleResult result = new RuleResult();
			result.setMessage("Component was already in watch list");
			result.setValidationRule("Duplicate Check");
			validationResult.getRuleResults().add(result);
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@APIDescription("Update existing new watch. On update: it will update the last view date.")
	@RequireAdmin(UserProfileRequireHandler.class)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/watches/{watchId}")
	public Response updateWatch(
			@PathParam(UserProfileRequireHandler.USERNAME_ID_PARAM)
			@RequiredParam String userId,
			@PathParam("watchId")
			@RequiredParam String watchId,
			@RequiredParam UserWatch userWatch)
	{
		userWatch.setActiveStatus(UserWatch.ACTIVE_STATUS);
		userWatch.setUsername(userId);
		userWatch.setUserWatchId(watchId);
		return saveWatch(userWatch, Boolean.FALSE);
	}

	private Response saveWatch(UserWatch watch, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(watch);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			watch.setCreateUser(SecurityUtil.getCurrentUserName());
			watch.setUpdateUser(SecurityUtil.getCurrentUserName());
			if (post) {
				watch = service.getUserService().saveWatch(watch);
				String username = StringProcessor.urlEncode(watch.getUsername());
				return Response.created(URI.create("v1/resource/userprofiles/"
						+ username
						+ "/watches/" + watch.getUserWatchId())).entity(watch).build();
			}
			return Response.ok(service.getUserService().saveWatch(watch)).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@DELETE
	@APIDescription("Removes a Users Watch.")
	@RequireAdmin(UserProfileRequireHandler.class)
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/watches/{watchId}")
	public Response updateWatch(
			@PathParam(UserProfileRequireHandler.USERNAME_ID_PARAM)
			@RequiredParam String userId,
			@PathParam("watchId")
			@RequiredParam String watchId)
	{
		service.getUserService().deleteWatch(watchId);
		return Response.noContent().build();
	}

	// ComponentRESTResource TRACKING section
	@GET
	@RequireAdmin
	@APIDescription("Gets the list of tracking details on a specified user. Always sorts by create date.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserTrackingWrapper.class)
	@Path("/{id}/tracking")
	public Response getComponentTracking(
			@PathParam("id")
			@RequiredParam String userId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		UserTracking userTrackingExample = new UserTracking();
		userTrackingExample.setCreateUser(userId);
		userTrackingExample.setActiveStatus(filterQueryParams.getStatus());

		QueryByExample<UserTracking> queryByExample = new QueryByExample(userTrackingExample);
		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());

		UserTracking userTrackingOrderExample = new UserTracking();
		userTrackingOrderExample.setCreateDts(QueryByExample.DATE_FLAG);
		queryByExample.setOrderBy(userTrackingOrderExample);
		queryByExample.setSortDirection(OpenStorefrontConstant.SORT_DESCENDING);

		List<UserTracking> userTrackings = service.getPersistenceService().queryByExample(UserTracking.class, queryByExample);
		long total = service.getPersistenceService().countByExample(new QueryByExample(QueryType.COUNT, userTrackingExample));
		return sendSingleEntityResponse(new UserTrackingWrapper(userTrackings, total));
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets the tracking details on a specified user and tracking id")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserTracking.class)
	@Path("/{id}/tracking/{trackingId}")
	public Response getUserTracking(
			@PathParam("id")
			@RequiredParam String userId,
			@PathParam("trackingId")
			@RequiredParam String trackingId)
	{
		UserTracking userTrackingExample = new UserTracking();
		userTrackingExample.setCreateUser(userId);
		userTrackingExample.setTrackingId(trackingId);

		UserTracking userTracking = service.getPersistenceService().queryOneByExample(UserTracking.class, userTrackingExample);
		return sendSingleEntityResponse(userTracking);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a tracking entry from the specified user")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/tracking/{trackingId}")
	public void deleteUserTracking(
			@PathParam("id")
			@RequiredParam String userId,
			@PathParam("id")
			@RequiredParam String trackingId)
	{
		UserTracking userTrackingExample = new UserTracking();
		userTrackingExample.setCreateUser(userId);
		userTrackingExample.setTrackingId(trackingId);

		//make sure the that it only remove records for the user
		UserTracking userTracking = service.getPersistenceService().queryOneByExample(UserTracking.class, userTrackingExample);
		if (userTracking != null) {
			service.getPersistenceService().setStatusOnEntity(UserTracking.class, trackingId, UserTracking.INACTIVE_STATUS);
		}
	}

	@POST
	@APIDescription("Add a tracking entry for the specified user")
	@RequireAdmin(UserProfileRequireHandler.class)
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(UserTracking.class)
	@Path("/{id}/tracking")
	public Response addUserTracking(
			@PathParam(UserProfileRequireHandler.USERNAME_ID_PARAM)
			@RequiredParam String userId,
			@RequiredParam UserTracking tracking)
	{
		tracking.setActiveStatus(UserTracking.ACTIVE_STATUS);
		tracking.setCreateUser(userId);
		return saveTracking(tracking, true);
	}

	@PUT
	@RequireAdmin(UserProfileRequireHandler.class)
	@APIDescription("Update a tracking entry for the specified user")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/tracking/{trackingId}")
	public Response updateUserTracking(
			@PathParam(UserProfileRequireHandler.USERNAME_ID_PARAM)
			@RequiredParam String userId,
			@PathParam("trackingId")
			@RequiredParam String trackingId,
			@RequiredParam UserTracking tracking)
	{
		tracking.setActiveStatus(UserTracking.ACTIVE_STATUS);
		tracking.setTrackingId(trackingId);
		tracking.setCreateUser(userId);
		return saveTracking(tracking, false);
	}

	private Response saveTracking(UserTracking tracking, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(tracking);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			tracking.setUpdateUser(SecurityUtil.getCurrentUserName());
			if (post) {
				tracking = service.getUserService().saveUserTracking(tracking);
				String username = StringProcessor.urlEncode(tracking.getCreateUser());
				return Response.created(URI.create("v1/resource/userprofiles/"
						+ username
						+ "/tracking/"
						+ tracking.getTrackingId())).entity(tracking).build();
			} else {
				return Response.ok(service.getUserService().saveUserTracking(tracking)).build();
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}

	}

//  This can be fleshed out in the future when we start keeping better track of what the most recently viewed compnents are
//	@GET
//	@APIDescription("Retrieves Recent Views.  The system keep the 5 most recent.  Sorted by most recent.")
//	@RequireAdmin
//	@Produces({MediaType.APPLICATION_JSON})
//	@Path("/{id}/recentviews")
//	@DataType(UserRecentView.class)
//	public RestListResponse getRecentviews(
//			@PathParam("id")
//			@DefaultValue(DEFAULT_USER)
//			@RequiredParam
//			String userId)
//	{
//		RestListResponse restListResponse = new RestListResponse();
//
//		return restListResponse;
//	}
}
