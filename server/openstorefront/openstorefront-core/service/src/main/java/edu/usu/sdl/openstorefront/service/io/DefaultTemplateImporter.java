/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.io;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormTemplate;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class DefaultTemplateImporter
		implements Initializable
{

	private static final Logger LOG = Logger.getLogger(DefaultTemplateImporter.class.getName());

	@Override
	public void initialize()
	{
		//check for default
		LOG.log(Level.INFO, "Checking for Default Submission Template.");
		SubmissionFormTemplate submissionFormTemplate = new SubmissionFormTemplate();
		submissionFormTemplate.setDefaultTemplate(Boolean.TRUE);
		submissionFormTemplate = submissionFormTemplate.find();
		if (submissionFormTemplate == null) {
			try (InputStream is = DefaultTemplateImporter.class.getResourceAsStream("/data/defaultSubmissionTemplate.json");) {

				//it gets exported as an array
				List<SubmissionFormTemplate> templates = StringProcessor.defaultObjectMapper().readValue(is, new TypeReference<List<SubmissionFormTemplate>>()
				{
				});
				if (templates.size() > 0) {
					LOG.log(Level.INFO, "Found Default Template");
					submissionFormTemplate = templates.get(0);
					ServiceProxy.getProxy().getSubmissionFormService().saveSubmissionTemplateAsDefault(submissionFormTemplate);

					LOG.log(Level.INFO, "Saved Default Template");
				} else {
					LOG.log(Level.SEVERE, "No default template defined. Check code");
				}

			} catch (IOException io) {
				LOG.log(Level.SEVERE, "Unable to read template.");
				LOG.log(Level.FINEST, null, io);
			}
		} else {
			LOG.log(Level.INFO, "Default Submission Template found.");
		}
	}

	@Override
	public void shutdown()
	{
		//ignore
	}

	@Override
	public boolean isStarted()
	{
		return true;
	}

}
