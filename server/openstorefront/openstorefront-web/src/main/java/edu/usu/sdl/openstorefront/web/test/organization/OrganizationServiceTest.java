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
import edu.usu.sdl.openstorefront.core.entity.Organization;
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
	@Override
	protected void runInternalTest()
	{
		/**
		 * To be continued...waiting on results from JIRA bug ticket
		 */
		ComponentAll componentAll = getTestComponent();
		Component orgComponent = componentAll.getComponent();
		orgComponent.setOrganization("Existing Test Organization");
		orgComponent.setName("Test Organization Component");

		ReviewAll review = new ReviewAll();
		ComponentReview componentReview = new ComponentReview();
		componentReview.setComment("Test Review Comment");
		componentReview.setUserTypeCode(END_USER);
		componentReview.setRating(4);
		componentReview.setTitle("Test Review Title");
		componentReview.setUserTimeCode("3 months");
		componentReview.setLastUsed(TimeUtil.currentDate());
		componentReview.setRecommend(true);
		componentReview.setOrganization("Existing Test Organization");
		review.setComponentReview(componentReview);
		componentAll.getReviews().add(review);

		QuestionAll question = new QuestionAll();
		ComponentQuestion componentQuestion = new ComponentQuestion();
		componentQuestion.setUserTypeCode(END_USER);
		componentQuestion.setQuestion("Did man really land on the moon?");
		componentQuestion.setOrganization("Existing Test Organization");
		question.setQuestion(componentQuestion);

		ComponentQuestionResponse compQuestionResp = new ComponentQuestionResponse();
		compQuestionResp.setQuestionId(componentQuestion.getQuestionId());
		compQuestionResp.setResponse("Why yes they did on July 20, 1969.  Thank you Mr. Armstrong.");
		compQuestionResp.setUserTypeCode(END_USER);
		compQuestionResp.setOrganization("Existing Test Organization");
		question.getResponds().add(compQuestionResp);
		componentAll.getQuestions().add(question);

		ComponentContact componentContact = new ComponentContact();
		componentContact.setContactType(SUBMITTER);
		componentContact.setFirstName("firstNameTest");
		componentContact.setLastName("lastNameTest");
		componentContact.setOrganization("Existing Test Organization");
		componentAll.getContacts().add(componentContact);

//		FileHistoryOption options = new FileHistoryOption();
//		options.setUploadIntegration(true);
//		options.setUploadQuestions(true);
//		options.setUploadReviews(true);
//		options.setUploadTags(true);

		componentAll = service.getComponentService().saveFullComponent(componentAll);
//		componentAll = service.getComponentService().saveFullComponent(componentAll, options);

		UserProfile userProfile = getTestUserProfile();
		String previousOrganization = userProfile.getOrganization();
		userProfile.setOrganization("First Test Organization");
		service.getUserService().saveUserProfile(userProfile);

		// Query organization and then change it to something else and save organization
		// then query the componentAll to see reflection of change in organization
		Organization organization = new Organization();
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
					results.append("Component review's organization updated successfully");
				} else {
					results.append("Failed - Component review's organization did not update successfully");
				}
			}
			List<QuestionAll> questionAllList = componentAllCheck.getQuestions();
			for (QuestionAll questions : questionAllList) {
				if ("New Test Organization".equals(questions.getQuestion().getOrganization())) {
					results.append("Component question's organization updated successfully");
				} else {
					failureReason.append("Failed - Component question's organization did not update successfully");
				}
			}
			for (QuestionAll questions : questionAllList) {
				List<ComponentQuestionResponse> questionResponses = questions.getResponds();
				for (ComponentQuestionResponse responses : questionResponses) {
					if ("New Test Organization".equals(responses.getOrganization())) {
						results.append("Component questions response's organization updated successfully");
					} else {
						failureReason.append("Failed - Component question response's organization did not update successfully");
					}
				}
			}
			List<ComponentContact> contacts = componentAllCheck.getContacts();
			for (ComponentContact contact : contacts) {
				if ("New Test Organization".equals(contact.getOrganization())) {
					results.append("Component contact's organization updated successfully");
				} else {
					failureReason.append("Failed - Component contact's organization did not update successfully");
				}
			}

			UserProfile profileCheck = new UserProfile();
			profileCheck.setUsername(TEST_USER);
			profileCheck = (UserProfile) profileCheck.find();

			if ("New Test Organization".equals(profileCheck.getOrganization())) {
				results.append("UserProfile's organization updated successfully");
			} else {
				failureReason.append("Failed - UserProfile's organization did not update successfully");
			}
		} else {
			failureReason.append("SubComponents and UserProfile failed to update");
		}

		Organization organizationMerge = new Organization();
		organizationMerge.setName("New Test Organization");
		organizationMerge = organizationMerge.find();

		Organization organizationTarget = new Organization();
		organizationTarget.setName("Merge Organization");
		service.getOrganizationService().saveOrganization(organizationTarget);
		organizationTarget = organizationTarget.find();

		// After the merge takes place the component still reflects the name of organization from new test organization
		// Storefront shows the component having the correct organization name for the component
		service.getOrganizationService().mergeOrganizations(organizationTarget.getOrganizationId(), organizationMerge.getOrganizationId());
		componentAll = service.getComponentService().getFullComponent(componentAll.getComponent().getComponentId());
		orgComponent = componentAll.getComponent();
		organizationCheck = new Organization();
		organizationCheck.setName("New Test Organization");
		organizationCheck = organizationCheck.find();

		if (orgComponent.getOrganization().equals("Merge Organization") && organizationCheck == null) {
			results.append("Organization Merge:  Successful").append("<br><br>");
		}
//else {
//			try {
//				resetUserProfile(userProfile, previousOrganization);
//				cleanupOrganization(componentAll, organizationMerge, organizationTarget);
//			} catch (AttachedReferencesException ex) {
//				Logger.getLogger(OrganizationServiceTest.class.getName()).log(Level.SEVERE, null, ex);
//			}
//		}
//
//		try {
//			resetUserProfile(userProfile, previousOrganization);
//			cleanupOrganization(componentAll, organization1, organizationTarget);
//		} catch (AttachedReferencesException ex) {
//			Logger.getLogger(OrganizationServiceTest.class.getName()).log(Level.SEVERE, null, ex);
//		}

	}

	public void cleanupOrganization(Organization org) throws AttachedReferencesException
	{
		service.getOrganizationService().removeOrganization(org.getOrganizationId());
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
	}

	@Override
	public String getDescription()
	{
		return "Organization Test";
	}
}
