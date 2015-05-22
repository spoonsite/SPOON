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
package edu.usu.sdl.openstorefront.web.test.system;

import edu.usu.sdl.openstorefront.storage.model.DBLogRecord;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class DBLogTest
		extends BaseTestCase
{

	private static final Logger log = Logger.getLogger(DBLogTest.class.getName());

	public DBLogTest()
	{
		this.description = "DB_Log_Test";
	}

	@Override
	protected void runInternalTest()
	{
		long startTime = System.currentTimeMillis();
		log.log(Level.INFO, "TESTING");
		results.append("Log Time: ").append(startTime - System.currentTimeMillis()).append("<br>");

		DBLogRecord logRecord = new DBLogRecord();
		logRecord.setLoggerName("TEST");
		logRecord.setEventDts(TimeUtil.currentDate());
		logRecord.setLevel(Level.FINE.getName());
		logRecord.setMessage("This is just a test record");

		startTime = System.currentTimeMillis();
		service.getSystemService().addLogRecord(logRecord);
		results.append("DB record Time: ").append(System.currentTimeMillis() - startTime).append("<br>");
	}

}
