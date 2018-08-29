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
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.api.SecurityService;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOptionBuilder;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.AlertType;
import edu.usu.sdl.openstorefront.core.entity.ApplicationProperty;
import edu.usu.sdl.openstorefront.core.entity.DataSensitivity;
import edu.usu.sdl.openstorefront.core.entity.DataSource;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.SecurityPolicy;
import edu.usu.sdl.openstorefront.core.entity.SecurityRole;
import edu.usu.sdl.openstorefront.core.entity.SecurityRoleData;
import edu.usu.sdl.openstorefront.core.entity.SecurityRolePermission;
import edu.usu.sdl.openstorefront.core.entity.UserApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserRegistration;
import edu.usu.sdl.openstorefront.core.entity.UserRole;
import edu.usu.sdl.openstorefront.core.entity.UserSecurity;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import edu.usu.sdl.openstorefront.core.model.AlertContext;
import edu.usu.sdl.openstorefront.core.view.UserFilterParams;
import edu.usu.sdl.openstorefront.core.view.UserSecurityView;
import edu.usu.sdl.openstorefront.core.view.UserSecurityWrapper;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.service.api.SecurityServicePrivate;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.service.message.MessageContext;
import edu.usu.sdl.openstorefront.service.message.ResetPasswordMessageGenerator;
import edu.usu.sdl.openstorefront.service.message.UserApprovedMessageGenerator;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.mail.Message;
import net.sf.ehcache.Element;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.codemonkey.simplejavamail.email.Email;
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
 *
 * @author dshurtleff
 */
public class SecurityServiceImpl
		extends ServiceProxy
		implements SecurityService, SecurityServicePrivate
{

	private static final Logger LOG = Logger.getLogger(SearchServiceImpl.class.getName());

	private static final String CURRENT_SECURITY_POLICY = "CURRENTSECURITYPOLICY";

	@Override
	public SecurityPolicy getSecurityPolicy()
	{
		SecurityPolicy securityPolicy = null;

		Element element;

		//Note: this is used in filter that may be running when system is not completely started.
		if (OSFCacheManager.isActive()) {
			element = OSFCacheManager.getApplicationCache().get(CURRENT_SECURITY_POLICY);
			if (element != null) {
				securityPolicy = (SecurityPolicy) element.getObjectValue();
			}

			if (securityPolicy == null) {
				securityPolicy = new SecurityPolicy();
				securityPolicy.setActiveStatus(SecurityPolicy.ACTIVE_STATUS);
				securityPolicy = securityPolicy.find();

				if (securityPolicy == null) {
					securityPolicy = getDefaultPolicy();
					securityPolicy = updateSecurityPolicy(securityPolicy);
				}

				element = new Element(CURRENT_SECURITY_POLICY, securityPolicy);
				OSFCacheManager.getApplicationCache().put(element);
			}
		} else {
			securityPolicy = getDefaultPolicy();
		}
		return securityPolicy;
	}

	private SecurityPolicy getDefaultPolicy()
	{
		SecurityPolicy securityPolicy = new SecurityPolicy();
		securityPolicy.setAllowRegistration(Boolean.TRUE);
		securityPolicy.setAutoApproveUsers(Boolean.FALSE);
		securityPolicy.setAllowJSONPSupport(Boolean.FALSE);
		securityPolicy.setCsrfSupport(Boolean.TRUE);
		securityPolicy.setLoginLockoutMaxAttempts(5);
		securityPolicy.setMinPasswordLength(8);
		securityPolicy.setRequireAdminUnlock(Boolean.FALSE);
		securityPolicy.setRequiresProofOfCitizenship(Boolean.FALSE);
		securityPolicy.setResetLockoutTimeMinutes(15);
		return securityPolicy;
	}

	@Override
	public SecurityPolicy updateSecurityPolicy(SecurityPolicy securityPolicy)
	{
		SecurityPolicy existing = persistenceService.findById(SecurityPolicy.class, securityPolicy.getPolicyId());
		if (existing != null) {
			existing.updateFields(securityPolicy);
			securityPolicy = persistenceService.persist(existing);
		} else {
			securityPolicy.setPolicyId(persistenceService.generateId());
			securityPolicy.populateBaseCreateFields();
			securityPolicy = persistenceService.persist(securityPolicy);
		}
		//Make sure we have a copy as we may cache it.
		SecurityPolicy securityPolicyNew = securityPolicy.copy();

		OSFCacheManager.getApplicationCache().remove(CURRENT_SECURITY_POLICY);
		LOG.log(Level.INFO, MessageFormat.format("Security Policy was update by: {0}", SecurityUtil.getCurrentUserName()));
		return securityPolicyNew;
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
				result.getRuleResults().add(ruleResult);
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

	private List<Rule> passwordRules()
	{
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
		ValidationResult validationResult = new ValidationResult();
		validationResult.merge(userRegistration.validate());
		validationResult.merge(validatePassword(userRegistration.getPassword().toCharArray()));

		//check for unique name
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(userRegistration.getUsername().toLowerCase());
		userSecurity = userSecurity.find();
		if (userSecurity != null) {
			RuleResult result = new RuleResult();
			result.setFieldName(UserSecurity.FIELD_USERNAME);
			result.setEntityClassName(UserProfile.class.getSimpleName());
			result.setMessage("Username already exists");
			result.setValidationRule("Username must be unique");
			validationResult.getRuleResults().add(result);
		}

		return validationResult;
	}

	@Override
	public ValidationResult processNewRegistration(UserRegistration userRegistration, Boolean sendEmail)
	{
		Objects.requireNonNull(userRegistration);
		ValidationResult validationResult = validateRegistration(userRegistration);
		if (validationResult.valid()) {

			UserRegistration existing = (userRegistration.getRegistrationId() != null && !userRegistration.getRegistrationId().isEmpty())
					? persistenceService.findById(UserRegistration.class, userRegistration.getRegistrationId()) : null;

			if (existing != null) {
				existing.updateFields(userRegistration);
				userRegistration = existing;
			} else {
				userRegistration.setRegistrationId(persistenceService.generateId());
				userRegistration.populateBaseCreateFields();
			}
			if (sendEmail) {
				userRegistration.setVerificationCode(generateRandomString(8));

				if (StringUtils.isNotBlank(userRegistration.getEmail())) {
					Map<String, Object> data = new HashMap<>();
					String subject = "Email Verification Code";
					data.put("verificationCode", userRegistration.getVerificationCode());
					data.put("replyName", PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MAIL_REPLY_NAME));
					data.put("replyAddress", PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MAIL_REPLY_ADDRESS));
					data.put("title", subject);
					Email email = MailManager.newTemplateEmail(MailManager.Templates.EMAIL_VERIFICATION.toString(), data);
					email.setSubject(subject);
					email.addRecipient(userRegistration.getFirstName(), userRegistration.getEmail(), Message.RecipientType.TO);
					MailManager.send(email);
				} else {
					LOG.log(Level.WARNING, "Missing email address unable to send verification code");
				}
			}
			persistenceService.persist(userRegistration);
		}
		return validationResult;
	}

	private String generateRandomString(int length)
	{
		if (length < 1) {
			throw new IllegalArgumentException("length < 1: " + length);
		}
		String symbols = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			buffer.append(symbols.charAt(random.nextInt(symbols.length())));
		}
		return buffer.toString();
	}

	@Override
	public ValidationResult processNewUser(UserRegistration userRegistration)
	{
		return processNewUser(userRegistration, false);
	}

	@Override
	public ValidationResult processNewUser(UserRegistration userRegistration, boolean forceApprove)
	{
		Objects.requireNonNull(userRegistration);
		ValidationResult validationResult = validateRegistration(userRegistration);
		if (validationResult.valid()) {
			UserRegistration existing = persistenceService.findById(UserRegistration.class, userRegistration.getRegistrationId());

			if (existing != null) {
				existing.updateFields(userRegistration);
				userRegistration = existing;
			} else {
				throw new OpenStorefrontRuntimeException("Unable to find user registration", "Check input: " + userRegistration.getUsername());
			}

			boolean autoApproveUser = forceApprove;
			if (!forceApprove) {
				autoApproveUser = getSecurityPolicy().getAutoApproveUsers() || SecurityUtil.hasPermission(SecurityPermission.ADMIN_USER_MANAGEMENT_APPROVE);
			}

			UserSecurity userSecurity = new UserSecurity();
			DefaultPasswordService passwordService = new DefaultPasswordService();
			String encryptedValue = passwordService.encryptPassword(userRegistration.getPassword());
			userSecurity.setPassword(encryptedValue);
			userSecurity.setUsername(userRegistration.getUsername().toLowerCase());
			userSecurity.setFailedLoginAttempts(0);
			userSecurity.setPasswordUpdateDts(TimeUtil.currentDate());
			userSecurity.setUsingDefaultPassword(userRegistration.getUsingDefaultPassword());
			userSecurity.populateBaseCreateFields();

			if (autoApproveUser) {
				userSecurity.setApprovalStatus(UserApprovalStatus.APPROVED);
				userSecurity.setActiveStatus(UserSecurity.ACTIVE_STATUS);
			} else {
				userSecurity.setApprovalStatus(UserApprovalStatus.PENDING);
				userSecurity.setActiveStatus(UserSecurity.INACTIVE_STATUS);
			}
			persistenceService.persist(userSecurity);

			UserProfile userProfile = new UserProfile();
			if (autoApproveUser) {
				userProfile.setActiveStatus(UserProfile.ACTIVE_STATUS);
			} else {
				userProfile.setActiveStatus(UserProfile.INACTIVE_STATUS);
			}
			userProfile.setUsername(userRegistration.getUsername().toLowerCase());
			userProfile.setEmail(userRegistration.getEmail());
			userProfile.setFirstName(userRegistration.getFirstName());
			userProfile.setLastName(userRegistration.getLastName());
			userProfile.setOrganization(userRegistration.getOrganization());
			userProfile.setPhone(userRegistration.getPhone());
			userProfile.setPositionTitle(userRegistration.getPositionTitle());
			if (StringUtils.isNotBlank(userRegistration.getUserTypeCode())) {
				userProfile.setUserTypeCode(userRegistration.getUserTypeCode());
			} else {
				userProfile.setUserTypeCode(UserTypeCode.END_USER);
			}
			userProfile.setNotifyOfNew(Boolean.FALSE);
			UserProfile savedUserProfile = getUserService().saveUserProfile(userProfile);

			userRegistration.setUserProfileId(savedUserProfile.getInternalGuid());
			persistenceService.persist(userRegistration);

			AlertContext alertContext = new AlertContext();
			alertContext.setAlertType(AlertType.USER_MANAGEMENT);
			alertContext.setDataTrigger(userRegistration);
			getAlertService().checkAlert(alertContext);
			LOG.log(Level.INFO, MessageFormat.format("User {0} was created.", userRegistration.getUsername()));

			if (!autoApproveUser) {
				alertContext = new AlertContext();
				alertContext.setAlertType(AlertType.USER_MANAGEMENT);
				alertContext.setDataTrigger(userSecurity);
				getAlertService().checkAlert(alertContext);
				LOG.log(Level.INFO, MessageFormat.format("User {0} needs admin approval to login.", userRegistration.getUsername()));
			}
		}
		return validationResult;
	}

	@Override
	public void approveRegistration(String username)
	{
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username.toLowerCase());
		userSecurity = userSecurity.findProxy();
		if (userSecurity != null) {
			userSecurity.setActiveStatus(UserSecurity.ACTIVE_STATUS);
			userSecurity.setApprovalStatus(UserApprovalStatus.APPROVED);
			userSecurity.populateBaseUpdateFields();
			persistenceService.persist(userSecurity);

			UserProfile userProfile = new UserProfile();
			userProfile.setUsername(username);
			userProfile = userProfile.find();
			MessageContext messageContent = new MessageContext(userProfile);

			UserApprovedMessageGenerator userApprovedMessageGenerator = new UserApprovedMessageGenerator(messageContent);
			Email email = userApprovedMessageGenerator.generateMessage();
			MailManager.send(email);

			LOG.log(Level.INFO, MessageFormat.format("User {0} was approved by: {1}", username, SecurityUtil.getCurrentUserName()));
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find user to approve", "Check input: " + username);
		}
	}

	@Override
	public String resetPasswordUser(String username, char[] password)
	{
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		String approvalCode = null;

		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username.toLowerCase());
		userSecurity = userSecurity.findProxy();
		if (userSecurity != null) {

			String rawApprovalCode = persistenceService.generateId();

			DefaultPasswordService passwordService = new DefaultPasswordService();
			String encryptedValue = passwordService.encryptPassword(password);
			userSecurity.setTempPassword(encryptedValue);
			userSecurity.setPasswordChangeApprovalCode(rawApprovalCode);
			userSecurity.populateBaseUpdateFields();
			persistenceService.persist(userSecurity);

			//Send the user an email
			UserProfile userProfile = new UserProfile();
			userProfile.setUsername(username.toLowerCase());
			userProfile = userProfile.find();
			if (userProfile != null) {
				if (StringUtils.isNotBlank(userProfile.getEmail())) {
					MessageContext messageContent = new MessageContext(userProfile);
					messageContent.setUserPasswordResetCode(rawApprovalCode);
					ResetPasswordMessageGenerator resetPasswordMessageGenerator = new ResetPasswordMessageGenerator(messageContent);
					Email email = resetPasswordMessageGenerator.generateMessage();
					MailManager.send(email);
				}
			} else {
				throw new OpenStorefrontRuntimeException("Unable to find user profile to reset", "Check input: " + username);
			}

			LOG.log(Level.INFO, MessageFormat.format("User {0} request a password change. Change is awaiting approval by user", username));
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find user to reset", "Check input: " + username);
		}
		return approvalCode;
	}

	@Override
	public boolean approveUserPasswordReset(String approvalCode)
	{
		boolean success = false;

		Objects.requireNonNull(approvalCode);

		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setPasswordChangeApprovalCode(approvalCode);
		userSecurity = userSecurity.findProxy();
		if (userSecurity != null) {
			userSecurity.setPassword(userSecurity.getTempPassword());
			userSecurity.setTempPassword(null);
			userSecurity.setPasswordChangeApprovalCode(null);
			userSecurity.setPasswordUpdateDts(TimeUtil.currentDate());
			userSecurity.setUsingDefaultPassword(Boolean.FALSE);
			userSecurity.populateBaseUpdateFields();
			persistenceService.persist(userSecurity);
			success = true;
		} else {
			LOG.log(Level.WARNING, MessageFormat.format("Unable to find user with password approval code: ", approvalCode));
		}
		return success;
	}

	@Override
	public void adminResetPassword(String username, char[] password)
	{
		Objects.requireNonNull(username);

		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username.toLowerCase());
		userSecurity = userSecurity.findProxy();

		if (userSecurity != null) {
			ValidationResult validationResult = validatePassword(password);
			if (validationResult.valid()) {
				DefaultPasswordService passwordService = new DefaultPasswordService();
				String encryptedValue = passwordService.encryptPassword(password);
				userSecurity.setPassword(encryptedValue);
				userSecurity.setPasswordUpdateDts(TimeUtil.currentDate());
				userSecurity.setUsingDefaultPassword(Boolean.FALSE);
				userSecurity.populateBaseUpdateFields();
				persistenceService.persist(userSecurity);
				LOG.log(Level.INFO, MessageFormat.format("User {0} password was reset by: {1}", username, SecurityUtil.getCurrentUserName()));
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
		username = username.toLowerCase();

		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username);
		userSecurity = userSecurity.findProxy();

		if (userSecurity != null) {
			userSecurity.setActiveStatus(UserSecurity.ACTIVE_STATUS);
			userSecurity.setFailedLoginAttempts(0);
			userSecurity.populateBaseUpdateFields();
			persistenceService.persist(userSecurity);
			LOG.log(Level.INFO, MessageFormat.format("user {0} was unlocked by: {1}", username, SecurityUtil.getCurrentUserName()));
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find user to unlock.", "Check input: " + username);
		}
	}

	@Override
	public void resetFailedAttempts(String username)
	{
		Objects.requireNonNull(username);
		username = username.toLowerCase();

		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username);
		userSecurity = userSecurity.findProxy();

		if (userSecurity != null) {
			userSecurity.setFailedLoginAttempts(0);
			userSecurity.populateBaseUpdateFields();
			persistenceService.persist(userSecurity);
			LOG.log(Level.INFO, MessageFormat.format("user {0} failed attempts was reset by: {1}", username, SecurityUtil.getCurrentUserName()));
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find user to reset failed attempts.", "Check input: " + username);
		}
	}

	@Override
	public void disableUser(String username)
	{
		Objects.requireNonNull(username);
		username = username.toLowerCase();

		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username);
		userSecurity = userSecurity.findProxy();

		if (userSecurity != null) {
			userSecurity.setActiveStatus(UserSecurity.INACTIVE_STATUS);
			userSecurity.setFailedLoginAttempts(0);
			userSecurity.populateBaseUpdateFields();
			persistenceService.persist(userSecurity);

			getUserService().deleteProfile(username);

			LOG.log(Level.INFO, MessageFormat.format("User {0} was locked by: {1}", username, SecurityUtil.getCurrentUserName()));
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find user to lock.", "Check input: " + username);
		}
	}

	@Override
	public ValidationResult validateSecurityRoleName(String roleName)
	{
		ValidationResult validationResult = new ValidationResult();

		//check name for uniqueness (case-insensitive)...rare to be case-sensitive in LDAPs
		SecurityRole existing = new SecurityRole();
		existing.setRoleName(roleName.toLowerCase());

		QueryByExample<SecurityRole> example = new QueryByExample<>(existing);
		example.getFieldOptions().put(SecurityRole.FIELD_ROLENAME, new GenerateStatementOptionBuilder().setMethod(GenerateStatementOption.METHOD_LOWER_CASE).build());
		existing = persistenceService.queryOneByExample(example);
		if (existing != null) {
			RuleResult result = new RuleResult();
			result.setFieldName(SecurityRole.FIELD_ROLENAME);
			result.setEntityClassName(SecurityRole.class.getSimpleName());
			result.setMessage("Rolename is already exists");
			result.setValidationRule("Rolename must be unique");
			validationResult.getRuleResults().add(result);
		}

		return validationResult;
	}

	@Override
	public SecurityRole saveSecurityRole(SecurityRole securityRole)
	{
		Objects.requireNonNull(securityRole);

		SecurityRole existing = persistenceService.findById(SecurityRole.class, securityRole.getRoleName());
		if (existing != null) {
			existing.updateFields(securityRole);
			securityRole = persistenceService.persist(existing);
		} else {
			securityRole.populateBaseCreateFields();
			securityRole = persistenceService.persist(securityRole);
		}
		securityRole = persistenceService.unwrapProxyObject(securityRole);
		LOG.log(Level.INFO, MessageFormat.format("Security Role {0} was created/updated by: {1}", securityRole.getRoleName(), SecurityUtil.getCurrentUserName()));
		return securityRole;
	}

	@Override
	public void addUserToRole(String username, String role)
	{
		Objects.requireNonNull(username);
		Objects.requireNonNull(role);

		UserProfile userProfile = new UserProfile();
		userProfile.setUsername(username);
		userProfile = userProfile.findProxy();

		if (userProfile != null) {
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
				userRole.setKeep(Boolean.TRUE);
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

		UserRole userRoleExample = new UserRole();
		userRoleExample.setRole(role);
		userRoleExample.setUsername(username);

		userRoleExample = userRoleExample.findProxy();
		if (userRoleExample != null) {
			persistenceService.delete(userRoleExample);
			LOG.log(Level.INFO, MessageFormat.format("Role {0} was removed from user: {1} by {2}", role, username, SecurityUtil.getCurrentUserName()));
		}
	}

	@Override
	public byte[] applicationCryptKey()
	{
		String key = getSystemService().getPropertyValue(ApplicationProperty.APPLICATION_CRYPT_KEY);
		if (key == null) {
			throw new OpenStorefrontRuntimeException("Crypt key is not set", "Set the application Property (Base64): " + ApplicationProperty.APPLICATION_CRYPT_KEY);
		}
		return Base64.getUrlDecoder().decode(key);
	}

	@Override
	public void deleteSecurityRole(String roleName, String moveUserToRole)
	{
		SecurityRole securityRole = persistenceService.findById(SecurityRole.class, roleName);
		if (securityRole != null) {
			UserRole userRoleExample = new UserRole();
			userRoleExample.setRole(roleName);
			List<UserRole> users = userRoleExample.findByExampleProxy();
			for (UserRole userRole : users) {
				if (StringUtils.isNotBlank(moveUserToRole)) {
					userRole.setRole(moveUserToRole);
					userRole.populateBaseUpdateFields();
					persistenceService.persist(userRole);
				} else {
					persistenceService.delete(userRole);
				}
			}

			String query = "update " + Evaluation.class.getSimpleName() + " set assignedGroup = null where assignedGroup = :rolename";
			Map<String, Object> evalQueryParams = new HashMap<>();
			evalQueryParams.put("rolename", securityRole.getRoleName());

			int updatedCount = persistenceService.runDbCommand(query, evalQueryParams);
			LOG.log(Level.FINE, MessageFormat.format("{0} evaluation(s) were unassigned from  group {1}", new Object[]{updatedCount, securityRole.getRoleName()}));

			getComponentServicePrivate().removeRoleFromComponentType(securityRole.getRoleName());
			LOG.log(Level.FINE, "Removed Role from entry type");

			getWorkPlanService().removeSecurityRole(securityRole.getRoleName());
			LOG.log(Level.FINE, "Removed Role from work plan");

			persistenceService.delete(securityRole);

			LOG.log(Level.INFO, MessageFormat.format("Role {0} was deleted by {2}. "
					+ (StringUtils.isNotBlank(moveUserToRole) ? " users were move to: "
					+ StringUtils.isNotBlank(moveUserToRole) : ""), roleName, SecurityUtil.getCurrentUserName()));
		}
	}

	@Override
	public UserContext getUserContext(String username)
	{
		UserContext userContext = null;

		UserProfile userProfile = new UserProfile();
		userProfile.setUsername(username);
		userProfile = userProfile.find();
		if (userProfile != null) {
			userContext = new UserContext();
			userContext.setUserProfile(userProfile);

			UserRole userRole = new UserRole();
			userRole.setActiveStatus(UserRole.ACTIVE_STATUS);
			userRole.setUsername(username);

			List<UserRole> roles = userRole.findByExample();
			List<SecurityRole> securityRoles = new ArrayList<>();
			for (UserRole role : roles) {
				SecurityRole securityRole = new SecurityRole();
				securityRole.setRoleName(role.getRole());
				securityRole = securityRole.find();
				securityRoles.add(securityRole);
			}

			SecurityRole defaultRole = new SecurityRole();
			defaultRole.setRoleName(SecurityRole.DEFAULT_GROUP);
			defaultRole = defaultRole.find();
			if (defaultRole != null) {
				securityRoles.add(defaultRole);
			}
			userContext.getRoles().addAll(securityRoles);
		} else {
			throw new OpenStorefrontRuntimeException("User doesn't exist: " + username, "User may have be deleted; relogin.");
		}
		return userContext;
	}

	@Override
	public UserSecurityWrapper getUserViews(UserFilterParams queryParams)
	{
		UserSecurityWrapper userSecurityWrapper = new UserSecurityWrapper();

		List<UserSecurityView> views = new ArrayList<>();

		//post filter
		UserSecurity userSecurityExample = new UserSecurity();
		if (Convert.toBoolean(queryParams.getAll()) == false) {
			userSecurityExample.setActiveStatus(queryParams.getStatus());
		}
		userSecurityExample.setApprovalStatus(queryParams.getApprovalState());

		QueryByExample<UserSecurity> queryByExample = new QueryByExample<>(userSecurityExample);

		if (StringUtils.isNotBlank(queryParams.getSearchField())
				&& StringUtils.isNotBlank(queryParams.getSearchValue())
				&& UserSecurity.FIELD_USERNAME.equals(queryParams.getSearchField())) {
			UserSecurity userSecuritySearchExample = new UserSecurity();
			try {
				BeanUtils.setProperty(userSecuritySearchExample, queryParams.getSearchField(), queryParams.getSearchValue().toLowerCase() + "%");

				SpecialOperatorModel<UserSecurity> specialOperatorModel = new SpecialOperatorModel<>();
				specialOperatorModel.setExample(userSecuritySearchExample);
				specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LIKE);
				specialOperatorModel.getGenerateStatementOption().setMethod(GenerateStatementOption.METHOD_LOWER_CASE);
				queryByExample.getExtraWhereCauses().add(specialOperatorModel);

			} catch (IllegalAccessException | InvocationTargetException ex) {
				LOG.log(Level.WARNING, MessageFormat.format("Unable to search user profiles by field: {0}", queryParams.getSearchField()));
			}
		}

		List<UserSecurity> users = persistenceService.queryByExample(queryByExample);
		if (!users.isEmpty()) {

			Set<String> usernames = users.stream()
					.map(u -> u.getUsername())
					.collect(Collectors.toSet());

			String query = "select from " + UserProfile.class.getSimpleName()
					+ " where username in :usernameList ";

			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("usernameList", usernames);

			if (StringUtils.isNotBlank(queryParams.getSearchField())
					&& StringUtils.isNotBlank(queryParams.getSearchValue())
					&& !UserSecurity.FIELD_USERNAME.equals(queryParams.getSearchField())) {
				query += " and " + queryParams.getSearchField() + ".toLowerCase() like :searchValue";
				parameterMap.put("searchValue", queryParams.getSearchValue().toLowerCase() + "%");
			}

			List<UserProfile> userProfiles = persistenceService.query(query, parameterMap);
			Map<String, List<UserProfile>> profileMap = userProfiles.stream().collect(Collectors.groupingBy(UserProfile::getUsername));
			for (UserSecurity userSecurity : users) {
				List<UserProfile> profiles = profileMap.getOrDefault(userSecurity.getUsername(), new ArrayList<>());
				if (!profiles.isEmpty()) {
					UserSecurityView view = UserSecurityView.toView(userSecurity, profiles.get(0));
					views.add(view);
				}
			}
			userSecurityWrapper.setTotalNumber(views.size());
			views = queryParams.filter(views);
			userSecurityWrapper.setData(views);
		}

		return userSecurityWrapper;
	}

	@Override
	public void deleteUserRegistration(String userRegistrationId)
	{
		UserRegistration userRegistration = new UserRegistration();
		userRegistration.setRegistrationId(userRegistrationId);
		persistenceService.deleteByExample(userRegistration);
	}

	@Override
	public void deleteUser(String username)
	{
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setUsername(username);
		userSecurity = userSecurity.findProxy();
		if (userSecurity != null) {

			UserRole userRole = new UserRole();
			userRole.setUsername(username);
			persistenceService.deleteByExample(userRole);
			UserProfile userProfile = persistenceService.findById(UserProfile.class, username);
			if (userProfile != null) {
				UserRegistration userRegistration = new UserRegistration();
				userRegistration.setUserProfileId(userProfile.getInternalGuid());
				persistenceService.deleteByExample(userRegistration);
			}

			getUserService().deleteProfile(username);

			//Evaluation assigned to user should be fine and can be reassigned later. Their profile will just be inactive.
			persistenceService.delete(userSecurity);

			LOG.log(Level.INFO, MessageFormat.format("User {0} was deleted by {1}. ", username, SecurityUtil.getCurrentUserName()));
		}
	}

	@Override
	public void updateRoleGroup(String username, Set<String> groups)
	{
		Objects.requireNonNull(username);
		Objects.requireNonNull(groups);

		UserRole userRole = new UserRole();
		userRole.setUsername(username);
		List<UserRole> userRoles = userRole.findByExampleProxy();

		Set<String> keepSet = new HashSet<>();
		for (UserRole role : userRoles) {
			if (!Convert.toBoolean(role.getKeep())) {
				persistenceService.delete(role);
			} else {
				keepSet.add(role.getRole());
			}
		}

		SecurityRole roleExample = new SecurityRole();
		roleExample.setActiveStatus(SecurityRole.ACTIVE_STATUS);

		List<SecurityRole> roles = roleExample.findByExample();
		Set<String> existingRoleSet = roles.stream()
				.map(SecurityRole::getRoleName)
				.collect(Collectors.toSet());

		for (String group : groups) {
			if (existingRoleSet.contains(group)) {
				if (!keepSet.contains(group)) {
					//we just need to add the role without having a security user
					userRole = new UserRole();
					userRole.setRole(group);
					userRole.setUsername(username);
					userRole.setUserRoleId(persistenceService.generateId());
					userRole.populateBaseCreateFields();
					persistenceService.persist(userRole);
				}
			} else {
				LOG.log(Level.FINER, MessageFormat.format("No Matching Role for group: {0}", group));
			}
		}
	}

	@Override
	public void forgotUser(String emailAddress)
	{
		String username = null;

		UserProfile userProfileExample = new UserProfile();
		userProfileExample.setEmail(emailAddress);

		List<UserProfile> userProfiles = userProfileExample.findByExample();
		for (UserProfile userProfile : userProfiles) {
			if (username == null) {
				username = "<b>" + userProfile.getUsername() + "</b>"
						+ " (" + userProfile.getFirstName() + " " + userProfile.getLastName() + ")";
			} else {
				username += "<br> " + "<b>" + userProfile.getUsername() + "</b>"
						+ " (" + userProfile.getFirstName() + " " + userProfile.getLastName() + ")";
			}
		}

		Map<String, Object> data = new HashMap<>();
		String subject = "Forgot Username";
		data.put("username", username);
		data.put("replyName", PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MAIL_REPLY_NAME));
		data.put("replyAddress", PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MAIL_REPLY_ADDRESS));
		data.put("title", subject);
		Email email = MailManager.newTemplateEmail(MailManager.Templates.FORGOT_USERNAME.toString(), data);
		email.setSubject(subject);
		email.addRecipient("", emailAddress, Message.RecipientType.TO);
		MailManager.send(email, true);

	}

	@Override
	public UserContext getGuestContext()
	{
		UserContext userContext = new UserContext();
		userContext.setGuest(true);
		userContext.setUserProfile(new UserProfile());
		userContext.getUserProfile().setActiveStatus(UserProfile.INACTIVE_STATUS);
		userContext.getUserProfile().setFirstName("Guest");
		userContext.getUserProfile().setUsername(OpenStorefrontConstant.ANONYMOUS_USER);

		SecurityRole guestRole = new SecurityRole();
		guestRole.setRoleName(SecurityRole.GUEST_GROUP);
		guestRole = guestRole.find();
		if (guestRole != null) {
			userContext.getRoles().add(guestRole);
		}
		return userContext;
	}

	@Override
	public UserContext getSystemContext()
	{
		UserContext userContext = new UserContext();
		userContext.setSystemUser(true);
		userContext.setUserProfile(new UserProfile());
		userContext.getUserProfile().setActiveStatus(UserProfile.INACTIVE_STATUS);
		userContext.getUserProfile().setFirstName(OpenStorefrontConstant.SYSTEM_USER);
		userContext.getUserProfile().setUsername(OpenStorefrontConstant.SYSTEM_USER);

		//psuedo role with all permission and access to all data
		SecurityRole systemRole = new SecurityRole();
		systemRole.setAllowUnspecifiedDataSensitivity(Boolean.TRUE);
		systemRole.setAllowUnspecifiedDataSource(Boolean.TRUE);
		systemRole.setRoleName("SPECIAL SYSTEM GROUP");
		systemRole.setDescription("The group is only for the System");

		List<SecurityPermission> permissions = getLookupService().findLookup(SecurityPermission.class);
		systemRole.setPermissions(new ArrayList<>());
		for (SecurityPermission permission : permissions) {
			SecurityRolePermission rolePermission = new SecurityRolePermission();
			rolePermission.setPermission(permission.getCode());
			systemRole.getPermissions().add(rolePermission);
		}

		List<DataSource> dataSources = getLookupService().findLookup(DataSource.class);
		systemRole.setDataSecurity(new ArrayList<>());
		for (DataSource dataSource : dataSources) {
			SecurityRoleData securityRoleData = new SecurityRoleData();
			securityRoleData.setDataSource(dataSource.getCode());
			systemRole.getDataSecurity().add(securityRoleData);
		}

		List<DataSensitivity> dataSensitivities = getLookupService().findLookup(DataSensitivity.class);
		for (DataSensitivity dataSensitivity : dataSensitivities) {
			SecurityRoleData securityRoleData = new SecurityRoleData();
			securityRoleData.setDataSensitivity(dataSensitivity.getCode());
			systemRole.getDataSecurity().add(securityRoleData);
		}

		userContext.getRoles().add(systemRole);

		return userContext;
	}

}
