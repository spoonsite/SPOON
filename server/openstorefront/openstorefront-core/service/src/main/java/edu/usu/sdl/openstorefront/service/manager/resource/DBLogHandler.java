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
package edu.usu.sdl.openstorefront.service.manager.resource;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.entity.DBLogRecord;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Handles pushing records to the Database
 *
 * @author dshurtleff
 */
public class DBLogHandler
		extends Handler
{

	private final ExecutorService asyncLoggerService = Executors.newSingleThreadExecutor();
	private boolean active = true;

	@Override
	public void publish(LogRecord record)
	{
		if (record != null && active) {
			try {
				//Filter Audit logging as it can fill the logs and it's better captured else where
				boolean logSecurityFilter = Convert.toBoolean(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_DBLOG_LOG_SECURITY, "false"));
				if (logSecurityFilter || record.getSourceClassName().equals("edu.usu.sdl.openstorefront.web.rest.SecurityFilter") == false) {
					DBLogRecord logRecord = DBLogRecord.fromLogRecord(record);

					Runnable task = () -> {
						ServiceProxy serviceProxy = new ServiceProxy();
						serviceProxy.getSystemService().addLogRecord(logRecord);
					};
					asyncLoggerService.submit(task);
				}
			} catch (Exception e) {
				getErrorManager().error("Failed to log Record", e, 1);
			}
		}
	}

	@Override
	public void flush()
	{
		//Nothing is Buffered
	}

	@Override
	public void close() throws SecurityException
	{
		active = false;
		asyncLoggerService.shutdown();
		try {
			asyncLoggerService.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException ex) {
			getErrorManager().error("Failed to shutdown db logger", ex, 2);
			Thread.currentThread().interrupt();
		}
	}

}
