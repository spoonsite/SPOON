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
package edu.usu.sdl.apiclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author ccummings
 */
public class ClientAPI
{

	private static Logger log = Logger.getLogger(AbstractService.class.getName());

	private static final String MEDIA_TYPE_JSON = "application/json";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String CSRF_TOKEN = "X-Csrf-Token";
	private LoginModel loginModel;

	private CloseableHttpClient httpclient;
	private BasicCookieStore cookieStore;
	private boolean connected = false;

	private final ObjectMapper objectMapper;

	public ClientAPI(ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
	}

	public ClientAPI()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void connect(String username, String password, String serverURL)
	{

		if (!isConnected()) {
			loginModel = new LoginModel();
			loginModel.setServerUrl(serverURL);
			loginModel.setSecurityUrl(serverURL + "/Login.action?Login");
			loginModel.setLogoffUrl(serverURL + "/Login.action?Logout");

			loginModel.setUsernameField("username");
			loginModel.setUsername(username);

			loginModel.setPasswordField("password");
			loginModel.setPassword(password);

			connect(loginModel);
		}
	}
	
	private void connect(LoginModel loginModel)
	{
		if (httpclient == null) {
			cookieStore = new BasicCookieStore();
			httpclient = HttpClients.custom()
					.setDefaultCookieStore(cookieStore)
					.build();

			logon(loginModel);
			connected = true;
		}
	}
	
	public boolean isConnected()
	{
		return connected;
	}

	private void logon(LoginModel loginModel)
	{
		Objects.requireNonNull(loginModel.getServerUrl(), "Server url not provided");
		Objects.requireNonNull(loginModel.getSecurityUrl(), "Security url not provided");
		this.loginModel = loginModel;
		//get the initial cookies
		HttpGet httpget = new HttpGet(loginModel.getServerUrl());
		try (CloseableHttpResponse response = httpclient.execute(httpget)) {
			HttpEntity entity = response.getEntity();

			log.log(Level.FINE, "Login form get: {0}", response.getStatusLine());
			EntityUtils.consume(entity);

			log.log(Level.FINEST, "Initial set of cookies:");
			List<Cookie> cookies = cookieStore.getCookies();
			if (cookies.isEmpty()) {
				log.log(Level.FINEST, "None");
			} else {
				for (Cookie cookie : cookies) {
					log.log(Level.FINEST, "- {0}", cookie.toString());
				}
			}
		} catch (IOException ex) {
			throw new ConnectionException("Unable to Connect.", ex);
		}

		//login
		try {
			HttpUriRequest login = RequestBuilder.post()
					.setUri(new URI(loginModel.getSecurityUrl()))
					.addParameter(loginModel.getUsernameField(), loginModel.getUsername())
					.addParameter(loginModel.getPasswordField(), loginModel.getPassword())
					.build();
			try (CloseableHttpResponse response = httpclient.execute(login)) {
				HttpEntity entity = response.getEntity();

				log.log(Level.FINE, "Login form get: {0}", response.getStatusLine());
				EntityUtils.consume(entity);

				log.log(Level.FINEST, "Post logon cookies:");
				List<Cookie> cookies = cookieStore.getCookies();
				if (cookies.isEmpty()) {
					log.log(Level.FINEST, "None");
				} else {
					for (Cookie cookie : cookies) {
						log.log(Level.FINEST, "- {0}", cookie.toString());
					}
				}
			}

			//For some reason production requires getting the first page first
			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setCircularRedirectsAllowed(true).build();

			HttpUriRequest data = RequestBuilder.get()
					.setUri(new URI(loginModel.getServerUrl()))
					.addHeader(CONTENT_TYPE, MEDIA_TYPE_JSON)
					.setConfig(defaultRequestConfig)
					.build();

			try (CloseableHttpResponse response = httpclient.execute(data)) {
				log.log(Level.FINE, "Response Status from connection: {0}  {1}", new Object[]{response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase()});
				HttpEntity entity1 = response.getEntity();
				EntityUtils.consume(entity1);
			}

		} catch (IOException | URISyntaxException ex) {
			throw new ConnectionException("Unable to login.", ex);
		}
	}

	/**
	 * Always disconnect when done calling services
	 */
	public void disconnect()
	{
		if (httpclient != null) {
			logoff();
			try {
				httpclient.close();
				httpclient = null;
				connected = false;
			} catch (IOException ex) {
				log.log(Level.SEVERE, "Unable to close client", ex);
			}
		}
	}

	protected void logoff()
	{
		if (connected) {
			HttpGet httpget = new HttpGet(loginModel.getLogoffUrl());
			try (CloseableHttpResponse response = httpclient.execute(httpget)) {
				HttpEntity entity = response.getEntity();
				EntityUtils.consume(entity);
			} catch (IOException ex) {
				throw new ConnectionException("Unable to Connect to Logoff.", ex);
			}
		}
	}
	
	public Map<String,String>  translateFilterQueryParams(FilterQueryParams params)
	{
		try {
			return BeanUtils.describe(params);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException exs) {
			throw new RuntimeException(exs);
		}
	}

	public APIResponse httpGet(String apiPath, Map<String, String> parameters)
	{
		APIResponse response = null;
		try {
			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setCircularRedirectsAllowed(true).build();

			RequestBuilder builder = RequestBuilder.get()
					.setUri(new URI(loginModel.getServerUrl() + apiPath))
					.addHeader(CONTENT_TYPE, MEDIA_TYPE_JSON)
					.setConfig(defaultRequestConfig);

			if (parameters != null) {
				for (String key : parameters.keySet()) {
					builder.addParameter(key, parameters.get(key));
				}
			}
			HttpUriRequest request = builder.build();

			try (CloseableHttpResponse httpResponse = httpclient.execute(request)) {
				response = new APIResponse(objectMapper);
				response.setResponseCode(httpResponse.getStatusLine().getStatusCode());
				HttpEntity entity1 = httpResponse.getEntity();

				StringBuilder data = new StringBuilder();
				try (BufferedReader in = new BufferedReader(new InputStreamReader(entity1.getContent()))) {
					in.lines().forEach(line -> {
						data.append(line).append("\n");
					});
				}
				response.setResponseBody(data.toString());
				EntityUtils.consume(entity1);
			}

		} catch (IOException | URISyntaxException ex) {
			throw new ConnectionException("Unable to Connect.", ex);
		}

		return response;
	}
	
	private String getCookieValue(String cookieKey)
	{
		String value = null;
		
		List<Cookie> cookies = cookieStore.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieKey)) {
				value = cookie.getValue();
			}
		}
		return value;
	}

	public <T extends Object> APIResponse httpPost(String apiPath, T value, Map<String, String> parameters)
	{
		APIResponse response = null;
		try {
			String entityRawValue = objectMapper.writeValueAsString(value);
			StringEntity entity = new StringEntity(entityRawValue);

			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setCircularRedirectsAllowed(true).build();
			
			RequestBuilder builder = RequestBuilder.post()
					.setUri(new URI(loginModel.getServerUrl() + apiPath))
					.addHeader(CONTENT_TYPE, MEDIA_TYPE_JSON)
					.addHeader(CSRF_TOKEN, getCookieValue(CSRF_TOKEN))
					.setConfig(defaultRequestConfig)
					.setEntity(entity);

			if (parameters != null) {
				for (String key : parameters.keySet()) {
					builder.addParameter(key, parameters.get(key));
				}
			}
			HttpUriRequest request = builder.build();

			try (CloseableHttpResponse httpResponse = httpclient.execute(request)) {
				response = new APIResponse(objectMapper);
				response.setResponseCode(httpResponse.getStatusLine().getStatusCode());
				HttpEntity entity1 = httpResponse.getEntity();

				StringBuilder data = new StringBuilder();
				try (BufferedReader in = new BufferedReader(new InputStreamReader(entity1.getContent()))) {
					in.lines().forEach(line -> {
						data.append(line).append("\n");
					});
				}
				response.setResponseBody(data.toString());
				EntityUtils.consume(entity1);
			}

		} catch (IOException | URISyntaxException ex) {
			throw new ConnectionException("Unable to Connect.", ex);
		}

		return response;
	}

	public void httpDelete(String apiPath, Map<String, String> parameters)
	{
		APIResponse response = null;
		try {
			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setCircularRedirectsAllowed(true).build();

			RequestBuilder builder = RequestBuilder.delete()
					.setUri(new URI(loginModel.getServerUrl() + apiPath))
					.addHeader(CONTENT_TYPE, MEDIA_TYPE_JSON)
					.addHeader(CSRF_TOKEN, getCookieValue(CSRF_TOKEN))
					.setConfig(defaultRequestConfig);

			if (parameters != null) {
				for (String key : parameters.keySet()) {
					builder.addParameter(key, parameters.get(key));
				}
			}
			HttpUriRequest request = builder.build();

			try (CloseableHttpResponse httpResponse = httpclient.execute(request)) {
				response = new APIResponse(objectMapper);
				response.setResponseCode(httpResponse.getStatusLine().getStatusCode());
				HttpEntity entity1 = httpResponse.getEntity();

				if (entity1 != null) {
					StringBuilder data = new StringBuilder();
					try (BufferedReader in = new BufferedReader(new InputStreamReader(entity1.getContent()))) {
						in.lines().forEach(line -> {
							data.append(line).append("\n");
						});
					}
					response.setResponseBody(data.toString());
					EntityUtils.consume(entity1);
				}
			}

		} catch (IOException | URISyntaxException ex) {
			throw new ConnectionException("Unable to Connect.", ex);
		}
	}

	public <T extends Object> APIResponse httpPut(String apiPath, T value, Map<String, String> parameters)
	{
		String requestData = null;
		APIResponse response = null;
		try {

			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setCircularRedirectsAllowed(true).build();

			RequestBuilder builder = RequestBuilder.put()
					.setUri(new URI(loginModel.getServerUrl() + apiPath))
					.addHeader(CONTENT_TYPE, MEDIA_TYPE_JSON)
					.addHeader(CSRF_TOKEN, getCookieValue(CSRF_TOKEN))
					.setConfig(defaultRequestConfig);

			if (value != null) {
				if (value.getClass().equals(String.class)) {
					requestData = value.toString();
				} else {
					requestData = objectMapper.writeValueAsString(value);
				}

				StringEntity entity = new StringEntity(requestData);

				builder.setEntity(entity);
			}
			
			if (parameters != null) {
				for (String key : parameters.keySet()) {
					builder.addParameter(key, parameters.get(key));
				}
			}
			HttpUriRequest request = builder.build();

			try (CloseableHttpResponse httpResponse = httpclient.execute(request)) {
				response = new APIResponse(objectMapper);
				response.setResponseCode(httpResponse.getStatusLine().getStatusCode());
				HttpEntity entity1 = httpResponse.getEntity();

				StringBuilder data = new StringBuilder();
				try (BufferedReader in = new BufferedReader(new InputStreamReader(entity1.getContent()))) {
					in.lines().forEach(line -> {
						data.append(line).append("\n");
					});
				}
				response.setResponseBody(data.toString());
				EntityUtils.consume(entity1);
			}

		} catch (IOException | URISyntaxException ex) {
			throw new ConnectionException("Unable to Connect.", ex);
		}

		return response;
	}

}
