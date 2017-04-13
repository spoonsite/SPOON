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
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Tests for Admin Attributes Tool
 *
 * @author dshurtleff
 */
public class AdminAttributesTest
		extends AdminTestBase
{

	public AdminAttributesTest()
	{
	}

	@Test
	public void toolLoaded()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
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

	}

}
