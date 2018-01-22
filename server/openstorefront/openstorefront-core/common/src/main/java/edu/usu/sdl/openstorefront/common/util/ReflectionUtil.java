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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Reflection and Service related methods
 *
 * @author dshurtleff
 */
public class ReflectionUtil
{

	private static final Logger log = Logger.getLogger(ReflectionUtil.class.getName());

	public static final String LOOKUP_ENTITY = "LookupEntity";
	public static final String BASECOMPONENT_ENTITY = "BaseComponent";
	public static final String COMPOSITE_KEY_SEPERATOR = "#";
	public static final String COMPOSITE_KEY_REPLACER = "~";

	/**
	 * This check for Value Model Objects <br>
	 * <b> WARNING: </b>
	 * Does not support Collection subtypes
	 *
	 * @param fieldClass
	 * @return
	 */
	public static boolean isComplexClass(Class fieldClass)
	{
		Objects.requireNonNull(fieldClass, "Class is required");

		boolean complex = false;
		if (!fieldClass.isPrimitive()
				&& !fieldClass.isEnum()
				&& !fieldClass.isArray()
				&& !fieldClass.getSimpleName().equalsIgnoreCase(String.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Long.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Short.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Character.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Byte.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Integer.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Boolean.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Double.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Float.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(BigDecimal.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Date.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(List.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Map.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Collection.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Queue.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Set.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(BigInteger.class.getSimpleName())) {
			complex = true;
		}
		return complex;
	}

	/**
	 * Check for class to see if it's a collection class <br>
	 * <b> Warning: </b>
	 * Does not support Collection subtypes
	 *
	 * @param checkClass
	 * @return
	 */
	public static boolean isCollectionClass(Class checkClass)
	{
		Objects.requireNonNull(checkClass, "Class is required");

		boolean collection = false;
		if (checkClass.getSimpleName().equalsIgnoreCase(List.class.getSimpleName())
				|| checkClass.getSimpleName().equalsIgnoreCase(Map.class.getSimpleName())
				|| checkClass.getSimpleName().equalsIgnoreCase(Collection.class.getSimpleName())
				|| checkClass.getSimpleName().equalsIgnoreCase(Queue.class.getSimpleName())
				|| checkClass.getSimpleName().equalsIgnoreCase(Set.class.getSimpleName())) {
			collection = true;
		}
		return collection;
	}

	/**
	 * This gets all declared field of the whole object hierarchy
	 *
	 * @param typeClass
	 * @return
	 */
	public static List<Field> getAllFields(Class typeClass)
	{
		Objects.requireNonNull(typeClass, "Class is required");

		List<Field> fields = new ArrayList<>();
		if (typeClass.getSuperclass() != null) {
			fields.addAll(getAllFields(typeClass.getSuperclass()));
		}
		for (Field field : typeClass.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers()) == false
					&& Modifier.isFinal(field.getModifiers()) == false) {
				fields.add(field);
			}
		}
		return fields;
	}

	/**
	 * This checks that an entity is a lookup entity
	 *
	 * @param entityClass
	 * @return
	 */
	public static boolean isSubLookupEntity(Class entityClass)
	{
		return isSubClass(LOOKUP_ENTITY, entityClass);
	}

	/**
	 * This checks class name to determine if a given class is subtype of the
	 * class name;
	 *
	 * @param className
	 * @param entityClass
	 * @return
	 */
	public static boolean isSubClass(String className, Class entityClass)
	{
		if (entityClass == null) {
			return false;
		}

		if (className.equals(entityClass.getSimpleName())) {
			return true;
		} else {
			return isSubClass(className, entityClass.getSuperclass());
		}
	}

	/**
	 * Check to see if two fields are different
	 *
	 * @param original
	 * @param newField
	 * @return True if different and false if the same
	 */
	public static boolean isFieldsDifferent(Object original, Object newField)
	{
		boolean changed = false;
		if (original != null && newField == null) {
			changed = true;
		} else if (original == null && newField != null) {
			changed = true;
		} else if (original != null && newField != null) {
			changed = !(original.equals(newField));
		}
		return changed;
	}

	/**
	 * Compares two comparable objects of the same type
	 *
	 * @param <T>
	 * @param origin
	 * @param other
	 * @return
	 */
	public static <T extends Comparable<T>> int compareObjects(T origin, T other)
	{
		int changed = 0;
		if (origin != null && other == null) {
			changed = -1;
		} else if (origin == null && other != null) {
			changed = 1;
		} else if (origin != null && other != null) {
			changed = origin.compareTo(other);
		}
		return changed;
	}

	/**
	 * Finds the Field given an object
	 *
	 * @param entity
	 * @param fieldName (Ignores case) (use the returned field for the exact
	 * member name)
	 * @return field or null if not found
	 */
	public static Field getField(Object entity, String fieldName)
	{
		Objects.requireNonNull(entity, "Entity must not be NULL");
		Field fieldFound = null;

		List<Field> fields = getAllFields(entity.getClass());
		for (Field field : fields) {
			if (field.getName().equalsIgnoreCase(fieldName)) {
				fieldFound = field;
			}
		}
		return fieldFound;
	}

}
