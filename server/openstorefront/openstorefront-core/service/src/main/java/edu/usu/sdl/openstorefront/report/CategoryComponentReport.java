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
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.core.sort.AttributeCodeArchComparator;
import edu.usu.sdl.openstorefront.core.sort.AttributeCodeComparator;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
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

	private static final String NO_CATEGORY = "None";

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

		//write header
		cvsGenerator.addLine("Category Entry Report", sdf.format(TimeUtil.currentDate()));
		cvsGenerator.addLine("Category: " + category + categoryMessage);
		cvsGenerator.addLine("");
		
		List<String> header = new ArrayList<>();
		header.add("Category Label");
		header.add("Category Description");
		header.add("Entry Name");
		header.add("Entry Description");
		header.add("Last Update Date");
		
		if (getBranding().getAllowSecurityMarkingsFlg()) {
			header.add("Security Marking");
		}		
		cvsGenerator.addLine(header.toArray());

		if (NO_CATEGORY.equals(category)) {

			Component componentExample = new Component();
			componentExample.setActiveStatus(Component.ACTIVE_STATUS);
			componentExample.setApprovalState(ApprovalStatus.APPROVED);

			List<Component> components = componentExample.findByExample();
			components = FilterEngine.filter(components);
			components.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, Component.FIELD_NAME));	
			
			for (Component component : components) {
				List<String> data = new ArrayList<>();
				data.add("");
				data.add("");
				data.add(component.getName());
				data.add(component.getDescription());
				data.add(sdf.format(component.getLastActivityDts()));
				if (getBranding().getAllowSecurityMarkingsFlg()) {
					if (StringUtils.isNotBlank(component.getSecurityMarkingType())) {
						data.add(component.getSecurityMarkingType());
					} else {
						data.add("");
					}
				}
				cvsGenerator.addLine(data.toArray());
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
			components = FilterEngine.filter(components);
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
							List<String> data = new ArrayList<>();
							data.add(code.getLabel());
							data.add(code.getDescription());
							data.add(component.getName());
							data.add(component.getDescription());
							data.add(sdf.format(component.getLastActivityDts()));
							if (getBranding().getAllowSecurityMarkingsFlg()) {
								if (StringUtils.isNotBlank(component.getSecurityMarkingType())) {
									data.add(component.getSecurityMarkingType());
								} else {
									data.add("");
								}
							}
							cvsGenerator.addLine(data.toArray());							
						}

					}
				}
			}

		}
	}

}
