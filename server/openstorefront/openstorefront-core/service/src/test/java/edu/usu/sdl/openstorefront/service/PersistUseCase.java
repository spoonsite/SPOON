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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.service.manager.OrientDBManager;
import edu.usu.sdl.openstorefront.service.manager.MemoryDBManager;
import edu.usu.sdl.openstorefront.service.testModels.TestChild;
import edu.usu.sdl.openstorefront.service.testModels.TestParent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author kbair
 */
public class PersistUseCase
{

	private static final Logger LOG = Logger.getLogger(PersistUseCase.class.getName());

	private static final String URL = "memory:test";
	private static String testDir;

	@BeforeClass
	public static void setup()
	{
		if (FileSystemManager.SYSTEM_TEMP_DIR.endsWith(File.separator)) {
			testDir = FileSystemManager.SYSTEM_TEMP_DIR + "osf-" + StringProcessor.uniqueId();
		} else {
			testDir = FileSystemManager.SYSTEM_TEMP_DIR + File.separator + "osf-" + StringProcessor.uniqueId();
		}

		FileSystemManager testFileSystemManager = FileSystemManager.getInstance();
		testFileSystemManager.setBaseDirectory(testDir);

		PropertiesManager testPropertiesManager = PropertiesManager.getInstance(testFileSystemManager);
		testPropertiesManager.setVersionFile("/unittest-version.properties");
		testPropertiesManager.setPropertiesFile("unittest.properties");

		OrientDBManager.getInstance().shutdown();
		String modelPackage = "edu.usu.sdl.openstorefront.service.testModels";
		String configFile = "test-orientdb-server-config.xml";
		OrientDBManager manager = MemoryDBManager.getInstance(URL, modelPackage, configFile, testFileSystemManager, testPropertiesManager);
//		//NOTE: (KB) this will fail if OrientDBManager is alreay initilzed
		Assert.assertEquals(modelPackage, manager.getEntityModelPackage());
		if (PropertiesManager.getInstance().getValue(PropertiesManager.KEY_DB_AT) == null) {
			PropertiesManager.getInstance().setProperty(PropertiesManager.KEY_DB_AT, "pass");
		}
		OrientDBManager.getInstance().initialize();
	}

	@AfterClass
	public static void cleanup()
	{
		OrientDBManager.getInstance().shutdown();

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

	@Test
	public void testPersistNestedObjects()
	{
		//Arrage
		System.out.println("persist - NestedObjects");

		//Act
		OrientPersistenceService service = new OrientPersistenceService(OrientDBManager.getInstance());
		TestParent p = new TestParent();
		p.setParentId(service.generateId());
		p.setChild(new TestChild());
		p.getChild().setChildId(service.generateId());
		p.getChild().setValue("This is a Test");
		TestParent result = service.persist(p);

		//Assert
	}

	@Test
	public void testPersistSharedObjects()
	{

		//Arrage
		System.out.println("persist - SharedObjects");

		//Act
		OrientPersistenceService service = new OrientPersistenceService(OrientDBManager.getInstance());
		service.begin();

		TestParent p1 = new TestParent();
		p1.setChild(new TestChild());
		p1.getChild().setValue("This is a Test");
		p1 = service.persist(p1);

		TestParent p2 = new TestParent();
		p2.setChild(p1.getChild());

		service.persist(p2);
		service.commit();
		//Assert
		Assert.assertEquals(p1.getChild().getChildId(), p2.getChild().getChildId());
	}
}
