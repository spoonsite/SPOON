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
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.HtmlGenerator;
import edu.usu.sdl.openstorefront.report.generator.HtmlToPdfGenerator;
import edu.usu.sdl.openstorefront.report.model.EntryListingDataSet;
import edu.usu.sdl.openstorefront.report.model.EntryListingReportLineModel;
import edu.usu.sdl.openstorefront.report.model.EntryListingReportModel;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import edu.usu.sdl.openstorefront.service.manager.ReportManager;
import edu.usu.sdl.openstorefront.service.manager.resource.ConfluenceClient;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class EntryListingReport
		extends BaseReport
{

	private static final Logger LOG = Logger.getLogger(EntryListingReport.class.getName());

	private static final int MAX_DESCRIPTION_SIZE = 300;
	private static final int MOST_RECENT_MAX = 5;

	public EntryListingReport(Report report)
	{
		super(report);
	}

	public EntryListingReport(Report report, Service service)
	{
		super(report, service);
	}

	public EntryListingReport(Report report, Service service, FilterEngine filterEngine)
	{
		super(report, service, filterEngine);
	}

	@Override
	protected EntryListingReportModel gatherData()
	{
		EntryListingReportModel entryListingReportModel = new EntryListingReportModel();
		entryListingReportModel.setTitle(PropertiesManager.getInstance().getValueDefinedDefault(PropertiesManager.KEY_APPLICATION_TITLE, "Storefront") + " Listing Index");
		String viewLinkBase = PropertiesManager.getInstance().getValueDefinedDefault(PropertiesManager.KEY_EXTERNAL_HOST_URL) + "/View.action?id=";

		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
		componentExample.setApprovalState(ApprovalStatus.APPROVED);
		List<Component> components = componentExample.findByExample();
		components = filterEngine.filter(components);

		if (!report.dataIdSet().isEmpty()) {
			components = components.stream().filter(c -> report.dataIdSet().contains(c.getComponentId())).collect(Collectors.toList());
		}
		components.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, Component.FIELD_NAME));

		entryListingReportModel.setTotalRecords(components.size());

		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setActiveStatus(Evaluation.ACTIVE_STATUS);
		Map<String, List<Evaluation>> evalMap = evaluationExample.findByExample()
				.stream()
				.collect(Collectors.groupingBy(Evaluation::getOriginComponentId));

		//sort and group models
		List<Component> recentlyUpdated = components.stream()
				.sorted((a, b) -> {
					return b.getLastActivityDts().compareTo(a.getLastActivityDts());
				})
				.limit(MOST_RECENT_MAX)
				.collect(Collectors.toList());

		recentlyUpdated.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_DESCENDING, Component.FIELD_LAST_ACTIVITY_DTS));

		for (Component component : recentlyUpdated) {
			entryListingReportModel.getRecentlyUpdated().getData().add(componentToLineModel(component, viewLinkBase, evalMap));
		}

		List<Evaluation> recentlyEvals = evalMap.values().stream()
				.flatMap(List::stream)
				.collect(Collectors.toList())
				.stream()
				.sorted((a, b) -> {
					return b.getUpdateDts().compareTo(a.getUpdateDts());
				})
				.filter(e -> {
					return e.getPublished();
				})
				.limit(MOST_RECENT_MAX)
				.collect(Collectors.toList());

		Map<String, Component> allComponentMap = new HashMap<>();
		for (Component component : components) {
			allComponentMap.put(component.getComponentId(), component);
		}

		for (Evaluation eval : recentlyEvals) {
			Component component = allComponentMap.get(eval.getOriginComponentId());
			if (component != null) {
				entryListingReportModel.getRecentlyEvaluated().getData().add(componentToLineModel(component, viewLinkBase, evalMap));
			} else {
				LOG.log(Level.WARNING, MessageFormat.format("Unable to find component for evaluation.  Eval Id: {0} Component Id: {1}", new Object[]{eval.getEvaluationId(), eval.getOriginComponentId()}));
			}
		}
		entryListingReportModel.getRecentlyEvaluated().getData().sort(new BeanComparator<>(OpenStorefrontConstant.SORT_DESCENDING, EntryListingReportLineModel.FIELD_LAST_UPDATED_DTS));

		Map<String, List<EntryListingReportLineModel>> groupMap = new HashMap<>();
		for (Component component : components) {

			String index = "" + component.getName().toUpperCase().trim().charAt(0);

			if (StringUtils.isAlpha(index) == false) {
				index = "#";
			}

			if (!groupMap.containsKey(index)) {
				groupMap.put(index, new ArrayList<>());
			}
			groupMap.get(index).add(componentToLineModel(component, viewLinkBase, evalMap));
		}

		for (String key : groupMap.keySet()) {
			EntryListingDataSet dataSet = new EntryListingDataSet();
			dataSet.setTitle(key);
			dataSet.setData(groupMap.get(key));
			entryListingReportModel.getData().add(dataSet);
		}

		entryListingReportModel.getData().sort((a, b) -> {
			return a.getTitle().compareTo(b.getTitle());
		});

		return entryListingReportModel;
	}

	/**
	 *  Loads information from the component object into LineModel object.
	 *  (later in other functions this object of the EntryListingReportLineModel class is
	 * 	 used to load information into the user's view.)
	 * 	This function maps information from the Component to the EntryListingReportLineModel
	 *  object. If you need to change what data is found in the EntryListingReportLineModel
	 *  object, you should do that here. 
	 * @param component
	 * @param viewLinkBase
	 * @param evalMap
	 * @return EntryListingReportLineModel
	 */
	private EntryListingReportLineModel componentToLineModel(Component component, String viewLinkBase, Map<String, List<Evaluation>> evalMap)
	{
		EntryListingReportLineModel lineModel = new EntryListingReportLineModel();

		lineModel.setComponentId(component.getComponentId());
		lineModel.setViewLink(viewLinkBase + component.getComponentId());

		String entryType = ConfluenceClient.confluenceEscapeCharater(
				service.getComponentService().getComponentTypeParentsString(component.getComponentType(), true)
		);
		lineModel.setEntryType(entryType);

		// Set time/date properties
		lineModel.setLastSubmitDts(component.getSubmittedDts());
		lineModel.setLastUpdatedDts(component.getLastActivityDts());
		lineModel.setlastVendorUpdateApprovedDate(component.getApprovedDts());

		String name = ConfluenceClient.confluenceEscapeCharater(component.getName());
		lineModel.setName(name);

		String description = StringProcessor.ellipseString(StringProcessor.stripHtml(component.getDescription()), MAX_DESCRIPTION_SIZE);
		description = StringProcessor.stripeExtendedChars(description);
		description = ConfluenceClient.confluenceEscapeCharater(description);

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

		return lineModel;
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
		writerMap.put(viewFormat, (ReportWriter<EntryListingReportModel>) this::writeConfluenceHtml);

		return writerMap;
	}

	private void writeHtml(BaseGenerator generator, EntryListingReportModel reportModel)
	{
		HtmlGenerator htmlGenerator = (HtmlGenerator) generator;
		String renderedTemplate = createHtml(reportModel, "entryListing.ftl");
		htmlGenerator.addLine(renderedTemplate);
	}

	private void writeConfluenceHtml(BaseGenerator generator, EntryListingReportModel reportModel)
	{
		HtmlGenerator htmlGenerator = (HtmlGenerator) generator;
		String renderedTemplate = createHtml(reportModel, "entryListingConfluence.ftl");
		htmlGenerator.addLine(renderedTemplate);
	}

	private void writePdf(BaseGenerator generator, EntryListingReportModel reportModel)
	{
		HtmlToPdfGenerator pdfGenerator = (HtmlToPdfGenerator) generator;
		String renderedTemplate = createHtml(reportModel, "entryListing.ftl");
		pdfGenerator.savePdfDocument(renderedTemplate);
	}

	/**
	 * For modularity this function was created, it generates the actual report the user sees in HTMl code string. All the other 'write-' functions above-
	 * writeHtml, writeConfluenceHtml, writePdf - they all take the HTML document string this generates and then go the extra step of converting html into pdf/
	 * confluence/etc... 
	 * @param reportModel
	 * @param templateFile
	 * @return String of HTML that is used for the actual report the user sees. 
	 */
	private String createHtml(EntryListingReportModel reportModel, String templateFile)
	{
		String renderedTemplate = null;
		try {
			Configuration templateConfig = ReportManager.getTemplateConfig();
			Template template = templateConfig.getTemplate(templateFile);
			Writer writer = new StringWriter();
			template.process(reportModel, writer);
			renderedTemplate = writer.toString();
		} catch (TemplateException | IOException e) {
			throw new OpenStorefrontRuntimeException(e);
		}

		return renderedTemplate;
	}

}
