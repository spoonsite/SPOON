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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.UserWatch;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
public class UserWatchView
{

	@NotNull
	private String watchId;

	@NotNull
	private Date lastUpdateDts;

	@NotNull
	private Date lastViewDts;

	@NotNull
	private Date createDts;

	@NotNull
	private String componentName;

	@NotNull
	@ConsumeField
	private String componentId;

	@NotNull
	@ConsumeField
	private Boolean notifyFlg;

	private String activeStatus;
	private String createUser;
	private String updateUser;

	public static UserWatchView toView(UserWatch watch, Component component)
	{
		UserWatchView view = new UserWatchView();
		view.setComponentId(watch.getComponentId());
		view.setCreateDts(watch.getCreateDts());
		if (component != null) {
			view.setComponentName(component.getName());
			view.setLastUpdateDts(component.getLastActivityDts());
		}
		view.setLastViewDts(watch.getLastViewDts());
		view.setNotifyFlg(watch.getNotifyFlg());
		view.setWatchId(watch.getUserWatchId());
		view.setActiveStatus(watch.getActiveStatus());
		view.setUpdateUser(watch.getUpdateUser());
		view.setCreateUser(watch.getCreateUser());
		return view;
	}

	public static List<UserWatchView> toViewList(List<UserWatch> watches)
	{
		List<UserWatchView> views = new ArrayList<>();

		Set<String> componentIdSet = new HashSet<>();
		watches.forEach(watch -> {
			componentIdSet.add(watch.getComponentId());
		});

		if (componentIdSet.isEmpty() == false) {
			String query = "Select from " + Component.class.getSimpleName() + " where componentId in :idSet";
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("idSet", componentIdSet);

			List<Component> components = ServiceProxyFactory.getServiceProxy().getPersistenceService().query(query, paramMap);
			Map<String, List<Component>> componentMap = components.stream().collect(Collectors.groupingBy(Component::getComponentId));
			watches.forEach(watch -> {
				List<Component> componentsList = componentMap.get(watch.getComponentId());
				if (componentsList != null) {
					views.add(toView(watch, componentsList.get(0)));
				}
			});
		}

		return views;
	}

	public String getWatchId()
	{
		return watchId;
	}

	public void setWatchId(String watchId)
	{
		this.watchId = watchId;
	}

	public Date getLastUpdateDts()
	{
		return lastUpdateDts;
	}

	public void setLastUpdateDts(Date lastUpdateDts)
	{
		this.lastUpdateDts = lastUpdateDts;
	}

	public Date getLastViewDts()
	{
		return lastViewDts;
	}

	public void setLastViewDts(Date lastViewDts)
	{
		this.lastViewDts = lastViewDts;
	}

	public Date getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	public String getComponentName()
	{
		return componentName;
	}

	public void setComponentName(String componentName)
	{
		this.componentName = componentName;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public Boolean getNotifyFlg()
	{
		return notifyFlg;
	}

	public void setNotifyFlg(Boolean notifyFlg)
	{
		this.notifyFlg = notifyFlg;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public String getUpdateUser()
	{
		return updateUser;
	}

	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}

}
