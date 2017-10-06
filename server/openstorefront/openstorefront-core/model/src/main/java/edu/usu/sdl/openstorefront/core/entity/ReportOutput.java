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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@Embeddable
public class ReportOutput
		implements Serializable
{

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = ReportFormat.class)
	@ConsumeField
	@FK(ReportFormat.class)
	private String reportTransmissionType;

	@ConsumeField
	@Embedded
	@OneToOne(orphanRemoval = true)
	private ReportTransmissionOption reportTransmissionOption;

	public ReportOutput()
	{
	}

	//TODO: Requires custom validation on certain types
	public String getReportTransmissionType()
	{
		return reportTransmissionType;
	}

	public void setReportTransmissionType(String reportTransmissionType)
	{
		this.reportTransmissionType = reportTransmissionType;
	}

	public ReportTransmissionOption getReportTransmissionOption()
	{
		return reportTransmissionOption;
	}

	public void setReportTransmissionOption(ReportTransmissionOption reportTransmissionOption)
	{
		this.reportTransmissionOption = reportTransmissionOption;
	}

}
