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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.entity.Branding;
import edu.usu.sdl.openstorefront.core.entity.TopicSearchItem;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jlaw
 * @author dshurtleff
 */
public class BrandingView
{

	private Branding branding;
	private List<TopicSearchView> topicSearchViews = new ArrayList<>();

	public BrandingView()
	{
	}

	public static BrandingView toView(Branding branding, List<TopicSearchItem> topicSearchItems)
	{
		BrandingView view = new BrandingView();
		view.setBranding(branding);
		view.setTopicSearchViews(TopicSearchView.toView(topicSearchItems));

		return view;
	}

	public List<TopicSearchView> getTopicSearchViews()
	{
		return topicSearchViews;
	}

	public void setTopicSearchViews(List<TopicSearchView> topicSearchViews)
	{
		this.topicSearchViews = topicSearchViews;
	}

	public Branding getBranding()
	{
		return branding;
	}

	public void setBranding(Branding branding)
	{
		this.branding = branding;
	}

}
