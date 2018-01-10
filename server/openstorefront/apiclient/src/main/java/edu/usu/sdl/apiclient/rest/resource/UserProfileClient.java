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
package edu.usu.sdl.apiclient.rest.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.APIResponse;
import edu.usu.sdl.apiclient.AbstractService;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserTracking;
import edu.usu.sdl.openstorefront.core.entity.UserWatch;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.UserProfileView;
import edu.usu.sdl.openstorefront.core.view.UserProfileWrapper;
import edu.usu.sdl.openstorefront.core.view.UserWatchView;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;

/**
 *
 * @author ccummings
 */
public class UserProfileClient extends AbstractService
{

	String basePath = "api/v1/resource/userprofiles";
	
	public UserProfileClient(ClientAPI client)
	{
		super(client);
	}
	
	public UserProfileClient()
	{
		this(new ClientAPI(new ObjectMapper()));
	}

	public Response addUserTracking(String userId, UserTracking tracking)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response addWatch(String userId, UserWatch userWatch)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void deleteUserProfile(String username)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void deleteUserProfiles(List<String> usernames)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void deleteUserTracking(String userId, String trackingId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response deleteWatch(String userId, String watchId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response exportUserProfiles(List<String> userIds)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getCurrentUser()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getUserTracking(String userId, FilterQueryParams filterQueryParams)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getUserTracking(String userId, String trackingId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public UserWatchView getWatch(String userId, String watchId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<UserWatchView> getWatches(String userId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response reactivateUserProfile(String username)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void reactivateUserProfiles(List<String> usernames)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response sendTestEmail(String username, String email)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response updateProfile(String userId, UserProfile inputProfile)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response updateProile(UserProfile inputProfile)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response updateUserTracking(String userId, String trackingId, UserTracking tracking)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response updateWatch(String userId, String watchId, UserWatch userWatch)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public UserProfileView userProfile(String userId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public UserProfileWrapper userProfiles(FilterQueryParams filterQueryParams, String searchField, String searchValue)
	{
		Map<String, String> parameters = client.translateFilterQueryParams(filterQueryParams);
		parameters.put("searchField", searchField);
		parameters.put("searchValue",searchValue);
		APIResponse response = client.httpGet(basePath, parameters);
		return response.getResponse(UserProfileWrapper.class);
	}

	public Response userProfilesLookup()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
