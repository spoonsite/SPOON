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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author ccummings
 */
public class AdminEntryTypesIT
		extends AdminTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

	@Test
	public void adminEntryTypesTest() throws InterruptedException
	{

		for (WebDriver driver : webDriverUtil.getDrivers()) {

			setupDriver(driver);
			createEntryType(driver, "AMAZING-TEST", "An Amazing Test");
			createAPIComponentType();
			editEntryTypes(driver, "AMAZING-TEST");
			toggleStatusEntryType(driver, "AMAZING-TEST");
			deleteEntryType(driver, "AMAZING-TEST");
		}
	}

	private void createAPIComponentType()
	{
		apiClient.getComponentTypeTestClient().createAPIComponentType("AAA-ENTRYTYPE-API");
	}

	public void setupDriver(WebDriver driver)
	{
		webDriverUtil.getPage(driver, "AdminTool.action?load=Entry-Types");

		(new WebDriverWait(driver, 10)).until((ExpectedCondition<Boolean>) (WebDriver driverLocal) -> {
			List<WebElement> titleElements = driverLocal.findElements(By.id("entryGrid_header-title-textEl"));
			if (titleElements.size() > 0) {
				return titleElements.get(0).isDisplayed();
			} else {
				
				return false;
			}
		});
	}

	public void createEntryType(WebDriver driver, String typeCode, String typeLabel) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#entryTypeAddBtn"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#entryForm-type-inputEl"))).sendKeys(typeCode);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#entryForm-type-label-inputEl"))).sendKeys(typeLabel);

		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#entryForm-type-description-inputCmp-iframeEl"))).sendKeys("An Amazing Test Description");

		List<String> radioBtns = Arrays.asList("entryForm-radio-allow-on-sub-bodyEl", "entryForm-radio-attributes-bodyEl",
				"entryForm-radio-relationships-bodyEl", "entryForm-radio-contacts-bodyEl", "entryForm-radio-resources-bodyEl",
				"entryForm-radio-media-bodyEl", "entryForm-radio-dependencies-bodyEl", "entryForm-radio-eval-info-bodyEl", 
				"entryForm-radio-reviews-bodyEl", "entryForm-radio-questions-bodyEl");

		for (String btn : radioBtns) {

			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#" + btn))).click();
		}

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#entryTypeForm-save"))).click();

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[aria-valuetext*='Saving...']")));
		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("[aria-valuetext*='Saving...']")));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void editEntryTypes(WebDriver driver, String typeCode) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		assertTrue(tableClickRowCol(".x-grid-item-container", typeCode, driver, 0));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#lookupGrid-tools-edit"))).click();

		driver.switchTo().frame(wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#entryForm-type-description-inputCmp-iframeEl"))));

		((JavascriptExecutor) driver).executeScript("document.body.innerHTML='An Amazing Test Description - Edited'");
		driver.switchTo().parentFrame();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#entryTypeForm-save"))).click();

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[aria-valuetext*='Saving...']")));
		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("[aria-valuetext*='Saving...']")));
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public void toggleStatusEntryType(WebDriver driver, String typeCode) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		assertTrue(tableClickRowCol(".x-grid-item-container", typeCode, driver, 0));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#lookupGrid-tools-status"))).click();

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[aria-valuetext*='Updating Status...']")));
		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("[aria-valuetext*='Updating Status...']")));
		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}

		List<WebElement> allRows = new ArrayList<>();
		allRows = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.cssSelector(".x-grid-item-container"), By.tagName("tr")));

		int colIndex = getColumnHeaderIndex(driver, "Active Status");
		if (colIndex == -1) {
			assertTrue(false);
		}

		for (WebElement row : allRows) {

			List<WebElement> cells = new ArrayList<>();
			try {
				cells = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(row, By.tagName("td")));
				WebElement cell = cells.get(0);

				if (cell.getText().equals("AMAZING-TEST")) {

					WebElement cellActiveStatus = cells.get(colIndex);
					if (cellActiveStatus.getText().equals("I")) {
						assertTrue(true);
						LOG.log(Level.INFO, "Successfully set code to inactive");
						break;
					} else {
						LOG.log(Level.SEVERE, "Unable to toggle status of code");
						assertTrue(false);
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public void deleteEntryType(WebDriver driver, String typeCode) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		assertTrue(tableClickRowCol(".x-grid-item-container", typeCode, driver, 0));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#lookupGrid-tools-remove"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#moveExistingDataComboBox-inputEl"))).click();

		List<WebElement> typeChoices = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.cssSelector("#moveExistingDataComboBox-picker-listEl"), By.tagName("li")));
		for (WebElement type : typeChoices) {
			if (type.getText().equals("DI2E Component")) {
				type.click();
			}
		}

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#applyBtnDeleteEntryType"))).click();

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[aria-valuetext*='Deleting entry type...']")));
		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("[aria-valuetext*='Deleting entry type...']")));
		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}

		assertFalse(tableClickRowCol(".x-grid-item-container", typeCode, driver, 0));
	}

	public int getColumnHeaderIndex(WebDriver driver, String headerName)
	{

		WebDriverWait wait = new WebDriverWait(driver, 10);
		List<WebElement> headers = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".x-column-header")));
		int col = 0;
		for (WebElement header : headers) {

			if (header.getText().equals(headerName)) {
				return col;
			}
			col++;
		}
		return -1;
	}
}
