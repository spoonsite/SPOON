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
package edu.usu.sdl.openstorefront.security.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * This was created to reduce the complexity of setting up users for
 * Unit/Integration tests. after we have a few tests in place we may want to
 * re-evaluate if using a supported realm provides more value
 *
 * @author kbair
 */
public class TestRealm extends AuthorizingRealm
{

	private static Map<String, String> passwords = new HashMap<>();
	private static Map<String, Set<String>> roles = new HashMap<>();
	private static Map<String, Set<String>> permissions = new HashMap<>();

	public static void clearLogin()
	{
		passwords = new HashMap<>();
		roles = new HashMap<>();
		permissions = new HashMap<>();
	}

	public static void setLogin(String username, String password, Set<String> userRoles)
	{
		passwords.put(username, password);
		roles.put(username, userRoles);
	}

	public static void setupRoles(String name, Set<String> permissions)
	{
		TestRealm.permissions.put(name, permissions);
	}

	private static Set<String> getPermissions(String username)
	{
		Set<String> result = new HashSet<>();
		roles.get(username).forEach((role) -> {
			result.addAll(TestRealm.permissions.get(role));
		});
		return result;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
			throws AuthenticationException
	{
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;

		String username = upToken.getUsername();
		if (username == null) {
			throw new AuthenticationException("Null usernames are not allowed by this realm.");
		}

		String password = passwords.get(username);
		if (password == null) {
			throw new AuthenticationException("No account found for user [" + username + "]");
		}

		return new SimpleAuthenticationInfo(username, password.toCharArray(), getName());
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
	{
		if (principals == null) {
			throw new AuthenticationException("PrincipalCollection method argument cannot be null.");
		}

		String username = (String) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles.get(username));
		info.setStringPermissions(getPermissions(username));
		return info;
	}

}
