/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.security.UserRecord;
import edu.usu.sdl.openstorefront.service.manager.model.LdapRecord;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author rfrazier
 */
public class LdapRecordTest
{
	/**
	 * Test of isObjectsDifferent method, of class EntityUtil.
	 */
	@Test
	public void testIsObjectsDifferent()
	{
		System.out.println("isObjectsDifferent");

		Component component = new Component();
		component.setName("Test");

		boolean consumeFieldsOnly = true;
		boolean expResult = false;
		boolean result = EntityUtil.isObjectsDifferent(component, component, consumeFieldsOnly);
		assertEquals(expResult, result);

		Component component2 = new Component();
		component2.setName("Test_2");
		expResult = true;
		result = EntityUtil.isObjectsDifferent(component, component2, consumeFieldsOnly);
		assertEquals(expResult, result);
		
		UserRecord userRecord1 = new UserRecord();
		userRecord1.setFirstName("Bob");
		userRecord1.setLastName("Test");
		userRecord1.setOrganization("Test");
		userRecord1.setEmail("Check@test.com");
		userRecord1.setPhone("555-555-5555");
		userRecord1.setUsername("Check");
		
		UserRecord userRecord2 = new LdapRecord();
		userRecord2.setFirstName("Bob");
		userRecord2.setLastName("Test");
		userRecord2.setOrganization("Test");
		userRecord2.setEmail("newemail@test.com");
		userRecord2.setPhone("555-555-5555");
		userRecord2.setUsername("Check");		
		
		expResult = true;
		result = EntityUtil.isObjectsDifferent(userRecord1, userRecord2, false);
		assertEquals(expResult, result);
		
		// test the reverse
		result = EntityUtil.isObjectsDifferent(userRecord2, userRecord1, false);
		assertEquals(expResult, result);
	}
}
