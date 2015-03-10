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
package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.ValidValueType;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
public class ScheduledReport
		extends BaseEntity<ScheduledReport>
{

	@PK(generated = true)
	@NotNull
	private String scheduleReportId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = ReportType.class)
	@ConsumeField
	private String reportType;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = ReportFormat.class)
	@ConsumeField
	private String reportFormat;

	@ConsumeField
	private ReportOption reportOption;

	@DataType(String.class)
	@ConsumeField
	@Pattern(regexp = OpenStorefrontConstant.EMAIL_PATTERN)
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_EMAIL)
	@Sanitize(TextSanitizer.class)
	private List<String> emailAddresses = new ArrayList<>();

	@NotNull
	@Min(1)
	@Max(30)
	@ConsumeField
	private Integer scheduleIntevalDays;

	private Date lastRanDts;

	public ScheduledReport()
	{
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

	public List<String> getEmailAddresses()
	{
		return emailAddresses;
	}

	public void setEmailAddresses(List<String> emailAddresses)
	{
		this.emailAddresses = emailAddresses;
	}

	public Integer getScheduleIntevalDays()
	{
		return scheduleIntevalDays;
	}

	public void setScheduleIntevalDays(Integer scheduleIntevalDays)
	{
		this.scheduleIntevalDays = scheduleIntevalDays;
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

}
