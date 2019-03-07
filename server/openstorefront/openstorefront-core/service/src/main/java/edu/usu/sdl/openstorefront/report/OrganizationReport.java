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

import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserTracking;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.model.OrganizationReportLineModel;
import edu.usu.sdl.openstorefront.report.model.OrganizationReportModel;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class OrganizationReport
		extends BaseReport
{

	private static final String NO_ORG = "No Organization";

	public OrganizationReport(Report report)
	{
		super(report);
	}

	@Override
	protected OrganizationReportModel gatherData()
	{
		UserProfile userProfileExample = new UserProfile();
		userProfileExample.setActiveStatus(UserProfile.ACTIVE_STATUS);
		List<UserProfile> userProfiles = service.getPersistenceService().queryByExample(userProfileExample);

		OrganizationReportModel organizationReportModel = new OrganizationReportModel();
		organizationReportModel.setTitle("User Organization Report");

		Map<String, List<UserProfile>> orgMap = new HashMap<>();
		for (UserProfile userProfile : userProfiles) {
			String organization = userProfile.getOrganization();
			if (StringUtils.isBlank(organization)) {
				if (orgMap.get(NO_ORG) == null) {
					orgMap.put(NO_ORG, new ArrayList<>());
				}
				orgMap.get(NO_ORG).add(userProfile);
			}

			if (StringUtils.isNotBlank(organization)) {
				if (orgMap.containsKey(organization)) {
					orgMap.get(organization).add(userProfile);
				} else {
					List<UserProfile> orgUsers = new ArrayList<>();
					orgUsers.add(userProfile);
					orgMap.put(organization, orgUsers);
				}
			}
		}

		for (String org : orgMap.keySet()) {
			OrganizationReportLineModel lineModel = new OrganizationReportLineModel();

			long reviews = getRecordCounts(ComponentReview.class, orgMap.get(org), null);
			long questions = getRecordCounts(ComponentQuestion.class, orgMap.get(org), null);
			long response = getRecordCounts(ComponentQuestionResponse.class, orgMap.get(org), null);
			long componentViews = getRecordCounts(ComponentTracking.class, orgMap.get(org), TrackEventCode.VIEW);
			long componentResourceClick = getRecordCounts(ComponentTracking.class, orgMap.get(org), TrackEventCode.EXTERNAL_LINK_CLICK);
			long logins = getRecordCounts(UserTracking.class, orgMap.get(org), TrackEventCode.LOGIN);

			lineModel.setUsers(orgMap.get(org).size());
			lineModel.setOrganization(org);
			lineModel.setReviews(reviews);
			lineModel.setQuestions(questions);
			lineModel.setComponentViews(componentViews);
			lineModel.setQuestionResponse(response);
			lineModel.setComponentResourcesClick(componentResourceClick);
			lineModel.setLogins(logins);

			organizationReportModel.getData().add(lineModel);
		}

		return organizationReportModel;
	}

	private long getRecordCounts(Class recordClass, List<UserProfile> userProfiles, String trackCodeType)
	{
		long count = repoFactory.getStandardEntityRepo().getRecordCountsByUsers(recordClass, userProfiles, trackCodeType);
		return count;
	}

	@Override
	protected Map<String, ReportWriter> getWriterMap()
	{
		Map<String, ReportWriter> writerMap = new HashMap<>();

		String viewCSV = outputKey(ReportTransmissionType.VIEW, ReportFormat.CSV);
		writerMap.put(viewCSV, (generator, reportModel) -> {
			writeCSV(generator, (OrganizationReportModel) reportModel);
		});

		String emailCSV = outputKey(ReportTransmissionType.EMAIL, ReportFormat.CSV);
		writerMap.put(emailCSV, (generator, reportModel) -> {
			writeCSV(generator, (OrganizationReportModel) reportModel);
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
	public List<ReportFormat> getSupportedFormats(String reportTransmissionType)
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

	private void writeCSV(BaseGenerator generator, OrganizationReportModel reportModel)
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		cvsGenerator.addLine(reportModel.getTitle(), sdf.format(reportModel.getCreateTime()));
		cvsGenerator.addLine(
				"Organization",
				"# Users",
				"# Reviews",
				"# Questions",
				"# Question Responses",
				"# Component Views",
				"# Component Resources Clicked",
				"# Logins"
		);

		for (OrganizationReportLineModel lineModel : reportModel.getData()) {
			cvsGenerator.addLine(
					lineModel.getOrganization(),
					lineModel.getUsers(),
					lineModel.getReviews(),
					lineModel.getQuestions(),
					lineModel.getQuestionResponse(),
					lineModel.getComponentViews(),
					lineModel.getComponentResourcesClick(),
					lineModel.getLogins()
			);
		}

	}

}
