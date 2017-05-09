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

import edu.usu.sdl.openstorefront.selenium.util.WebDriverUtil;
import edu.usu.sdl.openstorefront.ui.test.security.AccountSignupActivateTest;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author dshurtleff
 */
public class BrowserTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

	protected static WebDriverUtil webDriverUtil = new WebDriverUtil();

	/*@BeforeClass
	public static void setSize() throws Exception
	{
		webDriverUtil.get().manage().window.setSize(new Dimension(1080,800));
	}
	*/
 
	@AfterClass
	public static void tearDown() throws Exception
	{
		//Bot.driver().quit();
		//WebDriverExtensionsContext.removeDriver();
		webDriverUtil.closeDrivers();
	}

	protected static void login(){
		login("admin","Secret1@");
	}
	
	protected static void login(String userName, String passWord)
	{
		String uN = userName;
		String pW = passWord;
		
	    LOG.log(Level.INFO,"Starting the AccountsSignupActivateTest");
            for (WebDriver driver : webDriverUtil.getDrivers()) {
		    driver.manage().window().setSize(new Dimension(1200,1024));	   
			driver.get(webDriverUtil.getPage("login.jsp"));

			WebElement element = driver.findElement(By.name("username"));
			element.sendKeys(uN);
			// Enter password and hit ENTER since submit does not seem to work.
			driver.findElement(By.name("password")).sendKeys(pW, Keys.ENTER);

			//confirm login
			//TODO: make sure it can handle different landing pages
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

	protected static void logout()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			driver.get(webDriverUtil.getPage("Login.action?Logout"));

			WebElement element = driver.findElement(By.name("username"));
			element.sendKeys("admin");
			// Enter password and hit ENTER since submit does not seem to work.
			driver.findElement(By.name("password")).sendKeys("Secret1@", Keys.ENTER);

			//TODO: confirm logout
		}
	}

	// pass in table name, return Row, Column
	public boolean tableClickRowCol(String tableName, String searchFor)
	{
		int fRow =-1; int fColumn = -1;
		String localTable = tableName;
		String localSearch = searchFor;
		boolean theBool = false;
		// get the tableText[theRow][theColumn] from table theTable
		for (WebDriver driver : webDriverUtil.getDrivers()) { 
			driver.manage().window().setSize(new Dimension(1300,1150));

			WebElement table = driver.findElement(By.id(localTable)); 
			List<WebElement> allRows = table.findElements(By.tagName("tr")); 
			//String[][] tableText = new String[256][256];

			// Iterate through rows
			int theRow = 0;
			for (WebElement row : allRows) { 
				List<WebElement> cells = row.findElements(By.tagName("td")); 
				theRow ++;

				// Iterate through cells
				int theColumn = 0;
				for (WebElement cell : cells) { 
					//tableText[theRow][theColumn] = cell.getText();
					//System.out.println("Row = " + theRow + " Cell = " + theColumn + " TEXT = " + tableText[theRow][theColumn]);
					
					// If text found remember row, column
					if (localSearch.trim().toLowerCase().equals(cell.getText().toLowerCase())) {
						fRow = theRow; fColumn = theColumn;
						// System.out.println("TEXT '" + localSearch + "' WAS FOUND AT: " + fRow + ", " + fColumn);
						break;
					}
					
					// NOTE:  Finds the FIRST instance, if > 1 instance found
					if (fRow !=-1 || fColumn !=-1) break;
					theColumn ++;
				}
			}
				
		   // Now CLICK on the table! 
		   if (fRow !=-1 || fColumn !=-1) {
			    LOG.log(Level.INFO,"*** Clicking on the table at: ROW " + fRow + ", COLUMN " + fColumn + ". ***");
				fColumn ++; // increment by 1 as it is 0-based and you can't click on td 0th instance.
				driver.findElement(By.xpath("//tr[" + fRow + "]//td[" + fColumn + "]")).click();
				theBool = true;
		   }
		   else {
				LOG.log(Level.INFO,"*** The text '" + localSearch + "' was NOT FOUND in table " + localTable + ". ***");
				theBool = false;
		   } 
		   
	
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			Logger.getLogger(AccountSignupActivateTest.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return theBool;
		//return new TableItem(fRow, fColumn);
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
			LOG.log(Level.WARNING, "Unable to create Screenshot; no driver support for {0}", driver.getClass().getName());
		}
	}
}


