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
package edu.usu.sdl.openstorefront.test;

import au.com.bytecode.opencsv.CSVWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import edu.usu.sdl.openstorefront.service.io.AttributeImport;
import edu.usu.sdl.openstorefront.sort.AttributeTypeViewComparator;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeCodeView;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeTypeView;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class DataConvertTest
{

//	private static final int MAX_SEARCH_DESCRIPTION = 300;
//
//	@Test
//	public void testConvert() throws JsonProcessingException, IOException
//	{
//
//		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();
//		OldDataWrapper oldDataWrapper = objectMapper.readValue(new File("C:\\development\\storefront\\source\\old\\old_data\\new-data-all.json"), new TypeReference<OldDataWrapper>()
//		{
//		});
//
//		List<OldAsset> assets = oldDataWrapper.getData();
//
//		List<ComponentDetailView> newAssetMin = processData(assets, false);
//		List<ComponentDetailView> newAssetComplete = processData(assets, true);
//
//		//System.out.println(objectMapper.writeValueAsString(newAssetMin));
//		objectMapper.writeValue(new File("c:/development/storefront/data/components-min.json"), newAssetMin);
//		objectMapper.writeValue(new File("c:/development/storefront/data/components-full.json"), newAssetComplete);
//		objectMapper.writeValue(new File("c:/development/storefront/data/searchResults.json"), mapSearchResults(newAssetComplete));
//	}
//
//	private List<ComponentDetailView> processData(List<OldAsset> assets, boolean generateData)
//	{
//
//		Set<String> metaTypeToSkip = new HashSet<>();
//		metaTypeToSkip.add("Government Point of Contact Email Address");
//		metaTypeToSkip.add("Government Point of Contact Name");
//		metaTypeToSkip.add("Government Point of Contact Phone");
//		metaTypeToSkip.add("Government Point of Contact Organization");
//		metaTypeToSkip.add("Code Location URL");
//		metaTypeToSkip.add("Product Homepage");
//		metaTypeToSkip.add("DI2E Framework Evaluation Report URL");
//
//		//Dummy data
//		List<String> peopleNames = Arrays.asList("Cathy TEST", "Jack TEST", "Colby TEST", "Abby TEST", "Dawson TEST");
//		List<String> userTypes = Arrays.asList("End User", "Developer", "Project Manager");
//
//		//questions
//		Map<String, List<String>> questionMap = new HashMap<>();
//		questionMap.put("Which version supports y-docs 1.5+? (SAMPLE)", Arrays.asList("Version 3.1 added support.(SAMPLE)", "They depreciated support in version 4.0 since it was a rarely used feature.(SAMPLE)"));
//		questionMap.put("Which os platforms does this support? (SAMPLE)", Arrays.asList("CentOS 6.5 and MorphOS(SAMPLE)", "'I think they also have Windows and ReactOS support. (SAMPLE)"));
//		questionMap.put("Does this support the 2.0 specs? (SAMPLE)", Arrays.asList("No,  they planned to add support next Version(SAMPLE)", "Update: they backport support to version 1.6(SAMPLE)"));
//		questionMap.put("Are samples included? (SAMPLE)", Arrays.asList("They are included in a separate download.(SAMPLE)"));
//		questionMap.put("Are there alternate licenses? (SAMPLE)", Arrays.asList("You can ask their support team for a custom commerical license that should cover you needs.(SAMPLE)", "We've try to get an alternate license and we've been waiting for over 6 months for thier legal department.(SAMPLE)"));
//
//		List<Date> questionDates = Arrays.asList(TimeUtil.fromString("2014-1-03T10:15:30.00Z"),
//				TimeUtil.fromString("2012-2-04T11:15:30.00Z"),
//				TimeUtil.fromString("2013-10-13T12:15:30.00Z"),
//				TimeUtil.fromString("2013-3-03T1:15:30.00Z"),
//				TimeUtil.fromString("2013-4-30T2:15:30.00Z"));
//
//		List<Date> answerDates = Arrays.asList(TimeUtil.fromString("2014-2-03T10:15:30.00Z"),
//				TimeUtil.fromString("2014-2-04T11:15:30.00Z"),
//				TimeUtil.fromString("2014-5-13T12:15:30.00Z"),
//				TimeUtil.fromString("2014-3-03T1:15:30.00Z"),
//				TimeUtil.fromString("2014-4-30T2:15:30.00Z"));
//
//		Map<String, String> metadataMap = new HashMap<>();
//		metadataMap.put("Extra Metadata (SAMPLE)", "Unfiltered");
//		metadataMap.put("Support Common Interface 2.1 (SAMPLE)", "No");
//		metadataMap.put("Provides Web Hooks via RPC service(SAMPLE)", "Yes");
//		metadataMap.put("Common Uses (SAMPLE)", "UDOP, Information Sharing, Research");
//		metadataMap.put("Available to public (SAMPLE)", "YES");
//
////		Map<String, String> attriubuteMap = new HashMap<>();
////		attriubuteMap.put("License Restrictions (SAMPLE)", "Per CPU");
////		attriubuteMap.put("Funded (SAMPLE)", "Yes");
////		attriubuteMap.put("Network(s) Fielded (SAMPLE)", "NIPR, SIPR");
////		attriubuteMap.put("Accreditation Status (SAMPLE)", "ATO");
////		attriubuteMap.put("License Type (SAMPLE)", "FOSS");
////		attriubuteMap.put("Protection/ Impact Level (SAMPLE)", "DoD MAC Level");
////		attriubuteMap.put("Lifecycle Stage (SAMPLE)", "Development");
//		Map<String, String> reviewMap = new HashMap<>();
//		reviewMap.put("Good Product (SAMPLE)", "It's a good product.  The features are nice and performed well.  It quite configurable without a lot of setup and it worked out of the box.");
//		reviewMap.put("Just what I was looking for (SAMPLE)", "This was perfect it solved our issues and provided tools for things we didn't even anticipate.");
//		reviewMap.put("Great but missing features (SAMPLE)", "This is a great product however, it's missing what I think are critical features.  Hopefully, they are working on it for future updates.");
//		reviewMap.put("Confused (SAMPLE)", "This wasn't quite what I thought it was.");
//		reviewMap.put("Hassle (SAMPLE)", "I had issues trying to obtain the component and once I got it is very to difficult to install.");
//
//		List<String> pros = new ArrayList<>();
//		pros.add("Well tested");
//		pros.add("Active Development");
//		pros.add("Reliable");
//		pros.add("Open Source");
//		pros.add("Compact");
//		pros.add("Well Tested");
//
//		List<String> cons = new ArrayList<>();
//		cons.add("Poorly Tested");
//		cons.add("Security Concerns");
//		cons.add("Difficult Installation");
//		cons.add("Poor Documentation");
//		cons.add("Bulky");
//		cons.add("Legacy system");
//
//		List<String> organizations = new ArrayList<>();
//		organizations.add("NGA");
//		organizations.add("NSA");
//		organizations.add("DCGS Army");
//		organizations.add("DCGS Navy");
//		organizations.add("Private Company");
//
//		List<String> usedTimeCode = new ArrayList<>();
//		usedTimeCode.add("< 1 month'");
//		usedTimeCode.add("< 3 Months");
//		usedTimeCode.add("< 6 Months");
//		usedTimeCode.add("< 1 year");
//		usedTimeCode.add("< 3 years");
//		usedTimeCode.add("> 3 years");
//
//		Map<String, String> externalDependancyMap = new HashMap<>();
//		externalDependancyMap.put("Ruby", "Version 2.0+");
//		externalDependancyMap.put("Erlang", "Version 3.0");
//		externalDependancyMap.put("Linux", "Tested on CentOS 5 and Ubuntu Server 11.0");
//		externalDependancyMap.put("Windows", "Version 8.1");
//		externalDependancyMap.put("Tomcat", "Version 7 or 8");
//
//		List<String> evalSections = new ArrayList<>();
//		evalSections.add("Discoverable");
//		evalSections.add("Accessible");
//		evalSections.add("Documentation");
//		evalSections.add("Deployable");
//		evalSections.add("Usable");
//		evalSections.add("Error Handling");
//		evalSections.add("Integrable");
//		evalSections.add("I/O Validation");
//		evalSections.add("Testing");
//		evalSections.add("Monitoring");
//		evalSections.add("Performance");
//		evalSections.add("Scalability");
//		evalSections.add("Security");
//		evalSections.add("Maintainability");
//		evalSections.add("Community");
//		evalSections.add("Change Management");
//		evalSections.add("CA");
//		evalSections.add("Licensing");
//		evalSections.add("Roadmap");
//		evalSections.add("Willingness");
//		evalSections.add("Architecture Alignment");
//
//		List<String> evalLevelStatus = new ArrayList<>();
//		evalLevelStatus.add("P");
//		evalLevelStatus.add("H");
//
//		List<String> tags = new ArrayList<>();
//		tags.add("Mapping");
//		tags.add("Visualization");
//		tags.add("Reference");
//		tags.add("Data Exchange");
//		tags.add("Communication");
//		tags.add("UDOP");
//		tags.add("Charting");
//		tags.add("Testing");
//		tags.add("Access");
//
//		AttributeImport attributeImport = new AttributeImport();
//		Map<String, AttributeTypeView> attributeMap = attributeImport.loadAttributeMap();
//
//		ComponentAttributeView idamAttribute = new ComponentAttributeView();
//
//		ComponentAttribute componentAttribute = new ComponentAttribute();
//		ComponentAttributePk componentAttributePk = new ComponentAttributePk();
//		componentAttributePk.setAttributeType("DI2E-SVCV4-A");
//		componentAttributePk.setAttributeCode("1.2.1");
//		componentAttribute.setComponentAttributePk(componentAttributePk);
//
//		idamAttribute.setComponentAttribute(componentAttribute);
//		idamAttribute.setTypeDescription("DI2E SvcV-4 Alignment");
//		idamAttribute.setCodeDescription("Identity and Access Management");
//		idamAttribute.setImportantFlg(true);
//
//		Set<String> idamComponents = new HashSet<>();
//		idamComponents.add("Central Authentication Service (CAS)");
//		idamComponents.add("OpenAM");
//		idamComponents.add("DCGS-E Web Service Access Control Technical Profile");
//
//		SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy");
//
//		List<ComponentDetailView> newAssets = new ArrayList<>();
//		assets.forEach(oldAsset -> {
//
//			ComponentDetailView componentDetail = new ComponentDetailView();
//			//defaults
//			componentDetail.setActiveStatus(BaseEntity.ACTIVE_STATUS);
//
//			//map form old
//			componentDetail.setComponentId("" + oldAsset.getId());
//			componentDetail.setGuid(oldAsset.getUuid());
//			componentDetail.setUpdateDts(oldAsset.getEditedDate());
//
//			String description = oldAsset.getDescription().replace("\n", " <br>");
//			description = StringProcessor.stripeExtendedChars(description);
//			description = StringProcessor.createHrefUrls(description);
//			componentDetail.setDescription(description);
//
//			componentDetail.setApprovedDate(oldAsset.getApprovalDate());
//			componentDetail.setCreateDts(oldAsset.getCreateDate());
//			componentDetail.setVersion(oldAsset.getVersionName());
//
//			if ("Approved".equalsIgnoreCase(oldAsset.getApprovalStatus())) {
//				componentDetail.setApprovalState("A");
//			} else {
//				componentDetail.setApprovalState("P");
//			}
//
//			componentDetail.setCreateUser(oldAsset.getCreatedBy().getUsername());
//			componentDetail.setUpdateUser(oldAsset.getEditedBy().getUsername());
//			componentDetail.setOrganization(oldAsset.getOrganization());
//			componentDetail.setName(oldAsset.getTitle());
//
//			if (idamComponents.contains(componentDetail.getName())) {
//				componentDetail.getAttributes().add(idamAttribute);
//			}
//
//			if (StringUtils.isNotBlank(oldAsset.getReleaseDate())) {
//				componentDetail.setReleaseDate(sdfDate.parse(oldAsset.getReleaseDate(), new ParsePosition(0)));
//			}
//			componentDetail.setLastActivityDts(oldAsset.getLastActivity().getActivitiyDate());
//
//			oldAsset.getTechPocs().forEach(poc -> {
//				//Need to generate
//				Collections.shuffle(peopleNames);
//				ComponentContactView contact = new ComponentContactView();
//				contact.setPostionDescription("Technical POC");
//				contact.setName(peopleNames.get(0));
//				contact.setEmail("sample_email@test.com");
//				contact.setPhone("555-555-5555");
//				contact.setOrganization("sample organization");
//				componentDetail.getContacts().add(contact);
//			});
//
//			//Government Contact
//			oldAsset.getCustomFields().forEach(field -> {
//				if ("Government Point of Contact Name".equalsIgnoreCase(field.getName())) {
//					Collections.shuffle(peopleNames);
//					ComponentContactView contact = new ComponentContactView();
//					contact.setPostionDescription("Government POC");
//					contact.setName(peopleNames.get(0));
//					contact.setEmail("sample_email@test.com");
//					contact.setPhone("555-555-5555");
//					contact.setOrganization("sample organization");
//					componentDetail.getContacts().add(contact);
//				}
//			});
//
//			//media
//			oldAsset.getScreenshots().forEach(screen -> {
//				componentDetail.getComponentMedia().add(createMediaFromUrl(screen.getLargeImageUrl()));
//			});
//			if (StringUtils.isNotBlank(oldAsset.getImageLargeUrl())) {
//				componentDetail.getComponentMedia().add(createMediaFromUrl(oldAsset.getImageLargeUrl()));
//			}
//
//			//resources
//			oldAsset.getDocUrls().forEach(doc -> {
//				ComponentResourceView componentResource = new ComponentResourceView();
//				componentResource.setName("Documentation");
//				componentResource.setLink(StringProcessor.createHrefUrls(doc.getUrl(), true));
//				componentResource.setResourceType("Document");
//				componentDetail.getResources().add(componentResource);
//			});
//
//			if (StringUtils.isNotBlank(oldAsset.getInstallUrl())) {
//				ComponentResourceView componentResource = new ComponentResourceView();
//				componentResource.setName("Install Url");
//				componentResource.setLink(StringProcessor.createHrefUrls(oldAsset.getInstallUrl(), true));
//				componentResource.setResourceType("Document");
//				componentDetail.getResources().add(componentResource);
//			}
//
//			oldAsset.getCustomFields().forEach(field -> {
//				if (StringUtils.isNotBlank(field.getValue())) {
//					String type = null;
//					if ("Code Location URL".equals(field.getName())) {
//						type = "Code";
//					} else if ("Product Homepage".equals(field.getName())) {
//						type = "Homepage";
//					} else if ("DI2E Framework Evaluation Report URL".equals(field.getName())) {
//						type = "DI2E Framework Evaluation Report URL";
//					}
//					if (type != null) {
//						ComponentResourceView componentResource = new ComponentResourceView();
//						componentResource.setName(field.getName());
//						componentResource.setLink(StringProcessor.createHrefUrls(field.getValue(), true));
//						componentResource.setResourceType(type);
//						componentDetail.getResources().add(componentResource);
//					}
//				}
//			});
//
//			//metadata/attributes
//			ComponentAttributeView attribute = mapAttribute("TYPE", oldAsset.getTypes().getTitle(), attributeMap);
//			attribute.setImportantFlg(true);
//			componentDetail.getAttributes().add(attribute);
//
//			String oldStateLabel = oldAsset.getState().getTitle();
//			String stateLabel = null;
//			if ("NA - No Eval Planned".equals(oldStateLabel)) {
//				stateLabel = "Level 0 - Available for Reuse/Not Evaluated";
//			} else if ("Level 0 - Not assessed".equals(oldStateLabel)) {
//				stateLabel = "Level 0 - Available for Reuse/Not Evaluated";
//			} else if ("Level 1 - Checklist Complete".equals(oldStateLabel)) {
//				stateLabel = "Level 1 - Initial Reuse Analysis";
//			}
//
//			attribute = mapAttribute("DI2ELEVEL", stateLabel, attributeMap);
//			componentDetail.getEvaluation().setCurrentLevelCode(attribute.getComponentAttribute().getComponentAttributePk().getAttributeCode());
//			attribute.setImportantFlg(true);
//			componentDetail.getAttributes().add(attribute);
//
//			oldAsset.getCategories().forEach(category -> {
//
//				ComponentAttributeView catAttribute = new ComponentAttributeView();
//				AttributeTypeView attributeTypeView = attributeMap.get("DI2E-SVCV4-A");
//
//				//FIXME:
////				catAttribute.setType(attributeTypeView.getType());
////				catAttribute.setTypeDescription(attributeTypeView.getDescription());
////				for (AttributeCodeView attributeCodeView : attributeTypeView.getCodes()) {
////					String label = attributeCodeView.getLabel().substring(attributeCodeView.getLabel().indexOf(" ") + 1, attributeCodeView.getLabel().length());
////
////					if (label.equalsIgnoreCase(category.getTitle().trim())) {
////						catAttribute.setCode(attributeCodeView.getCode());
////						catAttribute.setCodeDescription(attributeCodeView.getLabel());
////						catAttribute.setCodeLongDescription(attributeCodeView.getDescription());
////						catAttribute.setImportantFlg(attributeTypeView.getImportantFlg());
////						break;
////					}
////				}
//				componentDetail.getAttributes().add(catAttribute);
//			});
//
//			oldAsset.getCustomFields().forEach(field -> {
//				if (StringUtils.isNotBlank(field.getValue())
//						&& !metaTypeToSkip.contains(field.getName())) {
//					String originTypeDesc = field.getName();
//					String originCodeLabel = field.getValue();
//
//					String newType = null;
//					String newCodeLabel = null;
//
//					//Skip N/A
//					if ("N/A".equals(originCodeLabel.trim()) == false) {
//						if ("Commercial Export Approved via EAR".equals(originTypeDesc)) {
//							newType = "CEEAR";
//							if ("Yes".equalsIgnoreCase(originCodeLabel)) {
//								newCodeLabel = "Yes";
//							} else {
//								newCodeLabel = "No";
//							}
//						} else if ("ITAR Export Approved".equals(originTypeDesc)) {
//							newType = "ITAR";
//							if ("Yes".equalsIgnoreCase(originCodeLabel)) {
//								newCodeLabel = "Yes";
//							} else {
//								newCodeLabel = "No";
//							}
//						} else if ("Software License Method".equals(originTypeDesc)) {
//							newType = "LICTYPE";
//							if ("Open Source".startsWith(originCodeLabel)) {
//								newCodeLabel = "Open Source";
//							} else if ("Enterprise".equalsIgnoreCase(originCodeLabel)) {
//								newCodeLabel = "Enterprise";
//							} else if ("Government Unlimited Rights".startsWith(originCodeLabel)) {
//								newCodeLabel = "Government Unlimited Rights";
//							}
//						} else if ("Software License Type".equals(originTypeDesc)) {
//							newType = "LICCLASS";
//							if ("Open Source".startsWith(originCodeLabel)) {
//								newCodeLabel = "FOSS";
//							} else if ("FOSS".equalsIgnoreCase(originCodeLabel)) {
//								newCodeLabel = "FOSS";
//							} else if ("GOSS".startsWith(originCodeLabel)) {
//								newCodeLabel = "GOSS";
//							} else if ("COTS".equalsIgnoreCase(originCodeLabel)) {
//								newCodeLabel = "COTS";
//							} else if ("GOTS".equalsIgnoreCase(originCodeLabel)) {
//								newCodeLabel = "GOTS";
//							} else if ("OpenSource".equalsIgnoreCase(originCodeLabel)) {
//								newCodeLabel = "FOSS";
//							}
//						} else if ("Life Cycle Stage".equals(originTypeDesc)) {
//							newType = "LIFECYCSTG";
//							if ("Development".equalsIgnoreCase(originCodeLabel)) {
//								newCodeLabel = "Development";
//							} else if ("Pilot".equalsIgnoreCase(originCodeLabel)) {
//								newCodeLabel = "Deployment Pilot";
//							} else if ("Operations".equalsIgnoreCase(originCodeLabel)) {
//								newCodeLabel = "Operations";
//							}
//						} else if ("OWF Compatible Widget (Y/N)".equals(originTypeDesc)) {
//							newType = "OWFCOMP";
//							if ("Yes".equalsIgnoreCase(originCodeLabel)) {
//								newCodeLabel = "Yes";
//							} else {
//								newCodeLabel = "No";
//							}
//						}
//					}
//					if (newType != null && newCodeLabel != null) {
//						ComponentAttributeView metaAttribute = mapAttribute(newType, newCodeLabel, attributeMap);
//						componentDetail.getAttributes().add(metaAttribute);
//					}
//
//				}
//			});
//
//			if (generateData) {
//				//filling some details at random with dummy data
//				Random random = new Random(System.currentTimeMillis());
//
//				//component relationships
//				if (random.nextInt(10) < 4) {
//					if (newAssets.size() > 2) {
//						if (random.nextInt(10) < 3) {
//							ComponentDetailView component = newAssets.get(random.nextInt(newAssets.size()));
//							ComponentRelationshipView relationship = new ComponentRelationshipView();
//							relationship.setComponentId(component.getComponentId());
//							relationship.setName(component.getName());
//							componentDetail.setParentComponent(relationship);
//						} else if (random.nextInt(10) < 3) {
//							ComponentDetailView component = newAssets.get(random.nextInt(newAssets.size()));
//							ComponentRelationshipView relationship = new ComponentRelationshipView();
//							relationship.setComponentId(component.getComponentId());
//							relationship.setName(component.getName());
//							componentDetail.getSubComponents().add(relationship);
//						}
//					}
//				}
//
//				//tags
//				if (random.nextInt(10) < 5) {
//					List<String> tempTags = new ArrayList<>(tags);
//					Collections.shuffle(tempTags);
//					int maxAttr = random.nextInt(3);
//					for (int i = 0; i < maxAttr; i++) {
//						ComponentTag componentTag = new ComponentTag();
//						componentTag.setText(tempTags.remove(0));
//						componentDetail.getTags().add(componentTag);
//					}
//				}
//
//				//questions and responses
//				if (random.nextInt(10) < 5) {
//					List<String> keys = new ArrayList<>();
//					keys.addAll(questionMap.keySet());
//					Collections.shuffle(keys);
//					int maxQuestions = random.nextInt(4);
//					for (int i = 0; i < maxQuestions; i++) {
//						String question = keys.remove(0);
//						List<String> responses = questionMap.get(question);
//						ComponentQuestionView componentQuestion = new ComponentQuestionView();
//						componentQuestion.setQuestion(question);
//
//						Collections.shuffle(peopleNames);
//						componentQuestion.setCreateUser(peopleNames.get(0));
//
//						Collections.shuffle(userTypes);
//						componentQuestion.setUserType(userTypes.get(0));
//
//						Collections.shuffle(questionDates);
//						componentQuestion.setCreateDts(questionDates.get(0));
//						componentQuestion.setUpdateDts(questionDates.get(0));
//
//						for (String reponse : responses) {
//							ComponentQuestionResponseView questionResponse = new ComponentQuestionResponseView();
//							Collections.shuffle(peopleNames);
//							questionResponse.setUsername(peopleNames.get(0));
//
//							Collections.shuffle(userTypes);
//							questionResponse.setUserType(userTypes.get(0));
//
//							Collections.shuffle(answerDates);
//							questionResponse.setAnsweredDate(answerDates.get(0));
//
//							questionResponse.setResponse(reponse);
//							componentQuestion.getResponses().add(questionResponse);
//						}
//
//						componentDetail.getQuestions().add(componentQuestion);
//					}
//				}
//
//				//attributes (see the confluence)
////				if (random.nextInt(10) < 8)
////				{
////					List<String> keys = new ArrayList<>();
////					keys.addAll(attriubuteMap.keySet());
////					Collections.shuffle(keys);
////					int maxAttr = random.nextInt(4);
////					for (int i=0; i<maxAttr; i++)
////					{
////						String  typeDescription = keys.remove(0);
////						String  codeDescription =  attriubuteMap.get(typeDescription);
////
////						ComponentAttributeView metaAttribute = new ComponentAttributeView();
////						metaAttribute.setTypeDescription(typeDescription);
////						metaAttribute.setCodeDescription(codeDescription);
////						componentDetail.getAttributes().add(metaAttribute);
////					}
////				}
//				//more metadata
//				if (random.nextInt(10) < 2) {
//					List<String> keys = new ArrayList<>();
//					keys.addAll(metadataMap.keySet());
//					Collections.shuffle(keys);
//					int maxAttr = random.nextInt(4);
//					for (int i = 0; i < maxAttr; i++) {
//						String label = keys.remove(0);
//						String value = metadataMap.get(label);
//						ComponentMetadataView metadata = new ComponentMetadataView();
//						metadata.setLabel(label);
//						metadata.setValue(value);
//						componentDetail.getMetadata().add(metadata);
//					}
//				}
//
//				//reviews
//				if (random.nextInt(10) < 7) {
//					List<String> keys = new ArrayList<>();
//					keys.addAll(reviewMap.keySet());
//					Collections.shuffle(keys);
//					int maxreviews = random.nextInt(4);
//					for (int i = 0; i < maxreviews; i++) {
//						String title = keys.remove(0);
//						String comment = reviewMap.get(title);
//
//						ComponentReviewView componentReview = new ComponentReviewView();
//						componentReview.setTitle(title);
//						componentReview.setComment(comment);
//
//						List<String> tempPros = new ArrayList<>(pros);
//						Collections.shuffle(tempPros);
//						int max = random.nextInt(3);
//						for (int j = 0; j < max; j++) {
//							ComponentReviewPro tag = new ComponentReviewPro();
//							tag.setText(tempPros.remove(0));
//							componentReview.getPros().add(tag);
//						}
//
//						List<String> tempCons = new ArrayList<>(cons);
//						Collections.shuffle(tempCons);
//						max = random.nextInt(3);
//						for (int j = 0; j < max; j++) {
//							ComponentReviewCon tag = new ComponentReviewCon();
//							tag.setText(tempCons.remove(0));
//							componentReview.getCons().add(tag);
//						}
//						Collections.shuffle(peopleNames);
//						componentReview.setCreateUser(peopleNames.get(0));
//						componentReview.setUpdateUser(peopleNames.get(0));
//
//						Collections.shuffle(userTypes);
//						componentReview.setUserTypeCode(userTypes.get(0));
//
//						Collections.shuffle(questionDates);
//						componentReview.setLastUsedDate(questionDates.get(0));
//
//						Collections.shuffle(answerDates);
//						componentReview.setUpdateDts(answerDates.get(0));
//
//						componentReview.setRating(random.nextInt(5) + 1);
//						componentReview.setRecommend(random.nextBoolean());
//
//						Collections.shuffle(organizations);
//						componentReview.setOrganization(organizations.get(0));
//
//						Collections.shuffle(usedTimeCode);
//						componentReview.setExperienceTimeType(usedTimeCode.get(0));
//
//						componentDetail.getReviews().add(componentReview);
//					}
//
//				}
//
//				//external Dependancies
//				if (random.nextInt(10) < 4) {
//					List<String> keys = new ArrayList<>();
//					keys.addAll(externalDependancyMap.keySet());
//					Collections.shuffle(keys);
//					int max = random.nextInt(2);
//					for (int i = 0; i < max; i++) {
//						String dependancy = keys.remove(0);
//						String comment = externalDependancyMap.get(dependancy);
//						ComponentExternalDependencyView externalDependancy = new ComponentExternalDependencyView();
//						externalDependancy.setComment(comment);
//						externalDependancy.setDependency(dependancy);
//						componentDetail.getDependencies().add(externalDependancy);
//					}
//				}
//
//				//eval data
//				ComponentEvaluationView evaluation = componentDetail.getEvaluation();
//
//				if (!"NA".equals(evaluation.getCurrentLevelCode())) {
//
//					if ("LEVEL0".equals(componentDetail.getEvaluation().getCurrentLevelCode())) {
//						//set status to C - complete
//						ComponentEvaluationScheduleView componentEvaluationSchedule = new ComponentEvaluationScheduleView();
//						componentEvaluationSchedule.setLevelStatus("C");
//						componentEvaluationSchedule.setEvaluationLevelCode("LEVEL0");
//						componentEvaluationSchedule.setCompletionDate(TimeUtil.fromString("2014-1-11T10:15:30.00Z"));
//						evaluation.getEvaluationSchedule().add(componentEvaluationSchedule);
//
//						//set status to C - complete
//						componentEvaluationSchedule = new ComponentEvaluationScheduleView();
//						componentEvaluationSchedule.setLevelStatus("N");
//						componentEvaluationSchedule.setEvaluationLevelCode("LEVEL1");
//						evaluation.getEvaluationSchedule().add(componentEvaluationSchedule);
//
//						//set status to C - complete
//						componentEvaluationSchedule = new ComponentEvaluationScheduleView();
//						componentEvaluationSchedule.setLevelStatus("N");
//						componentEvaluationSchedule.setEvaluationLevelCode("LEVEL2");
//						evaluation.getEvaluationSchedule().add(componentEvaluationSchedule);
//
//						//set status to C - complete
//						componentEvaluationSchedule = new ComponentEvaluationScheduleView();
//						componentEvaluationSchedule.setLevelStatus("N");
//						componentEvaluationSchedule.setEvaluationLevelCode("LEVEL3");
//						evaluation.getEvaluationSchedule().add(componentEvaluationSchedule);
//
//					} else if ("LEVEL1".equals(componentDetail.getEvaluation().getCurrentLevelCode())) {
//						//set status to C - complete
//						ComponentEvaluationScheduleView componentEvaluationSchedule = new ComponentEvaluationScheduleView();
//						componentEvaluationSchedule.setLevelStatus("C");
//						componentEvaluationSchedule.setEvaluationLevelCode("LEVEL0");
//						evaluation.getEvaluationSchedule().add(componentEvaluationSchedule);
//
//						//Fill in the general eval info
//						evaluation.setReviewedVersion("1.0");
//						evaluation.setStartDate(TimeUtil.fromString("2014-1-03T10:15:30.00Z"));
//						evaluation.setEndDate(TimeUtil.fromString("2014-3-01T10:15:30.00Z"));
//
//						componentEvaluationSchedule = new ComponentEvaluationScheduleView();
//						int check = random.nextInt(10);
//						if (check < 1) {
//							componentEvaluationSchedule.setLevelStatus("H");
//							componentEvaluationSchedule.setCompletionDate(TimeUtil.fromString("2014-2-11T10:15:30.00Z"));
//						} else if (check < 3) {
//							componentEvaluationSchedule.setLevelStatus("P");
//						} else {
//							componentEvaluationSchedule.setLevelStatus("C");
//							componentEvaluationSchedule.setCompletionDate(TimeUtil.fromString("2014-2-11T10:15:30.00Z"));
//
//							for (String sectionName : evalSections) {
//								ComponentEvaluationSectionView section = new ComponentEvaluationSectionView();
//								section.setName(sectionName);
//								section.setScore(random.nextInt(5) + 1);
//								if (random.nextInt(10) < 1) {
//									section.setScore(0);
//								}
//								evaluation.getEvaluationSections().add(section);
//							}
//
//						}
//
//						componentEvaluationSchedule.setEvaluationLevelCode("LEVEL1");
//						evaluation.getEvaluationSchedule().add(componentEvaluationSchedule);
//
//						//set status to C - complete
//						componentEvaluationSchedule = new ComponentEvaluationScheduleView();
//						componentEvaluationSchedule.setLevelStatus("N");
//						componentEvaluationSchedule.setEvaluationLevelCode("LEVEL2");
//						evaluation.getEvaluationSchedule().add(componentEvaluationSchedule);
//
//						//set status to C - complete
//						componentEvaluationSchedule = new ComponentEvaluationScheduleView();
//						componentEvaluationSchedule.setLevelStatus("N");
//						componentEvaluationSchedule.setEvaluationLevelCode("LEVEL3");
//						evaluation.getEvaluationSchedule().add(componentEvaluationSchedule);
//					}
//				}
//
////				else if ("LEVEL2".equals(componentDetail.getEvaluation().getCurrentLevelCode()))
////				{
////
////				} else if ("LEVEL3".equals(componentDetail.getEvaluation().getCurrentLevelCode()))
////				{
////							//set lower levels to complete
////
////				}
//			}
//
//			newAssets.add(componentDetail);
//		});
//		return newAssets;
//	}
//
//	private ComponentAttributeView mapAttribute(String attributeType, String codeLabel, Map<String, AttributeTypeView> attributeMap)
//	{
//		ComponentAttributeView attribute = new ComponentAttributeView();
//		AttributeTypeView attributeTypeView = attributeMap.get(attributeType);
//		attribute.setType(attributeTypeView.getType());
//		attribute.setTypeDescription(attributeTypeView.getDescription());
//		for (AttributeCodeView attributeCodeView : attributeTypeView.getCodes()) {
//			if (attributeCodeView.getLabel().equalsIgnoreCase(codeLabel.trim())) {
//				attribute.setCode(attributeCodeView.getCode());
//				attribute.setCodeDescription(attributeCodeView.getLabel());
//				attribute.setCodeLongDescription(attributeCodeView.getDescription());
//				attribute.setImportantFlg(attributeTypeView.getImportantFlg());
//				break;
//			}
//		}
//		return attribute;
//	}
//
//	private ComponentMediaView createMediaFromUrl(String url)
//	{
//		ComponentMediaView media = new ComponentMediaView();
//		String baseImagePath = "images/oldsite/";
//		String resource = StringProcessor.getResourceNameFromUrl(url);
//		media.setLink(baseImagePath + resource);
//		media.setContentType("image/" + resource.substring(resource.lastIndexOf(".") + 1, resource.length()));
//		return media;
//	}
//
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
//
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
			data.add("Archtechture flag");
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
					data.add(attributeTypeView.getType());
					data.add(attributeTypeView.getDescription());
					data.add(Boolean.toString(attributeTypeView.getArchtechtureFlg()));
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
////			Logger.getLogger(DataConvertTest.class.getName()).log(Level.SEVERE, null, ex);
////		}
////
////	}
}
