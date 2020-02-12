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
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

//{@link http://blogs.msdn.com/sdl/archive/2009/02/05/clickjacking-defense-in-ie8.aspx}
/**
 * This is added to prevent click-jacking by supported browsers Based on ESAPI
 * clickjackfilter see. only happens for the client ui
 *
 * @author dshurtleff
 */
@WebFilter(filterName = "ClickJack", urlPatterns = {"/views/*", "/*"})
public class ClickJackFilter
		implements Filter
{

	private String mode = "SAMEORIGIN";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		String configMode = filterConfig.getInitParameter("mode");
		if (configMode != null && (configMode.equals("DENY") || configMode.equals("SAMEORIGIN"))) {
			mode = configMode;
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		response.setCharacterEncoding("UTF-8");
		HttpServletResponse res = (HttpServletResponse) response;
		chain.doFilter(request, response);
		res.addHeader("X-FRAME-OPTIONS", mode);
	}

	@Override
	public void destroy()
	{
		//Do Nothing
	}

}
