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
package edu.usu.sdl.openstorefront.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * String processing methods and JSON handling.
 *
 * @author dshurtleff
 */
public class StringProcessor
{

	private static final Logger LOG = Logger.getLogger(StringProcessor.class.getName());

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private static final int MAX_RESOURCE_NAME = 35;

	/**
	 * Use this to get the GLOBAL ObjectMapper for manual JSON Handling
	 *
	 * @return
	 */
	public static ObjectMapper defaultObjectMapper()
	{
		OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return OBJECT_MAPPER;
	}

	public static String getResourceNameFromUrl(String url)
	{
		String resource = url;
		if (StringUtils.isNotBlank(url)) {
			resource = url.substring(url.lastIndexOf("/") + 1, url.length());
		}
		return resource;
	}

	/**
	 * This get the last path name (handles both / \)
	 *
	 * @param originalFilename
	 * @return just file name
	 */
	public static String getJustFileName(String originalFilename)
	{
		StringBuilder filename = new StringBuilder();
		if (StringUtils.isNotBlank(originalFilename)) {
			for (int i = originalFilename.length() - 1; i >= 0; i--) {
				char c = originalFilename.charAt(i);
				if (c == '/' || c == '\\') {
					break;
				} else {
					filename.append(c);
				}
			}
			filename.reverse();
		} else {
			return originalFilename;
		}
		return filename.toString();
	}

	/**
	 * Looks for http or ftp links in a block of text
	 *
	 * @param text
	 * @return found urls
	 */
	public static List<String> extractUrls(String text)
	{
		List<String> urls = new ArrayList<>();

		String tokens[] = text.split(" ");
		for (String token : tokens) {
			if (token.trim().toLowerCase().startsWith("http://")
					|| token.trim().toLowerCase().startsWith("https://")
					|| token.trim().toLowerCase().startsWith("ftps://")
					|| token.trim().toLowerCase().startsWith("ftp://")) {
				urls.add(token.trim());
			}
		}

		return urls;
	}

	public static String stripeExtendedChars(String data)
	{
		StringBuilder sb = new StringBuilder();
		for (char c : data.toCharArray()) {
			if (c <= 127) {
				sb.append(c);
			} else {
				sb.append(' ');
			}
		}
		return sb.toString();
	}

	public static String createHrefUrls(String text)
	{
		return createHrefUrls(text, false);
	}

	public static String createHrefUrls(String text, boolean showFullURL)
	{
		String replacedText = text;
		List<String> urls = extractUrls(text);
		for (String url : urls) {
			String resoureName = url;
			if (showFullURL == false) {
				resoureName = StringUtils.abbreviate(getResourceNameFromUrl(url), MAX_RESOURCE_NAME);
			}
			String link = "<a href='" + url + "' title='" + url + "' target='_blank'> " + resoureName + "</a>";
			replacedText = replacedText.replace(url, link);
		}
		return replacedText;
	}

	/**
	 * Remove all json fields not in the list to keep.
	 *
	 * @param json
	 * @param fieldsToKeep
	 * @return
	 */
	public static String stripeFieldJSON(String json, Set<String> fieldsToKeep)
	{
		ObjectMapper mapper = defaultObjectMapper();

		try {
			JsonNode rootNode = mapper.readTree(json);
			processNode(rootNode, fieldsToKeep);

			Object jsonString = mapper.readValue(rootNode.toString(), Object.class);
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonString);
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

	/**
	 * This only goes down one level
	 *
	 * @param rootNode
	 * @param fieldsToKeep
	 */
	private static void processNode(JsonNode rootNode, Set<String> fieldsToKeep)
	{
		if (rootNode instanceof ObjectNode) {
			ObjectNode object = (ObjectNode) rootNode;
			object.retain(fieldsToKeep);
		} else {
			for (JsonNode childNode : rootNode) {
				processNode(childNode, fieldsToKeep);
			}
		}
	}

	/**
	 * This will print an object to a string
	 *
	 * @param o
	 * @return
	 */
	public static String printObject(Object o)
	{
		StringBuilder sb = new StringBuilder();

		if (o != null) {
			try {
				Map fieldMap = BeanUtils.describe(o);
				fieldMap.keySet().stream().forEach((key) -> {
					sb.append(key).append(" = ").append(fieldMap.get(key)).append("\n");
				});
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
				Logger.getLogger(StringProcessor.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			sb.append(o);
		}

		return sb.toString();
	}

	/**
	 * This breaks on word is and so it's a bit loose when it comes to the max
	 * length. As the break wins out. It also looks for broken links and tries
	 * to preserve them.
	 *
	 * @param data
	 * @param max_length
	 * @return
	 */
	public static String ellipseString(String data, int max_length)
	{
		if (data == null) {
			return data;
		}

		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(data)) {
			String tokens[] = data.split(" ");
			boolean forceProcess = false;
			for (String token : tokens) {
				if ("<a".equalsIgnoreCase(token)) {
					forceProcess = true;
				}
				if (forceProcess
						&& "</a>".contains(token)) {
					forceProcess = false;
				}

				if (forceProcess
						|| sb.length() <= max_length) {
					sb.append(token).append(" ");
				}
			}
			sb.append("...");
		}

		return sb.toString();
	}

	public static String blankIfNull(String text)
	{
		if (text == null) {
			return "";
		} else {
			return text;
		}
	}

	public static String blankIfNull(Object text)
	{
		if (text == null) {
			return "";
		} else {
			return text.toString();
		}
	}

	public static String nullIfBlank(String text)
	{
		if (StringUtils.isBlank(text)) {
			return null;
		} else {
			return text;
		}
	}

	public static Boolean isEmail(String text)
	{
		if (text.matches(OpenStorefrontConstant.EMAIL_PATTERN)) {
			return true;
		}
		return false;
	}

	public static String stripHtml(String text)
	{
		if (StringUtils.isNotBlank(text)) {
			Document doc = Jsoup.parse(text);
			return doc.text().trim();
		}
		return text;
	}

	/**
	 * Converts a 1.1.1 to a BigDecimal for comparison
	 *
	 * @param code
	 * @return BigDecimal (returns zero on null)
	 */
	public static BigDecimal archtecureCodeToDecimal(String code)
	{
		BigDecimal result = BigDecimal.ZERO;
		if (StringUtils.isNotBlank(code)) {
			code = code.replace(".", "");
			if (code.length() > 1) {
				StringBuilder sb = new StringBuilder(code);
				sb.insert(1, ".");
				code = sb.toString();
			}
			result = Convert.toBigDecimal(code, result);
		}
		return result;
	}

	public static String urlEncode(String value)
	{
		if (StringUtils.isNotBlank(value)) {
			try {
				value = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				throw new OpenStorefrontRuntimeException("Unsupport encoding", "Check encode character set for the platform");
			}
		}
		return value;
	}

	public static String urlDecode(String value)
	{
		if (StringUtils.isNotBlank(value)) {
			try {
				value = URLDecoder.decode(value, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				throw new OpenStorefrontRuntimeException("Unsupport encoding", "Check encode character set for the platform");
			}
		}
		return value;
	}

	/**
	 * This will produce html highlighted stacktrace
	 *
	 * @param throwable
	 * @return
	 */
	public static String parseStackTraceHtml(Throwable throwable)
	{
		StringBuilder exception = new StringBuilder();

		if (throwable != null) {
			String message = throwable.getMessage();
			if (StringUtils.isNotBlank(message)) {
				exception.append("<b>Message:</b> <span style='color: red;'><b>").append(message.replace("\n", "<br>")).append("</b><br>");
			}
			for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
				String style = "color: grey; font-size: 10px;";
				if (stackTraceElement.getClassName().contains("edu.usu.sdl")) {
					style = "color: black; font-size: 12px; font-wieght: bold;";
				}
				exception.append("<span style='")
						.append(style).append("'>")
						.append(stackTraceElement.getClassName()).append(" (")
						.append(stackTraceElement.getMethodName()).append(") : ")
						.append(stackTraceElement.getLineNumber()).append(" ")
						.append("</span><br>");
			}
			if (throwable.getSuppressed().length > 0) {
				exception.append("Suppress Exceptions: ");
				for (Throwable suppressed : throwable.getSuppressed()) {
					exception.append(parseStackTraceHtml(suppressed)).append("<br>");
				}
			}

			if (throwable.getCause() != null) {

			}
		}

		return exception.toString();
	}

	/**
	 * Adds "(" as long as the input is not Blank
	 *
	 * @param s
	 * @return original string enclose or just original
	 */
	public static String enclose(String s)
	{
		return enclose(s, "(", ")");
	}

	public static String enclose(String s, String enclose)
	{
		return enclose(s, enclose, enclose);
	}

	public static String enclose(String s, String encloseStart, String encloseEnd)
	{
		if (StringUtils.isNotBlank(s)) {
			s = encloseStart + s + encloseEnd;
		}
		return s;
	}

	/**
	 * This can be used to code a key that is web-safe
	 *
	 * @param key
	 * @return
	 */
	public static String encodeWebKey(String key)
	{
		try {
			key = URLEncoder.encode(key, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new OpenStorefrontRuntimeException("Unsupported Character set.", "Likely a Programming error", ex);
		}
		return key;
	}

	/**
	 * This strips out characters that don't work well for keys
	 *
	 * @param key
	 * @return clean up key
	 */
	public static String cleanEntityKey(String key)
	{
		if (StringUtils.isNotBlank(key)) {
			List<String> badChars = Arrays.asList(" ", "/", "?", "&", "[", "]", "@", "!", "$", "'", "(", ")", "*", "+", ",", ";", "=", "%", ":", "~", "#");
			for (String badChar : badChars) {
				key = key.replace(badChar, "");
			}
		}
		return key;
	}

	/**
	 * Finds filename extension if possible
	 *
	 * @param filename
	 * @return extension or null if not found
	 */
	public static String getFileExtension(String filename)
	{
		String extension = null;
		if (StringUtils.isNotBlank(filename)) {
			int index = filename.lastIndexOf(".");
			if (index != -1) {
				extension = filename.substring(index + 1);
			}
		}
		return extension;
	}

	public static String cleanFileName(String badFileName)
	{
		if (StringUtils.isNotBlank(badFileName)) {
			List<Integer> bads = Arrays.asList(
					0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 34, 42, 47, 58, 60, 62, 63, 92, 124
			);
			Set<Integer> badChars = new HashSet<>();
			badChars.addAll(bads);

			StringBuilder cleanName = new StringBuilder();
			for (int i = 0; i < badFileName.length(); i++) {
				int c = (int) badFileName.charAt(i);
				if (badChars.contains(c) == false) {
					cleanName.append((char) c);
				}
			}
			return cleanName.toString();
		}
		return badFileName;
	}

	public static String puralize(int size, String nonPuralForm, String puralForm)
	{
		if (StringUtils.isNotBlank(nonPuralForm)) {
			if (size == 1) {
				return nonPuralForm;
			} else {
				if (StringUtils.isNotBlank(puralForm)) {
					return puralForm;
				}
				return nonPuralForm + "s";
			}
		} else {
			return nonPuralForm;
		}
	}

	public static String getHexFromBytes(byte[] bytes)
	{
		StringBuilder hexString = new StringBuilder();

		for (int i = 0; i < bytes.length; i++) {
			if ((0xff & bytes[i]) < 0x10) {
				hexString.append("0").append(Integer.toHexString((0xFF & bytes[i])));
			} else {
				hexString.append(Integer.toHexString(0xFF & bytes[i]));
			}
		}

		return hexString.toString();
	}

	/**
	 * This decode x00xx escape codes (UTF-8) codes
	 *
	 * @param input
	 * @return
	 */
	public static String decodeHexCharEscapes(String input)
	{
		if (StringUtils.isNotBlank(input)) {
			String newInput = input.replace("x", "");
			Integer charCode = Integer.valueOf(newInput, 16);
			if (charCode != null) {
				newInput = "" + (char) (charCode.intValue());
				return newInput;
			}
		}
		return input;
	}

	/**
	 * This is wrapper method for plugins
	 *
	 * @param input
	 * @return
	 */
	public static boolean stringIsNotBlank(String input)
	{
		return StringUtils.isNotBlank(input);
	}

	/**
	 * Converts the input to make it easier to format for a URL
	 *
	 * @param input
	 * @return
	 */
	public static String formatForFilename(String input)
	{
		if (StringUtils.isNotBlank(input)) {
			input = input.replace(" ", "_");
			input = cleanFileName(input);
		}
		return input;
	}

	/**
	 *
	 * @return
	 */
	public static String uniqueId()
	{
		return UUID.randomUUID().toString();
	}

}
