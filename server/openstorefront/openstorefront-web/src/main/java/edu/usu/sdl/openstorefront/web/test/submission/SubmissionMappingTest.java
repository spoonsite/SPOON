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
package edu.usu.sdl.openstorefront.web.test.submission;

import edu.usu.sdl.openstorefront.web.test.BaseTestCase;

/**
 *
 * @author dshurtleff
 */
public class SubmissionMappingTest
		extends BaseTestCase
{

	@Override
	public String getDescription()
	{
		return "Submission Mapping";
	}

	@Override
	protected void runInternalTest()
	{
		//Need to fill out at some point

//		SubmissionFormTemplate formTemplate = service.getSubmissionFormService().findTemplateForComponentType("AUTO_TEST_TYPE");
//
//		UserSubmission userSubmission = new UserSubmission();
//
//		userSubmission.setSubmissionName("AUTO-Test");
//		userSubmission.setOwnerUsername(TEST_USER);
//		userSubmission.setTemplateId(formTemplate.getSubmissionTemplateId());
//
//		userSubmission.setComponentType(service.getComponentService().getAllComponentTypes().get(0).getComponentType());
//
//		UserSubmissionField field = new UserSubmissionField();
//		field.setFieldId("2");
//		field.setTemplateFieldId("1");
//		field.setRawValue(badData());
//		userSubmission.setFields(new ArrayList<>());
//		userSubmission.getFields().add(field);
//
//		service.getSubmissionFormService().saveUserSubmission(userSubmission);
	}

	private String badData()
	{
		StringBuilder sb = new StringBuilder();
//		try {
//			List<String> lines = Files.readAllLines(Paths.get("C:\\test\\storefront\\bad_entry_data.txt"));
//			lines.forEach(line -> sb.append(line).append("\n"));
//		} catch (IOException ex) {
//			Logger.getLogger(SubmissionMappingTest.class.getName()).log(Level.SEVERE, null, ex);
//		}

		return sb.toString();
	}

}
