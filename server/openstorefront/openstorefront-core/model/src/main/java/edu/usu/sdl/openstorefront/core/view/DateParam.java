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
package edu.usu.sdl.openstorefront.core.view;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * Wraps a date for use with QueryParam accepts: yyyy-MM-dd'T'HH:mm:ss.sss unix
 * epoch milliseconds MM-dd-yyyy yyyy-MM-dd
 *
 * @author dshurtleff
 */
public class DateParam
{

	private static final Logger log = Logger.getLogger(DateParam.class.getName());

	private Date date;

	public DateParam(String input)
	{
		if (StringUtils.isBlank(input)) {
			date = null;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
			try {
				date = sdf.parse(input);
			} catch (ParseException e) {
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date = sdf.parse(input);
				} catch (ParseException ex) {
					sdf = new SimpleDateFormat("MM/dd/yyyy");
					try {
						date = sdf.parse(input);
					} catch (ParseException exc) {
						if (StringUtils.isNumeric(input)) {
							try {
								date = new Date(Long.parseLong(input));
							} catch (Exception exception) {
								//Don't throw error here it's to low level. (it will get wrapped several times)
								//if the date is not expected to be null handle error there.
								log.log(Level.FINE, MessageFormat.format("Unsupport date format: {0}", input));
							}
						} else {
							log.log(Level.FINE, MessageFormat.format("Unsupport date format: {0}", input));
						}
					}
				}
			}
		}
	}

	public Date getDate()
	{
		return date;
	}

}
