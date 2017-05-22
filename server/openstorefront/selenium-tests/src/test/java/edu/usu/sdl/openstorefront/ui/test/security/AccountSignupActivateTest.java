
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
		signupActivate("autoTestDEFALT");
	}
	
    public void signupActivate(String userName) throws InterruptedException {
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			deleteUserIfPresent(driver, userName);
			signupForm(driver, userName);
			activateAccount(driver, userName);
		}
    }

    // Delete if active
    private void deleteUserIfPresent(WebDriver driver, String userName) throws InterruptedException {
		driver.get(webDriverUtil.getPage("AdminTool.action?load=User-Management"));
		// TODO:  Per STORE-1658, we need an ALL in the drop-down boxes.
		for (int loop=0; loop<=3; loop ++) {
			// First (loop == 0) filter by Active and Approved, the default on page load
			
			// Filter on Active Status (Locked/Disabled; Active)
			if ((loop == 1) || (loop == 2)) {
				driver.findElement(By.xpath("//div[@id='filterActiveStatus-trigger-picker']")).click();
				driver.findElement(By.xpath("//li[contains(.,'Locked/Disabled')]")).click();
				sleep(1000);  // Need to explicity pause to let drop-down selection catch up
			}
	 		if (loop == 3) {
				driver.findElement(By.xpath("//div[@id='filterActiveStatus-trigger-picker']")).click();
				driver.findElement(By.xpath("//li[contains(.,'Active')]")).click();
				sleep(1000);  
			}	

			// Filter on Approval Satus (Approved; Pending)
			if ((loop == 1) || (loop == 3)) {
				// Filter by Active and Pending
				driver.findElement(By.xpath("//div[@id='filterApprovalStatus-trigger-picker']")).click();
				driver.findElement(By.xpath("//li[contains(.,'Pending')]")).click();
				sleep (500);
			}				
			if (loop == 2){
				driver.findElement(By.xpath("//div[@id='filterApprovalStatus-trigger-picker']")).click();
				driver.findElement(By.xpath("//li[contains(.,'Approved')]")).click();
				sleep (500);
			}

			// Delete if present
			if (tableClickRowCol("[data-test='userManagementTable'] .x-grid-view", userName, driver)) {
		//	if (tableClickRowCol("tableview-1125", userName, driver)) {
				driver.findElement(By.xpath("//span[contains(.,'Delete')]")).click();
				driver.findElement(By.xpath("//span[@id='button-1037-btnInnerEl']")).click();  // Confirmation YES
				LOG.log(Level.INFO, "*** EXISTING User '" + userName + "' DELETED ***");
				loop = 99;  // exit
			}
			sleep(1500);
			}
	    }

    public void signupForm(WebDriver driver, String userName) {
        // Navigate to the registration page
		driver.get(webDriverUtil.getPage("registration.jsp"));
		sleep(2000);
		// Fill out the form
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(userName);
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(userName + "A1!");
		driver.findElement(By.xpath("//input[@name='confirmPassword']")).sendKeys(userName + "A1!");
		driver.findElement(By.xpath("//input[@name='firstName']")).sendKeys(userName);
		driver.findElement(By.xpath("//input[@name='lastName']")).sendKeys(userName);
		driver.findElement(By.xpath("//input[@name='organization']")).sendKeys("Air Force");
		driver.findElement(By.xpath("//input[@name='email']")).sendKeys("blaine.esplin@sdl.usu.edu");
		driver.findElement(By.xpath("//input[@name='phone']")).sendKeys("435-555-5555");
		sleep(500);

		// SUBMIT the form
		driver.findElement(By.xpath("//span[@id='button-1026-btnInnerEl']")).click();
		LOG.log(Level.INFO, "--- User '" + userName + "' CREATED ---");
		sleep(4000);

	}


    private void activateAccount(WebDriver driver, String userName) throws InterruptedException {
        // Navigate to Admin Tools -> Application Management -> User Tools to activate

		driver.get(webDriverUtil.getPage("AdminTool.action?load=User-Management"));
		sleep(2500);

		// Don't care about searching other filters as this was just created, need to go here
		driver.findElement(By.xpath("//div[@id='filterActiveStatus-trigger-picker']")).click();
		driver.findElement(By.xpath("//li[contains(.,'Locked/Disabled')]")).click();
		sleep(2500);
		driver.findElement(By.xpath("//div[@id='filterApprovalStatus-trigger-picker']")).click();
		driver.findElement(By.xpath("//li[contains(.,'Pending')]")).click();
		sleep(1500);
		
		// Select and click Approve
		if (tableClickRowCol("tableview-1125", userName, driver)) {
			driver.findElement(By.xpath("//a[contains(.,'Approve')]")).click();
			LOG.log(Level.INFO, "--- User '" + userName + "' APPROVED ---");
			sleep(3000);
		}
		
		// Login as newly created and approved user
		sleep(500);
		login(userName, userName + "A1!");
		sleep(1500);
		LOG.log(Level.INFO, "--- Logged in as new user '" + userName + "' ---");
		login(); //logout and log back in as admin
				
	}  
}
