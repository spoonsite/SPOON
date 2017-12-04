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
package edu.usu.sdl.openstorefront.validation;

import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author kbair
 */
@RunWith(Parameterized.class)
public class HTMLSanitizerTest
{
	private final String input;
	private final String expectedOutput;

	public HTMLSanitizerTest(String input, String expectedOutput)
	{
		this.input = input;
		this.expectedOutput = expectedOutput;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> generateData()
	{
		// In this example, the parameter generator returns a List of
		// arrays.  Each array has two elements: { input, expectedOutput }.
		// These data are hard-coded into the class, but they could be
		// generated or loaded in any way you like.
		return Arrays.asList(new Object[][]{
			{null, null},
			{"<html><body><div class=\"box\" style=\"width: 1362px;top:100px;left:100px;\"><a href=\"/click\">click ME</a><div class=\"box\" style=\"top:100px;left:100px;\">Hello World</div></div></body></html>", "<div class=\"box\" style=\"width: 1362px;top:100px;left:100px;\"> \n <a href=\"/click\" rel=\"nofollow\">click ME</a> \n <div class=\"box\" style=\"top:100px;left:100px;\">\n   Hello World \n </div> \n</div>"},
			{"<html><body><div class=\"box\" style=\"width: 1362px;position:fixed;top:100px;left:100px;\"><a href=\"/click\">click ME</a><div class=\"box\" style=\"position:absolute;top:100px;left:100px;\">Hello World</div></div></body></html>", "<div class=\"box\" style=\"width: 1362px;top:100px;left:100px;\"> \n <a href=\"/click\" rel=\"nofollow\">click ME</a> \n <div class=\"box\" style=\"top:100px;left:100px;\">\n   Hello World \n </div> \n</div>"},
			{"<html><body><div class=\"box\" style=\"width: 1362px;position : fixed;top:100px;left:100px;\"><a href=\"/click\">click ME</a><div class=\"box\" style=\"position : absolute;top:100px;left:100px;\">Hello World</div></div></body></html>", "<div class=\"box\" style=\"width: 1362px;top:100px;left:100px;\"> \n <a href=\"/click\" rel=\"nofollow\">click ME</a> \n <div class=\"box\" style=\"top:100px;left:100px;\">\n   Hello World \n </div> \n</div>"},
			{"<html><body><div class=\"box\" style=\"width: 1362px;position: fixed;top:100px;left:100px;\"><a href=\"/click\">click ME</a><div class=\"box\" style=\"position: absolute;top:100px;left:100px;\">Hello World</div></div></body></html>", "<div class=\"box\" style=\"width: 1362px;top:100px;left:100px;\"> \n <a href=\"/click\" rel=\"nofollow\">click ME</a> \n <div class=\"box\" style=\"top:100px;left:100px;\">\n   Hello World \n </div> \n</div>"},
			{"<html><body><div class=\"box\" style=\"width: 1362px;position :fixed;top:100px;left:100px;\"><a href=\"/click\">click ME</a><div class=\"box\" style=\"position :absolute;top:100px;left:100px;\">Hello World</div></div></body></html>", "<div class=\"box\" style=\"width: 1362px;top:100px;left:100px;\"> \n <a href=\"/click\" rel=\"nofollow\">click ME</a> \n <div class=\"box\" style=\"top:100px;left:100px;\">\n   Hello World \n </div> \n</div>"},
			{"<html><body><div>Hello  World</div></body></html>", "<div>\n  Hello<br> World \n</div>"},// hidden whitespace Line Seperator
		});
	}

	@Test
	public void santizeTest()
	{
		HTMLSanitizer sanitizer = new HTMLSanitizer();
		Object result = sanitizer.santize(input);
		assertEquals(expectedOutput, result);
	}
}
