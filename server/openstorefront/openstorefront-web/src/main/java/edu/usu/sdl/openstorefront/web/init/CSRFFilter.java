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

import edu.usu.sdl.openstorefront.core.entity.SecurityPolicy;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
@WebFilter(filterName = "CSRFFilter", urlPatterns = {"/*"}, dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.ASYNC})
public class CSRFFilter
		implements Filter
{

	private static final String CSRF_TOKEN = "CSRF-TOKEN";
	private static final String X_CSRF_TOKEN = "X-Csrf-Token";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		//nothing to init
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{

		//look at the method (GET, HEAD, OPTIONS...no CSRF check is needed)
		Set<String> safeMethods = new HashSet<>();
		safeMethods.addAll(Arrays.asList(
				"GET",
				"HEAD",
				"OPTIONS"
		));

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		boolean upload = false;

		String contentType = httpRequest.getHeader("Content-Type");
		if (StringUtils.isNotBlank(contentType)) {
			if (contentType.toLowerCase().contains("multipart/form-data")) {
				upload = true;
			}
		}

		boolean valid = true;
		if (!safeMethods.contains(httpRequest.getMethod()) && !upload) {

			if (SecurityUtil.isLoggedIn()) {

				SecurityPolicy securityPolicy = ServiceProxy.getProxy().getSecurityService().getSecurityPolicy();
				if (securityPolicy.getCsrfSupport()) {

					//check session for token
					String token = (String) httpRequest.getSession().getAttribute(CSRF_TOKEN);
					if (token != null) {
						valid = false;
						
						String tokenFromHeader = httpRequest.getHeader(X_CSRF_TOKEN);
						if (StringUtils.isNotBlank(tokenFromHeader)) {
							if (tokenFromHeader.equals(token)) {
								valid = true;
							}
						} else {
							String tokenFromParameter = httpRequest.getParameter(X_CSRF_TOKEN);
							if (StringUtils.isNotBlank(tokenFromParameter)) {
								if (tokenFromParameter.equals(token)) {
									valid = true;
								}
							}
						}

						if (!valid) {
							HttpServletResponse httpResponse = (HttpServletResponse) response;
							httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF TOKEN is invalid");
						}

					} else {
						token = UUID.randomUUID().toString();
						httpRequest.getSession().setAttribute(CSRF_TOKEN, token);

						HttpServletResponse httpResponse = (HttpServletResponse) response;

						@SuppressWarnings("squid:S2092")
						Cookie cookie = new Cookie(X_CSRF_TOKEN, token);
						cookie.setHttpOnly(false);
						cookie.setPath("/");
						cookie.setMaxAge(-1);
						httpResponse.addCookie(cookie);

						//Let request through...origin server must be the first request
						//this will be case as the user must first login in order for a CSRF attack to be successful.
					}
				}
			}
		}

		if (valid) {
			chain.doFilter(request, response);
		} // Otherwise do nothing. See javax serverlet FilterChain
	}

	@Override
	public void destroy()
	{
		//nothing to shutdown
	}

}
