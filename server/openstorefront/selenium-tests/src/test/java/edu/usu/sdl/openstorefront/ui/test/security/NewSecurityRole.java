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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author besplin
 */
public class NewSecurityRole    
		extends BrowserTestBase {

    private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

    @BeforeClass
    public static void setupTest() {
        login();
    }

    /**
     *
     */
    public NewSecurityRole() {

    }

	@Test
    public void addRole() throws InterruptedException {
		addRole("AUTO-someRole", "autoTestDEFALT");
	}
	   
    public void addRole(String roleName, String userName) throws InterruptedException {
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			deleteRoleIfPresent(driver, roleName);
			addRoleBasic(driver, roleName);
			manageUsers(driver, roleName, userName);
			managePermissions(driver, roleName);
			manageDataSources(driver, roleName, true, true);
			manageDataSensitivity(driver, roleName);
		}
    }

    // Delete if active
    private void deleteRoleIfPresent(WebDriver driver, String roleName) throws InterruptedException {
		driver.get(webDriverUtil.getPage("AdminTool.action?load=Security-Roles"));
		// Click on Table Row Col containing roleName
		if (tableClickRowCol("tableview-1092", roleName, driver)) {
			driver.findElement(By.xpath("//span[contains(.,'Delete')]")).click();
			driver.findElement(By.xpath("//span[contains(.,'Confirm')]")).click();
			LOG.log(Level.INFO, "*** Old Security Role '" + roleName + "' DELETED ***");
		}
		else {
			LOG.log(Level.INFO, "*** Security Role " + roleName + " was not found. ***");
		}
	}
	
	private void addRoleBasic(WebDriver driver, String roleName) throws InterruptedException {
		driver.get(webDriverUtil.getPage("AdminTool.action?load=Security-Roles"));
		driver.findElement(By.xpath("//span[contains(.,'Add')]")).click();
		sleep(750); // Give box time to come up
		driver.findElement(By.xpath("//input[contains(@name,'roleName')]")).sendKeys(roleName);
		driver.findElement(By.xpath("//input[@name='description']")).sendKeys("Created by automation");
		driver.findElement(By.xpath("//input[@name='landingPage']")).sendKeys("/");
		sleep(500);
		driver.findElement(By.xpath("//span[contains(.,'Save')]")).click();
		sleep(2000);
		LOG.log(Level.INFO, "*** Added the role " + roleName + ". ***");
	}
	
	private void manageUsers(WebDriver driver, String roleName, String userName) throws InterruptedException {
		driver.get(webDriverUtil.getPage("AdminTool.action?load=Security-Roles"));
		if (tableClickRowCol("tableview-1092", roleName, driver)) {
			driver.findElement(By.xpath("//span[contains(.,'Manage Users')]")).click();
			sleep(250); //Users with xxxx role is now up
			driver.findElement(By.xpath("//input[contains(@name,'username')]")).sendKeys(userName);
			sleep(1250);
			driver.findElement(By.xpath("//input[contains(@name,'username')]")).sendKeys(Keys.ENTER);
			sleep(1600);
			driver.findElement(By.xpath("//span[@class='x-btn-button x-btn-button-default-toolbar-small x-btn-text  x-btn-icon x-btn-icon-left x-btn-button-center ']")).click();
			sleep(1500);
			// Verify it is in the list below
			boolean wasAdded = driver.findElement(By.xpath("//div[contains(.,'" + userName +"')]")).isDisplayed();
			if (wasAdded) {
				LOG.log(Level.INFO, "*** Successfully added " + userName + " to " + roleName +" role. ***");
			}
			else { 
				LOG.log(Level.SEVERE, "!!!!! Could not add the user " + userName + " to the role " + roleName + " !!!!!");
			}
		}
		else {
			LOG.log(Level.SEVERE, "!!!!! Could not find the role " + roleName + " in order to add a managed user. !!!!!");
		}
	}
	
	
	// Feed in a <List> of permissions to activate
	private void managePermissions(WebDriver driver, String roleName) throws InterruptedException {
		driver.get(webDriverUtil.getPage("AdminTool.action?load=Security-Roles"));
		
	}
	
	// True in boolean means to add it to the accessible side, false means to remove it.
	private void manageDataSources(WebDriver driver, String roleName, boolean DI2E, boolean ER2) throws InterruptedException {
		driver.get(webDriverUtil.getPage("AdminTool.action?load=Security-Roles"));
		
	}
	
	// Feed in a <List> of Data Distributions to Activate
	private void manageDataSensitivity(WebDriver driver, String roleName) throws InterruptedException {
		driver.get(webDriverUtil.getPage("AdminTool.action?load=Security-Roles"));
		
	}
}
