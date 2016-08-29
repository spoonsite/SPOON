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
package edu.usu.sdl.openstorefront.web.test.alert;

import edu.usu.sdl.openstorefront.core.entity.Alert;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;

/**
 *
 * @author ccummings
 */
public class AlertNullTest extends BaseTestCase
{

	private Alert alertNull = null;

	@Override
	protected void runInternalTest()
	{
		try {
			results.append("Saving alert...<br>");
			alertNull = service.getAlertService().saveAlert(null);
			failureReason.append("Required fields were null and alert was saved<br>");
		} catch (NullPointerException e) {
			results.append("Unable to save alert.  Required fields cannot be null<br>");
		}
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
		if (alertNull != null) {
			service.getAlertService().deleteAlert(alertNull.getAlertId());
		}
	}

	@Override
	public String getDescription()
	{
		return "Alert Null Test";
	}
}
