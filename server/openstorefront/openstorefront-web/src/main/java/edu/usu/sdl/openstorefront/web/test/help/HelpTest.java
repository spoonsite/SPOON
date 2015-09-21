/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.test.help;

import edu.usu.sdl.openstorefront.core.model.HelpSectionAll;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;

/**
 *
 * @author dshurtleff
 */
public class HelpTest
		extends BaseTestCase
{

	public HelpTest()
	{
		this.description = "Help Test";
	}

	@Override
	protected void runInternalTest()
	{
		HelpSectionAll userHelp = service.getSystemService().getAllHelp(false);
		results.append("Got User Help Records: ").append(userHelp.getChildSections().size() + 1).append("<br>");

		HelpSectionAll adminHelp = service.getSystemService().getAllHelp(true);
		results.append("Got Admin Help Records: ").append(adminHelp.getChildSections().size() + 1).append("<br>");
	}

}
