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
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import net.redhogs.cronparser.CronExpressionDescriptor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ScheduledReportView
		extends ScheduledReport
{

	private String reportTypeDescription;
	private String reportFormatDescription;
	private String cronDescription;

	public ScheduledReportView()
	{
	}

	public static ScheduledReportView toReportView(ScheduledReport report)
	{
		ScheduledReportView view = new ScheduledReportView();
		try {
			BeanUtils.copyProperties(view, report);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setReportTypeDescription(TranslateUtil.translate(ReportType.class, report.getReportType()));
		view.setReportFormatDescription(TranslateUtil.translate(ReportFormat.class, report.getReportFormat()));

		if (StringUtils.isNotBlank(report.getScheduleIntervalCron())) {
			try {
				view.setCronDescription(CronExpressionDescriptor.getDescription(report.getScheduleIntervalCron()));
			} catch (ParseException ex) {
				//ignore ex
				view.setCronDescription(report.getScheduleIntervalCron());
			}
		}

		return view;
	}

	public static List<ScheduledReportView> toReportView(List<ScheduledReport> reports)
	{
		List<ScheduledReportView> views = new ArrayList<>();
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

	public String getReportFormatDescription()
	{
		return reportFormatDescription;
	}

	public void setReportFormatDescription(String reportFormatDescription)
	{
		this.reportFormatDescription = reportFormatDescription;
	}

	public String getCronDescription()
	{
		return cronDescription;
	}

	public void setCronDescription(String cronDescription)
	{
		this.cronDescription = cronDescription;
	}

}
