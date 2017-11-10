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
package edu.usu.sdl.openstorefront.report;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.FeedbackTicket;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.entity.UserSecurity;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionResponseView;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionView;
import edu.usu.sdl.openstorefront.core.view.ComponentReviewView;
import edu.usu.sdl.openstorefront.core.view.EvaluationView;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.HtmlGenerator;
import edu.usu.sdl.openstorefront.report.generator.HtmlToPdfGenerator;
import edu.usu.sdl.openstorefront.report.model.AdminActionReportModel;
import edu.usu.sdl.openstorefront.report.model.BaseReportModel;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import edu.usu.sdl.openstorefront.service.manager.ReportManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author cyearsley
 */
public class AdminActionReport
		extends BaseReport
{

	private static final Logger LOG = Logger.getLogger(AdminActionReport.class.getName());

	private static final int MAX_CHAR_COUNT = 300;

	public AdminActionReport(Report report)
	{
		super(report);
	}

	@Override
	protected AdminActionReportModel gatherData()
	{
		AdminActionReportModel actionReportModel = new AdminActionReportModel();

		//	Get report data
		// Get pending entries
		Component componentExample = new Component();
		componentExample.setApprovalState(ApprovalStatus.PENDING);
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);

		// serparate into user submission (submission date is filled in) and admin pending (else)
		List<Component> pendingAdminEntries = componentExample.findByExample();
		List<Component> pendingUserEntries = new ArrayList<>();
		pendingAdminEntries.removeIf((Component item) -> {
			item.setDescription(StringProcessor.truncateHTML(item.getDescription(), MAX_CHAR_COUNT));
			if (item.getSubmittedDts() != null) {
				pendingUserEntries.add(item);
				return true;
			}
			return false;
		});
		actionReportModel.setPendingAdminEntries(pendingAdminEntries);
		actionReportModel.setPendingUserEntries(pendingUserEntries);

		// Pending change requests (pending approval, and pending change id is not null.)
		componentExample = new Component();
		componentExample.setApprovalState(ApprovalStatus.PENDING_STATUS);
		List<Component> changeRequests = componentExample.findByExample();
		changeRequests.removeIf(item -> item.getPendingChangeId() == null);

		// if componentId == eval.componentid... omit record
		changeRequests.removeIf((Component component) -> {
			Evaluation evaluationExample = new Evaluation();
			evaluationExample.setComponentId(component.getComponentId());
			Evaluation evaluation = evaluationExample.find();
			return !(evaluation == null);
		});
		actionReportModel.setChangeRequests(changeRequests);

		//	Get pending reviews
		ComponentReview reviewExample = new ComponentReview();
		reviewExample.setActiveStatus(StandardEntity.PENDING_STATUS);
		List<ComponentReview> pendingReviewList = reviewExample.findByExample();
		actionReportModel.setPendingReviews(ComponentReviewView.toViewList(pendingReviewList));

		//	Get pending questions
		ComponentQuestion questionExample = new ComponentQuestion();
		questionExample.setActiveStatus(StandardEntity.PENDING_STATUS);
		List<ComponentQuestion> pendingQuestionList = questionExample.findByExample();
		actionReportModel.setPendingQuestions(ComponentQuestionView.toViewList(pendingQuestionList, new HashMap<>()));

		//	Get pending responses
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setActiveStatus(StandardEntity.PENDING_STATUS);
		List<ComponentQuestionResponse> pendingResponses = responseExample.findByExample();
		actionReportModel.setPendingResponses(ComponentQuestionResponseView.toViewList(pendingResponses));

		//	Get pending feedback
		FeedbackTicket feedbackExample = new FeedbackTicket();
		feedbackExample.setActiveStatus(StandardEntity.ACTIVE_STATUS);
		List<FeedbackTicket> pendingFeedbackTickets = feedbackExample.findByExample();
		actionReportModel.setPendingFeedbackTickets(pendingFeedbackTickets);

		//	Get pending users
		UserSecurity userSecurityExample = new UserSecurity();
		userSecurityExample.setApprovalStatus(StandardEntity.PENDING_STATUS);
		List<UserSecurity> pendingUsers = userSecurityExample.findByExample();
		actionReportModel.setPendingUsers(pendingUsers);

		//	Get evaluation ready to be published
		//	Consider getting WorkflowStatus?
		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setPublished(Boolean.FALSE);
		List<Evaluation> pendingEvaluations = evaluationExample.findByExample();
		actionReportModel.setPendingEvaluations(EvaluationView.toView(pendingEvaluations));

		return actionReportModel;
	}

	@Override
	protected Map<String, ReportWriter> getWriterMap()
	{
		Map<String, ReportWriter> writerMap = new HashMap<>();

		String viewFormat = outputKey(ReportTransmissionType.VIEW, ReportFormat.HTML);
		writerMap.put(viewFormat, (ReportWriter<AdminActionReportModel>) this::writeHtml);

		viewFormat = outputKey(ReportTransmissionType.VIEW, ReportFormat.PDF);
		writerMap.put(viewFormat, (ReportWriter<AdminActionReportModel>) this::writePdf);

		viewFormat = outputKey(ReportTransmissionType.EMAIL, ReportFormat.HTML);
		writerMap.put(viewFormat, (ReportWriter<AdminActionReportModel>) this::writeHtml);

		viewFormat = outputKey(ReportTransmissionType.EMAIL, ReportFormat.PDF);
		writerMap.put(viewFormat, (ReportWriter<AdminActionReportModel>) this::writePdf);

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
				ReportFormat format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.HTML);
				formats.add(format);
				format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.PDF);
				formats.add(format);
				break;

			case ReportTransmissionType.EMAIL:
				format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.HTML);
				formats.add(format);
				format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.PDF);
				formats.add(format);
				break;
		}

		return formats;
	}

	@Override
	public String reportSummmary(BaseReportModel reportModel)
	{
		AdminActionReportModel actionReportModel = (AdminActionReportModel) reportModel;

		StringBuilder summary = new StringBuilder();
		summary.append("<h2>Action Report Summary</h2>");

		if (actionReportModel.outstandingItems()) {
			if (!actionReportModel.getPendingUserEntries().isEmpty()) {
				summary.append("User Submissions waiting for Approval: ")
						.append(actionReportModel.getPendingAdminEntries().size())
						.append("<br>");
			}
			if (!actionReportModel.getChangeRequests().isEmpty()) {
				summary.append("User change requests waiting for Approval: ")
						.append(actionReportModel.getChangeRequests().size())
						.append("<br>");
			}
			if (!actionReportModel.getPendingAdminEntries().isEmpty()) {
				summary.append("Librarian entries waiting for Approval: ")
						.append(actionReportModel.getPendingAdminEntries().size())
						.append("<br>");
			}
			if (!actionReportModel.getPendingFeedbackTickets().isEmpty()) {
				summary.append("Outstanding evaluations(see details for status): ")
						.append(actionReportModel.getPendingEvaluations().size())
						.append("<br>");
			}
			if (!actionReportModel.getPendingReviews().isEmpty()) {
				summary.append("User reviews waiting for Approval: ")
						.append(actionReportModel.getPendingReviews().size())
						.append("<br>");
			}
			if (!actionReportModel.getPendingQuestions().isEmpty()) {
				summary.append("User questions waiting for Approval: ")
						.append(actionReportModel.getPendingQuestions().size())
						.append("<br>");
			}
			if (!actionReportModel.getPendingResponses().isEmpty()) {
				summary.append("User question responses waiting for Approval: ")
						.append(actionReportModel.getPendingResponses().size())
						.append("<br>");
			}
			if (!actionReportModel.getPendingFeedbackTickets().isEmpty()) {
				summary.append("Outstanding feedback waiting for response: ")
						.append(actionReportModel.getPendingFeedbackTickets().size())
						.append("<br>");
			}
			if (!actionReportModel.getPendingUsers().isEmpty()) {
				summary.append("Pending users waiting for approval: ")
						.append(actionReportModel.getPendingUsers().size())
						.append("<br>");
			}

			summary.append("<br>");
			summary.append("Total Action Items: ")
					.append(actionReportModel.totalActions());
		} else {
			summary.append("There are no administrative tasks that require attention.");
		}

		return summary.toString();
	}

	private void writeHtml(BaseGenerator generator, AdminActionReportModel usageReportModel)
	{
		HtmlGenerator htmlGenerator = (HtmlGenerator) generator;
		String renderedTemplate = fillinTemplate(usageReportModel);
		htmlGenerator.addLine(renderedTemplate);
	}

	private void writePdf(BaseGenerator generator, AdminActionReportModel usageReportModel)
	{
		HtmlToPdfGenerator htmlPdfGenerator = (HtmlToPdfGenerator) generator;
		String renderedTemplate = fillinTemplate(usageReportModel);
		htmlPdfGenerator.savePdfDocument(renderedTemplate);
	}

	private String fillinTemplate(AdminActionReportModel adminActionReportModel)
	{
		String renderedTemplate = null;
		try {
			Configuration templateConfig = ReportManager.getTemplateConfig();
			Template template = templateConfig.getTemplate("actionReport.ftl");
			Writer writer = new StringWriter();
			template.process(adminActionReportModel, writer);
			renderedTemplate = writer.toString();
		} catch (Exception e) {
			throw new OpenStorefrontRuntimeException(e);
		}

		return renderedTemplate;
	}

}
