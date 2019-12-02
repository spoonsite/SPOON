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
package edu.usu.sdl.openstorefront.security;

import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.SecurityPolicy;
import edu.usu.sdl.openstorefront.core.entity.UserApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.UserSecurity;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

/**
 * This is used to connect to build security handling
 *
 * @author dshurtleff
 */
public class StorefrontRealm
		extends AuthorizingRealm
{

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
	{
		UserContext userContext = (UserContext) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.addRoles(userContext.roles());
		authorizationInfo.addStringPermissions(userContext.permissions());
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
	{
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

		//pull user security
		String username = usernamePasswordToken.getUsername().toLowerCase();
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username);
		userSecurity = userSecurity.find();
		if (userSecurity == null) {
			throw new UnknownAccountException("Unable to find user.");
		}

		ServiceProxy serviceProxy = ServiceProxy.getProxy();
		SecurityPolicy securityPolicy = serviceProxy.getSecurityService().getSecurityPolicy();
		if (userSecurity.getFailedLoginAttempts() == null) {
			userSecurity.setFailedLoginAttempts(0);
		}

		if (userSecurity.getFailedLoginAttempts() > securityPolicy.getLoginLockoutMaxAttempts()) {
			Date now = TimeUtil.currentDate();
			Instant instantLastAttempt = Instant.ofEpochMilli(userSecurity.getLastLoginAttempt().getTime());
			instantLastAttempt = instantLastAttempt.plus(securityPolicy.getResetLockoutTimeMinutes(), ChronoUnit.MINUTES);
			if (now.toInstant().isAfter(instantLastAttempt)) {
				if (securityPolicy.getRequireAdminUnlock() == false) {
					userSecurity.setFailedLoginAttempts(0);
				} else {
					throw new LockedAccountException("Account is locked due to multiple failed attempts. Requires admin to unlock.");
				}
			} else {
				throw new LockedAccountException("Account is locked due to multiple failed attempts.");
			}
		}

		userSecurity.setLastLoginAttempt(TimeUtil.currentDate());
		userSecurity.save();

		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo();
		simpleAuthenticationInfo.setCredentials(userSecurity.getPassword());

		SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
		UserContext userContext = serviceProxy.getSecurityService().getUserContext(username);
		principalCollection.add(userContext, StorefrontRealm.class.getSimpleName());
		simpleAuthenticationInfo.setPrincipals(principalCollection);

		return simpleAuthenticationInfo;
	}

	@Override
	protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException
	{
		try {
			super.assertCredentialsMatch(token, info);

			//clear failed attempts if needed
			UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

			UserSecurity userSecurity = new UserSecurity();
			userSecurity.setUsername(usernamePasswordToken.getUsername().toLowerCase());
			userSecurity = userSecurity.find();
			if (userSecurity.getFailedLoginAttempts() != null
					&& userSecurity.getFailedLoginAttempts() > 0) {
				userSecurity.setFailedLoginAttempts(0);
				userSecurity.save();
			}

			if (UserSecurity.INACTIVE_STATUS.equals(userSecurity.getActiveStatus())) {
				throw new DisabledAccountException("User " + usernamePasswordToken.getUsername() + " is disabled. Contact admin.");
			}

			if (UserApprovalStatus.PENDING.equals(userSecurity.getApprovalStatus())) {
				throw new DisabledAccountException("User " + usernamePasswordToken.getUsername() + " is not approved");
			}

		} catch (AuthenticationException authenticationException) {
			UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

			UserSecurity userSecurity = new UserSecurity();
			userSecurity.setUsername(usernamePasswordToken.getUsername().toLowerCase());
			userSecurity = userSecurity.find();

			if (userSecurity.getFailedLoginAttempts() == null) {
				userSecurity.setFailedLoginAttempts(1);
			} else {
				userSecurity.setFailedLoginAttempts(userSecurity.getFailedLoginAttempts() + 1);
				ServiceProxy serviceProxy = ServiceProxy.getProxy();
				SecurityPolicy securityPolicy = serviceProxy.getSecurityService().getSecurityPolicy();
				if (userSecurity.getFailedLoginAttempts() > securityPolicy.getLoginLockoutMaxAttempts()) {
					userSecurity.save();
					throw new LockedAccountException("Account is locked due to multiple failed attempts.");
				}
			}
			userSecurity.save();
			throw authenticationException;
		}
	}

}
