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
package edu.usu.sdl.openstorefront.service.job;

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.security.ExternalUserManager;
import edu.usu.sdl.openstorefront.service.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.util.Convert;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.JobExecutionContext;

/**
 * Job to Sync user profiles with an external source
 *
 * @author dshurtleff
 */
public class UserProfileSyncJob
		extends BaseJob
{

	private static final Logger log = Logger.getLogger(UserProfileSyncJob.class.getName());

	@Override
	protected void executeInternaljob(JobExecutionContext context)
	{
		String run = PropertiesManager.getValue(PropertiesManager.KEY_EXTERNAL_SYNC_ACTIVATE, "false");
		if (Convert.toBoolean(run)) {
			try {
				String userManagementClass = PropertiesManager.getValue(PropertiesManager.KEY_EXTERNAL_USER_MANAGER, "IniRealmManager");
				Class userManagerClass = Class.forName("edu.usu.sdl.openstorefront.security." + userManagementClass);
				ExternalUserManager userManager = (ExternalUserManager) userManagerClass.newInstance();
				service.getSystemService().syncUserProfilesWithUserManagement(userManager);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
				throw new OpenStorefrontRuntimeException(ex);
			}
		} else {
			log.log(Level.FINE, "External Sync Job is inactive.  To activate set 'external.sync.activate' to true in application config file.");
		}
	}

}
