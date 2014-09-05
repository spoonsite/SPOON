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

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentTracking;
import edu.usu.sdl.openstorefront.storage.model.UserProfile;
import edu.usu.sdl.openstorefront.storage.model.UserTracking;
import edu.usu.sdl.openstorefront.storage.model.UserWatch;
import edu.usu.sdl.openstorefront.util.ServiceUtil;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.model.UserProfileView;
import edu.usu.sdl.openstorefront.web.rest.model.UserWatchView;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *  User Profile Resource
 * @author dshurtleff
 */
@Path("v1/resource/userprofiles")
@APIDescription("A user profile contain information about the user and user specific data. A user profile is created at the time the user logins in.<br>"
		           + "Note: id can be set to \"CURRENTUSER\" to perform operations on the currently logged in user.")
public class UserProfileResource
	extends BaseResource
{
	private static final String DEFAULT_USER = "CURRENTUSER";
	
	@GET
	@APIDescription("Get a list of user profiles")
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserProfileView.class)
	public List<UserProfileView> userProfiles()
	{
		return UserProfileView.toViewList(service.getUserService().getAllProfiles());
	}
//	public RestListResponse userProfiles()
//	{
//		List<UserProfileView> userProfileViews = new ArrayList<>();
//		return sendListResponse(userProfileViews);
//	}
	
	@GET
	@APIDescription("Gets user profile specified by id.")
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserProfileView.class)
	@Path("/{id}")
	public UserProfileView userProfile(
			@PathParam("id") 
			@DefaultValue(DEFAULT_USER)
			@RequiredParam		
			String userId)
	{
		return UserProfileView.toView(service.getUserService().getUserProfile(userId));
	}
	
	@POST
	@APIDescription("Update user profile returns updated profile.")
	@RequireAdmin
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProile(
			@RequiredParam
			UserProfile inputProfile) 
	{
		inputProfile.setActiveStatus(UserProfile.ACTIVE_STATUS);
		ValidationModel validationModel = new ValidationModel(inputProfile);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (inputProfile.getUsername().isEmpty())
		{
			RuleResult result = new RuleResult();
			result.setEntityClassName("User Profile");
			result.setFieldName("username");
			result.setInvalidData(inputProfile.getUsername());
			result.setMessage("The username must be set when posting a new user.");
			result.setValidationRule("username is a NotNull field");
			validationResult.getRuleResults().add(result);
			return Response.ok(validationResult.toRestError()).build();
		}
		else if (validationResult.valid())
		{
			return Response.created(URI.create((service.getUserService().saveUserProfile(inputProfile)).getUsername())).build();
		}
		return Response.ok(validationResult.toRestError()).build();
	}
	
	@PUT
	@APIDescription("Update user profile returns updated profile.")
	@RequireAdmin
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response updateProile(
			@PathParam("id") 
			@DefaultValue(DEFAULT_USER) 
			@RequiredParam		
			String userId,
			@RequiredParam
			UserProfile inputProfile) 
	{
		inputProfile.setActiveStatus(UserProfile.ACTIVE_STATUS);
		inputProfile.setUsername(userId);
		ValidationModel validationModel = new ValidationModel(inputProfile);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid())
		{
			return Response.ok(service.getUserService().saveUserProfile(inputProfile)).build();
		}
		return Response.ok(validationResult.toRestError()).build();
	}
	
	@DELETE
	@APIDescription("Deactivate a profile")
	@RequireAdmin
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}")
	public Response deleteProfile(
			@PathParam("id")
			@RequiredParam
			String userId)
	{
		service.getUserService().deleteProfile(userId);
		return Response.noContent().build();
	}
	
	@GET
	@APIDescription("Retrieves Active User Watches.")
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{id}/watches")
	@DataType(UserWatchView.class)
	public List<UserWatchView> getWatches(
			@PathParam("id") 
			@DefaultValue(DEFAULT_USER) 
			@RequiredParam		
			String userId)
	{
		List<UserWatch> watches = service.getUserService().getWatches(userId);
		List<UserWatchView> views = new ArrayList<>();
		watches.forEach(watch->{
			Component component = service.getPersistenceService().findById(Component.class, watch.getComponentId());
			views.add(UserWatchView.toView(watch, component));
		});
		return views;
	}	
	
	@GET
	@APIDescription("Retrieves an user watch by id.")
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserWatchView.class)
	@Path("/{id}/watches/{watchId}")
	public UserWatchView getWatch(
			@PathParam("id") 
			@DefaultValue(DEFAULT_USER) 
			@RequiredParam		
			String userId,
			@PathParam("watchId") 			
			@RequiredParam		
			String watchId)
	{
		UserWatch watch = service.getUserService().getWatch(watchId);
		Component component = service.getPersistenceService().findById(Component.class, watch.getComponentId());
		return UserWatchView.toView(watch, component);
	}	
	
	@POST
	@APIDescription("Add a new watch to an existing user.")
	@RequireAdmin	
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/watches")
	public Response addWatch(
			@PathParam("id") 
			@DefaultValue(DEFAULT_USER) 
			@RequiredParam		
			String userId, 
			@RequiredParam
			UserWatch userWatch) 
	{
		userWatch.setActiveStatus(UserProfile.ACTIVE_STATUS);
		userWatch.setUsername(userId);
		//TODO: return the location of the created watch
		return saveWatch(userWatch, Boolean.TRUE);
	}
	
	@PUT
	@APIDescription("Update existing new watch. On update: it will update the last view date.")
	@RequireAdmin
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/watches/{watchId}")
	public Response updateWatch(			
			@PathParam("id") 
			@DefaultValue(DEFAULT_USER) 
			@RequiredParam		
			String userId, 
			@PathParam("watchId") 			
			@RequiredParam
			String watchId,
			@RequiredParam
			UserWatch userWatch) 
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
			watch.setCreateUser(ServiceUtil.getCurrentUserName());
			watch.setUpdateUser(ServiceUtil.getCurrentUserName());
			if (post)
			{
				return Response.created(URI.create("v1/resource/userProfile/" + service.getUserService().saveWatch(watch).getUserWatchId())).build();
			}
			return Response.ok(service.getUserService().saveWatch(watch)).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}
	
	@DELETE
	@APIDescription("Removes a Users Watch.")
	@RequireAdmin
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/watches/{watchId}")
	public Response updateWatch(			
			@PathParam("id") 
			@DefaultValue(DEFAULT_USER) 
			@RequiredParam
			String userId,
			@PathParam("watchId") 			
			@RequiredParam		
			String watchId) 
	{
		service.getUserService().deleteWatch(watchId);
		return Response.noContent().build();
	}	
	
	
	
	// ComponentRESTResource TRACKING section
	@GET
	@RequireAdmin
	@APIDescription("Get the list of tracking details on a specified entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserTracking.class)
	@Path("/{id}/tracking")
	public List<UserTracking> getComponentTracking(
			@PathParam("id")
			@RequiredParam String userId)
	{
		return service.getUserService().getBaseEntityByCreateUser(UserTracking.class, userId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a tracking entry from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/tracking/{trackingId}")
	public void deleteComponentTracking(
			@PathParam("id")
			@RequiredParam
			String componentId,
			@PathParam("id")
			@RequiredParam
			String trackingId)
	{
		service.getUserService().deactivateBaseEntity(UserTracking.class, trackingId);
	}

	@POST
	@APIDescription("Add a tracking entry for the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(UserTracking.class)
	@Path("/{id}/tracking")
	public Response addComponentTracking(
			@PathParam("id")
			@RequiredParam String userId,
			@RequiredParam UserTracking tracking)
	{
		tracking.setActiveStatus(UserTracking.ACTIVE_STATUS);
		tracking.setCreateUser(userId);
		return saveTracking(tracking, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update a tracking entry for the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/tracking/{trackingId}")
	public Response updateComponentTracking(
			@PathParam("id")
			@RequiredParam
			String userId,
			@PathParam("trackingId")
			@RequiredParam
			String trackingId,
			@RequiredParam
			UserTracking tracking)
	{
		tracking.setActiveStatus(ComponentTracking.ACTIVE_STATUS);
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
			tracking.setUpdateUser(ServiceUtil.getCurrentUserName());
			if (post) {
				// TODO: How does this work with composite keys?
				return Response.created(URI.create(service.getUserService().saveUserTracking(tracking).getTrackingId())).build();
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
