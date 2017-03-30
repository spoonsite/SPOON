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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ComponentMetadata;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import edu.usu.sdl.openstorefront.core.entity.ContactType;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ResourceType;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.core.view.ComponentResourceView;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.generator.HtmlGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ComponentDetailReport
		extends BaseReport
{

	private static final Logger log = Logger.getLogger(ExternalLinkValidationReport.class.getName());

	private List<Component> components;
	private Map<String, List<ComponentMetadata>> metaDataMap = new HashMap<>();
	private Map<String, Map<String, List<ComponentAttribute>>> codeToComponent = new HashMap<>();
	private Map<String, List<ComponentContact>> contactMap = new HashMap<>();
	private Map<String, List<ComponentResource>> resourceMap = new HashMap<>();

	public ComponentDetailReport(Report report)
	{
		super(report);
	}

	@Override
	protected void gatherData()
	{
		//Grab all active attributes
		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		componentAttributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		List<ComponentAttribute> allCodes = componentAttributeExample.findByExample();

		allCodes.forEach(attribute -> {
			if (codeToComponent.containsKey(attribute.getComponentId())) {
				if (codeToComponent.get(attribute.getComponentId()).containsKey(attribute.getComponentAttributePk().getAttributeType())) {
					codeToComponent.get(attribute.getComponentId()).get(attribute.getComponentAttributePk().getAttributeType()).add(attribute);
				} else {
					List<ComponentAttribute> attributeList = new ArrayList<>();
					attributeList.add(attribute);
					codeToComponent.get(attribute.getComponentId()).put(attribute.getComponentAttributePk().getAttributeType(), attributeList);
				}
			} else {
				Map<String, List<ComponentAttribute>> temp = new HashMap<>();
				List<ComponentAttribute> attributeList = new ArrayList<>();
				attributeList.add(attribute);
				temp.put(attribute.getComponentAttributePk().getAttributeType(), attributeList);
				codeToComponent.put(attribute.getComponentId(), temp);
			}
		});

		//Grab all metadata
		ComponentMetadata metadata = new ComponentMetadata();
		metadata.setActiveStatus(ComponentMetadata.ACTIVE_STATUS);
		List<ComponentMetadata> allMetadata = metadata.findByExample();
		metaDataMap = allMetadata.stream().collect(Collectors.groupingBy(ComponentMetadata::getComponentId));

		//Contacts
		ComponentContact componentContact = new ComponentContact();
		componentContact.setActiveStatus(ComponentContact.ACTIVE_STATUS);
		List<ComponentContact> allContacts = componentContact.findByExample();
		contactMap = allContacts.stream().collect(Collectors.groupingBy(ComponentContact::getComponentId));

		//Resources
		ComponentResource componentResource = new ComponentResource();
		componentResource.setActiveStatus(ComponentResource.ACTIVE_STATUS);
		List<ComponentResource> allResources = componentResource.findByExample();
		resourceMap = allResources.stream().collect(Collectors.groupingBy(ComponentResource::getComponentId));

		//Grab all components
		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
		componentExample.setApprovalState(ApprovalStatus.APPROVED);
		components = componentExample.findByExample();
		components = FilterEngine.filter(components);

		if (!report.dataIdSet().isEmpty()) {
			components = components.stream().filter(c -> report.dataIdSet().contains(c.getComponentId())).collect(Collectors.toList());
		}
		components.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, Component.FIELD_NAME));	
	}

	@Override
	protected void writeReport()
	{
		if (ReportFormat.CSV.equals(report.getReportFormat())) {
			generateCSV();
		} else if (ReportFormat.HTML.equals(report.getReportFormat())) {
			generateHtml();
		}
	}

	private void generateCSV()
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		//write header
		cvsGenerator.addLine("Entry Details Report", sdf.format(TimeUtil.currentDate()));
		cvsGenerator.addLine("");
		
		List<String> header = new ArrayList<>();
		header.add("Entry Name");
		header.add("Entry Organization");
		header.add("Entry Description");
		
		if (getBranding().getAllowSecurityMarkingsFlg()) {
			header.add("Security Marking");
		}		
		cvsGenerator.addLine(header.toArray());		

		for (Component component : components) {

			List<String> data = new ArrayList<>();
			data.add(component.getName());
			data.add(component.getOrganization());
			data.add(component.getDescription());

			if (getBranding().getAllowSecurityMarkingsFlg()) {
				data.add("Security Marking");
			}		
			cvsGenerator.addLine(data.toArray());				

			Map<String, List<ComponentAttribute>> attributeMap = codeToComponent.get(component.getComponentId());

			if (attributeMap != null) {
				cvsGenerator.addLine("Vitals");

				Map<String, String> typeDescriptionMap = new HashMap<>();
				for (String type : attributeMap.keySet()) {
					String typeLabel = service.getAttributeService().findType(type).getDescription();
					typeDescriptionMap.put(typeLabel, type);
				}

				List<String> attributeTypeList = new ArrayList<>(typeDescriptionMap.keySet());
				attributeTypeList.sort(null);

				for (String typeLabel : attributeTypeList) {
					String type = typeDescriptionMap.get(typeLabel);
					List<ComponentAttribute> attributes = attributeMap.get(type);

					if (attributes != null) {
						if (attributes.size() == 1) {
							ComponentAttributePk attributePk = attributes.get(0).getComponentAttributePk();
							AttributeCodePk attributeCodePk = new AttributeCodePk();

							attributeCodePk.setAttributeCode(attributePk.getAttributeCode());
							attributeCodePk.setAttributeType(attributePk.getAttributeType());
							AttributeCode attributeCode = service.getAttributeService().findCodeForType(attributeCodePk);
							String attributeLabel;
							if (attributeCode != null) {
								String securityMarking = "";
								if (getBranding().getAllowSecurityMarkingsFlg() && 
									StringUtils.isNotBlank(attributeCode.getSecurityMarkingType()))
								{
									securityMarking = "(" + attributeCode.getSecurityMarkingType() + ") ";
								}
								attributeLabel = securityMarking + attributeCode.getLabel();
							} else {
								attributeLabel = "Missing Code: " + attributeCodePk.getAttributeCode() + " on Type: " + attributeCodePk.getAttributeType();
							}
							cvsGenerator.addLine(
									"",
									typeLabel,
									attributeLabel
							);
						} else {
							cvsGenerator.addLine(
									"",
									typeLabel
							);
							for (ComponentAttribute componentAttribute : attributes) {
								AttributeCodePk attributeCodePk = new AttributeCodePk();

								attributeCodePk.setAttributeCode(componentAttribute.getComponentAttributePk().getAttributeCode());
								attributeCodePk.setAttributeType(componentAttribute.getComponentAttributePk().getAttributeType());
								AttributeCode attributeCode = service.getAttributeService().findCodeForType(attributeCodePk);
								String attributeLabel;
								if (attributeCode != null) {
									String securityMarking = "";
									if (getBranding().getAllowSecurityMarkingsFlg() && 
										StringUtils.isNotBlank(attributeCode.getSecurityMarkingType()))
									{
										securityMarking = "(" + attributeCode.getSecurityMarkingType() + ") ";
									}
									attributeLabel = securityMarking + attributeCode.getLabel();
								} else {
									attributeLabel = "Missing Code: " + attributeCodePk.getAttributeCode() + " on Type: " + attributeCodePk.getAttributeType();
								}

								cvsGenerator.addLine(
										"",
										"",
										attributeLabel
								);
							}
						}
					}
				}
			}

			//meta data
			List<ComponentMetadata> metaData = metaDataMap.get(component.getComponentId());			
			if (metaData != null) {
				metaData = FilterEngine.filter(metaData);
				
				cvsGenerator.addLine("MetaData");
				metaData.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, ComponentMetadata.FIELD_LABEL));

				for (ComponentMetadata metadataItem : metaData) {

					String securityMarking = "";
					if (getBranding().getAllowSecurityMarkingsFlg() && 
						StringUtils.isNotBlank(metadataItem.getSecurityMarkingType()))
					{
						securityMarking = "(" + metadataItem.getSecurityMarkingType() + ") ";
					}					
					
					cvsGenerator.addLine(
							"",
							metadataItem.getLabel(),
							securityMarking + metadataItem.getValue()
					);
				}
			}

			//contacts
			List<ComponentContact> contacts = contactMap.get(component.getComponentId());
			if (contacts != null) {
				contacts = FilterEngine.filter(contacts);
				
				cvsGenerator.addLine("Contacts");
				for (ComponentContact contact : contacts) {
					
					String securityMarking = "";
					if (getBranding().getAllowSecurityMarkingsFlg() && 
						StringUtils.isNotBlank(contact.getSecurityMarkingType()))
					{
						securityMarking = "(" + contact.getSecurityMarkingType() + ") ";
					}					
					
					Contact contactFull = contact.fullContact();					
					cvsGenerator.addLine(
							"",
							TranslateUtil.translate(ContactType.class, contact.getContactType()),
							securityMarking + contactFull.getFirstName(),
							contactFull.getLastName(),
							contactFull.getOrganization(),
							contactFull.getEmail(),
							contactFull.getPhone()
					);
				}
			}

			//resources
			List<ComponentResource> resources = resourceMap.get(component.getComponentId());
			if (resources != null) {
				resources = FilterEngine.filter(resources);
				
				cvsGenerator.addLine("Resources");
				for (ComponentResource resource : resources) {

					String securityMarking = "";
					if (getBranding().getAllowSecurityMarkingsFlg() && 
						StringUtils.isNotBlank(resource.getSecurityMarkingType()))
					{
						securityMarking = "(" + resource.getSecurityMarkingType() + ") ";
					}						
					
					ComponentResourceView view = ComponentResourceView.toView(resource);

					cvsGenerator.addLine(
							"",
							TranslateUtil.translate(ResourceType.class, view.getResourceType()),
							view.getDescription(),
							securityMarking + view.getLink()
					);
				}
			}

			cvsGenerator.addLine("");
		}
	}

	private void generateHtml()
	{
		HtmlGenerator htmlGenerator = (HtmlGenerator) generator;

		htmlGenerator.addLine("Component Details Report: " + sdf.format(TimeUtil.currentDate()));
		htmlGenerator.addSpace();

		htmlGenerator.addStyleBlock(
				"body{ font-family: Helvetica, Verdana, Arial, sans-serif; } "
				+ "h1 { background-color: #F1F1F1; } "
				+ "table{ "
				+ "border: 1px black solid; "
				+ "border-collapse: collapse;"
				+ "border-spacing: 0;"
				+ "} "
				+ "table td,th { "
				+ "padding-left: 5px; "
				+ "padding-right: 5px; "
				+ "} "
				+ "th { "
				+ "color: white; "
				+ "background-color: #414e68; "
				+ "border: 1px lightgray solid; "
				+ " } "
				+ " td { "
				+ "border: 1px lightgray solid; "
				+ " padding: 5px; "
				+ " } "
				+ " tr:nth-child(odd) { "
				+ " background-color: #eeeeee "
				+ " } "
				+ " tr:nth-child(even) {  "
				+ " background-color: white; "
				+ " } "
				+ "@media print {"
				+ " .pageBreak { "
				+ "    page-break-after: always; "
				+ " }}"
		);

		htmlGenerator.addLine("Entries (" + components.size() + ")");
		htmlGenerator.addRuleLine();

		for (Component component : components) {

			String securityMarking = "";
			if (getBranding().getAllowSecurityMarkingsFlg()) {
				securityMarking = "(" + component.getSecurityMarkingType() + ") ";
			}				
			
			htmlGenerator.addMainHeader(component.getName());
			htmlGenerator.addLine("<b>" + component.getOrganization() + "</b>");
			htmlGenerator.addSpace();
			htmlGenerator.addSpace();
			htmlGenerator.addLine(securityMarking + component.getDescription());
			htmlGenerator.addSpace();

			Map<String, List<ComponentAttribute>> attributeMap = codeToComponent.get(component.getComponentId());

			if (attributeMap != null) {
				htmlGenerator.addLine("<h2>Vitals</h2>");

				Map<String, String> typeDescriptionMap = new HashMap<>();
				for (String type : attributeMap.keySet()) {
					String typeLabel = service.getAttributeService().findType(type).getDescription();
					typeDescriptionMap.put(typeLabel, type);
				}

				List<String> attributeTypeList = new ArrayList<>(typeDescriptionMap.keySet());
				attributeTypeList.sort(null);

				htmlGenerator.addLine("<table>");
				htmlGenerator.addLine("<tr>");
				htmlGenerator.addLine("<th>Vital</th>");
				htmlGenerator.addLine("<th>Value</th>");
				htmlGenerator.addLine("</tr>");
				for (String typeLabel : attributeTypeList) {
					String type = typeDescriptionMap.get(typeLabel);
					List<ComponentAttribute> attributes = attributeMap.get(type);

					if (attributes != null) {

						for (ComponentAttribute componentAttribute : attributes) {
							AttributeCodePk attributeCodePk = new AttributeCodePk();

							attributeCodePk.setAttributeCode(componentAttribute.getComponentAttributePk().getAttributeCode());
							attributeCodePk.setAttributeType(componentAttribute.getComponentAttributePk().getAttributeType());
							AttributeCode attributeCode = service.getAttributeService().findCodeForType(attributeCodePk);
							String attributeLabel;
							if (attributeCode != null) {
								securityMarking = "";
								if (getBranding().getAllowSecurityMarkingsFlg() && 
									StringUtils.isNotBlank(attributeCode.getSecurityMarkingType()))
								{
									securityMarking = "(" + attributeCode.getSecurityMarkingType() + ") ";
								}
								attributeLabel = securityMarking + attributeCode.getLabel();
							} else {
								attributeLabel = "Missing Code: " + attributeCodePk.getAttributeCode() + " on Type: " + attributeCodePk.getAttributeType();
							}
							htmlGenerator.addLine("<tr>");
							htmlGenerator.addLine("<td><b>" + typeLabel + "</b></td>");
							htmlGenerator.addLine("<td>" + attributeLabel + "</td>");
							htmlGenerator.addLine("</tr>");
						}

					}
				}
				htmlGenerator.addLine("</table>");
			}

			//meta data
			List<ComponentMetadata> metaData = metaDataMap.get(component.getComponentId());
			if (metaData != null) {
				metaData = FilterEngine.filter(metaData);
				
				htmlGenerator.addLine("<h2>MetaData</h2>");
				metaData.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, ComponentMetadata.FIELD_LABEL));

				htmlGenerator.addLine("<table>");
				htmlGenerator.addLine("<tr>");
				htmlGenerator.addLine("<th>Label</th>");
				htmlGenerator.addLine("<th>Value</th>");
				htmlGenerator.addLine("</tr>");
				for (ComponentMetadata metadataItem : metaData) {

					securityMarking = "";
					if (getBranding().getAllowSecurityMarkingsFlg() && 
						StringUtils.isNotBlank(metadataItem.getSecurityMarkingType()))
					{
						securityMarking = "(" + metadataItem.getSecurityMarkingType() + ") ";
					}						
					
					htmlGenerator.addLine("<tr>");
					htmlGenerator.addLine("<td><b>" + metadataItem.getLabel() + "</b></td>");
					htmlGenerator.addLine("<td>"   + securityMarking + metadataItem.getValue() + "</td>");
					htmlGenerator.addLine("</tr>");
				}
				htmlGenerator.addLine("</table>");
			}

			//contacts
			List<ComponentContact> contacts = contactMap.get(component.getComponentId());
			if (contacts != null) {
				contacts = FilterEngine.filter(contacts);
				
				htmlGenerator.addLine("<h2>Contacts</h2>");
				htmlGenerator.addLine("<table>");
				htmlGenerator.addLine("<tr>");
				htmlGenerator.addLine("<th>Type</th>");
				htmlGenerator.addLine("<th>Firstname</th>");
				htmlGenerator.addLine("<th>Lastname</th>");
				htmlGenerator.addLine("<th>Organization</th>");
				htmlGenerator.addLine("<th>Email</th>");
				htmlGenerator.addLine("<th>Phone</th>");
				htmlGenerator.addLine("</tr>");
				for (ComponentContact contact : contacts) {

					securityMarking = "";
					if (getBranding().getAllowSecurityMarkingsFlg() && 
						StringUtils.isNotBlank(contact.getSecurityMarkingType()))
					{
						securityMarking = "(" + contact.getSecurityMarkingType() + ") ";
					}				
					
					Contact contactFull = contact.fullContact();
					
					htmlGenerator.addLine("<tr>");
					htmlGenerator.addLine("<td><b>" + TranslateUtil.translate(ContactType.class, contact.getContactType()) + "</b></td>");
					htmlGenerator.addLine("<td>" + StringProcessor.blankIfNull(securityMarking + contactFull.getFirstName()) + "</td>");
					htmlGenerator.addLine("<td>" + StringProcessor.blankIfNull(contactFull.getLastName()) + "</td>");
					htmlGenerator.addLine("<td>" + StringProcessor.blankIfNull(contactFull.getOrganization()) + "</td>");
					htmlGenerator.addLine("<td>" + StringProcessor.blankIfNull(contactFull.getEmail()) + "</td>");
					htmlGenerator.addLine("<td>" + StringProcessor.blankIfNull(contactFull.getPhone()) + "</td>");
					htmlGenerator.addLine("</tr>");

				}
				htmlGenerator.addLine("</table>");
			}

			//resources
			List<ComponentResource> resources = resourceMap.get(component.getComponentId());
			if (resources != null) {
				resources = FilterEngine.filter(resources);
				
				htmlGenerator.addLine("<h2>Resources</h2>");
				htmlGenerator.addLine("<table>");
				htmlGenerator.addLine("<tr>");
				htmlGenerator.addLine("<th>Type</th>");
				htmlGenerator.addLine("<th>Description</th>");
				htmlGenerator.addLine("<th>Link</th>");
				htmlGenerator.addLine("<th>Restricted (requires login/CAC)</th>");
				htmlGenerator.addLine("</tr>");
				for (ComponentResource resource : resources) {

					securityMarking = "";
					if (getBranding().getAllowSecurityMarkingsFlg() && 
						StringUtils.isNotBlank(resource.getSecurityMarkingType()))
					{
						securityMarking = "(" + resource.getSecurityMarkingType() + ") ";
					}					
					
					ComponentResourceView view = ComponentResourceView.toView(resource);

					htmlGenerator.addLine("<tr>");
					htmlGenerator.addLine("<td><b>" + TranslateUtil.translate(ResourceType.class, view.getResourceType()) + "</b></td>");
					htmlGenerator.addLine("<td>" + StringProcessor.blankIfNull(view.getDescription()) + "</td>");
					htmlGenerator.addLine("<td>" + securityMarking + view.getLink() + "</td>");
					htmlGenerator.addLine("<td>" + StringProcessor.blankIfNull(view.getRestricted()) + "</td>");
					htmlGenerator.addLine("</tr>");
				}
				htmlGenerator.addLine("</table>");
			}

			htmlGenerator.addRuleLine();
			htmlGenerator.addSpace();
			htmlGenerator.addLine("<span class='pageBreak'></span>");
		}

	}

}
