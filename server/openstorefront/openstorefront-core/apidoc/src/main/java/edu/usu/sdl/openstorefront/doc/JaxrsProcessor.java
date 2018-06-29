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
package edu.usu.sdl.openstorefront.doc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ParamTypeDescription;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.doc.annotation.ParameterRestrictions;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.annotation.ReturnType;
import edu.usu.sdl.openstorefront.doc.annotation.ValidationRequirement;
import edu.usu.sdl.openstorefront.doc.model.APIMethodModel;
import edu.usu.sdl.openstorefront.doc.model.APIParamModel;
import edu.usu.sdl.openstorefront.doc.model.APIResourceModel;
import edu.usu.sdl.openstorefront.doc.model.APITypeModel;
import edu.usu.sdl.openstorefront.doc.model.APIValueFieldModel;
import edu.usu.sdl.openstorefront.doc.model.APIValueModel;
import edu.usu.sdl.openstorefront.doc.model.SecurityRestriction;
import edu.usu.sdl.openstorefront.doc.security.CustomRequiredHandler;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.doc.sort.ApiMethodComparator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class JaxrsProcessor
{

	private static final Logger log = Logger.getLogger(JaxrsProcessor.class.getName());

	private JaxrsProcessor()
	{

	}

	@SuppressWarnings({"squid:S1872", "squid:S1905"})
	public static APIResourceModel processRestClass(Class resource, String rootPath)
	{
		APIDescription apiDescription = (APIDescription) resource.getAnnotation(APIDescription.class);

		APIResourceModel resourceModel = createResourceModel(resource, rootPath, apiDescription);

		//class parameters
		mapParameters(resourceModel.getResourceParams(), resource.getDeclaredFields());

		//methods
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		int methodId = 0;

		for (Method method : resource.getMethods()) {

			APIMethodModel methodModel = new APIMethodModel();
			methodModel.setId(methodId++);

			//rest method
			List<String> restMethods = findRestMethods(method);

			methodModel.setRestMethod(String.join(",", restMethods));

			if (restMethods.isEmpty()) {
				//skip non-rest methods
				continue;
			}

			setMethodModel(methodModel, method);

			try {
				if (!(method.getReturnType().getSimpleName().equalsIgnoreCase(Void.class.getSimpleName()))) {
					APIValueModel valueModel = new APIValueModel();
					DataType dataType = method.getAnnotation(DataType.class);

					boolean addResponseObject = true;
					if (method.getReturnType().getName().equals("javax.ws.rs.core.Response")
							&& dataType == null) {
						addResponseObject = false;
					}

					if (addResponseObject) {
						valueModel.setValueObjectName(method.getReturnType().getSimpleName());
						ReturnType returnType = (ReturnType) method.getAnnotation(ReturnType.class);
						Class returnTypeClass;
						if (returnType != null) {
							returnTypeClass = returnType.value();
						} else {
							returnTypeClass = method.getReturnType();
						}

						if (!method.getReturnType().getName().equals("javax.ws.rs.core.Response")
								&& !ReflectionUtil.isCollectionClass(method.getReturnType())) {
							try {
								valueModel.setValueObject(objectMapper.writeValueAsString(returnTypeClass.newInstance()));
								mapValueField(valueModel.getValueFields(), ReflectionUtil.getAllFields(returnTypeClass).toArray(new Field[0]));
								mapComplexTypes(valueModel.getAllComplexTypes(), ReflectionUtil.getAllFields(returnTypeClass).toArray(new Field[0]), false);

								apiDescription = (APIDescription) returnTypeClass.getAnnotation(APIDescription.class);
								if (apiDescription != null) {
									valueModel.setValueDescription(apiDescription.value());
								}
							} catch (InstantiationException iex) {
								log.log(Level.WARNING, MessageFormat.format("Unable to instantiated type: {0} make sure the type is not abstract.", returnTypeClass));
							}
						}

						if (dataType != null) {
							String typeName = dataType.value().getSimpleName();
							if (StringUtils.isNotBlank(dataType.actualClassName())) {
								typeName = dataType.actualClassName();
							}
							valueModel.setTypeObjectName(typeName);
							try {
								valueModel.setTypeObject(objectMapper.writeValueAsString(dataType.value().newInstance()));
								mapValueField(valueModel.getTypeFields(), ReflectionUtil.getAllFields(dataType.value()).toArray(new Field[0]));
								mapComplexTypes(valueModel.getAllComplexTypes(), ReflectionUtil.getAllFields(dataType.value()).toArray(new Field[0]), false);

								apiDescription = (APIDescription) dataType.value().getAnnotation(APIDescription.class);
								if (apiDescription != null) {
									valueModel.setTypeDescription(apiDescription.value());
								}

							} catch (NumberFormatException | InstantiationException iex) {
								log.log(Level.WARNING, MessageFormat.format("Unable to instantiated type: {0} make sure the type is not abstract.", dataType.value()));
							}
						}

						methodModel.setResponseObject(valueModel);
					}
				}
			} catch (IllegalAccessException | JsonProcessingException ex) {
				log.log(Level.WARNING, null, ex);
			}

			//method parameters
			mapMethodParameters(methodModel.getMethodParams(), method.getParameters());

			//Handle Consumed Objects
			mapConsumedObjects(methodModel, method.getParameters());

			resourceModel.getMethods().add(methodModel);
		}
		Collections.sort(resourceModel.getMethods(), new ApiMethodComparator<>());
		return resourceModel;
	}

	private static APIResourceModel createResourceModel(Class resource, String rootPath, APIDescription apiDescription)
	{
		APIResourceModel resourceModel = new APIResourceModel();

		resourceModel.setClassName(resource.getName());
		resourceModel.setResourceName(String.join(" ", StringUtils.splitByCharacterTypeCamelCase(resource.getSimpleName())));

		if (apiDescription != null) {
			resourceModel.setResourceDescription(apiDescription.value());
		}

		Path path = (Path) resource.getAnnotation(Path.class);
		if (path != null) {
			resourceModel.setResourcePath(rootPath + "/" + path.value());
		}

		RequireSecurity requireSecurity = (RequireSecurity) resource.getAnnotation(RequireSecurity.class);
		if (requireSecurity != null) {
			SecurityRestriction securityRestriction = getSecurityRestrictions(requireSecurity);
			resourceModel.setSecurityRestriction(securityRestriction);
		}

		return resourceModel;
	}

	private static void setMethodModel(APIMethodModel methodModel, Method method)
	{
		//produces
		Produces produces = method.getAnnotation(Produces.class);
		if (produces != null) {

			methodModel.setProducesTypes(String.join(",", produces.value()));
		}
		//consumes
		Consumes consumes = method.getAnnotation(Consumes.class);
		if (consumes != null) {

			methodModel.setConsumesTypes(String.join(",", consumes.value()));
		}
		APIDescription apiDescription = method.getAnnotation(APIDescription.class);
		if (apiDescription != null) {

			methodModel.setDescription(apiDescription.value());
		}
		Path path = method.getAnnotation(Path.class);
		if (path != null) {
			methodModel.setMethodPath(path.value());
		}
		RequireSecurity security = method.getAnnotation(RequireSecurity.class);
		if (security != null) {
			SecurityRestriction securityRestriction = getSecurityRestrictions(security);
			methodModel.setSecurityRestriction(securityRestriction);
		}
	}

	private static List<String> findRestMethods(Method method)
	{
		List<String> restMethods = new ArrayList<>();

		GET getMethod = method.getAnnotation(GET.class);
		POST postMethod = method.getAnnotation(POST.class);
		PUT putMethod = method.getAnnotation(PUT.class);
		DELETE deleteMethod = method.getAnnotation(DELETE.class);
		if (getMethod != null) {
			restMethods.add("GET");
		}
		if (postMethod != null) {
			restMethods.add("POST");
		}
		if (putMethod != null) {
			restMethods.add("PUT");
		}
		if (deleteMethod != null) {
			restMethods.add("DELETE");
		}

		return restMethods;
	}

	private static SecurityRestriction getSecurityRestrictions(RequireSecurity requireSecurity)
	{
		SecurityRestriction securityRestriction = new SecurityRestriction();
		if (requireSecurity.value() != null) {
			for (String permission : requireSecurity.value()) {
				securityRestriction.getPermissions().add(permission);
			}
		}
		if (requireSecurity.roles() != null) {
			for (String role : requireSecurity.roles()) {
				securityRestriction.getRoles().add(role);
			}
		}
		securityRestriction.setLogicOperation(requireSecurity.logicOperator().name());
		if (requireSecurity.specialCheck() != null) {
			try {
				CustomRequiredHandler requireHandler = requireSecurity.specialCheck().newInstance();
				securityRestriction.setSpecialCheck(requireHandler.getDescription());
			} catch (InstantiationException | IllegalAccessException ex) {
				log.log(Level.WARNING, "Unable to load special check handler. Check code.", ex);
			}
		}
		return securityRestriction;
	}

	@SuppressWarnings("squid:S3776")
	private static void mapConsumedObjects(APIMethodModel methodModel, Parameter[] parameters)
	{
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		for (Parameter parameter : parameters) {
			//deterimine if this is "Body" object
			List<Annotation> paramAnnotation = new ArrayList<>();
			paramAnnotation.add(parameter.getAnnotation(QueryParam.class));
			paramAnnotation.add(parameter.getAnnotation(FormParam.class));
			paramAnnotation.add(parameter.getAnnotation(MatrixParam.class));
			paramAnnotation.add(parameter.getAnnotation(HeaderParam.class));
			paramAnnotation.add(parameter.getAnnotation(CookieParam.class));
			paramAnnotation.add(parameter.getAnnotation(PathParam.class));
			paramAnnotation.add(parameter.getAnnotation(BeanParam.class));

			boolean consumeObject = true;
			for (Annotation annotation : paramAnnotation) {
				if (annotation != null) {
					consumeObject = false;
					break;
				}
			}

			if (consumeObject) {
				APIValueModel valueModel = new APIValueModel();
				try {
					valueModel.setValueObjectName(parameter.getType().getSimpleName());

					DataType dataType = parameter.getAnnotation(DataType.class);
					if (dataType != null) {
						String typeName = dataType.value().getSimpleName();
						if (StringUtils.isNotBlank(dataType.actualClassName())) {
							typeName = dataType.actualClassName();
						}
						valueModel.setTypeObjectName(typeName);

						try {
							valueModel.setTypeObject(objectMapper.writeValueAsString(dataType.value().newInstance()));
							Set<String> fieldList = mapValueField(valueModel.getTypeFields(), ReflectionUtil.getAllFields(dataType.value()).toArray(new Field[0]), true);
							String cleanUpJson = StringProcessor.stripeFieldJSON(valueModel.getTypeObject(), fieldList);
							valueModel.setTypeObject(cleanUpJson);
							mapComplexTypes(valueModel.getAllComplexTypes(), ReflectionUtil.getAllFields(dataType.value()).toArray(new Field[0]), true);

							APIDescription aPIDescription = (APIDescription) dataType.value().getAnnotation(APIDescription.class);
							if (aPIDescription != null) {
								valueModel.setTypeDescription(aPIDescription.value());
							}

						} catch (InstantiationException iex) {
							log.log(Level.WARNING, MessageFormat.format("Unable to instantiated type: {0} make sure the type is not abstract. Name: {1}", parameter.getType(), parameter.getName()));
						}
					} else {
						try {
							valueModel.setValueObject(objectMapper.writeValueAsString(parameter.getType().newInstance()));
							Set<String> fieldList = mapValueField(valueModel.getValueFields(), ReflectionUtil.getAllFields(parameter.getType()).toArray(new Field[0]), true);
							String cleanUpJson = StringProcessor.stripeFieldJSON(valueModel.getValueObject(), fieldList);
							valueModel.setValueObject(cleanUpJson);
							mapComplexTypes(valueModel.getAllComplexTypes(), ReflectionUtil.getAllFields(parameter.getType()).toArray(new Field[0]), true);

							APIDescription aPIDescription = parameter.getType().getAnnotation(APIDescription.class);
							if (aPIDescription != null) {
								valueModel.setTypeDescription(aPIDescription.value());
							}

						} catch (InstantiationException iex) {
							log.log(Level.WARNING, MessageFormat.format("Unable to instantiated type: {0} make sure the type is not abstract. Name: {1}", parameter.getType(), parameter.getName()));
						}
					}
				} catch (IllegalAccessException | JsonProcessingException ex) {
					log.log(Level.WARNING, null, ex);
				}

				//There can only be one consume(Request Body Parameter) object
				//We take the first one and ignore the rest.
				methodModel.setConsumeObject(valueModel);
				break;
			}
		}
	}

	@SuppressWarnings("squid:S1872")
	private static void mapComplexTypes(List<APITypeModel> typeModels, Field fields[], boolean onlyConsumeField)
	{
		//Should strip duplicate types
		Set<String> typesInList = new HashSet<>();
		typeModels.forEach(type -> {
			typesInList.add(type.getName());
		});

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		for (Field field : fields) {
			boolean capture = true;
			if (onlyConsumeField) {
				ConsumeField consumeField = (ConsumeField) field.getAnnotation(ConsumeField.class);
				if (consumeField == null) {
					capture = false;
				}
			}

			if (capture) {

				Class fieldClass = field.getType();
				DataType dataType = (DataType) field.getAnnotation(DataType.class);
				if (dataType != null) {
					fieldClass = dataType.value();
				}

				if (ReflectionUtil.isComplexClass(fieldClass)) {

					APITypeModel typeModel = new APITypeModel();
					typeModel.setName(fieldClass.getSimpleName());

					APIDescription aPIDescription = (APIDescription) fieldClass.getAnnotation(APIDescription.class);
					if (aPIDescription != null) {
						typeModel.setDescription(aPIDescription.value());
					}

					Set<String> fieldList = mapValueField(typeModel.getFields(), ReflectionUtil.getAllFields(fieldClass).toArray(new Field[0]), onlyConsumeField);
					if (fieldClass.isEnum()) {
						typeModel.setObject(Arrays.toString(fieldClass.getEnumConstants()));
					} else if (fieldClass.isInterface() == false) {
						try {
							if (fieldClass.isMemberClass()) {
								typeModel.setObject("{ See Parent Object }");
							} else {
								typeModel.setObject(objectMapper.writeValueAsString(fieldClass.newInstance()));
								String cleanUpJson = StringProcessor.stripeFieldJSON(typeModel.getObject(), fieldList);
								typeModel.setObject(cleanUpJson);
							}
						} catch (InstantiationException | IllegalAccessException | JsonProcessingException ex) {
							log.log(Level.WARNING, "Unable to process/map complex field: " + fieldClass.getSimpleName(), ex);
							typeModel.setObject("{ Unable to view }");
						}
						mapComplexTypes(typeModels, ReflectionUtil.getAllFields(fieldClass).toArray(new Field[0]), onlyConsumeField);
					}
					typeModels.add(typeModel);
					typesInList.add(typeModel.getName());
				}
			}
		}
	}

	private static void mapValueField(List<APIValueFieldModel> fieldModels, Field[] fields)
	{
		mapValueField(fieldModels, fields, false);
	}

	@SuppressWarnings("squid:S1872")
	private static Set<String> mapValueField(List<APIValueFieldModel> fieldModels, Field fields[], boolean onlyComsumeField)
	{
		Set<String> fieldNamesCaptured = new HashSet<>();

		for (Field field : fields) {
			boolean capture = true;

			if (onlyComsumeField) {
				ConsumeField consumeField = field.getAnnotation(ConsumeField.class);
				if (consumeField == null) {
					capture = false;
				}
			}

			if (capture) {
				APIValueFieldModel fieldModel = new APIValueFieldModel();
				fieldModel.setFieldName(field.getName());
				fieldNamesCaptured.add(field.getName());
				fieldModel.setType(field.getType().getSimpleName());

				DataType dataType = field.getAnnotation(DataType.class);
				if (dataType != null) {
					String typeName = dataType.value().getSimpleName();
					if (StringUtils.isNotBlank(dataType.actualClassName())) {
						typeName = dataType.actualClassName();
					}
					fieldModel.setType(fieldModel.getType() + ":  " + typeName);
				}

				NotNull requiredParam = field.getAnnotation(NotNull.class);
				if (requiredParam != null) {
					fieldModel.setRequired(true);
				}

				StringBuilder validation = new StringBuilder();
				ParamTypeDescription description = (ParamTypeDescription) field.getAnnotation(ParamTypeDescription.class);
				if (description != null) {
					validation.append(description.value()).append("<br>");
				}

				APIDescription aPIDescription = (APIDescription) field.getAnnotation(APIDescription.class);
				if (aPIDescription != null) {
					fieldModel.setDescription(aPIDescription.value());
				}

				if (field.getType().getSimpleName().equals("Date")) {
					validation.append("Timestamp (milliseconds since UNIX Epoch<br>");
				}

				if (field.getType().getSimpleName().equalsIgnoreCase("boolean")) {
					validation.append("T | F");
				}

				ValidationRequirement validationRequirement = field.getAnnotation(ValidationRequirement.class);
				if (validationRequirement != null) {
					validation.append(validationRequirement.value()).append("<br>");
				}

				PK pk = field.getAnnotation(PK.class);
				if (pk != null) {
					if (pk.generated()) {
						validation.append("Primary Key (Generated)").append("<br>");
					} else {
						validation.append("Primary Key").append("<br>");
					}
				}

				Min min = field.getAnnotation(Min.class);
				if (min != null) {
					validation.append("Min Value: ").append(min.value()).append("<br>");
				}

				Max max = field.getAnnotation(Max.class);
				if (max != null) {
					validation.append("Max Value: ").append(max.value()).append("<br>");
				}

				Size size = field.getAnnotation(Size.class);
				if (size != null) {
					validation.append("Min Length: ").append(size.min()).append(" Max Length: ").append(size.max()).append("<br>");
				}

				Pattern pattern = field.getAnnotation(Pattern.class);
				if (pattern != null) {
					validation.append("Needs to Match: ").append(pattern.regexp()).append("<br>");
				}

				ValidValueType validValueType = field.getAnnotation(ValidValueType.class);
				if (validValueType != null) {
					validation.append("Set of valid values: ").append(Arrays.toString(validValueType.value())).append("<br>");
					if (validValueType.lookupClass().length > 0) {
						validation.append("See Lookup table(s): <br>");
						for (Class lookupClass : validValueType.lookupClass()) {
							validation.append(lookupClass.getSimpleName());
						}
					}
					if (validValueType.enumClass().length > 0) {
						validation.append("See Enum List: <br>");
						for (Class enumClass : validValueType.enumClass()) {
							validation.append(enumClass.getSimpleName());
							if (enumClass.isEnum()) {
								validation.append(" (")
										.append(Arrays.toString(enumClass.getEnumConstants()))
										.append(") ");
							}
						}
					}
				}

				fieldModel.setValidation(validation.toString());

				fieldModels.add(fieldModel);
			}
		}
		return fieldNamesCaptured;
	}

	private static void mapMethodParameters(List<APIParamModel> parameterList, Parameter[] parameters)
	{
		for (Parameter parameter : parameters) {
			APIParamModel paramModel = new APIParamModel();
			paramModel.setFieldName(parameter.getName());

			QueryParam queryParam = parameter.getAnnotation(QueryParam.class);
			checkQueryParam(queryParam, paramModel);

			FormParam formParam = parameter.getAnnotation(FormParam.class);
			checkFormParam(formParam, paramModel);

			MatrixParam matrixParam = parameter.getAnnotation(MatrixParam.class);
			checkMatrixParam(matrixParam, paramModel);

			HeaderParam headerParam = parameter.getAnnotation(HeaderParam.class);
			checkHeaderParam(headerParam, paramModel);

			CookieParam cookieParam = parameter.getAnnotation(CookieParam.class);
			checkCookieParam(cookieParam, paramModel);

			PathParam pathParam = parameter.getAnnotation(PathParam.class);
			checkPathParam(pathParam, paramModel);

			BeanParam beanParam = parameter.getAnnotation(BeanParam.class);
			checkBeanParam(beanParam, parameter, parameterList);

			if (StringUtils.isNotBlank(paramModel.getParameterType())) {
				APIDescription aPIDescription = parameter.getAnnotation(APIDescription.class);
				if (aPIDescription != null) {
					paramModel.setParameterDescription(aPIDescription.value());
				}

				ParameterRestrictions restrictions = parameter.getAnnotation(ParameterRestrictions.class);
				if (restrictions != null) {
					paramModel.setRestrictions(restrictions.value());
				}

				RequiredParam requiredParam = parameter.getAnnotation(RequiredParam.class);
				if (requiredParam != null) {
					paramModel.setRequired(true);
				}

				DefaultValue defaultValue = parameter.getAnnotation(DefaultValue.class);
				if (defaultValue != null) {
					paramModel.setDefaultValue(defaultValue.value());
				}

				parameterList.add(paramModel);
			}
		}
	}

	private static void checkQueryParam(QueryParam queryParam, APIParamModel paramModel)
	{
		if (queryParam != null) {
			paramModel.setParameterType(QueryParam.class.getSimpleName());
			paramModel.setParameterName(queryParam.value());
		}
	}

	private static void checkFormParam(FormParam formParam, APIParamModel paramModel)
	{
		if (formParam != null) {
			paramModel.setParameterType(FormParam.class.getSimpleName());
			paramModel.setParameterName(formParam.value());
		}
	}

	private static void checkMatrixParam(MatrixParam matrixParam, APIParamModel paramModel)
	{
		if (matrixParam != null) {
			paramModel.setParameterType(MatrixParam.class.getSimpleName());
			paramModel.setParameterName(matrixParam.value());
		}
	}

	private static void checkPathParam(PathParam pathParam, APIParamModel paramModel)
	{
		if (pathParam != null) {
			paramModel.setParameterType(PathParam.class.getSimpleName());
			paramModel.setParameterName(pathParam.value());
		}
	}

	private static void checkHeaderParam(HeaderParam headerParam, APIParamModel paramModel)
	{
		if (headerParam != null) {
			paramModel.setParameterType(HeaderParam.class.getSimpleName());
			paramModel.setParameterName(headerParam.value());
		}
	}

	private static void checkCookieParam(CookieParam cookieParam, APIParamModel paramModel)
	{
		if (cookieParam != null) {
			paramModel.setParameterType(CookieParam.class.getSimpleName());
			paramModel.setParameterName(cookieParam.value());
		}
	}

	private static void checkBeanParam(BeanParam beanParam, Parameter parameter, List<APIParamModel> parameterList)
	{
		if (beanParam != null) {
			Class paramClass = parameter.getType();
			mapParameters(parameterList, ReflectionUtil.getAllFields(paramClass).toArray(new Field[0]));
		}
	}

	private static void mapParameters(List<APIParamModel> parameterList, Field fields[])
	{
		for (Field field : fields) {
			APIParamModel paramModel = new APIParamModel();
			paramModel.setFieldName(field.getName());

			QueryParam queryParam = field.getAnnotation(QueryParam.class);
			FormParam formParam = field.getAnnotation(FormParam.class);
			MatrixParam matrixParam = field.getAnnotation(MatrixParam.class);
			HeaderParam headerParam = field.getAnnotation(HeaderParam.class);
			CookieParam cookieParam = field.getAnnotation(CookieParam.class);
			PathParam pathParam = field.getAnnotation(PathParam.class);
			BeanParam beanParam = field.getAnnotation(BeanParam.class);

			if (queryParam != null) {
				paramModel.setParameterType(QueryParam.class.getSimpleName());
				paramModel.setParameterName(queryParam.value());
			}
			if (formParam != null) {
				paramModel.setParameterType(FormParam.class.getSimpleName());
				paramModel.setParameterName(formParam.value());
			}
			if (matrixParam != null) {
				paramModel.setParameterType(MatrixParam.class.getSimpleName());
				paramModel.setParameterName(matrixParam.value());
			}
			if (pathParam != null) {
				paramModel.setParameterType(PathParam.class.getSimpleName());
				paramModel.setParameterName(pathParam.value());
			}
			if (headerParam != null) {
				paramModel.setParameterType(HeaderParam.class.getSimpleName());
				paramModel.setParameterName(headerParam.value());
			}
			if (cookieParam != null) {
				paramModel.setParameterType(CookieParam.class.getSimpleName());
				paramModel.setParameterName(cookieParam.value());
			}

			if (beanParam != null) {
				Class fieldClass = field.getDeclaringClass();
				mapParameters(parameterList, fieldClass.getDeclaredFields());
			}

			if (StringUtils.isNotBlank(paramModel.getParameterType())) {

				APIDescription aPIDescription = field.getAnnotation(APIDescription.class);
				if (aPIDescription != null) {
					paramModel.setParameterDescription(aPIDescription.value());
				}

				ParameterRestrictions restrictions = field.getAnnotation(ParameterRestrictions.class);
				if (restrictions != null) {
					paramModel.setRestrictions(restrictions.value());
				}

				RequiredParam requiredParam = field.getAnnotation(RequiredParam.class);
				if (requiredParam != null) {
					paramModel.setRequired(true);
				}

				DefaultValue defaultValue = field.getAnnotation(DefaultValue.class);
				if (defaultValue != null) {
					paramModel.setDefaultValue(defaultValue.value());
				}

				parameterList.add(paramModel);
			}
		}
	}

}
