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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Version;

/**
 *
 * @author dshurtleff
 */
@APIDescription("This is part of the alert entity")
@Embeddable
public class SystemErrorAlertOption
		implements Serializable
{

	@ConsumeField
	private Boolean alertOnSystem;

	@ConsumeField
	private Boolean alertOnREST;

	@ConsumeField
	private Boolean alertOnIntegration;

	@ConsumeField
	private Boolean alertOnReport;

	@Version
	private String storageVersion;

	public SystemErrorAlertOption()
	{
	}

	public Boolean getAlertOnSystem()
	{
		return alertOnSystem;
	}

	public void setAlertOnSystem(Boolean alertOnSystem)
	{
		this.alertOnSystem = alertOnSystem;
	}

	public Boolean getAlertOnREST()
	{
		return alertOnREST;
	}

	public void setAlertOnREST(Boolean alertOnREST)
	{
		this.alertOnREST = alertOnREST;
	}

	public Boolean getAlertOnIntegration()
	{
		return alertOnIntegration;
	}

	public void setAlertOnIntegration(Boolean alertOnIntegration)
	{
		this.alertOnIntegration = alertOnIntegration;
	}

	public Boolean getAlertOnReport()
	{
		return alertOnReport;
	}

	public void setAlertOnReport(Boolean alertOnReport)
	{
		this.alertOnReport = alertOnReport;
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
