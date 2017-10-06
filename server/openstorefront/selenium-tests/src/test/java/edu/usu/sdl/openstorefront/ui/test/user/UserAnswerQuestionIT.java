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
package edu.usu.sdl.openstorefront.ui.test.user;

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.List;
import java.util.logging.Logger;
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
 * @author rfrazier
 */
public class UserAnswerQuestionIT
		extends UserTestBase
{

	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());
	private static final String submissionName = "Selenium Question";

	@BeforeClass
	public static void setupTest()
	{
		Component questionComponent = createUserComponent(submissionName);
		apiClient.getComponentRESTTestClient().addAPIComponentQuestion("Are you Selenium?", questionComponent);

	}

	@Test
	public void answerQuestionTest()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			webDriverUtil.getPage(driver, "UserTool.action?load=Questions");
			answerQuestion(driver);
		}

	}
	
	public void answerQuestion(WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);
		List<WebElement> table = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#questionGrid-body tr td")));
		WebElement question = null;
		boolean found = false;
		
		for (WebElement element : table)
		{
			if (element.getText().equals("Are you Selenium?"))
			{
				question = element;
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
		question.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='viewQuestionEntryBtn']"))).click();

		//find the Questions & Answers tab
		
		String winHandleBefore = driver.getWindowHandle();

		WebElement frame = driver.findElement(By.cssSelector("iframe"));
		System.out.println("Frame: " + frame.getAttribute("name"));

		driver.switchTo().frame(frame);
		sleep(500);
		
		List<WebElement> tabs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("[role='tab']")));
		System.out.println("Num tabs: " + tabs.size());
		WebElement tab = null;
		found = false;
		for (WebElement element : tabs)
		{
			System.out.println("Element: " + element.getText());
			if (element.getText().equals("Questions & Answers"))
			{
				tab = element;
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
		tab.click();
		sleep(1000);
	}

	public void verifyQuestionAnswer(WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".question-response")));
	}

}
