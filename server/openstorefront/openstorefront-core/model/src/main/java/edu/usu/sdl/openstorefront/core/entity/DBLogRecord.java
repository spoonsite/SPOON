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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import java.util.Date;
import java.util.logging.LogRecord;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Represents a log record")
public class DBLogRecord
		extends BaseEntity<DBLogRecord>
{

	@PK(generated = true)
	private String logId;

	@NotNull
	private String level;

	@NotNull
	private String loggerName;

	private String message;

	@NotNull
	private Date eventDts;
	private Long sequenceNumber;
	private String sourceClass;
	private String sourceMethod;
	private Integer threadId;
	private String stackTrace;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public DBLogRecord()
	{
	}

	public static DBLogRecord fromLogRecord(LogRecord logRecord)
	{
		DBLogRecord dBLogRecord = null;
		if (logRecord != null) {
			dBLogRecord = new DBLogRecord();
			dBLogRecord.setEventDts(new Date(logRecord.getMillis()));
			dBLogRecord.setThreadId(logRecord.getThreadID());
			dBLogRecord.setLevel(logRecord.getLevel().getName());
			dBLogRecord.setLoggerName(logRecord.getLoggerName());
			dBLogRecord.setMessage(logRecord.getMessage());
			dBLogRecord.setSequenceNumber(logRecord.getSequenceNumber());
			dBLogRecord.setSourceClass(logRecord.getSourceClassName());
			dBLogRecord.setSourceMethod(logRecord.getSourceMethodName());

			if (logRecord.getThrown() != null) {
				dBLogRecord.setStackTrace(StringProcessor.parseStackTraceHtml(logRecord.getThrown()));
			}

		}
		return dBLogRecord;
	}

	public String getLogId()
	{
		return logId;
	}

	public void setLogId(String logId)
	{
		this.logId = logId;
	}

	public String getLevel()
	{
		return level;
	}

	public void setLevel(String level)
	{
		this.level = level;
	}

	public String getLoggerName()
	{
		return loggerName;
	}

	public void setLoggerName(String loggerName)
	{
		this.loggerName = loggerName;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public Date getEventDts()
	{
		return eventDts;
	}

	public void setEventDts(Date eventDts)
	{
		this.eventDts = eventDts;
	}

	public Long getSequenceNumber()
	{
		return sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	public String getSourceClass()
	{
		return sourceClass;
	}

	public void setSourceClass(String sourceClass)
	{
		this.sourceClass = sourceClass;
	}

	public String getSourceMethod()
	{
		return sourceMethod;
	}

	public void setSourceMethod(String sourceMethod)
	{
		this.sourceMethod = sourceMethod;
	}

	public Integer getThreadId()
	{
		return threadId;
	}

	public void setThreadId(Integer threadId)
	{
		this.threadId = threadId;
	}

	public String getStackTrace()
	{
		return stackTrace;
	}

	public void setStackTrace(String stackTrace)
	{
		this.stackTrace = stackTrace;
	}

}
