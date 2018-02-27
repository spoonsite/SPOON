/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.exception.AttachedReferencesException;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.selenium.provider.AttributeProvider;
import edu.usu.sdl.openstorefront.selenium.provider.AuthenticationProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ClientApiProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ComponentAnswerProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ComponentProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ComponentQuestionProvider;
import edu.usu.sdl.openstorefront.selenium.provider.ComponentTypeProvider;
import edu.usu.sdl.openstorefront.selenium.provider.NotificationEventProvider;
import edu.usu.sdl.openstorefront.selenium.provider.OrganizationProvider;
import edu.usu.sdl.openstorefront.ui.test.BrowserTestBase;
import java.util.List;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
public class AdminDeletePendingAnswerIT
		extends BrowserTestBase
{
	private static final Logger LOG = Logger.getLogger(BrowserTestBase.class.getName());
	private final String SUBMISSION_NAME = "Selenium Question";
	private final String SUBMISSION_ANSWER = "Selenium Answer";
	private final String entryOrganization = "Selenium Organization";
	private AuthenticationProvider authProvider;
	private ClientApiProvider provider;
	private AttributeProvider attributeProvider;
	private OrganizationProvider organizationProvider;
	private ComponentProvider componentProvider;
	private ComponentTypeProvider componentTypeProvider;
	private ComponentQuestionProvider questionProvider;
	private ComponentAnswerProvider answerProvider;
	private NotificationEventProvider notificationProvider;
	
	@Before
	public void setupTest() throws InterruptedException
	{
		authProvider = new AuthenticationProvider(properties, webDriverUtil);
		authProvider.login();
		provider = new ClientApiProvider();
		
		//API create question and answer
		componentTypeProvider = new ComponentTypeProvider(provider.getAPIClient());
		attributeProvider = new AttributeProvider(provider.getAPIClient());
		organizationProvider = new OrganizationProvider(provider.getAPIClient());
		organizationProvider.createOrganization(SUBMISSION_NAME);
		componentProvider = new ComponentProvider(attributeProvider, organizationProvider, componentTypeProvider, provider.getAPIClient());
		Component component = componentProvider.createComponent(SUBMISSION_NAME, "Selenium Test Entry", entryOrganization);
		
		questionProvider = new ComponentQuestionProvider(provider.getAPIClient());
		ComponentQuestion question = questionProvider.addComponentQuestion("Are you Selenium?", component);
		
		answerProvider = new ComponentAnswerProvider(provider.getAPIClient());
		answerProvider.addComponentQuestionAnswer(SUBMISSION_ANSWER, question, component);
		
		notificationProvider = new NotificationEventProvider(provider.getAPIClient());
	}
	
	@Test
	public void trialTest()
	{
		for (WebDriver driver : webDriverUtil.getDrivers()) {
			webDriverUtil.getPage(driver, "UserTool.action?load=Questions");
			
			deleteAnswer(driver);
			//click Answers tab
			
			//select answer created by API in setupTest()
			
			//click Delete and confirm
			
			//Verify question answer no longer in table
			
			sleep(1000);
		}
	}
	
	public void deleteAnswer(WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 8);

		String mainWindow = driver.getWindowHandle();
		
		//click question
		WebElement question = findElementByString(driver, "#questionGrid-body tr td", "Are you Selenium?");
		Assert.assertTrue(question != null);
		question.click();
		//click view entry
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='viewQuestionEntryBtn']"))).click();

		WebElement frame = driver.findElement(By.cssSelector("iframe"));
		driver.switchTo().frame(frame);

		//click questions and answers tab
		WebElement tab = findElementByString(driver, "[role='tab']", "Questions & Answers");
		Assert.assertTrue(tab != null);
		tab.click();
		//click Delete on the answer
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".small-button-danger"))).click();
		//confirm delete click YES
		WebElement confirm = findElementByString(driver, ".x-btn", "Yes");
		Assert.assertTrue(confirm != null);
		confirm.click();

		//confirm answer no longer there
		sleep(100);
		List<WebElement> answers = driver.findElements(By.cssSelector(".question-response"));
		Assert.assertTrue(answers.size() < 1);
	}

	
	@After
	public void cleanupTest() throws AttachedReferencesException
	{
		componentProvider.cleanup();
		notificationProvider.cleanup();
		provider.clientDisconnect();
	}
}
