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
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

    private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

    @Test
    public void adminAttributesTest() throws InterruptedException {

        for (WebDriver driver : webDriverUtil.getDrivers()) {

            setup(driver);
            deleteAttribute(driver, "AAA MyTestAttribute17");
            createAttribute(driver, "AAA MyTestAttribute17", "MYTESTATTR17");
            attributeManageCodes(driver, "AAA MyTestAttribute17");
            editManageCodes(driver, "MyTestCodeLabel11");
            toggleStatusManageCodes(driver, "MyTestCodeLabel11");
            deleteAttribute(driver, "AAA MyTestAttribute17");
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

    public void createAttribute(WebDriver driver, String attrName, String attrCode) {

        sleep(200);
        driver.findElement(By.xpath("//*[@id='button-1095']")).click();
        sleep(500);
        driver.findElement(By.xpath("//*[@id='attributeGrid-tools-add']")).click();
        sleep(500);
        driver.findElement(By.xpath("//*[@id='editAttributeForm-label-inputEl']")).sendKeys(attrName);
        driver.findElement(By.xpath("//*[@id='editAttributeForm-code-inputEl']")).sendKeys(attrCode);

        // radio buttons
        boolean bValue = false;
        sleep(500);
        WebElement allowAllEntriesRadioBtn = driver.findElement(By.xpath("//*[@id='allEntryTypes-inputEl']"));
        bValue = allowAllEntriesRadioBtn.isSelected();
        if (!bValue) {
            allowAllEntriesRadioBtn.click();
        }
        System.out.println("Made it to create radio btns");
        WebElement requiredRadioBtn = driver.findElement(By.xpath("//*[@id='requiredFlagCheckBox-inputEl']"));
        bValue = requiredRadioBtn.isSelected();
        if (!bValue) {
            requiredRadioBtn.click();
        }
        driver.findElement(By.xpath("//*[@id='tool-1275-toolEl']")).click();
//        sleep(2000);

        try {
            WebElement element = driver.findElement(By.cssSelector("#tableview-1280 .x-grid-item-container table"));
            element.click();
        } catch (Exception e) {
            System.out.println(e);
        }
        sleep(1000);
//        System.out.println(element);
        driver.findElement(By.xpath("//*[@id='editAttributeForm-label-inputEl']")).click();
        sleep(2000);

        WebElement visibleRadioBtn = driver.findElement(By.xpath("//*[@id='checkboxfield-1202-inputEl']"));
        bValue = visibleRadioBtn.isSelected();
        if (!bValue) {
            visibleRadioBtn.click();
        }
        sleep(1000);
        WebElement allowUserCodesRadioBtn = driver.findElement(By.xpath("//*[@id='checkboxfield-1205-inputEl']"));
        bValue = allowUserCodesRadioBtn.isSelected();
        if (!bValue) {
            allowUserCodesRadioBtn.click();
        }
        sleep(1000);
        driver.findElement(By.xpath("//*[@id='editAttributeWin-save']")).click();
        sleep(2000);
    }

    public void deleteAttribute(WebDriver driver, String attrName) throws InterruptedException {

        if (tableClickRowCol("tableview-1092", attrName, driver)) {

            driver.findElement(By.xpath("//*[@id='attributeGrid-tools-action']")).click();
            sleep(1000);
            driver.findElement(By.id("attributeGrid-tools-action-delete-itemEl")).click();
            sleep(500);
            driver.findElement(By.xpath("//*[@id='button-1037']")).click();
        }
        sleep(5000);
    }

    public void attributeManageCodes(WebDriver driver, String attrName) throws InterruptedException {

        if (tableClickRowCol("tableview-1092", attrName, driver)) {

            driver.findElement(By.xpath("//*[@id='attributeGrid-tools-manageCodes']")).click();
            sleep(500);
            driver.findElement(By.xpath("//*[@id='button-1163']")).click();
            sleep(500);
            driver.findElement(By.xpath("//*[@id='editCodeForm-label-inputEl']")).sendKeys("MyTestCodeLabel11");
            driver.findElement(By.xpath("//*[@id='editCodeForm-code-inputEl']")).sendKeys("MYTESTCODETYPE11");
            driver.findElement(By.xpath("//*[@id='editCodeWin-save']")).click();

            // Download Attachment to be done manually 
        } else {

            LOG.log(Level.INFO, "Unable to add code");
        }
    }

    public void editManageCodes(WebDriver driver, String codeLabel) throws InterruptedException {

        
        if (tableClickRowCol("tableview-1158", codeLabel, driver)) {

            driver.findElement(By.xpath("//*[@id='codesGrid-tools-edit']")).click();
            sleep(500);
            
            ((JavascriptExecutor) driver).executeScript("tinyMCE.activeEditor.setContent('Testing My Code Type11')");
            sleep(400);
            driver.findElement(By.xpath("//*[@id='editCodeWin-save']")).click();
            sleep(1000);
        } else {

            LOG.log(Level.INFO, "Failed to edit attribute manage code");
        }

    }

    public void toggleStatusManageCodes(WebDriver driver, String codeLabel) throws InterruptedException {

        if (tableClickRowCol("tableview-1158", codeLabel, driver)) {
            
            // toggle status
            driver.findElement(By.xpath("//*[@id='codesGrid-tools-toggle']")).click();
            sleep(500);
            driver.findElement(By.xpath("//*[@id='codesFilter-activeStatus-trigger-picker']")).click();
            sleep(100);
            driver.findElement(By.id("codesFilter-activeStatus-inputEl")).clear();
            driver.findElement(By.id("codesFilter-activeStatus-inputEl")).sendKeys("Inactive");

            sleep(3000);
//            driver.switchTo().defaultContent();
            driver.findElement(By.id("tableview-1158")).click();
            driver.findElement(By.xpath("//*[@id='button-1161']")).click();

            sleep(500);
            // search for code in inactive table click and delete
            if (tableClickRowCol("tableview-1158", codeLabel, driver)) {

                driver.findElement(By.xpath("//*[@id='codesGrid-tools-delete']")).click();
                driver.findElement(By.xpath("//*[@id='button-1037']")).click();
                sleep(1000);
            }
            
            driver.findElement(By.xpath("//*[@id='manageCodesCloseBtn']")).click();
            sleep(2000);
        }  
    }
}
