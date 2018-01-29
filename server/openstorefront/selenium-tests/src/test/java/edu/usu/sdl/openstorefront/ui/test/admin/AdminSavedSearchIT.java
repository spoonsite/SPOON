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
import edu.usu.sdl.openstorefront.selenium.provider.ClientApiProvider;
import edu.usu.sdl.openstorefront.selenium.provider.SystemSearchProvider;
import java.util.List;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
public class AdminSavedSearchIT
		extends AdminTestBase
{

	private static final Logger LOG = Logger.getLogger(AdminSavedSearchIT.class.getName());
	private ClientApiProvider provider;
	private SystemSearchProvider searchProvider;
	private String searchName = "Selenium Saved Search";

	@Before
	public void setup() throws JsonProcessingException
	{
		provider = new ClientApiProvider();
		searchProvider = new SystemSearchProvider(provider.getAPIClient());
		searchProvider.createSystemSearch(searchName);
	}

	@Test
	public void adminSavedSearchTest() throws JsonProcessingException
	{

		for (WebDriver driver : webDriverUtil.getDrivers()) {

			webDriverUtil.getPage(driver, "AdminTool.action?load=Searches");
			createSavedSearch(driver);
			editSavedSearch(driver);
			sleep(2000);
			deleteSavedSearch(driver);
		}

	}

	public void createSavedSearch(WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		// click add button
		WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test = 'addBtnSearches']")));
		addBtn.click();

		// fill out form
		WebElement searchNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#searchName-inputEl")));
		searchNameField.sendKeys("Selenium DOD8500");

		WebElement searchType = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[name = 'searchType']")));
		searchType.click();

		List<WebElement> types = wait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.cssSelector("div[class*=x-boundlist-list-ct]:not([id*='filterActiveStatus-picker-listWrap'])"), By.tagName("li")));
		for (WebElement type : types) {
			if (type.getText().equals("Entry")) {
				type.click();
				break;
			}
		}

		WebElement fieldType = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[name = 'field']")));
		fieldType.click();

		List<WebElement> fieldsLi = wait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.cssSelector(".x-boundlist-list-ct.x-unselectable.x-scroller ul[aria-hidden='false']"), By.cssSelector("li")));
		for (WebElement field : fieldsLi) {
			if (field.getText().equals("Name")) {
				field.click();
				break;
			}
		}

		WebElement valueField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[name='value']")));
		valueField.sendKeys("RegressionTest");

		WebElement checkBoxCase = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".x-form-item-body.x-form-item-body-default.x-form-cb-wrap.x-form-cb-wrap-default")));
		if (!checkBoxCase.isSelected()) {
			checkBoxCase.click();
		}

		// click add criteria button
		List<WebElement> addCritBtnList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".x-btn-inner.x-btn-inner-default-small")));
		for (WebElement btn : addCritBtnList) {
			if (btn.getText().equals("Add Criteria")) {
				btn.click();
				break;
			}
		}
		// save search
		WebElement saveCriteriaBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='saveSearchCritBtn'")));
		saveCriteriaBtn.click();

		String searchId = searchProvider.getSystemSearchByName("Selenium DOD8500").getSearchId();
		searchProvider.registerSearchId(searchId);

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#searchgrid-body .x-component.x-border-box.x-mask")));
		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#searchgrid-body .x-component.x-border-box.x-mask")));
		} catch (Exception e) {
			System.out.println(e);
		}

		List<WebElement> searchesList = wait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.cssSelector(".x-grid-view"), By.tagName("td")));
		boolean inList = false;
		for (WebElement search : searchesList) {
			if (search.getText().equals("Selenium DOD8500")) {
				inList = true;
			}
		}

		Assert.assertTrue(inList);
	}

	public void editSavedSearch(WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);

		// find search
		WebElement searchTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".x-grid-view.x-grid-with-col-lines")));
		List<WebElement> searchesList = driver.findElements(By.tagName("td"));
		WebElement mySearch = null;

		for (WebElement search : searchesList) {
			if (search.getText().equals("Selenium DOD8500")) {
				mySearch = search;
			}
		}

		Assert.assertNotNull(mySearch);
		mySearch.click();

		WebElement editBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='editBtnSearches']")));
		editBtn.click();

		// change search name
		WebElement searchNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#searchName-inputEl")));
		searchNameField.clear();
		searchNameField.sendKeys("Selenium DOD8500 - Edited");

		// save search
		WebElement saveCriteriaBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='saveSearchCritBtn'")));
		saveCriteriaBtn.click();

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#searchgrid-body .x-component.x-border-box.x-mask")));
		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#searchgrid-body .x-component.x-border-box.x-mask")));
		} catch (Exception e) {
			System.out.println(e);
		}

		List<WebElement> mySearchList = wait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.cssSelector(".x-grid-view.x-grid-with-col-lines"), By.tagName("td")));
		boolean isInList = false;

		for (WebElement item : mySearchList) {
			sleep(200);
			if (item.getText().equals("Selenium DOD8500 - Edited")) {
				isInList = true;
				break;
			}
		}

		Assert.assertEquals(true, isInList);
	}

	public void deleteSavedSearch(WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);

		WebElement searchTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".x-grid-view.x-grid-with-col-lines")));;
		List<WebElement> searchesList = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(searchTable, By.tagName("td")));
		WebElement mySearch = null;

		for (WebElement search : searchesList) {
			if (search.getText().equals("Selenium DOD8500") || search.getText().equals("Selenium DOD8500 - Edited")) {
				mySearch = search;
				mySearch.click();
			}
		}

		Assert.assertNotNull(mySearch);

		WebElement toggleStatusBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='toggleBtnSearches']")));
		toggleStatusBtn.click();

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#searchgrid-body .x-component.x-border-box.x-mask")));
		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#searchgrid-body .x-component.x-border-box.x-mask")));
		} catch (Exception e) {
			System.out.println(e);
		}

		// Check to see if deleted/inactive
		List<WebElement> mySearchesList = wait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.cssSelector(".x-grid-view.x-grid-with-col-lines"), By.tagName("td")));
		boolean inList = false;

		for (WebElement item : mySearchesList) {
			if (item.getText().equals("Selenium DOD8500") || item.getText().equals("Selenium DOD8500 - Edited")) {
				inList = true;
			}
		}

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".x-mask-msg")));

		Assert.assertEquals(false, inList);
	}

	@After
	public void cleanUpTest()
	{
		searchProvider.cleanup();
		provider.clientDisconnect();
	}
}
