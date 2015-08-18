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
package edu.usu.sdl.openstorefront.core.sort;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 * @param <T>
 */
public class BeanComparator<T>
		implements Comparator<T>
{

	private static final Logger log = Logger.getLogger(BeanComparator.class.getName());

	private final String sortDirection;
	private final String sortField;

	public BeanComparator(String sortDirection, String sortField)
	{
		this.sortDirection = sortDirection;
		this.sortField = sortField;
	}

	@Override
	public int compare(T o1, T o2)
	{
		T obj1 = o1;
		T obj2 = o2;
		if (OpenStorefrontConstant.SORT_ASCENDING.equals(sortDirection)) {
			obj1 = o2;
			obj2 = o1;
		}

		if (obj1 != null && obj2 == null) {
			return 1;
		} else if (obj1 == null && obj2 != null) {
			return -1;
		} else if (obj1 != null && obj2 != null) {
			if (StringUtils.isNotBlank(sortField)) {
				try {
					Object o = obj1;
					Class<?> c = o.getClass();

					Field f = c.getDeclaredField(sortField);
					f.setAccessible(true);
					if (f.get(o) instanceof Date) {
						int compare = 0;
						DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
						if (BeanUtils.getProperty(obj1, sortField) != null && BeanUtils.getProperty(obj2, sortField) == null) {
							return 1;
						} else if (BeanUtils.getProperty(obj1, sortField) == null && BeanUtils.getProperty(obj2, sortField) != null) {
							return -1;
						} else if (BeanUtils.getProperty(obj1, sortField) != null && BeanUtils.getProperty(obj2, sortField) != null) {
							Date value1 = format.parse(BeanUtils.getProperty(obj1, sortField));
							Date value2 = format.parse(BeanUtils.getProperty(obj2, sortField));
							if (value1 != null && value2 == null) {
								return 1;
							} else if (value1 == null && value2 != null) {
								return -1;
							} else if (value1 != null && value2 != null) {

								compare = value1.compareTo(value2);
							}
						}
						return compare;
					} else {
						try {
							String value1 = BeanUtils.getProperty(obj1, sortField);
							String value2 = BeanUtils.getProperty(obj2, sortField);
							if (value1 != null && value2 == null) {
								return 1;
							} else if (value1 == null && value2 != null) {
								return -1;
							} else if (value1 != null && value2 != null) {

								if (StringUtils.isNotBlank(value1)
										&& StringUtils.isNotBlank(value2)
										&& StringUtils.isNumeric(value1)
										&& StringUtils.isNumeric(value2)) {

									BigDecimal numValue1 = new BigDecimal(value1);
									BigDecimal numValue2 = new BigDecimal(value2);
									return numValue1.compareTo(numValue2);
								} else {
									return value1.toLowerCase().compareTo(value2.toLowerCase());
								}
							}
						} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
							log.log(Level.FINER, MessageFormat.format("Sort field doesn''t exist: {0}", sortField));
						}
					}
				} catch (ParseException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
					try {
						String value1 = BeanUtils.getProperty(obj1, sortField);
						String value2 = BeanUtils.getProperty(obj2, sortField);
						if (value1 != null && value2 == null) {
							return 1;
						} else if (value1 == null && value2 != null) {
							return -1;
						} else if (value1 != null && value2 != null) {

							if (StringUtils.isNotBlank(value1)
									&& StringUtils.isNotBlank(value2)
									&& StringUtils.isNumeric(value1)
									&& StringUtils.isNumeric(value2)) {

								BigDecimal numValue1 = new BigDecimal(value1);
								BigDecimal numValue2 = new BigDecimal(value2);
								return numValue1.compareTo(numValue2);
							} else {
								return value1.toLowerCase().compareTo(value2.toLowerCase());
							}
						}
					} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex2) {
						log.log(Level.FINER, MessageFormat.format("Sort field doesn''t exist: {0}", sortField));
					}
				}
			}
		}
		return 0;
	}
}
