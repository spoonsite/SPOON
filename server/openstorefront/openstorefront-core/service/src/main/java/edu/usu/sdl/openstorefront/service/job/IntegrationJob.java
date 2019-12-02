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
package edu.usu.sdl.openstorefront.service.job;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.core.entity.ModificationType;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * External system integration
 *
 * @author dshurtleff
 */
@DisallowConcurrentExecution
public class IntegrationJob
		extends BaseJob
{

	public static final String COMPONENT_ID = "Component-Id";
	public static final String CONFIG_ID = "Config-Id";

	@Override
	protected void executeInternaljob(JobExecutionContext context)
	{
		Object componentIdObj = context.getMergedJobDataMap().get(COMPONENT_ID);
		if (componentIdObj != null) {
			String componentId = componentIdObj.toString();
			Object configIdObj = context.getMergedJobDataMap().get(CONFIG_ID);
			String configId = null;
			if (configIdObj != null) {
				configId = configIdObj.toString();
			}

		} else {
			throw new OpenStorefrontRuntimeException("Unable to get component id.  Job failed.", "Check config data.", ErrorTypeCode.INTEGRATION);
		}
	}

}
