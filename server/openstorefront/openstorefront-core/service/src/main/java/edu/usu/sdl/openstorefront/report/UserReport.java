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

import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import edu.usu.sdl.openstorefront.core.entity.UserWatch;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.model.BaseReportModel;
import edu.usu.sdl.openstorefront.report.model.UserReportModel;
import edu.usu.sdl.openstorefront.report.output.BaseOutput;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
public class UserReport
		extends BaseReport
{

	private List<UserProfile> userProfiles;

	public UserReport(Report report)
	{
		super(report);
	}

	@Override
	protected UserReportModel gatherData()
	{
		UserReportModel userReportModel = new UserReportModel();
		
		UserProfile userProfileExample = new UserProfile();
		userProfileExample.setActiveStatus(UserProfile.ACTIVE_STATUS);
		userProfiles = service.getPersistenceService().queryByExample(userProfileExample);
		
		userReportModel.setTitle("User Report");
		userReportModel.setCreateTime(TimeUtil.currentDate());
		
		
		
		
		
		return userReportModel;		
	}

	@Override
	protected void writeReport()
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		//write header
		cvsGenerator.addLine("User Report", sdf.format(TimeUtil.currentDate()));
		cvsGenerator.addLine(
				"Username",
				"Organization",
				"GUID",
				"First name",
				"Last name",
				"Email",
				"User Type",
				"First Login Date",
				"Last Login Date",
				"# Active Watches",
				"# Active Reviews",
				"# Active Questions",
				"# Active Question Responses",
				"# Tags",
				"# Entry Views"
		);

		//write Body
		Map<String, Date> loginMap = service.getUserService().getLastLogin(userProfiles);
		for (UserProfile userProfile : userProfiles) {

			UserWatch watchExample = new UserWatch();
			watchExample.setCreateUser(userProfile.getUsername());
			watchExample.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			long watches = service.getPersistenceService().countByExample(watchExample);

			ComponentReview componentReviewExample = new ComponentReview();
			componentReviewExample.setCreateUser(userProfile.getUsername());
			componentReviewExample.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			long reviews = service.getPersistenceService().countByExample(componentReviewExample);

			ComponentTag componentTagExample = new ComponentTag();
			componentTagExample.setCreateUser(userProfile.getUsername());
			componentTagExample.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			long tags = service.getPersistenceService().countByExample(componentTagExample);

			ComponentQuestion componentQuestionExample = new ComponentQuestion();
			componentQuestionExample.setCreateUser(userProfile.getUsername());
			componentQuestionExample.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			long questions = service.getPersistenceService().countByExample(componentQuestionExample);

			ComponentQuestionResponse componentQuestionResponseExample = new ComponentQuestionResponse();
			componentQuestionResponseExample.setCreateUser(userProfile.getUsername());
			componentQuestionResponseExample.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			long questionResponse = service.getPersistenceService().countByExample(componentQuestionResponseExample);

			String lastLogin = "";
			if (loginMap.containsKey(userProfile.getUsername())) {
				lastLogin = sdf.format(loginMap.get(userProfile.getUsername()));
			}

			ComponentTracking componentTrackingExample = new ComponentTracking();
			componentTrackingExample.setActiveStatus(ComponentTracking.ACTIVE_STATUS);
			componentTrackingExample.setCreateUser(userProfile.getUsername());
			componentTrackingExample.setTrackEventTypeCode(TrackEventCode.VIEW);
			long componentView = service.getPersistenceService().countByExample(componentTrackingExample);

			cvsGenerator.addLine(
					userProfile.getUsername(),
					userProfile.getOrganization(),
					userProfile.getExternalGuid() != null ? userProfile.getExternalGuid() : userProfile.getInternalGuid(),
					userProfile.getFirstName(),
					userProfile.getLastName(),
					userProfile.getEmail(),
					TranslateUtil.translate(UserTypeCode.class, userProfile.getUserTypeCode()),
					sdf.format(userProfile.getCreateDts()),
					lastLogin,
					watches,
					reviews,
					questions,
					questionResponse,
					tags,
					componentView
			);
		}

	}

	@Override
	protected void doOutput(BaseOutput outputHandler, BaseReportModel reportModel)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<ReportTransmissionType> getSupportedOutputs()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<ReportFormat> getSupportedFormat(String reportTransmissionType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
