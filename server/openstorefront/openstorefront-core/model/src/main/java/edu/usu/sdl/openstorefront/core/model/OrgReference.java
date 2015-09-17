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
package edu.usu.sdl.openstorefront.core.model;

import edu.usu.sdl.openstorefront.core.entity.OrganizationModel;

/**
 *
 * @author dshurtleff
 */
public class OrgReference
{

	@FunctionalInterface
	public interface ReferenceTranformer<T extends OrganizationModel>
	{

		public OrgReference transform(T entity);
	}

	private String referenceType;
	private String referenceId;
	private String referenceName;
	private String componentId;
	private String componentName;
	private String componentApproveStatus;
	private String activeStatus;

	public OrgReference()
	{
	}

	public String getReferenceId()
	{
		return referenceId;
	}

	public void setReferenceId(String referenceId)
	{
		this.referenceId = referenceId;
	}

	public String getReferenceName()
	{
		return referenceName;
	}

	public void setReferenceName(String referenceName)
	{
		this.referenceName = referenceName;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getComponentName()
	{
		return componentName;
	}

	public void setComponentName(String componentName)
	{
		this.componentName = componentName;
	}

	public String getReferenceType()
	{
		return referenceType;
	}

	public void setReferenceType(String referenceType)
	{
		this.referenceType = referenceType;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getComponentApproveStatus()
	{
		return componentApproveStatus;
	}

	public void setComponentApproveStatus(String componentApproveStatus)
	{
		this.componentApproveStatus = componentApproveStatus;
	}

}
