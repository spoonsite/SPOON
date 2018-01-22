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

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import static edu.usu.sdl.openstorefront.ui.test.search.SearchTestBase.createBasicSearchComponent;
import java.util.List;
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
public class AddTagSearchResultEntryIT
		extends SearchTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());
	private static String entryName1 = "A Selenium Test Entry";
	private static String entryName2 = "Another Selenium Test Entry";
	private static String tagName = "selenium";

	@BeforeClass
	public static void createEntries()
	{
		createBasicSearchComponent(entryName1);
		Component entry2 = createBasicSearchComponent(entryName2);
		apiClient.getComponentRESTTestClient().addTagToComponent(tagName, entry2);
		sleep(2500);
	}

	@Test
	public void addTagToSearchResultEntry()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			searchEntry(driver, entryName1);
			addTagToTestEntry(driver, tagName, entryName1);
			viewRelatedEntries(driver, tagName);
		}
	}

	protected void searchEntry(WebDriver driver, String entryName)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);

		webDriverUtil.getPage(driver, "Landing.action");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".home-search-field-new"))).sendKeys("Test");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-btn-default-small"))).click();

		List<WebElement> entryResults = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#resultsDisplayPanel-innerCt h2")));

		boolean isResult = false;

		for (WebElement entry : entryResults) {

			System.out.println("Entry Name: " + entry.getText());
			if (entry.getText().equals(entryName)) {
				isResult = true;
				entry.click();
				break;
			}
		}

		Assert.assertTrue(isResult);
	}

	protected void addTagToTestEntry(WebDriver driver, String tagName, String entryName)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);

		WebElement frame = driver.findElement(By.cssSelector("iframe"));

		driver.switchTo().frame(frame);
		sleep(500);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-qtip = 'Tags']"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#tagField-inputEl"))).sendKeys(tagName);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#tagPanel-innerCt .x-btn"))).click();

		sleep(500);

		List<WebElement> tagButtons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#tagPanel-innerCt .x-btn.x-unselectable.x-btn-default-small")));

		boolean isTag = false;

		for (WebElement tag : tagButtons) {

			System.out.println("tag name: " + tag.getText());
			if (tag.getText().equals(tagName)) {
				isTag = true;
				break;
			}
		}

		Assert.assertTrue(isTag);
	}

	protected void viewRelatedEntries(WebDriver driver, String tagName)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);
		List<WebElement> tagButtons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#tagPanel-innerCt .x-btn.x-unselectable.x-btn-default-small")));
		WebElement testTag = null;

		for (WebElement tag : tagButtons) {

			System.out.println("tag name: " + tag.getText());
			if (tag.getText().equals(tagName)) {
				testTag = tag;
				break;
			}
		}

		Assert.assertTrue(testTag != null);

		testTag.click();

		sleep(1000);

		WebElement close = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-qtip='Close dialog']")));
		close.click();
	}
}
