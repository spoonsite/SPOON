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
package edu.usu.sdl.openstorefront.service.search;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOptionBuilder;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ComponentSearchHandler
		extends BaseSearchHandler
{

	public ComponentSearchHandler(List<SearchElement> searchElements)
	{
		super(searchElements);
	}

	@Override
	protected ValidationResult internalValidate()
	{
		ValidationResult validationResult = new ValidationResult();

		for (SearchElement searchElement : searchElements) {
			if (StringUtils.isBlank(searchElement.getField())) {
				validationResult.getRuleResults().add(getRuleResult("field", "Required"));
			}

			boolean checkValue = true; 			
			Field field = ReflectionUtil.getField(new Component(), searchElement.getField());
			if (field == null) {
				validationResult.getRuleResults().add(getRuleResult("field", "Doesn't exist on component"));
			} else {
				Class type = field.getType();
				if (type.getSimpleName().equals(String.class.getSimpleName())) {
					//Nothing to check
				} else if (type.getSimpleName().equals(Integer.class.getSimpleName())) {
					if (StringUtils.isNumeric(searchElement.getValue()) == false) {
						validationResult.getRuleResults().add(getRuleResult("value", "Value should be an integer for this field"));
					}
				} else if (type.getSimpleName().equals(Date.class.getSimpleName())) {
					checkValue = false;
					if (searchElement.getStartDate() == null && searchElement.getEndDate() == null) {
						validationResult.getRuleResults().add(getRuleResult("startDate", "Start or End date should be entered for this field"));
						validationResult.getRuleResults().add(getRuleResult("endDate", "Start or End date should be entered for this field"));
					}
				} else if (type.getSimpleName().equals(Boolean.class.getSimpleName())) {
					//Nothing to check
				} else {
					validationResult.getRuleResults().add(getRuleResult("field", "Field type handling not supported"));
				}
			}
			if (checkValue && StringUtils.isBlank(searchElement.getValue())) {
				validationResult.getRuleResults().add(getRuleResult("value", "Required"));
			}
			
		}

		return validationResult;
	}

	@Override
	public List<String> processSearch()
	{
		List<String> foundIds = new ArrayList<>();
		SearchOperation.MergeCondition mergeCondition = SearchOperation.MergeCondition.OR;
		for (SearchElement searchElement : searchElements) {

			try {
				Component component = new Component();
				component.setActiveStatus(Component.ACTIVE_STATUS);
				component.setApprovalState(ApprovalStatus.APPROVED);
				
				Field field = ReflectionUtil.getField(new Component(), searchElement.getField());
				field.setAccessible(true);
				QueryByExample queryByExample = new QueryByExample(component);

				Class type = field.getType();
				if (type.getSimpleName().equals(String.class.getSimpleName())) {
					String likeValue = null;
					switch (searchElement.getStringOperation()) {
						case EQUALS:
							String value = searchElement.getValue();
							if (searchElement.getCaseInsensitive()) {
								queryByExample.getFieldOptions().put(field.getName(), 
											new GenerateStatementOptionBuilder().setMethod(GenerateStatementOption.METHOD_LOWER_CASE).build());
								value = value.toLowerCase();
							}
							field.set(component, value);
							break;
						default:
							likeValue = searchElement.getStringOperation().toQueryString(searchElement.getValue());
							break;
					}

					if (likeValue != null) {
						Component componentLike = new Component();
						if (searchElement.getCaseInsensitive()) {
							likeValue = likeValue.toLowerCase();
							queryByExample.getLikeExampleOption().setMethod(GenerateStatementOption.METHOD_LOWER_CASE);
						}
						field.set(componentLike, likeValue);
						queryByExample.setLikeExample(componentLike);
					}
				} else if (type.getSimpleName().equals(Integer.class.getSimpleName())) {
					field.set(component, Convert.toInteger(searchElement.getValue()));
					queryByExample.getFieldOptions().put(field.getName(),										
							new GenerateStatementOptionBuilder().setOperation(searchElement.getNumberOperation().toQueryOperation()).build());		
					
				} else if (type.getSimpleName().equals(Date.class.getSimpleName())) {

					Component componentStartExample = new Component();

					field.set(componentStartExample, searchElement.getStartDate());
					SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
					specialOperatorModel.setExample(componentStartExample);
					specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
					queryByExample.getExtraWhereCauses().add(specialOperatorModel);

					Component componentEndExample = new Component();

					field.set(componentEndExample, searchElement.getEndDate());
					specialOperatorModel = new SpecialOperatorModel();
					specialOperatorModel.setExample(componentEndExample);
					specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
					specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
					queryByExample.getExtraWhereCauses().add(specialOperatorModel);

				} else if (type.getSimpleName().equals(Boolean.class.getSimpleName())) {
					field.set(component, Convert.toBoolean(searchElement.getValue()));
				} else {
					throw new OpenStorefrontRuntimeException("Type: " + type.getSimpleName() + " is not support in this query handler", "Add support");
				}

				List<Component> components = serviceProxy.getPersistenceService().queryByExample(queryByExample);
				List<String> results = new ArrayList<>();
				for (Component contact : components) {
					results.add(contact.getComponentId());
				}
				foundIds = mergeCondition.apply(foundIds, results);
				mergeCondition = searchElement.getMergeCondition();
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException | OpenStorefrontRuntimeException e) {
				throw new OpenStorefrontRuntimeException("Unable to handle search request", e);
			}
		}
		return foundIds;
	}

}
