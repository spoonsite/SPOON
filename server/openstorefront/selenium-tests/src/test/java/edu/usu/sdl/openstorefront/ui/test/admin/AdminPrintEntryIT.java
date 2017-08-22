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

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminView;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author ccummings
 */
public class AdminPrintEntryIT
		extends AdminTestBase
{
	// custom template drop-down menu class = x-box-inner x-box-scroller-body-vertical
	// custom template btn id = customTemplateBtn
	// print button id = printCustomizedEntryBtn
	
	private static String entryName = "A Selenium Test Entry";
	
	@BeforeClass
	protected void createEntryToPrint()
	{
		createBasicSearchComponent(entryName);
	}
	
	@Test
	public void printEntryFromSearchResults()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {

			searchAndClickEntry(driver, "Test");
		}
	}
	
	public static void createBasicSearchComponent(String componentName)
	{
		Component myEntry = apiClient.getComponentRESTTestClient().createAPIComponent(componentName);
		System.out.println("My name is " + myEntry.getName());
		ComponentAdminView entry = null;

		int timer = 0;

		while (entry == null && timer < 10000) {

			timer += 200;
			sleep(200);
			entry = apiClient.getComponentRESTTestClient().getComponentByName(componentName);

		}
	}
	
	public void searchAndClickEntry(WebDriver driver, String entryName)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);
		List<WebElement> entryResults = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#resultsDisplayPanel-innerCt h2")));

		boolean isResult = false;

		for (WebElement entry : entryResults) {

			System.out.println("Entry Name: " + entry.getText());
			if (entry.getText().equals(entryName)) {
				isResult = true;
				entry.click();
				break;
			}
		}

		Assert.assertTrue(isResult);
	}

}
