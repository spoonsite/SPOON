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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Tests for Admin Attributes Tool
 *
 * @author ccummings
 */
public class AdminAttributesIT
		extends AdminTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

	@Test
	public void adminAttributesTest() throws InterruptedException
	{

		for (WebDriver driver : webDriverUtil.getDrivers()) {

			setupDriver(driver);
			deleteAttribute(driver, "MyTestAttribute17");
			createAttribute(driver, "MyTestAttribute17", "MYTESTATTR17");
			createAPIAttributeType();
			attributeManageCodes(driver, "MyTestAttribute17");
			editManageCodes(driver, "MyTestCodeLabel11");
			toggleStatusManageCodes(driver, "MyTestCodeLabel11");
			deleteAttribute(driver, "MyTestAttribute17");
		}
	}

	public void setupDriver(WebDriver driver)
	{

		webDriverUtil.getPage(driver, "AdminTool.action?load=Attributes");

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
	
	private void createAPIAttributeType()
	{
		apiClient.getAttributeTestClient().createAPIAttribute();
	}

	public void createAttribute(WebDriver driver, String attrName, String attrCode)
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='attributesRefreshBtn']"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#attributeGrid-tools-add"))).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#editAttributeForm-label-inputEl"))).sendKeys(attrName);

		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#editAttributeForm-code-inputEl"))).sendKeys(attrCode);

		// radio buttons
		boolean bValue = false;

		WebElement allowAllEntriesRadioBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#allEntryTypes-inputEl")));
		bValue = allowAllEntriesRadioBtn.isSelected();

		if (!bValue) {
			allowAllEntriesRadioBtn.click();
		}

		WebElement requiredRadioBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#requiredFlagCheckBox-inputEl")));
		bValue = requiredRadioBtn.isSelected();
		if (!bValue) {
			requiredRadioBtn.click();
		}

		WebElement plusTool = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#editAttributeForm-typesRequiredFor_header-targetEl .x-tool-plus")));
		plusTool.click();

		try {

			List<WebElement> tableList = wait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.cssSelector("[data-test='reqAttrList']"), By.cssSelector(".x-grid-cell.x-grid-td")));
			for (WebElement item : tableList) {
				if (item.getText().equals("DI2E Component")) {
					item.click();
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#editAttributeForm-label-inputEl"))).click();

		WebElement visibleRadioBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[name='visibleFlg']")));
		bValue = visibleRadioBtn.isSelected();
		if (!bValue) {
			visibleRadioBtn.click();
		}

		WebElement allowUserCodesRadioBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[name='allowUserGeneratedCodes']")));
		bValue = allowUserCodesRadioBtn.isSelected();
		if (!bValue) {
			allowUserCodesRadioBtn.click();
		}

		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#editAttributeWin-save"))).click();

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#attributeGrid-body .x-component.x-border-box.x-mask")));

		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#attributeGrid-body .x-component.x-border-box.x-mask")));
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public void deleteAttribute(WebDriver driver, String attrName) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);

		if (tableClickRowCol("#attributeGrid-body .x-grid-view", attrName, driver, 1)) {

			wait.until(ExpectedConditions.elementToBeClickable(By.id("attributeGrid-tools-action"))).click();

			wait.until(ExpectedConditions.elementToBeClickable(By.id("attributeGrid-tools-action-delete-itemEl"))).click();

			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-toolbar-item.x-btn-default-small:not([aria-hidden*='true'])"))).click();
		}

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#attributeGrid .x-component.x-border-box.x-mask")));

		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#attributeGrid .x-component.x-border-box.x-mask")));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void attributeManageCodes(WebDriver driver, String attrName) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);

		if (tableClickRowCol("#attributeGrid-body .x-grid-view", attrName, driver, 1)) {

			WebElement manageCodesBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("attributeGrid-tools-manageCodes")));
			manageCodesBtn.click();

			WebElement addNewCodeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("addNewCodeAttr")));
			addNewCodeBtn.click();

			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editCodeForm-label-inputEl"))).sendKeys("MyTestCodeLabel11");

			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editCodeForm-code-inputEl"))).sendKeys("MYTESTCODETYPE11");

			wait.until(ExpectedConditions.elementToBeClickable(By.id("editCodeWin-save"))).click();

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".x-mask-msg")));

			// Download Attachment to be done manually 
		} else {

			LOG.log(Level.INFO, "Unable to add code");
		}
	}

	public void editManageCodes(WebDriver driver, String codeLabel) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);

		if (tableClickRowCol("#codesGrid-body .x-grid-view", codeLabel, driver, 0)) {

			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#codesGrid-tools-edit"))).click();

			((JavascriptExecutor) driver).executeScript("tinyMCE.activeEditor.setContent('Testing My Code Type11')");

			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#editCodeWin-save"))).click();

			try {

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#codesGrid-body .x-component.x-border-box.x-mask")));

			} catch (Exception e) {
				System.out.println(e);
			}

			try {
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#codesGrid-body .x-component.x-border-box.x-mask")));
			} catch (Exception e) {
				System.out.println(e);
			}

		} else {

			LOG.log(Level.INFO, "Failed to edit attribute manage code");
		}

	}

	public void toggleStatusManageCodes(WebDriver driver, String codeLabel) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);

		if (tableClickRowCol("#codesGrid-body .x-grid-view", codeLabel, driver, 0)) {

			// toggle status
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#codesGrid-tools-toggle"))).click();

			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#codesFilter-activeStatus-trigger-picker"))).click();

			driver.findElement(By.id("codesFilter-activeStatus-inputEl")).clear();
			driver.findElement(By.id("codesFilter-activeStatus-inputEl")).sendKeys("Inactive");
			driver.findElement(By.id("codesFilter-activeStatus-inputEl")).sendKeys(Keys.RETURN);

			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#refreshAttrCodes"))).click();
			// search for code in inactive table click and delete
			if (tableClickRowCol("#codesGrid-body .x-grid-view", codeLabel, driver, 0)) {

				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#codesGrid-tools-delete"))).click();

				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".x-toolbar.x-docked.x-toolbar-footer .x-btn:not([aria-hidden*='true'])"))).click();

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#refreshAttrCodes")));
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#refreshAttrCodes"))).click();
			}

			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#manageCodesCloseBtn"))).click();
		}
	}
	
	@AfterClass
	public static void cleanup()
	{
		apiClient.cleanup();
	}
}
