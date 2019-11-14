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
package edu.usu.sdl.openstorefront.web.init;

import edu.usu.sdl.core.CoreSystem;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.web.atmosphere.AtmosphereNotificationListerner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import org.atmosphere.cpr.AtmosphereFramework;

/**
 * Use to init the application and shut it down properly
 *
 * @author dshurtleff
 */
public class ApplicationInit
{

	private static final Logger LOG = Logger.getLogger(ApplicationInit.class.getName());

	// inject Items that need starting at sytem startup
	@Inject
	private CoreSystem coreSystem;

	@Context
	private ServletContext context;

	public ApplicationInit()
	{
	}

	@PostConstruct
	public void contextInitialized()
	{
		//curb some noisy logs by default
		Logger atmospshereLog = Logger.getLogger("org.atmosphere");
		if (atmospshereLog != null) {
			atmospshereLog.setLevel(Level.OFF);
		}

		coreSystem.startup(() -> {
			AtmosphereFramework atmosphereFramework = (AtmosphereFramework) context.getAttribute("AtmosphereServlet");
			AtmosphereNotificationListerner atmosphereNotificationListerner = new AtmosphereNotificationListerner(atmosphereFramework);
			ServiceProxy.getProxy().getNotificationService().registerNotificationListerner(atmosphereNotificationListerner);
		});

	}

	@PreDestroy
	public void contextDestroyed()
	{
		AtmosphereFramework atmosphereFramework = (AtmosphereFramework) context.getAttribute("AtmosphereServlet");
		if (atmosphereFramework != null) {
			LOG.log(Level.INFO, "Shutdown Atmosphere");
			atmosphereFramework.destroy();
		}
	}

}
