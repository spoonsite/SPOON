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
import static edu.usu.sdl.openstorefront.core.entity.ContactType.SUBMITTER;
import edu.usu.sdl.openstorefront.core.entity.ExperienceTimeType;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import static edu.usu.sdl.openstorefront.core.entity.UserTypeCode.END_USER;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.OrgReference;
import edu.usu.sdl.openstorefront.core.model.QuestionAll;
import edu.usu.sdl.openstorefront.core.model.ReviewAll;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	@Override
	protected void runInternalTest()
	{

		/**
		 * Waiting on merge cache fix and update service so it updates global
		 * contact
		 */
		ComponentAll componentAll = getTestComponent();
		Component orgComponent = componentAll.getComponent();
		orgComponent.setOrganization("First Test Organization");
		orgComponent.setName("Test Organization Component");

		ReviewAll review = new ReviewAll();
		ComponentReview componentReview = new ComponentReview();
		componentReview.setComment("Test Review Comment");
		componentReview.setUserTypeCode(END_USER);
		componentReview.setRating(4);
		componentReview.setTitle("Test Review Title");
		componentReview.setUserTimeCode("ORGANIZATION_TEST");
		componentReview.setLastUsed(TimeUtil.currentDate());
		componentReview.setRecommend(true);
		componentReview.setOrganization("First Test Organization");
		componentReview.setComponentId(orgComponent.getComponentId());
		experience = new ExperienceTimeType();
		experience.setCode("ORGANIZATION_TEST");
		experience.setDescription("Organization Service Test");
		service.getLookupService().saveLookupValue(experience);
		service.getComponentService().saveComponentReview(componentReview);
		componentReview = new ComponentReview();
		componentReview.setComment("Test Review Comment");
		componentReview.setComponentId(orgComponent.getComponentId());
		componentReview.setUserTypeCode(END_USER);
		componentReview = (ComponentReview) componentReview.find();
		review.setComponentReview(componentReview);
		componentAll.getReviews().add(review);


		QuestionAll question = new QuestionAll();
		ComponentQuestion componentQuestion = new ComponentQuestion();
		componentQuestion.setUserTypeCode(END_USER);
		componentQuestion.setQuestion("Did man really land on the moon?");
		componentQuestion.setOrganization("First Test Organization");
		componentQuestion.setComponentId(orgComponent.getComponentId());
		service.getComponentService().saveComponentQuestion(componentQuestion);
		componentQuestion = new ComponentQuestion();
		componentQuestion.setComponentId(orgComponent.getComponentId());
		componentQuestion.setQuestion("Did man really land on the moon?");
		componentQuestion = (ComponentQuestion) componentQuestion.find();
		question.setQuestion(componentQuestion);
		
		ComponentQuestionResponse compQuestionResp = new ComponentQuestionResponse();
		compQuestionResp.setQuestionId(componentQuestion.getQuestionId());
		compQuestionResp.setComponentId(orgComponent.getComponentId());
		compQuestionResp.setResponse("Why yes they did on July 20, 1969.  Thank you Mr. Armstrong.");
		compQuestionResp.setUserTypeCode(END_USER);
		compQuestionResp.setOrganization("First Test Organization");
		service.getComponentService().saveComponentQuestionResponse(compQuestionResp);
		compQuestionResp = new ComponentQuestionResponse();
		compQuestionResp.setComponentId(orgComponent.getComponentId());
		compQuestionResp.setResponse("Why yes they did on July 20, 1969.  Thank you Mr. Armstrong.");
		compQuestionResp = (ComponentQuestionResponse) compQuestionResp.find();
		question.getResponds().add(compQuestionResp);
		componentAll.getQuestions().add(question);

		componentContact = new ComponentContact();
		componentContact.setContactType(SUBMITTER);
		componentContact.setComponentId(orgComponent.getComponentId());
		componentContact.setFirstName("firstNameTest");
		componentContact.setLastName("lastNameTest");
		componentContact.setOrganization("First Test Organization");
		service.getComponentService().saveComponentContact(componentContact);
		componentContact = new ComponentContact();
		componentContact.setComponentId(orgComponent.getComponentId());
		componentContact.setOrganization("First Test Organization");
		componentContact = (ComponentContact) componentContact.find();
		componentAll.getContacts().add(componentContact);

		FileHistoryOption options = new FileHistoryOption();
		options.setUploadIntegration(true);
		options.setUploadQuestions(true);
		options.setUploadReviews(true);
		options.setUploadTags(true);

		userProfile = getTestUserProfile();
		userProfile.setOrganization("First Test Organization");
		userProfile.setUsername("MyTestUsername");
		userProfile.setFirstName("MyFirstName");
		userProfile.setLastName("MyLastName");
		userProfile.setExternalGuid("8888-8888");
		service.getUserService().saveUserProfile(userProfile);

		componentAll = service.getComponentService().saveFullComponent(componentAll, options);

		// Query organization and then change it to something else and save organization
		// then query the componentAll to see reflection of change in organization
		organization = new Organization();
		organization.setName("First Test Organization");
		organization = organization.find();
		organization.setName("Second Test Organization");
		service.getOrganizationService().saveOrganization(organization);

		ComponentAll componentAllCheck = service.getComponentService().getFullComponent(componentAll.getComponent().getComponentId());
		Component orgComponentCheck = componentAllCheck.getComponent();
		Organization organizationCheck = new Organization();
		organizationCheck.setName("First Test Organization");
		organizationCheck = organization.find();

		if (orgComponentCheck.getOrganization().equals("Second Test Organization") && organizationCheck == null) {
			List<ReviewAll> reviewAllList = componentAllCheck.getReviews();
			for (ReviewAll reviews : reviewAllList) {
				if ("Second Test Organization".equals(reviews.getComponentReview().getOrganization())) {
					results.append("Component review's organization updated successfully<br>");
				} else {
					results.append("Failed - Component review's organization did not update successfully<br>");
				}
			}
			List<QuestionAll> questionAllList = componentAllCheck.getQuestions();
			for (QuestionAll questions : questionAllList) {
				if ("Second Test Organization".equals(questions.getQuestion().getOrganization())) {
					results.append("Component question's organization updated successfully<br>");
				} else {
					failureReason.append("Failed - Component question's organization did not update successfully<br>");
				}
			}
			for (QuestionAll questions : questionAllList) {
				List<ComponentQuestionResponse> questionResponses = questions.getResponds();
				for (ComponentQuestionResponse responses : questionResponses) {
					if ("Second Test Organization".equals(responses.getOrganization())) {
						results.append("Component questions response's organization updated successfully<br>");
					} else {
						failureReason.append("Failed - Component question response's organization did not update successfully<br>");
					}
				}
			}
			List<ComponentContact> contacts = componentAllCheck.getContacts();
			for (ComponentContact contact : contacts) {
				if ("Second Test Organization".equals(contact.getOrganization())) {
					results.append("Component contact's organization updated successfully<br>");
				} else {
					failureReason.append("Failed - Component contact's organization did not update successfully<br>");
				}
			}

			UserProfile profileCheck = new UserProfile();
			profileCheck.setUsername("MyTestUsername");
			profileCheck = (UserProfile) profileCheck.find();

			if ("Second Test Organization".equals(profileCheck.getOrganization())) {
				results.append("UserProfile's organization updated successfully<br><br>");
			} else {
				failureReason.append("Failed - UserProfile's organization did not update successfully<br><br>");
			}
		} else {
			failureReason.append("SubComponents and UserProfile failed to update<br>");
		}

		Organization organizationMerge = new Organization();
		organizationMerge.setName("Second Test Organization");
		organizationMerge = organizationMerge.find();

		organizationTarget = new Organization();
		organizationTarget.setName("Target Organization");
		service.getOrganizationService().saveOrganization(organizationTarget);
		organizationTarget = organizationTarget.find();

		// After the merge takes place the component still reflects the name of organization from new test organization
		// Storefront shows the component having the correct organization name for the component
		service.getOrganizationService().mergeOrganizations(organizationTarget.getOrganizationId(), organizationMerge.getOrganizationId());
		ComponentAll postMergeCompAll = service.getComponentService().getFullComponent(componentAllCheck.getComponent().getComponentId());
		Component postMergeComp = postMergeCompAll.getComponent();
		organizationCheck = new Organization();
		organizationCheck.setName("Second Test Organization");
		organizationCheck = organizationCheck.find();

		if (postMergeComp.getOrganization().equals("Target Organization") && organizationCheck == null) {
			results.append("Organization Merge:  Passed<br><br>");
		} else {
			failureReason.append("Organization Merge:  Failed<br><br>");
		}

		boolean activeComp = true;
		boolean approvedComp = true;
		List<OrgReference> references = service.getOrganizationService().findReferences("Target Organization", activeComp, approvedComp);

		// Check to see if expected references are returned with said organization
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
		if (organization != null) {
			try {
				service.getOrganizationService().removeOrganization(organization.getOrganizationId());
			} catch (AttachedReferencesException ex) {
				Logger.getLogger(OrganizationServiceTest.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		if (organizationTarget != null) {
			try {
				service.getOrganizationService().removeOrganization(organizationTarget.getOrganizationId());
			} catch (AttachedReferencesException ex) {
				Logger.getLogger(OrganizationServiceTest.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		if (componentContact != null) {
			componentContact.setOrganization(TEST_ORGANIZATION);

		}
	}

	@Override
	public String getDescription()
	{
		return "Organization Test";
	}
}
