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
public class AdminAddIntegrationIT
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
			createIntegrationComponent(componentName);
			componentIds.add(apiClient.getComponentRESTTestClient().getComponentByName(componentName).getComponent().getComponentId());
			createJiraMapping();
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
		List<WebElement> tabs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".x-tab.x-unselectable")));
		componentConfigTab = tabs.get(0);
		componentConfigTab.click();
		
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

	@AfterClass
	public static void cleanupTest()
	{
		componentIds.forEach((id) -> {
			apiClient.getComponentRESTTestClient().deleteAPIComponentIntegration(id);
		});
	}
}
