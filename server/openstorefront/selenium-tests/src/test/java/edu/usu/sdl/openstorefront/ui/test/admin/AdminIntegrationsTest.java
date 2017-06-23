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
public class AdminIntegrationsTest
		extends AdminTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());
	private WebElement componentConfigTab;
	private WebElement jiraMappingTab;

	@Test
	public void componentConfigTest() throws InterruptedException
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			webDriverUtil.getPage(driver, "AdminTool.action?load=Integrations");
			WebDriverWait wait = new WebDriverWait(driver, 10);
			List<WebElement> tabs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".x-tab.x-unselectable")));
			componentConfigTab = tabs.get(0);
			componentConfigTab.click();
			sleep(3000);
			addComponentConfiguration(driver, "1Time");
			deleteComponentConfiguration(driver, "1Time", "ASSET-1");
			deleteComponentConfiguration(driver, "1Time", "ASSET-2");

		}
	}

	@Test
	public void jiraMappingConfigTest()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			webDriverUtil.getPage(driver, "AdminTool.action?load=Integrations");
			WebDriverWait wait = new WebDriverWait(driver, 10);
			List<WebElement> tabs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".x-tab.x-unselectable")));
			jiraMappingTab = tabs.get(1);
			jiraMappingTab.click();
			sleep(3000);

		}
	}

	public void addComponentConfiguration(WebDriver driver, String componentName) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#componentConfigGrid-tools-add"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-form-field"))).sendKeys(componentName);

		Actions actionEnter = new Actions(driver);
		actionEnter.sendKeys(Keys.ENTER).perform();

		addJiraConfiguration(driver, "ASSET-1");
		Assert.assertTrue(tableClickRowCol(".x-panel.x-fit-item.x-window-item.x-panel-default.x-grid .x-grid-view .x-grid-item-container", "ASSET-1", driver, 4));
		addJiraConfiguration(driver, "ASSET-2");
		Assert.assertTrue(tableClickRowCol(".x-panel.x-fit-item.x-window-item.x-panel-default.x-grid .x-grid-view .x-grid-item-container", "ASSET-2", driver, 4));

		// do esc after saving configuration
		Actions actionEsc = new Actions(driver);
		actionEsc.sendKeys(Keys.ESCAPE).perform();
	}

	protected void addJiraConfiguration(WebDriver driver, String issueNumber)
	{
		WebDriverWait wait = new WebDriverWait(driver, 3);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='addBtnIntegrationWindow']"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name='integrationType']"))).click();
		List<WebElement> integrationTypes = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.cssSelector(".x-list-plain[aria-hidden='false']"), By.cssSelector("li")));
		for (WebElement type : integrationTypes) {
			if (type.getText().equals("Jira")) {
				type.click();
			}
		}

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

		WebDriverWait waitAddButtonWindow = new WebDriverWait(driver, 1);
		driverWait(() -> {
			waitAddButtonWindow.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='addBtnIntegrationWindow']")));
		}, 5000);
	}

	public void deleteComponentConfiguration(WebDriver driver, String componentName, String jiraIssueNumber)
			throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		webDriverUtil.getPage(driver, "AdminTool.action?load=Integrations");
		List<WebElement> headers = new ArrayList<>();
		headers = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".x-column-header-inner.x-leaf-column-header")));
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
		
		WebDriverWait waitEditButton = new WebDriverWait(driver, 1);
		driverWait(() -> {
			waitEditButton.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#componentConfigGrid-tools-edit"))).click();
		}, 5000);

		Assert.assertTrue(tableClickRowCol(".x-panel.x-fit-item.x-window-item.x-panel-default.x-grid .x-grid-view .x-grid-item-container", jiraIssueNumber, driver, 4));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#integrationDeleteBtn"))).click();

		List<WebElement> buttons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("[role='alertdialog'] .x-btn[aria-hidden='false']")));
		buttons.get(0).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-panel.x-fit-item.x-window-item.x-panel-default.x-grid .x-grid-view .x-grid-item-container")));
		
		Actions actionEsc = new Actions(driver);
		actionEsc.sendKeys(Keys.ESCAPE).perform();
	}
}
