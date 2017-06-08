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
import java.util.logging.Logger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author ccummings
 */
public class AdminEntryTypesTest
		extends AdminTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());

	@Test
	public void adminEntryTypesTest() throws InterruptedException
	{

		for (WebDriver driver : webDriverUtil.getDrivers()) {

			setup(driver);
			createEntryType(driver, "AMAZING-TEST", "An Amazing Test");
			
		}
	}

	public void setup(WebDriver driver)
	{
		driver.get(webDriverUtil.getPage("AdminTool.action?load=Entry-Types"));
		
		(new WebDriverWait(driver, 10)).until((ExpectedCondition<Boolean>) (WebDriver driverLocal) -> {
			List<WebElement> titleElements = driverLocal.findElements(By.id("entryGrid_header-title-textEl"));
			if (titleElements.size() > 0) {
				return titleElements.get(0).isDisplayed();
			} else {
				return false;
			}
		});
	}

	public void createEntryType(WebDriver driver, String typeCode, String typeLabel) throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#entryTypeAddBtn"))).click();
		
		
	}

	public void deleteEntryType(WebDriver driver, String typeCode) throws InterruptedException
	{
		assertTrue(tableClickRowCol(".x-grid-item-container", typeCode, driver, 0));
	}

	public void editEntryTypes(WebDriver driver, String codeLabel) throws InterruptedException
	{

	}
}
