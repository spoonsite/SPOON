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
package edu.usu.sdl.core.init;

import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
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

	private static final Logger LOG = Logger.getLogger(ApplyOnceInit.class.getName());

	protected static final String SKIP_APPLY = "-SKIP_APPLY-";

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
		SecurityUtil.initSystemUser();
		LOG.log(Level.INFO, () -> MessageFormat.format("Checking {0} to make sure it is applied.", appliedKey));

		String lastRunString = service.getSystemService().getPropertyValue(appliedKey + "_LASTRUN_DTS");
		if (StringUtils.isNotBlank(lastRunString)) {
			LOG.log(Level.INFO, () -> MessageFormat.format("Already Applied {0} on {1}", appliedKey, lastRunString));
		} else {
			String results = internalApply();
			if (!SKIP_APPLY.equals(results)) {
				LOG.log(Level.INFO, () -> MessageFormat.format("Applied {0} changes", appliedKey));
				service.getSystemService().saveProperty(appliedKey + "_LASTRUN_DTS", TimeUtil.dateToString(TimeUtil.currentDate()));
				service.getSystemService().saveProperty(appliedKey + "_STATUS", results);
			}
		}
	}

	/**
	 * Performs the actual operation
	 *
	 * @return true if applied successfully
	 */
	protected abstract String internalApply();

	/**
	 * Order to run in....lower run first
	 */
	public abstract int getPriority();

}
