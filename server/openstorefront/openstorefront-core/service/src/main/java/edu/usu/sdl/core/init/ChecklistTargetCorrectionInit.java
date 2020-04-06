/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.core.init;

import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistResponse;
import edu.usu.sdl.openstorefront.core.entity.ContentSection;
import edu.usu.sdl.openstorefront.core.entity.ContentSubSection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author jstrong
 */
public class ChecklistTargetCorrectionInit
		extends ApplyOnceInit
{

	private static final Logger LOG = Logger.getLogger(ChecklistTargetCorrectionInit.class.getName());

	public ChecklistTargetCorrectionInit()
	{
		super("ChecklistTargetCorrection-Init");
	}

	@Override
	protected String internalApply()
	{
		int fixedElements = 0;

		// Checklist Responses
		EvaluationChecklistResponse evaluationChecklistResponseExample = new EvaluationChecklistResponse();
		List<EvaluationChecklistResponse> checklistResponses = evaluationChecklistResponseExample.findByExampleProxy();
		for (EvaluationChecklistResponse checklistResponse : checklistResponses) {
			if (checklistResponse.getResponse() != null) {

				LOG.log(Level.INFO, "Setting target=\"_blank\" on checklist response " + checklistResponse.getQuestionId());

				String eval = checklistResponse.getResponse();
				Document doc = Jsoup.parse(eval);
				doc.select("a").attr("target", "_blank");
				String updatedResponse = doc.toString();
				checklistResponse.setResponse(updatedResponse);
				checklistResponse.save();

				fixedElements++;
			}
		}

		// Content Section
		ContentSection contentSectionExample = new ContentSection();
		List<ContentSection> contentSections = contentSectionExample.findByExampleProxy();
		for (ContentSection contentSection : contentSections) {
			if (contentSection.getContent() != null) {

				LOG.log(Level.INFO, "Setting target=\"_blank\" on content section");


				String content = contentSection.getContent();
				Document doc = Jsoup.parse(content);
				doc.select("a").attr("target", "_blank");
				String updatedContent = doc.toString();
				contentSection.setContent(updatedContent);
				contentSection.save();

				fixedElements++;
			}
		}


		// Content Sub Section
		ContentSubSection subSectionExample = new ContentSubSection();
		List<ContentSubSection> subSections = subSectionExample.findByExampleProxy();
		for (ContentSubSection subSection : subSections) {
			if (subSection.getContent() != null) {

				LOG.log(Level.INFO, "Setting target=\"_blank\" on content sub section");

				String content = subSection.getContent();
				Document doc = Jsoup.parse(content);
				doc.select("a").attr("target", "_blank");
				String updatedContent = doc.toString();
				subSection.setContent(updatedContent);
				subSection.save();

				fixedElements++;
			}
		}



		return "Fixed elements: " + fixedElements;
	}

	@Override
	public int getPriority()
	{
		return 20;
	}
}
