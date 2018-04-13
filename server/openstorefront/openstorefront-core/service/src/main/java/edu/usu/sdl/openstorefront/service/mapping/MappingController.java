/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.mapping;

import edu.usu.sdl.openstorefront.core.model.ComponentFormSet;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormField;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormSection;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormTemplate;
import edu.usu.sdl.openstorefront.core.entity.UserSubmission;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionField;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class MappingController
{

	private static final Logger LOG = Logger.getLogger(MappingController.class.getName());

	private MapperFactory mapperFactory;

	public MappingController()
	{
		mapperFactory = new MapperFactory();
	}

	public MappingController(MapperFactory mapperFactory)
	{
		this.mapperFactory = mapperFactory;
	}

	/**
	 * Check the template to make sure it can be mapped
	 *
	 * @param template
	 * @return
	 */
	public ValidationResult verifyTemplate(SubmissionFormTemplate template, String componentType)
	{
		Objects.requireNonNull(template);

		ValidationResult result = new ValidationResult();

		List<ComponentAll> allComponents = new ArrayList<>();
		ComponentAll mainComponent = new ComponentAll();
		Component component = new Component();
		component.setComponentType(componentType);
		component.setApprovalState(ApprovalStatus.NOT_SUBMITTED);
		mainComponent.setComponent(component);
		allComponents.add(mainComponent);

		try {
			verifySections(template, mainComponent, allComponents);
		} catch (MappingException ex) {
			RuleResult ruleResult = new RuleResult();
			ruleResult.setFieldName(ex.getFieldType());
			ruleResult.setMessage(ex.getLocalizedMessage());
			result.getRuleResults().add(ruleResult);
		}
		result.merge(validateComponents(allComponents));
		return result;
	}

	private void verifySections(SubmissionFormTemplate template, ComponentAll mainComponent, List<ComponentAll> allComponents) throws MappingException
	{
		if (template.getSections() != null) {
			for (SubmissionFormSection section : template.getSections()) {
				try {
					verifyFields(section.getFields(), mainComponent, allComponents);
				} catch (MappingException ex) {
					ex.setSectionName(section.getName());
					throw ex;
				}
			}
		}
	}

	private void verifyFields(List<SubmissionFormField> fields, ComponentAll mainComponent, List<ComponentAll> allComponents) throws MappingException
	{
		if (fields != null) {
			for (SubmissionFormField field : fields) {
				BaseMapper mapper = mapperFactory.getMapperForField(field.getMappingType());
				allComponents.addAll(mapper.mapField(mainComponent, field));
			}
		}
	}

	private ValidationResult validateComponents(List<ComponentAll> allComponents)
	{
		ValidationResult result = new ValidationResult();

		for (ComponentAll componentAll : allComponents) {
			result.merge(componentAll.validate());
		}

		return result;
	}

	/**
	 * Create entries from submission
	 *
	 * @param template
	 * @param userSubmission
	 * @return All entries created
	 */
	public List<ComponentAll> mapUserSubmissionToEntry(SubmissionFormTemplate template, UserSubmission userSubmission) throws MappingException
	{

		List<ComponentAll> allComponents = new ArrayList<>();
		ComponentAll mainComponent = new ComponentAll();
		Component component = new Component();
		component.setComponentType(userSubmission.getComponentType());
		mainComponent.setComponent(component);
		allComponents.add(mainComponent);

		mapTemplateSections(template, mainComponent, allComponents, userSubmission);

		return allComponents;
	}

	private void mapTemplateSections(SubmissionFormTemplate template, ComponentAll mainComponent, List<ComponentAll> allComponents, UserSubmission userSubmission) throws MappingException
	{
		if (template.getSections() != null) {

			Map<String, UserSubmissionField> userFieldMap = new HashMap<>();
			for (UserSubmissionField submissionField : userSubmission.getFields()) {
				userFieldMap.put(submissionField.getFieldId(), submissionField);
			}

			for (SubmissionFormSection section : template.getSections()) {
				try {
					mapTemplateFields(section.getFields(), mainComponent, allComponents, userFieldMap);
				} catch (MappingException ex) {
					ex.setSectionName(section.getName());
					throw ex;
				}
			}
		}
	}

	private void mapTemplateFields(List<SubmissionFormField> fields, ComponentAll mainComponent, List<ComponentAll> allComponents, Map<String, UserSubmissionField> submissionFields) throws MappingException
	{
		if (fields != null) {
			for (SubmissionFormField field : fields) {
				BaseMapper mapper = mapperFactory.getMapperForField(field.getMappingType());
				UserSubmissionField userField = submissionFields.get(field.getFieldId());
				if (userField != null) {
					allComponents.addAll(mapper.mapField(mainComponent, field, userField));
				} else {
					if (LOG.isLoggable(Level.FINEST)) {
						LOG.log(Level.FINEST, () -> "No user data for field: {0}" + field.getLabel());
					}
				}
			}
		}
	}

	/**
	 * Create a submission from a set of entries. It assumes all entries will be
	 * related.
	 *
	 * @param template
	 * @param userSubmission
	 * @return userSubmission with all data possible mapped
	 */
	public UserSubmission mapEntriesToUserSubmission(SubmissionFormTemplate template, ComponentFormSet componentFormSet) throws MappingException
	{
		Objects.requireNonNull(componentFormSet);
		Objects.requireNonNull(componentFormSet.getPrimary(), "Must have at least a primary component");
		Objects.requireNonNull(componentFormSet.getPrimary().getComponent());

		UserSubmission userSubmission = new UserSubmission();
		userSubmission.setTemplateId(template.getSubmissionTemplateId());
		userSubmission.setComponentType(componentFormSet.getPrimary().getComponent().getComponentType());
		userSubmission.setOriginalComponentId(componentFormSet.getPrimary().getComponent().getComponentId());
		userSubmission.setFields(new ArrayList<>());

		mapTemplateSectionsForSubmission(template, componentFormSet, userSubmission);

		return userSubmission;
	}

	private void mapTemplateSectionsForSubmission(SubmissionFormTemplate template, ComponentFormSet componentFormSet, UserSubmission userSubmission) throws MappingException
	{
		if (template.getSections() != null) {

			for (SubmissionFormSection section : template.getSections()) {
				try {
					mapTemplateFieldsForSubmission(section.getFields(), componentFormSet, userSubmission);
				} catch (MappingException ex) {
					ex.setSectionName(section.getName());
					throw ex;
				}
			}
		}
	}

	private void mapTemplateFieldsForSubmission(List<SubmissionFormField> fields, ComponentFormSet componentFormSet, UserSubmission userSubmission) throws MappingException
	{
		if (fields != null) {
			for (SubmissionFormField field : fields) {
				BaseMapper mapper = mapperFactory.getMapperForField(field.getMappingType());
				UserSubmissionField userSubmissionField = mapper.mapComponentToSubmission(field, componentFormSet);
				userSubmission.getFields().add(userSubmissionField);

			}
		}
	}

}
