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
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import edu.usu.sdl.openstorefront.core.entity.ContactType;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.model.SubmissionReportLineModel;
import edu.usu.sdl.openstorefront.report.model.SubmissionReportModel;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class SubmissionsReport
		extends BaseReport
{

	public SubmissionsReport(Report report)
	{
		super(report);
	}

	@Override
	protected SubmissionReportModel gatherData()
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
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		List<Component> found = service.getPersistenceService().queryByExample(queryByExample);
		List<Component> componentsSubmited = new ArrayList<>();
		for (Component component : found) {
			if (component.getSubmittedDts() != null) {
				componentsSubmited.add(component);
			} else if (ApprovalStatus.NOT_SUBMITTED.equals(component.getApprovalState())) {
				componentsSubmited.add(component);
			}
		}

		SubmissionReportModel submissionReportModel = new SubmissionReportModel();
		submissionReportModel.setTitle("Entry Submission Report");
		submissionReportModel.setDataStartDate(report.getReportOption().getStartDts());
		submissionReportModel.setDataEndDate(report.getReportOption().getEndDts());

		for (Component component : componentsSubmited) {

			ComponentAll componentAll = service.getComponentService().getFullComponent(component.getComponentId());

			Contact submitter = new Contact();
			submitter.setFirstName(OpenStorefrontConstant.NOT_AVAILABLE);

			for (ComponentContact componentContact : componentAll.getContacts()) {
				Contact contact = componentContact.fullContact();
				if (ContactType.SUBMITTER.equals(componentContact.getContactType())) {
					submitter = contact;
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

			SubmissionReportLineModel lineModel = new SubmissionReportLineModel();
			lineModel.setName(component.getName() + componentSecurityMarking);
			lineModel.setCreateDts(component.getCreateDts());
			lineModel.setUpdateDts(component.getUpdateDts());
			lineModel.setSubmittedDts(component.getSubmittedDts());
			lineModel.setSubmitterEmail(submitter.getEmail());
			lineModel.setSubmitterPhone(submitter.getPhone());
			lineModel.setSubmitterName(submitter.getFirstName() + " " + submitter.getLastName() + submitterSecurityMarking);
			lineModel.setSubmitterOrganization(submitter.getOrganization());
			lineModel.setActiveStatus(component.getActiveStatus());
			lineModel.setCurrentAprovalStatus(TranslateUtil.translate(ApprovalStatus.class, component.getApprovalState()));
			lineModel.setLoggedInUser(component.getCreateUser());

			submissionReportModel.getData().add(lineModel);
		}

		return submissionReportModel;
	}

	@Override
	protected Map<String, ReportWriter> getWriterMap()
	{
		Map<String, ReportWriter> writerMap = new HashMap<>();

		String viewCSV = outputKey(ReportTransmissionType.VIEW, ReportFormat.CSV);
		writerMap.put(viewCSV, (generator, reportModel) -> {
			writeCSV(generator, (SubmissionReportModel) reportModel);
		});

		String emailCSV = outputKey(ReportTransmissionType.EMAIL, ReportFormat.CSV);
		writerMap.put(emailCSV, (generator, reportModel) -> {
			writeCSV(generator, (SubmissionReportModel) reportModel);
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

		switch (reportTransmissionType) {
			case ReportTransmissionType.VIEW:
				ReportFormat format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.CSV);
				formats.add(format);
				break;

			case ReportTransmissionType.EMAIL:
				format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.CSV);
				formats.add(format);
				break;
		}

		return formats;
	}

	private void writeCSV(BaseGenerator generator, SubmissionReportModel reportModel)
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;
		cvsGenerator.addLine(reportModel.getTitle(), sdf.format(reportModel.getCreateTime()));
		cvsGenerator.addLine("Data Time Range (Based on Updated Date):  ", sdf.format(reportModel.getDataStartDate()) + " - " + sdf.format(reportModel.getDataEndDate()));

		cvsGenerator.addLine(
				"Entry Name",
				"Create Date",
				"Update Date",
				"Submitted Date",
				"Submitter Name",
				"Submitter Email",
				"Submitter Phone",
				"Submitter Organization",
				"Logged in User",
				"Current Approval Status",
				"Active Status"
		);

		for (SubmissionReportLineModel lineModel : reportModel.getData()) {
			cvsGenerator.addLine(
					lineModel.getName(),
					sdf.format(lineModel.getCreateDts()),
					sdf.format(lineModel.getUpdateDts()),
					sdf.format(lineModel.getSubmittedDts()),
					lineModel.getSubmitterName(),
					lineModel.getSubmitterEmail(),
					lineModel.getSubmitterPhone(),
					lineModel.getSubmitterOrganization(),
					lineModel.getLoggedInUser(),
					lineModel.getCurrentAprovalStatus(),
					lineModel.getActiveStatus()
			);
		}
	}

}
