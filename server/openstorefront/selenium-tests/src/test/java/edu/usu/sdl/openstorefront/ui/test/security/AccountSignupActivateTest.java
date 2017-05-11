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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Note on this test we don't want to login
 *
 * @author dshurtleff
 */
public class AccountSignupActivateTest
        extends BrowserTestBase {

    private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

    @BeforeClass
    public static void setupTest() {
        login();
    }

    /**
     *
     */
    public AccountSignupActivateTest() {

    }

    @Test
    public void signupActivate() throws InterruptedException {
        deleteUserIfPresent();
        signupForm();
        activateAccount();
    }

    // Delete if active
    private void deleteUserIfPresent() throws InterruptedException {

        for (WebDriver driver : webDriverUtil.getDrivers()) {
            driver.get(webDriverUtil.getPage("AdminTool.action?load=User-Management"));

            // TODO:  Per STORE-1658, we need an ALL in the drop-down boxes.
            driver.findElement(By.xpath("//div[@id='filterActiveStatus-trigger-picker']")).click();
            driver.findElement(By.xpath("//li[contains(.,'Locked/Disabled')]")).click();
            sleep(1500);  // Need to explicity pause to let drop-down selection catch up

            driver.findElement(By.xpath("//div[@id='filterApprovalStatus-trigger-picker']")).click();
            driver.findElement(By.xpath("//li[contains(.,'Pending')]")).click();

            // Delete if present
            if (tableClickRowCol("tableview-1125", "Test1", driver)) {
                driver.findElement(By.xpath("//span[contains(.,'Delete')]")).click();
                driver.findElement(By.xpath("//span[@id='button-1037-btnInnerEl']")).click();  // Confirmation YES
                LOG.log(Level.INFO, "*** User DELETED ***");
            }
            sleep(1500);
        }
        /*
        // Navigate to the registration page
        for (WebDriver driver : webDriverUtil.getDrivers()) {
            driver.get(webDriverUtil.getPage("AdminTool.action?load=User-Management"));
		

			// Search for the account to delete
			// TODO:  Per STORE-1658, we need an ALL in the drop-down boxes.
            // Delete if present
            if (tableClickRowCol("tableview-1125", "Test1", driver)) {
                driver.findElement(By.xpath("//span[contains(.,'Delete')]")).click();
                driver.findElement(By.xpath("//span[@id='button-1037-btnInnerEl']")).click();  // Confirmation YES
                LOG.log(Level.INFO, "*** User autotest1 was DELETED! ***");
            }
        }*/
    }

    public void signupForm() {
        // Navigate to the registration page
        for (WebDriver driver : webDriverUtil.getDrivers()) {
            driver.get(webDriverUtil.getPage("registration.jsp"));
            sleep(2000);
            // Fill out the form
            LOG.log(Level.INFO, "********** Fill out the signupForm ************");
            driver.findElement(By.xpath("//input[@name='username']")).sendKeys("autotest1");
            driver.findElement(By.xpath("//input[@name='password']")).sendKeys("autoTest1!");
            driver.findElement(By.xpath("//input[@name='confirmPassword']")).sendKeys("autoTest1!");
            driver.findElement(By.xpath("//input[@name='firstName']")).sendKeys("auto");
            driver.findElement(By.xpath("//input[@name='lastName']")).sendKeys("Test1");
            driver.findElement(By.xpath("//input[@name='organization']")).sendKeys("Air Force");
            driver.findElement(By.xpath("//input[@name='email']")).sendKeys("blaine.esplin@sdl.usu.edu");
            driver.findElement(By.xpath("//input[@name='phone']")).sendKeys("435-555-5555");
            sleep(2000);

            // SUBMIT the form
            driver.findElement(By.xpath("//span[@id='button-1026-btnInnerEl']")).click();
            sleep(9000);
            login("admin", "Secret1!");
        }
    }

    private void activateAccount() {
        // Navigate to Admin Tools -> Application Management -> User Tools to activate
        LOG.log(Level.INFO, "********** Starting activateAccount in AccountsSignupActivateTest ************");
        for (WebDriver driver : webDriverUtil.getDrivers()) {
            driver.get(webDriverUtil.getPage("AdminTool.action?load=User-Management"));
            sleep(2500);

            // Now filter by Locked/Disabled and Pending and activate.
            // *** TODO: ***  Do a SEARCH instead!
            driver.findElement(By.xpath("//div[@id='filterActiveStatus-trigger-picker']")).click();
            driver.findElement(By.xpath("//li[contains(.,'Locked/Disabled')]")).click();
            driver.findElement(By.xpath("//div[@id='filterApprovalStatus-trigger-picker']")).click();
            driver.findElement(By.xpath("//li[contains(.,'Pending')]")).click();

            // Select and click Approve
            if (tableClickRowCol("tableview-1125", "Test1", driver)) {
                driver.findElement(By.xpath("//span[@id='button-1130-btnEl']")).click();
                sleep(3000);

                driver.findElement(By.xpath("//a[contains(.,'Approve')]")).click();
            }
        }
    }
}
