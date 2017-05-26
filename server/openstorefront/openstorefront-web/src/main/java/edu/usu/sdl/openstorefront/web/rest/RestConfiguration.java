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
package edu.usu.sdl.openstorefront.web.rest;

import edu.usu.sdl.core.CoreSystem;
import edu.usu.sdl.openstorefront.web.init.AppStart;
import edu.usu.sdl.openstorefront.web.init.ApplicationInit;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author dshurtleff
 * @author kbair
 */
@ApplicationPath("api")
public class RestConfiguration
		extends ResourceConfig
{

	public static final String APPLICATION_BASE_PATH = "api";

	@Inject
	public RestConfiguration(ServiceLocator locator)
	{
		// jersy 2 does not support @Immediate once https://github.com/jersey/jersey/issues/2563 is reolcved for the version we are using
		// replace register(new AppStart()); with ServiceLocatorUtilities.enableImmediateScope(locator);

		register(new AppStart());
		register(new AbstractBinder(){
			@Override
            protected void configure() {
				// NOTE (KB): do not add anymore bind calls until a solution is found for getting stripes (ActionBeans) access to the jersey/hk2 Request Scope.
				bind(CoreSystem.class).to(CoreSystem.class).in(Singleton.class);
				bind(ApplicationInit.class).to(ApplicationInit.class).in(Singleton.class);
			}
		});
		packages(true, "edu.usu.sdl.openstorefront");
		//ApplicationInit init = locator.createAndInitialize(ApplicationInit.class);
	}

}
