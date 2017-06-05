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
package edu.usu.sdl.apiclient.rest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.APIResponse;
import edu.usu.sdl.apiclient.AbstractService;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.LoggerView;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.core.view.MediaRetrieveRequestModel;
import edu.usu.sdl.openstorefront.core.view.SystemStatusView;
import edu.usu.sdl.openstorefront.core.view.ThreadStatus;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import javax.ws.rs.core.Response;

/**
 *
 * @author ccummings
 */
public class ApplicationImpl
		extends AbstractService
{

	String basePath = "api/v1/service/application/";

	public ApplicationImpl(ClientAPI client)
	{
		super(client);
	}

	public ApplicationImpl()
	{
		this(new ClientAPI(new ObjectMapper()));
	}

	public LookupModel addConfigProperty(LookupModel lookupModel)
	{
		APIResponse response = client.httpPost(basePath + "configproperties", lookupModel, null);
		lookupModel = response.getResponse(LookupModel.class);
		return lookupModel;
	}

	public void clearAllDBLogs()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response flushCaches(String cacheName)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getApplicationStatus()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getCaches()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<LookupModel> getConfigProperties()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public LookupModel getConfigPropertiesForKey(String key)
	{
		APIResponse response = client.httpGet(basePath + "configproperties/" + key, null);
		LookupModel lookupModel = response.getResponse(LookupModel.class);
		return lookupModel;
	}

	public Response getLogFile(FilterQueryParams filterQueryParams)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<LookupModel> getLogLevels()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<LoggerView> getLoggers()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getManagers()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public LookupModel getShowFeedBack()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public SystemStatusView getSystemStatus()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getThreadStack(long threadId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<ThreadStatus> getThreads()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void removeConfigProperties(String key)
	{
		client.httpDelete(basePath + "configproperties/" + key, null);
	}

	public void restartApplication(String managerClass)
	{
		APIResponse response = client.httpPost(basePath + "restart", managerClass, null);
	}

	public Response restartManager(String managerClass)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response retrieveMedia(MediaRetrieveRequestModel retrieveRequest) throws MalformedURLException, IOException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response startManager(String managerClass)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response stopManager(String managerClass)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response toKey(String label)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public LoggerView updateApplicationProperty(String loggername, String level)
	{
		APIResponse response = client.httpPut(basePath + "logger/" + loggername + "/level", level, null);
		LoggerView logView = response.getResponse(LoggerView.class);
		
		return logView;
	}

	public Response updateConfigProperty(String use)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
