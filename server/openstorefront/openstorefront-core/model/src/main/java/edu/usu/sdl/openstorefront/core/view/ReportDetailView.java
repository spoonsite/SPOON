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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportOption;
import edu.usu.sdl.openstorefront.core.entity.ReportOutput;
import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dshurtleff
 */
public class ReportDetailView
{

	private String reportId;
	private String reportType;
	private String createUser;
	private Date createDts;
	private ReportOption options;
	private List<LookupModel> idsInReport = new ArrayList<>();
	private List<ReportOutput> outputs = new ArrayList<>();

	public ReportDetailView()
	{
	}

	public static ReportDetailView toView(Report report)
	{
		ReportDetailView view = new ReportDetailView();
		view.setReportId(report.getReportId());
		view.setReportType(report.getReportType());
		view.setCreateUser(report.getCreateUser());
		view.setCreateDts(report.getCreateDts());
		view.setOptions(report.getReportOption());
		view.setOutputs(report.getReportOutputs());
		view.setIdsInReport(idsToComponents(report.dataIdSet()));
		return view;
	}

	private static List<LookupModel> idsToComponents(Set<String> ids)
	{
		Service service = ServiceProxyFactory.getServiceProxy();
		List<LookupModel> views = new ArrayList<>();
		for (String id : ids) {
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(id);
			lookupModel.setDescription(service.getComponentService().getComponentName(id));
			views.add(lookupModel);
		}
		return views;
	}

	public static ReportDetailView toView(ScheduledReport report)
	{
		ReportDetailView view = new ReportDetailView();
		view.setReportId(report.getScheduleReportId());
		view.setReportType(report.getReportType());
		view.setCreateUser(report.getCreateUser());
		view.setCreateDts(report.getCreateDts());
		view.setOptions(report.getReportOption());
		view.setOutputs(report.getReportOutputs());
		return view;
	}

	public static List<ReportDetailView> toViewReports(List<Report> reports)
	{
		List<ReportDetailView> views = new ArrayList<>();
		reports.forEach(report -> {
			views.add(toView(report));
		});
		return views;
	}

	public static List<ReportDetailView> toViewScheduled(List<ScheduledReport> reports)
	{
		List<ReportDetailView> views = new ArrayList<>();
		reports.forEach(report -> {
			views.add(toView(report));
		});
		return views;
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

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public Date getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	public ReportOption getOptions()
	{
		return options;
	}

	public void setOptions(ReportOption options)
	{
		this.options = options;
	}

	public List<LookupModel> getIdsInReport()
	{
		return idsInReport;
	}

	public void setIdsInReport(List<LookupModel> idsInReport)
	{
		this.idsInReport = idsInReport;
	}

	public List<ReportOutput> getOutputs()
	{
		return outputs;
	}

	public void setOutputs(List<ReportOutput> outputs)
	{
		this.outputs = outputs;
	}

}
