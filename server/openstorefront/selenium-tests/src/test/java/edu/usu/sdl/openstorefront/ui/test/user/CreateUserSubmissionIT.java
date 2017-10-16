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
package edu.usu.sdl.openstorefront.ui.test.user;

import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.selenium.util.PropertiesUtil;
import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author ccummings
 */
public class CreateUserSubmissionIT
		extends UserTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());
	private static final String typeName = "Test Component Type";
	private String entryName = "My First Submission";
	private String email = PropertiesUtil.getProperties().getProperty("test.newuseremail");
	private static String autoApproveVal = "TRUE";
	
	@BeforeClass
	public static void setupTest()
	{
		LookupModel config = apiClient.getApplicationTestClient().getCurrentConfigProp("userreview.autoapprove");
		config.setDescription(autoApproveVal);
		apiClient.getApplicationTestClient().setConfigProperties(config);
		apiClient.getComponentTypeTestClient().createAPIComponentType(typeName);
	}

	@Test
	public void createUserSubmission()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			fillOutForm(driver, entryName);
		}
	}
	
	protected void fillOutForm(WebDriver driver, String submissionName)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);
		
		webDriverUtil.getPage(driver, "UserTool.action");
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#main-menu-submissions"))).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='newSubmissionBtn']"))).click();
		sleep(2000);
	}
	
	@AfterClass
	public static void cleanupTest()
	{
		
	}
}
