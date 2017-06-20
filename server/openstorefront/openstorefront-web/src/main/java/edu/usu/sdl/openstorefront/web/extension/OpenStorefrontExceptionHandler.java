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
package edu.usu.sdl.openstorefront.web.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.model.ErrorInfo;
import edu.usu.sdl.openstorefront.core.view.SystemErrorModel;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.exception.DefaultExceptionHandler;

/**
 * Translate Errors into something the front-end can handle
 *
 * @author dshurtleff
 */
public class OpenStorefrontExceptionHandler
		extends DefaultExceptionHandler
{

	public Resolution handleAll(Throwable error, HttpServletRequest request, HttpServletResponse response)
	{
		//ActionBean action = (ActionBean) request.getAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN);

		if (error == null
				|| error.getMessage().startsWith("Could not locate an ActionBean that is bound to the URL")) {
			return new ErrorResolution(Response.Status.NOT_FOUND.getStatusCode());
		} else {

			ErrorInfo errorInfo = new ErrorInfo(error, request);

			//Strip and senstive info (See Checklist Q: 410)
			ServiceProxy serviceProxy = new ServiceProxy();
			SystemErrorModel systemErrorModel = serviceProxy.getSystemService().generateErrorTicket(errorInfo);

			final ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();
			return new StreamingResolution(MediaType.APPLICATION_JSON)
			{

				@Override
				protected void stream(HttpServletResponse response) throws Exception
				{
					objectMapper.writeValue(response.getOutputStream(), systemErrorModel);
				}

			};
		}
	}

}
