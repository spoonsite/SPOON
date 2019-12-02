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

import edu.usu.sdl.core.init.ApplyOnceInit;
import edu.usu.sdl.core.init.PostInitApplyOnce;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.SystemManager;
import edu.usu.sdl.openstorefront.core.view.ManagerView;
import edu.usu.sdl.openstorefront.core.view.SystemStatusView;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.io.DefaultTemplateImporter;
import edu.usu.sdl.openstorefront.service.io.LookupImporter;
import edu.usu.sdl.openstorefront.service.manager.AsyncTaskManager;
import edu.usu.sdl.openstorefront.service.manager.ConfluenceManager;
import edu.usu.sdl.openstorefront.service.manager.DBLogManager;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import edu.usu.sdl.openstorefront.service.manager.LDAPManager;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.service.manager.OsgiManager;
import edu.usu.sdl.openstorefront.service.manager.PluginManager;
import edu.usu.sdl.openstorefront.service.manager.ReportManager;
import edu.usu.sdl.openstorefront.service.manager.SearchServerManager;
import edu.usu.sdl.openstorefront.service.manager.UnitManager;
import edu.usu.sdl.openstorefront.service.manager.UserAgentManager;
import edu.usu.sdl.openstorefront.service.manager.WorkPlanManager;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import net.java.truevfs.access.TVFS;
import net.sourceforge.stripes.util.ResolverUtil;

/**
 * Used to init the application
 *
 * @author dshurtleff
 */
public class CoreSystem
		implements SystemManager
{

	private static final Logger LOG = Logger.getLogger(CoreSystem.class.getName());

	private static AtomicBoolean started = new AtomicBoolean(false);
	private static String systemStatus = "Starting application...";
	private static String detailedStatus;

	public CoreSystem()
	{
	}

	//Order is important
	private static List<Initializable> managers = Arrays.asList(
			PropertiesManager.getInstance(),
			OsgiManager.getInstance(),
			FileSystemManager.getInstance(),
			DBManager.getInstance(),
			SearchServerManager.getInstance(),
			new OSFCacheManager(),
			new ConfluenceManager(),
			new LookupImporter(),
			new MailManager(),
			new JobManager(),
			new UserAgentManager(),
			new AsyncTaskManager(),
			new ReportManager(),
			new LDAPManager(),
			new DBLogManager(),
			new DefaultTemplateImporter(),
			new PluginManager(),
			new UnitManager(),
			WorkPlanManager.getInstance()
	);

	/**
	 * Don't throw errors in post Constructs as that puts the app server in a
	 * bad state.
	 */
	@PostConstruct
	public void construct()
	{
		//Call init to separately to control timing.
		LOG.log(Level.FINER, "Core Service constructed");
	}

	public void startup(StartupHandler startupHandler)
	{
		started.set(false);
		systemStatus = "Starting application...";
		SecurityUtil.initSystemUser();
		boolean startedAllManagers = loadAllManagers();
		boolean allInitsApplied = true;
		if (startedAllManagers) {
			allInitsApplied = appyInits(ApplyOnceInit.class);
		}
		if (startedAllManagers
				&& allInitsApplied) {
			systemStatus = "Application is Ready";
			detailedStatus = "Application started successfully";
			started.set(true);

			if (startupHandler != null) {
				startupHandler.postStartupHandler();
			}

			//run post int after everything else is started
			Timer t = new java.util.Timer();
			t.schedule(
					new java.util.TimerTask()
			{
				@Override
				public void run()
				{
					appyInits(PostInitApplyOnce.class);
					t.cancel();
				}
			},
					100
			);

		}
	}

	private static boolean loadAllManagers()
	{
		boolean startedAllManagers = true;
		for (Initializable manager : managers) {
			try {
				startupManager(manager);
			} catch (Exception e) {
				LOG.log(Level.SEVERE, "Application Failed to Initailize.  Check config and see trace.", e);
				systemStatus = "Failed Starting : " + manager.getClass().getSimpleName();
				detailedStatus = "Failure Trace: <br>" + StringProcessor.parseStackTraceHtml(e);
				startedAllManagers = false;
				break;
			}
		}
		return startedAllManagers;
	}

	@SuppressWarnings("squid:S1872")
	private static boolean appyInits(Class applyTypeClass)
	{
		boolean allInitsCreated = true;

		ResolverUtil resolverUtil = new ResolverUtil();
		resolverUtil.find(new ResolverUtil.IsA(applyTypeClass), "edu.usu.sdl.core.init");

		List<ApplyOnceInit> initSetups = new ArrayList<>();
		for (Object testObject : resolverUtil.getClasses()) {
			Class testClass = (Class) testObject;
			if (ApplyOnceInit.class.getSimpleName().equals(applyTypeClass.getSimpleName())) {
				if (ReflectionUtil.isSubClass(PostInitApplyOnce.class.getSimpleName(), testClass)) {
					break;
				}
			}
			try {
				if (ApplyOnceInit.class.getSimpleName().equals(testClass.getSimpleName()) == false
						&& PostInitApplyOnce.class.getSimpleName().equals(testClass.getSimpleName()) == false) {
					systemStatus = "Checking data init: " + testClass.getSimpleName();
					initSetups.add((ApplyOnceInit) testClass.newInstance());
				}
			} catch (InstantiationException | IllegalAccessException ex) {
				LOG.log(Level.SEVERE, "Failed to create apply once: " + testClass.getSimpleName(), ex);
				systemStatus = "Failed to create apply once: " + testClass.getSimpleName();
				detailedStatus = "Failure Trace: <br>" + StringProcessor.parseStackTraceHtml(ex);

				allInitsCreated = false;
			}
		}

		boolean allInitsApplied = true;
		if (allInitsCreated) {
			initSetups.sort((a, b) -> {
				return new Integer(a.getPriority()).compareTo(b.getPriority());
			});
			for (ApplyOnceInit applyOnceInit : initSetups) {
				try {
					applyOnceInit.applyChanges();
				} catch (Exception e) {
					LOG.log(Level.SEVERE, "Failed to apply: " + applyOnceInit.getClass().getSimpleName(), e);
					systemStatus = "Failed to apply: " + applyOnceInit.getClass().getSimpleName();
					detailedStatus = "Failure Trace: <br>" + StringProcessor.parseStackTraceHtml(e);

					allInitsApplied = false;
					//Drop out as migration may have order dependancies
					break;
				}
			}
		}
		return allInitsApplied;
	}

	private static void startupManager(Initializable initializable)
	{
		LOG.log(Level.INFO, MessageFormat.format("Starting up:{0}", initializable.getClass().getSimpleName()));
		systemStatus = MessageFormat.format("Starting up:{0}", initializable.getClass().getSimpleName());
		initializable.initialize();
	}

	private static void shutdownManager(Initializable initializable)
	{
		//On shutdown we want it to roll through
		LOG.log(Level.INFO, MessageFormat.format("Shutting down:{0}", initializable.getClass().getSimpleName()));
		systemStatus = MessageFormat.format("Shutting down:{0}", initializable.getClass().getSimpleName());
		try {
			initializable.shutdown();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Unable to Shutdown: " + initializable.getClass().getSimpleName(), e);
		}
	}

	public static List<ManagerView> getManagersView()
	{
		List<ManagerView> views = new ArrayList<>();

		int order = 1;
		for (Initializable manager : managers) {
			ManagerView view = new ManagerView();
			view.setName(manager.getClass().getSimpleName());
			view.setManagerClass(manager.getClass().getName());
			view.setOrder(order++);
			view.setStarted(manager.isStarted());
			views.add(view);
		}

		return views;
	}

	public static void startManager(String managerClassName)
	{
		Initializable manager = findManager(managerClassName, false);
		if (manager.isStarted() == false) {
			systemStatus = MessageFormat.format("Starting up:{0}", managerClassName);
			manager.initialize();
			LOG.log(Level.INFO, () -> "Started: " + managerClassName);
		}
	}

	public static Initializable findManager(String managerClassName, boolean skipFoundCheck)
	{
		Initializable foundManger = null;
		for (Initializable manager : managers) {
			if (manager.getClass().getName().equals(managerClassName)) {
				foundManger = manager;
				break;
			}
		}

		if (foundManger == null && !skipFoundCheck) {
			throw new OpenStorefrontRuntimeException("Unable to find manger: " + managerClassName, "Check input");
		}

		return foundManger;
	}

	public static void stopManager(String managerClassName)
	{
		Initializable manager = findManager(managerClassName, false);
		if (manager.isStarted()) {
			systemStatus = MessageFormat.format("Shutting down:{0}", managerClassName);
			manager.shutdown();
			LOG.log(Level.INFO, () -> "Stopped: " + managerClassName);
		}
	}

	public static void restartManager(String managerClassName)
	{
		//pause scheduler reduce auto process interference
		Initializable jobManager = findManager(JobManager.class.getName(), false);
		started.set(false);

		if (jobManager.isStarted()) {
			JobManager.pauseScheduler();
		}
		try {
			stopManager(managerClassName);
			startManager(managerClassName);
		} catch (Exception e) {
			throw new OpenStorefrontRuntimeException("Unable to restart manager: " + managerClassName, "Confirm application state and try or if system is in a bad state. Restart application.");
		}

		if (jobManager.isStarted()) {
			JobManager.resumeScheduler();
		}
		started.set(true);
	}

	@PreDestroy
	@SuppressWarnings("UseSpecificCatch")
	public void shutdown()
	{
		started.set(false);
		systemStatus = "Application is shutting down...";
		try {
			LOG.log(Level.INFO, "Unmount Truevfs");
			TVFS.umount();
		} catch (Exception e) {
			//Critical section
			LOG.log(Level.SEVERE, MessageFormat.format("Failed to unmount: {0}", e.getMessage()));
		}

		//Shutdown in reverse order to make sure the dependancies are good.
		Collections.reverse(managers);
		managers.forEach(manager -> {
			shutdownManager(manager);
		});
		Collections.reverse(managers);
		systemStatus = "Application is shutdown";
	}

	public static boolean isStarted()
	{
		boolean running = false;
		if (started.get()) {
			running = true;
		}
		return running;
	}

	public void restart()
	{
		if (isStarted()) {
			shutdown();
		}
		startup(null);
	}

	public static void standby(String message)
	{
		started.set(false);
		systemStatus = message != null ? message : "Standby...";
	}

	public static void resume(String message)
	{
		started.set(true);
		systemStatus = message != null ? message : "Application is Ready";
	}

	public static SystemStatusView getStatus()
	{
		SystemStatusView view = new SystemStatusView();
		view.setStarted(started.get());
		view.setSystemStatus(systemStatus);
		view.setDetailedStatus(detailedStatus);
		return view;
	}

	public static void setDetailedStatus(String aDetailedStatus)
	{
		detailedStatus = aDetailedStatus;
	}

	@Override
	public boolean managerStarted()
	{
		return CoreSystem.isStarted();
	}

}
