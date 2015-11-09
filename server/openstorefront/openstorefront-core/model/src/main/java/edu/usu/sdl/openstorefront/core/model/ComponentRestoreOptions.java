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
package edu.usu.sdl.openstorefront.core.model;

/**
 *
 * @author dshurtleff
 */
public class ComponentRestoreOptions
{

	private boolean restoreReviews;
	private boolean restoreQuestions;
	private boolean restoreTags;
	private boolean restoreIntegration;

	public ComponentRestoreOptions()
	{
	}

	public boolean getRestoreReviews()
	{
		return restoreReviews;
	}

	public void setRestoreReviews(boolean restoreReviews)
	{
		this.restoreReviews = restoreReviews;
	}

	public boolean getRestoreQuestions()
	{
		return restoreQuestions;
	}

	public void setRestoreQuestions(boolean restoreQuestions)
	{
		this.restoreQuestions = restoreQuestions;
	}

	public boolean getRestoreTags()
	{
		return restoreTags;
	}

	public void setRestoreTags(boolean restoreTags)
	{
		this.restoreTags = restoreTags;
	}

	public boolean getRestoreIntegration()
	{
		return restoreIntegration;
	}

	public void setRestoreIntegration(boolean restoreIntegration)
	{
		this.restoreIntegration = restoreIntegration;
	}

}
