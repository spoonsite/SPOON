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

import java.util.List;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.FindElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Tests for Admin Attributes Tool
 *
 * @author ccummings
 */
public class AdminAttributesTest
        extends AdminTestBase {

    @Test
    public void adminAttributesTest() {

        for (WebDriver driver : webDriverUtil.getDrivers()) {

            setup(driver);
            createAttribute(driver);
        }
    }

    public void setup(WebDriver driver) {

        driver.get(webDriverUtil.getPage("AdminTool.action?load=Attributes"));

        //attributeGrid_header-title-textEl
        //text = Manage Attributes
        (new WebDriverWait(driver, 10)).until((ExpectedCondition<Boolean>) (WebDriver driverLocal) -> {
            List<WebElement> titleElements = driverLocal.findElements(By.id("attributeGrid_header-title-textEl"));
            if (titleElements.size() > 0) {
                return titleElements.get(0).isDisplayed();
            } else {
                return false;
            }
        });
    }

    public void createAttribute(WebDriver driver) {

        driver.findElement(By.xpath("//*[@id='attributeGrid-tools-add']")).click();

        driver.findElement(By.xpath("//*[@id='editAttributeForm-label-inputEl']")).sendKeys("MyNewAttribute1");
        driver.findElement(By.xpath("//*[@id='editAttributeForm-code-inputEl']")).sendKeys("MyNewAttributeCode1");
        
        // radio buttons
        boolean bValue = false;
        
        WebElement allowAllEntriesRadioBtn = driver.findElement(By.xpath("//*[@id='allEntryTypes-inputEl']"));
        bValue = allowAllEntriesRadioBtn.isSelected();
        if (!bValue) {
            allowAllEntriesRadioBtn.click();
        }
        WebElement requiredRadioBtn = driver.findElement(By.xpath("//*[@id='requiredFlagCheckBox-inputEl']"));
        bValue = requiredRadioBtn.isSelected();
        if (!bValue) {
            requiredRadioBtn.click();
        }
        driver.findElement(By.xpath("//*[@id='tool-1273-toolEl']")).click();
        sleep(1000);
        
        WebElement element = driver.findElement(By.xpath("//*[@id='tableview-1278']"));
        System.out.println("Ok here");
        WebElement element1 = element.findElement(By.xpath("//*[@id='tableview-1278']/div[2]"));
        System.out.println("Do I get here");
        sleep(1000);
        WebElement table = driver.findElement(By.cssSelector("#ext-element-31"));
        System.out.println("My element: " + table);
        sleep(2000);

    }
}
