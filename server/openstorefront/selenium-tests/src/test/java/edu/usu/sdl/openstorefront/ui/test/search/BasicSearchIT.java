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

import edu.usu.sdl.openstorefront.common.exception.AttachedReferencesException;
import edu.usu.sdl.openstorefront.selenium.provider.AttributeProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ClientApiProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ComponentProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ComponentTypeProvider;
import edu.usu.sdl.openstorefront.selenium.provider.OrganizationProvider;
import edu.usu.sdl.openstorefront.ui.test.admin.AdminSavedSearchIT;
import java.util.ArrayList;
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
public class BasicSearchIT
		extends SearchTestBase
{

	private static final Logger LOG = Logger.getLogger(AdminSavedSearchIT.class.getName());
	private static String entryName = "SeleniumTest";
	private static String organizationName = "SeleniumOrganization";
	private static String compDescription = "SeleniumTest Description";
	private static ClientApiProvider provider;
	private static AttributeProvider attributeProvider;
	private static OrganizationProvider organizationProvider;
	private static ComponentProvider componentProvider;
	private static ComponentTypeProvider componentTypeProvider;
	private String searchNoQuotes = "SeleniumTest";
	private String searchWithQuotes = "\"SeleniumTest\"";

	@Before
	public void basicSearchComponent()
	{
		provider = new ClientApiProvider();
		attributeProvider = new AttributeProvider(provider.getAPIClient());
		organizationProvider = new OrganizationProvider(provider.getAPIClient());
		componentTypeProvider = new ComponentTypeProvider(provider.getAPIClient());
		componentProvider = new ComponentProvider(attributeProvider, organizationProvider, componentTypeProvider, provider.getAPIClient());
		componentProvider.createComponent(entryName, compDescription, organizationName);
		sleep(1000);
	}

	@Test
	public void basicSearchWithoutQuotesLandingPage()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			searchFromLandingPage(driver, searchNoQuotes);
			verifyResults(driver, entryName, searchNoQuotes);

		}
	}

	@Test
	public void basicSearchWithQuotesLandingPage()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			searchFromLandingPage(driver, searchWithQuotes);
			verifyResults(driver, entryName, searchWithQuotes);
		}
	}

	@Test
	public void basicSearchWithQuotesResultsPage()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			searchFromResultsPage(driver, searchWithQuotes);
			verifyResults(driver, entryName, searchWithQuotes);
		}
	}

	@Test
	public void basicSearchWithoutQuotesResultsPage()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			searchFromResultsPage(driver, searchNoQuotes);
			verifyResults(driver, entryName, searchNoQuotes);
		}
	}

	protected void searchFromLandingPage(WebDriver driver, String search)
	{
		webDriverUtil.getPage(driver, "Landing.action");

		WebDriverWait wait = new WebDriverWait(driver, 8);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".home-search-field-new"))).sendKeys(search);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-btn-default-small"))).click();

		sleep(1000);

	}

	protected void searchFromResultsPage(WebDriver driver, String search)
	{
		webDriverUtil.getPage(driver, "searchResults.jsp");

		WebDriverWait wait = new WebDriverWait(driver, 8);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#searchTextFieldResults-inputEl"))).sendKeys(search);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-btn-default-large"))).click();
	}

	public void verifyResults(WebDriver driver, String entryName, String searchName)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);

		List<WebElement> entryResults = new ArrayList<>();

		long startTime = System.currentTimeMillis();

		while (entryResults.isEmpty() && (System.currentTimeMillis() - startTime) < 60000) {

			entryResults = driver.findElements(By.cssSelector("#resultsDisplayPanel-innerCt h2"));
			
			if (entryResults.isEmpty()) {
				
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-btn-default-large"))).click();
			}
		}

		boolean isResult = false;

		for (WebElement entry : entryResults) {

			if (entry.getText().equals(entryName)) {
				isResult = true;
			}
		}

		Assert.assertTrue(isResult);
	}

	@After
	public void cleanupTest() throws AttachedReferencesException
	{
		componentProvider.cleanup();
		provider.clientDisconnect();
	}
}
