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

import edu.usu.sdl.openstorefront.core.entity.ComponentExternalDependency;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentExternalDependencyView
		extends StandardEntityView
{

	private String dependencyId;
	private String dependencyName;
	private String version;
	private String dependancyReferenceLink;
	private String comment;
	private Date updateDts;
	private String activeStatus;	

	public ComponentExternalDependencyView()
	{
	}

	public static ComponentExternalDependencyView toView(ComponentExternalDependency dependency)
	{
		ComponentExternalDependencyView view = new ComponentExternalDependencyView();
		view.setComment(dependency.getComment());
		view.setVersion(dependency.getVersion());
		view.setDependancyReferenceLink(dependency.getDependancyReferenceLink());
		view.setDependencyName(dependency.getDependencyName());
		view.setDependencyId(dependency.getDependencyId());
		view.setUpdateDts(dependency.getUpdateDts());
		view.setActiveStatus(dependency.getActiveStatus());
		view.toStandardView(dependency);

		return view;
	}

	public static List<ComponentExternalDependencyView> toViewList(List<ComponentExternalDependency> dependencies)
	{
		List<ComponentExternalDependencyView> views = new ArrayList<>();
		dependencies.forEach(dependency -> {
			views.add(toView(dependency));
		});
		return views;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getDependancyReferenceLink()
	{
		return dependancyReferenceLink;
	}

	public void setDependancyReferenceLink(String dependancyReferenceLink)
	{
		this.dependancyReferenceLink = dependancyReferenceLink;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public String getDependencyId()
	{
		return dependencyId;
	}

	public void setDependencyId(String dependencyId)
	{
		this.dependencyId = dependencyId;
	}

	public String getDependencyName()
	{
		return dependencyName;
	}

	public void setDependencyName(String dependencyName)
	{
		this.dependencyName = dependencyName;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

}
