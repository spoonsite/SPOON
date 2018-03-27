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
package edu.usu.sdl.openstorefront.service.component;

import com.orientechnologies.orient.core.record.impl.ODocument;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeTemplate;
import edu.usu.sdl.openstorefront.core.entity.FileDataMap;
import edu.usu.sdl.openstorefront.core.entity.RoleLink;
import edu.usu.sdl.openstorefront.core.entity.UserLink;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeNestedModel;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeOptions;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeRoleResolution;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeTemplateResolution;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeUserResolution;
import edu.usu.sdl.openstorefront.core.view.ComponentTypeView;
import edu.usu.sdl.openstorefront.service.ComponentServiceImpl;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;

/**
 *
 * @author dshurtleff
 */
public class ComponentTypeServiceImpl
		extends BaseComponentServiceImpl
{

	private static final Logger LOG = Logger.getLogger(ComponentTypeServiceImpl.class.getName());

	public ComponentTypeServiceImpl(ComponentServiceImpl componentService)
	{
		super(componentService);
	}

	public String getComponentTypeForComponent(String componentId)
	{
		String componentType = null;
		Element element = OSFCacheManager.getComponentTypeComponentCache().get(componentId);
		if (element != null) {
			componentType = (String) element.getObjectValue();
		} else {
			String query = "select componentId, componentType from " + Component.class.getSimpleName();
			List<ODocument> documents = persistenceService.query(query, null);
			for (ODocument document : documents) {
				Element newElement = new Element(document.field("componentId"), document.field("componentType"));
				if (document.field("componentId").equals(componentId)) {
					componentType = (String) document.field("componentType");
				}
				OSFCacheManager.getComponentTypeComponentCache().put(newElement);
			}
		}
		return componentType;
	}

	public ComponentTypeNestedModel getComponentType(ComponentTypeOptions componentTypeOptions)
	{
		Objects.requireNonNull(componentTypeOptions);

		ComponentTypeNestedModel typeModel = null;

		List<ComponentType> componentTypes = getAllComponentTypes();
		if (componentTypeOptions.getComponentType() == null) {
			typeModel = new ComponentTypeNestedModel();

			List<ComponentType> roots = new ArrayList<>();
			for (ComponentType componentType : componentTypes) {
				if (componentType.getParentComponentType() == null) {
					roots.add(componentType);
				}
			}
			for (ComponentType componentType : roots) {
				ComponentTypeNestedModel rootModel = new ComponentTypeNestedModel();
				rootModel.setComponentType(ComponentTypeView.toView(componentType));
				rootModel.getChildren().addAll(getChildrenComponentTypes(componentTypes, componentType));
				typeModel.getChildren().add(rootModel);
			}
		} else {
			ComponentType found = null;
			for (ComponentType componentType : componentTypes) {
				if (componentType.getComponentType().equals(componentTypeOptions.getComponentType())) {
					found = componentType;
				}
			}
			if (found != null) {
				typeModel = new ComponentTypeNestedModel();
				if (componentTypeOptions.getPullParents() && found.getParentComponentType() != null) {
					found = findTopParentComponentType(componentTypes, found);
				}
				typeModel.setComponentType(ComponentTypeView.toView(found));

				if (componentTypeOptions.getPullChildren()) {
					typeModel.getChildren().addAll(getChildrenComponentTypes(componentTypes, found));
				}
			}
		}
		return typeModel;
	}
	
	public List<String> getComponentTypeChildren(ComponentTypeNestedModel nestedModel)
	{
		List<String> childrenTypes = findChildrenTypes(nestedModel, new ArrayList<>());

		return childrenTypes;
	}

	private List<String> findChildrenTypes(ComponentTypeNestedModel nestedModel, List<String> childrenTypes)
	{
		if (nestedModel != null) {
			childrenTypes.add(nestedModel.getComponentType().getComponentType());
		}
		else {
			return new ArrayList<>();
		}
		
		List<ComponentTypeNestedModel> children = nestedModel.getChildren();
		if (children.size() <= 0) {
			return childrenTypes;
		}

		for (ComponentTypeNestedModel child : children) {
			childrenTypes.addAll(findChildrenTypes(child, new ArrayList<>()));
		}

		return childrenTypes;
	}

	private ComponentType findComponentType(List<ComponentType> componentTypes, String componentTypeId)
	{
		ComponentType found = null;
		for (ComponentType componentType : componentTypes) {
			if (componentType.getComponentType().equals(componentTypeId)) {
				found = componentType;
				break;
			}
		}
		return found;
	}

	private ComponentType findTopParentComponentType(List<ComponentType> componentTypes, ComponentType child)
	{
		if (child.getParentComponentType() != null) {
			ComponentType parent = null;
			for (ComponentType componentType : componentTypes) {
				if (componentType.getComponentType().equals(child.getParentComponentType())) {
					parent = findTopParentComponentType(componentTypes, componentType);
				}
			}
			return parent;
		} else {
			return child;
		}
	}

	private List<ComponentTypeNestedModel> getChildrenComponentTypes(List<ComponentType> componentTypes, ComponentType parentComponentType)
	{
		List<ComponentTypeNestedModel> childModels = new ArrayList<>();
		for (ComponentType componentType : componentTypes) {
			if (parentComponentType.getComponentType().equals(componentType.getParentComponentType())) {
				ComponentTypeNestedModel childModel = new ComponentTypeNestedModel();
				childModel.setComponentType(ComponentTypeView.toView(componentType));
				childModel.getChildren().addAll(getChildrenComponentTypes(componentTypes, componentType));
				childModels.add(childModel);
			}
		}
		return childModels;
	}

	@SuppressWarnings("unchecked")
	public List<ComponentType> getAllComponentTypes()
	{
		List<ComponentType> componentTypes;
		Element element = OSFCacheManager.getComponentTypeCache().get(OSFCacheManager.ALLCODE_KEY);
		if (element != null) {
			componentTypes = (List<ComponentType>) element.getObjectValue();
		} else {
			ComponentType componentType = new ComponentType();
			componentTypes = componentType.findByExample();
			element = new Element(OSFCacheManager.ALLCODE_KEY, componentTypes);
			OSFCacheManager.getComponentTypeCache().put(element);
		}

		return componentTypes;
	}

	public ComponentType saveComponentType(ComponentType componentType)
	{
		ComponentType existing = persistenceService.findById(ComponentType.class, componentType.getComponentType());
		if (existing != null) {
			existing.updateFields(componentType);
			componentType = persistenceService.persist(existing);
		} else {
			componentType.populateBaseCreateFields();
			componentType = persistenceService.persist(componentType);
		}
		OSFCacheManager.getComponentTypeCache().removeAll();

		return componentType;
	}

	public void removeComponentType(String componentType, String newComponentType)
	{
		ComponentType componentTypeFound = persistenceService.findById(ComponentType.class, componentType);
		if (componentTypeFound != null) {

			boolean inactivate = true;
			if (StringUtils.isNotBlank(newComponentType)) {
				ComponentType newType = persistenceService.findById(ComponentType.class, newComponentType);

				if (newType != null) {
					//migrate data
					Component setComponent = new Component();
					setComponent.setComponentType(newComponentType);

					Component whereComponent = new Component();
					whereComponent.setComponentType(componentType);

					persistenceService.updateByExample(Component.class, setComponent, whereComponent);

					FileDataMap setfileDataMap = new FileDataMap();
					setfileDataMap.setDefaultComponentType(newComponentType);

					FileDataMap wherefileDataMap = new FileDataMap();
					wherefileDataMap.setDefaultComponentType(componentType);
					persistenceService.updateByExample(FileDataMap.class, setfileDataMap, wherefileDataMap);

					//remove restrictions
					AttributeType attributeTypeExample = new AttributeType();
					List<AttributeType> allAttributes = attributeTypeExample.findByExample();
					List<AttributeType> updateAttributes = new ArrayList<>();
					for (AttributeType attributeType : allAttributes) {

						boolean addToUpdate = false;
						if (attributeType.getRequiredRestrictions() != null && !attributeType.getRequiredRestrictions().isEmpty()) {
							for (int i = attributeType.getRequiredRestrictions().size() - 1; i >= 0; i--) {
								String checkType = attributeType.getRequiredRestrictions().get(i).getComponentType();
								if (checkType.equals(componentType)) {
									attributeType.getRequiredRestrictions().remove(i);
									addToUpdate = true;
								}
							}
						}

						if (attributeType.getOptionalRestrictions() != null && !attributeType.getOptionalRestrictions().isEmpty()) {
							for (int i = attributeType.getOptionalRestrictions().size() - 1; i >= 0; i--) {
								String checkType = attributeType.getOptionalRestrictions().get(i).getComponentType();
								if (checkType.equals(componentType)) {
									attributeType.getOptionalRestrictions().remove(i);
									addToUpdate = true;
								}
							}
						}

						if (addToUpdate) {
							updateAttributes.add(attributeType);
						}

						for (AttributeType attributeTypeUpdated : updateAttributes) {
							componentService.getAttributeService().saveAttributeType(attributeTypeUpdated, false);
						}

						// Update children
						ComponentType ctExample = new ComponentType();
						ctExample.setParentComponentType(componentTypeFound.getComponentType());
						List<ComponentType> ctChildren = ctExample.findByExampleProxy();

						ctChildren.forEach(child -> {
							child.setParentComponentType(newComponentType);
							persistenceService.persist(child);
						});
					}

					//remove
					inactivate = false;
					persistenceService.delete(componentTypeFound);
				} else {
					LOG.log(Level.WARNING, MessageFormat.format("Unable to find new component type: {0}  to migrate data to.  Inactivating component type: {1}", new Object[]{
						newComponentType, componentType
					}));
				}
			}

			if (inactivate) {
				componentTypeFound.setActiveStatus(ComponentType.INACTIVE_STATUS);
				componentTypeFound.populateBaseUpdateFields();
				persistenceService.persist(componentTypeFound);
			}
			OSFCacheManager.getComponentCache().removeAll();
			OSFCacheManager.getComponentTypeCache().removeAll();
		}
	}

	public ComponentTypeTemplate saveComponentTemplate(ComponentTypeTemplate componentTypeTemplate)
	{
		Objects.requireNonNull(componentTypeTemplate);

		ComponentTypeTemplate existing = persistenceService.findById(ComponentTypeTemplate.class, componentTypeTemplate.getTemplateId());
		if (existing != null) {
			existing.updateFields(componentTypeTemplate);
			componentTypeTemplate = persistenceService.persist(existing);
		} else {
			if (StringUtil.isBlank(componentTypeTemplate.getTemplateId())) {
				componentTypeTemplate.setTemplateId(persistenceService.generateId());
			}
			componentTypeTemplate.populateBaseCreateFields();
			componentTypeTemplate = persistenceService.persist(componentTypeTemplate);
		}
		return componentTypeTemplate;
	}

	public void removeComponentTypeTemplate(String templateId)
	{
		ComponentTypeTemplate template = persistenceService.findById(ComponentTypeTemplate.class, templateId);
		if (template != null) {
			template.setActiveStatus(ComponentType.INACTIVE_STATUS);
			template.populateBaseUpdateFields();
			persistenceService.persist(template);
		}
	}

	public void deleteComponentTypeTemplate(String templateId)
	{
		ComponentTypeTemplate template = persistenceService.findById(ComponentTypeTemplate.class, templateId);
		if (template != null) {
			ComponentType componentType = new ComponentType();
			componentType.setComponentTypeTemplate(templateId);

			List<ComponentType> types = componentType.findByExample();
			if (types.isEmpty()) {
				persistenceService.delete(template);
			} else {
				throw new OpenStorefrontRuntimeException("Unable to delete; Entry types are point to the template.", "Remove the template from entry types (both active and inactive) and try again.");
			}
		}
	}

	public String resolveComponentTypeIcon(String componentType)
	{
		String typeIcon = null;
		List<ComponentType> componentTypes = getAllComponentTypes();

		for (ComponentType componentTypeLocal : componentTypes) {
			if (componentTypeLocal.getComponentType().equals(componentType)) {
				if (StringUtils.isNotBlank(componentTypeLocal.getIconUrl())) {
					typeIcon = componentTypeLocal.getIconUrl();
				} else {
					typeIcon = findFirstParentWithIcon(componentTypes, componentTypeLocal);
				}
			}
		}

		return typeIcon;
	}

	private String findFirstParentWithIcon(List<ComponentType> componentTypes, ComponentType child)
	{
		String iconUrl = null;

		if (child.getParentComponentType() != null) {
			for (ComponentType componentType : componentTypes) {
				if (componentType.getComponentType().equals(child.getParentComponentType())) {
					if (StringUtils.isNotBlank(componentType.getIconUrl())) {
						iconUrl = componentType.getIconUrl();
						break;
					} else {
						iconUrl = findFirstParentWithIcon(componentTypes, componentType);
					}
				}
			}
		}
		return iconUrl;
	}

	public void removeRoleFromComponentType(String roleName)
	{
		if (StringUtils.isNotBlank(roleName)) {
			boolean clearCache = false;

			List<ComponentType> componentTypes = getAllComponentTypes();
			for (ComponentType componentType : componentTypes) {
				if (componentType.getAssignedGroups() != null) {
					for (RoleLink roleLink : componentType.getAssignedGroups()) {
						if (roleLink.getRoleName().equals(roleName)) {
							boolean removeItem = performRemoveRoleFromType(componentType.getComponentType(), roleName);
							if (removeItem) {
								clearCache = true;
							}
						}
					}
				}
			}

			if (clearCache) {
				OSFCacheManager.getComponentTypeCache().removeAll();
			}
		}
	}

	private boolean performRemoveRoleFromType(String componentType, String roleName)
	{
		boolean removedItem = false;
		ComponentType componentTypeProxy = persistenceService.findById(ComponentType.class, componentType);
		if (componentTypeProxy != null) {

			if (componentTypeProxy.getAssignedGroups() != null) {
				componentTypeProxy.getAssignedGroups().removeIf((role) -> {
					return roleName.equals(role.getRoleName());
				});
				componentTypeProxy.populateBaseUpdateFields();
				persistenceService.persist(componentTypeProxy);

				removedItem = true;
			}
		} else {
			LOG.log(Level.FINEST, ()
					-> "Unable to find component type: " + componentType + " stale cache?"
			);
		}
		return removedItem;
	}

	public void removeUserFromComponentType(String username)
	{
		if (StringUtils.isNotBlank(username)) {
			boolean clearCache = false;

			List<ComponentType> componentTypes = getAllComponentTypes();
			for (ComponentType componentType : componentTypes) {
				if (componentType.getAssignedUsers() != null) {
					for (UserLink userLink : componentType.getAssignedUsers()) {
						if (userLink.getUsername().equals(username)) {
							boolean removeItem = performRemoveUserFromType(componentType.getComponentType(), username);
							if (removeItem) {
								clearCache = true;
							}
						}
					}
				}
			}

			if (clearCache) {
				OSFCacheManager.getComponentTypeCache().removeAll();
			}
		}
	}

	private boolean performRemoveUserFromType(String componentType, String username)
	{
		boolean removedItem = false;
		ComponentType componentTypeProxy = persistenceService.findById(ComponentType.class, componentType);
		if (componentTypeProxy != null) {

			if (componentTypeProxy.getAssignedUsers() != null) {
				componentTypeProxy.getAssignedUsers().removeIf((role) -> {
					return username.equals(role.getUsername());
				});
				componentTypeProxy.populateBaseUpdateFields();
				persistenceService.persist(componentTypeProxy);

				removedItem = true;
			}
		} else {
			LOG.log(Level.FINEST, ()
					-> "Unable to find component type: " + componentType + " stale cache?"
			);
		}
		return removedItem;
	}

	public ComponentTypeTemplate getComponentTypeTemplate(String templateId)
	{
		ComponentTypeTemplate template = new ComponentTypeTemplate();
		template.setTemplateId(templateId);
		template = template.find();
		return template;
	}

	public ComponentTypeTemplateResolution findTemplateForComponentType(String componentType)
	{
		ComponentTypeTemplateResolution templateResolution;

		List<ComponentType> componentTypes = getAllComponentTypes();
		ComponentType componentTypeFull = findComponentType(componentTypes, componentType);

		if (componentTypeFull.getComponentTypeTemplate() != null) {

			templateResolution = new ComponentTypeTemplateResolution();

			ComponentTypeTemplate template = getComponentTypeTemplate(componentTypeFull.getComponentTypeTemplate());
			if (template != null) {
				templateResolution.setTemplateId(componentTypeFull.getComponentTypeTemplate());
				templateResolution.setTemplateName(template.getName());
			} else {
				LOG.log(Level.WARNING, () -> "Override Template not found for: " + componentType);
				templateResolution = null;
			}

		} else {
			templateResolution = findFirstParentWithTemplate(componentTypes, componentTypeFull);
		}

		return templateResolution;
	}

	private ComponentTypeTemplateResolution findFirstParentWithTemplate(List<ComponentType> componentTypes, ComponentType child)
	{
		ComponentTypeTemplateResolution resolution = null;

		if (child.getParentComponentType() != null) {
			for (ComponentType componentType : componentTypes) {
				if (componentType.getComponentType().equals(child.getParentComponentType())) {
					if (componentType.getComponentTypeTemplate() != null) {
						resolution = new ComponentTypeTemplateResolution();
						resolution.setCameFromAncestor(true);
						resolution.setAncestorComponentType(componentType.getComponentType());
						resolution.setAncestorComponentTypeLabel(componentType.getLabel());

						ComponentTypeTemplate template = getComponentTypeTemplate(componentType.getComponentTypeTemplate());
						if (template != null) {
							resolution.setTemplateId(componentType.getComponentTypeTemplate());
							resolution.setTemplateName(template.getName());
						} else {
							LOG.log(Level.WARNING, () -> "Override Template not found for: " + componentType);
							resolution = null;
						}
						break;
					} else {
						resolution = findFirstParentWithTemplate(componentTypes, componentType);
					}
				}
			}
		}
		return resolution;
	}

	public ComponentTypeRoleResolution findRoleGroupsForComponentType(String componentType)
	{
		ComponentTypeRoleResolution roleResolution;

		List<ComponentType> componentTypes = getAllComponentTypes();
		ComponentType componentTypeFull = findComponentType(componentTypes, componentType);

		if (componentTypeFull.getAssignedGroups() != null
				&& !componentTypeFull.getAssignedGroups().isEmpty()) {

			roleResolution = new ComponentTypeRoleResolution();
			for (RoleLink link : componentTypeFull.getAssignedGroups()) {
				roleResolution.getRoles().add(link.getRoleName());
			}

		} else {
			roleResolution = findFirstParentWithRoles(componentTypes, componentTypeFull);
		}
		return roleResolution;
	}

	private ComponentTypeRoleResolution findFirstParentWithRoles(List<ComponentType> componentTypes, ComponentType child)
	{
		ComponentTypeRoleResolution resolution = null;

		if (child.getParentComponentType() != null) {
			for (ComponentType componentType : componentTypes) {
				if (componentType.getComponentType().equals(child.getParentComponentType())) {
					if (componentType.getAssignedGroups() != null
							&& !componentType.getAssignedGroups().isEmpty()) {

						resolution = new ComponentTypeRoleResolution();
						resolution.setCameFromAncestor(true);
						resolution.setAncestorComponentType(componentType.getComponentType());
						resolution.setAncestorComponentTypeLabel(componentType.getLabel());

						for (RoleLink link : componentType.getAssignedGroups()) {
							resolution.getRoles().add(link.getRoleName());
						}
						break;
					} else {
						resolution = findFirstParentWithRoles(componentTypes, componentType);
					}
				}
			}
		}
		return resolution;
	}

	public ComponentTypeUserResolution findUserForComponentType(String componentType)
	{
		ComponentTypeUserResolution resolution;

		List<ComponentType> componentTypes = getAllComponentTypes();
		ComponentType componentTypeFull = findComponentType(componentTypes, componentType);

		if (componentTypeFull.getAssignedUsers() != null
				&& !componentTypeFull.getAssignedUsers().isEmpty()) {

			resolution = new ComponentTypeUserResolution();
			for (UserLink link : componentTypeFull.getAssignedUsers()) {
				resolution.getUsernames().add(link.getUsername());
			}

		} else {
			resolution = findFirstParentWithUser(componentTypes, componentTypeFull);
		}
		return resolution;
	}

	private ComponentTypeUserResolution findFirstParentWithUser(List<ComponentType> componentTypes, ComponentType child)
	{
		ComponentTypeUserResolution resolution = null;

		if (child.getParentComponentType() != null) {
			for (ComponentType componentType : componentTypes) {
				if (componentType.getComponentType().equals(child.getParentComponentType())) {
					if (componentType.getAssignedUsers() != null
							&& !componentType.getAssignedUsers().isEmpty()) {

						resolution = new ComponentTypeUserResolution();
						resolution.setCameFromAncestor(true);
						resolution.setAncestorComponentType(componentType.getComponentType());
						resolution.setAncestorComponentTypeLabel(componentType.getLabel());

						for (UserLink link : componentType.getAssignedUsers()) {
							resolution.getUsernames().add(link.getUsername());
						}
						break;
					} else {
						resolution = findFirstParentWithUser(componentTypes, componentType);
					}
				}
			}
		}
		return resolution;
	}

	public ComponentType activateComponentType(String componentTypeId)
	{
		ComponentType existing = persistenceService.findById(ComponentType.class, componentTypeId);
		if (existing != null) {

			existing.setActiveStatus(ComponentType.ACTIVE_STATUS);
			existing.populateBaseUpdateFields();
			persistenceService.persist(existing);
			existing = persistenceService.unwrapProxyObject(existing);
			OSFCacheManager.getComponentTypeCache().removeAll();
		}
		return existing;
	}

}
