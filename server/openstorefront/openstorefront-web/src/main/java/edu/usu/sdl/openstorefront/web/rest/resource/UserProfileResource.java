/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import au.com.bytecode.opencsv.CSVWriter;
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
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserTracking;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import edu.usu.sdl.openstorefront.core.entity.UserWatch;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.core.view.UserProfileView;
import edu.usu.sdl.openstorefront.core.view.UserProfileWrapper;
import edu.usu.sdl.openstorefront.core.view.UserTrackingWrapper;
import edu.usu.sdl.openstorefront.core.view.UserWatchView;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.security.UserProfileRequireHandler;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sourceforge.stripes.util.bean.BeanUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * User Profile Resource
 *
 */
@Path("v1/resource/userprofiles")
@APIDescription("A user profile contains information about the user and user specific data. A user profile is created at the time the user logs in.<br>"
		+ "Note: id if set to the current user will not require admin rights.")
public class UserProfileResource
		extends BaseResource
{

	private static final Logger LOG = Logger.getLogger(UserProfileResource.class.getName());

	@GET
	@APIDescription("Get a list of user profiles")
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT_PROFILES)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserProfileWrapper.class)
	public Response userProfiles(@BeanParam FilterQueryParams filterQueryParams,
			@QueryParam("searchField") String searchField,
			@QueryParam("searchValue") String searchValue)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		// Initialize User Profile Status Example
		UserProfile userProfileExample = new UserProfile();
		QueryByExample queryByExample = new QueryByExample(userProfileExample);

		// Check For 'All' Parameter
		if (!filterQueryParams.getAll()) {

			userProfileExample.setActiveStatus(filterQueryParams.getStatus());
		}

		// Check For Search Parameters
		if (searchField != null && searchValue != null) {

			// Initialize User Profile Status Example
			UserProfile userProfileSearchExample = new UserProfile();

			try {
				BeanUtils.setProperty(userProfileSearchExample, searchField, searchValue.toLowerCase() + "%");

				// Define A Special Lookup Operation (LIKE)
				// (The Default Is Equals, Which We Still Need For Active Status)
				SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
				specialOperatorModel.setExample(userProfileSearchExample);
				specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LIKE);
				specialOperatorModel.getGenerateStatementOption().setMethod(GenerateStatementOption.METHOD_LOWER_CASE);
				queryByExample.getExtraWhereCauses().add(specialOperatorModel);

			} catch (IllegalAccessException | InvocationTargetException ex) {
				LOG.log(Level.WARNING, MessageFormat.format("Unable to search user profiles by field: {0}", searchField));
			}
		}

		UserProfile userProfileStartExample = new UserProfile();
		userProfileStartExample.setCreateDts(filterQueryParams.getStart());

		UserProfile userProfileEndExample = new UserProfile();
		userProfileEndExample.setCreateDts(filterQueryParams.getEnd());

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

		List<UserProfile> userProfiles = service.getPersistenceService().queryByExample(queryByExample);

		UserProfileWrapper userProfileWrapper = new UserProfileWrapper();
		userProfileWrapper.getData().addAll(UserProfileView.toViewList(userProfiles));
		userProfileWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));

		return sendSingleEntityResponse(userProfileWrapper);
	}

	@GET
	@APIDescription("Get a list of active user profiles for lookup")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/lookup")
	public Response userProfilesLookup()
	{
		List<LookupModel> profiles = new ArrayList<>();

		UserProfile userProfileExample = new UserProfile();
		userProfileExample.setActiveStatus(UserProfile.ACTIVE_STATUS);
		List<UserProfile> userProfiles = userProfileExample.findByExample();
		for (UserProfile userProfile : userProfiles) {
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(userProfile.getUsername());
			String name = userProfile.getUsername();
			if (StringUtils.isNotBlank(userProfile.getFirstName())) {
				name = userProfile.getFirstName() + ", " + userProfile.getLastName();
			}
			String email = StringUtils.isNotBlank(userProfile.getEmail()) ? " (" + userProfile.getEmail() + ")" : "";
			lookupModel.setDescription(name + email);
			profiles.add(lookupModel);
		}

		GenericEntity<List<LookupModel>> entity = new GenericEntity<List<LookupModel>>(profiles)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets the current user profile from the session. Includes permissions.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserProfileView.class)
	@Path("/currentuser")
	public Response getCurrentUser()
	{
		UserContext userContext = SecurityUtil.getUserContext();
		UserProfileView userProfileView = UserProfileView.toView(userContext);
		return sendSingleEntityResponse(userProfileView);
	}

	@GET
	@APIDescription("Gets user profile specified by id.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserProfileView.class)
	@Path("/{id}")
	public UserProfileView userProfile(
			@PathParam(UserProfileRequireHandler.USERNAME_ID_PARAM)
			@RequiredParam String userId)
	{
		UserProfileView userProfileView = null;

		if (SecurityUtil.getCurrentUserName().equals(userId)
				|| SecurityUtil.hasPermission(SecurityPermission.ADMIN_USER_MANAGEMENT_PROFILES)) {

			UserProfile userProfile = service.getUserService().getUserProfile(userId);
			if (userProfile != null) {
				userProfileView = UserProfileView.toView(userProfile);
			}
		}
		return userProfileView;
	}

	@POST
	@APIDescription("Update user profile and returns updated profile.")
	@RequireSecurity(value = {SecurityPermission.ADMIN_USER_MANAGEMENT_PROFILES}, specialCheck = UserProfileRequireHandler.class)
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
	@APIDescription("Updates user profile and returns updated profile.")
	@RequireSecurity(value = {SecurityPermission.ADMIN_USER_MANAGEMENT_PROFILES}, specialCheck = UserProfileRequireHandler.class)
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
	@APIDescription("Inactivates a user profile.  Note: if the user logs in, their profile will be reactivated.")
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT_PROFILES)
	@Path("/{username}")
	public void deleteUserProfile(
			@PathParam("username")
			@RequiredParam String username)
	{
		service.getUserService().deleteProfile(username);
	}

	@DELETE
	@APIDescription("Inactivates a list of user profiles. Takes a list of usernames(Strings) as params.")
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT_PROFILES)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/multiple")
	public void deleteUserProfiles(
			@RequiredParam
			@DataType(String.class) List<String> usernames)
	{

		for (String username : usernames) {
			service.getUserService().deleteProfile(username);
		}

	}

	@PUT
	@APIDescription("Reactivates a list of user profiles. Takes a list of usernames(Strings) as params.")
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT_PROFILES)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/multiple")
	public void reactivateUserProfiles(
			@RequiredParam
			@DataType(String.class) List<String> usernames)
	{

		for (String username : usernames) {
			service.getUserService().reactiveProfile(username);
		}

	}

	@PUT
	@APIDescription("Reactivates a user profile.")
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT_PROFILES)
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
	@APIDescription("Sends test email to user based on user id")
	@RequireSecurity(value = {SecurityPermission.ADMIN_USER_MANAGEMENT_PROFILES}, specialCheck = UserProfileRequireHandler.class)
	@Path("/{id}/test-email")
	public Response sendTestEmail(
			@PathParam(UserProfileRequireHandler.USERNAME_ID_PARAM) String username,
			String email
	)
	{
		service.getUserService().sendTestEmail(username, email);
		return Response.ok().build();
	}

	@GET
	@APIDescription("Retrieves active user watches.")
	@RequireSecurity(value = {SecurityPermission.ADMIN_WATCHES}, specialCheck = UserProfileRequireHandler.class)
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
	@APIDescription("Retrieves a user watch by id.")
	@RequireSecurity(value = {SecurityPermission.ADMIN_WATCHES}, specialCheck = UserProfileRequireHandler.class)
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
	@RequireSecurity(value = {SecurityPermission.ADMIN_WATCHES}, specialCheck = UserProfileRequireHandler.class)
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
	@APIDescription("Updates an existing watch.")
	@RequireSecurity(value = {SecurityPermission.ADMIN_WATCHES}, specialCheck = UserProfileRequireHandler.class)
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
	@APIDescription("Removes a user watch.")
	@RequireSecurity(value = {SecurityPermission.ADMIN_WATCHES}, specialCheck = UserProfileRequireHandler.class)
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/watches/{watchId}")
	public Response deleteWatch(
			@PathParam(UserProfileRequireHandler.USERNAME_ID_PARAM)
			@RequiredParam String userId,
			@PathParam("watchId")
			@RequiredParam String watchId)
	{
		service.getUserService().deleteWatch(watchId);
		return Response.noContent().build();
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING)
	@APIDescription("Gets the list of tracking details on a specified user. Always sorts by create date.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserTrackingWrapper.class)
	@Path("/{id}/tracking")
	public Response getUserTracking(
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

		List<UserTracking> userTrackings = service.getPersistenceService().queryByExample(queryByExample);
		long total = service.getPersistenceService().countByExample(new QueryByExample(QueryType.COUNT, userTrackingExample));
		return sendSingleEntityResponse(new UserTrackingWrapper(userTrackings, total));
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING)
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

		UserTracking userTracking = service.getPersistenceService().queryOneByExample(userTrackingExample);
		return sendSingleEntityResponse(userTracking);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING)
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
		UserTracking userTracking = service.getPersistenceService().queryOneByExample(userTrackingExample);
		if (userTracking != null) {
			service.getPersistenceService().setStatusOnEntity(UserTracking.class, trackingId, UserTracking.INACTIVE_STATUS);
		}
	}

	@POST
	@APIDescription("Add a tracking entry for the specified user")
	@RequireSecurity(value = SecurityPermission.ADMIN_TRACKING, specialCheck = UserProfileRequireHandler.class)
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
	@RequireSecurity(value = SecurityPermission.ADMIN_TRACKING, specialCheck = UserProfileRequireHandler.class)
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

	@POST
	@APIDescription("Exports user profiles in CSV format. Can consume a list of 'userId' form parameters. Not providing 'userId' parameters results in all profiles being exported.")
	@RequireSecurity(SecurityPermission.ADMIN_USER_MANAGEMENT_PROFILES)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/export")
	public Response exportUserProfiles(
			@FormParam("userId")
			@RequiredParam List<String> userIds)
	{

		// Declare List Of User Profiles
		// (Instantiated Later)
		List<UserProfile> userProfiles;

		// Initialize CSV Line Storage
		List<String[]> lines = new ArrayList<>();

		// Set DateTime Format
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

		// Initialize String Writer
		// (Stores CSV Data)
		StringWriter stringWriter = new StringWriter();

		// Initialize CSV Writer
		// (Processes CSV Data)
		CSVWriter writer = new CSVWriter(stringWriter);

		// Add CSV Headers To List Of Lines
		lines.add(new String[]{
			"Active Status",
			"Username",
			"First Name",
			"Last Name",
			"Organization",
			"User Type",
			"Last Login",
			"Email",
			"Phone",
			"Send Change Emails",
			"GUID",
			"Created Date",
			"Updated Date"
		});

		// Check For Provided User IDs
		if (userIds != null && userIds.size() > 0) {

			// Instantiate User Profile List
			userProfiles = new ArrayList<>();

			// Loop Through Provided User IDs
			for (String userId : userIds) {

				// Retrieve User Profile
				UserProfile userProfile = service.getUserService().getUserProfile(userId);

				// Add User Profile To List
				userProfiles.add(userProfile);
			}
		} else {

			// Get All User Profiles
			userProfiles = service.getUserService().getAllProfiles(Boolean.TRUE);
		}

		// Get User Profile Views
		// (Provides Additional Detail Over Profiles)
		List<UserProfileView> userProfileViews = UserProfileView.toViewList(userProfiles);

		// Loop Through User Profile Views
		for (UserProfileView userProfileView : userProfileViews) {

			////////////////////////////
			// Handle Potential NULLs //
			////////////////////////////
			// Store Notify Of New Value
			Boolean notifyOfNew = userProfileView.getNotifyOfNew();

			// Check For Null (Notify Of New)
			if (notifyOfNew == null) {

				// Initialize As False
				notifyOfNew = false;
			}

			// Store Last Login Value
			Date lastLoginDate = userProfileView.getLastLoginDts();

			// Initialize Last Login String
			String lastLoginString;

			// Check For Null (Last Login)
			if (lastLoginDate == null) {

				// Set Last Login As Empty String
				lastLoginString = "";
			} else {

				// Set Last Login As Formatted DateTime
				lastLoginString = df.format(lastLoginDate);
			}

			/////////////////////////
			// End Potential NULLs //
			/////////////////////////
			// Add CSV Row To List Of Lines
			lines.add(new String[]{
				userProfileView.getActiveStatus(),
				userProfileView.getUsername(),
				userProfileView.getFirstName(),
				userProfileView.getLastName(),
				userProfileView.getOrganization(),
				TranslateUtil.translate(UserTypeCode.class, userProfileView.getUserTypeCode()),
				lastLoginString,
				userProfileView.getEmail(),
				userProfileView.getPhone(),
				notifyOfNew ? "true" : "false",
				userProfileView.getGuid(),
				df.format(userProfileView.getCreateDts()),
				df.format(userProfileView.getUpdateDts())
			});
		}

		// Write Entire CSV
		writer.writeAll(lines);

		// Initialize Response
		// (Set CSV Data & Proper Headers)
		Response.ResponseBuilder response = Response.ok(stringWriter.toString());
		response.header("Content-Type", "application/csv");
		response.header("Content-Disposition", "attachment; filename=\"userProfileExport.csv\"");

		// Return Response
		return response.build();
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
