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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author dshurtleff
 */
public class ServiceUtil
{

	public static final String LOOKUP_ENTITY = "LookupEntity";

	public static boolean isComplexClass(Class fieldClass)
	{
		boolean complex = false;
		if (!fieldClass.isPrimitive()
				&& !fieldClass.isArray()
				&& !fieldClass.getSimpleName().equalsIgnoreCase(String.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Long.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Integer.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Boolean.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Double.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Float.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(BigDecimal.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Date.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(List.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Map.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Collection.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Set.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(BigInteger.class.getSimpleName())) {
			complex = true;
		}
		return complex;
	}

	public static boolean isSubLookupEntity(Class entityClass)
	{
		if (entityClass == null) {
			return false;
		}

		if ("LookupEntity".equals(entityClass.getSimpleName())) {
			return true;
		} else {
			return isSubLookupEntity(entityClass.getSuperclass());
		}
	}
}
