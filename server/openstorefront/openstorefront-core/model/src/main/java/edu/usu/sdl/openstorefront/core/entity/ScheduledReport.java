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

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = ReportFormat.class)
	@ConsumeField
	@FK(ReportFormat.class)
	private String reportFormat;

	@ConsumeField
	@Embedded
	@OneToOne(orphanRemoval = true)
	private ReportOption reportOption;

	@ConsumeField
	@Embedded
	@DataType(EmailAddress.class)
	@OneToMany(orphanRemoval = true)
	private List<EmailAddress> emailAddresses;

	@NotNull
	@Min(1)
	@Max(30)
	@ConsumeField
	private Integer scheduleIntervalDays;
	
	@ConsumeField
	@DataType(ReportDataId.class)
	@OneToMany(orphanRemoval = true)
	private List<ReportDataId> componentIds;

	private Date lastRanDts;

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

	public List<EmailAddress> getEmailAddresses()
	{
		return emailAddresses;
	}

	public void setEmailAddresses(List<EmailAddress> emailAddresses)
	{
		this.emailAddresses = emailAddresses;
	}

	public String getReportFormat()
	{
		return reportFormat;
	}

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
	
	public List<ReportDataId> getComponentIds()
	{
		return componentIds;
	}

	public void setComponentIds(List<ReportDataId> componentIds)
	{
		this.componentIds = componentIds;
	}

}
