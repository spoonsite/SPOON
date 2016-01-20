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

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.DBLogRecord;
import edu.usu.sdl.openstorefront.core.view.ApplicationStatus;
import edu.usu.sdl.openstorefront.core.view.DBLogRecordWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.LoggerView;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.core.view.MemoryPoolStatus;
import edu.usu.sdl.openstorefront.core.view.RestErrorModel;
import edu.usu.sdl.openstorefront.core.view.ThreadStatus;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireAdmin;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Field;
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
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sourceforge.stripes.util.bean.BeanUtil;
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

		applicationStatus.getHeapMemoryStatus().setName("Heap");
		applicationStatus.getHeapMemoryStatus().setDetails(memoryMXBean.getHeapMemoryUsage().toString());
		applicationStatus.getHeapMemoryStatus().setInitKb(memoryMXBean.getHeapMemoryUsage().getInit() != 0 ? memoryMXBean.getHeapMemoryUsage().getInit() / 1024 : 0);
		applicationStatus.getHeapMemoryStatus().setUsedKb(memoryMXBean.getHeapMemoryUsage().getUsed() != 0 ? memoryMXBean.getHeapMemoryUsage().getUsed() / 1024 : 0);
		applicationStatus.getHeapMemoryStatus().setMaxKb(memoryMXBean.getHeapMemoryUsage().getMax() != 0 ? memoryMXBean.getHeapMemoryUsage().getMax() / 1024 : 0);
		applicationStatus.getHeapMemoryStatus().setCommitedKb(memoryMXBean.getHeapMemoryUsage().getCommitted() != 0 ? memoryMXBean.getHeapMemoryUsage().getCommitted() / 1024 : 0);

		applicationStatus.getNonHeapMemoryStatus().setName("Non-Heap");
		applicationStatus.getNonHeapMemoryStatus().setDetails(memoryMXBean.getNonHeapMemoryUsage().toString());
		applicationStatus.getNonHeapMemoryStatus().setInitKb(memoryMXBean.getNonHeapMemoryUsage().getInit() != 0 ? memoryMXBean.getNonHeapMemoryUsage().getInit() / 1024 : 0);
		applicationStatus.getNonHeapMemoryStatus().setUsedKb(memoryMXBean.getNonHeapMemoryUsage().getUsed() != 0 ? memoryMXBean.getNonHeapMemoryUsage().getUsed() / 1024 : 0);
		applicationStatus.getNonHeapMemoryStatus().setMaxKb(memoryMXBean.getNonHeapMemoryUsage().getMax() != 0 ? memoryMXBean.getNonHeapMemoryUsage().getMax() / 1024 : 0);
		applicationStatus.getNonHeapMemoryStatus().setCommitedKb(memoryMXBean.getNonHeapMemoryUsage().getCommitted() != 0 ? memoryMXBean.getNonHeapMemoryUsage().getCommitted() / 1024 : 0);

		applicationStatus.setLiveThreadCount(threadMXBean.getThreadCount());
		applicationStatus.setTotalThreadCount(threadMXBean.getTotalStartedThreadCount());

		applicationStatus.setStartTime(new Date(runtimeMXBean.getStartTime()));
		applicationStatus.setUpTime(TimeUtil.millisToString(runtimeMXBean.getUptime()));

		for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
			applicationStatus.getGarbageCollectionInfos().add(TimeUtil.millisToString(garbageCollectorMXBean.getCollectionTime()) + " - (" + garbageCollectorMXBean.getCollectionCount() + ")");
		}

		for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
			MemoryPoolStatus memoryPoolStatus = new MemoryPoolStatus();
			memoryPoolStatus.setName(memoryPoolMXBean.getName() + " - " + memoryPoolMXBean.getType());
			memoryPoolStatus.setDetails(memoryPoolMXBean.getUsage().toString());
			memoryPoolStatus.setInitKb(memoryPoolMXBean.getUsage().getInit() != 0 ? memoryPoolMXBean.getUsage().getInit() / 1024 : 0);
			memoryPoolStatus.setUsedKb(memoryPoolMXBean.getUsage().getUsed() != 0 ? memoryPoolMXBean.getUsage().getUsed() / 1024 : 0);
			memoryPoolStatus.setMaxKb(memoryPoolMXBean.getUsage().getMax() != 0 ? memoryPoolMXBean.getUsage().getMax() / 1024 : 0);
			memoryPoolStatus.setCommitedKb(memoryPoolMXBean.getUsage().getCommitted() != 0 ? memoryPoolMXBean.getUsage().getCommitted() / 1024 : 0);

			applicationStatus.getMemoryPools().add(memoryPoolStatus);
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
	@APIDescription("Get a config property")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/configproperties/{key}")
	public Response getConfigPropertiesForKey(@PathParam("key") String key)
	{
		String value = PropertiesManager.getValueDefinedDefault(key);

		LookupModel lookupModel = new LookupModel();
		lookupModel.setCode(key);
		if (key.contains(PropertiesManager.PW_PROPERTY)) {
			lookupModel.setDescription(StringUtils.leftPad("", value.length(), "*"));
		} else {
			lookupModel.setDescription(value);
		}

		return sendSingleEntityResponse(lookupModel);
	}

	@POST
	@RequireAdmin
	@APIDescription("Save a config property")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(LookupModel.class)
	@Path("/configproperties")
	public Response addConfigProperty(
			LookupModel lookupModel
	)
	{
		return handleSaveConfigProperty(lookupModel);
	}

	private Response handleSaveConfigProperty(LookupModel lookupModel)
	{
		ValidationModel validationModel = new ValidationModel(lookupModel);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			PropertiesManager.setProperty(lookupModel.getCode(), lookupModel.getDescription());
		}
		return sendSingleEntityResponse(validationResult.toRestError());
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes config property (Allow it to fallback to the Default)")
	@Path("/configproperties/{key}")
	public void removeConfigProperties(@PathParam("key") String key)
	{
		PropertiesManager.removeProperty(key);
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
			} else if (levels.contains(level)) {
				logger.setLevel(Level.parse(level));
				saved = true;
			} else {
				RestErrorModel restErrorModel = new RestErrorModel();
				restErrorModel.getErrors().put("level", "Log level is not valid. Check level value passed in.");
				response = Response.ok(restErrorModel).build();
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

	@GET
	@RequireAdmin
	@APIDescription("Gets log records")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(DBLogRecord.class)
	@Path("/logrecords")
	public Response getLogFile(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		DBLogRecord logRecordExample = new DBLogRecord();

		DBLogRecord logStartExample = new DBLogRecord();
		logStartExample.setEventDts(TimeUtil.beginningOfDay(filterQueryParams.getStart()));

		DBLogRecord logEndExample = new DBLogRecord();
		logEndExample.setEventDts(TimeUtil.endOfDay(filterQueryParams.getEnd()));

		QueryByExample queryByExample = new QueryByExample(logRecordExample);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(logStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(logEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());
		queryByExample.setSortDirection(filterQueryParams.getSortOrder());

		DBLogRecord logRecordSortExample = new DBLogRecord();
		Field sortField = ReflectionUtil.getField(logRecordSortExample, filterQueryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), logRecordSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(logRecordSortExample);
		}

		List<DBLogRecord> logRecords = service.getPersistenceService().queryByExample(DBLogRecord.class, queryByExample);

		DBLogRecordWrapper logRecordWrapper = new DBLogRecordWrapper();
		logRecordWrapper.getLogRecords().addAll(logRecords);
		logRecordWrapper.setResults(logRecords.size());
		logRecordWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));

		return sendSingleEntityResponse(logRecordWrapper);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Clears all DB log records. Doesn't affect server logs. Note: application will automatically clear old records exceeding max allowed.")
	@Path("/logrecords")
	public void clearAllDBLogs()
	{
		service.getSystemService().clearAllLogRecord();
	}

	@PUT
	@RequireAdmin
	@APIDescription("Toggle Database logging; pass use=true or use=false")
	@DataType(LookupModel.class)
	@Path("/dblogger/{use}")
	public Response updateConfigProperty(
			@PathParam("use") String use
	)
	{
		service.getSystemService().toggleDBlogger(Convert.toBoolean(use));
		return Response.ok().build();
	}

	@GET
	@APIDescription("Gets information about whether the Jira User Feedback is available or not.")
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/showfeedback")
	public LookupModel getShowFeedBack()
	{
		LookupModel lookupModel = new LookupModel();
		lookupModel.setCode(PropertiesManager.getValue(PropertiesManager.KEY_ALLOW_JIRA_FEEDBACK, "True").toUpperCase());
		if (Convert.toBoolean(lookupModel.getCode())) {
			lookupModel.setDescription("Allow jira feedback");
		} else {
			lookupModel.setDescription("Do not allow jira feedback");
		}
		return lookupModel;
	}

}
