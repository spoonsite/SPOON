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
package edu.usu.sdl.openstorefront.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * Methods for Converting Values. Typically from External Input.
 *
 * @author dshurtleff
 */
public class Convert
{

	private Convert()
	{
	}

	/**
	 * Converts an object to a boolean It's meant for handling different string
	 * representation
	 *
	 * @param data
	 * @return
	 */
	public static boolean toBoolean(Object data)
	{
		if (data != null) {
			if (data instanceof Boolean) {
				return (Boolean) data;
			} else if (data instanceof String) {
				if ("T".equalsIgnoreCase(data.toString().trim())) {
					return Boolean.TRUE;
				} else if ("1".equalsIgnoreCase(data.toString().trim())) {
					return Boolean.TRUE;
				} else {
					return Boolean.parseBoolean(data.toString().trim());
				}
			} else {
				return Boolean.TRUE;
			}

		}
		return Boolean.FALSE;
	}

	/**
	 * Attempts to convert object to an Integer
	 *
	 * @param data
	 * @return the integer or null if it can't convert.
	 */
	public static Integer toInteger(Object data)
	{
		if (data != null) {
			try {
				if (data instanceof Integer) {
					return (Integer) data;
				} else if (data instanceof String) {
					return Integer.parseInt(data.toString());
				} else if (data instanceof BigDecimal) {
					return ((BigDecimal) data).intValue();
				} else if (data instanceof BigInteger) {
					return ((BigInteger) data).intValue();
				} else if (data instanceof Number) {
					return ((Number) data).intValue();
				}
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Attempts to convert object to an Long
	 *
	 * @param data
	 * @return the long or null if it can't convert.
	 */
	public static Long toLong(Object data)
	{
		if (data != null) {
			try {
				if (data instanceof Integer) {
					return Long.parseLong(Integer.toString((Integer) data));
				} else if (data instanceof String) {
					return Long.parseLong(data.toString());
				} else if (data instanceof BigDecimal) {
					return ((BigDecimal) data).longValue();
				} else if (data instanceof BigInteger) {
					return ((BigInteger) data).longValue();
				} else if (data instanceof Number) {
					return ((Number) data).longValue();
				}
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}

	public static BigDecimal toBigDecimal(Object data)
	{
		return toBigDecimal(data, null);
	}

	public static BigDecimal toBigDecimal(Object data, BigDecimal defaultDecimal)
	{
		if (data != null) {
			try {
				if (data instanceof Integer) {
					return BigDecimal.valueOf(((Integer) data).doubleValue());
				} else if (data instanceof String) {
					return new BigDecimal(data.toString());
				} else if (data instanceof BigDecimal) {
					return (BigDecimal) data;
				} else if (data instanceof BigInteger) {
					return BigDecimal.valueOf(((BigInteger) data).longValue());
				} else if (data instanceof Number) {
					return BigDecimal.valueOf(((Number) data).longValue());
				}
			} catch (NumberFormatException e) {
				return defaultDecimal;
			}
		}
		return null;
	}

	/**
	 * Attempts to convert several different formats
	 *
	 * MM/dd/yyyy HH:mm:ss z yyyy-mm-dd HH:mm:ss z yyyy-MM-dd'T'HH:mm:ss.SSS'Z
	 * MM/dd/yyyy yyyy-mm-dd date standard milliseconds
	 *
	 * @param dateString
	 * @return date
	 */
	public static Date toDate(String dateString)
	{
		Date dateConverted = null;

		if (StringUtils.isNotBlank(dateString)) {

			try {
				if (StringUtils.isNumeric(dateString)) {
					dateConverted = new Date(Long.parseLong(dateString));
				} else {

					String formats[] = {
						"MM/dd/yyyy HH:mm:ss z ",
						"yyyy-mm-dd HH:mm:ss z ",
						"yyyy-MM-dd'T'HH:mm:ss.SSS'Z",
						"MM/dd/yyyy",
						"yyyy-mm-dd ",};

					for (String format : formats) {
						SimpleDateFormat sdf = new SimpleDateFormat(format);
						dateConverted = sdf.parse(dateString, new ParsePosition(0));
						if (dateConverted != null) {
							break;
						}
					}
				}

			} catch (Exception e) {
				dateConverted = null;
			}
		}

		return dateConverted;
	}

}
