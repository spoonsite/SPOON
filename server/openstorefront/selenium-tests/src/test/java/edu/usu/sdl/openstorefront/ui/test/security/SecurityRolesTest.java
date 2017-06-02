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
	
	// establish order when just running the SecurityRolesTest class/ file
	@Test
	public void runSecurityRolesTest() throws InterruptedException{
		signupForAccounts();
		addSecurityRole();
		setSecurityRoles();
	//	importDataRestrictionEntries ();
	//	verifyPermissions();
	}
	
	public void signupForAccounts() throws InterruptedException{
		AccountSignupActivateTest newAccountSignup = new AccountSignupActivateTest();
		
		// Create new accounts and activate.  Log on as user then log back on as admin
		// TODO:  *** BREAK OUT INTO METHODS (DELETE, SIGNUPFORM, ACTIVATE)? ***
	//	newAccountSignup.signupActivate("autoUser");
	//	newAccountSignup.signupActivate("autoEval");
	//	newAccountSignup.signupActivate("autoAdmin");
	//	newAccountSignup.signupActivate("autoLibrarian");
	}
	
	public void addSecurityRole () throws InterruptedException {
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			NewSecurityRole newSecurityRole = new NewSecurityRole();

			// Set up new Security Role, add user to role
		//	newSecurityRole.deleteRoleIfPresent(driver, "AUTO-User");
		//	newSecurityRole.addRoleBasic(driver, "AUTO-User");
		//	newSecurityRole.addUserToRole(driver, "AUTO-User", "autoUser");
		}
	}
	
	public void setSecurityRoles () throws InterruptedException {
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			NewSecurityRole newSecurityRole = new NewSecurityRole();
		
			// Set up Permissions to use
			
			
			
		//	newSecurityRole.managePermissions(driver, roleName, permissions);
			

			// dataSource options: "DI2E", "ER2"
			dataSource.put("DI2E", true);
			dataSource.put("ER2", false);
			newSecurityRole.manageDataSources(driver, "AUTO-User", dataSource);

			// dataSens options: "DISTROA", "DISTROB", "DISTROC", "DISTROD", "DISTROE", "DISTROF",
			//					 "ITAR", "PUBLIC", "SENSITIVE"
			dataSens.put("DISTROA", true);
			dataSens.put("DISTROB", false);
			dataSens.put("DISTROC", true);
			dataSens.put("DISTROD", false);
			dataSens.put("DISTROE", true);
			dataSens.put("DISTROF", false);
			dataSens.put("ITAR", true);
			dataSens.put("PUBLIC", false);
			dataSens.put("SENSITIVE", true);
		//	newSecurityRole.manageDataSensitivity(driver, "AUTO-User", dataSens);
		}
	}
	
    public void importDataRestrictionEntries () {
    
    }
	
	public void verifyPermissions () {
		
	}

}	
