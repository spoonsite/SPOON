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
import edu.usu.sdl.openstorefront.core.view.UserFilterParams;
import edu.usu.sdl.openstorefront.core.view.UserSecurityWrapper;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.Set;

/**
 *
 * @author dshurtleff
 */
public interface SecurityService
		extends AsyncService
{

	/**
	 * Gets the security policy for the application
	 *
	 * @return
	 */
	SecurityPolicy getSecurityPolicy();

	/**
	 * Updates the policy
	 *
	 * @param securityPolicy
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	SecurityPolicy updateSecurityPolicy(SecurityPolicy securityPolicy);

	/**
	 * Validate password meets security rules
	 *
	 * @param password (un-hashed)
	 * @return
	 */
	ValidationResult validatePassword(char[] password);

	/**
	 * Generates a password the conforms to policy rules
	 *
	 * @return
	 */
	String generatePassword();

	/**
	 * Performs validation according to security policy
	 *
	 * @param userRegistration
	 * @return validation results
	 */
	ValidationResult validateRegistration(UserRegistration userRegistration);

	/**
	 * Handle processing the application and generating a verification email
	 *
	 * @param userRegistration
	 * @param sendEmail
	 * @return validation results
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	ValidationResult processNewRegistration(UserRegistration userRegistration, Boolean sendEmail);

	/**
	 * Handle processing the application into user and user profile
	 *
	 * @param userRegistration
	 * @return validation results
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	ValidationResult processNewUser(UserRegistration userRegistration);

	/**
	 * Approve Registration
	 *
	 * @param username
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void approveRegistration(String username);

	/**
	 * Hashes and stores temp password
	 *
	 * @param username
	 * @param password
	 * @return Approval code (Base64 encoded for web)
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	String resetPasswordUser(String username, char[] password);

	/**
	 * This will send an email to the emailAddress provided The email will
	 * indicate the user associated with the email
	 *
	 * @param emailAddress
	 */
	void forgotUser(String emailAddress);

	/**
	 * Approves user password reset
	 *
	 * @param approvalCode (Assumes it Base64 encoded for web)
	 * @return true if successful (see log for false reasons which shouldn't be
	 * pass to the user)
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	boolean approveUserPasswordReset(String approvalCode);

	/**
	 * This will directly reset password
	 *
	 * @param username
	 * @param password
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void adminResetPassword(String username, char[] password);

	/**
	 * Enable account and reset lock count
	 *
	 * @param username
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void unlockUser(String username);

	/**
	 * Resets failed login attempt (effectively manual unlocks an auto failed
	 * locked account)
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void resetFailedAttempts(String username);

	/**
	 * This will disable an account; prevent login (Only using the appropriate
	 * security realm); Remove user profile based user profile rules Which
	 * should disable watches.
	 *
	 * @param username
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void disableUser(String username);

	/**
	 * Delete User, User Registration, User Profile (In-Activates)
	 *
	 * @param username
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void deleteUser(String username);

	/**
	 * Delete User Registration
	 *
	 * @param userRegistrationId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void deleteUserRegistration(String userRegistrationId);

	/**
	 * Make sure role name is unique (This is for a new one only)
	 *
	 * @param roleName
	 * @return
	 */
	ValidationResult validateSecurityRoleName(String roleName);

	/**
	 * Create or updated a security role
	 *
	 * @param securityRole
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	SecurityRole saveSecurityRole(SecurityRole securityRole);

	/**
	 * Delete the role and drop or move the users in that role
	 *
	 * @param roleName
	 * @param moveUserToRole (Optional)
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void deleteSecurityRole(String roleName, String moveUserToRole);

	/**
	 * Adds user to a role (Only for built in security realm external realms
	 * should be handled by the external system.
	 *
	 * @param username
	 * @param role
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void addUserToRole(String username, String role);

	/**
	 * Removes a role from a user
	 *
	 * @param username
	 * @param role
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void removeRoleFromUser(String username, String role);

	/**
	 * Pulls the crypt key
	 *
	 * @return
	 */
	byte[] applicationCryptKey();

	/**
	 * Get user context
	 *
	 * @param username
	 * @return context or null if user is not found.
	 */
	UserContext getUserContext(String username);

	/**
	 * This will get the user in the built in security DB
	 *
	 * @param queryParams
	 * @return
	 */
	UserSecurityWrapper getUserViews(UserFilterParams queryParams);

	/**
	 * This will remove all existing roles for a user and add matching roles for
	 * the given set.
	 *
	 * This is used to handling supporting external role/group management
	 *
	 * @param username
	 * @param groups
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	void updateRoleGroup(String username, Set<String> groups);

}
