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
package edu.usu.sdl.openstorefront.doc;

import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.stripes.util.ResolverUtil;
import org.junit.Test;

/**
 * API Doc Generation test
 *
 * @author dshurtleff
 */
public class JaxrsProcessorTest
{

	/**
	 * Test of processRestClass method, of class JaxrsProcessor.
	 */
	@Test
	public void testProcessRestClass()
	{
		System.out.println("Checking main Resources");
		ResolverUtil resolverUtil = new ResolverUtil();
		resolverUtil.find(new ResolverUtil.IsA(BaseResource.class), "edu.usu.sdl.openstorefront.web.rest");

		List<Class> classList = new ArrayList<>();
		classList.addAll(resolverUtil.getClasses());
		for (Class resourceClass : classList) {
			System.out.println("Resources: " + resourceClass.getName());
			APIResourceModel result = JaxrsProcessor.processRestClass(resourceClass);
		}
		System.out.println("Done");
	}

}
