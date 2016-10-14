/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.entity.ChecklistQuestion;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseChecklistQuestionParser
		extends AbstractParser<ChecklistQuestion>
{

	private static final Logger LOG = Logger.getLogger(BaseChecklistQuestionParser.class.getName());

	protected static final int MAX_BUCKET_SIZE = 100;

	protected List<ChecklistQuestion> questions = new ArrayList<>();

	@Override
	protected List<ChecklistQuestion> getStorageBucket()
	{
		return questions;
	}

	@Override
	protected int getMaxBucketSize()
	{
		return MAX_BUCKET_SIZE;
	}

	@Override
	protected void performStorage()
	{
		service.getChecklistService().saveChecklistQuestion(questions);
	}

}
