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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.SecurityPolicy;
import edu.usu.sdl.openstorefront.core.entity.SecurityRole;
import edu.usu.sdl.openstorefront.core.entity.UserRegistration;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.security.Key;

/**
 *
 * @author dshurtleff
 */
public interface SecurityService
		extends AsyncService
{

	/**
	 * Gets the security policy for the application
	 * @return 
	 */
	SecurityPolicy getSecurityPolicy();
	
	/**
	 * Updates the policy
	 * @param securityPolicy 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void updateSecurityPolicy(SecurityPolicy securityPolicy);
	
	/**
	 * Validate password meets security rules
	 * @param password (un-hashed)
	 * @return 
	 */
	ValidationResult validatePassword(char[] password);
	
	/**
	 * Generates a password the conforms to policy rules
	 * @return 
	 */
	String generatePassword();
	
	/**
	 * Performs validation according to security policy
	 * @param userRegistration
	 * @return validation results
	 */
	ValidationResult validateRegistration(UserRegistration userRegistration);
		
	/**
	 * Handle processing the application into user and user profile
	 * @param userRegistration 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void processNewRegistration(UserRegistration userRegistration);	
	
	/**
	 * Hashes and stores temp password
	 * @param username
	 * @param password
	 * @return Approval code (Base64 encoded for web)
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	String resetPasswordUser(String username, char[] password);
	
	/**
	 * Approves user password reset
	 * @param username
	 * @param approvalCode (Assumes it Base64 encoded for web)
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void approveUserPasswordReset(String approvalCode);
	
	/**
	 * This will directly reset password
	 * @param username
	 * @param password 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void adminResetPassword(String username, char[] password);
	
	/**
	 * Enable account and reset lock count
	 * @param username 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void unlockUser(String username);
	
	/**
	 * This will disable an account; prevent login (Only using the appropriate 
	 * security realm)
	 * @param username 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void lockUser(String username);
	
	/**
	 * Create or updated a security role
	 * @param securityRole
	 * @return 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	SecurityRole saveSecurityRole(SecurityRole securityRole);

	/**
	 * Adds user to a role (Only for built in security realm external realms 
	 * should be handled by the external system.
	 * @param username
	 * @param role 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void addUserToRole(String username, String role);
	
	/**
	 * Removes a role from a user
	 * @param username
	 * @param role 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void removeRoleFromUser(String username, String role);
	
	/**
	 * Pulls or creates a crypt key
	 * @return 
	 */
	Key applicationCryptKey();
	
}
