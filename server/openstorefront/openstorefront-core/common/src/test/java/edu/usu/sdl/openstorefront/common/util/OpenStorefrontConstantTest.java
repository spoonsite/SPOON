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
public class OpenStorefrontConstantTest
{

	private static final Logger LOG = Logger.getLogger(OpenStorefrontConstantTest.class.getName());

	public OpenStorefrontConstantTest()
	{
		System.out.println(OpenStorefrontConstant.TaskStatus.WORKING);
		System.out.println(OpenStorefrontConstant.ListingType.COMPONENT.getDescription());
	}

	/**
	 * Test of getFileExtensionForMime method, of class OpenStorefrontConstant.
	 */
	@Test
	public void testGetFileExtensionForMime()
	{
		LOG.info("getFileExtensionForMime");

		LOG.info("Scenario 1: null operation");
		String mimeType = null;
		String expResult = OpenStorefrontConstant.NOT_AVAILABLE;
		String result = OpenStorefrontConstant.getFileExtensionForMime(mimeType);
		assertEquals(expResult, result);

		LOG.info("Scenario 2: valid type");
		mimeType = "audio/mpg";
		expResult = ".mpg";
		result = OpenStorefrontConstant.getFileExtensionForMime(mimeType);
		assertEquals(expResult, result);

		LOG.info("Scenario 3: valid xref");
		mimeType = "application/pdf";
		expResult = ".pdf";
		result = OpenStorefrontConstant.getFileExtensionForMime(mimeType);
		assertEquals(expResult, result);

		LOG.info("Scenario 4: invalid");
		mimeType = "junk";
		expResult = OpenStorefrontConstant.NOT_AVAILABLE;
		result = OpenStorefrontConstant.getFileExtensionForMime(mimeType);
		assertEquals(expResult, result);

	}

	/**
	 * Test of getMimeForFileExtension method, of class OpenStorefrontConstant.
	 */
	@Test
	public void testGetMimeForFileExtension()
	{
		LOG.info("getMimeForFileExtension");

		LOG.info("Scenario 1: null operation");
		String mimeType = null;
		String expResult = "application/octet-stream";
		String result = OpenStorefrontConstant.getMimeForFileExtension(mimeType);
		assertEquals(expResult, result);

		LOG.info("Scenario 2: valid type");
		String fileExtension = ".mpg";
		expResult = "video/mpg";
		result = OpenStorefrontConstant.getMimeForFileExtension(fileExtension);
		assertEquals(expResult, result);

		LOG.info("Scenario 3: invalid type");
		fileExtension = "junkage";
		expResult = "application/octet-stream";
		result = OpenStorefrontConstant.getMimeForFileExtension(fileExtension);
		assertEquals(expResult, result);

		LOG.info("Scenario 4: extended type");
		fileExtension = ".jpeg";
		expResult = "image/jpg";
		result = OpenStorefrontConstant.getMimeForFileExtension(fileExtension);
		assertEquals(expResult, result);

	}

}
