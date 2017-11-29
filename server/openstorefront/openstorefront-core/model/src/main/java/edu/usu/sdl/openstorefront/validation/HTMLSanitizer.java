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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

/**
 * The sanitizes HTML to prevent XSS attacks This will allow structure....but no
 * scripting and it will set the no follow
 *
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
			String safe = Jsoup.clean(fieldData.toString(), new Whitelist().preserveRelativeLinks(true)
					.addTags("a", "base", "b", "blockquote", "br", "caption", "cite", "code", "col",
							"colgroup", "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6",
							"i", "img", "li", "ol", "p", "pre", "q", "small", "strike", "strong",
							"sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u",
							"ul", "label", "video", "audio", "source", "track", "font")
					.addAttributes("a", "href", "title")
					.addAttributes("div", "ng-show", "class")
					.addAttributes("p", "data-attributelabel", "class")
					.addAttributes("blockquote", "cite")
					.addAttributes("col", "span", "width")
					.addAttributes("font", "color", "face")
					.addAttributes("colgroup", "span", "width")
					.addAttributes("img", "align", "alt", "height", "src", "title", "width", "data-storefront-ignore")
					.addAttributes("ol", "start", "type")
					.addAttributes("q", "cite")
					.addAttributes("video", "id", "width", "height", "controls", "preload", "autoplay", "loop", "muted", "src", "poster")
					.addAttributes("audio", "id", "width", "height", "controls", "preload", "autoplay", "loop", "muted", "src")
					.addAttributes("source", "src", "type", "media")
					.addAttributes("track", "src", "label", "kind", "srclang", "default")
					.addAttributes("q", "cite")
					.addAttributes("table", "summary", "width", "height", "border", "align", "cellspacing", "cellpadding", "bgcolor", "bordercolor", "class")
					.addAttributes("td", "abbr", "axis", "colspan", "rowspan", "width", "class")
					.addAttributes("th", "abbr", "axis", "colspan", "rowspan", "scope","width", "class")
					.addAttributes("tr", "class")
					.addAttributes("ul", "type")
					//.addProtocols("a", "href", "ftp", "http", "https", "mailto")
					.addProtocols("blockquote", "cite", "http", "https")
					.addProtocols("cite", "cite", "http", "https")
					.addProtocols("q", "cite", "http", "https")
					.addTags("component-list")
					.addTags("span")
					.addAttributes("component-list", "hide-more", "click-callback", "class-list", "title", "data", "cols", "type", "filters", "set-filters", "code")
					//.addAttributes("a", "ng-click", "id")
					.addAttributes(":all", "style")
					.addEnforcedAttribute("a", "rel", "nofollow")
			);
			safe = removeBadStyles(safe);
			// this is a hidden white space "Line Seperator" not an empty string
			// for information on Line Seperator see http://www.fileformat.info/info/unicode/char/2028/index.htm
			// "line separators basically correspond to HTML <BR>" http://unicode.org/versions/Unicode5.2.0/ch05.pdf pg. 147
			safe = safe.replace(" ", "<br>");
			return safe;
		}
	}

	private String removeBadStyles(String html)
	{
		String safe;
		Map<String, List<String>> badStyles = getBadStyles();
		if (!badStyles.isEmpty()) {
			Document doc = Jsoup.parse(html);
			badStyles.forEach((key, value) -> {
				value.forEach(styleValue -> {
					List<String> styleList = getStyleVariations(key, styleValue);
					styleList.forEach(styleToRemove -> {
						Elements tags = doc.select(String.format("[style*=\"%s\"]", styleToRemove));
						tags.forEach((element) -> {
							String elementStyles = element.attr("style");
							element.attr("style", elementStyles.replace(styleToRemove, ""));
						});
					});
				});
			});
			safe = doc.body().html();
		} else {
			safe = html;
		}
		return safe;
	}

	private List<String> getStyleVariations(String styleName, String styleValue)
	{
		List<String> styleList = new ArrayList();
		styleList.add(String.format("%s:%s", styleName, styleValue));
		styleList.add(String.format("%s :%s", styleName, styleValue));
		styleList.add(String.format("%s: %s", styleName, styleValue));
		styleList.add(String.format("%s : %s", styleName, styleValue));
		return styleList;
	}

	private Map<String, List<String>> getBadStyles()
	{
		Map<String, List<String>> badStyles = new HashMap<>();
		List<String> positionValues = new ArrayList();
		positionValues.add("absolute;");
		positionValues.add("fixed;");
		positionValues.add("static;");
		badStyles.put("position", positionValues);
		return badStyles;
	}
}
