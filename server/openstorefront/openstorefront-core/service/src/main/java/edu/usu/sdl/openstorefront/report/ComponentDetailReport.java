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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.view.ComponentAttributeView;
import edu.usu.sdl.openstorefront.core.view.ComponentContactView;
import edu.usu.sdl.openstorefront.core.view.ComponentDetailView;
import edu.usu.sdl.openstorefront.core.view.ComponentResourceView;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.generator.EmbeddedImagePreProcessor;
import edu.usu.sdl.openstorefront.report.generator.HtmlGenerator;
import edu.usu.sdl.openstorefront.report.generator.HtmlToPdfGenerator;
import edu.usu.sdl.openstorefront.report.generator.LocalMediaPDFHandler;
import edu.usu.sdl.openstorefront.report.generator.PDFRenderHandler;
import edu.usu.sdl.openstorefront.report.model.ComponentDetailReportLineModel;
import edu.usu.sdl.openstorefront.report.model.ComponentDetailReportModel;
import edu.usu.sdl.openstorefront.report.model.EntryDetailsOptions;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import edu.usu.sdl.openstorefront.service.manager.ReportManager;
import freemarker.template.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ComponentDetailReport
		extends BaseReport<ComponentDetailReportModel>
{

	//private static final Logger LOG = Logger.getLogger(ComponentDetailReport.class.getName());
	public ComponentDetailReport(Report report)
	{
		super(report);
	}

	@Override
	protected ComponentDetailReportModel gatherData()
	{
		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
		componentExample.setApprovalState(ApprovalStatus.APPROVED);
		List<Component> components = componentExample.findByExample();
		components = filterEngine.filter(components);

		if (!report.dataIdSet().isEmpty()) {
			components = components.stream().filter(c -> report.dataIdSet().contains(c.getComponentId())).collect(Collectors.toList());
		}
		components.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, Component.FIELD_NAME));

		ComponentDetailReportModel reportModel = new ComponentDetailReportModel();
		reportModel.setTitle("Entry Details Report");

		//This can be very memory intensive as it pulls alot of data and holds on to it until the report is written.
		for (Component component : components) {
			ComponentDetailReportLineModel lineModel = new ComponentDetailReportLineModel();
			ComponentDetailView componentDetail = service.getComponentService().getComponentDetails(component.getComponentId());
			lineModel.setComponent(componentDetail);
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
			writeCSV(generator, (ComponentDetailReportModel) reportModel);
		});

		viewCSV = outputKey(ReportTransmissionType.VIEW, ReportFormat.HTML);
		writerMap.put(viewCSV, (generator, reportModel) -> {
			writeHTML(generator, (ComponentDetailReportModel) reportModel);
		});

		viewCSV = outputKey(ReportTransmissionType.VIEW, ReportFormat.PDF);
		writerMap.put(viewCSV, (generator, reportModel) -> {
			writePDF(generator, (ComponentDetailReportModel) reportModel);
		});

		String emailCSV = outputKey(ReportTransmissionType.EMAIL, ReportFormat.CSV);
		writerMap.put(emailCSV, (generator, reportModel) -> {
			writeCSV(generator, (ComponentDetailReportModel) reportModel);
		});

		viewCSV = outputKey(ReportTransmissionType.EMAIL, ReportFormat.HTML);
		writerMap.put(viewCSV, (generator, reportModel) -> {
			writeHTML(generator, (ComponentDetailReportModel) reportModel);
		});

		viewCSV = outputKey(ReportTransmissionType.EMAIL, ReportFormat.PDF);
		writerMap.put(viewCSV, (generator, reportModel) -> {
			writePDF(generator, (ComponentDetailReportModel) reportModel);
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

		List<String> formatCodes = Arrays.asList(
				ReportFormat.CSV,
				ReportFormat.HTML,
				ReportFormat.PDF
		);

		switch (reportTransmissionType) {
			case ReportTransmissionType.VIEW:
			case ReportTransmissionType.EMAIL:
				for (String code : formatCodes) {
					ReportFormat format = service.getLookupService().getLookupEnity(ReportFormat.class, code);
					formats.add(format);
				}
				break;
		}

		return formats;
	}

	private void writeCSV(BaseGenerator generator, ComponentDetailReportModel reportModel)
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		//write header
		cvsGenerator.addLine(reportModel.getTitle(), sdf.format(reportModel.getCreateTime()));
		cvsGenerator.addLine("");

		cvsGenerator.addLine(
				"Entry Name",
				"Entry Organization",
				"Entry Description",
				"Last Vendor Approved Update",
				"Last System Update"
		);

		for (ComponentDetailReportLineModel lineModel : reportModel.getData()) {

			cvsGenerator.addLine(
					lineModel.getComponent().getName(),
					lineModel.getComponent().getOrganization(),
					StringProcessor.stripHtml(lineModel.getComponent().getDescription()),
					lineModel.getComponent().getApprovedDate(),
					lineModel.getComponent().getLastActivityDts()
			);

			List<ComponentAttributeView> attributes = lineModel.getComponent().getAttributes();
			if (attributes != null) {
				cvsGenerator.addLine("Vitals");

				for (ComponentAttributeView view : attributes) {

					cvsGenerator.addLine(
							"",
							view.getTypeDescription(),
							view.getCodeDescription()
					);
				}

			}

			if (lineModel.getComponent().getContacts() != null) {
				cvsGenerator.addLine("Contacts");
				for (ComponentContactView contact : lineModel.getComponent().getContacts()) {

					String securityMarking = "";
					if (getBranding().getAllowSecurityMarkingsFlg()
							&& StringUtils.isNotBlank(contact.getSecurityMarkingType())) {
						securityMarking = "(" + contact.getSecurityMarkingType() + ") ";
					}

					cvsGenerator.addLine(
							"",
							contact.getPositionDescription(),
							securityMarking + contact.getFirstName(),
							contact.getLastName(),
							contact.getOrganization(),
							contact.getEmail(),
							contact.getPhone()
					);
				}
			}

			if (lineModel.getComponent().getResources() != null) {

				cvsGenerator.addLine("Resources");
				for (ComponentResourceView resource : lineModel.getComponent().getResources()) {

					String securityMarking = "";
					if (getBranding().getAllowSecurityMarkingsFlg()
							&& StringUtils.isNotBlank(resource.getSecurityMarkingType())) {
						securityMarking = "(" + resource.getSecurityMarkingType() + ") ";
					}

					cvsGenerator.addLine(
							"",
							resource.getResourceTypeDesc(),
							resource.getDescription(),
							securityMarking + resource.getLink()
					);
				}
			}

			cvsGenerator.addLine("");
		}

	}

	private void writeHTML(BaseGenerator generator, ComponentDetailReportModel reportModel)
	{
		HtmlGenerator htmlGenerator = (HtmlGenerator) generator;

		String renderedTemplate = createHtml(reportModel);
		htmlGenerator.addLine(renderedTemplate);
	}

	private void writePDF(BaseGenerator generator, ComponentDetailReportModel reportModel)
	{
		HtmlToPdfGenerator pdfGenerator = (HtmlToPdfGenerator) generator;

		String renderedTemplate = createHtml(reportModel);
		EmbeddedImagePreProcessor embeddedImagePreProcessor = new EmbeddedImagePreProcessor(service);
		renderedTemplate = embeddedImagePreProcessor.processHtml(renderedTemplate);

		PDFRenderHandler renderHandler = (renderer) -> {
			LocalMediaPDFHandler handler = new LocalMediaPDFHandler(renderer.getSharedContext().getUserAgentCallback(), service);
			renderer.getSharedContext().setUserAgentCallback(handler);
		};

		pdfGenerator.savePdfDocument(renderedTemplate, renderHandler);
	}

	private String createHtml(ComponentDetailReportModel reportModel)
	{
		String renderedTemplate = null;
		try {
			// Initialize hashmap of variables that will be inserted into the HTML template
			Map<String, Object> root = new HashMap<>();

			// bind values to variables
			root.put("createTime",LocalDateTime.now());
			root.put("baseUrl", PropertiesManager.getInstance().getValueDefinedDefault(PropertiesManager.KEY_EXTERNAL_HOST_URL));

			EntryDetailsOptions options = new EntryDetailsOptions(report.getReportOption());
			root.put("reportOptions", options);

			root.put("allowSecurityMargkingsFlg", getBranding().getAllowSecurityMarkingsFlg());
			root.put("reportModel", reportModel);

			Configuration templateConfig = ReportManager.getTemplateConfig();

			//	Generate the report template
			Template template = templateConfig.getTemplate("detailReport.ftl");
			Writer writer = new StringWriter();
			template.process(root, writer);
			renderedTemplate = writer.toString();

		} catch (MalformedTemplateNameException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		} catch (TemplateException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		return renderedTemplate;
	}

	@Override
	public String reportSummmary(ComponentDetailReportModel reportModel)
	{
		StringBuilder summary = new StringBuilder();
		summary.append(super.reportSummmary(reportModel));

		summary.append("<br>Entries in report: ")
				.append(reportModel.getData().size())
				.append("<br><br>");

		reportModel.getData().forEach(line -> {
			summary.append(line.getComponent().getName()).append("<br>");
		});

		return summary.toString();
	}

}
