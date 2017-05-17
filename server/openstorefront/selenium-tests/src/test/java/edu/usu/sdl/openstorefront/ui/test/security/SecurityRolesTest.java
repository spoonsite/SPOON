package edu.usu.sdl.openstorefront.ui.test.security;

import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.logging.Logger;
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
		
	@Test
	public void signupForAccounts() throws InterruptedException{
		AccountSignupActivateTest newAccountSignup = new AccountSignupActivateTest();
/*		
		// Create new accounts and activate.  Log on as user then log back on as admin
		// TODO:  *** BREAK OUT INTO METHODS (DELETE, SIGNUPFORM, ACTIVATE)? ***
		newAccountSignup.signupActivate("autoUser");
		newAccountSignup.signupActivate("autoEval");
		newAccountSignup.signupActivate("autoAdmin");
		newAccountSignup.signupActivate("autoLibrarian");
*/
	}
	
	@Test
    public void SecurityRole () throws InterruptedException {
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			NewSecurityRole newSecurityRole = new NewSecurityRole();
/*
			// Set up new Security Role, add user to role
			newSecurityRole.deleteRoleIfPresent(driver, "AUTO-User");
			newSecurityRole.addRoleBasic(driver, "AUTO-User");
			newSecurityRole.addUserToRole(driver, "AUTO-User", "autoUser");
*/
		}
	}
	
	@Test
	public void setSecurityRoles () throws InterruptedException {
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			NewSecurityRole newSecurityRole = new NewSecurityRole();
			// Set up Permissions to use
			//newSecurityRole.managePermissions(driver, roleName, permissions);


			// Set up Data Sources to use
			dataSource.put("DI2E", true);
			dataSource.put("ER2", true);
			newSecurityRole.manageDataSources(driver, "AUTO-User", dataSource);


			// Set up Data Sensitivities to use
			//newSecurityRole.manageDataSensitivity(driver, roleName, dataSensitivity);
		}
	}
	
    @Test
    public void importDataRestrictionEntries () {
    
    }
	
	@Test
	public void verifyPermissions () {
		
	}

}	
