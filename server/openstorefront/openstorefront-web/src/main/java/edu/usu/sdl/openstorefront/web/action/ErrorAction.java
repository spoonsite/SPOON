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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import java.util.Enumeration;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ErrorAction
		extends BaseAction
{

	private int errorCode;

	@DefaultHandler
	public Resolution errorTrap()
	{
		//gather original request info
		StringBuilder info = new StringBuilder();
		String requestUrl = getContext().getRequest().getRequestURI();
		String requestMethod = getContext().getRequest().getMethod();

		StringBuilder input = new StringBuilder();
		if (StringUtils.isNotBlank(getContext().getRequest().getQueryString())) {
			input.append("Query: ").append(getContext().getRequest().getQueryString()).append("\n");
		}

		String inputData = input.toString();

		info.append("Original Request: ").append(requestUrl).append("<br>");
		info.append("Original Request Method: ").append(requestMethod).append("<br>");
		info.append("Original Input: ").append(inputData).append("<br>");

		StringBuilder headerInfo = new StringBuilder();
		Enumeration<String> names = getContext().getRequest().getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			StringBuilder valueInfo = new StringBuilder();
			Enumeration<String> values = getContext().getRequest().getHeaders(name);
			while (values.hasMoreElements()) {
				String value = values.nextElement();
				valueInfo.append(value).append(" | ");

			}
			headerInfo.append(name).append(" = ").append(valueInfo).append("<br>");
		}
		info.append("Original Headers: ").append(headerInfo).append("<br>");

		//Unhandled exception
		Throwable exception = (Throwable) getContext().getRequest().getAttribute("exception");
		if (exception != null) {
			throw new OpenStorefrontRuntimeException("Unhandled Exception on a page occured", info.toString(), exception);
		} else {
			throw new OpenStorefrontRuntimeException("Unhandled Exception on a page occured", info.toString());
		}
	}

	@HandlesEvent("Error")
	public Resolution generateError()
	{
		return new ErrorResolution(errorCode, "The is a test response");
	}

	public int getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(int errorCode)
	{
		this.errorCode = errorCode;
	}

}
