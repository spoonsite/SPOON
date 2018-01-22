/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.core.init;

import edu.usu.sdl.openstorefront.core.entity.SecurityRole;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Remove after 2.5
 *
 * @author dshurtleff
 */
public class AddGuestGroupInit
		extends ApplyOnceInit
{

	private static final Logger LOG = Logger.getLogger(AddGuestGroupInit.class.getName());

	public AddGuestGroupInit()
	{
		super("GUEST-GROUP-Init");
	}

	@Override
	protected String internalApply()
	{
		SecurityRole securityRole = new SecurityRole();
		securityRole.setRoleName(SecurityRole.GUEST_GROUP);

		securityRole = securityRole.find();
		if (securityRole == null) {
			securityRole = new SecurityRole();
			securityRole.setRoleName(SecurityRole.GUEST_GROUP);
			securityRole.setAllowUnspecifiedDataSensitivity(Boolean.TRUE);
			securityRole.setAllowUnspecifiedDataSource(Boolean.TRUE);
			securityRole.setDescription("Used for guests when allowed based on URL");
			securityRole.setLandingPage("/");
			service.getSecurityService().saveSecurityRole(securityRole);
			LOG.log(Level.CONFIG, "Guest group was setup");
		}

		return "Added Guest Group";
	}

	@Override
	public int getPriority()
	{
		return 25;
	}

}
