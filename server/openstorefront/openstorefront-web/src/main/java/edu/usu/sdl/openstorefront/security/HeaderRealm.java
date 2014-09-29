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

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.storage.model.UserProfile;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * This use to login based on Request Header info
 *
 * @author dshurtleff
 */
public class HeaderRealm
		extends AuthorizingRealm
{

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
	{
		UserContext userContext = (UserContext) principals.getPrimaryPrincipal();
		HeaderAuthToken headerAuthToken = new HeaderAuthToken();
		headerAuthToken.setUserContext(userContext);
		return populateAccount(headerAuthToken);
	}

	@Override
	public boolean supports(AuthenticationToken token)
	{
		return (token instanceof HeaderAuthToken);
	}

	@Override
	protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException
	{
		//just let this through
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
	{
		HeaderAuthToken headerAuthToken = (HeaderAuthToken) token;
		return populateAccount(headerAuthToken);
	}

	private HeaderAccount populateAccount(HeaderAuthToken headerAuthToken)
	{
		HeaderAccount headerAccount = new HeaderAccount();

		UserContext userContext = headerAuthToken.getUserContext();
		boolean admin = false;
		if (userContext == null) {
			ServiceProxy serviceProxy = new ServiceProxy();

			if (StringUtils.isBlank(headerAuthToken.getUsername())) {
				//They shouldn't get here unless open am is not confiured
				throw new AuthenticationException("Unable to login.  No credentials passed.  Auth filter not set.");
			}

			UserProfile userProfile = new UserProfile();
			userProfile.setUsername(headerAuthToken.getUsername());
			userProfile.setFirstName(headerAuthToken.getFirstname());
			userProfile.setLastName(headerAuthToken.getLastname());
			userProfile.setOrganization(headerAuthToken.getOrganization());
			userProfile.setEmail(headerAuthToken.getEmail());
			userProfile.setExternalGuid(headerAuthToken.getGuid());

			if (StringUtils.isNotBlank(headerAuthToken.getGroup())
					&& StringUtils.isNotBlank(headerAuthToken.getAdminGroupName())) {
				admin = headerAuthToken.getGroup().contains(headerAuthToken.getAdminGroupName());
			}
			userContext = serviceProxy.getUserService().handleLogin(userProfile, headerAuthToken.getRequest(), admin);
		} else {
			admin = userContext.isAdmin();
		}
		headerAccount.setCredentials(userContext);
		headerAccount.getSimplePrincipals().add(userContext, "Open Am Header User");
		if (admin) {
			headerAccount.getRoles().add(SecurityUtil.ADMIN_ROLE);
		}

		return headerAccount;
	}

}
