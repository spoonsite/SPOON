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
package edu.usu.sdl.openstorefront.ui.test.search;

import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import edu.usu.sdl.openstorefront.ui.test.admin.AdminTestBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author ccummings
 */
public class PrintSearchResultsEntryIT
		extends AdminTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

	private static String entryName = "A Selenium Test Entry";

	@BeforeClass
	public static void createEntryToPrint()
	{
		createBasicSearchComponent(entryName);
	}

	@Test
	public void printEntryFromSearchResults()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			searchAndClickEntry(driver, entryName);
			selectCustomTemplate(driver);
		}
	}

	public void searchAndClickEntry(WebDriver driver, String entryName)
	{
		webDriverUtil.getPage(driver, "Landing.action");
		
		WebDriverWait wait = new WebDriverWait(driver, 8);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".home-search-field-new"))).sendKeys("Test");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-btn-default-small"))).click();

		List<WebElement> entryResults = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#resultsDisplayPanel-innerCt h2")));

		boolean isResult = false;

		for (WebElement entry : entryResults) {

			System.out.println("Entry Results Name: " + entry.getText());

			if (entry.getText().equals(entryName)) {
				isResult = true;
				entry.click();
				break;
			}
		}

		Assert.assertTrue(isResult);
	}

	protected void selectCustomTemplate(WebDriver driver)
	{
		//[data-qtip = 'Print']
		WebDriverWait wait = new WebDriverWait(driver, 8);

		String winHandleBefore = driver.getWindowHandle();

		WebElement frame = driver.findElement(By.cssSelector("iframe"));

		driver.switchTo().frame(frame);

		sleep(500);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-qtip = 'Print']"))).click();

		Set<String> handles = driver.getWindowHandles();
		List windows = new ArrayList(handles);
		String printWindow = (String) windows.get(windows.size() - 1);
		driver.switchTo().window(printWindow);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#customTemplateBtn"))).click();

		List<WebElement> templateItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".x-menu-body.x-menu-body.x-unselectable .x-menu-item-text.x-menu-item-text-default.x-menu-item-indent-no-separator")));

		for (WebElement item : templateItems) {

			if (item.getText().equals("Description")) {
				item.click();
			}
		}

		driver.findElement(By.cssSelector("#customTemplateBtn")).click();

		driver.findElement(By.cssSelector(".x-body")).click();

		List<WebElement> templateSections = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#contentInfo-innerCt div h3")));

		boolean isSection = false;

		for (WebElement section : templateSections) {

			if (section.getText().equals("Description:")) {
				isSection = true;
			}
		}

		Assert.assertFalse(isSection);

		WebElement printBtn = driver.findElement(By.cssSelector("#printCustomizedEntryBtn"));

		boolean canPrint = false;

		if (printBtn.isDisplayed() && printBtn.isEnabled()) {
			canPrint = true;
		}

		Assert.assertTrue(canPrint);

		driver.close();

		driver.switchTo().window(winHandleBefore);

	}

}
