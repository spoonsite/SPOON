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

import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.common.exception.AttachedReferencesException;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.selenium.provider.AttributeProvider;
import edu.usu.sdl.openstorefront.selenium.provider.AuthenticationProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ClientApiProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ComponentProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ComponentTypeProvider;
import edu.usu.sdl.openstorefront.selenium.provider.NotificationEventProvider;
import edu.usu.sdl.openstorefront.selenium.provider.OrganizationProvider;
import edu.usu.sdl.openstorefront.selenium.provider.TagProvider;
import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
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
public class AddTagSearchResultEntryIT
		extends BrowserTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());
	private ClientApiProvider provider;
	private ComponentProvider componentProvider;
	private OrganizationProvider orgProvider;
	private TagProvider tagProvider;
	private AuthenticationProvider authProvider;
	private NotificationEventProvider notificationProvider;
	private String entryName1 = "A Selenium Test Entry";
	private String entryName2 = "Another Selenium Test Entry";
	private String tagName = "seleniumTag";
	private String organizationName = "Selenium Organization";

	@Before
	public void setup() throws InterruptedException
	{
		authProvider = new AuthenticationProvider(properties, webDriverUtil);
		authProvider.login();
		provider = new ClientApiProvider();
		ClientAPI apiClient = provider.getAPIClient();
		orgProvider = new OrganizationProvider(apiClient);
		orgProvider.createOrganization(organizationName);
		componentProvider = new ComponentProvider(new AttributeProvider(apiClient), orgProvider, new ComponentTypeProvider(apiClient), apiClient);
		componentProvider.createComponent(entryName1, "First Selenium Entry", organizationName);

		Component entry2 = componentProvider.createComponent(entryName2, "Second Selenium Entry", organizationName);
		tagProvider = new TagProvider(apiClient);
		tagProvider.addTagToComponent(entry2, tagName);
		notificationProvider = new NotificationEventProvider(provider.getAPIClient());
		sleep(2000);
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

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".home-search-field-new"))).sendKeys("Selenium");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-btn-default-small"))).click();

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

	@After
	public void cleanupTest() throws AttachedReferencesException
	{
		componentProvider.cleanup();
		notificationProvider.cleanup();
		provider.clientDisconnect();
	}
}
