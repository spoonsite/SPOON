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
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

	private static final Logger LOG = Logger.getLogger(SecurityUtil.class.getName());

	public static final String USER_CONTEXT_KEY = "USER_CONTEXT";

	private static final ThreadLocal<AtomicBoolean> SYSTEMUSER = new ThreadLocal<>();

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
	 * Gets the current user logged in.
	 *
	 * @return the username
	 */
	public static String getCurrentUserName()
	{
		String username = OpenStorefrontConstant.ANONYMOUS_USER;
		if (isSystemUser()) {
			username = OpenStorefrontConstant.SYSTEM_USER;
		}
		try {
			Subject currentUser = SecurityUtils.getSubject();
			if (currentUser.getPrincipal() != null) {
				username = currentUser.getPrincipal().toString();
			}
		} catch (Exception e) {
			LOG.log(Level.FINE, "Determing Username.  No user is logged in.  This is likely an auto process.");
		}
		return username;
	}

	/**
	 * This will add the user to the callable to run; If the security is not
	 * initialize then this will pass through. (Expected for System user prior
	 * to system init)
	 *
	 * @param <V>
	 * @param task
	 * @return
	 */
	public static <V> Callable<V> associateSecurity(Callable<V> task)
	{
		try {
			return SecurityUtils.getSubject().associateWith(task);
		} catch (Exception e) {
			//security manager is not active; pass through in this case
			LOG.log(Level.FINEST, "Security not active?", e);
			return task;
		}
	}

	/**
	 * Sets the current thread to be running under the system user
	 */
	public static void initSystemUser()
	{
		SYSTEMUSER.set(new AtomicBoolean(true));
	}

	/**
	 * Check to see if this the system user
	 *
	 * @return
	 */
	public static boolean isSystemUser()
	{
		if (SYSTEMUSER.get() == null) {
			return false;
		} else {
			return SYSTEMUSER.get().get();
		}
	}

	/**
	 * Checks the current user to see if they are an entry admin
	 *
	 * @return true if the user is an entry admin
	 */
	public static boolean isEntryAdminUser()
	{
		boolean admin = false;
		try {
			admin = hasPermission(SecurityPermission.ADMIN_ENTRY_APPROVE);
		} catch (Exception e) {
			LOG.log(Level.FINE, "Determining admin user.  No user is logged in.  This is likely an auto process.");
		}
		return admin;
	}

	/**
	 * Checks the current user to see if they are an evaluator
	 *
	 * @return true if the user is an evaluator
	 */
	public static boolean isEvaluatorUser()
	{
		boolean admin = false;
		try {
			admin = hasPermission(SecurityPermission.USER_EVALUATIONS_UPDATE);
		} catch (Exception e) {
			LOG.log(Level.FINE, "Determining evaluator user.  No user is logged in.  This is likely an auto process.");
		}
		return admin;
	}

	/**
	 * Find the current user context in the session. If the no user is no login
	 * the guest user is returned unless the user is the system in which case
	 * the system user is return
	 *
	 * @return context or null if not found (Only in the case were the there a
	 * login user with a missing context)
	 */
	public static UserContext getUserContext()
	{
		UserContext userContext = null;
		if (!isLoggedIn()) {
			if (isSystemUser()) {
				userContext = getSystemUserContext();
			} else {
				userContext = getGuestUserContext();
			}
		} else {
			try {
				Subject currentUser = SecurityUtils.getSubject();
				userContext = (UserContext) currentUser.getSession().getAttribute(USER_CONTEXT_KEY);
			} catch (Exception e) {
				LOG.log(Level.WARNING, "No user is logged in or security Manager hasn't started yet.");
			}
		}
		return userContext;
	}

	private static UserContext getGuestUserContext()
	{
		UserContext userContext = ServiceProxyFactory.getServiceProxy().getSecurityService().getGuestContext();
		return userContext;
	}

	private static UserContext getSystemUserContext()
	{
		UserContext userContext = ServiceProxyFactory.getServiceProxy().getSecurityService().getSystemContext();
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
			return getCurrentUserName().equals(entity.entityOwner());
		} else {
			return false;
		}
	}

	/**
	 * Checks the current for permission
	 *
	 * @param permissions
	 * @return true if the user has the permission (All of them)
	 */
	public static boolean hasPermission(String... permissions)
	{
		boolean allow = false;
		if (permissions == null || permissions.length == 0) {
			allow = true;
		} else {
			List<String> toCheck = new ArrayList<>();
			for (String permission : permissions) {
				if (StringUtils.isNotBlank(permission)) {
					toCheck.add(permission);
				}
			}
			try {
				SecurityUtils.getSubject().checkPermissions(toCheck.toArray(new String[0]));
				allow = true;
			} catch (AuthorizationException authorizationException) {
				LOG.log(Level.FINEST, MessageFormat.format("User does not have permissions: {0}", Arrays.toString(permissions)), authorizationException);
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

	@SuppressWarnings("UseSpecificCatch")
	public static void logout(HttpServletRequest request, HttpServletResponse response)
	{
		Subject currentUser = SecurityUtils.getSubject();
		String userLoggedIn = SecurityUtil.getCurrentUserName();

		currentUser.logout();
		request.getSession().invalidate();

		//Handle all exceptions (workaround for tomcat bug in 7.0_65)
		try {
			request.logout();
		} catch (Exception ex) {
			LOG.log(Level.WARNING, () -> "Unable to log out of container authorization system. Error message:\n" + ex.getMessage());
			LOG.log(Level.FINEST, "Trace of Logout Failure", ex);
		}

		//For now invalidate all cookies; in the future there may be some that should persist.
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				cookie.setValue("-");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}

		if (OpenStorefrontConstant.ANONYMOUS_USER.equals(userLoggedIn)) {
			LOG.log(Level.INFO, "User was not logged when the logout was called.");
		} else {
			LOG.log(Level.INFO, MessageFormat.format("Logged off user: {0}", userLoggedIn));
		}
	}
}
