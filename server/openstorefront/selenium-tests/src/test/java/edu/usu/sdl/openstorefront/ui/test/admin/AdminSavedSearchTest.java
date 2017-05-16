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
package edu.usu.sdl.openstorefront.ui.test.admin;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.FindElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author ccummings
 */
public class AdminSavedSearchTest extends AdminTestBase {
    
    @Test
    public void adminSavedSearchTest() {
        
        for (WebDriver driver : webDriverUtil.getDrivers()) {
            
            setup(driver);
            createSavedSearch(driver);
            deleteSavedSearch(driver);
        }
        
    }
    
    public void setup(WebDriver driver) {
        
    }
    
    public void createSavedSearch(WebDriver driver) {
        
    }
    
    public void deleteSavedSearch(WebDriver driver) {
        
    }

}
