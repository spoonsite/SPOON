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
package edu.usu.sdl.openstorefront.validation;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

/**
 * This will validate an entity by using the bean validation annotations on the
 * entity
 *
 * @author dshurtleff
 */
public class ValidationUtil
{

	private static final Logger log = Logger.getLogger(ValidationUtil.class.getName());

	private static final List<BaseRule> rules = Arrays.asList(
			new MaxValueRule(),
			new MinValueRule(),
			new PatternRule(),
			new UniqueRule(),
			new RequiredRule(),
			new SizeRule(),
			new ForeignKeyRule(),
			new ValidValueRule()
	);

	private ValidationUtil()
	{
	}

	public static boolean isValid(ValidationModel validateModel)
	{
		return validate(validateModel).valid();
	}

	public static ValidationResult validate(ValidationModel validateModel)
	{
		Objects.requireNonNull(validateModel);

		ValidationResult validationResult = new ValidationResult();
		if (validateModel.getDataObject() == null
				&& validateModel.isAcceptNull() == false) {
			RuleResult ruleResult = new RuleResult();
			ruleResult.setMessage("The whole data object is null.");
			ruleResult.setValidationRule("Don't allow null object");
			validationResult.getRuleResults().add(ruleResult);
		} else {
			if (validateModel.getDataObject() != null) {
				if (validateModel.getDataObject() instanceof Collection) {
					((Collection) validateModel.getDataObject()).stream().forEach((dataObject) -> {
						validationResult.getRuleResults().addAll(validate(ValidationModel.copy(validateModel, dataObject)).getRuleResults());
					});
				} else {
					if (validateModel.getApplyDefaults() && validateModel.getDataObject() instanceof BaseEntity) {
						BaseEntity baseEntity = (BaseEntity) validateModel.getDataObject();
						baseEntity.applyDefaultValues();
					}
					validationResult.getRuleResults().addAll(validateFields(validateModel, validateModel.getDataObject().getClass(), null, null));
				}
			}
		}
		return validationResult;
	}

	private static List<RuleResult> validateFields(final ValidationModel validateModel, Class dataClass, String parentFieldName, String parentType)
	{
		List<RuleResult> ruleResults = new ArrayList<>();

		if (validateModel.getDataObject() == null
				&& validateModel.isAcceptNull() == false) {
			RuleResult validationResult = new RuleResult();
			validationResult.setMessage("The whole data object is null.");
			validationResult.setValidationRule("Don't allow null object");
			validationResult.setEntityClassName(parentType);
			validationResult.setFieldName(parentFieldName);
			ruleResults.add(validationResult);
		} else {
			if (validateModel.getDataObject() != null) {
				if (dataClass.getSuperclass() != null) {
					ruleResults.addAll(validateFields(validateModel, dataClass.getSuperclass(), null, null));
				}

				for (Field field : dataClass.getDeclaredFields()) {
					Class fieldClass = field.getType();
					boolean process = true;
					if (validateModel.isConsumeFieldsOnly()) {
						ConsumeField consumeField = (ConsumeField) field.getAnnotation(ConsumeField.class);
						if (consumeField == null) {
							process = false;
						}
					}

					if (process) {
						if (ReflectionUtil.isComplexClass(fieldClass)) {
							//composition class
							if (Logger.class.getName().equals(fieldClass.getName()) == false
									&& fieldClass.isEnum() == false) {
								try {
									Method method = validateModel.getDataObject().getClass().getMethod("get" + StringUtils.capitalize(field.getName()), (Class<?>[]) null);
									Object returnObj = method.invoke(validateModel.getDataObject(), (Object[]) null);
									boolean check = true;
									if (returnObj == null) {
										NotNull notNull = (NotNull) fieldClass.getAnnotation(NotNull.class);
										if (notNull == null) {
											check = false;
										}
									}
									if (check) {
										ruleResults.addAll(validateFields(ValidationModel.copy(validateModel, returnObj), fieldClass, field.getName(), validateModel.getDataObject().getClass().getSimpleName()));
									}
								} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
									throw new OpenStorefrontRuntimeException(ex);
								}
							}
						} else if (fieldClass.getSimpleName().equalsIgnoreCase(List.class.getSimpleName())
								|| fieldClass.getSimpleName().equalsIgnoreCase(Map.class.getSimpleName())
								|| fieldClass.getSimpleName().equalsIgnoreCase(Collection.class.getSimpleName())
								|| fieldClass.getSimpleName().equalsIgnoreCase(Set.class.getSimpleName())) {
							//multi
							if (fieldClass.getSimpleName().equalsIgnoreCase(Map.class.getSimpleName())) {
								try {
									Method method = validateModel.getDataObject().getClass().getMethod("get" + StringUtils.capitalize(field.getName()), (Class<?>[]) null);
									Object returnObj = method.invoke(validateModel.getDataObject(), (Object[]) null);
									Map mapObj = (Map) returnObj;
									for (Object entryObj : mapObj.entrySet()) {
										ruleResults.addAll(validateFields(ValidationModel.copy(validateModel, entryObj), entryObj.getClass(), field.getName(), validateModel.getDataObject().getClass().getSimpleName()));
									}
								} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
									throw new OpenStorefrontRuntimeException(ex);
								}
							} else {
								try {
									Method method = validateModel.getDataObject().getClass().getMethod("get" + StringUtils.capitalize(field.getName()), (Class<?>[]) null);
									Object returnObj = method.invoke(validateModel.getDataObject(), (Object[]) null);
									if (returnObj != null) {
										for (Object itemObj : (Collection) returnObj) {
											if (itemObj != null) {
												if (ReflectionUtil.isComplexClass(itemObj.getClass())) {
													ruleResults.addAll(validateFields(ValidationModel.copy(validateModel, itemObj), itemObj.getClass(), field.getName(), validateModel.getDataObject().getClass().getSimpleName()));
												}
											} else {
												log.log(Level.WARNING, "There is a NULL item in a collection.  Check data passed in to validation.");
											}
										}
									} else {
										NotNull notNull = field.getAnnotation(NotNull.class);
										if (notNull != null) {
											RuleResult ruleResult = new RuleResult();
											ruleResult.setMessage("Collection is required");
											ruleResult.setEntityClassName(validateModel.getDataObject().getClass().getSimpleName());
											ruleResult.setFieldName(field.getName());
											ruleResult.setValidationRule("Requires value");
											ruleResults.add(ruleResult);
										}
									}
								} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
									throw new OpenStorefrontRuntimeException(ex);
								}
							}

						} else {
							//simple case
							for (BaseRule rule : rules) {
								//Stanize if requested
								if (validateModel.getSantize()) {
									Sanitize santizers = field.getAnnotation(Sanitize.class);
									if (santizers != null) {
										for (Class<? extends Sanitizer> sanitizeClass : santizers.value()) {
											try {
												Sanitizer santizer = sanitizeClass.newInstance();

												Method method = dataClass.getMethod("get" + StringUtils.capitalize(field.getName()), (Class<?>[]) null);
												Object returnObj = method.invoke(validateModel.getDataObject(), (Object[]) null);

												Object newValue = santizer.santize(returnObj);

												method = dataClass.getMethod("set" + StringUtils.capitalize(field.getName()), String.class);
												method.invoke(validateModel.getDataObject(), newValue);

											} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
												throw new OpenStorefrontRuntimeException(ex);
											}
										}
									}
								}

								RuleResult validationResult = rule.processField(field, validateModel.getDataObject());
								if (validationResult != null) {
									ruleResults.add(validationResult);
								}
							}
						}
					}
				}
			}
		}

		return ruleResults;
	}

}
