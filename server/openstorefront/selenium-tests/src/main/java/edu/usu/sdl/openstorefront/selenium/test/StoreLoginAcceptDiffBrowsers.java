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
package edu.usu.sdl.openstorefront.selenium.test;

/**
 *
 * @author besplin
 */
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StoreLoginAcceptDiffBrowsers
{
	private static final Logger LOG = Logger.getLogger(StoreLoginAcceptDiffBrowsers.class.getName());

	public static void main(String[] args)
	{
		//Setup browser drivers
		System.setProperty("webdriver.gecko.driver", "drivers\\firefox\\geckodriver.exe");
		System.setProperty("webdriver.edge.driver", "drivers\\edge\\MicrosoftWebDriver.exe");
		System.setProperty("webdriver.chrome.driver", "drivers\\chrome\\chromedriver.exe");

		// Different browser drivers, loop through
		List<WebDriver> drivers = new ArrayList<>();
		drivers.add(new FirefoxDriver());
		drivers.add(new EdgeDriver()); //NOTE:  ALL Instances of Chrome MUST be closed or it will throw an error
		drivers.add(new ChromeDriver());

		for (WebDriver driver : drivers) {
			// Generic driver set to browser
			driver.get("http://store-accept.usu.di2e.net/openstorefront/");

			// Find the text input element by its name
			WebElement element = driver.findElement(By.name("username"));
			element.sendKeys("admin");
			// Enter password and hit ENTER since submit does not seem to work.
			driver.findElement(By.name("password")).sendKeys("Secret1@", Keys.ENTER);

			// Wait for the page to load, timeout after 10 seconds
			(new WebDriverWait(driver, 12)).until(new ExpectedCondition<Boolean>()
			{
				public Boolean apply(WebDriver d)
				{
					LOG.log(Level.INFO, String.format("Logo image is displayed? (logged on successfully) %s", driver.findElement(By.id("logoImage")).isDisplayed()));
					return d.findElement(By.id("logoImage")).isDisplayed();
				}
			});

			//Close the browser
			driver.close();  // Does NOT work for Firefox, does nothing.
			//driver.quit();  // In Firefox says it crashed.

		}
	}
}
