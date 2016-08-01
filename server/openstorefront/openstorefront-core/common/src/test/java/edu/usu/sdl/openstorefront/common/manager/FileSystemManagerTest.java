/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.common.manager;

import static edu.usu.sdl.openstorefront.common.manager.FileSystemManager.copy;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ccummings
 */
public class FileSystemManagerTest
{

	public FileSystemManagerTest()
	{
	}

	@BeforeClass
	public static void setUpClass()
	{
	}

	@AfterClass
	public static void tearDownClass()
	{
	}

	@Before
	public void setUp()
	{
	}

	@After
	public void tearDown()
	{
	}

	/**
	 * Test of copy method, of class FileSystemManager.
	 */
	@Test
	public void testCopy() throws IOException
	{
		System.out.println("testCopy: ");

		String fileName = "readme.txt";
		byte[] b = fileName.getBytes();
		ByteArrayInputStream bin = new ByteArrayInputStream(b);
		ByteArrayOutputStream bout = new ByteArrayOutputStream(b.length);
		long result = copy(bin, bout);
		assertEquals(b.length, result);

		String testUrl = "http://espn.go.com/";
		b = testUrl.getBytes();
		bin = new ByteArrayInputStream(b);
		bout = new ByteArrayOutputStream(b.length);
		result = copy(bin, bout);
		assertEquals(b.length, result);

		try {
			bin = new ByteArrayInputStream(b);
			result = copy(bin, null);
			fail("Outputstream object cannot be null");
		} catch (NullPointerException e) {

		}
		try {
			result = copy(null, bout);
			fail("Inputstream object cannot be null");
		} catch (NullPointerException e) {

		}
	}
}
