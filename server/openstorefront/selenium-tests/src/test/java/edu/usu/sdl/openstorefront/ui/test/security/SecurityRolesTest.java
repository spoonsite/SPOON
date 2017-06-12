package edu.usu.sdl.openstorefront.ui.test.security;

import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

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
/**
 *
 * @author besplin
 */
public class SecurityRolesTest
		extends NewSecurityRole
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

	// located in SecurityTestBase, read this in to be used in setSecurityRoles method below
	@Before
	public void setupSecurityRoles()
	{
		defineCustomizedSecurityRoles();
		defineDefaultSecurityRoles();
		defineAdminSecurityRoles();
		defineEvaluatorSecurityRoles();
		defineLibrarianSecurityRoles();
		defineUserSecurityRoles();
	}

	// establish order when just running the SecurityRolesTest class/ file
	@Test
	public void runSecurityRolesTest() throws InterruptedException
	{
		signupForAccounts();
		addSecurityRole();
		setSecurityRoles();
		//	importDataRestrictionEntries ();
		//	verifyPermissions();
	}

	public void signupForAccounts() throws InterruptedException
	{
		AccountSignupActivateTest newAccountSignup = new AccountSignupActivateTest();

		// Create new accounts and activate.  Log on as user then log back on as admin
		// NOTE-- usernames get converted to lower case
		newAccountSignup.signupActivate("auto-customized-user");
		newAccountSignup.signupActivate("auto-admin-user");
//		newAccountSignup.signupActivate("auto-eval-user");
//		newAccountSignup.signupActivate("auto-librarian-user");
//		newAccountSignup.signupActivate("auto-user-user");
	}

	public void addSecurityRole() throws InterruptedException
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			NewSecurityRole newSecurityRole = new NewSecurityRole();

			// Set up new Security Role, add user to role
			newSecurityRole.deleteRoleIfPresent(driver, "auto-customized-role");
			newSecurityRole.addSecurityRole(driver, "auto-customized-role", true, true);

			// ************ DO THIS FROM USER MANAGEMENT? *************
//			newSecurityRole.addUserToRole(driver, "auto-user-role", "autoUser");

			// REPEAT HERE FOR OTHER ROLES AND USERS
			newSecurityRole.deleteRoleIfPresent(driver, "auto-admin-role");
			newSecurityRole.addSecurityRole(driver, "auto-admin-role", true, true);

		}
	}

	public void setSecurityRoles() throws InterruptedException
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			NewSecurityRole newSecurityRole = new NewSecurityRole();

			// For the "Role", Set Data Sources, Data Sensitivies, and Permissions
			// Change this one for a quick test
			newSecurityRole.setDataSources(driver, "auto-customized-role", customizedDataSources);
			newSecurityRole.setDataSensitivity(driver, "auto-customized-role", customizedDataSensitivities);
			newSecurityRole.setPermissions(driver, "auto-customized-role", customizedPermissions);

			// DO NOT Change these
			newSecurityRole.setDataSources(driver, "DEFAULT-GROUP", defaultDataSources);
			newSecurityRole.setDataSensitivity(driver, "DEFAULT-GROUP", defaultDataSensitivities);
			newSecurityRole.setPermissions(driver, "DEFAULT-GROUP", defaultPermissions);

			newSecurityRole.setDataSources(driver, "auto-admin-role", adminDataSources);
			newSecurityRole.setDataSensitivity(driver, "auto-admin-role", adminDataSensitivities);
			newSecurityRole.setPermissions(driver, "auto-admin-role", adminPermissions);

			newSecurityRole.setDataSources(driver, "auto-evaluator-role", evaluatorDataSources);
			newSecurityRole.setDataSensitivity(driver, "auto-evaluator-role", evaluatorDataSensitivities);
			newSecurityRole.setPermissions(driver, "auto-evaluator-role", evaluatorPermissions);

			newSecurityRole.setDataSources(driver, "auto-librarian-role", librarianDataSources);
			newSecurityRole.setDataSensitivity(driver, "auto-librarian-role", librarianDataSensitivities);
			newSecurityRole.setPermissions(driver, "auto-librarian-role", librarianPermissions);

			newSecurityRole.setDataSources(driver, "auto-user-role", userDataSources);
			newSecurityRole.setDataSensitivity(driver, "auto-user-role", userDataSensitivities);
			newSecurityRole.setPermissions(driver, "auto-user-role", userPermissions);
		}
	}

	public void importDataRestrictionEntries()
	{

	}

	public void verifyPermissions()
	{

	}

}
