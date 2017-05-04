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
package edu.usu.sdl.openstorefront.web.rest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Allows for tracing route requests
 *
 * @author dshurtleff
 */
@Provider
public class TraceInterceptor
		implements WriterInterceptor
{

	private static final Logger LOG = Logger.getLogger(TraceInterceptor.class.getName());

	@Context
	HttpServletRequest request;

	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException
	{
		if (LOG.isLoggable(Level.FINEST)) {
			StringBuilder message = new StringBuilder();
			message.append(request.getMethod()).append(" ");
			if (StringUtils.isNotBlank(request.getQueryString())) {
				message.append(request.getRequestURL()).append("?").append(request.getQueryString());
			} else {
				message.append(request.getRequestURL());
			}
			LOG.finest(message.toString());
		}
		context.proceed();
	}

}
