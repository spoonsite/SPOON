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
package edu.usu.sdl.openstorefront.ui.test;

import edu.usu.sdl.openstorefront.selenium.util.DriverWork;
import edu.usu.sdl.openstorefront.selenium.util.PropertiesUtil;
import edu.usu.sdl.openstorefront.selenium.util.WebDriverUtil;
import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author dshurtleff
 */
public class BrowserTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());
	
	// WebDriverUtil and Properties objects must stay in BrowserTestBase because there can only be one instance that all tests share
	protected static WebDriverUtil webDriverUtil;
	protected static Properties properties;

	@BeforeClass
	public static void setupBrowserTestBase()
	{
		properties = PropertiesUtil.getProperties();
		webDriverUtil = new WebDriverUtil(properties);
	}

	@AfterClass
	public static void cleanup() throws Exception
	{
		LOG.log(Level.INFO, "Starting cleanup");
		webDriverUtil.closeDrivers();
	}

	// Making Thread.sleep "universal"
	protected static void sleep(int mills)
	{
		try {
			Thread.sleep(mills);
		} catch (InterruptedException ex) {
			Logger.getLogger(BrowserTestBase.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	protected void driverWait(DriverWork work, long maxMilliSeconds)
	{
		boolean done = false;
		long startTime = System.currentTimeMillis();

		while (!done && (System.currentTimeMillis() - startTime) < maxMilliSeconds) {
			try {
				work.performWork();
				done = true;
			} catch (WebDriverException ex) {
				sleep(500);
				LOG.log(Level.WARNING, "{0} Retrying...", ex.getMessage());
			}
		}

		if (!done) {
			throw new WebDriverException("Browser failure");
		}
	}

	/**
	 * Used to located item in table
	 *
	 * @param cssSelector cssSelector used to find table
	 * @param searchFor text in the cell to find
	 * @param driver Selenium webdriver
	 * @param columnIndex index of the column being searched
	 * @return true if cell is found, false otherwise
	 * @throws InterruptedException
	 */
	public boolean tableClickRowCol(String cssSelector, String searchFor, WebDriver driver, int columnIndex) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 3);

		try {
			List<WebElement> allRows;
			driverWait(() -> {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
			}, 5000);

			allRows = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.cssSelector(cssSelector), By.tagName("tr")));

			int theRow = 0;
			for (WebElement row : allRows) {

				List<WebElement> cells;
				try {
					cells = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(row, By.tagName("td")));
					theRow++;

					WebElement cell = cells.get(columnIndex);
					
					if (cell.getText().equals(searchFor)) {
						Actions builder = new Actions(driver);
						builder.moveToElement(row).perform();
						sleep(100);
						builder.click().perform();
						return true;
					}
				} catch (Exception e) {

				}
			}

		} catch (Exception e) {
			LOG.log(Level.WARNING, "*** The text ''{0}'' was NOT FOUND in table {1}, with current filters set. ***", new Object[]{searchFor, cssSelector});
			System.out.println(e);
		}

		return false;
	}

	/**
	 * This assumes the driver is on the correct page before this is called This
	 * stores the image in the configure report directory.
	 *
	 * @param driver
	 */
	protected static void takeScreenShot(WebDriver driver)
	{
		if (driver instanceof TakesScreenshot) {
			TakesScreenshot screenshot = (TakesScreenshot) driver;
			File scrFile = screenshot.getScreenshotAs(OutputType.FILE);

			//TODO: save
			//webDriverUtil.saveReportArtifact(in);
		} else {
			LOG.log(Level.WARNING, "*** Unable to create Screenshot; no driver support for {0} ***", driver.getClass().getName());
		}
	}
}
