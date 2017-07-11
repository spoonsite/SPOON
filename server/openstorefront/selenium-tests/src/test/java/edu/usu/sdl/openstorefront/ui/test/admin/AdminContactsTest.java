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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author ccummings
 */
public class AdminContactsTest
		extends AdminTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

	@Test
	public void adminContactsTest() throws InterruptedException
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			setupDriver(driver);
			createContact(driver, "AAA-TesterFirst", "AAA-TesterLast", "MyAmazingTest-Organization", properties.getProperty("test.newuseremail"));
			createAPIContact();
			editContact(driver, "AAA-TesterFirst", "000-000-0000");
			toggleStatusContact(driver, "AAA-TesterFirst", "Active");
			Assert.assertTrue(verifyStatus(driver, "AAA-TesterFirst", "Inactive"));
			deleteContact(driver, "AAA-TesterFirst", "Inactive");
			sleep(1000);
		}
	}

	private void createAPIContact()
	{
		apiClient.getContactTestClient().createAPIContact("BBB-TesterFirst", "BBB-TesterLast", properties.getProperty("test.newuseremail"), "MyAmazingTest-Organization");
	}
	
	public void setupDriver(WebDriver driver)
	{
		webDriverUtil.getPage(driver, "AdminTool.action?load=Contacts");

		(new WebDriverWait(driver, 10)).until((ExpectedCondition<Boolean>) (WebDriver driverLocal) -> {
			List<WebElement> titleElements = driverLocal.findElements(By.id("contactGrid_header-title-textEl"));
			if (titleElements.size() > 0) {
				return titleElements.get(0).isDisplayed();
			} else {
				return false;
			}
		});
	}

	public void createContact(WebDriver driver, String fName, String lName, String organization, String email)
			throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#contactMngAddBtn"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name*='organization']"))).sendKeys(organization);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name*='firstName']"))).sendKeys(fName);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name*='lastName']"))).sendKeys(lName);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name*='email']"))).sendKeys(email);
		sleep(200);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test*='saveBtnAddContact'"))).click();

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#addEditWindow_header-title-textEl")));

		try {
			wait.until(ExpectedConditions.invisibilityOfElementWithText(By.cssSelector(".x-mask-msg-text"), "Loading..."));
		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}

		try {
			wait.until(ExpectedConditions.invisibilityOfElementWithText(By.cssSelector(".x-mask-msg-text"), "Saving..."));
		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}

		Assert.assertTrue(tableClickRowCol("#contactGrid-body", fName, driver, 0));

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#contactRefreshBtn"))).click();

	}

	public void editContact(WebDriver driver, String fName, String phone) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#contactRefreshBtn"))).click();

		try {
			wait.until(ExpectedConditions.invisibilityOfElementWithText(By.cssSelector(".x-mask-msg-text"), "Loading..."));
		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}

		Assert.assertTrue(tableClickRowCol("#contactGrid-body .x-grid-item-container", fName, driver, 0));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#contactMngEditBtn"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name*='phone']"))).sendKeys(phone);

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test*='saveBtnAddContact'"))).click();

		try {
			wait.until(ExpectedConditions.invisibilityOfElementWithText(By.cssSelector(".x-mask-msg-text"), "Loading..."));
		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#addEditWindow_header-title-textEl")));

		List<WebElement> allRows = new ArrayList<WebElement>();
		allRows = wait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.cssSelector("#contactGrid-body .x-grid-item-container"), By.tagName("tr")));

		int colIndex = getColumnHeaderIndex(driver, "Phone", ".x-grid-header-ct");

		for (WebElement row : allRows) {

			List<WebElement> cells = new ArrayList<WebElement>();
			try {
				cells = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(row, By.tagName("td")));
				WebElement cell = cells.get(0);

				if (cell.getText().equals(fName)) {

					WebElement cellPhone = cells.get(colIndex);
					if (cellPhone.getText().equals(phone)) {
						Assert.assertTrue(true);
						LOG.log(Level.INFO, "Successfully set code to inactive");
						break;
					} else {
						LOG.log(Level.SEVERE, "Unable to toggle status of code");
						Assert.assertTrue(false);
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public void toggleStatusContact(WebDriver driver, String fName, String currStatus) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#filterActiveStatus-inputEl"))).click();

		List<WebElement> status = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.cssSelector("#filterActiveStatus-picker"), By.cssSelector("li")));

		for (WebElement s : status) {

			if (s.getText().equals(currStatus)) {
				s.click();
				sleep(200);
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#contactRefreshBtn"))).click();

				try {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-mask-msg-text")));
				} catch (Exception e) {
					LOG.log(Level.INFO, e.toString());
				}
				try {
					wait.until(ExpectedConditions.invisibilityOfElementWithText(By.cssSelector(".x-mask-msg-text"), "Loading..."));
				} catch (Exception e) {
					LOG.log(Level.INFO, e.toString());
				}
				break;
			}
		}

		Assert.assertTrue(tableClickRowCol("#contactGrid-body .x-grid-item-container", fName, driver, 0));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test*='toggleStatusContactBtn']"))).click();

		List<WebElement> toolbarBtns = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.cssSelector(".x-toolbar.x-docked.x-toolbar-footer"), By.cssSelector(".x-btn[aria-hidden='false']")));

		for (WebElement button : toolbarBtns) {
			if (button.getText().equals("Yes")) {
				button.click();
			}
		}

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-mask-msg-text")));
		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}
		try {
			wait.until(ExpectedConditions.invisibilityOfElementWithText(By.cssSelector(".x-mask-msg-text"), "Loading..."));
		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}

	}

	public boolean verifyStatus(WebDriver driver, String fName, String expectedStatus)
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		boolean found = false;

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#filterActiveStatus-inputEl"))).click();

		List<WebElement> status = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.cssSelector("#filterActiveStatus-picker"), By.cssSelector("li")));

		for (WebElement s : status) {

			if (s.getText().equals(expectedStatus)) {
				s.click();
				sleep(200);
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#contactRefreshBtn"))).click();

				try {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-mask-msg-text")));
				} catch (Exception e) {
					LOG.log(Level.INFO, e.toString());
				}
				try {
					wait.until(ExpectedConditions.invisibilityOfElementWithText(By.cssSelector(".x-mask-msg-text"), "Loading..."));
				} catch (Exception e) {
					LOG.log(Level.INFO, e.toString());
				}
				break;
			}
		}

		List<WebElement> allRows = new ArrayList<WebElement>();
		allRows = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.cssSelector("#contactGrid-body .x-grid-item-container"), By.tagName("tr")));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-grid-header-ct")));

		int colIndex = getColumnHeaderIndex(driver, "Active Status", ".x-grid-header-ct .x-column-header-text");
		if (colIndex == -1) {
			Assert.assertTrue(false);
		}

		for (WebElement row : allRows) {

			List<WebElement> cells = new ArrayList<WebElement>();
			try {
				cells = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(row, By.tagName("td")));
				WebElement cell = cells.get(0);

				if (cell.getText().equals(fName)) {

					found = true;
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return found;
	}

	public int getColumnHeaderIndex(WebDriver driver, String headerName, String cssSelectorHeader)
	{
		System.out.println("My cssSelector; " + cssSelectorHeader);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		List<WebElement> headers = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(cssSelectorHeader)));
		int col = 0;
		for (WebElement header : headers) {

			if (header.getText().equals(headerName)) {
				return col;
			}
			col++;
		}
		return -1;
	}

	public void deleteContact(WebDriver driver, String contactFirstName, String currStatus) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#filterActiveStatus-inputEl"))).click();

		List<WebElement> status = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.cssSelector("#filterActiveStatus-picker"), By.cssSelector("li")));

		for (WebElement s : status) {

			if (s.getText().equals(currStatus)) {
				s.click();
				sleep(200);
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#contactRefreshBtn"))).click();

				try {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-mask-msg-text")));
				} catch (Exception e) {
					LOG.log(Level.INFO, e.toString());
				}
				try {
					wait.until(ExpectedConditions.invisibilityOfElementWithText(By.cssSelector(".x-mask-msg-text"), "Loading..."));
				} catch (Exception e) {
					LOG.log(Level.INFO, e.toString());
				}
				break;
			}
		}

		tableClickRowCol("#contactGrid-body .x-grid-item-container", contactFirstName, driver, 0);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='contactsDeleteBtn']"))).click();

		List<WebElement> toolbarBtns = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.cssSelector(".x-toolbar.x-docked.x-toolbar-footer"), By.cssSelector(".x-btn[aria-hidden='false']")));

		for (WebElement button : toolbarBtns) {
			if (button.getText().equals("Yes")) {
				button.click();
				sleep(200);
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#contactRefreshBtn"))).click();

				try {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-mask-msg-text")));
				} catch (Exception e) {
					LOG.log(Level.INFO, e.toString());
				}
				try {
					wait.until(ExpectedConditions.invisibilityOfElementWithText(By.cssSelector(".x-mask-msg-text"), "Loading..."));
				} catch (Exception e) {
					LOG.log(Level.INFO, e.toString());
				}
				break;
			}
		}
	}

	@AfterClass
	public static void cleanup()
	{
		apiClient.cleanup();
	}
}
