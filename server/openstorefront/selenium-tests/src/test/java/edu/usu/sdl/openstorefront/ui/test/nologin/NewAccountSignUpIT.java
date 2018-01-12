/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.ui.test.nologin;

import edu.usu.sdl.openstorefront.core.entity.UserRegistration;
import edu.usu.sdl.openstorefront.selenium.apitestclient.UserRegistrationTestClient;
import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author rfrazier
 */
public class NewAccountSignUpIT
		extends BrowserTestBase
{

	private final String organization = "Storefront";
	private static final String userName = "BobTester";
	private static final Logger LOG = Logger.getLogger(NewAccountSignUpIT.class.getName());

	@Test
	public void TestNoLogin()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			webDriverUtil.getPage(driver, "registration.jsp");
			submitRegistrationForm(driver);

			// login as admin and verify registration request
			login();
			webDriverUtil.getPage(driver, "AdminTool.action?load=User-Management");
			verifyRegistration(driver);
		}
	}

	private void verifyRegistration(WebDriver driver)
	{
		// click on Registrations tab
		List<WebElement> tabs = driver.findElements(By.cssSelector(".x-tab.x-unselectable.x-box-item"));
		tabs.get(1).click();
		// find username in table
		WebElement table = driver.findElements(By.cssSelector(".x-panel-body.x-grid-with-col-lines.x-grid-with-row-lines")).get(1);
		String text = table.getText();
		assert text.contains(userName);
	}

	private void submitRegistrationForm(WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 20);
		// Fill out the form
		driver.findElement(By.cssSelector("input[name='username']")).sendKeys(userName);
		driver.findElement(By.cssSelector("input[name='password']")).sendKeys(userName + "A1!");
		driver.findElement(By.cssSelector("input[name='confirmPassword']")).sendKeys(userName + "A1!");
		driver.findElement(By.cssSelector("input[name='firstName']")).sendKeys(userName);
		driver.findElement(By.cssSelector("input[name='lastName']")).sendKeys(userName);
		driver.findElement(By.cssSelector("input[name='organization']")).sendKeys(organization);
		driver.findElement(By.cssSelector("input[name='email']")).sendKeys(userName + "@test.com");
		driver.findElement(By.cssSelector("input[name='phone']")).sendKeys("435-555-5555");

		// retrieve verfication code from server
		String registrationId = getRegistrationId(driver);
		Assert.assertNotEquals("failed to load registration ID", registrationId, "");

		UserRegistrationTestClient client = apiClient.getUserRegistrationTestClient();
		UserRegistration registration = client.getAPIUserRegistration(registrationId);

		driver.findElement(By.cssSelector("input[name='verificationCode']")).sendKeys(registration.getVerificationCode());

		LOG.log(Level.INFO, "--- verification Code {0} CREATED for {1} ---", new Object[]{registration.getVerificationCode(), registrationId});

		// SUBMIT the form
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Signup"))).click();

		// WAIT for signup to complete 
		LOG.log(Level.INFO, "--- User ''{0}'' CREATED ---", userName);

		// click login
		driver.findElement(By.cssSelector("[data-test='loginAfterSignup']")).click();
	}

	private String getRegistrationId(WebDriver driver)
	{

		WebDriverWait wait = new WebDriverWait(driver, 20);
		// generate the email verification code
		driver.findElement(By.id("verificationCodeButton")).click();
		// click confirmation pop-up
		driverWait(() -> wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("x-component.x-border-box.x-mask.x-component-default .x-mask-msg-text"))), 5000);
		driverWait(() -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[id*=\"messagebox-\"].x-window.x-message-box a"))), 5000);
		driver.findElement(By.cssSelector("[id*=\"messagebox-\"].x-window.x-message-box a")).click();

		// get the registrationID
		long startTime = System.currentTimeMillis();
		long maxMilliSeconds = 5000;
		String registrationId = "";
		WebElement element = driver.findElement(By.id("registrationId-inputEl"));
		while (registrationId.equals("") && (System.currentTimeMillis() - startTime) < maxMilliSeconds) {
			registrationId = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].value;", element);
		}
		return registrationId;
	}
	
	@AfterClass
	public static void cleanupTest()
	{
		apiClient.getUserTestClient().deleteAPIUser(userName);
	}
}
