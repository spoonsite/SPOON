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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.validation.CleanKeySanitizer;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Actual type of component")
public class ComponentType
		extends StandardEntity<ComponentType>
{

	@PK(generated = false)
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@NotNull
	@Sanitize(CleanKeySanitizer.class)
	@ConsumeField
	private String type;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String label;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String description;

	@ConsumeField
	private Boolean dataEntryAttributes;

	@ConsumeField
	private Boolean dataEntryRelationships;

	@ConsumeField
	private Boolean dataEntryContacts;

	@ConsumeField
	private Boolean dataEntryResources;

	@ConsumeField
	private Boolean dataEntryMedia;

	@ConsumeField
	private Boolean dataEntryDependancies;

	@ConsumeField
	private Boolean dataEntryMetadata;

	@ConsumeField
	private Boolean dataEntryEvaluationInformation;

	@FK(value = ComponentTypeTemplate.class, enforce = true)
	@ConsumeField
	private String componentTypeTemplate;

	public static final String COMPONENT = "COMP";
	public static final String ARTICLE = "ARTICLE";

	//Dummy Value for filtering
	public static final String ALL = "ALL";

	public ComponentType()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		ComponentType componentType = (ComponentType) entity;

		this.setLabel(componentType.getLabel());
		this.setDescription(componentType.getDescription());
		this.setComponentTypeTemplate(componentType.getComponentTypeTemplate());
		this.setDataEntryAttributes(componentType.getDataEntryAttributes());
		this.setDataEntryContacts(componentType.getDataEntryContacts());
		this.setDataEntryDependancies(componentType.getDataEntryDependancies());
		this.setDataEntryEvaluationInformation(componentType.getDataEntryEvaluationInformation());
		this.setDataEntryMedia(componentType.getDataEntryMedia());
		this.setDataEntryMetadata(componentType.getDataEntryMetadata());
		this.setDataEntryRelationships(componentType.getDataEntryRelationships());
		this.setDataEntryResources(componentType.getDataEntryResources());

	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Boolean getDataEntryAttributes()
	{
		return dataEntryAttributes;
	}

	public void setDataEntryAttributes(Boolean dataEntryAttributes)
	{
		this.dataEntryAttributes = dataEntryAttributes;
	}

	public Boolean getDataEntryRelationships()
	{
		return dataEntryRelationships;
	}

	public void setDataEntryRelationships(Boolean dataEntryRelationships)
	{
		this.dataEntryRelationships = dataEntryRelationships;
	}

	public Boolean getDataEntryContacts()
	{
		return dataEntryContacts;
	}

	public void setDataEntryContacts(Boolean dataEntryContacts)
	{
		this.dataEntryContacts = dataEntryContacts;
	}

	public Boolean getDataEntryResources()
	{
		return dataEntryResources;
	}

	public void setDataEntryResources(Boolean dataEntryResources)
	{
		this.dataEntryResources = dataEntryResources;
	}

	public Boolean getDataEntryMedia()
	{
		return dataEntryMedia;
	}

	public void setDataEntryMedia(Boolean dataEntryMedia)
	{
		this.dataEntryMedia = dataEntryMedia;
	}

	public Boolean getDataEntryDependancies()
	{
		return dataEntryDependancies;
	}

	public void setDataEntryDependancies(Boolean dataEntryDependancies)
	{
		this.dataEntryDependancies = dataEntryDependancies;
	}

	public Boolean getDataEntryMetadata()
	{
		return dataEntryMetadata;
	}

	public void setDataEntryMetadata(Boolean dataEntryMetadata)
	{
		this.dataEntryMetadata = dataEntryMetadata;
	}

	public Boolean getDataEntryEvaluationInformation()
	{
		return dataEntryEvaluationInformation;
	}

	public void setDataEntryEvaluationInformation(Boolean dataEntryEvaluationInformation)
	{
		this.dataEntryEvaluationInformation = dataEntryEvaluationInformation;
	}

	public String getComponentTypeTemplate()
	{
		return componentTypeTemplate;
	}

	public void setComponentTypeTemplate(String componentTypeTemplate)
	{
		this.componentTypeTemplate = componentTypeTemplate;
	}

}
