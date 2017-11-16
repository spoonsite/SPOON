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
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.report.model.EntryListingReportLineModel;
import edu.usu.sdl.openstorefront.report.model.EntryListingReportModel;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author dshurtleff
 */
public class EntryListingReport
		extends BaseReport
{

	private static final int MAX_DESCRIPTION_SIZE = 300;

	public EntryListingReport(Report report)
	{
		super(report);
	}

	@Override
	protected EntryListingReportModel gatherData()
	{
		EntryListingReportModel entryListingReportModel = new EntryListingReportModel();

		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
		componentExample.setApprovalState(ApprovalStatus.APPROVED);
		List<Component> components = componentExample.findByExample();
		components = filterEngine.filter(components);

		if (!report.dataIdSet().isEmpty()) {
			components = components.stream().filter(c -> report.dataIdSet().contains(c.getComponentId())).collect(Collectors.toList());
		}
		components.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, Component.FIELD_NAME));

		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setActiveStatus(Evaluation.ACTIVE_STATUS);
		Map<String, List<Evaluation>> evalMap = evaluationExample.findByExample()
				.stream()
				.collect(Collectors.groupingBy(Evaluation::getOriginComponentId));

		for (Component component : components) {
			EntryListingReportLineModel lineModel = new EntryListingReportLineModel();

			lineModel.setComponentId(component.getComponentId());
			lineModel.setEntryType(TranslateUtil.translateComponentType(component.getComponentType()));
			lineModel.setLastUpdatedDts(component.getLastActivityDts());
			lineModel.setName(component.getName());
			String description = StringProcessor.ellipseString(StringProcessor.stripHtml(component.getDescription()), MAX_DESCRIPTION_SIZE);
			lineModel.setShortDescription(description);

			List<Evaluation> evals = evalMap.get(component.getComponentId());
			if (evals != null) {

				//lineModel.setEvaluationStatus();
			}

			entryListingReportModel.getData().add(lineModel);
		}

		return entryListingReportModel;
	}

	@Override
	protected Map<String, ReportWriter> getWriterMap()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<ReportTransmissionType> getSupportedOutputs()
	{
		List<ReportTransmissionType> transmissionTypes = new ArrayList<>();

		ReportTransmissionType view = service.getLookupService().getLookupEnity(ReportTransmissionType.class, ReportTransmissionType.VIEW);
		ReportTransmissionType email = service.getLookupService().getLookupEnity(ReportTransmissionType.class, ReportTransmissionType.EMAIL);
		ReportTransmissionType confluence = service.getLookupService().getLookupEnity(ReportTransmissionType.class, ReportTransmissionType.CONFLUENCE);
		transmissionTypes.add(view);
		transmissionTypes.add(email);
		transmissionTypes.add(confluence);

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
				break;

			case ReportTransmissionType.EMAIL:
				format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.HTML);
				formats.add(format);
				break;

			case ReportTransmissionType.CONFLUENCE:
				format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.HTML);
				formats.add(format);
				break;
		}

		return formats;
	}

}
