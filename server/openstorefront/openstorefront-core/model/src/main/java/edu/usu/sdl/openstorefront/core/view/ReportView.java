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
import edu.usu.sdl.openstorefront.core.entity.ReportOutput;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ReportView
		extends Report
{

	private static final long serialVersionUID = 1L;

	private String reportTypeDescription;
	private String reportFormatDescription;
	private String runStatusDescription;
	private long remainingReportLifetime;
	private int reportLifetimeMax;
	private boolean noViewAvailable;
	private String reportViewFormat;

	public static ReportView toReportView(Report report)
	{
		return toReportView(report, PropertiesManager.getInstance());
	}

	public static ReportView toReportView(Report report, PropertiesManager propertiesManager)
	{
		ReportView view = new ReportView();
		try {
			BeanUtils.copyProperties(view, report);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setReportTypeDescription(TranslateUtil.translate(ReportType.class, report.getReportType()));

		try {
			view.setReportLifetimeMax(Integer.parseInt(propertiesManager.getValueDefinedDefault(PropertiesManager.KEY_REPORT_LIFETIME)));
		} catch (NumberFormatException e) {
			//	If the configured report lifetime is invalid, fallback to the default value for the max report lifetime
			view.setReportLifetimeMax(Integer.parseInt(PropertiesManager.REPORT_HISTORY_DAYS_TO_LIVE));
		}

		String format = null;
		for (ReportOutput output : report.getReportOutputs()) {
			if (ReportTransmissionType.VIEW.equals(output.getReportTransmissionType())) {
				format = output.getReportTransmissionOption().getReportFormat();
			}
		}

		if (StringUtils.isNotBlank(format)) {
			view.setReportFormatDescription(TranslateUtil.translate(ReportFormat.class, format));
			view.setReportViewFormat(format);
		} else {
			view.setNoViewAvailable(true);
		}

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

	public boolean getNoViewAvailable()
	{
		return noViewAvailable;
	}

	public void setNoViewAvailable(boolean noViewAvailable)
	{
		this.noViewAvailable = noViewAvailable;
	}

	public String getReportViewFormat()
	{
		return reportViewFormat;
	}

	public void setReportViewFormat(String reportViewFormat)
	{
		this.reportViewFormat = reportViewFormat;
	}

}
