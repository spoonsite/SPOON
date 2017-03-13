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

import com.orientechnologies.orient.core.record.impl.ODocument;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserTracking;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class OrganizationReport
		extends BaseReport
{

	private static final String NO_ORG = "No Organization";

	private List<UserProfile> userProfiles;

	public OrganizationReport(Report report)
	{
		super(report);
	}

	@Override
	protected void gatherData()
	{
		UserProfile userProfileExample = new UserProfile();
		userProfileExample.setActiveStatus(UserProfile.ACTIVE_STATUS);
		userProfiles = service.getPersistenceService().queryByExample(userProfileExample);
	}

	@Override
	protected void writeReport()
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		//write header
		cvsGenerator.addLine("User Organization Report", sdf.format(TimeUtil.currentDate()));
		cvsGenerator.addLine(
				"Organization",
				"# Users",
				"# Reviews",
				"# Questions",
				"# Question Responses",
				"# Component Views",
				"# Component Resources Clicked",
				"# Logins"
		);

		Map<String, List<UserProfile>> orgMap = new HashMap<>();
		for (UserProfile userProfile : userProfiles) {
			String organization = userProfile.getOrganization();
			if (StringUtils.isBlank(organization)) {
				if (orgMap.get(NO_ORG) == null) {
					orgMap.put(NO_ORG, new ArrayList<>());
				}
				orgMap.get(NO_ORG).add(userProfile);
			}

			if (StringUtils.isNotBlank(organization)) {
				if (orgMap.containsKey(organization)) {
					orgMap.get(organization).add(userProfile);
				} else {
					List<UserProfile> orgUsers = new ArrayList<>();
					orgUsers.add(userProfile);
					orgMap.put(organization, orgUsers);
				}
			}
		}

		for (String org : orgMap.keySet()) {

			long reviews = getRecordCounts(ComponentReview.class, orgMap.get(org), null);
			long questions = getRecordCounts(ComponentQuestion.class, orgMap.get(org), null);
			long response = getRecordCounts(ComponentQuestionResponse.class, orgMap.get(org), null);
			long componentViews = getRecordCounts(ComponentTracking.class, orgMap.get(org), TrackEventCode.VIEW);
			long componentResourceClick = getRecordCounts(ComponentTracking.class, orgMap.get(org), TrackEventCode.EXTERNAL_LINK_CLICK);
			long logins = getRecordCounts(UserTracking.class, orgMap.get(org), TrackEventCode.LOGIN);

			cvsGenerator.addLine(
					org,
					orgMap.get(org).size(),
					reviews,
					questions,
					response,
					componentViews,
					componentResourceClick,
					logins
			);

		}

	}

	private long getRecordCounts(Class recordClass, List<UserProfile> userProfiles, String trackCodeType)
	{
		long count = 0;

		List<String> userIds = new ArrayList<>();
		for (UserProfile userProfile : userProfiles) {
			userIds.add(userProfile.getUsername());
		}

		if (userIds.isEmpty() == false) {
			StringBuilder query = new StringBuilder();
			query.append("select count(*) from ")
					.append(recordClass.getSimpleName())
					.append(" where ").append(" createUser IN :createUserListParam ");

			if (StringUtils.isNotBlank(trackCodeType)) {
				query.append(" AND trackEventTypeCode = :eventCodeParam ");
			}

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("createUserListParam", userIds);
			paramMap.put("eventCodeParam", trackCodeType);

			List<ODocument> documents = service.getPersistenceService().query(query.toString(), paramMap);
			if (documents.isEmpty() == false) {
				count = documents.get(0).field("count");
			}
		}

		return count;
	}

}
