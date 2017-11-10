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
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.Date;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Defines a sheduled report")
public class ScheduledReport
		extends StandardEntity<ScheduledReport>
{

	@PK(generated = true)
	@NotNull
	private String scheduleReportId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = ReportType.class)
	@ConsumeField
	@FK(ReportType.class)
	private String reportType;

	@Deprecated
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = ReportFormat.class)
	@ConsumeField
	@FK(ReportFormat.class)
	@APIDescription("Deprecated: Use Report Outputs and NOT this field")
	private String reportFormat;

	@ConsumeField
	@Embedded
	@OneToOne(orphanRemoval = true)
	private ReportOption reportOption;

	@Deprecated
	@ConsumeField
	@Embedded
	@DataType(EmailAddress.class)
	@OneToMany(orphanRemoval = true)
	@APIDescription("Deprecated: Use Report Outputs and NOT this field")
	private List<EmailAddress> emailAddresses;

	@Min(1)
	@Max(30)
	@ConsumeField
	private Integer scheduleIntervalDays;

	@ConsumeField
	@Min(1)
	private Integer scheduleIntervalMinutes;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	private String scheduleIntervalCron;

	@ConsumeField
	@Embedded
	@DataType(ReportDataId.class)
	@OneToMany(orphanRemoval = true)
	private List<ReportDataId> ids;

	private Date lastRanDts;

	@ConsumeField
	@Embedded
	@DataType(ReportOutput.class)
	@OneToMany(orphanRemoval = true)
	private List<ReportOutput> reportOutputs;

	public ScheduledReport()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		ScheduledReport scheduledReport = (ScheduledReport) entity;

		this.setLastRanDts(scheduledReport.getLastRanDts());
		this.setEmailAddresses(scheduledReport.getEmailAddresses());
		this.setReportFormat(scheduledReport.getReportFormat());
		this.setReportOption(scheduledReport.getReportOption());
		this.setReportType(scheduledReport.getReportType());
		this.setScheduleIntervalDays(scheduledReport.getScheduleIntervalDays());
		this.setScheduleIntervalMinutes(scheduledReport.getScheduleIntervalMinutes());
		this.setScheduleIntervalCron(scheduledReport.getScheduleIntervalCron());
		this.setIds(scheduledReport.getIds());
		this.setReportOutputs(scheduledReport.getReportOutputs());

	}

	public ValidationResult customValidation()
	{
		ValidationResult validationResult = new ValidationResult();

		if (getReportOutputs() == null || getReportOutputs().isEmpty()) {
			RuleResult ruleResult = new RuleResult();
			ruleResult.setEntityClassName(ReportOutput.class.getSimpleName());
			ruleResult.setFieldName("reportOutputs");
			ruleResult.setMessage("Must have at least one output");
			validationResult.getRuleResults().add(ruleResult);
		} else {
			for (ReportOutput output : getReportOutputs()) {
				validationResult.merge(output.customValidation());
			}
		}
		return validationResult;
	}

	public String getScheduleReportId()
	{
		return scheduleReportId;
	}

	public void setScheduleReportId(String scheduleReportId)
	{
		this.scheduleReportId = scheduleReportId;
	}

	public String getReportType()
	{
		return reportType;
	}

	public void setReportType(String reportType)
	{
		this.reportType = reportType;
	}

	public ReportOption getReportOption()
	{
		return reportOption;
	}

	public void setReportOption(ReportOption reportOption)
	{
		this.reportOption = reportOption;
	}

	@Deprecated
	public List<EmailAddress> getEmailAddresses()
	{
		return emailAddresses;
	}

	@Deprecated
	public void setEmailAddresses(List<EmailAddress> emailAddresses)
	{
		this.emailAddresses = emailAddresses;
	}

	@Deprecated
	public String getReportFormat()
	{
		return reportFormat;
	}

	@Deprecated
	public void setReportFormat(String reportFormat)
	{
		this.reportFormat = reportFormat;
	}

	public Date getLastRanDts()
	{
		return lastRanDts;
	}

	public void setLastRanDts(Date lastRanDts)
	{
		this.lastRanDts = lastRanDts;
	}

	public Integer getScheduleIntervalDays()
	{
		return scheduleIntervalDays;
	}

	public void setScheduleIntervalDays(Integer scheduleIntervalDays)
	{
		this.scheduleIntervalDays = scheduleIntervalDays;
	}

	public List<ReportOutput> getReportOutputs()
	{
		return reportOutputs;
	}

	public void setReportOutputs(List<ReportOutput> reportOutputs)
	{
		this.reportOutputs = reportOutputs;
	}

	public Integer getScheduleIntervalMinutes()
	{
		return scheduleIntervalMinutes;
	}

	public void setScheduleIntervalMinutes(Integer scheduleIntervalMinutes)
	{
		this.scheduleIntervalMinutes = scheduleIntervalMinutes;
	}

	public String getScheduleIntervalCron()
	{
		return scheduleIntervalCron;
	}

	public void setScheduleIntervalCron(String scheduleIntervalCron)
	{
		this.scheduleIntervalCron = scheduleIntervalCron;
	}

	public List<ReportDataId> getIds()
	{
		return ids;
	}

	public void setIds(List<ReportDataId> ids)
	{
		this.ids = ids;
	}

}
