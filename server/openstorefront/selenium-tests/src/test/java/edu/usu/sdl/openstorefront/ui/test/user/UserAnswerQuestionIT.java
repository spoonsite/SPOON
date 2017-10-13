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
	private static final String SUBMISSION_NAME = "Selenium Question";
	private static final String SUBMISSION_ANSWER = "Selenium Answer";

	@BeforeClass
	public static void setupTest()
	{
		Component questionComponent = createUserComponent(SUBMISSION_NAME);
		apiClient.getComponentRESTTestClient().addAPIComponentQuestion("Are you Selenium?", questionComponent);
	}

	@Test
	public void answerQuestionTest()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			webDriverUtil.getPage(driver, "UserTool.action?load=Questions");
			answerQuestion(driver);
			verifyQuestionAnswer(driver);
		}
	}

	public void answerQuestion(WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);

		String mainWindow = driver.getWindowHandle();

		WebElement question = findElementByString(driver, "#questionGrid-body tr td", "Are you Selenium?");
		Assert.assertTrue(question != null);
		question.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='viewQuestionEntryBtn']"))).click();

		WebElement frame = driver.findElement(By.cssSelector("iframe"));
		driver.switchTo().frame(frame);

		WebElement tab = findElementByString(driver, "[role='tab']", "Questions & Answers");
		Assert.assertTrue(tab != null);
		tab.click();

		WebElement asnBtn = findElementByString(driver, ".x-btn", "Answer");
		Assert.assertTrue(asnBtn != null);
		asnBtn.click();

		WebElement answerIframe = driver.findElement(By.cssSelector(".x-htmleditor-iframe"));
		driver.switchTo().frame(answerIframe);
		WebElement answerTextArea = driver.findElement(By.cssSelector("body"));
		Assert.assertTrue(answerTextArea != null);
		answerTextArea.sendKeys(SUBMISSION_ANSWER);

		driver.switchTo().parentFrame();

		WebElement post = findElementByString(driver, ".x-btn-inner", "Post");
		Assert.assertTrue(post != null);
		post.click();

		driver.switchTo().window(mainWindow);
	}

	public void verifyQuestionAnswer(WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);

		WebElement frame = driver.findElement(By.cssSelector("iframe"));
		driver.switchTo().frame(frame);

		List<WebElement> questionResponseList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".question-response")));
		boolean isFound = false;
		for (WebElement response : questionResponseList) {
			if (response.getText().equals("A. " + SUBMISSION_ANSWER)) {
				isFound = true;
				break;
			}
		}
		Assert.assertTrue(isFound);
	}

	private WebElement findElementByString(WebDriver driver, String selector, String text)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);
		List<WebElement> els = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(selector)));
		WebElement element = null;

		for (WebElement el : els) {
			if (el.getText().equals(text)) {
				element = el;
				break;
			}
		}
		return element;
	}

}
