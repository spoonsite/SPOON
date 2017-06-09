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
package edu.usu.sdl.openstorefront.ui.test.user;

import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author dshurtleff
 */
public class UserProfileTest
		extends UserTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

	@Test
	public void userProfileTest()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			setup(driver);
			editProfile(driver, "cameron.cummings@sdl.usu.edu");
			sendTestMessage(driver);
		}
		
	}
	
	public void setup(WebDriver driver)
	{
		driver.get(webDriverUtil.getPage("UserTool.action"));

		(new WebDriverWait(driver, 10)).until((ExpectedCondition<Boolean>) (WebDriver driverLocal) -> {
			List<WebElement> titleElements = driverLocal.findElements(By.id("dashPanel_header-title-textEl"));
			if (titleElements.size() > 0) {
				return titleElements.get(0).isDisplayed();
			} else {
				return false;
			}
		});
	}
	
	public void editProfile(WebDriver driver, String email)
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#userHeaderProfileBtn"))).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name*='email']"))).clear();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name*='email']"))).sendKeys(email);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#saveProfileFormBtn"))).click();
		
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-mask-msg-text")));
		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}
		
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".x-mask-msg-text")));
		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}
	}
	
	public void sendTestMessage(WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#emailSendTestBtn"))).click();
		
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-window.x-toast")));
		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}
		
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".x-window.x-toast")));
		} catch (Exception e) {
			LOG.log(Level.INFO, e.toString());
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#dashboardUserHomeButton"))).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#dashPanel_header-title-textEl")));

	}
}

