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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.storage.model.ComponentRelationship;
import edu.usu.sdl.openstorefront.storage.model.RelationshipType;
import edu.usu.sdl.openstorefront.util.TranslateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author dshurtleff
 */
public class ComponentRelationshipView
{

	private String relationshipId;
	private String ownerComponentId;
	private String ownerComponentName;
	private String targetComponentId;
	private String targetComponentName;
	private String relationshipType;
	private String relationshipTypeDescription;
	private Date updateDts;

	public ComponentRelationshipView()
	{
	}

	public static ComponentRelationshipView toView(ComponentRelationship componentRelationship)
	{
		Objects.requireNonNull(componentRelationship, "Component Required");

		ServiceProxy serviceProxy = new ServiceProxy();
		ComponentRelationshipView relationshipView = new ComponentRelationshipView();
		relationshipView.setRelationshipId(componentRelationship.getComponentRelationshipId());
		relationshipView.setOwnerComponentId(componentRelationship.getComponentId());
		relationshipView.setOwnerComponentName(serviceProxy.getComponentService().getComponentName(componentRelationship.getComponentId()));
		relationshipView.setTargetComponentId(componentRelationship.getRelatedComponentId());
		relationshipView.setTargetComponentName(serviceProxy.getComponentService().getComponentName(componentRelationship.getRelatedComponentId()));
		relationshipView.setRelationshipType(componentRelationship.getRelationshipType());
		relationshipView.setRelationshipTypeDescription(TranslateUtil.translate(RelationshipType.class, componentRelationship.getRelationshipType()));
		relationshipView.setUpdateDts(componentRelationship.getUpdateDts());

		return relationshipView;
	}

	public static List<ComponentRelationshipView> toViewList(List<ComponentRelationship> componentRelationships)
	{
		List<ComponentRelationshipView> views = new ArrayList<>();
		componentRelationships.forEach(componentRelationship -> {
			views.add(toView(componentRelationship));
		});
		return views;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public String getOwnerComponentId()
	{
		return ownerComponentId;
	}

	public void setOwnerComponentId(String ownerComponentId)
	{
		this.ownerComponentId = ownerComponentId;
	}

	public String getOwnerComponentName()
	{
		return ownerComponentName;
	}

	public void setOwnerComponentName(String ownerComponentName)
	{
		this.ownerComponentName = ownerComponentName;
	}

	public String getTargetComponentId()
	{
		return targetComponentId;
	}

	public void setTargetComponentId(String targetComponentId)
	{
		this.targetComponentId = targetComponentId;
	}

	public String getTargetComponentName()
	{
		return targetComponentName;
	}

	public void setTargetComponentName(String targetComponentName)
	{
		this.targetComponentName = targetComponentName;
	}

	public String getRelationshipType()
	{
		return relationshipType;
	}

	public void setRelationshipType(String relationshipType)
	{
		this.relationshipType = relationshipType;
	}

	public String getRelationshipTypeDescription()
	{
		return relationshipTypeDescription;
	}

	public void setRelationshipTypeDescription(String relationshipTypeDescription)
	{
		this.relationshipTypeDescription = relationshipTypeDescription;
	}

	public String getRelationshipId()
	{
		return relationshipId;
	}

	public void setRelationshipId(String relationshipId)
	{
		this.relationshipId = relationshipId;
	}

}
