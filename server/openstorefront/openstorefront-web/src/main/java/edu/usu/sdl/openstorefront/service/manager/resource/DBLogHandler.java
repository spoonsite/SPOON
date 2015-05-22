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

import edu.usu.sdl.openstorefront.storage.model.DBLogRecord;
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

	@Override
	public void publish(LogRecord record)
	{
		if (record != null) {
			DBLogRecord logRecord = DBLogRecord.fromLogRecord(record);
			//need to Bufffer; Handle error *use report error

			//ServiceProxy serviceProxy = new ServiceProxy();
			//serviceProxy.getSystemService().addLogRecord(logRecord);
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
		//Nothing to close
	}

}
