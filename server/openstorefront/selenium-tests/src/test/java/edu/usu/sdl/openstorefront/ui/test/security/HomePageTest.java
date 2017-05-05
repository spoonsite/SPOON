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

public class HomePageTest
		extends BrowserTestBase
{
    private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());
    @BeforeClass
    public static void setupTest(){
        
	login();
    }
    
    /**
     *
     */
    public HomePageTest(){

    }

    @Test
    // Output build version at bottom of the page
    public void versionUnderTest(){
        for (WebDriver driver : webDriverUtil.getDrivers()) { 
              driver.get(webDriverUtil.getPage("index.jsp"));
              String vers = driver.findElement(By.xpath("//div[@class='home-footer-version']")).getText();
              //TODO:  Replace with Logger Out
              LOG.log(Level.INFO, "*******   VERSION UNDER TEST:  {0}   *******", vers);

        }
    }
    
    @Test
    // Help File test
    public void helpMenu(){
        for (WebDriver driver : webDriverUtil.getDrivers()) { 
            driver.findElement(By.xpath("//span[@class='x-btn-wrap x-btn-wrap-default-large x-btn-arrow x-btn-arrow-right']")).click();
            driver.findElement(By.xpath("//b[contains(.,'Help')]")).click();
            // Verify Help Window, switch the focus?
            
            // Expand and collapse some menu items
        }
    }

    // Print and search on Help window
    public void printSearchHelp(){
        
    }

    // Feedback/ issues
    public void feedbackIssues(){
        
    }

    // Home page links, check for bad links (Highlights, Recents, footer, etc.)
    public void homepageLinkCheck(){
        
    }
    
    // Logout
    public void logoutTest(){
        logout();
        // ensure signin screen is shown.
    }
            
}
