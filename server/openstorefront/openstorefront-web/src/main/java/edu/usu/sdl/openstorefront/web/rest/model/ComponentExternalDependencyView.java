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

import edu.usu.sdl.openstorefront.storage.model.ComponentExternalDependency;

/**
 *
 * @author dshurtleff
 */
public class ComponentExternalDependencyView
{

	private String dependency;
	private String version;
	private String dependancyReferenceLink;
	private String comment;

	public ComponentExternalDependencyView()
	{
	}
	
	public static ComponentExternalDependencyView toView(ComponentExternalDependency dependency)
	{
		ComponentExternalDependencyView view = new ComponentExternalDependencyView();
		view.setComment(dependency.getComment());
		view.setVersion(dependency.getVersion());
		view.setDependancyReferenceLink(dependency.getDependancyReferenceLink());
		view.setDependency(dependency.getDependencyName());
		return view;
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

	public String getDependency()
	{
		return dependency;
	}

	public void setDependency(String dependency)
	{
		this.dependency = dependency;
	}

}
