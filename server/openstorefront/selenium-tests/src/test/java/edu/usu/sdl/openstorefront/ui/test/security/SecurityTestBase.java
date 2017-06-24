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
package edu.usu.sdl.openstorefront.ui.test.security;

import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author dshurtleff
 */
public class SecurityTestBase
		extends BrowserTestBase
{

	@BeforeClass
	public static void setupBaseTest()
	{
		login();
	}

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

	protected Map<String, Boolean> customizedDataSources = new HashMap<>();
	protected Map<String, Boolean> customizedDataSensitivities = new HashMap<>();
	protected Map<String, Boolean> customizedPermissions = new HashMap();

	protected Map<String, Boolean> defaultDataSources = new HashMap<>();
	protected Map<String, Boolean> defaultDataSensitivities = new HashMap<>();
	protected Map<String, Boolean> defaultPermissions = new HashMap();

	protected Map<String, Boolean> adminDataSources = new HashMap<>();
	protected Map<String, Boolean> adminDataSensitivities = new HashMap<>();
	protected Map<String, Boolean> adminPermissions = new HashMap();

	protected Map<String, Boolean> evaluatorDataSources = new HashMap<>();
	protected Map<String, Boolean> evaluatorDataSensitivities = new HashMap<>();
	protected Map<String, Boolean> evaluatorPermissions = new HashMap();

	protected Map<String, Boolean> librarianDataSources = new HashMap<>();
	protected Map<String, Boolean> librarianDataSensitivities = new HashMap<>();
	protected Map<String, Boolean> librarianPermissions = new HashMap();

	protected Map<String, Boolean> userDataSources = new HashMap<>();
	protected Map<String, Boolean> userDataSensitivities = new HashMap<>();
	protected Map<String, Boolean> userPermissions = new HashMap();

	public void defineCustomizedSecurityRoles()
	{
		// FEEL FREE to change this one for whatever you need for a customized test

		customizedDataSources.put("DI2E", true);
		customizedDataSources.put("ER2", false);

		customizedDataSensitivities.put("DISTROA", true);
		customizedDataSensitivities.put("DISTROB", false);
		customizedDataSensitivities.put("DISTROC", true);
		customizedDataSensitivities.put("DISTROD", false);
		customizedDataSensitivities.put("DISTROE", true);
		customizedDataSensitivities.put("DISTROF", false);
		customizedDataSensitivities.put("ITAR", true);
		customizedDataSensitivities.put("PUBLIC", false);
		customizedDataSensitivities.put("SENSITIVE", true);

		customizedPermissions.put("ADMIN-ALERT-MANAGEMENT", true);
		customizedPermissions.put("ADMIN-ATTRIBUTE-MANAGEMENT", false);
		customizedPermissions.put("ADMIN-BRANDING", true);
		customizedPermissions.put("ADMIN-CONTACT-MANAGEMENT", false);
		customizedPermissions.put("ADMIN-DATA-IMPORT-EXPORT", true);
		customizedPermissions.put("ADMIN-ENTRY-MANAGEMENT", false);
		customizedPermissions.put("ADMIN-ENTRY-TYPES", true);
		customizedPermissions.put("ADMIN-ENTRY-TEMPLATES", false);
		customizedPermissions.put("ADMIN-EVALUATION-MANAGEMENT", true);
		customizedPermissions.put("ADMIN-EVALUATION-TEMPLATE", false);
		customizedPermissions.put("ADMIN-EVALUATION-TEMPLATE-CHECKLIST", true);
		customizedPermissions.put("ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION", false);
		customizedPermissions.put("ADMIN-EVALUATION-TEMPLATE-SECTION", true);
		customizedPermissions.put("ADMIN-FEEDBACK", false);
		customizedPermissions.put("ADMIN-HIGHLIGHTS", true);
		customizedPermissions.put("ADMIN-INTEGRATION", false);
		customizedPermissions.put("ADMIN-JOB-MANAGEMENT", true);
		customizedPermissions.put("ADMIN-LOOKUPS", false);
		customizedPermissions.put("ADMIN-MEDIA", true);
		customizedPermissions.put("ADMIN-MESSAGE-MANAGEMENT", false);
		customizedPermissions.put("ADMIN-ORGANIZATION", true);
		customizedPermissions.put("ADMIN-QUESTIONS", false);
		customizedPermissions.put("ADMIN-REVIEW", true);
		customizedPermissions.put("ADMIN-SEARCH", false);
		customizedPermissions.put("ADMIN-SECURITY", true);
		customizedPermissions.put("ADMIN-ROLE-MANAGEMENT", false);
		customizedPermissions.put("ADMIN-SYSTEM-MANAGEMENT", true);
		customizedPermissions.put("ADMIN-TEMPMEDIA-MANAGEMENT", false);
		customizedPermissions.put("ADMIN-TRACKING", true);
		customizedPermissions.put("ADMIN-USER-MANAGEMENT", false);
		customizedPermissions.put("ADMIN-USER-MANAGEMENT-PROFILES", true);
		customizedPermissions.put("ADMIN-WATCHES", false);
		customizedPermissions.put("ADMIN-ORGANIZATION-EXTRACTION", true);
		customizedPermissions.put("API-DOCS", false);
		customizedPermissions.put("ENTRY-TAG", true);
		customizedPermissions.put("EVALUATIONS", false);
		customizedPermissions.put("RELATION-VIEW-TOOL", true);
		customizedPermissions.put("REPORTS", false);
		customizedPermissions.put("REPORTS-ALL", true);
		customizedPermissions.put("REPORTS-SCHEDULE", false);
		customizedPermissions.put("USER-SUBMISSIONS", true);
	}

	public void defineDefaultSecurityRoles()
	{
		//	!!! PLEASE DO NOT CHANGE unless the manual regression test is also updated !!!

		defaultDataSources.put("DI2E", false);
		defaultDataSources.put("ER2", false);

		defaultDataSensitivities.put("DISTROA", false);
		defaultDataSensitivities.put("DISTROB", false);
		defaultDataSensitivities.put("DISTROC", false);
		defaultDataSensitivities.put("DISTROD", false);
		defaultDataSensitivities.put("DISTROE", false);
		defaultDataSensitivities.put("DISTROF", true);
		defaultDataSensitivities.put("ITAR", false);
		defaultDataSensitivities.put("PUBLIC", false);
		defaultDataSensitivities.put("SENSITIVE", false);

		defaultPermissions.put("ADMIN-ALERT-MANAGEMENT", false);
		defaultPermissions.put("ADMIN-ATTRIBUTE-MANAGEMENT", false);
		defaultPermissions.put("ADMIN-BRANDING", false);
		defaultPermissions.put("ADMIN-CONTACT-MANAGEMENT", false);
		defaultPermissions.put("ADMIN-DATA-IMPORT-EXPORT", false);
		defaultPermissions.put("ADMIN-ENTRY-MANAGEMENT", false);
		defaultPermissions.put("ADMIN-ENTRY-TYPES", false);
		defaultPermissions.put("ADMIN-ENTRY-TEMPLATES", false);
		defaultPermissions.put("ADMIN-EVALUATION-MANAGEMENT", false);
		defaultPermissions.put("ADMIN-EVALUATION-TEMPLATE", false);
		defaultPermissions.put("ADMIN-EVALUATION-TEMPLATE-CHECKLIST", false);
		defaultPermissions.put("ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION", false);
		defaultPermissions.put("ADMIN-EVALUATION-TEMPLATE-SECTION", false);
		defaultPermissions.put("ADMIN-FEEDBACK", false);
		defaultPermissions.put("ADMIN-HIGHLIGHTS", false);
		defaultPermissions.put("ADMIN-INTEGRATION", false);
		defaultPermissions.put("ADMIN-JOB-MANAGEMENT", false);
		defaultPermissions.put("ADMIN-LOOKUPS", false);
		defaultPermissions.put("ADMIN-MEDIA", false);
		defaultPermissions.put("ADMIN-MESSAGE-MANAGEMENT", false);
		defaultPermissions.put("ADMIN-ORGANIZATION", false);
		defaultPermissions.put("ADMIN-QUESTIONS", false);
		defaultPermissions.put("ADMIN-REVIEW", false);
		defaultPermissions.put("ADMIN-SEARCH", false);
		defaultPermissions.put("ADMIN-SECURITY", false);
		defaultPermissions.put("ADMIN-ROLE-MANAGEMENT", false);
		defaultPermissions.put("ADMIN-SYSTEM-MANAGEMENT", false);
		defaultPermissions.put("ADMIN-TEMPMEDIA-MANAGEMENT", false);
		defaultPermissions.put("ADMIN-TRACKING", false);
		defaultPermissions.put("ADMIN-USER-MANAGEMENT", false);
		defaultPermissions.put("ADMIN-USER-MANAGEMENT-PROFILES", false);
		defaultPermissions.put("ADMIN-WATCHES", false);
		defaultPermissions.put("ADMIN-ORGANIZATION-EXTRACTION", false);
		defaultPermissions.put("API-DOCS", false);
		defaultPermissions.put("ENTRY-TAG", true);
		defaultPermissions.put("EVALUATIONS", false);
		defaultPermissions.put("RELATION-VIEW-TOOL", true);
		defaultPermissions.put("REPORTS", true);
		defaultPermissions.put("REPORTS-ALL", false);
		defaultPermissions.put("REPORTS-SCHEDULE", true);
		defaultPermissions.put("USER-SUBMISSIONS", true);
	}

	public void defineAdminSecurityRoles()
	{
		//	!!! PLEASE DO NOT CHANGE unless the manual regression test is also updated !!!

		adminDataSources.put("DI2E", true);
		adminDataSources.put("ER2", true);

		adminDataSensitivities.put("DISTROA", true);
		adminDataSensitivities.put("DISTROB", true);
		adminDataSensitivities.put("DISTROC", true);
		adminDataSensitivities.put("DISTROD", true);
		adminDataSensitivities.put("DISTROE", true);
		adminDataSensitivities.put("DISTROF", true);
		adminDataSensitivities.put("ITAR", true);
		adminDataSensitivities.put("PUBLIC", true);
		adminDataSensitivities.put("SENSITIVE", true);

		adminPermissions.put("ADMIN-ALERT-MANAGEMENT", true);
		adminPermissions.put("ADMIN-ATTRIBUTE-MANAGEMENT", true);
		adminPermissions.put("ADMIN-BRANDING", true);
		adminPermissions.put("ADMIN-CONTACT-MANAGEMENT", true);
		adminPermissions.put("ADMIN-DATA-IMPORT-EXPORT", true);
		adminPermissions.put("ADMIN-ENTRY-MANAGEMENT", true);
		adminPermissions.put("ADMIN-ENTRY-TYPES", true);
		adminPermissions.put("ADMIN-ENTRY-TEMPLATES", true);
		adminPermissions.put("ADMIN-EVALUATION-MANAGEMENT", true);
		adminPermissions.put("ADMIN-EVALUATION-TEMPLATE", true);
		adminPermissions.put("ADMIN-EVALUATION-TEMPLATE-CHECKLIST", true);
		adminPermissions.put("ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION", true);
		adminPermissions.put("ADMIN-EVALUATION-TEMPLATE-SECTION", true);
		adminPermissions.put("ADMIN-FEEDBACK", true);
		adminPermissions.put("ADMIN-HIGHLIGHTS", true);
		adminPermissions.put("ADMIN-INTEGRATION", true);
		adminPermissions.put("ADMIN-JOB-MANAGEMENT", true);
		adminPermissions.put("ADMIN-LOOKUPS", true);
		adminPermissions.put("ADMIN-MEDIA", true);
		adminPermissions.put("ADMIN-MESSAGE-MANAGEMENT", true);
		adminPermissions.put("ADMIN-ORGANIZATION", true);
		adminPermissions.put("ADMIN-QUESTIONS", true);
		adminPermissions.put("ADMIN-REVIEW", true);
		adminPermissions.put("ADMIN-SEARCH", true);
		adminPermissions.put("ADMIN-SECURITY", true);
		adminPermissions.put("ADMIN-ROLE-MANAGEMENT", true);
		adminPermissions.put("ADMIN-SYSTEM-MANAGEMENT", true);
		adminPermissions.put("ADMIN-TEMPMEDIA-MANAGEMENT", true);
		adminPermissions.put("ADMIN-TRACKING", true);
		adminPermissions.put("ADMIN-USER-MANAGEMENT", true);
		adminPermissions.put("ADMIN-USER-MANAGEMENT-PROFILES", true);
		adminPermissions.put("ADMIN-WATCHES", true);
		adminPermissions.put("ADMIN-ORGANIZATION-EXTRACTION", true);
		adminPermissions.put("API-DOCS", true);
		adminPermissions.put("ENTRY-TAG", true);
		adminPermissions.put("EVALUATIONS", true);
		adminPermissions.put("RELATION-VIEW-TOOL", true);
		adminPermissions.put("REPORTS", true);
		adminPermissions.put("REPORTS-ALL", true);
		adminPermissions.put("REPORTS-SCHEDULE", true);
		adminPermissions.put("USER-SUBMISSIONS", true);
	}

	public void defineEvaluatorSecurityRoles()
	{
		//	!!! PLEASE DO NOT CHANGE unless the manual regression test is also updated !!!

		evaluatorDataSources.put("DI2E", false);
		evaluatorDataSources.put("ER2", true);

		evaluatorDataSensitivities.put("DISTROA", false);
		evaluatorDataSensitivities.put("DISTROB", true);
		evaluatorDataSensitivities.put("DISTROC", false);
		evaluatorDataSensitivities.put("DISTROD", false);
		evaluatorDataSensitivities.put("DISTROE", true);
		evaluatorDataSensitivities.put("DISTROF", true);
		evaluatorDataSensitivities.put("ITAR", false);
		evaluatorDataSensitivities.put("PUBLIC", false);
		evaluatorDataSensitivities.put("SENSITIVE", false);

		evaluatorPermissions.put("ADMIN-ALERT-MANAGEMENT", false);
		evaluatorPermissions.put("ADMIN-ATTRIBUTE-MANAGEMENT", false);
		evaluatorPermissions.put("ADMIN-BRANDING", false);
		evaluatorPermissions.put("ADMIN-CONTACT-MANAGEMENT", false);
		evaluatorPermissions.put("ADMIN-DATA-IMPORT-EXPORT", false);
		evaluatorPermissions.put("ADMIN-ENTRY-MANAGEMENT", false);
		evaluatorPermissions.put("ADMIN-ENTRY-TYPES", false);
		evaluatorPermissions.put("ADMIN-ENTRY-TEMPLATES", false);
		evaluatorPermissions.put("ADMIN-EVALUATION-MANAGEMENT", false);
		evaluatorPermissions.put("ADMIN-EVALUATION-TEMPLATE", false);
		evaluatorPermissions.put("ADMIN-EVALUATION-TEMPLATE-CHECKLIST", false);
		evaluatorPermissions.put("ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION", false);
		evaluatorPermissions.put("ADMIN-EVALUATION-TEMPLATE-SECTION", false);
		evaluatorPermissions.put("ADMIN-FEEDBACK", false);
		evaluatorPermissions.put("ADMIN-HIGHLIGHTS", false);
		evaluatorPermissions.put("ADMIN-INTEGRATION", false);
		evaluatorPermissions.put("ADMIN-JOB-MANAGEMENT", false);
		evaluatorPermissions.put("ADMIN-LOOKUPS", false);
		evaluatorPermissions.put("ADMIN-MEDIA", false);
		evaluatorPermissions.put("ADMIN-MESSAGE-MANAGEMENT", false);
		evaluatorPermissions.put("ADMIN-ORGANIZATION", false);
		evaluatorPermissions.put("ADMIN-QUESTIONS", false);
		evaluatorPermissions.put("ADMIN-REVIEW", false);
		evaluatorPermissions.put("ADMIN-SEARCH", false);
		evaluatorPermissions.put("ADMIN-SECURITY", false);
		evaluatorPermissions.put("ADMIN-ROLE-MANAGEMENT", false);
		evaluatorPermissions.put("ADMIN-SYSTEM-MANAGEMENT", false);
		evaluatorPermissions.put("ADMIN-TEMPMEDIA-MANAGEMENT", false);
		evaluatorPermissions.put("ADMIN-TRACKING", false);
		evaluatorPermissions.put("ADMIN-USER-MANAGEMENT", false);
		evaluatorPermissions.put("ADMIN-USER-MANAGEMENT-PROFILES", false);
		evaluatorPermissions.put("ADMIN-WATCHES", false);
		evaluatorPermissions.put("ADMIN-ORGANIZATION-EXTRACTION", false);
		evaluatorPermissions.put("API-DOCS", false);
		evaluatorPermissions.put("ENTRY-TAG", true);
		evaluatorPermissions.put("EVALUATIONS", true);
		evaluatorPermissions.put("RELATION-VIEW-TOOL", true);
		evaluatorPermissions.put("REPORTS", true);
		evaluatorPermissions.put("REPORTS-ALL", false);
		evaluatorPermissions.put("REPORTS-SCHEDULE", true);
		evaluatorPermissions.put("USER-SUBMISSIONS", true);
	}

	public void defineLibrarianSecurityRoles()
	{
		//	!!! PLEASE DO NOT CHANGE unless the manual regression test is also updated !!!

		librarianDataSources.put("DI2E", true);
		librarianDataSources.put("ER2", true);

		librarianDataSensitivities.put("DISTROA", true);
		librarianDataSensitivities.put("DISTROB", false);
		librarianDataSensitivities.put("DISTROC", false);
		librarianDataSensitivities.put("DISTROD", false);
		librarianDataSensitivities.put("DISTROE", false);
		librarianDataSensitivities.put("DISTROF", false);
		librarianDataSensitivities.put("ITAR", true);
		librarianDataSensitivities.put("PUBLIC", true);
		librarianDataSensitivities.put("SENSITIVE", true);

		librarianPermissions.put("ADMIN-ALERT-MANAGEMENT", true);
		librarianPermissions.put("ADMIN-ATTRIBUTE-MANAGEMENT", true);
		librarianPermissions.put("ADMIN-BRANDING", false);
		librarianPermissions.put("ADMIN-CONTACT-MANAGEMENT", true);
		librarianPermissions.put("ADMIN-DATA-IMPORT-EXPORT", true);
		librarianPermissions.put("ADMIN-ENTRY-MANAGEMENT", true);
		librarianPermissions.put("ADMIN-ENTRY-TYPES", true);
		librarianPermissions.put("ADMIN-ENTRY-TEMPLATES", true);
		librarianPermissions.put("ADMIN-EVALUATION-MANAGEMENT", true);
		librarianPermissions.put("ADMIN-EVALUATION-TEMPLATE", true);
		librarianPermissions.put("ADMIN-EVALUATION-TEMPLATE-CHECKLIST", true);
		librarianPermissions.put("ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION", true);
		librarianPermissions.put("ADMIN-EVALUATION-TEMPLATE-SECTION", true);
		librarianPermissions.put("ADMIN-FEEDBACK", true);
		librarianPermissions.put("ADMIN-HIGHLIGHTS", true);
		librarianPermissions.put("ADMIN-INTEGRATION", true);
		librarianPermissions.put("ADMIN-JOB-MANAGEMENT", false);
		librarianPermissions.put("ADMIN-LOOKUPS", true);
		librarianPermissions.put("ADMIN-MEDIA", true);
		librarianPermissions.put("ADMIN-MESSAGE-MANAGEMENT", false);
		librarianPermissions.put("ADMIN-ORGANIZATION", true);
		librarianPermissions.put("ADMIN-QUESTIONS", true);
		librarianPermissions.put("ADMIN-REVIEW", true);
		librarianPermissions.put("ADMIN-SEARCH", true);
		librarianPermissions.put("ADMIN-SECURITY", false);
		librarianPermissions.put("ADMIN-ROLE-MANAGEMENT", false);
		librarianPermissions.put("ADMIN-SYSTEM-MANAGEMENT", false);
		librarianPermissions.put("ADMIN-TEMPMEDIA-MANAGEMENT", false);
		librarianPermissions.put("ADMIN-TRACKING", false);
		librarianPermissions.put("ADMIN-USER-MANAGEMENT", false);
		librarianPermissions.put("ADMIN-USER-MANAGEMENT-PROFILES", true);
		librarianPermissions.put("ADMIN-WATCHES", true);
		librarianPermissions.put("ADMIN-ORGANIZATION-EXTRACTION", false);
		librarianPermissions.put("API-DOCS", false);
		librarianPermissions.put("ENTRY-TAG", false);
		librarianPermissions.put("EVALUATIONS", true);
		librarianPermissions.put("RELATION-VIEW-TOOL", false);
		librarianPermissions.put("REPORTS", true);
		librarianPermissions.put("REPORTS-ALL", false);
		librarianPermissions.put("REPORTS-SCHEDULE", false);
		librarianPermissions.put("USER-SUBMISSIONS", false);
	}

	public void defineUserSecurityRoles()
	{
		//	!!! PLEASE DO NOT CHANGE unless the manual regression test is also updated !!!

		userDataSources.put("DI2E", true);
		userDataSources.put("ER2", false);

		userDataSensitivities.put("DISTROA", false);
		userDataSensitivities.put("DISTROB", false);
		userDataSensitivities.put("DISTROC", true);
		userDataSensitivities.put("DISTROD", false);
		userDataSensitivities.put("DISTROE", false);
		userDataSensitivities.put("DISTROF", true);
		userDataSensitivities.put("ITAR", false);
		userDataSensitivities.put("PUBLIC", false);
		userDataSensitivities.put("SENSITIVE", true);

		userPermissions.put("ADMIN-ALERT-MANAGEMENT", false);
		userPermissions.put("ADMIN-ATTRIBUTE-MANAGEMENT", false);
		userPermissions.put("ADMIN-BRANDING", false);
		userPermissions.put("ADMIN-CONTACT-MANAGEMENT", false);
		userPermissions.put("ADMIN-DATA-IMPORT-EXPORT", false);
		userPermissions.put("ADMIN-ENTRY-MANAGEMENT", false);
		userPermissions.put("ADMIN-ENTRY-TYPES", false);
		userPermissions.put("ADMIN-ENTRY-TEMPLATES", false);
		userPermissions.put("ADMIN-EVALUATION-MANAGEMENT", false);
		userPermissions.put("ADMIN-EVALUATION-TEMPLATE", false);
		userPermissions.put("ADMIN-EVALUATION-TEMPLATE-CHECKLIST", false);
		userPermissions.put("ADMIN-EVALUATION-TEMPLATE-CHECKLIST-QUESTION", false);
		userPermissions.put("ADMIN-EVALUATION-TEMPLATE-SECTION", false);
		userPermissions.put("ADMIN-FEEDBACK", false);
		userPermissions.put("ADMIN-HIGHLIGHTS", false);
		userPermissions.put("ADMIN-INTEGRATION", false);
		userPermissions.put("ADMIN-JOB-MANAGEMENT", false);
		userPermissions.put("ADMIN-LOOKUPS", false);
		userPermissions.put("ADMIN-MEDIA", false);
		userPermissions.put("ADMIN-MESSAGE-MANAGEMENT", false);
		userPermissions.put("ADMIN-ORGANIZATION", false);
		userPermissions.put("ADMIN-QUESTIONS", false);
		userPermissions.put("ADMIN-REVIEW", false);
		userPermissions.put("ADMIN-SEARCH", false);
		userPermissions.put("ADMIN-SECURITY", false);
		userPermissions.put("ADMIN-ROLE-MANAGEMENT", false);
		userPermissions.put("ADMIN-SYSTEM-MANAGEMENT", false);
		userPermissions.put("ADMIN-TEMPMEDIA-MANAGEMENT", false);
		userPermissions.put("ADMIN-TRACKING", false);
		userPermissions.put("ADMIN-USER-MANAGEMENT", false);
		userPermissions.put("ADMIN-USER-MANAGEMENT-PROFILES", false);
		userPermissions.put("ADMIN-WATCHES", false);
		userPermissions.put("ADMIN-ORGANIZATION-EXTRACTION", false);
		userPermissions.put("API-DOCS", false);
		userPermissions.put("ENTRY-TAG", false);
		userPermissions.put("EVALUATIONS", false);
		userPermissions.put("RELATION-VIEW-TOOL", false);
		userPermissions.put("REPORTS", false);
		userPermissions.put("REPORTS-ALL", false);
		userPermissions.put("REPORTS-SCHEDULE", false);
		userPermissions.put("USER-SUBMISSIONS", false);
	}

	public void tableDeleteAllRoles(String cssSelector, WebDriver driver, int columnClickOn)
	{
		WebDriverWait wait = new WebDriverWait(driver, 4);

		try {
			List<WebElement> allRows = new ArrayList<WebElement>();
			allRows = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.cssSelector(cssSelector), By.tagName("tr")));
			// Need to refresh up here
			
			for (WebElement row : allRows) {

				List<WebElement> cells = new ArrayList<WebElement>();
				try {
					cells = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(row, By.tagName("td")));

					WebElement clickCol = cells.get(columnClickOn);
					Actions builder = new Actions(driver);
					builder.moveToElement(clickCol).perform();
					sleep(100);
					builder.click().perform();

					// Click Yes on confirmation pop-up box; deleting role from user
					try {
						List<WebElement> confButtons = driver.findElements(By.cssSelector(".x-btn-inner.x-btn-inner-default-small"));
						for (WebElement button : confButtons) {
							if (button.getText().equals("Yes")) {
								button.click();
							}
						}
					} catch (Exception e) {
						LOG.log(Level.SEVERE, "*** Could not find confirmation button 'YES' to click. ***");
					}
				} catch (Exception e) {

				}
			}
		} catch (Exception e) {
			LOG.log(Level.WARNING,
					"*** Not able to click on column " + columnClickOn + " searching by this cssSelector for a table: \n"
					+ cssSelector + " ***");
			System.out.println(e);
		}
	}

}
