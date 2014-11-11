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
package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.doc.ValidValueType;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
public class ComponentIntegration
		extends BaseEntity
{

	@PK
	@NotNull
	@ConsumeField
	private String componentId;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_CRON)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String refreshRate;

	@NotNull
	@ValidValueType(value = {}, lookupClass = RunStatus.class)
	private String status;
	private Date lastStartTime;
	private Date lastEndTime;

	public ComponentIntegration()
	{

	}

	public String getRefreshRate()
	{
		return refreshRate;
	}

	public void setRefreshRate(String refreshRate)
	{
		this.refreshRate = refreshRate;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public Date getLastStartTime()
	{
		return lastStartTime;
	}

	public void setLastStartTime(Date lastStartTime)
	{
		this.lastStartTime = lastStartTime;
	}

	public Date getLastEndTime()
	{
		return lastEndTime;
	}

	public void setLastEndTime(Date lastEndTime)
	{
		this.lastEndTime = lastEndTime;
	}

}
