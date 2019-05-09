/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.rest.service;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import edu.usu.sdl.openstorefront.core.model.Architecture;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.view.ComponentRelationshipView;
import edu.usu.sdl.openstorefront.core.view.RelationshipView;
import edu.usu.sdl.openstorefront.web.rest.resource.AttributeResource;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
@Path("v1/service/relationship")
@APIDescription("Relationship Services that cross multiple resources")
public class RelationshipService
		extends BaseResource
{

	@GET
	@APIDescription("Find the next level of a given entity")
	@DataType(RelationshipView.class)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNextRelationships(
			@QueryParam("key") String entityKey,
			@QueryParam("entityType") String entityType
	)
	{
		List<RelationshipView> views = new ArrayList<>();

		entityKey = entityKey.replace("~", "#");

		//check to see if key exists
		boolean entityExists = false;
		switch (entityType) {
			case RelationshipView.ENTITY_TYPE_COMPONENT: {
				Component component = new Component();
				component.setComponentId(entityKey);
				component = component.find();
				component = filterEngine.filter(component);
				if (component != null) {
					entityExists = true;

					//pull details
					ComponentAll componentAll = service.getComponentService().getFullComponent(component.getComponentId());

					//organization
					RelationshipView view = new RelationshipView();
					view.setKey(componentAll.getComponent().getComponentId());
					view.setName(componentAll.getComponent().getName());
					view.setEntityType(RelationshipView.ENTITY_TYPE_COMPONENT);
					view.setTargetKey(Organization.toKey(componentAll.getComponent().getOrganization()));
					view.setTargetName(componentAll.getComponent().getOrganization());
					view.setTargetEntityType(RelationshipView.ENTITY_TYPE_ORGANIZATION);
					views.add(view);

					//relationships
					List<ComponentRelationshipView> componentRelationshipViews = ComponentRelationshipView.toViewList(componentAll.getRelationships());
					componentRelationshipViews = componentRelationshipViews.stream()
							.filter(r -> r.getOwnerApproved() && r.getTargetApproved())
							.collect(Collectors.toList());
					for (ComponentRelationshipView componentRelationshipView : componentRelationshipViews) {
						view = new RelationshipView();
						view.setKey(componentRelationshipView.getOwnerComponentId());
						view.setName(componentRelationshipView.getOwnerComponentName());
						view.setEntityType(RelationshipView.ENTITY_TYPE_COMPONENT);
						view.setRelationshipLabel(componentRelationshipView.getRelationshipTypeDescription());
						view.setTargetKey(componentRelationshipView.getTargetComponentId());
						view.setTargetName(componentRelationshipView.getTargetComponentName());
						view.setTargetEntityType(RelationshipView.ENTITY_TYPE_COMPONENT);
						views.add(view);
					}

					//attributes
					for (ComponentAttribute componentAttribute : componentAll.getAttributes()) {
						AttributeCodePk attributeCodePk = new AttributeCodePk();
						attributeCodePk.setAttributeCode(componentAttribute.getComponentAttributePk().getAttributeCode());
						attributeCodePk.setAttributeType(componentAttribute.getComponentAttributePk().getAttributeType());

						AttributeCode attributeCode = service.getAttributeService().findCodeForType(attributeCodePk);
						if (attributeCode != null) {
							AttributeType attributeType = service.getAttributeService().findType(attributeCodePk.getAttributeType());

							RelationshipView relationship = new RelationshipView();
							relationship.setKey(componentAttribute.getComponentId());
							relationship.setName(service.getComponentService().getComponentName(componentAttribute.getComponentId()));
							relationship.setEntityType(RelationshipView.ENTITY_TYPE_COMPONENT);
							relationship.setRelationType(RelationshipView.ATTRIBUTE_CODE_RELATION);
							relationship.setTargetKey(attributeCode.getAttributeCodePk().toKey());
							relationship.setRelationshipLabel(attributeType.getDescription() + (StringUtils.isNotBlank(attributeType.getAttributeUnit()) ? " (" + attributeType.getAttributeUnit() + ")" : ""));
							relationship.setTargetName(attributeCode.getLabel() + (StringUtils.isNotBlank(attributeType.getAttributeUnit()) ? " (" + attributeType.getAttributeUnit() + ")" : ""));
							relationship.setTargetEntityType(RelationshipView.ENTITY_TYPE_ATTRIBUTE);

							views.add(relationship);
						}
					}

					//tag
					for (ComponentTag tag : componentAll.getTags()) {
						view = new RelationshipView();
						view.setKey(tag.getComponentId());
						view.setName(service.getComponentService().getComponentName(tag.getComponentId()));
						view.setEntityType(RelationshipView.ENTITY_TYPE_COMPONENT);
						view.setTargetKey(tag.getText());
						view.setTargetName(tag.getText());
						view.setTargetEntityType(RelationshipView.ENTITY_TYPE_TAG);
						views.add(view);
					}

				}
			}
			break;
			case RelationshipView.ENTITY_TYPE_ORGANIZATION: {
				Organization organization = new Organization();
				organization.setOrganizationId(entityKey);
				organization = organization.find();
				if (organization != null) {
					entityExists = true;

					Component componentExample = new Component();
					componentExample.setActiveStatus(Component.ACTIVE_STATUS);
					componentExample.setApprovalState(ApprovalStatus.APPROVED);
					componentExample.setOrganization(organization.getName());
					List<Component> components = componentExample.findByExample();
					components = filterEngine.filter(components);

					for (Component component : components) {
						RelationshipView view = new RelationshipView();
						view.setKey(entityKey);
						view.setName(organization.getName());
						view.setEntityType(RelationshipView.ENTITY_TYPE_ORGANIZATION);
						view.setTargetKey(component.getComponentId());
						view.setTargetName(component.getName());
						view.setTargetEntityType(RelationshipView.ENTITY_TYPE_COMPONENT);
						views.add(view);
					}
				}
			}
			break;
			case RelationshipView.ENTITY_TYPE_ATTRIBUTE: {
				AttributeCodePk attributeCodePk = AttributeCodePk.fromKey(entityKey);
				if (attributeCodePk != null) {

					AttributeCode attributeCode = new AttributeCode();
					attributeCode.setAttributeCodePk(attributeCodePk);

					attributeCode = (AttributeCode) attributeCode.find();
					if (attributeCode != null) {
						entityExists = true;
						AttributeType attributeType = service.getAttributeService().findType(attributeCodePk.getAttributeType());

						ComponentAttribute componentAttributeExample = new ComponentAttribute();
						componentAttributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);

						ComponentAttributePk componentAttributePk = new ComponentAttributePk();
						componentAttributePk.setAttributeCode(attributeCodePk.getAttributeCode());
						componentAttributePk.setAttributeType(attributeCodePk.getAttributeType());
						componentAttributeExample.setComponentAttributePk(componentAttributePk);

						List<ComponentAttribute> componentAttributes = componentAttributeExample.findByExample();
						componentAttributes = filterEngine.filter(componentAttributes, true);

						for (ComponentAttribute componentAttribute : componentAttributes) {
							if (service.getComponentService().checkComponentApproval(componentAttribute.getComponentId())) {
								RelationshipView relationship = new RelationshipView();
								relationship.setKey(entityKey);
								relationship.setName(attributeCode.getLabel());
								relationship.setEntityType(RelationshipView.ENTITY_TYPE_ATTRIBUTE);
								relationship.setRelationType(RelationshipView.ATTRIBUTE_CODE_RELATION);
								relationship.setRelationshipLabel(attributeType.getDescription());
								relationship.setTargetKey(componentAttribute.getComponentId());
								relationship.setTargetName(service.getComponentService().getComponentName(componentAttribute.getComponentId()));
								relationship.setTargetEntityType(RelationshipView.ENTITY_TYPE_COMPONENT);

								views.add(relationship);
							}
						}
					}
				} else {
					AttributeType attributeType = new AttributeType();
					attributeType.setAttributeType(entityKey);

					attributeType = (AttributeType) attributeType.find();
					if (attributeType != null) {
						entityExists = true;

						if (attributeType.getArchitectureFlg()) {
							Architecture architecture = service.getAttributeService().generateArchitecture(attributeType.getAttributeType());

							AttributeResource attributeResource = new AttributeResource();
							attributeResource.buildRelations(views, architecture, null);
						} else {
							List<AttributeCode> attributeCodes = service.getAttributeService().findCodesForType(attributeType.getAttributeType());
							for (AttributeCode attributeCode : attributeCodes) {
								RelationshipView relationship = new RelationshipView();
								relationship.setKey(attributeCode.getAttributeCodePk().toKey());
								relationship.setName(attributeCode.getLabel());
								relationship.setEntityType(RelationshipView.ENTITY_TYPE_ATTRIBUTE);
								relationship.setRelationType(RelationshipView.ATTRIBUTE_CODE_RELATION);
								relationship.setRelationshipLabel(attributeType.getDescription());
								relationship.setTargetKey(attributeType.getAttributeType());
								relationship.setTargetName(attributeType.getDescription());
								relationship.setTargetEntityType(RelationshipView.ENTITY_TYPE_ATTRIBUTE);

								views.add(relationship);
							}
						}
					}
				}
			}
			break;
			case RelationshipView.ENTITY_TYPE_TAG: {
				ComponentTag tagExample = new ComponentTag();
				tagExample.setText(entityKey);
				List<ComponentTag> tags = tagExample.findByExample();
				tags = filterEngine.filter(tags, true);
				if (!tags.isEmpty()) {
					entityExists = true;

					for (ComponentTag tag : tags) {
						RelationshipView view = new RelationshipView();
						view.setKey(tag.getComponentId());
						view.setName(service.getComponentService().getComponentName(tag.getComponentId()));
						view.setEntityType(RelationshipView.ENTITY_TYPE_COMPONENT);
						view.setTargetKey(tag.getText());
						view.setTargetName(tag.getText());
						view.setTargetEntityType(RelationshipView.ENTITY_TYPE_TAG);
						views.add(view);
					}
				}
			}
			break;
		}

		if (entityExists) {
			GenericEntity<List<RelationshipView>> entity = new GenericEntity<List<RelationshipView>>(views)
			{
			};
			return sendSingleEntityResponse(entity);
		} else {
			return sendSingleEntityResponse(null);
		}
	}

}
