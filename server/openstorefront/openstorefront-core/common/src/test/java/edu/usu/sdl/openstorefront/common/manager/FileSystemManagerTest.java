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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
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
 * @author dshurtleff
 */
public class FileSystemManagerTest
{

	private static final Logger LOG = Logger.getLogger(FileSystemManagerTest.class.getName());

	public FileSystemManagerTest()
	{
	}

	private String testDir = FileSystemManager.SYSTEM_TEMP_DIR + "osf-" + StringProcessor.uniqueId();
	private FileSystemManager testFileSystemManager;
	private StringBuilder handlerResults = new StringBuilder();

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
		testFileSystemManager = FileSystemManager.getInstance();
		testFileSystemManager.setBaseDirectory(testDir);
	}

	@After
	public void tearDown()
	{
		LOG.info("Clean up temp data folder");
		File file = new File(testDir);
		if (file.exists()) {
			try {
				FileUtils.deleteDirectory(file);
			} catch (IOException ex) {
				LOG.warning("Unable to delete temp directory");
			}
		}
	}

	/**
	 * Test of copy method, of class FileSystemManager.
	 */
	@Test
	public void testCopy() throws IOException
	{
		LOG.info("testCopy: ");

		LOG.info("Scenario 1: Copy everything filled out");
		String fileName = "readme.txt";
		byte[] b = fileName.getBytes();
		ByteArrayInputStream bin = new ByteArrayInputStream(b);
		ByteArrayOutputStream bout = new ByteArrayOutputStream(b.length);
		long result = testFileSystemManager.copy(bin, bout);
		assertEquals(b.length, result);

		LOG.info("Scenario 2: Null Output");
		try {
			bin = new ByteArrayInputStream(b);
			result = testFileSystemManager.copy(bin, null);
			fail("Outputstream object cannot be null");
		} catch (NullPointerException e) {
		}

		LOG.info("Scenario 3: Null Input");
		try {
			result = testFileSystemManager.copy(null, bout);
			fail("Inputstream object cannot be null");
		} catch (NullPointerException e) {
		}

	}

	/**
	 * Expected: To show directories on the main directory. For the test we can
	 * use the inited directories.
	 */
	@Test
	public void testGetTopLevelDirectories()
	{
		LOG.info("getTopLevelDirectories");

		Set<String> exclude = null;

		LOG.info("Scenario 1: Default no exclude");
		List<String> expResult = Arrays.asList(
				testFileSystemManager.getBaseDirectory() + FileSystemManager.IMPORT_DIR.replace("/", File.separator),
				testFileSystemManager.getBaseDirectory() + FileSystemManager.MAIN_PERM_DIR.replace("/", File.separator)
		);
		testFileSystemManager.initialize();

		List<String> result = testFileSystemManager.getTopLevelDirectories(exclude);
		assertEquals(expResult, result);

		exclude = new HashSet<>();
		exclude.add(testFileSystemManager.getBaseDirectory() + FileSystemManager.IMPORT_DIR.replace("/", File.separator));

		LOG.info("Scenario 2: Test Exclude");
		expResult = Arrays.asList(
				testFileSystemManager.getBaseDirectory() + FileSystemManager.MAIN_PERM_DIR.replace("/", File.separator)
		);

		result = testFileSystemManager.getTopLevelDirectories(exclude);
		assertEquals(expResult, result);
	}

	/**
	 *
	 * Expected: The directory request will be created under the base path
	 */
	@Test
	public void testGetDir()
	{
		LOG.info("getDir");

		LOG.info("Scenario 1: Create Directory");
		String directory = "/unittest";
		File expResult = new File(testFileSystemManager.getBaseDirectory() + directory);
		File result = testFileSystemManager.getDir(directory);
		assertEquals(expResult, result);

		if (!result.exists()) {
			fail("The directory should exist");
		}

		LOG.info("Scenario 2: Null Input");
		try {
			testFileSystemManager.getDir(null);
			fail("This should throw expection");
		} catch (NullPointerException ex) {
		}

	}

	/**
	 *
	 * Test of getConfig method, of class FileSystemManager.
	 */
	@Test
	public void testGetConfig()
	{
		LOG.info("getConfig");

		LOG.info("Scenario 1: Get the dummy config");
		String configFilename = "unittest-dummy.properties";
		File expResult = new File(testFileSystemManager.getBaseDirectory() + FileSystemManager.CONFIG_DIR.replace("/", File.separator) + "/" + configFilename);
		File result = testFileSystemManager.getConfig(configFilename);
		assertEquals(expResult, result);

		if (!result.exists()) {
			fail("The file should exist");
		}

		LOG.info("Scenario 2: Try to get one that doesn't exist");
		try {
			testFileSystemManager.getConfig(configFilename + "bad");
			fail("This should throw an error");
		} catch (OpenStorefrontRuntimeException ex) {
		}

		LOG.info("Scenario 3: NULL");
		try {
			testFileSystemManager.getDir(null);
			fail("This should throw expection");
		} catch (NullPointerException ex) {
		}
	}

	/**
	 * testGetConfig checks all of the other senarios that this uses.
	 */
	@Test
	public void testGetImportLookup_String()
	{
		LOG.info("getImportLookup");

		LOG.info("Scenario 1: Get the dummy lookup");
		String configFilename = "dummy-lookup.csv";
		File expResult = new File(testFileSystemManager.getBaseDirectory() + FileSystemManager.IMPORT_LOOKUP_DIR.replace("/", File.separator) + "/" + configFilename);
		File result = testFileSystemManager.getImportLookup(configFilename);
		assertEquals(expResult, result);
	}

	/**
	 * Expected: To call the new file handler after create the file If the file
	 * already exist the handler should not be called
	 */
	@Test
	public void testGetImportLookupWithNewFileHandler()
	{

		LOG.info("getImportLookup with hanndlers");

		LOG.info("Scenario 1: New File with handler");

		NewFileHandler newFileHandler = (File newFile) -> {
			LOG.log(Level.INFO, "Handler called with: {0}", newFile.getPath());
			handlerResults.append("Called");
		};

		String configFilename = "dummy-lookup.csv";
		File expResult = new File(testFileSystemManager.getBaseDirectory() + FileSystemManager.IMPORT_LOOKUP_DIR.replace("/", File.separator) + "/" + configFilename);
		File result = testFileSystemManager.getImportLookup(configFilename, newFileHandler);
		assertEquals(expResult, result);

		if (handlerResults.length() == 0) {
			fail("Handler should have been called");
		}

		LOG.info("Scenario 2: existing File with handler");
		handlerResults = new StringBuilder();
		testFileSystemManager.getImportLookup(configFilename, newFileHandler);

		if (handlerResults.length() != 0) {
			fail("Handler should have NOT been called");
		}

	}

	/**
	 * Test of getApplicationResourceFile method, of class FileSystemManager.
	 */
	@Test
	public void testGetApplicationResourceFile()
	{
		LOG.info("getApplicationResourceFile");

		LOG.info("Scenario 1: Exist");
		boolean foundFile = false;
		String resource = "/unittest-dummy.properties";
		InputStream result = testFileSystemManager.getApplicationResourceFile(resource);
		if (result != null) {
			foundFile = true;
		}
		assertEquals(true, foundFile);

		LOG.info("Scenario 2: BAD");
		try {
			testFileSystemManager.getApplicationResourceFile("dummy");
			fail("This should throw missing expection");
		} catch (OpenStorefrontRuntimeException ex) {
		}

		LOG.info("Scenario 3: NULL");
		try {
			testFileSystemManager.getApplicationResourceFile(null);
			fail("This should throw a null expection");
		} catch (NullPointerException ex) {
		}

	}

	/**
	 * Expected: it should create init directories and set to started
	 */
	@Test
	public void testInitialize()
	{
		LOG.info("initialize");
		testFileSystemManager.initialize();

		File expResult = new File(testFileSystemManager.getBaseDirectory() + FileSystemManager.IMPORT_DIR.replace("/", File.separator));
		if (!expResult.exists()) {
			fail("Missing the import directory");
		}

		if (!testFileSystemManager.isStarted()) {
			fail("File Manager should be started");
		}

	}

	/**
	 * Expected: FileSystem should not be started after shutdown
	 */
	@Test
	public void testShutdown()
	{
		LOG.info("shutdown");

		testFileSystemManager.shutdown();

		if (testFileSystemManager.isStarted()) {
			fail("File manager should NOT be started");
		}

	}

}
