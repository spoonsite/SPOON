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
package edu.usu.sdl.openstorefront.web.rest.service;

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.service.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.web.rest.model.ApplicationStatus;
import edu.usu.sdl.openstorefront.web.rest.model.LoggerView;
import edu.usu.sdl.openstorefront.web.rest.model.ThreadStatus;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import edu.usu.sdl.openstorefront.web.viewmodel.LookupModel;
import edu.usu.sdl.openstorefront.web.viewmodel.RestErrorModel;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;

/**
 * Application related services
 *
 * @author dshurtleff
 */
@Path("v1/service/application")
@APIDescription("Application related services")
public class Application
		extends BaseResource
{

	@GET
	@RequireAdmin
	@APIDescription("Gets the application system status")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ApplicationStatus.class)
	@Path("/status")
	public Response getApplicationStatus()
	{
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

		ApplicationStatus applicationStatus = new ApplicationStatus();
		applicationStatus.setApplicationVersion(PropertiesManager.getApplicationVersion());
		applicationStatus.setProcessorCount(operatingSystemMXBean.getAvailableProcessors());
		applicationStatus.setSystemLoad(operatingSystemMXBean.getSystemLoadAverage());
		applicationStatus.setSystemProperties(runtimeMXBean.getSystemProperties());
		applicationStatus.setHeapMemory(memoryMXBean.getHeapMemoryUsage().toString());
		applicationStatus.setLiveThreadCount(threadMXBean.getThreadCount());
		applicationStatus.setTotalThreadCount(threadMXBean.getTotalStartedThreadCount());
		applicationStatus.setNonHeapMemory(memoryMXBean.getNonHeapMemoryUsage().toString());
		applicationStatus.setStartTime(new Date(runtimeMXBean.getStartTime()));
		applicationStatus.setUpTime(TimeUtil.millisToString(runtimeMXBean.getUptime()));

		for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
			applicationStatus.getGarbageCollectionInfos().add(TimeUtil.millisToString(garbageCollectorMXBean.getCollectionTime()) + " - (" + garbageCollectorMXBean.getCollectionCount() + ")");
		}

		for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
			applicationStatus.getMemoryPoolInfos().add(memoryPoolMXBean.getName() + " - " + memoryPoolMXBean.getType() + " (" + memoryPoolMXBean.getUsage().toString() + ")");
		}

		return sendSingleEntityResponse(applicationStatus);
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets the application system thread and status")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ThreadStatus.class)
	@Path("/threads")
	public List<ThreadStatus> getThreads()
	{
		List<ThreadStatus> threadStatuses = new ArrayList<>();

		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		ThreadInfo threadInfos[] = threadMXBean.dumpAllThreads(false, false);

		for (ThreadInfo info : threadInfos) {
			ThreadStatus threadStatus = new ThreadStatus();
			threadStatus.setId(info.getThreadId());
			threadStatus.setName(info.getThreadName());
			threadStatus.setStatus(info.getThreadState().name());
			threadStatus.setDetails(info.toString().replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;").replace("\n", "<br>"));
			threadStatuses.add(threadStatus);
		}

		return threadStatuses;
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets config properties")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/configproperties")
	public List<LookupModel> getConfigProperties()
	{
		List<LookupModel> lookupModels = new ArrayList<>();

		Map<String, String> props = PropertiesManager.getAllProperties();
		for (String key : props.keySet()) {
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(key);
			String value = props.get(key);
			if (key.contains(PropertiesManager.PW_PROPERTY)) {
				lookupModel.setDescription(StringUtils.leftPad("", value.length(), "*"));
			} else {
				lookupModel.setDescription(value);
			}
			lookupModels.add(lookupModel);
		}
		return lookupModels;
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets Loggers in the system")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LoggerView.class)
	@Path("/loggers")
	public List<LoggerView> getLoggers()
	{
		List<LoggerView> loggers = new ArrayList<>();

		List<String> allLoggers = LogManager.getLoggingMXBean().getLoggerNames();
		Collections.sort(allLoggers);

		for (String name : allLoggers) {
			LoggerView loggerView = new LoggerView();
			loggerView.setName(name);

			Level levelLocal = LogManager.getLogManager().getLogger(name).getLevel();
			String levelName = "";
			if (levelLocal != null) {
				levelName = levelLocal.getName();
			}
			loggerView.setLevel(levelName);

			Logger localLogger = LogManager.getLogManager().getLogger(name);
			for (Handler handlerLocal : localLogger.getHandlers()) {
				loggerView.getHandlers().add(handlerLocal.getClass().getName() + " = " + handlerLocal.getLevel().getName());
			}
			loggers.add(loggerView);
		}

		return loggers;
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets log levels")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/loglevels")
	public List<LookupModel> getLogLevels()
	{
		List<LookupModel> lookupModels = new ArrayList<>();

		loadLevels().forEach(levelName -> {
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(levelName);
			lookupModel.setDescription(levelName);
			lookupModels.add(lookupModel);
		});
		return lookupModels;
	}

	@PUT
	@RequireAdmin
	@APIDescription("Sets logger level")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.WILDCARD})
	@DataType(LoggerView.class)
	@Path("/logger/{loggername}/level")
	public Response updateApplicationProperty(
			@PathParam("loggername")
			@RequiredParam String loggername,
			String level)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		Logger logger = LogManager.getLogManager().getLogger(loggername);
		if (logger != null) {
			List<String> levels = loadLevels();
			boolean saved = false;
			if (StringUtils.isBlank(level)) {
				logger.setLevel(null);
				saved = true;
			} else {
				if (levels.contains(level)) {
					logger.setLevel(Level.parse(level));
					saved = true;
				} else {
					RestErrorModel restErrorModel = new RestErrorModel();
					restErrorModel.getErrors().put("level", "Log level is not valid. Check level value passed in.");
					response = Response.ok(restErrorModel).build();
				}
			}
			if (saved) {
				LoggerView loggerView = new LoggerView();
				loggerView.setName(loggername);
				loggerView.setLevel(level);
				for (Handler handlerLocal : logger.getHandlers()) {
					loggerView.getHandlers().add(handlerLocal.getClass().getName() + " = " + handlerLocal.getLevel().getName());
				}
				response = Response.ok(loggerView).build();
			}
		}
		return response;
	}

	private List<String> loadLevels()
	{
		List<String> logLevels = Arrays.asList(Level.OFF.getName(),
				Level.SEVERE.getName(),
				Level.WARNING.getName(),
				Level.CONFIG.getName(),
				Level.INFO.getName(),
				Level.FINE.getName(),
				Level.FINER.getName(),
				Level.FINEST.getName(),
				Level.ALL.getName());
		return logLevels;
	}

}
