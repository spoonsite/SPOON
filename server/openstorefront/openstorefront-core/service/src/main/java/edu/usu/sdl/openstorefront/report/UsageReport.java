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
package edu.usu.sdl.openstorefront.report;

import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserTracking;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import edu.usu.sdl.openstorefront.core.entity.UserWatch;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.model.UsageReportLineModel;
import edu.usu.sdl.openstorefront.report.model.UsageReportModel;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
public class UsageReport
		extends BaseReport
{

	public UsageReport(Report report)
	{
		super(report);
	}

	@Override
	protected UsageReportModel gatherData()
	{
		updateReportTimeRange();

		UsageReportModel usageReportModel = new UsageReportModel();
		usageReportModel.setTitle("Usage Report");
		usageReportModel.setDataStartDate(report.getReportOption().getStartDts());
		usageReportModel.setDataEndDate(report.getReportOption().getEndDts());

		UserTracking userTrackingExample = new UserTracking();
		userTrackingExample.setActiveStatus(UserTracking.ACTIVE_STATUS);
		userTrackingExample.setTrackEventTypeCode(TrackEventCode.LOGIN);

		UserTracking userTrackingStartExample = new UserTracking();
		userTrackingStartExample.setEventDts(report.getReportOption().getStartDts());

		UserTracking userTrackingEndExample = new UserTracking();
		userTrackingEndExample.setEventDts(report.getReportOption().getEndDts());

		QueryByExample queryByExample = new QueryByExample(userTrackingExample);
		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(userTrackingStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(userTrackingEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		List<UserTracking> userTrackings = service.getPersistenceService().queryByExample(queryByExample);

		UserWatch userWatchExample = new UserWatch();
		userWatchExample.setActiveStatus(UserWatch.ACTIVE_STATUS);
		long activeWatches = service.getPersistenceService().countByExample(userWatchExample);

		ComponentTracking componentTrackingExample = new ComponentTracking();
		componentTrackingExample.setActiveStatus(ComponentTracking.ACTIVE_STATUS);
		componentTrackingExample.setTrackEventTypeCode(TrackEventCode.VIEW);

		ComponentTracking componentTrackingStartExample = new ComponentTracking();
		componentTrackingStartExample.setEventDts(report.getReportOption().getStartDts());

		ComponentTracking componentTrackingEndExample = new ComponentTracking();
		componentTrackingEndExample.setEventDts(report.getReportOption().getEndDts());

		queryByExample = new QueryByExample(componentTrackingExample);
		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(componentTrackingStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(componentTrackingEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		long componentViews = service.getPersistenceService().countByExample(queryByExample);

		componentTrackingExample.setTrackEventTypeCode(TrackEventCode.EXTERNAL_LINK_CLICK);
		long componentLinkClicks = service.getPersistenceService().countByExample(queryByExample);

		usageReportModel.setNumberOfLogins(userTrackings.size());
		usageReportModel.setCurrentActiveWatches(activeWatches);
		usageReportModel.setComponentViews(componentViews);
		usageReportModel.setComponentResourcesClicks(componentLinkClicks);

		UserProfile userProfileExample = new UserProfile();
		List<UserProfile> allUsers = service.getPersistenceService().queryByExample(userProfileExample);
		Map<String, UserProfile> userMap = new HashMap<>();
		allUsers.forEach(user -> {
			userMap.put(user.getUsername(), user);
		});

		Map<String, Long> trackMap = new HashMap<>();
		userTrackings.forEach(userTracting -> {
			if (trackMap.containsKey(userTracting.getCreateUser())) {
				trackMap.put(userTracting.getCreateUser(), trackMap.get(userTracting.getCreateUser()) + 1);
			} else {
				trackMap.put(userTracting.getCreateUser(), 1L);
			}
		});

		for (String username : trackMap.keySet()) {
			UsageReportLineModel lineModel = new UsageReportLineModel();

			UserProfile userProfile = userMap.get(username);
			lineModel.setUsername(username);

			componentTrackingExample = new ComponentTracking();
			componentTrackingExample.setActiveStatus(ComponentTracking.ACTIVE_STATUS);
			componentTrackingExample.setTrackEventTypeCode(TrackEventCode.VIEW);
			componentTrackingExample.setCreateUser(username);

			componentTrackingStartExample = new ComponentTracking();
			componentTrackingStartExample.setEventDts(report.getReportOption().getStartDts());
			componentTrackingEndExample = new ComponentTracking();
			componentTrackingEndExample.setEventDts(report.getReportOption().getEndDts());

			queryByExample = new QueryByExample(componentTrackingExample);
			specialOperatorModel = new SpecialOperatorModel();
			specialOperatorModel.setExample(componentTrackingStartExample);
			specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
			queryByExample.getExtraWhereCauses().add(specialOperatorModel);

			specialOperatorModel = new SpecialOperatorModel();
			specialOperatorModel.setExample(componentTrackingEndExample);
			specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
			specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
			queryByExample.getExtraWhereCauses().add(specialOperatorModel);

			long userComponentViews = service.getPersistenceService().countByExample(queryByExample);

			componentTrackingExample.setTrackEventTypeCode(TrackEventCode.EXTERNAL_LINK_CLICK);
			long userComponentLinkClicks = service.getPersistenceService().countByExample(queryByExample);

			lineModel.setUsername(username);
			lineModel.setUserType(TranslateUtil.translate(UserTypeCode.class, userProfile.getUserTypeCode()));
			lineModel.setUserOrganization(userProfile.getOrganization());
			lineModel.setUserGUID(userProfile.getExternalGuid() != null ? userProfile.getExternalGuid() : userProfile.getInternalGuid());
			lineModel.setUserEmail(userProfile.getEmail());
			lineModel.setComponentViews(userComponentViews);
			lineModel.setResourcesClicked(userComponentLinkClicks);
			lineModel.setNumberOfLogins(trackMap.get(username));

			usageReportModel.getData().add(lineModel);
		}

		return usageReportModel;
	}

	@Override
	protected Map<String, ReportWriter> getWriterMap()
	{
		Map<String, ReportWriter> writerMap = new HashMap<>();

		String viewCSV = outputKey(ReportTransmissionType.VIEW, ReportFormat.CSV);
		writerMap.put(viewCSV, (generator, reportModel) -> {
			writeCSV(generator, (UsageReportModel) reportModel);
		});

		String emailCSV = outputKey(ReportTransmissionType.EMAIL, ReportFormat.CSV);
		writerMap.put(emailCSV, (generator, reportModel) -> {
			writeCSV(generator, (UsageReportModel) reportModel);
		});

		return writerMap;
	}

	@Override
	public List<ReportTransmissionType> getSupportedOutputs()
	{
		List<ReportTransmissionType> transmissionTypes = new ArrayList<>();

		ReportTransmissionType view = service.getLookupService().getLookupEnity(ReportTransmissionType.class, ReportTransmissionType.VIEW);
		ReportTransmissionType email = service.getLookupService().getLookupEnity(ReportTransmissionType.class, ReportTransmissionType.EMAIL);
		transmissionTypes.add(view);
		transmissionTypes.add(email);

		return transmissionTypes;
	}

	@Override
	public List<ReportFormat> getSupportedFormat(String reportTransmissionType)
	{
		List<ReportFormat> formats = new ArrayList<>();

		switch (reportTransmissionType) {
			case ReportTransmissionType.VIEW:
				ReportFormat format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.CSV);
				formats.add(format);
				break;

			case ReportTransmissionType.EMAIL:
				format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.CSV);
				formats.add(format);
				break;
		}

		return formats;
	}

	private void writeCSV(BaseGenerator generator, UsageReportModel reportModel)
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		//write header
		cvsGenerator.addLine(reportModel.getTitle(), sdf.format(reportModel.getCreateTime()));
		cvsGenerator.addLine("Data Time Range:  ", sdf.format(reportModel.getDataStartDate()) + " - " + sdf.format(reportModel.getDataEndDate()));
		cvsGenerator.addLine("Summary");
		cvsGenerator.addLine(
				"# Logins",
				"Current Active Watches",
				"Component Views",
				"Component Resources Clicked"
		);

		cvsGenerator.addLine(
				reportModel.getNumberOfLogins(),
				reportModel.getCurrentActiveWatches(),
				reportModel.getComponentViews(),
				reportModel.getComponentResourcesClicks()
		);

		cvsGenerator.addLine("Details");
		cvsGenerator.addLine(
				"Username",
				"User GUID",
				"User Organization",
				"User Type",
				"User Email",
				"# Logins",
				"Component Viewed",
				"Component Resources Clicked"
		);

		for (UsageReportLineModel reportLineModel : reportModel.getData()) {

			cvsGenerator.addLine(
					reportLineModel.getUsername(),
					reportLineModel.getUserGUID(),
					reportLineModel.getUserOrganization(),
					reportLineModel.getUserType(),
					reportLineModel.getUserEmail(),
					reportLineModel.getNumberOfLogins(),
					reportLineModel.getComponentViews(),
					reportLineModel.getResourcesClicked()
			);
		}

	}

}
