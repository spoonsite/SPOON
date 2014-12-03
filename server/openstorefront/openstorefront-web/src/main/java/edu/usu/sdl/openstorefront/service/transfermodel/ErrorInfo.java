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
package edu.usu.sdl.openstorefront.service.transfermodel;

import edu.usu.sdl.openstorefront.storage.model.ErrorTypeCode;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * Used to pass around the error information
 *
 * @author dshurtleff
 */
public class ErrorInfo
		implements Serializable
{

	private static final Logger log = Logger.getLogger(OpenStorefrontConstant.ERROR_LOGGER);

	private Throwable error;
	private String clientIp = "";
	private String requestUrl = "";
	private String requestMethod = "";
	private String inputData = "";
	private String errorTypeCode = ErrorTypeCode.SYSTEM;

	public ErrorInfo(Throwable error, HttpServletRequest request)
	{
		this.error = error;

		if (request != null) {
			requestUrl = request.getRequestURI();
			requestMethod = request.getMethod();
			clientIp = request.getRemoteAddr();

			//Check for header ip it may be forwarded by a proxy
			String clientIpFromHeader = request.getHeader("x-forwarded-for");
			if (StringUtils.isNotBlank(clientIpFromHeader)) {
				clientIp = " Forward for: " + clientIpFromHeader;
			} else {
				clientIpFromHeader = request.getHeader("x-real-ip");
				if (StringUtils.isNotBlank(clientIpFromHeader)) {
					clientIp = clientIp = " X-real IP: " + clientIpFromHeader;
				} else {
					clientIp = request.getRemoteAddr();
				}
			}

			StringBuilder input = new StringBuilder();
			if (StringUtils.isNotBlank(request.getQueryString())) {
				input.append("Query: ").append(request.getQueryString()).append("\n");
			}

			inputData = input.toString();
		}
	}

	public Throwable getError()
	{
		return error;
	}

	public void setError(Throwable error)
	{
		this.error = error;
	}

	public String getClientIp()
	{
		return clientIp;
	}

	public void setClientIp(String clientIp)
	{
		this.clientIp = clientIp;
	}

	public String getRequestUrl()
	{
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl)
	{
		this.requestUrl = requestUrl;
	}

	public String getRequestMethod()
	{
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod)
	{
		this.requestMethod = requestMethod;
	}

	public String getInputData()
	{
		return inputData;
	}

	public void setInputData(String inputData)
	{
		this.inputData = inputData;
	}

	public String getErrorTypeCode()
	{
		return errorTypeCode;
	}

	public void setErrorTypeCode(String errorTypeCode)
	{
		this.errorTypeCode = errorTypeCode;
	}

}
