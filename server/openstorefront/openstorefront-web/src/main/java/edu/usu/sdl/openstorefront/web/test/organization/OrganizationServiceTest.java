/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.test.organization;

import edu.usu.sdl.openstorefront.common.exception.AttachedReferencesException;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import static edu.usu.sdl.openstorefront.core.entity.ContactType.SUBMITTER;
import edu.usu.sdl.openstorefront.core.entity.ExperienceTimeType;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import static edu.usu.sdl.openstorefront.core.entity.StandardEntity.ACTIVE_STATUS;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import static edu.usu.sdl.openstorefront.core.entity.UserTypeCode.END_USER;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.QuestionAll;
import edu.usu.sdl.openstorefront.core.model.ReviewAll;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class OrganizationServiceTest extends BaseTestCase
{

	private ExperienceTimeType experience = null;
	private Organization organization = null;
	private Organization organizationTarget = null;
	private UserProfile userProfile = null;
	private ComponentContact componentContact = null;
	private Contact contactFromCompContact = null;

	@Override
	protected void runInternalTest()
	{
		ComponentAll componentAll = getTestComponent();
		Component orgComponent = componentAll.getComponent();
		orgComponent.setOrganization("First-Test-001 Organization");
		orgComponent.setName("Test-Organization-Service Component");

		ReviewAll review = new ReviewAll();
		ComponentReview componentReview = new ComponentReview();
		componentReview.setComment("Test Review Comment for Organization Test");
		componentReview.setUserTypeCode(END_USER);
		componentReview.setRating(4);
		componentReview.setTitle("Test Review Title");
		componentReview.setUserTimeCode("ORGANIZATION_TEST");
		componentReview.setLastUsed(TimeUtil.currentDate());
		componentReview.setRecommend(true);
		componentReview.setOrganization("First-Test-001 Organization");
		componentReview.setComponentId(orgComponent.getComponentId());
		experience = new ExperienceTimeType();
		experience.setCode("ORGANIZATION_TEST");
		experience.setDescription("Experience - Organization Service Test");
		service.getLookupService().saveLookupValue(experience);
		service.getComponentService().saveComponentReview(componentReview);
		componentReview = new ComponentReview();
		componentReview.setComment("Test Review Comment for Organization Test");
		componentReview.setComponentId(orgComponent.getComponentId());
		componentReview.setUserTypeCode(END_USER);
		componentReview = componentReview.find();
		review.setComponentReview(componentReview);
		componentAll.getReviews().add(review);

		QuestionAll question = new QuestionAll();
		ComponentQuestion componentQuestion = new ComponentQuestion();
		componentQuestion.setUserTypeCode(END_USER);
		componentQuestion.setQuestion("Did man really land on the moon?");
		componentQuestion.setOrganization("First-Test-001 Organization");
		componentQuestion.setComponentId(orgComponent.getComponentId());
		service.getComponentService().saveComponentQuestion(componentQuestion);
		componentQuestion = new ComponentQuestion();
		componentQuestion.setComponentId(orgComponent.getComponentId());
		componentQuestion.setQuestion("Did man really land on the moon?");
		componentQuestion.setOrganization("First-Test-001 Organization");
		componentQuestion = componentQuestion.find();
		question.setQuestion(componentQuestion);

		ComponentQuestionResponse compQuestionResp = new ComponentQuestionResponse();
		compQuestionResp.setQuestionId(componentQuestion.getQuestionId());
		compQuestionResp.setComponentId(orgComponent.getComponentId());
		compQuestionResp.setResponse("Why yes they did on July 20, 1969.  Thank you Mr. Armstrong.");
		compQuestionResp.setUserTypeCode(END_USER);
		compQuestionResp.setOrganization("First-Test-001 Organization");
		service.getComponentService().saveComponentQuestionResponse(compQuestionResp);
		compQuestionResp = new ComponentQuestionResponse();
		compQuestionResp.setComponentId(orgComponent.getComponentId());
		compQuestionResp.setResponse("Why yes they did on July 20, 1969.  Thank you Mr. Armstrong.");
		compQuestionResp.setOrganization("First-Test-001 Organization");
		compQuestionResp = compQuestionResp.find();
		question.getResponds().add(compQuestionResp);
		componentAll.getQuestions().add(question);

		componentContact = new ComponentContact();
		componentContact.setContactType(SUBMITTER);
		componentContact.setComponentId(orgComponent.getComponentId());
		componentContact.setFirstName("ComponentContactFirstName1");
		componentContact.setLastName("ComponentContactLastName1");
		componentContact.setOrganization("First-Test-001 Organization");
		componentContact.setEmail("componentcontact01@compcontact.com");
		service.getComponentService().saveComponentContact(componentContact);
		componentContact = new ComponentContact();
		componentContact.setComponentId(orgComponent.getComponentId());
		componentContact.setEmail("componentcontact01@compcontact.com");
		componentContact.setOrganization("First-Test-001 Organization");
		componentContact = componentContact.find();
		componentAll.getContacts().add(componentContact);

		contactFromCompContact = componentContact.fullContact();

		FileHistoryOption options = new FileHistoryOption();
		options.setUploadIntegration(true);
		options.setUploadQuestions(true);
		options.setUploadReviews(true);
		options.setUploadTags(true);

		userProfile = getTestUserProfile();
		userProfile.setOrganization("First-Test-001 Organization");
		userProfile.setUsername("OrgTestUsername1");
		userProfile.setFirstName("OrgTestFirstName2");
		userProfile.setLastName("OrgTestLastName3");
		userProfile.setExternalGuid("8888-8888");
		userProfile = service.getUserService().saveUserProfile(userProfile);
		userProfile.setActiveStatus(ACTIVE_STATUS);

		componentAll = service.getComponentService().saveFullComponent(componentAll, options);

		// Query organization and then change it to something else and save organization
		// then query the componentAll to see reflection of change in organization
		organization = new Organization();
		organization.setName("First-Test-001 Organization");
		organization = organization.find();
		organization.setName("Second-Test-002 Organization");
		service.getOrganizationService().saveOrganization(organization);

		ComponentAll componentAllCheck = service.getComponentService().getFullComponent(componentAll.getComponent().getComponentId());
		Component orgComponentCheck = componentAllCheck.getComponent();
		Organization organizationCheck = new Organization();
		organizationCheck.setName("First-Test-001 Organization");
		organizationCheck = organization.find();

		if (orgComponentCheck.getOrganization().equals("Second-Test-002 Organization") && organizationCheck == null) {
			List<ReviewAll> reviewAllList = componentAllCheck.getReviews();
			for (ReviewAll reviews : reviewAllList) {
				if ("Second-Test-002 Organization".equals(reviews.getComponentReview().getOrganization())) {
					results.append("Component review's organization updated successfully<br>");
				} else {
					results.append("Failed - Component review's organization did not update successfully<br>");
				}
			}
			List<QuestionAll> questionAllList = componentAllCheck.getQuestions();
			for (QuestionAll questions : questionAllList) {
				if ("Second-Test-002 Organization".equals(questions.getQuestion().getOrganization())) {
					results.append("Component question's organization updated successfully<br>");
				} else {
					failureReason.append("Failed - Component question's organization did not update successfully<br>");
				}
			}
			for (QuestionAll questions : questionAllList) {
				List<ComponentQuestionResponse> questionResponses = questions.getResponds();
				for (ComponentQuestionResponse responses : questionResponses) {
					if ("Second-Test-002 Organization".equals(responses.getOrganization())) {
						results.append("Component questions response's organization updated successfully<br>");
					} else {
						failureReason.append("Failed - Component question response's organization did not update successfully<br>");
					}
				}
			}
			List<ComponentContact> contacts = componentAllCheck.getContacts();
			for (ComponentContact contact : contacts) {
				if ("Second-Test-002 Organization".equals(contact.getOrganization())) {
					results.append("Component contact's organization updated successfully<br>");
				} else {
					failureReason.append("Failed - Component contact's organization did not update successfully<br>");
				}
			}

			UserProfile profileCheck = new UserProfile();
			profileCheck.setUsername("OrgTestUsername1");
			profileCheck = profileCheck.find();

			if ("Second-Test-002 Organization".equals(profileCheck.getOrganization())) {
				results.append("UserProfile's organization updated successfully<br><br>");
			} else {
				failureReason.append("Failed - UserProfile's organization did not update successfully<br><br>");
			}
		} else {
			failureReason.append("SubComponents and UserProfile failed to update<br>");
		}

		Organization organizationMerge = new Organization();
		organizationMerge.setName("Second-Test-002 Organization");
		organizationMerge = organizationMerge.find();

		organizationTarget = new Organization();
		organizationTarget.setName("Target Organization 005");
		service.getOrganizationService().saveOrganization(organizationTarget);
		organizationTarget = organizationTarget.find();

		// After the merge takes place the component still reflects the name of organization from new test organization
		// Storefront shows the component having the correct organization name for the component
		service.getOrganizationService().mergeOrganizations(organizationTarget.getOrganizationId(), organizationMerge.getOrganizationId());
		ComponentAll postMergeCompAll = service.getComponentService().getFullComponent(componentAllCheck.getComponent().getComponentId());
		Component postMergeComp = postMergeCompAll.getComponent();
		organizationCheck = new Organization();
		organizationCheck.setName("Second-Test-002 Organization");
		organizationCheck = organizationCheck.find();

		if (postMergeComp.getOrganization().equals("Target Organization 005") && organizationCheck == null) {
			results.append("Organization Merge:  Passed<br><br>");
		} else {
			failureReason.append("Organization Merge:  Failed<br><br>");
		}
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
		if (experience != null) {
			service.getLookupService().removeValue(ExperienceTimeType.class, experience.getCode());
		}
		if (userProfile != null) {
			userProfile.setOrganization(TEST_ORGANIZATION);
			service.getUserService().saveUserProfile(userProfile);
		}
		if (contactFromCompContact != null) {
			service.getContactService().deleteContact(contactFromCompContact.getContactId());
		}

		if (organization != null) {
			try {
				service.getOrganizationService().removeOrganization(organization.getOrganizationId());
			} catch (AttachedReferencesException ex) {
				failureReason.append(ex).append("- Unable to delete ").append(organization.getName()).append("<br><br>");

			}
		}
		if (organizationTarget != null) {
			try {
				service.getOrganizationService().removeOrganization(organizationTarget.getOrganizationId());
			} catch (AttachedReferencesException ex) {
				failureReason.append(ex).append("- Unable to delete ").append(organizationTarget.getName()).append("<br><br>");
			}
		}
		if (organization != null) {
			try {
				service.getOrganizationService().removeOrganization(organization.getOrganizationId());
			} catch (AttachedReferencesException ex) {
				failureReason.append(ex).append("- Unable to delete ").append(organization.getName()).append("<br><br>");
			}
		}

		Organization organizationCheck = new Organization();
		organizationCheck.setName("Second-Test-002 Organization");
		organizationCheck = organizationCheck.find();

		if (organizationCheck != null) {
			try {
				service.getOrganizationService().removeOrganization(organizationCheck.getOrganizationId());
			} catch (AttachedReferencesException ex) {
				failureReason.append(ex).append("- Unable to delete ").append(organizationCheck.getName()).append("<br><br>");
			}
		}

	}

	@Override
	public String getDescription()
	{
		return "Organization Test";
	}
}
