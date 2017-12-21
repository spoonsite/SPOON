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
		super("Security-Init");
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

		//init default user and roles for system
		//Default Role
		SecurityRole securityRole = new SecurityRole();
		securityRole.setRoleName(SecurityRole.DEFAULT_GROUP);
		securityRole.setAllowUnspecifiedDataSensitivity(Boolean.TRUE);
		securityRole.setAllowUnspecifiedDataSource(Boolean.TRUE);
		securityRole.setDescription("Default group that all users belong to.");
		securityRole.setLandingPage("/");

		List<SecurityRolePermission> permissions = new ArrayList<>();
		List<String> permissionsToAdd = Arrays.asList(
				SecurityPermission.ENTRY_TAG,
				SecurityPermission.REPORTS_SCHEDULE,
				SecurityPermission.REPORTS,
				SecurityPermission.USER_SUBMISSIONS,
				SecurityPermission.RELATIONSHIP_VIEW_TOOL
		);

		for (String newPermission : permissionsToAdd) {
			SecurityRolePermission permission = new SecurityRolePermission();
			permission.setPermission(newPermission);
			permissions.add(permission);
		}
		securityRole.setPermissions(permissions);
		service.getSecurityService().saveSecurityRole(securityRole);
		LOG.log(Level.CONFIG, "Setup default group");

		//Guest Role
		securityRole = new SecurityRole();
		securityRole.setRoleName(SecurityRole.GUEST_GROUP);
		securityRole.setAllowUnspecifiedDataSensitivity(Boolean.TRUE);
		securityRole.setAllowUnspecifiedDataSource(Boolean.TRUE);
		securityRole.setDescription("Used for guests when allowed based on URL");
		securityRole.setLandingPage("/");
		service.getSecurityService().saveSecurityRole(securityRole);
		LOG.log(Level.CONFIG, "Guest group was setup");

		//Admin Role
		String adminRoleName = PropertiesManager.getValue(
				PropertiesManager.KEY_SECURITY_DEFAULT_ADMIN_GROUP,
				"STOREFRONT-Admin"
		);

		securityRole = new SecurityRole();
		securityRole.setRoleName(adminRoleName);
		securityRole.setAllowUnspecifiedDataSensitivity(Boolean.TRUE);
		securityRole.setAllowUnspecifiedDataSource(Boolean.TRUE);
		securityRole.setDescription("Super admin group");
		securityRole.setLandingPage("/");

		permissions = new ArrayList<>();
		permissionsToAdd = Arrays.asList(SecurityPermission.ADMIN_ALERT_MANAGEMENT,
				SecurityPermission.ADMIN_ATTRIBUTE_MANAGEMENT,
				SecurityPermission.ADMIN_BRANDING,
				SecurityPermission.ADMIN_CONTACT_MANAGEMENT,
				SecurityPermission.ADMIN_DATA_IMPORT_EXPORT,
				SecurityPermission.ADMIN_ENTRY_MANAGEMENT,
				SecurityPermission.ADMIN_ENTRY_TEMPLATES,
				SecurityPermission.ADMIN_ENTRY_TYPES,
				SecurityPermission.ADMIN_EVALUATION_TEMPLATE,
				SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST,
				SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION,
				SecurityPermission.ADMIN_EVALUATION_TEMPLATE_SECTION,
				SecurityPermission.ADMIN_FEEDBACK,
				SecurityPermission.ADMIN_HIGHLIGHTS,
				SecurityPermission.ADMIN_INTEGRATION,
				SecurityPermission.ADMIN_JOB_MANAGEMENT,
				SecurityPermission.ADMIN_LOOKUPS,
				SecurityPermission.ADMIN_MEDIA,
				SecurityPermission.ADMIN_MESSAGE_MANAGEMENT,
				SecurityPermission.ADMIN_ORGANIZATION,
				SecurityPermission.ADMIN_ORGANIZATION_EXTRACTION,
				SecurityPermission.ADMIN_QUESTIONS,
				SecurityPermission.ADMIN_REVIEW,
				SecurityPermission.ADMIN_SEARCH,
				SecurityPermission.ADMIN_SYSTEM_MANAGEMENT,
				SecurityPermission.ADMIN_TEMPMEDIA_MANAGEMENT,
				SecurityPermission.ADMIN_TRACKING,
				SecurityPermission.ADMIN_USER_MANAGEMENT,
				SecurityPermission.ADMIN_USER_MANAGEMENT_PROFILES,
				SecurityPermission.ADMIN_WATCHES,
				SecurityPermission.EVALUATIONS,
				SecurityPermission.ADMIN_EVALUATION_MANAGEMENT,
				SecurityPermission.API_DOCS,
				SecurityPermission.REPORTS_ALL,
				SecurityPermission.ADMIN_SECURITY,
				SecurityPermission.ADMIN_ROLE_MANAGEMENT
		);

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

		//Evaluator Group
		String evaluatorRoleName = "STOREFRONT-Evaluators";
		securityRole = new SecurityRole();
		securityRole.setRoleName(evaluatorRoleName);
		securityRole.setAllowUnspecifiedDataSensitivity(Boolean.TRUE);
		securityRole.setAllowUnspecifiedDataSource(Boolean.TRUE);
		securityRole.setDescription("Evaluators only group");
		securityRole.setLandingPage("/");

		permissions = new ArrayList<>();
		permissionsToAdd = Arrays.asList(
				SecurityPermission.EVALUATIONS
		);

		for (String newPermission : permissionsToAdd) {
			SecurityRolePermission permission = new SecurityRolePermission();
			permission.setPermission(newPermission);
			permissions.add(permission);
		}
		securityRole.setPermissions(permissions);
		service.getSecurityService().saveSecurityRole(securityRole);
		LOG.log(Level.CONFIG, "Setup Evaluator group");

		//Librarian
		String libRoleName = "STOREFRONT-Librarian";
		securityRole = new SecurityRole();
		securityRole.setRoleName(libRoleName);
		securityRole.setAllowUnspecifiedDataSensitivity(Boolean.TRUE);
		securityRole.setAllowUnspecifiedDataSource(Boolean.TRUE);
		securityRole.setDescription("Librarian - Data management group");
		securityRole.setLandingPage("/");

		permissions = new ArrayList<>();
		permissionsToAdd = Arrays.asList(
				SecurityPermission.ADMIN_ALERT_MANAGEMENT,
				SecurityPermission.ADMIN_ATTRIBUTE_MANAGEMENT,
				SecurityPermission.ADMIN_CONTACT_MANAGEMENT,
				SecurityPermission.ADMIN_DATA_IMPORT_EXPORT,
				SecurityPermission.ADMIN_ENTRY_MANAGEMENT,
				SecurityPermission.ADMIN_ENTRY_TEMPLATES,
				SecurityPermission.ADMIN_ENTRY_TYPES,
				SecurityPermission.ADMIN_EVALUATION_TEMPLATE,
				SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST,
				SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION,
				SecurityPermission.ADMIN_EVALUATION_TEMPLATE_SECTION,
				SecurityPermission.ADMIN_FEEDBACK,
				SecurityPermission.ADMIN_HIGHLIGHTS,
				SecurityPermission.ADMIN_INTEGRATION,
				SecurityPermission.ADMIN_LOOKUPS,
				SecurityPermission.ADMIN_MEDIA,
				SecurityPermission.ADMIN_WATCHES,
				SecurityPermission.ADMIN_EVALUATION_MANAGEMENT,
				SecurityPermission.ADMIN_USER_MANAGEMENT_PROFILES,
				SecurityPermission.EVALUATIONS,
				SecurityPermission.ADMIN_ORGANIZATION,
				SecurityPermission.ADMIN_QUESTIONS,
				SecurityPermission.ADMIN_REVIEW,
				SecurityPermission.ADMIN_SEARCH
		);

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
