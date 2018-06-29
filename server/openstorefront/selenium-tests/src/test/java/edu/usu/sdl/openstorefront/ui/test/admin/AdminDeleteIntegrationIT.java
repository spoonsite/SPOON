/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.ui.test.admin;

import edu.usu.sdl.openstorefront.common.exception.AttachedReferencesException;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.selenium.provider.AttributeProvider;
import edu.usu.sdl.openstorefront.selenium.provider.AuthenticationProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ClientApiProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ComponentIntegrationProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ComponentProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ComponentTypeProvider;
import edu.usu.sdl.openstorefront.selenium.provider.NotificationEventProvider;
import edu.usu.sdl.openstorefront.selenium.provider.OrganizationProvider;
import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.List;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author ccummings
 */
public class AdminDeleteIntegrationIT
		extends BrowserTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());
	private String componentName = "DeleteIntegrationTestComp";
	private String compDescription = "Integration Description";
	private String compOrganization = "Integration Test Organization";
	private ClientApiProvider provider;
	private ComponentProvider componentProvider;
	private AttributeProvider attributeProvider;
	private OrganizationProvider organizationProvider;
	private ComponentTypeProvider compTypeProvider;
	private ComponentIntegrationProvider compIntegrationProvider;
	private AuthenticationProvider authProvider;
	private NotificationEventProvider notificationProvider;

	@Before
	public void setup() throws InterruptedException
	{
		authProvider = new AuthenticationProvider(properties, webDriverUtil);
		authProvider.login();
		provider = new ClientApiProvider();
		notificationProvider = new NotificationEventProvider(provider.getAPIClient());
		attributeProvider = new AttributeProvider(provider.getAPIClient());
		organizationProvider = new OrganizationProvider(provider.getAPIClient());
		compTypeProvider = new ComponentTypeProvider(provider.getAPIClient());
		compIntegrationProvider = new ComponentIntegrationProvider(provider.getAPIClient());
		componentProvider = new ComponentProvider(attributeProvider, organizationProvider, compTypeProvider, provider.getAPIClient());
		Component testComponent = componentProvider.createComponent(componentName, compDescription, compOrganization);
		compIntegrationProvider.createComponentIntegration(testComponent);
		sleep(1000);
	}

	@Test
	public void deleteComponentIntegrationTest() throws InterruptedException
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			webDriverUtil.getPage(driver, "AdminTool.action?load=Integrations");
			deleteIntegration(driver, componentName);
		}
	}

	public void deleteIntegration(WebDriver driver, String componentName) throws InterruptedException
	{
		webDriverUtil.getPage(driver, "AdminTool.action?load=Integrations");
		WebDriverWait wait = new WebDriverWait(driver, 5);

		List<WebElement> tabs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".x-tab.x-unselectable")));
		WebElement componentConfigTab = tabs.get(0);
		componentConfigTab.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#componentConfigGrid-tools-refresh"))).click();

		List<WebElement> headers = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".x-column-header-inner.x-leaf-column-header")));
		WebElement componentHeader = null;
		for (WebElement header : headers) {
			if (header.getText().equals("Component")) {
				componentHeader = header;
				break;
			}
		}

		int count = 0;
		while (!tableClickRowCol("#componentConfigGrid-body .x-grid-view", componentName, driver, 1) && count < 3) {
			Actions action = new Actions(driver);
			action.moveToElement(componentHeader).click().perform();
			count++;
		}

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#componentConfigGrid-tools-delete"))).click();

		List<WebElement> confirmBtns = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-toolbar-item.x-btn-default-small[aria-hidden='false']")));
		confirmBtns.get(0).click();

		sleep(1000);

		driverWait(() -> {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#componentConfigGrid-tools-refresh"))).click();
		}, 5);

		Assert.assertFalse(tableClickRowCol("#componentConfigGrid-body .x-grid-view", componentName, driver, 1));
	}

	@After
	public void cleanupTest() throws AttachedReferencesException
	{
		compIntegrationProvider.cleanup();
		componentProvider.cleanup();
		notificationProvider.cleanup();
		provider.clientDisconnect();
	}
}

// to be used in delete integration config test
//	public void deleteJiraIntegrationConfig(WebDriver driver, String componentName, String jiraIssueNumber)
//			throws InterruptedException
//	{
//		WebDriverWait wait = new WebDriverWait(driver, 5);
//		webDriverUtil.getPage(driver, "AdminTool.action?load=Integrations");
//		List<WebElement> headers = new ArrayList<>();
//		headers = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".x-column-header-inner.x-leaf-column-header")));
//		WebElement componentHeader = null;
//		for (WebElement header : headers) {
//			if (header.getText().equals("Component")) {
//				componentHeader = header;
//				break;
//			}
//		}
//
//		int count = 0;
//		while (!tableClickRowCol("#componentConfigGrid-body .x-grid-view", componentName, driver, 1) && count < 3) {
//			Actions action = new Actions(driver);
//			action.moveToElement(componentHeader).click().perform();
//			count++;
//		}
//
//		WebDriverWait waitEditButton = new WebDriverWait(driver, 1);
//		driverWait(() -> {
//			waitEditButton.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#componentConfigGrid-tools-edit"))).click();
//		}, 5000);
//
//		Assert.assertTrue(tableClickRowCol(".x-panel.x-fit-item.x-window-item.x-panel-default.x-grid .x-grid-view .x-grid-item-container", jiraIssueNumber, driver, 4));
//
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#integrationDeleteBtn"))).click();
//
//		List<WebElement> buttons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("[role='alertdialog'] .x-btn[aria-hidden='false']")));
//		buttons.get(0).click();
//
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-panel.x-fit-item.x-window-item.x-panel-default.x-grid .x-grid-view .x-grid-item-container")));
//
//		Actions actionEsc = new Actions(driver);
//		actionEsc.sendKeys(Keys.ESCAPE).perform();
//	}
