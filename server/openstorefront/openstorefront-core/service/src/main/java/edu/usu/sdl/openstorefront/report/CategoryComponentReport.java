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
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.sort.AttributeCodeArchComparator;
import edu.usu.sdl.openstorefront.core.sort.AttributeCodeComparator;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
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

	public CategoryComponentReport(Report report)
	{
		super(report);
	}

	@Override
	protected void gatherData()
	{
	}

	@Override
	protected void writeReport()
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		String category = AttributeType.DI2E_SVCV4;
		if (report.getReportOption() != null
				&& StringUtils.isNotBlank(report.getReportOption().getCategory())) {
			category = report.getReportOption().getCategory();
		}

		//write header
		cvsGenerator.addLine("Category Component Report", sdf.format(TimeUtil.currentDate()));
		cvsGenerator.addLine("Category:" + category);
		cvsGenerator.addLine("");
		cvsGenerator.addLine(
				"Category Label",
				"Category Description",
				"Component Name",
				"Component Description",
				//"Security Classification",
				"Last Update Date"
		);

		//Only grab approved/active components
		AttributeType attributeType = service.getAttributeService().findType(category);
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

		if (!report.dataIdSet().isEmpty()) {
			components = components.stream().filter(c -> report.dataIdSet().contains(c.getComponentId())).collect(Collectors.toList());
		}

		Map<String, Component> componentMap = new HashMap<>();
		components.forEach(component -> {
			componentMap.put(component.getComponentId(), component);
		});

		for (AttributeCode code : codes) {
			cvsGenerator.addLine(
					code.getLabel(),
					code.getDescription()
			);

			List<String> componentIds = codeComponentMap.get(code.getAttributeCodePk().getAttributeCode());
			if (componentIds != null) {
				for (String componentId : codeComponentMap.get(code.getAttributeCodePk().getAttributeCode())) {
					Component component = componentMap.get(componentId);
					if (component != null) {
						cvsGenerator.addLine(
								"",
								"",
								component.getName(),
								component.getDescription(),
								//component.getSecurityMarkingType(),
								sdf.format(component.getLastActivityDts())
						);
					}
				}
			}
		}
	}

}
