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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@Embeddable
public class WorkPlanStepRole
		implements Serializable
{

	private static final long serialVersionUID = 1L;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@FK(SecurityRole.class)
	private String securityRole;

	@Version
	private String storageVersion;

	public WorkPlanStepRole()
	{
	}

	public String getSecurityRole()
	{
		return securityRole;
	}

	public void setSecurityRole(String securityRole)
	{
		this.securityRole = securityRole;
	}

	public String getStorageVersion()
	{
		return storageVersion;
	}

	public void setStorageVersion(String storageVersion)
	{
		this.storageVersion = storageVersion;
	}

}
