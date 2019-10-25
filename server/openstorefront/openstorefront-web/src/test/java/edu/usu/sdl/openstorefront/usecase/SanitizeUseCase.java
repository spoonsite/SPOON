/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.usecase;

import edu.usu.sdl.openstorefront.validation.LinkSanitizer;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class SanitizeUseCase
{

	@Test
	public void testSanitize()
	{
		LinkSanitizer linkSanitizer = new LinkSanitizer();

		String out = (String) linkSanitizer.sanitize("Media.action?GeneralMedia&name=Test");
		System.out.println(out);

		out = (String) linkSanitizer.sanitize("ws://www.google.com/q=test&check=true");
		System.out.println(out);

		out = (String) linkSanitizer.sanitize("<a href='ws://www.google.com/q=test&check=true'>test</a>");
		System.out.println(out);

		out = (String) linkSanitizer.sanitize("<a href='Media.action?GeneralMedia&name=Test'>test2</a>");
		System.out.println(out);

		out = (String) linkSanitizer.sanitize("<a href='http://test.com/Media.action?GeneralMedia&name=Test'>test2</a>");
		System.out.println(out);

	}

	@Test
	public void testStringPadRemoval()
	{
		System.out.println(StringUtils.stripStart("0001", "0"));
		System.out.println(StringUtils.stripStart("0301", "0"));
		System.out.println(StringUtils.stripStart("5", "0"));
		System.out.println(StringUtils.stripStart("0000", "0"));
		System.out.println(StringUtils.stripStart("1000", "0"));
		System.out.println(StringUtils.stripStart("0020", "0"));
		System.out.println(StringUtils.stripStart("bob", "0"));
	}

}
