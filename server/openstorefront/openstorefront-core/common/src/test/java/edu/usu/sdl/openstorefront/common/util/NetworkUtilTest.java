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
import javax.servlet.http.HttpServletRequest;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author dshurtleff
 */
public class NetworkUtilTest
{

	private static final Logger LOG = Logger.getLogger(NetworkUtilTest.class.getName());

	public NetworkUtilTest()
	{
	}

	/**
	 * Test of getClientIp method, of class NetworkUtil.
	 */
	@Test
	public void testGetClientIp()
	{
		LOG.info("getClientIp");
		HttpServletRequest request = Mockito.mock(MockHttpServletRequest.class);

		LOG.info("Scenario 1: null request");

		String result = NetworkUtil.getClientIp(null);
		assertEquals(OpenStorefrontConstant.NOT_AVAILABLE, result);

		LOG.info("Scenario 2: Empty header");

		Mockito.when(request.getRemoteAddr()).thenReturn("unittest");
		Mockito.when(request.getHeader(Mockito.anyString())).thenReturn(null);

		String expResult = "unittest";
		result = NetworkUtil.getClientIp(request);
		assertEquals(expResult, result);

		LOG.info("Scenario 3: Forward header");
		Mockito.when(request.getHeader("x-forwarded-for")).thenReturn("1");

		expResult = "unittest Forward for: 1";
		result = NetworkUtil.getClientIp(request);
		assertEquals(expResult, result);

		LOG.info("Scenario 4: Real IP header");
		request = Mockito.mock(MockHttpServletRequest.class);
		Mockito.when(request.getRemoteAddr()).thenReturn("unittest");
		Mockito.when(request.getHeader("x-real-ip")).thenReturn("2");

		expResult = "unittest X-real IP: 2";
		result = NetworkUtil.getClientIp(request);
		assertEquals(expResult, result);

	}

}
