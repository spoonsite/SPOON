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
package edu.usu.sdl.openstorefront.web.init;

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * This is use to apply conversion and one time setup items that don't need to
 * persist between releases Rules for operations: -Impotent (Meaning running
 * multiple times should be harmless)
 *
 * @author dshurtleff
 */
public abstract class ApplyOnceInit
{

	private static final Logger log = Logger.getLogger(ApplyOnceInit.class.getName());

	protected ServiceProxy service = new ServiceProxy();
	protected String appliedKey;

	public ApplyOnceInit()
	{
		appliedKey = "DEFAULT_APPLY";
	}

	protected ApplyOnceInit(String appliedKey)
	{
		this.appliedKey = appliedKey;
	}

	public void applyChanges()
	{
		log.log(Level.INFO, MessageFormat.format("Checking {0} to make sure it''s applied.", appliedKey));

		String lastRunString = service.getSystemService().getPropertyValue(appliedKey + "_LASTRUN_DTS");
		if (StringUtils.isNotBlank(lastRunString)) {
			log.log(Level.INFO, MessageFormat.format("Already Applied {0} on {1}", appliedKey, lastRunString));
		} else {
			boolean success = internalApply();
			log.log(Level.INFO, MessageFormat.format("Applied {0} changes", appliedKey));
			service.getSystemService().saveProperty(appliedKey + "_LASTRUN_DTS", TimeUtil.dateToString(TimeUtil.currentDate()));
			service.getSystemService().saveProperty(appliedKey + "_STATUS", Boolean.toString(success));
		}
	}

	/**
	 * Performs the actual operation
	 *
	 * @return true if applied successfully
	 */
	protected abstract boolean internalApply();

}
