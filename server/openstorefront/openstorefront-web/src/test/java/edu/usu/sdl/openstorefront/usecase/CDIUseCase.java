/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.usecase;

import edu.usu.sdl.openstorefront.web.model.PageModel;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class CDIUseCase
{

	@Test
	public void testNewLocator()
	{
		ServiceLocatorFactory factory = ServiceLocatorFactory.getInstance();

		ServiceLocator locator = factory.create("storefront");

//		ServiceLocatorUtilities.bind(locator,
//				new AbstractBinder()
//		{
//			@Override
//			protected void configure()
//			{
//				bind(new RequestScope()).to(RequestScope.class);
//			}
//		},
//				//new ContextInjectionResolverImpl.Binder(),
//				new RequestContext.Binder());
		DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);

		DynamicConfiguration config = dcs.createDynamicConfiguration();

		Descriptor descriptor = BuilderHelper.
				link(PageModel.class).
				to(PageModel.class).
				build();
		config.bind(descriptor);

		config.commit();

//		List<ActiveDescriptor> descriptors = locator..getDescriptors(new Filter(){
//			@Override
//			public boolean matches(Descriptor d)
//			{
//				return true;
//			}
//
//		});
		//List<PageModel> pageModels = locator.getAllServices(PageModel.class);
		//System.out.println("Page Model: " + pageModels);
		//locator.createAndInitialize(TestInject.class);
		//TestInject testInject = locator.create(TestInject.class);
		//TestInject testInject = locator.createAndInitialize(TestInject.class);
		TestInject testInject = new TestInject();

		locator.inject(testInject);
		testInject.printName();
	}

}
