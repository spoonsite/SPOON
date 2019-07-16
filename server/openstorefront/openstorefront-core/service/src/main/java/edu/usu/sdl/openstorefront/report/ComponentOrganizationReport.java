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
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.model.ComponentOrganizationReportLineModel;
import edu.usu.sdl.openstorefront.report.model.ComponentOrganizationReportModel;
import edu.usu.sdl.openstorefront.report.model.EntryOrgDetailModel;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ComponentOrganizationReport
		extends BaseReport
{

	public ComponentOrganizationReport(Report report)
	{
		super(report);
	}

	@Override
	protected ComponentOrganizationReportModel gatherData()
	{
		ComponentOrganizationReportModel reportModel = new ComponentOrganizationReportModel();

		Map<String, Object> params = new HashMap<>();
		String componentFilter = "";
		if (!report.dataIdSet().isEmpty()) {
			params = new HashMap<>();
			params.put("idlistParam", report.dataIdSet());
			componentFilter = " and componentId in :idlistParam";
		}

		String restrictionQuery = filterEngine.queryComponentRestriction();

		List<ODocument> documents = service.getPersistenceService().query(
				"Select organization, name, name.toLowerCase()"
				+ " as sortname, securityMarkingType, submittedDts, lastActivityDts, approvedDts, approvalState from " + Component.class.getSimpleName()
				+ " where approvalState='" + ApprovalStatus.APPROVED + "' and "
				+ (StringUtils.isNotBlank(restrictionQuery) ? restrictionQuery + " and " : "")
				+ " activeStatus= '" + Component.ACTIVE_STATUS + "' " + componentFilter + " order by sortname", params);

		//group by org
		Map<String, List<ODocument>> orgMap = new HashMap<>();

		documents.forEach(document
				-> {
			String org = document.field("organization");
			if (StringUtils.isBlank(org)) {
				org = "No Organization Specified";
			}
			if (orgMap.containsKey(org)) {
				orgMap.get(org).add(document);
			} else {
				List<ODocument> records = new ArrayList<>();
				records.add(document);
				orgMap.put(org, records);
			}
		});
		long totalComponents = 0;
		List<String> sortedOrganizations = new ArrayList<>(orgMap.keySet());

		sortedOrganizations.sort(null);

		for (String organization : sortedOrganizations) {
			ComponentOrganizationReportLineModel lineModel = new ComponentOrganizationReportLineModel();
			lineModel.setOrganization(organization);
			for (ODocument document : orgMap.get(organization)) {
				EntryOrgDetailModel detailModel = new EntryOrgDetailModel();
				detailModel.setName(document.field("name"));
				detailModel.setLastSubmitDts(document.field("submittedDts"));
				detailModel.setLastActivityDts(document.field("lastActivityDts"));
				detailModel.setApprovedDts(document.field("approvedDts"));
				detailModel.setApprovalState(document.field("approvalState"));

				lineModel.getEntries().add(detailModel);
				totalComponents++;
			}
			reportModel.getData().add(lineModel);
		}
		reportModel.setTotalComponent(totalComponents);
		reportModel.setTotalOrganizations(orgMap.keySet().size());

		return reportModel;
	}

	@Override
	protected Map<String, ReportWriter> getWriterMap()
	{
		Map<String, ReportWriter> writerMap = new HashMap<>();

		String viewCSV = outputKey(ReportTransmissionType.VIEW, ReportFormat.CSV);
		writerMap.put(viewCSV, (generator, reportModel) -> {
			writeCSV(generator, (ComponentOrganizationReportModel) reportModel);
		});

		String emailCSV = outputKey(ReportTransmissionType.EMAIL, ReportFormat.CSV);
		writerMap.put(emailCSV, (generator, reportModel) -> {
			writeCSV(generator, (ComponentOrganizationReportModel) reportModel);
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
	public List<ReportFormat> getSupportedFormats(String reportTransmissionType)
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

	private void writeCSV(BaseGenerator generator, ComponentOrganizationReportModel reportModel)
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		cvsGenerator.addLine(reportModel.getTitle(), sdf.format(reportModel.getCreateTime()));

		cvsGenerator.addLine(
				"Organization",
				"Entry Name",
				"Last Vendor Update",
				"Last Vendor Update Approved Date",
				"Last System Update",
				"Approve Status"
		);

		for (ComponentOrganizationReportLineModel lineModel : reportModel.getData()) {
			cvsGenerator.addLine(lineModel.getOrganization());

			for (EntryOrgDetailModel detailModel : lineModel.getEntries()) {
				cvsGenerator.addLine(
					"",
					detailModel.getName(),
					// Imported entries dont have lastSubmitDts @see{Component#submittedDts}
					detailModel.getLastSubmitDts() == null ? null : sdf.format(detailModel.getLastSubmitDts()),
					sdf.format(detailModel.getApprovedDts()),
					sdf.format(detailModel.getLastActivityDts()),
					detailModel.getApprovalState());
			}
			cvsGenerator.addLine("Total", lineModel.getEntries().size());
			cvsGenerator.addLine("");
		}

		cvsGenerator.addLine("");
		cvsGenerator.addLine("Report Totals");
		cvsGenerator.addLine("Total Organizations: " + reportModel.getTotalOrganizations());
		cvsGenerator.addLine("Total Entries: " + reportModel.getTotalComponent());

	}

}
