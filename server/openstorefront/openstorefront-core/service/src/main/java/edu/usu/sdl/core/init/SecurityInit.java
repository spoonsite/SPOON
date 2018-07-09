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
package edu.usu.sdl.core.init;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.core.entity.ApplicationProperty;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.SecurityRole;
import edu.usu.sdl.openstorefront.core.entity.SecurityRolePermission;
import edu.usu.sdl.openstorefront.core.entity.UserRegistration;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.shiro.crypto.AesCipherService;

/**
 *
 * @author dshurtleff
 */
public class SecurityInit
		extends ApplyOnceInit
{

	private static final Logger LOG = Logger.getLogger(SecurityInit.class.getName());

	public SecurityInit()
	{
		super("Security-Init-v2");
	}

	@Override
	protected String internalApply()
	{
		AesCipherService cipherService = new AesCipherService();
		Key key = cipherService.generateNewKey();
		byte[] keyBytes = key.getEncoded();
		String cryptKey = Base64.getUrlEncoder().encodeToString(keyBytes);
		service.getSystemService().saveProperty(ApplicationProperty.APPLICATION_CRYPT_KEY, cryptKey);
		LOG.log(Level.CONFIG, "Setup Crypt Key");

		// remove all existing default roles and init default user and roles for system
		SecurityRole exampleRole = new SecurityRole();
		List<String> rolesToBeRemoved = Arrays.asList(SecurityRole.DEFAULT_GROUP, SecurityRole.GUEST_GROUP, 
		SecurityRole.ADMIN_ROLE, SecurityRole.EVALUATOR_ROLE, SecurityRole.LIBRARIAN_ROLE);
		exampleRole.findByExample().forEach(role -> {
			if (rolesToBeRemoved.contains(role.getRoleName())) {
				role.delete();
			}
		});

		// =================================
		//  D E F A U L T - R O L E (DONE)
		// =================================
		SecurityRole securityRole = new SecurityRole();
		securityRole.setRoleName(SecurityRole.DEFAULT_GROUP);
		securityRole.setAllowUnspecifiedDataSensitivity(Boolean.TRUE);
		securityRole.setAllowUnspecifiedDataSource(Boolean.TRUE);
		securityRole.setDescription("Default group that all users belong to.");
		securityRole.setLandingPage("/");

		List<SecurityRolePermission> permissions = new ArrayList<>();
		final List<String> permissionsToAdd = new ArrayList<>();

		// iterate through each security permission lookup and use the records
		//		that use the default role
		service.getLookupService().findLookup(SecurityPermission.class).forEach(item -> {
			if (item.getDefaultRoles() != null && item.getDefaultRoles().contains(SecurityRole.DEFAULT_GROUP)) {
				permissionsToAdd.add(item.getCode());
			}
		});

		for (String newPermission : permissionsToAdd) {
			SecurityRolePermission permission = new SecurityRolePermission();
			permission.setPermission(newPermission);
			permissions.add(permission);
		}
		securityRole.setPermissions(permissions);
		service.getSecurityService().saveSecurityRole(securityRole);
		LOG.log(Level.CONFIG, "Setup default group");

		// =================================
		// G U E S T - R O L E (DONE)
		// =================================
		securityRole = new SecurityRole();
		securityRole.setRoleName(SecurityRole.GUEST_GROUP);
		securityRole.setAllowUnspecifiedDataSensitivity(Boolean.TRUE);
		securityRole.setAllowUnspecifiedDataSource(Boolean.TRUE);
		securityRole.setDescription("Used for guests when allowed based on URL");
		securityRole.setLandingPage("/");
		service.getSecurityService().saveSecurityRole(securityRole);
		LOG.log(Level.CONFIG, "Guest group was setup");

		// =================================
		// A D M I N - R O L E (DONE)
		// =================================
		String adminRoleName = PropertiesManager.getInstance().getValue(
				PropertiesManager.KEY_SECURITY_DEFAULT_ADMIN_GROUP,
				SecurityRole.ADMIN_ROLE
		);

		securityRole = new SecurityRole();
		securityRole.setRoleName(adminRoleName);
		securityRole.setAllowUnspecifiedDataSensitivity(Boolean.TRUE);
		securityRole.setAllowUnspecifiedDataSource(Boolean.TRUE);
		securityRole.setDescription("Super admin group");
		securityRole.setLandingPage("/");

		// add admin permissions (all the permissions)
		permissions = new ArrayList<>();
		permissionsToAdd.clear();
		service.getLookupService().findLookup(SecurityPermission.class).forEach(item -> {
			permissionsToAdd.add(item.getCode());
		});

		for (String newPermission : permissionsToAdd) {
			SecurityRolePermission permission = new SecurityRolePermission();
			permission.setPermission(newPermission);
			permissions.add(permission);
		}
		securityRole.setPermissions(permissions);
		service.getSecurityService().saveSecurityRole(securityRole);
		LOG.log(Level.CONFIG, "Setup Admin group");

		//just for backward comp add STORE-Admin
		String oldAdminGroupName = "STORE-Admin";
		securityRole.setRoleName(oldAdminGroupName);
		service.getSecurityService().saveSecurityRole(securityRole);
		LOG.log(Level.CONFIG, "Setup Extra Admin group");

		// =================================
		// E V A L U A T O R - G R O U P (DONE)
		// =================================
		String evaluatorRoleName = SecurityRole.EVALUATOR_ROLE;
		securityRole = new SecurityRole();
		securityRole.setRoleName(evaluatorRoleName);
		securityRole.setAllowUnspecifiedDataSensitivity(Boolean.TRUE);
		securityRole.setAllowUnspecifiedDataSource(Boolean.TRUE);
		securityRole.setDescription("Evaluators only group");
		securityRole.setLandingPage("/");

		permissions = new ArrayList<>();
		permissionsToAdd.clear();

		// iterate through each security permission lookup and use the records
		//		that use the evaluator role
		service.getLookupService().findLookup(SecurityPermission.class).forEach(item -> {
			if (item.getDefaultRoles() != null && item.getDefaultRoles().contains(SecurityRole.EVALUATOR_ROLE)) {
				permissionsToAdd.add(item.getCode());
			}
		});

		for (String newPermission : permissionsToAdd) {
			SecurityRolePermission permission = new SecurityRolePermission();
			permission.setPermission(newPermission);
			permissions.add(permission);
		}
		securityRole.setPermissions(permissions);
		service.getSecurityService().saveSecurityRole(securityRole);
		LOG.log(Level.CONFIG, "Setup Evaluator group");

		// =================================
		// L I B R A R I A N (DONE)
		// =================================
		String libRoleName = SecurityRole.LIBRARIAN_ROLE;
		securityRole = new SecurityRole();
		securityRole.setRoleName(libRoleName);
		securityRole.setAllowUnspecifiedDataSensitivity(Boolean.TRUE);
		securityRole.setAllowUnspecifiedDataSource(Boolean.TRUE);
		securityRole.setDescription("Librarian - Data management group");
		securityRole.setLandingPage("/");

		permissions = new ArrayList<>();
		permissionsToAdd.clear();

		// iterate through each security permission lookup and use the records
		//		that use the evaluator role
		service.getLookupService().findLookup(SecurityPermission.class).forEach(item -> {
			if (item.getDefaultRoles() != null && item.getDefaultRoles().contains(SecurityRole.LIBRARIAN_ROLE)) {
				permissionsToAdd.add(item.getCode());
			}
		});

		for (String newPermission : permissionsToAdd) {
			SecurityRolePermission permission = new SecurityRolePermission();
			permission.setPermission(newPermission);
			permissions.add(permission);
		}
		securityRole.setPermissions(permissions);
		service.getSecurityService().saveSecurityRole(securityRole);
		LOG.log(Level.CONFIG, "Setup Librarian group");

		//Admin Registration
		String adminUsername = "admin";
		UserRegistration userRegistration = new UserRegistration();
		userRegistration.setEmail("na");
		userRegistration.setFirstName("Local");
		userRegistration.setLastName("Admin");
		userRegistration.setOrganization("Storefront");
		userRegistration.setPassword("Secret1@");
		userRegistration.setPhone("na");
		userRegistration.setUserTypeCode(UserTypeCode.END_USER);
		userRegistration.setUsername(adminUsername);
		userRegistration.setUsingDefaultPassword(Boolean.TRUE);
		service.getSecurityService().processNewRegistration(userRegistration, false);
		service.getSecurityServicePrivate().processNewUser(userRegistration, true);
		LOG.log(Level.CONFIG, "Register Admin User");

		//approve admin user
		service.getSecurityService().approveRegistration(adminUsername);
		LOG.log(Level.CONFIG, "Approved Admin User");

		//Add to admin role
		service.getSecurityService().addUserToRole(adminUsername, adminRoleName);
		service.getSecurityService().addUserToRole(adminUsername, oldAdminGroupName);
		LOG.log(Level.CONFIG, "Add Admin User to Admin Group");

		return "Setup Security";
	}

	@Override
	public int getPriority()
	{
		return 15;
	}

}
