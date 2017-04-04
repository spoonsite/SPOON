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
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.model.FieldChangeModel;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Defines Relationship between components")
public class ComponentRelationship
		extends BaseComponent<ComponentRelationship>
		implements LoggableModel<ComponentRelationship>
{

	@PK(generated = true)
	@NotNull
	private String componentRelationshipId;

	@NotNull
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = RelationshipType.class)
	@FK(RelationshipType.class)
	private String relationshipType;

	@NotNull
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@FK(Component.class)
	private String relatedComponentId;

	public ComponentRelationship()
	{
	}

	@Override
	public String uniqueKey()
	{
		return getRelationshipType() + OpenStorefrontConstant.GENERAL_KEY_SEPARATOR + getRelatedComponentId();
	}

	@Override
	protected void customKeyClear()
	{
		setComponentRelationshipId(null);
	}

	@Override
	public void updateFields(StandardEntity entity)
	{
		ComponentRelationship componentRelationship = (ComponentRelationship) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, componentRelationship);
		super.updateFields(entity);

		this.setComponentId(componentRelationship.getComponentId());
		this.setRelatedComponentId(componentRelationship.getRelatedComponentId());
		this.setRelationshipType(componentRelationship.getRelationshipType());

	}

	@Override
	public List<FieldChangeModel> findChanges(ComponentRelationship updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("componentRelationshipId");
		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		return TranslateUtil.translate(RelationshipType.class, getRelationshipType()) + " - " + ServiceProxyFactory.getServiceProxy().getComponentService().getComponentName(getRelatedComponentId());
	}

	public String getComponentRelationshipId()
	{
		return componentRelationshipId;
	}

	public void setComponentRelationshipId(String componentRelationshipId)
	{
		this.componentRelationshipId = componentRelationshipId;
	}

	public String getRelationshipType()
	{
		return relationshipType;
	}

	public void setRelationshipType(String relationshipType)
	{
		this.relationshipType = relationshipType;
	}

	public String getRelatedComponentId()
	{
		return relatedComponentId;
	}

	public void setRelatedComponentId(String relatedComponentId)
	{
		this.relatedComponentId = relatedComponentId;
	}

}
