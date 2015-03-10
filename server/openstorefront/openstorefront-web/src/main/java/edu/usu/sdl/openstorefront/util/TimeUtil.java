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
package edu.usu.sdl.openstorefront.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;

/**
 * Useful method dealing with time. Helps keep the time handling centralized.
 *
 * @author dshurtleff
 */
public class TimeUtil
{

	private static final String OMP_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	public static Date fromString(String value)
	{
		Date newDate = null;
		if (StringUtils.isNotBlank(value)) {
			SimpleDateFormat sdf = new SimpleDateFormat(OMP_DATE_FORMAT);
			newDate = sdf.parse(value, new ParsePosition(0));
		}
		return newDate;
	}

	public static String dateToString(Date value)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(OMP_DATE_FORMAT);
		return sdf.format(value);
	}

	public static Date currentDate()
	{
		return new Date(System.currentTimeMillis());
	}

	public static long daysToMillis(long days)
	{
		long daysInMillis = days * 24L * 60L * 60000L;
		return daysInMillis;
	}

	public static Date beginningOfDay(Date date)
	{
		Instant instant = Instant.ofEpochMilli(date.getTime()).truncatedTo(ChronoUnit.DAYS);
		return new Date(instant.toEpochMilli());
	}

	public static Date endOfDay(Date date)
	{
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		localDateTime = localDateTime.withHour(23)
				.withMinute(59)
				.withSecond(59)
				.with(ChronoField.MILLI_OF_SECOND, 999);
		return new Date(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());

	}

	public static String millisToString(long millis)
	{
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - (hours * 60);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - ((hours * 60 * 60) + (minutes * 60));
		millis = millis - ((hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000));
		return String.format("%d hr(s) %d min(s) %d sec(s) %d ms", hours, minutes, seconds, millis);
	}

}
