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

import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.junit.Assert;
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
		extends AdminTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());
	private String componentName = "DeleteIntegrationTestComp";

	@Test
	public void deleteComponentIntegrationTest() throws InterruptedException
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			webDriverUtil.getPage(driver, "AdminTool.action?load=Integrations");
			apiClient.getComponentRESTTestClient().createAPIComponentIntegration(componentName);
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

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#componentConfigGrid-tools-delete"))).click();

		List<WebElement> confirmBtns = new ArrayList<>();
		confirmBtns = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-toolbar-item.x-btn-default-small[aria-hidden='false']")));
		confirmBtns.get(0).click();

		sleep(1000);

		driverWait(() -> {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#componentConfigGrid-tools-refresh"))).click();
		}, 5);

		Assert.assertFalse(tableClickRowCol("#componentConfigGrid-body .x-grid-view", componentName, driver, 1));

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
