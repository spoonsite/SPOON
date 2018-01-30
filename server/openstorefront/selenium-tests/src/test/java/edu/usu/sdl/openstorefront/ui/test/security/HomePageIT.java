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

import edu.usu.sdl.openstorefront.selenium.provider.AuthenticationProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ClientApiProvider;
import edu.usu.sdl.openstorefront.selenium.provider.NotificationEventProvider;
import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePageIT
		extends BrowserTestBase
{

	private final Logger LOG = Logger.getLogger(HomePageIT.class.getName());
	private AuthenticationProvider authProvider;
	private NotificationEventProvider notificationProvider;
	private ClientApiProvider provider;

	@Before
	public void setup() throws InterruptedException
	{
		provider = new ClientApiProvider();
		authProvider = new AuthenticationProvider(properties, webDriverUtil);
		authProvider.login();
		notificationProvider = new NotificationEventProvider(provider.getAPIClient());
	}

	@Test
	// Output build version at bottom of the page
	public void versionUnderTest()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			webDriverUtil.getPage(driver, "index.jsp");
			String vers = driver.findElement(By.xpath("//div[@class='home-footer-version']")).getText();
			//TODO:  Replace with Logger Out
			LOG.log(Level.INFO, "*******   VERSION UNDER TEST:  {0}   *******", vers);
		}
	}

	@Test
	// Help File test
	public void helpMenu()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			driver.findElement(By.xpath("//span[@class='x-btn-wrap x-btn-wrap-default-large x-btn-arrow x-btn-arrow-right']")).click();
			driver.findElement(By.xpath("//b[contains(.,'Help')]")).click();
		}
	}

	// Logout
	public void logoutTest()
	{
		logout();
		// ensure signin screen is shown.
	}

	@After
	public void cleanupTest()
	{
		notificationProvider.cleanup();
	}

}
