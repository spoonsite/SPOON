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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.model.ComponentReportLineModel;
import edu.usu.sdl.openstorefront.report.model.ComponentReportModel;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author dshurtleff
 */
public class ComponentReport
		extends BaseReport
{

	public ComponentReport(Report report)
	{
		super(report);
	}

	@Override
	protected ComponentReportModel gatherData()
	{
		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
		componentExample.setApprovalState(ApprovalStatus.APPROVED);
		List<Component> components = service.getPersistenceService().queryByExample(componentExample);
		if (!report.dataIdSet().isEmpty()) {
			components = components.stream().filter(c -> report.dataIdSet().contains(c.getComponentId())).collect(Collectors.toList());
		}
		components = filterEngine.filter(components);
		components.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, Component.FIELD_NAME));

		ComponentReportModel componentReportModel = new ComponentReportModel();
		componentReportModel.setTitle("Entry Report");

		for (Component component : components) {

			ComponentTracking componentTrackingExample = new ComponentTracking();
			componentTrackingExample.setActiveStatus(ComponentTracking.ACTIVE_STATUS);
			componentTrackingExample.setComponentId(component.getComponentId());
			componentTrackingExample.setTrackEventTypeCode(TrackEventCode.VIEW);
			long views = service.getPersistenceService().countByExample(componentTrackingExample);

			componentTrackingExample.setTrackEventTypeCode(TrackEventCode.EXTERNAL_LINK_CLICK);
			long resourcesClicked = service.getPersistenceService().countByExample(componentTrackingExample);

			ComponentReview componentReviewExample = new ComponentReview();
			componentReviewExample.setComponentId(component.getComponentId());
			componentReviewExample.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			long reviews = service.getPersistenceService().countByExample(componentReviewExample);

			ComponentTag componentTagExample = new ComponentTag();
			componentTagExample.setComponentId(component.getComponentId());
			componentTagExample.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			long tags = service.getPersistenceService().countByExample(componentTagExample);

			ComponentQuestion componentQuestionExample = new ComponentQuestion();
			componentQuestionExample.setComponentId(component.getComponentId());
			componentQuestionExample.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			long questions = service.getPersistenceService().countByExample(componentQuestionExample);

			ComponentQuestionResponse componentQuestionResponseExample = new ComponentQuestionResponse();
			componentQuestionResponseExample.setComponentId(component.getComponentId());
			componentQuestionResponseExample.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			long questionResponse = service.getPersistenceService().countByExample(componentQuestionResponseExample);

			componentTrackingExample = new ComponentTracking();
			componentTrackingExample.setActiveStatus(ComponentTracking.ACTIVE_STATUS);
			componentTrackingExample.setComponentId(component.getComponentId());
			componentTrackingExample.setTrackEventTypeCode(TrackEventCode.VIEW);

			ComponentTracking componentTrackingOrderExample = new ComponentTracking();
			componentTrackingOrderExample.setEventDts(QueryByExample.DATE_FLAG);

			QueryByExample<ComponentTracking> queryByExample = new QueryByExample<>(componentTrackingExample);
			queryByExample.setMaxResults(1);
			queryByExample.setOrderBy(componentTrackingOrderExample);
			queryByExample.setSortDirection(OpenStorefrontConstant.SORT_ASCENDING);

			ComponentTracking componentTracking = service.getPersistenceService().queryOneByExample(queryByExample);
			Date lastViewed = null;
			if (componentTracking != null) {
				lastViewed = componentTracking.getEventDts();
			}

			ComponentReportLineModel lineModel = new ComponentReportLineModel();

			lineModel.setName(component.getName());
			lineModel.setOrganization(component.getOrganization());
			lineModel.setActiveStatus(component.getActiveStatus());
			lineModel.setApprovalDts(component.getApprovedDts());
			lineModel.setApprovalStatus(component.getApprovalState());
			lineModel.setApprovalUser(component.getApprovedUser());
			lineModel.setLastActivityDts(component.getLastActivityDts());
			lineModel.setCreateDts(component.getCreateDts());
			lineModel.setCreateUser(component.getCreateUser());
			lineModel.setLastViewed(lastViewed);
			lineModel.setActiveQuestionResponses(questionResponse);
			lineModel.setActiveQuestions(questions);
			lineModel.setActiveReviews(reviews);
			lineModel.setResourcesClicked(resourcesClicked);
			lineModel.setTags(tags);
			lineModel.setViews(views);

			componentReportModel.getData().add(lineModel);
		}

		return componentReportModel;
	}

	@Override
	protected Map<String, ReportWriter> getWriterMap()
	{
		Map<String, ReportWriter> writerMap = new HashMap<>();

		String viewCSV = outputKey(ReportTransmissionType.VIEW, ReportFormat.CSV);
		writerMap.put(viewCSV, (generator, reportModel) -> {
			writeCSV(generator, (ComponentReportModel) reportModel);
		});

		String emailCSV = outputKey(ReportTransmissionType.EMAIL, ReportFormat.CSV);
		writerMap.put(emailCSV, (generator, reportModel) -> {
			writeCSV(generator, (ComponentReportModel) reportModel);
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

	private void writeCSV(BaseGenerator generator, ComponentReportModel reportModel)
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;
		cvsGenerator.addLine(reportModel.getTitle(), sdf.format(reportModel.getCreateTime()));

		cvsGenerator.addLine(
				"Name",
				"Organization",
				"Last Activity Date",
				"Approval Status",
				"Approval Date",
				"Approval User",
				"Active Status",
				"Create Date",
				"Create User",
				"Last Viewed",
				"Views (For Tracking Period)",
				"Resources Clicked",
				"Active Reviews",
				"Tags",
				"Active Questions",
				"Active Question Responses"
		);

		for (ComponentReportLineModel lineModel : reportModel.getData()) {
			cvsGenerator.addLine(
					lineModel.getName(),
					lineModel.getOrganization(),
					sdf.format(lineModel.getLastActivityDts()),
					lineModel.getApprovalStatus(),
					lineModel.getApprovalDts() == null ? "" : sdf.format(lineModel.getApprovalDts()),
					lineModel.getApprovalUser(),
					lineModel.getActiveStatus(),
					sdf.format(lineModel.getCreateDts()),
					lineModel.getCreateUser(),
					lineModel.getLastViewed() == null ? "" : sdf.format(lineModel.getLastViewed()),
					lineModel.getViews(),
					lineModel.getResourcesClicked(),
					lineModel.getActiveReviews(),
					lineModel.getTags(),
					lineModel.getActiveQuestions(),
					lineModel.getActiveQuestionResponses()
			);
		}

	}

}
