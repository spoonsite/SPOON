/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/**
 * Allow basic html tags
 *
 * @author dshurtleff
 */
public class BasicHTMLSanitizer
		extends Sanitizer
{

	@Override
	public Object santize(Object fieldData)
	{
		if (fieldData == null) {
			return fieldData;
		} else {
			String safe = Jsoup.clean(
					fieldData.toString(),
					"",
					Whitelist.basic()
							.addTags("div")
							.addTags("font")
							.addAttributes("font", "color", "face", "size", "style")
							.addAttributes(":all", "style"),
					new Document.OutputSettings().prettyPrint(false)
			);
			safe = StringProcessor.removeBadStyles(safe);
			// this is a hidden white space "Line Seperator" not an empty string
			// for information on Line Seperator see http://www.fileformat.info/info/unicode/char/2028/index.htm
			// "line separators basically correspond to HTML <BR>" http://unicode.org/versions/Unicode5.2.0/ch05.pdf pg. 147
			String lineSeparator = Character.toString((char) 0x2028);
			safe = safe.replace(lineSeparator, "<br>");

			return safe;
		}
	}

}
