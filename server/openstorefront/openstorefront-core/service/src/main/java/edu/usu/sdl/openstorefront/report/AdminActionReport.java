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

import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.FeedbackTicket;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.entity.UserSecurity;
import edu.usu.sdl.openstorefront.report.generator.HtmlGenerator;
import edu.usu.sdl.openstorefront.report.generator.HtmlToPdfGenerator;
import edu.usu.sdl.openstorefront.service.manager.ReportManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cyearsley
 */
public class AdminActionReport
		extends BaseReport
{
	private static final Logger LOG = Logger.getLogger(ExternalLinkValidationReport.class.getName());
	private static final int MAX_CHAR_COUNT = 800;
	private HashMap root = new HashMap();
	private String renderedTemplate;
	
	public AdminActionReport (Report report)
	{
		super(report);
	}
	
	@Override
	protected void writeReport()
	{
		if (ReportFormat.HTML.equals(report.getReportFormat())) {
			generateHTML();
		}
		else if (ReportFormat.PDF.equals(report.getReportFormat())) {
			generatePDF();
		}
	}
	
	@Override
	protected void gatherData()
	{
		//	Get report data
		// Get pending entries
		Component componentExample = new Component();
		componentExample.setApprovalState(ApprovalStatus.PENDING);
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
		
		// serparate into user submission (submission date is filled in) and admin pending (else)
		List<Component> pendingAdminEntries = componentExample.findByExample();
		List<Component> pendingUserEntries = new ArrayList<>();
		pendingAdminEntries.removeIf((Component item) -> {
			item.setDescription(truncateHTML(item.getDescription()));
			if (item.getSubmittedDts() != null) {
				pendingUserEntries.add(item);
				return true;
			}
			return false;
		});
		
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
		
		//	Get pending reviews
		ComponentReview reviewExample = new ComponentReview();
		reviewExample.setActiveStatus(StandardEntity.PENDING_STATUS);
		List<ComponentReview> pendingReviewList = reviewExample.findByExample();
		List<HashMap> pendingReviews = new ArrayList<>();
		
		for (ComponentReview review : pendingReviewList) {
			Component reviewComponent = new Component();
			reviewComponent.setComponentId(review.getComponentId());
			reviewComponent = reviewComponent.find();
			
			HashMap reviewHash = new HashMap();
			reviewHash.put("review", review);
			reviewHash.put("componentName", reviewComponent.getName());
			
			pendingReviews.add(reviewHash);
		}
		
		//	Get pending questions
		ComponentQuestion questionExample = new ComponentQuestion();
		questionExample.setActiveStatus(StandardEntity.PENDING_STATUS);
		List<ComponentQuestion> pendingQuestionList = questionExample.findByExample();
		List<HashMap> pendingQuestions = new ArrayList<>();
		for (ComponentQuestion question : pendingQuestionList) {
			Component questionComponent = new Component();
			questionComponent.setComponentId(question.getComponentId());
			questionComponent = questionComponent.find();
			
			HashMap reviewHash = new HashMap();
			reviewHash.put("question", question);
			reviewHash.put("componentName", questionComponent.getName());
			
			pendingQuestions.add(reviewHash);
		}
		
		//	Get pending responses
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setActiveStatus(StandardEntity.PENDING_STATUS);
		List<ComponentQuestionResponse> pendingResponses = responseExample.findByExample();
		
		HashMap<String, List<ComponentQuestionResponse>> questionsWithPendingResponses = new HashMap<>();
		HashMap<String, String> entriesWithPendingResponses = new HashMap();
		for (ComponentQuestionResponse response : pendingResponses) {
			ComponentQuestion question = new ComponentQuestion();
			question.setQuestionId(response.getQuestionId());
			question = question.find();
			
			Component responseComponent = new Component();
			responseComponent.setComponentId(response.getComponentId());
			responseComponent = responseComponent.find();
			
			if (!questionsWithPendingResponses.containsKey(question.getQuestion())) {
				questionsWithPendingResponses.put(question.getQuestion(), new ArrayList<>());
			}
			questionsWithPendingResponses.get(question.getQuestion()).add(response);
			entriesWithPendingResponses.put(question.getQuestion(), responseComponent.getName());
		}
		
		//	Get pending feedback
		FeedbackTicket feedbackExample = new FeedbackTicket();
		feedbackExample.setActiveStatus(StandardEntity.ACTIVE_STATUS);
		List<FeedbackTicket> pendingFeedbackTickets = feedbackExample.findByExample();
		
		//	Get pending users
		UserSecurity userSecurityExample = new UserSecurity();
		userSecurityExample.setApprovalStatus(StandardEntity.PENDING_STATUS);
		List<UserSecurity> pendingUsers = userSecurityExample.findByExample();
		
		//	Get evaluation ready to be published
			//	Consider getting WorkflowStatus?
		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setPublished(Boolean.FALSE);
		List<Evaluation> pendingEvaluations = evaluationExample.findByExample();
		
		HashMap<String, List<Evaluation>> entriesWithPendingEvaluations = new HashMap();
		for (Evaluation evaluation : pendingEvaluations) {
			Component evalComponent = new Component();
			evalComponent.setComponentId(evaluation.getComponentId());
			evalComponent = evalComponent.find();
			
			if (evalComponent == null) {
				evalComponent = new Component();
				evalComponent.setComponentId(evaluation.getOriginComponentId());
				evalComponent = evalComponent.find();
			
			}
			if (!entriesWithPendingEvaluations.containsKey(evalComponent.getName())) {
				entriesWithPendingEvaluations.put(evalComponent.getName(), new ArrayList<>());
			}
			entriesWithPendingEvaluations.get(evalComponent.getName()).add(evaluation);
		}
		
		//	Generate HTML
		root.put("pendingAdminEntries", pendingAdminEntries);
		root.put("pendingUserEntries", pendingUserEntries);
		root.put("changeRequests", changeRequests);
		root.put("pendingReviews", pendingReviews);
		root.put("pendingQuestions", pendingQuestions);
		root.put("questionsWithPendingResponses", questionsWithPendingResponses);
		root.put("entriesWithPendingResponses", entriesWithPendingResponses);
		root.put("pendingFeedbackTickets", pendingFeedbackTickets);
		root.put("pendingUsers", pendingUsers);
		root.put("entriesWithPendingEvaluations", entriesWithPendingEvaluations);
		root.put("reportDate", sdf.format(TimeUtil.currentDate()));
		
		// if the report is HTML or PDF
		if (ReportFormat.HTML.equals(report.getReportFormat()) || ReportFormat.PDF.equals(report.getReportFormat())) {
			try
			{
				Configuration templateConfig = ReportManager.getTemplateConfig();
				Template template = templateConfig.getTemplate("actionReport.ftl");
				Writer writer = new StringWriter();
				template.process(root, writer);
				renderedTemplate = writer.toString();
			}
			catch (Exception e)
			{
				LOG.log(Level.WARNING, MessageFormat.format("There was a problem when generating an action report: {0}", e));
			}
		}
		
		// ELSE TODO: render the template for CSV
	}
	
	// TODO: If needed, add 'generateCSV' method.
	
	private void generatePDF()
	{
		HtmlToPdfGenerator htmlPdfGenerator = (HtmlToPdfGenerator) generator;
		htmlPdfGenerator.savePdfDocument(renderedTemplate);
	}
	
	private void generateHTML()
	{
		HtmlGenerator htmlGenerator = (HtmlGenerator) generator;
		htmlGenerator.addLine(renderedTemplate);
	}
	
	//	Truncates a string of HTML relative to MAX_CHAR_COUNT
	//		This roughly respects HTML, as it tries not to remove HTML tags.
	private String truncateHTML(String html) {
		List<String> htmlList = new ArrayList<>(Arrays.asList(html.split("")));
		boolean canDelete = true;
		boolean hasRemoved = false;
		for (int ii = htmlList.size() - 1; ii > -1; ii--) {
			
			//	Detect if the cursor is within an HTML tag
			if(htmlList.get(ii).equals(">")) {
				canDelete = false;
				continue;
			}
			else if (htmlList.get(ii).equals("<")) {
				canDelete = true;
				continue;
			}
			
			//	Bail if the cursor is inside the accepted char count
			if (ii < MAX_CHAR_COUNT) {
				if (hasRemoved && canDelete) {
					if (!"<".equals(htmlList.get(ii+1))) {
						htmlList.set(ii+1, " ... ");
					}
					else {
						htmlList.set(ii, " ... ");
					}
				}
				break;
			}
			
			//	"remove" the current item given that canDelete == true
			if (canDelete && ii > MAX_CHAR_COUNT) {
				htmlList.set(ii, "");
				hasRemoved = true;
			}
		}
		
		//	Return the truncated result
		html = String.join("", htmlList);
		return html;
	}
}
