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

import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
import org.apache.commons.beanutils.BeanUtils;

/**
 * Reflection and Service related methods
 *
 * @author dshurtleff
 */
public class ServiceUtil
{

	private static final Logger log = Logger.getLogger(ServiceUtil.class.getName());

	public static final String LOOKUP_ENTITY = "LookupEntity";
	public static final String BASECOMPONENT_ENTITY = "BaseComponent";
	public static final String COMPOSITE_KEY_SEPERATOR = "#";
	public static final String COMPOSITE_KEY_REPLACER = "~";

	/**
	 * This check for Value Model Objects
	 *
	 * @param fieldClass
	 * @return
	 */
	public static boolean isComplexClass(Class fieldClass)
	{
		Objects.requireNonNull(fieldClass, "Class is required");

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
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Queue.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(Set.class.getSimpleName())
				&& !fieldClass.getSimpleName().equalsIgnoreCase(BigInteger.class.getSimpleName())) {
			complex = true;
		}
		return complex;
	}

	/**
	 * Check for class to see if it's a collection class
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
	 * Compares to object of the same type
	 *
	 * @param original
	 * @param compare
	 * @param consumeFieldsOnly
	 * @return True is different, false if the same
	 */
	public static boolean isObjectsDifferent(Object original, Object compare, boolean consumeFieldsOnly)
	{
		boolean changed = false;

		if (original != null && compare == null) {
			changed = true;
		} else if (original == null && compare != null) {
			changed = true;
		} else if (original != null && compare != null) {
			if (original.getClass().isInstance(compare)) {
				List<Field> fields = getAllFields(original.getClass());
				for (Field field : fields) {
					boolean check = true;
					if (consumeFieldsOnly) {
						ConsumeField consume = (ConsumeField) field.getAnnotation(ConsumeField.class);
						if (consume == null) {
							check = false;
						}
					}
					if (check) {
						try {
							changed = isFieldsDifferent(BeanUtils.getProperty(original, field.getName()), BeanUtils.getProperty(compare, field.getName()));
							if (changed) {
								break;
							}
						} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
							throw new OpenStorefrontRuntimeException("Can't compare object types", ex);
						}
					}
				}
			} else {
				throw new OpenStorefrontRuntimeException("Can't compare different object types", "Check objects");
			}
		}
		return changed;
	}

	/**
	 * Compares to consume fields of two object of the same type Note this won't
	 * work with proxy object.
	 *
	 * @param original
	 * @param compare
	 * @return compare value (0 if equals)
	 */
	public static int compareConsumeFields(Object original, Object compare)
	{
		int value = 0;
		if (original != null && compare == null) {
			value = -1;
		} else if (original == null && compare != null) {
			value = 1;
		} else if (original != null && compare != null) {
			if (original.getClass().isInstance(compare)) {
				List<Field> fields = getAllFields(original.getClass());
				for (Field field : fields) {
					ConsumeField consume = (ConsumeField) field.getAnnotation(ConsumeField.class);
					if (consume != null) {
						try {
							field.setAccessible(true);
							value = compareObjects((Comparable) field.get(original), (Comparable) field.get(compare));
							if (value != 0) {
								break;
							}
						} catch (IllegalArgumentException | IllegalAccessException ex) {
							throw new OpenStorefrontRuntimeException("Can't compare object fields", ex);
						}
					}
				}
			} else {
				throw new OpenStorefrontRuntimeException("Can't compare different object types", "Check objects");
			}
		}
		return value;
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
	 * Finds the PK field of an entity (there should only be one)
	 *
	 * @param <T>
	 * @param entity
	 * @return the PK field or null if not found
	 */
	public static <T extends BaseEntity> Field getPKField(T entity)
	{
		Objects.requireNonNull(entity, "Entity must not be NULL");
		Field pkField = null;

		List<Field> fields = getAllFields(entity.getClass());
		for (Field field : fields) {
			PK idAnnotation = field.getAnnotation(PK.class);
			if (idAnnotation != null) {
				pkField = field;
			}
		}
		return pkField;
	}

	public static <T extends BaseEntity> boolean isPKFieldGenerated(T entity)
	{
		boolean generated = false;

		Field field = getPKField(entity);
		if (field != null) {
			PK idAnnotation = field.getAnnotation(PK.class);
			if (idAnnotation != null) {
				generated = idAnnotation.generated();
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find PK for enity: " + entity.getClass().getName(), "Check entity passed in.");
		}

		return generated;
	}

	/**
	 * Get the value of the PK field
	 *
	 * @param <T>
	 * @param entity
	 * @return PK value or a String key for composite PKs
	 */
	public static <T extends BaseEntity> String getPKFieldValue(T entity)
	{
		String value = null;
		Field field = getPKField(entity);
		if (field != null) {
			field.setAccessible(true);
			Object pkValue;
			try {
				pkValue = field.get(entity);
				if (pkValue != null) {
					value = pkValue.toString();
				}
			} catch (IllegalArgumentException | IllegalAccessException ex) {
				throw new OpenStorefrontRuntimeException("Unable to get value on " + entity.getClass().getName(), "Check entity passed in.");
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find PK for enity: " + entity.getClass().getName(), "Check entity passed in.");
		}
		return value;
	}

	/**
	 * This only support updating NON-composite keys.
	 *
	 * @param <T>
	 * @param entity
	 * @param value
	 */
	public static <T extends BaseEntity> void updatePKFieldValue(T entity, String value)
	{
		Field field = getPKField(entity);
		if (field != null) {
			if (isSubClass("BasePK", field.getType()) == false) {
				try {
					field.setAccessible(true);
					field.set(entity, value);
				} catch (IllegalArgumentException | IllegalAccessException ex) {
					throw new OpenStorefrontRuntimeException("Unable to set value on " + entity.getClass().getName(), "Check entity passed in.");
				}
			} else {
				throw new OpenStorefrontRuntimeException("Set value on Composite PK is not supported. Entity:  " + entity.getClass().getName(), "Check entity passed in.");
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find PK for enity: " + entity.getClass().getName(), "Check entity passed in.");
		}
	}

}
