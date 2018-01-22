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
package edu.usu.sdl.openstorefront.core.security;

import edu.usu.sdl.openstorefront.security.SecurityUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests Security Util
 *
 * @author dshurtleff
 */
public class SecurityUtilTest
{

	@Test
	public void testSystemUser()
	{
		System.out.println("Start System user: " + SecurityUtil.isSystemUser());
		Assert.assertEquals(false, SecurityUtil.isSystemUser());
		new Thread(() -> {
			SecurityUtil.initSystemUser();
			System.out.println("Set System user: " + SecurityUtil.isSystemUser());
			Assert.assertEquals(true, SecurityUtil.isSystemUser());
		}).start();

		System.out.println("After Set System user: " + SecurityUtil.isSystemUser());
		Assert.assertEquals(false, SecurityUtil.isSystemUser());
		new Thread(() -> {
			System.out.println("Not set System user: " + SecurityUtil.isSystemUser());
			Assert.assertEquals(false, SecurityUtil.isSystemUser());
		}).start();

	}

}
