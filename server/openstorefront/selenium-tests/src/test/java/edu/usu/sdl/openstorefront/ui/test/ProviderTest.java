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
package edu.usu.sdl.openstorefront.ui.test;
import edu.usu.sdl.openstorefront.selenium.provider.ComponentProvider;
import edu.usu.sdl.openstorefront.selenium.util.CleanupServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ccummings
 */
public class ProviderTest
{
	private CleanupServiceImpl cleanupService;
	
	@Before
	public void setup()
	{
		cleanupService = new CleanupServiceImpl();
	}
	
	@Test
	public void cleanupComponentProvider()
	{
		cleanupService.Register(ComponentProvider.class, "SomeId");
		Assert.assertTrue(!cleanupService.getProviders().isEmpty());
		cleanupService.cleanup();
		Assert.assertTrue(cleanupService.getProviders().isEmpty());
	}
}
