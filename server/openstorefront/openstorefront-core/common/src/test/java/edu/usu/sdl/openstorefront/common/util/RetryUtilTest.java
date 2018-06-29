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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class RetryUtilTest
{

	private static final Logger LOG = Logger.getLogger(RetryUtilTest.class.getName());

	private int retriedCount;

	public RetryUtilTest()
	{
	}

	/**
	 * Test of retryAction method, of class RetryUtil.
	 */
	@Test
	public void testRetryAction()
	{
		LOG.info("retryAction");

		LOG.info("Scenario 1: null operation");
		try {
			RetryOperation operation = null;
			RetryUtil.retryAction(10, operation);
			Assert.fail("Should notsucceed");
		} catch (NullPointerException ex) {
		}

		LOG.info("Scenario 2: operation 3 retries success");
		int maxRetries = 3;
		RetryOperation operation = () -> {
			retriedCount++;
			if (retriedCount < maxRetries) {
				throw new RuntimeException("Fail");
			}
		};
		RetryUtil.retryAction(maxRetries, operation);

		LOG.info("Scenario 3: operation 3 retries fail");
		operation = () -> {
			throw new RuntimeException("Fail");
		};
		try {
			RetryUtil.retryAction(maxRetries, operation);
			Assert.fail("Should not succeed");
		} catch (OpenStorefrontRuntimeException ex) {
			System.out.println(ex.getErrorTypeCode());
			System.out.println(ex.getLocalizedMessage());
			System.out.println(ex.getPotentialResolution());
		}
	}

}
