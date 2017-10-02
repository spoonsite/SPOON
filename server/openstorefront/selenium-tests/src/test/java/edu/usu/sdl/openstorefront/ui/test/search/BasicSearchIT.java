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
import edu.usu.sdl.openstorefront.core.view.ComponentAdminView;
import edu.usu.sdl.openstorefront.ui.test.admin.AdminSavedSearchIT;
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
public class BasicSearchIT
		extends SearchTestBase
{

	private static final Logger LOG = Logger.getLogger(AdminSavedSearchIT.class.getName());
	private static String entryName = "A Selenium Test Entry";
	
	@BeforeClass
	public static void basicSearchComponent()
	{
		createBasicSearchComponent(entryName);
	}

	@Test
	public void basicSearchWithoutQuotesLandingPage()
	{

		for (WebDriver driver : webDriverUtil.getDrivers()) {

			searchFromLandingPage(driver, "Test");
			verifyResults(driver, entryName);

		}

	}
	 
	@Test
	public void basicSearchWithQuotesLandingPage()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			
			searchFromLandingPage(driver, "\"A Selenium Test Entry\"");
			verifyResults(driver, entryName);
		}
 	}

	@Test
	public void basicSearchWithQuotesResultsPage()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			
			searchFromResultsPage(driver, "\"A Selenium Test Entry\"");
			verifyResults(driver, entryName);
		}
	}
	
	@Test
	public void basicSearchWithoutQuotesResultsPage()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			
			searchFromResultsPage(driver, "Test");
			verifyResults(driver, entryName);
		}
	}
	
	protected void searchFromLandingPage(WebDriver driver, String search)
	{
		webDriverUtil.getPage(driver, "Landing.action");
		
		WebDriverWait wait = new WebDriverWait(driver, 8);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".home-search-field-new"))).sendKeys(search);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-btn-default-small"))).click();

		sleep(1500);

	}
	
	protected void searchFromResultsPage(WebDriver driver, String search)
	{
		webDriverUtil.getPage(driver, "searchResults.jsp");
		
		WebDriverWait wait = new WebDriverWait(driver, 8);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#searchTextFieldResults-inputEl"))).sendKeys(search);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-btn-default-large"))).click();
		
		sleep(1500);
	}

	public void verifyResults(WebDriver driver, String entryName)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);
		List<WebElement> entryResults = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#resultsDisplayPanel-innerCt h2")));

		boolean isResult = false;

		for (WebElement entry : entryResults) {

			System.out.println("Entry Name: " + entry.getText());
			if (entry.getText().equals(entryName)) {
				isResult = true;
			}
		}

		Assert.assertTrue(isResult);
	}

	public static void createBasicSearchComponent(String componentName)
	{
		Component myEntry = apiClient.getComponentRESTTestClient().createAPIComponent(componentName);
		System.out.println("Entry Name: " + myEntry.getName());
		ComponentAdminView entry = null;

		int timer = 0;

		while (entry == null && timer < 10000) {

			timer += 200;
			sleep(200);
			entry = apiClient.getComponentRESTTestClient().getComponentByName(componentName);

		}
	}
}
