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

import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.service.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.service.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.service.transfermodel.ComponentAll;
import edu.usu.sdl.openstorefront.storage.model.ApprovalStatus;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentContact;
import edu.usu.sdl.openstorefront.storage.model.ContactType;
import edu.usu.sdl.openstorefront.storage.model.Report;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.util.TranslateUtil;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class SubmissionsReport
		extends BaseReport
{

	private List<Component> componentsSubmited = new ArrayList<>();

	public SubmissionsReport(Report report)
	{
		super(report);
	}

	@Override
	protected void gatherData()
	{
		updateReportTimeRange();

		Component componentExample = new Component();

		Component componentStartExample = new Component();
		componentStartExample.setUpdateDts(report.getReportOption().getStartDts());

		Component componentEndExample = new Component();
		componentEndExample.setUpdateDts(report.getReportOption().getEndDts());

		QueryByExample queryByExample = new QueryByExample(componentExample);
		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(componentStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(componentEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParamaterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		List<Component> found = service.getPersistenceService().queryByExample(Component.class, queryByExample);
		for (Component component : found) {
			if (component.getSubmittedDts() != null) {
				componentsSubmited.add(component);
			} else if (ApprovalStatus.NOT_SUBMITTED.equals(component.getApprovalState())) {
				componentsSubmited.add(component);
			}
		}
	}

	private void updateReportTimeRange()
	{
		if (report.getReportOption().getPreviousDays() != null) {
			Instant instant = Instant.now();
			instant = instant.minus(1, ChronoUnit.DAYS);
			report.getReportOption().setStartDts(TimeUtil.beginningOfDay(new Date(instant.toEpochMilli())));
			report.getReportOption().setEndDts(TimeUtil.endOfDay(new Date(instant.toEpochMilli())));
		}
		if (report.getReportOption().getStartDts() == null) {
			report.getReportOption().setStartDts(TimeUtil.beginningOfDay(new Date()));
		}
		if (report.getReportOption().getEndDts() == null) {
			report.getReportOption().setEndDts(TimeUtil.endOfDay(new Date()));
		}
	}

	@Override
	protected void writeReport()
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		//write header
		cvsGenerator.addLine("Component Submission Report", sdf.format(TimeUtil.currentDate()));
		cvsGenerator.addLine("Data Time Range:  ", sdf.format(report.getReportOption().getStartDts()) + " - " + sdf.format(report.getReportOption().getEndDts()));
		cvsGenerator.addLine(
				"Name",
				"Submitted Date",
				"Submitter Name",
				"Submitter Email",
				"Submitter Phone",
				"Organization",
				"Logged in User",
				"Current Approval Status",
				"Active Status"
		);

		//write data
		for (Component component : componentsSubmited) {
			ComponentAll componentAll = service.getComponentService().getFullComponent(component.getComponentId());

			ComponentContact submitter = new ComponentContact();
			submitter.setFirstName(OpenStorefrontConstant.NOT_AVAILABLE);

			for (ComponentContact componentContact : componentAll.getContacts()) {
				if (ContactType.SUBMITTER.equals(componentContact.getContactType())) {
					submitter = componentContact;
				}
			}

			String componentSecurityMarking = "";
			if (StringUtils.isNotBlank(component.getSecurityMarkingType())) {
				componentSecurityMarking = " (" + component.getSecurityMarkingType() + ")";
			}

			String submitterSecurityMarking = "";
			if (StringUtils.isNotBlank(submitter.getSecurityMarkingType())) {
				submitterSecurityMarking = " (" + submitter.getSecurityMarkingType() + ")";
			}

			cvsGenerator.addLine(
					component.getName() + componentSecurityMarking,
					component.getSubmittedDts() != null ? sdf.format(component.getSubmittedDts()) : "",
					submitter.getFirstName() + " " + submitter.getLastName() + submitterSecurityMarking,
					submitter.getEmail(),
					submitter.getPhone(),
					submitter.getOrganization(),
					component.getCreateUser(),
					TranslateUtil.translate(ApprovalStatus.class, component.getApprovalState()),
					component.getActiveStatus()
			);
		}

	}

}
