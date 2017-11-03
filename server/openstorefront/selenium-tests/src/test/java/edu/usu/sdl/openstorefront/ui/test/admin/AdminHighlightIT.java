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

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author ccummings
 */
public class AdminHighlightIT
		extends AdminTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

	@Test
	public void adminHighlightTest() throws InterruptedException, JsonProcessingException
	{

		for (WebDriver driver : webDriverUtil.getDrivers()) {

			createHighlight(driver);
			createAPIHighlight();
			createAPISavedSearch();
			editHighlight(driver);
			addSavedSearchToHighlight(driver);
			deleteHighlight(driver);

		}
	}

	public void createAPIHighlight()
	{
		apiClient.getHighlightTestClient().createAPIHighlight();
	}

	public void createAPISavedSearch() throws JsonProcessingException
	{
		apiClient.getSystemSearchTestClient().createAPISystemSearch();
	}

	public void createHighlight(WebDriver driver) throws InterruptedException
	{

		webDriverUtil.getPage(driver, "AdminTool.action?load=Highlights");
		WebDriverWait wait = new WebDriverWait(driver, 5);

		// Click add button
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#highlightGrid-tools-add"))).click();

		// Fill out form
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#highlightEntryForm-Title-inputEl"))).sendKeys("TestHighlight1");

		((JavascriptExecutor) driver).executeScript("tinyMCE.activeEditor.setContent('Test Description 1')");

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#highlightEntryForm-Type-trigger-picker"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(.,'Component')]"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test = 'addEditHighlightSave']"))).click();

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#highlightGrid-body .x-component.x-border-box.x-mask")));
		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#highlightGrid-body .x-component.x-border-box.x-mask")));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void editHighlight(WebDriver driver) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		// locate highlight in table and edit
		if (tableClickRowCol("#highlightGrid-body .x-grid-view", "TestHighlight1", driver, 0)) {

			// Click Edit
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#highlightGrid-tools-edit"))).click();

			// Add/Edit text in tinyMCE textarea
			((JavascriptExecutor) driver).executeScript("tinyMCE.activeEditor.setContent('Test Description 1 - Edited')");

			// click save
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test = 'addEditHighlightSave']"))).click();

			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#highlightGrid-body .x-component.x-border-box.x-mask")));
			} catch (Exception e) {
				System.out.println(e);
			}

			try {
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#highlightGrid-body .x-component.x-border-box.x-mask")));
			} catch (Exception e) {
				System.out.println(e);
			}
			// refresh table
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='highlightRefreshBtn']"))).click();
		}
	}

	public void addSavedSearchToHighlight(WebDriver driver) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		// locate highlight in table and edit
		Assert.assertTrue(tableClickRowCol("#highlightGrid-body .x-grid-view", "TestHighlight1", driver, 0));

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#highlightGrid-tools-edit"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".mce-ico.mce-i-none"))).click();

		try {
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("x-title-text.x-title-text-default.x-title-item"), "Insert Link to Saved Search"));

		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}

		Assert.assertTrue(tableClickRowCol("#linkToSaveSearchWindow-body .x-grid-item-container", "An API Test Admin Saved Search", driver, 0));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#insertLinkBtn"))).click();
		
		sleep(2000);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test = 'addEditHighlightSave']"))).click();

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#highlightGrid-body .x-component.x-border-box.x-mask")));
		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#highlightGrid-body .x-component.x-border-box.x-mask")));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void deleteHighlight(WebDriver driver) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		assertTrue(tableClickRowCol("#highlightGrid-body .x-grid-view", "TestHighlight1", driver, 0));

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#highlightGrid-tools-delete"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".x-message-box .x-window-bodyWrap .x-btn:not([style*='display'])"))).click();

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#highlightGrid-body .x-component.x-border-box.x-mask")));
		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#highlightGrid-body .x-component.x-border-box.x-mask")));
		} catch (Exception e) {
			System.out.println(e);
		}

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='highlightRefreshBtn']"))).click();
	}

	@After
	public void cleanUpTest() throws InterruptedException
	{

	}

}
