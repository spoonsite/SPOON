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
package edu.usu.sdl.openstorefront.usecase;

import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttributePk;
import edu.usu.sdl.openstorefront.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class ValidationUseCase
{

	@Test
	public void testValidation()
	{
//		TestEntity testEntity = null;
//
//		UserProfile userProfile = new UserProfile();
//		UserTypeCode userTypeCode = new UserTypeCode();
//		userTypeCode.setCode("User");
//		userTypeCode.setDescription("End-User");
//		userProfile.getUserTypeCode().add(userTypeCode);
//
//		boolean valid = ValidationUtil.isValid(new ValidationModel(userProfile));
//		System.out.println("Valid: " + valid);
//
//		ValidationResult validationResult = ValidationUtil.validate(new ValidationModel(userProfile));
//		System.out.println(validationResult.toString());
	}

	@Test
	public void testCopy() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		Component component = new Component();
		component.setName("Test");
		component.setUpdateDts(TimeUtil.currentDate());

		Component destComponent = new Component();

		BeanUtils.copyProperties(destComponent, component);

		ComponentAttribute componentAttribute = new ComponentAttribute();
		ComponentAttributePk componentAttributePk = new ComponentAttributePk();
		componentAttributePk.setAttributeCode("T");
		componentAttributePk.setAttributeType("T2");
		componentAttributePk.setComponentId("T3");
		componentAttribute.setComponentAttributePk(componentAttributePk);

		System.out.println(BeanUtils.describe(componentAttribute));

	}

	@Test
	public void testGenerate()
	{
		ComponentAttribute componentAttribute = new ComponentAttribute();
		ComponentAttributePk componentAttributePk = new ComponentAttributePk();
		componentAttributePk.setAttributeCode("T");
		componentAttributePk.setAttributeType("T2");
		componentAttributePk.setComponentId("T3");
		componentAttribute.setComponentAttributePk(componentAttributePk);

		System.out.println(generateWhereClause(componentAttribute));
		System.out.println(mapParameters(componentAttribute));

		Component component = new Component();
		component.setName("Test");
		component.setUpdateDts(TimeUtil.currentDate());

		System.out.println(generateWhereClause(component));
		System.out.println(mapParameters(component));

		System.out.println("Names: " + generateExampleNames(component));
	}

	private <T> String generateWhereClause(T example)
	{
		return generateWhereClause(example, "");
	}

	private <T> String generateWhereClause(T example, String parentFieldName)
	{
		StringBuilder where = new StringBuilder();

		try {
			Map fieldMap = BeanUtils.describe(example);
			boolean addAnd = false;
			for (Object field : fieldMap.keySet()) {

				if ("class".equalsIgnoreCase(field.toString()) == false) {
					Object value = fieldMap.get(field);
					if (value != null) {

						Method method = example.getClass().getMethod("get" + StringUtils.capitalize(field.toString()), (Class<?>[]) null);
						Object returnObj = method.invoke(example, (Object[]) null);
						if (ReflectionUtil.isComplexClass(returnObj.getClass())) {
							if (StringUtils.isNotBlank(parentFieldName)) {
								parentFieldName = parentFieldName + ".";
							}
							parentFieldName = parentFieldName + field;
							where.append(generateWhereClause(returnObj, parentFieldName));
						} else {
							if (addAnd) {
								where.append(" AND ");
							} else {
								addAnd = true;
								where.append(" ");
							}

							String fieldName = field.toString();
							if (StringUtils.isNotBlank(parentFieldName)) {
								fieldName = parentFieldName + "." + fieldName;
							}

							where.append(fieldName).append(" = :").append(fieldName.replace(".", "_")).append("Param");
						}
					}
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
			throw new RuntimeException(ex);
		}
		return where.toString();
	}

	private <T> Map<String, Object> mapParameters(T example)
	{
		return mapParameters(example, "");
	}

	private <T> Map<String, Object> mapParameters(T example, String parentFieldName)
	{
		Map<String, Object> parameterMap = new HashMap<>();
		try {
			Map fieldMap = BeanUtils.describe(example);
			for (Object field : fieldMap.keySet()) {

				if ("class".equalsIgnoreCase(field.toString()) == false) {
					Object value = fieldMap.get(field);
					if (value != null) {

						Method method = example.getClass().getMethod("get" + StringUtils.capitalize(field.toString()), (Class<?>[]) null);
						Object returnObj = method.invoke(example, (Object[]) null);
						if (ReflectionUtil.isComplexClass(returnObj.getClass())) {
							if (StringUtils.isNotBlank(parentFieldName)) {
								parentFieldName = parentFieldName + "_";
							}
							parentFieldName = parentFieldName + field;
							parameterMap.putAll(mapParameters(returnObj, parentFieldName));
						} else {
							String fieldName = field.toString();
							if (StringUtils.isNotBlank(parentFieldName)) {
								fieldName = parentFieldName + "_" + fieldName;
							}
							parameterMap.put(fieldName + "Param", value);
						}
					}
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
			throw new RuntimeException(ex);
		}
		return parameterMap;
	}

	private <T> String generateExampleNames(T example)
	{
		return generateExampleNames(example, "");
	}

	private <T> String generateExampleNames(T example, String parentFieldName)
	{
		StringBuilder where = new StringBuilder();

		try {
			Map fieldMap = BeanUtils.describe(example);
			boolean addAnd = false;
			for (Object field : fieldMap.keySet()) {

				if ("class".equalsIgnoreCase(field.toString()) == false) {
					Object value = fieldMap.get(field);
					if (value != null) {

						Method method = example.getClass().getMethod("get" + StringUtils.capitalize(field.toString()), (Class<?>[]) null);
						Object returnObj = method.invoke(example, (Object[]) null);
						if (ReflectionUtil.isComplexClass(returnObj.getClass())) {
							if (StringUtils.isNotBlank(parentFieldName)) {
								parentFieldName = parentFieldName + ".";
							}
							parentFieldName = parentFieldName + field;
							if (addAnd) {
								where.append(",");
							} else {
								addAnd = true;
								where.append(" ");
							}

							where.append(generateWhereClause(returnObj, parentFieldName));
						} else {
							if (addAnd) {
								where.append(",");
							} else {
								addAnd = true;
								where.append(" ");
							}

							String fieldName = field.toString();
							if (StringUtils.isNotBlank(parentFieldName)) {
								fieldName = parentFieldName + "." + fieldName;
							}

							where.append(fieldName);
						}
					}
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
			throw new RuntimeException(ex);
		}
		return where.toString();
	}

}
