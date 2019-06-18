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

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.model.BaseReportModel;
import edu.usu.sdl.openstorefront.report.model.EntryStatusDetailModel;
import edu.usu.sdl.openstorefront.report.model.EntryStatusReportModel;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author dshurtleff
 */
public class EntryStatusReport
		extends BaseReport
{

	private static final int MAX_DESCRIPTION_SIZE = 150;

	private Map<String, UserProfile> userMap = new HashMap<>();

	public EntryStatusReport(Report report)
	{
		super(report);
	}

	@Override
	protected EntryStatusReportModel gatherData()
	{
		EntryStatusReportModel reportModel = new EntryStatusReportModel();
		reportModel.setTitle("Entry Status");
		updateReportTimeRange();
		reportModel.setDataStartDate(report.getReportOption().getStartDts());
		reportModel.setDataEndDate(report.getReportOption().getEndDts());

		Component componentExample = new Component();
		componentExample.setActiveStatus(componentExample.getActiveStatus());

		QueryByExample queryByExample = new QueryByExample(componentExample);

		Component componentStartExample = new Component();
		componentStartExample.setCreateDts(report.getReportOption().getStartDts());

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(componentStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		Component componentEndExample = new Component();
		componentEndExample.setCreateDts(report.getReportOption().getEndDts());

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(componentEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		List<Component> components = service.getPersistenceService().queryByExample(queryByExample);
		components = filterEngine.filter(components);
		components.removeIf(c -> {
			return c.getPendingChangeId() != null;
		});

		for (Component component : components) {
			EntryStatusDetailModel detailModel = new EntryStatusDetailModel();
			detailModel.setName(component.getName());
			detailModel.setCreateUser(component.getCreateUser());
			detailModel.setCreateDts(component.getCreateDts());
			detailModel.setLastVendorUpdateApproveDate(component.getApprovedDts());
			detailModel.setLastSystemUpdDate(component.getLastActivityDts());
			String description = StringProcessor.ellipseString(StringProcessor.stripHtml(component.getDescription()), MAX_DESCRIPTION_SIZE);
			detailModel.setDescription(description);

			detailModel.setEntryType(TranslateUtil.translateComponentType(component.getComponentType()));
			detailModel.setStatus(TranslateUtil.translate(ApprovalStatus.class, component.getApprovalState()));

			UserProfile user = findUser(component.getCreateUser());

			if (user != null) {
				detailModel.setCreateUserEmail(user.getEmail());
				detailModel.setCreateUserOrganization(user.getOrganization());
			}

			if (component.getSubmittedDts() != null) {
				detailModel.setUserSubmitted(true);
				detailModel.setSubmissionDate(component.getSubmittedDts());
			}

			reportModel.getCreatedEntries().add(detailModel);
		}

		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setActiveStatus(Evaluation.ACTIVE_STATUS);

		queryByExample = new QueryByExample(evaluationExample);

		Evaluation evaluationStartExample = new Evaluation();
		evaluationStartExample.setCreateDts(report.getReportOption().getStartDts());

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(evaluationStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		Evaluation evaluationEndExample = new Evaluation();
		evaluationEndExample.setCreateDts(report.getReportOption().getEndDts());

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(componentEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		List<Evaluation> evaluations = service.getPersistenceService().queryByExample(queryByExample);
		evaluations = filterEngine.filter(evaluations);
		List<EntryStatusDetailModel> entryStatusDetailModels = toEvalToLineModel(reportModel, evaluations);
		reportModel.setEvaluationsPublished(entryStatusDetailModels);

		List<Evaluation> allEvals = evaluationExample.findByExample();
		allEvals = filterEngine.filter(allEvals);

		List<Evaluation> publishedEvals = allEvals.stream()
				.filter(e -> {
					boolean keep = false;
					if (e.getPublished()) {
						if (e.getUpdateDts().after(reportModel.getDataStartDate())
								&& e.getUpdateDts().before(reportModel.getDataEndDate())) {
							keep = true;
						}
					}
					return keep;
				})
				.collect(Collectors.toList());

		entryStatusDetailModels = toEvalToLineModel(reportModel, publishedEvals);
		reportModel.setEvaluationsPublished(entryStatusDetailModels);

		allEvals.removeIf(e -> {
			return e.getPublished();
		});
		entryStatusDetailModels = toEvalToLineModel(reportModel, allEvals);
		reportModel.setEvaluationsInprogress(entryStatusDetailModels);

		return reportModel;
	}

	private List<EntryStatusDetailModel> toEvalToLineModel(EntryStatusReportModel reportModel, List<Evaluation> evaluations)
	{
		List<EntryStatusDetailModel> entryStatusDetailModels = new ArrayList<>();
		for (Evaluation evaluation : evaluations) {

			Component component = new Component();
			component.setComponentId(evaluation.getComponentId());
			component = component.find();
			if (component == null) {
				component = new Component();
				component.setComponentId(evaluation.getOriginComponentId());
				component = component.find();
			}

			EntryStatusDetailModel detailModel = new EntryStatusDetailModel();
			detailModel.setName(component.getName());
			detailModel.setCreateUser(evaluation.getCreateUser());
			detailModel.setCreateDts(evaluation.getCreateDts());
			detailModel.setEntryType(TranslateUtil.translateComponentType(component.getComponentType()));

			String description = StringProcessor.ellipseString(StringProcessor.stripHtml(component.getDescription()), MAX_DESCRIPTION_SIZE);
			detailModel.setDescription(description);

			if (evaluation.getPublished()) {
				detailModel.setStatus("Published");
				detailModel.setPublished(true);
			} else {
				detailModel.setStatus("Unpublished: " + evaluation.getWorkflowStatus());
			}

			UserProfile user = findUser(evaluation.getCreateUser());

			if (user != null) {
				detailModel.setCreateUserEmail(user.getEmail());
				detailModel.setCreateUserOrganization(user.getOrganization());
			}

			entryStatusDetailModels.add(detailModel);
		}
		return entryStatusDetailModels;
	}

	private UserProfile findUser(String userName)
	{
		UserProfile user = userMap.get(userName);
		if (user == null) {
			UserProfile profileExample = new UserProfile();
			profileExample.setUsername(userName);
			user = profileExample.find();
			if (user != null) {
				userMap.put(user.getUsername(), user);
			}
		}
		return user;
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

	@Override
	protected Map<String, ReportWriter> getWriterMap()
	{
		Map<String, ReportWriter> writerMap = new HashMap<>();

		String viewFormat = outputKey(ReportTransmissionType.VIEW, ReportFormat.CSV);
		writerMap.put(viewFormat, (ReportWriter<EntryStatusReportModel>) this::writeCSV);

		viewFormat = outputKey(ReportTransmissionType.EMAIL, ReportFormat.CSV);
		writerMap.put(viewFormat, (ReportWriter<EntryStatusReportModel>) this::writeCSV);

		return writerMap;
	}

	@Override
	public String reportSummmary(BaseReportModel reportModel)
	{
		StringBuilder summary = new StringBuilder();

		EntryStatusReportModel model = (EntryStatusReportModel) reportModel;

		summary.append("<h2>" + model.getTitle() + " Summary</h2>");
		summary.append("Generated on ").append(sdf.format(reportModel.getCreateTime())).append("<br>");
		summary.append("Reporting Period ")
				.append(sdf.format(reportModel.getDataStartDate()))
				.append(" - ")
				.append(sdf.format(reportModel.getDataEndDate()))
				.append("<br><br>");

		summary.append("<b>Entries Created in Period:</b> ").append(model.entryCreated()).append("<br>");
		summary.append("<b>User submissions:</b> ").append(model.userSubmissions()).append("<br>");

		summary.append("<b>Evaluations Started in Period:</b> ").append(model.getEvaluations().size()).append("<br>");
		summary.append("<b>Evaluations Currently In Progress:</b> ").append(model.getEvaluationsInprogress().size()).append("<br>");
		summary.append("<b>Evaluations Published in Period:</b> ").append(model.getEvaluationsPublished().size()).append("<br>");

		return summary.toString();
	}

	private void writeCSV(BaseGenerator generator, EntryStatusReportModel model)
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		cvsGenerator.addLine(model.getTitle(), sdf.format(model.getCreateTime()));

		cvsGenerator.addLine("Data Time Range:  ", sdf.format(model.getDataStartDate()) + " - " + sdf.format(model.getDataEndDate()));
		cvsGenerator.addLine("Summary");
		cvsGenerator.addLine(
				"Entries Created in Period",
				"User submissions",
				"Evaluations Started in Period",
				"Evaluations Currently In Progress",
				"Evaluations Published in Period"
		);

		cvsGenerator.addLine(
				model.entryCreated(),
				model.userSubmissions(),
				model.getEvaluations().size(),
				model.getEvaluationsInprogress().size(),
				model.getEvaluationsPublished().size()
		);

		cvsGenerator.addLine("Details");
		cvsGenerator.addLine("");

		writeDetails(generator, "Entries Created", model.getCreatedEntries(), false);
		cvsGenerator.addLine("");

		writeDetails(generator, "User Submissions", model.userSubmissionsDetails(), true);
		cvsGenerator.addLine("");

		writeDetails(generator, "Evaluations Started", model.getEvaluations(), false);
		cvsGenerator.addLine("");

		writeDetails(generator, "Evaluations Currently In Progress", model.getEvaluationsInprogress(), false);
		cvsGenerator.addLine("");

		writeDetails(generator, "Evaluations Published in Period", model.getEvaluationsPublished(), false);

	}
	
	/**
	 * writeDetails function, like all 'write-'-esque functions, ... is used in .writeCSV() exensively.
	 * 
	 * @param generator
	 * Currently it doesn't matter what you pass through this parameter, this funcion defines and uses it's own CSV generator to
	 * use and does nothing with the parameter passed through to it. Having this parameter here make it extenable for if this report
	 * needs to be extended to other formats in the future,  however.  
	 * @param sectionName
	 * Conditional logic has been added  to only write a "Create User Organization","Last Vendor Update Approved" column if the 
	 * second parameter being passed (the sectionName parameter) is equal to "Entries Created". Those columns are really only needed
	 * for that particular case. 
	 * @param details
	 * @param submitted
	 */
	private void writeDetails(BaseGenerator generator, String sectionName, List<EntryStatusDetailModel> details, boolean submitted)
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		cvsGenerator.addLine(sectionName);
		
		// If this is the Entries Create section, show an extra two columns, "Last Vendor Update Approved" and
		// "Last System Update", else dont show those extra columns. The cvsGenerator object does not have support
		// for anything like a .appendLine, therefore some duplicate code is nessisary. 
		if ( sectionName == "Entries Created"){
			cvsGenerator.addLine(
				"Name",
				"Entry Type",
				"Description",
				"Status",
				"Create User",
				"Create Date",
				"Create User Email",
				"Create User Organization",
				"Last Vendor Update Approved",
				"Last System Update",
				submitted ? "Submission Date" : ""
			);
		}
		else {
			cvsGenerator.addLine(
				"Name",
				"Entry Type",
				"Description",
				"Status",
				"Create User",
				"Create Date",
				"Create User Email",
				"Create User Organization",
				submitted ? "Submission Date" : ""
			);
		}
				
		for (EntryStatusDetailModel detailModel : details) {
			
			// If "Entries Created" section, display extra columns.
			if ( sectionName == "Entries Created"){
				cvsGenerator.addLine(
						detailModel.getName(),
						detailModel.getEntryType(),
						detailModel.getDescription(),
						detailModel.getStatus(),
						detailModel.getCreateUser(),
						sdf.format(detailModel.getCreateDts()),
						detailModel.getCreateUserEmail(),
						detailModel.getCreateUserOrganization(),
						detailModel.getLastVendorUpdateApproveDate(),
						detailModel.getLastSystemUpdDate(),
						submitted ? sdf.format(detailModel.getSubmissionDate()) : ""
					);
			}
			else {
				cvsGenerator.addLine(
						detailModel.getName(),
						detailModel.getEntryType(),
						detailModel.getDescription(),
						detailModel.getStatus(),
						detailModel.getCreateUser(),
						sdf.format(detailModel.getCreateDts()),
						detailModel.getCreateUserEmail(),
						detailModel.getCreateUserOrganization(),
						submitted ? sdf.format(detailModel.getSubmissionDate()) : ""
					);
			}
		}

	}

}
