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
package edu.usu.sdl.openstorefront.selenium.provider;

import edu.usu.sdl.openstorefront.selenium.util.WebDriverUtil;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author ccummings
 */
public class AuthenticationProvider
{

	protected WebDriverUtil webDriverUtil;
	protected Properties properties;
	private static final Logger LOG = Logger.getLogger(AuthenticationProvider.class.getName());

	public AuthenticationProvider(Properties prop, WebDriverUtil util)
	{
		webDriverUtil = util;
		properties = prop;
	}

	public void login() throws InterruptedException
	{
		String username = properties.getProperty("test.username");
		String password = properties.getProperty("test.password");
		login(username, password);
	}

	protected void login(String userName, String passWord) throws InterruptedException
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			WebDriverWait wait = new WebDriverWait(driver, 20);
			// Make sure logged out before attempting login.
			webDriverUtil.getPage(driver, "Login.action?Logout");

			// Now log in
			webDriverUtil.getPage(driver, "login.jsp");

			WebElement userNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
			userNameElement.sendKeys(userName);

			// Enter password and hit ENTER since submit does not seem to work.
			WebElement userPassword = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
			userPassword.sendKeys(passWord);
			Thread.sleep(1000);

			WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='loginBtn']")));
			loginBtn.click();

			// Look for the titleText
			try {
				wait.until(ExpectedConditions.stalenessOf(userNameElement));
				wait.until(ExpectedConditions.titleContains("Storefront"));
				LOG.log(Level.INFO, "*** Sucessfully logged in as ''{0}'' ***", userName);
			} catch (Exception e) {
				LOG.log(Level.WARNING, "--- EXCEPTION --- {0}", e);
				String message = driver.findElement(By.cssSelector(".showError")).getText();
				LOG.log(Level.WARNING, "--- Problem logging in as ''{0}'' ---\n Login Page MESSAGE is: --- ''{1}'' ---", new Object[]{userName, message});
			}
		}
	}
}
