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
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import edu.usu.sdl.openstorefront.core.entity.ContactType;
import edu.usu.sdl.openstorefront.core.entity.ContentSection;
import edu.usu.sdl.openstorefront.core.entity.ContentSubSection;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.ResourceType;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.core.model.ChecklistAll;
import edu.usu.sdl.openstorefront.core.model.ContentSectionAll;
import edu.usu.sdl.openstorefront.core.model.EvaluationAll;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.core.view.ChecklistResponseView;
import edu.usu.sdl.openstorefront.core.view.ComponentContactView;
import edu.usu.sdl.openstorefront.core.view.ComponentDetailView;
import edu.usu.sdl.openstorefront.core.view.ComponentExternalDependencyView;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionResponseView;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionView;
import edu.usu.sdl.openstorefront.core.view.ComponentRelationshipView;
import edu.usu.sdl.openstorefront.core.view.ComponentResourceView;
import edu.usu.sdl.openstorefront.core.view.ComponentReviewProCon;
import edu.usu.sdl.openstorefront.core.view.ComponentReviewView;
import edu.usu.sdl.openstorefront.core.view.EvaluationChecklistRecommendationView;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.generator.HtmlGenerator;
import edu.usu.sdl.openstorefront.report.generator.HtmlToPdfGenerator;
import edu.usu.sdl.openstorefront.report.model.BaseReportModel;
import edu.usu.sdl.openstorefront.report.model.ComponentDetailReportLineModel;
import edu.usu.sdl.openstorefront.report.model.ComponentDetailReportModel;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import edu.usu.sdl.openstorefront.service.manager.ReportManager;
import freemarker.core.ParseException;
import freemarker.template.*;
import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
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

	private static final Logger LOG = Logger.getLogger(ComponentDetailReport.class.getName());

	public ComponentDetailReport(Report report)
	{
		super(report);
	}

	@Override
	protected ComponentDetailReportModel gatherData()
	{
		//Grab all active attributes
		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		componentAttributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		List<ComponentAttribute> allCodes = componentAttributeExample.findByExample();

		Map<String, Map<String, List<ComponentAttribute>>> codeToComponent = new HashMap<>();
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

		//Contacts
		Map<String, List<ComponentContact>> contactMap = new HashMap<>();
		ComponentContact componentContact = new ComponentContact();
		componentContact.setActiveStatus(ComponentContact.ACTIVE_STATUS);
		List<ComponentContact> allContacts = componentContact.findByExample();
		contactMap = allContacts.stream().collect(Collectors.groupingBy(ComponentContact::getComponentId));

		//Resources
		Map<String, List<ComponentResource>> resourceMap = new HashMap<>();
		ComponentResource componentResource = new ComponentResource();
		componentResource.setActiveStatus(ComponentResource.ACTIVE_STATUS);
		List<ComponentResource> allResources = componentResource.findByExample();
		resourceMap = allResources.stream().collect(Collectors.groupingBy(ComponentResource::getComponentId));

		//Grab all components
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

		for (Component component : components) {
			ComponentDetailReportLineModel lineModel = new ComponentDetailReportLineModel();
			lineModel.setComponent(component);

			List<ComponentContact> contacts = contactMap.get(component.getComponentId());
			contacts = filterEngine.filter(contacts);
			lineModel.setContacts(contacts);

			List<ComponentResource> resources = resourceMap.get(component.getComponentId());
			resources = filterEngine.filter(resources);
			lineModel.setResources(resources);

			lineModel.setAttributes(codeToComponent.get(component.getComponentId()));
		}

		return reportModel;
	}

	@Override
	protected void writeReport()
	{
		if (ReportFormat.CSV.equals(report.getReportFormat())) {
			generateCSV();
		} else if (ReportFormat.HTML.equals(report.getReportFormat()) || ReportFormat.PDF.equals(report.getReportFormat())) {
			generateHtml();

			try {
				Configuration templateConfig = ReportManager.getTemplateConfig();

				//	Generate the report template
				Template template = templateConfig.getTemplate("detailReport.ftl");
				Writer writer = new StringWriter();
				template.process(root, writer);
				String renderedTemplate = writer.toString();

				//	Save/write as the appropriate report
				//	HTML report
				if (ReportFormat.HTML.equals(report.getReportFormat())) {
					HtmlGenerator htmlGenerator = (HtmlGenerator) generator;
					htmlGenerator.addLine(renderedTemplate);
				} //	PDF report
				else if (ReportFormat.PDF.equals(report.getReportFormat())) {
					HtmlToPdfGenerator htmlPdfGenerator = (HtmlToPdfGenerator) generator;
					htmlPdfGenerator.savePdfDocument(renderedTemplate);
				}

			} catch (TemplateException | IOException e) {
				throw new OpenStorefrontRuntimeException("Failed to write the report! - " + e);
			}
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
								if (getBranding().getAllowSecurityMarkingsFlg()
										&& StringUtils.isNotBlank(attributeCode.getSecurityMarkingType())) {
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
									if (getBranding().getAllowSecurityMarkingsFlg()
											&& StringUtils.isNotBlank(attributeCode.getSecurityMarkingType())) {
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
					if (getBranding().getAllowSecurityMarkingsFlg()
							&& StringUtils.isNotBlank(metadataItem.getSecurityMarkingType())) {
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
					if (getBranding().getAllowSecurityMarkingsFlg()
							&& StringUtils.isNotBlank(contact.getSecurityMarkingType())) {
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
					if (getBranding().getAllowSecurityMarkingsFlg()
							&& StringUtils.isNotBlank(resource.getSecurityMarkingType())) {
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
		try {
			Configuration templateConfig = ReportManager.getTemplateConfig();
			Map root = new HashMap();

			// create a list of components
			List<Map> componentList = new ArrayList<>();
			for (Component component : components) {
				String securityMarking = "";
				Map componentRoot = new HashMap();
				ComponentDetailView componentDetail = service.getComponentService().getComponentDetails(component.getComponentId());

				// Generate dependencies
				List<ComponentExternalDependencyView> allDependencies = componentDetail.getDependencies();
				if (allDependencies != null) {

					List<Map> dependenciesList = new ArrayList<>();
					for (ComponentExternalDependencyView dependent : allDependencies) {

						Map dependentHash = new HashMap();
						dependentHash.put("name", dependent.getDependencyName());
						dependentHash.put("version", dependent.getVersion());
						dependentHash.put("link", dependent.getDependancyReferenceLink());
						dependentHash.put("comment", dependent.getComment());

						dependenciesList.add(dependentHash);
					}

					componentRoot.put("dependencies", dependenciesList);
				}

				// Generate tags
				List<ComponentTag> allTags = componentDetail.getTags();
				if (allTags != null) {

					List<Map> tagsList = new ArrayList<>();
					for (ComponentTag tag : allTags) {

						Map tagHash = new HashMap();
						tagHash.put("text", tag.getText());
						tagsList.add(tagHash);
					}

					componentRoot.put("tags", tagsList);
				}

				// Generate relationships
				List<ComponentRelationshipView> allRelationships = componentDetail.getRelationships();
				if (allRelationships != null) {

					List<Map> relationsList = new ArrayList<>();
					for (ComponentRelationshipView relation : allRelationships) {

						Map relationHash = new HashMap();
						relationHash.put("type", relation.getRelationshipTypeDescription());
						//relationHash.put("type", relation.getRelationshipType());
						relationHash.put("targetName", relation.getTargetComponentName());
						relationHash.put("componentName", component.getName());
						relationsList.add(relationHash);
					}

					componentRoot.put("relationships", relationsList);
				}

				// Generate reviews
				List<ComponentReviewView> allReviews = componentDetail.getReviews();
				if (allReviews != null) {

					List<Map> reviewsList = new ArrayList<>();
					for (ComponentReviewView review : allReviews) {

						Map reviewHash = new HashMap();
						reviewHash.put("rating", review.getRating());
						reviewHash.put("title", review.getTitle());
						reviewHash.put("username", review.getUsername());
						reviewHash.put("lastUsed", review.getLastUsed());
						reviewHash.put("organization", review.getOrganization());
						reviewHash.put("timeDescription", review.getUserTimeDescription());
						reviewHash.put("comment", review.getComment());
						reviewHash.put("recommended", review.isRecommend());

						List<ComponentReviewProCon> allReviewPros = review.getPros();
						List<ComponentReviewProCon> allReviewCons = review.getCons();
						List<Map> reviewPros = new ArrayList<>();
						if (allReviewPros != null) {
							for (ComponentReviewProCon pro : allReviewPros) {
								Map proHash = new HashMap();
								proHash.put("pro", pro.getText());
								reviewPros.add(proHash);
							}
						}

						List<Map> reviewCons = new ArrayList<>();
						if (allReviewCons != null) {
							for (ComponentReviewProCon con : allReviewCons) {
								Map conHash = new HashMap();
								conHash.put("con", con.getText());
								reviewCons.add(conHash);
							}
						}

						reviewHash.put("pros", reviewPros);
						reviewHash.put("cons", reviewCons);
						reviewsList.add(reviewHash);
					}

					componentRoot.put("reviews", reviewsList);
				}

				// Generate Q/A
				List<ComponentQuestionView> allQuestions = componentDetail.getQuestions();
				if (allQuestions != null) {

					List<Map> questionsList = new ArrayList<>();
					for (ComponentQuestionView question : allQuestions) {

						Map questionHash = new HashMap();
						questionHash.put("question", question.getQuestion());
						questionHash.put("username", question.getUsername());
						questionHash.put("date", question.getCreateDts());

						List<ComponentQuestionResponseView> allResponses = question.getResponses();
						if (allResponses != null) {

							List<Map> responsesList = new ArrayList<>();
							for (ComponentQuestionResponseView response : allResponses) {

								Map responseHash = new HashMap();
								responseHash.put("response", response.getResponse());
								responseHash.put("username", response.getUsername());
								responseHash.put("date", response.getAnsweredDate());
								responsesList.add(responseHash);
							}

							questionHash.put("responses", responsesList);
						}

						questionsList.add(questionHash);
					}

					componentRoot.put("QA", questionsList);
				}

				// Generate evaluation data
				List<EvaluationAll> allEvals = componentDetail.getFullEvaluations();
				if (allEvals != null) {

					List<Map> evalList = new ArrayList<>();
					for (EvaluationAll evaluationView : allEvals) {
						Evaluation evaluation = evaluationView.getEvaluation();
						securityMarking = "";
						if (getBranding().getAllowSecurityMarkingsFlg() && StringUtils.isNotBlank(evaluation.getSecurityMarkingType())) {
							securityMarking = "(" + evaluation.getSecurityMarkingType() + ") ";
						}

						Map evalHash = new HashMap();

						EvaluationAll evaluationAll = service.getEvaluationService().getEvaluation(evaluation.getEvaluationId());
						ChecklistAll checklistAll = evaluationAll.getCheckListAll();
						List<EvaluationChecklistRecommendationView> allChecklistRecommendations = checklistAll.getRecommendations();

						evalHash.put("checklistSummary", checklistAll.getEvaluationChecklist().getSummary());
						evalHash.put("version", evaluation.getVersion());

						// Recommendations
						List<Map> checklistRecommendations = new ArrayList<>();
						for (EvaluationChecklistRecommendationView recommendation : allChecklistRecommendations) {
							Map recommendationHash = new HashMap();
							recommendationHash.put("recommendation", recommendation.getRecommendation());
							recommendationHash.put("reason", recommendation.getReason());
							recommendationHash.put("type", recommendation.getRecommendationType());
							recommendationHash.put("typeDescription", recommendation.getRecommendationTypeDescription());

							checklistRecommendations.add(recommendationHash);
						}
						evalHash.put("recommendations", checklistRecommendations);

						// Reusability Scores
						List<Map> reusabilityScores = new ArrayList<>();

						// get the score sections (and group them)
						Map<String, List<ChecklistResponseView>> scoreSections = checklistAll
								.getResponses()
								.stream()
								.collect(Collectors.groupingBy(
										p -> p.getQuestion().getEvaluationSectionDescription()
								));

						// get the average and base scores, and the reusability factor
						Set<String> scoreKeyset = new TreeSet(scoreSections.keySet());
						for (String key : scoreKeyset) {
							Double averageScore = scoreSections.get(key)
									.stream()
									.filter(p -> p.getScore() != null)
									.collect(Collectors.averagingDouble(
											p -> p.getScore().doubleValue()
									));

							Map scoreHash = new HashMap();
							scoreHash.put("factor", key);
							scoreHash.put("averageScore", (averageScore > 0) ? Math.round(averageScore * 10.0) / 10.0 : 0);
							scoreHash.put("score", (averageScore > 0) ? averageScore.intValue() : "N/A");

							reusabilityScores.add(scoreHash);
						}
						evalHash.put("scores", reusabilityScores);

						// Sections
						List<ContentSectionAll> evaluationSectionsAll = evaluationAll.getContentSections();
						List<Map> evaluationSections = new ArrayList<>();
						for (ContentSectionAll sectionAll : evaluationSectionsAll) {
							ContentSection section = sectionAll.getSection();
							Map sectionHash = new HashMap();

							// get sub sections
							List<Map> evaluationSubSections = new ArrayList<>();
							for (ContentSubSection subSection : sectionAll.getSubsections()) {
								Map subSectionHash = new HashMap();
								subSectionHash.put("title", subSection.getTitle());
								subSectionHash.put("content", subSection.getContent());
								subSectionHash.put("isPrivate", subSection.getPrivateSection());
								subSectionHash.put("hideTitle", subSection.getHideTitle());
								subSectionHash.put("hideContent", subSection.getNoContent());

								evaluationSubSections.add(subSectionHash);
							}

							sectionHash.put("subSections", evaluationSubSections);
							sectionHash.put("title", section.getTitle());
							sectionHash.put("content", section.getContent());
							sectionHash.put("isPrivate", section.getPrivateSection());
							sectionHash.put("hideContent", section.getNoContent());

							evaluationSections.add(sectionHash);
						}
						evalHash.put("evaluationSections", evaluationSections);

						// Evaluation checklist details
						List<ChecklistResponseView> checklistDetailsAll = evaluationAll.getCheckListAll().getResponses();
						List<Map> checklistDetails = new ArrayList<>();
						for (ChecklistResponseView detail : checklistDetailsAll) {
							Map detailHash = new HashMap();
							detailHash.put("qId", detail.getQuestion().getQid());
							detailHash.put("question", detail.getQuestion().getQuestion());
							detailHash.put("score", detail.getScore());
							detailHash.put("response", detail.getResponse());
							detailHash.put("section", detail.getQuestion().getEvaluationSectionDescription());

							checklistDetails.add(detailHash);
						}
						evalHash.put("checklistDetails", checklistDetails);

						// Add this evaluation to the evaluation list
						evalList.add(evalHash);
					}
					componentRoot.put("evaluations", evalList);
				}

				// Generate vitals data
				Map<String, List<ComponentAttribute>> attributeMap = codeToComponent.get(component.getComponentId());
				if (attributeMap != null) {

					Map<String, String> typeDescriptionMap = new HashMap<>();
					for (String type : attributeMap.keySet()) {
						String typeLabel = service.getAttributeService().findType(type).getDescription();
						typeDescriptionMap.put(typeLabel, type);
					}

					List<String> attributeTypeList = new ArrayList<>(typeDescriptionMap.keySet());
					attributeTypeList.sort(null);

					// Make a list of all the vitals
					List<Map> vitalsList = new ArrayList<>();
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
									if (getBranding().getAllowSecurityMarkingsFlg()
											&& StringUtils.isNotBlank(attributeCode.getSecurityMarkingType())) {
										securityMarking = "(" + attributeCode.getSecurityMarkingType() + ") ";
									}
									attributeLabel = securityMarking + attributeCode.getLabel();
								} else {
									attributeLabel = "Missing Code: " + attributeCodePk.getAttributeCode() + " on Type: " + attributeCodePk.getAttributeType();
								}

								// Add to the list of vitals
								Map vitalsHash = new HashMap();
								vitalsHash.put("typeLabel", typeLabel);
								vitalsHash.put("attributeLabel", attributeLabel);
								vitalsList.add(vitalsHash);
							}

						}
					}
					componentRoot.put("vitals", vitalsList);
				}

				// Generate Contancts
				List<ComponentContactView> contacts = componentDetail.getContacts();
				if (contacts != null) {

					// make a list of contacts
					List<Map> contactsList = new ArrayList<>();
					for (ComponentContactView contact : contacts) {

						securityMarking = "";
						if (getBranding().getAllowSecurityMarkingsFlg()
								&& StringUtils.isNotBlank(contact.getSecurityMarkingType())) {
							securityMarking = "(" + contact.getSecurityMarkingType() + ") ";
						}

						// Add to the contacts list
						Map contactsHash = new HashMap();
						contactsHash.put("type", contact.getContactType());
						contactsHash.put("firstName", securityMarking + contact.getFirstName());
						contactsHash.put("lastName", contact.getLastName());
						contactsHash.put("org", contact.getOrganization());
						contactsHash.put("email", contact.getEmail());
						contactsHash.put("phone", contact.getPhone());

						contactsList.add(contactsHash);
					}
					componentRoot.put("contacts", contactsList);
				}

				// Generate Resources
				List<ComponentResource> resources = resourceMap.get(component.getComponentId());
				if (resources != null) {
					resources = FilterEngine.filter(resources);

					// make a list of resources
					List<Map> resourcesList = new ArrayList<>();
					for (ComponentResource resource : resources) {

						securityMarking = "";
						if (getBranding().getAllowSecurityMarkingsFlg()
								&& StringUtils.isNotBlank(resource.getSecurityMarkingType())) {
							securityMarking = "(" + resource.getSecurityMarkingType() + ") ";
						}

						ComponentResourceView view = ComponentResourceView.toView(resource);
						Map resourcesHash = new HashMap();

						// Add to the resources list
						resourcesHash.put("type", TranslateUtil.translate(ResourceType.class, view.getResourceType()));
						resourcesHash.put("description", StringProcessor.blankIfNull(view.getDescription()));
						resourcesHash.put("link", securityMarking + view.getLink());
						resourcesHash.put("restricted", StringProcessor.blankIfNull(view.getRestricted()));

						resourcesList.add(resourcesHash);
					}
					componentRoot.put("resources", resourcesList);
				}

				componentRoot.put("component", component);
				componentRoot.put("name", component.getName());
				componentList.add(componentRoot);
			}

			// generate the template
			root.put("components", componentList);
			root.put("reportOptions", report.getReportOption());
			root.put("allowSecurityMargkingsFlg", getBranding().getAllowSecurityMarkingsFlg());
			root.put("reportSize", components.size());
			root.put("reportDate", sdf.format(TimeUtil.currentDate()));
			Template template = templateConfig.getTemplate("detailReport.ftl");
			Writer writer = new StringWriter();
			template.process(root, writer);
			String renderedTemplate = writer.toString();
			htmlGenerator.addLine(renderedTemplate);
		} catch (Exception e) {
			LOG.log(Level.WARNING, MessageFormat.format("There was a problem when generating a detail report: {0}", e));
			throw new OpenStorefrontRuntimeException(e);
		}
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
	public List<ReportFormat> getSupportedFormat(String reportTransmissionType)
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
				"Entry Description"
		);

		for (ComponentDetailReportLineModel lineModel : reportModel.getData()) {

			cvsGenerator.addLine(
					lineModel.getComponent().getName(),
					lineModel.getComponent().getOrganization(),
					StringProcessor.stripHtml(lineModel.getComponent().getDescription())
			);

			Map<String, List<ComponentAttribute>> attributeMap = lineModel.getAttributes();
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
								if (getBranding().getAllowSecurityMarkingsFlg()
										&& StringUtils.isNotBlank(attributeCode.getSecurityMarkingType())) {
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
									if (getBranding().getAllowSecurityMarkingsFlg()
											&& StringUtils.isNotBlank(attributeCode.getSecurityMarkingType())) {
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

			if (lineModel.getContacts() != null) {
				cvsGenerator.addLine("Contacts");
				for (ComponentContact contact : lineModel.getContacts()) {

					String securityMarking = "";
					if (getBranding().getAllowSecurityMarkingsFlg()
							&& StringUtils.isNotBlank(contact.getSecurityMarkingType())) {
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

			if (lineModel.getResources() != null) {

				cvsGenerator.addLine("Resources");
				for (ComponentResource resource : lineModel.getResources()) {

					String securityMarking = "";
					if (getBranding().getAllowSecurityMarkingsFlg()
							&& StringUtils.isNotBlank(resource.getSecurityMarkingType())) {
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
		pdfGenerator.savePdfDocument(renderedTemplate);
	}

	private String createHtml(ComponentDetailReportModel reportModel)
	{
		String renderedTemplate = null;
		try {
			Configuration templateConfig = ReportManager.getTemplateConfig();

			//	Generate the report template
			Template template = templateConfig.getTemplate("detailReport.ftl");
			Writer writer = new StringWriter();
			template.process(root, writer);
			renderedTemplate = writer.toString();

		} catch (MalformedTemplateNameException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		} catch (ParseException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		return renderedTemplate;
	}

	@Override
	public String reportSummmary(BaseReportModel reportModel)
	{
		StringBuilder summary = new StringBuilder();

		//summary
		//list entries in report
		return summary.toString();
	}

}
