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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationTemplate;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.WorkflowStatus;
import edu.usu.sdl.openstorefront.core.model.EvaluationAll;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.core.view.EvaluationView;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.model.EvaluationStatusReportLineModel;
import edu.usu.sdl.openstorefront.report.model.EvaluationStatusReportModel;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class EvaluationStatusReport
		extends BaseReport
{

	private List<Evaluation> evaluations = new ArrayList<>();

	public EvaluationStatusReport(Report report)
	{
		super(report);
	}

	@Override
	protected EvaluationStatusReportModel gatherData()
	{
		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setActiveStatus(Evaluation.ACTIVE_STATUS);
		evaluationExample.setPublished(Boolean.FALSE);

		if (StringUtils.isNotBlank(report.getReportOption().getAssignedUser())) {
			evaluationExample.setAssignedUser(report.getReportOption().getAssignedUser());
		}

		if (StringUtils.isNotBlank(report.getReportOption().getAssignedGroup())) {
			evaluationExample.setAssignedGroup(report.getReportOption().getAssignedGroup());
		}
		evaluations = evaluationExample.findByExample();
		evaluations = filterEngine.filter(evaluations);

		EvaluationStatusReportModel reportModel = new EvaluationStatusReportModel();
		reportModel.setTitle("Evaluation Status Report");

		EvaluationTemplate evaluationTemplateExample = new EvaluationTemplate();
		List<EvaluationTemplate> evaluationTemplates = evaluationTemplateExample.findByExample();
		Map<String, List<EvaluationTemplate>> templateMap = evaluationTemplates.stream()
				.collect(Collectors.groupingBy(EvaluationTemplate::getTemplateId));

		List<EvaluationView> views = EvaluationView.toView(evaluations);
		views.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, EvaluationView.FIELD_COMPONENT_NAME));

		for (EvaluationView view : views) {
			EvaluationAll evaluationAll = service.getEvaluationService().getEvaluation(view.getEvaluationId());

			String templateName = OpenStorefrontConstant.NOT_AVAILABLE;
			if (templateMap.containsKey(evaluationAll.getEvaluation().getTemplateId())) {
				templateName = templateMap.get(evaluationAll.getEvaluation().getTemplateId()).get(0).getName();
			}

			EvaluationStatusReportLineModel lineModel = new EvaluationStatusReportLineModel();

			lineModel.setEntryName(view.getComponentName());
			lineModel.setAssignedGroup(evaluationAll.getEvaluation().getAssignedGroup());
			lineModel.setAssignedUser(evaluationAll.getEvaluation().getAssignedUser());
			lineModel.setEvaluationTemplate(templateName);
			lineModel.setEvaluationVersion(evaluationAll.getEvaluation().getVersion());
			lineModel.setPercentageComplete(evaluationAll.calcProgress());
			lineModel.setWorkflowStatus(TranslateUtil.translate(WorkflowStatus.class, evaluationAll.getEvaluation().getWorkflowStatus()));

			reportModel.getData().add(lineModel);
		}

		return reportModel;
	}

	@Override
	protected Map<String, ReportWriter> getWriterMap()
	{
		Map<String, ReportWriter> writerMap = new HashMap<>();

		String viewCSV = outputKey(ReportTransmissionType.VIEW, ReportFormat.CSV);
		writerMap.put(viewCSV, (generator, reportModel) -> {
			writeCSV(generator, (EvaluationStatusReportModel) reportModel);
		});

		String emailCSV = outputKey(ReportTransmissionType.EMAIL, ReportFormat.CSV);
		writerMap.put(emailCSV, (generator, reportModel) -> {
			writeCSV(generator, (EvaluationStatusReportModel) reportModel);
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

	private void writeCSV(BaseGenerator generator, EvaluationStatusReportModel reportModel)
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		cvsGenerator.addLine(reportModel.getTitle(), sdf.format(reportModel.getCreateTime()));
		cvsGenerator.addLine("");
		cvsGenerator.addLine(
				"Entry Name",
				"Evaluation Version",
				"Evaluation Template",
				"Assigned Group",
				"Assigned User",
				"Workflow Status",
				"Percentage Complete"
		);

		for (EvaluationStatusReportLineModel lineModel : reportModel.getData()) {

			cvsGenerator.addLine(
					lineModel.getEntryName(),
					lineModel.getEvaluationVersion(),
					lineModel.getEvaluationTemplate(),
					lineModel.getAssignedGroup(),
					lineModel.getAssignedUser(),
					lineModel.getWorkflowStatus(),
					lineModel.getPercentageComplete().toPlainString() + "%"
			);

		}

	}

}
