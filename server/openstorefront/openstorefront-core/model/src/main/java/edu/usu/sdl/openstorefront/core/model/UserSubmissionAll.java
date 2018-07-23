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
package edu.usu.sdl.openstorefront.core.model;

import edu.usu.sdl.openstorefront.core.entity.UserSubmission;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionMedia;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class UserSubmissionAll
{

	private UserSubmission userSubmission;
	private List<UserSubmissionMedia> media = new ArrayList<>();

	public UserSubmission getUserSubmission()
	{
		return userSubmission;
	}

	public void setUserSubmission(UserSubmission userSubmission)
	{
		this.userSubmission = userSubmission;
	}

	public List<UserSubmissionMedia> getMedia()
	{
		return media;
	}

	public void setMedia(List<UserSubmissionMedia> media)
	{
		this.media = media;
	}

}
