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

import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import edu.usu.sdl.openstorefront.ui.test.security.AccountSignupActivateTest;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author ccummings
 */
public class AdminHighlightTest extends AdminTestBase {

    private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

    @BeforeClass
    public static void setupTest() {
        login();
    }

    @Test
    public void adminHighlightTest() {

        for (WebDriver driver : webDriverUtil.getDrivers()) {

            createHighlight(driver);
        }
    }

    public void createHighlight(WebDriver driver) {

        driver.get(webDriverUtil.getPage("AdminTool.action?load=Highlights"));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(AccountSignupActivateTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Click add button and fill out form
        driver.findElement(By.xpath("//*[@id='highlightGrid-tools-add']")).click();

        driver.findElement(By.xpath("//*[@id='highlightEntryForm-Title-inputEl']")).sendKeys("TestHighlight1");
        driver.findElement(By.cssSelector("#tinymce > p")).sendKeys("TestHighlight1 Description");
        driver.findElement(By.xpath("//*[@id=\'highlightEntryForm-Type-trigger-picker\']")).click();
        driver.findElement(By.xpath("//li[contains(.,'Component')]"));

        driver.findElement(By.xpath("//*[@id='button-1099']")).click();
    }
}
