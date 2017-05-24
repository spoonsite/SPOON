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
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author dshurtleff
 */
public class BrowserTestBase {

    private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

    protected static WebDriverUtil webDriverUtil = new WebDriverUtil();

    /*@BeforeClass
	public static void setSize() throws Exception
	{
		webDriverUtil.get().manage().window.setSize(new Dimension(1080,800));
	}
     */
    @AfterClass
    public static void tearDown() throws Exception {
        //Bot.driver().quit();
        //WebDriverExtensionsContext.removeDriver();
        webDriverUtil.closeDrivers();
    }

    protected static void login() {
        login("admin", "Secret1@");
    }

    protected static void login(String userName, String passWord) {
        String uN = userName;
        String pW = passWord;

        for (WebDriver driver : webDriverUtil.getDrivers()) {
            // Make sure logged out before attempting login.
            driver.get(webDriverUtil.getPage("Login.action?Logout"));

            // Now log in
            driver.get(webDriverUtil.getPage("login.jsp"));
            WebElement element = driver.findElement(By.name("username"));
            element.sendKeys(uN);
            // Enter password and hit ENTER since submit does not seem to work.
            driver.findElement(By.name("password")).sendKeys(pW, Keys.ENTER);

            //confirm login
            //TODO: make sure it can handle different landing pages
            (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driverLocal) {
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

    protected static void logout() {
        for (WebDriver driver : webDriverUtil.getDrivers()) {
            driver.get(webDriverUtil.getPage("Login.action?Logout"));

            //TODO: confirm logout, return -1 or 0 or a boolean?
        }
    }

    // Making Tread.sleep "universal"
    protected void sleep(int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException ex) {
            Logger.getLogger(AccountSignupActivateTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Used to located item in table
     * @param cssSelector cssSelector used to find table
     * @param searchFor text in the cell to find
     * @param driver Selenium webdriver
     * @return true if cell is found, false otherwise
     * @throws InterruptedException 
     */
    public boolean tableClickRowCol(String cssSelector, String searchFor, WebDriver driver) throws InterruptedException {
        int fRow = -1;
        int fColumn = -1;

        WebDriverWait waitForTable = new WebDriverWait(driver, 20);
        List<WebElement> allRows = waitForTable.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.cssSelector(cssSelector), By.tagName("tr")));
        
        // Iterate through rows
        int theRow = 0;
        for (WebElement row : allRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            theRow++;

            // Iterate through cells
            int theColumn = 0;
            for (WebElement cell : cells) {
                //tableText[theRow][theColumn] = cell.getText();
                //System.out.println("Row = " + theRow + " Cell = " + theColumn + " TEXT = " + tableText[theRow][theColumn]);

                // If text found remember row, column
                if (searchFor.toLowerCase().equals(cell.getText().toLowerCase())) {
                    fRow = theRow;
                    fColumn = theColumn;
                    LOG.log(Level.INFO, "--- Clicking on the table at: ROW " + fRow + ", COLUMN " + fColumn + ". ---");
                    cell.click();
                    return true;
                    // System.out.println("TEXT '" + localSearch + "' WAS FOUND AT: " + fRow + ", " + fColumn);
                }
                
                theColumn++;
            }
        }

        LOG.log(Level.WARNING, "*** The text '" + searchFor + "' was NOT FOUND in table " + cssSelector + ", with current filters set. ***");
        return false;
        //return new TableItem(fRow, fColumn);
    }

    /**
     * This assumes the driver is on the correct page before this is called This
     * stores the image in the configure report directory.
     *
     * @param driver
     */
    protected static void takeScreenShot(WebDriver driver) {
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
