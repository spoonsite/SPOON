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

import java.math.BigDecimal;
import java.math.BigInteger;

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
					return (Long) data;
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

}
