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
package edu.usu.sdl.core.init;

import edu.usu.sdl.openstorefront.core.entity.ComponentType;

/**
 * Adds saves default types (Component, Article)
 *
 * @author dshurtleff
 */
public class ComponentTypeInit
		extends ApplyOnceInit
{

	public ComponentTypeInit()
	{
		super("Component-Type");
	}

	@Override
	protected String internalApply()
	{

		//defaults
		ComponentType componentType = new ComponentType();
		componentType.setComponentType(ComponentType.COMPONENT);
		componentType.setLabel("DI2E Component");
		componentType.setDescription("Main entry representing a DI2E Component");
		componentType.setAllowOnSubmission(true);
		componentType.setDataEntryAttributes(Boolean.TRUE);
		componentType.setDataEntryContacts(Boolean.TRUE);
		componentType.setDataEntryEvaluationInformation(Boolean.TRUE);
		componentType.setDataEntryMedia(Boolean.TRUE);
		componentType.setDataEntryRelationships(Boolean.TRUE);
		componentType.setDataEntryResources(Boolean.TRUE);
		componentType.setDataEntryReviews(Boolean.TRUE);
		componentType.setDataEntryQuestions(Boolean.TRUE);

		//Skip
		//componentType.setDataEntryDependencies(Boolean.TRUE);
		//componentType.setDataEntryMetadata(Boolean.TRUE);
		service.getComponentService().saveComponentType(componentType);

		componentType = new ComponentType();
		componentType.setComponentType(ComponentType.ARTICLE);
		componentType.setAllowOnSubmission(false);
		componentType.setDataEntryAttributes(Boolean.TRUE);
		componentType.setDataEntryQuestions(Boolean.TRUE);
		componentType.setDataEntryMedia(Boolean.TRUE);
		componentType.setDataEntryContacts(Boolean.TRUE);
		componentType.setDataEntryResources(Boolean.TRUE);
		componentType.setDataEntryRelationships(Boolean.TRUE);
		componentType.setLabel("Article");
		componentType.setDescription("Informational Entries used to display information on a topic and show related items.");

		service.getComponentService().saveComponentType(componentType);
		return "Added defaults";
	}

	@Override
	public int getPriority()
	{
		return 5;
	}

}
