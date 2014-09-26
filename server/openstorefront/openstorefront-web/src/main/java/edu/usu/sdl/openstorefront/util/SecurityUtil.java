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
package edu.usu.sdl.openstorefront.util;

import edu.usu.sdl.openstorefront.security.UserContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.shiro.SecurityUtils;
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
			log.log(Level.WARNING, "Security Manager hasn't started yet.  The user can't be obtain until the application has started.");
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
			log.log(Level.WARNING, "Security Manager hasn't started yet.  The user can't be obtain until the application has started.");
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
			log.log(Level.WARNING, "Security Manager hasn't started yet.  The user can't be obtain until the application has started.");
		}
		return userContext;
	}

}
