/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import java.io.File;
import java.io.IOException;
import java.util.Map;
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
 * @author dshurtleff
 */
public class PropertiesManagerTest
{

	private static final Logger LOG = Logger.getLogger(PropertiesManagerTest.class.getName());

	public PropertiesManagerTest()
	{
	}

	private String testDir = FileSystemManager.SYSTEM_TEMP_DIR + "osf-" + StringProcessor.uniqueId();
	private FileSystemManager testFileSystemManager;
	private PropertiesManager testPropertiesManager;

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

		//reset every test
		testPropertiesManager = new PropertiesManager(testFileSystemManager);

		testPropertiesManager.setVersionFile("/unittest-version.properties");
		testPropertiesManager.setPropertiesFile("unittest.properties");

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
				LOG.log(Level.WARNING, "Unable to delete temp directory", ex);
			}
		}
	}

	/**
	 * Expected: Get the dummy version
	 */
	@Test
	public void testGetApplicationVersion()
	{
		LOG.info("getApplicationVersion");

		String expResult = "v5.1 (2025-01-26 9:52:00 MST)";
		String result = testPropertiesManager.getApplicationVersion();
		assertEquals(expResult, result);

	}

	/**
	 * Expected: Get the dummy version
	 */
	@Test
	public void testGetModuleVersion()
	{
		LOG.info("getModuleVersion");

		String expResult = "5.1";
		String result = testPropertiesManager.getModuleVersion();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getValueDefinedDefault method, of class PropertiesManager.
	 */
	@Test
	public void testGetValueDefinedDefault()
	{
		LOG.info("getValueDefinedDefault");

		LOG.info("Scenario 1: Use Defined Default no existing");
		String expResult = "7";
		String result = testPropertiesManager.getValueDefinedDefault(PropertiesManager.KEY_NOTIFICATION_MAX_DAYS);
		assertEquals(expResult, result);

		LOG.info("Scenario 2: Use without Defined Default but existing property");
		result = testPropertiesManager.getValueDefinedDefault("test");
		expResult = "dummy";
		assertEquals(expResult, result);

		LOG.info("Scenario 3: Use without Defined Default no existing property");
		result = testPropertiesManager.getValueDefinedDefault("thisDoesNotExist");
		expResult = null;
		assertEquals(expResult, result);
	}

	/**
	 * Test of getValueDefinedDefault method, of class PropertiesManager.
	 */
	@Test
	public void testGetValueDefinedDefaultWithDefault()
	{
		LOG.info("getValueDefinedDefault *With Default");

		LOG.info("Scenario 1: Use Defined Default");
		String expResult = "7";
		String result = testPropertiesManager.getValueDefinedDefault(PropertiesManager.KEY_NOTIFICATION_MAX_DAYS, "Test");
		assertEquals(expResult, result);

		LOG.info("Scenario 2: Use Defined Default but existing property");
		result = testPropertiesManager.getValueDefinedDefault("test", "junk");
		expResult = "dummy";
		assertEquals(expResult, result);

		LOG.info("Scenario 3: Use without Defined Default");
		result = testPropertiesManager.getValueDefinedDefault("thisDoesNotExist", "hello");
		expResult = "hello";
		assertEquals(expResult, result);
	}

	/**
	 * Test of getValue method, of class PropertiesManager.
	 */
	@Test
	public void testGetValue()
	{
		LOG.info("getValue");

		LOG.info("Scenario 1: get property that is define");

		String expResult = "dummy";
		String result = testPropertiesManager.getValue("test");
		assertEquals(expResult, result);

		LOG.info("Scenario 2: get property that is NOT define");
		expResult = null;
		result = testPropertiesManager.getValue("junk");
		assertEquals(expResult, result);

	}

	/**
	 * Test of removeProperty method, of class PropertiesManager.
	 */
	@Test
	public void testRemoveProperty()
	{
		LOG.info("removeProperty");

		LOG.info("Scenario 1: remove existing key");

		String key = "test";
		testPropertiesManager.removeProperty(key);

		String result = testPropertiesManager.getValue(key);
		assertEquals(null, result);

		LOG.info("Scenario 2: removed key should not be in properties file");
		testPropertiesManager.initialize();
		result = testPropertiesManager.getValue(key);
		assertEquals(null, result);

		LOG.info("Scenario 3: remove key that doesn't exist");

		key = "junk";
		testPropertiesManager.removeProperty(key);

		result = testPropertiesManager.getValue(key);
		assertEquals(null, result);
	}

	/**
	 * Test of getValue method, of class PropertiesManager.
	 */
	@Test
	public void testGetValueWithDefault()
	{
		LOG.info("getValue default");

		LOG.info("Scenario 1: get property that is define");

		String expResult = "dummy";
		String result = testPropertiesManager.getValue("test", "junk");
		assertEquals(expResult, result);

		LOG.info("Scenario 2: get property that is NOT define");
		expResult = "ok";
		result = testPropertiesManager.getValue("junk", "ok");
		assertEquals(expResult, result);
	}

	/**
	 * Test of setProperty method, of class PropertiesManager.
	 */
	@Test
	public void testSetProperty()
	{
		LOG.info("setProperty");

		LOG.info("Scenario 1: Update existing key");

		String key = "test";
		testPropertiesManager.setProperty(key, "new");

		String result = testPropertiesManager.getValue(key);
		assertEquals("new", result);

		LOG.info("Scenario 2: Update existing key");
		String newKey = "newtest";
		testPropertiesManager.setProperty(newKey, "apple");

		result = testPropertiesManager.getValue(newKey);
		assertEquals("apple", result);

		LOG.info("Scenario 3: Updated key should be in properties file");
		testPropertiesManager.initialize();
		result = testPropertiesManager.getValue(key);
		assertEquals("new", result);

		LOG.info("Scenario 4: Added key should be in properties file");
		result = testPropertiesManager.getValue(newKey);
		assertEquals("apple", result);
	}

	/**
	 * Test of getAllProperties method, of class PropertiesManager.
	 */
	@Test
	public void testGetAllProperties()
	{
		LOG.info("getAllProperties");

		Map<String, String> result = testPropertiesManager.getAllProperties();

		if (!result.containsKey("test")) {
			fail("The test key should be in the map");
		}
	}

	/**
	 * Expect: To get the default value with machine specific info
	 */
	@Test
	public void testGetNodeName()
	{
		LOG.info("getNodeName");

		String expResult = PropertiesManager.DEFAULT_NODE_NAME;
		String result = testPropertiesManager.getNodeName();

		if (!result.startsWith(expResult)) {
			fail("Should start with default node name");
		}
	}

	/**
	 * Test of initialize method, of class PropertiesManager.
	 */
	@Test
	public void testInitialize()
	{
		LOG.info("initialize");
		testPropertiesManager.initialize();

		LOG.info("Scenario 1: Check Defined Default should be setup");
		String expResult = "7";
		String result = testPropertiesManager.getValueDefinedDefault(PropertiesManager.KEY_NOTIFICATION_MAX_DAYS);
		assertEquals(expResult, result);

		LOG.info("Scenario 2: Check a property from file");
		result = testPropertiesManager.getValueDefinedDefault("test");
		expResult = "dummy";
		assertEquals(expResult, result);

		LOG.info("Scenario 3: Should be started");
		if (!testPropertiesManager.isStarted()) {
			fail("Property Manager should be started");
		}
	}

	/**
	 * Test of shutdown method, of class PropertiesManager.
	 */
	@Test
	public void testShutdown()
	{
		LOG.info("shutdown");

		testPropertiesManager.shutdown();

		if (testPropertiesManager.isStarted()) {
			fail("Properties manager should NOT be started");
		}
	}

}
