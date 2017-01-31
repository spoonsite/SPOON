/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.core.api.SecurityService;
import edu.usu.sdl.openstorefront.core.entity.SecurityPolicy;
import edu.usu.sdl.openstorefront.core.entity.SecurityRole;
import edu.usu.sdl.openstorefront.core.entity.UserRegistration;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import net.sf.ehcache.Element;

/**
 *
 * @author dshurtleff
 */
public class SecurityServiceImpl
		extends ServiceProxy
		implements SecurityService
{
	private static final String CURRENT_SECURITY_POLICY = "CURRENTSECURITYPOLICY";
	
	
	@Override
	public SecurityPolicy getSecurityPolicy()
	{
		SecurityPolicy securityPolicy = null;
		Element element = OSFCacheManager.getApplicationCache().get(CURRENT_SECURITY_POLICY);
		if (element != null) {
			securityPolicy = (SecurityPolicy) element.getObjectValue();
		}
		
		if (securityPolicy == null) {
			
		}	
		return securityPolicy;
	}

	@Override
	public void updateSecurityPolicy(SecurityPolicy securityPolicy)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public ValidationResult validateRegistration(UserRegistration userRegistration)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void processNewRegistration(UserRegistration userRegistration)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String resetPasswordUser(String username, byte[] password)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void approveUserPasswordReset(String username, String approvalCode)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void adminResetPassword(String username, byte[] password)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void unlockUser(String username)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void lockUser(String username)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public SecurityRole saveSecurityRole(SecurityRole securityRole)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void addUserToRole(String username, String role)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void removeRoleFromUser(String username, String role)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
