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

import java.util.List;
import org.junit.After;
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
public class AdminSavedSearchTest
		extends AdminTestBase
{

	@BeforeClass
	public static void setupTest()
	{
		login();
	}

	@Test
	public void adminSavedSearchTest()
	{

		for (WebDriver driver : webDriverUtil.getDrivers()) {

			createSavedSearch(driver);
			editSavedSearch(driver);
			deleteSavedSearch(driver);
		}

	}

	public void createSavedSearch(WebDriver driver)
	{

		driver.get(webDriverUtil.getPage("AdminTool.action?load=Searches"));

		// click add button
		WebDriverWait waitAddBtn = new WebDriverWait(driver, 5);
		WebElement addBtn = waitAddBtn.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test = 'addBtnSearches']")));
		addBtn.click();

		// fill out form
		WebDriverWait waitSearchNameField = new WebDriverWait(driver, 5);
		WebElement searchNameField = waitSearchNameField.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#searchName-inputEl")));
		searchNameField.sendKeys("Regression DOD8500");

		WebDriverWait waitSearchType = new WebDriverWait(driver, 5);
		WebElement searchType = waitSearchType.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[name = 'searchType']")));
		searchType.click();

		WebDriverWait waitListTypes = new WebDriverWait(driver, 5);
		List<WebElement> types = waitListTypes.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.cssSelector("div[class*=x-boundlist-list-ct]:not([id*='filterActiveStatus-picker-listWrap'])"), By.tagName("li")));
		for (WebElement type : types) {
			if (type.getText().equals("Entry")) {
				type.click();
				break;
			}
		}

		WebDriverWait waitSearchField = new WebDriverWait(driver, 5);
		WebElement fieldType = waitSearchField.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[name = 'field']")));
		fieldType.click();

		WebDriverWait waitFields = new WebDriverWait(driver, 5);
		List<WebElement> fieldsLi = waitFields.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.cssSelector(".x-boundlist-list-ct.x-unselectable.x-scroller ul[aria-hidden='false']"), By.cssSelector("li")));
		for (WebElement field : fieldsLi) {
			if (field.getText().equals("Name")) {
				field.click();
				break;
			}
		}

		WebDriverWait waitValueField = new WebDriverWait(driver, 5);
		WebElement valueField = waitValueField.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[name='value']")));
		valueField.sendKeys("RegressionTest");

		WebDriverWait waitCheckBox = new WebDriverWait(driver, 5);
		WebElement checkBoxCase = waitCheckBox.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".x-form-item-body.x-form-item-body-default.x-form-cb-wrap.x-form-cb-wrap-default")));
		if (!checkBoxCase.isSelected()) {
			checkBoxCase.click();
		}

		// click add criteria button
		WebDriverWait waitAddCritBtn = new WebDriverWait(driver, 5);
		List<WebElement> addCritBtnList = waitAddCritBtn.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".x-btn-inner.x-btn-inner-default-small")));
		for (WebElement btn : addCritBtnList) {
			if (btn.getText().equals("Add Criteria")) {
				btn.click();
				break;
			}
		}
		// save search
		WebDriverWait waitSaveCriteriaBtn = new WebDriverWait(driver, 5);
		WebElement saveCriteriaBtn = waitSaveCriteriaBtn.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='saveSearchCritBtn'")));
		saveCriteriaBtn.click();

		WebDriverWait waitSearchTable = new WebDriverWait(driver, 5);
		WebElement searchTable = waitSearchTable.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".x-grid-view.x-grid-with-col-lines")));
		List<WebElement> searchesList = driver.findElements(By.tagName("td"));
		boolean inList = false;
		for (WebElement search : searchesList) {
			if (search.getText().equals("Regression DOD8500")) {
				inList = true;
			}
		}

		Assert.assertEquals(true, inList);
	}

	public void editSavedSearch(WebDriver driver)
	{
		// find search
		WebDriverWait waitSearchTable = new WebDriverWait(driver, 5);
		WebElement searchTable = waitSearchTable.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".x-grid-view.x-grid-with-col-lines")));
		List<WebElement> searchesList = driver.findElements(By.tagName("td"));
		WebElement mySearch = null;

		for (WebElement search : searchesList) {
			if (search.getText().equals("Regression DOD8500")) {
				mySearch = search;
			}
		}

		Assert.assertNotNull(mySearch);

		mySearch.click();

		WebDriverWait waitEditBtn = new WebDriverWait(driver, 5);
		WebElement editBtn = waitEditBtn.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='editBtnSearches']")));
		editBtn.click();

		// change search name
		WebDriverWait waitSearchNameField = new WebDriverWait(driver, 5);
		WebElement searchNameField = waitSearchNameField.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#searchName-inputEl")));
		searchNameField.clear();
		searchNameField.sendKeys("Regression DOD8500 - Edited");

		// save search
		WebDriverWait waitSaveCriteriaBtn = new WebDriverWait(driver, 5);
		WebElement saveCriteriaBtn = waitSaveCriteriaBtn.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='saveSearchCritBtn'")));
		saveCriteriaBtn.click();

		WebDriverWait waitForLoadingDone = new WebDriverWait(driver, 5);
		waitForLoadingDone.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".x-mask-msg")));

		WebDriverWait waitForListTd = new WebDriverWait(driver, 5);
		List<WebElement> mySearchList = waitForListTd.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.cssSelector(".x-grid-view.x-grid-with-col-lines"), By.tagName("td")));
		boolean isInList = false;

		for (WebElement item : mySearchList) {
			sleep(200);
			if (item.getText().equals("Regression DOD8500 - Edited")) {
				isInList = true;
				break;
			}
		}

		Assert.assertEquals(true, isInList);
	}

	public void deleteSavedSearch(WebDriver driver)
	{
		WebDriverWait waitSearchTable = new WebDriverWait(driver, 5);
		WebElement searchTable = waitSearchTable.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".x-grid-view.x-grid-with-col-lines")));
		WebDriverWait waitForListTd = new WebDriverWait(driver, 5);
		List<WebElement> searchesList = waitForListTd.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(searchTable, By.tagName("td")));
		WebElement mySearch = null;

		for (WebElement search : searchesList) {
			if (search.getText().equals("Regression DOD8500") || search.getText().equals("Regression DOD8500 - Edited")) {
				mySearch = search;
				mySearch.click();
			}
		}

		Assert.assertNotNull(mySearch);

		WebDriverWait waitToggleBtn = new WebDriverWait(driver, 5);
		WebElement toggleStatusBtn = waitToggleBtn.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='toggleBtnSearches']")));
		toggleStatusBtn.click();

		WebDriverWait waitForLoadingDone = new WebDriverWait(driver, 5);
		waitForLoadingDone.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".x-mask-msg")));
		
		// Check to see if deleted/inactive
		WebDriverWait waitForListElement = new WebDriverWait(driver, 5);
		List<WebElement> mySearchesList = waitForListElement.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.cssSelector(".x-grid-view.x-grid-with-col-lines"), By.tagName("td")));
		boolean inList = false;

		for (WebElement item : mySearchesList) {
			if (item.getText().equals("Regression DOD8500") || item.getText().equals("Regression DOD8500 - Edited")) {
				inList = true;
			}
		}

		waitForLoadingDone = new WebDriverWait(driver, 5);
		waitForLoadingDone.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".x-mask-msg")));

		Assert.assertEquals(false, inList);
	}

	@After
	public void cleanUpTest()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			WebDriverWait waitSearchTable = new WebDriverWait(driver, 5);
			WebElement searchTable = waitSearchTable.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".x-grid-view.x-grid-with-col-lines")));
			WebDriverWait waitForListTd = new WebDriverWait(driver, 5);
			List<WebElement> searchesList = waitForListTd.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(searchTable, By.tagName("td")));
			WebElement mySearch;

			for (WebElement search : searchesList) {
				if (search.getText().equals("Regression DOD8500") || search.getText().equals("Regression DOD8500 - Edited")) {
					mySearch = search;
					mySearch.click();

					WebDriverWait waitToggleBtn = new WebDriverWait(driver, 5);
					WebElement toggleStatusBtn = waitToggleBtn.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='toggleBtnSearches']")));
					toggleStatusBtn.click();

					break;
				}
			}

		}
	}
}
