/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.test.search;

import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ArchitectureSearchTest
		extends BaseTestCase
{

	public ArchitectureSearchTest()
	{
		this.description = "Architecture Search";
	}

	@Override
	protected void runInternalTest()
	{
		results.append("Searching...<br>");
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeType(AttributeType.DI2E_SVCV4);
		attributeCodePk.setAttributeCode("1");

		List<ComponentSearchView> componentSearchViews = service.getSearchService().architectureSearch(attributeCodePk, new FilterQueryParams());
		results.append("Results...").append("<br><br>");
		componentSearchViews.forEach(view -> {
			results.append(view.getName()).append("   Type:").append(view.getListingType()).append("<br>");
		});
	}

}
