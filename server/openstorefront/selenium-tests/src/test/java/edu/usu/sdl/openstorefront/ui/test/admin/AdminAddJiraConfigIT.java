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
package edu.usu.sdl.openstorefront.ui.test.admin;

import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author ccummings
 */
public class AdminAddJiraConfigIT
		extends AdminTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());
	private WebElement componentConfigTab;
	private static List<String> componentIds = new ArrayList<>();
	private String componentName = "Asset-Test-IntegrationComp";

	@Test
	public void componentConfigTest() throws InterruptedException
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			webDriverUtil.getPage(driver, "AdminTool.action?load=Integrations");
			WebDriverWait wait = new WebDriverWait(driver, 10);
			createIntegrationComponent(componentName);
			componentIds.add(apiClient.getComponentRESTTestClient().getComponentByName(componentName).getComponent().getComponentId());
			createJiraMapping();
			List<WebElement> tabs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".x-tab.x-unselectable")));
			componentConfigTab = tabs.get(0);
			componentConfigTab.click();
			addComponentConfiguration(driver, componentName);
		}
	}

	public void createJiraMapping()
	{
		apiClient.getAttributeTestClient().createJiraMapping();
	}

	public void createIntegrationComponent(String componentName)
	{
		apiClient.getComponentRESTTestClient().createAPIComponent(componentName);
	}

	public void addComponentConfiguration(WebDriver driver, String componentName) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);

		driver.navigate().refresh();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#componentConfigGrid-tools-add"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-form-field"))).sendKeys(componentName);

		Actions actionEnter = new Actions(driver);
		actionEnter.sendKeys(Keys.ENTER).perform();

		addJiraIntegrationConfig(driver, "ASSET-1");
		Assert.assertTrue(tableClickRowCol(".x-panel.x-fit-item.x-window-item.x-panel-default.x-grid .x-grid-view .x-grid-item-container", "ASSET-1", driver, 4));
		addJiraIntegrationConfig(driver, "ASSET-2");
		Assert.assertTrue(tableClickRowCol(".x-panel.x-fit-item.x-window-item.x-panel-default.x-grid .x-grid-view .x-grid-item-container", "ASSET-2", driver, 4));

		// esc after saving configuration
		Actions actionEsc = new Actions(driver);
		actionEsc.sendKeys(Keys.ESCAPE).perform();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#componentConfigGrid-tools-refresh"))).click();
		
		sleep(1000);
	}

	// to be used in separate test
//	public void deleteComponentConfiguration(WebDriver driver, String componentName) throws InterruptedException
//	{
//		webDriverUtil.getPage(driver, "AdminTool.action?load=Integrations");
//
//		WebDriverWait wait = new WebDriverWait(driver, 5);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#componentConfigGrid-tools-refresh"))).click();
//
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
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#componentConfigGrid-tools-delete"))).click();
//
//		List<WebElement> confirmBtns = new ArrayList<>();
//		confirmBtns = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-toolbar-item.x-btn-default-small[aria-hidden='false']")));
//		confirmBtns.get(0).click();
//
//		sleep(1000);
//
//		driverWait(() -> {
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#componentConfigGrid-tools-refresh"))).click();
//		}, 5);
//
//		Assert.assertFalse(tableClickRowCol("#componentConfigGrid-body .x-grid-view", componentName, driver, 1));
//
//	}

	protected void addJiraIntegrationConfig(WebDriver driver, String issueNumber)
	{
		WebDriverWait wait = new WebDriverWait(driver, 4);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='addBtnIntegrationWindow']"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name='integrationType']"))).click();
		List<WebElement> integrationTypes = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.cssSelector(".x-list-plain[aria-hidden='false']"), By.cssSelector("li")));
		for (WebElement type : integrationTypes) {
			if (type.getText().equals("Jira")) {
				type.click();
			}
		}

		sleep(250);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name='projectType']"))).click();
		List<WebElement> projectTypes = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.cssSelector(".x-list-plain[aria-hidden='false']"), By.cssSelector("li")));
		for (WebElement type : projectTypes) {
			if (type.getText().equals("ASSET")) {
				type.click();
			}
		}

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name='issueNumber']"))).sendKeys(issueNumber);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#checkJiraNumberButton"))).click();

		driverWait(() -> {
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#theSaveBtnConfig"))).click();
		}, 5000);

		WebDriverWait waitAddButtonWindow = new WebDriverWait(driver, 3);
		driverWait(() -> {
			waitAddButtonWindow.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='addBtnIntegrationWindow']")));
		}, 5000);
		
		sleep(250);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='refreshBtnIntegrationWindow']"))).click();
	}

	// to be used in separate test
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
	
	@AfterClass
	public static void cleanupTest()
	{
		componentIds.forEach((id) -> {
			apiClient.getComponentRESTTestClient().deleteComponentIntegration(id);
		});
	}
}
