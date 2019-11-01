/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOptionBuilder;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ModificationType;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ComponentUniqueHandler
		implements UniqueHandler<Component>
{

	private static final Logger LOG = Logger.getLogger(ComponentUniqueHandler.class.getName());

	@Override
	public boolean isUnique(Field field, Object value, Component fullDataObject)
	{
		boolean isUnique = true;

		if (ModificationType.MERGE.equals(fullDataObject.getLastModificationType())) {
			//skip check 
			return isUnique;
		}

		Service service = ServiceProxyFactory.getServiceProxy();

		//find records with the same name (case-insensitive)
		//This triggers mongo regex which is a contains
		Component componentSearch = new Component();
		componentSearch.setName(((String) value).toLowerCase());

		QueryByExample<Component> queryByExample = new QueryByExample<>(componentSearch);
		queryByExample.getFieldOptions().put(Component.FIELD_NAME,
				new GenerateStatementOptionBuilder()
						.setMethod(GenerateStatementOption.METHOD_LOWER_CASE)
						.build());

		List<Component> existingComponents = service.getPersistenceService().queryByExample(queryByExample);
		for (Component component : existingComponents) {

			//Make sure it's an exact match
			if (component.getName().equalsIgnoreCase(value.toString())) {

			//see duplicate is me
			if (component.getComponentId().equals(fullDataObject.getComponentId()) == false) {

				//indicate a new record
				if (fullDataObject.getComponentId() != null) {
					//check to see if entry is a child of me
					if (fullDataObject.getComponentId().equals(component.getPendingChangeId()) == false) {

						//if the record is a change request check parent name if they are the same that ok
						if (StringUtils.isNotBlank(fullDataObject.getPendingChangeId())) {

							//change request; check parent
							Component parentComponent = new Component();
							parentComponent.setComponentId(fullDataObject.getPendingChangeId());
							parentComponent = parentComponent.find();
							if (parentComponent != null) {
								if (parentComponent.getName().equalsIgnoreCase(fullDataObject.getName()) == false) {
									isUnique = false;
								}
							} else {
								LOG.log(Level.WARNING, MessageFormat.format("Change Request is orphaned. Entry Name: {0} ID: {1}",
										new Object[]{fullDataObject.getName(), fullDataObject.getComponentId()}));

								isUnique = false;
							}
						} else {
							isUnique = false;
						}
					}
				} else {
					isUnique = false;
				}
			}
		}

		return isUnique;
	}

	@Override
	public String getMessage()
	{
		return "Provided name already exists, and duplication is not permitted.  Please choose another name.";
	}

}
