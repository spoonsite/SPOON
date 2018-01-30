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
import edu.usu.sdl.openstorefront.selenium.provider.AuthenticationProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ClientApiProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ComponentProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ComponentTypeProvider;
import edu.usu.sdl.openstorefront.selenium.provider.NotificationEventProvider;
import edu.usu.sdl.openstorefront.selenium.provider.OrganizationProvider;
import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
public class ViewSearchResultEntryIT
		extends BrowserTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());
	private ClientApiProvider provider;
	private AttributeProvider attributeProvider;
	private OrganizationProvider organizationProvider;
	private ComponentProvider componentProvider;
	private ComponentTypeProvider componentTypeProvider;
	private AuthenticationProvider authProvider;
	private NotificationEventProvider notificationProvider;
	private String entryName = "SeleniumTest";
	private String entryOrganization = "Selenium Organization";

	@Before
	public void setup() throws InterruptedException
	{
		authProvider = new AuthenticationProvider(properties, webDriverUtil);
		authProvider.login();
		provider = new ClientApiProvider();
		attributeProvider = new AttributeProvider(provider.getAPIClient());
		organizationProvider = new OrganizationProvider(provider.getAPIClient());
		organizationProvider.createOrganization(entryOrganization);
		componentTypeProvider = new ComponentTypeProvider(provider.getAPIClient());
		componentProvider = new ComponentProvider(attributeProvider, organizationProvider, componentTypeProvider, provider.getAPIClient());
		componentProvider.createComponent(entryName, "Selenium entry for test", entryOrganization);
		notificationProvider = new NotificationEventProvider(provider.getAPIClient());
	}

	@Test
	public void viewSearchResultEntry()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			searchForEntry(driver, entryName);
			verifyResults(driver, entryName);
			viewFullPageEntry(driver, entryName);

		}
	}

	protected void searchForEntry(WebDriver driver, String searchName)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".home-search-field-new"))).sendKeys(searchName);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-btn-default-small"))).click();

	}

	protected void verifyResults(WebDriver driver, String entryName)
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
				entry.click();
				break;
			}
		}

		Assert.assertTrue(isResult);
	}

	protected void viewFullPageEntry(WebDriver driver, String entryName)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);

		WebElement detailsFrame = driver.findElement(By.cssSelector("iframe"));
		driver.switchTo().frame(detailsFrame);
		sleep(1500);

		WebElement title = driver.findElement(By.cssSelector(".details-title-name"));
		System.out.println("The title is " + title.getText());

		boolean isTitle = title.getText().contains(entryName);
		Assert.assertTrue(isTitle);

		String parentWindow = driver.getWindowHandle();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-qtip = 'Full Page']"))).click();

		Set<String> handles = driver.getWindowHandles();
		List<String> winHandles = new ArrayList<>(handles);

		driver.switchTo().window(winHandles.get(winHandles.size() - 1));

		WebElement entryTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#titlePanel-innerCt .details-title-name")));
		boolean isEntryTitle = entryTitle.getText().contains(entryName);

		Assert.assertTrue(isEntryTitle);

		driver.close();

		driver.switchTo().window(parentWindow);

		driver.switchTo().frame(detailsFrame);

		WebElement detailsTitle = driver.findElement(By.cssSelector(".details-title-name"));
		System.out.println("The title is " + title.getText());

		boolean isDetailsTitle = detailsTitle.getText().contains(entryName);
		Assert.assertTrue(isDetailsTitle);
	}

	@After
	public void cleanupTest() throws AttachedReferencesException
	{
		componentProvider.cleanup();
		notificationProvider.cleanup();
		provider.clientDisconnect();
	}
}
