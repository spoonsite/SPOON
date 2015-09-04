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

/**
 *
 * @author dshurtleff
 */
public class ComponentTypeReport
		extends BaseReport
{

	public ComponentTypeReport(Report report)
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

		// Setup the csv headers
		//TODO: this should be a report option
		String type = AttributeType.TYPE;
		String svcv4 = AttributeType.DI2E_SVCV4;
		String level = AttributeType.DI2ELEVEL;

		//write header
		cvsGenerator.addLine("Component Type Report", sdf.format(TimeUtil.currentDate()));
		cvsGenerator.addLine("");
		cvsGenerator.addLine(
				"Component Name",
				"Component Description",
				"Component Type",
				"Component Evaluation",
				"SvcV-4 Mapping"
		);

		// Grab the attributes for svcv4
		List<AttributeCode> codeq = service.getAttributeService().findCodesForType(svcv4);
		codeq.addAll(service.getAttributeService().findCodesForType(type));
		codeq.addAll(service.getAttributeService().findCodesForType(level));
		Map<String, AttributeCode> codes = new HashMap<>();
		for (AttributeCode code : codeq) {
			codes.put(code.getAttributeCodePk().getAttributeCode(), code);
		}

		//Grab all active attributes
		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		ComponentAttributePk componentAttributePk = new ComponentAttributePk();

		componentAttributePk.setAttributeType(type);
		componentAttributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		componentAttributeExample.setComponentAttributePk(componentAttributePk);

		List<ComponentAttribute> allCodes = componentAttributeExample.findByExample();

		componentAttributePk.setAttributeType(svcv4);
		componentAttributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		componentAttributeExample.setComponentAttributePk(componentAttributePk);

		allCodes.addAll(componentAttributeExample.findByExample());

		componentAttributePk.setAttributeType(level);
		componentAttributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		componentAttributeExample.setComponentAttributePk(componentAttributePk);

		allCodes.addAll(componentAttributeExample.findByExample());

		Map<String, Map<String, List<ComponentAttribute>>> codeToComponent = new HashMap<>();
		allCodes.forEach(attribute -> {
			if (codeToComponent.containsKey(attribute.getComponentId())) {
				if (codeToComponent.get(attribute.getComponentId()).containsKey(attribute.getComponentAttributePk().getAttributeType())) {
					codeToComponent.get(attribute.getComponentId()).get(attribute.getComponentAttributePk().getAttributeType()).add(attribute);
				}
				else {
					List<ComponentAttribute> attributeList = new ArrayList<>();
					attributeList.add(attribute);
					codeToComponent.get(attribute.getComponentId()).put(attribute.getComponentAttributePk().getAttributeType(), attributeList);
				}
			}
			else {
				Map<String, List<ComponentAttribute>> temp = new HashMap<>();
				List<ComponentAttribute> attributeList = new ArrayList<>();
				attributeList.add(attribute);
				temp.put(attribute.getComponentAttributePk().getAttributeType(), attributeList);
				codeToComponent.put(attribute.getComponentId(), temp);
			}
		});

		//Grab all components
		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
		componentExample.setApprovalState(ApprovalStatus.APPROVED);

		List<Component> components = componentExample.findByExample();
		int test = 0;
		for (Component component : components) {
			if (component != null && !component.getComponentId().isEmpty()) {
				boolean first = true;
				Map<String, List<ComponentAttribute>> attributeMaps = codeToComponent.get(component.getComponentId());
				List<ComponentAttribute> types = attributeMaps.get(AttributeType.TYPE);
				List<ComponentAttribute> svcv4s = attributeMaps.get(AttributeType.DI2E_SVCV4);
				List<ComponentAttribute> levels = attributeMaps.get(AttributeType.DI2ELEVEL);
				while ((types != null && types.size() > 0) || (svcv4s != null && svcv4s.size() > 0) || (levels != null && levels.size() > 0)) {
					try {
						if (first) {
							test++;
							first = false;
							ComponentAttribute t = (types != null && types.size() > 0) ? types.remove(0) : null;
							ComponentAttribute s = (svcv4s != null && svcv4s.size() > 0) ? svcv4s.remove(0) : null;
							ComponentAttribute l = (levels != null && levels.size() > 0) ? levels.remove(0) : null;
							String tempType = (t != null && t.getComponentAttributePk() != null && t.getComponentAttributePk().getAttributeCode() != null) ? t.getComponentAttributePk().getAttributeCode() : "";
							String tempSvcV4 = (s != null && s.getComponentAttributePk() != null && s.getComponentAttributePk().getAttributeCode() != null) ? s.getComponentAttributePk().getAttributeCode() : "";
							String tempLevel = (l != null && l.getComponentAttributePk() != null && l.getComponentAttributePk().getAttributeCode() != null) ? l.getComponentAttributePk().getAttributeCode() : "";
							String attSvcV4 = (!tempSvcV4.equals("")) ? ((codes.get(tempSvcV4) != null) ? codes.get(tempSvcV4).getLabel() : "") : "";
							String attType = (!tempType.equals("")) ? ((codes.get(tempType) != null) ? codes.get(tempType).getLabel() : "") : "";
							String attLevel = (!tempLevel.equals("")) ? ((codes.get(tempLevel) != null) ? codes.get(tempLevel).getLabel() : "") : "";
							cvsGenerator.addLine(
									component.getName(),
									component.getDescription(),
									attType,
									attLevel,
									attSvcV4
							);
						}
						else {
							ComponentAttribute t = (types != null && types.size() > 0) ? types.remove(0) : null;
							ComponentAttribute s = (svcv4s != null && svcv4s.size() > 0) ? svcv4s.remove(0) : null;
							ComponentAttribute l = (levels != null && levels.size() > 0) ? levels.remove(0) : null;
							String tempType = (t != null && t.getComponentAttributePk() != null && t.getComponentAttributePk().getAttributeCode() != null) ? t.getComponentAttributePk().getAttributeCode() : "";
							String tempSvcV4 = (s != null && s.getComponentAttributePk() != null && s.getComponentAttributePk().getAttributeCode() != null) ? s.getComponentAttributePk().getAttributeCode() : "";
							String tempLevel = (l != null && l.getComponentAttributePk() != null && l.getComponentAttributePk().getAttributeCode() != null) ? l.getComponentAttributePk().getAttributeCode() : "";
							String attSvcV4 = (!tempSvcV4.equals("")) ? ((codes.get(tempSvcV4) != null) ? codes.get(tempSvcV4).getLabel() : "") : "";
							String attType = (!tempType.equals("")) ? ((codes.get(tempType) != null) ? codes.get(tempType).getLabel() : "") : "";
							String attLevel = (!tempLevel.equals("")) ? ((codes.get(tempLevel) != null) ? codes.get(tempLevel).getLabel() : "") : "";
							cvsGenerator.addLine(
									"",
									"",
									attType,
									attLevel,
									attSvcV4
							);
						}
					}
					catch (NullPointerException e) {
						//we hit e
					}
				}
				cvsGenerator.addLine("");
			}
		}
	}
}
