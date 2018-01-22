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
public class TextSanitizerTest
{

	private final String input;
	private final String expectedOutput;

	public TextSanitizerTest(String input, String expectedOutput)
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
		String lineSeparator = Character.toString((char) 0x2028);
		return Arrays.asList(new Object[][]{
			{null, null},
			{"<html><body><div><a href=\"/click\">click ME</a><div>Hello World</div></div></body></html>", "click MEHello World"},
			{"<html><body><div>Hello  World</div></body></html>", "Hello\n World"},// hidden whitespace Line Seperator
			{"Hello " + lineSeparator + "World", "Hello\n\nWorld"},// hidden whitespace Line Seperator
		});
	}

	@Test
	public void santizeTest()
	{
		TextSanitizer sanitizer = new TextSanitizer();
		Object result = sanitizer.santize(input);
		assertEquals(expectedOutput, result);
	}
}
