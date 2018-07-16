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
 * @author ra nethercott
 */
@APIDescription("Holds work plan information")
public class WorkPlan
		extends StandardEntity<Alert>
{

	private static final long serialVersionUID = 1L;

	@PK(generated = true)
	@NotNull
	private String workPlanId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String name;

	@DataType(WorkPlanStep.class)
	@ConsumeField
	@Embedded
	@OneToMany(orphanRemoval = true)
	private List<WorkPlanStep> steps;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String inProgressColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String pendingColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String completeColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String changeRequestColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String type;

	//Entry Types array of strings?
	// @DataType(ComponentType.class)
	// @ConsumeField
	// @Embedded
	// @OneToMany(orphanRemoval = true)
	// private List<ComponentType> ;
	

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public WorkPlan()
	{
	}

	//@Override
	
	// public WorkPlanStep goToStep(int stepNumber)
	// {
	// 	return this.steps.get(stepNumber);
	// }

	// public void setAlertId(String alertId)
	// {
	// 	this.alertId = alertId;
	// }

	// public String getAlertType()
	// {
	// 	return alertType;
	// }


}
