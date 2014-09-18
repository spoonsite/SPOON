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

/**
 * Methods for Converting Values. Typically from External Input.
 *
 * @author dshurtleff
 */
public class Convert
{

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

}
