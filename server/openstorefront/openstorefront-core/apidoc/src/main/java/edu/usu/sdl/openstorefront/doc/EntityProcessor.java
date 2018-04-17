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
package edu.usu.sdl.openstorefront.doc;

import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.Unique;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.doc.model.EntityConstraintModel;
import edu.usu.sdl.openstorefront.doc.model.EntityDocModel;
import edu.usu.sdl.openstorefront.doc.model.EntityFieldModel;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.Sanitizer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class EntityProcessor
{

	private static final Logger LOG = Logger.getLogger(EntityProcessor.class.getName());

	private EntityProcessor()
	{
	}

	public static List<EntityDocModel> processEntites(List<Class> entities)
	{
		List<EntityDocModel> entityDocModels = new ArrayList<>();

		LOG.log(Level.FINEST, "Construct Entities");
		for (Class entity : entities) {
			EntityDocModel docModel = createEntityModel(entity);
			addSuperClass(entity.getSuperclass(), docModel);
			for (Class interfaceClass : entity.getInterfaces()) {
				docModel.getImplementedEntities().add(createEntityModel(interfaceClass));
			}
			entityDocModels.add(docModel);
		}

		entityDocModels.sort((EntityDocModel o1, EntityDocModel o2) -> o1.getName().compareTo(o2.getName()));
		return entityDocModels;
	}

	@SuppressWarnings("squid:S1872")
	private static void addSuperClass(Class entity, EntityDocModel docModel)
	{
		if (entity != null) {
			if (Object.class.getName().equals(entity.getName()) == false) {
				docModel.getParentEntities().add(createEntityModel(entity));
				addSuperClass(entity.getSuperclass(), docModel);
			}
		}
	}

	private static EntityDocModel createEntityModel(Class entity)
	{
		EntityDocModel docModel = new EntityDocModel();
		docModel.setName(entity.getSimpleName());

		@SuppressWarnings("unchecked")
		APIDescription description = (APIDescription) entity.getAnnotation(APIDescription.class);
		if (description != null) {
			docModel.setDescription(description.value());
		}

		if (!entity.isInterface()) {
			addFields(entity, docModel);
		}

		return docModel;
	}

	@SuppressWarnings("squid:S1872")
	private static void addFields(Class entity, EntityDocModel docModel)
	{
		if (entity != null) {
			Field[] fields = entity.getDeclaredFields();
			for (Field field : fields) {
				//Skip static field
				if ((Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) == false) {

					EntityFieldModel fieldModel = new EntityFieldModel();
					fieldModel.setName(field.getName());
					fieldModel.setType(field.getType().getSimpleName());
					fieldModel.setOriginClass(entity.getSimpleName());
					fieldModel.setEmbeddedType(ReflectionUtil.isComplexClass(field.getType()));
					if (ReflectionUtil.isCollectionClass(field.getType())) {
						DataType dataType = field.getAnnotation(DataType.class);
						if (dataType != null) {
							String typeClass = dataType.value().getSimpleName();
							if (StringUtils.isNotBlank(dataType.actualClassName())) {
								typeClass = dataType.actualClassName();
							}
							fieldModel.setGenericType(typeClass);
						}
					}

					APIDescription description = field.getAnnotation(APIDescription.class);
					if (description != null) {
						fieldModel.setDescription(description.value());
					}

					for (Annotation annotation : field.getAnnotations()) {
						if (annotation.annotationType().getName().equals(APIDescription.class.getName()) == false) {
							EntityConstraintModel entityConstraintModel = new EntityConstraintModel();
							entityConstraintModel.setName(annotation.annotationType().getSimpleName());

							APIDescription annotationDescription = annotation.annotationType().getAnnotation(APIDescription.class);
							if (annotationDescription != null) {
								entityConstraintModel.setDescription(annotationDescription.value());
							}

							//rules
							Object annObj = field.getAnnotation(annotation.annotationType());
							if (annObj instanceof NotNull) {
								entityConstraintModel.setRules("Field is required");
							} else if (annObj instanceof PK) {
								PK pk = (PK) annObj;
								entityConstraintModel.setRules("<b>Generated:</b> " + pk.generated());
								fieldModel.setPrimaryKey(true);
							} else if (annObj instanceof FK) {
								FK fk = (FK) annObj;

								StringBuilder sb = new StringBuilder();
								sb.append("<b>Foreign Key:</b> ").append(fk.value().getSimpleName());
								sb.append(" (<b>Enforce</b>: ").append(fk.enforce());
								sb.append(" <b>Soft reference</b>: ").append(fk.softReference());
								if (StringUtils.isNotBlank(fk.referencedField())) {
									sb.append(" <b>Reference Field</b>: ").append(fk.referencedField());
								}
								sb.append(" )");
								entityConstraintModel.setRules(sb.toString());
							} else if (annObj instanceof ConsumeField) {
								entityConstraintModel.setRules("");
							} else if (annObj instanceof Size) {
								Size size = (Size) annObj;
								entityConstraintModel.setRules("<b>Min:</b> " + size.min() + " <b>Max:</b> " + size.max());
							} else if (annObj instanceof Pattern) {
								Pattern pattern = (Pattern) annObj;
								entityConstraintModel.setRules("<b>Pattern:</b> " + pattern.regexp());
							} else if (annObj instanceof Sanitize) {
								Sanitize sanitize = (Sanitize) annObj;
								List<String> sanitizerList = new ArrayList<>();
								for (Class<? extends Sanitizer> sanitizeClass : sanitize.value()) {
									sanitizerList.add(sanitizeClass.getSimpleName());
								}
								entityConstraintModel.setRules("<b>Sanitize:</b> " + String.join(", ", sanitizerList));

							} else if (annObj instanceof Unique) {
								Unique unique = (Unique) annObj;
								entityConstraintModel.setRules("<b>Handler:</b> " + unique.value().getSimpleName());
							} else if (annObj instanceof ValidValueType) {
								ValidValueType validValueType = (ValidValueType) annObj;
								StringBuilder sb = new StringBuilder();
								if (validValueType.value().length > 0) {
									sb.append(" <b>Values:</b> ").append(Arrays.toString(validValueType.value()));
								}

								if (validValueType.lookupClass().length > 0) {
									sb.append(" <b>Lookups:</b> ");
									for (Class lookupClass : validValueType.lookupClass()) {
										sb.append(lookupClass.getSimpleName()).append("  ");
									}
								}

								if (validValueType.enumClass().length > 0) {
									sb.append(" <b>Enumerations:</b> ");
									for (Class enumClass : validValueType.enumClass()) {
										sb.append(enumClass.getSimpleName())
												.append("  (");
										sb.append(Arrays.toString(enumClass.getEnumConstants())).append(")");
									}
								}

								entityConstraintModel.setRules(sb.toString());
							} else if (annObj instanceof Min) {
								Min min = (Min) annObj;
								entityConstraintModel.setRules("<b>Min value:</b> " + min.value());
							} else if (annObj instanceof Max) {
								Max max = (Max) annObj;
								entityConstraintModel.setRules("<b>Max value:</b> " + max.value());
							} else if (annObj instanceof Version) {
								entityConstraintModel.setRules("Entity version; For Multi-Version control");
							} else if (annObj instanceof DataType) {
								DataType dataType = (DataType) annObj;
								String typeClass = dataType.value().getSimpleName();
								if (StringUtils.isNotBlank(dataType.actualClassName())) {
									typeClass = dataType.actualClassName();
								}
								entityConstraintModel.setRules("<b>Type:</b> " + typeClass);
							} else {
								entityConstraintModel.setRules(annotation.toString());
							}

							//Annotations that have related classes
							if (annObj instanceof DataType) {
								DataType dataType = (DataType) annObj;
								entityConstraintModel.getRelatedClasses().add(dataType.value().getSimpleName());
							}
							if (annObj instanceof FK) {
								FK fk = (FK) annObj;
								entityConstraintModel.getRelatedClasses().add(fk.value().getSimpleName());
							}
							if (annObj instanceof ValidValueType) {
								ValidValueType validValueType = (ValidValueType) annObj;
								for (Class lookupClass : validValueType.lookupClass()) {
									entityConstraintModel.getRelatedClasses().add(lookupClass.getSimpleName());
								}

								StringBuilder sb = new StringBuilder();
								for (Class enumClass : validValueType.enumClass()) {
									sb.append("<br>");
									sb.append(enumClass.getSimpleName())
											.append(":  (");
									sb.append(Arrays.toString(enumClass.getEnumConstants())).append(")");
								}
								entityConstraintModel.setRules(entityConstraintModel.getRules() + " " + sb.toString());
							}

							fieldModel.getConstraints().add(entityConstraintModel);
						}
					}

					docModel.getFieldModels().add(fieldModel);
				}
			}
			addFields(entity.getSuperclass(), docModel);
		}
	}

}
