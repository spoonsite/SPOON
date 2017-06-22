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
import java.util.List;
import java.util.logging.Logger;
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
	public void componentConfigTest()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			webDriverUtil.getPage(driver, "AdminTool.action?load=Integrations");
			WebDriverWait wait = new WebDriverWait(driver, 10);
			List<WebElement> tabs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".x-tab.x-unselectable")));
			componentConfigTab = tabs.get(0);
			componentConfigTab.click();
			sleep(3000);
			addComponentConfiguration(driver, "VANTAGE WESS OZONE Widget");

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

	public void addComponentConfiguration(WebDriver driver, String componentName)
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#componentConfigGrid-tools-add"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-form-field"))).sendKeys(componentName);

		Actions actionEnter = new Actions(driver);
		actionEnter.sendKeys(Keys.ENTER).perform();

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

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name='issueNumber']"))).sendKeys("ASSET-1");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#checkJiraNumberButton"))).click();

		// Excellent way to wait for page loading
//		try {
//			ExpectedCondition condition = new ExpectedCondition<Boolean>()
//			{
//				@Override
//				public Boolean apply(final WebDriver driver)
//				{
//					WebElement element = driver.findElement(By.cssSelector("#theSaveBtnConfig"));
//					return !element.isDisplayed();
//				}
//			};
//			WebDriverWait w = new WebDriverWait(driver, 10);
//			w.until(condition);
//		} catch (Exception ex) {
//			LOG.log(Level.INFO, "Timed out looking for save button on configuration window ****************");
//		}
		
		driverWait(()->{
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#theSaveBtnConfig"))).click();
		}, 5000);
		
		driverWait(()->{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='addBtnIntegrationWindow']"))).click();
		}, 5000);
	

		// do esc after saving configuration
		Actions actionEsc = new Actions(driver);
		actionEsc.sendKeys(Keys.ESCAPE).perform();
	}

}
