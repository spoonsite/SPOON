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

import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds a report history record")
public class Report
		extends StandardEntity<Report>
{

	@PK(generated = true)
	@NotNull
	private String reportId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = ReportType.class)
	@ConsumeField
	@FK(ReportType.class)
	private String reportType;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = RunStatus.class)
	@FK(RunStatus.class)
	private String runStatus;

	@ConsumeField
	@Embedded
	@DataType(ReportDataId.class)
	@OneToMany(orphanRemoval = true)
	private List<ReportDataId> ids;

	@ConsumeField
	@Embedded
	@OneToOne(orphanRemoval = true)
	private ReportOption reportOption;

	private Boolean scheduled;
	private String scheduledId;

	@ConsumeField
	@Embedded
	@DataType(ReportOutput.class)
	@OneToMany(orphanRemoval = true)
	private List<ReportOutput> reportOutputs;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public Report()
	{
	}

	public Path pathToReport()
	{
		Path path = null;
		if (StringUtils.isNotBlank((getReportId()))) {
			File reportDir = FileSystemManager.getInstance().getDir(FileSystemManager.REPORT_DIR);
			path = Paths.get(reportDir.getPath() + "/" + getReportId());
		}
		return path;
	}

	public Set<String> dataIdSet()
	{
		Set<String> dataSet = new HashSet<>();
		if (getIds() != null) {
			for (ReportDataId dataId : getIds()) {
				dataSet.add(dataId.getId());
			}
		}
		return dataSet;
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

	public String getReportId()
	{
		return reportId;
	}

	public void setReportId(String reportId)
	{
		this.reportId = reportId;
	}

	public String getReportType()
	{
		return reportType;
	}

	public void setReportType(String reportType)
	{
		this.reportType = reportType;
	}

	public String getRunStatus()
	{
		return runStatus;
	}

	public void setRunStatus(String runStatus)
	{
		this.runStatus = runStatus;
	}

	public ReportOption getReportOption()
	{
		return reportOption;
	}

	public void setReportOption(ReportOption reportOption)
	{
		this.reportOption = reportOption;
	}

	public Boolean getScheduled()
	{
		return scheduled;
	}

	public void setScheduled(Boolean scheduled)
	{
		this.scheduled = scheduled;
	}

	public List<ReportDataId> getIds()
	{
		return ids;
	}

	public void setIds(List<ReportDataId> ids)
	{
		this.ids = ids;
	}

	public List<ReportOutput> getReportOutputs()
	{
		return reportOutputs;
	}

	public void setReportOutputs(List<ReportOutput> reportOutputs)
	{
		this.reportOutputs = reportOutputs;
	}

	public String getScheduledId()
	{
		return scheduledId;
	}

	public void setScheduledId(String scheduledId)
	{
		this.scheduledId = scheduledId;
	}

}
