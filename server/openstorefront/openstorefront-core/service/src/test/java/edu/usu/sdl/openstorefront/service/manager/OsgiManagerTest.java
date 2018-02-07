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
package edu.usu.sdl.openstorefront.service.manager;

import org.junit.Assert;
import org.junit.Test;
import org.osgi.framework.Bundle;

/**
 *
 * @author ccummings
 */
public class OsgiManagerTest
{

	public OsgiManagerTest()
	{
	}

	/**
	 * Test of init and cleanup methods, of class OsgiManager.
	 */
	@Test
	public void testInit()
	{
		System.out.println("init");
		OsgiManager instance = OsgiManager.getInstance();

		instance.init();
		Assert.assertNotNull(instance.getFelix());

		Assert.assertEquals(Bundle.ACTIVE, instance.getFelix().getState());
		instance.cleanup();
		Assert.assertNotEquals(Bundle.ACTIVE, instance.getFelix().getState());
	}

	/**
	 * Test of isStarted method, of class OsgiManager.
	 */
	@Test
	public void testIsStarted()
	{
		System.out.println("isStarted");
		OsgiManager instance = OsgiManager.getInstance();
		boolean expResult = false;
		boolean result = instance.isStarted();
		Assert.assertEquals(expResult, result);

		instance.initialize();
		result = instance.isStarted();

		Assert.assertTrue(result);
		// TODO review the generated test code and remove the default call to fail.
		instance.shutdown();

		result = instance.isStarted();
		Assert.assertFalse(result);
	}

}
