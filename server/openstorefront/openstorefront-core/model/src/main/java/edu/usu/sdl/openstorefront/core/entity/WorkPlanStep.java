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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds work plan step information")
public class WorkPlanStep
		extends StandardEntity<WorkPlanStep>
{

	private static final long serialVersionUID = 1L;

	@PK(generated = true)
	@NotNull
	private String workPlanStepId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String name;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_80)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String description;

	

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public WorkPlanStep()
	{
	}

//	@Override
	// public <T extends StandardEntity> void updateFields(T entity)
	// {
	// 	super.updateFields(entity);

	// 	Alert alertUpdate = (Alert) entity;
	// 	this.setAlertType(alertUpdate.getAlertType());
	// 	this.setEmailAddresses(alertUpdate.getEmailAddresses());
	// 	this.setName(alertUpdate.getName());
	// 	this.setSystemErrorAlertOption(alertUpdate.getSystemErrorAlertOption());
	// 	this.setUserDataAlertOption(alertUpdate.getUserDataAlertOption());
	// 	this.setComponentTypeAlertOptions(alertUpdate.getComponentTypeAlertOptions());
	// }

	public String getWorkPlanStepId()
	{
		return workPlanStepId;
	}

	public void setWorkPlanStepId(String wPStepId)
	{
		this.workPlanStepId = wPStepId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	
}
