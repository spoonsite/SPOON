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
package edu.usu.sdl.openstorefront.service.transfermodel;

/**
 *
 * @author dshurtleff
 */
public class ComponentUploadOption
{

	private boolean uploadReviews;
	private boolean uploadQuestions;
	private boolean uploadTags;
	private boolean uploadIntegration;

	public ComponentUploadOption()
	{
	}

	public boolean getUploadReviews()
	{
		return uploadReviews;
	}

	public void setUploadReviews(boolean uploadReviews)
	{
		this.uploadReviews = uploadReviews;
	}

	public boolean getUploadQuestions()
	{
		return uploadQuestions;
	}

	public void setUploadQuestions(boolean uploadQuestions)
	{
		this.uploadQuestions = uploadQuestions;
	}

	public boolean getUploadTags()
	{
		return uploadTags;
	}

	public void setUploadTags(boolean uploadTags)
	{
		this.uploadTags = uploadTags;
	}

	public boolean getUploadIntegration()
	{
		return uploadIntegration;
	}

	public void setUploadIntegration(boolean uploadIntegration)
	{
		this.uploadIntegration = uploadIntegration;
	}

}
