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
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
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
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.HtmlGenerator;
import edu.usu.sdl.openstorefront.report.generator.HtmlToPdfGenerator;
import edu.usu.sdl.openstorefront.report.model.EntryListingReportLineModel;
import edu.usu.sdl.openstorefront.report.model.EntryListingReportModel;
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
		entryListingReportModel.setTitle(PropertiesManager.getValueDefinedDefault(PropertiesManager.KEY_APPLICATION_TITLE, "Storefront") + " Listing Index");
		String viewLinkBase = PropertiesManager.getValueDefinedDefault(PropertiesManager.KEY_EXTERNAL_HOST_URL) + "/View.action?id=";

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
			lineModel.setViewLink(viewLinkBase + component.getComponentId());
			lineModel.setEntryType(TranslateUtil.translateComponentType(component.getComponentType()));
			lineModel.setLastUpdatedDts(component.getLastActivityDts());
			lineModel.setName(component.getName());
			String description = StringProcessor.ellipseString(StringProcessor.stripHtml(component.getDescription()), MAX_DESCRIPTION_SIZE);
			description = StringProcessor.stripeExtendedChars(description);

			lineModel.setShortDescription(description);
			lineModel.setEvaluationStatus("");

			List<Evaluation> evals = evalMap.get(component.getComponentId());
			if (evals != null) {
				String status = "";
				if (evals.size() > 1) {
					int completeCount = 0;
					for (Evaluation evaluation : evals) {
						if (evaluation.getPublished()) {
							completeCount++;
						}
					}
					if (completeCount == evals.size()) {
						status = "Multiple Complete";
					} else if (completeCount > 0) {
						status = "Complete evaluations and some In Progress";
					} else {
						status = "Multiple evaluations In Progress";
					}

				} else {
					Evaluation evaluation = evals.get(0);
					if (evaluation.getPublished()) {
						status = "Complete";
					} else {
						status = "In Progress";
					}
				}
				lineModel.setEvaluationStatus(status);
			}

			entryListingReportModel.getData().add(lineModel);
		}

		return entryListingReportModel;
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

				format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.PDF);
				formats.add(format);
				break;

			case ReportTransmissionType.EMAIL:
				format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.HTML);
				formats.add(format);

				format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.PDF);
				formats.add(format);
				break;

			case ReportTransmissionType.CONFLUENCE:
				format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.HTML);
				formats.add(format);
				break;
		}

		return formats;
	}

	@Override
	protected Map<String, ReportWriter> getWriterMap()
	{
		Map<String, ReportWriter> writerMap = new HashMap<>();

		String viewFormat = outputKey(ReportTransmissionType.VIEW, ReportFormat.HTML);
		writerMap.put(viewFormat, (ReportWriter<EntryListingReportModel>) this::writeHtml);

		viewFormat = outputKey(ReportTransmissionType.VIEW, ReportFormat.PDF);
		writerMap.put(viewFormat, (ReportWriter<EntryListingReportModel>) this::writePdf);

		viewFormat = outputKey(ReportTransmissionType.EMAIL, ReportFormat.HTML);
		writerMap.put(viewFormat, (ReportWriter<EntryListingReportModel>) this::writeHtml);

		viewFormat = outputKey(ReportTransmissionType.EMAIL, ReportFormat.PDF);
		writerMap.put(viewFormat, (ReportWriter<EntryListingReportModel>) this::writePdf);

		viewFormat = outputKey(ReportTransmissionType.CONFLUENCE, ReportFormat.HTML);
		writerMap.put(viewFormat, (ReportWriter<EntryListingReportModel>) this::writeHtml);

		return writerMap;
	}

	private void writeHtml(BaseGenerator generator, EntryListingReportModel reportModel)
	{
		HtmlGenerator htmlGenerator = (HtmlGenerator) generator;
		String renderedTemplate = createHtml(reportModel);
		htmlGenerator.addLine(renderedTemplate);
	}

	private void writePdf(BaseGenerator generator, EntryListingReportModel reportModel)
	{
		HtmlToPdfGenerator pdfGenerator = (HtmlToPdfGenerator) generator;
		String renderedTemplate = createHtml(reportModel);
		pdfGenerator.savePdfDocument(renderedTemplate);
	}

	private String createHtml(EntryListingReportModel reportModel)
	{
		String renderedTemplate = null;
		try {
			Configuration templateConfig = ReportManager.getTemplateConfig();
			Template template = templateConfig.getTemplate("entryListing.ftl");
			Writer writer = new StringWriter();
			template.process(reportModel, writer);
			renderedTemplate = writer.toString();
		} catch (Exception e) {
			throw new OpenStorefrontRuntimeException(e);
		}

		return renderedTemplate;
	}

}
