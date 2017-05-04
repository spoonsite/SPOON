/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.openstorefront.core.util;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import static edu.usu.sdl.openstorefront.common.util.ReflectionUtil.compareObjects;
import static edu.usu.sdl.openstorefront.common.util.ReflectionUtil.getAllFields;
import static edu.usu.sdl.openstorefront.common.util.ReflectionUtil.isFieldsDifferent;
import static edu.usu.sdl.openstorefront.common.util.ReflectionUtil.isSubClass;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DefaultFieldValue;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class EntityUtil
{

	/**
	 * This will set default on the fields that are marked with a default and
	 * are null
	 *
	 * @param entity
	 */
	public static void setDefaultsOnFields(Object entity)
	{
		Objects.requireNonNull(entity, "Entity must not be NULL");
		List<Field> fields = getAllFields(entity.getClass());
		for (Field field : fields) {
			DefaultFieldValue defaultFieldValue = field.getAnnotation(DefaultFieldValue.class);
			if (defaultFieldValue != null) {
				field.setAccessible(true);
				try {
					if (field.get(entity) == null) {
						String value = defaultFieldValue.value();
						Class fieldClass = field.getType();
						if (fieldClass.getSimpleName().equalsIgnoreCase(String.class.getSimpleName())) {
							field.set(entity, value);
						} else if (fieldClass.getSimpleName().equalsIgnoreCase(Long.class.getSimpleName())) {
							field.set(entity, value);
						} else if (fieldClass.getSimpleName().equalsIgnoreCase(Integer.class.getSimpleName())) {
							field.set(entity, Integer.parseInt(value));
						} else if (fieldClass.getSimpleName().equalsIgnoreCase(Boolean.class.getSimpleName())) {
							field.set(entity, Convert.toBoolean(value));
						} else if (fieldClass.getSimpleName().equalsIgnoreCase(Double.class.getSimpleName())) {
							field.set(entity, Double.parseDouble(value));
						} else if (fieldClass.getSimpleName().equalsIgnoreCase(Float.class.getSimpleName())) {
							field.set(entity, Float.parseFloat(value));
						} else if (fieldClass.getSimpleName().equalsIgnoreCase(BigDecimal.class.getSimpleName())) {
							field.set(entity, Convert.toBigDecimal(value));
						} else if (fieldClass.getSimpleName().equalsIgnoreCase(Date.class.getSimpleName())) {
							field.set(entity, TimeUtil.fromString(value));
						} else if (fieldClass.getSimpleName().equalsIgnoreCase(BigInteger.class.getSimpleName())) {
							field.set(entity, new BigInteger(value));
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException ex) {
					throw new OpenStorefrontRuntimeException("Unable to get value on " + entity.getClass().getName(), "Check entity passed in.");
				}
			}
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

		List<Field> fields = ReflectionUtil.getAllFields(entity.getClass());
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

	/**
	 * This correct for an proxy instance which give the proxy class
	 *
	 * @param className
	 * @return
	 */
	public static String getRealClassName(String className)
	{
		if (StringUtils.isNotBlank(className)) {
			if (className.contains("$$")) {
				String tokens[] = className.split("_");
				className = tokens[0];
			}
		}
		return className;
	}

}
