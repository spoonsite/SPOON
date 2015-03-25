/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.usecase;

import au.com.bytecode.opencsv.CSVWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.openstorefront.service.transfermodel.ComponentAll;
import edu.usu.sdl.openstorefront.sort.AttributeTypeViewComparator;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttributePk;
import edu.usu.sdl.openstorefront.storage.model.ComponentContact;
import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.storage.model.ComponentResource;
import edu.usu.sdl.openstorefront.storage.model.ContactType;
import edu.usu.sdl.openstorefront.storage.model.MediaType;
import edu.usu.sdl.openstorefront.storage.model.ResourceType;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.StringProcessor;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeCodeView;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeTypeView;
import edu.usu.sdl.openstorefront.web.tool.OldAsset;
import edu.usu.sdl.openstorefront.web.tool.OldAssetCategory;
import edu.usu.sdl.openstorefront.web.tool.OldDataWrapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class DataConvertUseCase
{

	private static final int MAX_SEARCH_DESCRIPTION = 300;

	@Test
	public void testConvert() throws JsonProcessingException, IOException
	{

		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();
		OldDataWrapper oldDataWrapper = objectMapper.readValue(new File("C:\\development\\storefront\\source\\old\\old_data\\new-data-all.json"), new TypeReference<OldDataWrapper>()
		{
		});

		List<OldAsset> assets = oldDataWrapper.getData();

		List<ComponentAll> newAssetMin = processData(assets, false);
		newAssetMin.forEach(asset -> {
			String filename = asset.getComponent().getName().replace(" ", "_");
			if (filename.length() > 15) {
				filename = filename.substring(0, 15);
			}
			filename = filename + "_" + asset.getComponent().getComponentId();
			try {
				objectMapper.writeValue(new File("C:\\development\\storefront\\source\\master\\openstorefront\\server\\components\\" + filename + ".json"), asset);
			} catch (IOException ex) {
				Logger.getLogger(DataConvertUseCase.class.getName()).log(Level.SEVERE, null, ex);
			}

		});

//		List<ComponentDetailView> newAssetComplete = processData(assets, true);
		//System.out.println(objectMapper.writeValueAsString(newAssetMin));
//		objectMapper.writeValue(new File("c:/development/storefront/data/components-min.json"), newAssetMin);
//		objectMapper.writeValue(new File("c:/development/storefront/data/components-full.json"), newAssetComplete);
//		objectMapper.writeValue(new File("c:/development/storefront/data/searchResults.json"), mapSearchResults(newAssetComplete));
	}

	private List<ComponentAll> processData(List<OldAsset> assets, boolean generateData)
	{

		Set<String> metaTypeToSkip = new HashSet<>();
		metaTypeToSkip.add("Government Point of Contact Email Address");
		metaTypeToSkip.add("Government Point of Contact Name");
		metaTypeToSkip.add("Government Point of Contact Phone");
		metaTypeToSkip.add("Government Point of Contact Organization");
		metaTypeToSkip.add("Code Location URL");
		metaTypeToSkip.add("Product Homepage");
		metaTypeToSkip.add("DI2E Framework Evaluation Report URL");

		//Dummy data
		List<String> peopleNames = Arrays.asList("Cathy TEST", "Jack TEST", "Colby TEST", "Abby TEST", "Dawson TEST");
		List<String> userTypes = Arrays.asList("End User", "Developer", "Project Manager");

		//questions
		Map<String, List<String>> questionMap = new HashMap<>();
		questionMap.put("Which version supports y-docs 1.5+? (SAMPLE)", Arrays.asList("Version 3.1 added support.(SAMPLE)", "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)"));
		questionMap.put("Which os platforms does this support? (SAMPLE)", Arrays.asList("CentOS 6.5 and MorphOS(SAMPLE)", "'I think they also have Windows and ReactOS support. (SAMPLE)"));
		questionMap.put("Does this support the 2.0 specs? (SAMPLE)", Arrays.asList("No,  they planned to add support next Version(SAMPLE)", "Update: they backport support to version 1.6(SAMPLE)"));
		questionMap.put("Are samples included? (SAMPLE)", Arrays.asList("They are included in a separate download.(SAMPLE)"));
		questionMap.put("Are there alternate licenses? (SAMPLE)", Arrays.asList("You can ask their support team for a custom commerical license that should cover you needs.(SAMPLE)", "We've try to get an alternate license and we've been waiting for over 6 months for thier legal department.(SAMPLE)"));

		List<Date> questionDates = Arrays.asList(TimeUtil.fromString("2014-1-03T10:15:30.00Z"),
				TimeUtil.fromString("2012-2-04T11:15:30.00Z"),
				TimeUtil.fromString("2013-10-13T12:15:30.00Z"),
				TimeUtil.fromString("2013-3-03T1:15:30.00Z"),
				TimeUtil.fromString("2013-4-30T2:15:30.00Z"));

		List<Date> answerDates = Arrays.asList(TimeUtil.fromString("2014-2-03T10:15:30.00Z"),
				TimeUtil.fromString("2014-2-04T11:15:30.00Z"),
				TimeUtil.fromString("2014-5-13T12:15:30.00Z"),
				TimeUtil.fromString("2014-3-03T1:15:30.00Z"),
				TimeUtil.fromString("2014-4-30T2:15:30.00Z"));

		Map<String, String> metadataMap = new HashMap<>();
		metadataMap.put("Extra Metadata (SAMPLE)", "Unfiltered");
		metadataMap.put("Support Common Interface 2.1 (SAMPLE)", "No");
		metadataMap.put("Provides Web Hooks via RPC service(SAMPLE)", "Yes");
		metadataMap.put("Common Uses (SAMPLE)", "UDOP, Information Sharing, Research");
		metadataMap.put("Available to public (SAMPLE)", "YES");

//		Map<String, String> attriubuteMap = new HashMap<>();
//		attriubuteMap.put("License Restrictions (SAMPLE)", "Per CPU");
//		attriubuteMap.put("Funded (SAMPLE)", "Yes");
//		attriubuteMap.put("Network(s) Fielded (SAMPLE)", "NIPR, SIPR");
//		attriubuteMap.put("Accreditation Status (SAMPLE)", "ATO");
//		attriubuteMap.put("License Type (SAMPLE)", "FOSS");
//		attriubuteMap.put("Protection/ Impact Level (SAMPLE)", "DoD MAC Level");
//		attriubuteMap.put("Lifecycle Stage (SAMPLE)", "Development");
		Map<String, String> reviewMap = new HashMap<>();
		reviewMap.put("Good Product (SAMPLE)", "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.");
		reviewMap.put("Just what I was looking for (SAMPLE)", "This was perfect it solved our issues and provided tools for things we didn't even anticipate.");
		reviewMap.put("Great but missing features (SAMPLE)", "This is a great product however, it's missing what I think are critical features.  Hopefully, they are working on it for future updates.");
		reviewMap.put("Confused (SAMPLE)", "This wasn't quite what I thought it was.");
		reviewMap.put("Hassle (SAMPLE)", "I had issues trying to obtain the component and once I got it is very to difficult to install.");

		List<String> pros = new ArrayList<>();
		pros.add("Well tested");
		pros.add("Active Development");
		pros.add("Reliable");
		pros.add("Open Source");
		pros.add("Compact");
		pros.add("Well Tested");

		List<String> cons = new ArrayList<>();
		cons.add("Poorly Tested");
		cons.add("Security Concerns");
		cons.add("Difficult Installation");
		cons.add("Poor Documentation");
		cons.add("Bulky");
		cons.add("Legacy system");

		List<String> organizations = new ArrayList<>();
		organizations.add("NGA");
		organizations.add("NSA");
		organizations.add("DCGS Army");
		organizations.add("DCGS Navy");
		organizations.add("Private Company");

		List<String> usedTimeCode = new ArrayList<>();
		usedTimeCode.add("< 1 month'");
		usedTimeCode.add("< 3 Months");
		usedTimeCode.add("< 6 Months");
		usedTimeCode.add("< 1 year");
		usedTimeCode.add("< 3 years");
		usedTimeCode.add("> 3 years");

		Map<String, String> externalDependancyMap = new HashMap<>();
		externalDependancyMap.put("Ruby", "Version 2.0+");
		externalDependancyMap.put("Erlang", "Version 3.0");
		externalDependancyMap.put("Linux", "Tested on CentOS 5 and Ubuntu Server 11.0");
		externalDependancyMap.put("Windows", "Version 8.1");
		externalDependancyMap.put("Tomcat", "Version 7 or 8");

		List<String> evalSections = new ArrayList<>();
		evalSections.add("Discoverable");
		evalSections.add("Accessible");
		evalSections.add("Documentation");
		evalSections.add("Deployable");
		evalSections.add("Usable");
		evalSections.add("Error Handling");
		evalSections.add("Integrable");
		evalSections.add("I/O Validation");
		evalSections.add("Testing");
		evalSections.add("Monitoring");
		evalSections.add("Performance");
		evalSections.add("Scalability");
		evalSections.add("Security");
		evalSections.add("Maintainability");
		evalSections.add("Community");
		evalSections.add("Change Management");
		evalSections.add("CA");
		evalSections.add("Licensing");
		evalSections.add("Roadmap");
		evalSections.add("Willingness");
		evalSections.add("Architecture Alignment");

		List<String> evalLevelStatus = new ArrayList<>();
		evalLevelStatus.add("P");
		evalLevelStatus.add("H");

		List<String> tags = new ArrayList<>();
		tags.add("Mapping");
		tags.add("Visualization");
		tags.add("Reference");
		tags.add("Data Exchange");
		tags.add("Communication");
		tags.add("UDOP");
		tags.add("Charting");
		tags.add("Testing");
		tags.add("Access");

		AttributeImport attributeImport = new AttributeImport();
		Map<String, AttributeTypeView> attributeMap = attributeImport.loadAttributeMap();
		ComponentAttribute idamAttribute = new ComponentAttribute();
		ComponentAttributePk componentAttributePk = new ComponentAttributePk();
		componentAttributePk.setAttributeType("DI2E-SVCV4-A");
		componentAttributePk.setAttributeCode("1.2.1");
		idamAttribute.setComponentAttributePk(componentAttributePk);

		Set<String> idamComponents = new HashSet<>();
		idamComponents.add("Central Authentication Service (CAS)");
		idamComponents.add("OpenAM");
		idamComponents.add("DCGS-E Web Service Access Control Technical Profile");

		SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy");

		List<ComponentAll> newAssets = new ArrayList<>();

		String createUser;
		String updateUser;
		for (OldAsset oldAsset : assets) {

			ComponentAll componentAll = new ComponentAll();
			componentAll.setComponent(new Component());
			Component componentDetail = componentAll.getComponent();

			//defaults
			componentDetail.setActiveStatus(BaseEntity.ACTIVE_STATUS);

			//map form old
			componentDetail.setComponentId("" + oldAsset.getId());
			componentDetail.setGuid(oldAsset.getUuid());
			componentDetail.setUpdateDts(oldAsset.getEditedDate());
			if (componentDetail.getUpdateDts() == null) {
				componentDetail.setUpdateDts(TimeUtil.currentDate());
			}

			String description = oldAsset.getDescription().replace("\n", " <br>");
			description = StringProcessor.stripeExtendedChars(description);
			description = StringProcessor.createHrefUrls(description);
			componentDetail.setDescription(description);

			componentDetail.setApprovedDts(oldAsset.getApprovalDate());
			componentDetail.setCreateDts(oldAsset.getCreateDate());
			if (componentDetail.getCreateDts() == null) {
				componentDetail.setCreateDts(componentDetail.getUpdateDts());
			}
			componentDetail.setVersion(oldAsset.getVersionName());

			createUser = StringUtils.isNotBlank(oldAsset.getCreatedBy().getUsername()) ? oldAsset.getCreatedBy().getUsername() : OpenStorefrontConstant.SYSTEM_ADMIN_USER;
			updateUser = StringUtils.isNotBlank(oldAsset.getEditedBy().getUsername()) ? oldAsset.getEditedBy().getUsername() : OpenStorefrontConstant.SYSTEM_ADMIN_USER;

			if ("Approved".equalsIgnoreCase(oldAsset.getApprovalStatus())) {
				componentDetail.setApprovalState(OpenStorefrontConstant.ComponentApprovalStatus.A.name());
				if (componentDetail.getApprovedDts() == null) {
					componentDetail.setApprovedDts(TimeUtil.currentDate());
					componentDetail.setApprovedUser(createUser);
				}
			} else {
				componentDetail.setApprovalState(OpenStorefrontConstant.ComponentApprovalStatus.P.name());
			}

			componentDetail.setCreateUser(createUser);
			componentDetail.setUpdateUser(updateUser);
			componentDetail.setOrganization(oldAsset.getOrganization());
			if (StringUtils.isBlank(oldAsset.getOrganization())) {
				componentDetail.setOrganization(OpenStorefrontConstant.NOT_AVAILABLE);
			}

			componentDetail.setName(oldAsset.getTitle());

			if (idamComponents.contains(componentDetail.getName())) {
				idamAttribute.getComponentAttributePk().setComponentId(componentDetail.getComponentId());
				idamAttribute.setComponentId(componentDetail.getComponentId());
				componentAll.getAttributes().add(idamAttribute);
			}

			if (StringUtils.isNotBlank(oldAsset.getReleaseDate())) {
				componentDetail.setReleaseDate(sdfDate.parse(oldAsset.getReleaseDate(), new ParsePosition(0)));
			}
			componentDetail.setLastActivityDts(oldAsset.getLastActivity().getActivitiyDate());
			if (componentDetail.getLastActivityDts() == null) {
				componentDetail.setLastActivityDts(TimeUtil.currentDate());
			}

			oldAsset.getTechPocs().forEach(poc -> {

				ComponentContact contact = new ComponentContact();
				contact.setActiveStatus(ComponentContact.ACTIVE_STATUS);
				contact.setComponentId(componentDetail.getComponentId());
				contact.setContactType(ContactType.TECHINCAL);
				contact.setFirstName(poc);
				//contact.setEmail();
				//contact.setPhone("555-555-5555");
				contact.setOrganization(OpenStorefrontConstant.NOT_AVAILABLE);
				contact.setCreateUser(componentDetail.getCreateUser());
				contact.setUpdateUser(componentDetail.getUpdateUser());
				contact.setCreateDts(componentDetail.getCreateDts());
				contact.setUpdateDts(componentDetail.getUpdateDts());
				componentAll.getContacts().add(contact);
			});

			//Government Contact
			oldAsset.getCustomFields().forEach(field -> {
				if ("Government Point of Contact Name".equalsIgnoreCase(field.getName())) {
					ComponentContact contact = new ComponentContact();
					contact.setContactType(ContactType.GOVERNMENT);
					contact.setFirstName(field.getValue());
					if (StringUtils.isBlank(contact.getFirstName())) {
						contact.setFirstName(OpenStorefrontConstant.NOT_AVAILABLE);
					}
//					contact.setEmail("sample_email@tt.com");
//					contact.setPhone("555-555-5555");
//					contact.setOrganization("sample organization");
					contact.setOrganization(OpenStorefrontConstant.NOT_AVAILABLE);
					contact.setCreateUser(componentDetail.getCreateUser());
					contact.setUpdateUser(componentDetail.getUpdateUser());
					contact.setCreateDts(componentDetail.getCreateDts());
					contact.setUpdateDts(componentDetail.getUpdateDts());
					componentAll.getContacts().add(contact);
				}
			});

			//media
			oldAsset.getScreenshots().forEach(screen -> {
				componentAll.getMedia().add(createMediaFromUrl(componentDetail, screen.getLargeImageUrl()));
			});
			if (StringUtils.isNotBlank(oldAsset.getImageLargeUrl())) {
				componentAll.getMedia().add(createMediaFromUrl(componentDetail, oldAsset.getImageLargeUrl()));
			}

			//resources
			oldAsset.getDocUrls().forEach(doc -> {
				ComponentResource componentResource = new ComponentResource();
				componentResource.setLink(StringProcessor.createHrefUrls(doc.getUrl(), true));
				componentResource.setResourceType(ResourceType.DOCUMENT);
				componentResource.setComponentId(componentDetail.getComponentId());
				componentResource.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
				componentResource.setCreateUser(componentDetail.getCreateUser());
				componentResource.setUpdateUser(componentDetail.getUpdateUser());
				componentResource.setCreateDts(componentDetail.getCreateDts());
				componentResource.setUpdateDts(componentDetail.getUpdateDts());

				componentAll.getResources().add(componentResource);
			});

//			if (StringUtils.isNotBlank(oldAsset.getInstallUrl())) {
//				ComponentResourceView componentResource = new ComponentResourceView();
//				componentResource.setName("Install Url");
//				componentResource.setLink(StringProcessor.createHrefUrls(oldAsset.getInstallUrl(), true));
//				componentResource.setResourceType("Document");
//				componentAll.getResources().add(componentResource);
//			}
			oldAsset.getCustomFields().forEach(field -> {
				if (StringUtils.isNotBlank(field.getValue())) {
					String type = null;
					if ("Code Location URL".equals(field.getName())) {
						type = ResourceType.CODE;
					} else if ("Product Homepage".equals(field.getName())) {
						type = ResourceType.HOME_PAGE;
					} else if ("DI2E Framework Evaluation Report URL".equals(field.getName())) {
						type = ResourceType.DI2E_EVAL_REPORT;
					}
					if (type != null) {
						ComponentResource componentResource = new ComponentResource();
						componentResource.setLink(StringProcessor.createHrefUrls(field.getValue(), true));
						componentResource.setResourceType(type);
						componentResource.setComponentId(componentDetail.getComponentId());
						componentResource.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
						componentResource.setCreateUser(componentDetail.getCreateUser());
						componentResource.setUpdateUser(componentDetail.getUpdateUser());
						componentResource.setCreateDts(componentDetail.getCreateDts());
						componentResource.setUpdateDts(componentDetail.getUpdateDts());
						componentAll.getResources().add(componentResource);
					}
				}
			});

			Map<String, String> typeXRefMap = new HashMap<>();
			typeXRefMap.put("Application", "APP");
			typeXRefMap.put("Development Tools", "DEVTOOL");
			typeXRefMap.put("Documentation", "DOC");
			typeXRefMap.put("Environment", "ENV");
			typeXRefMap.put("Widget", "WIDGET");
			typeXRefMap.put("Other", "OTH");
			typeXRefMap.put("OZONE App", "OZONE");
			typeXRefMap.put("Service Endpoint", "SERVICE");
			typeXRefMap.put("Software", "SOFTWARE");
			typeXRefMap.put("Standards, Specifications, and APIs", "SPEC");
			typeXRefMap.put("Web App", "WEBAPP");

			//metadata/attributes
			ComponentAttribute attribute = mapAttribute(componentDetail, AttributeType.TYPE, typeXRefMap.get(oldAsset.getTypes().getTitle()));
			componentAll.getAttributes().add(attribute);

			String oldStateLabel = oldAsset.getState().getTitle();
			String stateLabel = null;

			Set<String> stateCodeSet = new HashSet<>();
			stateCodeSet.add(AttributeCode.DI2ELEVEL_NA);
			stateCodeSet.add(AttributeCode.DI2ELEVEL_LEVEL0);
			stateCodeSet.add(AttributeCode.DI2ELEVEL_LEVEL1);
			stateCodeSet.add(AttributeCode.DI2ELEVEL_LEVEL2);
			stateCodeSet.add(AttributeCode.DI2ELEVEL_LEVEL3);

			if ("NA - No Eval Planned".equals(oldStateLabel)) {
				stateLabel = AttributeCode.DI2ELEVEL_NA;
				stateCodeSet.remove(AttributeCode.DI2ELEVEL_NA);
				stateCodeSet.remove(AttributeCode.DI2ELEVEL_LEVEL0);
				stateCodeSet.remove(AttributeCode.DI2ELEVEL_LEVEL1);
				stateCodeSet.remove(AttributeCode.DI2ELEVEL_LEVEL2);
				stateCodeSet.remove(AttributeCode.DI2ELEVEL_LEVEL3);
			} else if ("Level 0 - Not assessed".equals(oldStateLabel)) {
				stateLabel = AttributeCode.DI2ELEVEL_LEVEL0;
				stateCodeSet.remove(AttributeCode.DI2ELEVEL_LEVEL0);
				stateCodeSet.remove(AttributeCode.DI2ELEVEL_NA);
			} else if ("Level 1 - Checklist Complete".equals(oldStateLabel)) {
				stateLabel = AttributeCode.DI2ELEVEL_LEVEL1;
				stateCodeSet.remove(AttributeCode.DI2ELEVEL_LEVEL1);
				stateCodeSet.remove(AttributeCode.DI2ELEVEL_NA);
			}

			attribute = mapAttribute(componentDetail, AttributeType.DI2ELEVEL, stateLabel);
			componentAll.getAttributes().add(attribute);

			for (OldAssetCategory category : oldAsset.getCategories()) {
				ComponentAttribute catAttribute = new ComponentAttribute();
				ComponentAttributePk attributePk = new ComponentAttributePk();
				attributePk.setAttributeType(AttributeType.DI2E_SVCV4);
				attributePk.setComponentId(componentDetail.getComponentId());
				catAttribute.setComponentAttributePk(attributePk);
				catAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
				catAttribute.setCreateUser(componentDetail.getCreateUser());
				catAttribute.setUpdateUser(componentDetail.getUpdateUser());
				catAttribute.setCreateDts(componentDetail.getCreateDts());
				catAttribute.setUpdateDts(componentDetail.getUpdateDts());
				catAttribute.setComponentId(componentDetail.getComponentId());

				AttributeTypeView attributeTypeView = attributeMap.get(AttributeType.DI2E_SVCV4);

				for (AttributeCodeView attributeCodeView : attributeTypeView.getCodes()) {
					String label = attributeCodeView.getLabel().substring(attributeCodeView.getLabel().indexOf(" ") + 1, attributeCodeView.getLabel().length());

					if (label.equalsIgnoreCase(category.getTitle().trim())) {
						catAttribute.getComponentAttributePk().setAttributeCode(attributeCodeView.getCode());
						break;
					}
				}
				if (StringUtils.isNotBlank(catAttribute.getComponentAttributePk().getAttributeCode())) {
					componentAll.getAttributes().add(catAttribute);
				}
			}

			oldAsset.getCustomFields().forEach(field -> {
				if (StringUtils.isNotBlank(field.getValue())
						&& !metaTypeToSkip.contains(field.getName())) {
					String originTypeDesc = field.getName();
					String originCodeLabel = field.getValue();

					String newType = null;
					String newCode = null;

					//Skip N/A
					if ("N/A".equals(originCodeLabel.trim()) == false) {
						if ("Commercial Export Approved via EAR".equals(originTypeDesc)) {
							newType = "CEEAR";
							if ("Yes".equalsIgnoreCase(originCodeLabel)) {
								newCode = "Y";
							} else {
								newCode = "N";
							}
						} else if ("ITAR Export Approved".equals(originTypeDesc)) {
							newType = "ITAR";
							if ("Yes".equalsIgnoreCase(originCodeLabel)) {
								newCode = "Y";
							} else {
								newCode = "N";
							}
						} else if ("Software License Method".equals(originTypeDesc)) {
							newType = "LICTYPE";
							if ("Open Source".startsWith(originCodeLabel)) {
								newCode = "OPENSRC";
							} else if ("Enterprise".equalsIgnoreCase(originCodeLabel)) {
								newCode = "ENT";
							} else if ("Government Unlimited Rights".startsWith(originCodeLabel)) {
								newCode = "GOVUNL";
							}
						} else if ("Software License Type".equals(originTypeDesc)) {
							newType = "LICCLASS";
							if ("Open Source".startsWith(originCodeLabel)) {
								newCode = "FOSS";
							} else if ("FOSS".equalsIgnoreCase(originCodeLabel)) {
								newCode = "FOSS";
							} else if ("GOSS".startsWith(originCodeLabel)) {
								newCode = "GOSS";
							} else if ("COTS".equalsIgnoreCase(originCodeLabel)) {
								newCode = "COTS";
							} else if ("GOTS".equalsIgnoreCase(originCodeLabel)) {
								newCode = "GOTS";
							} else if ("OpenSource".equalsIgnoreCase(originCodeLabel)) {
								newCode = "FOSS";
							}
						} else if ("Life Cycle Stage".equals(originTypeDesc)) {
							newType = "LIFECYCSTG";
							if ("Development".equalsIgnoreCase(originCodeLabel)) {
								newCode = "DEV";
							} else if ("Pilot".equalsIgnoreCase(originCodeLabel)) {
								newCode = "PILOT";
							} else if ("Operations".equalsIgnoreCase(originCodeLabel)) {
								newCode = "OPR";
							}
						} else if ("OWF Compatible Widget (Y/N)".equals(originTypeDesc)) {
							newType = "OWFCOMP";
							if ("Yes".equalsIgnoreCase(originCodeLabel)) {
								newCode = "Y";
							} else {
								newCode = "N";
							}
						}
					}
					if (newType != null && newCode != null) {
						ComponentAttribute metaAttribute = mapAttribute(componentDetail, newType, newCode);
						componentAll.getAttributes().add(metaAttribute);
					}

				}
			});

			newAssets.add(componentAll);
		}
		return newAssets;
	}

	private ComponentAttribute mapAttribute(Component component, String attributeType, String code)
	{
		ComponentAttribute attribute = new ComponentAttribute();
		ComponentAttributePk attributePk = new ComponentAttributePk();
		attributePk.setAttributeType(attributeType);
		attributePk.setAttributeCode(code.trim());
		attributePk.setComponentId(component.getComponentId());
		attribute.setComponentAttributePk(attributePk);
		attribute.setComponentId(component.getComponentId());
		attribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		attribute.setCreateUser(component.getCreateUser());
		attribute.setUpdateUser(component.getUpdateUser());
		attribute.setCreateDts(component.getCreateDts());
		attribute.setUpdateDts(component.getUpdateDts());
		return attribute;
	}

	private ComponentMedia createMediaFromUrl(Component component, String url)
	{
		ComponentMedia media = new ComponentMedia();
		String resource = StringProcessor.getResourceNameFromUrl(url);
		media.setComponentId(component.getComponentId());
		media.setFileName(resource);
		media.setMediaTypeCode(MediaType.IMAGE);
		media.setMimeType("image/" + resource.substring(resource.lastIndexOf(".") + 1, resource.length()));
		media.setActiveStatus(ComponentMedia.ACTIVE_STATUS);
		media.setCreateUser(component.getCreateUser());
		media.setUpdateUser(component.getUpdateUser());
		media.setCreateDts(component.getCreateDts());
		media.setUpdateDts(component.getUpdateDts());
		return media;
	}

//	private List<SearchResult> mapSearchResults(List<ComponentDetailView> details)
//	{
//		List<SearchResult> searchResults = new ArrayList<>();
//		Random random = new Random(System.currentTimeMillis());
//		details.forEach(detail -> {
//
//			SearchResult searchResult = new SearchResult();
//			searchResult.setComponentId(detail.getComponentId());
//			searchResult.setResourceLocation("api/v1/resource/components/" + detail.getComponentId() + "/detail");
//			searchResult.setName(detail.getName());
//
//			//Strip the <br> may other
//			String description = detail.getDescription().replace("<br>", "");
//			description = StringProcessor.eclipseString(description, MAX_SEARCH_DESCRIPTION);
//			searchResult.setDescription(description);
//			searchResult.setAverageRating(random.nextInt(6));
//			searchResult.setViews(random.nextInt(200));
//			searchResult.setLastActivityDts(detail.getLastActivityDts());
//			searchResult.setListingType("Component");
//			searchResult.setOrganization(detail.getOrganization());
//			searchResult.setTotalNumberOfReviews(random.nextInt(100));
//			searchResult.setUpdateDts(detail.getUpdateDts());
//
//			//FIXME:
////			detail.getAttributes().forEach(attrib -> {
////				SearchResultAttribute attribute = new SearchResultAttribute();
////				attribute.setType(attrib.getType());
////				attribute.setCode(attrib.getCode());
////				searchResult.getAttributes().add(attribute);
////
////			});
//			searchResult.getTags().addAll(detail.getTags());
//
//			searchResults.add(searchResult);
//		});
//
//		//Add article
//		SearchResult searchResult = new SearchResult();
//		searchResult.setName("IdAM");
//		searchResult.setDescription("Identity and Access Management Article.....");
//
//		String type = "DI2E-SVCV4-A";
//		String code = "1.2.1";
//
//		searchResult.setArticleAttributeCode(type);
//		searchResult.setArticleAttributeType(code);
//		searchResult.setLastActivityDts(new Date(System.currentTimeMillis()));
//		searchResult.setListingType("Article");
//		searchResult.setOrganization("PMO");
//		searchResult.setUpdateDts(new Date(System.currentTimeMillis()));
//
//		SearchResultAttribute attribute = new SearchResultAttribute();
//		attribute.setType(type);
//		attribute.setCode(code);
//		searchResult.getAttributes().add(attribute);
//		searchResult.setResourceLocation("api/v1/resource/attributes/" + type + "/attributeCode/" + code + "/article");
//		searchResults.add(searchResult);
//
//		return searchResults;
//	}
	@Test
	public void testAttribute() throws JsonProcessingException, IOException
	{
		//RestListResponse<AttributeTypeView> response = new RestListResponse<>();

		AttributeImport attributeImport = new AttributeImport();
//		response.setData(attributeImport.loadAttributes());
//		response.setResults(response.getData().size());
//		response.setTotalResults(response.getData().size());
//
//		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();
//		objectMapper.writeValue(new File("c:/development/storefront/data/attributes.json"), response);

		List<AttributeTypeView> attributeTypeViews = attributeImport.loadAttributes();
		Collections.sort(attributeTypeViews, new AttributeTypeViewComparator<>());
		try (CSVWriter out = new CSVWriter(new OutputStreamWriter(new FileOutputStream("C:\\var\\openstorefront\\import\\allattributes.csv")));) {
			//write header
			List<String> data = new ArrayList<>();
			data.add("Attribute Type");
			data.add("Type Description");
			data.add("Architecture flag");
			data.add("Visible");
			data.add("Important");
			data.add("Required");
			data.add("Code");
			data.add("Code Label");
			data.add("Code Description");
			data.add("External Link");

			out.writeNext(data.toArray(new String[0]));
			for (AttributeTypeView attributeTypeView : attributeTypeViews) {
				for (AttributeCodeView attributeCodeView : attributeTypeView.getCodes()) {
					data = new ArrayList<>();
					data.add(attributeTypeView.getAttributeType());
					data.add(attributeTypeView.getDescription());
					data.add(Boolean.toString(attributeTypeView.getArchitectureFlg()));
					data.add(Boolean.toString(attributeTypeView.getVisibleFlg()));
					data.add(Boolean.toString(attributeTypeView.getImportantFlg()));
					data.add(Boolean.toString(attributeTypeView.getRequiredFlg()));
					data.add(attributeCodeView.getCode());
					data.add(attributeCodeView.getLabel());
					data.add(attributeCodeView.getDescription());
					data.add(attributeCodeView.getFullTextLink());
					out.writeNext(data.toArray(new String[0]));
				}
			}
		}

	}
//
////	@Test
////	public void formatInput()
////	{
////		ObjectMapper mapper = defaultObjectMapper();
////
////		try
////		{
////			JsonNode rootNode = mapper.readTree(new File("C:\\development\\storefront\\source\\old\\old_data\\new-asset-data-all.json"));
////
////			Object jsonString = mapper.readValue(rootNode.toString(), Object.class);
////			String formattedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonString);
////			Files.write(Paths.get("C:\\development\\storefront\\source\\old\\old_data\\new-asset-data-all2.json"), formattedJson.getBytes(), StandardOpenOption.CREATE);
////
////		} catch (IOException ex)
////		{
////			throw new OpenStorefrontRuntimeException(ex);
////		}
////	}
////	@Test
////	public void readSv4Export()
////	{
////		try
////		{
////			CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream("\\var\\openstorefront\\import\\svcv-4_export.csv")));
////			List<String[]> lines = reader.readAll();
////
////			lines.forEach(line ->{
////
////				System.out.println(Arrays.toString(line));
////			});
////
////		} catch (IOException ex)
////		{
////			Logger.getLogger(DataConvertUseCase.class.getName()).log(Level.SEVERE, null, ex);
////		}
////
////	}
}
