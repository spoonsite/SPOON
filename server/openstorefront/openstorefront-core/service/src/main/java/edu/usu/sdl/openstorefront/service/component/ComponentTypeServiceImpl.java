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
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormTemplate;
import edu.usu.sdl.openstorefront.core.entity.UserLink;
import edu.usu.sdl.openstorefront.core.entity.UserSubmission;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
					componentType = document.field("componentType");
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
			typeModel = constructNestedModelNoParent(componentTypes);
		} else {
			ComponentType found = findComponentTypeInList(componentTypes, componentTypeOptions);
			if (found != null) {
				typeModel = constructNestedModelWithParent(componentTypeOptions, found, componentTypes);
			}
		}
		return typeModel;
	}

	private ComponentTypeNestedModel constructNestedModelWithParent(ComponentTypeOptions componentTypeOptions, ComponentType found, List<ComponentType> componentTypes)
	{
		ComponentTypeNestedModel typeModel = new ComponentTypeNestedModel();
		if (componentTypeOptions.getPullParents() && found.getParentComponentType() != null) {
			found = findTopParentComponentType(componentTypes, found);
		}
		typeModel.setComponentType(ComponentTypeView.toView(found));
		if (componentTypeOptions.getPullChildren() && found != null) {
			typeModel.getChildren().addAll(getChildrenComponentTypes(componentTypes, found));
		}
		return typeModel;
	}

	private ComponentType findComponentTypeInList(List<ComponentType> componentTypes, ComponentTypeOptions componentTypeOptions)
	{
		ComponentType found = null;
		for (ComponentType componentType : componentTypes) {
			if (componentType.getComponentType().equals(componentTypeOptions.getComponentType())) {
				found = componentType;
			}
		}
		return found;
	}

	private ComponentTypeNestedModel constructNestedModelNoParent(List<ComponentType> componentTypes)
	{
		ComponentTypeNestedModel typeModel = new ComponentTypeNestedModel();
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
		return typeModel;
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
		if (found == null) {
			throw new OpenStorefrontRuntimeException("Unable to find component Type: " + componentTypeId, "Check input");
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

	private void resetComponentParent(ComponentType componentType)
	{
		componentType.setParentComponentType(null);
		ComponentType componentTypeTemp = persistenceService.findById(ComponentType.class, componentType.getComponentType());
		componentTypeTemp.setParentComponentType(null);
		persistenceService.persist(componentTypeTemp);
	}

	// check that
	// 1. no orphans (i.e. parent does not exist)
	// 2. a component is not a parent of itself
	private void fixOrphans(List<ComponentType> componentTypes)
	{
		Set<String> typeSet = componentTypes.stream()
				.map(ComponentType::getComponentType)
				.collect(Collectors.toSet());

		for (ComponentType componentTypeA : componentTypes) {
			// if parent of itself
			if (componentTypeA.getParentComponentType() != null
					&& componentTypeA.getParentComponentType().equals(componentTypeA.getComponentType())) {
				resetComponentParent(componentTypeA);

			} else if (componentTypeA.getParentComponentType() != null
					&& !typeSet.contains(componentTypeA.getParentComponentType())) {
				resetComponentParent(componentTypeA);
			}
		}
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
			fixOrphans(componentTypes);
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
					removeTypeMigrateData(newComponentType, componentType);
					removeTypeCleanupAttributes(componentType);
					removeTypeFromSubmissionTemplates(componentType);
					removeTypeUpdateChildren(componentTypeFound, newComponentType);

					//remove
					inactivate = false;
					persistenceService.delete(componentTypeFound);
				} else {
					LOG.log(Level.WARNING, () -> MessageFormat.format("Unable to find new component type: {0}  to migrate data to.  Inactivating component type: {1}",
							newComponentType, componentType
					));
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

	private void removeTypeFromSubmissionTemplates(String componentType)
	{
		Objects.requireNonNull(componentType);

		SubmissionFormTemplate submissionFormTemplate = new SubmissionFormTemplate();
		submissionFormTemplate.setEntryType(componentType);

		List<SubmissionFormTemplate> allTemplates = submissionFormTemplate.findByExampleProxy();
		for (SubmissionFormTemplate template : allTemplates) {
			template.setEntryType(null);
			persistenceService.persist(template);
		}
	}

	private void removeTypeUpdateChildren(ComponentType componentTypeFound, String newComponentType)
	{
		// Update children
		ComponentType childComponentTypes = new ComponentType();
		childComponentTypes.setParentComponentType(componentTypeFound.getComponentType());
		List<ComponentType> componentTypeChildren = childComponentTypes.findByExampleProxy();

		componentTypeChildren.forEach(child -> {
			child.setParentComponentType(newComponentType);
			persistenceService.persist(child);
		});
	}

	private void removeTypeCleanupAttributes(String componentType)
	{
		//remove restrictions
		AttributeType attributeTypeExample = new AttributeType();
		List<AttributeType> allAttributes = attributeTypeExample.findByExampleProxy();
		for (AttributeType attributeType : allAttributes) {

			boolean updateAttributeBasedOnRequired = removeTypeRequiredAttributeToRemove(attributeType, componentType);
			boolean updateAttributeBasedOnOptional = removeTypeOptionalAttributeToRemove(attributeType, componentType);

			if (updateAttributeBasedOnRequired || updateAttributeBasedOnOptional) {
				persistenceService.persist(attributeType);
			}

		}
	}

	private boolean removeTypeOptionalAttributeToRemove(AttributeType attributeType, String componentType)
	{
		boolean updateAttribute = false;
		if (attributeType.getOptionalRestrictions() != null && !attributeType.getOptionalRestrictions().isEmpty()) {
			for (int i = attributeType.getOptionalRestrictions().size() - 1; i >= 0; i--) {
				String checkType = attributeType.getOptionalRestrictions().get(i).getComponentType();
				if (checkType.equals(componentType) || findComponentType(getAllComponentTypes(), checkType) == null) {
					attributeType.getOptionalRestrictions().remove(i);
					updateAttribute = true;
				}
			}
		}
		return updateAttribute;
	}

	private boolean removeTypeRequiredAttributeToRemove(AttributeType attributeType, String componentType)
	{
		boolean updateAttribute = false;
		if (attributeType.getRequiredRestrictions() != null && !attributeType.getRequiredRestrictions().isEmpty()) {
			for (int i = attributeType.getRequiredRestrictions().size() - 1; i >= 0; i--) {
				String checkType = attributeType.getRequiredRestrictions().get(i).getComponentType();
				if (checkType.equals(componentType) || findComponentType(getAllComponentTypes(), checkType) == null) {
					attributeType.getRequiredRestrictions().remove(i);
					updateAttribute = true;
				}
			}
		}
		return updateAttribute;
	}

	private void removeTypeMigrateData(String newComponentType, String componentType)
	{
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

		UserSubmission setUserSubmission = new UserSubmission();
		setUserSubmission.setComponentType(newComponentType);

		UserSubmission whereUserSubmission = new UserSubmission();
		whereUserSubmission.setComponentType(componentType);
		persistenceService.updateByExample(UserSubmission.class, setUserSubmission, whereUserSubmission);

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

	public Boolean resolveComponentTypeIncludeIconInSearch(String componentType)
	{
		Boolean includeInSearch = Boolean.FALSE;
		List<ComponentType> componentTypes = getAllComponentTypes();

		for (ComponentType componentTypeLocal : componentTypes) {
			if (componentTypeLocal.getComponentType().equals(componentType)
					&& componentTypeLocal.getIncludeIconInSearch() != null) {

				includeInSearch = componentTypeLocal.getIncludeIconInSearch();
				break;
			}
		}

		return includeInSearch;
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
					clearCache = removeRoleProcessRoleLinks(componentType, roleName, clearCache);
				}
			}

			if (clearCache) {
				OSFCacheManager.getComponentTypeCache().removeAll();
			}
		}
	}

	private boolean removeRoleProcessRoleLinks(ComponentType componentType, String roleName, boolean clearCache)
	{
		for (RoleLink roleLink : componentType.getAssignedGroups()) {
			if (roleLink.getRoleName().equals(roleName)) {
				boolean removeItem = performRemoveRoleFromType(componentType.getComponentType(), roleName);
				if (removeItem) {
					clearCache = true;
				}
			}
		}
		return clearCache;
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
					clearCache = removeUserProcessUserLinks(componentType, username, clearCache);
				}
			}

			if (clearCache) {
				OSFCacheManager.getComponentTypeCache().removeAll();
			}
		}
	}

	private boolean removeUserProcessUserLinks(ComponentType componentType, String username, boolean clearCache)
	{
		for (UserLink userLink : componentType.getAssignedUsers()) {
			if (userLink.getUsername().equals(username)) {
				boolean removeItem = performRemoveUserFromType(componentType.getComponentType(), username);
				if (removeItem) {
					clearCache = true;
				}
			}
		}
		return clearCache;
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
			resolution = processFirstParentWithTemplate(componentTypes, child);
		}
		return resolution;
	}

	private ComponentTypeTemplateResolution processFirstParentWithTemplate(List<ComponentType> componentTypes, ComponentType child)
	{
		ComponentTypeTemplateResolution resolution = null;
		for (ComponentType componentType : componentTypes) {
			if (componentType.getComponentType().equals(child.getParentComponentType())) {
				if (componentType.getComponentTypeTemplate() != null) {
					resolution = constructTemplateResolution(componentType);
					break;
				} else {
					resolution = findFirstParentWithTemplate(componentTypes, componentType);
				}
			}
		}
		return resolution;
	}

	private ComponentTypeTemplateResolution constructTemplateResolution(ComponentType componentType)
	{
		ComponentTypeTemplateResolution resolution = new ComponentTypeTemplateResolution();
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
			resolution = processFirstParentWithRoles(componentTypes, child);
		}
		return resolution;
	}

	private ComponentTypeRoleResolution processFirstParentWithRoles(List<ComponentType> componentTypes, ComponentType child)
	{
		ComponentTypeRoleResolution resolution = null;
		for (ComponentType componentType : componentTypes) {
			if (componentType.getComponentType().equals(child.getParentComponentType())) {
				if (componentType.getAssignedGroups() != null
						&& !componentType.getAssignedGroups().isEmpty()) {

					resolution = constructRoleResolution(componentType);
					break;
				} else {
					resolution = findFirstParentWithRoles(componentTypes, componentType);
				}
			}
		}
		return resolution;
	}

	private ComponentTypeRoleResolution constructRoleResolution(ComponentType componentType)
	{
		ComponentTypeRoleResolution resolution = new ComponentTypeRoleResolution();
		resolution.setCameFromAncestor(true);
		resolution.setAncestorComponentType(componentType.getComponentType());
		resolution.setAncestorComponentTypeLabel(componentType.getLabel());
		for (RoleLink link : componentType.getAssignedGroups()) {
			resolution.getRoles().add(link.getRoleName());
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
			resolution = processFirstParentWithUser(componentTypes, child);
		}
		return resolution;
	}

	private ComponentTypeUserResolution processFirstParentWithUser(List<ComponentType> componentTypes, ComponentType child)
	{
		ComponentTypeUserResolution resolution = null;
		for (ComponentType componentType : componentTypes) {
			if (componentType.getComponentType().equals(child.getParentComponentType())) {
				if (componentType.getAssignedUsers() != null
						&& !componentType.getAssignedUsers().isEmpty()) {

					resolution = constructUserResolution(componentType);
					break;
				} else {
					resolution = findFirstParentWithUser(componentTypes, componentType);
				}
			}
		}
		return resolution;
	}

	private ComponentTypeUserResolution constructUserResolution(ComponentType componentType)
	{
		ComponentTypeUserResolution resolution = new ComponentTypeUserResolution();
		resolution.setCameFromAncestor(true);
		resolution.setAncestorComponentType(componentType.getComponentType());
		resolution.setAncestorComponentTypeLabel(componentType.getLabel());
		for (UserLink link : componentType.getAssignedUsers()) {
			resolution.getUsernames().add(link.getUsername());
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

	public Component changeComponentType(String componentId, String newType)
	{
		Component component = persistenceService.findById(Component.class, componentId);
		if (component != null) {
			ComponentType found = persistenceService.findById(ComponentType.class, newType);
			if (found != null) {

				if (!newType.equals(component.getComponentType())) {

					component.setComponentType(newType);
					component.populateBaseUpdateFields();
					persistenceService.persist(component);

					updateComponentLastActivity(componentId);
				}

			} else {
				throw new OpenStorefrontRuntimeException("Unable to find component type.", "Check name of type: " + newType);
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find component.", "Check id passed: " + componentId);
		}
		return component;
	}

	public List<ComponentType> getComponentTypeParents(String componentTypeId, Boolean reverseOrder)
	{
		List<ComponentType> componentTypes = getAllComponentTypes();
		ComponentType currentComponentType = findComponentType(componentTypes, componentTypeId);

		List<ComponentType> componentTypeParents = new ArrayList<>();

		if (currentComponentType != null && currentComponentType.getParentComponentType() != null) {
			do {
				currentComponentType = findComponentType(componentTypes, currentComponentType.getParentComponentType());

				if (currentComponentType != null) {
					componentTypeParents.add(currentComponentType);
				}
			} while (currentComponentType != null && currentComponentType.getParentComponentType() != null);

			if (reverseOrder) {
				Collections.reverse(componentTypeParents);
			}
		}

		return componentTypeParents;
	}

	public String getComponentTypeParentsString(String componentTypeId, Boolean reverseOrder)
	{
		List<ComponentType> componentTypes = getAllComponentTypes();
		ComponentType typeLocal;
		try {
			typeLocal = findComponentType(componentTypes, componentTypeId);
		} catch (OpenStorefrontRuntimeException ex) {
			LOG.log(Level.WARNING, ex, () -> "Unable to Find Component Type: " + componentTypeId);
			return "(" + componentTypeId + ")";
		}

		List<ComponentType> parentChildComponentTypes = new ArrayList<>();
		List<ComponentType> parentComponentTypes = getComponentTypeParents(componentTypeId, reverseOrder);

		if (reverseOrder) {
			parentChildComponentTypes.addAll(parentComponentTypes);
			parentChildComponentTypes.add(typeLocal);
		} else {
			parentChildComponentTypes.add(typeLocal);
			parentChildComponentTypes.addAll(parentComponentTypes);
		}

		String labels = parentChildComponentTypes.stream()
				.map(t -> t.getLabel())
				.collect(Collectors.joining(reverseOrder ? " > " : " < "));

		return labels;
	}

}
