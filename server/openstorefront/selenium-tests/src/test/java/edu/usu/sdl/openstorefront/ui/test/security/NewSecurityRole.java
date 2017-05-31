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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author besplin
 */
public class NewSecurityRole    
		extends SecurityTestBase {

    private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

    @BeforeClass
    public static void setupTest() {
        login();
    }

    public NewSecurityRole() {
		
    }

    // Delete if active
    public void deleteRoleIfPresent(WebDriver driver, String roleName) throws InterruptedException {
		driver.get(webDriverUtil.getPage("AdminTool.action?load=Security-Roles"));
		//driver.navigate().refresh();
		sleep(1500);
		// Click on Table Row Col containing roleName
		if (tableClickRowCol("[data-test='securityRolesTable'] .x-grid-view", roleName, driver)) {
			driver.findElement(By.xpath("//span[contains(.,'Delete')]")).click();
			sleep(750); 
			driver.findElement(By.xpath("//span[contains(.,'Confirm')]")).click();
			sleep(500);
			
			// *** FIXES detached from page error message ***  TODO: Put in a seperate method if used again?
			boolean breakIt = true;
				while (true) {
				breakIt = true;
				try {
					sleep(500);
					// Command that was erring out (detached) earlier.
					tableClickRowCol("[data-test='securityRolesTable'] .x-grid-view", roleName, driver);
					LOG.log(Level.INFO, "--- Waiting for element to show up so that it is not detached ---");
				} catch (Exception e) {
					if (e.getMessage().contains("element is not attached")) {
						breakIt = false;
					}
				}
				if (breakIt) {
					LOG.log(Level.INFO, "--- Successfully waited for detached element to show up, continuing on now --");
					break;
				}
		    }
				
				
			// Check to ensure deletion
			if (tableClickRowCol("[data-test='securityRolesTable'] .x-grid-view", roleName, driver)) {
				LOG.log(Level.WARNING, "*** Could NOT delete  '" + roleName + "' ***");
			}
			else {
				LOG.log(Level.INFO, "--- Old Security Role '" + roleName + "' DELETED ---");
				sleep(1000);
			}
		}
		else {
			LOG.log(Level.INFO, "--- Old (Deleted) Security Role " + roleName + " was not found. ---");
		}
	
	}

	public void addRoleBasic(WebDriver driver, String roleName) throws InterruptedException {
		driver.get(webDriverUtil.getPage("AdminTool.action?load=Security-Roles"));
		//driver.navigate().refresh();
		sleep(1500);		
		driver.findElement(By.xpath("//span[contains(.,'Add')]")).click();
		sleep(1250); // Give box time to come up
		driver.findElement(By.xpath("//input[contains(@name,'roleName')]")).sendKeys(roleName);
		driver.findElement(By.xpath("//input[@name='description']")).sendKeys("Created by automation");
		driver.findElement(By.xpath("//input[@name='landingPage']")).sendKeys("/");
		sleep(500);
		driver.findElement(By.xpath("//span[contains(.,'Save')]")).click();
		sleep(2000);
		if (tableClickRowCol("[data-test='securityRolesTable'] .x-grid-view", roleName, driver)) {
			LOG.log(Level.INFO, "--- Added the role " + roleName + ". ---");
		}else {
			LOG.log(Level.WARNING, "*** Could NOT ADD the role " + roleName + ". ***");
		}
	}
	
	public void addUserToRole(WebDriver driver, String roleName, String userName) throws InterruptedException {
		driver.get(webDriverUtil.getPage("AdminTool.action?load=Security-Roles"));
		//driver.navigate().refresh();
		sleep(1500);
		if (tableClickRowCol("[data-test='securityRolesTable'] .x-grid-view", roleName, driver)) {
			driver.findElement(By.xpath("//span[contains(.,'Manage Users')]")).click();
			sleep(250); //Users with xxxx role is now up
			
			// Start typing in the Add User drop down box, then hit ENTER
			driver.findElement(By.xpath("//input[contains(@name,'username')]")).sendKeys(userName);
			sleep(1000);
			driver.findElement(By.xpath("//input[contains(@name,'username')]")).sendKeys(Keys.ENTER);
			sleep(1250);
			
			// Hit add button (when active?)
			driver.findElement(By.xpath("//span[@class='x-btn-button x-btn-button-default-toolbar-small x-btn-text  x-btn-icon x-btn-icon-left x-btn-button-center ']")).click();
			sleep(1250);
			
			// Verify it is in the list of added usernames
			boolean wasAdded = driver.findElement(By.xpath("//div[contains(.,'" + userName.toLowerCase() +"')]")).isDisplayed();
			if (wasAdded) {
				LOG.log(Level.INFO, "--- Successfully added " + userName.toLowerCase() + " to " + roleName +" role. ---");
			}
			else { 
				LOG.log(Level.SEVERE, "!!!!! Could not add the user " + userName.toLowerCase() + " to the role " + roleName + " !!!!!");
			}
		}
		else {
			LOG.log(Level.SEVERE, "!!!!! Could not find the role " + roleName + " in order to add a managed user. !!!!!");
		}
	}
	
	
	public void managePermissions(WebDriver driver, String roleName, Map<String, Boolean> dataSource) throws InterruptedException {
		driver.get(webDriverUtil.getPage("AdminTool.action?load=Security-Roles"));
		sleep(1500);
	
		// TODO:  Write here
	}
	
	
	// True in boolean means to add it to the accessible side, false means to remove it.
	public void manageDataSources(WebDriver driver, String roleName, Map<String, Boolean> dataSource) throws InterruptedException {
		driver.get(webDriverUtil.getPage("AdminTool.action?load=Security-Roles"));
		sleep(1500);
		
		// Print HashMap 'dataSource' that was passed in from SecurityRolesTest.java
		for (String key : dataSource.keySet()) {
				System.out.println("dataSource = " + key + " Active? " + dataSource.get(key));
		    }
	
		if (tableClickRowCol("[data-test='securityRolesTable'] .x-grid-view", roleName, driver)) {
			driver.findElement(By.xpath("//span[contains(.,'Manage Data Restrictions')]")).click();
			
			// Wait for Data Restrictions box to come up
			WebDriverWait waitBox = new WebDriverWait(driver, 20);
			waitBox.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-window.x-layer")));

			// Ensure you're on the Data Sources tab (should be there by default)
			List <WebElement> dataTabs = driver.findElements(By.cssSelector(".x-tab.x-unselectable.x-box-item.x-tab-default.x-top.x-tab-top.x-tab-default-top"));
			for (WebElement tab: dataTabs) {
				if (tab.getText().equals("Data Sources")) {
					tab.click();
				}
			}

			// if true move left to right if in left restricted table
			for (String key : dataSource.keySet()) {
				if (dataSource.get(key)) { // should be in the RIGHT Accessible table
					//System.out.println("LOOKING FOR   ---   " + key);
					if (tableClickRowCol("dataSourcesGrid-body", key, driver)) { // is in Restricted table (LEFT) currently
						sleep(500);
						System.out.println("dataSlourcesGrid-body (RESTRICTED on the LEFT contains " + key + " as " + dataSource.get(key)  );
						
						// ********** DRAG move here **********
					}
				}
			}
		}else {
			LOG.log(Level.WARNING, "*** Could not find the Role of " + roleName + " to set the dataSources ***  !!! FUTURE tests using these settings will FAIL !!!");
		} 
	
	}
	
	// Feed in a <List> of Data Distributions to Activate
	public void manageDataSensitivity(WebDriver driver, String roleName, Map<String, Boolean> dataSens) throws InterruptedException {
		driver.get(webDriverUtil.getPage("AdminTool.action?load=Security-Roles"));
		sleep(1500);
		
		if (tableClickRowCol("[data-test='securityRolesTable'] .x-grid-view", roleName, driver)) {
			driver.findElement(By.xpath("//span[contains(.,'Manage Data Restrictions')]")).click();

			// Wait for Data Restrictions box to come up
			WebDriverWait waitBox = new WebDriverWait(driver, 20);
			waitBox.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".x-window.x-layer")));
		    
			// Click on Data Sensitvity tab
			List <WebElement> dataTabs = driver.findElements(By.cssSelector(".x-tab.x-unselectable.x-box-item.x-tab-default.x-top.x-tab-top.x-tab-default-top"));
			for (WebElement tab: dataTabs) {
				if (tab.getText().equals("Data Sensitivity")) {
					tab.click();
				}
			}

			// Retreive dataSource desired settings
//			for (String key : dataSens.keySet()) {
//				if (dataSens.get(key)) {
//					System.out.println(key + " +++ This is 'true' move to RIGHT (Accessible) if it is NOT already there.");
												
			// Restricted:  x-grid-view x-grid-with-col-lines x-grid-with-row-lines x-fit-item x-grid-view-default x-unselectable x-scroll	
			// Accessible:  x-grid-view x-grid-with-col-lines x-grid-with-row-lines x-fit-item x-grid-view-default x-unselectable x-scroller
			
			List <WebElement> restrictedList = driver.findElements(By.cssSelector(".x-grid-view.x-grid-with-col-lines.x-grid-with-row-lines.x-fit-item.x-grid-view-default.x-unselectable.x-scroll"));
			List <WebElement> AccessibleList = driver.findElements(By.cssSelector(".x-grid-view.x-grid-with-col-lines.x-grid-with-row-lines.x-fit-item.x-grid-view-default.x-unselectable.x-scroller"));

			// Loop through restrictedList and move to AccesibleList if key is true (dataSens)
			for (WebElement item: restrictedList) {
				// if needs to be in accesible
				System.out.println(item.getText());
				// ********** MOVE TO THE RIGHT HERE *****************	
				(new Actions (driver)).dragAndDrop(restrictedList, AccessibleList).perform();
			}
			


//					}
//					else {
//						System.out.println(key + " --- this is 'false' move to the LEFT (Restricted) if it is NOT already there.");
						// MOVE TO THE LEFT HERE
//					}
				}
			}
		}
	}
}