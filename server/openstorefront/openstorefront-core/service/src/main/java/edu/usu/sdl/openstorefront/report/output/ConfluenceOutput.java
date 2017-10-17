/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.report.output;

import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportOutput;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionOption;
import edu.usu.sdl.openstorefront.report.BaseReport;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.GeneratorOptions;
import edu.usu.sdl.openstorefront.report.model.BaseReportModel;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.service.manager.ConfluenceManager;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.Ancestor;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.Content;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.ContentBody;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.ContentVersion;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.RepresentationStorage;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.Space;
import edu.usu.sdl.openstorefront.service.manager.resource.ConfluenceClient;
import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ConfluenceOutput
		extends BaseOutput
{

	private static final Logger LOG = Logger.getLogger(ConfluenceOutput.class.getName());

	private ByteArrayOutputStream generatedReport;

	public ConfluenceOutput(ReportOutput reportOutput, Report report, BaseReport reportGenerator, UserContext userContext)
	{
		super(reportOutput, report, reportGenerator, userContext);
	}

	@Override
	protected BaseGenerator init()
	{
		GeneratorOptions generatorOptions = new GeneratorOptions(report);
		generatedReport = new ByteArrayOutputStream();
		generatorOptions.setOutputStream(generatedReport);
		BaseGenerator generator = BaseGenerator.getGenerator(reportOutput.getReportTransmissionOption().getReportFormat(), generatorOptions);
		return generator;
	}

	@Override
	protected void finishOutput(BaseReportModel reportModel)
	{
		try (ConfluenceClient client = ConfluenceManager.getPoolInstance().getClient()) {
			ReportTransmissionOption options = reportOutput.getReportTransmissionOption();

			//see if there is an existing page
			Content existing = client.getPage(options.getConfluenceSpace(), options.getConfluencePage());

			Content content = new Content();
			content.setTitle(options.getConfluencePage());
			Space space = new Space();
			space.setKey(options.getConfluenceSpace());
			content.setSpace(space);

			ContentBody contentBody = new ContentBody();
			RepresentationStorage storage = new RepresentationStorage();
			storage.setValue(generatedReport.toString());
			contentBody.setStorage(storage);
			content.setBody(contentBody);

			if (StringUtils.isNotBlank(options.getConfuenceParentPageId())) {
				List<Ancestor> ancestors = new ArrayList<>();
				Ancestor ancestor = new Ancestor();
				ancestor.setId(options.getConfuenceParentPageId());
				ancestors.add(ancestor);

				content.setAncestors(ancestors);
			}

			if (existing != null) {

				content.setId(existing.getId());

				int newVersion = 2;
				if (existing.getVersion() != null) {
					newVersion = existing.getVersion().getNumber() + 1;
				}
				ContentVersion version = new ContentVersion();
				version.setNumber(newVersion);
				content.setVersion(version);

				client.updatePage(content);
				LOG.log(Level.INFO, MessageFormat.format("Updated page in confluence (Space-Page): {0}-{1}", new Object[]{options.getConfluenceSpace(), options.getConfluencePage()}));

			} else {
				client.createPage(content);
				LOG.log(Level.INFO, MessageFormat.format("Posted a new page to confluence (Space-Page): {0}-{1}", new Object[]{options.getConfluenceSpace(), options.getConfluencePage()}));
			}

		}
	}

}
