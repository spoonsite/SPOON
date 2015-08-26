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
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.view.JsonResponse;
import edu.usu.sdl.openstorefront.security.HeaderRealm;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.web.init.ShiroAdjustedFilter;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

/**
 * Login/Logoff Handling
 *
 * @author dshurtleff
 */
public class LoginAction
		extends BaseAction
{

	private static final Logger log = Logger.getLogger(LoginAction.class.getName());

	@Validate(required = true, on = "Login")
	private String username;

	@Validate(required = true, on = "Login")
	private String password;

	private boolean remember;
	private String gotoPage;

	private static final String STUB_HEADER = "X_STUBHEADER_X";

	@DefaultHandler
	public Resolution loginHandler()
	{
		gotoPage = (String) getContext().getRequest().getSession().getAttribute(ShiroAdjustedFilter.REFERENCED_FILTER_URL_ATTRIBUTE);
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.isAuthenticated()) {
			if (StringUtils.isNotBlank(gotoPage)) {
				if (gotoPage.toLowerCase().startsWith("http")) {
					return new RedirectResolution(gotoPage, false);
				} else {
					return new RedirectResolution(gotoPage);
				}
			}
			return new RedirectResolution("/");
		}

		if (HeaderRealm.isUsingHeaderRealm()) {
			Resolution resolution;
			if (HeaderRealm.handleHeaderLogin(getContext().getRequest())) {
				resolution = handleLoginRedirect();
			} else {
				log.log(Level.SEVERE, "Check configuration; Unable to login user using header realm");
				resolution = new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, "Unable to access system.");
			}
			if (resolution != null) {
				return resolution;
			}
		}
		getContext().getRequest().getSession().setAttribute(ShiroAdjustedFilter.REFERENCED_URL_ATTRIBUTE, gotoPage);
		return new ForwardResolution("/login.jsp").addParameter("gotoPage", gotoPage);
	}

	private Resolution handleLoginRedirect()
	{
		String startPage = startPage();
		if (startPage.toLowerCase().startsWith("http")) {
			return new RedirectResolution(startPage, false);
		} else {
			return new RedirectResolution(startPage);
		}
		//This for using shiro Direct redirect to be used with authc = org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter
//		return new OnwardResolution(LoginAction.class)
//		{
//
//			@Override
//			public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception
//			{
//				WebUtils.redirectToSavedRequest(request, response, startPage);
//			}
//		};
	}

	private String startPage()
	{
		String startPage = "/";
		if (StringUtils.isNotBlank(gotoPage)) {
			startPage = gotoPage;
		}
		startPage = startPage.replace("Login.action", "");
		return startPage;
	}

	@HandlesEvent("CheckHeaders")
	public Resolution checkHeaders()
	{
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

		return new StreamingResolution(MediaType.TEXT_HTML, headerInfo.toString());
	}

	@HandlesEvent("Login")
	public Resolution login()
	{
		Map<String, String> errors = new HashMap<>();

		org.apache.shiro.mgt.SecurityManager securityManager = SecurityUtils.getSecurityManager();
		if (securityManager instanceof DefaultWebSecurityManager) {
			DefaultWebSecurityManager webSecurityManager = (DefaultWebSecurityManager) securityManager;
			for (Realm realm : webSecurityManager.getRealms()) {
				if (realm instanceof HeaderRealm) {
					return handleLoginRedirect();
				}
			}
		}

		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		try {
			currentUser.login(token);
			UserProfile userProfile = new UserProfile();
			userProfile.setUsername(username);
			service.getUserService().handleLogin(userProfile, getContext().getRequest(), null);
			String startPage = startPage();
			if (startPage.toLowerCase().startsWith("http") == false) {
				startPage = getContext().getServletContext().getContextPath() + startPage;
			}
			JsonResponse jsonResponse = new JsonResponse();
			jsonResponse.setMessage(startPage);
			return streamResults(jsonResponse);
		} catch (AuthenticationException uea) {
			log.log(Level.WARNING, MessageFormat.format("{0} Failed to login.", username));
			log.log(Level.FINEST, "Failed to login Details: ", uea);
			errors.put("username", "Unable to login. Check username and password.");
		}
		return streamErrorResponse(errors);
	}

	@HandlesEvent("Logout")
	public Resolution logout()
	{
		Subject currentUser = SecurityUtils.getSubject();
		String userLoggedIn = SecurityUtil.getCurrentUserName();
		currentUser.logout();
		getContext().getRequest().getSession().invalidate();
		try {
			getContext().getRequest().logout();
		} catch (ServletException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}

		//For now invalidate all cookies; in the future there may be some that should persist.
		Cookie[] cookies = getContext().getRequest().getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				cookie.setValue("-");
				cookie.setMaxAge(0);
				getContext().getResponse().addCookie(cookie);
			}
		}

		if (OpenStorefrontConstant.ANONYMOUS_USER.equals(userLoggedIn)) {
			log.log(Level.INFO, "User was not logged when the logut was called.");
		} else {
			log.log(Level.INFO, MessageFormat.format("Logged off user: {0}", userLoggedIn));
		}

		String logoutUrl = PropertiesManager.getValue(PropertiesManager.KEY_LOGOUT_URL, "/login.jsp");
		if (StringUtils.isBlank(logoutUrl)) {
			logoutUrl = "/login.jsp";
		}
		if (logoutUrl.toLowerCase().startsWith("http")) {
			return new RedirectResolution(logoutUrl, false);
		} else {
			return new RedirectResolution(logoutUrl);
		}
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean getRemember()
	{
		return remember;
	}

	public void setRemember(boolean remember)
	{
		this.remember = remember;
	}

	public String getGotoPage()
	{
		return gotoPage;
	}

	public void setGotoPage(String gotoPage)
	{
		this.gotoPage = gotoPage;
	}

}
