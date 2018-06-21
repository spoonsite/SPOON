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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.entity.UserSubmissionMedia;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class UserSubmissionMediaView
{

	private String link;
	private String mimeType;
	private String submissionMediaId;
	private UserSubmissionMedia media;

	public UserSubmissionMediaView()
	{
		//ignore for serialization
	}

	public static UserSubmissionMediaView toView(UserSubmissionMedia userSubmissionMedia)
	{
		UserSubmissionMediaView view = new UserSubmissionMediaView();
		view.setMedia(userSubmissionMedia);
		view.setMimeType(userSubmissionMedia.getFile().getMimeType());
		view.setSubmissionMediaId(userSubmissionMedia.getSubmissionMediaId());
		view.setLink("Media.action?LoadMedia&mediaId=" + userSubmissionMedia.getFile().getMediaFileId());
		return view;
	}

	public static List<UserSubmissionMediaView> toView(List<UserSubmissionMedia> userSubmissionMedia)
	{
		List<UserSubmissionMediaView> views = new ArrayList<>();
		userSubmissionMedia.forEach(media -> {
			views.add(toView(media));
		});

		return views;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getMimeType()
	{
		return mimeType;
	}

	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	public String getSubmissionMediaId()
	{
		return submissionMediaId;
	}

	public void setSubmissionMediaId(String submissionMediaId)
	{
		this.submissionMediaId = submissionMediaId;
	}

	public UserSubmissionMedia getMedia()
	{
		return media;
	}

	public void setMedia(UserSubmissionMedia media)
	{
		this.media = media;
	}

}
