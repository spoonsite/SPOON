/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.test.branding;

import edu.usu.sdl.openstorefront.core.entity.Branding;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;

/**
 *
 * @author ccummings
 */
public class BrandingServiceTest extends BaseTestCase
{

	private Branding brandingTest1 = null;
	private Branding brandingTest2 = null;

	@Override
	protected void runInternalTest()
	{
		brandingTest1 = new Branding();
		brandingTest1.setName("test_branding_1");
		brandingTest1 = service.getBrandingService().saveBranding(brandingTest1);
		brandingTest1.setActiveStatus(Branding.ACTIVE_STATUS);
		service.getPersistenceService().persist(brandingTest1);

		brandingTest2 = new Branding();
		brandingTest2.setName("test_branding_2");
		brandingTest2.setActiveStatus(Branding.INACTIVE_STATUS);
		brandingTest2 = service.getBrandingService().saveBranding(brandingTest2);

		service.getBrandingService().setBrandingAsCurrent(brandingTest2.getBrandingId());

		brandingTest2 = getCurrentBranding(brandingTest2.getBrandingId());
		brandingTest1 = getCurrentBranding(brandingTest1.getBrandingId());


		if (Branding.INACTIVE_STATUS.equals(brandingTest1.getActiveStatus())
				&& Branding.ACTIVE_STATUS.equals(brandingTest2.getActiveStatus())) {
			results.append("Change current branding:  Test Passed<br><br>");
		} else {
			failureReason.append("Change current branding:  Test Failed - unable to change branding<br><br>");
		}
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
		service.getBrandingService().setBrandingAsCurrent(null);
		if (brandingTest1 != null) {
			service.getBrandingService().deleteBranding(brandingTest1.getBrandingId());
		}
		if (brandingTest2 != null) {
			service.getBrandingService().deleteBranding(brandingTest2.getBrandingId());
		}
	}

	public Branding getCurrentBranding(String brandingId)
	{
		Branding currentBranding = service.getPersistenceService().findById(Branding.class, brandingId);

		return currentBranding;
	}

	@Override
	public String getDescription()
	{
		return "Branding Test";
	}
}
