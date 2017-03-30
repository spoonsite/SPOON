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
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationTemplate;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.WorkflowStatus;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.core.model.EvaluationAll;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.core.view.EvaluationView;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import java.util.ArrayList;
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
	protected void gatherData()
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
		evaluations = FilterEngine.filter(evaluations);
		
	}

	@Override
	protected void writeReport()
	{		
		CSVGenerator cvsGenerator = (CSVGenerator) generator;
		
		cvsGenerator.addLine("Evaluation Status Report", sdf.format(TimeUtil.currentDate()));
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
						
			cvsGenerator.addLine(
					view.getComponentName(),
					evaluationAll.getEvaluation().getVersion(),
					templateName,
					evaluationAll.getEvaluation().getAssignedGroup(),
					evaluationAll.getEvaluation().getAssignedUser(),
					TranslateUtil.translate(WorkflowStatus.class, evaluationAll.getEvaluation().getWorkflowStatus()),
					evaluationAll.calcProgress().toPlainString() + "%"
			);
		}
	
	}
	
}
