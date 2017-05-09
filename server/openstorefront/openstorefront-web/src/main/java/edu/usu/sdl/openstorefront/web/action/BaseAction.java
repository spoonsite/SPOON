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
package edu.usu.sdl.openstorefront.web.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.view.JsonFormLoad;
import edu.usu.sdl.openstorefront.core.view.JsonResponse;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.core.MediaType;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Base Action Handler
 *
 * @author dshurtleff
 */
public abstract class BaseAction
		implements ActionBean, ValidationErrorHandler
{

	private static final long MAX_UPLOAD_SIZE = 1048576000L;

	private static final Logger LOG = Logger.getLogger(BaseAction.class.getName());

	protected ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();

	protected ActionBeanContext context;
	protected String projectId;

	protected final ServiceProxy service = new ServiceProxy();

	public String getApplicationVersion()
	{
		return PropertiesManager.getApplicationVersion();
	}

	protected String getCookieValue(String key)
	{
		String value = null;

		Cookie cookies[] = getContext().getRequest().getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(key)) {
					value = cookie.getValue();
				}
			}
		}

		return value;
	}

	protected boolean doesFileExceedLimit(FileBean fileBean)
	{
		return doesFileExceedLimit(fileBean, MAX_UPLOAD_SIZE);
	}

	protected boolean doesFileExceedLimit(FileBean fileBean, long limit)
	{
		boolean exceeds = false;

		if (fileBean != null) {
			if (fileBean.getSize() > limit) {
				exceeds = true;
			}
		}
		return exceeds;
	}

	protected String getPageOutput(String pathToPage)
	{
		HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(getContext().getResponse())
		{
			private final StringWriter sw = new StringWriter();

			@Override
			public PrintWriter getWriter() throws IOException
			{
				return new PrintWriter(sw);
			}

			@Override
			public String toString()
			{
				return sw.toString();
			}
		};
		try {
			getContext().getRequest().getRequestDispatcher(pathToPage).include(getContext().getRequest(), responseWrapper);
		} catch (ServletException | IOException ex) {
			LOG.log(Level.SEVERE, "Unable to find page", ex);
		}
		String content = responseWrapper.toString();
		return content;
	}

	protected void deleteUploadFile(FileBean fileBean)
	{
		try {
			fileBean.delete();
		} catch (IOException ex) {
			LOG.log(Level.WARNING, "Unable to remove temp upload file.", ex);
		}
	}

	protected void mapFields(Map<String, Object> fields, Object data, String propertyRoot)
	{
		try {
			Map fieldMap = BeanUtils.describe(data);
			for (Object field : fieldMap.keySet()) {
				if ("class".equalsIgnoreCase(field.toString()) == false) {
					fields.put(propertyRoot + "." + field, fieldMap.get(field));
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
			LOG.log(Level.SEVERE, null, ex);
		}
	}

	protected Resolution streamFormLoad(Map<String, Object> fields)
	{
		JsonFormLoad jsonFormLoad = new JsonFormLoad();
		jsonFormLoad.setData(fields);
		return formLoadResults(jsonFormLoad);
	}

	protected Resolution formLoadResults(final JsonFormLoad data)
	{
		return new StreamingResolution(MediaType.APPLICATION_JSON)
		{

			@Override
			protected void stream(HttpServletResponse response) throws Exception
			{
				objectMapper.writeValue(response.getOutputStream(), data);
			}
		};
	}

	protected <T> Resolution streamResults(List<T> data)
	{
		return streamResults(data, null);
	}

	protected <T> Resolution streamResults(List<T> data, String contentType)
	{
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setData(data);
		jsonResponse.setTotalResults(data.size());
		jsonResponse.setResults(data.size());

		return streamResults(jsonResponse, contentType);
	}

	protected Resolution streamResults(Object data)
	{
		return streamResults(data, null);
	}

	protected Resolution streamResults(Object data, String contentType)
	{
		if (StringUtils.isBlank(contentType)) {
			contentType = MediaType.APPLICATION_JSON;
		}
		return new StreamingResolution(contentType)
		{

			@Override
			protected void stream(HttpServletResponse response) throws Exception
			{
				objectMapper.writeValue(response.getOutputStream(), data);
			}
		};
	}

	protected Resolution streamResults(final JsonResponse jsonResponse)
	{
		return streamResults(jsonResponse, null);
	}

	protected Resolution streamResults(final JsonResponse jsonResponse, String contentType)
	{
		if (StringUtils.isBlank(contentType)) {
			contentType = MediaType.APPLICATION_JSON;
		}
		return new StreamingResolution(contentType)
		{

			@Override
			protected void stream(HttpServletResponse response) throws Exception
			{
				objectMapper.writeValue(response.getOutputStream(), jsonResponse);
			}
		};
	}

	protected Resolution streamErrorResponse(Map<String, String> errors)
	{
		return streamErrorResponse(errors, false);
	}

	protected Resolution streamUploadResponse(Map<String, String> errors)
	{
		return streamErrorResponse(errors, true);
	}

	protected Resolution streamErrorResponse(Map<String, String> errors, boolean upload)
	{
		JsonResponse jsonResponse = new JsonResponse();
		if (errors != null && errors.size() > 0) {
			jsonResponse.setSuccess(false);
			jsonResponse.setErrors(errors);
		}
		return handleErrorResponse(jsonResponse, upload);
	}

	private Resolution handleErrorResponse(final JsonResponse jsonResponse, boolean upload)
	{
		String contentType = MediaType.APPLICATION_JSON;
		if (upload) {
			contentType = MediaType.TEXT_HTML;
		}

		return new StreamingResolution(contentType)
		{

			@Override
			protected void stream(HttpServletResponse response) throws Exception
			{
				objectMapper.writeValue(response.getOutputStream(), jsonResponse);
			}
		};
	}

	@Override
	public Resolution handleValidationErrors(ValidationErrors ve) throws Exception
	{
		Map<String, String> errors = new HashMap<>();

		if (ve.hasFieldErrors()) {
			for (String errorkey : ve.keySet()) {
				List<ValidationError> validationErrors = ve.get(errorkey);
				for (ValidationError validationError : validationErrors) {
					errors.put(validationError.getFieldName(), "Field is required or Format not accepted.");
				}
			}
		} else {
			//only global?
			for (String errorkey : ve.keySet()) {
				List<ValidationError> validationErrors = ve.get(errorkey);
				for (ValidationError validationError : validationErrors) {
					LOG.log(Level.INFO, "(Validation) Global Error Key: {0} Message: {1} Action Path: {2}", new Object[]{errorkey, validationError.toString(), validationError.getActionPath()});
				}
			}
		}

		return streamErrorResponse(errors);
	}

	@Override
	public void setContext(ActionBeanContext abc)
	{
		context = abc;
	}

	@Override
	public ActionBeanContext getContext()
	{
		return context;
	}

	public String getProjectId()
	{
		return projectId;
	}

	public void setProjectId(String projectId)
	{
		this.projectId = projectId;
	}

}
