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

import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.sort.AttributeCodeArchComparator;
import edu.usu.sdl.openstorefront.core.sort.AttributeCodeComparator;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.model.CategoryComponentReportLineModel;
import edu.usu.sdl.openstorefront.report.model.CategoryComponentReportModel;
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
public class CategoryComponentReport
		extends BaseReport
{

	private static final String NO_CATEGORY = "None";

	public CategoryComponentReport(Report report)
	{
		super(report);
	}

	@Override
	protected CategoryComponentReportModel gatherData()
	{
		CategoryComponentReportModel reportModel = new CategoryComponentReportModel();
		reportModel.setTitle("Category Entry Report");

		String category = NO_CATEGORY;
		String categoryMessage = "";
		if (StringUtils.isNotBlank(report.getReportOption().getCategory())) {
			AttributeType attributeType = service.getPersistenceService().findById(AttributeType.class, report.getReportOption().getCategory());
			if (attributeType != null) {
				category = report.getReportOption().getCategory();
			} else {
				categoryMessage = " (Selected category was not found.  Check report options and select another category.)";
			}
		}
		reportModel.setCategory(category + categoryMessage);

		if (NO_CATEGORY.equals(category)) {

			Component componentExample = new Component();
			componentExample.setActiveStatus(Component.ACTIVE_STATUS);
			componentExample.setApprovalState(ApprovalStatus.APPROVED);

			List<Component> components = componentExample.findByExample();
			components = filterEngine.filter(components);
			components.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, Component.FIELD_NAME));

			for (Component component : components) {
				CategoryComponentReportLineModel lineModel = new CategoryComponentReportLineModel();
				lineModel.setName(component.getName());
				lineModel.setDecription(component.getDescription());
				lineModel.setLastActivityDts(component.getLastActivityDts());
				reportModel.getData().add(lineModel);
			}

		} else {

			//Only grab approved/active components
			AttributeType attributeType = service.getPersistenceService().findById(AttributeType.class, category);
			List<AttributeCode> codes = service.getAttributeService().findCodesForType(category);

			if (Convert.toBoolean(attributeType.getArchitectureFlg())) {
				codes.sort(new AttributeCodeArchComparator<>());
			} else {
				codes.sort(new AttributeCodeComparator<>());
			}

			ComponentAttribute componentAttributeExample = new ComponentAttribute();
			ComponentAttributePk componentAttributePk = new ComponentAttributePk();
			componentAttributePk.setAttributeType(category);
			componentAttributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
			componentAttributeExample.setComponentAttributePk(componentAttributePk);

			List<ComponentAttribute> attributes = componentAttributeExample.findByExample();

			Map<String, List<String>> codeComponentMap = new HashMap<>();
			attributes.forEach(attribute -> {
				if (codeComponentMap.containsKey(attribute.getComponentAttributePk().getAttributeCode())) {
					codeComponentMap.get(attribute.getComponentAttributePk().getAttributeCode()).add(attribute.getComponentId());
				} else {
					List<String> componentIds = new ArrayList<>();
					componentIds.add(attribute.getComponentId());
					codeComponentMap.put(attribute.getComponentAttributePk().getAttributeCode(), componentIds);
				}
			});

			Component componentExample = new Component();
			componentExample.setActiveStatus(Component.ACTIVE_STATUS);
			componentExample.setApprovalState(ApprovalStatus.APPROVED);

			List<Component> components = componentExample.findByExample();
			components = filterEngine.filter(components);
			components.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, Component.FIELD_NAME));

			if (!report.dataIdSet().isEmpty()) {
				components = components.stream().filter(c -> report.dataIdSet().contains(c.getComponentId())).collect(Collectors.toList());
			}

			Map<String, Component> componentMap = new HashMap<>();
			components.forEach(component -> {
				componentMap.put(component.getComponentId(), component);
			});

			for (AttributeCode code : codes) {
				List<String> componentIds = codeComponentMap.get(code.getAttributeCodePk().getAttributeCode());
				if (componentIds != null) {
					for (String componentId : codeComponentMap.get(code.getAttributeCodePk().getAttributeCode())) {
						Component component = componentMap.get(componentId);
						if (component != null) {

							CategoryComponentReportLineModel lineModel = new CategoryComponentReportLineModel();
							lineModel.setName(component.getName());
							lineModel.setDecription(component.getDescription());
							lineModel.setLastActivityDts(component.getLastActivityDts());
							lineModel.setCategoryLabel(code.getLabel());
							lineModel.setCategoryDescription(code.getDescription());
							reportModel.getData().add(lineModel);

						}

					}
				}
			}

		}

		return reportModel;
	}

	@Override
	protected Map<String, ReportWriter> getWriterMap()
	{
		Map<String, ReportWriter> writerMap = new HashMap<>();

		String viewCSV = outputKey(ReportTransmissionType.VIEW, ReportFormat.CSV);
		writerMap.put(viewCSV, (generator, reportModel) -> {
			writeCSV(generator, (CategoryComponentReportModel) reportModel);
		});

		String emailCSV = outputKey(ReportTransmissionType.EMAIL, ReportFormat.CSV);
		writerMap.put(emailCSV, (generator, reportModel) -> {
			writeCSV(generator, (CategoryComponentReportModel) reportModel);
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

	private void writeCSV(BaseGenerator generator, CategoryComponentReportModel reportModel)
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		cvsGenerator.addLine(reportModel.getTitle(), sdf.format(reportModel.getCreateTime()));
		cvsGenerator.addLine("Category: " + reportModel.getCategory());
		cvsGenerator.addLine("");

		cvsGenerator.addLine(
				"Category Label",
				"Category Description",
				"Entry Name",
				"Entry Description",
				"Last Update Date"
		);

		//Note: the details should be grouped by code by default
		for (CategoryComponentReportLineModel lineModel : reportModel.getData()) {
			cvsGenerator.addLine(
					lineModel.getCategoryLabel(),
					StringProcessor.stripHtml(lineModel.getCategoryDescription()),
					lineModel.getName(),
					StringProcessor.ellipseString(StringProcessor.stripHtml(lineModel.getDecription()), 300),
					sdf.format(lineModel.getLastActivityDts())
			);
		}

	}

}
