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
		
			/* permissions	"ADMIN-ALERT-MANAGEMENT", "ADMIN-ATTRIBUTE-MANAGEMENT", "ADMIN-BRANDING", "ADMIN-CONTACT-MANAGEMENT",
				options:	"ADMIN-DATA-IMPORT-EXPORT", "ADMIN-ENTRY-MANAGEMENT", "ADMIN-ENTRY-TYPES", "ADMIN-ENTRY-TEMPLATES", 
							"ADMIN-EVALUATION-MANAGEMENT",	"ADMIN-EVALUATION-TEMPLATE", "ADMIN-EVALUATION-TEMPLATE-CHECKLIST", 
							"ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION", "ADMIN-EVALUATION-TEMPLATE-SECTION", "ADMIN-FEEDBACK",
							"ADMIN-HIGHLIGHTS", ADMIN-INTEGRATION", "ADMIN-JOB-MANAGEMENT", "ADMIN-LOOKUPS", "ADMIN-MEDIA", 
							"ADMIN-MESSAGE-MANAGEMENT", "ADMIN-ORGANIZATION", "ADMIN-QUESTIONS", "ADMIN-REVIEW", "ADMIN-SEARCH", 
							"ADMIN-SECURITY", "ADMIN-ROLE-MANAGEMENT", "ADMIN-SYSTEM-MANAGEMENT", "ADMIN-TEMPMEDIA-MANAGEMENT", 
							"ADMIN-TRACKING", "ADMIN-USER-MANAGEMENT", "ADMIN-USER-MANAGEMENT-PROFILES", "ADMIN-WATCHES", 
							"ADMIN-ORGANIZATION-EXTRACTION", "API-DOCS", "ENTRY-TAG", "EVALUATIONS", "RELATION-VIEW-TOOL", 
							"REPORTS", "REPORTS-ALL", "REPORTS-SCHEDULE", "USER-SUBMISSIONS"
			*/						
			permissions.put("ADMIN-ALERT-MANAGEMENT", false);
			permissions.put("ADMIN-ATTRIBUTE-MANAGEMENT", false);
			permissions.put("ADMIN-BRANDING", false);
			permissions.put("ADMIN-CONTACT-MANAGEMENT", false);
			permissions.put("ADMIN-DATA-IMPORT-EXPORT", false);
			permissions.put("ADMIN-ENTRY-MANAGEMENT", false);
			permissions.put("ADMIN-ENTRY-TYPES", false);
			permissions.put("ADMIN-ENTRY-TEMPLATES", false);
			permissions.put("ADMIN-EVALUATION-MANAGEMENT", false);
			permissions.put("ADMIN-EVALUATION-TEMPLATE", false);
			permissions.put("ADMIN-EVALUATION-TEMPLATE-CHECKLIST", false);
			permissions.put("ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION", false);
			permissions.put("ADMIN-EVALUATION-TEMPLATE-SECTION", false);
			permissions.put("ADMIN-FEEDBACK", false);
			permissions.put("ADMIN-HIGHLIGHTS", false);
			permissions.put("ADMIN-INTEGRATION", false);
			permissions.put("ADMIN-JOB-MANAGEMENT", false);
			permissions.put("ADMIN-LOOKUPS", false);
			permissions.put("ADMIN-MEDIA", false);
			permissions.put("ADMIN-MESSAGE-MANAGEMENT", false);
			permissions.put("ADMIN-ORGANIZATION", false);
			permissions.put("ADMIN-QUESTIONS", false);
			permissions.put("ADMIN-REVIEW", false);
			permissions.put("ADMIN-SEARCH", false);
			permissions.put("ADMIN-SECURITY", false);
			permissions.put("ADMIN-ROLE-MANAGEMENT", false);
			permissions.put("ADMIN-SYSTEM-MANAGEMENT", false);
			permissions.put("ADMIN-TEMPMEDIA-MANAGEMENT", false);
			permissions.put("ADMIN-TRACKING", false);
			permissions.put("ADMIN-USER-MANAGEMENT", false);
			permissions.put("ADMIN-USER-MANAGEMENT-PROFILES", false);
			permissions.put("ADMIN-WATCHES", false);
			permissions.put("ADMIN-ORGANIZATION-EXTRACTION", false);
			permissions.put("API-DOCS", false);
			permissions.put("ENTRY-TAG", false);
			permissions.put("EVALUATIONS", false);
			permissions.put("RELATION-VIEW-TOOL", false);
			permissions.put("REPORTS", false);
			permissions.put("REPORTS-ALL", false);
			permissions.put("REPORTS-SCHEDULE", false);
			permissions.put("USER-SUBMISSIONS", false);

			newSecurityRole.managePermissions(driver, "AUTO-User", permissions);


			// dataSource options: "DI2E", "ER2"
			dataSource.put("DI2E", false);
			dataSource.put("ER2", false);

	//		newSecurityRole.manageDataSources(driver, "AUTO-User", dataSource);

			
			// dataSens options: "DISTROA", "DISTROB", "DISTROC", "DISTROD", "DISTROE", "DISTROF",
			//					 "ITAR", "PUBLIC", "SENSITIVE"
			dataSens.put("DISTROA", true);
			dataSens.put("DISTROB", true);
			dataSens.put("DISTROC", true);
			dataSens.put("DISTROD", true);
			dataSens.put("DISTROE", true);
			dataSens.put("DISTROF", true);
			dataSens.put("ITAR", true);
			dataSens.put("PUBLIC", true);
			dataSens.put("SENSITIVE", true);
			
		//	newSecurityRole.manageDataSensitivity(driver, "AUTO-User", dataSens);
		}
	}
	
    public void importDataRestrictionEntries () {
    
    }
	
	public void verifyPermissions () {
		
	}

}	
