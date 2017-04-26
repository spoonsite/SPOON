/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.web.init;

import edu.usu.sdl.core.CoreSystem;
import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dshurtleff
 */
@WebFilter(filterName = "SystemStatusFilter",
		urlPatterns = {"/*"},
		displayName = "System Status",
		description = "Check system status",
		dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.ASYNC})
public class SystemStatusFilter
		implements Filter
{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		//nothing to init
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		if (CoreSystem.isStarted()) {
			chain.doFilter(request, response);
		} else {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			RequestDispatcher requestDispatcher = httpServletRequest.getRequestDispatcher("/WEB-INF/securepages/systemStandby.jsp");
			requestDispatcher.include(request, response);
		}
	}

	@Override
	public void destroy()
	{
		//nothing to shutdown
	}

}
