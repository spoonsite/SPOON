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
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.SecurityMarkingType;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
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
public class ComponentOrganizationReport
		extends BaseReport
{

	public ComponentOrganizationReport(Report report)
	{
		super(report);
	}

	@Override
	protected void gatherData()
	{
	}

	@Override
	protected void writeReport()
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		//write header
		cvsGenerator.addLine("Entry Organization Report", sdf.format(TimeUtil.currentDate()));

		List<String> header = new ArrayList<>();
		header.add("Organization");
		header.add("Entry Name");
		header.add("Last Update Date");
		header.add("Approve Status");

		if (getBranding().getAllowSecurityMarkingsFlg()) {
			header.add("Security Marking");
		}
		cvsGenerator.addLine(header.toArray());

		Map<String, Object> params = new HashMap<>();
		String componentFilter = "";
		if (!report.dataIdSet().isEmpty()) {
			params = new HashMap<>();
			params.put("idlistParam", report.dataIdSet());
			componentFilter = " and componentId in :idlistParam";
		}
		
		String restrictionQuery = FilterEngine.queryComponentRestriction();
		
		List<ODocument> documents = service.getPersistenceService().query("Select organization, name, name.toLowerCase() as sortname, securityMarkingType, lastActivityDts, approvalState from " + Component.class.getSimpleName()
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
		}
		);

		long totalComponents = 0;
		List<String> sortedOrganizations = new ArrayList<>(orgMap.keySet());

		sortedOrganizations.sort(
				null);

		for (String organization : sortedOrganizations) {
			cvsGenerator.addLine(organization);
			for (ODocument document : orgMap.get(organization)) {

				List<String> data = new ArrayList<>();
				data.add("");
				data.add(document.field("name"));		
				data.add(sdf.format(document.field("lastActivityDts")));
				data.add(document.field("approvalState"));

				if (getBranding().getAllowSecurityMarkingsFlg()) {
					String securityMarking = document.field("securityMarkingType");
					data.add(securityMarking == null ? "" : "(" + securityMarking + ") - " + TranslateUtil.translate(SecurityMarkingType.class, securityMarking));
				}
				cvsGenerator.addLine(data.toArray());

				totalComponents++;
			}
			cvsGenerator.addLine("Total", orgMap.get(organization).size());
			cvsGenerator.addLine("");
		}

		cvsGenerator.addLine(
				"");
		cvsGenerator.addLine(
				"Report Totals");
		cvsGenerator.addLine(
				"Total Organizations: " + orgMap.keySet().size());
		cvsGenerator.addLine(
				"Total Entries: " + totalComponents);
	}

}
