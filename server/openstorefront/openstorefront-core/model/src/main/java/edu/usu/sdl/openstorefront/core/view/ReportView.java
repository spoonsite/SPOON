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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class ReportView
		extends Report
{

	private String reportTypeDescription;
	private String reportFormatDescription;
	private String runStatusDescription;
	private long remainingReportLifetime;
	private int reportLifetimeMax;

	public ReportView()
	{
		try {
			this.reportLifetimeMax = Integer.parseInt(PropertiesManager.getValue(PropertiesManager.KEY_REPORT_LIFETIME));
		}
		catch (NumberFormatException e) {
			//	If the configured report lifetime is invalid, fallback to the default value for the max report lifetime
			this.reportLifetimeMax = Integer.parseInt(PropertiesManager.getValueDefinedDefault(PropertiesManager.KEY_REPORT_LIFETIME));
		}
	}

	public static ReportView toReportView(Report report)
	{
		ReportView view = new ReportView();
		try {
			BeanUtils.copyProperties(view, report);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setReportTypeDescription(TranslateUtil.translate(ReportType.class, report.getReportType()));
		view.setReportFormatDescription(TranslateUtil.translate(ReportFormat.class, report.getReportFormat()));
		view.setRunStatusDescription(TranslateUtil.translate(RunStatus.class, report.getRunStatus()));
		return view;
	}

	public static List<ReportView> toReportView(List<Report> reports)
	{
		List<ReportView> views = new ArrayList<>();
		reports.forEach(report -> {
			views.add(toReportView(report));
		});
		return views;
	}

	public String getReportTypeDescription()
	{
		return reportTypeDescription;
	}

	public void setReportTypeDescription(String reportTypeDescription)
	{
		this.reportTypeDescription = reportTypeDescription;
	}

	public String getRunStatusDescription()
	{
		return runStatusDescription;
	}

	public void setRunStatusDescription(String runStatusDescription)
	{
		this.runStatusDescription = runStatusDescription;
	}

	public String getReportFormatDescription()
	{
		return reportFormatDescription;
	}

	public void setReportFormatDescription(String reportFormatDescription)
	{
		this.reportFormatDescription = reportFormatDescription;
	}
	
	public long getRemainingReportLifetime()
	{
		return remainingReportLifetime;
	}

	public void setRemainingReportLifetime(long remainingReportLifetime)
	{
		this.remainingReportLifetime = remainingReportLifetime;
	}
	
	public int getReportLifetimeMax()
	{
		return reportLifetimeMax;
	}

	public void setReportLifetimeMax(int reportLifetimeMax)
	{
		this.reportLifetimeMax = reportLifetimeMax;
	}

}
