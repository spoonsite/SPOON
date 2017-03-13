/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Version;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Part of Security Role")
@Embeddable
public class SecurityRoleData
		implements Serializable
{

	@ConsumeField
	@ValidValueType(value = {}, lookupClass = DataSensitivity.class)
	@FK(DataSensitivity.class)
	private String dataSensitivity;

	@ConsumeField
	@ValidValueType(value = {}, lookupClass = DataSource.class)
	@FK(DataSource.class)
	private String dataSource;

	@Version
	private String storageVersion;

	public SecurityRoleData()
	{
	}

	public String getStorageVersion()
	{
		return storageVersion;
	}

	public void setStorageVersion(String storageVersion)
	{
		this.storageVersion = storageVersion;
	}

	public String getDataSensitivity()
	{
		return dataSensitivity;
	}

	public void setDataSensitivity(String dataSensitivity)
	{
		this.dataSensitivity = dataSensitivity;
	}

	public String getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(String dataSource)
	{
		this.dataSource = dataSource;
	}

}
