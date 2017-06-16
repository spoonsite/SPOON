
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
package edu.usu.sdl.openstorefront.ui.test.security;

import edu.usu.sdl.apiclient.rest.resource.UserRegistrationResourceImpl;
import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Note on this test we don't want to login
 *
 * @author dshurtleff
 */
public class AccountSignupActivateTest
		extends SecurityTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());
	private static List<String> accountSignupIDs = new ArrayList<>();
	private static UserRegistrationResourceImpl apiAccountSignup = new UserRegistrationResourceImpl();

	@BeforeClass
	public static void setupTest()
	{
		String server = properties.getProperty("test.server", "http://localhost:8080/openstorefront/");
		String username = properties.getProperty("test.username");
		String password = properties.getProperty("test.password");
		apiAccountSignup.connect(username, password, server);
	}

	/**
	 *
	 */
	public AccountSignupActivateTest()
	{

	}

	@Test
	public void signupActivate() throws InterruptedException
	{
		signupActivate("auto-test-default");
	}

	// Order of tests when running all of the way through
	public void signupActivate(String userName) throws InterruptedException
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			deleteUserIfPresent(driver, userName);
			signupForm(driver, userName);
			activateAccount(driver, userName);
		}
	}

	// Delete if active
	private void deleteUserIfPresent(WebDriver driver, String userName) throws InterruptedException
	{
		driver.get(webDriverUtil.getPage("AdminTool.action?load=User-Management"));

		// TODO:  Per STORE-1658, we need an ALL in the drop-down boxes.  
		for (int loop = 0; loop < 3; loop++) {
			String activeStatusDropDownText;
			String approvalStatusDropDownText;

			// Filter on *Active Status*
			if ((loop == 0) || (loop == 3)) {
				activeStatusDropDownText = "Active";
			} else {
				activeStatusDropDownText = "Locked/Disabled";
			}

			setActiveStatus(activeStatusDropDownText, driver);

			// Filter on *Approval Status*
			if ((loop == 0) || (loop == 1)) {
				approvalStatusDropDownText = "Approved";
			} else {
				approvalStatusDropDownText = "Pending";
			}
			// ActiveStatusDropDown arrow and get list of elements in it
			setApprovalStatus(approvalStatusDropDownText, driver);

			// Drop-down selectors finished, now search for user in the table and delete if present
			if (tableClickRowCol("[data-test='xPanelTable'] .x-grid-item-container", userName, driver, 0)) {
				driver.findElement(By.xpath("//span[contains(.,'Delete')]")).click();

				// WAIT for confirmation button to come up
				driver.findElement(By.xpath("//span[@id='button-1037-btnInnerEl']")).click();  // Confirmation button "YES"

				// WAIT for deletion
				LOG.log(Level.INFO, "*** EXISTING User '" + userName + "' DELETED ***");
				loop = 99;  // exit
			}
		}
	}

	public void signupForm(WebDriver driver, String userName)
	{
		// Navigate to the registration page
		driver.get(webDriverUtil.getPage("registration.jsp"));
		sleep(2000);
		// Fill out the form
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(userName);
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(userName + "A1!");
		driver.findElement(By.xpath("//input[@name='confirmPassword']")).sendKeys(userName + "A1!");
		driver.findElement(By.xpath("//input[@name='firstName']")).sendKeys(userName);
		driver.findElement(By.xpath("//input[@name='lastName']")).sendKeys(userName);
		driver.findElement(By.xpath("//input[@name='organization']")).sendKeys("Air Force");
		driver.findElement(By.xpath("//input[@name='email']")).sendKeys("blaine.esplin@sdl.usu.edu");
		driver.findElement(By.xpath("//input[@name='phone']")).sendKeys("435-555-5555");
		sleep(500);

		// SUBMIT the form
		driver.findElement(By.xpath("//span[@id='button-1026-btnInnerEl']")).click();

		// WAIT for signup to complete 
		LOG.log(Level.INFO, "--- User '" + userName + "' CREATED ---");

	}

	private void activateAccount(WebDriver driver, String userName) throws InterruptedException
	{
		// Navigate to Admin Tools -> Application Management -> User Tools to activate
		driver.get(webDriverUtil.getPage("AdminTool.action?load=User-Management"));
		// Switch to activeStatus = Locked/Disabled and approvalStatus = Pending so it can be approved

		setActiveStatus("Locked/Disabled", driver);
		setApprovalStatus("Pending", driver);

		// Select and click Approve
		if (tableClickRowCol("[data-test='xPanelTable'] .x-grid-view", userName, driver, 0)) {
			driver.findElement(By.xpath("//a[contains(.,'Approve')]")).click();

			// Wait for Approving User Display Block to go away
			WebDriverWait wait = new WebDriverWait(driver, 20);

			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='xPanelTable'] .x-component.x-border-box.x-mask.x-component-default[aria-hidden*='false'] .x-mask-msg-text")));
			} catch (Exception e) {
				LOG.log(Level.INFO, e.toString());
			}

			try {
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("[data-test='xPanelTable'] x-component.x-border-box.x-mask.x-component-default .x-mask-msg-text")));
			} catch (Exception e) {
				LOG.log(Level.INFO, e.toString());
			}

			// Change filter to Active and Approved 
			setActiveStatus("Active", driver);
			setApprovalStatus("Approved", driver);

			// Verify user has been approved
			if (tableClickRowCol("[data-test='xPanelTable'] .x-grid-view", userName, driver, 0)) {
				LOG.log(Level.INFO, "--- User '" + userName + "' APPROVED and in the Active, Approved User Management List ---");
			} else {
				LOG.log(Level.SEVERE, "!!! User '" + userName + "' was NOT approved !!!  Check the User Mangement page under Active Status = Locked/ Disabled and Approval Status = Pending.  MANUALLY approve the user " + userName);
			}
		}

		// Login as newly created and approved user
		login(userName, userName.toLowerCase() + "A1!");
		LOG.log(Level.INFO,
				"--- Logged in as new user '" + userName + "' ---");
		login(); //logout and log back in as admin
	}

	public void setActiveStatus(String activeStatusDropDownText, WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);

		// ActiveStatusDropDown arrow and get list of elements in it
		WebElement ActiveStatArrow = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#filterActiveStatus-inputEl")));
		ActiveStatArrow.click();
		List<WebElement> statusList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#filterActiveStatus-picker-listEl li")));
		// Change Active Status to looped value set above
		for (WebElement element : statusList) {
			if (element.getText().equals(activeStatusDropDownText)) {
				element.click();
			}
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("[data-ref='msgWrapEl']")));
	}

	public void setApprovalStatus(String approvalStatusDropDownText, WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);

		WebElement ApprStatArrow = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#filterApprovalStatus-inputEl")));
		ApprStatArrow.click();

		List<WebElement> thestatusList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#filterApprovalStatus-picker-listEl li")));
		// Change Active Status to looped value set above
		for (WebElement element : thestatusList) {
			if (element.getText().equals(approvalStatusDropDownText)) {
				element.click();
			}
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("[data-ref='msgWrapEl']")));
	}

	@AfterClass
	public static void cleanup()
	{
		for (String id : accountSignupIDs) {

		}

		apiAccountSignup.disconnect();
	}
}
