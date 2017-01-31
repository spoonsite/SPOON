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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.api.SecurityService;
import edu.usu.sdl.openstorefront.core.entity.SecurityPolicy;
import edu.usu.sdl.openstorefront.core.entity.SecurityRole;
import edu.usu.sdl.openstorefront.core.entity.UserRegistration;
import edu.usu.sdl.openstorefront.core.entity.UserRole;
import edu.usu.sdl.openstorefront.core.entity.UserSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.security.Key;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.ehcache.Element;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.WhitespaceRule;

/**
 * Handles the user security data relationships
 * @author dshurtleff
 */
public class SecurityServiceImpl
		extends ServiceProxy
		implements SecurityService
{
	private static final Logger LOG = Logger.getLogger(SearchServiceImpl.class.getName());	
	
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
			securityPolicy = new SecurityPolicy();
			securityPolicy.setActiveStatus(SecurityPolicy.ACTIVE_STATUS);
			securityPolicy = securityPolicy.find();
			
			if (securityPolicy == null)	{
				//default
				securityPolicy = new SecurityPolicy();
				securityPolicy.setAllowRegistration(Boolean.TRUE);
				securityPolicy.setAutoApproveUsers(Boolean.FALSE);
				securityPolicy.setAllowJSONPSupport(Boolean.FALSE);
				securityPolicy.setCrsfSupport(Boolean.TRUE);
				securityPolicy.setLoginLockoutMaxAttempts(5);
				securityPolicy.setMinPasswordLength(8);
				securityPolicy.setRequireAdminUnlock(Boolean.FALSE);
				securityPolicy.setRequiresProofOfCitizenship(Boolean.FALSE);
				securityPolicy.setResetLockoutTimeMinutes(15);								
			} else {
				element = new Element(CURRENT_SECURITY_POLICY, securityPolicy);
				OSFCacheManager.getApplicationCache().put(element);
			}
		}	
		return securityPolicy;
	}

	@Override
	public void updateSecurityPolicy(SecurityPolicy securityPolicy)
	{
		SecurityPolicy existing = persistenceService.findById(SecurityPolicy.class, securityPolicy.getPolicyId());
		if (existing != null) {
			existing.updateFields(securityPolicy);
			persistenceService.persist(existing);
		} else {
			securityPolicy.setPolicyId(persistenceService.generateId());
			securityPolicy.populateBaseCreateFields();
			persistenceService.persist(securityPolicy);
		}			
		OSFCacheManager.getApplicationCache().remove(CURRENT_SECURITY_POLICY);
		LOG.log(Level.INFO, MessageFormat.format("Security Policy was update by: {0}", SecurityUtil.getCurrentUserName()));	
	}

	@Override
	public ValidationResult validatePassword(char[] password)
	{
		ValidationResult result = new ValidationResult();		
		PasswordValidator validator = new PasswordValidator(passwordRules());
		
		org.passay.RuleResult checkResult = validator.validate(new PasswordData(new String(password)));
		
		if (checkResult.isValid() == false) {
			for (String errorMessage : validator.getMessages(checkResult)) {
				RuleResult ruleResult = new RuleResult();
				ruleResult.setEntityClassName(UserSecurity.class.getSimpleName());			
				ruleResult.setFieldName(UserSecurity.PASSWORD_FIELD);
				ruleResult.setMessage(errorMessage);
			}
		}
		return result;
	}	
	
	@Override
	public String generatePassword()
	{
		SecurityPolicy securityPolicy = getSecurityPolicy();
		
		PasswordGenerator generator = new PasswordGenerator();
		String password = generator.generatePassword(securityPolicy.getMinPasswordLength(), Arrays.asList(
			new CharacterRule(EnglishCharacterData.UpperCase, 1),
			new CharacterRule(EnglishCharacterData.LowerCase, 1),
			new CharacterRule(EnglishCharacterData.Digit, 1),
			new CharacterRule(EnglishCharacterData.Special, 1)
		));
		return password;
	}	
	
	private List<Rule> passwordRules() {
		SecurityPolicy securityPolicy = getSecurityPolicy();
		
		List<Rule> rules = Arrays.asList(
			new LengthRule(securityPolicy.getMinPasswordLength(), OpenStorefrontConstant.FIELD_SIZE_80),
			new CharacterRule(EnglishCharacterData.UpperCase, 1),
			new CharacterRule(EnglishCharacterData.LowerCase, 1),
			new CharacterRule(EnglishCharacterData.Digit, 1),
			new CharacterRule(EnglishCharacterData.Special, 1),
			new WhitespaceRule()
		);		
		return rules;
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
	public String resetPasswordUser(String username, char[] password)
	{
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		
		String approvalCode = null;
		
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username);
		userSecurity = userSecurity.findProxy();
		if (userSecurity != null) {
			
//			String rawApprovalCode = username + 
//						OpenStorefrontConstant.GENERAL_KEY_SEPARATOR + 
//						OpenStorefrontConstant.GENERAL_KEY_SEPARATOR + 
//						OpenStorefrontConstant.GENERAL_KEY_SEPARATOR + 
//						persistenceService.generateId();
//			
//			
//			
//			
//			DefaultPasswordService passwordService = new DefaultPasswordService();
//			String encryptedValue = passwordService.encryptPassword(password);
//			userSecurity.setTempPassword(encryptedValue);
//			userSecurity.setPasswordChangeApprovalCode();
			
		
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find user to reset", "Check input: " + username);
		}
		return approvalCode;
	}

	@Override
	public void approveUserPasswordReset(String approvalCode)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void adminResetPassword(String username, char[] password)
	{
		Objects.requireNonNull(username);
		
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username);
		userSecurity = userSecurity.findProxy();
		
		if (userSecurity != null) {		
			ValidationResult validationResult = validatePassword(password);
			if (validationResult.valid()) {			
				DefaultPasswordService passwordService = new DefaultPasswordService();
				String encryptedValue = passwordService.encryptPassword(password);
				userSecurity.setPassword(encryptedValue);
				userSecurity.populateBaseUpdateFields();
				persistenceService.persist(userSecurity);
				LOG.log(Level.INFO, MessageFormat.format("user {0} password was reset by: {1}", username, SecurityUtil.getCurrentUserName()));			
			} else {
				throw new OpenStorefrontRuntimeException("Password is not valid", validationResult.toString());
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find user to reset", "Check input: " + username);
		}
	}

	@Override
	public void unlockUser(String username)
	{
		Objects.requireNonNull(username);
		
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username);
		userSecurity = userSecurity.findProxy();
		
		if (userSecurity != null) {
			userSecurity.setActiveStatus(UserSecurity.ACTIVE_STATUS);
			userSecurity.setFailLoginAttempts(0);
			userSecurity.populateBaseUpdateFields();
			persistenceService.persist(userSecurity);
			LOG.log(Level.INFO, MessageFormat.format("user {0} was unlocked by: {1}", username, SecurityUtil.getCurrentUserName()));			
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find user to unlock.", "Check input: " + username);
		}	
	}

	@Override
	public void lockUser(String username)
	{
		Objects.requireNonNull(username);
		
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username);
		userSecurity = userSecurity.findProxy();
		
		if (userSecurity != null) {
			userSecurity.setActiveStatus(UserSecurity.INACTIVE_STATUS);
			userSecurity.setFailLoginAttempts(0);
			userSecurity.populateBaseUpdateFields();
			persistenceService.persist(userSecurity);
			LOG.log(Level.INFO, MessageFormat.format("User {0} was locked by: {1}", username, SecurityUtil.getCurrentUserName()));			
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find user to lock.", "Check input: " + username);
		}
	}

	@Override
	public SecurityRole saveSecurityRole(SecurityRole securityRole)
	{
		Objects.requireNonNull(securityRole);
		
		SecurityRole existing = persistenceService.find(SecurityRole.class, securityRole.getRoleName());
		if (existing != null) {
			existing.updateFields(securityRole);
			securityRole = persistenceService.persist(existing);					
		} else {
			securityRole.populateBaseCreateFields();
			securityRole = persistenceService.persist(securityRole);				
		}
		securityRole = persistenceService.unwrapProxyObject(SecurityRole.class, securityRole);
		return securityRole;
	}

	@Override
	public void addUserToRole(String username, String role)
	{
		Objects.requireNonNull(username);
		Objects.requireNonNull(role);
		
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username);
		userSecurity = userSecurity.findProxy();
		
		if (userSecurity != null) {
			UserRole userRole = new UserRole();
			userRole.setRole(role);
			userRole.setUsername(username);
			
			userRole = userRole.find();
			if (userRole != null) {
				LOG.log(Level.INFO, MessageFormat.format("User {0} already had role: {1}", username, role));			
			} else {
				userRole = new UserRole();
				userRole.setRole(role);
				userRole.setUsername(username);
				userRole.setUserRoleId(persistenceService.generateId());
				userRole.populateBaseCreateFields();
				persistenceService.persist(userRole);
				
				LOG.log(Level.INFO, MessageFormat.format("Role {0} was added to user: {1} by {2}", role, username, SecurityUtil.getCurrentUserName()));			
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find user to add role to.", "Check input: " + username);
		}
	}

	@Override
	public void removeRoleFromUser(String username, String role)
	{
		Objects.requireNonNull(username);
		Objects.requireNonNull(role);
		
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username);
		userSecurity = userSecurity.findProxy();
		
		if (userSecurity != null) {
			UserRole userRoleExample = new UserRole();
			userRoleExample.setRole(role);
			userRoleExample.setUsername(username);
			
			userRoleExample = userRoleExample.find();
			if (userRoleExample != null) {
				persistenceService.deleteByExample(userRoleExample);
				LOG.log(Level.INFO, MessageFormat.format("Role {0} was removed from user: {1} by {2}",  role, username, SecurityUtil.getCurrentUserName()));			
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find user to add role to.", "Check input: " + username);
		}
	}

	@Override
	public Key applicationCryptKey()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
