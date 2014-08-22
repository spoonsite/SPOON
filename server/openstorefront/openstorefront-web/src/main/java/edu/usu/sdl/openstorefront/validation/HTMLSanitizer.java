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

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * The sanitizes HTML to prevent XSS attacks This will allow structure....but no
 * scripting and it will set the no follow
 *
 * @See http://jsoup.org/cookbook/cleaning-html/whitelist-sanitizer
 * @author dshurtleff
 */
public class HTMLSanitizer
		extends Sanitizer
{

	@Override
	public Object santize(Object fieldData)
	{
		if (fieldData == null) {
			return fieldData;
		} else {
			String safe = Jsoup.clean(fieldData.toString(), Whitelist.relaxed().addEnforcedAttribute("a", "rel", "nofollow"));
			return safe;
		}
	}

}
