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

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
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
	public void testOsgiManager()
	{
		System.out.println("OsgiManager/Felix");
		PropertiesManager mockPropertiesManager = Mockito.mock(PropertiesManager.class);
		Mockito.when(mockPropertiesManager.getModuleVersion()).thenReturn("3.0");

		OsgiManager instance = OsgiManager.getInstance(mockPropertiesManager);
		instance.initialize();
		Assert.assertNotNull(instance.getFelix());
		
		// TODO: Need to refactor class to verify successful config of Felix
		Service service = ServiceProxyFactory.getServiceProxy();
		Assert.assertNotNull(service);

		Assert.assertEquals(Bundle.ACTIVE, instance.getFelix().getState());
		instance.shutdown();
		Assert.assertNotEquals(Bundle.ACTIVE, instance.getFelix().getState());
		
		boolean result = instance.isStarted();
		Assert.assertFalse(result);
	}

}
