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
package edu.usu.sdl.openstorefront.service.component;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.entity.AlertType;
import edu.usu.sdl.openstorefront.core.entity.ComponentUpdateQueue;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.core.model.AlertContext;
import edu.usu.sdl.openstorefront.service.ComponentServiceImpl;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import java.util.Objects;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseComponentServiceImpl
{

//	private static final Logger LOG = Logger.getLogger(BaseComponentServiceImpl.class.getName());
	protected ComponentServiceImpl componentService;
	protected PersistenceService persistenceService;
	protected CoreComponentServiceImpl core;
	protected SubComponentServiceImpl sub;
	protected IntegrationComponentServiceImpl integration;
	protected ComponentTypeServiceImpl type;
	protected FilterEngine filterEngine;

	public BaseComponentServiceImpl(ComponentServiceImpl componentService)
	{
		this.componentService = componentService;
		persistenceService = componentService.getPersistenceService();
		this.filterEngine = componentService.getFilterEngine();
	}

	public void init()
	{
		core = componentService.getCore();
		sub = componentService.getSub();
		integration = componentService.getIntegration();
	}

	protected void updateComponentLastActivity(String componentId)
	{
		Objects.requireNonNull(componentId, "Component Id is required");

		cleanupCache(componentId);

		ComponentUpdateQueue componentUpdateQueue = new ComponentUpdateQueue();
		componentUpdateQueue.setComponentId(componentId);
		componentUpdateQueue.setUpdateDts(TimeUtil.currentDate());
		componentUpdateQueue.setNodeId(PropertiesManager.getInstance().getNodeName());
		componentUpdateQueue.setUpdateId(componentService.getPersistenceService().generateId());
		componentUpdateQueue.setModificationType(componentService.getModificationType());
		componentUpdateQueue.populateBaseCreateFields();
		componentService.getPersistenceService().persist(componentUpdateQueue);

	}

	protected void cleanupCache(String componentId)
	{
		Objects.requireNonNull(componentId, "Component Id is required");

		OSFCacheManager.getComponentCache().remove(componentId);
		OSFCacheManager.getComponentLookupCache().remove(componentId);
		OSFCacheManager.getComponentApprovalCache().removeAll();
		OSFCacheManager.getComponentDataRestrictionCache().remove(componentId);
		OSFCacheManager.getComponentIconCache().remove(componentId);
		OSFCacheManager.getComponentTypeComponentCache().remove(componentId);
		OSFCacheManager.getSearchCache().removeAll();
	}

	protected void handleUserDataAlert(Object data)
	{
		AlertContext alertContext = new AlertContext();
		alertContext.setAlertType(AlertType.USER_DATA);
		alertContext.setDataTrigger(data);
		componentService.getAlertService().checkAlert(alertContext);
	}

}
