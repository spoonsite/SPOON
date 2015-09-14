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
import edu.usu.sdl.openstorefront.doc.model.EntityConstraintModel;
import edu.usu.sdl.openstorefront.doc.model.EntityDocModel;
import edu.usu.sdl.openstorefront.doc.model.EntityFieldModel;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
public class EntityProcessor
{

	private static final Logger log = Logger.getLogger(EntityProcessor.class.getName());

	private EntityProcessor()
	{
	}

	public static List<EntityDocModel> processEntites(List<Class> entities)
	{
		List<EntityDocModel> entityDocModels = new ArrayList<>();

		log.log(Level.FINEST, "Construct Entities");
		for (Class entity : entities) {
			EntityDocModel docModel = createEntityModel(entity);
			addSuperClass(entity.getSuperclass(), docModel);
			for (Class interfaceClass : entity.getInterfaces()) {
				docModel.getImplementedEntities().add(createEntityModel(interfaceClass));
			}
		}

		entityDocModels.sort((EntityDocModel o1, EntityDocModel o2) -> o1.getName().compareTo(o2.getName()));
		return entityDocModels;
	}

	private static void addSuperClass(Class entity, EntityDocModel docModel)
	{
		if (entity != null) {
			docModel.getParentEntities().add(createEntityModel(entity));
			addSuperClass(entity.getSuperclass(), docModel);
		}
	}

	private static EntityDocModel createEntityModel(Class entity)
	{
		EntityDocModel docModel = new EntityDocModel();
		docModel.setName(entity.getSimpleName());

		APIDescription description = (APIDescription) entity.getAnnotation(APIDescription.class);
		if (description != null) {
			docModel.setDescription(description.value());
		}

		if (!entity.isInterface()) {
			addFields(entity, docModel);
		}

		return docModel;
	}

	private static void addFields(Class entity, EntityDocModel docModel)
	{
		if (entity != null) {
			Field[] fields = entity.getDeclaredFields();
			for (Field field : fields) {
				EntityFieldModel fieldModel = new EntityFieldModel();
				fieldModel.setName(field.getName());
				fieldModel.setType(field.getType().getSimpleName());
				fieldModel.setOriginClass(entity.getSimpleName());
				fieldModel.setEmbeddedType(ReflectionUtil.isComplexClass(field.getDeclaringClass()));
				if (ReflectionUtil.isCollectionClass(field.getType())) {
					fieldModel.setGenericType(field.getGenericType().getTypeName());
				}

				APIDescription description = (APIDescription) field.getAnnotation(APIDescription.class);
				if (description != null) {
					fieldModel.setDescription(description.value());
				}

				for (Annotation annotation : field.getAnnotations()) {
					if (annotation.annotationType().getName().equals(APIDescription.class.getName()) == false) {
						EntityConstraintModel entityConstraintModel = new EntityConstraintModel();
						entityConstraintModel.setName(annotation.annotationType().getSimpleName());
						entityConstraintModel.setRules(annotation.toString());

						APIDescription annotationDescription = (APIDescription) annotation.annotationType().getAnnotation(APIDescription.class);
						if (annotationDescription != null) {
							entityConstraintModel.setDescription(annotationDescription.value());
						}

						//Annotation that have related classes
						Object annObj = field.getAnnotation(annotation.annotationType());
						if (annObj instanceof NotNull) {
							fieldModel.getConstraints().add(entityConstraintModel);
						}
					}
				}

				docModel.getFieldModels().add(fieldModel);
			}
			addFields(entity.getSuperclass(), docModel);
		}
	}

}
