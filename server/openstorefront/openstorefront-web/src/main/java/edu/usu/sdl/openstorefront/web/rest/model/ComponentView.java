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

import edu.usu.sdl.openstorefront.storage.model.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentView
{
	private String componentId;
	private String name;
	private String description;
	private String parentComponentId;
	private String guid;
	private String organization;
	private Date releaseDate;
	private Date postDate;
	private String version;
	private String approvalState;
	private String approvedUser;
	private Date approvedDts;
	private String createUser;
	private Date createDts;
	private String updateUser;
	private Date updateDts;
	private Date lastActivityDts;

	public ComponentView()
	{
	}
	
	public static List<ComponentView> toViewList(List<Component> components)
	{
		List<ComponentView> views = new ArrayList<>();
		components.forEach(component->{
			views.add(ComponentView.toView(component));
		});
		return views;
	}
	
	public static ComponentView toView(Component component)
	{
		ComponentView view = new ComponentView();
			view.setComponentId(component.getComponentId());
			view.setName(component.getName());
			view.setDescription(component.getDescription());
			view.setParentComponentId(component.getParentComponentId());
			view.setGuid(component.getGuid());
			view.setOrganization(component.getOrganization());
			view.setReleaseDate(component.getReleaseDate());
			view.setPostDate(component.getPostDate());
			view.setVersion(component.getVersion());
			view.setApprovalState(component.getApprovalState());
			view.setApprovedUser(component.getApprovedUser());
			view.setApprovedDts(component.getApprovedDts());
			view.setCreateUser(component.getCreateUser());
			view.setCreateDts(component.getCreateDts());
			view.setUpdateUser(component.getUpdateUser());
			view.setUpdateDts(component.getUpdateDts());
			view.setLastActivityDts(component.getLastActivityDts());
		return view;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getParentComponentId()
	{
		return parentComponentId;
	}

	public void setParentComponentId(String parentComponentId)
	{
		this.parentComponentId = parentComponentId;
	}

	public String getGuid()
	{
		return guid;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getApprovalState()
	{
		return approvalState;
	}

	public void setApprovalState(String approvalState)
	{
		this.approvalState = approvalState;
	}

	public String getApprovedUser()
	{
		return approvedUser;
	}

	public void setApprovedUser(String approvedUser)
	{
		this.approvedUser = approvedUser;
	}

	public Date getApprovedDts()
	{
		return approvedDts;
	}

	public void setApprovedDts(Date approvedDts)
	{
		this.approvedDts = approvedDts;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public Date getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	public String getUpdateUser()
	{
		return updateUser;
	}

	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public Date getLastActivityDts()
	{
		return lastActivityDts;
	}

	public void setLastActivityDts(Date lastActivityDts)
	{
		this.lastActivityDts = lastActivityDts;
	}

	public Date getReleaseDate()
	{
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate)
	{
		this.releaseDate = releaseDate;
	}

	public Date getPostDate()
	{
		return postDate;
	}

	public void setPostDate(Date postDate)
	{
		this.postDate = postDate;
	}

}
