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
package edu.usu.sdl.openstorefront.web.action.admin;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.web.action.BaseAction;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang3.StringUtils;

/**
 * Runtime Access to log levels.
 *
 * @author dshurtleff
 */
public class LoggingAction
		extends BaseAction
{

	private static final Logger LOG = Logger.getLogger(LoggingAction.class.getName());

	@Validate(required = true, on = {"Handlers"})
	private String handler;

	@Validate(required = true, on = {"UpdateLogLevel", "Handlers"})
	private String logger;
	private String level;

	@DefaultHandler
	public Resolution viewLoggers()
	{
		StringBuilder results = new StringBuilder();

		List<String> allLoggers = LogManager.getLoggingMXBean().getLoggerNames();
		Collections.sort(allLoggers);
		results.append("<table border='1'><tr style='background-color: lightgrey; '><th >Logger</th><th>Level</th><th>Handlers</th>");
		for (String name : allLoggers) {
			results.append("<tr>");
			Level levelLocal = LogManager.getLogManager().getLogger(name).getLevel();
			String levelName = "";
			if (levelLocal != null) {
				levelName = levelLocal.getName();
			}
			results.append("<td><b>").append(name).append("</b></td><td> ").append(levelName).append("</td>");

			results.append("<td>");
			Logger localLogger = LogManager.getLogManager().getLogger(name);
			for (Handler handlerLocal : localLogger.getHandlers()) {
				results.append(handlerLocal.getClass().getName()).append(" = ").append(handlerLocal.getLevel().getName()).append("<br>");
			}
			results.append("</td>");

			results.append("</tr>");
		}
		results.append("</table>");
		return new StreamingResolution("text/html", results.toString());
	}

	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@HandlesEvent("UpdateLogLevel")
	public Resolution updateLogLevel()
	{
		Logger localLogger = LogManager.getLogManager().getLogger(logger);
		if (localLogger != null) {
			if (StringUtils.isNotBlank(level)) {
				localLogger.setLevel(Level.parse(level));
			} else {
				localLogger.setLevel(null);
			}
			LOG.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find logger", "Check name");
		}

		return viewLoggers();
	}

	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@HandlesEvent("UpdateHandlerLevel")
	@SuppressWarnings("squid:S1872")
	public Resolution updateHandlerLevel()
	{
		Logger localLogger = LogManager.getLogManager().getLogger(logger);
		if (localLogger != null) {

			for (Handler handlerLocal : localLogger.getHandlers()) {
				if (handlerLocal.getClass().getName().equals(handler)) {
					if (StringUtils.isNotBlank(level)) {
						handlerLocal.setLevel(Level.parse(level));
					} else {
						handlerLocal.setLevel(null);
					}
				}
			}
			LOG.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find logger", "Check name");
		}

		return viewLoggers();
	}

	public String getLogger()
	{
		return logger;
	}

	public void setLogger(String logger)
	{
		this.logger = logger;
	}

	public String getLevel()
	{
		return level;
	}

	public void setLevel(String level)
	{
		this.level = level;
	}

	public String getHandler()
	{
		return handler;
	}

	public void setHandler(String handler)
	{
		this.handler = handler;
	}

}
