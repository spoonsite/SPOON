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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static edu.usu.sdl.openstorefront.common.util.StringProcessor.cleanEntityKey;
import static edu.usu.sdl.openstorefront.common.util.StringProcessor.cleanFileName;
import static edu.usu.sdl.openstorefront.common.util.StringProcessor.enclose;
import static edu.usu.sdl.openstorefront.common.util.StringProcessor.encodeWebKey;
import static edu.usu.sdl.openstorefront.common.util.StringProcessor.getFileExtension;
import static edu.usu.sdl.openstorefront.common.util.StringProcessor.puralize;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class StringProcessorTest
{

	public StringProcessorTest()
	{
	}

	/**
	 * Test of defaultObjectMapper method, of class StringProcessor.
	 */
	@Test
	public void testDefaultObjectMapper()
	{
		System.out.println("defaultObjectMapper");

		ObjectMapper result = StringProcessor.defaultObjectMapper();
		assertNotNull(result);
	}

	/**
	 * Test of getResourceNameFromUrl method, of class StringProcessor.
	 */
	@Test
	public void testGetResourceNameFromUrl()
	{
		System.out.println("getResourceNameFromUrl");
		String url = "";
		String expResult = "";
		String result = StringProcessor.getResourceNameFromUrl(url);
		assertEquals(expResult, result);

		url = "null";
		expResult = "null";
		result = StringProcessor.getResourceNameFromUrl(url);
		assertEquals(expResult, result);

		url = "http:/google.com/test";
		expResult = "test";
		result = StringProcessor.getResourceNameFromUrl(url);
		assertEquals(expResult, result);

		url = "http:/google.com/test?query=p";
		expResult = "test?query=p";
		result = StringProcessor.getResourceNameFromUrl(url);
		assertEquals(expResult, result);

		url = "query=p";
		expResult = "query=p";
		result = StringProcessor.getResourceNameFromUrl(url);
		assertEquals(expResult, result);
	}

	/**
	 * Test of extractUrls method, of class StringProcessor.
	 */
	@Test
	public void testExtractUrls()
	{
		System.out.println("extractUrls");
		String text = "";
		List<String> result = StringProcessor.extractUrls(text);
		if (result.size() > 0) {
			fail("Unexpected size from results. " + result.size());
		}

		text = "null";
		result = StringProcessor.extractUrls(text);
		System.out.println(Arrays.toString(result.toArray(new String[0])));

		text = "a http://google.com";
		result = StringProcessor.extractUrls(text);
		System.out.println(Arrays.toString(result.toArray(new String[0])));

		text = "a http://google.com stuff  http://yahoo.com";
		result = StringProcessor.extractUrls(text);
		System.out.println(Arrays.toString(result.toArray(new String[0])));

		text = "first http://google.com second url http://espn.com third . url http://finance.yahoo.com";
		result = StringProcessor.extractUrls(text);
		System.out.println(Arrays.toString(result.toArray(new String[0])));
	}

	/**
	 * Test of stripeExtendedChars method, of class StringProcessor.
	 */
	@Test
	public void testStripeExtendedChars()
	{
		System.out.println("stripeExtendedChars");
		String data = "A";
		String expResult = "A";
		String result = StringProcessor.stripeExtendedChars(data);
		assertEquals(expResult, result);

		data = "A█";
		expResult = "A ";
		result = StringProcessor.stripeExtendedChars(data);
		assertEquals(expResult, result);

	}

	/**
	 * Test of createHrefUrls method, of class StringProcessor.
	 */
	@Test
	public void testCreateHrefUrls()
	{
		System.out.println("createHrefUrls");
		String text = "http://google.com";
		boolean showFullURL = false;
		String expResult = "<a href='http://google.com' title='http://google.com' target='_blank'> google.com</a>";
		String result = StringProcessor.createHrefUrls(text, showFullURL);
		assertEquals(expResult, result);

		text = "https://facebook.com/";
		expResult = "<a href='https://facebook.com/' title='https://facebook.com/' target='_blank'> </a>";
		result = StringProcessor.createHrefUrls(text, showFullURL);
		assertEquals(expResult, result);
	}

	/**
	 * Test of stripeFieldJSON method, of class StringProcessor.
	 *
	 * @throws com.fasterxml.jackson.core.JsonProcessingException
	 */
	@Test
	public void testStripeFieldJSON() throws JsonProcessingException
	{
		System.out.println("stripeFieldJSON");
		TestModel userTypeCode = new TestModel();
		userTypeCode.setCode("Test");
		userTypeCode.setDescription("Test2");

		Set<String> fieldsToKeep = new HashSet<>();
		fieldsToKeep.add("code");
		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();
		String result = StringProcessor.stripeFieldJSON(objectMapper.writeValueAsString(userTypeCode), fieldsToKeep);
		System.out.println(result);
		if (result.contains("description")) {
			fail("Description shouldn't be in reults");
		}
	}

	/**
	 * Test of printObject method, of class StringProcessor.
	 */
	@Test
	public void testPrintObject()
	{
		System.out.println("printObject");
		TestModel userTypeCode = new TestModel();
		userTypeCode.setCode("Test");
		userTypeCode.setDescription("Test2");
		String result = StringProcessor.printObject(userTypeCode);
		System.out.println("PrintObject result: " + result);

	}

	/**
	 * Test of ellipseString method, of class StringProcessor.
	 */
	@Test
	public void testEllipseString()
	{
		System.out.println("eclipseString");
		String data = "This is a test string that is really long.  Does it all show?";
		String result = StringProcessor.ellipseString(data, 20);
		System.out.println(result);
		if (result.endsWith("...") == false) {
			fail("It didn't eclispe the text");
		}
	}

	/**
	 * Test of blankIfNull method, of class StringProcessor.
	 */
	@Test
	public void testBlankIfNull_Object()
	{
		System.out.println("blankIfNull");
		Object text = null;
		String expResult = "";
		String result = StringProcessor.blankIfNull(text);
		assertEquals(expResult, result);

		Object text2 = "not_null";
		result = StringProcessor.blankIfNull(text2);
		expResult = "not_null";
		assertEquals(expResult, result);
	}

	/**
	 * Test of stripHtml method, of class StringProcessor.
	 */
	@Test
	public void testStripHtml()
	{
		System.out.println("stripHtml");
		String text = "<a href=\"http://clavin.bericotechnologies.com\" rel=\"nofollow\"> http://clavin.bericotechnologies.com</a>";
		String expResult = "http://clavin.bericotechnologies.com";
		String result = StringProcessor.stripHtml(text);
		System.out.println(result);
		assertEquals(expResult, result);

		text = "<a href =\"http://www.apachecorp.com/About_Apache/index.aspx\" "
				+ "rel=\"nofollow\"> http://www.apachecorp.com/About_Apache/index.aspx";
		expResult = "http://www.apachecorp.com/About_Apache/index.aspx";
		result = StringProcessor.stripHtml(text);
		System.out.println("StripHtml with long url and missing closing anchor: " + result);
		assertEquals(expResult, result);

	}

	/**
	 * Test of archtecureCodeToDecimal method, of class StringProcessor.
	 */
	@Test
	public void testArchtecureCodeToDecimal()
	{
		System.out.println("archtecureCodeToDecimal");
		String code = "1.2.1.1";
		BigDecimal result = StringProcessor.archtecureCodeToDecimal(code);
		System.out.println(result.toPlainString());

		code = "1";
		result = StringProcessor.archtecureCodeToDecimal(code);
		System.out.println(result.toPlainString());

		code = "1.";
		result = StringProcessor.archtecureCodeToDecimal(code);
		System.out.println(result.toPlainString());

		code = "1.2";
		result = StringProcessor.archtecureCodeToDecimal(code);
		System.out.println(result.toPlainString());

		code = "";
		result = StringProcessor.archtecureCodeToDecimal(code);
		System.out.println(result.toPlainString());

		code = null;
		result = StringProcessor.archtecureCodeToDecimal(code);
		System.out.println(result.toPlainString());

	}

	@Test
	public void testEnclose()
	{
		String test = "";
		String result = enclose(test, "(", ")");
		System.out.print("Enclose results: " + result + " , ");

		test = null;
		result = enclose(test, "(", ")");
		System.out.print(result + " , ");

		test = "testing...";
		result = enclose(test, "(", ")");
		System.out.println(result);
	}

	@Test
	public void testEncodeWebKey()
	{
		String test1 = null;

		try {
			String result = encodeWebKey(test1);
			fail("Returned value unexpected");
		} catch (NullPointerException e) {

		}

		String test2 = "https://google.com/# q=&computer%science";
		String result = encodeWebKey(test2);
		String expResult = "https%3A%2F%2Fgoogle.com%2F%23+q%3D%26computer%25science";
		System.out.println(result);
		assertEquals(result, expResult);

		String test3 = "";
		result = encodeWebKey(test3);
		System.out.println("The result: " + result);
		expResult = "";
		assertEquals(result, expResult);

	}

	@Test
	public void testCleanEntityKey()
	{
		String testKey = "U(n)i=q+u~e K*e@y!";
		String result = cleanEntityKey(testKey);
		String expResult = "UniqueKey";
		System.out.println(result);
		assertEquals(result, expResult);
	}

	@Test
	public void testGetFileExtension()
	{
		System.out.println("File Extension Test:");
		String testFile = "testImg.jpg";
		String result = getFileExtension(testFile);
		String expResult = "jpg";
		assertEquals(result, expResult);

		testFile = null;
		result = getFileExtension(testFile);
		expResult = null;
		assertEquals(result, expResult);

		testFile = "";
		result = getFileExtension(testFile);
		expResult = null;
		assertEquals(result, expResult);
	}

	@Test
	public void testCleanFileName()
	{
		System.out.println("testCleanFileName: ");
		String testFileName = null;
		String result = cleanFileName(testFileName);
		String expResult = null;
		assertEquals(result, expResult);

		testFileName = "";
		result = cleanFileName(testFileName);
		expResult = "";
		assertEquals(result, expResult);

		testFileName = "Second*Try\".Rpt";
		result = cleanFileName(testFileName);
		expResult = "SecondTry.Rpt";
		assertEquals(result, expResult);

		testFileName = "My?" + (char) (7) + "Urgent|Memo/.txt";
		result = cleanFileName(testFileName);
		expResult = "MyUrgentMemo.txt";
		assertEquals(result, expResult);

		testFileName = "my|Back" + (char) (28) + "Flip.gif";
		result = cleanFileName(testFileName);
		expResult = "myBackFlip.gif";
		assertEquals(result, expResult);
	}

	@Test
//	puralize(int size, String nonPuralForm, String puralForm)
//	size refers to how many objects
	public void testPuralize()
	{
		System.out.println("testPuralize");
		int sizeOne = 1;
		int sizeTwo = 2;

		String test = null;
		String result = puralize(sizeOne, test, null);
		String expResult = null;
		assertEquals(result, expResult);

		result = puralize(sizeTwo, test, null);
		expResult = null;
		assertEquals(result, expResult);

		test = "";
		result = puralize(sizeOne, test, null);
		expResult = "";
		assertEquals(result, expResult);

		result = puralize(sizeTwo, test, null);
		expResult = "";
		assertEquals(result, expResult);

		test = "robot";
		result = puralize(sizeOne, test, null);
		expResult = "robot";
		assertEquals(result, expResult);

		result = puralize(sizeTwo, test, null);
		expResult = "robots";
		assertEquals(result, expResult);
	}

//		public static String getHexFromBytes(byte[] bytes)
	@Test
	public void testGetHexFromBytes() throws NoSuchAlgorithmException
	{
		System.out.println("testGetHexFromBytes:");
		String imgUrl = "http://fm.cnbc.com/applications/cnbc.com/resources/img/editorial/2015/12/23/103263155-GettyImages-450831380.530x298.jpg?v=1450888968";
		String hashUrl = StringProcessor.getHexFromBytes(MessageDigest.getInstance("SHA-1").digest(imgUrl.getBytes()));
		String expHashUrl = "bdea7192ed2dfcd01ad525d0ab8ce2b48ab16c95";
		assertEquals(hashUrl, expHashUrl);

		imgUrl = "https://www.airforce.com/version/1468393507/data/page/satellites/howtojoin01.jpg";
		hashUrl = StringProcessor.getHexFromBytes(MessageDigest.getInstance("SHA-1").digest(imgUrl.getBytes()));
		expHashUrl = "1b53f7dd4d03adae30fcad1fc19e7301fafad174";
		assertEquals(hashUrl, expHashUrl);
	}

	@Test
	public void testEmail()
	{
		List<String> emails = Arrays.asList(
				"test.me@sld.xgh.eud",
				"d.t@v-a.com",
				"d.t+1@v-a.com",
				"d.t?@v-a.com",
				"r@localhost",
				"d.t@v-a.com",
				"a",
				"@"
		);

		for (String email : emails) {
			System.out.print(email);
			System.out.print(" - ");
			System.out.print(StringProcessor.isEmail(email));
			System.out.print("\n");
		}
	}

	/**
	 * Test of getJustFileName method, of class StringProcessor.
	 */
	@Test
	public void testGetJustFileName()
	{
		System.out.println("getJustFileName");
		String originalFilename = "";
		String expResult = "";
		String result = StringProcessor.getJustFileName(originalFilename);
		assertEquals(expResult, result);

		expResult = "Test.png";
		result = StringProcessor.getJustFileName(".../akfjaklf/askjdkl/Test.png");
		assertEquals(expResult, result);

		result = StringProcessor.getJustFileName("Test.png");
		assertEquals(expResult, result);

		result = StringProcessor.getJustFileName("\\hera\\aaa\\akfjaklf\\Test.png");
		assertEquals(expResult, result);

		result = StringProcessor.getJustFileName("\\hera/aaa\\akfjaklf/Test.png");
		assertEquals(expResult, result);

		expResult = "Baker_file.pdf";
		result = StringProcessor.getJustFileName("\\Projects\\Baker_Project\\Baker_file.pdf");
		assertEquals(expResult, result);
	}

	/**
	 * Test of createHrefUrls method, of class StringProcessor.
	 */
	@Test
	public void testCreateHrefUrls_String()
	{
		System.out.println("createHrefUrls");
		String text = "https://facebook.com/";
		String expResult = "<a href='https://facebook.com/' title='https://facebook.com/' target='_blank'> </a>";
		String result = StringProcessor.createHrefUrls(text);
		assertEquals(expResult, result);
	}

	/**
	 * Test of blankIfNull method, of class StringProcessor.
	 */
	@Test
	public void testBlankIfNull_String()
	{
		System.out.println("blankIfNull");
		String text = "";
		String expResult = "";
		String result = StringProcessor.blankIfNull(text);
		assertEquals(expResult, result);

		text = null;
		expResult = "";
		result = StringProcessor.blankIfNull(text);
		assertEquals(expResult, result);

	}

	/**
	 * Test of nullIfBlank method, of class StringProcessor.
	 */
	@Test
	public void testNullIfBlank()
	{
		System.out.println("nullIfBlank");
		String text = "";
		String expResult = null;
		String result = StringProcessor.nullIfBlank(text);
		assertEquals(expResult, result);

		text = "a";
		expResult = "a";
		result = StringProcessor.nullIfBlank(text);
		assertEquals(expResult, result);

	}

	/**
	 * Test of urlEncode method, of class StringProcessor.
	 */
	@Test
	public void testUrlEncode()
	{
		System.out.println("urlEncode");
		String value = "";
		String expResult = "";
		String result = StringProcessor.urlEncode(value);
		assertEquals(expResult, result);

		value = " a";
		expResult = "+a";
		result = StringProcessor.urlEncode(value);
		assertEquals(expResult, result);

	}

	/**
	 * Test of urlDecode method, of class StringProcessor.
	 */
	@Test
	public void testUrlDecode()
	{
		System.out.println("urlDecode");
		String value = "";
		String expResult = "";
		String result = StringProcessor.urlDecode(value);
		assertEquals(expResult, result);

		value = "http://google.com/+a";
		expResult = "http://google.com/ a";
		result = StringProcessor.urlDecode(value);
		assertEquals(expResult, result);

	}

	/**
	 * Test of parseStackTraceHtml method, of class StringProcessor.
	 */
	@Test
	public void testParseStackTraceHtml()
	{
		System.out.println("parseStackTraceHtml");
		Throwable throwable = new RuntimeException("Test");
		String expResult = "<b>Message:</b> <span style='color: red;'><b>Test";
		String result = StringProcessor.parseStackTraceHtml(throwable);

		if (!result.startsWith(expResult)) {
			fail("Should start with message");
		}

	}

//	/**
//	 * Test of enclose method, of class StringProcessor.
//	 */
//	@Test
//	public void testEnclose_String()
//	{
//		System.out.println("enclose");
//		String s = "";
//		String expResult = "";
//		String result = StringProcessor.enclose(s);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of enclose method, of class StringProcessor.
//	 */
//	@Test
//	public void testEnclose_String_String()
//	{
//		System.out.println("enclose");
//		String s = "";
//		String enclose = "";
//		String expResult = "";
//		String result = StringProcessor.enclose(s, enclose);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//	/**
//	 * Test of enclose method, of class StringProcessor.
//	 */
//	@Test
//	public void testEnclose_3args()
//	{
//		System.out.println("enclose");
//		String s = "";
//		String encloseStart = "";
//		String encloseEnd = "";
//		String expResult = "";
//		String result = StringProcessor.enclose(s, encloseStart, encloseEnd);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
	/**
	 * Test of decodeHexCharEscapes method, of class StringProcessor.
	 */
	@Test
	public void testDecodeHexCharEscapes()
	{
		System.out.println("decodeHexCharEscapes");
		String input = "x0020";
		String expResult = " ";
		String result = StringProcessor.decodeHexCharEscapes(input);
		assertEquals(expResult, result);
	}

//	/**
//	 * Test of formatForFilename method, of class StringProcessor.
//	 */
//	@Test
//	public void testFormatForFilename()
//	{
//		System.out.println("formatForFilename");
//		String input = "";
//		String expResult = "";
//		String result = StringProcessor.formatForFilename(input);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
	/**
	 * Expected: To not throw error
	 */
	@Test
	public void testUniqueId()
	{
		System.out.println("uniqueId");
		String result = StringProcessor.uniqueId();
		System.out.println(result);
	}

	/**
	 * Test of truncateHTML method, of class StringProcessor.
	 */
	@Test
	public void testTruncateHTML()
	{
		System.out.println("truncateHTML");
		String html = "<b>test</b><br><br>abcdssa<b>";
		int maxCharCount = 10;
		String expResult = "<b>test</b><br><br><b>";
		String result = StringProcessor.truncateHTML(html, maxCharCount);
		assertEquals(expResult, result);
	}

	/**
	 * Test of splitURLQuery method, of class StringProcessor.
	 */
	@Test
	public void testSplitURLQuery()
	{
		System.out.println("splitURLQuery");
		String query = "a=b&b=c";
		Map<String, List<String>> result = StringProcessor.splitURLQuery(query);

		if (!result.containsKey("a")) {
			fail("Should contain a");
		}
	}

	@Test
	public void testEscapeHtmlQuotes()
	{
		assertEquals("a", StringProcessor.escapeHtmlQuotes("a"));
		assertEquals("a&#39;t", StringProcessor.escapeHtmlQuotes("a't"));
		assertEquals("a&#34;t", StringProcessor.escapeHtmlQuotes("a\"t"));
	}

}
