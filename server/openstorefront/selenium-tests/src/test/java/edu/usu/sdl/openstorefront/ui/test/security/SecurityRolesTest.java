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
		newAccountSignup.signupActivate("autoCutomized");  // NOT ACTIVATING?!?  Not going to Locked/ Pending (stays on approved)
//		newAccountSignup.signupActivate("autoAdmin");
//		newAccountSignup.signupActivate("autoEval");
//		newAccountSignup.signupActivate("autoLibrarian");
//		newAccountSignup.signupActivate("autoUser");
	}

	public void addSecurityRole() throws InterruptedException
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			NewSecurityRole newSecurityRole = new NewSecurityRole();

			// ***************	CHANGE THIS TO INCLUDE CHECK AND UNCHECK **********************
			// Set up new Security Role, add user to role
//			newSecurityRole.deleteRoleIfPresent(driver, "AUTO-User");
//			newSecurityRole.addRoleBasic(driver, "AUTO-User");
			// ************ DO THIS FROM USER MANAGEMENT? *************
//			newSecurityRole.addUserToRole(driver, "AUTO-User", "autoUser");

//			newSecurityRole.deleteRoleIfPresent(driver, "AUTO-Customized");
//			newSecurityRole.addRoleBasic(driver, "AUTO-Customized");
//			newSecurityRole.addUserToRole(driver, "AUTO-Customized", "autoCustomized");  // NOT ADDING USER TO ROLE
		}
	}

	public void setSecurityRoles() throws InterruptedException
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			NewSecurityRole newSecurityRole = new NewSecurityRole();

			// For the "Role", Set Data Sources, Data Sensitivies, and Permissions
			// Change this one for a quick test
			newSecurityRole.setDataSources(driver, "AUTO-Customized", customizedDataSources);
			newSecurityRole.setDataSensitivity(driver, "AUTO-Customized", customizedDataSensitivities);
			newSecurityRole.setPermissions(driver, "AUTO-Customized", customizedPermissions);

			// DO NOT Change these
			newSecurityRole.setDataSources(driver, "DEFAULT-GROUP", defaultDataSources);
			newSecurityRole.setDataSensitivity(driver, "DEFAULT-GROUP", defaultDataSensitivities);
			newSecurityRole.setPermissions(driver, "DEFAULT-GROUP", defaultPermissions);

			newSecurityRole.setDataSources(driver, "AUTO-Admin", adminDataSources);
			newSecurityRole.setDataSensitivity(driver, "AUTO-Admin", adminDataSensitivities);
			newSecurityRole.setPermissions(driver, "AUTO-Admin", adminPermissions);

			newSecurityRole.setDataSources(driver, "AUTO-Evaluator", evaluatorDataSources);
			newSecurityRole.setDataSensitivity(driver, "AUTO-Evaluator", evaluatorDataSensitivities);
			newSecurityRole.setPermissions(driver, "AUTO-Evaluator", evaluatorPermissions);

			newSecurityRole.setDataSources(driver, "AUTO-Librarian", librarianDataSources);
			newSecurityRole.setDataSensitivity(driver, "AUTO-Librarian", librarianDataSensitivities);
			newSecurityRole.setPermissions(driver, "AUTO-Librarian", librarianPermissions);

			newSecurityRole.setDataSources(driver, "AUTO-User", userDataSources);
			newSecurityRole.setDataSensitivity(driver, "AUTO-User", userDataSensitivities);
			newSecurityRole.setPermissions(driver, "AUTO-User", userPermissions);
		}
	}

	public void importDataRestrictionEntries()
	{

	}

	public void verifyPermissions()
	{

	}

}
