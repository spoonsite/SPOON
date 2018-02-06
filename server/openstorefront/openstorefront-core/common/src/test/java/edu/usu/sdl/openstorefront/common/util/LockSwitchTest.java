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
package edu.usu.sdl.openstorefront.common.util;

import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class LockSwitchTest
{

	private static final Logger LOG = Logger.getLogger(LockSwitchTest.class.getName());

	public LockSwitchTest()
	{
	}

	/**
	 * Expected: to be true when tripped
	 */
	@Test
	public void testIsSwitched()
	{
		LOG.info("isSwitched");

		LOG.info("Secenario 1: Initial State");
		LockSwitch instance = new LockSwitch();
		boolean expResult = false;
		boolean result = instance.isSwitched();
		assertEquals(expResult, result);

		LOG.info("Secenario 2: After trip State");
		instance = new LockSwitch();
		expResult = true;
		instance.setSwitched(true);
		result = instance.isSwitched();
		assertEquals(expResult, result);

	}

}
