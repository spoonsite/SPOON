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

import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.List;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author dshurtleff
 */
public class LoginTest
		extends BrowserTestBase
{
	@Test
	public void loginTest()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			driver.get(webDriverUtil.getPage("login.jsp"));

			// Find the text input element by its name
			WebElement element = driver.findElement(By.name("username"));
			element.sendKeys("admin");
			// Enter password and hit ENTER since submit does not seem to work.
			driver.findElement(By.name("password")).sendKeys("Secret1@", Keys.ENTER);

			// Now submit the form. WebDriver will find the form for us from the element
			//element.submit();
			// Wait for the page to load, timeout after 10 seconds
			(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>()
			{
				public Boolean apply(WebDriver driverLocal)
				{
					//login check may be home page or tool page
					List<WebElement> titleElements = driverLocal.findElements(By.id("homeTitle"));
					if (titleElements.size() > 0) {
						return titleElements.get(0).isDisplayed();
					} else {
						titleElements = driverLocal.findElements(By.id("titleTextId"));
						if (titleElements.size() > 0) {
							return titleElements.get(0).isDisplayed();
						} else {
							return false;
						}
					}
				}
			});
		}
	}

}
