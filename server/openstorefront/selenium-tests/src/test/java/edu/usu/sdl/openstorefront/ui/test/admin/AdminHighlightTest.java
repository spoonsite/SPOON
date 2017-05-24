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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
    public void adminHighlightTest() throws InterruptedException {

        for (WebDriver driver : webDriverUtil.getDrivers()) {

            createHighlight(driver);
            editHighlight(driver);
            deleteHighlight(driver);

        }
    }

    public void createHighlight(WebDriver driver) throws InterruptedException {

        driver.get(webDriverUtil.getPage("AdminTool.action?load=Highlights"));

        // Check to see if already exists and delete if existing
        deleteHighlight(driver);

        // Click add button
        WebDriverWait waitAddBtn = new WebDriverWait(driver, 20);
        WebElement addBtn = waitAddBtn.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='highlightGrid-tools-add']")));
        addBtn.click();

        // Fill out form
        WebDriverWait waitEntryForm = new WebDriverWait(driver, 20);
        WebElement entryForm = waitEntryForm.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='highlightEntryForm-Title-inputEl']")));
        entryForm.sendKeys("TestHighlight1");

        ((JavascriptExecutor) driver).executeScript("tinyMCE.activeEditor.setContent('Test Description 1')");

        WebDriverWait waitDropDown = new WebDriverWait(driver, 20);
        WebElement dropDownBtn = waitDropDown.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\'highlightEntryForm-Type-trigger-picker\']")));
        dropDownBtn.click();

        WebDriverWait waitComponentSelect = new WebDriverWait(driver, 20);
        WebElement selectComponent = waitComponentSelect.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(.,'Component')]")));
        selectComponent.click();

        WebDriverWait waitSaveBtn = new WebDriverWait(driver, 20);
        WebElement saveBtn = waitSaveBtn.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test = 'addEditHighlightSave']")));
        saveBtn.click();

        WebDriverWait waitLoadingFinished = new WebDriverWait(driver, 20);
        waitLoadingFinished.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#highlightGrid-body .x-mask")));

    }

    public void editHighlight(WebDriver driver) throws InterruptedException {

        // locate highlight in table and edit
        if (tableClickRowCol("#highlightGrid-body .x-grid-view", "TestHighlight1", driver)) {

            // Click Edit
            WebDriverWait waitEditBtn = new WebDriverWait(driver, 20);
            WebElement editBtn = waitEditBtn.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='highlightGrid-tools-edit']")));
            editBtn.click();

            // Add/Edit text in tinyMCE textarea
            ((JavascriptExecutor) driver).executeScript("tinyMCE.activeEditor.setContent('Test Description 1 - Edited')");

            // click save
            WebDriverWait waitSaveBtn = new WebDriverWait(driver, 20);
            WebElement saveBtn = waitSaveBtn.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test = 'addEditHighlightSave']")));
            saveBtn.click();

            WebDriverWait waitLoadingFinished = new WebDriverWait(driver, 20);
            waitLoadingFinished.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#highlightGrid-body .x-mask")));

            // refresh table
            WebDriverWait waitRefreshBtn = new WebDriverWait(driver, 20);
            WebElement refreshBtn = waitRefreshBtn.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='highlightRefreshBtn']")));
            refreshBtn.click();
        }
    }

    public void deleteHighlight(WebDriver driver) throws InterruptedException {

        if (tableClickRowCol("#highlightGrid-body .x-grid-view", "TestHighlight1", driver)) {

            WebDriverWait wait = new WebDriverWait(driver, 20);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='highlightGrid-tools-delete']")));
            element.click();

            WebDriverWait waitYesButton = new WebDriverWait(driver, 20);

            WebElement yesButton = waitYesButton.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".x-message-box .x-window-bodyWrap .x-btn:not([style*='display'])")));
            yesButton.click();

            WebDriverWait waitRefreshBtn = new WebDriverWait(driver, 20);
            WebElement refreshBtn = waitRefreshBtn.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='highlightRefreshBtn']")));
            // refresh table
            refreshBtn.click();
        }
    }
}
