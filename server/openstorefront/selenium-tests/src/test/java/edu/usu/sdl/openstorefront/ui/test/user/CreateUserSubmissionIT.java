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

import static edu.usu.sdl.openstorefront.core.entity.ApprovalStatus.PENDING;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminView;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.selenium.util.PropertiesUtil;
import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.List;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
	private static String entryName = "My First Test Submission";
	private static String entryOrganization = "The Singleton Factory";
	private String entryDesc = "Stop sniffing my cookies";
	private String email = PropertiesUtil.getProperties().getProperty("test.newuseremail");
	private static String autoApproveVal = "TRUE";
	private WebElement nextBtn = null;
	private WebElement submitReviewBtn = null;
	private static ComponentAdminView entry = null;

	@BeforeClass
	public static void setupTest()
	{
		LookupModel config = apiClient.getApplicationTestClient().getCurrentConfigProp("userreview.autoapprove");
		config.setDescription(autoApproveVal);
		apiClient.getApplicationTestClient().setConfigProperties(config);
		apiClient.getComponentTypeTestClient().createAPIComponentType(typeName);
		apiClient.getOrganizationTestClient().createOrganization(entryOrganization);
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
		WebDriverWait wait = new WebDriverWait(driver, 10);

		webDriverUtil.getPage(driver, "UserTool.action");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#main-menu-submissions"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='newSubmissionBtn']"))).click();

		List<WebElement> buttons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".x-toolbar.x-docked.x-toolbar-default.x-docked-bottom a")));
		setButtons(buttons);

		nextBtn.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name='componentType']"))).click();

		List<WebElement> typeOptions = driver.findElements(By.cssSelector(".x-boundlist.x-boundlist-floating.x-layer.x-boundlist-default.x-border-box li"));
		boolean found = false;
		for (WebElement option : typeOptions) {
			if (option.getText().equals("Test Component Type - test label"))
			{
				option.click();
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
		
		driver.findElement(By.cssSelector("[name='name']")).sendKeys(entryName);
		sleep(100);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='organizationInput'] [name='organization']"))).sendKeys(entryOrganization);
		sleep(300);
		((JavascriptExecutor) driver).executeScript("tinyMCE.activeEditor.setContent('" + entryDesc + "')");

		nextBtn.click();
		sleep(2000);
		nextBtn.click();
		sleep(2000);
		driverWait(() -> {
			setButtons(buttons);
			submitReviewBtn.click();
		}, 5);
		sleep(2000);
		
		ComponentAdminView compView = apiClient.getComponentRESTTestClient().getComponentByName(entryName);
		Assert.assertEquals(compView.getComponent().getName(), entryName);
		Assert.assertEquals(compView.getComponent().getApprovalState(), PENDING);
	}

	protected void setButtons(List<WebElement> buttons)
	{
		for (WebElement btn : buttons) {
			System.out.println(btn.getText());
			if (btn.getText().equals("Next")) {
				nextBtn = btn;
			} else if (btn.getText().equals("Submit For Review")) {
				submitReviewBtn = btn;
			}
		}
	}

	@AfterClass
	public static void cleanupTest()
	{
		entry = apiClient.getComponentRESTTestClient().getComponentByName(entryName);
		String deleteEntry = entry.getComponent().getComponentId();
		apiClient.getComponentRESTTestClient().deleteAPIComponent(deleteEntry);
	}
}
