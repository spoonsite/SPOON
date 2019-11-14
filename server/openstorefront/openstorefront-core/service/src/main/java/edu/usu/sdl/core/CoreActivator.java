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
package edu.usu.sdl.core;

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.SystemManager;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Registers core services.
 *
 * @author dshurtleff
 */
public class CoreActivator
		implements BundleActivator
{

	private static final Logger log = Logger.getLogger(CoreActivator.class.getName());

	private ServiceRegistration registration;
	private ServiceRegistration systemRegistration;

	@Override
	public void start(BundleContext context) throws Exception
	{
		registration = context.registerService(Service.class, new ServiceProxy(), null);
		log.log(Level.INFO, "Registered Core Service");

		systemRegistration = context.registerService(SystemManager.class, new CoreSystem(), null);
		log.log(Level.INFO, "Registered Core System");
	}

	@Override
	public void stop(BundleContext context) throws Exception
	{
		if (registration != null) {
			registration.unregister();
			log.log(Level.INFO, "Unregistered Core Service");

			systemRegistration.unregister();
			log.log(Level.INFO, "Unregistered Core System");

		}
	}

}
