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
package edu.usu.sdl.openstorefront.web.init;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.ShiroFilter;

/**
 *
 * @author dshurtleff
 */
public class ShiroAdjustedFilter
		extends ShiroFilter
{

	public static final String REFERENCED_URL_ATTRIBUTE = "REFERENCED_URL";
	public static final String REFERENCED_FILTER_URL_ATTRIBUTE = "REFERENCED_FILTER_URL";

	@Override
	protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException
	{
		if (servletRequest instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
			String url = httpServletRequest.getRequestURL().toString();
			if (url.contains("Login.action")
					&& url.contains(";")) {
				HttpServletResponse response = (HttpServletResponse) servletResponse;
				response.sendRedirect(httpServletRequest.getContextPath());
				return;
			}

			if (url.endsWith("Login.action") == false && url.contains("/api/") == false) {
				String queryString = httpServletRequest.getQueryString();

				if (StringUtils.isNotBlank(queryString)) {
					url = url + "?" + queryString;
				}
				httpServletRequest.getSession().setAttribute(REFERENCED_FILTER_URL_ATTRIBUTE, url);
			}
		}
		super.doFilterInternal(servletRequest, servletResponse, chain);
	}

}
