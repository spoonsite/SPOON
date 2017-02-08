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
package edu.usu.sdl.openstorefront.security;

import static edu.usu.sdl.openstorefront.common.util.NetworkUtil.getClientIp;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;

/**
 * General security methods
 *
 * @author dshurtleff
 */
public class SecurityUtil
{

	private static final Logger log = Logger.getLogger(SecurityUtil.class.getName());

	public static final String ADMIN_ROLE = "administrator";
	public static final String USER_CONTEXT_KEY = "USER_CONTEXT";

	/**
	 * Is the current request logged in
	 *
	 * @return
	 */
	public static boolean isLoggedIn()
	{
		boolean loggedIn;
		try {
			Subject currentUser = SecurityUtils.getSubject();
			loggedIn = currentUser.isAuthenticated();
		} catch (Exception e) {
			//ignore
			loggedIn = false;
		}
		return loggedIn;
	}

	/**
	 * Check for Guest or Anonymous user
	 *
	 * @return true if guest
	 */
	public static boolean isGuest()
	{
		boolean guest;
		try {
			guest = !SecurityUtils.getSubject().isRemembered();
		} catch (Exception e) {
			//ignore
			guest = true;
		}
		return guest;
	}

	/**
	 * Gets the current user logged in.
	 *
	 * @return the username
	 */
	public static String getCurrentUserName()
	{
		String username = OpenStorefrontConstant.ANONYMOUS_USER;
		try {
			Subject currentUser = SecurityUtils.getSubject();
			if (currentUser.getPrincipal() != null) {
				username = currentUser.getPrincipal().toString();
			}
		} catch (Exception e) {
			log.log(Level.FINE, "Determing Username.  No user is logged in.  This is likely an auto process.");
		}
		return username;
	}

	/**
	 * Checks the current user to see if they are an admin
	 *
	 * @return true if the user is an admin
	 */
	public static boolean isAdminUser()
	{
		boolean admin = false;
		try {
			Subject currentUser = SecurityUtils.getSubject();
			admin = currentUser.hasRole(ADMIN_ROLE);
		} catch (Exception e) {
			log.log(Level.FINE, "Determining admin user.  No user is logged in.  This is likely an auto process.");
		}
		return admin;
	}

	/**
	 * Find the current user context in the session
	 *
	 * @return context or null if not found
	 */
	public static UserContext getUserContext()
	{
		UserContext userContext = null;
		try {
			Subject currentUser = SecurityUtils.getSubject();
			userContext = (UserContext) currentUser.getSession().getAttribute(USER_CONTEXT_KEY);
		} catch (Exception e) {
			log.log(Level.WARNING, "No user is logged in or security Manager hasn't started yet.");
		}
		return userContext;
	}

	/**
	 * Check to see if the current user created the entity
	 *
	 * @param entity (if null it will return false)
	 * @return
	 */
	public static boolean isCurrentUserTheOwner(StandardEntity entity)
	{
		if (entity != null) {
			return getCurrentUserName().equals(entity.getCreateUser());
		} else {
			return false;
		}
	}
	
	/**
	 * Checks the current for permission
	 * @param permission
	 * @return true if the user has the permission (All of them)
	 */
	public static boolean hasPermission(String... permissions)
	{
		boolean allow = false;
		if (permissions == null) {
			allow = true;
		} else {
			try {
				SecurityUtils.getSubject().checkPermissions(permissions);			
			} catch (AuthorizationException authorizationException) {
				log.log(Level.FINEST, MessageFormat.format("User does not have permissions: {0}", Arrays.toString(permissions)), authorizationException);			
			}
		}
		return allow;	
	}

	/**
	 * Constructs an Admin Audit Log Message
	 *
	 * @param request
	 * @return message
	 */
	public static String adminAuditLogMessage(HttpServletRequest request)
	{
		StringBuilder message = new StringBuilder();

		message.append("User: ")
				.append(getCurrentUserName())
				.append(" (").append(getClientIp(request)).append(") ")
				.append(" Called Restricted API: ");
		if (request != null) {
			message.append(request.getMethod()).append(" ");
			if (StringUtils.isNotBlank(request.getQueryString())) {
				message.append(request.getRequestURL()).append("?").append(request.getQueryString());
			} else {
				message.append(request.getRequestURL());
			}
		}

		return message.toString();
	}

}
