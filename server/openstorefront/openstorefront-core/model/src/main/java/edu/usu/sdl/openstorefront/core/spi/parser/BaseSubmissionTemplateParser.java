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
package edu.usu.sdl.openstorefront.core.spi.parser;

import edu.usu.sdl.openstorefront.core.entity.SubmissionFormTemplate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseSubmissionTemplateParser
		extends AbstractParser<SubmissionFormTemplate>
{

	protected static final int MAX_BUCKET_SIZE = 5;

	protected List<SubmissionFormTemplate> templates = new ArrayList<>();

	@Override
	protected List<SubmissionFormTemplate> getStorageBucket()
	{
		return templates;
	}

	@Override
	protected int getMaxBucketSize()
	{
		return MAX_BUCKET_SIZE;
	}

	@Override
	protected void performStorage()
	{
		for (SubmissionFormTemplate template : templates) {
			service.getSubmissionFormService().saveSubmissionFormTemplate(template);
		}

	}

}
