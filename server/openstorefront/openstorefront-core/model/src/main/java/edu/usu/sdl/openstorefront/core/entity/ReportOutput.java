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
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

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
	@ValidValueType(value = {}, lookupClass = ReportTransmissionType.class)
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

	public String toFormatKey()
	{
		String key = getReportTransmissionType() + "-" + getReportTransmissionOption().getReportFormat();
		return key;
	}

	public ValidationResult customValidation()
	{
		ValidationResult validationResult = new ValidationResult();

		if (StringUtils.isNotBlank(reportTransmissionType)) {

			if (reportTransmissionOption != null) {
				switch (reportTransmissionType) {
					case ReportTransmissionType.EMAIL:
						if (reportTransmissionOption.getEmailAddresses() == null
								|| reportTransmissionOption.getEmailAddresses().isEmpty()) {

							RuleResult ruleResult = new RuleResult();
							ruleResult.setEntityClassName(ReportTransmissionOption.class.getSimpleName());
							ruleResult.setFieldName("emailAddress");
							ruleResult.setMessage("Must have at least one email address.");
							validationResult.getRuleResults().add(ruleResult);
						}
						break;
					case ReportTransmissionType.CONFLUENCE:
						if (StringUtils.isBlank(reportTransmissionOption.getConfluenceSpace())) {
							RuleResult ruleResult = new RuleResult();
							ruleResult.setEntityClassName(ReportTransmissionOption.class.getSimpleName());
							ruleResult.setFieldName("confluenceSpace");
							ruleResult.setMessage("Confluence Requires Space name");
							validationResult.getRuleResults().add(ruleResult);
						}
						if (StringUtils.isBlank(reportTransmissionOption.getConfluencePage())) {
							RuleResult ruleResult = new RuleResult();
							ruleResult.setEntityClassName(ReportTransmissionOption.class.getSimpleName());
							ruleResult.setFieldName("confluencePage");
							ruleResult.setMessage("Confluence Requires Page name");
							validationResult.getRuleResults().add(ruleResult);
						}
						break;
				}
			}

		} else {
			RuleResult ruleResult = new RuleResult();
			ruleResult.setEntityClassName(ReportOutput.class.getSimpleName());
			ruleResult.setFieldName("reportTransmissionType");
			ruleResult.setMessage("Missing tranmission type");
			validationResult.getRuleResults().add(ruleResult);
		}

		return validationResult;
	}

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
